package caceresenzo.apps.quickhour.config;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import caceresenzo.libs.internationalization.i18n;

public class Config {
	
	public static final String APPLICATION_NAME = "QuickHour";
	
	public static final String STARTING_DAY = "day1";
	public static final Map<String, String> POSSIBLE_DAYS = new LinkedHashMap<String, String>();
	
	public static final String CONFIG_PATH = "config/application.json";
	public static final String USERS_PATH = "config/users.json";
	public static final String REFERENCES_FORMATS_PATH = "config/references-formats.json";
	public static final String REFERENCES_SORT_TEMPLATE_PATH = "config/organization.xlsx";
	
	public static float TARGET_HOUR_COUNT = 38.0F;
	public static String LAST_FOLDER_OPEN = "./myhour/";
	public static String LAST_FOLDER_EXPORT_OPEN = "./export/";
	public static String FILE_EXTENSION = "qhr";
	public static String EXCEL_FILE_EXTENSION = "xlsx";
	
	public static void prepareConfig() {
		for (int i = 0; i < 7; i++) {
			POSSIBLE_DAYS.put("day" + i, i18n.getString("date.day.day" + i));
		}
	}
	
	public static String getDayByIndex(int index) {
		if (index > POSSIBLE_DAYS.size()) {
			return null;
		}
		
		int actualIndex = 0;
		for (Object key : POSSIBLE_DAYS.keySet()) {
			if (index == actualIndex++) {
				return String.valueOf(key);
			}
		}
		
		return null;
	}
	
	public static int getIndexByDay(String day) {
		if (day == null) {
			return 0;
		}
		
		int actualIndex = 0;
		for (Object key : POSSIBLE_DAYS.keySet()) {
			if (String.valueOf(key).equalsIgnoreCase(day)) {
				return actualIndex;
			}
			actualIndex++;
		}
		
		return 0;
	}
	
	public static List<String> getDaysOfTheWeek(boolean sundayAtTheEnd) {
		List<String> days = new ArrayList<>();
		
		for (int i = sundayAtTheEnd ? 1 : 0; i <= 6; i++) {
			days.add("day" + i);
		}
		
		if (sundayAtTheEnd) {
			days.add("day0");
		}
		
		return days;
	}
	
}