package caceresenzo.apps.quickhour.codec.implementations;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import caceresenzo.apps.quickhour.codec.UserCodec;
import caceresenzo.apps.quickhour.models.QuickHourUser;
import caceresenzo.libs.filesystem.FileChecker;
import caceresenzo.libs.filesystem.FileUtils;
import caceresenzo.libs.json.JsonObject;
import caceresenzo.libs.json.parser.JsonParser;
import caceresenzo.libs.parse.ParseUtils;
import caceresenzo.libs.string.SimpleLineStringBuilder;
import caceresenzo.libs.string.StringUtils;

public class JsonUserCodec extends UserCodec {
	
	private static final String JSON_KEY_USERS = "users";
	private static final String JSON_KEY_USERS_DISPLAY = "display";
	private static final String JSON_KEY_USERS_DISABLED = "disabled";
	
	@SuppressWarnings("unchecked")
	@Override
	public List<QuickHourUser> read(File file) throws Exception {
		List<QuickHourUser> users = new ArrayList<QuickHourUser>();
		
		if (!FileChecker.checkFile(file, true, true)) {
			write(file, null);
			return users;
		}
		
		JsonObject json = (JsonObject) new JsonParser().parse(StringUtils.fromFile(file));
		
		HashMap<String, Object> usersMap = (HashMap<String, Object>) json.get(JSON_KEY_USERS);
		for (Entry<String, Object> usersMapEntry : usersMap.entrySet()) {
			HashMap<String, Object> userDataMap = (HashMap<String, Object>) usersMapEntry.getValue();
			
			String name = usersMapEntry.getKey();
			String display = ParseUtils.parseString(userDataMap.get(JSON_KEY_USERS_DISPLAY), name);
			boolean disabled = ParseUtils.parseBoolean(userDataMap.get(JSON_KEY_USERS_DISABLED), false);
			
			users.add(new QuickHourUser(name).withDisplay(display).isDisabled(disabled).addToMainManager());
		}
		
		return users;
	}
	
	@Override
	public void write(File file, List<QuickHourUser> users) throws Exception {
		if (users == null || users.isEmpty()) {
			FileUtils.writeStringToFile("{\"users\":{}}", file);
			return;
		}
		
		SimpleLineStringBuilder builder = new SimpleLineStringBuilder();
		
		builder.appendln("{");
		builder.appendln(TAB + "\"" + JSON_KEY_USERS + "\": {");
		
		Iterator<QuickHourUser> iterator = users.iterator();
		while (iterator.hasNext()) {
			QuickHourUser user = iterator.next();
			
			builder.appendln(TAB + TAB + "\"" + user.getName() + "\": {");
			builder.appendln(TAB + TAB + TAB + "\"" + JSON_KEY_USERS_DISPLAY + "\": \"" + user.getDisplay() + "\"" + (user.isDisabled() ? "," : ""));
			
			if (user.isDisabled()) {
				builder.appendln(TAB + TAB + TAB + "\"" + JSON_KEY_USERS_DISABLED + "\": true");
			}
			
			builder.appendln(TAB + TAB + "}" + (iterator.hasNext() ? "," : ""));
		}
		
		builder.appendln(TAB + "}");
		builder.appendln("}");
		
		FileUtils.writeStringToFile(builder.toString(), file);
	}
	
}