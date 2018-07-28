package caceresenzo.apps.quickhour;

import java.io.File;

import caceresenzo.apps.quickhour.codec.implementations.JsonQuickHourFileCodec;
import caceresenzo.apps.quickhour.codec.implementations.JsonUserCodec;
import caceresenzo.apps.quickhour.config.Config;
import caceresenzo.apps.quickhour.config.Language;
import caceresenzo.apps.quickhour.manager.QuickHourManager;
import caceresenzo.apps.quickhour.models.QuickHourFile;
import caceresenzo.apps.quickhour.ui.QuickHourWindow;
import caceresenzo.libs.logger.Logger;

public class Bootstrap {
	
	public static void main(String[] args) throws Exception {
		Logger.setStaticLength(20);
		Language.getLanguage().initialize();
		Config.prepareConfig();
		
		QuickHourManager.getQuickHourManager().initialize();
		
		/*
		 * Loading data
		 */
		new JsonUserCodec().read(new File("config/users.json"));
		QuickHourFile quickHourFile = new JsonQuickHourFileCodec().read(new File("myhour/WEEK 30.qhr"));
		
		QuickHourWindow.start();
	}
	
}