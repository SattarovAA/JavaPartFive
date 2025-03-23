package org.jpf.config.kafka;

import org.jpf.model.kafka.RegistrationEvent;
import org.springframework.context.annotation.Configuration;

/**
 * Kafka configuration for RegistrationEvent.
 *
 * @see KafkaSimpleConfig
 * @see RegistrationEvent
 */
@Configuration
public class KafkaRegistrationEventConfig
        extends KafkaSimpleConfig<RegistrationEvent> {
}
