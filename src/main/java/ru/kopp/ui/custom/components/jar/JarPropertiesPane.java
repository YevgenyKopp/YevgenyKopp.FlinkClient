package ru.kopp.ui.custom.components.jar;

import ru.kopp.client.api.rest.FlinkResponse;
import ru.kopp.client.api.rest.FlinkRestService;
import ru.kopp.client.model.jar.FlinkJar;
import ru.kopp.client.model.jobs.FlinkJobSubmission;
import ru.kopp.services.FlinkErrorService;
import ru.kopp.ui.custom.components.common.FlinkBasePanel;
import ru.kopp.ui.custom.services.DesignGuide;
import ru.kopp.ui.frames.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Objects;

public class JarPropertiesPane extends FlinkBasePanel {

    private final FlinkRestService flinkRestService = FlinkRestService.getFlinkRestService();

    public JarPropertiesPane(FlinkJar flinkJar) {
        //Appearance
        setBackground(DesignGuide.lightGray());

        //Create Components
        JLabel lbClass = new JLabel("Entry Class:");
        JLabel lbParallelism = new JLabel("Parallelism:");
        JLabel lbArguments = new JLabel("Program Arguments:");
        JLabel lbSavepoint = new JLabel("Savepoint Path:");
        JComboBox<String> combEntryClass = new JComboBox<>();
        JTextField txtParallelism = new JTextField(20);
        JTextField txtArguments = new JTextField(20);
        JTextField txtSavepoint = new JTextField(20);
        JCheckBox cbRestoredState = new JCheckBox("Allow Non Restored State");
        JButton btnPlan = new JButton("View Plan");
        JButton btnSubmit = new JButton("Submit");

        //Components Appearance
        combEntryClass.setEditable(true);
        cbRestoredState.setOpaque(false);
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSubmit.setForeground(DesignGuide.white());
        btnSubmit.setBackground(DesignGuide.darkBlue());
        txtParallelism.setToolTipText("Integer Value");
        Color txtForeground = txtParallelism.getForeground();

        //Components Mapping
        add(lbClass, "ax left, pushx, gap 3 n 0 0");
        add(lbParallelism, "ax left, pushx, wrap, gap 13 n 0 0");
        add(combEntryClass, "sg 1, w 50:n, pushx, growx, gap n n 0 n");
        add(txtParallelism, "sg 1, w 50:n, pushx, growx, wrap, gap 10");
        add(lbArguments, "ax left, pushx, gap 3 n 0 0");
        add(lbSavepoint, "ax left, pushx, wrap, gap 13 n 0 0");
        add(txtArguments, "sg 1, w 50:n, pushx, growx");
        add(txtSavepoint, "sg 1, w 50:n, pushx, growx, wrap,gap 10");
        add(cbRestoredState, "w 50:n, al left");
        add(btnPlan, "w 50:100:100, al right");
        add(btnSubmit, "w 50:100:100, al right, cell 1 4");

        //Listeners
        btnSubmit.addActionListener(e -> {
            try {
                int parallelism;
                if (txtParallelism.getText().equals("")) parallelism = 1;
                else parallelism = Integer.parseInt(txtParallelism.getText());

                String entry = Objects.requireNonNull(combEntryClass.getSelectedItem()).toString();
                String savepoint = txtSavepoint.getText();
                String arguments = txtArguments.getText();

                FlinkJobSubmission flinkJobSubmission = new FlinkJobSubmission();

                flinkJobSubmission.setAllowNonRestoredState(cbRestoredState.isSelected());
                flinkJobSubmission.setEntryClass(entry);
                flinkJobSubmission.setParallelism(parallelism);
                flinkJobSubmission.setSavepointPath(savepoint);
                flinkJobSubmission.setProgramArgs(arguments);

                new SwingWorker<FlinkResponse, Void>() {
                    @Override
                    protected FlinkResponse doInBackground() {
                        return flinkRestService.submitJob(flinkJobSubmission, flinkJar.getId());
                    }

                    @Override
                    protected void done() {
                        try {
                            FlinkResponse response = get();
                            if (response.getStatusCode() != 200) {
                                FlinkErrorService.showError(
                                        "Failed to submit " + flinkJar.getName(),
                                        response
                                );
                            } else {
                                MainWindow.getMainWindow().updateAll();
                                MainWindow.getMainWindow().getTabbedPane().setSelectedIndex(2);
                            }
                        } catch (Exception interruptedException) {
                            FlinkErrorService.showError(
                                    "Request Error",
                                    new FlinkResponse("Failed to get response!"));
                        }

                        cancel(true);
                        super.done();
                    }
                }.execute();
            } catch (NumberFormatException exception) {
                txtParallelism.setForeground(DesignGuide.red());
                updateUI();
            }

            txtParallelism.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    txtParallelism.setForeground(txtForeground);
                }
            });

        });

        btnPlan.addActionListener(e -> new JarPlanView(flinkJar));

        //Set Entry Classes
        if (flinkJar.getEntry() != null && !flinkJar.getEntry().isEmpty()) {
            flinkJar.getEntry().forEach(entry -> combEntryClass.addItem(entry.getName()));
            combEntryClass.setSelectedItem(combEntryClass.getItemAt(0));
        }
    }
}