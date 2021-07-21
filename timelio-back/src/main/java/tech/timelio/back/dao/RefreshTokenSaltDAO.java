package tech.timelio.back.dao;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import tech.timelio.back.modele.RefreshTokenSalt;
import tech.timelio.back.modele.Utilisateur;

@Repository
public interface RefreshTokenSaltDAO 
	extends PagingAndSortingRepository<RefreshTokenSalt,Long> {

	@Transactional
	long deleteByOwner(Utilisateur owner);
	
	Optional<RefreshTokenSalt> findByOwner(Utilisateur owner);
	
	boolean existsByValueAndOwnerId(String value,Long userId);
}
