package views;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import controller.JoueurCardUpdateListener;
import models.cartes.Carte;
import models.joueur.Joueur;
import models.joueur.JoueurVirtuel;

public class AffichageJV extends JPanel implements JoueurCardUpdateListener{
	
	public CardView divinite;
	public JPanel champsDeBataille;
	
	public AffichageJV(JoueurVirtuel j, Dimension d){
		
		j.getGestionnaire_Cartes_Joueur().addListCartesListener(this);
		
		this.setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		
		this.divinite = new CardView(j.getGestionnaire_Cartes_Joueur().getDivinite(), Sizes.BOT_CARD_SIZE);
		
		champsDeBataille = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		this.add(this.divinite);
		this.add(this.champsDeBataille);
	}

	@Override
	public void majMain(Joueur joueur, List<Carte> main) {
		//Do Nothing
	}

	@Override
	public void majChampsDeBataille(Joueur joueur, List<Carte> champsDeBataille) {
		this.champsDeBataille.removeAll();
		for(int i=0;i<champsDeBataille.size();i++){
			this.champsDeBataille.add(new CardView(champsDeBataille.get(i), Sizes.BOT_CARD_SIZE));
		}
		this.champsDeBataille.revalidate();
		this.champsDeBataille.repaint();
		try {
			Thread.sleep(600);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
