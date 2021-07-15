package tech.timelio.back.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import tech.timelio.back.modele.RefreshTokenSalt;

@Repository
public interface RefreshTokenSaltDAO 
	extends PagingAndSortingRepository<RefreshTokenSalt,Long> {

}
