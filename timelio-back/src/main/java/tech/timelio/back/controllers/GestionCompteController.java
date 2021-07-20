package tech.timelio.back.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import tech.timelio.back.business.interfaces.GestionCompteService;
import tech.timelio.back.business.interfaces.exceptions.AlreadyExistsException;
import tech.timelio.back.forms.CreationUtilisateurForm;

@RestController
public class GestionCompteController {
	@Autowired
	protected GestionCompteService gestionCompte;
	
	@PostMapping("/account")
	public ResponseEntity<String> creerUtilisateur
		(@Valid @RequestBody CreationUtilisateurForm form) throws AlreadyExistsException{
		gestionCompte.creerUtilisateur(form.getPseudo(), form.getEmail(), 
				form.getMdp(), false);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
}
