package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import controller.listeners.CardClickListener;
import models.cartes.Apocalypse;
import models.cartes.Carte;
import models.cartes.Croyant;
import models.cartes.Deus_Ex;
import models.cartes.Divinite;
import models.cartes.Guide_Spirituel;
import models.cartes.Religion;
import models.enums.Dogme;

public class CardView extends JPanel implements MouseListener{
	
	private Carte carte;
	private int size = 200;
	public static final String PATH = "ressources/";
	private boolean surbrillance=false;
	
	private CardClickListener cardClickListener;
	
	public CardView(Carte carte,int size){
		this.carte = carte;
		this.size = size;
		this.addMouseListener(this);
		
		if(carte instanceof Divinite){
			this.setPreferredSize(new Dimension(size,(size/3)*2));
			this.setMaximumSize(new Dimension(size,(size/3)*2));
		}
		else this.setPreferredSize(new Dimension((size/3)*2, size));
		
		this.setLayout(new BorderLayout());
		JPanel northPanel = new JPanel();
		northPanel.setLayout(new BorderLayout());
		StringBuffer buf;
		if(carte instanceof Croyant || carte instanceof Guide_Spirituel || carte instanceof Divinite){
			JPanel dogmes = new JPanel();
			dogmes.setLayout(new GridLayout(2,2));
			
			Iterator<Dogme> it = null;
			if(carte instanceof Divinite){
				it =  ((Divinite)carte).getSesDogmes().iterator();
			}
			else it = ((Religion)carte).getSesDogmes().iterator();
			while(it.hasNext()){
				buf = new StringBuffer();
				buf.append(PATH).append(it.next().getName()).append(".jpg");
				//System.out.println(buf.toString());
				dogmes.add(new ImagePanel(buf.toString(), size/12, size/12));
			}
			northPanel.add(dogmes, BorderLayout.WEST);
		}
		
		if(carte.getOrigine() != null){
			buf = new StringBuffer();
			buf.append(PATH).append(carte.getOrigine().getOrigineName()).append(".jpg");
			northPanel.add(new ImagePanel(buf.toString(), size/6, size/6),BorderLayout.EAST);
		}
		
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout());
		
		JLabel nom = new JLabel(carte.getNom(),SwingConstants.CENTER);
		centerPanel.add(nom, BorderLayout.NORTH);
		
		JPanel sacrificePanel = new JPanel();
		sacrificePanel.setLayout(new BorderLayout());
		buf = new StringBuffer();
		buf.append("<html>Sacrifice :<br>").append(" ").append(carte.getCapaciteDesc()).append("</html>");
		JLabel desc = new JLabel(buf.toString(),SwingConstants.CENTER);
		
		sacrificePanel.add(desc,BorderLayout.NORTH);
		centerPanel.add(sacrificePanel,BorderLayout.CENTER);
		
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new BorderLayout());
		if(carte instanceof Croyant || carte instanceof Guide_Spirituel){
			String nbr = null;
			switch(((Religion)carte).getNombre()){
			case 1 : nbr = "nbr_un";
				break;
			case 2 : nbr = "nbr_deux";
				break;
			case 3 : nbr = "nbr_trois";
				break;
			case 4 : nbr = "nbr_quatre";
				break;
			case 5 : nbr = "nbr_cinq";
				break;
			default : break;
			}
			buf = new StringBuffer();
			buf.append(PATH).append(nbr).append(".jpg");
			southPanel.add(new ImagePanel(buf.toString(), size/10, size/8), BorderLayout.EAST);
		}
		String type = null;
		if(carte instanceof Apocalypse) type = "Apocalypse";
		else if(carte instanceof Croyant) type = "Croyant";
		else if(carte instanceof Guide_Spirituel) type = "Guide Spirituel";
		else if(carte instanceof Deus_Ex) type = "Deus Ex";
		else if(carte instanceof Divinite) type = "Divinite";
		southPanel.add(new JLabel(type),BorderLayout.WEST);
		
		this.add(northPanel, BorderLayout.NORTH);
		this.add(centerPanel, BorderLayout.CENTER);
		this.add(southPanel, BorderLayout.SOUTH);
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK,2,true));
		this.setVisible(true);
	}
	
	public CardView(int size){
		this.size = size;
		int width, height;
		width = (size/3)*2;
		height = size;
		this.setPreferredSize(new Dimension(width, height));
		this.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		StringBuffer buf = new StringBuffer();
		buf.append(PATH).append("back.jpg");
		JPanel back = new ImagePanel(buf.toString(), width, height);
		
		this.add(back);
		this.setVisible(true);
	}
	
	public Carte getCarte() {
		return carte;
	}
	
	public void setSurbrillance(boolean put){
		if(put == true){
			//On met la carte en surbrillance
			this.setBorder(BorderFactory.createLineBorder(Color.GREEN,2,true));
			
		}
		else{
			//On enleve la surbrillance de la carte
			this.setBorder(BorderFactory.createLineBorder(Color.BLACK,2,true));
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(this.cardClickListener != null){
			this.cardClickListener.cardClicked(this.carte);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	public void addCardClickListener(CardClickListener cardClickListener){
		this.cardClickListener = cardClickListener;
	}
	
	public void removeCardClickListener(){
		this.cardClickListener = null;
	}
}
