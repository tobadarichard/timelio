package tech.timelio.back.business.interfaces;

import tech.timelio.back.business.interfaces.exceptions.AlreadyExistsException;
import tech.timelio.back.business.interfaces.exceptions.ExpiredTokenException;
import tech.timelio.back.business.interfaces.exceptions.NotFoundException;
import tech.timelio.back.business.interfaces.exceptions.UserNotVerifiedException;
import tech.timelio.back.modele.Utilisateur;

public interface GestionCompteService {
	/**
	 * Crée un nouvel utilisateur
	 * @param pseudo Le pseudo de l'utilisateur
	 * @param email L'email de l'utilisateur
	 * @param mdp Le mot de passe de l'utilisateur
	 * @param verifie Si le compte de l'utilisateur est verifie
	 * @return L'utilisateur crée
	 * @throws AlreadyExistsException Si un utilisateur possède déjà l'email
	 */
	public Utilisateur creerUtilisateur(String pseudo,String email,String mdp,
			boolean verifie) throws AlreadyExistsException;
	
	/**
	 * Connecte l'utilisateur
	 * @param email L'email de l'utilisateur
	 * @param mdp Le mot de passe de l'utilisateur
	 * @return L'utilisateur correspondant au couple email/mdp
	 * @throws NotFoundException Si l'utilisateur n'est pas trouvé
	 * @throws UserNotVerifiedException Si le compte de l'utilisateur n'est pas 
	 * vérifié
	 */
	public Utilisateur login(String email,String mdp) 
			throws NotFoundException, UserNotVerifiedException;
	
	/**
	 * Demande le changement de mot de passe de l'utilisateur. <br />
	 * Le mot de passe n'est pas changé mais un token pour changer de 
	 * mot de passe est crée.
	 * @param email L'adresse email de l'utilisateur qui veut reinitisaliser son
	 * mot de passe
	 * @throws NotFoundException Si aucun utilisateur ne possède cette adresse 
	 * email
	 */
	public void demandeResetMdp(String email) throws NotFoundException;
	
	/**
	 * Change le mot de passe de l'utilisateur
	 * @param resetMdpTokenValue Le token pour reinitialiser le mot de passe
	 * @param newMdp Le nouveau mot de passe
	 * @throws NotFoundException Si le token n'est pas trouvé
	 * @throws ExpiredTokenException Si le token est trouvé mais expiré
	 */
	public void resetMdp(String resetMdpTokenValue, String newMdp) 
			throws NotFoundException, ExpiredTokenException;
	
	/**
	 * Verifie le compte d'un utilisateur
	 * @param verifyTokenValue Le token pour vérifier le compte
	 * @throws NotFoundException Si le token n'est pas trouvé
	 * @throws ExpiredTokenException Si le token est trouvé mais expiré
	 */
	public void verifierCompte(String verifyTokenValue) 
			throws NotFoundException, ExpiredTokenException;
	
	/**
	 * Reinitialise le token de vérification de compte
	 * @param verifyTokenValue Le token pour vérifier le compte
	 * @throws NotFoundException Si le token n'est pas trouvé
	 */
	public void resetTokenVerifierCompte(String verifyTokenValue) 
			throws NotFoundException;
	
	/**
	 * Change le mot de passe d'un utilisateur
	 * @param userId L'identifiant de l'utilisateur
	 * @param oldMdp L'ancien mot de passe de l'utilisateur
	 * @param newMdp Le nouveau mot de passe de l'utilisateur
	 * @return L'utilisateur
	 * @throws NotFoundException Si l'utilisateur n'existe pas ou le mot de passe
	 *  est incorrect
	 */
	public Utilisateur changerMdp(Long userId,String oldMdp, String newMdp) 
			throws NotFoundException;
	
	/**
	 * Change le pseudo d'un utilisateur
	 * @param userId L'identifiant de l'utilisateur
	 * @param newPseudo Le nouveau pseudo de l'utilisateur
	 * @return L'utilisateur
	 * @throws NotFoundException Si l'utilisateur n'existe pas
	 */
	public Utilisateur changerPseudo(Long userId,String newPseudo) 
			throws NotFoundException;
	
	/**
	 * Renvoie l'utilisateur associé à un identifiant
	 * @param userId L'identifiant de l'utilisateur
	 * @return L'utilisateur associé
	 * @throws NotFoundException Si l'utilisateur n'existe pas 
	 */
	public Utilisateur getInfos(Long userId) throws NotFoundException;
	
	/**
	 * Supprime l'utilisateur avec l'identifiant précisé
	 * @param userId L'identifiant de l'utilisateur
	 * @throws NotFoundException
	 */
	public void supprimerCompte(Long userId) throws NotFoundException;
}
