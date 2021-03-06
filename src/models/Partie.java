package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import controller.Gestionnaire_cartes_partie;
import controller.listeners.PartieCardUpdateListener;
import controller.listeners.PartieUpdateListener;
import models.cartes.Carte;
import models.cartes.Divinite;
import models.enums.Origine;
import models.enums.Retour;
import models.joueur.Joueur;
import models.joueur.JoueurReel;
import views.MainFrame;
import views.CardView;
import views.DiceView;

public class Partie {
	
	private static Set<Joueur> joueurs;
	private Gestionnaire_cartes_partie gcp = null;
	public static int numeroTour;
	
	private static PartieUpdateListener partieUpdateListener;
	
	public Partie(Set<Joueur> joueurs, ArrayList<Carte> deck){
		
		Partie.joueurs = joueurs;
		Collections.shuffle(deck);
		//R�cup�ration des divinit�es dans la paquet de carte.
		Queue<Divinite> divinites = this.getDivinites(deck);
		deck.removeAll(divinites);
		
		//Distribution des divinites aux joueurs
		this.distributionCartes(deck, divinites);
		Partie.numeroTour = 1;
		
		new MainFrame(this);
		this.jouerPartie(true);
	}
	
	/**
	 * Lance une partie
	 * Cette m�thode contient la boucle principal
	 */
	public void jouerPartie(boolean jouer){
		while(jouer){
			jouer = this.jouerTour();
			Partie.numeroTour += 1;
		}
		System.out.println("Fin de partie");
	}
	
	private boolean jouerTour(){
		System.err.println("Tour " + numeroTour);
		//Lancer du d�s de cosmogonie 
		Origine o = De_Cosmogonie.lancerDe();
		this.partieUpdateListener.showMessageDialog("Tour num�ros : " + Partie.numeroTour + " Origine : " + o.name());
		distribuerPointsAction(o);
		System.out.println("Distribution des points d'action.");
		Iterator<Joueur> it = joueurs.iterator();
		Retour next;
		while(it.hasNext()){
			next = it.next().jouer();
			Gestionnaire_cartes_partie.joinTable();
			//Si la m�thode jouer renvois faux et qu'il y a moins de 4 joueurs dans la partie, fin de la partie
			if(next.equals(Retour.APOCALYPSE) && joueurs.size()<4){
				//Le joueur ayant le plus de points de pri�re remporte la partie.
				Joueur joueurMax = Partie.getJoueurMax();
				if(joueurMax != null){
					System.err.println(joueurMax.toString() + " emporte la partie !");
					partieUpdateListener.showMessageDialog(joueurMax.toString() + " emporte la partie !");
					return false;
				}else System.out.println("Joueurs � �galit�, la partie continue !");
			}
			//Si la m�thode jouer renvois faux et qu'il y a au moins 4 joueurs dans la partie, 
			else if(next.equals(Retour.APOCALYPSE) && joueurs.size()>=4){
				//�limine un joueur et reset le tours.
				Joueur joueurMin = Partie.getJoueurMin();
				if(joueurMin != null){
					System.err.println(joueurMin.toString() + " est �limin� de la partie !");
					partieUpdateListener.showMessageDialog(joueurMin.toString() + " est �limin� de la partie !");
					Partie.joueurs.remove(joueurMin);
					partieUpdateListener.retirerJoueur(joueurMin);
					this.clear();
					return true;
				}else System.out.println("Joueurs � �galit�, personne n'est �limin�, la partie continue !");
			}
			else if(next.equals(Retour.STOPTOUR)){
				this.clear();
				return true;
			}
		}
		//Le tour de jeu s'est bien d�roul�. Le premier joueur est plac� � la fin de la liste de joueurs.
		this.toTheEnd();
		this.clear();
		return true;
	}
	
	
	
	private void distribuerPointsAction(Origine o){
		Iterator<Joueur> it = joueurs.iterator();
		while(it.hasNext()){
			(it.next()).attribuerPointsAction(o);
		}
	}
	
	/**
	 * Place le premier joueur � la fin de la liste de joueur. 
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
			//Chaque joueur d�marre avec 7 cartes
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
	 * Permet d'obtenir le joueur ayant le plus de points de pri�re de la partie.
	 * @return le joueur ayant le plus de points de pri�re, null si �galit�.
	 */
	public static Joueur getJoueurMax(){
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
	 * Permet d'obtenir le joueur ayant le moins de points de pri�re de la partie.
	 * @return le joueur ayant le moins de points de pri�re, null si �galit�.
	 */
	public static Joueur getJoueurMin(){
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
	
	/**
	 * Remet � true les booleans pour permettre aux joueurs de pouvoir � nouveau sacrifier des cartes.
	 */
	public void clear(){
		Iterator<Joueur> it = Partie.joueurs.iterator();
		Joueur j = null;
		while(it.hasNext()){
			j = it.next();
			j.setIncrementerPointsAction(true);
			j.getGestionnaire_Cartes_Joueur().setSacrificeCroyant(true);
			j.getGestionnaire_Cartes_Joueur().setSacrificeGuide(true);
		}
	}
	
	public static Set<Joueur> getJoueurs() {
		return joueurs;
	}
	
	public static void addPartieUpdateListener(PartieUpdateListener partieUpdateListener){
		Partie.partieUpdateListener = partieUpdateListener;
	}
}
