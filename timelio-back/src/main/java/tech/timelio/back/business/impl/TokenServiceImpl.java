package tech.timelio.back.business.impl;

import java.time.LocalDateTime;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import tech.timelio.back.business.interfaces.TokenService;
import tech.timelio.back.business.interfaces.exceptions.ExpiredTokenException;
import tech.timelio.back.business.interfaces.exceptions.NotFoundException;
import tech.timelio.back.dao.RefreshTokenSaltDAO;
import tech.timelio.back.dao.TokenDAO;
import tech.timelio.back.modele.RefreshTokenSalt;
import tech.timelio.back.modele.Token;
import tech.timelio.back.modele.TokenType;
import tech.timelio.back.modele.Utilisateur;
import tech.timelio.back.utils.CodeGenerator;

@Service
public class TokenServiceImpl implements TokenService {
	@Value("${timelio.secret}") 
	protected String stringKey;
	@Autowired 
	protected TokenDAO tokenDAO;
	@Autowired
	protected RefreshTokenSaltDAO saltDAO;
	@Autowired
	protected JwtParser parser;
	public static final String USER_ID_CLAIM = "userId";
	
	private Token creerToken(Utilisateur user,TokenType type) {
		tokenDAO.deleteByTypeAndOwner(type, user);
		
		Token token = new Token();
		LocalDateTime now = LocalDateTime.now();
		token.setCreatedAt(now);
		token.setExpiredAt(now.plusHours(2));
		token.setOwner(user);
		token.setType(type);
		token.setValue(CodeGenerator.genererValeurToken());
		
		return tokenDAO.save(token);
	}
	
	private String creerTokenDepuisSel(String sel,String pseudo,Long userId) {
		SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(stringKey));
		return Jwts.builder()
				.setSubject(pseudo)
				.claim("value", sel)
				.claim(USER_ID_CLAIM, userId)
				.setIssuedAt(new Date())
				.signWith(key).compact();
	}
	
	private String creerTokenAcces(Long userId) {
		SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(stringKey));
		Date start = new Date();
		Date expire = new Date(start.getTime()+ (1000*60*3)); // start + 3 minutes
		return Jwts.builder()
				.claim(USER_ID_CLAIM, userId)
				.setIssuedAt(start)
				.setExpiration(expire)
				.signWith(key).compact();
	}
	
	private String creerTokenRafraichissement(Utilisateur user) {
		RefreshTokenSalt salt = new RefreshTokenSalt();
		salt.setValue(CodeGenerator.genererSelToken());
		salt.setOwner(user);
		salt = saltDAO.save(salt);
		
		return creerTokenDepuisSel(salt.getValue(), user.getPseudo(),user.getId());
	}

	@Override
	public String recupererTokenRafraichissement(Utilisateur user) {
		RefreshTokenSalt salt = user.getRefreshTokenSalt();
		if (salt == null)
			return creerTokenRafraichissement(user);
		else
			return creerTokenDepuisSel(salt.getValue(), user.getPseudo(),user.getId());
	}

	@Override
	public Token creerTokenVerification(Utilisateur user) {
		return creerToken(user, TokenType.VERIFY_ACCOUNT);
	}

	@Override
	public Token creerTokenResetMdp(Utilisateur user) {
		return creerToken(user, TokenType.RESET_PASSWORD);
	}

	@Override
	public Utilisateur trouverUtilisateurParToken(String tokenValue, TokenType type)
			throws NotFoundException, ExpiredTokenException {
		Token token = tokenDAO.findByValueAndType(tokenValue, type)
				.orElseThrow(() -> new NotFoundException("Non trouvé"));
		if (LocalDateTime.now().isAfter(token.getExpiredAt()))
			throw new ExpiredTokenException();
		return token.getOwner();
	}
	
	@Override
	public void supprimerTokenResetMdp(Utilisateur owner) {
		tokenDAO.deleteByTypeAndOwner(TokenType.RESET_PASSWORD, owner);
		saltDAO.deleteByOwner(owner);
		creerTokenRafraichissement(owner);
	}
	
	@Override
	public void supprimerTokenVerify(Utilisateur owner) {
		tokenDAO.deleteByTypeAndOwner(TokenType.VERIFY_ACCOUNT, owner);
	}
	
	@Override
	public Token recreerTokenVerification(String verifyTokenValue) throws NotFoundException {
		Token token = tokenDAO.findByValueAndType(verifyTokenValue, TokenType.VERIFY_ACCOUNT)
				.orElseThrow(() -> new NotFoundException("Non trouvé"));
		return creerTokenVerification(token.getOwner());
	}
	
	@Override
	public String creerTokenAcces(String refreshToken) throws NotFoundException {
		try {
			Claims claims = parser.parseClaimsJws(refreshToken).getBody();
			String tokenValue = claims.get("value", String.class);
			Long userId = claims.get(USER_ID_CLAIM, Long.class);
			if (!saltDAO.existsByValueAndOwnerId(tokenValue, userId))
				throw new NotFoundException("Token invalide");
			
			return creerTokenAcces(userId);
		} catch (JwtException e) {
			throw new NotFoundException("Token invalide");
		}
		
	}
}
