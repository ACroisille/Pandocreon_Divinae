package controller;

import java.util.List;

import models.cartes.Carte;
import models.joueur.Joueur;

public interface JoueurCardUpdateListener {
	public void majMain(Joueur joueur, List<Carte> main);
	public void majChampsDeBataille(Joueur joueur, List<Carte> champsDeBataille);
}
