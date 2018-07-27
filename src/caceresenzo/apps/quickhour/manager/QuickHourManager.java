package caceresenzo.apps.quickhour.manager;

import java.util.ArrayList;
import java.util.List;

import caceresenzo.apps.quickhour.models.QuickHourDay;
import caceresenzo.apps.quickhour.models.QuickHourReference;
import caceresenzo.apps.quickhour.models.QuickHourUser;

public class QuickHourManager {
	
	private static QuickHourManager MANAGER;
	
	private List<QuickHourUser> users;
	
	public QuickHourManager() {
		this.users = new ArrayList<QuickHourUser>();
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
	
	public static QuickHourManager getQuickHourManager() {
		if (MANAGER == null) {
			MANAGER = new QuickHourManager();
		}
		
		return MANAGER;
	}
	
}