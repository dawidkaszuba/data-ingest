package pl.dawidkaszuba.data_ingest.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import pl.dawidkaszuba.data_ingest.exception.SerializationException;
import pl.dawidkaszuba.data_ingest.service.NotificationService;


@Slf4j
@Service
public class KafkaNotificationService implements NotificationService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String rawDataTopicName;
    private final ObjectMapper objectMapper;

    public KafkaNotificationService(KafkaTemplate<String, String> kafkaTemplate,
                                    @Value("${data-ingest.kafka.raw-data-topic.name}") String rawDataTopicName,
                                    ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.rawDataTopicName = rawDataTopicName;
        this.objectMapper = objectMapper;
    }

    @Override
    public void sendRawData(JsonNode normalizedData) {
        try {
            String json = objectMapper.writeValueAsString(normalizedData);
            kafkaTemplate.send(rawDataTopicName, json);
        } catch (JsonProcessingException e) {
            throw new SerializationException("Error serialization of JSON message has occurred ", e);
        }
    }

}
