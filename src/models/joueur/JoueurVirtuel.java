package models.joueur;

import java.util.List;
import java.util.Set;


import models.cartes.Carte;
import models.enums.Origine;
import models.enums.Retour;
import models.joueur.strategies.Strategy;

public class JoueurVirtuel extends Joueur{
	
	private Integer numJoueur;
	private Strategy strategy;
	
	public JoueurVirtuel(Integer numJoueur,Strategy strategy){
		super();
		this.numJoueur = numJoueur;
		this.strategy = strategy;
	}
	
	@Override
	public Retour jouer() {
		return this.strategy.jouer(this);
	}
	
	@Override
	public Carte repondre(Carte sacrifice) {
		return this.strategy.repondre(this,sacrifice);
	}
	
	@Override
	public Carte cardPeeker(List<Carte> cartes) {
		return this.strategy.cardPeeker(cartes);
	}
	
	@Override
	public Joueur joueurPeeker(Set<Joueur> joueurs) {
		return this.strategy.joueurPeeker(joueurs);
	}
	
	@Override
	public Origine originePeeker() {
		return this.strategy.originePeeker();
	}
	
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("\nJoueur Virtuel : ").append(numJoueur).append("\n");
		buf.append(super.toString());
		return buf.toString();
	}
}
