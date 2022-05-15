package com.infinity.architecture.utils.backservices.api;

import org.jetbrains.annotations.NotNull;

import okhttp3.internal.platform.Platform;
import okhttp3.logging.HttpLoggingInterceptor;

public class ApiLogger implements HttpLoggingInterceptor.Logger {
    private String currentLogMsg = "";

    @Override
    public void log(@NotNull String message) {
        if (
            message.contains("--> GET") ||
            message.contains("--> HEAD") ||
            message.contains("--> POST") ||
            message.contains("--> DELETE") ||
            message.contains("--> PUT") ||
            message.contains("--> PATCH") ||
            message.contains("--> CONNECT")
        ) {
            currentLogMsg = "";
        }
        currentLogMsg += message;

        if (message.contains("<-- END HTTP") || message.contains("<-- HTTP FAILED")) {
            ApiLog.addLogMsg(currentLogMsg);
        }

        // Send firebase events here
        Platform.get().log(message, Platform.INFO, null);
    }
}
