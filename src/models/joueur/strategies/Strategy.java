package models.joueur.strategies;

import models.enums.Retour;
import models.joueur.Joueur;

public interface Strategy {
	public Retour jouer(Joueur joueur);
}
