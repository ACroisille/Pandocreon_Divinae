package controller.listeners;

import java.util.List;
import java.util.Set;

import models.cartes.Carte;
import models.joueur.Joueur;

public interface JoueurCardPeekerListener {
	public void afficherMessage(String msg);
	public Carte cardPeekerMain(List<Carte> cartes);
	public Carte cardPeekerChampsDeBataille(List<Carte> cartes);
	public Joueur joueurPeeker(Set<Joueur> joueurs);
	public boolean yesOrNo();
}
