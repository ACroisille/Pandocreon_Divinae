package views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.JoueurCardUpdateListener;
import models.cartes.Carte;
import models.enums.Origine;
import models.joueur.Joueur;
import models.joueur.JoueurReel;

public class JoueurReelUI extends JPanel implements JoueurCardUpdateListener{
	
	public JPanel champsDeBataillePanel;
	public JPanel southPanel;
	
	public JPanel divinitePanel;
	public JPanel mainPanel;
	public JPanel infoPanel;
	
	public JLabel pointsJour;
	public JLabel pointsNuit;
	public JLabel pointsNeant;
	
	public JoueurReelUI(JoueurReel j, Dimension d){
		
		j.getGestionnaire_Cartes_Joueur().addListCartesListener(this);
		
		this.setLayout(new BorderLayout());
		
		divinitePanel = new JPanel(new FlowLayout());
		divinitePanel.add(new CardView(j.getGestionnaire_Cartes_Joueur().getDivinite(), Sizes.HUMAN_CARD_SIZE));
		
		mainPanel = new JPanel(new FlowLayout());
		for(int i=0;i<j.getGestionnaire_Cartes_Joueur().getMain().size();i++){
			mainPanel.add(new CardView(j.getGestionnaire_Cartes_Joueur().getMain().get(i),Sizes.HUMAN_CARD_SIZE));
		}
		
		infoPanel = new JPanel(new GridLayout(3, 3));
		infoPanel.add(new ImagePanel("ressources/jour.jpg", d.width/24, d.height/24));
		pointsJour = new JLabel(j.getPointsAction().get(Origine.JOUR).toString());
		infoPanel.add(pointsJour);
		infoPanel.add(new ImagePanel("ressources/nuit.jpg", d.width/24, d.height/24));
		pointsNuit = new JLabel(j.getPointsAction().get(Origine.NUIT).toString());
		infoPanel.add(pointsNuit);
		infoPanel.add(new ImagePanel("ressources/neant.jpg", d.width/24, d.height/24));
		pointsNeant = new JLabel(j.getPointsAction().get(Origine.NEANT).toString());
		infoPanel.add(pointsNeant);
		
		southPanel = new JPanel(new BorderLayout());
		//southPanel.setPreferredSize(new Dimension(d.width, d.height/4));
		southPanel.add(divinitePanel,BorderLayout.WEST);
		southPanel.add(mainPanel,BorderLayout.CENTER);
		southPanel.add(infoPanel,BorderLayout.EAST);
		
		champsDeBataillePanel = new JPanel(new FlowLayout());
		//champsDeBataillePanel.setPreferredSize(new Dimension(d.width, d.height/4));
		
		this.add(southPanel, BorderLayout.SOUTH);
		this.add(champsDeBataillePanel,BorderLayout.NORTH);
	}

	@Override
	public void majMain(Joueur joueur, List<Carte> main) {
		this.mainPanel.removeAll();
		for(int i=0;i<main.size();i++){
			this.mainPanel.add(new CardView(main.get(i),Sizes.HUMAN_CARD_SIZE));
		}
		this.mainPanel.revalidate();
		this.mainPanel.repaint();
	}

	@Override
	public void majChampsDeBataille(Joueur joueur, List<Carte> champsDeBataille) {
		this.champsDeBataillePanel.removeAll();
		for(int i=0;i<champsDeBataille.size();i++){
			this.champsDeBataillePanel.add(new CardView(champsDeBataille.get(i), Sizes.HUMAN_CARD_SIZE));
		}
		this.champsDeBataillePanel.revalidate();
		this.champsDeBataillePanel.repaint();
	}
	
}
