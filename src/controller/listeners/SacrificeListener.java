package controller.listeners;

import java.util.EventListener;

import models.cartes.Carte;
import models.enums.Retour;

public interface SacrificeListener {
	Retour enReponse(Carte sacrifice);
}
