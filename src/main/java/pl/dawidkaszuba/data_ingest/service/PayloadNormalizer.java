package pl.dawidkaszuba.data_ingest.service;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface PayloadNormalizer {
    ObjectNode normalize(String topic, String payload);
}
