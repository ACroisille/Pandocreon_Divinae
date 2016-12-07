package controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import models.De_Cosmogonie;
import models.Partie;
import models.cartes.Carte;
import models.cartes.Croyant;
import models.cartes.Dogme;
import models.cartes.Origine;
import models.joueur.Joueur;

public abstract class BuildCapacites {
	
	private static HashMap<String, Capacite> capacites;
	
	public static void loadCapacites(){
		capacites = new HashMap<String,Capacite>();
		
		capacites.put("incrementerOrigine", new Capacite(){
			//Donne au joueur un point d'action de même origine que la carte
			@Override
			public void capacite(Carte carte, Joueur user) {
				user.incrementerPointAction(carte.getOrigine());
			}
		});
		
		capacites.put("empecherSacrificeCroyant", new Capacite() {
			//Empèche un joueur possèdant une divinité dont un dogme est différent de la carte de sacrifié un croyant pendant ce tours.
			@Override
			public void capacite(Carte carte, Joueur user) {
				Set<Joueur> joueurs = new HashSet<Joueur>();
				joueurs.addAll(Partie.getJoueurs());
				joueurs.remove(user);
				//Restreindre la liste de joueurs
				Iterator<Joueur> it = joueurs.iterator();
				while(it.hasNext()){
					Joueur j = it.next();
					boolean add = false;
					if(carte.getOrigine().equals(Origine.JOUR) || carte.getOrigine().equals(Origine.NEANT)){
						if(!j.getGestionnaire_Cartes_Joueur().getDivinite().getSesDogmes().contains(Dogme.NATURE) &&
								!j.getGestionnaire_Cartes_Joueur().getDivinite().getSesDogmes().contains(Dogme.MYSTIQUE)) it.remove();
					}
					else if(carte.getOrigine().equals(Origine.NUIT)){
						if(!j.getGestionnaire_Cartes_Joueur().getDivinite().getSesDogmes().contains(Dogme.HUMAIN) &&
								!j.getGestionnaire_Cartes_Joueur().getDivinite().getSesDogmes().contains(Dogme.MYSTIQUE)) it.remove();
					}
				}
				Joueur cible = user.joueurPeeker(joueurs);
				if(cible != null) cible.getGestionnaire_Cartes_Joueur().setSacrificeCroyant(false);
			}
		});
		
		capacites.put("empecherSacrificeGuide", new Capacite() {
			//Empèche un joueur possèdant une divinité dont un dogme est différent de la carte de sacrifié un guide pendant ce tours.
			@Override
			public void capacite(Carte carte, Joueur user) {
				Set<Joueur> joueurs = new HashSet<Joueur>();
				joueurs.addAll(Partie.getJoueurs());
				joueurs.remove(user);
				//Restreindre la liste de joueurs
				Iterator<Joueur> it = joueurs.iterator();
				while(it.hasNext()){
					Joueur j = it.next();
					boolean add = false;
					if(carte.getOrigine().equals(Origine.JOUR) || carte.getOrigine().equals(Origine.NEANT)){
						if(!j.getGestionnaire_Cartes_Joueur().getDivinite().getSesDogmes().contains(Dogme.CHAOS) &&
								!j.getGestionnaire_Cartes_Joueur().getDivinite().getSesDogmes().contains(Dogme.MYSTIQUE)) it.remove();
					}
					else if(carte.getOrigine().equals(Origine.NUIT)){
						if(!j.getGestionnaire_Cartes_Joueur().getDivinite().getSesDogmes().contains(Dogme.HUMAIN) &&
								!j.getGestionnaire_Cartes_Joueur().getDivinite().getSesDogmes().contains(Dogme.SYMBOLES)) it.remove();
					}
				}
				Joueur cible = user.joueurPeeker(joueurs);
				if(cible != null) cible.getGestionnaire_Cartes_Joueur().setSacrificeGuide(false);
				
			}
		});
		
		capacites.put("piocherCartes", new Capacite() {
			//Permet au joueur de piocher deux cartes au hasard dans la main d'un autre joueur.
			@Override
			public void capacite(Carte carte, Joueur user) {
				Set<Joueur> joueurs = new HashSet<Joueur>();
				joueurs.addAll(Partie.getJoueurs());
				joueurs.remove(user);
				Joueur cible = user.joueurPeeker(joueurs);
				if(cible != null){
					Carte c = null;
					for(int i=0;i<2;i++){
						c= cible.getGestionnaire_Cartes_Joueur().donnerCarte();
						if(c != null) user.getGestionnaire_Cartes_Joueur().addMain(carte);
					}
				}
			}
		});
		
		capacites.put("obligerSacrifierCroyant", new Capacite() {
			
			@Override
			public void capacite(Carte carte, Joueur user) {
				Set<Joueur> joueurs = new HashSet<Joueur>();
				joueurs.addAll(Partie.getJoueurs());
				joueurs.remove(user);
				Joueur cible = user.joueurPeeker(joueurs);
				//La cible doit sacrifier un croyant
				Carte carteaSacrifier = null;
				if(cible != null){
					carteaSacrifier = cible.cardPeeker(cible.getGestionnaire_Cartes_Joueur().getCroyantsChampsDeBataille());
					if(carteaSacrifier != null){
						try {
							cible.sacrifierCarteChampsDeBataille(carteaSacrifier);
						} catch (NoTypeException e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
		
		capacites.put("obligerSacrifierGuide", new Capacite() {
			
			@Override
			public void capacite(Carte carte, Joueur user) {
				Set<Joueur> joueurs = new HashSet<Joueur>();
				joueurs.addAll(Partie.getJoueurs());
				joueurs.remove(user);
				Joueur cible = user.joueurPeeker(joueurs);
				//La cible doit sacrifier un croyant
				Carte carteaSacrifier = null;
				if(cible != null){
					carteaSacrifier = cible.cardPeeker(cible.getGestionnaire_Cartes_Joueur().getGuidesChampsDeBataille());
					if(carteaSacrifier != null){
						try {
							cible.sacrifierCarteChampsDeBataille(carteaSacrifier);
						} catch (NoTypeException e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
		
		capacites.put("faireRevenirGuideMain", new Capacite() {
			
			@Override
			public void capacite(Carte carte, Joueur user) {
				Set<Joueur> joueurs = new HashSet<Joueur>();
				joueurs.addAll(Partie.getJoueurs());
				joueurs.remove(user);
				Joueur cible = user.joueurPeeker(joueurs);
				//La cible doit sacrifier un croyant
				Carte carteAFaireRevenir = null;
				if(cible != null){
					carteAFaireRevenir = cible.cardPeeker(cible.getGestionnaire_Cartes_Joueur().getGuidesChampsDeBataille());
					if(carteAFaireRevenir != null){	
						cible.getGestionnaire_Cartes_Joueur().addMain(carteAFaireRevenir);
					}
				}
			}
		});
		
		capacites.put("relancerDe", new Capacite() {
			@Override
			public void capacite(Carte carte, Joueur user) {
				Origine o = De_Cosmogonie.lancerDe();
				Set<Joueur> joueurs = new HashSet<Joueur>();
				joueurs.addAll(Partie.getJoueurs());
				Iterator<Joueur> it = joueurs.iterator();
				while(it.hasNext()){
					it.next().attribuerPointsAction(o);
				}
			}
		});
		
		
	}
	

	
	public static Capacite getCapacite(String key){
		return  capacites.get(key);
	}
	
}
