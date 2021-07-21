package tech.timelio.back.business.interfaces;

import tech.timelio.back.modele.Utilisateur;

public class UtilisateurEtToken {
	protected Utilisateur userInfos;
	protected String refreshToken;
	
	public Utilisateur getUserInfos() {
		return userInfos;
	}
	public void setUserInfos(Utilisateur userInfos) {
		this.userInfos = userInfos;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	
	
}
