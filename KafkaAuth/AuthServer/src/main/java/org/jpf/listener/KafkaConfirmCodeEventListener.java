package org.jpf.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jpf.model.kafka.ConfirmCodeEvent;
import org.jpf.service.UserService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Kafka listener for {@link ConfirmCodeEvent}.
 */
@Component
@ConditionalOnProperty(prefix = "app.kafka.listener",
        name = "confirm-code-event",
        havingValue = "true")
@RequiredArgsConstructor
@Slf4j
public class KafkaConfirmCodeEventListener {
    private final UserService userService;

    /**
     * Kafka listener for {@link ConfirmCodeEvent}.<br>
     * Save entity with {@link UserService}.
     *
     * @param message   {@link ConfirmCodeEvent}.
     * @param key       UUID.
     * @param partition Integer.
     * @param timestamp Long.
     * @param topic     String.
     */
    @KafkaListener(topics = "${app.kafka.topic.userConfirmRegistration}",
            groupId = "${app.kafka.kafkaMessageGroupId}",
            containerFactory = "kafkaListenerContainerFactory")
    void orderListener(@Payload ConfirmCodeEvent message,
                       @Header(value = KafkaHeaders.RECEIVED_KEY, required = false) UUID key,
                       @Header(value = KafkaHeaders.RECEIVED_PARTITION, required = false) Integer partition,
                       @Header(value = KafkaHeaders.RECEIVED_TIMESTAMP, required = false) Long timestamp,
                       @Header(value = KafkaHeaders.RECEIVED_TOPIC, required = false) String topic) {
        log.info("Topic: ConfirmCodeEvent");
        log.info("Received message: {}", message);
        log.info("Key: {}, Partition: {}, Timestamp: {}, Topic: {}",
                key, partition, timestamp, topic);
        userService.save(message);
    }
}