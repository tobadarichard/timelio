package tech.timelio.back.auth;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import tech.timelio.back.controllers.UtilisateurId;
import tech.timelio.back.dao.UtilisateurDAO;

public class AuthenticationFilter implements Filter {
	protected UtilisateurDAO utilisateurDAO;
	protected JwtParser parser;
	
	public AuthenticationFilter(UtilisateurDAO utilisateurDAO, JwtParser parser) {
		this.utilisateurDAO = utilisateurDAO;
		this.parser = parser;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, 
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		String token = httpRequest.getHeader("Authorization");
		if (token == null || token.length() < 7) {
			httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}
		token = token.substring(7);
		
		try {
			Long userId = parser.parseClaimsJws(token).getBody()
					.get("userId", Long.class);
			httpRequest.setAttribute("userId", new UtilisateurId(userId));
			chain.doFilter(httpRequest, httpResponse);
		} catch (JwtException e) {
			httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}		
	}
	

	public UtilisateurDAO getUtilisateurDAO() {
		return utilisateurDAO;
	}

	public void setUtilisateurDAO(UtilisateurDAO utilisateurDAO) {
		this.utilisateurDAO = utilisateurDAO;
	}

	public JwtParser getParser() {
		return parser;
	}

	public void setParser(JwtParser parser) {
		this.parser = parser;
	}
}
