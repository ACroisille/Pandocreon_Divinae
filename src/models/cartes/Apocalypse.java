package models.cartes;

import controller.Capacite;
import models.joueur.Joueur;

public class Apocalypse extends Carte{
	
	private static Capacite capacite = new Capacite() {
		@Override
		public void capacite(Joueur j) {
			// TODO Auto-generated method stub
			
		}
	};
	
	public Apocalypse(Origine origine){
		super(ConstanteCarte.APOCALYPSE,"",origine);
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer buf = new StringBuffer();
		buf.append("APOCALYPSE : ").append(this.capaciteDesc).append(" ").append(this.origine);
		return buf.toString();
	}
	
}
