package org.jpf.service.impl;

import org.jpf.model.kafka.ConfirmCodeEvent;
import org.jpf.model.kafka.RegistrationEvent;
import org.jpf.service.kafka.ConfirmCodeEventService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ConfirmServiceImplTest tests")
class ConfirmServiceImplTest {
    @InjectMocks
    ConfirmServiceImpl confirmService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private ConfirmCodeEventService confirmCodeEventService;

    @Test
    @DisplayName("work test: send ConfirmCodeEvent when RegistrationEvent.")
    void givenRegistrationEventWhenWorkThenSendEvent() {
        String email = "string@mail.com";
        String encodedCode = "encodedCode";
        RegistrationEvent event = new RegistrationEvent(email);
        ConfirmCodeEvent confirmCodeEvent = new ConfirmCodeEvent(
                email, encodedCode
        );

        when(passwordEncoder.encode(anyString()))
                .thenReturn(encodedCode);

        confirmService.work(event);

        verify(confirmCodeEventService, times(1))
                .send(confirmCodeEvent);
    }
}
