package org.jpf.service.impl.kafka;

import org.jpf.model.dto.security.RegistrationRequest;
import org.jpf.model.kafka.RegistrationEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.lang.reflect.Field;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("RegistrationEventServiceImplTest test")
class RegistrationEventServiceImplTest {
    @InjectMocks
    private RegistrationEventServiceImpl registrationEventService;
    @Mock
    private KafkaTemplate<String, RegistrationEvent> kafkaUserTemplate;
    private static final String defaultTopicName = "topic-name";

    @BeforeEach
    void setUp() {
        registrationEventService = new RegistrationEventServiceImpl(
                kafkaUserTemplate
        );
        try {
            Field topicName = RegistrationEventServiceImpl.class
                    .getDeclaredField("topicName");
            topicName.setAccessible(true);
            topicName.set(registrationEventService, defaultTopicName);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("send test: send correct RegistrationEvent with kafkaTemplate.")
    void givenRegistrationRequestWhenSendThenKafkaTemplateSendEvent() {
        String email = "string@mail.com";
        var request = new RegistrationRequest(email);
        var expected = new RegistrationEvent(email);

        registrationEventService.send(request);

        verify(kafkaUserTemplate, times(1))
                .send(defaultTopicName, expected);
    }
}
