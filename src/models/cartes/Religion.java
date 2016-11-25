package models.cartes;

import java.util.Set;

import controller.Capacite;

public abstract class Religion extends Carte{
	
	protected Integer nombre;
	protected Set<Dogme> sesDogmes;
	
	public Religion(String nom,String capaciteDesc,Origine origine,Integer nombre,Set<Dogme> sesDogmes) {
		super(nom,capaciteDesc,origine);
		this.nombre = nombre;
		this.sesDogmes = sesDogmes;
	}
	
}
