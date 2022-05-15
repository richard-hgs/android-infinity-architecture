package com.infinity.architecture.utils.nfc.android;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.nio.charset.Charset;

public class NfcHelper {
    // TAG
    private static final String TAG = "NfcHelper";

    // CONTEXT
    private Activity activity;
    private Context context;

    // LISTENER
    private NfcListener nfcListener;

    // NFC ADAPTER
    private NfcAdapter nfcAdapter;

    // VARIAVEL QUE SALVA ULTIMO CARTAO RECEBIDO
    private Ndef ndef;

    private String readedTextNfc;

    // CONSTRUTORES
    public NfcHelper(Activity activity, Context context, NfcAdapter nfcAdapter, NfcListener nfcListener) {
        this.activity = activity;
        this.context = context;
        this.nfcAdapter = nfcAdapter;
        this.nfcListener = nfcListener;
    }

    // REALIZA A LEITURA DO CARTAO
    @SuppressLint("MissingPermission")
    private String lerTextoNfc() {
        String message = null;
        try {
            if (ndef != null) {
                ndef.connect();
                NdefMessage ndefMessage = ndef.getNdefMessage();
                message = new String(ndefMessage.getRecords()[0].getPayload());
                //Log.d(TAG, "readFromNFC: " + message);

                //Toast.makeText(activity, "read: " + message, Toast.LENGTH_SHORT).show();
                // mTvMessage.setText(message);
                ndef.close();
            }
        } catch (IOException | FormatException e) {
            e.printStackTrace();
            Toast.makeText(activity, "erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return message;
    }

    // LISTENER DE LEITURA DE NOVO CARTAO PELO APARELHO
    @SuppressLint("MissingPermission")
    public void onNewIntent(Intent intent) {
        try {
            Log.d(TAG, "onNewIntent: " + intent.getAction());

            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

            if(tag != null) {
                //Toast.makeText(activity, "NfcDetectado", Toast.LENGTH_SHORT).show();
                ndef = Ndef.get(tag);
                //LogHelper.file(FileHelper.DEFAULT_LOG_FILE, "tag: " + (ndef != null ? ndef.toString() : null), "http://192.168.0.6:80/projetos/buypoint_cielo/backup.php", true);
                if (ndef != null) {
                    // readFromNFC(ndef);

                    readedTextNfc = lerTextoNfc();

                    nfcListener.onCardDetected(this);
                } else { // CASO TENHA ACONTECIDO ALGUM ERRO
                    try {
                        // RESETA O CARTAO NFC
                        NdefFormatable ndefFormatable = NdefFormatable.get(tag);
                        if (ndefFormatable != null) {
                            // initialize tag with new NDEF message
                            try {
                                ndefFormatable.connect();
                                NdefRecord mimeRecord = NdefRecord.createMime("text/plain", "Hello World".getBytes(Charset.forName("US-ASCII")));
                                ndefFormatable.format(new NdefMessage(mimeRecord));
                            } finally {
                                try {
                                    ndefFormatable.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // LISTENER DE INSTANCIA DE TIPOS DE LEITURAS DE CARTOES
    @SuppressLint("MissingPermission")
    public void onResume(Class classe) {
        try {
            IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
            IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
            IntentFilter techDetected = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
            IntentFilter[] nfcIntentFilter = new IntentFilter[]{techDetected, tagDetected, ndefDetected};

            PendingIntent pendingIntent = PendingIntent.getActivity(
                    context, 0, new Intent(context, classe).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
            if (nfcAdapter != null) {
                nfcAdapter.enableForegroundDispatch(activity, pendingIntent, nfcIntentFilter, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("MissingPermission")
    public void onPause() {
        try {
            if(nfcAdapter!= null) {
                nfcAdapter.disableForegroundDispatch(activity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getReadedTextNfc() {
        return readedTextNfc;
    }
}
