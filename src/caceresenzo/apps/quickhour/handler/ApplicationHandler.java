package caceresenzo.apps.quickhour.handler;

import java.io.File;
import java.util.Map;

import caceresenzo.apps.quickhour.codec.SortReferenceTemplateCodec;
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
		
		private static final String JSON_ORGANIZER = "organizer";
		private static final String JSON_ORGANIZER_CODEC_CLASS = "codec_class";
		private static final String JSON_ORGANIZER_PAGE = "sheet_page";
		private static final String JSON_ORGANIZER_FILE = "file";
		
		@SuppressWarnings("unchecked")
		@Override
		public Void read(File file) throws Exception {
			String data = FileChecker.checkFile(file, null, true, false);
			
			JsonObject json = (JsonObject) new JsonParser().parse(data);
			
			/* Settings */
			Map<String, Object> settingsMap = (Map<String, Object>) json.get(JSON_SETTINGS);
			
			if (settingsMap != null) {
				if (settingsMap.containsKey(JSON_SETTINGS_LAST_EXPORT_PATH)) {
					Config.LAST_FOLDER_EXPORT_OPEN = (String) settingsMap.get(JSON_SETTINGS_LAST_EXPORT_PATH);
					
					printLoaded("SETTINGS_LAST_EXPORT_PATH", Config.LAST_FOLDER_EXPORT_OPEN);
				}
				
				if (settingsMap.containsKey(JSON_SETTINGS_LAST_OPEN_PATH)) {
					Config.LAST_FOLDER_OPEN = (String) settingsMap.get(JSON_SETTINGS_LAST_OPEN_PATH);
					
					printLoaded("SETTINGS_LAST_OPEN_PATH", Config.LAST_FOLDER_OPEN);
				}
			}
			
			/* Extentions */
			Map<String, Object> extensionsMap = (Map<String, Object>) json.get(JSON_EXTENSIONS);
			
			if (extensionsMap != null) {
				if (extensionsMap.containsKey(JSON_EXTENSIONS_EXCEL)) {
					Config.EXCEL_FILE_EXTENSION = (String) extensionsMap.get(JSON_EXTENSIONS_EXCEL);
					
					printLoaded("EXTENSIONS_EXCEL", Config.EXCEL_FILE_EXTENSION);
				}
				
				if (extensionsMap.containsKey(JSON_EXTENSIONS_QUICKHOUR)) {
					Config.FILE_EXTENSION = (String) extensionsMap.get(JSON_EXTENSIONS_QUICKHOUR);
					
					printLoaded("EXTENSIONS_QUICKHOUR", Config.FILE_EXTENSION);
				}
			}
			
			/* QuickHour */
			Map<String, Object> quickhourMap = (Map<String, Object>) json.get(JSON_QUICKHOUR);
			
			if (quickhourMap != null) {
				if (quickhourMap.containsKey(JSON_QUICKHOUR_HOURGOAL)) {
					Config.TARGET_HOUR_COUNT = ParseUtils.parseFloat(quickhourMap.get(JSON_QUICKHOUR_HOURGOAL), Config.TARGET_HOUR_COUNT);
					
					printLoaded("QUICKHOUR_HOURGOAL", Config.TARGET_HOUR_COUNT);
				}
			}
			
			/* Organizer */
			Map<String, Object> organizerMap = (Map<String, Object>) json.get(JSON_ORGANIZER);
			
			if (organizerMap != null) {
				if (organizerMap.containsKey(JSON_ORGANIZER_CODEC_CLASS)) {
					try {
						Config.REFERENCE_SORT_CODEC = (SortReferenceTemplateCodec) Class.forName("caceresenzo.apps.quickhour.codec.implementations." + organizerMap.get(JSON_ORGANIZER_CODEC_CLASS)).newInstance();
					} catch (Exception exception) {
						Logger.exception(exception, "Class %s can't be loaded.", organizerMap.get(JSON_ORGANIZER_CODEC_CLASS));
					}
					
					printLoaded("ORGANIZER_CODEC_CLASS", Config.REFERENCE_SORT_CODEC.getClass().getSimpleName());
				}
				
				if (organizerMap.containsKey(JSON_ORGANIZER_PAGE)) {
					Config.REFERENCE_SORT_CODEC_SHEET_PAGE = ParseUtils.parseInt(organizerMap.get(JSON_ORGANIZER_PAGE), Config.NO_VALUE);
					
					printLoaded("ORGANIZER_PAGE", Config.REFERENCE_SORT_CODEC_SHEET_PAGE);
				}
				
				if (organizerMap.containsKey(JSON_ORGANIZER_FILE)) {
					Config.REFERENCES_SORT_TEMPLATE_PATH = (String) organizerMap.get(JSON_ORGANIZER_FILE);
					
					printLoaded("ORGANIZER_FILE", Config.REFERENCES_SORT_TEMPLATE_PATH);
				}
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
		 * },
		 * 
		 * "organizer": {
		 * 
		 * "codec_class": "ExcelReferenceSortTemplateCodec",
		 * 
		 * "sheet_page": -1,
		 * 
		 * "file": "config/organization.xlsx"
		 * 
		 * }
		 * 
		 */
		@Override
		public void write(File file, Void v) throws Exception {
			SimpleLineStringBuilder builder = new SimpleLineStringBuilder();
			
			builder.appendln("{");
			
			builder.appendln(TAB + "\"" + JSON_SETTINGS + "\": {");
			builder.appendln(TAB + TAB + "\"" + JSON_SETTINGS_LAST_EXPORT_PATH + "\": \"" + formatPath(Config.LAST_FOLDER_EXPORT_OPEN) + "\",");
			builder.appendln(TAB + TAB + "\"" + JSON_SETTINGS_LAST_OPEN_PATH + "\": \"" + formatPath(Config.LAST_FOLDER_OPEN) + "\"");
			builder.appendln(TAB + "},");
			
			builder.appendln(TAB + "\"" + JSON_EXTENSIONS + "\": {");
			builder.appendln(TAB + TAB + "\"" + JSON_EXTENSIONS_EXCEL + "\": \"" + Config.EXCEL_FILE_EXTENSION + "\",");
			builder.appendln(TAB + TAB + "\"" + JSON_EXTENSIONS_QUICKHOUR + "\": \"" + Config.FILE_EXTENSION + "\"");
			builder.appendln(TAB + "},");
			
			builder.appendln(TAB + "\"" + JSON_QUICKHOUR + "\": {");
			builder.appendln(TAB + TAB + "\"" + JSON_QUICKHOUR_HOURGOAL + "\": " + Config.TARGET_HOUR_COUNT + "");
			builder.appendln(TAB + "},");
			
			builder.appendln(TAB + "\"" + JSON_ORGANIZER + "\": {");
			builder.appendln(TAB + TAB + "\"" + JSON_ORGANIZER_CODEC_CLASS + "\": \"" + Config.REFERENCE_SORT_CODEC.getClass().getSimpleName() + "\",");
			builder.appendln(TAB + TAB + "\"" + JSON_ORGANIZER_PAGE + "\": " + Config.REFERENCE_SORT_CODEC_SHEET_PAGE + ",");
			builder.appendln(TAB + TAB + "\"" + JSON_ORGANIZER_FILE + "\": \"" + Config.REFERENCES_SORT_TEMPLATE_PATH + "\"");
			builder.appendln(TAB + "}");
			
			builder.appendln("}");
			
			StringUtils.stringToFile(file, builder.toString());
		}
		
		private String formatPath(String basePath) {
			return (basePath + (basePath.endsWith("\\") || basePath.endsWith("/") ? "" : "/")).replace("\\", "/");
		}
		
	}
	
}