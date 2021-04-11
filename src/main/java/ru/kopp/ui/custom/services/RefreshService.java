package ru.kopp.ui.custom.services;

import ru.kopp.ui.frames.MainWindow;

import javax.swing.*;


public class RefreshService {

    private final Timer timer = new Timer(5000, e -> MainWindow.getMainWindow().updateAll());

    public RefreshService() {
        timer.start();
    }

    public void setTime(int index) {
        switch (FlinkDelay.values()[index]) {
            case FIVE_SECONDS:
                timer.stop();
                timer.setDelay(5000);
                timer.start();
                break;

            case THIRTY_SECONDS:
                timer.stop();
                timer.setDelay(30000);
                timer.start();
                break;

            case SIXTY_SECONDS:
                timer.stop();
                timer.setDelay(60000);
                timer.start();
                break;

            default:
                timer.stop();
        }
    }

    enum FlinkDelay {
        MANUAL,
        FIVE_SECONDS,
        THIRTY_SECONDS,
        SIXTY_SECONDS
    }
}