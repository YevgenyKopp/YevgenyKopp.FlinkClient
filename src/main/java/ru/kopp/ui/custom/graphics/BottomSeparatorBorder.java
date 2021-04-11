package ru.kopp.ui.custom.graphics;

import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.geom.Line2D;

public class BottomSeparatorBorder extends AbstractBorder {
    private final Insets insets=new Insets(0,0,0,0);
    private final Color color;

    public BottomSeparatorBorder(Color color) {
        this.color=color;
    }

    @Override
    public Insets getBorderInsets(Component component) {
        return insets;
    }

    @Override
    public Insets getBorderInsets(Component component, Insets insets) {
        return getBorderInsets(component);
    }

    @Override
    public void paintBorder(
            Component component,
            Graphics graphics,
            int x, int y,
            int width, int height) {

        Graphics2D graphics2D = (Graphics2D) graphics;

        int strokePad = 0;
        Line2D line=new Line2D.Double(strokePad,
                height - 1,
                width,
                height - 1);


        graphics2D.setColor(color);
        graphics2D.draw(line);
    }
}