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

import exceptions.DependencyException;
import exceptions.NoTypeException;
import models.De_Cosmogonie;
import models.Partie;
import models.cartes.Apocalypse;
import models.cartes.Carte;
import models.cartes.Croyant;
import models.cartes.Deus_Ex;
import models.cartes.Guide_Spirituel;
import models.enums.Dogme;
import models.enums.Origine;
import models.enums.Retour;
import models.joueur.Joueur;

public abstract class BuildCapacites {
	
	
	
	public static Map<String, Capacite> loadCapacites(){
		Map<String, Capacite> capacites = new HashMap<String,Capacite>();
		//TODO trouver solution pour les cartes sacrifier par obligation.
		capacites.put("osef", new Capacite() {
			
			@Override
			public Retour capacite(Carte carte, Joueur user) {
				System.err.println("Not implemented yet !");
				return Retour.CONTINUE;
			}
		});
		
		capacites.put("apocalypse", new Capacite() {
			
			@Override
			public Retour capacite(Carte carte, Joueur user) {
				
				//Retire toutes les immunit�es 
				Set<Joueur> joueurs = Partie.getJoueurs();
				Iterator<Joueur> it = joueurs.iterator();
				while(it.hasNext()){
					Joueur j = it.next();
					for(int i=0;i<j.getGestionnaire_Cartes_Joueur().getChampsDeBataille().size();i++){
						if(j.getGestionnaire_Cartes_Joueur().getChampsDeBataille().get(i).getImmunite().equals(true)){
							j.getGestionnaire_Cartes_Joueur().getChampsDeBataille().get(i).setImmunite(false);
						}
					}
				}
				System.err.println("Capacite : Apocalypse");
				return Retour.APOCALYPSE;
			}
		});
		
		capacites.put("incrementerOrigine", new Capacite(){
			//Donne au joueur un point d'action de m�me origine que la carte
			@Override
			public Retour capacite(Carte carte, Joueur user) {
				user.incrementerPointAction(carte.getOrigine());
				System.err.println("Capacite : incrementerOrigine");
				return Retour.CONTINUE;
			}
		});
		 
		capacites.put("empecherSacrificeCroyant", new Capacite() {
			//Emp�che un joueur poss�dant une divinit� dont un dogme est diff�rent de la carte de sacrifi� un croyant pendant ce tours.
			@Override
			public Retour capacite(Carte carte, Joueur user) {
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
				System.err.println("Capacite : empecherSacrificeCroyant " + cible);
				return Retour.CONTINUE;
			}
		});
		
		capacites.put("empecherSacrificeGuide", new Capacite() {
			//Emp�che un joueur poss�dant une divinit� dont un dogme est diff�rent de la carte de sacrifi� un guide pendant ce tours.
			@Override
			public Retour capacite(Carte carte, Joueur user) {
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
				System.err.println("Capacite : empecherSacrificeGuide " + cible);
				return Retour.CONTINUE;
			}
		});
		
		capacites.put("piocherCartes", new Capacite() {
			//Permet au joueur de piocher deux cartes au hasard dans la main d'un autre joueur.
			@Override
			public Retour capacite(Carte carte, Joueur user) {
				Set<Joueur> joueurs = BuildCapacites.getJoueursPartie(user);
				Joueur cible = user.joueurPeeker(joueurs);
				int nbCartes = 0;
				if(carte instanceof Croyant) nbCartes = 2;
				else if(carte instanceof Deus_Ex) nbCartes = 3;
				if(cible != null){
					Carte c = null;
					for(int i=0;i<nbCartes;i++){
						c= cible.getGestionnaire_Cartes_Joueur().donnerCarte();
						if(c != null) user.getGestionnaire_Cartes_Joueur().addMain(carte);
					}
				}
				System.err.println("Capacit� : piocherCarte " + cible);
				return Retour.CONTINUE;
			}
		});
		
		capacites.put("obligerSacrificeCroyant", new Capacite() {
			
			@Override
			public Retour capacite(Carte carte, Joueur user) {
				Set<Joueur> joueurs = BuildCapacites.getJoueursPartie(user);
				////////////////////////////////////////////////////////////
				Joueur cible = user.joueurPeeker(joueurs);
				Retour ret = Retour.CONTINUE;
				//La cible doit sacrifier un croyant
				Carte carteaSacrifier = null;
				if(cible != null){
					carteaSacrifier = cible.cardPeeker(cible.getGestionnaire_Cartes_Joueur().getCroyantsChampsDeBataille());
					if(carteaSacrifier != null){
						try {
							ret = cible.sacrifierCarteChampsDeBataille(carteaSacrifier,false);
						} catch (NoTypeException e) {
							e.printStackTrace();
						} catch (DependencyException e) {
							e.printStackTrace();
						}
					}
				}
				System.err.println("Capacit� : obligerSacrificeCroyant " + carteaSacrifier);
				return ret;
			}
		});
		
		capacites.put("obligerSacrificeGuide", new Capacite() {
			
			@Override
			public Retour capacite(Carte carte, Joueur user) {
				Set<Joueur> joueurs = BuildCapacites.getJoueursPartie(user);
				
				//Si la carte jou� est Devin, alors la liste de joueurs selectionnable est r�duite.
				if(carte.getNom().equals("Devin")){
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
				Retour ret = Retour.CONTINUE;
				//La cible doit sacrifier un Guide
				Carte carteaSacrifier = null;
				if(cible != null){
					carteaSacrifier = cible.cardPeeker(cible.getGestionnaire_Cartes_Joueur().getGuidesChampsDeBataille());
					if(carteaSacrifier != null){
						//Si la carte est un Anarchiste alors el, si lui ou sa Divinit� ne croit pas au Dogme Chaos. 
						//Les capacit�s sp�ciales sont jou�es normalement.
						if(carte.getNom().equals("Anarchiste") && cible.getGestionnaire_Cartes_Joueur().getDivinite().getSesDogmes().contains(Dogme.CHAOS)
								&& ((Guide_Spirituel)carteaSacrifier).getDogmes().contains(Dogme.CHAOS)){
							cible.getGestionnaire_Cartes_Joueur().defausserChampsDeBataille(carteaSacrifier);
						}
						else{
							try {
								ret = cible.sacrifierCarteChampsDeBataille(carteaSacrifier,false);
							} catch (NoTypeException e) {
								e.printStackTrace();
							} catch (DependencyException e) {
								e.printStackTrace();
							}
						}
						
					}
				}
				System.err.println("Capacit� : obligerSacrificeGuide " + carteaSacrifier);
				return ret;
			}
		});
		
		capacites.put("faireRevenirGuideMain", new Capacite() {
			
			@Override
			public Retour capacite(Carte carte, Joueur user) {
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
				System.err.println("Capacit� : faireRevenirGuideMain " + carteAFaireRevenir);
				return Retour.CONTINUE;
			}
		});
		
		capacites.put("relancerDe", new Capacite() {
			@Override
			public Retour capacite(Carte carte, Joueur user) {
				Origine o = De_Cosmogonie.lancerDe();
				Set<Joueur> joueurs = new HashSet<Joueur>();
				joueurs.addAll(Partie.getJoueurs());
				Iterator<Joueur> it = joueurs.iterator();
				while(it.hasNext()){
					it.next().attribuerPointsAction(o);
				}
				System.err.println("Capacit� : relancerDe " + o.name());
				
				return Retour.STOPTOUR;
			}
		});
		
		capacites.put("defausserGuide", new Capacite() {
			
			@Override
			public Retour capacite(Carte carte, Joueur user) {
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
				System.err.println("Capacit� : DefausserGuide " + carteaDefausser);
				return Retour.CONTINUE;
			}
		});
	
		capacites.put("volerPointsAction", new Capacite() {
			@Override
			public Retour capacite(Carte carte, Joueur user) {
				Set<Joueur> joueurs = BuildCapacites.getJoueursPartie(user);
				Joueur cible = user.joueurPeeker(joueurs);
				user.getPointsAction().replace(Origine.JOUR, user.getPointsAction().get(Origine.JOUR) + cible.getPointsAction().get(Origine.JOUR));
				user.getPointsAction().replace(Origine.NUIT, user.getPointsAction().get(Origine.NUIT) + cible.getPointsAction().get(Origine.NUIT));
				user.getPointsAction().replace(Origine.NEANT, user.getPointsAction().get(Origine.NEANT) + cible.getPointsAction().get(Origine.NEANT));
				System.err.println("Capacit� : volerPointsAction " + cible.toString());
				return Retour.CONTINUE;
			}
		});
		
		capacites.put("activerCapacite", new Capacite() {
			
			@Override
			public Retour capacite(Carte carte, Joueur user) {
				Set<Joueur> joueurs = BuildCapacites.getJoueursPartie(user);
				Joueur cible = null;
				if(carte instanceof Croyant){
					cible = user.joueurPeeker(joueurs);
				}
				Retour ret = Retour.CONTINUE;
				//La cible doit sacrifier un croyant
				Carte carteaActiver = null;
				if(carte instanceof Croyant && cible != null){
					carteaActiver = cible.cardPeeker(cible.getGestionnaire_Cartes_Joueur().getCroyantsChampsDeBataille());
					if(carteaActiver != null){
						ret = carteaActiver.getCapacite().capacite(carteaActiver, user);
					}
				}
				else if(carte instanceof Deus_Ex){
					carteaActiver = user.cardPeeker(user.getGestionnaire_Cartes_Joueur().getChampsDeBataille());
					if(carteaActiver != null){
						ret = carteaActiver.getCapacite().capacite(carteaActiver, user);
					}
				}
				
				System.err.println("Capacit� : activerCapacite " + carteaActiver);
				return ret;
			}
		});
			
		capacites.put("obligerSacrificeCroyantAll", new Capacite() {
			
			@Override
			public Retour capacite(Carte carte, Joueur user) {
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
					Carte carteaSacrifier = j.cardPeeker(j.getGestionnaire_Cartes_Joueur().getCroyantsChampsDeBataille());
					if(carteaSacrifier != null){
						try {
							Retour ret = j.sacrifierCarteChampsDeBataille(carteaSacrifier,false);
							if(!ret.equals(Retour.CONTINUE)) return ret;
						} catch (NoTypeException e) {
							e.printStackTrace();
						} catch (DependencyException e) {
							e.printStackTrace();
						}
					}
				}
				System.err.println("Capacite : obligerSacrificeCroyantAll ");
				return Retour.CONTINUE;
			}
		});
		
		capacites.put("empecherIncrementationPointsActionAll", new Capacite() {
			
			@Override
			public Retour capacite(Carte carte, Joueur user) {
				Set<Joueur> joueurs = new HashSet<Joueur>();
				joueurs.addAll(Partie.getJoueurs());
				Iterator<Joueur> it = joueurs.iterator();
				while(it.hasNext()){
					it.next().setIncrementerPointsAction(false);
				}
				System.err.println("Capacit� : empecherIncrementationPointsActionAll");
				return Retour.CONTINUE;
			}
		});
			
		capacites.put("clerc", new Capacite() {
			
			@Override
			public Retour capacite(Carte carte, Joueur user) {
				Origine o = user.originePeeker();
				for(int i=0;i<((Guide_Spirituel)carte).getSesCroyants().size();i++){
					user.incrementerPointAction(o);
				}
				System.err.println("Capacit� : clerc");
				return Retour.CONTINUE;
			}
		});
		
		capacites.put("shaman", new Capacite() {
			
			@Override
			public Retour capacite(Carte carte, Joueur user) {
				Set<Joueur> joueurs = BuildCapacites.getJoueursPartie(user);
				//Si la carte est Shaman, r�duit la liste de joueur selectionnables � 
				Iterator<Joueur> it = joueurs.iterator();
				while(it.hasNext()){
					Joueur j = it.next();
					if(!j.getGestionnaire_Cartes_Joueur().getDivinite().getSesDogmes().contains(Dogme.HUMAIN)){
						it.remove();
					}
				}
				////////////////////////////////////////////////////////////
				Joueur cible = user.joueurPeeker(joueurs);
					//Tous les croyants d'origine Neant sont sacrifi�s
				for(int i=0;i<cible.getGestionnaire_Cartes_Joueur().getCroyantsChampsDeBataille().size();i++){
					if(cible.getGestionnaire_Cartes_Joueur().getCroyantsChampsDeBataille().get(i).getOrigine().equals(Origine.NEANT)){
						try {
							Retour ret = cible.sacrifierCarteChampsDeBataille(cible.getGestionnaire_Cartes_Joueur().getCroyantsChampsDeBataille().get(i),false);
							if(!ret.equals(Retour.CONTINUE)) return ret;
						} catch (NoTypeException e) {
							e.printStackTrace();
						} catch (DependencyException e) {
							e.printStackTrace();
						}
					}
				}
				System.err.println("Capacit� : shaman");
				return Retour.CONTINUE;
			}
		});

		capacites.put("paladin", new Capacite() {
			
			@Override
			public Retour capacite(Carte carte, Joueur user) {
				for(int i=0;i<Gestionnaire_cartes_partie.getTable().size();i++){
					Carte c = Gestionnaire_cartes_partie.getTable().get(i);
					if((c.getOrigine().equals(Origine.NUIT) || c.getOrigine().equals(Origine.NEANT)) && ((Croyant)c).getDogmes().contains(Dogme.NATURE)){
						Gestionnaire_cartes_partie.getTable().remove(c);
						Gestionnaire_cartes_partie.getDefausse().add(c);
					}
				}
				System.err.println("Capacit� : Paladin");
				return Retour.CONTINUE;
			}
		});
		
		capacites.put("ascete", new Capacite() {
			
			@Override
			public Retour capacite(Carte carte, Joueur user) {
				//R�duire aux joueurs ayant le dogme humain ou symbole
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
						Retour ret = cible.sacrifierCarteChampsDeBataille(carte,false);
						if(!ret.equals(Retour.CONTINUE)) return ret;
					} catch (NoTypeException e) {
						e.printStackTrace();
					} catch (DependencyException e) {
						e.printStackTrace();
					}
				}
				System.err.println("Capacit� : ascete");
				return Retour.CONTINUE;
			}
		});
		
		capacites.put("exorciste", new Capacite() {
			
			@Override
			public Retour capacite(Carte carte, Joueur user) {
				Carte c = user.cardPeeker(user.getGestionnaire_Cartes_Joueur().getGuidesChampsDeBataille());
				if(c != null){
					user.getGestionnaire_Cartes_Joueur().getMain().add(c);
					user.getGestionnaire_Cartes_Joueur().getChampsDeBataille().remove(c);
					for(int i=0;i<((Guide_Spirituel)c).getSesCroyants().size();i++){
						Gestionnaire_cartes_partie.addDefausse(c);
						user.getGestionnaire_Cartes_Joueur().getChampsDeBataille().remove(c);
					}
					((Guide_Spirituel)c).libererCroyants();
				}
				System.out.println("Capacit� : exorciste");
				return Retour.CONTINUE;
			}
		});
		
		capacites.put("sorcier", new Capacite() {
			
			@Override
			public Retour capacite(Carte carte, Joueur user) {
				System.err.println("Sorcier : non impl�ment�");		
				return Retour.CONTINUE;
			}
		});
	
		capacites.put("tyran", new Capacite() {
			
			@Override
			public Retour capacite(Carte carte, Joueur user) {
				for(int i=0;i<Gestionnaire_cartes_partie.getTable().size();i++){
					if(Gestionnaire_cartes_partie.getTable().get(i).getDogmes().contains(Dogme.MYSTIQUE)){
						Gestionnaire_cartes_partie.removeTable(carte);
						Gestionnaire_cartes_partie.addDefausse(carte);
					}
				}
				System.err.println("Capacit� : tyran");
				return Retour.CONTINUE;
			}
		});
	
		capacites.put("messie", new Capacite() {
			
			@Override
			public Retour capacite(Carte carte, Joueur user) {
				//TODO trouver solution pour finir tour 
				Origine o = user.originePeeker();
				Iterator<Joueur> it = Partie.getJoueurs().iterator();
				while(it.hasNext()){
					it.next().attribuerPointsAction(o);
				}
				System.err.println("Capacit� : messie");
				return Retour.STOPTOUR;
			}
		});
	
		//--------------------------------------------------------------------
		//DEUS EX-------------------------------------------------------------
		//--------------------------------------------------------------------

		capacites.put("colereDivine", new Capacite() {
			
			@Override
			public Retour capacite(Carte carte, Joueur user) {
				Set<Joueur> joueurs = BuildCapacites.getJoueursPartie(user);
				
				Joueur cible = user.joueurPeeker(joueurs);
				//La cible doit sacrifier un Guide
				Carte carteaSacrifier = null;
				List<Carte> guides = cible.getGestionnaire_Cartes_Joueur().getGuidesChampsDeBataille();
				if(carte.getOrigine().equals(Origine.JOUR)){
					for(int i=0;i<cible.getGestionnaire_Cartes_Joueur().getGuidesChampsDeBataille().size();i++){
						if(!cible.getGestionnaire_Cartes_Joueur().getGuidesChampsDeBataille().get(i).getOrigine().equals(Origine.NEANT)
								&& !cible.getGestionnaire_Cartes_Joueur().getGuidesChampsDeBataille().get(i).getOrigine().equals(Origine.NUIT)){
							guides.remove(cible.getGestionnaire_Cartes_Joueur().getGuidesChampsDeBataille().get(i));
						}
					}
				}
				else if(carte.getOrigine().equals(Origine.NUIT)){
					for(int i=0;i<cible.getGestionnaire_Cartes_Joueur().getGuidesChampsDeBataille().size();i++){
						if(!cible.getGestionnaire_Cartes_Joueur().getGuidesChampsDeBataille().get(i).getOrigine().equals(Origine.NEANT)
								&& !cible.getGestionnaire_Cartes_Joueur().getGuidesChampsDeBataille().get(i).getOrigine().equals(Origine.JOUR)){
							guides.remove(cible.getGestionnaire_Cartes_Joueur().getGuidesChampsDeBataille().get(i));
						}
					}
				}
				if(cible != null){
					carteaSacrifier = cible.cardPeeker(guides);
					if(carteaSacrifier != null){
						//Si la carte jou� est col�re divine, la carte n'est pas sacrifi� mais juste d�fauss�.
						cible.getGestionnaire_Cartes_Joueur().defausserChampsDeBataille(carteaSacrifier);
					}
				}
				return Retour.CONTINUE;			}
		});
		
		capacites.put("stase", new Capacite() {
			
			@Override
			public Retour capacite(Carte carte, Joueur user) {
				Carte guide = user.cardPeeker(user.getGestionnaire_Cartes_Joueur().getGuidesChampsDeBataille());
				if(guide != null) guide.setImmunite(true);
				for(int i=0;i<((Guide_Spirituel)guide).getSesCroyants().size();i++){
					((Guide_Spirituel)guide).getSesCroyants().get(i).setImmunite(true);
				}
				return Retour.CONTINUE;
			}
		});
		
		capacites.put("voleGuide", new Capacite() {
			
			@Override
			public Retour capacite(Carte carte, Joueur user) {
				Set<Joueur> joueurs = BuildCapacites.getJoueursPartie(user);
				//Restreindre la liste de joueurs
				Iterator<Joueur> it = joueurs.iterator();
				while(it.hasNext()){
					Joueur j = it.next();
					if(j.getGestionnaire_Cartes_Joueur().getGuidesChampsDeBataille().size() < 1){
						it.remove();
					}
				}
				Joueur cible = user.joueurPeeker(joueurs);
				if(cible != null){
					Carte guide = user.cardPeeker(cible.getGestionnaire_Cartes_Joueur().getGuidesChampsDeBataille());
					if(guide != null){
						//transfert du guide
						user.getGestionnaire_Cartes_Joueur().getChampsDeBataille().add(guide);
						cible.getGestionnaire_Cartes_Joueur().getChampsDeBataille().remove(guide);
						//transfert des croyants
						for(int i=0;i<((Guide_Spirituel)guide).getSesCroyants().size();i++){
							user.getGestionnaire_Cartes_Joueur().getChampsDeBataille().add(((Guide_Spirituel)guide).getSesCroyants().get(i));
							cible.getGestionnaire_Cartes_Joueur().getChampsDeBataille().remove(((Guide_Spirituel)guide).getSesCroyants().get(i));
						}
					}
				}
				return Retour.CONTINUE;
			}
		});

		capacites.put("fourberie", new Capacite() {
			
			@Override
			public Retour capacite(Carte carte, Joueur user) {
				Set<Joueur> joueurs = BuildCapacites.getJoueursPartie(user);
				////////////////////////////////////////////////////////////
				//Restreindre la liste de joueurs
				Iterator<Joueur> it = joueurs.iterator();
				while(it.hasNext()){
					Joueur j = it.next();
					if(j.getGestionnaire_Cartes_Joueur().getCroyantsChampsDeBataille().size() >1){
						it.remove();
					}
				}
				Joueur cible = user.joueurPeeker(joueurs);
				//La cible doit sacrifier un croyant
				Carte carteaSacrifier = null;
				if(cible != null){
					for(int i=0;i<2;i++){
						carteaSacrifier = cible.cardPeeker(cible.getGestionnaire_Cartes_Joueur().getCroyantsChampsDeBataille());
						if(carteaSacrifier != null){
							cible.getGestionnaire_Cartes_Joueur().defausserChampsDeBataille(carteaSacrifier);
						}
					}
				}
				return Retour.CONTINUE;
			}
		});
		
		capacites.put("influence", new Capacite() {
			
			@Override
			public Retour capacite(Carte carte, Joueur user) {
				//Avec cette capacit� le Joueur n'est pas celui qui appelle la capacit� mais celui que l'on souhaite contrer.
				Retour ret = Retour.CONTINUE;
				Origine o = null;
				if(Partie.getLast() != null){
					o = Partie.getLast().getOrigine();
				}
				
				switch(carte.getNom()){
				case "Influence Jour" : 
					if(o == Origine.JOUR) ret = Retour.CANCEL;
					break;
				case "Influence Nuit" : 
					if(o == Origine.NUIT) ret = Retour.CANCEL;
					break;
				case "Influence Neant" : 
					if(o == Origine.NEANT) ret = Retour.CANCEL;
					break;
				case "Influence Nulle" :
					ret = Retour.CANCEL;
					break;
				default : ret = Retour.CONTINUE;
					break;
				}
				return ret;
			}
		});
		
		capacites.put("transe", new Capacite() {
			
			@Override
			public Retour capacite(Carte carte, Joueur user) {
				//carte correspond � la carte jou� dont les benefices seront attribu�s au joueur qui contre...
				
				carte.getCapacite().capacite(carte, user);
				return Retour.CANCEL;
			}
		});
		
		capacites.put("mirroir", new Capacite() {
			
			@Override
			public Retour capacite(Carte carte, Joueur user) {
				//Avec cette capacit� le Joueur n'est pas celui qui appelle la capacit� mais celui que l'on souhaite contrer.
				carte.getCapacite().capacite(carte, user);
				return Retour.CANCEL;
			}
		});
		
		
		//--------------------------------------------------------------------
		//DIVINITES-----------------------------------------------------------
		//--------------------------------------------------------------------
		
		capacites.put("brewalen", new Capacite() {
			
			@Override
			public Retour capacite(Carte carte, Joueur user) {
				Retour ret = Retour.CONTINUE;
				if(Partie.getLast() != null && (Partie.getLast() instanceof Apocalypse || Partie.getLast().getNom().equals("Martyr"))){
					ret = Retour.CANCEL;
				}
				return ret;
			}
		});
		
		capacites.put("shingva", new Capacite() {
			
			@Override
			public Retour capacite(Carte carte, Joueur user) {
				Set<Joueur> joueurs = BuildCapacites.getJoueursPartie(user);
				Joueur cible = user.joueurPeeker(joueurs);
				Retour ret = Retour.CONTINUE;
				Carte carteaSacrifier = null;
				if(cible != null){
					List<Carte> guides = cible.getGestionnaire_Cartes_Joueur().getGuidesChampsDeBataille();
					Iterator<Carte> it = guides.iterator();
					while(it.hasNext()){
						Carte c = it.next();
						if(!((Guide_Spirituel)c).getDogmes().contains(Dogme.SYMBOLES) && !((Guide_Spirituel)c).getDogmes().contains(Dogme.NATURE)){
							it.remove();
						}
					}
					carteaSacrifier = cible.cardPeeker(guides);
					if(carteaSacrifier != null){
						try {
							ret = cible.sacrifierCarteChampsDeBataille(carteaSacrifier,false);
						} catch (NoTypeException e) {
							e.printStackTrace();
						} catch (DependencyException e) {
							e.printStackTrace();
						}
					}
				}
				return ret;
			}
		});

		capacites.put("obligerPoseSacrifice", new Capacite() {
			
			@Override
			public Retour capacite(Carte carte, Joueur user) {
				Set<Joueur> joueurs = BuildCapacites.getJoueursPartie(user);
				Joueur cible = user.joueurPeeker(joueurs);
				Retour ret = Retour.CONTINUE;
				if(cible != null){
					for(int i=0;i<cible.getGestionnaire_Cartes_Joueur().getMain().size();i++){
						if(cible.getGestionnaire_Cartes_Joueur().getMain().get(i) instanceof Apocalypse){
							try {
								ret = cible.jouerCarteMain(cible.getGestionnaire_Cartes_Joueur().getMain().get(i), false);
							} catch (NoTypeException e) {
								e.printStackTrace();
							}
						}
						break;
					}
				}
				return ret;
			}
		});
		
		capacites.put("drinded", new Capacite() {
			
			@Override
			public Retour capacite(Carte carte, Joueur user) {
				Retour ret = Retour.CONTINUE;
				if(Partie.getLast() != null && Partie.getLast() instanceof Guide_Spirituel){
					ret = Retour.CANCEL;
				}
				return ret;
			}
		});

		capacites.put("detruireCroyants", new Capacite() {
			
			@Override
			public Retour capacite(Carte carte, Joueur user) {
				List<Carte> table = new ArrayList<Carte>(Gestionnaire_cartes_partie.getTable());
				Iterator<Carte> it = table.iterator();
				while(it.hasNext()){
					Carte c = it.next();
					if(carte.getOrigine().equals(Origine.NUIT) && c.getOrigine() == Origine.JOUR){
							Gestionnaire_cartes_partie.getTable().remove(c);
					}
					else if(carte.getOrigine().equals(Origine.JOUR) && c.getOrigine() == Origine.NEANT){
						Gestionnaire_cartes_partie.getTable().remove(c);
					}
				}
				return Retour.CONTINUE;
			}
		});
		
		capacites.put("gwenhelen", new Capacite() {
			
			@Override
			public Retour capacite(Carte carte, Joueur user) {
				Set<Joueur> joueurs = BuildCapacites.getJoueursPartie(user);
				Joueur cible = user.joueurPeeker(joueurs);
				
				if(cible != null){
					user.getPointsAction().replace(Origine.NEANT, user.getPointsAction().get(Origine.NEANT) + cible.getGestionnaire_Cartes_Joueur().getGuidesChampsDeBataille().size());
				}
				
				return Retour.CONTINUE;
			}
		});
		
		capacites.put("romtec", new Capacite() {
			
			@Override
			public Retour capacite(Carte carte, Joueur user) {
				System.err.println("Romtec : not implemented yet !");
				return null;
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
