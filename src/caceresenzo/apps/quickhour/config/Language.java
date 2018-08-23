package caceresenzo.apps.quickhour.config;

import caceresenzo.libs.internationalization.HardInternationalization;
import caceresenzo.libs.internationalization.i18n;

public class Language {
	
	public static final String LANGUAGE_FRENCH = "Fran�ais";
	
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
			o("error.parse-cli", "Erreur, l'interpreteur de commande � renvoy� une erreur: %s");
			o("error.codec.error", "Erreur, le convertisseur � rencontrer une erreur: %s\nVoir la console pour plus d'information.");
			o("error.codec.user.failed-saving", "Erreur, la sauvegarde du fichier des employ�s � �chou�.\nRaison: %s");
			o("error.codec.quickhourfile.unresolved-name", "Erreur, le nom suivant est inconnu de la base de donn�e: \"%s\".\nCette entr� ne sera pas pris en compte; pour corriger cela, rajoutez un employ� du m�me nom et r�ouvrez le fichier.");
			o("error.featrure.not-implemented", "Erreur, fonctionnalit� non implent�.\nCommande: %s");
			
			o("info.title", "Information");
			
			o("dialog.confirm.warning.title", "Attention!");
			
			o("dialog.confirm.save.title", "Sauvegarde");
			o("dialog.confirm.save.button.yes", "Oui");
			o("dialog.confirm.save.button.no", "Non");
			o("dialog.confirm.save.context.save-unsaved-work", "Sauvegarder le travail \"%s\" ?");
			o("dialog.confirm.save.error.save-unsaved-work.save-failed", "Erreur, la sauvegarde du fichier n'a pas r�ussi...\nRaison: %s");
			o("dialog.confirm.save.error.save-unsaved-work.save-failed-retry", "Voulez vous r��sayer ?");
			o("dialog.confirm.save.error.save-unsaved-work.cancelled", "Sauvegarde annul�!");
			
			o("dialog.file.open.error.file-is-null", "Erreur, aucun fichier selectionn�.");
			o("dialog.file.open.error.load-failed", "Erreur, le fichier n'a pas pu �tre charg�.\nRaison: %s");
			
			o("manager.quickhour.references-formats.default.display", "<DEFAUT>");
			
			o("menu.file.title", "Fichier");
			o("menu.file.item.new", "Nouveau");
			o("menu.file.item.open", "Ouvrir...");
			o("menu.file.item.save", "Enregistrer");
			o("menu.file.item.save-as", "Enregistrer sous...");
			o("menu.file.item.export", "Exporter (Excel)...");
			o("menu.file.item.close", "Fermer");
			
			o("menu.quickhour.title", "QuickHour");
			o("menu.quickhour.item.quit", "Quitter l'application");
			o("menu.quickhour.item.debug", "D�bogage");
			o("menu.quickhour.item.debug.subitem.dump-file", "Dumper un fichier");
			
			o("ui.dialog.x.button.ok", "Valider");
			o("ui.dialog.x.button.cancel", "Annuler");
			o("ui.dialog.x.button.close", "Fermer");
			o("ui.dialog.x.button.next-entry", "Entr� suivante");
			
			o("ui.window.main.container.border.title", "Insertion d'heure rapide");
			o("ui.window.user.container.border.title", "Employ�(s)");
			o("ui.window.user.button.new", "Nouveau");
			o("ui.window.hour.container.border.title", "Heure(s)");
			o("ui.window.hour.container.info.empty-hour", "Aucune heure n'est disponible");
			o("ui.window.hour.button.new", "Nouveau");
			o("ui.window.hour.button.new.quick", "Syst�me d'insertion rapide");
			
			o("ui.dialog.new-user.label.firstname", "Pr�nom");
			o("ui.dialog.new-user.label.lastname", "Nom");
			o("ui.dialog.new-user.label.display", "Affichage");
			o("ui.dialog.new-user.label.key", "Cl�");
			o("ui.dialog.new-user.checkbox.auto-config", "Configuration auto.");
			o("ui.dialog.new-user.error.empty-data", "Valeur non conforme. Ajout annul�.");
			o("ui.dialog.new-user.warning.add-confirmation", "Voulez-vous ajouter l'employ� %s ?");
			
			o("ui.dialog.quick-hour.container.constants", "Constantes");
			o("ui.dialog.quick-hour.label.selection-user", "Employ�");
			o("ui.dialog.quick-hour.label.selection-reference-format", "Format de r�f�rence");
			o("ui.dialog.quick-hour.label.selection-day", "Jour");
			o("ui.dialog.quick-hour.container.hour-entry", "Ajout d'une heure");
			o("ui.dialog.quick-hour.label.reference", "R�f�rence");
			o("ui.dialog.quick-hour.label.time", "Temps");
			o("ui.dialog.quick-hour.label.time-real-value", "Valeur r�el: %s");
			o("ui.dialog.quick-hour.button.validate-hour", "Valider");
			o("ui.dialog.quick-hour.button.next-day", "Jour suivant");
			o("ui.dialog.quick-hour.error.no-user", "Erreur, aucun employ� n'est s�lectionn�.");
			o("ui.dialog.quick-hour.error.field-reference-empty", "Erreur, la r�f�rence est vide.");
			o("ui.dialog.quick-hour.error.field-time-empty", "Erreur, le temps est vide.");
			o("ui.dialog.quick-hour.error.field-time-invalid-number", "Erreur, le temps n'est pas un chiffre.");
			
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
			
			o("application.config.error.failed-saving", "Erreur, le fichier de configuration n'a pas r�ussi � se sauvegarder.\nCause: %s");
			
			o("export.manager.organization-template.file-not-found", "Erreur, la feuille d'organisation n'� pas �t� trouv�. L'exportation se fera que sur les references connus.");
			o("dialog.file.export.error.file-is-null", "Erreur, aucun fichier selectionn�. Exportation annul�");
			o("export.error.failed", "Erreur, l'exportation a rencontrer un probl�me.\nRaison: %s");
			o("export.info.success", "Exportation termin�.\nChemin d'acc�s: %s");
			o("export.column.preheader.total-known-reference-column", "NBR REF. CONNU");
			o("export.column.user", "EMPLOY�".toUpperCase());
			o("export.column.day", "JOUR");
			o("export.column.total", "TOTAL");
			o("export.column.total-week", "T. SEM.");
			o("export.column.total-reference", "T. REF.");
			o("export.error.unreferenced-references.list", "Des r�f�rences inconnus ont �t� trouv�:\n%s\nTotal: %s");
			o("export.error.unreferenced-references.list.format", "  - %s (heure(s): %s)\n");
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