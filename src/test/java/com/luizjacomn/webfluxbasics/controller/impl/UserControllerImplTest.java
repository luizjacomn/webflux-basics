package com.luizjacomn.webfluxbasics.controller.impl;

import com.github.javafaker.Faker;
import com.luizjacomn.webfluxbasics.entity.User;
import com.luizjacomn.webfluxbasics.mapper.UserMapper;
import com.luizjacomn.webfluxbasics.model.request.UserRequest;
import com.luizjacomn.webfluxbasics.model.response.UserResponse;
import com.luizjacomn.webfluxbasics.repository.UserRepository;
import com.luizjacomn.webfluxbasics.service.UserService;
import org.springframework.dao.DuplicateKeyException;
import com.mongodb.reactivestreams.client.MongoClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoReactiveDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoReactiveRepositoriesAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@EnableAutoConfiguration(exclude = { MongoReactiveDataAutoConfiguration.class, MongoReactiveRepositoriesAutoConfiguration.class })
@AutoConfigureWebTestClient
class UserControllerImplTest {

    private static final String BASE_URI = "/users";

    @Autowired
    private WebTestClient webTestClient;

    @InjectMocks
    private UserService service;

    @MockBean
    private UserRepository repository;

    @MockBean
    private UserMapper mapper;

    @MockBean
    private MongoClient mongoClient;

    @MockBean
    private ReactiveMongoTemplate reactiveMongoTemplate;

    private final Faker faker = new Faker();

    @Test
    @DisplayName("Test save a user")
    void testSaveWithSuccess() {
        // Arrange
        final var userRequest = new UserRequest(faker.name().name(), faker.internet().emailAddress(), faker.internet().password());
        final var expectedUserResponse = new UserResponse(faker.idNumber().valid(), faker.name().name(), faker.internet().emailAddress());

        Mockito.when(mapper.toEntity(ArgumentMatchers.any(UserRequest.class))).thenReturn(new User());
        Mockito.when(repository.save(ArgumentMatchers.any(User.class))).thenReturn(Mono.just(new User()));
        Mockito.when(mapper.toResponse(ArgumentMatchers.any(User.class))).thenReturn(expectedUserResponse);

        // Act
        webTestClient.post()
                     .uri(BASE_URI)
                     .contentType(MediaType.APPLICATION_JSON)
                     .body(BodyInserters.fromValue(userRequest))
                     .exchange()
        // Assert
                     .expectStatus().isCreated();

        Mockito.verify(repository).save(ArgumentMatchers.any(User.class));
        Mockito.verify(mapper).toResponse(ArgumentMatchers.any(User.class));
    }

    @Test
    @DisplayName("Test endpoint save with bad request on validation")
    void testSaveWithBadRequestOnValidation() {
        // Arrange
        final var request = new UserRequest(faker.name().name().concat(" "), faker.internet().emailAddress(), faker.internet().password());

        // Act
        webTestClient.post()
                     .uri(BASE_URI)
                     .contentType(MediaType.APPLICATION_JSON)
                     .body(BodyInserters.fromValue(request))
                     .exchange()
        // Assert
                     .expectStatus().isBadRequest()
                     .expectBody()
                     .jsonPath("$.path").isEqualTo(BASE_URI)
                     .jsonPath("$.status").isEqualTo(HttpStatus.BAD_REQUEST.value())
                     .jsonPath("$.error").isEqualTo("Validation error")
                     .jsonPath("$.message").isEqualTo("Error on validation attributes")
                     .jsonPath("$.fieldErrors[0].fieldName").isEqualTo("name")
                     .jsonPath("$.fieldErrors[0].message").isEqualTo("must not contain leading or trailing whitespace");
    }

    @Test
    @DisplayName("Test endpoint save with bad request on duplicated email")
    void testSaveWithBadRequestOnDuplicatedEmail() {
        // Arrange
        final var request = new UserRequest(faker.name().name(), faker.internet().emailAddress(), faker.internet().password());
        Mockito.when(mapper.toEntity(ArgumentMatchers.any(UserRequest.class))).thenReturn(new User());
        Mockito.when(repository.save(ArgumentMatchers.any(User.class))).thenThrow(new DuplicateKeyException("Mocking exception with message: email dup key"));

        // Act
        webTestClient.post()
                     .uri(BASE_URI)
                     .contentType(MediaType.APPLICATION_JSON)
                     .body(BodyInserters.fromValue(request))
                     .exchange()
        // Assert
                     .expectStatus().isBadRequest()
                     .expectBody()
                     .jsonPath("$.path").isEqualTo(BASE_URI)
                     .jsonPath("$.status").isEqualTo(HttpStatus.BAD_REQUEST.value())
                     .jsonPath("$.error").isEqualTo(HttpStatus.BAD_REQUEST.getReasonPhrase())
                     .jsonPath("$.message").isEqualTo("E-mail already registered");
    }

    @Test
    @DisplayName("Test endpoint save with bad request on duplicated key (default handling)")
    void testSaveWithBadRequestOnDuplicatedKeyDefaultHandling() {
        // Arrange
        final var request = new UserRequest(faker.name().name(), faker.internet().emailAddress(), faker.internet().password());
        Mockito.when(mapper.toEntity(ArgumentMatchers.any(UserRequest.class))).thenReturn(new User());
        Mockito.when(repository.save(ArgumentMatchers.any(User.class))).thenThrow(new DuplicateKeyException("Mocking exception with message: another key dup key"));

        // Act
        webTestClient.post()
                     .uri(BASE_URI)
                     .contentType(MediaType.APPLICATION_JSON)
                     .body(BodyInserters.fromValue(request))
                     .exchange()
        // Assert
                     .expectStatus().isBadRequest()
                     .expectBody()
                     .jsonPath("$.path").isEqualTo(BASE_URI)
                     .jsonPath("$.status").isEqualTo(HttpStatus.BAD_REQUEST.value())
                     .jsonPath("$.error").isEqualTo(HttpStatus.BAD_REQUEST.getReasonPhrase())
                     .jsonPath("$.message").isEqualTo("Dup key exception");
    }

