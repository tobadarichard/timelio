package tech.timelio.back.modele;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
public class EmploiTemps {
	@Id
	@GeneratedValue
	protected Long id;
	protected String nom;
	@Column(unique = true)
	protected String codeAcces;
	protected boolean publique;
	@ManyToOne
	@JsonProperty(access = Access.WRITE_ONLY)
	protected Utilisateur owner;
	@OneToMany(mappedBy = "emploiTemps",cascade = CascadeType.REMOVE)
	@JsonProperty(access = Access.WRITE_ONLY)
	protected List<Evenement> evenements;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getCodeAcces() {
		return codeAcces;
	}
	public void setCodeAcces(String codeAcces) {
		this.codeAcces = codeAcces;
	}
	public boolean isPublique() {
		return publique;
	}
	public void setPublique(boolean publique) {
		this.publique = publique;
	}
	public Utilisateur getOwner() {
		return owner;
	}
	public void setOwner(Utilisateur owner) {
		this.owner = owner;
	}
	public List<Evenement> getEvenements() {
		return evenements;
	}
	public void setEvenements(List<Evenement> evenements) {
		this.evenements = evenements;
	}
}
