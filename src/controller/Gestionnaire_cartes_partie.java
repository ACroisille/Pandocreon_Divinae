package controller;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import models.cartes.Carte;
import models.cartes.Croyant;
import models.cartes.Divinite;

public class Gestionnaire_cartes_partie {
	
	private static Queue<Carte> pioche;
	private static List<Carte> defausse;
	private static List<Croyant> table;
	private static List<Croyant> pileTable;
	private static Queue<Divinite> divinitesRestantes;
	
	public Gestionnaire_cartes_partie(List<Carte> deck,Queue<Divinite> divinitesRestantes){
		this.pioche = new LinkedList<Carte>(deck);
		this.defausse = new ArrayList<Carte>();
		this.table = new ArrayList<Croyant>();
		this.pileTable = new ArrayList<Croyant>();
		this.divinitesRestantes = divinitesRestantes;
	}
	
	public static void addDefausse(Carte carte){
		Gestionnaire_cartes_partie.defausse.add(carte);
	}
	
	public static void addTable(Carte carte){
		Gestionnaire_cartes_partie.table.add((Croyant)carte);
	}
	
	public static void addPileTable(Carte carte){
		Gestionnaire_cartes_partie.pileTable.add((Croyant)carte);
	}
	
	/**
	 * Les cartes dans la pile de table sont ajouter � la table pour que le prochain joueur y ai acc�s.
	 */
	public static void joinTable(){
		Gestionnaire_cartes_partie.table.addAll(Gestionnaire_cartes_partie.pileTable);
		Gestionnaire_cartes_partie.pileTable.clear();
	}
	
	public static List<Croyant> getTable(){
		return Gestionnaire_cartes_partie.table;
	}
	
	public static void removeTable(Carte carte){
		Gestionnaire_cartes_partie.table.remove(carte);
	}
	
	public static Carte piocherCarte(){
		return pioche.poll();
	}
	
	
	public static String afficherCartesPartie(){
		StringBuffer buf = new StringBuffer();
		buf.append("\nNombre de cartes dans la pioche : ").append(Gestionnaire_cartes_partie.pioche.size()).append("\n");
		buf.append("Nombre de cartes dans la defausse : ").append(Gestionnaire_cartes_partie.defausse.size()).append("\n\n");
		buf.append("Cartes au centre de la table : \n");
		for(int i=0;i<Gestionnaire_cartes_partie.table.size();i++){
			buf.append(Gestionnaire_cartes_partie.table.get(i).toString()).append("\n");
		}
		return buf.toString();
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer buf = new StringBuffer();
		buf.append("\nNombre de cartes dans la pioche : ").append(this.pioche.size()).append("\n");
		buf.append("Nombre de cartes dans la defausse : ").append(this.defausse.size()).append("\n\n");
		buf.append("Cartes au centre de la table : \n");
		for(int i=0;i<this.table.size();i++){
			buf.append(this.table.get(i).toString()).append("\n");
		}
		return buf.toString();
	}
}
