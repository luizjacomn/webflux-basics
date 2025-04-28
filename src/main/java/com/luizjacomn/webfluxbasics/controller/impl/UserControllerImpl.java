package com.luizjacomn.webfluxbasics.controller.impl;

import com.luizjacomn.webfluxbasics.controller.UserController;
import com.luizjacomn.webfluxbasics.model.request.UserRequest;
import com.luizjacomn.webfluxbasics.model.response.UserResponse;
import com.luizjacomn.webfluxbasics.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserControllerImpl implements UserController {

    private final UserService service;

    @PostMapping
    @Override
    public ResponseEntity<Mono<Void>> save(@RequestBody final UserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.save(request)
                .then()
        );
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<Mono<UserResponse>> find(@PathVariable final String id) {
        return null;
    }

    @GetMapping
    @Override
    public ResponseEntity<Flux<UserResponse>> findAll() {
        return null;
    }

    @PatchMapping("/{id}")
    @Override
    public ResponseEntity<Mono<UserResponse>> update(@PathVariable final String id, @RequestBody final UserRequest request) {
        return null;
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<Mono<Void>> delete(@PathVariable final String id) {
        return null;
    }

}
