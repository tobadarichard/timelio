package tech.timelio.back.business.interfaces.exceptions;

public class NotFoundException extends Exception {
	private static final long serialVersionUID = 3599318864878652269L;
	
	public NotFoundException(String message) {
		super(message);
	}
}
