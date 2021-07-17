package tech.timelio.back.business;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import tech.timelio.back.business.impl.EmploiTempsServiceImpl;
import tech.timelio.back.business.interfaces.exceptions.NotFoundException;
import tech.timelio.back.modele.EmploiTemps;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import(EmploiTempsServiceImpl.class)
class EmploiTempsServiceTest {
	@PersistenceContext
	public EntityManager entityManager;
	@Autowired
	public EmploiTempsServiceImpl emploiService;
	
	@Test
	void testShouldAddPublicEmploi() {
		EmploiTemps emploi = emploiService.creerEmploi("Mon emploi test", true, null);
		assertEquals(30, emploi.getCodeAcces().length());
		assertNotNull(emploi.getId());
	}
	
	@Test
	void testShouldThrowIllegalArgument() {
		assertThrows(IllegalArgumentException.class, 
				() -> emploiService.creerEmploi("Mon emploi test", false, null));
	}
	
	@Test
	void testShouldThrowNonUniqueCodeAcces() {
		EmploiTemps emploi = new EmploiTemps();
		emploi.setCodeAcces("test");
		entityManager.persist(emploi);
		EmploiTemps emploiCopieCode = new EmploiTemps();
		emploiCopieCode.setCodeAcces("test");
		entityManager.persist(emploiCopieCode);
		
		assertThrows(PersistenceException.class,() -> entityManager.flush());
	}

	@Test
	void testDeleteShouldThrowNotFound() {
		assertThrows(NotFoundException.class,() -> emploiService.supprimerEmploi(5l));
	}
	
	@Test
	void testShouldDelete() {
		EmploiTemps emploi = new EmploiTemps();
		entityManager.persist(emploi);
		entityManager.flush();
		
		assertDoesNotThrow(() -> emploiService.supprimerEmploi(emploi.getId()));
		assertNull(entityManager.find(EmploiTemps.class, emploi.getId()));
	}
	
	@Test
	void testGetShouldThrowNotFound() {
		assertThrows(NotFoundException.class,() -> emploiService.recupererEmploi("test"));
	}
	
	@Test
	void testShouldGet() {
		EmploiTemps emploi = new EmploiTemps();
		emploi.setCodeAcces("test");
		entityManager.persist(emploi);
		entityManager.flush();
		
		try {
			EmploiTemps emploiRecupere = 
					emploiService.recupererEmploi("test");
			assertEquals(emploi.getId(),emploiRecupere.getId());
		} catch (NotFoundException e) {
			fail("Should not throw not found");
		}
	}
	
	@Test
	void testShouldUpdate() {
		EmploiTemps oldEmploi = new EmploiTemps();
		oldEmploi.setNom("ancien");
		oldEmploi.setPublique(true);
		entityManager.persist(oldEmploi);
		entityManager.flush();
		
		assertDoesNotThrow(() -> emploiService.majEmploi(oldEmploi.getId(),
				"nouveau",true));
		EmploiTemps nouveauEmploi = entityManager.find(EmploiTemps.class,
				oldEmploi.getId());
		assertEquals("nouveau", nouveauEmploi.getNom());
	}
}
