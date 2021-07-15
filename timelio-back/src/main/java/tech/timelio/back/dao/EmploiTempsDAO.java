package tech.timelio.back.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import tech.timelio.back.modele.EmploiTemps;

@Repository
public interface EmploiTempsDAO 
	extends PagingAndSortingRepository<EmploiTemps,Long> {

}
