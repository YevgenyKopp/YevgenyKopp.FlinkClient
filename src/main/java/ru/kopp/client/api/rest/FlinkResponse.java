package ru.kopp.client.api.rest;

import com.squareup.moshi.Moshi;
import ru.kopp.client.model.responses.FlinkErrors;
import ru.kopp.services.FlinkLoggerService;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class FlinkResponse {

    private final int statusCode;
    private String shortMessage;
    private String detailedMessage;


    public FlinkResponse(int statusCode, String shortMessage, String detailedMessage) {
        this.statusCode = statusCode;
        setShortMessage(shortMessage);
        setDetailedMessage(detailedMessage);
    }

    public FlinkResponse(String shortMessage) {
        this.statusCode = -1;
        setShortMessage(shortMessage);
        this.detailedMessage = "";
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getDetailedMessage() {
        return detailedMessage;
    }

    public void setDetailedMessage(String detailedMessage) {
        if (detailedMessage.length() > 0 && !detailedMessage.startsWith("<!--")) {
            try {
                Moshi moshi = new Moshi.Builder().build();
                FlinkErrors flinkErrors = moshi.adapter(FlinkErrors.class).fromJson(detailedMessage);
                StringBuilder stringBuilder = new StringBuilder();

                List<String> errors = Objects.requireNonNull(flinkErrors).getErrors();
                String rootException = Objects.requireNonNull(flinkErrors).getRootException();
                String exceptions = Objects.requireNonNull(flinkErrors).getDetailedExceptions();

                if (errors != null) errors.forEach(error -> stringBuilder.append(error).append("\n\n"));
                if (rootException != null) stringBuilder.append(rootException).append("\n\n");
                if (exceptions != null) stringBuilder.append(exceptions);
                this.detailedMessage = stringBuilder.toString();
            } catch (IOException e) {
                FlinkLoggerService.getLogger().warning("Failed to parse json!");
            }
        } else this.detailedMessage = "";
    }

    public String getShortMessage() {
        return shortMessage;
    }

    public void setShortMessage(String shortMessage) {
        this.shortMessage = shortMessage
                .replace("{", ":\n\t")
                .replace("[", "")
                .replace("]", "")
                .replace("}", "")
                .replace(",", "\n\t");
    }
}