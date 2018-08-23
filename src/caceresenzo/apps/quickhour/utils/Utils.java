package caceresenzo.apps.quickhour.utils;

import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import caceresenzo.apps.quickhour.config.Config;
import caceresenzo.libs.codec.chartable.SeparatorCharTable;
import caceresenzo.libs.filesystem.FileUtils;
import caceresenzo.libs.internationalization.i18n;
import caceresenzo.libs.logger.Logger;

public class Utils implements SeparatorCharTable {
	
	public static void showErrorDialog(String i18nKey, Object... data) {
		showDialog(JOptionPane.ERROR_MESSAGE, "error.title", i18nKey, data);
	}
	
	public static void showInfoDialog(String i18nKey, Object... data) {
		showDialog(JOptionPane.INFORMATION_MESSAGE, "info.title", i18nKey, data);
	}
	
	public static void showDialog(int type, String i18nTitle, String i18nKey, Object... data) {
		Toolkit.getDefaultToolkit().beep();
		JOptionPane.showMessageDialog(null, data == null || data.length == 0 ? i18n.getString(i18nKey) : i18n.getString(i18nKey, data), i18n.getString(i18nTitle), type);
	}
	
	public static boolean showWarningConfirm(String i18nKey, Object... data) {
		return showConfirm("dialog.confirm.warning.title", i18nKey, data);
	}
	
	public static boolean showConfirm(String i18nTitle, String i18nKey, Object... data) {
		return JOptionPane.showConfirmDialog(null, data == null || data.length == 0 ? i18n.getString(i18nKey) : i18n.getString(i18nKey, data), i18n.getString(i18nTitle), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
	}
	
	public static boolean showSaveConfirm(String i18nKey, Object... data) {
		return JOptionPane.showOptionDialog(null, data == null || data.length == 0 ? i18n.getString(i18nKey) : i18n.getString(i18nKey, data), i18n.getString("dialog.confirm.save.title"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[] { i18n.getString("dialog.confirm.save.button.yes"), i18n.getString("dialog.confirm.save.button.no") }, "default") == JOptionPane.YES_OPTION;
	}
	
	public static File selectFile(String extention) {
		Logger.info("Opening file chooser dialog, extension: " + extention + ", last saved path: " + (extention.equalsIgnoreCase(Config.EXCEL_FILE_EXTENSION) ? Config.LAST_FOLDER_EXPORT_OPEN : Config.LAST_FOLDER_OPEN));
		
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(extention.equalsIgnoreCase(Config.EXCEL_FILE_EXTENSION) ? Config.LAST_FOLDER_EXPORT_OPEN : Config.LAST_FOLDER_OPEN));
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int response = fileChooser.showSaveDialog(new JLabel(""));
		if (response == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			File absoluteParent = new File(selectedFile.getAbsolutePath()).getParentFile();
			
			if (!FileUtils.getExtension(selectedFile).equalsIgnoreCase("." + extention)) {
				selectedFile = new File(new File(selectedFile.getAbsolutePath()).getParent(), selectedFile.getName() + (selectedFile.getName().endsWith(".") ? "" : ".") + Config.FILE_EXTENSION);
			}
			
			if (extention.equalsIgnoreCase(Config.EXCEL_FILE_EXTENSION)) {
				Config.LAST_FOLDER_EXPORT_OPEN = absoluteParent.getAbsolutePath();
			} else {
				Config.LAST_FOLDER_OPEN = absoluteParent.getAbsolutePath();
			}
			
			return selectedFile;
		} else {
			// return new File("New QuickHour File.qhr");
			return null;
		}
	}
	
	public static List<String> readExcel(File file) throws Exception {
		return readExcel(file, 0);
	}
	
	public static List<String> readExcel(File file, int sheet) throws Exception {
		FileInputStream excelFile = new FileInputStream(file);
		Workbook workbook = new XSSFWorkbook(excelFile);
		Sheet datatypeSheet = workbook.getSheetAt(sheet);
		Iterator<Row> iterator = datatypeSheet.iterator();
		
		List<String> data = new ArrayList<String>();
		
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
		
		workbook.close();
		
		return data;
	}
	
}