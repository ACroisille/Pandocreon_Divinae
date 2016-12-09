package models.joueur;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import controller.Gestionnaire_cartes_partie;
import controller.NoTypeException;
import models.cartes.Apocalypse;
import models.cartes.Carte;
import models.cartes.Croyant;
import models.cartes.Guide_Spirituel;
import models.cartes.Origine;

public class JoueurReel extends Joueur{
	
	private String nom;
	private Scanner scan;
	
	public JoueurReel(String nom){
		super();
		this.nom = nom;
		this.scan = new Scanner(System.in);
	}
	
	@Override
	public boolean jouer() {
		System.out.println(this.toString());
		//System.out.println(super.gcj.toString());
		//Phase de défausse 
		this.phaseDefausse();
		//Remplir main
		this.phaseCompleterMain();
		//Jouer carte action
		System.out.println(Gestionnaire_cartes_partie.afficherCartesPartie());
		if(!this.phaseJouerCarteMain()) return false;
		
		//sacrifier carte du champs de bataille.
		System.out.println(super.gcj.champsDeBatailleToString());
		if(!this.phaseSacrificeCarteChampsDeBataille()) return false;
		
		return true;
	}
	
	public void phaseDefausse(){
		System.out.println("Souhaitez vous vous défausser d'une partie ou la totalité de votre main ?");
		Carte carte = null;
		do{
			carte = cardPeeker(super.gcj.getMain());
			if(carte != null){
				super.getGestionnaire_Cartes_Joueur().defausserMain(carte);
			}
		}while(carte != null);
		
	}
	
	public void phaseCompleterMain(){
		System.out.println("Souhaitez vous completer votre main ? (y/n)");
		if(this.yesOrNo(this.scan)) super.gcj.remplirMain();
	}
	
	public boolean phaseJouerCarteMain(){
		System.out.println("Souhaitez vous jouer une carte de votre main ?");
		Carte carte = null;
		do{
			
			carte = cardPeeker(super.gcj.cartesJouables(super.pointsAction,super.gcj.getMain()));
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

	public boolean phaseSacrificeCarteChampsDeBataille(){
		System.out.println("Souhaitez vous sacrifier une carte du champs de bataille ?");
		Carte carte = null;		
		do{
			System.out.println("Cartes sacrifiables sur le champs de bataille : ");
			System.out.println(super.gcj.cartesJouablesToString(super.gcj.cartesJouables(super.pointsAction,super.gcj.getChampsDeBataille())));
			carte = cardPeeker(super.gcj.cartesJouables(super.pointsAction,super.gcj.getChampsDeBataille()));
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
		return true;
	}
	
	@Override
	public Carte cardPeeker(List<Carte> cartes) {
		System.out.println("Quel carte voulez vous sélectionner ? (0) pour arrêter.");
		System.out.println("Cartes selectionnables : ");
		System.out.println(super.gcj.cartesJouablesToString(cartes));
		String input;
		do{
			input = this.scan.nextLine();
		}while(!input.matches("[0-9]+") || Integer.parseInt(input) <0 || Integer.parseInt(input) > cartes.size());
		Integer index = Integer.parseInt(input);
		if(index == 0) return null;
		else return cartes.get(index - 1);
	}
	
	@Override
	public Joueur joueurPeeker(Set<Joueur> joueurs) {
		Iterator<Joueur> it = joueurs.iterator();
		Joueur j = null;
		do{
			while(it.hasNext()){
				j = it.next();
				System.out.println(j.toString());
				System.out.println(j.getGestionnaire_Cartes_Joueur().champsDeBatailleToString());
				System.out.println("Voulez vous cibler ce joueur ? (y/n)");
				if(this.yesOrNo(this.scan)) return j;
				j=null;
			}
		}while(j.equals(null));
		return null;
	}
	
	@Override
	public Origine originePeeker() {
		List<Origine> origines = new ArrayList<Origine>();
		origines.add(Origine.JOUR);
		origines.add(Origine.NUIT);
		origines.add(Origine.NEANT);
		
		Iterator<Origine> it = origines.iterator();
		int count = 1;
		while(it.hasNext()){
			System.out.println(count + " " + it.next());
		}
		System.out.println("Quel origine souhaitez vous sélectionner ? ");
		String input;
		do{
			input = this.scan.nextLine();
		}while(!input.matches("[0-9]+") || Integer.parseInt(input) <1 || Integer.parseInt(input) > 3);
		return origines.get(Integer.parseInt(input));
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
