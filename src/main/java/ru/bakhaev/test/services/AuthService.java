package ru.bakhaev.test.services;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.bakhaev.test.dto.User;
import ru.bakhaev.test.repositories.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final TokenService tokenService;

    public AuthService(UserRepository userRepository,
                       TokenService tokenService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    //if we don't use mongodb we'll add @Transactional
    public Mono<User> save(User user) {
        user.setToken(tokenService.generateToken(user.getEmail()));
        user.setCreated(System.currentTimeMillis());
        return userRepository.save(user);
    }

    public Mono<User> findByToken(String token) {
        return userRepository.findByToken(token);
    }

    public Mono<User> findByEmailAndPassword(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }

    public Mono<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
