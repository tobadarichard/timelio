package tech.timelio.back.dao;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import tech.timelio.back.modele.Evenement;

@Repository
public interface EvenementDAO 
	extends PagingAndSortingRepository<Evenement,Long> {
	
	Optional<Evenement> findByIdAndEmploiTempsId(Long id, Long idEmploi);
	
	boolean existsByIdAndEmploiTempsId(Long id, Long idEmploi);

	Page<Evenement> findAllByEmploiTempsId(Long idEmploi,Pageable pagination);
	
	@Query("SELECT event FROM Evenement event WHERE event.emploiTemps.id = ?1 "
			+ "AND event.dateDebut > ?2 "
			+ "AND event.dateDebut < ?3 "
			+ "AND event.description like %?4%"
			+ "ORDER BY event.dateDebut ASC")
	List<Evenement> searchEvents(Long idEmploi, ZonedDateTime start, ZonedDateTime end,
			String description);
}
