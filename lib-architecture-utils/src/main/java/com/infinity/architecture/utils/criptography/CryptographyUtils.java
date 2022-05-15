package com.infinity.architecture.utils.criptography;


import android.util.Base64;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

public class CryptographyUtils {

    // VARIAVEIS
    private static final String UNICODE_FORMAT = "UTF8";
    public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
    private KeySpec ks;
    private SecretKeyFactory skf;
    private Cipher cipher;
    byte[] arrayBytes;
    private String myEncryptionKey;
    private String myEncryptionScheme;
    SecretKey key;

    // CONSTRUTOR
    public CryptographyUtils() throws Exception {
        myEncryptionKey = "ritech77x7*0123437546123abrusjydeqzcmxnsia128shx28hax82";
        myEncryptionScheme = DESEDE_ENCRYPTION_SCHEME;
        arrayBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
        ks = new DESedeKeySpec(arrayBytes);
        skf = SecretKeyFactory.getInstance(myEncryptionScheme);
        cipher = Cipher.getInstance(myEncryptionScheme);
        key = skf.generateSecret(ks);
    }

    // CRIPTOGRAFA A STRING
    public String criptografarString(String unencryptedString) {
        String encryptedString = null;
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] plainText = unencryptedString.getBytes(UNICODE_FORMAT);
            byte[] encryptedText = cipher.doFinal(plainText);
            encryptedString = new String(Base64.encode(encryptedText, Base64.DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptedString;
    }

    // DESCRIPTOGRAFA A STRING
    public String descriptografarString(String encryptedString) {
        String decryptedText=null;
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] encryptedText = Base64.decode(encryptedString, Base64.DEFAULT);
            byte[] plainText = cipher.doFinal(encryptedText);
            decryptedText= new String(plainText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptedText;
    }

    // DESCRIPTOGRAFAR O TEXTO DO ARQUIVO SELECIONADO
    public void decriptTxtFile(File file) throws Exception {
        // LE O TEXTO DO ARQUIVO
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuilder stringBuilderTxtFile = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilderTxtFile.append(line);
        }

        bufferedReader.close();
        fileReader.close();

        // ESCREVE NO ARQUIVO
        File file2 = new File("cache_decripted.txt");
        FileWriter fileWriter = new FileWriter(file2);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(descriptografarString(stringBuilderTxtFile.toString()));
        bufferedWriter.close();
        fileWriter.close();
    }

    // RETORNA STRING DESCRIPTOGRAFADA DO ARQUIVO SELECIONADO
    public String decriptTxtFileToStr(File file) throws Exception {
        // LE O TEXTO DO ARQUIVO
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuilder stringBuilderTxtFile = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilderTxtFile.append(line);
        }

        bufferedReader.close();
        fileReader.close();

        return descriptografarString(stringBuilderTxtFile.toString());
    }
}
