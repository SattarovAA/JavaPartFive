package org.jpf.service.impl;

import org.jpf.exception.EntityNotFoundException;
import org.jpf.model.entity.User;
import org.jpf.model.kafka.ConfirmCodeEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("UserServiceImplTest tests")
class UserServiceImplTest {
    private UserServiceImpl userService;
    private Map<String, User> repository = new HashMap<>();

    @BeforeEach
    void setUp() {
        repository = new HashMap<>();
        userService = new UserServiceImpl(repository);
    }

    @Test
    @DisplayName("findByEmail test: find user by exists email.")
    void givenCorrectEmailWhenFindByEmailThenUser() {
        String email = "string@mail.com";
        User expected = new User("pass", email);
        repository.put(email, expected);

        User actual = userService.findByEmail(email);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("findByEmail test: find user by not exists email.")
    void givenIncorrectEmailWhenFindByEmailThenUser() {
        String email = "string@mail.com";

        assertThrows(EntityNotFoundException.class,
                () -> userService.findByEmail(email));
    }

    @Test
    @DisplayName("save test: save user to repository.")
    void givenConfirmCodeEventWhenSaveThenUser() {
        String userEmail = "string@mail.com";
        String pass = "pass";
        ConfirmCodeEvent event = new ConfirmCodeEvent(
                userEmail, pass
        );
        User actual = userService.save(event);

        assertEquals(userEmail, actual.getEmail());
        assertEquals(pass, actual.getPassword());
        assertTrue(repository.containsKey(actual.getEmail()));
    }
}
