package ru.kopp;

import com.formdev.flatlaf.intellijthemes.FlatCyanLightIJTheme;
import ru.kopp.ui.frames.ConnectionDialog;

public class Main {
    public static void main(String[] args) {
        FlatCyanLightIJTheme.install();
        new ConnectionDialog();
    }
}
