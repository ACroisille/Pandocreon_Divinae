package views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import controller.listeners.JoueurCardUpdateListener;
import models.cartes.Carte;
import models.joueur.Joueur;
import models.joueur.JoueurVirtuel;

public class AffichageJV extends JPanel implements JoueurCardUpdateListener{
	
	public CardView divinite;
	public JPanel champsDeBataille;
	public JScrollPane scrollPane;
	
	public AffichageJV(JoueurVirtuel j, Dimension d){
		j.getGestionnaire_Cartes_Joueur().addListCartesListener(this);
		
		this.setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		
		Border blackline = BorderFactory.createLineBorder(Color.black);
		TitledBorder title = BorderFactory.createTitledBorder(blackline, "Joueur Virtuel " + j.getNumJoueur());
		title.setTitleJustification(TitledBorder.LEFT);
		this.setBorder(title);
		
		this.divinite = new CardView(j.getGestionnaire_Cartes_Joueur().getDivinite(), Sizes.BOT_CARD_SIZE);
		
		champsDeBataille = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		scrollPane = new JScrollPane(champsDeBataille);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		scrollPane.setMaximumSize(new Dimension(d.width/4, d.height/4));
		
		this.add(this.divinite);
		this.add(scrollPane);
	}

	@Override
	public void majMain(Joueur joueur, List<Carte> main) {
		//Do Nothing
	}

	@Override
	public void majChampsDeBataille(Joueur joueur, List<Carte> champsDeBataille) {
		//Libération de la mémoire
		for(int j=0;j<this.champsDeBataille.getComponentCount();j++){
			((CardView)this.champsDeBataille.getComponent(j)).dispose();
		}
		this.champsDeBataille.removeAll();
		
		for(int i=0;i<champsDeBataille.size();i++){
			this.champsDeBataille.add(new CardView(champsDeBataille.get(i), Sizes.BOT_CARD_SIZE));
		}
		
		try {
			Thread.sleep(600);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
