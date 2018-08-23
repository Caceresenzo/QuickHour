package caceresenzo.apps.quickhour.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import caceresenzo.apps.quickhour.codec.implementations.JsonReferenceFormatCodec;
import caceresenzo.apps.quickhour.config.Config;
import caceresenzo.apps.quickhour.handler.ApplicationHandler;
import caceresenzo.apps.quickhour.models.QuickHourDay;
import caceresenzo.apps.quickhour.models.QuickHourReference;
import caceresenzo.apps.quickhour.models.QuickHourUser;
import caceresenzo.apps.quickhour.models.ReferenceFormat;
import caceresenzo.apps.quickhour.models.SortTemplateReference;
import caceresenzo.apps.quickhour.utils.Utils;
import caceresenzo.libs.internationalization.i18n;
import caceresenzo.libs.logger.Logger;

public class QuickHourManager {
	
	private static QuickHourManager MANAGER;
	
	private List<QuickHourUser> users;
	private List<ReferenceFormat> referencesFormats;
	private List<SortTemplateReference> knownReferences;
	
	public QuickHourManager() {
		this.users = new ArrayList<>();
		this.referencesFormats = new ArrayList<>();
	}
	
	public QuickHourManager initialize() throws Exception {
		referencesFormats.addAll(new JsonReferenceFormatCodec().read(new File(Config.REFERENCES_FORMATS_PATH)));
		
		ApplicationHandler.initalize();
		
		Logger.info("Reading reference template...");
		knownReferences = Config.REFERENCE_SORT_CODEC.read(new File(Config.REFERENCES_SORT_TEMPLATE_PATH));
		if (knownReferences == null) {
			Utils.showErrorDialog("export.manager.organization-template.file-not-found");
			knownReferences = new ArrayList<>();
		} else {
			referencesFormats.addAll(knownReferences);
		}
		Logger.info("Done.");
		
		return this;
	}
	
	public boolean isUserExisting(QuickHourUser user) {
		return users.contains(user);
	}
	
	public void addUser(QuickHourUser user) {
		Logger.info("New used loaded: " + user.getName());
		users.add(user);
	}
	
	public QuickHourUser getUser(String userName) {
		for (QuickHourUser user : users) {
			if (user.getName().equalsIgnoreCase(userName)) {
				return user;
			}
		}
		
		return null;
	}
	
	public float countHour(QuickHourUser user) {
		float totalHour = 0.0F;
		
		if (user.getDays() == null || user.getDays().isEmpty()) {
			return 0.0F;
		}
		
		for (QuickHourDay day : user.getDays()) {
			for (QuickHourReference reference : day.getReferences()) {
				totalHour += reference.getHourCount();
			}
		}
		
		return totalHour;
	}
	
	public void emptyHoursOfEveryone() {
		for (QuickHourUser user : users) {
			user.applyDays(new ArrayList<QuickHourDay>());
		}
	}
	
	public QuickHourManager sort() {
		users.sort(QuickHourUser.COMPARATOR);
		return this;
	}
	
	public List<QuickHourUser> getUsers() {
		return users;
	}
	
	public List<ReferenceFormat> getReferencesFormats() {
		if (referencesFormats.isEmpty()) {
			referencesFormats.add(new ReferenceFormat("default", i18n.getString("manager.quickhour.references-formats.default.display"), "%s"));
		}
		
		return referencesFormats;
	}
	
	public List<SortTemplateReference> getKnownReferences() {
		return knownReferences;
	}
	
	public static QuickHourManager getQuickHourManager() {
		if (MANAGER == null) {
			MANAGER = new QuickHourManager();
		}
		
		return MANAGER;
	}
	
}