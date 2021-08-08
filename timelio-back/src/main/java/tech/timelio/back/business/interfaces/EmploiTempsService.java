package tech.timelio.back.business.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import tech.timelio.back.business.interfaces.exceptions.NotFoundException;
import tech.timelio.back.modele.EmploiTemps;
import tech.timelio.back.modele.Utilisateur;

/**
 * Service chargé des opérations sur les emplois du temps
 *
 */
public interface EmploiTempsService {
	/**
	 * Crée un nouvel emploi du temps pour un utilisateur
	 * @param nom Le nom de l'emploi du temps
	 * @param publique Si l'emploi du temps doit être publique
	 * @param owner L'utilisateur à qui appartient l'emploi du temps (non nul)
	 * @return L'emploi du temps crée
	 * @throws IllegalArgumentException Si l'emploi du temps n'est pas publique et que
	 *  l'utilisateur est nul
	 */
	public EmploiTemps creerEmploi(String nom, boolean publique,
			Utilisateur owner);
	
	/**
	 * Crée un nouvel emploi du temps anonyme. <br />
	 * Un emploi du temps anonyme est publique, n'a pas de propriétaire et 
	 * est accessible depuis son code d'acces.
	 * @param nom Le nom de l'emploi du temps
	 * @return L'emploi du temps crée
	 */
	public EmploiTemps creerEmploi(String nom);
	
	/**
	 * Supprimer un emploi du temps
	 * @param id L'identifiant de l'emploi du temps à supprimer
	 * @throws NotFoundException Si l'emploi du temps n'est pas trouvé
	 */
	public void supprimerEmploi(Long id) throws NotFoundException;
	
	/**
	 * Supprimer un emploi du temps
	 * @param codeAcces Le code d'acces du cours
	 * @throws NotFoundException Si l'emploi du temps n'est pas trouvé
	 */
	public void supprimerEmploi(String codeAcces) throws NotFoundException;
	
	/**
	 * Recupere un emploi du temps
	 * @param id L'identifiant de l'emploi du temps recherché
	 * @return L'emploi du temps
	 * @throws NotFoundException Si l'emploi du temps n'est pas trouvé
	 */
	public EmploiTemps recupererEmploi(Long id) throws NotFoundException;
	
	/**
	 * Recupere un emploi du temps
	 * @param codeAcces Le code d'acces du cours
	 * @return L'emploi du temps
	 * @throws NotFoundException Si l'emploi du temps n'est pas trouvé
	 */
	public EmploiTemps recupererEmploi(String codeAcces) throws NotFoundException;
	
	/**
	 * Met à jour un emploi du temps privé
	 * @param id L'identifiant de l'emploi du temps
	 * @param newNom Le nouveau nom
	 * @param newPublique La nouvelle visibilité
	 * @return L'emploi du temps mis à jour
	 * @throws NotFoundException SI l'emploi du temps n'est pas trouvé
	 */
	public EmploiTemps majEmploi(Long id, String newNom,boolean newPublique) 
			throws NotFoundException;
	
	/**
	 * Met à jour un emploi du temps publique
	 * @param codeAcces Le code d'acces de l'emploi du temps
	 * @param newNom Le nouveau nom
	 * @return L'emploi du temps mis à jour
	 * @throws NotFoundException SI l'emploi du temps n'est pas trouvé
	 */
	public EmploiTemps majEmploi(String codeAcces, String newNom) 
			throws NotFoundException;
	
	/**
	 * Recupere les emplois du temps d'un utilisateur
	 * @param owner L'utilisateur dont on veut récuperer les emplois du temps
	 * @param pagination La page de données recherchée
	 * @return La partie voulue des emplois du temps
	 */
	public Page<EmploiTemps> listeEmploi(Utilisateur owner, Pageable pagination);

}
