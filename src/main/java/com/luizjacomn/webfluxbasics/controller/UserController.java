package com.luizjacomn.webfluxbasics.controller;

import com.luizjacomn.webfluxbasics.model.request.UserRequest;
import com.luizjacomn.webfluxbasics.model.response.UserResponse;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserController {

    ResponseEntity<Mono<Void>> save(final UserRequest request);

    ResponseEntity<Mono<UserResponse>> find(final String id);

    ResponseEntity<Flux<UserResponse>> findAll();

    ResponseEntity<Mono<UserResponse>> update(final String id, final UserRequest request);

    ResponseEntity<Mono<Void>> delete(final String id);

}
