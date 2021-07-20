package tech.timelio.back.forms;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class CreationUtilisateurForm {
	@NotEmpty
	protected String pseudo;
	@Email
	protected String email;
	@NotEmpty
	protected String mdp;
	
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
}
