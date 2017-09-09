package ru.bakhaev.test.response;

public class TokenResponse extends CustomResponseEntity {
    private String token;

    public TokenResponse() {
    }

    public TokenResponse(Boolean success, String message, String token) {
        super(success, message);
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
