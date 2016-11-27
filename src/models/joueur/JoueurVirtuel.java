package models.joueur;

public class JoueurVirtuel extends Joueur{
	
	private Integer numJoueur;
	
	public JoueurVirtuel(Integer numJoueur){
		super();
		this.numJoueur = numJoueur;
	}
	
	@Override
	public boolean jouer() {
		return false;
	}
	
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("\nJoueur Virtuel : ").append(numJoueur).append("\n");
		buf.append(super.toString());
		return buf.toString();
	}
}
