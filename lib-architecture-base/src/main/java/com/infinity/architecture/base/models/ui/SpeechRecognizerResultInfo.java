package com.infinity.architecture.base.models.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SpeechRecognizerResultInfo {
    private boolean errorExists;
    private int errorCode;
    private String errorMsg;

    private Bundle results;
    private String firstTextResult;
    private ArrayList<String> textResults;

    private SpeechRecognizerResultInfo() {
    }

    public static SpeechRecognizerResultInfo getSuccessInstance(
        @Nullable Bundle results, @Nullable String firstTextResult, @Nullable ArrayList<String> textResults
    ) {
        SpeechRecognizerResultInfo speechRecognizerResultInfo = new SpeechRecognizerResultInfo();
        speechRecognizerResultInfo.errorExists = false;
        speechRecognizerResultInfo.errorCode = 0;
        speechRecognizerResultInfo.errorMsg = null;
        speechRecognizerResultInfo.results = results;
        speechRecognizerResultInfo.firstTextResult = firstTextResult;
        speechRecognizerResultInfo.textResults = textResults;

        return speechRecognizerResultInfo;
    }

    public static SpeechRecognizerResultInfo getErrorInstance(
        int errorCode, @NonNull String errorMsg
    ) {
        SpeechRecognizerResultInfo speechRecognizerResultInfo = new SpeechRecognizerResultInfo();
        speechRecognizerResultInfo.errorExists = true;
        speechRecognizerResultInfo.errorCode = errorCode;
        speechRecognizerResultInfo.errorMsg = errorMsg;

        return speechRecognizerResultInfo;
    }

    public boolean isErrorExists() {
        return errorExists;
    }

    public int getErrorCode() {
        return errorCode;
    }

    @Nullable
    public String getErrorMsg() {
        return errorMsg;
    }

    public Bundle getResults() {
        return results;
    }

    @Nullable
    public String getFirstTextResult() {
        return firstTextResult;
    }

    @Nullable
    public ArrayList<String> getTextResults() {
        return textResults;
    }
}
