package com.infinity.architecture.utils.variables;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.infinity.architecture.utils.reflection.ReflectionUtils;

import java.lang.reflect.Array;
import java.util.Random;

public class VarUtils {
    // TAG
    private static final String TAG = "VarHelper";

    // CONVERTE INT PARA BYTE
    public static int intToByte(int number) throws Exception {
        int tmp = number & 0xff;
        if ((tmp & 0x80) == 0x80) {
            int bit = 1;
            int mask = 0;
            for(;;) {
                mask |= bit;
                if ((tmp & bit) == 0) {
                    bit <<=1;
                    continue;
                }
                int left = tmp & (~mask);
                int right = tmp & mask;
                left = ~left;
                left &= (~mask);
                tmp = left | right;
                tmp = -(tmp & 0xff);
                break;
            }
        }
        return tmp;
    }

    // CONVERT BIN_INT(0 ou 1) TO BOOLEAN
    public static boolean binIntToBoolean(int binInt) throws Exception {
        return binInt == 1;
    }

    // CONVERTE 8 BITS EM UM INT ARRAY PARA BYTE
    public static byte bitToByte(int[] bits) throws Exception {

        reverseArray(bits);

        int decimalOfBits = 0;
        for (int i=0; i<bits.length; i++) {
            decimalOfBits = decimalOfBits + (int) (Math.pow(bits[i] * 2, bits[i] > 0 ? i : 1));
        }

        return (byte) decimalOfBits;
    }

    // REVERTE O ARRAY
    public static <T> void reverseArray(T[] reverseArray) throws Exception {
        for (int i = 0; i < reverseArray.length / 2; i++) {
            T temp = reverseArray[i];
            reverseArray[i] = reverseArray[reverseArray.length - i - 1];
            reverseArray[reverseArray.length - i - 1] = temp;
        }
    }
    // REVERTE O ARRAY
    public static void reverseArray(int[] intArray) throws Exception {
        for (int i = 0; i < intArray.length / 2; i++) {
            int temp = intArray[i];
            intArray[i] = intArray[intArray.length - i - 1];
            intArray[intArray.length - i - 1] = temp;
        }
    }
    // REVERTE O ARRAY
    public static void reverseArray(byte[] byteArray) throws Exception {
        for (int i = 0; i < byteArray.length / 2; i++) {
            byte temp = byteArray[i];
            byteArray[i] = byteArray[byteArray.length - i - 1];
            byteArray[byteArray.length - i - 1] = temp;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T concat(@NonNull T first, @NonNull T ...others) {
        int size = Array.getLength(first);

        for(T arrAt : others) {
            size += Array.getLength(arrAt);
        }

        T newArr = (T) ReflectionUtils.createPrimitiveArrayOfType(first.getClass().getComponentType(), size);

        int offset = 0;


        System.arraycopy(first, 0, newArr, offset, Array.getLength(first));
        offset += Array.getLength(first);

        for(T arrAt : others) {
            System.arraycopy(arrAt, 0, newArr, offset, Array.getLength(arrAt));
            offset += Array.getLength(arrAt);
        }

        return newArr;
    }

    // CONCATENA OS BITS
    public static int[] concatBits(int[] bits1, int[]... bitsn) throws Exception {
        // RECEBE O TAMANHO FINAL DO BIT_ARRAY
        int sizeOfFinalBits = bits1.length;
        for (int[] cursor : bitsn) {
            sizeOfFinalBits = sizeOfFinalBits + cursor.length;
        }

        // VARIAVEL DO RETORNO
        int[] bitsConcatened = new int[sizeOfFinalBits];
        // ARMAZENA POS ATUAL DO BITS_CONCATENATED
        int posOfBitsConcatenated = 0;

        // ATRIBUI O PRIMEIRO VETOR DE BITS
        reverseArray(bits1);
        for (int i=0; i<bits1.length; i++) {
            bitsConcatened[posOfBitsConcatenated] = bits1[i];
            posOfBitsConcatenated++;
        }

        // ATRIBUI OS VETORES DE BITS RESTANTES
        for (int[] cursor : bitsn) {
            reverseArray(cursor);
            for (int i=0; i<cursor.length; i++) {
                bitsConcatened[posOfBitsConcatenated] = cursor[i];
                posOfBitsConcatenated++;
            }
        }

        // REVERTE O ARRAY PARA FICAR NA POSICAO DE BITS CORRETA
        reverseArray(bitsConcatened);

        return bitsConcatened;
    }

    /**
     * RECEBE LONG DE UMA STRING
     * @param value                     O VALOR INFORMADO
     * @return                          A STRING INFORMADA EM FORMATO LONG
     * @throws NumberFormatException    CASO O VALOR SEJA INVÁLIDO
     */
    public static long getNullableLong(@Nullable String value) throws NumberFormatException {
        long retorno = 0;
        if (value != null && value.length() > 0) {
            retorno = Long.parseLong(value);
        }
        return retorno;
    }

    /**
     * RECEBE INT DE UMA STRING
     * @param value                     O VALOR INFORMADO
     * @return                          A STRING INFORMADA EM FORMATO INT
     * @throws NumberFormatException    CASO O VALOR SEJA INVÁLIDO
     */
    public static int getNullableInt(@Nullable String value) throws NumberFormatException {
        int retorno = 0;
        if (value != null && value.length() > 0) {
            retorno = Integer.parseInt(value);
        }
        return retorno;
    }

    /**
     * RECEBE FLOAT DE UMA STRING
     * @param value                     O VALOR INFORMADO
     * @return                          A STRING INFORMADA EM FORMATO FLOAT
     * @throws NumberFormatException    CASO O VALOR SEJA INVÁLIDO
     */
    public static float getNullableFloat(@Nullable String value) throws NumberFormatException {
        float retorno = 0;
        if (value != null && value.length() > 0) {
            retorno = Float.parseFloat(value);
        }
        return retorno;
    }

    /**
     * RETORNA UM INTEIRO RANDOMIZADO
     * @param min   VALOR MINIMO DO INT
     * @param max   VALOR MAXIMO DO INT
     * @return      VALOR RANDOMIZADO
     */
    public static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    /**
     * RETORNA UM FLOAT RANDOMIZADO
     * @param min   VALOR MINIMO DO FLOAT
     * @param max   VALOR MAXIMO DO FLOAT
     * @return      VALOR RANDOMIZADO
     */
    public static float getRandomNumberInRange(float min, float max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random rand = new Random();

        return rand.nextFloat() * (max - min) + min;
    }

    public static <T> T ifNull(T t1, T defval) {
        return (t1 != null ? t1 : defval);
    }
}
