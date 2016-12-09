package models.cartes;

import controller.Capacite;
import models.enums.Origine;
import models.joueur.Joueur;

public class Apocalypse extends Carte{
	
	public Apocalypse(Origine origine){
		super(ConstanteCarte.APOCALYPSE,"",origine,null);
	}
	
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer buf = new StringBuffer();
		buf.append("APOCALYPSE : ").append(this.capaciteDesc).append("\n Origine : ").append(this.origine);
		return buf.toString();
	}
	
}
