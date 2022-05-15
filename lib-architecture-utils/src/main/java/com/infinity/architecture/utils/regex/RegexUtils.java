package com.infinity.architecture.utils.regex;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {

    public static ArrayList<RegexMatchInfo> searchPatternInStr(String pattern, String str) {
        ArrayList<RegexMatchInfo> regexMatchInfos = new ArrayList<>();

        Pattern regexPattern = Pattern.compile(pattern);

        Matcher matcher = regexPattern.matcher(str);
        while (matcher.find()) {
            ArrayList<RegexMatchGroupInfo> regexMatchGroupInfos = new ArrayList<>();

            int groupCount = matcher.groupCount();
            for (int i=0; i<groupCount; i++) {
                String groupValue = matcher.group(i+1);
                regexMatchGroupInfos.add(
                    RegexMatchGroupInfo.getInstance(groupValue)
                );
            }

            int matchStart = matcher.start();
            int matchEnd = matcher.end();

            regexMatchInfos.add(
                RegexMatchInfo.getInstance(
                    matcher.group(),
                    matchStart,
                    matchEnd,
                    regexMatchGroupInfos
                )
            );
        }

        return regexMatchInfos;
    }
}
