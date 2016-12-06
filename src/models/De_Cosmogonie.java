package models;

import models.cartes.Origine;

public class De_Cosmogonie {

	private static De_Cosmogonie de = null;
	
	public static final De_Cosmogonie getInstance(){
		if(de==null){
			de = new De_Cosmogonie();
		}
		return de;
	}
	public static Origine lancerDe(){
		Origine[] o = {Origine.JOUR,Origine.NUIT,Origine.NEANT}; 
		return o[(int)(Math.random()*o.length)];
	}
	

}
