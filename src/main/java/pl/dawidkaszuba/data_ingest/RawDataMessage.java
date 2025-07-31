package pl.dawidkaszuba.data_ingest;

import java.time.OffsetDateTime;

public record RawDataMessage(String rawMessage, String source, OffsetDateTime datetime) {}
