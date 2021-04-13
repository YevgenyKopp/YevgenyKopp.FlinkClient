package ru.kopp.client.model.responses;

import com.squareup.moshi.Json;

@SuppressWarnings("unused")
public class FlinkConfig {

    @Json(name = "flink-version")
    private String flinkVersion;

    public String getFlinkVersion() {
        return flinkVersion;
    }

    public void setFlinkVersion(String flinkVersion) {
        this.flinkVersion = flinkVersion;
    }
}
