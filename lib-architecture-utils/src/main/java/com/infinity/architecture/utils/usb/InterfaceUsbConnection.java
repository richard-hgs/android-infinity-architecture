package com.infinity.architecture.utils.usb;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

public interface InterfaceUsbConnection {
    void onDeviceEncontrado(UsbDevice device, UsbManager usbManager);
    void onDeviceNaoEncontrado();
    void onPermissaoNegada();
}
