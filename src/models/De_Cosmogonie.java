package models;

import models.enums.Origine;

public class De_Cosmogonie {
	static Origine origine;
	private static De_Cosmogonie de = null;
	
	public static final De_Cosmogonie getInstance(){
		if(de==null){
			de = new De_Cosmogonie();
		}
		return de;
	}
	public static Origine lancerDe(){
		Origine[] o = {Origine.JOUR,Origine.NUIT,Origine.NEANT};
		origine=o[(int)(Math.random()*o.length)];
		return origine;
	}
	public static Origine getOrigine(){
		return origine;
	}
	

}
