package models.joueur;

public class JoueurVirtuel extends Joueur{
	
	private Integer numJoueur;
	
	public JoueurVirtuel(Integer numJoueur){
		super();
		this.numJoueur = numJoueur;
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
		buf.append("\nJoueur Virtuel : ").append(numJoueur).append("\n");
		buf.append(super.toString());
		return buf.toString();
	}
}
