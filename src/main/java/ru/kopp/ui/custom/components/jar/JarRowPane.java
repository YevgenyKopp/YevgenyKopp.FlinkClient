package ru.kopp.ui.custom.components.jar;

import net.miginfocom.swing.MigLayout;
import ru.kopp.client.api.rest.FlinkResponse;
import ru.kopp.client.api.rest.FlinkRestService;
import ru.kopp.client.model.jar.FlinkJar;
import ru.kopp.services.FlinkErrorService;
import ru.kopp.ui.custom.components.common.FlinkBasePanel;
import ru.kopp.ui.custom.graphics.BottomSeparatorBorder;
import ru.kopp.ui.custom.services.DesignGuide;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class JarRowPane extends FlinkBasePanel {
    private final FlinkJar flinkJar;
    private final FlinkRestService flinkRestService = FlinkRestService.getFlinkRestService();
    private final JarPropertiesPane propertiesPane;
    private final BottomSeparatorBorder separator = new BottomSeparatorBorder(DesignGuide.gray());

    public JarRowPane(FlinkJar flinkJar) {
        this.flinkJar=flinkJar;

        //Appearance
        setLayout(new MigLayout("insets 0"));
        setBorder(separator);

        //Create Components
        String displayedEntry = flinkJar.getEntry().size() == 1
                ? flinkJar.getEntry().get(0).getName()
                : "--";

        JLabel lbName = new JLabel(flinkJar.getName());
        JLabel lbUploadTime = new JLabel(flinkJar.getUploaded());
        JLabel lbEntry = new JLabel(displayedEntry);
        JButton btnDelete = new JButton("Delete");
        propertiesPane = new JarPropertiesPane(flinkJar);

        //Components Appearance
        btnDelete.setForeground(DesignGuide.white());
        btnDelete.setBackground(DesignGuide.darkBlue());
        btnDelete.setFont(new Font("Segoe UI", Font.BOLD, 12));

        //Mapping
        add(lbName, "pushx,growx, al left, w 50:150:500, gap 5 n 10 n");
        add(lbUploadTime, "pushx,growx, al left, w 50:200:550");
        add(lbEntry, "pushx,growx, al left, w 50:400:1000");
        add(btnDelete, "al center, w 50:100:100, wrap, gap n 5 n n");
        add(propertiesPane, "pushx, growx, hidemode 3, span 4");
        propertiesPane.setVisible(false);

        //Listeners
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (e.getPoint().getY() < btnDelete.getHeight() + 10)
                    setBackground(DesignGuide.lightBlue());
                else setBackground(DesignGuide.white());
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(DesignGuide.white());
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getPoint().getY() < btnDelete.getHeight() + 10) {
                    if (propertiesPane.isVisible()) {
                        propertiesPane.setVisible(false);
                        setBorder(separator);
                    } else {
                        JarsListPanel.getJarsListPanel().hideAllProperties();
                        propertiesPane.setVisible(true);
                        setBorder(BorderFactory.createLineBorder(DesignGuide.gray()));
                    }
                }
            }
        });

        btnDelete.addActionListener(e ->
                new SwingWorker<FlinkResponse, Void>() {
                    @Override
                    protected FlinkResponse doInBackground() {
                        return flinkRestService.deleteJar(flinkJar.getId());
                    }

                    @Override
                    protected void done() {
                        try {
                            FlinkResponse response = get();
                            if (response.getStatusCode() != 200)
                                FlinkErrorService.showError(
                                        "Failed to delete " + flinkJar.getName(),
                                        response
                                );
                        } catch (Exception interruptedException) {
                            FlinkErrorService.showError(
                                    "Request Error",
                                    new FlinkResponse("Failed to get response!"));
                        }

                        JarsListPanel.getJarsListPanel().updateList();
                        cancel(true);
                        super.done();
                    }
                }.execute()
        );

    }

    public JarPropertiesPane getPropertiesPane() {
        return propertiesPane;
    }

    public FlinkJar getFlinkJar() {
        return flinkJar;
    }

    public void removeSquareBorder() {
        setBorder(separator);
    }
}