package tech.timelio.back.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import tech.timelio.back.modele.Evenement;

@Repository
public interface EvenementDAO 
	extends PagingAndSortingRepository<Evenement,Long> {

}
