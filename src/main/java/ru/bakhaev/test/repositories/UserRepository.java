package ru.bakhaev.test.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import ru.bakhaev.test.dto.User;

public interface UserRepository extends ReactiveCrudRepository<User, Long> {

    Mono<User> findByToken(String token);

    Mono<User> findByEmailAndPassword(String email, String password);

    Mono<User> findByEmail(String email);

}
