package models.cartes;

import controller.Capacite;
import models.enums.Origine;
import models.joueur.Joueur;

public class Deus_Ex extends Carte{

	public Deus_Ex(String nom,String capaciteDesc,Origine origine,Capacite capacite) {
		super(nom,capaciteDesc,origine,capacite);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer buf = new StringBuffer();
		buf.append("DEUS EX : ").append(this.nom).append("\n").append(this.capaciteDesc).append("\n Origine : ").append(this.origine);
		return buf.toString();
	}
}
