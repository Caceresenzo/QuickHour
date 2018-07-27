package caceresenzo.apps.quickhour.models;

import java.util.Comparator;
import java.util.List;

import caceresenzo.apps.quickhour.manager.QuickHourManager;

public class QuickHourUser {
	
	public static final Comparator<QuickHourUser> COMPARATOR = new Comparator<QuickHourUser>() {
		@Override
		public int compare(QuickHourUser quickHourUser1, QuickHourUser quickHourUser2) {
			return quickHourUser1.getName().compareToIgnoreCase(quickHourUser2.getName());
		}
	};
	
	private String name, display;
	private boolean disabled = false;
	private List<QuickHourDay> days;
	
	public QuickHourUser(String name) {
		this.name = name;
	}
	
	public QuickHourUser addToMainManager() {
		QuickHourManager.getQuickHourManager().addUser(this);
		return this;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDisplay() {
		return display;
	}
	
	public QuickHourUser withDisplay(String display) {
		this.display = display;
		return this;
	}
	
	public boolean isDisabled() {
		return disabled;
	}
	
	public QuickHourUser isDisabled(boolean disabled) {
		this.disabled = disabled;
		return this;
	}
	
	public List<QuickHourDay> getDays() {
		return days;
	}
	
	public QuickHourUser applyDays(List<QuickHourDay> days) {
		this.days = days;
		return this;
	}
	
	@Override
	public String toString() {
		return "User[name=\"" + name + "\"]";
	}
	
}