package ru.bakhaev.test.response;

import ru.bakhaev.test.dto.User;

public class UserResponse extends CustomResponseEntity {
    private User user;

    public UserResponse() {
    }

    public UserResponse(Boolean success, String message, User user) {
        super(success, message);
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
