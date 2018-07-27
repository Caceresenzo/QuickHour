package caceresenzo.apps.quickhour.models;

public class QuickHourReference {
	
	private String reference;
	private float hourCount;
	
	public QuickHourReference(String reference) {
		this(reference, 0.0F);
	}
	
	public QuickHourReference(String reference, float hourCount) {
		this.reference = reference;
		this.hourCount = hourCount;
	}
	
	public String getReference() {
		return reference;
	}
	
	public float getHourCount() {
		return hourCount;
	}
	
	public float addToCount(float add) {
		hourCount += add;
		return hourCount;
	}
	
	public QuickHourReference updateHourCount(float hourCount) {
		this.hourCount = hourCount;
		return this;
	}
	
	@Override
	public String toString() {
		return reference + "=" + hourCount;
	}
	
}