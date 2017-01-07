package views;

import java.awt.Dimension;
import java.awt.Image;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import models.enums.Origine;
import models.De_Cosmogonie;


public class DiceView extends JPanel {

	public DiceView(){
		JLabel titre=new JLabel("Dé Cosmogonie");
		titre.setAlignmentX(CENTER_ALIGNMENT);
		titre.setSize(new Dimension(100,100));
		Box.createHorizontalBox();
		this.add(titre);
		Origine origine=De_Cosmogonie.getOrigine();
		switch(origine){
		
		case JOUR:
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			ImageIcon image= new ImageIcon("src//views//ressources//jour.jpg");
			Image originalImage=image.getImage();
			Image newImage=originalImage.getScaledInstance(120,120,Image.SCALE_DEFAULT);
			image=new ImageIcon(newImage);
			JLabel de= new JLabel(image);
			de.setAlignmentX(CENTER_ALIGNMENT);
			this.add(de);
			break;
		case NUIT:
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			ImageIcon image1= new ImageIcon("src//views//ressources//nuit.jpg");
			Image originalImage1=image1.getImage();
			Image newImage1=originalImage1.getScaledInstance(120,120,Image.SCALE_DEFAULT);
			image1=new ImageIcon(newImage1);
			JLabel de1= new JLabel(image1);
			de1.setAlignmentX(CENTER_ALIGNMENT);
			this.add(de1);
			break;
		
		case NEANT:
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			ImageIcon image11= new ImageIcon("src//views//ressources//neant.jpg");
			Image originalImage11=image11.getImage();
			Image newImage11=originalImage11.getScaledInstance(120,120,Image.SCALE_DEFAULT);
			image11=new ImageIcon(newImage11);
			JLabel de11= new JLabel(image11);
			de11.setAlignmentX(CENTER_ALIGNMENT);
			this.add(de11);
			break;
			
		default:
			break;
		}
		this.setVisible(true);
	}
}
