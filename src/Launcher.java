import java.util.ArrayList;

import models.cartes.BuildCartes;
import models.cartes.Carte;

/**
 * 
 */

/**
 * @author Antoine Croisille
 *
 */
public class Launcher {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Lancement de Pandocréon");
		ArrayList<Carte> deck = BuildCartes.getCartes();
		for(int i=0;i<deck.size();i++){
			System.out.println(deck.get(i).toString());
		}
		System.out.println("Nombre de cartes : " + deck.size());
	}

}
