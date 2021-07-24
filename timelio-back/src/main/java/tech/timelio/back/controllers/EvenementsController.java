package tech.timelio.back.controllers;

import java.util.List;

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
import tech.timelio.back.business.interfaces.CriteresRerchercheEvents;
import tech.timelio.back.business.interfaces.EmploiTempsService;
import tech.timelio.back.business.interfaces.EvenementService;
import tech.timelio.back.business.interfaces.exceptions.NotFoundException;
import tech.timelio.back.forms.evenement.EventForm;
import tech.timelio.back.forms.exceptions.InvalidFormException;
import tech.timelio.back.modele.EmploiTemps;
import tech.timelio.back.modele.Evenement;

@RestController
@CrossOrigin(origins = "${timelio.url-front}")
public class EvenementsController {
	@Autowired
	protected AuthService authService;
	@Autowired
	protected EmploiTempsService emploiService;
	@Autowired
	protected EvenementService eventService;
	
	@PostMapping("/emplois/{codeAcces}/events")
	public Evenement creerEvent(@Valid @RequestBody EventForm form,
			@PathVariable String codeAcces) throws InvalidFormException, NotFoundException{
		EmploiTemps emploi = emploiService.recupererEmploi(codeAcces);
		Evenement event = form.getEvent();
		event.setEmploiTemps(emploi);
		return eventService.creerEvenement(event);
	}
	
	@PostMapping("/user/emplois/{id}/events")
	public Evenement creerEvent(@Valid @RequestBody EventForm form,
			@RequestAttribute UtilisateurId userId,@PathVariable Long id) 
					throws ForbiddenActionException, InvalidFormException, NotFoundException{
		authService.isEmploiOwner(id, userId.getUtilisateur());
		EmploiTemps emploi = emploiService.recupererEmploi(id);
		Evenement event = form.getEvent();
		event.setEmploiTemps(emploi);
		return eventService.creerEvenement(event);
	}
	
	@GetMapping("/emplois/{codeAcces}/events")
	public Page<Evenement> listerEvents(@PathVariable String codeAcces,Pageable pagination) 
			throws NotFoundException{
		Long idEmploi = emploiService.recupererEmploi(codeAcces).getId();
		return eventService.listerEvenements(idEmploi, pagination);
	}
	
	@GetMapping("/user/emplois/{id}/events")
	public Page<Evenement> listerEvents(@RequestAttribute UtilisateurId userId, 
			@PathVariable Long id,Pageable pagination) throws ForbiddenActionException{
		authService.isEmploiOwner(id, userId.getUtilisateur());
		return eventService.listerEvenements(id, pagination);
	}
	
	//TODO : améliorer la recherche pour prendre en compte la période
	
	@PostMapping("/emplois/{codeAcces}/events/search")
	public List<Evenement> searchEvents(@PathVariable String codeAcces,
			@Valid @RequestBody CriteresRerchercheEvents criteres) throws NotFoundException {
		Long idEmploi = emploiService.recupererEmploi(codeAcces).getId();
		return eventService.chercherEvenements(criteres, idEmploi);
	}
	
	@PostMapping("/user/emplois/{idEmploi}/events/search")
	public List<Evenement> searchEvents(@PathVariable Long idEmploi, 
			@RequestAttribute UtilisateurId userId, 
			@Valid @RequestBody CriteresRerchercheEvents criteres) 
					throws ForbiddenActionException {
		authService.isEmploiOwner(idEmploi, userId.getUtilisateur());
		return eventService.chercherEvenements(criteres, idEmploi);
	}
	
	@GetMapping("/emplois/{codeAcces}/events/{idEvent}")
	public Evenement getEvent(@PathVariable String codeAcces,@PathVariable Long idEvent) 
			throws NotFoundException {
		Long idEmploi = emploiService.recupererEmploi(codeAcces).getId();
		return eventService.recupererEvenement(idEmploi, idEvent);
	}
	
	@GetMapping("/user/emplois/{idEmploi}/events/{idEvent}")
	public Evenement getEvent(@PathVariable Long idEmploi,@PathVariable Long idEvent,
			@RequestAttribute UtilisateurId userId) 
					throws NotFoundException, ForbiddenActionException {
		authService.isEmploiOwner(idEmploi, userId.getUtilisateur());
		return eventService.recupererEvenement(idEmploi, idEvent);
	}
	
	@DeleteMapping("/emplois/{codeAcces}/events/{idEvent}")
	public void supprimerEvent(@PathVariable String codeAcces,@PathVariable Long idEvent) 
			throws NotFoundException {
		Long idEmploi = emploiService.recupererEmploi(codeAcces).getId();
		eventService.supprimerEvenement(idEmploi, idEvent);
	}
	
	@DeleteMapping("/user/emplois/{idEmploi}/events/{idEvent}")
	public void supprimerEvent(@PathVariable Long idEmploi,@PathVariable Long idEvent,
			@RequestAttribute UtilisateurId userId) 
					throws NotFoundException, ForbiddenActionException {
		authService.isEmploiOwner(idEmploi, userId.getUtilisateur());
		eventService.supprimerEvenement(idEmploi, idEvent);
	}
	
	@PutMapping("/emplois/{codeAcces}/events/{idEvent}")
	public Evenement majEvent(@PathVariable String codeAcces,@PathVariable Long idEvent,
			@Valid @RequestBody EventForm form) throws NotFoundException, InvalidFormException {
		EmploiTemps emploi = emploiService.recupererEmploi(codeAcces);
		Evenement event = form.getEvent();
		event.setEmploiTemps(emploi);
		event.setId(idEvent);
		return eventService.majEvenement(emploi.getId(), event);
	}
	
	@PutMapping("/user/emplois/{idEmploi}/events/{idEvent}")
	public Evenement majEvent(@PathVariable Long idEmploi,@PathVariable Long idEvent,
			@Valid @RequestBody EventForm form,@RequestAttribute UtilisateurId userId) 
					throws NotFoundException, ForbiddenActionException, InvalidFormException {
		authService.isEmploiOwner(idEmploi, userId.getUtilisateur());
		EmploiTemps emploi = emploiService.recupererEmploi(idEmploi);
		Evenement event = form.getEvent();
		event.setEmploiTemps(emploi);
		event.setId(idEvent);
		return eventService.majEvenement(emploi.getId(), event);
	}

}
