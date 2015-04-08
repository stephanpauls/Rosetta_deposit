package com.exlibris.dps.sdk.dgtconv;

public class FileInfo {

	private String USE;
	private String PID;
	private String naam;
	private edu.harvard.hul.ois.mets.File file;
	private String DMDID;
	
	public FileInfo() {
		USE = null;
		PID = null;
		naam = null;
		file = null;
		DMDID = null;
	}
	
	public void clear() {
		USE = null;
		PID = null;
		naam = null;
		file = null;
		DMDID = null;	}

	public edu.harvard.hul.ois.mets.File getFile() {
		return file;
	}

	public void setFile(edu.harvard.hul.ois.mets.File file) {
		this.file = file;
	}

	public String getUSE() {
		return USE;
	}

	public void setUSE(String uSE) {
		USE = uSE;
	}

	public String getPID() {
		return PID;
	}

	public void setPID(String pID) {
		PID = pID;
	}

	public String getNaam() {
		return naam;
	}

	public void setNaam(String naam) {
		this.naam = naam;
	}

	public String getDMDID() {
		return DMDID;
	}

	public void setDMDID(String dMDID) {
		DMDID = dMDID;
	}

}
	
