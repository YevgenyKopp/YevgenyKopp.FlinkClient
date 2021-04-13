package ru.kopp.ui.frames;

import ru.kopp.client.api.rest.FlinkResponse;
import ru.kopp.client.api.rest.FlinkRestService;
import ru.kopp.client.model.responses.FlinkConfig;
import ru.kopp.services.FlinkErrorService;
import ru.kopp.ui.custom.components.common.FlinkBasePanel;
import ru.kopp.ui.custom.services.DesignGuide;
import ru.kopp.ui.custom.services.ResourceService;
import ru.kopp.ui.custom.services.TextFieldValidator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.Objects;

public class ConnectionDialog extends JFrame {

    private final JTextField txtUrl = new JTextField(20);
    private final FlinkRestService flinkRestService = FlinkRestService.getFlinkRestService();

    public ConnectionDialog() throws HeadlessException {
        //Appearance
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setTitle("Cluster URL");
        setIconImage(Objects.requireNonNull(ResourceService.getImageIcon("squirrel-icon.png")));

        //Create Components
        JLabel gif = new JLabel();
        JLabel lblUrl = new JLabel("Cluster URL:");
        JButton btnConnect = new JButton("Connect");
        FlinkBasePanel panel = new FlinkBasePanel();

        //Components Appearance
        URL urlGif = getClass().getClassLoader().getResource("loading.gif");
        if (urlGif != null) {
            ImageIcon iconGif = new ImageIcon(urlGif);
            gif.setIcon(iconGif);
        }
        gif.setVisible(false);
        txtUrl.putClientProperty("JComponent.outline", DesignGuide.blue());
        txtUrl.setText("http://localhost:8081");
        txtUrl.setToolTipText("http://...");
        new TextFieldValidator(txtUrl).validate();
        getRootPane().setDefaultButton(btnConnect);

        //Components Mapping
        panel.add(lblUrl, "ay center");
        panel.add(txtUrl);
        panel.add(gif, "ay center, ax right, wrap");
        panel.add(btnConnect, "span3, w 50, h 30, al center");
        add(panel);

        //Listeners
        btnConnect.addActionListener(event -> new SwingWorker<FlinkConfig, Void>() {

            @Override
            protected FlinkConfig doInBackground() {
                gif.setVisible(true);

                flinkRestService.setMainUrl(txtUrl.getText());
                return flinkRestService.connect();
            }

            @Override
            protected void done() {
                gif.setVisible(false);
                try {
                    FlinkConfig flinkConfig = get();

                    MainWindow mainWindow = MainWindow.getMainWindow();
                    mainWindow.setFlinkVersion(flinkConfig.getFlinkVersion());
                    mainWindow.setVisible(false);
                    mainWindow.revalidate();
                    mainWindow.repaint();
                    mainWindow.showWindow();

                    dispose();

                } catch (Exception e) {
                    FlinkErrorService.showError(
                            "Request Error",
                            new FlinkResponse("Failed to get connection status!"));
                }
                cancel(true);
                super.done();
            }
        }.execute());

        txtUrl.getDocument().addDocumentListener(new TextFieldValidator(txtUrl));

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (MainWindow.getMainWindow().isVisible()) {
                    FlinkRestService.getFlinkRestService().close();
                    System.exit(1);
                }
            }
        });

        //Show Dialog
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}