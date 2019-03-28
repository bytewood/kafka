package bytewood.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
@SpringBootApplication
@EnableScheduling()
@EnableConfigurationProperties({
        KafkaSettings.class})
@RequiredArgsConstructor
public class TopicCreatorApplication {

    private final KafkaSettings kafkaSettings;
    private final NumberProducer numberProducer;
    private final KafkaTemplate<String, Long> numbersKafkaTemplate;

    public static void main(String[] args) {
        SpringApplication.run(TopicCreatorApplication.class, args);
    }

    @Scheduled(cron = "${schedule.numbers.cron}")
    public void run() {
        final Long number = numberProducer.materialize();

        final ListenableFuture<SendResult<String, Long>> o = numbersKafkaTemplate.send(kafkaSettings.getBootstrapTopic(), number);

        o.addCallback(new NumbersFutureCallback(number));
    }

    @RequiredArgsConstructor
    private static class NumbersFutureCallback implements ListenableFutureCallback<SendResult<String, Long>> {

        private final Long number;

        @Override
        public void onFailure(final Throwable throwable) {
            log.error("Failed to send [{}], due to {}", number, throwable.getCause().getMessage());
        }

        @Override
        public void onSuccess(final SendResult<String, Long> stringLongSendResult) {
            final ProducerRecord<String, Long> o = stringLongSendResult.getProducerRecord();
            log.debug("Sent [{}] to kafka topic '{}'", o.value(), o.topic());
        }
    }
}
