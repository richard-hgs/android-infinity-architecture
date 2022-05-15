package com.infinity.architecture.utils.regex;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class RegexMatchInfo {
    private String value;
    private int start;
    private int end;

    private ArrayList<RegexMatchGroupInfo> groups = new ArrayList<>();

    private RegexMatchInfo() {
    }

    public static RegexMatchInfo getInstance(@NonNull String value, int start, int end, @NonNull  ArrayList<RegexMatchGroupInfo> groups) {
        RegexMatchInfo regexMatchInfo = new RegexMatchInfo();
        regexMatchInfo.value = value;
        regexMatchInfo.start = start;
        regexMatchInfo.end = end;
        regexMatchInfo.groups = groups;
        return regexMatchInfo;
    }

    public String getValue() {
        return value;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public ArrayList<RegexMatchGroupInfo> getGroups() {
        return groups;
    }


    @Override
    public String toString() {
        return "RegexMatchInfo{" +
                "value='" + value + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", groups=" + groups +
                '}';
    }
}
