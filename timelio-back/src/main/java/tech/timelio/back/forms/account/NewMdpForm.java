package tech.timelio.back.forms.account;

import javax.validation.constraints.NotEmpty;

public class NewMdpForm {
	@NotEmpty
	protected String mdp;

	public String getMdp() {
		return mdp;
	}

	public void setMdp(String mdp) {
		this.mdp = mdp;
	}
}
