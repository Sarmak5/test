package ru.bakhaev.test.response;

import org.springframework.http.HttpStatus;

public class ErrorResponse extends CustomResponseEntity {
    private HttpStatus error;

    public ErrorResponse() {
    }

    public ErrorResponse(Boolean success, String message, HttpStatus error) {
        super(success, message);
        this.error = error;
    }

    public HttpStatus getError() {
        return error;
    }
}
