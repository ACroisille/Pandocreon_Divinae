package models.enums;

public enum Dogme {
	NATURE("nature"),
	HUMAIN("humain"),
	SYMBOLES("symboles"),
	MYSTIQUE("mystique"),
	CHAOS("chaos");
	
	private String dogme=null;
	
	Dogme(String dogme){
		this.dogme = dogme;
	}
	
	public String getName(){
		return this.dogme;
	}
		
}
