package controller.listeners;

import models.joueur.Joueur;

public interface PartieUpdateListener {
	public void showMessageDialog(String msg);
	public void retirerJoueur(Joueur joueur);
}
