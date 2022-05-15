package com.infinity.architecture.utils.nfc.usb;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;

import com.acs.smartcard.Reader;
import com.acs.smartcard.ReaderException;
import com.infinity.architecture.utils.string.StringUtils;
import com.infinity.architecture.utils.treemap.TreeMapHelper;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


public class NfcUsbHelper {
    private static final String TAG = "NfcUsbHelper";
    private Activity activity;

    // NFC USB
    private NfcAdapter mNfcAdapter;
    private UsbManager mUsbManager;
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private Reader mReader;
    private ArrayAdapter<String> mReaderAdapter;

    // Total de espaço disponível para gravacao
    private int tamanhoCartaoNfc = 720; // Bytes
    private int tamanhoCartaoEmUso = -1; // Bytes

    private boolean aborted = false;

    // CONSTANTES
    public static final String PROTOCOLO_TODOS = "*";
    public static final String PROTOCOLO_T0 = "T=0";
    public static final String PROTOCOLO_T1 = "T=1";
    public static final String PROTOCOLO_TCL = "T=CL";

    public static final Byte APDU_TIPO_AUTENTICACAO_A = 0x60;
    public static final Byte APDU_TIPO_AUTENTICACAO_B = 0x61;
    public static final Byte APDU_TIPO_CHAVE_1 = 0x00;
    public static final Byte APDU_TIPO_CHAVE_2 = 0x01;

    public static Map<Byte,Byte> mapBlockTrailer = new TreeMapHelper<>(
            new HashMap<Byte, Byte>() {{
                put((byte) 0x04,(byte) 0x07); // trailerBlock 0x07
                put((byte) 0x05,(byte) 0x07); // trailerBlock 0x07
                put((byte) 0x06,(byte) 0x07); // trailerBlock 0x07

                put((byte) 0x08,(byte) 0x0B); // trailerBlock 0x0B
                put((byte) 0x09,(byte) 0x0B); // trailerBlock 0x0B
                put((byte) 0x0A,(byte) 0x0B); // trailerBlock 0x0B

                put((byte) 0x0C,(byte) 0x0F); // trailerBlock 0x0F
                put((byte) 0x0D,(byte) 0x0F); // trailerBlock 0x0F
                put((byte) 0x0E,(byte) 0x0F); // trailerBlock 0x0F

                put((byte) 0x10,(byte) 0x13); // trailerBlock 0x13
                put((byte) 0x11,(byte) 0x13); // trailerBlock 0x13
                put((byte) 0x12,(byte) 0x13); // trailerBlock 0x13

                put((byte) 0x14,(byte) 0x17); // trailerBlock 0x17
                put((byte) 0x15,(byte) 0x17); // trailerBlock 0x17
                put((byte) 0x16,(byte) 0x17); // trailerBlock 0x17

                put((byte) 0x18,(byte) 0x1B); // trailerBlock 0x1B
                put((byte) 0x19,(byte) 0x1B); // trailerBlock 0x1B
                put((byte) 0x1A,(byte) 0x1B); // trailerBlock 0x1B

                put((byte) 0x1C,(byte) 0x1F); // trailerBlock 0x1F
                put((byte) 0x1D,(byte) 0x1F); // trailerBlock 0x1F
                put((byte) 0x1E,(byte) 0x1F); // trailerBlock 0x1F

                put((byte) 0x20,(byte) 0x23); // trailerBlock 0x23
                put((byte) 0x21,(byte) 0x23); // trailerBlock 0x23
                put((byte) 0x22,(byte) 0x23); // trailerBlock 0x23

                put((byte) 0x24,(byte) 0x27); // trailerBlock 0x27
                put((byte) 0x25,(byte) 0x27); // trailerBlock 0x27
                put((byte) 0x26,(byte) 0x27); // trailerBlock 0x27

                put((byte) 0x28,(byte) 0x2B); // trailerBlock 0x2B
                put((byte) 0x29,(byte) 0x2B); // trailerBlock 0x2B
                put((byte) 0x2A,(byte) 0x2B); // trailerBlock 0x2B

                put((byte) 0x2C,(byte) 0x2F); // trailerBlock 0x2F
                put((byte) 0x2D,(byte) 0x2F); // trailerBlock 0x2F
                put((byte) 0x2E,(byte) 0x2F); // trailerBlock 0x2F

                put((byte) 0x30,(byte) 0x33); // trailerBlock 0x33
                put((byte) 0x31,(byte) 0x33); // trailerBlock 0x33
                put((byte) 0x32,(byte) 0x33); // trailerBlock 0x33

                put((byte) 0x34,(byte) 0x37); // trailerBlock 0x37
                put((byte) 0x35,(byte) 0x37); // trailerBlock 0x37
                put((byte) 0x36,(byte) 0x37); // trailerBlock 0x37

                put((byte) 0x38,(byte) 0x3B); // trailerBlock 0x3B
                put((byte) 0x39,(byte) 0x3B); // trailerBlock 0x3B
                put((byte) 0x3A,(byte) 0x3B); // trailerBlock 0x3B

                put((byte) 0x3C,(byte) 0x3F); // trailerBlock 0x3F
                put((byte) 0x3D,(byte) 0x3F); // trailerBlock 0x3F
                put((byte) 0x3E,(byte) 0x3F); // trailerBlock 0x3F
            }}
    ).setComparator(new Comparator<Byte>() {
        @Override
        public int compare(Byte o1, Byte o2) {
            return o1.compareTo(o2);
        }
    }).getSortedMap();

