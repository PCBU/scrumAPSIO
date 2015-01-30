package model;

import java.lang.Override;
import java.lang.String;
import java.util.Date;

public class Mission {

	/**
	 * Consultant unique associé à la mission
	 */
	private Consultant consultant;

	/**
	 * Date de début
	 */
	private Date debut;

	/**
	 * Date de fin
	 */
	private Date fin;

	/**
	 * Intitulé de la mission
	 */
	private String intitule;

	/**
	 * Client associé à la mission
	 */
	private Client client;

	public Mission(Consultant consultant, Date debut, Date fin, String intitule, Client client) {
		this.consultant = consultant;
		this.debut = debut;
		this.fin = fin;
		this.intitule = intitule;
		this.client = client;
	}

	public Consultant getConsultant() {
		return consultant;
	}

	public void setConsultant(Consultant consultant) {
		this.consultant = consultant;
	}

	public Date getDebut() {
		return debut;
	}

	public void setDebut(Date debut) {
		this.debut = debut;
	}

	public Date getFin() {
		return fin;
	}

	public Client getClient() {
		return client;
	}

	@Override
	public String toString() {
		return "Mission{" +
				"consultant=" + consultant +
				", debut=" + debut +
				", fin=" + fin +
				", intitule='" + intitule + '\'' +
				'}';
	}
}
