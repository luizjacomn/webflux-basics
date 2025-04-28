package com.luizjacomn.webfluxbasics.service;

import com.luizjacomn.webfluxbasics.entity.User;
import com.luizjacomn.webfluxbasics.mapper.UserMapper;
import com.luizjacomn.webfluxbasics.model.request.UserRequest;
import com.luizjacomn.webfluxbasics.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository repository;

    private final UserMapper mapper;

    public Mono<User> save(final UserRequest request) {
        return repository.save(mapper.toEntity(request));
    }

}
