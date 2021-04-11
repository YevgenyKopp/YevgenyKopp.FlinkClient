package ru.kopp.client.model.responses;

import com.squareup.moshi.Json;
import ru.kopp.client.model.jobs.FlinkJobDetailedException;

import java.util.List;

@SuppressWarnings("unused")
public class FlinkErrors {
    @Json(name = "errors")
    private List<String> errors;

    @Json(name = "root-exception")
    private String rootException;

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    @Json(name = "all-exceptions")
    private List<FlinkJobDetailedException> detailedExceptions;

    public List<String> getErrors() {
        return errors;
    }

    public String getRootException() {
        return rootException;
    }

    public String getDetailedExceptions() {
        StringBuilder stringBuilder = new StringBuilder();
        if (detailedExceptions != null)
            detailedExceptions.forEach(detailedExceptions -> stringBuilder.append(detailedExceptions.getException()).append("\n"));
        return stringBuilder.toString();
    }
}