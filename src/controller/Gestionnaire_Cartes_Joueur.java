package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.sun.org.apache.bcel.internal.generic.INSTANCEOF;

import models.cartes.Apocalypse;
import models.cartes.Carte;
import models.cartes.ConstanteCarte;
import models.cartes.Croyant;
import models.cartes.Deus_Ex;
import models.cartes.Divinite;
import models.cartes.Guide_Spirituel;
import models.cartes.Origine;
import models.joueur.Joueur;

public class Gestionnaire_Cartes_Joueur {
	
	public static final int SIZE_MAX = 7;
	
	private Joueur joueur;
	private List<Carte> main;
	private List<Guide_Spirituel> guides;
	private List<Croyant> croyants;
	private Divinite divinite;
	private Carte pilePose=null,pileSacrifice=null; //Ajouter listener dessus
	
	public Gestionnaire_Cartes_Joueur(Joueur joueur, List<Carte> main,Divinite divinite){
		this.joueur = joueur;
		this.main = main;
		this.divinite = divinite;
		this.guides = new ArrayList<Guide_Spirituel>();
		this.croyants = new ArrayList<Croyant>();
	}
	
	/**
	 * Transfert les croyants ralliés par un guide spirituel de la table vers le champ de bataille. 
	 * @param guide
	 * @param croyants
	 */
	public void transfertCroyants(Guide_Spirituel guide,List<Croyant> croyants){
		this.croyants.addAll(croyants);
		for(int i=0;i<croyants.size();i++){
			Gestionnaire_cartes_partie.removeTable(croyants.get(i));
		}
	}
	
	/**
	 * Transfert une partie ou la totalité des cartes de la main du joueur dans la défausse. .
	 * Ajoute la liste de carte au Gestionnaire Carte Partie
	 */
	public void defausserMain(List<Carte> defausse){
		this.main.removeAll(defausse);
		for(int i = 0; i < defausse.size(); i++){
			Gestionnaire_cartes_partie.addDefausse(defausse.get(i));
		}
	}
	
	public void defausserChampsDeBataille(Carte carte){
		if(carte instanceof Croyant){
			if(this.croyants.contains((Croyant)carte)){
				croyants.remove((Croyant)carte);
				Gestionnaire_cartes_partie.addDefausse(carte);
			}
		}
		else if (carte instanceof Guide_Spirituel){
			if(this.guides.contains((Guide_Spirituel)carte)){
				guides.remove((Guide_Spirituel)carte);
				Gestionnaire_cartes_partie.addDefausse(carte);
			}
		}
	}
	
	/**
	 * Transfert une carte de la pioche vers la main du joueur.
	 */
	public void piocherCarte(){
		this.main.add(Gestionnaire_cartes_partie.piocherCarte());
	}
	
	/**
	 * Remplis la main du joueur à 7 cartes. 
	 */
	public void remplirMain(){
		for(int i = this.main.size()-1;i<SIZE_MAX;i++){
			this.piocherCarte();
		}
	}
	
	/** 
	 * Transfert une carte de la main / du champs de bataille pour la pose / le sacrifice
	 * @param carte La carte joué.
	 */
	public void intentionJouerCarte(Carte carte) throws NoTypeException{
		if(main.contains(carte)){
			main.remove(carte);
			if(carte instanceof Croyant || carte instanceof Guide_Spirituel){
				this.pilePose = carte;
			}
			else if(carte instanceof Deus_Ex || carte instanceof Apocalypse){
				this.pileSacrifice = carte;
			}
			else throw new NoTypeException("La carte n'a pas de TYPE ou est NULL.");
		}
		else if(croyants.contains(carte)){
			croyants.remove(carte);
			this.pileSacrifice = carte;
		}
		else if(guides.contains(carte)){
			guides.remove(carte);
			this.pileSacrifice = carte;
		}
		else System.err.println("Le joueur ne possède pas la carte spécifié !");
	}
	
