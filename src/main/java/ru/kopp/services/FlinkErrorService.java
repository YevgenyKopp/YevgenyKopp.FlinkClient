package ru.kopp.services;

import ru.kopp.client.api.rest.FlinkResponse;
import ru.kopp.ui.custom.components.common.FlinkErrorDialog;

public class FlinkErrorService {
    public static void showError(String title, FlinkResponse flinkResponse) {
        new FlinkErrorDialog(title, flinkResponse);

        if (!flinkResponse.getShortMessage().equals(""))
            FlinkLoggerService.getLogger().warning(flinkResponse.getShortMessage());
    }
}