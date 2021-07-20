package tech.timelio.back.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.timelio.back.dao.AuthRepository;
import tech.timelio.back.modele.Utilisateur;

@Service
public class AuthService {
	@Autowired
	protected AuthRepository authRepo;
	
	public void isEmploiOwner(Long emploiId,Utilisateur owner) 
			throws ForbiddenActionException{
		if (authRepo.countEmploi(emploiId, owner) == 0)
			throw new ForbiddenActionException();
	}

	public AuthRepository getAuthRepo() {
		return authRepo;
	}

	public void setAuthRepo(AuthRepository authRepo) {
		this.authRepo = authRepo;
	}
}