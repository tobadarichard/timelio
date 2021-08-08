package tech.timelio.back.business.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import tech.timelio.back.business.interfaces.exceptions.NotFoundException;
import tech.timelio.back.modele.Evenement;

public interface EvenementService {
	
	/**
	 * Crée un evenement en le persistant dans la base de données
	 * @param event L'evenement à créer
	 * @return L'evenement crée
	 */
	public Evenement creerEvenement(Evenement event);
	
	/**
	 * Met à jour un evenement
	 * @param idEmploi L'identifiant de l'emploi du temps contenant l'evenement
	 * @param event L'evenement à mettre à jour
	 * @return L'evenement mis à jour
	 * @throws NotFoundException Si l'evenement n'est pas trouvé
	 */
	public Evenement majEvenement(Long idEmploi, Evenement event) throws NotFoundException;
	
	/**
	 * Supprimer un evenement
	 * @param idEmploi L'identifiant de l'emploi du temps contenant l'evenement
	 * @param id L'identifiant de l'evenement
	 * @throws NotFoundException Si l'evenement n'est pas trouvé
	 */
	public void supprimerEvenement(Long idEmploi, Long id) throws NotFoundException;
	
	/**
	 * Recupere un evenement
	 * @param idEmploi L'identifiant de l'emploi du temps contenant l'evenement
	 * @param id L'identifiant de l'evenement
	 * @return L'evenement correspondant
	 * @throws NotFoundException Si l'evenement n'est pas trouvé
	 */
	public Evenement recupererEvenement(Long idEmploi, Long id) throws NotFoundException;
	
	/**
	 * Recupere une page d'evenements d'un certain emploi du temps
	 * @param idEmploi L'identifiant de l'emploi du temps
	 * @param pagination La page de donnée voulue
	 * @return La page de donnée récupérée
	 */
	public Page<Evenement> listerEvenements(Long idEmploi,Pageable pagination);
	
	/**
	 * Recherche des evenements dans un emploi du temps
	 * @param criteres Les criteres de la recherche
	 * @param idEmploi L'id de l'emploi du temps
	 * @return La liste des résultats trouvés
	 */
	public List<Evenement> chercherEvenements(CriteresRerchercheEvents criteres, Long idEmploi);
	
	
}
