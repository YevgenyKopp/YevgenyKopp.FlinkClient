package ru.kopp.client.model.jar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public class FlinkJar {
    private String id;
    private String name;
    private long uploaded;
    private List<FlinkJarEntry> entry;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUploaded() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd, HH:mm:ss");
        return dateFormat.format(new Date(uploaded));
    }

    public List<FlinkJarEntry> getEntry() {
        return entry;
    }

    public void setEntry(List<FlinkJarEntry> entry) {
        this.entry = entry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FlinkJar)) return false;
        FlinkJar flinkJar = (FlinkJar) o;
        return Objects.equals(id, flinkJar.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, uploaded, entry);
    }
}