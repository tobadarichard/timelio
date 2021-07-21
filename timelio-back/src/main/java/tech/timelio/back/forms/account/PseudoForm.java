package tech.timelio.back.forms.account;

import javax.validation.constraints.NotEmpty;

public class PseudoForm {
	@NotEmpty
	protected String pseudo;

	public String getPseudo() {
		return pseudo;
	}

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}
}
