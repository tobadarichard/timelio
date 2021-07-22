package tech.timelio.back.forms.account;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class PseudoForm {
	@NotEmpty
	@Pattern(regexp = "^\\w+$")
	protected String pseudo;

	public String getPseudo() {
		return pseudo;
	}

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}
}
