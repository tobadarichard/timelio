package tech.timelio.back.dao;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import tech.timelio.back.modele.Utilisateur;

@Repository
public interface UtilisateurDAO extends 
	PagingAndSortingRepository<Utilisateur, Long> {
	
	boolean existsByEmail(String email);
	
	Optional<Utilisateur> findByEmail(String email);
	
	Optional<Utilisateur> findByEmailAndVerifie(String email,boolean verifie);
	
	Optional<Utilisateur> findByIdAndVerifie(Long userId,boolean verifie);
	
	@Transactional
	long deleteByIdAndVerifie(Long id,boolean verifie);

}