    @Test
    @DisplayName("Test find by id endpoint with success")
    void testFindByIdWithSuccess() {
        // Arrange
        final var id = faker.idNumber().valid();
        final var name = faker.name().name();
        final var email = faker.internet().emailAddress();

        final var userResponse = new UserResponse(id, name, email);

        Mockito.when(repository.findById(ArgumentMatchers.anyString())).thenReturn(Mono.just(new User()));
        Mockito.when(mapper.toResponse(ArgumentMatchers.any(User.class))).thenReturn(userResponse);

        // Act
        webTestClient.get()
                     .uri(BASE_URI.concat("/%s".formatted(id)))
                     .accept(MediaType.APPLICATION_JSON)
                     .exchange()
        // Assert
                     .expectStatus().isOk()
                     .expectBody()
                     .jsonPath("$.id").isEqualTo(id)
                     .jsonPath("$.name").isEqualTo(name)
                     .jsonPath("$.email").isEqualTo(email);

        Mockito.verify(repository).findById(ArgumentMatchers.anyString());
        Mockito.verify(mapper).toResponse(ArgumentMatchers.any(User.class));
    }

    @Test
    @DisplayName("Test find by id endpoint with not found")
    void testFindByIdWithNotFound() {
        // Arrange
        final var id = faker.idNumber().valid();

        Mockito.when(repository.findById(ArgumentMatchers.anyString())).thenReturn(Mono.empty());

        // Act
        webTestClient.get()
                .uri(BASE_URI.concat("/%s".formatted(id)))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                // Assert
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.path").isEqualTo(BASE_URI.concat("/%s".formatted(id)))
                .jsonPath("$.status").isEqualTo(HttpStatus.NOT_FOUND.value())
                .jsonPath("$.error").isEqualTo(HttpStatus.NOT_FOUND.getReasonPhrase())
                .jsonPath("$.message").isEqualTo("User not found: id = %s".formatted(id));
    }

    @Test
    @DisplayName("Test find all endpoint with success")
    void testFindAllWithSuccess() {
        // Arrange
        final var id = faker.idNumber().valid();
        final var name = faker.name().name();
        final var email = faker.internet().emailAddress();

        final var userResponse = new UserResponse(id, name, email);

        Mockito.when(repository.findAll()).thenReturn(Flux.just(new User()));
        Mockito.when(mapper.toResponse(ArgumentMatchers.any(User.class))).thenReturn(userResponse);

        // Act
        webTestClient.get()
                     .uri(BASE_URI)
                     .accept(MediaType.APPLICATION_JSON)
                     .exchange()
        // Assert
                     .expectStatus().isOk()
                     .expectBody()
                     .jsonPath("$.[0].id").isEqualTo(id)
                     .jsonPath("$.[0].name").isEqualTo(name)
                     .jsonPath("$.[0].email").isEqualTo(email);

        Mockito.verify(repository).findAll();
        Mockito.verify(mapper).toResponse(ArgumentMatchers.any(User.class));
    }

    @Test
    @DisplayName("Test update endpoint with success")
    void testUpdateWithSuccess() {
        // Arrange
        final var id = faker.idNumber().valid();
        final var name = faker.name().name();
        final var email = faker.internet().emailAddress();
        final var password = faker.internet().password();

        final var request = new UserRequest(name, email, password);
        final var userResponse = new UserResponse(id, name, email);

        Mockito.when(mapper.toResponse(ArgumentMatchers.any(User.class))).thenReturn(userResponse);
        Mockito.when(mapper.toEntity(ArgumentMatchers.any(UserRequest.class), ArgumentMatchers.any(User.class))).thenReturn(new User());
        Mockito.when(repository.findById(ArgumentMatchers.anyString())).thenReturn(Mono.just(new User()));
        Mockito.when(repository.save(ArgumentMatchers.any(User.class))).thenReturn(Mono.just(new User()));

        // Act
        webTestClient.patch()
                     .uri(BASE_URI.concat("/%s".formatted(id)))
                     .contentType(MediaType.APPLICATION_JSON)
                     .body(BodyInserters.fromValue(request))
                     .exchange()
        // Assert
                     .expectStatus().isOk()
                     .expectBody()
                     .jsonPath("$.id").isEqualTo(id)
                     .jsonPath("$.name").isEqualTo(name)
                     .jsonPath("$.email").isEqualTo(email);

        Mockito.verify(mapper).toResponse(ArgumentMatchers.any(User.class));
        Mockito.verify(mapper).toEntity(ArgumentMatchers.any(UserRequest.class), ArgumentMatchers.any(User.class));
        Mockito.verify(repository).findById(ArgumentMatchers.anyString());
        Mockito.verify(repository).save(ArgumentMatchers.any(User.class));
    }

    @Test
    @DisplayName("Test delete endpoint with success")
    void testDeleteWithSuccess() {
        // Arrange
        Mockito.when(repository.findAndRemove(ArgumentMatchers.anyString())).thenReturn(Mono.just(new User()));

        // Act
        webTestClient.delete()
                     .uri(BASE_URI.concat("/%s".formatted(faker.idNumber().valid())))
                     .exchange()
        // Assert
                     .expectStatus().isNoContent();

        Mockito.verify(repository).findAndRemove(ArgumentMatchers.anyString());
    }

}
