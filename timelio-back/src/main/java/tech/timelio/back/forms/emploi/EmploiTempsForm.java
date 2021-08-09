package tech.timelio.back.forms.emploi;

import javax.validation.constraints.NotBlank;

public class EmploiTempsForm {
	@NotBlank
	protected String nom;

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}
}
