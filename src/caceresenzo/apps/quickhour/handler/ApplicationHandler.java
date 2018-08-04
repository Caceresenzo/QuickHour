package caceresenzo.apps.quickhour.handler;

import java.io.File;
import java.util.HashMap;

import caceresenzo.apps.quickhour.config.Config;
import caceresenzo.libs.codec.ReadWriteCodec;
import caceresenzo.libs.codec.chartable.JsonCharTable;
import caceresenzo.libs.filesystem.FileChecker;
import caceresenzo.libs.json.JsonObject;
import caceresenzo.libs.json.parser.JsonParser;
import caceresenzo.libs.logger.Logger;
import caceresenzo.libs.parse.ParseUtils;
import caceresenzo.libs.string.SimpleLineStringBuilder;
import caceresenzo.libs.string.StringUtils;

public class ApplicationHandler {
	
	private static ApplicationConfigCodec codec = new ApplicationConfigCodec();
	private static File applicationFile = new File(Config.CONFIG_PATH);
	
	public static void initalize() throws Exception {
		codec.read(applicationFile);
	}
	
	public static void saveConfig() throws Exception {
		codec.write(applicationFile, null);
	}
	
	private static class ApplicationConfigCodec extends ReadWriteCodec<Void> implements JsonCharTable {
		
		private static final String JSON_SETTINGS = "settings";
		private static final String JSON_SETTINGS_LAST_EXPORT_PATH = "last_export_path";
		private static final String JSON_SETTINGS_LAST_OPEN_PATH = "last_open_path";
		
		private static final String JSON_EXTENSIONS = "extensions";
		private static final String JSON_EXTENSIONS_EXCEL = "excel";
		private static final String JSON_EXTENSIONS_QUICKHOUR = "quickhour";
		
		private static final String JSON_QUICKHOUR = "quickhour";
		private static final String JSON_QUICKHOUR_HOURGOAL = "hour_goal";
		
		@SuppressWarnings("unchecked")
		@Override
		public Void read(File file) throws Exception {
			String data = FileChecker.checkFile(file, null, true, false);
			
			JsonObject json = (JsonObject) new JsonParser().parse(data);
			
			HashMap<String, Object> settingsMap = (HashMap<String, Object>) json.getOrDefault(JSON_SETTINGS, new HashMap<>());
			
			if (settingsMap.containsKey(JSON_SETTINGS_LAST_EXPORT_PATH)) {
				Config.LAST_FOLDER_OPEN = (String) settingsMap.get(JSON_SETTINGS_LAST_EXPORT_PATH);
				printLoaded("SETTINGS_LAST_EXPORT_PATH", Config.LAST_FOLDER_OPEN);
			}
			if (settingsMap.containsKey(JSON_SETTINGS_LAST_OPEN_PATH)) {
				Config.LAST_FOLDER_EXPORT_OPEN = (String) settingsMap.get(JSON_SETTINGS_LAST_OPEN_PATH);
				printLoaded("SETTINGS_LAST_OPEN_PATH", Config.LAST_FOLDER_EXPORT_OPEN);
			}
			
			HashMap<String, Object> extensionsMap = (HashMap<String, Object>) json.getOrDefault(JSON_EXTENSIONS, new HashMap<>());
			
			if (extensionsMap.containsKey(JSON_EXTENSIONS_EXCEL)) {
				Config.EXCEL_FILE_EXTENSION = (String) extensionsMap.get(JSON_EXTENSIONS_EXCEL);
				printLoaded("EXTENSIONS_EXCEL", Config.EXCEL_FILE_EXTENSION);
				
			}
			if (extensionsMap.containsKey(JSON_EXTENSIONS_QUICKHOUR)) {
				Config.FILE_EXTENSION = (String) extensionsMap.get(JSON_EXTENSIONS_QUICKHOUR);
				printLoaded("EXTENSIONS_QUICKHOUR", Config.FILE_EXTENSION);
			}
			
			HashMap<String, Object> quickhourMap = (HashMap<String, Object>) json.getOrDefault(JSON_QUICKHOUR, new HashMap<>());
			
			if (quickhourMap.containsKey(JSON_QUICKHOUR_HOURGOAL)) {
				Config.TARGET_HOUR_COUNT = ParseUtils.parseFloat(quickhourMap.get(JSON_QUICKHOUR_HOURGOAL), Config.TARGET_HOUR_COUNT);
				printLoaded("QUICKHOUR_HOURGOAL", Config.TARGET_HOUR_COUNT);
			}
			
			return null;
		}
		
		private void printLoaded(String key, Object value) {
			Logger.info("Loading application property: \"%s\" with value \"%s\"", key, value);
		}
		
		/*
		 * "settings": {
		 * 
		 * "last_export_path": "",
		 * 
		 * "last_open_path": ""
		 * 
		 * },
		 * 
		 * 
		 * "extensions": {
		 * 
		 * "excel": "xlsx",
		 * 
		 * "quickhour": "qhr"
		 * 
		 * },
		 * 
		 * "quickhour": {
		 * 
		 * "hour_goal": 38.0
		 * 
		 * }
		 * 
		 */
		@Override
		public void write(File file, Void v) throws Exception {
			SimpleLineStringBuilder builder = new SimpleLineStringBuilder();
			
			builder.appendln("{");
			
			builder.appendln(TAB + "\"" + JSON_SETTINGS + "\": {");
			builder.appendln(TAB + TAB + "\"" + JSON_SETTINGS_LAST_EXPORT_PATH + "\": \"" + (Config.LAST_FOLDER_EXPORT_OPEN + (Config.LAST_FOLDER_EXPORT_OPEN.endsWith("\\") || Config.LAST_FOLDER_EXPORT_OPEN.endsWith("/") ? "" : "/")).replace("\\", "/") + "\",");
			builder.appendln(TAB + TAB + "\"" + JSON_SETTINGS_LAST_OPEN_PATH + "\": \"" + (Config.LAST_FOLDER_OPEN + (Config.LAST_FOLDER_OPEN.endsWith("\\") || Config.LAST_FOLDER_OPEN.endsWith("/") ? "" : "/")).replace("\\", "/") + "\"");
			builder.appendln(TAB + "},");
			
			builder.appendln(TAB + "\"" + JSON_EXTENSIONS + "\": {");
			builder.appendln(TAB + TAB + "\"" + JSON_EXTENSIONS_EXCEL + "\": \"" + Config.EXCEL_FILE_EXTENSION + "\",");
			builder.appendln(TAB + TAB + "\"" + JSON_EXTENSIONS_QUICKHOUR + "\": \"" + Config.FILE_EXTENSION + "\"");
			builder.appendln(TAB + "},");
			
			builder.appendln(TAB + "\"" + JSON_QUICKHOUR + "\": {");
			builder.appendln(TAB + TAB + "\"" + JSON_QUICKHOUR_HOURGOAL + "\": " + Config.TARGET_HOUR_COUNT + "");
			builder.appendln(TAB + "}");
			
			builder.appendln("}");
			
			StringUtils.stringToFile(file, builder.toString());
		}
		
	}
	
}