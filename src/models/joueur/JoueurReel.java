package models.joueur;

import controller.Gestionnaire_cartes_partie;

public class JoueurReel extends Joueur{
	
	private String nom;
	
	public JoueurReel(String nom){
		super();
		this.nom = nom;
	}
	
	@Override
	public boolean jouer() {
		System.out.println("VOS CARTES : ");
		System.out.println(super.gcj.toString());
		System.out.println("CARTES AU MILIEU : ");
		System.out.println(Gestionnaire_cartes_partie.afficherCartesPartie());
		
		
		return false;
	}
	
	
	
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("\nJoueur Reel : ").append(nom).append("\n");
		buf.append(super.toString());
		return buf.toString();
	}
}
