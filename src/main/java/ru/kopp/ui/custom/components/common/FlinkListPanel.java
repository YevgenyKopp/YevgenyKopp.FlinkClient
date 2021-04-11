package ru.kopp.ui.custom.components.common;

import net.miginfocom.swing.MigLayout;
import ru.kopp.ui.custom.services.ResourceService;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public abstract class FlinkListPanel extends FlinkBasePanel {
    protected final FlinkBasePanel listPanel = new FlinkBasePanel(new MigLayout("insets 0"));
    protected final FlinkBasePanel emptyListPanel=new FlinkBasePanel(new MigLayout("insets 0"));
    protected final JScrollPane scrollPane = new JScrollPane(listPanel);
    protected final JLabel lbPanelName = new JLabel();
    protected FlinkHeadersPane headersPane;

    @SuppressWarnings("unused")
    public abstract void updateList();

    protected void construct() {
        //Appearance
        setLayout(new MigLayout("hidemode 3"));

        //Create Static Components
        JLabel iconSquirrel = new JLabel();
        Image emptyBoxIcon = Objects.requireNonNull(ResourceService.getImageIcon("empty-box.png"));
        JLabel emptyBox=new JLabel("",SwingConstants.CENTER);

        emptyBox.setVerticalAlignment(SwingConstants.CENTER);

        //Components Appearance
        Image squirrelIcon = Objects.requireNonNull(ResourceService.getImageIcon("squirrel-icon.png"));
        iconSquirrel.setIcon(new ImageIcon(squirrelIcon));
        lbPanelName.setFont(new Font("Tahoma", Font.BOLD, 20));
        emptyBox.setIcon(new ImageIcon(emptyBoxIcon));
        emptyListPanel.setVisible(false);

        //Components Mapping
        add(iconSquirrel, "al left, gap n 10 n n");
        add(lbPanelName, "ax left, ay center, cell 0 0, pushx,growx,wrap");
        add(headersPane, "w 850:850, pushx, growx, wrap");
        emptyListPanel.add(emptyBox,"ax center, ay center, push, grow");
        add(emptyListPanel,"al center center,push,grow,wrap");

        //Add ScrollPane
        scrollPane.setBorder(null);
        add(scrollPane, "w 50:900:n, h 50:400:n, push,grow");
    }

    protected void setLabel(String label){
        lbPanelName.setText(label);
    }
}