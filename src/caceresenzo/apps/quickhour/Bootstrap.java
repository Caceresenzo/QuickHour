package caceresenzo.apps.quickhour;

import caceresenzo.apps.quickhour.config.Language;

public class Bootstrap {
	
	public static void main(String[] args) {
		Language.getLanguage().initialize();
		
		
	}
	
}