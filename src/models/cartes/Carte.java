package models.cartes;

import controller.Capacite;

public abstract class Carte{
	
	protected String nom;
	protected String capaciteDesc;
	protected Origine origine;
	
	protected boolean capaciteUsed;
	protected Capacite capacite;
	
	public Carte(String nom,String capaciteDesc,Origine origine){
		this.nom = nom;
		this.capaciteDesc = capaciteDesc;
		this.origine = origine;
		this.capacite = capacite;
		this.capaciteUsed = false;
	}
	
	public void setCapacite(Capacite c){
		this.capacite = capacite;
	}
	
}
