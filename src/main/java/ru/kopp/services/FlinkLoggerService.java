package ru.kopp.services;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class FlinkLoggerService {
    public static Logger getLogger() {
        Logger logger = Logger.getLogger("flinkMessages.txt");
        if(logger.getHandlers().length==0){
            try {
                FileHandler fileHandler = new FileHandler(System.getProperty("user.dir") + "\\flinkMessages.txt");

                logger.addHandler(fileHandler);
                SimpleFormatter formatter = new SimpleFormatter();

                fileHandler.setFormatter(formatter);
            } catch (IOException e) {
                System.out.println("Unable to create messages.log:\n" + e.getMessage());
                logger = null;
            }
        }
        return logger;
    }
}