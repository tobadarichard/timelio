package tech.timelio.back.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import tech.timelio.back.auth.AuthService;
import tech.timelio.back.auth.ForbiddenActionException;
import tech.timelio.back.business.interfaces.EmploiTempsService;
import tech.timelio.back.business.interfaces.exceptions.NotFoundException;
import tech.timelio.back.forms.emploi.EmploiTempsForm;
import tech.timelio.back.forms.emploi.EmploiTempsUserForm;
import tech.timelio.back.modele.EmploiTemps;

@RestController
@CrossOrigin(origins = "${timelio.url-front}")
public class EmploisTempsController {
	@Autowired
	protected EmploiTempsService serviceEmploi;
	@Autowired
	protected AuthService authService;
	
	@PostMapping("/emplois")
	public EmploiTemps creerEmploiPublique(@Valid @RequestBody EmploiTempsForm form){
		return serviceEmploi.creerEmploi(form.getNom());
	}
	
	@PostMapping("/user/emplois")
	public EmploiTemps creerEmploiUtilisateur(
			@Valid @RequestBody EmploiTempsUserForm form,
			@RequestAttribute UtilisateurId userId){
		return serviceEmploi.creerEmploi(form.getNom(),
				form.getPublique(),userId.getUtilisateur());
	}
	
	@DeleteMapping("/emplois/{codeAcces}")
	public void supprimerEmploiPublique(@PathVariable String codeAcces) 
			throws NotFoundException{
		serviceEmploi.supprimerEmploi(codeAcces);
	}
	
	@DeleteMapping("/user/emplois/{id}")
	public void supprimerEmploiUtilisateur(@PathVariable Long id,
			@RequestAttribute UtilisateurId userId) 
					throws NotFoundException, ForbiddenActionException{
		authService.isEmploiOwner(id, userId.getUtilisateur());
		serviceEmploi.supprimerEmploi(id);
	}

	@GetMapping("/emplois/{codeAcces}")
	public EmploiTemps recupererEmploiPublique(@PathVariable String codeAcces) 
			throws NotFoundException {
		return serviceEmploi.recupererEmploi(codeAcces);
	}
	
	@GetMapping("/user/emplois/{id}")
	public EmploiTemps recupererEmploiUtilisateur(@PathVariable Long id,
			@RequestAttribute UtilisateurId userId) 
					throws NotFoundException, ForbiddenActionException {
		authService.isEmploiOwner(id, userId.getUtilisateur());
		return serviceEmploi.recupererEmploi(id);
	}
	
	@PutMapping("/emplois/{codeAcces}")
	public EmploiTemps majEmploiPublique(@PathVariable String codeAcces,
			@Valid @RequestBody EmploiTempsForm form) throws NotFoundException {
		return serviceEmploi.majEmploi(codeAcces,form.getNom());
	}
	
	@PutMapping("/user/emplois/{id}")
	public EmploiTemps majEmploiUtilisateur(@PathVariable Long id,
			@RequestAttribute UtilisateurId userId,
			@Valid @RequestBody EmploiTempsUserForm form) 
					throws NotFoundException, ForbiddenActionException {
		authService.isEmploiOwner(id, userId.getUtilisateur());
		return serviceEmploi.majEmploi(id,form.getNom(),form.getPublique());
	}
	
	@GetMapping("/user/emplois")
	public Page<EmploiTemps> listerEmploiUtilisateur(
			@RequestAttribute UtilisateurId userId,Pageable pagination) {
		return serviceEmploi.listeEmploi(userId.getUtilisateur(), pagination);
	}
}
