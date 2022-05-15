package com.infinity.architecture.utils.backservices.api;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ApiLog {
    private static final ArrayList<String> lastLogMessages = new ArrayList<>();

    public static void addLogMsg(@Nullable String logMsg) {
        if (logMsg != null) {
            if (lastLogMessages.size() > 10) {
                lastLogMessages.remove(0);
            }

            lastLogMessages.add(logMsg);
        }
    }

    @NonNull
    public static ArrayList<String> getAvailableLogMsgs() {
        return lastLogMessages;
    }

    @NonNull
    public static String getAvailableLogMsgsAsStr() {
        return getAvailableLogMsgsAsStr(lastLogMessages.size());
    }

    @NonNull
    public static String getAvailableLogMsgsAsStr(int size) {
        StringBuilder strBuilderLogs = new StringBuilder();

        for(int i=0; i < size; i++) {
            strBuilderLogs.append("-----------------LOG($i)-----------------");
            strBuilderLogs.append(lastLogMessages.size() > 0 ? lastLogMessages.get(i) : null);
            strBuilderLogs.append("-----------------LOG($i)-----------------");
        }

        return strBuilderLogs.toString();
    }
}
