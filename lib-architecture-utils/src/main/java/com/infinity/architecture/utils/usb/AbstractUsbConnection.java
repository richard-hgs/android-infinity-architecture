package com.infinity.architecture.utils.usb;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

public abstract class AbstractUsbConnection
        implements InterfaceUsbConnection {

    @Override
    public void onDeviceEncontrado(UsbDevice device, UsbManager usbManager) {

    }

    @Override
    public void onDeviceNaoEncontrado() {

    }

    @Override
    public void onPermissaoNegada() {

    }
}
