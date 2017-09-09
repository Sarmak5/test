package ru.bakhaev.test.exceptions;

import org.springframework.http.HttpStatus;

public class AuthException extends RuntimeException {

	private static final long serialVersionUID = 8631466448974323851L;

	private final HttpStatus error;
	private final String message;
	private final Boolean success;

	public AuthException(HttpStatus error, String message, Boolean success) {
		this.error = error;
		this.message = message;
		this.success = success;
	}

	public HttpStatus getHttpStatus() {
		return error;
	}
	
	public String getMessage() {
		return message;
	}

	public Boolean getSuccess() {
		return success;
	}
}
