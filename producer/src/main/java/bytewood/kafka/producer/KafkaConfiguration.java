package bytewood.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.kafka.core.KafkaAdmin;

import javax.annotation.PostConstruct;
import java.util.HashMap;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class KafkaConfiguration {

    private final KafkaSettings kafkaSettings;

    private final GenericApplicationContext applicationContext;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        final HashMap<String, Object> cfg = new HashMap<>();
        cfg.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaSettings.getBootstrapAddress());
        return new KafkaAdmin(cfg);
    }

    @PostConstruct
    public void addTopics() {
        kafkaSettings.getTopics().forEach(t -> {
            this.applicationContext.registerBean(t + "-kafka-topic", NewTopic.class, () -> new NewTopic(t, 1, (short) 1));
        });
    }
}
