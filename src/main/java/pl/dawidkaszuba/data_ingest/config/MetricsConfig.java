package pl.dawidkaszuba.data_ingest.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "metrics")
@PropertySource(value = "classpath:metrics-units.yml", factory = YamlPropertySourceFactory.class)
public class MetricsConfig {

    private Map<String, String> units = new HashMap<>();

    public Map<String, String> getUnits() {
        return units;
    }

    public void setUnits(Map<String, String> units) {
        this.units = units;
    }

    public String getUnitForField(String field) {
        return units.getOrDefault(field, "");
    }
}
