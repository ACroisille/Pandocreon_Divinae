package models.enums;

public enum Origine {
	JOUR("Jour"),
	NUIT("Nuit"),
	AUBE("Aube"),
	CREPUSCULE("Crepuscule"),
	NEANT("Neant");
	
	private String origine;
	
	Origine(String origine){
		this.origine = origine;
	}
}
