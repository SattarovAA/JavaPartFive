package org.jpf.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jpf.model.kafka.RegistrationEvent;
import org.jpf.service.ConfirmService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Kafka listener for {@link RegistrationEvent}.
 */
@Component
@ConditionalOnProperty(prefix = "app.kafka.listener",
        name = "registration-event",
        havingValue = "true")
@RequiredArgsConstructor
@Slf4j
public class KafkaRegistrationEventListener {
    private final ConfirmService confirmService;

    /**
     * Kafka listener for {@link RegistrationEvent}.<br>
     * Save entity with {@link ConfirmService}.
     *
     * @param message   {@link RegistrationEvent}.
     * @param key       UUID.
     * @param partition Integer.
     * @param timestamp Long.
     * @param topic     String.
     */
    @KafkaListener(topics = "${app.kafka.topic.userInitRegistration}",
            groupId = "${app.kafka.kafkaMessageGroupId}",
            containerFactory = "kafkaListenerContainerFactory")
    void orderListener(@Payload RegistrationEvent message,
                       @Header(value = KafkaHeaders.RECEIVED_KEY, required = false) UUID key,
                       @Header(value = KafkaHeaders.RECEIVED_PARTITION, required = false) Integer partition,
                       @Header(value = KafkaHeaders.RECEIVED_TIMESTAMP, required = false) Long timestamp,
                       @Header(value = KafkaHeaders.RECEIVED_TOPIC, required = false) String topic) {
        log.info("Topic: RegistrationEvent");
        log.info("Received message: {}", message);
        log.info("Key: {}, Partition: {}, Timestamp: {}, Topic: {}",
                key, partition, timestamp, topic);
        confirmService.work(message);
    }
}
