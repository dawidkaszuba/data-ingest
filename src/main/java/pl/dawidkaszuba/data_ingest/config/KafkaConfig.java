package pl.dawidkaszuba.data_ingest.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    private final String rawDataTopicName;
    private final int rawDataTopicPartitions;
    private final short rawDataTopicReplicas;


    public KafkaConfig(@Value("${data-ingest.kafka.raw-data-topic.name}") String rawDataTopicName,
                       @Value("${data-ingest.kafka.raw-data-topic.partitions}")  int rawDataTopicPartitions,
                       @Value("${data-ingest.kafka.raw-data-topic.replicas}")  short rawDataTopicReplicas) {
        this.rawDataTopicName = rawDataTopicName;
        this.rawDataTopicPartitions = rawDataTopicPartitions;
        this.rawDataTopicReplicas = rawDataTopicReplicas;
    }

    @Bean
    public NewTopic rawDataTopic() {
        return new NewTopic(rawDataTopicName, rawDataTopicPartitions, rawDataTopicReplicas);
    }
}
