package models.joueur;

public class JoueurReel extends Joueur{
	
	private String nom;
	
	public JoueurReel(String nom){
		super();
		this.nom = nom;
	}
	
	@Override
	public void jouer() {
		// TODO Auto-generated method stub
		super.jouer();
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer buf = new StringBuffer();
		buf.append("\nJoueur Reel : ").append(nom).append("\n");
		buf.append(super.toString());
		return buf.toString();
	}
}
