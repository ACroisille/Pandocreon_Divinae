package controller;

import java.util.Set;

import models.cartes.Carte;
import models.enums.Retour;
import models.joueur.Joueur;

public interface Capacite {
	public Retour capacite(Carte carte,Joueur user);
}
