package bytewood.kafka.producer;

import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

@Service
public class DefaultNumberProducer implements NumberProducer {

    private Random random = new Random(UUID.randomUUID().getLeastSignificantBits());
    @Override
    public Long materialize() {
        final long o = random.longs(0, 100).findFirst().getAsLong();
        if (o == 42L) {
            throw new IllegalStateException("Produced number cannot be the meaning of life");
        }
        return o;
    }
}
