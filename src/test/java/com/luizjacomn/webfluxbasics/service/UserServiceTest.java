package com.luizjacomn.webfluxbasics.service;

import com.github.javafaker.Faker;
import com.luizjacomn.webfluxbasics.entity.User;
import com.luizjacomn.webfluxbasics.mapper.UserMapper;
import com.luizjacomn.webfluxbasics.model.request.UserRequest;
import com.luizjacomn.webfluxbasics.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Objects;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private final Faker faker = new Faker();

    @Test
    void save() {
        // Arrange
        var userRequest = new UserRequest(faker.name().name(), faker.internet().emailAddress(), faker.internet().password());

        var user = new User();

        Mockito.when(userMapper.toEntity(ArgumentMatchers.any(UserRequest.class))).thenReturn(user);
        Mockito.when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(Mono.just(new User()));

        // Act
        var result = userService.save(userRequest);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(Objects::nonNull)
                .expectComplete()
                .verify();

        Mockito.verify(userRepository).save(ArgumentMatchers.any(User.class));
    }

    @Test
    void findById() {
        // Arrange
        var id = faker.idNumber().valid();

        var user = new User();
        user.setId(id);

        Mockito.when(userRepository.findById(ArgumentMatchers.anyString())).thenReturn(Mono.just(user));

        // Act
        var result = userService.findById(id);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(Objects::nonNull)
                .expectComplete()
                .verify();

        Mockito.verify(userRepository).findById(ArgumentMatchers.anyString());
    }

    @Test
    void findAll() {
        // Arrange
        Mockito.when(userRepository.findAll()).thenReturn(Flux.just(new User()));

        // Act
        var result = userService.findAll();

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(Objects::nonNull)
                .expectComplete()
                .verify();

        Mockito.verify(userRepository).findAll();
    }

    @Test
    void update() {
        // Arrange
        var id = faker.idNumber().valid();
        var email = faker.internet().emailAddress();

        var userRequest = new UserRequest(null, email, null);

        var user = new User();
        user.setId(id);
        user.setName(faker.name().name());
        user.setEmail(email);
        user.setPassword(faker.internet().password());

        Mockito.when(userMapper.toEntity(ArgumentMatchers.any(UserRequest.class), ArgumentMatchers.any(User.class))).thenReturn(user);
        Mockito.when(userRepository.findById(ArgumentMatchers.anyString())).thenReturn(Mono.just(user));
        Mockito.when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(Mono.just(user));

        // Act
        var result = userService.update(id, userRequest);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response -> response.getEmail().equals(userRequest.email()))
                .expectComplete()
                .verify();

        Mockito.verify(userRepository).save(ArgumentMatchers.any(User.class));
    }

    @Test
    void delete() {
        // Arrange
        var id = faker.idNumber().valid();

        var user = new User();
        user.setId(id);

        Mockito.when(userRepository.findAndRemove(ArgumentMatchers.anyString())).thenReturn(Mono.just(user));

        // Act
        var result = userService.delete(id);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response -> response.getId().equals(id))
                .expectComplete()
                .verify();

        Mockito.verify(userRepository).findAndRemove(ArgumentMatchers.anyString());
    }

}