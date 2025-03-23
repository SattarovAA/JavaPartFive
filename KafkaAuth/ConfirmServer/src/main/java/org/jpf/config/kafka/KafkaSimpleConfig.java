package org.jpf.config.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Default Kafka configuration.
 *
 * @param <T> dto entity for Kafka messages.
 */
public class KafkaSimpleConfig<T> {
    /**
     * Bootstrap kafka servers from configuration file.
     *
     * @value config.application.yml : spring.kafka.bootstrap-servers.
     * @see #producerFactory(ObjectMapper)
     * @see #consumerFactory(ObjectMapper)
     */
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;
    /**
     * Kafka Message Group id from configuration file.
     *
     * @value config.application.yml : app.kafka.kafkaMessageGroupId.
     * @see #consumerFactory(ObjectMapper)
     */
    @Value("${app.kafka.kafkaMessageGroupId}")
    private String kafkaMessageGroupId;

    /**
     * Bean {@link ProducerFactory} for entity {@link T}.
     * <br>
     * Uses {@link DefaultKafkaProducerFactory#DefaultKafkaProducerFactory(Map, Serializer, Serializer)}.
     *
     * @param objectMapper default {@link ObjectMapper} for {@link JsonSerializer}.
     * @return {@link ProducerFactory} with updated configuration for entity {@link T}.
     * @see #kafkaUserTemplate(ProducerFactory)
     * @see #bootstrapServers
     * @see StringSerializer
     * @see JsonSerializer
     */
    @Bean
    public ProducerFactory<String, T> producerFactory(
            ObjectMapper objectMapper) {
        Map<String, Object> config = new HashMap<>();

        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(config,
                new StringSerializer(),
                new JsonSerializer<>(objectMapper)
        );
    }

    /**
     * Bean {@link KafkaTemplate} for entity {@link T}.
     *
     * @param producerFactory {@link ProducerFactory} with updated configuration for entity {@link T}.
     *                        {@link #producerFactory(ObjectMapper)}.
     * @return {@link KafkaTemplate} with updated configuration for entity {@link T}.
     */
    @Bean
    public KafkaTemplate<String, T> kafkaUserTemplate(
            ProducerFactory<String, T> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    /**
     * Bean {@link ConsumerFactory} for entity {@link T}.
     * <br>
     * Uses {@link DefaultKafkaConsumerFactory#DefaultKafkaConsumerFactory(Map, Deserializer, Deserializer)}.
     *
     * @param objectMapper default {@link ObjectMapper} for {@link JsonDeserializer}.
     * @return {@link ConsumerFactory} updated for entity {@link T}.
     * @see #bootstrapServers
     * @see #kafkaMessageGroupId
     * @see JsonDeserializer
     * @see StringDeserializer
     */
    @Bean
    public ConsumerFactory<String, T> consumerFactory(
            ObjectMapper objectMapper) {
        Map<String, Object> config = new HashMap<>();

        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaMessageGroupId);
        config.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        return new DefaultKafkaConsumerFactory<>(
                config,
                new StringDeserializer(),
                new JsonDeserializer<>(objectMapper)
        );
    }

    /**
     * Bean {@link ConcurrentKafkaListenerContainerFactory} for entity {@link T}.
     *
     * @param consumerFactory {@link ConsumerFactory} with updated configuration for entity {@link T}.
     * @return {@link ConcurrentKafkaListenerContainerFactory} with updated configuration for entity {@link T}.
     * @see #consumerFactory(ObjectMapper)
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, T> kafkaListenerContainerFactory(
            ConsumerFactory<String, T> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, T> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);

        return factory;
    }
}
