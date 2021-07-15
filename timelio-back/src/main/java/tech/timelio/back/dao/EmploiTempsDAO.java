package tech.timelio.back.dao;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import tech.timelio.back.modele.EmploiTemps;
import tech.timelio.back.modele.Utilisateur;

@Repository
public interface EmploiTempsDAO 
	extends PagingAndSortingRepository<EmploiTemps,Long> {
	
	Optional<EmploiTemps> findOneByCodeAcces(String codeAcces);
	
	Page<EmploiTemps> findAllByOwner(Utilisateur owner,Pageable pagination);
}
