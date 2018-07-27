package caceresenzo.apps.quickhour.codec.implementations;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import caceresenzo.apps.quickhour.codec.QuickHourFileCodec;
import caceresenzo.apps.quickhour.manager.QuickHourManager;
import caceresenzo.apps.quickhour.models.QuickHourDay;
import caceresenzo.apps.quickhour.models.QuickHourFile;
import caceresenzo.apps.quickhour.models.QuickHourReference;
import caceresenzo.apps.quickhour.models.QuickHourUser;
import caceresenzo.apps.quickhour.utils.Utils;
import caceresenzo.libs.comparator.Version;
import caceresenzo.libs.filesystem.FileChecker;
import caceresenzo.libs.filesystem.FileUtils;
import caceresenzo.libs.json.JsonObject;
import caceresenzo.libs.json.parser.JsonParser;
import caceresenzo.libs.parse.ParseUtils;
import caceresenzo.libs.string.SimpleLineStringBuilder;
import caceresenzo.libs.string.StringUtils;

public class JsonQuickHourFileCodec extends QuickHourFileCodec {
	
	private static final String JSON_KEY_VERSION = "version";
	private static final String JSON_KEY_FILE = "file";
	private static final String JSON_KEY_FILE_NAME = "name";
	private static final String JSON_KEY_FILE_DESCRIPTION = "description";
	private static final String JSON_KEY_HOURS = "hours";
	private static final String JSON_KEY_HOURS_USER_DAY_REFERENCE = "reference";
	private static final String JSON_KEY_HOURS_USER_DAY_TIME = "time";
	
	private QuickHourManager quickHourManager = QuickHourManager.getQuickHourManager();
	
	@SuppressWarnings("unchecked")
	@Override
	public QuickHourFile read(File file) throws Exception {
		QuickHourFile quickHourFile = new QuickHourFile();
		
		if (!FileChecker.checkFile(file, true, true)) {
			return null;
		}
		
		JsonObject json = (JsonObject) new JsonParser().parse(StringUtils.fromFile(file));
		
		/*
		 * Version
		 */
		quickHourFile.setFileVersion(new Version(ParseUtils.parseString(json.get(JSON_KEY_VERSION), "0")));
		
		/*
		 * File
		 */
		HashMap<String, Object> fileMap = (HashMap<String, Object>) json.get(JSON_KEY_FILE);
		quickHourFile.setFileName(ParseUtils.parseString(fileMap.get(JSON_KEY_FILE_NAME), file.getName()));
		quickHourFile.setFileDescription(ParseUtils.parseString(fileMap.get(JSON_KEY_FILE_DESCRIPTION), ""));
		
		/*
		 * Hours
		 */
		List<QuickHourUser> usersHours = new ArrayList<QuickHourUser>();
		HashMap<String, Object> hoursMap = (HashMap<String, Object>) json.get(JSON_KEY_HOURS);
		for (Entry<String, Object> hoursMapEntry : hoursMap.entrySet()) {
			String userName = hoursMapEntry.getKey();
			
			QuickHourUser user = quickHourManager.getUser(userName);
			
			if (user == null) {
				Utils.showErrorDialog("error.codec.quickhourfile.unresolved-name", userName);
				continue;
			}
			usersHours.add(user);
			
			List<QuickHourDay> days = new ArrayList<QuickHourDay>();
			
			HashMap<String, Object> dayMap = (HashMap<String, Object>) hoursMapEntry.getValue();
			for (Entry<String, Object> dayMapEntry : dayMap.entrySet()) {
				String dayName = dayMapEntry.getKey();
				List<QuickHourReference> references = new ArrayList<QuickHourReference>();
				
				days.add(new QuickHourDay(dayName, references));
				
				List<HashMap<String, Object>> referencesMapList = (List<HashMap<String, Object>>) dayMapEntry.getValue();
				for (HashMap<String, Object> referencesMap : referencesMapList) {
					String referenceString = ParseUtils.parseString(referencesMap.get(JSON_KEY_HOURS_USER_DAY_REFERENCE), "???");
					float hourCount = ParseUtils.parseFloat(referencesMap.get(JSON_KEY_HOURS_USER_DAY_TIME), 0.0F);
					
					QuickHourReference reference = new QuickHourReference(referenceString, hourCount);
					// Logger.info("user: %s, day: %s, ref: %s, time: %s", user.getName(), dayName, reference.getReference(), reference.getHourCount());
					references.add(reference);
				}
			}
			
			days.sort(QuickHourDay.COMPARATOR);
			user.applyDays(days);
		}
		
		quickHourFile.setUsersHours(usersHours);
		
		return quickHourFile;
	}
	
	@Override
	public void write(File file, QuickHourFile quickHourFile) throws Exception {
		if (quickHourFile == null) {
			return;
		}
		
		SimpleLineStringBuilder builder = new SimpleLineStringBuilder();
		
		builder.appendln("{");
		
		/*
		 * Version
		 */
		builder.appendln(TAB + "\"" + JSON_KEY_VERSION + "\": \"" + quickHourFile.getFileVersion().get() + "\",");
		
		/*
		 * File
		 */
		builder.appendln(TAB + "\"" + JSON_KEY_FILE + "\": {");
		builder.appendln(TAB + TAB + "\"" + JSON_KEY_FILE_NAME + "\": \"" + quickHourFile.getFileName() + "\",");
		builder.appendln(TAB + TAB + "\"" + JSON_KEY_FILE_DESCRIPTION + "\": \"" + quickHourFile.getFileDescription() + "\"");
		builder.appendln(TAB + "},");
		
		/*
		 * Hours
		 */
		builder.appendln(TAB + "\"" + JSON_KEY_HOURS + "\": {");
		
		Iterator<QuickHourUser> userIterator = quickHourFile.getUsersHours().iterator();
		while (userIterator.hasNext()) {
			QuickHourUser user = userIterator.next();
			
			builder.appendln(TAB + TAB + "\"" + user.getName() + "\": {");
			
			Iterator<QuickHourDay> dayIterator = user.getDays().iterator();
			while (dayIterator.hasNext()) {
				QuickHourDay day = dayIterator.next();
				
				builder.appendln(TAB + TAB + TAB + "\"" + day.getDayName() + "\": [");
				
				Iterator<QuickHourReference> referenceIterator = day.getReferences().iterator();
				while (referenceIterator.hasNext()) {
					QuickHourReference reference = referenceIterator.next();
					
					builder.appendln(TAB + TAB + TAB + TAB + "{");
					builder.appendln(TAB + TAB + TAB + TAB + TAB + "\"" + JSON_KEY_HOURS_USER_DAY_REFERENCE + "\": \"" + reference.getReference() + "\",");
					builder.appendln(TAB + TAB + TAB + TAB + TAB + "\"" + JSON_KEY_HOURS_USER_DAY_TIME + "\": \"" + reference.getHourCount() + "\"");
					builder.appendln(TAB + TAB + TAB + TAB + "}" + (referenceIterator.hasNext() ? "," : ""));
				}
				
				builder.appendln(TAB + TAB + TAB + "]" + (dayIterator.hasNext() ? "," : ""));
			}
			
			builder.appendln(TAB + TAB + "}" + (userIterator.hasNext() ? "," : ""));
		}
		
		builder.appendln(TAB + "}");
		
		builder.appendln("}");
		
		FileUtils.writeStringToFile(builder.toString(), file);
	}
	
}