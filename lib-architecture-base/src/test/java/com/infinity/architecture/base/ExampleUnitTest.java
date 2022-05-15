package com.infinity.architecture.base;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testUriParser() {
        String contentText = "content://";
        String uriToParse = "content://com.infinity.architecture.base.fileprovider/my_files/profile_picture_20211124_160454.png";
        if (uriToParse.contains(contentText)) {
            int contentStartIndex = uriToParse.indexOf(contentText);
            int contentEndIndex = contentStartIndex + contentText.length();
            String lastPart = uriToParse.substring(contentEndIndex);
            String[] lastPartSplit = lastPart.split("/");
            if (lastPartSplit.length > 2) {
                String providerPackageName = lastPartSplit[0];
                String providerPathName = lastPartSplit[1];
                String pathInPath = "";
                for (int i = 2; i < lastPartSplit.length; i++) {
                    pathInPath += (i > 2 ? File.pathSeparator : "") + lastPartSplit[i];
                }
                System.out.println(contentText + providerPackageName + "/" + providerPathName + "/" + pathInPath);
            }
        }
    }
}