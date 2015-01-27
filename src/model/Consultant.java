package model;

public class Consultant {
	private String nom;
	private String prenom;
	private String adresse;
	private String telephone;
	
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

	@Override
	public String toString() {
		return "Consultant [nom=" + nom + ", prenom=" + prenom + ", adresse="
				+ adresse + ", telephone=" + telephone + "]";
	}			
}
