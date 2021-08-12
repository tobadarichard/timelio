package tech.timelio.back.forms.emploi;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class EmploiTempsUserForm {
	@NotBlank	
	protected String nom;
	@NotNull
	protected Boolean publique;

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public Boolean getPublique() {
		return publique;
	}

	public void setPublique(Boolean publique) {
		this.publique = publique;
	}
}
