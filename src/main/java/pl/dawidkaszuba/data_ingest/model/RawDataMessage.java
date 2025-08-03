package pl.dawidkaszuba.data_ingest.model;

import java.time.OffsetDateTime;

public record RawDataMessage(String rawMessage, String source, OffsetDateTime datetime) {}
