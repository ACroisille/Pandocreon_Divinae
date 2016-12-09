package models.cartes;

import java.util.Set;

import controller.Capacite;
import models.joueur.Joueur;

public class Croyant extends Religion{
	
	private Guide_Spirituel guide;
	
	public Croyant(String nom,String capaciteDesc,Origine origine,Integer nombre,Set<Dogme> sesDogmes,Capacite capacite){
		super(nom,capaciteDesc,origine,nombre,sesDogmes,capacite);
		guide = null;
	}

	public int getPointsPriere(){
		return super.nombre;
	}
	
	public void setGuide(Guide_Spirituel guide) {
		this.guide = guide;
	}
	
	public Set<Dogme> getDogmes(){
		return super.sesDogmes;
	}
	
	public Guide_Spirituel getGuide() {
		return guide;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer buf = new StringBuffer();
		buf.append("CROYANT : ").append(this.nom).append("\n").append("Points de prière : ").append(super.nombre);
		buf.append("\n").append(this.capaciteDesc).append("\n Origine : ").append(this.origine).append("\n Dogmes : ").append(this.sesDogmes.toString());
		return buf.toString();
	}

}
