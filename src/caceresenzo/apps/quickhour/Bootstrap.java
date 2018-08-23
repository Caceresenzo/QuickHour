package caceresenzo.apps.quickhour;

import java.io.File;

import javax.swing.JOptionPane;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import caceresenzo.apps.quickhour.codec.implementations.JsonUserCodec;
import caceresenzo.apps.quickhour.config.Config;
import caceresenzo.apps.quickhour.config.Language;
import caceresenzo.apps.quickhour.handler.WorkHandler;
import caceresenzo.apps.quickhour.manager.QuickHourManager;
import caceresenzo.apps.quickhour.ui.HourEditorWindow;
import caceresenzo.libs.internationalization.i18n;
import caceresenzo.libs.logger.Logger;

public class Bootstrap {
	
	private static File inputFile;
	
	public static void main(String[] args) throws Exception {
		Logger.setStaticLength(20);
		Language.getLanguage().initialize();
		Config.prepareConfig();
		
		/* CLI parsing */
		Options options = new Options();
		
		Option inputOption = new Option("i", "input", true, "input file");
		inputOption.setRequired(false);
		options.addOption(inputOption);
		
		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		CommandLine commandLine = null;
		try {
			commandLine = parser.parse(options, args);
		} catch (ParseException exception) {
			System.out.println(exception.getMessage());
			formatter.printHelp("Quick Hour", options);
			
			JOptionPane.showMessageDialog(null, i18n.getString("error.parse-cli", exception.getLocalizedMessage()), i18n.getString("error.title"), JOptionPane.ERROR_MESSAGE);
			
			System.exit(1);
		}
		
		try {
			if (commandLine.getOptionValue("input") != null) {
				inputFile = new File(commandLine.getOptionValue("input"));
			}
		} catch (Exception exception) {
			Logger.exception(exception, "Failed to get input from command line");
		}
		
		/* Main manager initialization */
		QuickHourManager.getQuickHourManager().initialize();
		
		/* Loading data */
		new JsonUserCodec().read(new File(Config.USERS_PATH));
		// QuickHourFile quickHourFile = new JsonQuickHourFileCodec().read(new File("myhour/WEEK 30.qhr"));
		
		/* Start UI */
		HourEditorWindow.start();
	}
	
	public static void onUiReady() {
		if (inputFile != null && inputFile.exists()) {
			WorkHandler.openFile(inputFile);
		}
	}
	
}