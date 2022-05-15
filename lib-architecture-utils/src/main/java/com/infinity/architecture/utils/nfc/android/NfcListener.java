package com.infinity.architecture.utils.nfc.android;

public interface NfcListener {
    void onException(Throwable t);

    void onCardDetected(NfcHelper nfcHelper);
}
