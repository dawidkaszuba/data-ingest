package pl.dawidkaszuba.data_ingest.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.dawidkaszuba.data_ingest.config.MetricsConfig;
import pl.dawidkaszuba.data_ingest.service.PayloadNormalizer;

@Slf4j
@Service
public class JsonPayloadNormalizer implements PayloadNormalizer {

    private static final String SOURCE_PREFIX = "mqtt";
    private static final String VALUE = "value";

    private final ObjectMapper objectMapper;
    private final MetricsConfig metricsConfig;
    private final String mqttAddress;

    public JsonPayloadNormalizer(ObjectMapper objectMapper,
                                 MetricsConfig metricsConfig,
                                 @Value("${mqtt.broker-url}") String mqttAddress) {
        this.objectMapper = objectMapper;
        this.metricsConfig = metricsConfig;
        this.mqttAddress = mqttAddress;
    }

    @Override
    public ObjectNode normalize(String topic, String payload) {
        try {
            JsonNode rawData = objectMapper.readTree(payload);

            ObjectNode normalizedData = objectMapper.createObjectNode();
            normalizedData.put("device", topic.substring(topic.lastIndexOf('/') + 1));
            normalizedData.put("source", SOURCE_PREFIX + "@" + mqttAddress);
            normalizedData.put("received", java.time.OffsetDateTime.now().toString());
            normalizedData.put("schemaVersion", "1.0");

            ObjectNode metrics = objectMapper.createObjectNode();
            rawData.fieldNames().forEachRemaining(field -> {
                JsonNode value = rawData.get(field);
                ObjectNode metric = objectMapper.createObjectNode();
                if (value == null || value.isNull()) {
                    metric.putNull(VALUE);
                } else if (value.isNumber()) {
                    metric.put(VALUE, value.doubleValue());
                } else {
                    metric.put(VALUE, value.asText());
                }
                metric.put("unit", metricsConfig.getUnitForField(field));
                metrics.set(field, metric);
            });

            normalizedData.set("metrics", metrics);
            return normalizedData;
        } catch (Exception e) {
            log.error("Błąd parsowania JSON: {}", e.getMessage());
            return null;
        }
    }
}
