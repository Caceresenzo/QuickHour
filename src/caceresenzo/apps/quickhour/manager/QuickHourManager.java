package caceresenzo.apps.quickhour.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import caceresenzo.apps.quickhour.codec.implementations.JsonReferenceFormatCodec;
import caceresenzo.apps.quickhour.config.Config;
import caceresenzo.apps.quickhour.models.QuickHourDay;
import caceresenzo.apps.quickhour.models.QuickHourReference;
import caceresenzo.apps.quickhour.models.QuickHourUser;
import caceresenzo.apps.quickhour.models.ReferenceFormat;
import caceresenzo.libs.internationalization.i18n;

public class QuickHourManager {
	
	private static QuickHourManager MANAGER;
	
	private List<QuickHourUser> users;
	private List<ReferenceFormat> referencesFormats;
	
	public QuickHourManager() {
		this.users = new ArrayList<QuickHourUser>();
		this.referencesFormats = new ArrayList<ReferenceFormat>();
	}
	
	public QuickHourManager initialize() throws Exception {
		referencesFormats.addAll(new JsonReferenceFormatCodec().read(new File(Config.REFERENCES_FORMATS_PATH)));
		return this;
	}
	
	public boolean isUserExisting(QuickHourUser user) {
		return users.contains(user);
	}
	
	public void addUser(QuickHourUser user) {
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
	
	public static QuickHourManager getQuickHourManager() {
		if (MANAGER == null) {
			MANAGER = new QuickHourManager();
		}
		
		return MANAGER;
	}
	
}