package caceresenzo.apps.quickhour.utils;

import javax.swing.JOptionPane;

import caceresenzo.libs.internationalization.i18n;

public class Utils {
	
	public static void showDialog(String i18nKey, Object... data) {
		showDialog(JOptionPane.ERROR_MESSAGE, "error.title", i18nKey, data);
	}
	
	public static void showDialog(int type, String i18nTitle, String i18nKey, Object... data) {
		JOptionPane.showMessageDialog(null, data == null || data.length == 0 ? i18n.getString(i18nKey) : i18n.getString(i18nKey, data), i18n.getString(i18nTitle), type);
	}
	
}