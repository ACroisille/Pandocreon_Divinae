package models.joueur;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controller.Gestionnaire_Cartes_Joueur;
import models.cartes.Carte;
import models.cartes.ConstanteCarte;
import models.cartes.Divinite;
import models.cartes.Origine;

public abstract class Joueur {
	protected Map<Origine,Integer> pointsAction;
	protected Gestionnaire_Cartes_Joueur gcj = null;

	public Joueur(){
		this.pointsAction = new HashMap<Origine,Integer>();
	}
	
	public void attachGestionnaire_Cartes_Joueur(List<Carte> main, Divinite divinite){
		this.gcj = new Gestionnaire_Cartes_Joueur(this, main, divinite);
	}
	
	public Gestionnaire_Cartes_Joueur getGestionnaire_Cartes_Joueur(){
		return this.gcj;
	}
	public void jouer(){};
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer buf = new StringBuffer();
		buf.append("Points d'action : ").append(pointsAction.toString()).append(ConstanteCarte.BARRE);
		buf.append(gcj.toString()).append("\n");
		return buf.toString();
	}
}
