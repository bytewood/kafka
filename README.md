# Bytewood Kafka

Experiments with Kafka and Kafka Streams

## Install and run Kafka on mac

```
ROOT= ~
VERSION="5.1.2-2.11"
FILE="confluent-community-${VERSION}.tar.gz"

cd "${ROOT}"
curl -O "http://packages.confluent.io/archive/5.1/${FILE}"
tar -xzvf "${FILE}"
alias cf="${ROOT}/confluent-${VERSION}/bin/confluent"

cf start
cf status 
cf stop
```


### What does it do
Create configurable topics using KafkaAdminClient
A random number is produced and sent to kafka topic "current-value" on a configurable schedule


