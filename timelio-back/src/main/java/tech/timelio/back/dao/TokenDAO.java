package tech.timelio.back.dao;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import tech.timelio.back.modele.Token;
import tech.timelio.back.modele.TokenType;
import tech.timelio.back.modele.Utilisateur;

@Repository
public interface TokenDAO extends PagingAndSortingRepository<Token, Long>{
	@Transactional
	long deleteByTypeAndOwner(TokenType type, Utilisateur owner);
	
	Optional<Token> findByValueAndType(String value,TokenType type);
}
