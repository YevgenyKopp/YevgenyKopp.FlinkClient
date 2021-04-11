package ru.kopp.ui.custom.components.jar;

import ru.kopp.client.api.rest.FlinkResponse;
import ru.kopp.client.api.rest.FlinkRestService;
import ru.kopp.client.model.jar.FlinkJar;
import ru.kopp.services.FlinkErrorService;
import ru.kopp.ui.custom.components.common.FlinkListPanel;
import ru.kopp.ui.custom.services.DesignGuide;
import ru.kopp.ui.custom.services.ResourceService;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JarsListPanel extends FlinkListPanel {

    private static JarsListPanel jarsListPanel;
    private final FlinkRestService flinkRestService = FlinkRestService.getFlinkRestService();
    private final List<JarRowPane> rowPanes = new ArrayList<>();

    private JarsListPanel() {
        this.headersPane=new JarsHeadersPane();
        construct();
        setLabel("Uploaded Jars");

        //Create Components
        JButton btnAddJar = new JButton("Add New");

        //Components Appearance
        Image plusIcon = Objects.requireNonNull(ResourceService.getImageIcon("plus.png"));
        btnAddJar.setIcon(new ImageIcon(plusIcon));
        btnAddJar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAddJar.setForeground(DesignGuide.white());
        btnAddJar.setBackground(DesignGuide.darkBlue());

        //Components Mapping
        add(btnAddJar,"cell 0 0, al right");

        //Listeners
        btnAddJar.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();

            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    ".jar or .py", "jar", "py");
            chooser.setFileFilter(filter);

            int returnVal = chooser.showOpenDialog(null);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File jarFile = chooser.getSelectedFile();

                new SwingWorker<FlinkResponse, Void>() {
                    @Override
                    protected FlinkResponse doInBackground() {
                        return flinkRestService.uploadJar(jarFile);
                    }

                    @Override
                    protected void done() {
                        try {
                            FlinkResponse response = get();
                            if (response.getStatusCode() != 200)
                                FlinkErrorService.showError("Failed to upload jar " + jarFile.getName(), response);
                        } catch (Exception interruptedException) {
                            FlinkErrorService.showError(
                                    "Request Error",
                                    new FlinkResponse("Failed to get response!"));
                        }

                        updateList();
                        cancel(true);
                        super.done();
                    }
                }.execute();
            }
        });
    }

    public static JarsListPanel getJarsListPanel() {
        if (jarsListPanel == null) {
            jarsListPanel = new JarsListPanel();
            jarsListPanel.updateList();
        }
        return jarsListPanel;
    }

    @Override
    public void updateList() {
        new SwingWorker<Void, Void>() {
            List<FlinkJar> flinkJars;

            @Override
            protected Void doInBackground() {
                flinkJars = flinkRestService.getJars();
                return null;
            }

            @Override
            protected void done() {
                emptyListPanel.setVisible(flinkJars.size() == 0);
                scrollPane.setVisible(flinkJars.size() != 0);
                if (rowPanes.size() > 0) {
                    rowPanes.forEach(rowPane -> {
                        if (!flinkJars.contains(rowPane.getFlinkJar())) {
                            //remove(rowPane);
                            listPanel.remove(rowPane);
                        }
                    });
                } else {
                    for (FlinkJar flinkJar : flinkJars) {
                        JarRowPane newRowPane = new JarRowPane(flinkJar);
                        rowPanes.add(newRowPane);
                        listPanel.add(newRowPane, "w 50:n,wrap,pushx,growx");
                    }
                }

                if (flinkJars.size() > 0) {
                    for (FlinkJar flinkJar : flinkJars) {
                        boolean toAdd = true;
                        for (JarRowPane rowPane : rowPanes) {
                            if (flinkJar.equals(rowPane.getFlinkJar())) {
                                toAdd = false;
                                break;
                            }
                        }
                        if (toAdd) {
                            JarRowPane newRowPane = new JarRowPane(flinkJar);
                            rowPanes.add(newRowPane);
                            listPanel.add(newRowPane, "w 50:n,wrap,pushx,growx");
                        }
                    }
                } else {
                    rowPanes.forEach(listPanel::remove);
                    rowPanes.clear();
                }
                updateUI();

                cancel(true);
                super.done();
            }
        }.execute();
    }

    public void hideAllProperties() {
        rowPanes.forEach(pane -> {
            pane.getPropertiesPane().setVisible(false);
            pane.removeSquareBorder();
        });
    }
}