package ru.kopp.ui.custom.components.job;

public class RunningJobsListPanel extends CompletedJobsListPanel {

    private static RunningJobsListPanel runningJobsListPanel;

    protected RunningJobsListPanel() {
        super();
        setLabel("Running Jobs");
    }

    public static RunningJobsListPanel getJobsListPanel() {
        if (runningJobsListPanel == null) {
            runningJobsListPanel = new RunningJobsListPanel();
            runningJobsListPanel.updateList();
        }
        return runningJobsListPanel;
    }

    @Override
    protected boolean isJobFits(String status) {
        switch (status.toLowerCase()) {
            case "finished":
            case "canceled":
            case "failed":
                return false;
            default:
                return true;
        }
    }
}