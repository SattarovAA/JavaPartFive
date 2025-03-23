package org.jpf.service.impl.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jpf.model.kafka.ConfirmCodeEvent;
import org.jpf.service.kafka.ConfirmCodeEventService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Service for sending confirmCode event.
 *
 * @see ConfirmCodeEvent
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class ConfirmCodeEventServiceImpl
        implements ConfirmCodeEventService {
    @Value("${app.kafka.topic.userConfirmRegistration}")
    private String topicName;
    private final KafkaTemplate<String, ConfirmCodeEvent> kafkaUserTemplate;

    @Override
    public void send(ConfirmCodeEvent event) {
        log.info("Try to send confirm code event for user with email {}", event.email());
        kafkaUserTemplate.send(topicName, event);
    }
}