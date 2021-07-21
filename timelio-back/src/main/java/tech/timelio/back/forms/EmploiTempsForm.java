package tech.timelio.back.forms;

import javax.validation.constraints.NotEmpty;

public class EmploiTempsForm {
	@NotEmpty
	protected String nom;

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}
}
