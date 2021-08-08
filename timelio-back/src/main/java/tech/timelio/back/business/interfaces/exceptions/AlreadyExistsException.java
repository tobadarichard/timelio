package tech.timelio.back.business.interfaces.exceptions;

public class AlreadyExistsException extends Exception {
	private static final long serialVersionUID = -7346699794041876982L;

	public AlreadyExistsException(String message) {
		super(message);
	}
}
