package views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import com.sun.org.apache.xpath.internal.operations.Or;

import controller.listeners.CardClickListener;
import controller.listeners.JoueurCardPeekerListener;
import controller.listeners.JoueurCardUpdateListener;
import controller.listeners.JoueurPointsActionsListener;
import models.cartes.Carte;
import models.enums.Origine;
import models.joueur.Joueur;
import models.joueur.JoueurReel;
import models.joueur.JoueurVirtuel;
import sun.dc.pr.PathStroker;
import views.selecter.CardPeeker;
import views.selecter.YesOrNo;

public class JoueurReelUI extends JPanel implements JoueurCardUpdateListener, JoueurCardPeekerListener, JoueurPointsActionsListener{
	
	public JPanel champsDeBataillePanel;
	public JPanel southPanel;
	
	public JPanel divinitePanel;
	public JPanel mainPanel;
	public JPanel infoPanel;
	public JPanel phasePanel;
	
	public JButton passBtn;
	public JLabel phaseInfoLbl;
	
	public JLabel pointsJour;
	public JLabel pointsNuit;
	public JLabel pointsNeant;
	
	
	public JoueurReelUI(JoueurReel j, Dimension d){
		
		j.getGestionnaire_Cartes_Joueur().addListCartesListener(this);
		j.addJoueurCardPeekerListener(this);
		j.addJoueurPointsActionsListener(this);
		
		this.setLayout(new BorderLayout());
		Border loweredbevel = BorderFactory.createLoweredBevelBorder();
		this.setBorder(loweredbevel);
		
		divinitePanel = new JPanel(new FlowLayout());
		divinitePanel.add(new CardView(j.getGestionnaire_Cartes_Joueur().getDivinite(), Sizes.HUMAN_CARD_SIZE));
		
		mainPanel = new JPanel(new FlowLayout());
		for(int i=0;i<j.getGestionnaire_Cartes_Joueur().getMain().size();i++){
			CardView cardView = new CardView(j.getGestionnaire_Cartes_Joueur().getMain().get(i),Sizes.HUMAN_CARD_SIZE);
			mainPanel.add(cardView);
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
		
		phasePanel = new JPanel();
		phasePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		passBtn = new JButton("Passer");
		phaseInfoLbl = new JLabel();
		phasePanel.add(passBtn);
		phasePanel.add(phaseInfoLbl);
		
		this.add(southPanel, BorderLayout.SOUTH);
		this.add(champsDeBataillePanel,BorderLayout.CENTER);
		this.add(phasePanel,BorderLayout.NORTH);
	}

	@Override
	public void majMain(Joueur joueur, List<Carte> main) {
		
		for(int j=0;j<this.mainPanel.getComponentCount();j++){
			((CardView)this.mainPanel.getComponent(j)).dispose();
			
		}
		this.mainPanel.removeAll();
		
		for(int i=0;i<main.size();i++){
			this.mainPanel.add(new CardView(main.get(i),Sizes.HUMAN_CARD_SIZE));
		}
		this.mainPanel.revalidate();
		this.mainPanel.repaint();
	}

	@Override
	public void majChampsDeBataille(Joueur joueur, List<Carte> champsDeBataille) {
		
		for(int j=0;j<this.champsDeBataillePanel.getComponentCount();j++){
			((CardView)this.champsDeBataillePanel.getComponent(j)).dispose();
		}
		this.champsDeBataillePanel.removeAll();
		
		for(int i=0;i<champsDeBataille.size();i++){
			this.champsDeBataillePanel.add(new CardView(champsDeBataille.get(i), Sizes.HUMAN_CARD_SIZE));
		}
		this.champsDeBataillePanel.revalidate();
		this.champsDeBataillePanel.repaint();
	}
	
	@Override
	public void afficherMessage(String msg) {
		this.phaseInfoLbl.setText(msg);
	}
	
	@Override
	public Carte cardPeekerMain(List<Carte> cartes) {
		this.passBtn.setVisible(true);
		List<CardView> cardViews = new ArrayList<CardView>();
		//Fait apparaitre en vert les cartes pouvant être joué
		for(int i=0;i<this.mainPanel.getComponentCount();i++){
			if(cartes.contains(((CardView)this.mainPanel.getComponent(i)).getCarte())){
				((CardView)this.mainPanel.getComponent(i)).setSurbrillance(true);
				cardViews.add(((CardView)this.mainPanel.getComponent(i)));
			}
		}
		
		Carte carte = selectCard(cardViews);
		
		//Remettre le bord des cartes en noir
		for(int i=0;i<this.mainPanel.getComponentCount();i++){
				((CardView)this.mainPanel.getComponent(i)).setSurbrillance(false);
		}
		
		return carte;
	}
	
	@Override
	public Carte cardPeekerChampsDeBataille(List<Carte> cartes) {
		this.passBtn.setVisible(true);
		List<CardView> cardViews = new ArrayList<CardView>();
		//Fait apparaitre en vert les cartes pouvant être joué
		for(int i=0;i<this.champsDeBataillePanel.getComponentCount();i++){
			if(cartes.contains(((CardView)this.champsDeBataillePanel.getComponent(i)).getCarte())){
				((CardView)this.champsDeBataillePanel.getComponent(i)).setSurbrillance(true);
				cardViews.add(((CardView)this.champsDeBataillePanel.getComponent(i)));
			}
		}
		
		Carte carte = selectCard(cardViews);
		
		//Remettre le bord des cartes en noir
		for(int i=0;i<this.champsDeBataillePanel.getComponentCount();i++){
			((CardView)this.champsDeBataillePanel.getComponent(i)).setSurbrillance(false);
		}
		
		return carte;
	}
	
	@Override
	public Joueur joueurPeeker(Set<Joueur> joueurs) {
		this.passBtn.setVisible(true);
		
		List<CardView> cardViews = new ArrayList<CardView>();
		Iterator<Joueur> it = joueurs.iterator();
		while(it.hasNext()){
			Joueur j = it.next();
			if(j instanceof JoueurReel){
				cardViews.add((CardView) this.divinitePanel.getComponent(0));
				((CardView) this.divinitePanel.getComponent(0)).setSurbrillance(true);
			}
			else if(j instanceof JoueurVirtuel){
				cardViews.add(((MainFrame) SwingUtilities.getWindowAncestor(this)).tableUI.champsDeBataillePanel.get((JoueurVirtuel)j).divinite);
				((MainFrame) SwingUtilities.getWindowAncestor(this)).tableUI.champsDeBataillePanel.get((JoueurVirtuel)j).divinite.setSurbrillance(true);
			}
		}
		
		Carte divinite = this.selectCard(cardViews);
		Joueur elu = null;
		//Si la divinité est null c'est que le joueur a cliqué sur passer
		if(divinite != null){	
			it = joueurs.iterator();
			while(it.hasNext()){
				Joueur j = it.next();
				if(j.getGestionnaire_Cartes_Joueur().getDivinite().equals(divinite)){
					elu = j;
					break;
				}
			}
		}
		
		for(int i=0;i<cardViews.size();i++){
			cardViews.get(i).setSurbrillance(false);
		}
		
		return elu;
	}
	
	private Carte selectCard(List<CardView> cardViews){
		CardPeeker peeker = new CardPeeker(cardViews, this.passBtn);
		peeker.start();
		synchronized (peeker) {
			try {
				peeker.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//System.out.println(peeker.getClicked());
		return peeker.getClicked();
	}

	
	@Override
	public boolean yesOrNo() {
		this.passBtn.setVisible(false);
		
		JButton yes = new JButton("Oui");
		JButton no = new JButton("Non");
		
		this.phasePanel.add(yes);
		this.phasePanel.add(no);
		this.phasePanel.revalidate();
		this.phasePanel.repaint();
		
		YesOrNo yon = new YesOrNo(yes, no);
		yon.start();
		
		synchronized (yon) {
			try {
				yon.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		this.phasePanel.remove(yes);
		this.phasePanel.remove(no);
		this.phasePanel.revalidate();
		this.phasePanel.repaint();
		
		return yon.getYesOrNo();
	}

	@Override
	public void updatePointsActions(Map<Origine, Integer> pointsAction) {
		this.pointsJour.setText(pointsAction.get(Origine.JOUR).toString());
		this.pointsNuit.setText(pointsAction.get(Origine.NUIT).toString());
		this.pointsNeant.setText(pointsAction.get(Origine.NEANT).toString());
		this.infoPanel.revalidate();
		this.infoPanel.repaint();
	}

	
	
}
