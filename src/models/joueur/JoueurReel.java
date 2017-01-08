package models.joueur;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import controller.Gestionnaire_cartes_partie;
import controller.listeners.JoueurCardPeekerListener;
import controller.listeners.JoueurCardUpdateListener;
import exceptions.DependencyException;
import exceptions.NoTypeException;
import models.Partie;
import models.cartes.Apocalypse;
import models.cartes.Carte;
import models.cartes.Croyant;
import models.cartes.Guide_Spirituel;
import models.enums.Origine;
import models.enums.Retour;

public class JoueurReel extends Joueur{
	
	private String nom;
	private Scanner scan;
	
	private JoueurCardPeekerListener joueurCardPeekerListener;
	
	public JoueurReel(String nom){
		super();
		this.nom = nom;
		this.scan = new Scanner(System.in);
	}
	
	@Override
	public Retour jouer() {
		Retour ret = Retour.CONTINUE;
		System.out.println(this.toString());
		//System.out.println(super.gcj.toString());
		//Phase de d�fausse 
		this.phaseDefausse();
		//Remplir main
		this.phaseCompleterMain();
		//Jouer carte action
		System.out.println("Votre Divinit� : " + this.gcj.getDivinite());

		ret = this.phaseUtiliserDivinite();
		if(ret.equals(Retour.APOCALYPSE) || ret.equals(Retour.STOPTOUR)) return ret;
		
		System.out.println(Gestionnaire_cartes_partie.afficherCartesPartie());
		ret = this.phaseJouerCarteMain();
		if(ret.equals(Retour.APOCALYPSE) || ret.equals(Retour.STOPTOUR)) return ret;
		
		//sacrifier carte du champs de bataille.
		System.out.println(super.gcj.champsDeBatailleToString());
		ret = this.phaseSacrificeCarteChampsDeBataille();
		if(ret.equals(Retour.APOCALYPSE) || ret.equals(Retour.STOPTOUR)) return ret;
		
		return ret;
	}
		
	public void phaseDefausse(){
		System.out.println("Souhaitez vous vous d�fausser d'une carte de votre main ?");
		Carte carte = null;
		do{
			
			this.joueurCardPeekerListener.afficherMessage("Souhaitez vous vous d�fausser d'une carte de votre main ?");
			carte = this.joueurCardPeekerListener.cardPeekerMain(super.gcj.getMain());
			//carte = this.cardPeeker(super.gcj.getMain());
			
			if(carte != null){
				super.getGestionnaire_Cartes_Joueur().defausserMain(carte);
			}
		}while(carte != null);
	}
	
	public void phaseCompleterMain(){
		System.out.println("Souhaitez vous completer votre main ? (y/n)");
		
		this.joueurCardPeekerListener.afficherMessage("Souhaitez vous completer votre main ?");
		
		//if(this.yesOrNo(this.scan)) super.gcj.remplirMain();
		if(this.joueurCardPeekerListener.yesOrNo()) super.gcj.remplirMain();
	}
	
	public Retour phaseUtiliserDivinite(){
		Retour ret = Retour.CONTINUE;
		if(!this.gcj.getDivinite().isCapaciteUsed()){
			System.out.println("Voulez vous activer la capacit� de votre divinit� ? (y/n)");
			this.joueurCardPeekerListener.afficherMessage("Voulez vous activer la capacit� de votre divinit� ?");
			//if(this.yesOrNo(scan)) ret = super.activerCapaciteDivinite();
			if(this.joueurCardPeekerListener.yesOrNo()) ret = super.activerCapaciteDivinite();
		}
		return ret;
	}
	
	public Retour phaseJouerCarteMain(){
		System.out.println("Souhaitez vous jouer une carte de votre main ?");
		this.joueurCardPeekerListener.afficherMessage("Souhaitez vous jouer une carte de votre main ?");
		
		Carte carte = null;
		Retour ret = Retour.CONTINUE;
		do{
			carte = this.joueurCardPeekerListener.cardPeekerMain(super.gcj.cartesJouables(super.pointsAction,super.gcj.getMain()));
			//carte = cardPeeker(super.gcj.cartesJouables(super.pointsAction,super.gcj.getMain()));
			if(carte != null){
				//La carte est jou�
				try {
					ret = super.jouerCarteMain(carte,true);
					//if(carte instanceof Apocalypse) return false;
				} catch (NoTypeException e) {
					e.printStackTrace();
				}
			}
		}while(carte != null && super.gcj.cartesJouables(super.pointsAction,super.gcj.getMain()).size() > 0 && ret.equals(Retour.CONTINUE));
		return ret;
	}

