package caceresenzo.apps.quickhour.handler;

import java.io.File;
import java.util.List;

import caceresenzo.apps.quickhour.codec.implementations.JsonUserCodec;
import caceresenzo.apps.quickhour.config.Config;
import caceresenzo.apps.quickhour.manager.QuickHourManager;
import caceresenzo.apps.quickhour.models.QuickHourUser;
import caceresenzo.apps.quickhour.utils.Utils;
import caceresenzo.libs.logger.Logger;

public class ClosingHandler {
	
	public static void handleClose() {
		Logger.info("ClosingHandler as been called, saving everything up!");
		
		List<QuickHourUser> users = QuickHourManager.getQuickHourManager().getUsers();
		
		try {
			new JsonUserCodec().write(new File(Config.USERS_PATH), users);
		} catch (Exception exception) {
			Logger.exception(exception, "Error when saving users file.");
			Utils.showErrorDialog("error.codec.user.failed-saving", exception.getLocalizedMessage());
		}
		
		boolean canQuit = WorkHandler.checkSave();
		
		if (canQuit) {
			System.exit(0);
		}
	}
	
}