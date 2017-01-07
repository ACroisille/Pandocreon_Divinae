package controller.listeners;

import java.util.List;

import models.cartes.Carte;

public interface JoueurCardPeekerListener {
	public void afficherMessage(String msg);
	public Carte cardPeekerMain(List<Carte> cartes);
	public Carte cardPeekerChampsDeBataille(List<Carte> cartes);
	public boolean yesOrNo();
}
