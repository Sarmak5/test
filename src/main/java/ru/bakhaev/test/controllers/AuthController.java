package ru.bakhaev.test.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.bakhaev.test.response.CustomResponseEntity;
import ru.bakhaev.test.response.TokenResponse;
import ru.bakhaev.test.dto.User;
import ru.bakhaev.test.response.UserResponse;
import ru.bakhaev.test.exceptions.AuthException;
import ru.bakhaev.test.services.AuthService;

@RestController
public class AuthController {

    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "/login")
    public Mono<? extends CustomResponseEntity> login(@RequestBody User user) {
        return authService.findByEmailAndPassword(user.getEmail(), user.getPassword())
            .map(found -> new UserResponse(true, "", found))
            .switchIfEmpty(
                Mono.error(new AuthException(HttpStatus.FORBIDDEN, "Wrong credentials", false))
            );
    }

    @PostMapping(value = "/register")
    public Mono<CustomResponseEntity> register(@RequestBody User user) {
        return Mono.justOrEmpty(user.getEmail())
                .flatMap(authService::findByEmail)
                .defaultIfEmpty(new User())
                .flatMap(existedUser -> {
                    if (existedUser.getId() != null) {
                        LOG.info("User already exists {}", user);
                        throw new AuthException(HttpStatus.BAD_REQUEST,
                                "User already exists", false);
                    }
                    return this.authService.save(user)
                            .map(currentUser -> new TokenResponse(true, "", currentUser.getToken()));
                });
    }

    @PostMapping(value = "/confirm")
    public Mono<CustomResponseEntity> confirm(@RequestBody User user) {
        return Mono.justOrEmpty(user.getToken())
                .flatMap(authService::findByToken)
                .defaultIfEmpty(new User())
                .flatMap(existedUser -> {
                    if (existedUser.getId() == null) {
                        LOG.info("Token wasn't found {}", user);
                        throw new AuthException(HttpStatus.BAD_REQUEST,
                                "Token wasn't found", false);
                    }
                    return this.authService.findByToken(user.getToken())
                            .map(currentUser -> new UserResponse(true, "", currentUser));
                });
    }
}
