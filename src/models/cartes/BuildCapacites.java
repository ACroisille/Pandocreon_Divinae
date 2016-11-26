package models.cartes;

import java.util.HashMap;

import controller.Capacite;
import models.joueur.Joueur;

public abstract class BuildCapacites {
	
	private static HashMap<String, Capacite> capacites;
	
	public static void loadCapacites(){
		capacites = new HashMap<String,Capacite>();
		
		capacites.put("ajouterPointJoueur", new Capacite() {
			@Override
			public void capacite(Joueur j) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public static Capacite getCapacite(String key){
		return  capacites.get(key);
	}
	
}
