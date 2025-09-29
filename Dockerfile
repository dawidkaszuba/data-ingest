FROM eclipse-temurin:21-jdk

RUN useradd -r -u 1001 -m -d /home/appuser appuser \
    && mkdir -p /opt/data-ingest \
    && chown -R appuser:appuser /opt/data-ingest

COPY --chown=appuser:appuser ./target/*.jar /opt/data-ingest/data-ingest.jar

USER appuser
EXPOSE 8080
WORKDIR /opt/data-ingest
ENTRYPOINT ["java", "-jar", "/opt/data-ingest/data-ingest.jar"]
