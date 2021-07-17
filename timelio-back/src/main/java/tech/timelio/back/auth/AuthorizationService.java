package tech.timelio.back.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.timelio.back.dao.AuthorizationRepository;
import tech.timelio.back.modele.Utilisateur;

@Service
public class AuthorizationService {
	@Autowired
	protected AuthorizationRepository authRepo;
	
	public void isEmploiOwner(Long emploiId,Utilisateur owner) 
			throws ForbiddenActionException{
		if (authRepo.countEmploi(emploiId, owner) == 0)
			throw new ForbiddenActionException();
	}

	public AuthorizationRepository getAuthRepo() {
		return authRepo;
	}

	public void setAuthRepo(AuthorizationRepository authRepo) {
		this.authRepo = authRepo;
	}
}