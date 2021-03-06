package models.joueur;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import controller.Gestionnaire_Cartes_Joueur;
import controller.Gestionnaire_cartes_partie;
import controller.listeners.JoueurPointsActionsListener;
import controller.listeners.SacrificeListener;
import exceptions.DependencyException;
import exceptions.NoTypeException;
import models.Partie;
import models.cartes.Apocalypse;
import models.cartes.Carte;
import models.cartes.ConstanteCarte;
import models.cartes.Croyant;
import models.cartes.Deus_Ex;
import models.cartes.Divinite;
import models.cartes.Guide_Spirituel;
import models.enums.Origine;
import models.enums.Retour;

public class Joueur implements SacrificeListener{
	protected Map<Origine,Integer> pointsAction;
	protected Gestionnaire_Cartes_Joueur gcj = null;
	protected boolean incrementerPointsAction = true;
	
	protected SacrificeListener sacrificeListener;
	protected JoueurPointsActionsListener joueurPointsActionsListener;
	
	public Joueur(){
		this.pointsAction = new HashMap<Origine,Integer>();
		this.pointsAction.put(Origine.JOUR, 0);
		this.pointsAction.put(Origine.NUIT, 0);
		this.pointsAction.put(Origine.NEANT, 0);
	}
	
	/**
	 * Permet au joueur de jouer tant que ses points d'action le lui permettent.
	 * @return Renvois faux si une carte APOCALYPSE a �t� jou� sinon renvois vrai.
	 */
	public Retour jouer(){
		return Retour.CONTINUE;
	};
	
	/**
	 * Permet au joueur de jouer une carte de sa main.
	 * @param carte La carte que le joueur souhaite jouer.
	 * @return La carte jou� ou null.
	 * @throws NoTypeException 
	 */
	public Retour jouerCarteMain(Carte carte,boolean self) throws NoTypeException{
		if(gcj.isJouable(carte, this.pointsAction) || !self){
			//Intention de jouer la carte
			Retour ret = Retour.CONTINUE;
			if(self)this.payerCoutCarte(carte);
			
			ret = gcj.intentionJouerCarte(carte);
				//Si le reour le l'intention est cancel, la carte est d�fauss� et l'op�ration de jouerCarteMain est arr�t�
			if(ret.equals(Retour.CONTINUE)){
				if(carte instanceof Croyant){
					//Rien � faire pour le moment
					System.out.println("Une carte Croyant a �t� ajout� � la pile de Croyants");
				}
				else if(carte instanceof Guide_Spirituel){
					System.out.println("Une carte Guide Spirituel a �t� pos�, il ramm�ne les croyants � lui");
				}
				else if(carte instanceof Deus_Ex){
					//Utilise la capacit� de la carte
					System.err.println("Sacrifice de : " + carte.getNom());
					ret = carte.getCapacite().capacite(carte, this);
				}
				else if(carte instanceof Apocalypse){
					//Active une apocalypse
					ret = carte.getCapacite().capacite(carte, this);
				}
				else throw new NoTypeException("La carte est de type DIVINITE ou est NULL.");
			}
			//La carte a �t� jouer
			gcj.transfertCarteJouer(carte);
			return ret;
		}
		System.err.println("Vous ne pouvez pas jouer cette carte !");
		return null;
	}
	
	/**
	 * Permet au joueur de sacrifier une des cartes qui se trouvant sur le champs de bataille.
	 * @param indice L'indice de la carte devant lui.
	 * @throws NoTypeException 
	 * @throws DependencyException 
	 */
	public Retour sacrifierCarteChampsDeBataille(Carte carte,boolean self) throws DependencyException, NoTypeException{
		if((gcj.isJouable(carte, this.pointsAction) || !self) &&  gcj.isSacrifiable(carte)){
			Retour ret = Retour.CONTINUE;
			
			if(self) this.payerCoutCarte(carte);
			//La carte est mise dans la pile de sacrifice
			ret = gcj.intentionJouerCarte(carte);
						
			for(int i=0;i<this.gcj.getGuidesChampsDeBataille().size();i++){
				if(((Guide_Spirituel) this.gcj.getGuidesChampsDeBataille().get(i)).getSesCroyants().size() == 0){
					throw new DependencyException("Un guide n'a pas de croyant mais est quand m�me sur le champs de bataille.");
				}
			}
			for(int i=0;i<this.gcj.getCroyantsChampsDeBataille().size();i++){
				if(((Croyant) this.gcj.getCroyantsChampsDeBataille().get(i)).getGuide() == null){
					throw new DependencyException("Un croyant n'a pas de guide mais est quand m�me sur le champs de bataille.");
				}
			}
			if(ret.equals(Retour.CONTINUE)){	
				//La capacit� est activ�
				System.err.println("Sacrifice de : " + carte.getNom());
				ret = carte.getCapacite().capacite(carte, this);
			}
			gcj.transfertCarteJouer(carte);
			return ret;
		}
		System.err.println("Vous ne pouvez pas jouer cette carte !");
		return Retour.CONTINUE;
	}
	
	
	@Override
	public Retour enReponse(Carte sacrifice) {
		Retour ret = Retour.CONTINUE;
		if(Gestionnaire_cartes_partie.getPileSacrificeSize() < 2){
			Set<Joueur> joueurs = new LinkedHashSet<Joueur>(Partie.getJoueurs());
			joueurs.remove(this);
			Iterator<Joueur> it = joueurs.iterator();
			while(it.hasNext()){
				Joueur j = it.next();
				Carte c = j.repondre(sacrifice);
				if(c != null){
					System.err.println("EN REPONSE \n" + c.toString());
					if(c instanceof Divinite){
						ret = j.activerCapaciteDivinite();
					}
					else{
						try {
							ret = j.jouerCarteMain(c,true);
						} catch (NoTypeException e) {
							e.printStackTrace();
						}
					}
					if(!ret.equals(Retour.CONTINUE)) return ret;
				}
			}
		}
		return ret;
	}
	
	
	public Carte repondre(Carte sacrifice){
		return null;
	}
	
