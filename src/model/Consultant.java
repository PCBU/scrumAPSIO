package model;

import javafx.util.Pair;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class Consultant implements Serializable {
	private String nom;
	private String prenom;
	private String adresse;
	private String telephone;
	private ArrayList<String> competences;
	private ArrayList<Pair<DateTime, DateTime>> abscences;
	
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
		this.abscences = new ArrayList<Pair<DateTime, DateTime>>();
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

	public  boolean retirerCompetence(String comp){
		boolean passage = false;
		Iterator<String> iterator = competences.iterator();

		while (iterator.hasNext() && !passage) {
			String a = iterator.next();
			if(a.equals(comp)) {
				iterator.remove();
				passage = true;
			}
		}

		return passage;
	}

	public void ajoutAbscence(DateTime debut, DateTime fin){
		abscences.add(new Pair<DateTime, DateTime>(debut, fin));
	}

	public void retirerAbscence(DateTime debut){
		int i = 0;
		for(Pair<DateTime, DateTime> l : abscences){

			if(l.getKey().equals(debut)){
				abscences.remove(i);
			}
			i++;
		}

	}
	@Override
	public String toString() {
		return "Consultant [nom=" + nom + ", prenom=" + prenom + ", adresse="
				+ adresse + ", telephone=" + telephone + "Liste compétences : " + competences +
				"Abscence : " + abscences +"]";
	}			
}
