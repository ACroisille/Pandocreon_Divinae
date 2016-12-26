import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JFrame;

import controller.BuildCartes;
import models.Partie;
import models.cartes.Carte;
import models.joueur.Joueur;
import models.joueur.JoueurReel;
import models.joueur.JoueurVirtuel;
import models.joueur.strategies.StrategyNormal;
import views.CardView;


public class Launcher {

	public static Set<Joueur> setJoueurs(String nomJoueurReel,Integer nombreJoueursVirtuels){
		Set<Joueur> joueurs = new LinkedHashSet<Joueur>();
		if(nomJoueurReel != null){
			joueurs.add(new JoueurReel(nomJoueurReel));
		}
		
		for(int i=0;i<nombreJoueursVirtuels;i++){
			joueurs.add(new JoueurVirtuel(i+1,new StrategyNormal()));
		}
		return joueurs;
	}
	
	public static void main(String[] args) {
		System.out.println("Lancement de Pandocréon");
		
		System.out.println("Chargement des cartes...");
		ArrayList<Carte> deck = BuildCartes.getCartes();
		
		//Debug : carte affichage d'une carte de façon aléatoire
		Collections.shuffle(deck);
		JFrame frame = new JFrame();
		frame.add(new CardView(deck.get(0)));
		frame.pack();
		frame.setVisible(true);
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Voulez vous ajouter un joueur humain ? Si oui entrez son nom, sinon entrer (skip)");
		String input = null; 
		do{
			input =  sc.nextLine();
		}while(input.equals("") && !input.equals("skip"));
		String nomHumain=null;
		if(!input.equals("skip")){
			nomHumain = input;
		}
		
		System.out.println("Combien de joueurs non humain voulez vous ?");
		do{
			input = sc.nextLine();
		}while(!input.matches("[0-9]+") || Integer.parseInt(input) <1 || Integer.parseInt(input) > 4);
		Integer index = Integer.parseInt(input);
		
		System.out.println("Chargement des joueurs...");
		Set<Joueur> joueurs =  Launcher.setJoueurs(nomHumain, index);
		System.out.println("Lancement d'une partie...");
		new Partie(joueurs, deck);
		
	}
	
	

}
