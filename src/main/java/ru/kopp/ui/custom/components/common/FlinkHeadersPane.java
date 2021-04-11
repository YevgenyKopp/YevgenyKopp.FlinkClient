package ru.kopp.ui.custom.components.common;

import net.miginfocom.swing.MigLayout;
import ru.kopp.ui.custom.graphics.BottomSeparatorBorder;
import ru.kopp.ui.custom.services.DesignGuide;

import javax.swing.*;

public abstract class FlinkHeadersPane extends JComponent {

    public FlinkHeadersPane() {
        //Appearance
        setLayout(new MigLayout());
        setBorder(new BottomSeparatorBorder(DesignGuide.gray()));
    }
}