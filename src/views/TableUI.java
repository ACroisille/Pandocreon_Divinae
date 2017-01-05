package views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import controller.Gestionnaire_cartes_partie;
import controller.PartieCardUpdateListener;
import models.cartes.Croyant;
import models.joueur.Joueur;
import models.joueur.JoueurVirtuel;

public class TableUI extends JPanel implements PartieCardUpdateListener{

	public Map<JoueurVirtuel,AffichageJV> champsDeBataillePanel;
	public JPanel joueursVirtuelUI;
	public JPanel cartesTable;
	
	public TableUI(Set<Joueur> joueurs, Dimension d){
		
		Gestionnaire_cartes_partie.addPartieCardUpdateListener(this);
		
		this.setLayout(new BorderLayout());
		
		this.cartesTable = new JPanel(new FlowLayout(FlowLayout.CENTER));
		this.add(this.cartesTable, BorderLayout.EAST);
		
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	
}
