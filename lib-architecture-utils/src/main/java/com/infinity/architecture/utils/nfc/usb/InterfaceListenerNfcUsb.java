package com.infinity.architecture.utils.nfc.usb;

public interface InterfaceListenerNfcUsb {

    void onCardInserted(NfcUsbHelper nfcUsbHelper);

    void onCardStateChange(int slotNum, int prevState, int currState);

    void onDeviceNotFound();

    void onUsbPermissionDenied();
}
