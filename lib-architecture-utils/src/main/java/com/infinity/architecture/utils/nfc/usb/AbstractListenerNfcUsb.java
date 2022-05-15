package com.infinity.architecture.utils.nfc.usb;

public abstract class AbstractListenerNfcUsb implements InterfaceListenerNfcUsb {
    @Override
    public void onCardInserted(NfcUsbHelper nfcUsbHelper) {

    }

    @Override
    public void onCardStateChange(int slotNum, int prevState, int currState) {

    }

    @Override
    public void onDeviceNotFound() {

    }

    @Override
    public void onUsbPermissionDenied() {

    }
}
