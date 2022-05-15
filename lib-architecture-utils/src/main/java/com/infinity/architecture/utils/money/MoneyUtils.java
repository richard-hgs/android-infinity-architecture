package com.infinity.architecture.utils.money;

import com.infinity.architecture.utils.locale.LocaleHelper;

import java.text.NumberFormat;

public class MoneyUtils {
    // ========================BUYPOINT=====================
    //função que formata numeros reais para ficar sempre com 2 casas. retorna uma String.
    public static String formatarFloat(float numero, int digitoMinimo, int digitoMaximo) {
        NumberFormat formatter = NumberFormat.getInstance(LocaleHelper.PORTUGUES);
        formatter.setMinimumFractionDigits(digitoMinimo);
        formatter.setMaximumFractionDigits(digitoMaximo);
        String output = formatter.format(numero); //.replace(",", ".");

        return output;
    }

    public static String formatarDouble(double numero, int digitoMinimo, int digitoMaximo) {
        NumberFormat formatter = NumberFormat.getInstance(LocaleHelper.PORTUGUES);
        formatter.setMinimumFractionDigits(digitoMinimo);
        formatter.setMaximumFractionDigits(digitoMaximo);
        String output = formatter.format(numero); //.replace(",", ".");

        return output;
    }
}
