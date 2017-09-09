package ru.bakhaev.test.handlers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.bakhaev.test.response.ErrorResponse;
import ru.bakhaev.test.exceptions.AuthException;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
public class AuthExceptionHandler {

	@ExceptionHandler(AuthException.class)
	public ResponseEntity<?> handleControllerException(AuthException ex) {
		return new ResponseEntity<>(new ErrorResponse(ex.getSuccess(), ex.getMessage(), ex.getHttpStatus()), ex.getHttpStatus());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleControllerException(Exception ex) {
		return new ResponseEntity<>(new ErrorResponse(false, ex.getMessage(), INTERNAL_SERVER_ERROR), INTERNAL_SERVER_ERROR);
	}

}
