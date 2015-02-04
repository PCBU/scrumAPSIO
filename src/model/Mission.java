package model;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.lang.Override;
import java.lang.String;
import java.util.Date;

public class Mission implements Serializable {

	/**
	 * Consultant unique associé à la mission
	 */
	private Consultant consultant;

	/**
	 * Date de début
	 */
	private DateTime debut;

	/**
	 * Date de fin
	 */
	private DateTime fin;

	/**
	 * Intitulé de la mission
	 */
	private String intitule;

	/**
	 * Client associé à la mission
	 */
	private Client client;

	public Mission(Consultant consultant, DateTime debut, DateTime fin, String intitule, Client client) {
		this.consultant = consultant;
		this.debut = debut;
		this.fin = fin;
		this.intitule = intitule;
		this.client = client;
	}

	public Mission(DateTime debut, DateTime fin, String intitule, Client client) {
		this.debut = debut;
		this.fin = fin;
		this.intitule = intitule;
		this.client = client;
	}

	public Consultant getConsultant() {
		return consultant;
	}

	public DateTime getDebut() {
		return debut;
	}

	public DateTime getFin() {
		return fin;
	}

	public String getIntitule() {
		return intitule;
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

	public boolean isVaccante() {
		return this.consultant == null;
	}
}
