package tech.timelio.back.forms;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class EmploiTempsUserForm {
	@NotEmpty(message = "Veuillez saisir un nom pour l'emploi du temps")
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
