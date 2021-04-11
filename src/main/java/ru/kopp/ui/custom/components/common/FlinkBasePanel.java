package ru.kopp.ui.custom.components.common;

import net.miginfocom.swing.MigLayout;
import ru.kopp.ui.custom.services.DesignGuide;

import javax.swing.*;
import java.awt.*;

public class FlinkBasePanel extends JPanel {
    public FlinkBasePanel() {
        setLayout(new MigLayout());
        setBackground(DesignGuide.white());
    }

    public FlinkBasePanel(LayoutManager layout) {
        super(layout);
        setBackground(DesignGuide.white());
    }
}