    // QUANDO RESPOSTA DA PERMISSAO É RECIBIDA
    private final BroadcastReceiver usbPermissionReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // SE A AÇÃO FOR DO TIPO PERMISSAO USB
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    // RECEBE O DISPOSITIVO
                    UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    // VERIFICA SE PERMISSAO FOI GARANTIDA
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if(device != null) {

                            // CHAMA O RECEIVER USB ATTACHED
                            Log.d(TAG, "Opening device: " + device.getDeviceName() + "...");

                            //nfcUsbOpenConn(device);

                            //usbAttachedReceiver.onReceive(context, intent);

                            // APOS PERMISSAO USA O LISTENER DE DISPOSITIVO ATTACHED
                            usbAttachedReceiver.onReceive(context, intent);
                        }
                    } else {
                        Log.d(TAG, "permission denied for device " + device);

                        if (abstractListenerNfcUsb != null) {
                            abstractListenerNfcUsb.onUsbPermissionDenied();
                        }
                    }
                }
            }
        }
    };

    // QUANDO UM DISPOSITIVO USB É CONECTADO
    private BroadcastReceiver usbAttachedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // RECEBE O DISPOSITIVO
                            UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                            // INTENT REQUISITA PERMISSAO
                            PendingIntent mPermissionIntent = PendingIntent.getBroadcast(activity, 0, new Intent(ACTION_USB_PERMISSION), 0);

                            Log.d(TAG, "device: " + device);

                            // VERIFICA SE O DISPOSITIVO TEM PERMISSAO USB
                            if (mUsbManager != null) {
                                if (!mUsbManager.hasPermission(device)) { // NAO POSSUI PERMISSAO USB
                                    // REQUISITA A PERMISSAO
                                    mUsbManager.requestPermission(device, mPermissionIntent);
                                } else { // PERMISSAO USB ACEITA
                                    if (device != null) {
                                        // LISTENER DO LEITOR DO CARTAO
                                        mReader = new Reader(mUsbManager);
                                        mReader.setOnStateChangeListener(new Reader.OnStateChangeListener() {
                                            @Override
                                            public void onStateChange(int slotNum, int prevState, int currState) {
                                                Log.d(TAG, "slotNum: " + slotNum + " - prevState: " + NfcUsbUtils.nfcUsbStateToString(prevState) + " - currState " + NfcUsbUtils.nfcUsbStateToString(currState));

                                                if (aborted) {
                                                    Log.d(TAG, "usb nfc reading Aborted");
                                                    return;
                                                }

                                                if (abstractListenerNfcUsb != null) {
                                                    abstractListenerNfcUsb.onCardStateChange(slotNum, prevState, currState);
                                                }

                                                // Cartão inserido
                                                if (currState == Reader.CARD_PRESENT) {
                                                    nfcUsbPowerUpCard();
                                                }
                                            }
                                        });

                                        Log.d(TAG, "Opening device: " + device.getDeviceName() + "... " + device.getVendorId());

                                        // CONECTA COM O LEITOR DE CARTAO
                                        nfcUsbOpenConn(device);
                                    }
                                }
                            }

                            //Toast.makeText(context, "usbAttached", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    // Listener
    private AbstractListenerNfcUsb abstractListenerNfcUsb;

    // Construtor instancia
    public NfcUsbHelper(Activity activity, AbstractListenerNfcUsb abstractListenerNfcUsb) {
        this.activity = activity;
        this.abstractListenerNfcUsb = abstractListenerNfcUsb;
    }

    // Recebe o leitor acr122u usb
    public void getAcr122Usb() {
        // RECEBE O GERENCIADOR DO USB
        mUsbManager = (UsbManager) activity.getSystemService(Context.USB_SERVICE);

        Log.d(TAG, "getAcr122Usb - usbManager: " + mUsbManager);

        if (mUsbManager != null) {
            // Recebe os dispositivos usb conectados anteriormente ao aparelho
            HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();

            Log.d(TAG, "deviceList.size() -> " + deviceList.size());

            if (deviceList.size() == 0) {
                abstractListenerNfcUsb.onDeviceNotFound();
                return;
            }

            // EXIBE DISPOSITIVOS USB
            for (Map.Entry entries : deviceList.entrySet()) {
                Log.d(TAG, entries.getKey() + ": " + entries.getValue());
            }

            // CONECTA AO PRIMEIRO USB
            for (Map.Entry<String, UsbDevice> entries : deviceList.entrySet()) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putParcelable(UsbManager.EXTRA_DEVICE, entries.getValue());
                intent.putExtras(bundle);
                usbAttachedReceiver.onReceive(activity, intent);
                break;
            }
        }

        IntentFilter permissionFilter = new IntentFilter(ACTION_USB_PERMISSION);
        activity.registerReceiver(usbPermissionReceiver, permissionFilter);

        IntentFilter filterUsbAttached = new IntentFilter(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        activity.registerReceiver(usbAttachedReceiver, filterUsbAttached);


        /*if (mUsbManager != null) {
            mReader = new Reader(mUsbManager);
            mReader.setOnStateChangeListener(new Reader.OnStateChangeListener() {
                @Override
                public void onStateChange(int slotNum, int prevState, int currState) {
                    Log.d(TAG, "slotNum: " + slotNum + " - prevState: " + NfcUsbUtils.nfcUsbStateToString(prevState) + " - currState " + NfcUsbUtils.nfcUsbStateToString(currState));

                    if (abstractListenerNfcUsb != null) {
                        abstractListenerNfcUsb.onCardStateChange(slotNum, prevState, currState);
                    }

                    // Cartão inserido
                    if (currState == Reader.CARD_PRESENT){
                        nfcUsbPowerUpCard();
                    }
                }
            });
        }

        // Recebe os dispositivos usb do aparelho
        HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
        for (Map.Entry entries : deviceList.entrySet()) {
            Log.d(TAG, entries.getKey() + ": " + entries.getValue());
        }*/

        // Se algum dispositivo encontrado na usb
        /*if (deviceList.size() > 0) {
            // Recebe o primeiro dispositivo
            Map.Entry entry = deviceList.entrySet().iterator().next();
            UsbDevice device = deviceList.get(entry.getKey());
        } else {
            Log.d(TAG, "Nenhum dispositivo encontrado, tente novamente");

            if (this.abstractListenerNfcUsb != null) {
                abstractListenerNfcUsb.onDeviceNotFound();
            }
        }*/
    }

    // Carrega o cartao usb (passo 1 antes de ler e escrever)
    private void nfcUsbPowerUpCard() {
        try {
            byte[] atr = mReader.power(0, Reader.CARD_WARM_RESET);
            Log.d(TAG, "powerUpCard: " + NfcUsbUtils.bin2hex(atr));

            nfcUsbProtocol();
        } catch (ReaderException e) {
            e.printStackTrace();
        }
    }

    // Abre a conexao com o leitor usb
    private void nfcUsbOpenConn(UsbDevice device) {
        // Conecta ao dispositivo
        try {
            mReader.open(device);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    // Seta o protocolo cartao usb (passo 2 antes de ler e escrever)
    private void nfcUsbProtocol() {
        try {
            mReader.setProtocol(0, Reader.PROTOCOL_T0 | Reader.PROTOCOL_T1);

            try {
                Thread.sleep(500);

                if (this.abstractListenerNfcUsb != null) {
                    this.abstractListenerNfcUsb.onCardInserted(this);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (ReaderException e) {

            e.printStackTrace();
        }
    }

    // Transmitir dados cartao usb apdu
    private byte[] nfcUsbSendApduData(byte[] command) {
        byte[] response = new byte[50];
        int responseLength;
        byte[] newResponse = new byte[16];
        try {
            responseLength = mReader.transmit(0, command, command.length, response, response.length);

            for (int i=0; i<16; i++) {
                newResponse[i] = response[i];
            }
        } catch (ReaderException e) {
            e.printStackTrace();
        }

        return newResponse;
    }

    // Transmiti dados cartao usb controlado
    private void nfcUsbSendApduDataControled() {
        // Get firmware version (ACR122)
        byte[] command = { (byte)0xFF, (byte)0x00, (byte)0x48, (byte)0x00, (byte)0x08 };
        byte[] response = new byte[300];
        int responseLength;
        try {
            responseLength = mReader.control(0, Reader.IOCTL_CCID_ESCAPE, command,
                    command.length, response, response.length);
            Log.d(TAG, "responseLength1: " + responseLength);
            Log.d(TAG, "resp2: " + new String(response, StandardCharsets.US_ASCII).substring(0, responseLength));
        } catch (ReaderException e) {
            e.printStackTrace();
        }
    }

    public String lerTexoNfc() throws Exception {
        return lerTexoNfc(null);
    }

    // FUNCOES UTEIS
    // Le o texto salvo no NfcCard
    public String lerTexoNfc(@Nullable Integer singleSlotNumToRead) throws Exception {
        String strResposta = "";

        ArrayList<Byte> dadosRecebidos = new ArrayList<>();

        if (singleSlotNumToRead != null) {
            String slotNumHex = Integer.toString(singleSlotNumToRead, 16);
            byte slotNumByte = singleSlotNumToRead.byteValue();

            Log.d(TAG, "slotNum: " + singleSlotNumToRead + " - slotNumHex: 0x" + slotNumHex);

            if (mapBlockTrailer.containsKey(slotNumByte)) {
                nfcUsbSendApduData(NfcUsbUtils.gerarApduCommandAuth(slotNumByte, APDU_TIPO_AUTENTICACAO_B, APDU_TIPO_CHAVE_2));

                System.out.print("key: 0x" + NfcUsbUtils.bin2hex(new byte[] {(byte) slotNumByte}) + " - value: 0x" + NfcUsbUtils.bin2hex(new byte[]{(byte) slotNumByte}));

                byte[] resposta = nfcUsbSendApduData(NfcUsbUtils.gerarApduCommandRead(slotNumByte));

                System.out.println(" :" + NfcUsbUtils.bin2hex(resposta));

                dadosRecebidos.addAll(Arrays.asList(NfcUsbUtils.bytesToBytes(resposta)));

                byte[] array = NfcUsbUtils.arrayListToByteArray(dadosRecebidos);

                strResposta = new String(array, StandardCharsets.UTF_8);
            }
        } else {

            for (Map.Entry cursor : mapBlockTrailer.entrySet()) {
                nfcUsbSendApduData(NfcUsbUtils.gerarApduCommandAuth((byte) cursor.getKey(), APDU_TIPO_AUTENTICACAO_B, APDU_TIPO_CHAVE_2));

                //System.out.print("key: 0x" + bin2hex(new byte[] {(byte)cursor.getKey()}) + " - value: 0x" + bin2hex(new byte[]{(byte)cursor.getValue()}));

                byte[] resposta = nfcUsbSendApduData(NfcUsbUtils.gerarApduCommandRead((byte) cursor.getKey()));

                //System.out.println(" :" + bin2hex(resposta));

                dadosRecebidos.addAll(Arrays.asList(NfcUsbUtils.bytesToBytes(resposta)));

                // Assim que encontrar o final do texto finaliza a leitura
                if (dadosRecebidos.indexOf((byte) 0xFE) != -1) {
                    break;
                }
            }

            // Remove o cabeçalho
            dadosRecebidos.subList(0, 11).clear();

            // Remove o ultimo digito fe e retorna o array com o texto
            if (dadosRecebidos.indexOf((byte) 0xFE) != -1) {
                byte[] array = NfcUsbUtils.arrayListToByteArray(dadosRecebidos.subList(0, dadosRecebidos.indexOf((byte) 0xFE)));

                strResposta = new String(array, StandardCharsets.UTF_8);

                // CORRIGE FORMATACAO DA NFC QUANDO ESCRITA PELO ANDROID
                if (strResposta.substring(0, 6).contains("/plain")) {
                    strResposta = strResposta.substring(6);
                }

                tamanhoCartaoEmUso = 11;
                tamanhoCartaoEmUso = tamanhoCartaoEmUso + array.length;
            }
        }

        return strResposta;
    }

    // Gera o comando que escreve na memoria
    public void escreverTextoNfc(String texto) throws Exception {
        escreverTextoNfc(texto, null);
    }

    public void escreverTextoNfc(HashMap<Integer, String> slotsDataToWrite) throws Exception {
        escreverTextoNfc(null, slotsDataToWrite);
    }

    public void escreverTextoNfc(@Nullable String texto, @Nullable HashMap<Integer, String> slotsDataToWrite) throws Exception {
        if (texto != null) {
            Byte[] inicializaTexto = new Byte[]{ // Cabeçalho da tag nfc
                    (byte) 0x00, // TNF (Type Name Format Field - Indica o tipo (Text))
                    (byte) 0x00, // TNF
                    (byte) 0x03, // TNF
                    (byte) (texto.length() + 7), // IL (Length of Text Byte) (texto.lenth() + 7)
                    (byte) 0xD1, // SR (Short bit read) (0xD1 - Padrao)
                    (byte) 0x01, // CF (Chunk Flag - indica se e o primeiro registro ou o do meio) (0x01 - Padrao)
                    (byte) (texto.length() + 3), // ME (Message End - indica se o registro e o fim da mensagem) (texto.length() + 3)
                    (byte) 0x54, // VALOR FIXO
                    (byte) 0x02, // VALOR FIXO
                    (byte) 0x65, // VALOR FIXO
                    (byte) 0x6E, // TIPO DE LINGUAGEM (EN)
            };

            // Finaliza essa string
            byte finalizaTexto = (byte) 0xFE;

            ArrayList<Byte> listaDadosEnviar = new ArrayList<>();
            listaDadosEnviar.addAll(Arrays.asList(inicializaTexto));
            listaDadosEnviar.addAll(Arrays.asList(NfcUsbUtils.bytesToBytes(texto.getBytes())));
            listaDadosEnviar.add(finalizaTexto);

            int countBytesTotal = 0;

            // Para cada bloco de dados de 16 bytes
            for (Map.Entry<Byte, Byte> entry : mapBlockTrailer.entrySet()) {
                // Variavel fixa de envio de dados no blocoNumber
                byte[] fixedDataToWrite = new byte[]{
                        (byte) 0xFF,            // Class
                        (byte) 0xD6,            // INS
                        (byte) 0x00,            // P1
                        (byte) entry.getKey(),  // P2 (blockNumber)
                        (byte) 0x10,            // Data Length (16 bytes)
                        // DADOS 16 BYTES
                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00
                };

                // Recebe cada bloco e armazena os 16bytes em um array
                byte[] data16byteToWrite = new byte[16];
                int countBytes = 0;
                for (int i = countBytesTotal; i < listaDadosEnviar.size(); i++) {
                    if (countBytes < 16) {
                        data16byteToWrite[i - countBytesTotal] = listaDadosEnviar.get(i);
                        countBytes++;
                    } else {
                        break;
                    }
                }

                // Insere os dados ao bloco fixo para ser enviado
                for (int i = 0; i < data16byteToWrite.length; i++) {
                    fixedDataToWrite[i + 5] = data16byteToWrite[i];
                }

                //System.out.println("bloco: " + bin2hex(new byte[]{entry.getKey()}) + " - data16ByteToWrite: " + bin2hex(data16byteToWrite));


                //System.out.println("bloco: " + bin2hex(new byte[]{entry.getKey()}) + " - fixedDataToWrite: " + bin2hex(fixedDataToWrite));

                // Autentica o cartao
                nfcUsbSendApduData(
                    NfcUsbUtils.gerarApduCommandAuth(entry.getKey(), APDU_TIPO_AUTENTICACAO_B, APDU_TIPO_CHAVE_2)
                );

                // Envia os dados
                nfcUsbSendApduData(fixedDataToWrite);

                // Soma a quantidade já enviada
                if (countBytesTotal + 16 < listaDadosEnviar.size()) {
                    countBytesTotal = countBytesTotal + 16;
                } else {
                    countBytesTotal = listaDadosEnviar.size();
                }

                // Verifica se a quantidade enviada é igual a quantidade total a ser enviada e finaliza o loop
                if (countBytesTotal == listaDadosEnviar.size()) {
                    break;
                }
            }
        }

        if (slotsDataToWrite != null) {
            for (Map.Entry<Integer, String> entry : slotsDataToWrite.entrySet()) {
                Integer key = entry.getKey();
                String currentValue = entry.getValue();

                String slotNumHex = Integer.toString(key, 16);
                byte slotNumByte = key.byteValue();

                Log.d(TAG, "slotNum: " + key + " - slotNumHex: 0x" + slotNumHex);

                if (mapBlockTrailer.containsKey(slotNumByte)) {
                    String dataToWrite = "0000000000000000";

                    if (StringUtils.isDigitsOnly(currentValue)) {
                        // Is number
                        dataToWrite = StringUtils.leadingChars(currentValue, "0", 16);
                    } else {
                        // Is text
                        dataToWrite = StringUtils.trailingChars(currentValue, " ", 16);
                    }

                    if (dataToWrite.length() > 16) {
                        dataToWrite = dataToWrite.substring(0, 17);
                    }

                    if (dataToWrite.length() != 16) {
                        Log.d(TAG, "Não foi possível gravar os dados, tamanho '" + dataToWrite.length() + "' bytes dos dados diferente de 16 bytes...");
                        return;
                    }

                    // Variavel fixa de envio de dados no blocoNumber
                    byte[] fixedDataToWrite = new byte[]{
                            (byte) 0xFF,            // Class
                            (byte) 0xD6,            // INS
                            (byte) 0x00,            // P1
                            (byte) key.byteValue(), // P2 (blockNumber)
                            (byte) 0x10,            // Data Length (16 bytes)
                            // DADOS 16 BYTES
                            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00
                    };

                    byte[] data16byteToWrite = dataToWrite.getBytes();

                    // Insere os dados ao bloco fixo para ser enviado
                    for (int i = 0; i < data16byteToWrite.length; i++) {
                        fixedDataToWrite[i + 5] = data16byteToWrite[i];
                    }

                    // Autentica o cartao
                    nfcUsbSendApduData(
                            NfcUsbUtils.gerarApduCommandAuth(key.byteValue(), APDU_TIPO_AUTENTICACAO_B, APDU_TIPO_CHAVE_2)
                    );

                    // Envia os dados
                    nfcUsbSendApduData(fixedDataToWrite);
                }
            }
        }
    }

    // Retorna em bytes o tamanho em uso do nfc
    public int getNfcUsoDeMemoria() throws Exception {
        int tamanhoAtualCartaoEmUso = -1;

        if (tamanhoCartaoEmUso == -1) {
            ArrayList<Byte> dadosRecebidos = new ArrayList<>();

            for (Map.Entry cursor : mapBlockTrailer.entrySet()) {
                nfcUsbSendApduData(NfcUsbUtils.gerarApduCommandAuth((byte) cursor.getKey(), APDU_TIPO_AUTENTICACAO_B, APDU_TIPO_CHAVE_2));

                byte[] resposta = nfcUsbSendApduData(NfcUsbUtils.gerarApduCommandRead((byte) cursor.getKey()));

                dadosRecebidos.addAll(Arrays.asList(NfcUsbUtils.bytesToBytes(resposta)));

                // Assim que encontrar o final do texto finaliza a leitura
                if (dadosRecebidos.indexOf((byte) 0xFE) != -1) {
                    break;
                }
            }

            // Remove o cabeçalho
            dadosRecebidos.subList(0, 11).clear();

            // Remove o ultimo digito fe e retorna o array com o texto
            if (dadosRecebidos.indexOf((byte) 0xFE) != -1) {
                byte[] array = NfcUsbUtils.arrayListToByteArray(dadosRecebidos.subList(0, dadosRecebidos.indexOf((byte) 0xFE)));

                tamanhoAtualCartaoEmUso = 11;
                tamanhoAtualCartaoEmUso = tamanhoAtualCartaoEmUso + array.length;
                //strResposta = new String(array, StandardCharsets.UTF_8);
            }
        } else {
            tamanhoAtualCartaoEmUso = tamanhoCartaoEmUso;
        }

        tamanhoCartaoEmUso = -1;

        if (tamanhoAtualCartaoEmUso == -1){
            return 0;
        } else {
            return tamanhoAtualCartaoEmUso;
        }
    }

    // DESREGISTRA OS RECEIVERS
    public void unregistReceivers() {
        try {
            Log.d(TAG, "unregisterReceivers");
            if (mReader != null) {
//                Field[] fields = mReader.getClass().getDeclaredFields();
//                UsbDeviceConnection usbDeviceConnection = null;
//                List<UsbInterface> usbInterfaceList = null;
//                for (int i=0; i< fields.length; i++) {
//                    Field fieldAt = fields[i];
//                    fieldAt.setAccessible(true);
//                    Object fieldAtValue = fieldAt.get(mReader);
//                    if (fieldAtValue != null) {
//                        if (fieldAtValue instanceof UsbDeviceConnection) {
//                            usbDeviceConnection = (UsbDeviceConnection) fieldAtValue;
//                        }
//                        String superClassName = Objects.requireNonNull(fieldAtValue.getClass().getGenericSuperclass()).getClass().getSimpleName();
//
//                        Log.d(TAG, "ReaderFieldType: " + superClassName + fieldAtValue.getClass().getSimpleName() + " - fieldName: " + fieldAt.getName());
//                    }
//                }
//
//                if (usbDeviceConnection != null) {
//                    usbDeviceConnection.close();
//                }

                mReader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        setAborted(true);

        try {
            activity.unregisterReceiver(usbAttachedReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            activity.unregisterReceiver(usbPermissionReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            activity = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isAborted() {
        return aborted;
    }

    public void setAborted(boolean aborted) {
//        try {
//            throw new Exception("TEST");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        this.aborted = aborted;
    }
}
