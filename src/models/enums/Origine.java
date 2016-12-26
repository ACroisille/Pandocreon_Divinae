package models.enums;

public enum Origine {
	JOUR("jour"),
	NUIT("nuit"),
	AUBE("aube"),
	CREPUSCULE("crepuscule"),
	NEANT("neant");
	
	private String origine;
	
	Origine(String origine){
		this.origine = origine;
	}
	
	public String getOrigineName() {
		return origine;
	}
}
