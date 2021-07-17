package tech.timelio.back.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import tech.timelio.back.auth.ForbiddenActionException;
import tech.timelio.back.business.interfaces.exceptions.NotFoundException;

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
}
