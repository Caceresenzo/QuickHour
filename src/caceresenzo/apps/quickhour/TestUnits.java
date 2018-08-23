package caceresenzo.apps.quickhour;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import caceresenzo.apps.quickhour.codec.implementations.JsonQuickHourFileCodec;
import caceresenzo.apps.quickhour.codec.implementations.JsonUserCodec;
import caceresenzo.apps.quickhour.config.Config;
import caceresenzo.apps.quickhour.config.Language;
import caceresenzo.apps.quickhour.handler.WorkHandler;
import caceresenzo.apps.quickhour.manager.QuickHourManager;
import caceresenzo.apps.quickhour.models.QuickHourDay;
import caceresenzo.apps.quickhour.models.QuickHourFile;
import caceresenzo.apps.quickhour.models.QuickHourReference;
import caceresenzo.apps.quickhour.models.QuickHourUser;
import caceresenzo.apps.quickhour.utils.Utils;
import caceresenzo.libs.codec.chartable.SeparatorCharTable;
import caceresenzo.libs.logger.Logger;
import caceresenzo.libs.string.SimpleLineStringBuilder;
import caceresenzo.libs.string.StringUtils;

public class TestUnits {
	
	public static class UserJsonTestUnits {
		
		public static void main(String[] args) throws Exception {
			List<QuickHourUser> users = new JsonUserCodec().read(new File("config/users.json"));
			
			for (QuickHourUser user : users) {
				Logger.info("user: %s, with display: %s", user.getName(), user.getDisplay());
			}
			
			new JsonUserCodec().write(new File("test/config/users.json"), users);
		}
		
	}
	
	public static class QuickHourFileJsonTestUnits {
		
		public static void main(String[] args) throws Exception {
			// Need users database to be configured
			new JsonUserCodec().read(new File("config/users.json"));
			
			QuickHourFile quickHourFile = new JsonQuickHourFileCodec().read(new File("myhour/WEEK 30.qhr"));
			
			Logger.info("File Version: " + quickHourFile.getFileVersion().get());
			Logger.info("File Name: " + quickHourFile.getFileName());
			Logger.info("File Name: " + quickHourFile.getFileDescription());
			Logger.info("Hour size: " + quickHourFile.getUsersHours().size());
			
			for (QuickHourUser user : quickHourFile.getUsersHours()) {
				Logger.info("\t| user: " + user.getName());
				
				for (QuickHourDay day : user.getDays()) {
					Logger.info("\t| \t| day: " + day.getDayName());
					
					for (QuickHourReference reference : day.getReferences()) {
						Logger.info("\t| \t| \t| ref: " + reference.getReference() + " [time: " + reference.getHourCount() + "]");
					}
				}
			}
			
			new JsonQuickHourFileCodec().write(new File("test/myhour/WEEK 30.qhr"), quickHourFile);
		}
		
	}
	
	public static class DialogTestUnits {
		
		public static void main(String[] args) throws Exception {
			Language.getLanguage().initialize();
			Utils.showErrorDialog("error.codec.quickhourfile.unresolved-name", "enzo.caceres");
		}
		
	}
	
	public static class LoopTest {
		
		public static void main(String[] args) throws Exception {
			Logger.info(Config.getDaysOfTheWeek(true));
		}
		
	}
	
	public static class ExportTest {
		
		public static void main(String[] args) throws Exception {
			Logger.setStaticLength(20);
			Language.getLanguage().initialize();
			Config.prepareConfig();
			
			QuickHourManager.getQuickHourManager().initialize();
			
			new JsonUserCodec().read(new File("config/users.json"));
			
			WorkHandler.setActualQuickHourFile(new JsonQuickHourFileCodec().read(new File("./myhour/debug.qhr")));
			WorkHandler.exportFile(new File("./export/debug.xlsx"));
		}
		
	}
	
	public static class OrganizationReadingTest implements SeparatorCharTable {
		
		public static void main(String[] args) throws Exception {
			for (String string : Utils.readExcel(new File("./config/organization.xlsx"))) {
				System.out.println(string);
				// for (String string : getData(new File("./config/organization.xlsx"))) {
				// for (String splitedString : string.split(",")) {
				//
				// System.out.println(splitedString);
				// }
			}
		}
		
	}
	
	public static class FileDumperTest implements SeparatorCharTable {
		
		public static void main(String[] args) throws Exception {
			dump(new File("."), true);
		}
		
		public static void dump(File file, boolean reSplit) throws Exception {
			File outputFolder = new File(file.getAbsoluteFile().getParentFile(), "/" + file.getName() + "--output/").getAbsoluteFile();
			outputFolder.mkdirs();
			
			int index = 0;
			for (List<String> strings : getData(file)) {
				Logger.info("Processing sheet: " + index);
				
				SimpleLineStringBuilder builder = new SimpleLineStringBuilder();
				
				for (String string : strings) {
					if (reSplit) {
						for (String substring : string.split(",")) {
							if (substring == "-") {
								continue;
							}
							
							builder.appendln(substring);
						}
					} else {
						builder.appendln(string);
					}
				}
				
				StringUtils.stringToFile(new File(outputFolder, "SHEET_" + index + ".txt"), builder.toString());
				
				index++;
			}
		}
		
		public static List<List<String>> getData(File file) throws Exception {
			FileInputStream excelFile = new FileInputStream(file);
			Workbook workbook = new XSSFWorkbook(excelFile);
			
			List<List<String>> everyContent = new ArrayList<>();
			
			for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
				Sheet datatypeSheet = workbook.getSheetAt(i);
				Iterator<Row> iterator = datatypeSheet.iterator();
				
				List<String> data = new ArrayList<>();
				data.add(workbook.getNameAt(i).getSheetName());
				data.add("\n");
				
				while (iterator.hasNext()) {
					Row currentRow = iterator.next();
					
					Iterator<Cell> cellIterator = currentRow.iterator();
					
					String cellLine = "";
					while (cellIterator.hasNext()) {
						Cell currentCell = cellIterator.next();
						
						String cellData = DATA_FILL;
						
						switch (currentCell.getCellTypeEnum()) { // getCellTypeEnum shown as deprecated for version 3.15, ill be renamed to getCellType starting from version 4.0
							case STRING: {
								cellData = currentCell.getStringCellValue();
								break;
							}
							case NUMERIC: {
								cellData = String.valueOf((int) currentCell.getNumericCellValue());
								break;
							}
							
							default:
								break;
						}
						
						if (cellData != null) {
							cellLine += cellData + (cellIterator.hasNext() ? DATA_SEPARATOR : "");
						}
					}
					
					data.add(cellLine);
				}
				
				everyContent.add(data);
			}
			
			workbook.close();
			
			return everyContent;
		}
	}
	
}