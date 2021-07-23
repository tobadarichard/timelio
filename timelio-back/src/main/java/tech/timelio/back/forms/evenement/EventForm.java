package tech.timelio.back.forms.evenement;

import java.time.Duration;
import java.time.Period;
import java.time.ZonedDateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import tech.timelio.back.forms.exceptions.InvalidFormException;
import tech.timelio.back.modele.Evenement;

public class EventForm {
	@NotNull
	protected ZonedDateTime dateDebut;
	@NotNull
	protected Duration duree;
	@Pattern(regexp = "^\\w+$")
	protected String description;
	@Pattern(regexp = "^#(?:[0-9a-fA-F]{3}){1,2}$")
	protected String couleur;
	@NotNull
	protected boolean periodique;
	protected Period periode;
	
	public Evenement getEvent() throws InvalidFormException {
		Evenement event = new Evenement();
		event.setDateDebut(dateDebut);
		event.setDuree(duree);
		event.setDescription(description);
		event.setCouleur(couleur);
		event.setPeriodique(periodique);
		
		if (periodique && periode == null)
			throw new InvalidFormException("La période doit être définie");
		
		event.setPeriode(periodique ? periode : null);
		return event;
	}

	public void setDateDebut(ZonedDateTime dateDebut) {
		this.dateDebut = dateDebut;
	}

	public void setDuree(Duration duree) {
		this.duree = duree;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setCouleur(String couleur) {
		this.couleur = couleur;
	}

	public void setPeriodique(boolean periodique) {
		this.periodique = periodique;
	}

	public void setPeriode(Period periode) {
		this.periode = periode;
	}
}
