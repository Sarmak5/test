package ru.bakhaev.test.response;

public class CustomResponseEntity {
    private Boolean success;
    private String message;

    public CustomResponseEntity() {
    }

    public CustomResponseEntity(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
