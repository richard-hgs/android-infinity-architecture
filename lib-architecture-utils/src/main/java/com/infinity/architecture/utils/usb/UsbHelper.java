package com.infinity.architecture.utils.usb;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class UsbHelper {
    private static final String TAG = "UsbHelper";

    private Activity activity;

    private UsbManager mUsbManager;
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";

    private AbstractUsbConnection abstractUsbConnection;

    // VARIAVEL DE TIMOUT PERMISSAO
    private boolean permissaoRequisitada = false;
    private boolean permissaoConfirmada = false;

    // CONSTRUTOR
    public UsbHelper(Activity activity, int mVendorId, AbstractUsbConnection abstractUsbConnection) throws Exception {
        this.activity = activity;
        this.abstractUsbConnection = abstractUsbConnection;

        mUsbManager = (UsbManager) activity.getSystemService(Context.USB_SERVICE);

        HashMap<String, UsbDevice> deviceList = null;
        if (mUsbManager != null) {
            // Recebe os dispositivos usb do aparelho
            deviceList = mUsbManager.getDeviceList();
            for (Map.Entry entries : deviceList.entrySet()) {
                Log.d(TAG, entries.getKey() + ": " + entries.getValue());

                //LogHelper.file(FileHelper.DEFAULT_LOG_FILE, "dispositivo: " + entries.getKey() + ": " + entries.getValue(), "http://192.168.0.25:80/projetos/buypoint_cielo/backup.php", true);
            }
        }

        // Se algum dispositivo encontrado na usb
        if (deviceList != null && deviceList.size() > 0) {
            Map.Entry entry = null;
            if (mVendorId == 0) {
                // Recebe o primeiro dispositivo
                entry = deviceList.entrySet().iterator().next();
            } else {
                // Recebe o dispositivo especifico
                for (Map.Entry cursor : deviceList.entrySet()){
                    @SuppressWarnings("SuspiciousMethodCalls")
                    UsbDevice usbDeviceTemp = deviceList.get(cursor.getKey());
                    if (usbDeviceTemp.getVendorId() == mVendorId){
                        entry = cursor;
                        break;
                    }
                }
            }

            // Dispositivo selecionado encontrado
            if (entry != null) {
                @SuppressWarnings("SuspiciousMethodCalls")
                UsbDevice device = deviceList.get(entry.getKey());

                PendingIntent mPermissionIntent = PendingIntent.getBroadcast(activity, 0, new Intent(ACTION_USB_PERMISSION), 0);
                IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
                activity.registerReceiver(mUsbReceiver, filter);

                if (mUsbManager != null) {
                    if (!mUsbManager.hasPermission(device)) { // Se não tiver a permissao de leitura de dispositivo
                        Log.d(TAG, "Requisitando permissao: " + device.getDeviceName() + "...");
                        //permissaoRequisitada = true;

                        // Requisita a permissao
                        mUsbManager.requestPermission(device, mPermissionIntent);
                    } else {
                        if(device != null) {
                            //call method to set up device communication
                            Log.d(TAG, "Opening device: " + device.getDeviceName() + "...");

                            if (this.abstractUsbConnection != null) {
                                abstractUsbConnection.onDeviceEncontrado(device, mUsbManager);
                            }
                        }
                    }
                }
            } else if (this.abstractUsbConnection != null) { // Dispositivo nao encontrado
                Log.d(TAG, "Dispositivo nao encontrado: ");
                this.abstractUsbConnection.onDeviceNaoEncontrado();
            }
        } else {
            Log.d(TAG, "Nenhum dispositivo encontrado, tente novamente");
            if (this.abstractUsbConnection != null) {
                abstractUsbConnection.onDeviceNaoEncontrado();
            }
        }
    }

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                //permissaoConfirmada = true;
                synchronized (this) {
                    UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if(device != null) {
                            // call method to set up device communication
                            Log.d(TAG, "Opening device: " + device.getDeviceName() + "...");
                            if (UsbHelper.this.abstractUsbConnection != null) {
                                abstractUsbConnection.onDeviceEncontrado(device, mUsbManager);
                            }
                        }
                    } else {
                        Log.d(TAG, "permission denied for device " + device);
                        if (UsbHelper.this.abstractUsbConnection != null) {
                            abstractUsbConnection.onPermissaoNegada();
                        }
                    }
                }
            } else if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
                UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
            }
        }
    };

    public void unregisterReceiver() {
        try {
            activity.unregisterReceiver(mUsbReceiver);
        } catch (Exception e){
            // Nao faz nada
        }
    }

}
