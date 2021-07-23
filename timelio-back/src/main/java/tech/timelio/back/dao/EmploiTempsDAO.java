package tech.timelio.back.dao;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import tech.timelio.back.modele.EmploiTemps;
import tech.timelio.back.modele.Utilisateur;

//TODO : evenements ?
@Repository
public interface EmploiTempsDAO 
	extends PagingAndSortingRepository<EmploiTemps,Long> {
	
	Optional<EmploiTemps> findOneByCodeAcces(String codeAcces);
	
	@EntityGraph(attributePaths = "evenements",type = EntityGraphType.LOAD)
	Optional<EmploiTemps> findWithEventsById(Long id);
	
	@EntityGraph(attributePaths = "evenements",type = EntityGraphType.LOAD)
	Optional<EmploiTemps> findOneWithEventsByCodeAcces(String codeAcces);
	
	Page<EmploiTemps> findAllByOwner(Utilisateur owner,Pageable pagination);
}
