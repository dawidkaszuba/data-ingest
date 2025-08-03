package pl.dawidkaszuba.data_ingest.service.impl;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.dawidkaszuba.data_ingest.service.NotificationService;
import pl.dawidkaszuba.data_ingest.service.PayloadNormalizer;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Slf4j
@Service
public class MqttSubscriberService implements pl.dawidkaszuba.data_ingest.service.MqttSubscriberService {

    private final MqttClient mqttClient;
    private final NotificationService notificationService;
    private final String[] topics;
    private final PayloadNormalizer payloadNormalizer;

    public MqttSubscriberService(MqttClient mqttClient,
                                 NotificationService notificationService,
                                 @Value("${mqtt.topics}") String[] topics,
                                 PayloadNormalizer payloadNormalizer) {
        this.mqttClient = mqttClient;
        this.notificationService = notificationService;
        this.topics = topics;
        this.payloadNormalizer = payloadNormalizer;
        subscribe();
    }

    @Override
    public void subscribe() {
        Arrays.stream(topics).forEach(topic -> {
            try {
                mqttClient.subscribe(topic, (t, message) -> {
                    String payload = new String(message.getPayload(), StandardCharsets.UTF_8);
                    ObjectNode normalizedData = payloadNormalizer.normalize(t, payload);
                    if (normalizedData != null) {
                        notificationService.sendRawData(normalizedData);
                    }
                });
            } catch (MqttException e) {
                log.error("Błąd mqtt: {}", e.getMessage());
            }
        });
    }

}
