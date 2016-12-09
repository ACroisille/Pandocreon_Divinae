package models.joueur;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import controller.Gestionnaire_Cartes_Joueur;
import controller.Gestionnaire_cartes_partie;
import exceptions.NoTypeException;
import models.cartes.Apocalypse;
import models.cartes.Carte;
import models.cartes.ConstanteCarte;
import models.cartes.Croyant;
import models.cartes.Deus_Ex;
import models.cartes.Divinite;
import models.cartes.Guide_Spirituel;
import models.cartes.Origine;

public abstract class Joueur {
	protected Map<Origine,Integer> pointsAction;
	protected Gestionnaire_Cartes_Joueur gcj = null;
	protected boolean incrementerPointsAction = true;
	
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
	public boolean jouer(){
		return false;
	};
	
	/**
	 * Permet au joueur de jouer une carte de sa main.
	 * @param carte La carte que le joueur souhaite jouer.
	 * @return La carte jou� ou null.
	 * @throws NoTypeException 
	 */
	public Carte jouerCarteMain(Carte carte) throws NoTypeException{
		if(gcj.isJouable(carte, this.pointsAction)){
			//Intention de jouer la carte
			this.payerCoutCarte(carte);
			gcj.intentionJouerCarte(carte);//La carte passe dans la pilePose et un listener est d�clench�. 
			if(carte instanceof Croyant){
				//Rien � faire pour le moment
				System.out.println("Une carte Croyant a �t� ajout� � la pile de Croyants");
			}
			else if(carte instanceof Guide_Spirituel){
				System.out.println("Une carte Guide Spirituel a �t� pos�, il ramm�ne les croyants � lui");
			}
			else if(carte instanceof Deus_Ex){
				//Utilise la capacit� de la carte
				System.out.println("Une carte DeusEx a �t� pos�, elle est sacrifi�");
				carte.getCapacite().capacite(carte, this);
			}
			else if(carte instanceof Apocalypse){
				//Active une apocalypse
				System.err.println("APOCALYPSE");
				carte.getCapacite().capacite(carte, this);
			}
			else throw new NoTypeException("La carte est de type DIVINITE ou est NULL.");
			//La carte a �t� jouer
			gcj.transfertCarteJouer(carte);
			return carte;
		}
		
		//System.err.println("Vous ne pouvez pas jouer cette carte !");
		return null;
	}
	
	/**
	 * Permet au joueur de sacrifier une des cartes qui se trouvant sur le champs de bataille.
	 * @param indice L'indice de la carte devant lui.
	 * @throws NoTypeException 
	 */
	public Carte sacrifierCarteChampsDeBataille(Carte carte) throws NoTypeException{
		if(gcj.isJouable(carte, this.pointsAction) && gcj.isSacrifiable(carte)){
			//La carte est mise dans la pile de sacrifice
			gcj.intentionJouerCarte(carte);
			if(carte instanceof Croyant){
				 System.out.println("Sacrifice d'une carte croyant.");
				 //Si la carte croyant �tait la derni�re de son guide, le guide est d�fauss�.
				 if(((Croyant)carte).getGuide().getSesCroyants().size() == 1){
					 gcj.defausserChampsDeBataille(((Croyant)carte).getGuide());
				 }
			}
			else if(carte instanceof Guide_Spirituel){
				System.out.println("Sacrifice d'une carte Guide Spirituel.");
				//Si le guide poss�dais des croyants, ils reviennent au centre de la table. 
				gcj.defausserChampsDeBataille(carte);
			}
			else throw new NoTypeException("La carte n'est pas de type CROYANT ou GUIDE_SPIRITUEL.");
			this.payerCoutCarte(carte);
			//La capacit� est activ�
			carte.getCapacite().capacite(carte, this);
			
			gcj.transfertCarteJouer(carte);
			return carte;
		}
		System.err.println("Vous ne pouvez pas jouer cette carte !");
		return null;
	}
	
	/**
	 * Permet au joueur d'utiliser une carte sans origine ou sa divinit�.
	 * @param carte
	 */
	public void enReponse(Carte carte){
		
	}
	
	/**
	 * Permet au joueur d'utiliser la capacit� de sa divinit�. 
	 */
	public void activerCapaciteDivinite(Carte carte){
		
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
	}
	
	public void decrementerPointsAction(Origine origine){
		pointsAction.replace(origine, pointsAction.get(origine) - 1);
	}
	
	public void setIncrementerPointsAction(boolean incrementerPointsAction) {
		this.incrementerPointsAction = incrementerPointsAction;
	}
	
	public void attachGestionnaire_Cartes_Joueur(List<Carte> main, Divinite divinite){
		this.gcj = new Gestionnaire_Cartes_Joueur(this, main, divinite);
	}
	
	public Gestionnaire_Cartes_Joueur getGestionnaire_Cartes_Joueur(){
		return this.gcj;
	}
	
	public Map<Origine, Integer> getPointsAction() {
		return pointsAction;
	}
	
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("Points d'action : ").append(pointsAction.toString()).append(ConstanteCarte.BARRE);
		//buf.append(gcj.toString()).append("\n");
		return buf.toString();
	}
}
