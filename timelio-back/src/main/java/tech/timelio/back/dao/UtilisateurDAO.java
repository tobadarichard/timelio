package tech.timelio.back.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import tech.timelio.back.modele.Utilisateur;

@Repository
public interface UtilisateurDAO extends 
	PagingAndSortingRepository<Utilisateur, Long> {

}
