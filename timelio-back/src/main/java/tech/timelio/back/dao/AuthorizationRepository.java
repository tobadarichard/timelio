package tech.timelio.back.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import tech.timelio.back.modele.Utilisateur;

@Repository
public interface AuthorizationRepository extends JpaRepository<Utilisateur, Long> {
	
	@Query("SELECT count(*) FROM EmploiTemps et WHERE et.id = ?1 "
			+ "AND et.owner = ?2")
	long countEmploi(Long emploiId,Utilisateur owner);
}
