package ru.kopp.ui.custom.components.jar;

import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import net.miginfocom.swing.MigLayout;
import okio.Buffer;
import ru.kopp.client.api.rest.FlinkRestService;
import ru.kopp.services.FlinkLoggerService;
import ru.kopp.client.model.jar.FlinkJar;
import ru.kopp.client.model.plan.FlinkUploadedPlans;
import ru.kopp.ui.custom.components.common.FlinkBasePanel;
import ru.kopp.ui.custom.services.DesignGuide;
import ru.kopp.ui.custom.services.ResourceService;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Logger;

public class JarPlanView extends JDialog {

    private final FlinkJar flinkJar;
    private final FlinkRestService flinkRestService = FlinkRestService.getFlinkRestService();
    private final Logger logger = FlinkLoggerService.getLogger();
    private final JTextArea txtArea = new JTextArea();

    public JarPlanView(FlinkJar flinkJar) {
        this.flinkJar = flinkJar;

        FlinkBasePanel panel = new FlinkBasePanel();

        //Appearance
        setTitle("Plan View");
        setLayout(new MigLayout("insets 0"));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setIconImage(Objects.requireNonNull(ResourceService.getImageIcon("squirrel-icon.png")));

        //Create Components
        JLabel lbPlan = new JLabel(flinkJar.getName() + " Plan:");
        JScrollPane scrollPane = new JScrollPane(txtArea);
        JButton btnClose = new JButton("Close");

        //Components Appearance
        txtArea.setBorder(BorderFactory.createLineBorder(DesignGuide.gray()));
        lbPlan.setFont(new Font("Tahoma", Font.BOLD, 14));
        txtArea.setEditable(false);
        getRootPane().setDefaultButton(btnClose);

        //Components Mapping
        panel.add(lbPlan, "al left, wrap");
        panel.add(scrollPane, "push,grow,al left, w 200:700:n, h 200:700:n, wrap");
        panel.add(btnClose, "al right, w 50:100:100");

        //Print Plan
        getPlan();

        //Listeners
        btnClose.addActionListener(e -> dispose());

        //Show Dialog
        add(panel, "push, grow");
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void getPlan() {
        new SwingWorker<Void, Void>() {
            FlinkUploadedPlans flinkPlans;

            @Override
            protected Void doInBackground() {
                flinkPlans = flinkRestService.getJarPlan(flinkJar.getId());
                return null;
            }

            @Override
            protected void done() {
                try (Buffer buffer = new Buffer();
                     JsonWriter jsonWriter = JsonWriter.of(buffer)) {

                    Moshi moshi = new Moshi.Builder().build();
                    jsonWriter.setIndent("    ");

                    moshi.adapter(FlinkUploadedPlans.class).toJson(jsonWriter, flinkPlans);
                    txtArea.setText(buffer.readUtf8());

                } catch (IOException e) {
                    logger.warning("Failed to load plan for " + flinkJar.getName());
                }
                cancel(true);
                super.done();
            }
        }.execute();
    }
}