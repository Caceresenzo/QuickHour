package caceresenzo.apps.quickhour.handler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import caceresenzo.apps.quickhour.codec.implementations.ExcelExportQuickHourFileCodec;
import caceresenzo.apps.quickhour.codec.implementations.JsonQuickHourFileCodec;
import caceresenzo.apps.quickhour.config.Config;
import caceresenzo.apps.quickhour.manager.QuickHourManager;
import caceresenzo.apps.quickhour.models.QuickHourFile;
import caceresenzo.apps.quickhour.ui.HourEditorWindow;
import caceresenzo.apps.quickhour.utils.Utils;
import caceresenzo.libs.filesystem.FilenameUtils;
import caceresenzo.libs.logger.Logger;

public class WorkHandler {
	
	private static boolean unsavedWork = false, alreadySavedFile = false;
	private static File actualFile = null;
	private static QuickHourFile actualQuickHourFile = null;
	
	private WorkHandler() {
		;
	}
	
	public static void openFile() {
		File openFile = Utils.selectFile(Config.FILE_EXTENSION);
		
		if (openFile == null) {
			Utils.showErrorDialog("dialog.file.open.error.file-is-null");
			return;
		}
		
		try {
			QuickHourManager.getQuickHourManager().emptyHoursOfEveryone();
			
			actualQuickHourFile = new JsonQuickHourFileCodec().read(openFile);
			
			HourEditorWindow.getHourEditorWindow().notifyUiCompleteRefresh();
			
			actualFile = openFile;
		} catch (Exception exception) {
			Logger.exception(exception, "Failed to load file");
			Utils.showErrorDialog("dialog.file.open.error.load-failed", exception.getLocalizedMessage());
		}
	}
	
	public static boolean saveFile(boolean saveAs) {
		while (true) {
			try {
				if (saveAs || actualFile == null) {
					actualFile = Utils.selectFile(Config.FILE_EXTENSION);
					
					if (actualFile == null) {
						Utils.showInfoDialog("dialog.confirm.save.error.save-unsaved-work.cancelled");
						return false;
					}
				}
				
				getActualQuickHourFile(); // Just create on if null
				
				if (actualQuickHourFile.getFileName() == null || actualQuickHourFile.getFileName().isEmpty()) {
					actualQuickHourFile.setFileName(FilenameUtils.getName(actualFile.getName()));
				}
				
				new JsonQuickHourFileCodec().write(actualFile, actualQuickHourFile);
				
				// throw new RuntimeException();
				break;
			} catch (Exception exception) {
				Logger.exception(exception, "Error when saving file");
				Utils.showErrorDialog("dialog.confirm.save.error.save-unsaved-work.save-failed", exception.getLocalizedMessage());
				
				if (!Utils.showWarningConfirm("dialog.confirm.save.error.save-unsaved-work.save-failed-retry")) {
					return false;
				}
				
				if (!alreadySavedFile) {
					actualFile = null;
				}
			}
		}
		
		unsavedWork = false;
		alreadySavedFile = true;
		
		return true;
	}
	
	public static boolean checkSave() {
		if (actualQuickHourFile != null) {
			if (WorkHandler.hasUnsavedWork()) {
				if (Utils.showSaveConfirm("dialog.confirm.save.context.save-unsaved-work", actualQuickHourFile.getFileName())) {
					return WorkHandler.saveFile(false);
				}
			}
		}
		
		return true;
	}
	
	public static void closeWorksheet() {
		if (WorkHandler.checkSave()) {
			QuickHourManager.getQuickHourManager().emptyHoursOfEveryone();
			HourEditorWindow.getHourEditorWindow().selectUser(null);
			HourEditorWindow.getHourEditorWindow().updateUsers();
			
			alreadySavedFile = false;
			actualQuickHourFile = new QuickHourFile();
			actualFile = null;
		}
	}
	public static void exportFile() {
		exportFile(Utils.selectFile(Config.EXCEL_FILE_EXTENSION));
	}
	
	public static void exportFile(File exportFile) {
		checkSave();
		
		if (exportFile == null) {
			Utils.showErrorDialog("dialog.file.export.error.file-is-null");
			return;
		}
		
		// TODO: Open dialog to allow custom entry
		
		try {
			new ExcelExportQuickHourFileCodec().write(exportFile, actualQuickHourFile);
		} catch (Exception exception) {
			Logger.exception(exception, "Failed to export file");
			Utils.showErrorDialog("export.error.failed", exception.getLocalizedMessage());
		}
	}
	
	public static boolean hasUnsavedWork() {
		return unsavedWork;
	}
	
	public static void hasUnsavedWork(boolean unsavedWork) {
		WorkHandler.unsavedWork = unsavedWork;
	}
	
	public static boolean isAlreadySavedFile() {
		return alreadySavedFile;
	}
	
	public static void isAlreadySavedFile(boolean alreadySavedFile) {
		WorkHandler.alreadySavedFile = alreadySavedFile;
	}
	
	public static File getActualFile() {
		return actualFile;
	}
	
	public static void setActualFile(File actualFile) {
		WorkHandler.actualFile = actualFile;
	}
	
	public static QuickHourFile getActualQuickHourFile() {
		if (actualQuickHourFile == null) {
			QuickHourManager.getQuickHourManager().emptyHoursOfEveryone();
			actualQuickHourFile = new QuickHourFile();
			HourEditorWindow.getHourEditorWindow().notifyUiCompleteRefresh();
		}
		return actualQuickHourFile;
	}
	
	public static void setActualQuickHourFile(QuickHourFile actualQuickHourFile) {
		WorkHandler.actualQuickHourFile = actualQuickHourFile;
	}
	
}