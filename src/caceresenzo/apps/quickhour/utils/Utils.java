package caceresenzo.apps.quickhour.utils;

import java.awt.Toolkit;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import caceresenzo.apps.quickhour.config.Config;
import caceresenzo.libs.filesystem.FileUtils;
import caceresenzo.libs.internationalization.i18n;

public class Utils {
	
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
	
}