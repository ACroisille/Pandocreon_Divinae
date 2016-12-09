package models.cartes;

import java.util.Set;

import controller.Capacite;
import models.enums.Dogme;
import models.enums.Origine;

public abstract class Religion extends Carte{
	
	protected Integer nombre;
	protected Set<Dogme> sesDogmes;
	
	public Religion(String nom,String capaciteDesc,Origine origine,Integer nombre,Set<Dogme> sesDogmes,Capacite capacite) {
		super(nom,capaciteDesc,origine,capacite);
		this.nombre = nombre;
		this.sesDogmes = sesDogmes;
	}
	
	public Integer getNombre() {
		return nombre;
	}
	
}
