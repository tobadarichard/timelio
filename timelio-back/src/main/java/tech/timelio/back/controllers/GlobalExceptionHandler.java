package tech.timelio.back.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import tech.timelio.back.auth.ForbiddenActionException;
import tech.timelio.back.business.interfaces.exceptions.AlreadyExistsException;
import tech.timelio.back.business.interfaces.exceptions.ExpiredTokenException;
import tech.timelio.back.business.interfaces.exceptions.NotFoundException;
import tech.timelio.back.business.interfaces.exceptions.UserNotVerifiedException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(NotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<String> handleNotFound(NotFoundException e){
		return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(ForbiddenActionException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ResponseEntity<String> handleForbidden(ForbiddenActionException e){
		return new ResponseEntity<>(HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(AlreadyExistsException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ResponseEntity<String> handleAlreadyExists(AlreadyExistsException e){
		return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(UserNotVerifiedException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<String> handleNotVerified(UserNotVerifiedException e){
		return new ResponseEntity<>("Compte non vérifié",HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ExpiredTokenException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<String> handleExpired(ExpiredTokenException e){
		return new ResponseEntity<>("Token expiré",HttpStatus.BAD_REQUEST);
	}
}
