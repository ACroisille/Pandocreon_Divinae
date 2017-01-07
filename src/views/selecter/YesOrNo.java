package views.selecter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class YesOrNo extends Thread implements ActionListener{

	private Boolean yesOrNo = null;
	private JButton yes, no;
	
	public YesOrNo(JButton yes, JButton no) {
		this.yes = yes;
		this.no = no;
		yes.addActionListener(this);
		no.addActionListener(this);
	}
	
	@Override
	public void run() {
		this.aquiringResponse();
	}
	
	public synchronized void aquiringResponse(){
		while(yesOrNo == null){
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public synchronized void pass(boolean answer){
		this.yesOrNo = answer;
		notify();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(this.yes)){
			this.pass(true);
		}
		else if(e.getSource().equals(this.no)){
			this.pass(false);
		}
	}
	
	public Boolean getYesOrNo() {
		return yesOrNo;
	}
}
