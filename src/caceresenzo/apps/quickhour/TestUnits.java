package caceresenzo.apps.quickhour;

import java.io.File;
import java.util.List;

import caceresenzo.apps.quickhour.codec.implementations.JsonQuickHourFileCodec;
import caceresenzo.apps.quickhour.codec.implementations.JsonUserCodec;
import caceresenzo.apps.quickhour.config.Language;
import caceresenzo.apps.quickhour.models.QuickHourDay;
import caceresenzo.apps.quickhour.models.QuickHourFile;
import caceresenzo.apps.quickhour.models.QuickHourReference;
import caceresenzo.apps.quickhour.models.QuickHourUser;
import caceresenzo.apps.quickhour.utils.Utils;
import caceresenzo.libs.logger.Logger;

public class TestUnits {
	
	public static class UserJsonTestUnits {
		
		public static void main(String[] args) throws Exception {
			List<QuickHourUser> users = new JsonUserCodec().read(new File("config/users.json"));
			
			for (QuickHourUser user : users) {
				Logger.info("user: %s, with display: %s", user.getName(), user.getDisplay());
			}
			
			new JsonUserCodec().write(new File("test/config/users.json"), users);
		}
		
	}
	
	public static class QuickHourFileJsonTestUnits {
		
		public static void main(String[] args) throws Exception {
			// Need users database to be configured
			new JsonUserCodec().read(new File("config/users.json"));
			
			QuickHourFile quickHourFile = new JsonQuickHourFileCodec().read(new File("myhour/WEEK 30.qhr"));

			Logger.info("File Version: " + quickHourFile.getFileVersion().get());
			Logger.info("File Name: " + quickHourFile.getFileName());
			Logger.info("File Name: " + quickHourFile.getFileDescription());
			Logger.info("Hour size: " + quickHourFile.getUsersHours().size());
			
			for (QuickHourUser user : quickHourFile.getUsersHours()) {
				Logger.info("\t| user: " + user.getName());
				
				for (QuickHourDay day : user.getDays()) {
					Logger.info("\t| \t| day: " + day.getDayName());

					for (QuickHourReference reference : day.getReferences()) {
						Logger.info("\t| \t| \t| ref: " + reference.getReference() + " [time: " + reference.getHourCount() + "]");
					}
				}
			}
			
			
			new JsonQuickHourFileCodec().write(new File("test/myhour/WEEK 30.qhr"), quickHourFile);
		}
		
	}
	
	public static class DialogTestUnits {
		
		public static void main(String[] args) throws Exception {
			Language.getLanguage().initialize();
			Utils.showDialog("error.codec.quickhourfile.unresolved-name", "enzo.caceres");
		}
		
	}
	
}