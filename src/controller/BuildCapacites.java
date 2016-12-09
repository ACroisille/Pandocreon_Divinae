package controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import models.De_Cosmogonie;
import models.Partie;
import models.cartes.Apocalypse;
import models.cartes.Carte;
import models.cartes.Croyant;
import models.cartes.Dogme;
import models.cartes.Guide_Spirituel;
import models.cartes.Origine;
import models.joueur.Joueur;

public abstract class BuildCapacites {
	
	
	
	public static Map<String, Capacite> loadCapacites(){
		Map<String, Capacite> capacites = new HashMap<String,Capacite>();
		//TODO trouver solution pour les cartes sacrifier par obligation.
		capacites.put("osef", new Capacite() {
			
			@Override
			public void capacite(Carte carte, Joueur user) {
				System.err.println("Not implemented yet !");
			}
		});
		
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
				Set<Joueur> joueurs = BuildCapacites.getJoueursPartie(user);
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
				Set<Joueur> joueurs = BuildCapacites.getJoueursPartie(user);
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
				Set<Joueur> joueurs = BuildCapacites.getJoueursPartie(user);
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
		
		capacites.put("obligerSacrificeCroyant", new Capacite() {
			
			@Override
			public void capacite(Carte carte, Joueur user) {
				Set<Joueur> joueurs = BuildCapacites.getJoueursPartie(user);
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
		
		capacites.put("obligerSacrificeGuide", new Capacite() {
			
			@Override
			public void capacite(Carte carte, Joueur user) {
				Set<Joueur> joueurs = BuildCapacites.getJoueursPartie(user);
				//Pour le sacrifice de Devin
				if(carte instanceof Guide_Spirituel){
					Iterator<Joueur> it = joueurs.iterator();
					while(it.hasNext()){
						Joueur j = it.next();
						if(!j.getGestionnaire_Cartes_Joueur().getDivinite().getSesDogmes().contains(Dogme.NATURE) && 
								!j.getGestionnaire_Cartes_Joueur().getDivinite().getSesDogmes().contains(Dogme.MYSTIQUE)){
							it.remove();
						}
					}
				}
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
				Set<Joueur> joueurs = BuildCapacites.getJoueursPartie(user);
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
		
		capacites.put("defausserGuide", new Capacite() {
			
			@Override
			public void capacite(Carte carte, Joueur user) {
				Set<Joueur> joueurs = BuildCapacites.getJoueursPartie(user);
				Joueur cible = user.joueurPeeker(joueurs);
				//La cible doit sacrifier un croyant
				Carte carteaDefausser = null;
				if(cible != null){
					carteaDefausser = cible.cardPeeker(cible.getGestionnaire_Cartes_Joueur().getGuidesChampsDeBataille());
					if(carteaDefausser != null){
						cible.getGestionnaire_Cartes_Joueur().defausserChampsDeBataille(carteaDefausser);;
					}
				}
			}
		});
	
		capacites.put("volerPointsAction", new Capacite() {
			@Override
			public void capacite(Carte carte, Joueur user) {
				Set<Joueur> joueurs = BuildCapacites.getJoueursPartie(user);
				Joueur cible = user.joueurPeeker(joueurs);
				user.getPointsAction().replace(Origine.JOUR, user.getPointsAction().get(Origine.JOUR) + cible.getPointsAction().get(Origine.JOUR));
				user.getPointsAction().replace(Origine.NUIT, user.getPointsAction().get(Origine.NUIT) + cible.getPointsAction().get(Origine.NUIT));
				user.getPointsAction().replace(Origine.NEANT, user.getPointsAction().get(Origine.NEANT) + cible.getPointsAction().get(Origine.NEANT));
			}
		});
		
		capacites.put("activerCapaciteCroyant", new Capacite() {
			
			@Override
			public void capacite(Carte carte, Joueur user) {
				Set<Joueur> joueurs = BuildCapacites.getJoueursPartie(user);
				Joueur cible = user.joueurPeeker(joueurs);
				//La cible doit sacrifier un croyant
				Carte carteaActiver = null;
				if(cible != null){
					carteaActiver = cible.cardPeeker(cible.getGestionnaire_Cartes_Joueur().getCroyantsChampsDeBataille());
					if(carteaActiver != null){
						carteaActiver.getCapacite().capacite(carteaActiver, user);
					}
				}
			}
		});
			
		capacites.put("obligerSacrificeCroyantAll", new Capacite() {
			
			@Override
			public void capacite(Carte carte, Joueur user) {
				Set<Joueur> joueurs = BuildCapacites.getJoueursPartie(user);
				
				Set<Joueur> joueursSelect = new LinkedHashSet<Joueur>();
				Joueur cible = null;
				do{
					cible = user.joueurPeeker(joueurs);
					if(cible != null){
						joueursSelect.add(cible);
						joueurs.remove(cible);
					}
				}while(cible != null);
				
				//La cible doit sacrifier un croyant
				Iterator<Joueur> it = joueursSelect.iterator();
				while(it.hasNext()){
					Joueur j = it.next();
					Carte carteaSacrifier = j.cardPeeker(cible.getGestionnaire_Cartes_Joueur().getCroyantsChampsDeBataille());
					if(carteaSacrifier != null){
						try {
							j.sacrifierCarteChampsDeBataille(carteaSacrifier);
						} catch (NoTypeException e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
		
		capacites.put("empecherIncrementationPointsActionAll", new Capacite() {
			
			@Override
			public void capacite(Carte carte, Joueur user) {
				Set<Joueur> joueurs = new HashSet<Joueur>();
				joueurs.addAll(Partie.getJoueurs());
				Iterator<Joueur> it = joueurs.iterator();
				while(it.hasNext()){
					it.next().setIncrementerPointsAction(false);
				}
			}
		});
			
		capacites.put("martyr", new Capacite() {
			
			@Override
			public void capacite(Carte carte, Joueur user) {
				//TODO trouver solution
				System.out.println("Apocalypse now !");
			}
		});
		
		capacites.put("clerc", new Capacite() {
			
			@Override
			public void capacite(Carte carte, Joueur user) {
				Origine o = user.originePeeker();
				for(int i=0;i<((Guide_Spirituel)carte).getSesCroyants().size();i++){
					user.incrementerPointAction(o);
				}
			}
		});
		
		capacites.put("ascete", new Capacite() {
			
			@Override
			public void capacite(Carte carte, Joueur user) {
				//Réduire aux joueurs ayant le dogme humain ou symbole
				Set<Joueur> joueurs = BuildCapacites.getJoueursPartie(user);
				Iterator<Joueur> it = joueurs.iterator();
				while(it.hasNext()){
					Joueur j = it.next();
					if(!j.getGestionnaire_Cartes_Joueur().getDivinite().getSesDogmes().contains(Dogme.HUMAIN) && 
							!j.getGestionnaire_Cartes_Joueur().getDivinite().getSesDogmes().contains(Dogme.SYMBOLES)){
						it.remove();
					}
				}
				Joueur cible = user.joueurPeeker(joueurs);
				for(int i=0;i<2;i++){
					Carte select = user.cardPeeker(cible.getGestionnaire_Cartes_Joueur().getCroyantsChampsDeBataille());
					try {
						cible.sacrifierCarteChampsDeBataille(carte);
					} catch (NoTypeException e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		capacites.put("exorciste", new Capacite() {
			
			@Override
			public void capacite(Carte carte, Joueur user) {
				System.err.println("Exorciste : non implémenté");
			}
		});
		
		capacites.put("sorcier", new Capacite() {
			
			@Override
			public void capacite(Carte carte, Joueur user) {
				System.err.println("Sorcier : non implémenté");				
			}
		});
	
		capacites.put("tyran", new Capacite() {
			
			@Override
			public void capacite(Carte carte, Joueur user) {
				for(int i=0;i<Gestionnaire_cartes_partie.getTable().size();i++){
					if(Gestionnaire_cartes_partie.getTable().get(i).getDogmes().contains(Dogme.MYSTIQUE)){
						Gestionnaire_cartes_partie.removeTable(carte);
						Gestionnaire_cartes_partie.addDefausse(carte);
					}
				}
			}
		});
	
		capacites.put("messie", new Capacite() {
			
			@Override
			public void capacite(Carte carte, Joueur user) {
				//TODO trouver solution pour finir tour 
				Origine o = user.originePeeker();
				Iterator<Joueur> it = Partie.getJoueurs().iterator();
				while(it.hasNext()){
					it.next().attribuerPointsAction(o);
				}
			}
		});
	
		return capacites;
	}
	
	private static Set<Joueur> getJoueursPartie(Joueur user){
		Set<Joueur> joueurs = new HashSet<Joueur>();
		joueurs.addAll(Partie.getJoueurs());
		joueurs.remove(user);
		return joueurs;
	}

	
}