	public Retour phaseSacrificeCarteChampsDeBataille(){
		System.out.println("Souhaitez vous sacrifier une carte du champs de bataille ?");
		this.joueurCardPeekerListener.afficherMessage("Souhaitez vous sacrifier une carte du champs de bataille ?");
		
		Carte carte = null;		
		Retour ret = Retour.CONTINUE;
		do{
			//System.out.println("Cartes sacrifiables sur le champs de bataille : ");
			//System.out.println(super.gcj.cartesJouablesToString(super.gcj.cartesJouables(super.pointsAction,super.gcj.getChampsDeBataille())));
			//carte = cardPeeker(super.gcj.cartesJouables(super.pointsAction,super.gcj.getChampsDeBataille()));
			carte = this.joueurCardPeekerListener.cardPeekerChampsDeBataille(super.gcj.cartesJouables(super.pointsAction,super.gcj.getChampsDeBataille()));
			
			if(carte != null){
				//La carte est jou�
				try {
					ret = super.sacrifierCarteChampsDeBataille(carte,true);
				} catch (NoTypeException e) {
					e.printStackTrace();
				} catch (DependencyException e) {
					e.printStackTrace();
				}
			}
		}while(carte != null && super.gcj.cartesJouables(super.pointsAction,super.gcj.getMain()).size() > 0 && ret.equals(Retour.CONTINUE));
		this.joueurCardPeekerListener.afficherMessage("En attente des autres joueurs...");
		return ret;
	}
	
	@Override
	public Carte repondre(Carte sacrifice) {
		this.joueurCardPeekerListener.afficherMessage("Une carte a �t� ajout� � la pile de sacrifice. Souhaitez vous la contrer ?");
		List<Carte> cartes = new ArrayList<Carte>(this.gcj.getCartesReponse());
		if(!this.gcj.getDivinite().isCapaciteUsed()){
			cartes.add(this.gcj.getDivinite());
		}
		//Carte c = this.cardPeeker(cartes);
		Carte carte = this.joueurCardPeekerListener.cardPeekerMain(cartes);
		return carte;
	}
	
	@Override
	public Carte cardPeeker(List<Carte> cartes) {
		System.out.println("Quel carte voulez vous s�lectionner ? (0) pour arr�ter.");
		System.out.println("Cartes selectionnables : ");
		System.out.println(super.gcj.cartesJouablesToString(cartes));
		String input;
		do{
			input = this.scan.nextLine();
		}while(!input.matches("[0-9]+") || Integer.parseInt(input) <0 || Integer.parseInt(input) > cartes.size());
		Integer index = Integer.parseInt(input);
		if(index == 0) return null;
		else return cartes.get(index - 1);
	}
	
	@Override
	public Joueur joueurPeeker(Set<Joueur> joueurs) {
		Iterator<Joueur> it = joueurs.iterator();
		Joueur j = null;
		do{
			while(it.hasNext()){
				j = it.next();
				System.out.println(j.toString());
				System.out.println(j.getGestionnaire_Cartes_Joueur().champsDeBatailleToString());
				System.out.println("Voulez vous cibler ce joueur ? (y/n)");
				if(this.yesOrNo(this.scan)) return j;
				j=null;
			}
		}while(j.equals(null));
		return null;
	}
	
	@Override
	public Origine originePeeker() {
		List<Origine> origines = new ArrayList<Origine>();
		origines.add(Origine.JOUR);
		origines.add(Origine.NUIT);
		origines.add(Origine.NEANT);
		
		Iterator<Origine> it = origines.iterator();
		int count = 1;
		while(it.hasNext()){
			System.out.println(count + " " + it.next());
		}
		System.out.println("Quel origine souhaitez vous s�lectionner ? ");
		String input;
		do{
			input = this.scan.nextLine();
		}while(!input.matches("[0-9]+") || Integer.parseInt(input) <1 || Integer.parseInt(input) > 3);
		return origines.get(Integer.parseInt(input));
	}
	
	public Boolean yesOrNo(Scanner scan){
		String input=null;
		do{
			input = scan.nextLine();
		}while(!input.equals("y") && !input.equals("n") && !input.equals("Y") && !input.equals("N"));
		if(input.equals("y") || input.equals("Y")) return true;
		else if(input.equals("n") || input.equals("N")) return false;
		else return null;
	}
	
	public void addJoueurCardPeekerListener(JoueurCardPeekerListener joueurCardPeekerListener){
		this.joueurCardPeekerListener = joueurCardPeekerListener;
	}
	
	
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("\nJoueur Reel : ").append(nom).append("\n");
		buf.append(super.toString());
		return buf.toString();
	}
}
