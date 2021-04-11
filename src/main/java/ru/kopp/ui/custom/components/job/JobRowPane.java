package ru.kopp.ui.custom.components.job;

import net.miginfocom.swing.MigLayout;
import ru.kopp.client.api.rest.FlinkResponse;
import ru.kopp.client.api.rest.FlinkRestService;
import ru.kopp.client.model.jobs.FlinkJob;
import ru.kopp.ui.custom.components.common.FlinkBasePanel;
import ru.kopp.ui.custom.graphics.BottomSeparatorBorder;
import ru.kopp.ui.custom.services.DesignGuide;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class JobRowPane extends FlinkBasePanel {
    private final FlinkRestService flinkRestService = FlinkRestService.getFlinkRestService();
    private FlinkResponse jobExceptions;

    public JobRowPane(FlinkJob flinkJob) {
        //Appearance
        setLayout(new MigLayout("insets 0"));
        setBorder(new BottomSeparatorBorder(DesignGuide.gray()));

        //Create Components
        JLabel lbName = new JLabel(flinkJob.getName());
        JLabel lbStartTime = new JLabel(flinkJob.getStartTime());
        JLabel lbDuration = new JLabel(flinkJob.getDuration());
        JLabel lbEndTime = new JLabel(flinkJob.getEndTime());

        List<JLabel> labels = new ArrayList<>();
        flinkJob.getTasks().forEach((key, value) -> {
            if (value != 0) {
                JLabel lbTaskCount = new JLabel(String.valueOf(value), SwingConstants.CENTER);
                lbTaskCount.setOpaque(true);
                lbTaskCount.setForeground(DesignGuide.white());
                lbTaskCount.setBackground(getStatusColor(key));
                lbTaskCount.setToolTipText(key.toUpperCase());
                lbTaskCount.setFont(new Font("Tahoma", Font.BOLD, 14));
                lbTaskCount.setPreferredSize(new Dimension(20, 20));
                labels.add(lbTaskCount);
            }
        });
        FlinkBasePanel pLabels=new FlinkBasePanel();

        JLabel lbStatus = new JLabel(flinkJob.getState(), SwingConstants.CENTER);
        JButton btnExceptions = new JButton("Exceptions");

        //Components Appearance
        lbStatus.setForeground(DesignGuide.white());
        lbStatus.setBackground(getStatusColor(lbStatus.getText()));
        lbStatus.setOpaque(true);
        lbStatus.setPreferredSize(new Dimension(60, 15));
        lbStatus.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnExceptions.setEnabled(false);
        btnExceptions.setForeground(DesignGuide.white());
        btnExceptions.setBackground(DesignGuide.darkBlue());
        btnExceptions.setFont(new Font("Segoe UI", Font.BOLD, 12));

        //Mapping
        add(lbName, "pushx,growx, al left, w 50:150:500, gap 5");
        add(lbStartTime, "pushx,growx, al left, w 50:150:500");
        add(lbDuration, "pushx,growx, al left, w 50:150:500");
        add(lbEndTime, "pushx,growx,al left, w 50:150:500");
        labels.forEach(value -> pLabels.add(value, "ax left, h 10"));
        add(pLabels,"ax left, pushx, cell 4 0, h 10, w 50:150:500");
        add(lbStatus, "al left, pushx, w 50:150:500");
        add(btnExceptions, "al center, w 50:100:100, gap n 5 5 5");

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                jobExceptions = flinkRestService.getJobExceptions(flinkJob.getId());
                return null;
            }

            @Override
            protected void done() {
                btnExceptions.setEnabled(!jobExceptions.getDetailedMessage().equals(""));
                cancel(true);
                super.done();
            }
        }.execute();

        //Listeners
        btnExceptions.addActionListener(event -> new JobExceptionsPane(
                flinkJob.getName() + " Exceptions",
                jobExceptions)
        );
    }

    private static Color getStatusColor(String status) {
        switch (status.toLowerCase()) {
            case "total":
                return DesignGuide.black();

            case "created":
            case "scheduled":
                return DesignGuide.blue();

            case "finished":
                return DesignGuide.green();

            case "canceled":
                return DesignGuide.orange();

            case "failed":
                return DesignGuide.red();

            default:
                return DesignGuide.yellow();
        }
    }
}