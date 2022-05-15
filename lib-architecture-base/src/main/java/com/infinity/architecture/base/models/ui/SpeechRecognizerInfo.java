package com.infinity.architecture.base.models.ui;

import android.content.Intent;
import android.speech.RecognizerIntent;

import androidx.annotation.NonNull;

import com.infinity.architecture.base.R;

import java.util.Locale;

public class SpeechRecognizerInfo {
    private Intent speechIntent;

    private SpeechRecognizerInfo() {

    }

    public static SpeechRecognizerInfo getInstance(@NonNull Intent speechIntent) {
        SpeechRecognizerInfo speechRecognizerInfo = new SpeechRecognizerInfo();
        speechRecognizerInfo.speechIntent = speechIntent;
        return speechRecognizerInfo;
    }

    public static Intent getSpeechIntent(String promtMsg) {
        //Intent to listen to user vocal input and return result in same activity
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        //Use a language model based on free-form speech recognition.
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        //Message to display in dialog box
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, promtMsg);

        return intent;
    }

    @NonNull
    public Intent getSpeechIntent() {
        return speechIntent;
    }
}
