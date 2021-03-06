package tech.timelio.back.business.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.util.Frequency;
import biweekly.util.Recurrence;
import tech.timelio.back.business.interfaces.EmploiTempsService;
import tech.timelio.back.business.interfaces.exceptions.NotFoundException;
import tech.timelio.back.dao.EmploiTempsDAO;
import tech.timelio.back.modele.EmploiTemps;
import tech.timelio.back.modele.Evenement;
import tech.timelio.back.modele.Utilisateur;
import tech.timelio.back.utils.CodeGenerator;

@Service
public class EmploiTempsServiceImpl implements EmploiTempsService {
	@Autowired
	protected EmploiTempsDAO emploiTempsDAO;
	
	private EmploiTemps majEmploiTemps(EmploiTemps emploi,String newNom,
			boolean newPublique) {
		emploi.setNom(newNom);
		if (emploi.isPublique() && !newPublique)
			emploi.setCodeAcces(null);
		else if (!emploi.isPublique() && newPublique)
			emploi.setCodeAcces(CodeGenerator.genererCodeAcces());
		emploi.setPublique(newPublique);
		return emploiTempsDAO.save(emploi);
	}
	
	private NotFoundException getEmploiNotFoundException() {
		return new NotFoundException("Cet emploi du temps n'existe pas");
	}

	private ICalendar exportEmploiEvents(String nom,List<Evenement> evenements) {
		ICalendar ical = new ICalendar();
		ical.setName(nom);
		if (evenements == null || evenements.isEmpty()) return ical;
		
		evenements.forEach(event -> {
			VEvent vEvent = new VEvent();
			vEvent.setSummary(event.getDescription());
			
			vEvent.setDateStart(Date.from(event.getDateDebut().toInstant()));
			vEvent.setDateEnd(Date.from(event.getDateDebut().plus(event.getDuree())
					.toInstant()));
			
			if (event.isPeriodique()) {
				Recurrence recurrence = new Recurrence.Builder(Frequency.DAILY)
						.interval(event.getPeriode().getDays())
						.build();
				vEvent.setRecurrenceRule(recurrence);
			}
			ical.addEvent(vEvent);
		});
		
		return ical;
	}
	@Override
	public EmploiTemps creerEmploi(String nom, boolean publique, 
			Utilisateur owner) {
		if (!publique && owner == null)
			throw new IllegalArgumentException("L'emploi du temps ne peut ??tre priv?? "
					+ "sans proprietaire");
		EmploiTemps emploi = new EmploiTemps();
		emploi.setNom(nom);
		emploi.setPublique(publique);
		emploi.setOwner(owner);
		emploi.setCodeAcces(publique ? CodeGenerator.genererCodeAcces() : null);
		return emploiTempsDAO.save(emploi);
	}

	@Override
	public EmploiTemps creerEmploi(String nom) {
		return creerEmploi(nom, true, null);
	}

	@Override
	public void supprimerEmploi(Long id) throws NotFoundException {
		if (!emploiTempsDAO.existsById(id)) throw getEmploiNotFoundException();
		emploiTempsDAO.deleteById(id);
	}

	@Override
	public void supprimerEmploi(String codeAcces) throws NotFoundException {
		Long id = emploiTempsDAO.findOneByCodeAcces(codeAcces)
			.orElseThrow(this::getEmploiNotFoundException)
			.getId();
		emploiTempsDAO.deleteById(id);
		
	}

	@Override
	public EmploiTemps recupererEmploi(Long id) throws NotFoundException {
		return emploiTempsDAO.findWithEventsById(id)
				.orElseThrow(this::getEmploiNotFoundException);
	}

	@Override
	public EmploiTemps recupererEmploi(String codeAcces) throws NotFoundException {
		return emploiTempsDAO.findOneWithEventsByCodeAcces(codeAcces)
				.orElseThrow(this::getEmploiNotFoundException);
	}

	@Override
	public EmploiTemps majEmploi(Long id, String newNom,
			boolean newPublique) throws NotFoundException {
		EmploiTemps emploi = emploiTempsDAO.findById(id)
				.orElseThrow(this::getEmploiNotFoundException);
		return majEmploiTemps(emploi, newNom,newPublique);
	}

	@Override
	public EmploiTemps majEmploi(String codeAcces, String newNom) 
			throws NotFoundException {
		EmploiTemps emploi = emploiTempsDAO.findOneByCodeAcces(codeAcces)
				.orElseThrow(this::getEmploiNotFoundException);
		return majEmploiTemps(emploi,newNom,true);
	}

	@Override
	public Page<EmploiTemps> listeEmploi(Utilisateur owner, Pageable pagination) {
		return emploiTempsDAO.findAllByOwner(owner, pagination);
	}
	
	@Override
	public ICalendar exporterEmploi(Long idEmploi) throws NotFoundException {
		EmploiTemps emploi = emploiTempsDAO.findWithEventsById(idEmploi)
				.orElseThrow(this::getEmploiNotFoundException);
		return exportEmploiEvents(emploi.getNom(),emploi.getEvenements());
	}
	
	@Override
	public ICalendar exporterEmploi(String codeAcces) throws NotFoundException {
		EmploiTemps emploi = emploiTempsDAO.findOneWithEventsByCodeAcces(codeAcces)
				.orElseThrow(this::getEmploiNotFoundException);
		return exportEmploiEvents(emploi.getNom(),emploi.getEvenements());
	}
	
	public EmploiTempsDAO getEmploiTempsDAO() {
		return emploiTempsDAO;
	}

	public void setEmploiTempsDAO(EmploiTempsDAO emploiTempsDAO) {
		this.emploiTempsDAO = emploiTempsDAO;
	}
}
