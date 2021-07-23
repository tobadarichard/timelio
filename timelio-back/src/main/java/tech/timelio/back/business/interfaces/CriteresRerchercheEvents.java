package tech.timelio.back.business.interfaces;

import java.time.ZonedDateTime;

import javax.validation.constraints.NotNull;

public class CriteresRerchercheEvents {
	@NotNull
	protected ZonedDateTime dateDebut;
	@NotNull
	protected ZonedDateTime dateFin;
	protected String description;
	
	public ZonedDateTime getDateDebut() {
		return dateDebut;
	}
	public void setDateDebut(ZonedDateTime dateDebut) {
		this.dateDebut = dateDebut;
	}
	public ZonedDateTime getDateFin() {
		return dateFin;
	}
	public void setDateFin(ZonedDateTime dateFin) {
		this.dateFin = dateFin;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String descriptionContient) {
		this.description = descriptionContient;
	}
}
