package tech.timelio.back.business.impl;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import tech.timelio.back.business.interfaces.CriteresRerchercheEvents;
import tech.timelio.back.business.interfaces.EvenementService;
import tech.timelio.back.business.interfaces.exceptions.NotFoundException;
import tech.timelio.back.dao.EvenementDAO;
import tech.timelio.back.modele.Evenement;
@Repository
public class EvenementServiceImpl implements EvenementService {
	@Autowired
	protected EvenementDAO eventDAO;

	private NotFoundException getNotFound() {
		return new NotFoundException("Evenement non trouvé");
	}

	@Override
	public Evenement creerEvenement(Evenement event) {
		return eventDAO.save(event);
	}

	@Override
	public Evenement majEvenement(Long idEmploi, Evenement event) throws NotFoundException {
		if (!eventDAO.existsByIdAndEmploiTempsId(event.getId(),idEmploi))
			throw getNotFound();
		return eventDAO.save(event);
	}

	@Override
	public void supprimerEvenement(Long idEmploi,Long id) throws NotFoundException {
		if (!eventDAO.existsByIdAndEmploiTempsId(id,idEmploi))
			throw getNotFound();
		eventDAO.deleteById(id);
	}

	@Override
	public Evenement recupererEvenement(Long idEmploi, Long id) throws NotFoundException {
		return eventDAO.findByIdAndEmploiTempsId(id,idEmploi).orElseThrow(this::getNotFound);
	}

	@Override
	public Page<Evenement> listerEvenements(Long idEmploi, Pageable pagination) {
		return eventDAO.findAllByEmploiTempsId(idEmploi, pagination);
	}

	@Override
	public List<Evenement> chercherEvenements(CriteresRerchercheEvents criteres, Long idEmploi) {
		String description = criteres.getDescription();
		if (description == null)
			description = "";
		return eventDAO.searchEvents(idEmploi,criteres.getDateDebut(),
				criteres.getDateFin(), description)
				.stream()
				.filter( event -> {
					if (!event.isPeriodique()) return true;

					ZonedDateTime debut = criteres.getDateDebut();
					ZonedDateTime fin = criteres.getDateFin();
					if (event.getDateDebut().isAfter(fin)) return false;

					long distance = event.getDateDebut().until(debut, ChronoUnit.DAYS);
					long periodsToAdd = (distance / event.getPeriode().getDays())+1;

					ZonedDateTime firstDateIn = event.getDateDebut()
							.plusDays(periodsToAdd*event.getPeriode().getDays());

					return firstDateIn.isAfter(debut) && firstDateIn.isBefore(fin);
				})
				.collect(Collectors.toList());	
	}

}
