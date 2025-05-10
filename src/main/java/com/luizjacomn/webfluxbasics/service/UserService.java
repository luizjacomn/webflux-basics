package com.luizjacomn.webfluxbasics.service;

import com.luizjacomn.webfluxbasics.entity.User;
import com.luizjacomn.webfluxbasics.repository.UserRepository;
import com.luizjacomn.webfluxbasics.service.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository repository;

    public Mono<User> save(final User newUser) {
        return repository.save(newUser);
    }

    public Mono<User> findById(String id) {
        return repository.findById(id)
                         .switchIfEmpty(Mono.error(new ObjectNotFoundException("User not found: id = %s".formatted(id))));
    }

    public Flux<User> findAll() {
        return repository.findAll();
    }

}
