package tech.timelio.back.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;

import tech.timelio.back.business.impl.GestionCompteServiceImpl;
import tech.timelio.back.business.interfaces.TokenService;
import tech.timelio.back.business.interfaces.UtilisateurEtToken;
import tech.timelio.back.business.interfaces.exceptions.AlreadyExistsException;
import tech.timelio.back.business.interfaces.exceptions.NotFoundException;
import tech.timelio.back.business.interfaces.exceptions.UserNotVerifiedException;
import tech.timelio.back.modele.Token;
import tech.timelio.back.modele.Utilisateur;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
@Import(GestionCompteServiceImpl.class)
class GestionCompteServiceTest {
	@MockBean
	ApplicationEventPublisher publisher;
	@MockBean
	PasswordEncoder encoder;
	@MockBean
	TokenService tokenService;
	@Autowired
	GestionCompteServiceImpl service;
	@PersistenceContext
	EntityManager entityManager;
	
	Utilisateur userTest;
	
	
	@BeforeEach
	void setup() {
		service.setPublisher(publisher);
		userTest = new Utilisateur();
		userTest.setEmail("usermail@mail.com");
		userTest.setMdp("mdp");
		userTest.setVerifie(true);
		userTest.setPseudo("User test");
		entityManager.persist(userTest);
	}
	
	@Test
	void testShouldCreateUser() throws AlreadyExistsException {
		when(tokenService.creerTokenVerification(any(Utilisateur.class))).thenReturn(new Token());
		Utilisateur created = service.creerUtilisateur("pseudo", "mail@mail.com", "mdp", false);
		
		verify(publisher).publishEvent(any(Object.class));
		assertNotNull(created.getId());
	}
	
	@Test
	void testShouldFailLoginNotFound() {
		assertThrows(NotFoundException.class, () -> service.login("email", "mdp"));
	}
	
	@Test
	void testShouldLogin() throws NotFoundException, UserNotVerifiedException {
		when(tokenService.recupererTokenRafraichissement(any(Utilisateur.class)))
			.thenReturn("tokenTest");
		when(encoder.matches(any(), any())).thenReturn(true);
		UtilisateurEtToken result = service.login("usermail@mail.com", "mdp");
		
		assertEquals(userTest.getId(), result.getUserInfos().getId());
		assertEquals("tokenTest", result.getRefreshToken());
	}
}
