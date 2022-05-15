package com.infinity.architecture.utils.string;

import android.text.Editable;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class StringUtils {

    public static <T> T ifNull(T obj1, @NonNull T obj2) {
        if (obj1 != null) {
            return obj1;
        } else {
            return obj2;
        }
    }

    public static String leadingChars(String str, String lead, int size) {
        StringBuilder leadStr = new StringBuilder("" + str);
        while(leadStr.length() < size) {
            leadStr.insert(0, lead);
        }
        return leadStr.toString();
    }

    public static String trailingChars(String str, String lead, int size) {
        StringBuilder leadStr = new StringBuilder("" + str);
        while(leadStr.length() < size) {
            leadStr.append(lead);
        }
        return leadStr.toString();
    }

    public static String trimLeadingChars(String str, char lead) {
        StringBuilder leadStr = new StringBuilder("" + str);

        boolean continueSearch = true;
        while (continueSearch) {
            if (leadStr.charAt(0) == lead) {
                leadStr.deleteCharAt(0);
            } else {
                continueSearch = false;
            }
        }

        return leadStr.toString();
    }

    public static List<String> splitEqually(String text, int size) {
        // Give the list the right capacity to start with. You could use an array
        // instead if you wanted.
        List<String> ret = new ArrayList<String>((text.length() + size - 1) / size);

        for (int start = 0; start < text.length(); start += size) {
            ret.add(text.substring(start, Math.min(text.length(), start + size)));
        }
        return ret;
    }

    public static boolean containsIgnoreCase(String str, String searchStr) {
        if(str == null || searchStr == null) return false;

        final int length = searchStr.length();
        if (length == 0)
            return true;

        for (int i = str.length() - length; i >= 0; i--) {
            if (str.regionMatches(true, i, searchStr, 0, length))
                return true;
        }
        return false;
    }

    /**
     * RETORNA TODAS AS POSICOES PARA A STRING INFORMADA
     * @param word      STRING A PROCURAR AS POSICOES
     * @param guess     CARACTERES A SEREM PROCURADOS NA STRING
     * @return          LISTA CONTENDO TODAS AS POSICOES
     */
    public static ArrayList<Integer> allIndexOf(String word, String guess) {
        ArrayList<Integer> allIndexes = new ArrayList<>();
        for (int index = word.indexOf(guess);
             index >= 0;
             index = word.indexOf(guess, index + 1))
        {
            System.out.println(index);

            allIndexes.add(index);
        }

        return allIndexes;
    }

    public static int substrInStrCount(String str, String subStr) {
        if (str != null && subStr != null) {
            return str.split(subStr, -1).length-1;
        }
        return 0;
    }

    @Nullable
    public static String editableToString(Editable editable) {
        if (editable != null) {
            return editable.toString();
        } else {
            return null;
        }
    }

    public static String encodeFormUrlData(HashMap<String, String> formUrlData) throws Exception {
        StringBuilder formUrlEncodedStr = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> formUrlEncodedAt : formUrlData.entrySet()) {
            if(!first) {
                formUrlEncodedStr.append("&");
            } else {
                first = false;
            }
            formUrlEncodedStr.append(URLEncoder.encode(formUrlEncodedAt.getKey().trim(), "UTF-8")).append("=").append(URLEncoder.encode(formUrlEncodedAt.getValue().trim(), "UTF-8"));
        }
        return formUrlEncodedStr.toString();
    }

    public static boolean isDigitsOnly(String string) {
        return TextUtils.isDigitsOnly(string);
    }

    // Alinha texto direita e preço à esquerda
    public static String spanAlinhado(String strInicio, String strFim, String spanChar, int spanCharsLength, int qtdeLinhasPula) {
        StringBuilder stringBuilder = new StringBuilder();

        int minMiddleCharsLen = 0;
        int newMinMiddleCharsLen = minMiddleCharsLen > 0 ? minMiddleCharsLen - 1 : 0;

        if (strInicio.length() + strFim.length() <= spanCharsLength) { // cabe em uma linha
            stringBuilder.append(strInicio);

            int spanLength = spanCharsLength - strInicio.length() - strFim.length();

            for (int i=0; i<spanLength; i++){
                stringBuilder.append(spanChar);
            }

            stringBuilder.append(strFim);

            if (qtdeLinhasPula > 0) {
                stringBuilder.append("\r");
                for (int i=0; i<qtdeLinhasPula; i++){
                    stringBuilder.append("\n");
                }
            }
        } else {
            // Doesn't fit the length, needs to be cut
            int trimType = 0;
            String strStartTrimmed = strInicio;
            String strEndTrimmed = strFim;

            if (trimType == 1) {
                // Trim start
                if (strInicio.length() >= spanCharsLength) {
                    strStartTrimmed = strInicio.substring(0, spanCharsLength -3);
                }

                // Trim end
                strEndTrimmed = strFim.substring(0, (spanCharsLength - strStartTrimmed.length()) - (1 + newMinMiddleCharsLen));
            } else {
                // Trim end
                if (strEndTrimmed.length() >= spanCharsLength) {
                    strEndTrimmed = strFim.substring(0, spanCharsLength -3);
                }

                // Trim start
                strStartTrimmed = strInicio.substring(0, (spanCharsLength - strEndTrimmed.length()) - (1 + newMinMiddleCharsLen));
            }

            stringBuilder.append(spanAlinhado(strStartTrimmed, strEndTrimmed, spanChar, spanCharsLength, qtdeLinhasPula));
        }

        return stringBuilder.toString();
    }

    // Texto no meio dos separadores
    public static String textBetweenSeparators(String texto, String spanChar, int spanCharLength) {

        StringBuilder stringBuilder = new StringBuilder();

        if (texto.length() <= spanCharLength) {
            int spanDividido = (spanCharLength - texto.length())/2;

            for (int i=0;i<spanDividido; i++){
                stringBuilder.append(spanChar);
            }

            stringBuilder.append(texto);

            for (int i=0;i<spanDividido; i++){
                stringBuilder.append(spanChar);
            }

            stringBuilder.append("\r\n");
        }

        return stringBuilder.toString();
    }

    // Formata o texto para ocupar o espaço disponivel do span em relaçao aos outros spans
    public static String textMaxAvailableSize(String strsOfSpans, int maxLength, String strToFormat) {
        int tamanhoUtilizado = strsOfSpans.length();
        int tamanhoDisponivel = maxLength - (tamanhoUtilizado + 1);

        String retorno = strToFormat;

        if (strToFormat.length() > tamanhoDisponivel) {
            retorno = strToFormat.substring(0, tamanhoDisponivel);
        }

        return retorno;
    }

    public static String strMaxLength(String str, int maxLength) {
        if (str != null && str.length() > maxLength) {
            return str.substring(0, maxLength);
        } else {
            return str;
        }
    }

    // Gera o separador
    public static String getSeparator(int spanLength, String separadorChar){
        return getSeparator(spanLength, separadorChar, 1);
    }

    // Gera o separador
    public static String getSeparator(int spanLength, String separadorChar, int qtdLinhasPula){
        StringBuilder stringBuilder = new StringBuilder();

        for (int i=1; i<spanLength; i++){
            stringBuilder.append(separadorChar);
        }

        for (int i=0; i<qtdLinhasPula; i++) {
            stringBuilder.append("\r\n");
        }


        return stringBuilder.toString();
    }

    // ABREVIA PALAVRAS PARA O TAMANHO SELECIONADO
    public static String abreviateWords(String text, int wordMaxSize) throws Exception {
        StringBuilder retorno = new StringBuilder();
        String[] words = text.split(" ");
        for (String word : words) {

            if (word.length() > wordMaxSize){
                retorno.append(word.substring(0, wordMaxSize)).append(". ");
            } else {
                retorno.append(word).append(" ");
            }
        }

        return retorno.toString();
    }

    public static String generateGuid() {
        return UUID.randomUUID().toString();
    }
}
