package tech.timelio.back.modele;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class RefreshTokenSalt {
	@Id
	@GeneratedValue
	protected Long id;
	protected String value;
	@OneToOne
	protected Utilisateur owner;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Utilisateur getOwner() {
		return owner;
	}
	public void setOwner(Utilisateur owner) {
		this.owner = owner;
	}
}
