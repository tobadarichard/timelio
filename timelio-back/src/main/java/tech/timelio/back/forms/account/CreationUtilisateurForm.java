package tech.timelio.back.forms.account;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class CreationUtilisateurForm {
	@NotEmpty
	@Pattern(regexp = "^\\w+$")
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
