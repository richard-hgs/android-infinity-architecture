package com.infinity.architecture.utils.regex;

import androidx.annotation.NonNull;


public class RegexMatchGroupInfo {
    private String value;

    private RegexMatchGroupInfo() {
    }

    public static RegexMatchGroupInfo getInstance(@NonNull String value) {
        RegexMatchGroupInfo regexMatchGroupInfo = new RegexMatchGroupInfo();
        regexMatchGroupInfo.value = value;
        return regexMatchGroupInfo;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "RegexMatchGroupInfo{" +
                "value='" + value + '\'' +
                '}';
    }
}
