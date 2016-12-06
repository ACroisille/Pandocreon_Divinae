package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import controller.Gestionnaire_cartes_partie;
import controller.NoTypeException;
import models.cartes.Carte;
import models.cartes.Divinite;
import models.cartes.Origine;
import models.joueur.Joueur;
import models.joueur.JoueurReel;

public class Partie {
	
	private Set<Joueur> joueurs;
	private Gestionnaire_cartes_partie gcp = null;
	
	public Partie(Set<Joueur> joueurs, ArrayList<Carte> deck){
		this.joueurs = joueurs;
		
		Collections.shuffle(deck);
		//Récupèration des divinitées dans la paquet de carte.
		Queue<Divinite> divinites = this.getDivinites(deck);
		deck.removeAll(divinites);
		
		//Distribution des divinites aux joueurs
		this.distributionCartes(deck, divinites);
		
		this.jouerPartie(true);
	}
	
	/**
	 * Lance une partie
	 * Cette méthode contient la boucle principal
	 */
	public void jouerPartie(boolean jouer){
		while(jouer){
			jouer = this.jouerTour();
		}
		System.out.println("Fin de partie");
	}
	
	private boolean jouerTour(){
		//Lancer du dés de cosmogonie 
		distribuerPointsAction(De_Cosmogonie.lancerDe());
		System.out.println("Distribution des points d'action.");
		Iterator<Joueur> it = joueurs.iterator();
		boolean next;
		while(it.hasNext()){
			next = it.next().jouer();
			Gestionnaire_cartes_partie.joinTable();
			//Si la méthode jouer renvois faux et qu'il y a moins de 4 joueurs dans la partie, fin de la partie
			if(!next && joueurs.size()<4){
				//Le joueur ayant le plus de points de prière remporte la partie.
				Joueur joueurMax = this.getJoueurMax();
				if(joueurMax != null){
					System.out.println(joueurMax.toString() + " emporte la partie !");
					return false;
				}else System.out.println("Joueurs à égalité, la partie continue !");
			}
			//Si la méthode jouer renvois faux et qu'il y a au moins 4 joueurs dans la partie, 
			else if(!next && joueurs.size()>=4){
				//élimine un joueur et reset le tours.
				Joueur joueurMin = this.getJoueurMin();
				if(joueurMin != null){
					System.out.println(joueurMin.toString() + " est éliminé de la partie !");
					return true;
				}else System.out.println("Joueurs à égalité, personne n'est éliminé, la partie continue !");
			}
		}
		//Le tour de jeu s'est bien déroulé. Le premier joueur est placé à la fin de la liste de joueurs.
		this.toTheEnd();
		return true;
	}
	
	
	
	private void distribuerPointsAction(Origine o){
		Iterator<Joueur> it = joueurs.iterator();
		while(it.hasNext()){
			(it.next()).attribuerPointsAction(o);
		}
	}
	
	/**
	 * Place le premier joueur à la fin de la liste de joueur. 
	 */
	private void toTheEnd(){
		Iterator<Joueur> it = joueurs.iterator();
		Joueur j = it.next();
		joueurs.remove(j);
		joueurs.add(j);
	}
	
	private void distributionCartes(ArrayList<Carte> deck, Queue<Divinite> divinites){
		Iterator<Joueur> it = joueurs.iterator();
		while(it.hasNext()){
			//Chaque joueur démarre avec 7 cartes
			List<Carte> main =  new ArrayList<Carte>(deck.subList(0, 7));
			deck.removeAll(main);
			(it.next()).attachGestionnaire_Cartes_Joueur(main, divinites.poll());
		}
		this.gcp = new Gestionnaire_cartes_partie(deck, divinites);
	}
	
	private Queue<Divinite> getDivinites(ArrayList<Carte> deck){
		Queue<Divinite> divinites = new LinkedList<Divinite>();
		for(int i=0;i<deck.size();i++){
			if(deck.get(i) instanceof Divinite){
				divinites.add((Divinite)deck.get(i));
			}
		}
		return divinites;
	}
	
	/**
	 * Permet d'obtenir le joueur ayant le plus de points de prière de la partie.
	 * @return le joueur ayant le plus de points de prière, null si égalité.
	 */
	private Joueur getJoueurMax(){
		int max=0;
		Joueur joueurMax = null;
		Iterator<Joueur> it = joueurs.iterator();
		Joueur j = null;
		while(it.hasNext()){
			j = it.next();
			if(j.getGestionnaire_Cartes_Joueur().compterPointsPriere() > max){
				joueurMax = j;
				max = j.getGestionnaire_Cartes_Joueur().compterPointsPriere();
			}
		}
		it = joueurs.iterator();
		while(it.hasNext()){
			j = it.next();
			if(j.getGestionnaire_Cartes_Joueur().compterPointsPriere() == max && !j.equals(joueurMax)) return null;
		}
		return joueurMax;
	}
	
	/**
	 * Permet d'obtenir le joueur ayant le moins de points de prière de la partie.
	 * @return le joueur ayant le moins de points de prière, null si égalité.
	 */
	private Joueur getJoueurMin(){
		int min=10000;
		Joueur joueurMin = null;
		Iterator<Joueur> it = joueurs.iterator();
		Joueur j = null;
		while(it.hasNext()){
			j = it.next();
			if(j.getGestionnaire_Cartes_Joueur().compterPointsPriere() < min){
				joueurMin = j;
				min = j.getGestionnaire_Cartes_Joueur().compterPointsPriere();
			}
		}
		it = joueurs.iterator();
		while(it.hasNext()){
			j = it.next();
			if(j.getGestionnaire_Cartes_Joueur().compterPointsPriere() == min && !j.equals(joueurMin)) return null;
		}
		return joueurMin;
	}
	
}
