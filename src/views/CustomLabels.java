package views;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JLabel;

public class CustomLabels extends JLabel{
	
	private String text;
	private int rotation;
	private int x, y;
	private int width, height;
	private int fontSize;
	
	public CustomLabels(String text,int rotation, int x, int y, int width, int height, int fontSize) {
		this.text = text;
		this.rotation = rotation;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.fontSize = fontSize;
		this.setPreferredSize(new Dimension(width, height));
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		Font helvetica = new Font ("Helvetica", Font.BOLD, fontSize);
		g2.setFont(helvetica);
		g2.rotate(Math.toRadians(rotation));
		g2.drawString(text, x, y);
	}
}
