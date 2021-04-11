package ru.kopp.client.model.jar;

import java.util.Objects;

@SuppressWarnings("unused")
public class FlinkJarEntry {
    private String name;
    private String description;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FlinkJarEntry)) return false;
        FlinkJarEntry that = (FlinkJarEntry) o;
        return Objects.equals(name, that.name) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description);
    }
}