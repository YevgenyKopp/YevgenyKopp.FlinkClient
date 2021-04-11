package ru.kopp.client.model.jobs;

import com.squareup.moshi.Json;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
public class FlinkJob {

    private String jid;
    private String name;
    private String state;

    @Json(name = "start-time")
    private long startTime;

    @Json(name = "end-time")
    private long endTime;

    private long duration;

    @Json(name = "last-modification")
    private long lastModification;

    private Map<String, Integer> tasks;

    public String getId() {
        return jid;
    }

    public String getName() {
        return name;
    }

    private static DateFormat getDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd, HH:mm:ss");
    }

    public String getState() {
        return state;
    }

    public String getStartTime() {
        return getDateFormat().format(new Date(startTime));
    }

    public String getEndTime() {
        return getDateFormat().format(new Date(endTime));
    }

    public String getDuration() {

        long hours = TimeUnit.MILLISECONDS.toHours(duration);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration));
        long seconds = TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration));
        long millis = duration - TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(duration));

        return (hours != 0 ? hours + "h " : "") +
                (minutes != 0 ? minutes + "m " : "") +
                (seconds != 0 ? seconds + "s " : "") +
                (millis != 0 ? millis + "ms " : "");
    }

    public String getLastModification() {
        return getDateFormat().format(new Date(startTime));
    }

    public Map<String, Integer> getTasks() {
        return tasks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FlinkJob)) return false;
        FlinkJob flinkJob = (FlinkJob) o;
        return Objects.equals(jid, flinkJob.jid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jid, name, state, startTime, endTime, duration, lastModification, tasks);
    }
}