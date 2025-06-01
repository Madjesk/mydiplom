package com.example.mailintegration.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.DefaultKafkaConsumerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {
    @Bean
    DefaultKafkaConsumerFactoryCustomizer concurrencyCustomizer() {
        return factory -> factory.getConfigurationProperties()
                .put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "100");
    }
}