	/**
	 * Tranfert une carte du joueur vers le champs de bataille / la table / la défausse.
	 * @param carte La carte sacrifié.
	 * @throws NoTypeException 
	 */
	public void transfertCarteJouer(Carte carte) throws NoTypeException{
		//Si la carte est dans la pile de pose.
		if(carte.equals(this.pilePose)){
			if(carte instanceof Croyant){
				//La carte est ajouter à la pile de croyant de la table pour que le joueur n'y ai pas accès.
				Gestionnaire_cartes_partie.addPileTable(this.pilePose);
			}
			else if(carte instanceof Guide_Spirituel){
				//On ajoute la carte au champs de bataille.
				this.guides.add((Guide_Spirituel) this.pilePose);
				//Le guide rassemble les croyants. 
				this.transfertCroyants((Guide_Spirituel) carte, ((Guide_Spirituel)carte).ammenerCroyants());
			}
			this.pilePose = null;
		}
		else if(carte.equals(this.pileSacrifice)){
			Gestionnaire_cartes_partie.addDefausse(carte);
			this.pileSacrifice = null;
		}		
		else throw new NoTypeException("Il n'y a pas de carte dans la pile.");
	}
	
	public void remettreSurChampsDeBataille(Croyant carte){
		this.croyants.remove(carte);
		Gestionnaire_cartes_partie.addDefausse(carte);
	}
	
	
	/**
	 * Permet au joueur de savoir quels cartes il peut jouer en fonction de ses points d'action. 
	 * @return Une sous liste de main. 
	 */
	public List<Carte> cartesJouables(Map<Origine,Integer> pointsAction){
		List<Carte> jouables = new ArrayList<Carte>();
		for(int i=0;i<main.size();i++){
			if(isJouable(main.get(i), pointsAction)) jouables.add(main.get(i));
		}
		return jouables;
	}
	
	/**
	 * Permet au joueur de savoir s'il peut jouer une carte. 
	 * @param carte La carte que le joueur souhaite jouer.
	 * @param pointsAction Les points d'action du joueur.
	 * @return Vrai si la carte peut être jouer, faux sinon. 
	 */
	public boolean isJouable(Carte carte, Map<Origine,Integer> pointsAction){
		//Si la carte est d'origine 
		if(carte.getOrigine().equals(null)) return true;
		else if(carte.getOrigine().equals(Origine.NEANT) && pointsAction.get(Origine.NEANT) == 0){
			if((pointsAction.get(Origine.JOUR) + pointsAction.get(Origine.NUIT)) > 2) return true;
			else return false;
		}
		else if(pointsAction.get(carte.getOrigine()) > 0) return true;
		else return false;
	}
	
	public Divinite getDivinite() {
		return divinite;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer buf = new StringBuffer();
		buf.append(this.diviniteToString());
		buf.append(this.mainToString());
		buf.append(this.guidesToString());
		buf.append(this.croyantsToString());
		return buf.toString();
	}
	
	public String diviniteToString(){
		StringBuffer buf = new StringBuffer();
		buf.append("\nVotre Divinitée : ");
		buf.append(divinite.toString()).append(ConstanteCarte.BARRE);
		return buf.toString();
	}
	
	public String mainToString(){
		StringBuffer buf = new StringBuffer();
		buf.append("\nCartes en main : ").append(ConstanteCarte.BARRE);
		int count = 1;
		Iterator<Carte> it = main.iterator();
		while(it.hasNext()){
			buf.append(count).append(" - ").append(((Carte)it.next()).toString()).append(ConstanteCarte.PETITEBARRE);
			count++;
		}
		return buf.toString();
	}
	
	public String guidesToString(){
		StringBuffer buf = new StringBuffer();
		buf.append("\nCartes Guide Spirituel devant vous : ").append(ConstanteCarte.BARRE);
		int count = 1;
		Iterator<Guide_Spirituel> it = guides.iterator();
		while(it.hasNext()){
			buf.append(count).append(" - ").append(((Carte)it.next()).toString()).append(ConstanteCarte.PETITEBARRE);
		}
		return buf.toString();
	}
	
	public String croyantsToString(){
		StringBuffer buf = new StringBuffer();
		buf.append("\nCartes Croyant devant vous : ").append(ConstanteCarte.BARRE);
		int count = 1;
		Iterator<Croyant> it = croyants.iterator();
		while(it.hasNext()){
			buf.append(count).append(" - ").append(((Carte)it.next()).toString()).append(ConstanteCarte.PETITEBARRE);
		}
		return buf.toString();
	}
	
}
