package tech.timelio.back;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import tech.timelio.back.auth.AuthenticationFilter;
import tech.timelio.back.dao.UtilisateurDAO;

@SpringBootApplication
public class TimelioBackApplication{

	public static void main(String[] args) {
		SpringApplication.run(TimelioBackApplication.class, args);
	}
	
	//Le filtre qui vérifie l'authentification
	@Bean
	public FilterRegistrationBean<AuthenticationFilter> getAuthFilter(
			UtilisateurDAO utilisateurDAO,JwtParser parser) {
		AuthenticationFilter filter =
				new AuthenticationFilter(utilisateurDAO,parser);
		FilterRegistrationBean<AuthenticationFilter> registrationBean = 
				new FilterRegistrationBean<>();
		registrationBean.setFilter(filter);
		//TODO : mettre à jour les chemins
		registrationBean.addUrlPatterns("/user/*");
		return registrationBean;
	}
	
	@Bean
	public Hibernate5Module datatypeHibernateModule() {
	  return new Hibernate5Module();
	}
	
	@Bean
	public JwtParser getJwtParser(@Value("${timelio.secret}") String stringKey) {
		SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(stringKey));
		return Jwts.parserBuilder().setSigningKey(key).build();
	}
	
	@Bean
	public PasswordEncoder encoder() {
	    return new BCryptPasswordEncoder();
	}
}
