package models.cartes;

import java.util.Set;

public class Divinite extends Carte{
	private String carteDesc;
	private Set<Dogme> sesDogmes;
	
	public Divinite(String nom, String carteDesc,String capaciteDesc, Origine origine,Set<Dogme> sesDogmes) {
		super(nom, capaciteDesc, origine);
		// TODO Auto-generated constructor stub
		this.carteDesc = carteDesc;
		this.sesDogmes = sesDogmes;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer buf = new StringBuffer();
		buf.append(this.nom).append(" ").append(carteDesc).append(" ").append(this.capaciteDesc).append(" ").append(this.origine).append(" ").append(this.sesDogmes.toString());
		return buf.toString();
	}
}
