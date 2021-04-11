package ru.kopp.client.model.jar;

import java.util.List;

@SuppressWarnings("unused")
public class FlinkUploadedJars {
    private String address;
    private List<FlinkJar> files;

    public List<FlinkJar> getJars() {
        return files;
    }
}