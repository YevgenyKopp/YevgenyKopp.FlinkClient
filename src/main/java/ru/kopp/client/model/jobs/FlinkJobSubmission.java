package ru.kopp.client.model.jobs;

import java.util.List;

@SuppressWarnings("unused")
public class FlinkJobSubmission {

    private boolean allowNonRestoredState;
    private String entryClass;
    private String jobId;
    private int parallelism;
    private String programArgs;
    private List<String> programArgsList;
    private String savepointPath;

    public void setAllowNonRestoredState(boolean allowNonRestoredState) {
        this.allowNonRestoredState = allowNonRestoredState;
    }

    public void setEntryClass(String entryClass) {
        this.entryClass = entryClass;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public void setParallelism(int parallelism) {
        this.parallelism = parallelism;
    }

    public void setProgramArgs(String programArgs) {
        this.programArgs = programArgs;
    }

    public void setProgramArgsList(List<String> programArgsList) {
        this.programArgsList = programArgsList;
    }

    public void setSavepointPath(String savepointPath) {
        this.savepointPath = savepointPath;
    }

    public boolean isAllowNonRestoredState() {
        return allowNonRestoredState;
    }

    public String getEntryClass() {
        return entryClass;
    }

    public String getJobId() {
        return jobId;
    }

    public int getParallelism() {
        return parallelism;
    }

    public String getProgramArgs() {
        return programArgs;
    }

    public List<String> getProgramArgsList() {
        return programArgsList;
    }

    public String getSavepointPath() {
        return savepointPath;
    }
}