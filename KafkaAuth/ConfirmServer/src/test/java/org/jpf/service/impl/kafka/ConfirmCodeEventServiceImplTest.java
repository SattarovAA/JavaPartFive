package org.jpf.service.impl.kafka;

import org.jpf.model.kafka.ConfirmCodeEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.lang.reflect.Field;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("ConfirmCodeEventServiceImplTest tests")
class ConfirmCodeEventServiceImplTest {
    private ConfirmCodeEventServiceImpl eventService;
    @Mock
    private KafkaTemplate<String, ConfirmCodeEvent> kafkaUserTemplate;
    private final String defaultTopicName = "topic-name";

    @BeforeEach
    void setUp() {
        eventService = new ConfirmCodeEventServiceImpl(
                kafkaUserTemplate
        );
        try {
            Field topicName = ConfirmCodeEventServiceImpl.class
                    .getDeclaredField("topicName");
            topicName.setAccessible(true);
            topicName.set(eventService, defaultTopicName);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("send test: send correct ConfirmCodeEvent with kafkaTemplate.")
    void givenRegistrationRequestWhenSendThenKafkaTemplateSendEvent() {
        var request = new ConfirmCodeEvent(
                "string@mail.com", "encodedCode"
        );

        eventService.send(request);

        verify(kafkaUserTemplate, times(1))
                .send(defaultTopicName, request);
    }
}
