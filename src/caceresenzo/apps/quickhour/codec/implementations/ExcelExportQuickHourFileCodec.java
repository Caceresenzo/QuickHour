package caceresenzo.apps.quickhour.codec.implementations;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import caceresenzo.apps.quickhour.codec.QuickHourFileCodec;
import caceresenzo.apps.quickhour.config.Config;
import caceresenzo.apps.quickhour.manager.QuickHourManager;
import caceresenzo.apps.quickhour.models.QuickHourDay;
import caceresenzo.apps.quickhour.models.QuickHourFile;
import caceresenzo.apps.quickhour.models.QuickHourReference;
import caceresenzo.apps.quickhour.models.QuickHourUser;
import caceresenzo.libs.internationalization.i18n;

public class ExcelExportQuickHourFileCodec extends QuickHourFileCodec {
	
	@Override
	public void write(File file, QuickHourFile quickHourFile) throws Exception {
		if (quickHourFile == null) {
			return;
		}
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet(Config.APPLICATION_NAME);
		
		int rowNum = 0;
		System.out.println("Creating excel");
		
		for (List<Object> datatype : createDataTable(quickHourFile)) {
			Row row = sheet.createRow(rowNum++);
			
			int colNum = 0;
			for (Object field : datatype) {
				sheet.autoSizeColumn(colNum);
				Cell cell = row.createCell(colNum++);
				
				if (field instanceof String) {
					cell.setCellValue((String) field);
				} else if (field instanceof Integer) {
					cell.setCellValue((Integer) field);
				} else if (field instanceof Float) {
					cell.setCellValue((Float) field);
				} else {
					cell.setCellValue("DEF:" + String.valueOf(field));
				}
			}
		}
		
		try {
			FileOutputStream outputStream = new FileOutputStream(file);
			workbook.write(outputStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			workbook.close();
		}
		
		System.out.println("Done");
	}
	
	private List<List<Object>> createDataTable(QuickHourFile quickHourFile) {
		List<List<Object>> output = new ArrayList<List<Object>>();
		
		List<Object> skip = new ArrayList<Object>();
		
		/*
		 * Header
		 */
		List<Object> header = new ArrayList<Object>();
		List<Object> subheader = new ArrayList<Object>();
		
		header.add(i18n.getString("export.column.user"));
		header.add("");
		header.add(i18n.getString("export.column.day"));
		header.add(i18n.getString("export.column.total"));
		header.add(i18n.getString("export.column.total-week"));
		header.add("");
		header.add("");
		
		for (int i = 0; i < header.size() - 1; i++) {
			subheader.add("");
		}
		subheader.add(i18n.getString("export.column.total-reference"));
		
		Map<String, Float> referencesMap = extractAllStringReferenceAndTime(quickHourFile);
		
		for (Entry<String, Float> entry : referencesMap.entrySet()) {
			header.add(entry.getKey());
			subheader.add(entry.getValue());
		}
		
		output.add(header);
		output.add(subheader);
		
		output.add(skip);
		output.add(skip);
		
		/*
		 * Content
		 */
		List<String> daysOfTheWeek = Config.getDaysOfTheWeek(true);
		
		for (QuickHourUser user : quickHourFile.getUsersHours()) {
			boolean firstLoop = true;
			
			for (String dayOfTheWeek : daysOfTheWeek) {
				List<Object> line = new ArrayList<Object>();
				
				line.add(firstLoop ? user.getName() : "");
				line.add("");
				
				if (firstLoop) {
					firstLoop = false;
				}
				
				line.add(i18n.getString("date.day." + dayOfTheWeek).toUpperCase());
				
				if (user.getDays() != null && !user.getDays().isEmpty()) {
					for (QuickHourDay day : user.getDays()) {
						if (day.getDayName().equalsIgnoreCase("day0")) {
							continue;
						}
						
						if (day.getDayName().equalsIgnoreCase(dayOfTheWeek)) {
							line.add(day.countHour());
							
							line.add("");
							line.add("");
							line.add("");
							
							if (day.getReferences() != null && !day.getReferences().isEmpty()) {
								for (String referenceString : referencesMap.keySet()) {
									boolean found = false;
									
									for (QuickHourReference reference : day.getReferences()) {
										
										if (found = referenceString.equals(reference.getReference())) {
											line.add(reference.getHourCount());
											break;
										}
									}
									
									if (!found) {
										line.add("");
									}
								}
							}
							
							break;
						}
					}
				}
				
				if (dayOfTheWeek.equalsIgnoreCase("day0")) {
					line.add("");
					line.add(QuickHourManager.getQuickHourManager().countHour(user));
				}
				
				output.add(line);
			}
			output.add(skip);
		}
		
		return output;
	}
	
	private Map<String, Float> extractAllStringReferenceAndTime(QuickHourFile quickHourFile) {
		Map<String, Float> referencesMap = new TreeMap<String, Float>();
		
		for (QuickHourUser user : quickHourFile.getUsersHours()) {
			if (user.getDays() == null || user.getDays().isEmpty()) {
				continue;
			}
			
			for (QuickHourDay day : user.getDays()) {
				if (day.getReferences() == null || day.getReferences().isEmpty()) {
					continue;
				}
				
				for (QuickHourReference reference : day.getReferences()) {
					float mapTime = referencesMap.getOrDefault(reference.getReference(), 0.0F);
					
					mapTime += reference.getHourCount();
					
					referencesMap.put(reference.getReference(), mapTime);
				}
			}
		}
		
		return referencesMap;
	}
	
}