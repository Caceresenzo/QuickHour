package caceresenzo.apps.quickhour.models;

public class SortTemplateReference extends ReferenceFormat {
	
	private boolean displayable;
	
	public SortTemplateReference(String sortTemplate) {
		this(sortTemplate, true);
	}
	
	public SortTemplateReference(String sortTemplate, boolean displayable) {
		super(SortTemplateReference.class.getSimpleName() + ":" + sortTemplate, sortTemplate, sortTemplate);
		
		this.displayable = displayable;
	}
	
	public String getString() {
		return this.getDisplay().toUpperCase();
	}
	
	public boolean isDisplayable() {
		return displayable;
	}
	
}