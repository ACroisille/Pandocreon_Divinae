package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import controller.listeners.PartieCardUpdateListener;
import models.Partie;
import models.cartes.Carte;
import models.cartes.ConstanteCarte;
import models.cartes.Croyant;
import models.cartes.Divinite;

public class Gestionnaire_cartes_partie {
	
	private static Queue<Carte> pioche;
	private static List<Carte> defausse;
	private static List<Croyant> table;
	private static List<Croyant> pileTable;
	private static Queue<Divinite> divinitesRestantes;
	private static Stack<Carte> pileSacrifice;
	
	private static PartieCardUpdateListener partieCardUpdateListener;
	
	public Gestionnaire_cartes_partie(List<Carte> deck,Queue<Divinite> divinitesRestantes){
		Gestionnaire_cartes_partie.pioche = new LinkedList<Carte>(deck);
		Gestionnaire_cartes_partie.defausse = new ArrayList<Carte>();
		Gestionnaire_cartes_partie.table = new ArrayList<Croyant>();
		Gestionnaire_cartes_partie.pileTable = new ArrayList<Croyant>();
		Gestionnaire_cartes_partie.divinitesRestantes = divinitesRestantes;
		Gestionnaire_cartes_partie.pileSacrifice = new Stack<Carte>();
	}
	
	public static void addDefausse(Carte carte){
		Gestionnaire_cartes_partie.defausse.add(carte);
		partieCardUpdateListener.majDefausse(carte);
	}
	
	public static void addTable(Carte carte){
		Gestionnaire_cartes_partie.table.add((Croyant)carte);
		partieCardUpdateListener.majTable(Gestionnaire_cartes_partie.table);
	}
	
	public static void addPileTable(Carte carte){
		Gestionnaire_cartes_partie.pileTable.add((Croyant)carte);
	}
	
	/**
	 * Les cartes dans la pile de table sont ajouter à la table pour que le prochain joueur y ai accès.
	 */
	public static void joinTable(){
		Gestionnaire_cartes_partie.table.addAll(Gestionnaire_cartes_partie.pileTable);
		Gestionnaire_cartes_partie.pileTable.clear();
		partieCardUpdateListener.majTable(Gestionnaire_cartes_partie.table);
	}
	
	public static List<Croyant> getTable(){
		return Gestionnaire_cartes_partie.table;
	}
	
	public static Queue<Carte> getPioche() {
		return pioche;
	}
	
	public static void removeTable(Carte carte){
		Gestionnaire_cartes_partie.table.remove(carte);
		partieCardUpdateListener.majTable(Gestionnaire_cartes_partie.table);
	}
	
	public static Carte piocherCarte(){
		if(pioche.peek() == null){
			//On reremplis la pioche à partir de la defausse
			//Mélange de la defausse
			Collections.shuffle(defausse);
			pioche.addAll(defausse);
			defausse.clear();
		}
		return pioche.poll();
	}
	
	public static List<Carte> getDefausse() {
		return defausse;
	}
	
	public static void addPileSacrifice(Carte sacrifice){
		//Ajout d'une carte à la pile de sacrifices
		Gestionnaire_cartes_partie.pileSacrifice.push(sacrifice);
		partieCardUpdateListener.majSacrifice(sacrifice);
	}
	
	public static Carte peekPileSacrifice(){
		if(Gestionnaire_cartes_partie.pileSacrifice.size() > 0){
			return Gestionnaire_cartes_partie.pileSacrifice.peek();
		}else{
			partieCardUpdateListener.majSacrifice(null);
			return null;
		}
	}
	
	public static Carte popPileSacrifice(){
		partieCardUpdateListener.majSacrifice(Gestionnaire_cartes_partie.peekPileSacrifice());
		Carte c = Gestionnaire_cartes_partie.pileSacrifice.pop();
		if(Gestionnaire_cartes_partie.pileSacrifice.size() == 0){
			partieCardUpdateListener.majSacrifice(null);
		}
		return c;
	}
	
	public static Carte getLast(){
		Carte wanted = null;
		if(Gestionnaire_cartes_partie.pileSacrifice.size()>=2){
			Carte back = Gestionnaire_cartes_partie.pileSacrifice.pop();
			wanted = Gestionnaire_cartes_partie.pileSacrifice.peek();
			Gestionnaire_cartes_partie.pileSacrifice.push(back);
		}
		return wanted;
	}
	
	public static int getPileSacrificeSize(){
		return Gestionnaire_cartes_partie.pileSacrifice.size();
	}
	
	public static void addPartieCardUpdateListener(PartieCardUpdateListener partieCardUpdateListener){
		Gestionnaire_cartes_partie.partieCardUpdateListener = partieCardUpdateListener;
	}
	
	public static String afficherCartesPartie(){
		StringBuffer buf = new StringBuffer();
		//buf.append("\nNombre de cartes dans la pioche : ").append(Gestionnaire_cartes_partie.pioche.size()).append("\n");
		//buf.append("Nombre de cartes dans la defausse : ").append(Gestionnaire_cartes_partie.defausse.size()).append("\n\n");
		buf.append("Cartes au centre de la table : \n").append(ConstanteCarte.BARRE);
		for(int i=0;i<Gestionnaire_cartes_partie.table.size();i++){
			buf.append(Gestionnaire_cartes_partie.table.get(i).toString()).append("\n").append(ConstanteCarte.PETITEBARRE).append("\n").append(ConstanteCarte.PETITEBARRE);
		}
		return buf.toString();
	}
	
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		//buf.append("\nNombre de cartes dans la pioche : ").append(this.pioche.size()).append("\n");
		//buf.append("Nombre de cartes dans la defausse : ").append(this.defausse.size()).append("\n\n");
		buf.append("Cartes au centre de la table : \n");
		for(int i=0;i<this.table.size();i++){
			buf.append(this.table.get(i).toString()).append("\n");
		}
		return buf.toString();
	}
}
