package views.selecter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;

import controller.listeners.CardClickListener;
import models.cartes.Carte;
import views.CardView;

public class CardPeeker extends Thread implements CardClickListener,ActionListener{
	
	private Carte clicked=null;
	private boolean pass = false;
	
	public CardPeeker(List<CardView> cardViews, JButton pass){
		for(int i=0;i<cardViews.size();i++){
			cardViews.get(i).addCardClickListener(this);
		}
		pass.addActionListener(this);
	}
	
	@Override
	public void run() {
		this.acquireCard();
	}

	public synchronized void pushCard(Carte carte){
		this.clicked = carte;
		notify();
	}
	
	public synchronized void pass(){
		this.pass = true;
		notify();
	}
	
	public synchronized void acquireCard(){
		while(clicked == null && pass == false){
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void cardClicked(Carte carte) {
		this.pushCard(carte);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.pass();
	}
	
	public Carte getClicked() {
		return clicked;
	}

}
