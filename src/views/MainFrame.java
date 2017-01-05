package views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.sun.glass.ui.Size;

import controller.JoueurCardUpdateListener;
import controller.PartieCardUpdateListener;
import models.Partie;
import models.cartes.Carte;
import models.cartes.Croyant;
import models.joueur.Joueur;
import models.joueur.JoueurReel;
import models.joueur.JoueurVirtuel;

public class MainFrame extends JFrame {
	
	private Partie partie;
	
	private JoueurReelUI joueurReelUI;
	private TableUI tableUI;
	
	
	private Dimension screenSize;
	
	public MainFrame(Partie partie){
		super("MainFrame");
		this.partie = partie;
		
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(screenSize);
		this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		this.setLayout(new BorderLayout());
		
		Iterator<Joueur> it = Partie.getJoueurs().iterator();
		while(it.hasNext()){
			Joueur j = it.next();
			if(j instanceof JoueurReel){
				joueurReelUI = new JoueurReelUI((JoueurReel)j, screenSize);
				this.add(joueurReelUI,BorderLayout.SOUTH);
			}
			
		}
		tableUI = new TableUI(Partie.getJoueurs(), screenSize);
		this.add(tableUI, BorderLayout.NORTH);
		
		this.setVisible(true);
	}

}
