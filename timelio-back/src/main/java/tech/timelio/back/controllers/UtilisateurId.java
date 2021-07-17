package tech.timelio.back.controllers;

import tech.timelio.back.modele.Utilisateur;

public class UtilisateurId {
	protected Long id;

	public UtilisateurId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Utilisateur getUtilisateur() {
		Utilisateur user = new Utilisateur();
		user.setId(id);
		return user;
	}
}
