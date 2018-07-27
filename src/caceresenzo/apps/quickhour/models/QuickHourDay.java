package caceresenzo.apps.quickhour.models;

import java.util.Comparator;
import java.util.List;

import caceresenzo.libs.internationalization.i18n;

public class QuickHourDay {
	
	public static final Comparator<QuickHourDay> COMPARATOR = new Comparator<QuickHourDay>() {
		@Override
		public int compare(QuickHourDay quickHourDay1, QuickHourDay quickHourDay2) {
			return quickHourDay1.getDayName().compareToIgnoreCase(quickHourDay2.getDayName());
		}
	};
	
	private String dayName;
	private List<QuickHourReference> references;
	
	public QuickHourDay(String dayName, List<QuickHourReference> references) {
		this.dayName = dayName;
		this.references = references;
	}
	
	public String getDayName() {
		return dayName;
	}
	
	public String getI18nDayName() {
		return i18n.getString("date.day." + dayName);
	}
	
	public List<QuickHourReference> getReferences() {
		return references;
	}
	
	public void applyReferences(List<QuickHourReference> references) {
		this.references = references;
	}
	
}