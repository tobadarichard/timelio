package tech.timelio.back.business.impl.events;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EventHandler {

	@EventListener
	public void handleUserCreation(UserCreatedEvent event) {
		//TODO : envoi du mail de confirmation
	}
	
	@EventListener
	public void handleResetPassword(ResetUserPasswordEvent event) {
		//TODO : envoi du mail pour nouveau mot de passe
	}
}
