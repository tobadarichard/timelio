package tech.timelio.back.modele;

import java.time.Duration;
import java.time.Period;
import java.time.ZonedDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
@Entity
public class Evenement {
	@Id
	@GeneratedValue
	protected Long id;
	protected ZonedDateTime dateDebut;
	protected Duration duree;
	protected String description;
	protected String couleur;
	protected boolean periodique;
	protected Period periode;
	@ManyToOne
	@JsonProperty(access = Access.WRITE_ONLY)
	protected EmploiTemps emploiTemps;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public ZonedDateTime getDateDebut() {
		return dateDebut;
	}
	public void setDateDebut(ZonedDateTime dateDebut) {
		this.dateDebut = dateDebut;
	}
	public Duration getDuree() {
		return duree;
	}
	public void setDuree(Duration duree) {
		this.duree = duree;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCouleur() {
		return couleur;
	}
	public void setCouleur(String couleur) {
		this.couleur = couleur;
	}
	public boolean isPeriodique() {
		return periodique;
	}
	public void setPeriodique(boolean periodique) {
		this.periodique = periodique;
	}
	public Period getPeriode() {
		return periode;
	}
	public void setPeriode(Period periode) {
		this.periode = periode;
	}
	public EmploiTemps getEmploiTemps() {
		return emploiTemps;
	}
	public void setEmploiTemps(EmploiTemps emploiTemps) {
		this.emploiTemps = emploiTemps;
	}
}
