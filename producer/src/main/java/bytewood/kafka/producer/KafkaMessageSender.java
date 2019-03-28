package bytewood.kafka.producer;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import static org.apache.kafka.common.requests.DeleteAclsResponse.log;

@Service
@RequiredArgsConstructor
public class KafkaMessageSender {

    private final NumberProducer numberProducer;
    private final KafkaSettings kafkaSettings;
    private final KafkaTemplate<String, Long> numbersKafkaTemplate;

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
