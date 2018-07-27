package caceresenzo.apps.quickhour.config;

import caceresenzo.libs.internationalization.HardInternationalization;
import caceresenzo.libs.internationalization.i18n;

public class Language {
	
	public static final String LANGUAGE_FRENCH = "Français";
	
	private static Language LANGUAGE;
	private HardInternationalization selected = null;
	
	private Language() {
		selected = new French();
	}
	
	public void initialize() {
		i18n.setSelectedLanguage(LANGUAGE_FRENCH);
	}
	
	private class French extends HardInternationalization {
		
		public French() {
			super();
			register(LANGUAGE_FRENCH);
		}
		
		@Override
		public void set() {
			o("error.title", "Erreur");
			o("error.parse-cli", "Erreur, l'interpreteur de commande à renvoyé une erreur: %s");
			o("error.codec.error", "Erreur, le convertisseur à rencontrer une erreur: %s\nVoir la console pour plus d'information.");
			o("error.codec.quickhourfile.unresolved-name", "Erreur, le nom suivant est inconnu de la base de donnée: \"%s\".\nCette entré ne sera pas pris en compte; pour corriger cela, rajoutez un utilisateur du même nom et réouvrez le fichier.");
			
			o("ui.window.main.container.border.title", "Insertion d'heure rapide");
			o("ui.window.user.container.border.title", "Employé");
			o("ui.window.user.button.new", "Nouveau");
			o("ui.window.hour.container.border.title", "Heure(s)");
			o("ui.window.hour.container.info.empty-hour", "Aucune heure n'est disponible");
			o("ui.window.hour.button.new", "Nouveau");
			o("ui.window.hour.button.new.quick", "Système d'insertion rapide");
			
			o("date.day.day0", "Dimanche");
			o("date.day.sunday", "Dimanche");
			o("date.day.day1", "Lundi");
			o("date.day.monday", "Lundi");
			o("date.day.day2", "Mardi");
			o("date.day.tuesday", "Mardi");
			o("date.day.day3", "Mercredi");
			o("date.day.wednesday", "Mercredi");
			o("date.day.day4", "Jeudi");
			o("date.day.thursday", "Jeudi");
			o("date.day.day5", "Vendredi");
			o("date.day.friday", "Vendredi");
			o("date.day.day6", "Samedi");
			o("date.day.saturday", "Samedi");
		}
		
	}
	
	public HardInternationalization getSelected() {
		return selected;
	}
	
	public static Language getLanguage() {
		if (LANGUAGE == null) {
			LANGUAGE = new Language();
		}
		
		return LANGUAGE;
	}
	
	public static HardInternationalization getActual() {
		return getLanguage().getSelected();
	}
	
}