package org.jpf.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jpf.exception.EntityNotFoundException;
import org.jpf.model.entity.User;
import org.jpf.model.kafka.ConfirmCodeEvent;
import org.jpf.service.UserService;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final Map<String, User> repository;

    @Override
    public User findByEmail(String email) {
        User result = repository.get(email);
        if (result == null) {
            throw new EntityNotFoundException(MessageFormat.format(
                    "User with email {0} not found!",
                    email
            ));
        }
        return repository.get(email);
    }

    @Override
    public User save(ConfirmCodeEvent message) {
        User user = User.builder()
                .email(message.email())
                .password(message.confirmCode())
                .build();
        log.info("new User: {}", user);
        if (repository.containsKey(message.email())) {
            log.warn("User with email {} update value.", message.email());
        }
        repository.put(message.email(), user);
        return user;
    }
}
