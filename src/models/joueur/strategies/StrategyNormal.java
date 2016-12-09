package models.joueur.strategies;

import java.util.ArrayList;
import java.util.List;

import controller.Gestionnaire_cartes_partie;
import exceptions.NoTypeException;
import models.Partie;
import models.cartes.Apocalypse;
import models.cartes.Carte;
import models.cartes.Croyant;
import models.cartes.Deus_Ex;
import models.cartes.Guide_Spirituel;
import models.joueur.Joueur;

public class StrategyNormal implements Strategy{
	
	
	
	@Override
	public boolean jouer(Joueur joueur) {
		System.err.println(joueur.toString());
		//Phase de défausse 
		this.phaseDefausse(joueur);
		//Remplir main
		this.phaseCompleterMain(joueur);
		//Jouer carte action
		if(!this.phaseJouerCarteMain(joueur)) return false;
		
		System.out.println(joueur.getGestionnaire_Cartes_Joueur().champsDeBatailleToString());
		
		//sacrifier carte du champs de bataille.
		if(!this.phaseSacrificeCarteChampsDeBataille(joueur)) return false;
		return true;
	}
	
	public void phaseDefausse(Joueur joueur){
		//Retire de sa main toutes les cartes qu'il ne peut pas jouer
		List<Carte> nePasDefausse = joueur.getGestionnaire_Cartes_Joueur().cartesJouables(joueur.getPointsAction(), joueur.getGestionnaire_Cartes_Joueur().getMain());
		List<Carte> defausse = new ArrayList<Carte>();
		for(int i=0;i<joueur.getGestionnaire_Cartes_Joueur().getMain().size();i++){
			if(!nePasDefausse.contains(joueur.getGestionnaire_Cartes_Joueur().getMain().get(i))) defausse.add(joueur.getGestionnaire_Cartes_Joueur().getMain().get(i));
		}
		System.out.println("DEFAUSSE : " + defausse.size());
		joueur.getGestionnaire_Cartes_Joueur().defausserMain(defausse);
	}
	
	public void phaseCompleterMain(Joueur joueur){
		if(joueur.getGestionnaire_Cartes_Joueur().getMain().size() < 7) joueur.getGestionnaire_Cartes_Joueur().remplirMain();
		System.out.println("MAIN : " + joueur.getGestionnaire_Cartes_Joueur().getMain().size());
		System.out.println("PIOCHE : " + Gestionnaire_cartes_partie.getPioche().size());
	}
	
	public boolean phaseJouerCarteMain(Joueur joueur){
		List<Carte> cartesJouables = joueur.getGestionnaire_Cartes_Joueur().cartesJouables(joueur.getPointsAction(), joueur.getGestionnaire_Cartes_Joueur().getMain());
		Carte picked = null;
		do{
			picked = this.selectionCarteMain(joueur, cartesJouables);
			if(picked != null){
				if(picked instanceof Apocalypse) return false;
				else{
					try {
						joueur.jouerCarteMain(picked);
					} catch (NoTypeException e) {
						e.printStackTrace();
					}
					//Pour ne jouer qu'un seul deus ex par tour
					if(picked instanceof Deus_Ex) picked = null;
				}
			}
			cartesJouables = joueur.getGestionnaire_Cartes_Joueur().cartesJouables(joueur.getPointsAction(), joueur.getGestionnaire_Cartes_Joueur().getMain());
		}while(cartesJouables.size() > 0 && picked != null);
		return true;
	}
	
	public boolean phaseSacrificeCarteChampsDeBataille(Joueur joueur){
		//Un joueur virtuel ne sacrifie qu'une seul carte par tour
		List<Carte> cartesJouables = joueur.getGestionnaire_Cartes_Joueur().cartesJouables(joueur.getPointsAction(), joueur.getGestionnaire_Cartes_Joueur().getCroyantsChampsDeBataille());
		if(cartesJouables.size() > 0){
			try {
				joueur.sacrifierCarteChampsDeBataille(cartesJouables.get(0));
			} catch (NoTypeException e) {
				e.printStackTrace();
			}
		}
		else if(joueur.getGestionnaire_Cartes_Joueur().cartesJouables(joueur.getPointsAction(), joueur.getGestionnaire_Cartes_Joueur().getGuidesChampsDeBataille()).size() > 0){
			cartesJouables = joueur.getGestionnaire_Cartes_Joueur().cartesJouables(joueur.getPointsAction(), joueur.getGestionnaire_Cartes_Joueur().getGuidesChampsDeBataille());
			try {
				joueur.sacrifierCarteChampsDeBataille(cartesJouables.get(0));
			} catch (NoTypeException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
	
	
	public Carte selectionCarteMain(Joueur joueur, List<Carte> main){
		for(int i=0;i<main.size();i++){
			if(main.get(i) instanceof Apocalypse && ((Partie.getJoueurs().size() >= 4 && Partie.getJoueurMin() != null && !joueur.equals(Partie.getJoueurMin())) 
					|| (Partie.getJoueurs().size() < 4 && Partie.getJoueurMax()!=null && joueur.equals(Partie.getJoueurMax())))){
				return main.get(i);
			}
			else if(main.get(i) instanceof Croyant && Gestionnaire_cartes_partie.getTable().size()<2){
				return main.get(i);
			}
			else if(main.get(i) instanceof Guide_Spirituel && ((Guide_Spirituel)main.get(i)).croyantsDisponible().size() >= ((Guide_Spirituel)main.get(i)).getNombre()){
				return main.get(i);
			}
			else if(main.get(i) instanceof Deus_Ex){
				boolean contain = false;
				for(int j=0;j<main.size();j++){
					if(main.get(j) instanceof Croyant || main.get(j) instanceof Guide_Spirituel) contain = true;
				}
				if(!contain) return main.get(i);
			}
		}
		//Si aucune carte n'est digne d'être joué, retourne null. 
		return null;
	}
	
	
}
