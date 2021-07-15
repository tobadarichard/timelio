package tech.timelio.back.modele;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Utilisateur {
	@Id
	@GeneratedValue
	protected Long id;
	protected String pseudo;
	protected String email;
	protected String mdp;
	protected boolean verifie;
	@OneToOne(mappedBy = "owner", cascade = CascadeType.REMOVE)
	protected RefreshTokenSalt refreshTokenSalt;
	@OneToMany(mappedBy = "owner", cascade = CascadeType.REMOVE)
	protected List<Token> tokens;
	@OneToMany(mappedBy = "owner", cascade = CascadeType.REMOVE)
	protected List<EmploiTemps> emploisTemps;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPseudo() {
		return pseudo;
	}
	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMdp() {
		return mdp;
	}
	public void setMdp(String mdp) {
		this.mdp = mdp;
	}
	public boolean isVerifie() {
		return verifie;
	}
	public void setVerifie(boolean verifie) {
		this.verifie = verifie;
	}
	public RefreshTokenSalt getRefreshTokenSalt() {
		return refreshTokenSalt;
	}
	public void setRefreshTokenSalt(RefreshTokenSalt refreshTokenSalt) {
		this.refreshTokenSalt = refreshTokenSalt;
	}
	public List<Token> getTokens() {
		return tokens;
	}
	public void setTokens(List<Token> tokens) {
		this.tokens = tokens;
	}
	public List<EmploiTemps> getEmploisTemps() {
		return emploisTemps;
	}
	public void setEmploisTemps(List<EmploiTemps> emploisTemps) {
		this.emploisTemps = emploisTemps;
	}
}
