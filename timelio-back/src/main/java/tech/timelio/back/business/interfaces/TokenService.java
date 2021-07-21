package tech.timelio.back.business.interfaces;

import tech.timelio.back.business.interfaces.exceptions.ExpiredTokenException;
import tech.timelio.back.business.interfaces.exceptions.NotFoundException;
import tech.timelio.back.modele.Token;
import tech.timelio.back.modele.TokenType;
import tech.timelio.back.modele.Utilisateur;

public interface TokenService {
	
	
	/**
	 * Recupere le token de rafraichissement pour l'utilisateur
	 * @param user L'utilisateur
	 * @return Le token sous forme String
	 */
	public String recupererTokenRafraichissement(Utilisateur user);
	
	/**
	 * Crée un token de vérification de compte pour l'utilisateur
	 * @param user L'utilisateur
	 * @return Le token de vérification de compte
	 */
	public Token creerTokenVerification(Utilisateur user);
	
	/**
	 * Crée un token pour changer de mot de passe
	 * @param user L'utilisateur
	 * @return Le token pour changement de mot de passe
	 */
	public Token creerTokenResetMdp(Utilisateur user);
	
	/**
	 * Trouve un utilisateur à partir de son token
	 * @param tokenValue La valeur du token
	 * @param type Le type du token
	 * @return L'utilisateur associé au token
	 * @throws NotFoundException Si le token n'est pas trouvé
	 * @throws ExpiredTokenException Si le token est expiré
	 */
	public Utilisateur trouverUtilisateurParToken(String tokenValue, TokenType type) 
			throws NotFoundException, ExpiredTokenException;
	
	/**
	 * Supprimer le token de reinitisaliton de l'utilisateur. <br />
	 * Cette méthode invalide également le token de rafraichissement : il faudra donc 
	 * se reconnecter.
	 * @param owner L'utilisateur
	 */
	public void supprimerTokenResetMdp(Utilisateur owner);
	
	/** Supprimer le token de vérification de l'utilisateur. <br />
	 * @param owner L'utilisateur
	 */
	public void supprimerTokenVerify(Utilisateur owner);

	/**
	 * Recrée le token de vérification
	 * @param verifyTokenValue L'ancien token de vérification
	 * @return Le nouveau token de vérification
	 * @throws NotFoundException Si l'ancien token n'est pas trouvé
	 */
	public Token recreerTokenVerification(String verifyTokenValue) throws NotFoundException;
	
	/**
	 * Crée un token d'acces pour l'utilisateur à partir de la valeur du token de 
	 * rafraichissement. Sa durée de validité est de 3 minutes.
	 * @param refreshToken Le token de rafraichissement
	 * @return Un chaine de caractère qui représente le token d'acces
	 * @throws NotFoundException Si le token n'est pas retrouvé
	 */
	public String creerTokenAcces(String refreshToken) throws NotFoundException;

}
