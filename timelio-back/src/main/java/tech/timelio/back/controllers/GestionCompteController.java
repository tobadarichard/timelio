package tech.timelio.back.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import tech.timelio.back.business.interfaces.GestionCompteService;
import tech.timelio.back.business.interfaces.UtilisateurEtToken;
import tech.timelio.back.business.interfaces.exceptions.AlreadyExistsException;
import tech.timelio.back.business.interfaces.exceptions.ExpiredTokenException;
import tech.timelio.back.business.interfaces.exceptions.NotFoundException;
import tech.timelio.back.business.interfaces.exceptions.UserNotVerifiedException;
import tech.timelio.back.forms.account.CreationUtilisateurForm;
import tech.timelio.back.forms.account.EmailForm;
import tech.timelio.back.forms.account.LoginForm;
import tech.timelio.back.forms.account.NewAndOldMdpForm;
import tech.timelio.back.forms.account.NewMdpForm;
import tech.timelio.back.forms.account.PseudoForm;
import tech.timelio.back.forms.account.TokenForm;
import tech.timelio.back.modele.Utilisateur;

@RestController
public class GestionCompteController {
	@Autowired
	protected GestionCompteService gestionCompte;
	
	@PostMapping("/account/register")
	public ResponseEntity<String> creerUtilisateur(@Valid @RequestBody CreationUtilisateurForm form)
			throws AlreadyExistsException{
		gestionCompte.creerUtilisateur(form.getPseudo(), form.getEmail(), 
				form.getMdp(), false);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@PostMapping("/account/login")
	public UtilisateurEtToken loginUtilisateur(@Valid @RequestBody LoginForm form) 
			throws NotFoundException, UserNotVerifiedException {
		return gestionCompte.login(form.getEmail(), form.getMdp());
	}
	
	@PostMapping("/account/reset-password")
	public void demandeResetMdp(@Valid @RequestBody EmailForm form) throws NotFoundException {
		gestionCompte.demandeResetMdp(form.getEmail());
	}
	
	@PostMapping("/account/reset-password/{token}")
	public void resetMdp(@Valid @RequestBody NewMdpForm form,@PathVariable String token) 
			throws NotFoundException, ExpiredTokenException {
		gestionCompte.resetMdp(token, form.getMdp());
	}
	
	@GetMapping("/account/verify/{token}")
	public void verifierCompte(@PathVariable String token) 
			throws NotFoundException, ExpiredTokenException {
		gestionCompte.verifierCompte(token);
	}
	
	@GetMapping("/account/verify/{token}/resend")
	public void redemanderTokenVerificationCompte(@PathVariable String token) 
			throws NotFoundException{
		gestionCompte.resetTokenVerifierCompte(token);
	}
	
	@GetMapping("/user/account")
	public Utilisateur getInfosUtilisateur(@RequestAttribute UtilisateurId userId) 
			throws NotFoundException {
		return gestionCompte.getInfos(userId.getId());
	}
	
	@PutMapping("/user/account")
	public Utilisateur majInfosUtilisateur(@RequestAttribute UtilisateurId userId,
			@Valid @RequestBody PseudoForm form) throws NotFoundException {
		return gestionCompte.changerPseudo(userId.getId(), form.getPseudo());
	}
	
	@PutMapping("/user/account/password")
	public UtilisateurEtToken majMdpUtilisateur(@RequestAttribute UtilisateurId userId,
			@Valid @RequestBody NewAndOldMdpForm form) throws NotFoundException {
		return gestionCompte.changerMdp(
				userId.getId(), form.getOldMdp(), form.getNewMdp());
	}
	
	@DeleteMapping("/user/account")
	public void supprimerUtilisateur(@RequestAttribute UtilisateurId userId) 
			throws NotFoundException {
		gestionCompte.supprimerCompte(userId.getId());
	}
	
	@PostMapping("/account/access-token")
	public String getAccesToken(@Valid @RequestBody TokenForm form) throws NotFoundException {
		return gestionCompte.recupererTokenAcces(form.getToken());
	}
}
