package controller;

import java.util.EventListener;

import models.cartes.Carte;
import models.enums.Retour;

public interface SacrificeListener extends EventListener{
	Retour enReponse(Carte sacrifice);
}
