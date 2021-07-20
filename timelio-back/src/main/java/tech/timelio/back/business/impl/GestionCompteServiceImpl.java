package tech.timelio.back.business.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import tech.timelio.back.business.impl.events.UserCreatedEvent;
import tech.timelio.back.business.interfaces.GestionCompteService;
import tech.timelio.back.business.interfaces.exceptions.AlreadyExistsException;
import tech.timelio.back.business.interfaces.exceptions.ExpiredTokenException;
import tech.timelio.back.business.interfaces.exceptions.NotFoundException;
import tech.timelio.back.business.interfaces.exceptions.UserNotVerifiedException;
import tech.timelio.back.dao.TokenDAO;
import tech.timelio.back.dao.UtilisateurDAO;
import tech.timelio.back.modele.Token;
import tech.timelio.back.modele.TokenType;
import tech.timelio.back.modele.Utilisateur;
import tech.timelio.back.utils.CodeGenerator;

@Service
public class GestionCompteServiceImpl implements GestionCompteService {
	@Autowired
	protected UtilisateurDAO userDAO;
	@Autowired 
	protected TokenDAO tokenDAO;
	@Autowired
	protected ApplicationEventPublisher publisher;
	@Autowired
	protected PasswordEncoder encoder;
	
	
	private Token creerTokenVerification(Utilisateur user) {
		Token token = new Token();
		LocalDateTime now = LocalDateTime.now();
		
		token.setCreatedAt(now);
		token.setExpiredAt(now.plusHours(2));
		token.setOwner(user);
		token.setType(TokenType.VERIFY_ACCOUNT);
		token.setValue(CodeGenerator.genererValeurToken());
		return tokenDAO.save(token);
	}

	@Override
	public Utilisateur creerUtilisateur(String pseudo, String email, String mdp,
			boolean verifie) throws AlreadyExistsException {
		if (userDAO.existsByEmail(email))
			throw new AlreadyExistsException("Un utilisateur possède déjà cette adresse "
					+ "mail");
		
		Utilisateur user = new Utilisateur();
		user.setPseudo(pseudo);
		user.setEmail(email);
		user.setMdp(encoder.encode(mdp));
		user.setVerifie(verifie);
		user = userDAO.save(user);
		
		if (!verifie) {
			Token token = creerTokenVerification(user);
			publisher.publishEvent(new UserCreatedEvent(token.getValue(), email));
		}
		return user;
	}


	@Override
	public Utilisateur login(String email, String mdp) 
			throws NotFoundException, UserNotVerifiedException {
		Utilisateur user = userDAO.findByEmailAndMdp(email, mdp)
				.orElseThrow(() -> new NotFoundException("Utilisateur non trouvé"));
		if (!user.isVerifie())
			throw new UserNotVerifiedException();
		return user;
	}

	@Override
	public void demandeResetMdp(String email) throws NotFoundException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resetMdp(String resetMdpTokenValue, String newMdp) throws NotFoundException, ExpiredTokenException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void verifierCompte(String verifyTokenValue) throws NotFoundException, ExpiredTokenException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resetTokenVerifierCompte(String verifyTokenValue) throws NotFoundException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Utilisateur changerMdp(Long userId, String oldMdp, String newMdp) throws NotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Utilisateur changerPseudo(Long userId, String newPseudo) throws NotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Utilisateur getInfos(Long userId) throws NotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void supprimerCompte(Long userId) throws NotFoundException {
		// TODO Auto-generated method stub
		
	}

	public UtilisateurDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UtilisateurDAO userDAO) {
		this.userDAO = userDAO;
	}
}
