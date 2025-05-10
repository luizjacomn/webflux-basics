package com.luizjacomn.webfluxbasics.service;

import com.luizjacomn.webfluxbasics.service.exception.ObjectNotFoundException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StandardService<T, D> {

    Mono<T> save(final D data);

    Mono<T> findById(String id);

    Flux<T> findAll();

    Mono<T> update(String id, D data);

    Mono<T> delete(String id);

    default Mono<T> handleNotFound(String id, Class<T> clazz) {
        return Mono.error(new ObjectNotFoundException("%s not found: id = %s".formatted(clazz.getSimpleName(), id)));
    }

}
