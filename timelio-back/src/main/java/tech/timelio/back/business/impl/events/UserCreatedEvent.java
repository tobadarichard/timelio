package tech.timelio.back.business.impl.events;

public class UserCreatedEvent {
	protected String tokenValue;
	protected String email;
	
	public UserCreatedEvent() {}
	
	public UserCreatedEvent(String tokenValue, String email) {
		this.tokenValue = tokenValue;
		this.email = email;
	}
	
	public String getTokenValue() {
		return tokenValue;
	}
	public void setTokenValue(String tokenValue) {
		this.tokenValue = tokenValue;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
