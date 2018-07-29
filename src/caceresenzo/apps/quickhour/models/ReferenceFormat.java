package caceresenzo.apps.quickhour.models;

public class ReferenceFormat {
	
	private final String name, display, format;
	
	public ReferenceFormat(String name, String display, String format) {
		this.name = name;
		this.display = display;
		this.format = format;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDisplay() {
		return display;
	}
	
	public String getFormat() {
		return format;
	}
	
}