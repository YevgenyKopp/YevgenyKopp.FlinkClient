package ru.kopp.ui.custom.components.common;

import com.formdev.flatlaf.intellijthemes.FlatCyanLightIJTheme;
import net.miginfocom.swing.MigLayout;
import ru.kopp.ui.custom.services.DesignGuide;
import ru.kopp.ui.custom.services.RefreshService;
import ru.kopp.ui.custom.services.ResourceService;
import ru.kopp.ui.frames.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class FlinkMenuBar extends JMenuBar {

    public FlinkMenuBar() {
        //Appearance
        setLayout(new MigLayout("insets 0"));

        //Create Components
        JButton refresh=new JButton("Refresh");
        JComboBox<String> refreshMode=new JComboBox<>();

        //Components Appearance
        Image refreshIcon = Objects.requireNonNull(ResourceService.getImageIcon("refresh-icon.png"));
        refresh.setIcon(new ImageIcon(refreshIcon));
        refresh.putClientProperty("JComponent.outline",getBackground());
        refresh.setBackground(null);
        refreshMode.addItem("manual");
        refreshMode.addItem("every 5 seconds");
        refreshMode.addItem("every 30 seconds");
        refreshMode.addItem("every 60 seconds");
        refreshMode.setSelectedIndex(1);
        refreshMode.setBackground(null);
        refreshMode.setBorder(BorderFactory.createLineBorder(DesignGuide.gray()));

        //Components Mapping
        add(refresh, "ax right,pushx");
        add(refreshMode,"ax right, gap n 3 3 3");

        //Auto Refresh
        RefreshService refreshService=new RefreshService();

        //Listeners
        refresh.addActionListener(e-> MainWindow.getMainWindow().updateAll());

        refreshMode.addItemListener(e-> refreshService.setTime(refreshMode.getSelectedIndex()));
    }

    public static void main(String[] args) {
        FlatCyanLightIJTheme.install();
        JFrame frame=new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        FlinkBasePanel panel=new FlinkBasePanel(new MigLayout("insets 0"));

        panel.add(new FlinkMenuBar(),"pushx,growx,wrap");
        panel.add(new FlinkBasePanel(),"push,grow,wrap");
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }


}