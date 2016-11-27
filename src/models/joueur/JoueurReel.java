package models.joueur;



public class JoueurReel extends Joueur{
	
	private String nom;
	
	public JoueurReel(String nom){
		super();
		this.nom = nom;
	}
	
	@Override
	public boolean jouer() {
		System.out.println(super.gcj.toString());
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
