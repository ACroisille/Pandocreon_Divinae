package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import models.cartes.Carte;
import models.cartes.ConstanteCarte;
import models.cartes.Croyant;
import models.cartes.Divinite;
import models.cartes.Guide_Spirituel;
import models.joueur.Joueur;

public class Gestionnaire_Cartes_Joueur {
	private Joueur joueur;
	private List<Carte> main;
	private List<Guide_Spirituel> guides;
	private List<Croyant> croyants;
	private Divinite divinite;
	
	public Gestionnaire_Cartes_Joueur(Joueur joueur, List<Carte> main,Divinite divinite){
		this.joueur = joueur;
		this.main = main;
		this.divinite = divinite;
		this.guides = new ArrayList<Guide_Spirituel>();
		this.croyants = new ArrayList<Croyant>();
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer buf = new StringBuffer();
		buf.append(this.diviniteToString());
		buf.append(this.mainToString());
		buf.append(this.guidesToString());
		buf.append(this.croyantsToString());
		return buf.toString();
	}
	
	public String diviniteToString(){
		StringBuffer buf = new StringBuffer();
		buf.append("\nVotre Divinitée : ");
		buf.append(divinite.toString()).append(ConstanteCarte.BARRE);
		return buf.toString();
	}
	
	public String mainToString(){
		StringBuffer buf = new StringBuffer();
		buf.append("\nCartes en main : ").append(ConstanteCarte.BARRE);
		Iterator<Carte> it = main.iterator();
		while(it.hasNext()){
			buf.append(((Carte)it.next()).toString()).append(ConstanteCarte.PETITEBARRE);
		}
		return buf.toString();
	}
	
	public String guidesToString(){
		StringBuffer buf = new StringBuffer();
		buf.append("\nCartes Guide Spirituel devant vous : ").append(ConstanteCarte.BARRE);
		Iterator<Guide_Spirituel> it = guides.iterator();
		while(it.hasNext()){
			buf.append(((Carte)it.next()).toString()).append(ConstanteCarte.PETITEBARRE);
		}
		return buf.toString();
	}
	
	public String croyantsToString(){
		StringBuffer buf = new StringBuffer();
		buf.append("\nCartes Croyant devant vous : ").append(ConstanteCarte.BARRE);
		Iterator<Croyant> it = croyants.iterator();
		while(it.hasNext()){
			buf.append(((Carte)it.next()).toString()).append(ConstanteCarte.PETITEBARRE);
		}
		return buf.toString();
	}
	
}
