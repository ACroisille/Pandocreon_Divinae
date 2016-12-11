package models.joueur.strategies;

import java.util.List;
import java.util.Set;

import models.cartes.Carte;
import models.enums.Origine;
import models.enums.Retour;
import models.joueur.Joueur;

public interface Strategy {
	public Retour jouer(Joueur joueur);
	public void phaseDefausse(Joueur joueur);
	public void phaseCompleterMain(Joueur joueur);
	public Retour phaseUtiliserDivinite(Joueur joueur);
	public Retour phaseJouerCarteMain(Joueur joueur);
	public Retour phaseSacrificeCarteChampsDeBataille(Joueur joueur);
	public Carte selectionCarteMain(Joueur joueur, List<Carte> main);
	public Carte repondre(Joueur joueur, Carte sacrifice);
	public Carte cardPeeker(List<Carte> cartes);
	public Joueur joueurPeeker(Set<Joueur> joueurs);
	public Origine originePeeker();
}
