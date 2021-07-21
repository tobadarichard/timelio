package tech.timelio.back.business.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import tech.timelio.back.business.impl.events.MailInfos;
import tech.timelio.back.business.impl.events.ResetUserPasswordEvent;
import tech.timelio.back.business.impl.events.UserCreatedEvent;
import tech.timelio.back.business.interfaces.GestionCompteService;
import tech.timelio.back.business.interfaces.TokenService;
import tech.timelio.back.business.interfaces.UtilisateurEtToken;
import tech.timelio.back.business.interfaces.exceptions.AlreadyExistsException;
import tech.timelio.back.business.interfaces.exceptions.ExpiredTokenException;
import tech.timelio.back.business.interfaces.exceptions.NotFoundException;
import tech.timelio.back.business.interfaces.exceptions.UserNotVerifiedException;
import tech.timelio.back.dao.UtilisateurDAO;
import tech.timelio.back.modele.Token;
import tech.timelio.back.modele.TokenType;
import tech.timelio.back.modele.Utilisateur;

@Service
public class GestionCompteServiceImpl implements GestionCompteService {
	@Autowired
	protected UtilisateurDAO userDAO;
	@Autowired
	protected ApplicationEventPublisher publisher;
	@Autowired
	protected PasswordEncoder encoder;
	@Autowired
	protected TokenService tokenService;

	private NotFoundException getUserNotFoundException() {
		return new NotFoundException("Utilisateur non trouvé");
	}
	
	private Utilisateur changerMdp(Utilisateur user, String newMdp) {
		user.setMdp(encoder.encode(newMdp));
		tokenService.supprimerTokenResetMdp(user);
		return userDAO.save(user);
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
			Token token = tokenService.creerTokenVerification(user);
			publisher.publishEvent(
					new UserCreatedEvent(new MailInfos(token.getValue(), user)));
		}
		
		return user;
	}

	@Override
	public UtilisateurEtToken login(String email, String mdp) 
			throws NotFoundException, UserNotVerifiedException {
		Utilisateur user = userDAO.findByEmail(email)
				.orElseThrow(() -> new NotFoundException("Utilisateur/mot de passe non trouvé"));
		
		if (!encoder.matches(mdp, user.getMdp()))
			throw new NotFoundException("Utilisateur/mot de passe non trouvé");
		if (!user.isVerifie())
			throw new UserNotVerifiedException();
		
		String token = tokenService.recupererTokenRafraichissement(user);
		
		UtilisateurEtToken userEtToken = new UtilisateurEtToken();
		userEtToken.setRefreshToken(token);
		userEtToken.setUserInfos(user);
		return userEtToken;
	}

	@Override
	public void demandeResetMdp(String email) throws NotFoundException {
		Utilisateur user = userDAO.findByEmailAndVerifie(email,true)
				.orElseThrow(this::getUserNotFoundException);
		Token token = tokenService.creerTokenResetMdp(user);
		publisher.publishEvent(
				new ResetUserPasswordEvent(new MailInfos(token.getValue(), user)));
	}

	@Override
	public void resetMdp(String resetMdpTokenValue, String newMdp) 
			throws NotFoundException, ExpiredTokenException {
		Utilisateur owner = tokenService.trouverUtilisateurParToken(
				resetMdpTokenValue, TokenType.RESET_PASSWORD);
		changerMdp(owner, newMdp);
	}

	@Override
	public void verifierCompte(String verifyTokenValue) 
			throws NotFoundException, ExpiredTokenException {
		Utilisateur owner = tokenService.trouverUtilisateurParToken(
				verifyTokenValue, TokenType.VERIFY_ACCOUNT);
		owner.setVerifie(true);
		userDAO.save(owner);
		tokenService.supprimerTokenVerify(owner);
	}

	@Override
	public void resetTokenVerifierCompte(String verifyTokenValue) throws NotFoundException {
		Token token = tokenService.recreerTokenVerification(verifyTokenValue);
		publisher.publishEvent(
				new UserCreatedEvent(new MailInfos(token.getValue(), token.getOwner())));
	}

	@Override
	public UtilisateurEtToken changerMdp(Long userId, String oldMdp, String newMdp) 
			throws NotFoundException {
		Utilisateur user = userDAO.findByIdAndVerifie(userId,true)
				.orElseThrow(this::getUserNotFoundException);
		if (!encoder.matches(oldMdp, user.getMdp()))
			throw getUserNotFoundException();
		user = changerMdp(user, newMdp);
		
		String token = tokenService.recupererTokenRafraichissement(user);
		UtilisateurEtToken userEtToken = new UtilisateurEtToken();
		userEtToken.setRefreshToken(token);
		userEtToken.setUserInfos(user);
		return userEtToken;
	}

	@Override
	public Utilisateur changerPseudo(Long userId, String newPseudo) throws NotFoundException {
		Utilisateur user = userDAO.findByIdAndVerifie(userId,true)
				.orElseThrow(this::getUserNotFoundException);
		user.setPseudo(newPseudo);
		return userDAO.save(user);
	}

	@Override
	public Utilisateur getInfos(Long userId) throws NotFoundException {
		return userDAO.findByIdAndVerifie(userId,true)
				.orElseThrow(this::getUserNotFoundException);
	}

	@Override
	public void supprimerCompte(Long userId) throws NotFoundException {
		userDAO.deleteByIdAndVerifie(userId, true);
	}

	@Override
	public String recupererTokenAcces(String refreshToken) throws NotFoundException {
		return tokenService.creerTokenAcces(refreshToken);
	}
	
	public UtilisateurDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UtilisateurDAO userDAO) {
		this.userDAO = userDAO;
	}

	public ApplicationEventPublisher getPublisher() {
		return publisher;
	}

	public void setPublisher(ApplicationEventPublisher publisher) {
		this.publisher = publisher;
	}

	public PasswordEncoder getEncoder() {
		return encoder;
	}

	public void setEncoder(PasswordEncoder encoder) {
		this.encoder = encoder;
	}
}
