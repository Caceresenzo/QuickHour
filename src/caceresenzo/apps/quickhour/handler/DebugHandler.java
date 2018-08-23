package caceresenzo.apps.quickhour.handler;

import java.io.File;

import caceresenzo.apps.quickhour.TestUnits;
import caceresenzo.apps.quickhour.utils.Utils;
import caceresenzo.libs.logger.Logger;

public class DebugHandler {
	
	public static void openDumpDialog() {
		try {
			File inputFile = Utils.selectFile(null);
			
			TestUnits.FileDumperTest.dump(inputFile, false);
			
			Logger.success("Dump done");
		} catch (Exception exception) {
			Logger.exception(exception, "Failed to dump file");
		}
	}
	
}