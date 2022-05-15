package com.infinity.architecture.utils.nfc.usb;

import com.acs.smartcard.Reader;

import java.math.BigInteger;
import java.util.List;

public class NfcUsbUtils {

    public NfcUsbUtils() {
        // Construtor
    }

    // Converte byte[] to Byte[]
    public static Byte[] bytesToBytes(byte[] bytesPrim) {
        Byte[] bytes = new Byte[bytesPrim.length];
        for (int i=0; i< bytesPrim.length; i++){
            bytes[i] = bytesPrim[i];
        }
        return bytes;
    }

    public static byte[] arrayListToByteArray(List<Byte> arrayList) {
        byte[] retorno = new byte[arrayList.size()];

        for (int i=0; i<arrayList.size(); i++){
            retorno[i] = arrayList.get(i);
        }

        return retorno;
    }

    public static byte[] bytesToBytes2(Byte[] bytes){
        byte[] bytes2 = new byte[bytes.length];
        for (int i=0; i<bytes.length; i++){
            bytes2[i] = bytes[i];
        }

        return bytes2;
    }

    // Gera o comando de autenticacao
    public static byte[] gerarApduCommandAuth(byte blockNumber, byte tipoAutenticacao, byte tipoChave){
        byte[] apduArrayAuth = {
                (byte) 0xff, // Class
                (byte) 0x86, //INS
                (byte) 0x00, //P1
                (byte) 0x00, //P2
                (byte) 0x05, //LC
                //Authentication Data bytes
                (byte) 0x01, //Version
                (byte) 0x00, //Byte 2
                (byte) blockNumber, //Block number
                (byte) tipoAutenticacao, //Key type 0x060 ou 0x061
                (byte) tipoChave  //Key number 0x00 ou 0x01
        };

        return apduArrayAuth;
    }

    // Gera o comando de leitura
    public static byte[] gerarApduCommandRead(byte blockNumber) {
        byte[] apduArrayRead = {
                (byte) 0xff, //Class
                (byte) 0xb0, //INS
                (byte) 0x00, //P1
                (byte) blockNumber, //P2 = Block number
                (byte) 0x10, // LC = Number of bytes to read (16 bytes)
        };

        return apduArrayRead;
    }

    // Convert byte para Hexadecimal
    public static String bin2hex(byte[] data) {
        return String.format("%0" + (data.length * 2) + "X", new BigInteger(1, data));
    }

    // Estados do leitor usb
    public static String nfcUsbStateToString(int state) {
        switch (state) {
            case Reader.CARD_UNKNOWN:
                return "Cartão desconhecido";
            case Reader.CARD_ABSENT:
                return "Leitor sem cartão nfc";
            case Reader.CARD_PRESENT:
                return "Leitor com cartão nfc";
            case Reader.CARD_SWALLOWED:
                return "Cartão engolido";
            case Reader.CARD_POWERED:
                return "Cartão carregado";
            case Reader.CARD_NEGOTIABLE:
                return "Cartão negociável";
            case Reader.CARD_SPECIFIC:
                return "Cartão específico";
            default:
                return "Cartão desconhecido";
        }
    }
}
