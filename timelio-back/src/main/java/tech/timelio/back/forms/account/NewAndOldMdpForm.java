package tech.timelio.back.forms.account;

import javax.validation.constraints.NotEmpty;

public class NewAndOldMdpForm {
	@NotEmpty
	protected String oldMdp;
	
	@NotEmpty
	protected String newMdp;

	public String getOldMdp() {
		return oldMdp;
	}

	public void setOldMdp(String oldMdp) {
		this.oldMdp = oldMdp;
	}

	public String getNewMdp() {
		return newMdp;
	}

	public void setNewMdp(String newMdp) {
		this.newMdp = newMdp;
	}
}
