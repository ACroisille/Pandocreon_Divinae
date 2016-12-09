import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import controller.BuildCartes;
import models.Partie;
import models.cartes.Carte;
import models.joueur.Joueur;
import models.joueur.JoueurReel;
import models.joueur.JoueurVirtuel;
import models.joueur.StrategyNormal;


public class Launcher {

	public static Set<Joueur> setJoueurs(String nomJoueurReel,Integer nombreJoueursVirtuels){
		Set<Joueur> joueurs = new LinkedHashSet<Joueur>();
		if(nomJoueurReel != null){
			joueurs.add(new JoueurReel(nomJoueurReel));
		}
		//joueurs.add(new JoueurReel("joueur2"));
	
		for(int i=0;i<nombreJoueursVirtuels;i++){
			joueurs.add(new JoueurVirtuel(i+1,new StrategyNormal()));
		}
		return joueurs;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Lancement de Pandocréon");
		
		System.out.println("Chargement des cartes...");
		ArrayList<Carte> deck = BuildCartes.getCartes();
		System.out.println("Chargement des joueurs...");
		Set<Joueur> joueurs =  Launcher.setJoueurs(null, 4);
		System.out.println("Lancement d'une partie...");
		new Partie(joueurs, deck);
		
	}
	
	

}
