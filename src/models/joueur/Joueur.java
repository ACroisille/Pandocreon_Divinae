package models.joueur;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controller.Gestionnaire_Cartes_Joueur;
import models.cartes.Carte;
import models.cartes.ConstanteCarte;
import models.cartes.Divinite;
import models.cartes.Origine;

public abstract class Joueur {
	protected Map<Origine,Integer> pointsAction;
	protected Gestionnaire_Cartes_Joueur gcj = null;

	public Joueur(){
		this.pointsAction = new HashMap<Origine,Integer>();
	}
	
	/**
	 * Permet au joueur de jouer
	 * @return Renvois faux si une carte APOCALYPSE a été joué sinon renvois vrai.
	 */
	public boolean jouer(){
		return false;
	};
	
	/**
	 * Permet au joueur de jouer une carte de sa main.
	 * @param indice L'indice de la carte dans sa main.
	 * @return Vrai si la carte a éffectivement été joué. Faux sinon. 
	 */
	public boolean jouerCarte(int indice){
		return false;
	}
	
	/**
	 * Permet au joueur de sacrifier une des cartes qui se trouvent devant lui.
	 * @param indice L'indice de la carte devant lui.
	 */
	public void sacrifierCarte(int indice){
		
	}
	
	/**
	 * Permet au joueur d'utiliser la capacité de sa divinité. 
	 */
	public void activerCapaciteDivinite(){
		
	}
	
	/**
	 * Permet au joueur de savoir quels cartes il peut jouer en fonction de ses points d'action. 
	 * @return Une sous liste de main. 
	 */
	public List<Carte> cartesJouables(){
		return null;
	}
	
	/**
	 * Distribue des points d'action au joueur en fonction de sa divinité. 
	 * @param origine L'Origine tiré par le dé de cosmogonie. 
	 */
	public void attribuerPointsAction(Origine origine){
		//TODO Implèmenter la distribution des points d'action en fonction de la divinitée de chacun des joueurs
	}
	
	/**
	 * Ajoute 1 point d'action dans la PointsAction en fonction de l'Origine.
	 * @param origine L'Origine. 
	 */
	public void incrementerPointAction(Origine origine){
		//TODO Ajoute 1 point d'action dans la HashMap en fonction 
	}
	
	public void attachGestionnaire_Cartes_Joueur(List<Carte> main, Divinite divinite){
		this.gcj = new Gestionnaire_Cartes_Joueur(this, main, divinite);
	}
	
	public Gestionnaire_Cartes_Joueur getGestionnaire_Cartes_Joueur(){
		return this.gcj;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer buf = new StringBuffer();
		buf.append("Points d'action : ").append(pointsAction.toString()).append(ConstanteCarte.BARRE);
		buf.append(gcj.toString()).append("\n");
		return buf.toString();
	}
}
