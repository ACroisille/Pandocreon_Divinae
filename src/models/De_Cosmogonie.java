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
		return Origine.values()[(int)(Math.random()*Origine.values().length)];
		
	}
	

}
