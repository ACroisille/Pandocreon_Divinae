package models.joueur;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import controller.Gestionnaire_cartes_partie;
import controller.NoTypeException;
import models.cartes.Apocalypse;
import models.cartes.Carte;
import models.cartes.Croyant;
import models.cartes.Guide_Spirituel;

public class JoueurReel extends Joueur{
	
	private String nom;
	
	public JoueurReel(String nom){
		super();
		this.nom = nom;
	}
	
	@Override
	public boolean jouer() {
		Scanner scan = new Scanner(System.in);
		System.out.println(this.toString());
		System.out.println(super.gcj.toString());
		//Phase de défausse 
		//this.phaseDefausse(scan);
		//Remplir main
		this.phaseCompleterMain(scan);
		//Jouer carte action
		System.out.println(Gestionnaire_cartes_partie.afficherCartesPartie());
		if(!this.phaseJouerCarteMain(scan)) return false;
		
		//sacrifier carte du champs de bataille.
		System.out.println(super.gcj.champsDeBatailleToString());
		this.phaseSacrificeCarteChampsDeBataille(scan);
		
		return true;
	}
	
	public void phaseDefausse(Scanner scan){
		System.out.println("Souhaitez vous vous défausser d'une partie ou la totalité de votre main ?");
		List<Carte> defausse = new ArrayList<Carte>();
		Carte carte = null;
		do{
			carte = cardPeeker(super.gcj.getMain(), scan);
			if(carte != null){
				if(!defausse.contains(carte)) defausse.add(carte);
			}
		}while(carte != null);
		super.gcj.defausserMain(defausse);	
	}
	
	public void phaseCompleterMain(Scanner scan){
		System.out.println("Souhaitez vous completer votre main ? (y/n)");
		if(this.yesOrNo(scan)) super.gcj.remplirMain();
	}
	
	public boolean phaseJouerCarteMain(Scanner scan){
		System.out.println("Souhaitez vous jouer une carte de votre main ?");
		Carte carte = null;
		do{
			System.out.println("Cartes jouables dans votre main : ");
			System.out.println(super.gcj.cartesJouablesToString(super.gcj.cartesJouables(super.pointsAction,super.gcj.getMain())));
			carte = cardPeeker(super.gcj.cartesJouables(super.pointsAction,super.gcj.getMain()), scan);
			if(carte != null){
				//La carte est joué
				try {
					super.jouerCarteMain(carte);
					if(carte instanceof Apocalypse) return false;
				} catch (NoTypeException e) {
					e.printStackTrace();
				}
			}
		}while(carte != null && super.gcj.cartesJouables(super.pointsAction,super.gcj.getMain()).size() > 0);
		return true;
	}

	public void phaseSacrificeCarteChampsDeBataille(Scanner scan){
		System.out.println("Souhaitez vous sacrifier une carte du champs de bataille ?");
		Carte carte = null;		
		do{
			System.out.println("Cartes sacrifiables sur le champs de bataille : ");
			System.out.println(super.gcj.cartesJouablesToString(super.gcj.cartesJouables(super.pointsAction,super.gcj.getChampsDeBataille())));
			carte = cardPeeker(super.gcj.cartesJouables(super.pointsAction,super.gcj.getChampsDeBataille()), scan);
			if(carte != null){
				//La carte est joué
				try {
					super.sacrifierCarteChampsDeBataille(carte);
				} catch (NoTypeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}while(carte != null && super.gcj.cartesJouables(super.pointsAction,super.gcj.getMain()).size() > 0);
	}
	public Carte cardPeeker(List<Carte> cartes,Scanner scan){
		System.out.println("Quel carte voulez vous sélectionner ? (0) pour arrêter.");
		String input;
		do{
			input = scan.nextLine();
		}while(!input.matches("[0-9]+"));
		Integer index = Integer.parseInt(input);
		if(index == 0) return null;
		else return cartes.get(index - 1);
	}
	
	public Boolean yesOrNo(Scanner scan){
		String input=null;
		do{
			input = scan.nextLine();
		}while(!input.equals("y") && !input.equals("n") && !input.equals("Y") && !input.equals("N"));
		if(input.equals("y") || input.equals("Y")) return true;
		else if(input.equals("n") || input.equals("N")) return false;
		else return null;
	}
	
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("\nJoueur Reel : ").append(nom).append("\n");
		buf.append(super.toString());
		return buf.toString();
	}
}
