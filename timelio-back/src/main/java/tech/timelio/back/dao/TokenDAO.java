package tech.timelio.back.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import tech.timelio.back.modele.Token;

@Repository
public interface TokenDAO extends PagingAndSortingRepository<Token, Long>{

}
