package com.luizjacomn.webfluxbasics.service;

import com.luizjacomn.webfluxbasics.entity.User;
import com.luizjacomn.webfluxbasics.mapper.UserMapper;
import com.luizjacomn.webfluxbasics.model.request.UserRequest;
import com.luizjacomn.webfluxbasics.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class UserService implements StandardService<User, UserRequest> {

    private final UserRepository repository;

    private final UserMapper userMapper;

    public Mono<User> save(final UserRequest newUser) {
        return repository.save(userMapper.toEntity(newUser));
    }

    public Mono<User> findById(String id) {
        return repository.findById(id)
                         .switchIfEmpty(this.handleNotFound(id, User.class));
    }

    public Flux<User> findAll() {
        return repository.findAll();
    }

    public Mono<User> update(String id, final UserRequest updatedUser) {
        return this.findById(id)
                   .map(user -> userMapper.toEntity(updatedUser, user))
                   .flatMap(repository::save);
    }

    public Mono<User> delete(final String id) {
        return repository.findAndRemove(id)
                         .switchIfEmpty(this.handleNotFound(id, User.class));
    }

}
