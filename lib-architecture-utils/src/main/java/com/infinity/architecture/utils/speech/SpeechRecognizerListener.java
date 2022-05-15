package com.infinity.architecture.utils.speech;

import android.os.Bundle;

public interface SpeechRecognizerListener {
    void onResults(Bundle results, String firstRecognizedText);

    void onError(int error, String errorMsg);
}
