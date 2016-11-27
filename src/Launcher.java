import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import models.Partie;
import models.cartes.BuildCartes;
import models.cartes.Carte;
import models.joueur.Joueur;
import models.joueur.JoueurReel;
import models.joueur.JoueurVirtuel;


public class Launcher {

	public static Set<Joueur> setJoueurs(String nomJoueurReel,Integer nombreJoueursVirtuels){
		Set<Joueur> joueurs = new HashSet<Joueur>();
		if(!nomJoueurReel.equals(null)){
			joueurs.add(new JoueurReel(nomJoueurReel));
		}
		for(int i=0;i<nombreJoueursVirtuels;i++){
			joueurs.add(new JoueurVirtuel(i+1));
		}
		return joueurs;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Lancement de Pandocréon");
		
		System.out.println("Chargement des cartes...");
		ArrayList<Carte> deck = BuildCartes.getCartes();
		System.out.println("Chargement des joueurs...");
		Set<Joueur> joueurs =  Launcher.setJoueurs("Antoine", 2);
		System.out.println("Lancement d'une partie...");
		new Partie(joueurs, deck);
		
	}
	
	

}
