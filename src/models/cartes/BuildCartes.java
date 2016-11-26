package models.cartes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public abstract class BuildCartes {
	
	public static ArrayList<Carte> getCartes(){		
		ArrayList<Carte> deck = new ArrayList<Carte>();
		Document document = getXMLDocument();
		
		Element racine = document.getDocumentElement();
		NodeList deckNodes = getDeckChildsNodes(racine);
		
		//Pour chaque cartes
		for(int i=0;i<deckNodes.getLength();i++){
			if(deckNodes.item(i).getNodeType() == Node.ELEMENT_NODE && deckNodes.item(i).getNodeName().equals("card")){
				Node card = deckNodes.item(i);
				String template = card.getAttributes().getNamedItem("template").getTextContent();
				
				NodeList cardNodes = card.getChildNodes();
				//Pour chaque elements
				String nomCarte = null;
				String capaciteDesc = null;
				String carteDesc = null;
				String id = null;
				for(int j=0;j<cardNodes.getLength();j++){
					if(cardNodes.item(j).getNodeType() == Node.ELEMENT_NODE){
						//System.out.println(j);
						if(j == 1) nomCarte = cardNodes.item(j).getTextContent().trim();
						else if(j == 2) carteDesc = cardNodes.item(j).getTextContent().trim();
						else if(j == 3) capaciteDesc = cardNodes.item(j).getTextContent().trim();	
						else if(j == 5){
							if(cardNodes.item(j).getAttributes().getNamedItem("id") != null) id = cardNodes.item(j).getAttributes().getNamedItem("id").getTextContent();
							else carteDesc = cardNodes.item(j).getTextContent().trim();;
						}
					}
				}
				
				String templates[]  = template.split(",");
				//Instanciation d'une carte
				Origine origine = null;
				Integer nombre = null;
				Set<Dogme> dogmes = null;
				
				switch(templates[0]){
					case ConstanteCarte.CROYANT : 
						//System.out.println("Type Croyant");
						origine = pickOrigine(templates[1].replaceAll(" ", ""));
						nombre = pickNombre(id);
						dogmes = pickDogmes(templates[2].split("-")[1]);
						Croyant c = new Croyant(nomCarte,capaciteDesc,origine,nombre,dogmes);
						deck.add(c);
						//System.out.println(c);
						break;
					case ConstanteCarte.GUIDE : 
						//System.out.println("Type Guide");
						origine = pickOrigine(templates[1].replaceAll(" ", ""));
						nombre = pickNombre(id);
						dogmes = pickDogmes(templates[2].split("-")[1]);
						Guide_Spirituel g = new Guide_Spirituel(nomCarte,capaciteDesc,origine,nombre,dogmes);
						deck.add(g);
						//System.out.println(g);
						break;
					case ConstanteCarte.DEUSEX : 
						//System.out.println("Type Deus Ex");
						if(templates.length>1) origine = pickOrigine(templates[1].replaceAll(" ", ""));
						Deus_Ex d = new Deus_Ex(nomCarte, capaciteDesc, origine);
						deck.add(d);
						//System.out.println(d);
						break;
					case ConstanteCarte.APOCALYPSE : 
						//System.out.println("Type Apocalypse");
						if(templates.length>1) origine = pickOrigine(templates[1].replaceAll(" ", ""));
						Apocalypse a = new Apocalypse(origine);
						deck.add(a);
						//System.out.println(a);
						break;
					case ConstanteCarte.DIVINITE : 
						//System.out.println("Type Divinite");
						origine = pickOrigine(templates[1].replaceAll(" ", ""));
						dogmes = pickDogmes(templates[2].split("-")[2]);
						Divinite divinite = new Divinite(nomCarte, capaciteDesc, carteDesc, origine, dogmes);
						deck.add(divinite);
						//System.out.println(divinite);
						break;
					default : 
						System.out.println("Type inconnue");
						break;
				}
			}
		}
		return deck;
	}
	
	private static Document getXMLDocument() {
		//Les cartes étants stockées dans un document XML, nous allons parser ce document.
				//Création d'un DOM
				DocumentBuilderFactory dom = DocumentBuilderFactory.newInstance();
				Document document = null;
				//Création d'un Parseur
				try {
					DocumentBuilder builder = dom.newDocumentBuilder();
					File file = new File("src\\models\\cartes\\cartes.xml");
					document = builder.parse(file);
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
				return document;
	}
	
	private static NodeList getDeckChildsNodes(Element racine){
		NodeList rootsNodes = racine.getChildNodes();
		
		for(int i=0;i<rootsNodes.getLength();i++){
			if(rootsNodes.item(i).getNodeType() == Node.ELEMENT_NODE && rootsNodes.item(i).getNodeName().equals("deck")){
				return rootsNodes.item(i).getChildNodes();
			}
		}
		return null;
	}
	
	private static Origine pickOrigine(String origine){
		switch(origine){
		case ConstanteCarte.JOUR : return Origine.JOUR;
		case ConstanteCarte.NUIT : return Origine.NUIT;
		case ConstanteCarte.AUBE : return Origine.AUBE;
		case ConstanteCarte.NEANT : return Origine.NEANT;
		case ConstanteCarte.DIVINITE_JOUR : return Origine.JOUR;
		case ConstanteCarte.DIVINITE_NUIT : return Origine.NUIT;
		case ConstanteCarte.DIVINITE_AUBE : return Origine.AUBE;
		case ConstanteCarte.DIVINITE_NEANT : return Origine.NEANT;
		case ConstanteCarte.DIVINITE_CREPUSCULE : return Origine.CREPUSCULE;
		default : return null;
		}
	}
	
	private static Set<Dogme> pickDogmes(String dogmes){
		String[] listDogme = dogmes.split("_");
		Set<Dogme> dogmesSet = new HashSet<Dogme>();
		
		for(int i=0;i<listDogme.length;i++){
			if(listDogme[i].equals(ConstanteCarte.NATURE)) dogmesSet.add(Dogme.NATURE);
			if(listDogme[i].equals(ConstanteCarte.HUMAIN)) dogmesSet.add(Dogme.HUMAIN);
			if(listDogme[i].equals(ConstanteCarte.SYMBOLES)) dogmesSet.add(Dogme.SYMBOLES);
			if(listDogme[i].equals(ConstanteCarte.MYSTIQUE)) dogmesSet.add(Dogme.MYSTIQUE);
			if(listDogme[i].equals(ConstanteCarte.CHAOS)) dogmesSet.add(Dogme.CHAOS);
		}
		
		return dogmesSet;
		
	}
	
	private static Integer pickNombre(String id){
		switch(id){
		case "nbr_un" : return 1;
		case "nbr_deux" : return 2;
		case "nbr_trois" : return 3;
		case "nbr_quatre" : return 4;
		case "nbr_cinq" : return 5;
		default : return null;
		}
	}
}
