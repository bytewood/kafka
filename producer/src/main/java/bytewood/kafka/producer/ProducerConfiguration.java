package bytewood.kafka.producer;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;

@Configuration
@RequiredArgsConstructor
public class ProducerConfiguration {

    private final KafkaSettings kafkaSettings;

    @Bean
    public ProducerFactory<String, Long> nubmerProducerFactory() {
        final HashMap<String, Object> o = new HashMap<>();
        o.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaSettings.getBootstrapAddress());
        o.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        o.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        return new DefaultKafkaProducerFactory<>(o);
    }

    @Bean
    public KafkaTemplate<String, Long> numbersKafkaTemplate() {
        return new KafkaTemplate<>(nubmerProducerFactory());
    }
}
