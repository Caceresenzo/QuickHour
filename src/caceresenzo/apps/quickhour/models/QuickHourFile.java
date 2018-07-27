package caceresenzo.apps.quickhour.models;

import java.util.List;

import caceresenzo.libs.comparator.Version;

public class QuickHourFile {
	
	private Version fileVersion;
	private String fileName, fileDescription;
	private List<QuickHourUser> usersHours;
	
	public QuickHourFile() {
		this(new Version("0"), null, null, null);
	}
	
	public QuickHourFile(Version fileVersion, String fileName, String fileDescription, List<QuickHourUser> userHours) {
		this.fileVersion = fileVersion;
		this.fileName = fileName;
		this.fileDescription = fileDescription;
		this.usersHours = userHours;
	}
	
	public Version getFileVersion() {
		return fileVersion;
	}
	
	public void setFileVersion(Version fileVersion) {
		this.fileVersion = fileVersion;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getFileDescription() {
		return fileDescription;
	}
	
	public void setFileDescription(String fileDescription) {
		this.fileDescription = fileDescription;
	}
	
	public List<QuickHourUser> getUsersHours() {
		return usersHours;
	}
	
	public void setUsersHours(List<QuickHourUser> usersHours) {
		this.usersHours = usersHours;
	}
	
}