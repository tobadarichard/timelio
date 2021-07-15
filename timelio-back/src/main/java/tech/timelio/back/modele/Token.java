package tech.timelio.back.modele;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Token {
	@Id
	@GeneratedValue
	protected Long id;
	protected LocalDateTime createdAt;
	protected LocalDateTime expiredAt;
	protected String value;
	@Enumerated(EnumType.STRING)
	protected TokenType type;
	@ManyToOne
	protected Utilisateur owner;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public LocalDateTime getExpiredAt() {
		return expiredAt;
	}
	public void setExpiredAt(LocalDateTime expiredAt) {
		this.expiredAt = expiredAt;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public TokenType getType() {
		return type;
	}
	public void setType(TokenType type) {
		this.type = type;
	}
	public Utilisateur getOwner() {
		return owner;
	}
	public void setOwner(Utilisateur owner) {
		this.owner = owner;
	}
}
