package org.jpf.service.impl.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jpf.model.dto.security.RegistrationRequest;
import org.jpf.model.kafka.RegistrationEvent;
import org.jpf.service.kafka.RegistrationEventService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for sending registration event.
 *
 * @see RegistrationRequest
 * @see RegistrationEvent
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class RegistrationEventServiceImpl
        implements RegistrationEventService {
    @Value("${app.kafka.topic.userInitRegistration}")
    private String topicName;
    private final KafkaTemplate<String, RegistrationEvent> kafkaUserTemplate;

    /**
     * Send information about creating a new {@link RegistrationRequest}.
     *
     * @param model {@link RegistrationRequest} for creation new event.
     */
    @Override
    public void send(RegistrationRequest model) {
        log.info("Try to send registration event for user with email {}", model.email());
        RegistrationEvent registrationEvent = new RegistrationEvent(model.email());
        kafkaUserTemplate.send(topicName, registrationEvent);
    }
}
