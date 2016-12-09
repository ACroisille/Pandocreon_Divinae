package models.enums;

public enum Retour {
	
	CONTINUE(0),
	STOPTOUR(1),
	APOCALYPSE(2);
	
	private int retour;
	
	private Retour(int retour) {
		this.retour = retour;
	}
}
