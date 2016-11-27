package de;

import models.cartes.Origine;

public class De_Cosmogonie {
	private De_Cosmogonie()
	{}
	private static De_Cosmogonie INSTANCE = null;
	public static final De_Cosmogonie getInstance(){
		if(INSTANCE==null){
			INSTANCE = new De_Cosmogonie();
		}
		return INSTANCE;
	}
	public static Origine lancerDe(){
		return Origine.values()[(int)(Math.random()*Origine.values().length)];
		
	}
	

}
