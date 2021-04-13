package ru.kopp.ui.frames;

import net.miginfocom.swing.MigLayout;
import ru.kopp.client.api.rest.FlinkRestService;
import ru.kopp.ui.custom.components.common.FlinkMenuBar;
import ru.kopp.ui.custom.components.jar.JarsListPanel;
import ru.kopp.ui.custom.components.job.CompletedJobsListPanel;
import ru.kopp.ui.custom.components.job.RunningJobsListPanel;
import ru.kopp.ui.custom.services.ResourceService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

public class MainWindow extends JFrame {

    private static MainWindow mainWindow;
    private final JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.LEFT);
    private final JarsListPanel jarsPanel = JarsListPanel.getJarsListPanel();
    private final RunningJobsListPanel runningJobsPanel = RunningJobsListPanel.getJobsListPanel();
    private final CompletedJobsListPanel completedJobsPanel = CompletedJobsListPanel.getJobsListPanel();
    private String flinkVersion;

    private MainWindow() {
    }

    public static MainWindow getMainWindow() {
        if (mainWindow == null) mainWindow = new MainWindow();
        return mainWindow;
    }

    public void showWindow() {
        //Appearance
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setTitle("Flink Client");
        setIconImage(Objects.requireNonNull(ResourceService.getImageIcon("squirrel-icon.png")));
        setLayout(new MigLayout());
        setMinimumSize(new Dimension(1300, 500));
        setPreferredSize(new Dimension(1400, 600));

        //Components Appearance
        tabbedPane.putClientProperty("JTabbedPane.tabHeight", 60);
        tabbedPane.putClientProperty("JTabbedPane.minimumTabWidth", 150);
        tabbedPane.putClientProperty("JTabbedPane.showTabSeparators", true);
        tabbedPane.putClientProperty("JTabbedPane.tabAlignment", SwingConstants.LEADING);
        Image submitIcon = Objects.requireNonNull(ResourceService.getImageIcon("submit-icon.png"));
        Image playIcon = Objects.requireNonNull(ResourceService.getImageIcon("play-icon.png"));
        Image completedIcon = Objects.requireNonNull(ResourceService.getImageIcon("completed-icon.png"));

        //Components Mapping
        tabbedPane.addTab("Submit New Job", new ImageIcon(submitIcon), jarsPanel);
        tabbedPane.addTab("Running Jobs", new ImageIcon(playIcon), runningJobsPanel);
        tabbedPane.addTab("Completed Jobs", new ImageIcon(completedIcon), completedJobsPanel);
        add(new FlinkMenuBar(flinkVersion), "pushx,growx,wrap");
        add(tabbedPane, "push, grow, cell 0 1");

        //Listeners
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });

        //Show Window
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    private void exit() {
        int exit = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to exit the application?",
                "Exit Application",
                JOptionPane.YES_NO_OPTION);
        if (JOptionPane.YES_OPTION == exit) {
            FlinkRestService.getFlinkRestService().close();
            System.exit(1);
        }
    }

    public void updateAll() {
        jarsPanel.updateList();
        completedJobsPanel.updateList();
        runningJobsPanel.updateList();
    }

    public void setFlinkVersion(String flinkVersion) {
        this.flinkVersion = flinkVersion;
    }
}