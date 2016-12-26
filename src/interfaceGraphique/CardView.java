package interfaceGraphique;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import models.cartes.Carte;

public class CardView extends JPanel {
	ImageIcon image= new ImageIcon("image//fond_guides.jpg");
    Image originalImage=image.getImage();
	Image newImage=originalImage.getScaledInstance(200,300,Image.SCALE_DEFAULT);

	public CardView(Carte carte){
		this.setLayout(new BorderLayout());
	    image=new ImageIcon(newImage);
		JLabel nom=new JLabel(carte.getNom());
		JLabel origine=new JLabel(carte.getOrigine());
		this.add(nom,BorderLayout.PAGE_START);
		this.add(origine,BorderLayout.CENTER);
		this.setSize(200, 300);
		this.setVisible(true);
	}
	public void paintComponent(Graphics g) {
		g.drawImage(newImage, 0, 0, null);
		}

	public static void main(String[] args) {
		
		// TODO Auto-generated method stub
		
		Carte carte=new Carte("carte1","origine");
		CardView vueCarte=new CardView(carte);
		JFrame frame=new JFrame();
		frame.add(vueCarte);
		frame.setSize(200,300);
		frame.setVisible(true);
	
	}
}
