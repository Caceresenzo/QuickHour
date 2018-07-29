package caceresenzo.apps.quickhour.handler;

import java.io.File;

import caceresenzo.apps.quickhour.codec.implementations.JsonQuickHourFileCodec;
import caceresenzo.apps.quickhour.manager.QuickHourManager;
import caceresenzo.apps.quickhour.models.QuickHourFile;
import caceresenzo.apps.quickhour.ui.QuickHourWindow;
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
		File openFile = Utils.selectFile();
		
		if (openFile == null) {
			Utils.showErrorDialog("dialog.file.open.error.file-is-null");
			return;
		}
		
		try {
			QuickHourManager.getQuickHourManager().emptyHoursOfEveryone();
			
			actualQuickHourFile = new JsonQuickHourFileCodec().read(openFile);
			
			QuickHourWindow.getQuickHourWindow().notifyUiCompleteRefresh();
			
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
					actualFile = Utils.selectFile();
					
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
			QuickHourWindow.getQuickHourWindow().notifyUiCompleteRefresh();
		}
		return actualQuickHourFile;
	}
	
	public static void setActualQuickHourFile(QuickHourFile actualQuickHourFile) {
		WorkHandler.actualQuickHourFile = actualQuickHourFile;
	}
	
}