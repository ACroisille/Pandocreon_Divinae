package controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import controller.listeners.JoueurCardUpdateListener;
import controller.listeners.SacrificeListener;
import exceptions.NoTypeException;
import models.Partie;
import models.cartes.Apocalypse;
import models.cartes.Carte;
import models.cartes.ConstanteCarte;
import models.cartes.Croyant;
import models.cartes.Deus_Ex;
import models.cartes.Divinite;
import models.cartes.Guide_Spirituel;
import models.enums.Origine;
import models.enums.Retour;
import models.joueur.Joueur;

public class Gestionnaire_Cartes_Joueur {
	
	public static final int SIZE_MAX = 7;
	
	private Joueur joueur;
	private List<Carte> main;
	private List<Carte> champsDeBataille;
	private Divinite divinite;
	private Carte pilePose=null;
	
	private Boolean sacrificeCroyant = true; 
	private Boolean	sacrificeGuide = true;
	
	private SacrificeListener sacrificeListener;
	private JoueurCardUpdateListener joueurCardUpdateListener;
	
	public Gestionnaire_Cartes_Joueur(Joueur joueur, List<Carte> main,Divinite divinite){
		this.joueur = joueur;
		this.main = main;
		this.divinite = divinite;
		this.champsDeBataille = new ArrayList<Carte>();
	}
	
	/**
	 * Transfert les croyants ralliés par un guide spirituel de la table vers le champ de bataille. 
	 * @param guide
	 * @param croyants
	 */
	public void transfertCroyants(Guide_Spirituel guide,List<Carte> croyants){
		this.champsDeBataille.addAll(croyants);
		//Mise à jours de l'UI
		this.joueurCardUpdateListener.majChampsDeBataille(this.joueur, champsDeBataille);
		for(int i=0;i<croyants.size();i++){
			Gestionnaire_cartes_partie.removeTable(croyants.get(i));
		}
	}
	
	/**
	 * Transfert une partie ou la totalité des cartes de la main du joueur dans la défausse. .
	 * Ajoute la liste de carte au Gestionnaire Carte Partie
	 */
	public void defausserMain(List<Carte> defausse){
		for(int i = 0; i < defausse.size(); i++){
			this.main.remove(defausse.get(i));
			Gestionnaire_cartes_partie.addDefausse(defausse.get(i));
		}
		//Mise à jours de l'UI
		this.joueurCardUpdateListener.majMain(this.joueur, main);
	}
	
	public void defausserMain(Carte carte){
		this.main.remove(carte);
		//Mise à jours de l'UI
		this.joueurCardUpdateListener.majMain(this.joueur, main);
		Gestionnaire_cartes_partie.addDefausse(carte);
	}
	
	/**
	 * Transfert une carte de la pioche vers la main du joueur.
	 */
	public void piocherCarte(){
		this.main.add(Gestionnaire_cartes_partie.piocherCarte());
		//Mise à jours de l'UI
		this.joueurCardUpdateListener.majMain(this.joueur, main);
	}
	
	/**
	 * Remplis la main du joueur à 7 cartes. 
	 */
	public void remplirMain(){
		for(int i = this.main.size()-1;i<SIZE_MAX-1;i++){
			this.piocherCarte();
		}
	}
	
	/**
	 * Donne une carte (au hasard).
	 * @return Carte
	 */
	public Carte donnerCarte(){
		if(main.size()>0){
			//Mise à jours de l'UI
			this.joueurCardUpdateListener.majMain(this.joueur, main);
			return main.remove(0);
		}
		else return null;
	}
	
