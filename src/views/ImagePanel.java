package views;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel extends JPanel{
	
	private BufferedImage image;
	private int width, height;
	
	public ImagePanel(String path, int width, int height){
		this.width = width;
		this.height = height;
		try {                
	          image = ImageIO.read(new File(getClass().getResource(path).toURI()));
	       } catch (IOException | URISyntaxException ex) {
	            ex.printStackTrace();
	       }
		this.setPreferredSize(new Dimension(width, height));
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		g.drawImage(image, 0, 0, width, height, this);
	}
}
