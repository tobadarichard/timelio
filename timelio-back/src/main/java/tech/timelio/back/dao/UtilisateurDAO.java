package tech.timelio.back.dao;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import tech.timelio.back.modele.Utilisateur;

@Repository
public interface UtilisateurDAO extends 
	PagingAndSortingRepository<Utilisateur, Long> {
	
	boolean existsByEmail(String email);
	
	Optional<Utilisateur> findByEmailAndMdp(String email,String mdp);
	
	Optional<Utilisateur> findByEmail(String email);

}
