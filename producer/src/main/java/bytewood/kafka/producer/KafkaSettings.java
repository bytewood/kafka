package bytewood.kafka.producer;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collection;

@Data
@ConfigurationProperties(prefix = "kafka")
public class KafkaSettings {
    private String bootstrapAddress;
    private String bootstrapTopic;
    private Collection<String> topics;
}
