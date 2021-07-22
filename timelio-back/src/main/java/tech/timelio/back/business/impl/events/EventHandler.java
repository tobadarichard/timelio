package tech.timelio.back.business.impl.events;

import java.io.UnsupportedEncodingException;

import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import tech.timelio.back.modele.Utilisateur;

@Component
public class EventHandler {
	@Autowired
	protected JavaMailSender sender;
	@Autowired
	protected MailTemplates templates;
	@Value("${spring.mail.username}")
	protected String username;
	@Value("${timelio.url-front}")
	protected String urlFront;
	
	private void envoyerMail(String sujet,String body,String email) {
		MimeMessage mail = sender.createMimeMessage();
		try {
			mail.setFrom(new InternetAddress(username, "Equipe timelio"));
			mail.setRecipient(RecipientType.TO, new InternetAddress(email));
			mail.setSubject(sujet);
			mail.setContent(body, "text/html");
			
			sender.send(mail);
		} catch (MessagingException | UnsupportedEncodingException e) {
			//TODO Gérer cette situation ?
		}
	}

	@EventListener
	public void handleUserCreation(UserCreatedEvent event) {
		MailInfos infos = event.getInfos();
		Utilisateur user = infos.getUser();
		
		String urlVerification = urlFront+"/account/verify/"+infos.getTokenValue();
		String body = String.format(templates.getVerifierCompte(),user.getPseudo(),urlFront,
				urlVerification);
		envoyerMail("Verification de votre compte Timelio", body, user.getEmail());
	}
	
	@EventListener
	public void handleResetPassword(ResetUserPasswordEvent event) {
		MailInfos infos = event.getInfos();
		Utilisateur user = infos.getUser();
		
		String urlReset = urlFront+"/account/reset-password/"+infos.getTokenValue();
		String body = String.format(templates.getResetMdp(),user.getPseudo(),urlReset);
		envoyerMail("Reinitialiser votre mot de passe", body, user.getEmail());
	}
}
