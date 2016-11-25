package models.cartes;

public enum Dogme {
	NATURE("Nature"),
	HUMAIN("Humain"),
	SYMBOLES("Symboles"),
	MYSTIQUE("Mystique"),
	CHAOS("Chaos");
	
	private String dogme=null;
	
	Dogme(String dogme){
		this.dogme = dogme;
	}
		
}
