package models.cartes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import controller.Capacite;
import controller.Gestionnaire_cartes_partie;
import models.enums.Dogme;
import models.enums.Origine;

public class Guide_Spirituel extends Religion{
	private ArrayList<Croyant> sesCroyants;
	
	public Guide_Spirituel(String nom, String capaciteDesc, Origine origine, Integer nombre,
			Set<Dogme> sesDogmes,Capacite capacite) {
		super(nom, capaciteDesc, origine, nombre, sesDogmes,capacite);
		// TODO Auto-generated constructor stub
		sesCroyants = new ArrayList<Croyant>();
	}
	
	/**
	 * Permet d'obtenir les croyants qu'un guide ramm�ne � lui.
	 * Ajoute les premiers croyants � sa liste de Croyant.
	 * Pour l'instant on ne laisse pas le choix au joueur.
	 * @return La liste de croyants que le guide ramm�ne � lui. 
	 */
	public List<Carte> ammenerCroyants(){
		//Donner la possibilit�e au joueur de choisir dans la liste de croyant.
		List<Carte> croyants = null;
		if(this.croyantsDisponible().size() >= super.nombre){
			//S'il y a assez de croyants disponibles
			croyants = new ArrayList<Carte>(this.croyantsDisponible().subList(0, this.nombre));
		}else croyants = new ArrayList<Carte>(this.croyantsDisponible());
		
		for(int i=0;i<croyants.size();i++){
			((Croyant)croyants.get(i)).setGuide(this);
			this.sesCroyants.add((Croyant)croyants.get(i));
		}
		return croyants;
	}
	
	/**
	 * Si le guide est d�fausser ou revient dans le main de son propri�taire alors les croyants sont lib�r�s.
	 */
	public void libererCroyants(){
		for(int i=0;i<this.sesCroyants.size();i++){
			this.sesCroyants.get(i).setGuide(null);
			this.sesCroyants.remove(i);
		}
	}
	
	public void removeCroyant(Croyant croyant){
		croyant.setGuide(null);
		this.sesCroyants.remove(croyant);
	}
	
	/**
	 * Permet d'obtenir la liste des croyants qu'un guide peut rammener � lui.
	 * @return La liste de croyant.
	 */
	public List<Carte> croyantsDisponible(){
		List<Carte> dispo = new ArrayList<Carte>();
		for(int i=0;i<Gestionnaire_cartes_partie.getTable().size();i++){
			Iterator<Dogme> itCroyant = Gestionnaire_cartes_partie.getTable().get(i).sesDogmes.iterator();
			boolean possible = false;
			while(itCroyant.hasNext()){
				Dogme dogme = (Dogme)itCroyant.next();
				Iterator<Dogme> itGuide = this.sesDogmes.iterator();
				while(itGuide.hasNext()){
					if(dogme.equals((Dogme)itGuide.next())) possible = true;
				}
			}
			if(possible) dispo.add(Gestionnaire_cartes_partie.getTable().get(i));
		}
		return dispo;
	}
	
	public ArrayList<Croyant> getSesCroyants() {
		return sesCroyants;
	}
	
	public Set<Dogme> getDogmes(){
		return super.sesDogmes;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer buf = new StringBuffer();
		buf.append("GUIDE : ").append(this.nom).append("\n").append("Nombre de croyants pouvant �tres ralli�s : ");
		buf.append(super.nombre).append("\n").append(this.capaciteDesc).append("\n Origine : ").append(this.origine).append("\n Dogmes").append(this.sesDogmes.toString());
		return buf.toString();
	}

}