	/**
	 * Permet au joueur d'utiliser la capacit� de sa divinit�. 
	 */
	public Retour activerCapaciteDivinite(){
		//V�rifier que la divinit� n'ai pas d�ja utilis� son pouvoir 
		Retour ret = Retour.CONTINUE;
		if(!this.getGestionnaire_Cartes_Joueur().getDivinite().isCapaciteUsed()){
			ret = this.getGestionnaire_Cartes_Joueur().getDivinite().getCapacite().capacite(this.getGestionnaire_Cartes_Joueur().getDivinite(), this);
			this.getGestionnaire_Cartes_Joueur().getDivinite().setCapaciteUsed(true);
		}
		return ret;
	}
	
	public Carte cardPeeker(List<Carte> cartes){
		return null;
	}
	
	/**
	 * Permet au joueur de cibler un autre joueur.
	 * @param joueurs
	 * @return Le joueur cibl�
	 */
	public Joueur joueurPeeker(Set<Joueur> joueurs){
		return null;
	}
	
	public Origine originePeeker(){
		return null;
	}
	
	/**
	 * Retire des points d'action au joueur en fonction de la carte qu'il veut jouer.
	 * @param carte
	 */
	private void payerCoutCarte(Carte carte){
		if(carte.getOrigine() != null){
			if(carte.getOrigine().equals(Origine.NEANT) && pointsAction.get(Origine.NEANT) == 0){
				 if(this.pointsAction.get(Origine.JOUR) == this.pointsAction.get(Origine.NUIT)){
					 this.decrementerPointsAction(Origine.JOUR);
					 this.decrementerPointsAction(Origine.NUIT);
				 }
				 else if(this.pointsAction.get(Origine.JOUR) > this.pointsAction.get(Origine.NUIT)){
					 this.decrementerPointsAction(Origine.JOUR);
					 this.decrementerPointsAction(Origine.JOUR);
				 }
				 else if(this.pointsAction.get(Origine.JOUR) < this.pointsAction.get(Origine.NUIT)){
					 this.decrementerPointsAction(Origine.NUIT);
					 this.decrementerPointsAction(Origine.NUIT);
				 }
			}
			else this.decrementerPointsAction(carte.getOrigine());
		}
	}
	
	/**
	 * Distribue des points d'action au joueur en fonction de sa divinit�. 
	 * @param origine L'Origine tir� par le d� de cosmogonie. 
	 */
	public void attribuerPointsAction(Origine origine){
		//System.out.println("Origine : " + origine);
		//System.out.println("Origine divinit� : " + this.gcj.getDivinite().getOrigine());
		switch(this.gcj.getDivinite().getOrigine()){
		case JOUR : 
			if(origine.equals(Origine.JOUR)){
				this.incrementerPointAction(origine);
				this.incrementerPointAction(origine);
			}
			break;
		case NUIT :
			if(origine.equals(Origine.NUIT)){
				this.incrementerPointAction(origine);
				this.incrementerPointAction(origine);
			}
			break;
		case AUBE :
			if(origine.equals(Origine.JOUR)){
				this.incrementerPointAction(origine);
			}
			if(origine.equals(Origine.NUIT)){
				this.incrementerPointAction(origine);
			}
			break;
		case CREPUSCULE :
			if(origine.equals(Origine.NUIT)){
				this.incrementerPointAction(origine);
			}
			if(origine.equals(Origine.NEANT)){
				this.incrementerPointAction(origine);
			}
			break;
		default : break;
		}
	}
	
	/**
	 * Ajoute 1 point d'action dans la PointsAction en fonction de l'Origine.
	 * @param origine L'Origine. 
	 */
	public void incrementerPointAction(Origine origine){
		//System.out.println("Incr�mentation Point d'action : " + origine);
		if(incrementerPointsAction) pointsAction.replace(origine, pointsAction.get(origine) + 1);
		if(this.joueurPointsActionsListener != null) this.joueurPointsActionsListener.updatePointsActions(pointsAction);
	}
	
	public void decrementerPointsAction(Origine origine){
		pointsAction.replace(origine, pointsAction.get(origine) - 1);
		if(this.joueurPointsActionsListener != null) this.joueurPointsActionsListener.updatePointsActions(pointsAction);
	}
	
	public void setIncrementerPointsAction(boolean incrementerPointsAction) {
		this.incrementerPointsAction = incrementerPointsAction;
	}
	
	public void attachGestionnaire_Cartes_Joueur(List<Carte> main, Divinite divinite){
		this.gcj = new Gestionnaire_Cartes_Joueur(this, main, divinite);
		this.gcj.addSacrificeListener(this);
	}
	
	public Gestionnaire_Cartes_Joueur getGestionnaire_Cartes_Joueur(){
		return this.gcj;
	}
	
	public Map<Origine, Integer> getPointsAction() {
		return pointsAction;
	}
	
	public void addJoueurPointsActionsListener(JoueurPointsActionsListener joueurPointsActionsListener){
		this.joueurPointsActionsListener = joueurPointsActionsListener;
	}
	
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("Points d'action : ").append(pointsAction.toString()).append(ConstanteCarte.BARRE);
		//buf.append(gcj.toString()).append("\n");
		return buf.toString();
	}
}
