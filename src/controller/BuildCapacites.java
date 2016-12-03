package controller;

import java.util.HashMap;
import java.util.Set;

import models.joueur.Joueur;

public abstract class BuildCapacites {
	
	private static HashMap<String, Capacite> capacites;
	
	public static void loadCapacites(){
		capacites = new HashMap<String,Capacite>();
		
		capacites.put("cap", new Capacite() {
			
			@Override
			public void capacite(Set<Joueur> joueurs) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public static Capacite getCapacite(String key){
		return  capacites.get(key);
	}
	
}
