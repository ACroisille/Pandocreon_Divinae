package models.joueur;

import java.util.Iterator;
import java.util.List;
import java.util.Set;


import models.De_Cosmogonie;
import models.cartes.Carte;
import models.enums.Origine;
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
	public boolean jouer() {
		return this.strategy.jouer(this);
	}
	
	@Override
	public Carte cardPeeker(List<Carte> cartes) {
		if(cartes.size() > 0) return cartes.get(0);
		else return null;
	}
	
	@Override
	public Joueur joueurPeeker(Set<Joueur> joueurs) {
		Iterator<Joueur> it = joueurs.iterator();
		return it.next();
	}
	
	@Override
	public Origine originePeeker() {
		return De_Cosmogonie.lancerDe();
	}
	
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("\nJoueur Virtuel : ").append(numJoueur).append("\n");
		buf.append(super.toString());
		return buf.toString();
	}
}
