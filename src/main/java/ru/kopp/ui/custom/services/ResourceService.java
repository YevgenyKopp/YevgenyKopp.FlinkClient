package ru.kopp.ui.custom.services;

import ru.kopp.services.FlinkLoggerService;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ResourceService {
    public static Image getImageIcon(String name) {
        try {
            return new ImageIcon(ImageIO.read(ClassLoader.getSystemResource(name))).getImage();
        } catch (IOException e) {
            FlinkLoggerService.getLogger().info("Failed to load icon " + name);
            return null;
        }
    }
}