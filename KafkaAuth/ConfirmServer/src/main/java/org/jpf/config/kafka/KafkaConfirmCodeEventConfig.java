package org.jpf.config.kafka;

import org.jpf.model.kafka.ConfirmCodeEvent;
import org.springframework.context.annotation.Configuration;

/**
 * Kafka configuration for RegistrationEvent.
 *
 * @see KafkaSimpleConfig
 * @see ConfirmCodeEvent
 */
@Configuration
public class KafkaConfirmCodeEventConfig
        extends KafkaSimpleConfig<ConfirmCodeEvent> {
}
