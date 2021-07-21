package tech.timelio.back.forms.account;

import javax.validation.constraints.NotEmpty;

public class TokenForm {
	@NotEmpty
	protected String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
