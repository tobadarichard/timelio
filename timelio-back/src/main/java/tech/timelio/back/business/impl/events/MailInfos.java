package tech.timelio.back.business.impl.events;

import tech.timelio.back.modele.Utilisateur;

public class MailInfos {
	protected String tokenValue;
	protected Utilisateur user;
	
	public MailInfos() {}
	
	public MailInfos(String tokenValue, Utilisateur user) {
		this.tokenValue = tokenValue;
		this.user = user;
	}
	
	public String getTokenValue() {
		return tokenValue;
	}
	
	public void setTokenValue(String tokenValue) {
		this.tokenValue = tokenValue;
	}

	public Utilisateur getUser() {
		return user;
	}

	public void setUser(Utilisateur user) {
		this.user = user;
	}

}
