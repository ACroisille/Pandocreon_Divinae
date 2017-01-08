package controller.listeners;

import java.util.List;

import models.cartes.Carte;
import models.cartes.Croyant;

public interface PartieCardUpdateListener {
	public void majTable(List<Croyant> table);
	public void majDefausse(Carte carte);
	public void majSacrifice(Carte carte);
}
