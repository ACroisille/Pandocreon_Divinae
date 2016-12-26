package views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Iterator;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import models.cartes.Apocalypse;
import models.cartes.Carte;
import models.cartes.Croyant;
import models.cartes.Deus_Ex;
import models.cartes.Divinite;
import models.cartes.Guide_Spirituel;
import models.cartes.Religion;
import models.enums.Dogme;

public class CardView extends JPanel {
	
	private int size = 300;
	

	public CardView(Carte carte){
		final String PATH = "ressources/";
		this.setPreferredSize(new Dimension((size/3)*2, size));
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
		this.setVisible(true);
		
	}
	

}
