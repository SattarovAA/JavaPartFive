package org.jpf.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jpf.model.kafka.ConfirmCodeEvent;
import org.jpf.model.kafka.RegistrationEvent;
import org.jpf.service.ConfirmService;
import org.jpf.service.kafka.ConfirmCodeEventService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class ConfirmServiceImpl implements ConfirmService {
    private final PasswordEncoder passwordEncoder;
    private final ConfirmCodeEventService confirmCodeEventService;

    @Override
    public void work(RegistrationEvent message) {
        printConfirmCode(message.email());
    }

    private void printConfirmCode(String email) {
        String confirmCode = UUID.randomUUID().toString();

        log.info("Confirm code from Confirm service: {}", confirmCode);

        ConfirmCodeEvent event = new ConfirmCodeEvent(
                email,
                passwordEncoder.encode(confirmCode)
        );
        confirmCodeEventService.send(event);
    }
}
