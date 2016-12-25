package models.joueur.strategies;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import controller.Gestionnaire_cartes_partie;
import exceptions.DependencyException;
import exceptions.NoTypeException;
import models.De_Cosmogonie;
import models.Partie;
import models.cartes.Apocalypse;
import models.cartes.Carte;
import models.cartes.Croyant;
import models.cartes.Deus_Ex;
import models.cartes.Guide_Spirituel;
import models.enums.Origine;
import models.enums.Retour;
import models.joueur.Joueur;

public class StrategyNormal implements Strategy{
	
	
	@Override
	public Retour jouer(Joueur joueur) {
		System.out.println(joueur.toString());
		
		Retour ret = Retour.CONTINUE;
		//Phase de défausse 
		this.phaseDefausse(joueur);
		//Remplir main
		this.phaseCompleterMain(joueur);
		
		ret = this.phaseUtiliserDivinite(joueur);
		if(ret.equals(Retour.APOCALYPSE) || ret.equals(Retour.STOPTOUR)) return ret;
		
		//Jouer carte action
		ret = this.phaseJouerCarteMain(joueur);
		if(ret.equals(Retour.APOCALYPSE) || ret.equals(Retour.STOPTOUR)) return ret;
		
		System.out.println(joueur.getGestionnaire_Cartes_Joueur().champsDeBatailleToString());
		
		//sacrifier carte du champs de bataille.
		ret = this.phaseSacrificeCarteChampsDeBataille(joueur);
		if(ret.equals(Retour.APOCALYPSE) || ret.equals(Retour.STOPTOUR)) return ret;
		return ret;
	}
	
	@Override
	public void phaseDefausse(Joueur joueur){
		//Retire de sa main toutes les cartes qu'il ne peut pas jouer
		List<Carte> nePasDefausse = joueur.getGestionnaire_Cartes_Joueur().cartesJouables(joueur.getPointsAction(), joueur.getGestionnaire_Cartes_Joueur().getMain());
		List<Carte> defausse = new ArrayList<Carte>();
		for(int i=0;i<joueur.getGestionnaire_Cartes_Joueur().getMain().size();i++){
			if(!nePasDefausse.contains(joueur.getGestionnaire_Cartes_Joueur().getMain().get(i))) defausse.add(joueur.getGestionnaire_Cartes_Joueur().getMain().get(i));
		}
		//System.out.println("DEFAUSSE : " + defausse.size());
		joueur.getGestionnaire_Cartes_Joueur().defausserMain(defausse);
	}
	
	@Override
	public void phaseCompleterMain(Joueur joueur){
		if(joueur.getGestionnaire_Cartes_Joueur().getMain().size() < 7) joueur.getGestionnaire_Cartes_Joueur().remplirMain();
		System.out.println("MAIN : " + joueur.getGestionnaire_Cartes_Joueur().getMain().size()+"\n");
		//System.out.println("PIOCHE : " + Gestionnaire_cartes_partie.getPioche().size());
	}
	
	@Override
	public Retour phaseUtiliserDivinite(Joueur joueur){
		Retour ret = Retour.CONTINUE;
		if(!joueur.getGestionnaire_Cartes_Joueur().getDivinite().isCapaciteUsed()){
			String nom = joueur.getGestionnaire_Cartes_Joueur().getDivinite().getNom();
			if(nom.equals("Shingva") || nom.equals("Llewella") || nom.equals("Gorpa") || nom.equals("Pui-Tara") || nom.equals("Yartsur")
					|| nom.equals("Gwenhelen") || nom.equals("Killinstred") ){
				int nb = (int) (Math.random() * 6 );
				if(nb == 1){
					System.err.println("Capacité divinité : " + nom + " activé !");
					ret = joueur.activerCapaciteDivinite();
				}
			}
		}
		return ret;
	}
	
	@Override	
	public Retour phaseJouerCarteMain(Joueur joueur){
		System.out.println("Phase jouer carte main");
		List<Carte> cartesJouables = joueur.getGestionnaire_Cartes_Joueur().cartesJouables(joueur.getPointsAction(), joueur.getGestionnaire_Cartes_Joueur().getMain());
		
		Carte picked = null;
		Retour ret = Retour.CONTINUE;
		do{
			picked = this.selectionCarteMain(joueur, cartesJouables);
			if(picked != null){
				try {
					ret = joueur.jouerCarteMain(picked,true);
				} catch (NoTypeException e) {
					e.printStackTrace();
				}
				//Pour ne jouer qu'un seul deus ex par tour
				if(picked instanceof Deus_Ex) picked = null;
			}
			cartesJouables = joueur.getGestionnaire_Cartes_Joueur().cartesJouables(joueur.getPointsAction(), joueur.getGestionnaire_Cartes_Joueur().getMain());
		}while(cartesJouables.size() > 0 && picked != null && ret.equals(Retour.CONTINUE) );
		return ret;
	}
	
