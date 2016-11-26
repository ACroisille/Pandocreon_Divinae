package models.cartes;

import controller.Capacite;
import models.joueur.Joueur;

public class Deus_Ex extends Carte{

	public Deus_Ex(String nom,String capaciteDesc,Origine origine) {
		super(nom,capaciteDesc,origine);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer buf = new StringBuffer();
		buf.append("DEUS EX : ").append(this.nom).append(" ").append(this.capaciteDesc).append(" ").append(this.origine);
		return buf.toString();
	}
}
