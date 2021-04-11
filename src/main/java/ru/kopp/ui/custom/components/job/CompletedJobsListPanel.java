package ru.kopp.ui.custom.components.job;

import ru.kopp.client.api.rest.FlinkRestService;
import ru.kopp.client.model.jobs.FlinkJob;
import ru.kopp.ui.custom.components.common.FlinkListPanel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class CompletedJobsListPanel extends FlinkListPanel {

    private static CompletedJobsListPanel completedJobsListPanel;
    private final FlinkRestService flinkRestService = FlinkRestService.getFlinkRestService();
    private final List<JobRowPane> rowPanes = new ArrayList<>();

    protected CompletedJobsListPanel() {
        this.headersPane = new JobsHeadersPane();
        construct();
        setLabel("Completed Jobs");
    }

    public static CompletedJobsListPanel getJobsListPanel() {
        if (completedJobsListPanel == null) {
            completedJobsListPanel = new CompletedJobsListPanel();
            completedJobsListPanel.updateList();
        }
        return completedJobsListPanel;
    }

    public void updateList() {
        new SwingWorker<Void, Void>() {
            List<FlinkJob> flinkJobs;

            @Override
            protected Void doInBackground() {
                flinkJobs = flinkRestService.getJobs();
                return null;
            }

            @Override
            protected void done() {
                rowPanes.forEach(listPanel::remove);
                rowPanes.clear();
                flinkJobs.forEach(flinkJob -> {
                    if (isJobFits(flinkJob.getState())) {
                        JobRowPane newRowPane = new JobRowPane(flinkJob);
                        rowPanes.add(newRowPane);
                        listPanel.add(newRowPane, "w 50:n,wrap,pushx,growx");
                    }
                });
                emptyListPanel.setVisible(rowPanes.size() == 0);
                scrollPane.setVisible(rowPanes.size() != 0);

                updateUI();

                cancel(true);
                super.done();
            }
        }.execute();
    }

    protected boolean isJobFits(String status) {
        switch (status.toLowerCase()) {
            case "finished":
            case "canceled":
            case "failed":
                return true;
            default:
                return false;
        }
    }
}