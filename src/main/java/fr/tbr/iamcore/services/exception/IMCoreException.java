package fr.tbr.iamcore.services.exception;

/**
 * This exception encapsulates the exception in the application
 * @author Dattaprasad
 *
 */
public class IMCoreException extends Exception {

	public IMCoreException() {
		super();
	}
	
	public IMCoreException(String msg) {
		super(msg);
	}

	public IMCoreException(String msg, Throwable throwable) {
		super(msg, throwable);
	}
}
