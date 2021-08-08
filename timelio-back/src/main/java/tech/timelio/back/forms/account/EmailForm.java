package tech.timelio.back.forms.account;

import javax.validation.constraints.Email;

public class EmailForm {
	@Email
	protected String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