	@Override
	public Retour phaseSacrificeCarteChampsDeBataille(Joueur joueur){
		//Un joueur virtuel ne sacrifie qu'une seul carte par tour
		List<Carte> cartesJouables = joueur.getGestionnaire_Cartes_Joueur().cartesJouables(joueur.getPointsAction(), joueur.getGestionnaire_Cartes_Joueur().getCroyantsChampsDeBataille());
		Retour ret = Retour.CONTINUE;
		if(cartesJouables.size() > 0){
			try {
				ret = joueur.sacrifierCarteChampsDeBataille(cartesJouables.get(0),true);
			} catch (NoTypeException e) {
				e.printStackTrace();
			} catch (DependencyException e) {
				e.printStackTrace();
			}
		}
		else if(joueur.getGestionnaire_Cartes_Joueur().cartesJouables(joueur.getPointsAction(), joueur.getGestionnaire_Cartes_Joueur().getGuidesChampsDeBataille()).size() > 0){
			cartesJouables = joueur.getGestionnaire_Cartes_Joueur().cartesJouables(joueur.getPointsAction(), joueur.getGestionnaire_Cartes_Joueur().getGuidesChampsDeBataille());
			try {
				ret = joueur.sacrifierCarteChampsDeBataille(cartesJouables.get(0),true);
			} catch (NoTypeException e) {
				e.printStackTrace();
			} catch (DependencyException e) {
				e.printStackTrace();
			}
		}
		return ret;
	}
	
	public Carte selectionCarteMain(Joueur joueur, List<Carte> main){
		for(int i=0;i<main.size();i++){
			if(main.get(i) instanceof Apocalypse && ((Partie.getJoueurs().size() >= 4 && Partie.getJoueurMin() != null && !joueur.equals(Partie.getJoueurMin())) 
					|| (Partie.getJoueurs().size() < 4 && Partie.getJoueurMax()!=null && joueur.equals(Partie.getJoueurMax())))){
				return main.get(i);
			}
			else if(main.get(i) instanceof Croyant && Gestionnaire_cartes_partie.getTable().size()<2){
				return main.get(i);
			}
			else if(main.get(i) instanceof Guide_Spirituel && ((Guide_Spirituel)main.get(i)).croyantsDisponible().size() >= ((Guide_Spirituel)main.get(i)).getNombre()){
				return main.get(i);
			}
			else if(main.get(i) instanceof Deus_Ex && main.get(i).getOrigine() != null){
				boolean contain = false;
				for(int j=0;j<main.size();j++){
					if(main.get(j) instanceof Croyant || main.get(j) instanceof Guide_Spirituel) contain = true;
				}
				if(!contain) return main.get(i);
			}
		}
		//Si aucune carte n'est digne d'être joué, retourne null. 
		return null;
	}

	@Override
	public Carte repondre(Joueur joueur, Carte sacrifice) {
		if(sacrifice.getOrigine() != null){
			List<Carte> cartes = new ArrayList<Carte>(joueur.getGestionnaire_Cartes_Joueur().getCartesReponse());
			Iterator<Carte> it = cartes.iterator();
			while(it.hasNext()){
				if(!it.next().getNom().split(" ")[0].equals("Influence")){
					it.remove();
				}
			}
		
			String nom = joueur.getGestionnaire_Cartes_Joueur().getDivinite().getNom();
			if(!joueur.getGestionnaire_Cartes_Joueur().getDivinite().isCapaciteUsed()&&
					(nom.equals("Brewalen") || nom.equals("Dinded"))){
				cartes.add(joueur.getGestionnaire_Cartes_Joueur().getDivinite());
			}
			return this.cardPeeker(cartes);
		}
		else return null;
	}

	@Override
	public Carte cardPeeker(List<Carte> cartes) {
		if(cartes.size() > 0){
			int nb = (int) (Math.random() * (cartes.size()-1));
			return cartes.get(nb);
		}else return null;
	}

	@Override
	public Joueur joueurPeeker(Set<Joueur> joueurs) {
		Iterator<Joueur> it = joueurs.iterator();
		if(it.hasNext()) return it.next();
		else return null;
	}

	@Override
	public Origine originePeeker() {
		return De_Cosmogonie.lancerDe();
	}
	
	
}
