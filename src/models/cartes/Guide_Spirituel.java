package models.cartes;

import java.util.ArrayList;
import java.util.Set;

import controller.Capacite;

public class Guide_Spirituel extends Religion{
	private ArrayList<Croyant> sesCroyants;
	
	public Guide_Spirituel(String nom, String capaciteDesc, Origine origine, Integer nombre,
			Set<Dogme> sesDogmes) {
		super(nom, capaciteDesc, origine, nombre, sesDogmes);
		// TODO Auto-generated constructor stub
		sesCroyants = new ArrayList<Croyant>();
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer buf = new StringBuffer();
		buf.append("GUIDE : ").append(this.nom).append(" ").append(this.capaciteDesc).append(" ").append(this.origine).append(" ").append(this.sesDogmes.toString());
		return buf.toString();
	}

}
