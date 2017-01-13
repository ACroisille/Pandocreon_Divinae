package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import controller.Gestionnaire_cartes_partie;
import controller.listeners.PartieCardUpdateListener;
import models.cartes.Carte;
import models.cartes.Croyant;
import models.joueur.Joueur;
import models.joueur.JoueurVirtuel;

public class TableUI extends JPanel implements PartieCardUpdateListener{

	public Map<JoueurVirtuel,AffichageJV> champsDeBataillePanel;
	public JPanel joueursVirtuelUI;
	public JPanel cartesTable;
	public JPanel cartesSpeciales;
	
	public JPanel piochePanel;
	public JPanel defaussePanel;
	public JPanel sacrificePanel;
	
	public CardView pioche;
	public CardView defausse;
	public CardView sacrifice;
	
	public TableUI(Set<Joueur> joueurs, Dimension d){
		
		Gestionnaire_cartes_partie.addPartieCardUpdateListener(this);
		
		this.setLayout(new BorderLayout());
		
		this.cartesSpeciales = new JPanel(new GridLayout(3, 1));
		
		this.piochePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		this.pioche = new CardView(Sizes.HUMAN_CARD_SIZE);
		this.piochePanel.add(this.pioche);
		this.piochePanel.setBorder(this.createTitledBorder("Pioche"));
		
		this.defaussePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		this.defausse = new CardView(Sizes.HUMAN_CARD_SIZE);
		this.defaussePanel.add(this.defausse);
		this.defaussePanel.setBorder(this.createTitledBorder("Defausse"));
		
		this.sacrificePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		this.sacrifice = new CardView(Sizes.HUMAN_CARD_SIZE);
		this.sacrificePanel.add(this.sacrifice);
		this.sacrificePanel.setBorder(this.createTitledBorder("Sacrifices"));
		
		this.cartesSpeciales.add(this.piochePanel);
		this.cartesSpeciales.add(this.defaussePanel);
		this.cartesSpeciales.add(this.sacrificePanel);
		this.add(this.cartesSpeciales,BorderLayout.EAST);
		
		this.cartesTable = new JPanel(new FlowLayout(FlowLayout.CENTER));
		this.cartesTable.setBorder(this.createTitledBorder("Cartes au centre de la table"));
		this.cartesTable.setBackground(new Color(37, 140, 6));
		this.add(this.cartesTable, BorderLayout.CENTER);
		
		this.champsDeBataillePanel = new HashMap<JoueurVirtuel,AffichageJV>();
		this.joueursVirtuelUI = new JPanel();
		this.joueursVirtuelUI.setLayout(new BoxLayout(this.joueursVirtuelUI,BoxLayout.Y_AXIS));
		
		Iterator<Joueur> it = joueurs.iterator();
		while(it.hasNext()){
			Joueur j = it.next();
			if(j instanceof JoueurVirtuel){
				this.champsDeBataillePanel.put((JoueurVirtuel) j,new AffichageJV((JoueurVirtuel)j, d));
				this.joueursVirtuelUI.add(champsDeBataillePanel.get(j));
			}
		}
		
		this.add(this.joueursVirtuelUI, BorderLayout.WEST);
	}

	@Override
	public void majTable(List<Croyant> table) {
		this.cartesTable.removeAll();
		for(int i=0;i<Gestionnaire_cartes_partie.getTable().size();i++){
			this.cartesTable.add(new CardView(Gestionnaire_cartes_partie.getTable().get(i), Sizes.HUMAN_CARD_SIZE));
		}
		this.cartesTable.revalidate();
		this.cartesTable.repaint();
		try {
			Thread.sleep(600);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void majSacrifice(Carte carte) {
		this.sacrificePanel.removeAll();
		if(carte != null){		
			this.sacrificePanel.add(new CardView(carte, Sizes.HUMAN_CARD_SIZE));
		}else{
			this.sacrificePanel.add(new CardView(Sizes.HUMAN_CARD_SIZE));
		}
		this.sacrificePanel.revalidate();
		this.sacrificePanel.repaint();
		try {
			Thread.sleep(600);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void majDefausse(Carte carte) {
		this.defaussePanel.removeAll();
		this.defaussePanel.add(new CardView(carte, Sizes.HUMAN_CARD_SIZE));
		this.defaussePanel.revalidate();
		this.defaussePanel.repaint();
	}

	private TitledBorder createTitledBorder(String title){
		Border blackline = BorderFactory.createLineBorder(Color.black);
		TitledBorder titleBorder = BorderFactory.createTitledBorder(blackline, title);
		titleBorder.setTitleJustification(TitledBorder.LEFT);
		return titleBorder;
	}
}