	/** 
	 * Transfert une carte de la main / du champs de bataille pour la pose / le sacrifice
	 * @param carte La carte joué.
	 */
	public Retour intentionJouerCarte(Carte carte) throws NoTypeException{
		Retour ret = Retour.CONTINUE;
		if(main.contains(carte)){
			main.remove(carte);
			//Mise à jours de l'UI
			this.joueurCardUpdateListener.majMain(this.joueur, main);
			if(carte instanceof Croyant || carte instanceof Guide_Spirituel){
				System.out.println("\nUne carte"+carte.getClass().getName()+" a été ajouté à la pile de pose :");
				System.out.println(carte.toString());
				
				this.pilePose = carte;
			}
			else if(carte instanceof Deus_Ex || carte instanceof Apocalypse){
				System.out.println("\nUne carte "+carte.getClass().getName()+" a été ajouté à la pile de sacrifice :");
				System.out.println(carte.toString());
				
				//this.pileSacrifice = carte;
				Partie.pileSacrifice.push(carte);
				
				ret = this.sacrificeListener.enReponse(carte);
			}
			else throw new NoTypeException("La carte n'a pas de TYPE ou est NULL.");
		}
		else if(champsDeBataille.contains(carte)){
			champsDeBataille.remove(carte);
			//Mise à jours de l'UI
			this.joueurCardUpdateListener.majChampsDeBataille(this.joueur, champsDeBataille);
			System.out.println("\nUne carte "+carte.getClass().getName()+" a été ajouté à la pile de sacrifice :");
			System.out.println(carte.toString());
			
			Partie.pileSacrifice.push(carte);
			
			ret = this.sacrificeListener.enReponse(carte);
		}
		else{
			System.err.println("Le joueur ne possède pas la carte spécifié !");
		}
		return ret;
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
				this.champsDeBataille.add(this.pilePose);
				//Mise à jours de l'UI
				this.joueurCardUpdateListener.majChampsDeBataille(this.joueur, champsDeBataille);
				//Le guide rassemble les croyants. 
				this.transfertCroyants((Guide_Spirituel) carte, ((Guide_Spirituel)carte).ammenerCroyants());
			}
			this.pilePose = null;
		}
		else if(carte.equals(Partie.pileSacrifice.peek())){
			Gestionnaire_cartes_partie.addDefausse(carte);
			Partie.pileSacrifice.pop();
		}		
		else throw new NoTypeException("Il n'y a pas de carte dans la pile.");
	}
	
	/**
	 * Permet au joueur de savoir quels cartes il peut jouer en fonction de ses points d'action. 
	 * @return Une sous liste de main. 
	 */
	public List<Carte> cartesJouables(Map<Origine,Integer> pointsAction,List<Carte> list){
		List<Carte> jouables = new ArrayList<Carte>();
		for(int i=0;i<list.size();i++){
			if(isJouable(list.get(i), pointsAction)) jouables.add(list.get(i));
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
		if(carte.getOrigine() == null) return true;
		else if(carte.getOrigine().equals(Origine.NEANT) && pointsAction.get(Origine.NEANT) == 0){
			if((pointsAction.get(Origine.JOUR) + pointsAction.get(Origine.NUIT)) > 2) return true;
			else return false;
		}
		else if(pointsAction.get(carte.getOrigine()) > 0) return true;
		else{
			//System.err.println("Impossible de jouer la carte pour une raison inconnue");
			return false;
		}
	}
	
	public boolean isSacrifiable(Carte carte){
		if(carte instanceof Croyant && this.sacrificeCroyant.equals(false)) return false;
		else if(carte instanceof Guide_Spirituel && this.sacrificeGuide.equals(false)) return false;
		else return true;
	}
	/**
	 * Compte le nombre de points de prières d'un joueur.
	 * @return Son nombre de points de prières. 
	 */
	public int compterPointsPriere(){
		int totale = 0;
		for(int i=0;i<this.champsDeBataille.size();i++){
			if(this.champsDeBataille.get(i) instanceof Croyant) totale += ((Croyant)this.champsDeBataille.get(i)).getPointsPriere();
		}
		return totale;
	}
	
	public Divinite getDivinite() {
		return divinite;
	}
	
	public List<Carte> getChampsDeBataille() {
		return this.champsDeBataille;
	}
	public List<Carte> getCroyantsChampsDeBataille(){
		List<Carte> array = new ArrayList<Carte>();
		for(int i=0;i<this.champsDeBataille.size();i++){
			if(this.champsDeBataille.get(i) instanceof Croyant) array.add(this.champsDeBataille.get(i));
		}
		return array;
	}
	public List<Carte> getGuidesChampsDeBataille(){
		List<Carte> array = new ArrayList<Carte>();
		for(int i=0;i<this.champsDeBataille.size();i++){
			if(this.champsDeBataille.get(i) instanceof Guide_Spirituel) array.add(this.champsDeBataille.get(i));
		}
		return array;
	}
	
	public List<Carte> getCartesReponse(){
		List<Carte> reponse = new ArrayList<Carte>();
		for(int i=0;i<this.main.size();i++){
			if(this.main.get(i).getOrigine() == null){
				reponse.add(this.main.get(i));
			}
		}
		return reponse;
	}
	
	public void addMain(Carte carte){
		if(this.champsDeBataille.contains(carte)){
			this.gererDependances(carte);
			//La carte est retiré du champs de bataille
			this.champsDeBataille.remove(carte);
		}
		this.main.add(carte);
		
		//Mise à jours de l'UI
		this.joueurCardUpdateListener.majMain(this.joueur, main);
	}
	
	public void defausserChampsDeBataille(Carte carte){
		this.gererDependances(carte);
		this.champsDeBataille.remove(carte);
		//Mise à jours de l'UI
		this.joueurCardUpdateListener.majChampsDeBataille(this.joueur, champsDeBataille);
		Gestionnaire_cartes_partie.addDefausse(carte);
	}
	
	/**
	 * Gere les dépendances entre croyants et guides 
	 * @param carte
	 */
	public void gererDependances(Carte carte){
		if(carte instanceof Guide_Spirituel){
			//Defausse le guide et remet ses croyants sur la table
			for(int i=0; i < ((Guide_Spirituel)carte).getSesCroyants().size();i++){
					this.remettreSurTable((Croyant)((Guide_Spirituel)carte).getSesCroyants().get(i));
			}
			//Les croyants sont dissociés du guide
			((Guide_Spirituel)carte).libererCroyants();
		}
		else if(carte instanceof Croyant){
			//Defausse le croyant, si il était le dernier son guide est défausser
			if(((Croyant)carte).getGuide().getSesCroyants().size() == 1){
				//Le croyant est le dernier, le guide est donc défaussé
				this.champsDeBataille.remove(((Croyant)carte).getGuide());
				defausserChampsDeBataille(((Croyant)carte).getGuide());
				//Même s'il ne reste qu'un seul croyant rattacher au guide, il faut le libèrer.
				//if(((Croyant)carte).getGuide() != null) ((Croyant)carte).getGuide().libererCroyants();
			}
		}
	}
	
	/**
	 * Remet un croyant au centre de la table.
	 * @param carte La carte croyant à remettre au centre de la table.
	 * @throws NoTypeException
	 */
	public void remettreSurTable(Croyant carte){
		this.champsDeBataille.remove(carte);
		//Mise à jours de l'UI
		this.joueurCardUpdateListener.majChampsDeBataille(this.joueur, champsDeBataille);
		Gestionnaire_cartes_partie.addTable(carte);
	}
	
	public List<Carte> getMain() {
		return this.main;
	}
	
	public Boolean getSacrificeCroyant() {
		return this.sacrificeCroyant;
	}
	public Boolean getSacrificeGuide() {
		return this.sacrificeGuide;
	}
	public void setSacrificeCroyant(Boolean sacrificeCroyant) {
		this.sacrificeCroyant = sacrificeCroyant;
	}
	
	public void setSacrificeGuide(Boolean sacrificeGuide) {
		this.sacrificeGuide = sacrificeGuide;
	}
	
	public void addSacrificeListener(SacrificeListener sacrificeListener){
		this.sacrificeListener = sacrificeListener;
	}
	
	public void addListCartesListener(JoueurCardUpdateListener joueurCardUpdateListener){
		this.joueurCardUpdateListener = joueurCardUpdateListener;
	}
	
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append(this.diviniteToString());
		buf.append(this.mainToString());
		buf.append(this.champsDeBatailleToString());
		//buf.append(this.croyantsToString());
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
	
	public String cartesJouablesToString(List<Carte> cartes){
		StringBuffer buf = new StringBuffer();
		buf.append(ConstanteCarte.BARRE);
		int count = 1;
		Iterator<Carte> it = cartes.iterator();
		while(it.hasNext()){
			buf.append(count).append(" - ").append(((Carte)it.next()).toString()).append(ConstanteCarte.PETITEBARRE);
			count++;
		}
		return buf.toString();
	}
	
	public String champsDeBatailleToString(){
		StringBuffer buf = new StringBuffer();
		buf.append("\nCartes sur le champs de bataille : ").append(ConstanteCarte.BARRE);
		int count = 1;
		Iterator<Carte> it = champsDeBataille.iterator();
		while(it.hasNext()){
			buf.append(count).append(" - ").append((it.next()).toString()).append(ConstanteCarte.PETITEBARRE);
		}
		return buf.toString();
	}
	/*
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
	*/
}
