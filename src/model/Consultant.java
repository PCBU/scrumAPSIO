package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class Consultant implements Serializable {
	private String nom;
	private String prenom;
	private String adresse;
	private String telephone;
	private ArrayList<String> competences;
	
	public Consultant() {
		super();
	}

	public Consultant(String nom, String prenom, String adresse,
			String telephone) {
		super();
		this.nom = nom;
		this.prenom = prenom;
		this.adresse = adresse;
		this.telephone = telephone;
		this.competences = new ArrayList<String>();
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public void ajouterCompetence(String comp) {
		competences.add(comp);
	}

	public  void retirerCompetence(String comp){
		for(String val : competences){
			Iterator<String> iterator = competences.iterator();
			while (iterator.hasNext()){
				String a = iterator.next();
				if(a == comp){
					iterator.remove();
				}
			}
		}
	}
	@Override
	public String toString() {
		return "Consultant [nom=" + nom + ", prenom=" + prenom + ", adresse="
				+ adresse + ", telephone=" + telephone + "Liste compétence" + competences + "]";
	}			
}
