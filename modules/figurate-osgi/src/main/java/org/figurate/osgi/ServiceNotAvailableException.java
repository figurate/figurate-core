package org.figurate.osgi;

public class ServiceNotAvailableException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ServiceNotAvailableException(String message) {
		super(message);
	}

	public ServiceNotAvailableException(Throwable cause) {
		super(cause);
	}

	public ServiceNotAvailableException(String message, Throwable cause) {
		super(message, cause);
	}
}
