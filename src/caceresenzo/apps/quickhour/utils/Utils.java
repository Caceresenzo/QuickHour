package caceresenzo.apps.quickhour.utils;

import javax.swing.JOptionPane;

import caceresenzo.libs.internationalization.i18n;

public class Utils {
	
	public static void showErrorDialog(String i18nKey, Object... data) {
		showDialog(JOptionPane.ERROR_MESSAGE, "error.title", i18nKey, data);
	}
	
	public static void showDialog(int type, String i18nTitle, String i18nKey, Object... data) {
		JOptionPane.showMessageDialog(null, data == null || data.length == 0 ? i18n.getString(i18nKey) : i18n.getString(i18nKey, data), i18n.getString(i18nTitle), type);
	}
	
	public static boolean showWarningConfirm(String i18nKey, Object... data) {
		return showConfirm("dialog.confirm.warning.title", i18nKey, data);
	}
	
	public static boolean showConfirm(String i18nTitle, String i18nKey, Object... data) {
		return JOptionPane.showConfirmDialog(null,  data == null || data.length == 0 ? i18n.getString(i18nKey) : i18n.getString(i18nKey, data), i18n.getString(i18nTitle), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
	}
	
}