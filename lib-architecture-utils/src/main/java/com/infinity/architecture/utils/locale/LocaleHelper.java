package com.infinity.architecture.utils.locale;

import androidx.annotation.NonNull;

import java.util.Locale;

public class LocaleHelper {

    public static final int INT_LOCALE_PORTUGUES = 1;
    public static final int INT_LOCALE_ENGLISH = 2;
    public static final int INT_LOCALE_FRENCH = 3;
    public static final int INT_LOCALE_GERMAN = 4;
    public static final int INT_LOCALE_ITALIAN = 5;
    public static final int INT_LOCALE_JAPANESE = 6;
    public static final int INT_LOCALE_KOREAN = 7;
    public static final int INT_LOCALE_CHINESE = 8;
    public static final int INT_LOCALE_SIMPLIFIED_CHINESE = 9;
    public static final int INT_LOCALE_TRADITIONAL_CHINESE = 10;
    public static final int INT_LOCALE_UK = 11;
    public static final int INT_LOCALE_US = 12;
    public static final int INT_LOCALE_CANADA = 13;
    public static final int INT_LOCALE_CANADA_FRENCH = 14;

    /** Useful constant for language.
     */
    static public final Locale ENGLISH = Locale.ENGLISH;

    /** Useful constant for language.
     */
    static public final Locale FRENCH = Locale.FRENCH;

    /** Useful constant for language.
     */
    static public final Locale GERMAN = Locale.GERMAN;

    /** Useful constant for language.
     */
    static public final Locale ITALIAN = Locale.ITALIAN;

    /** Useful constant for language.
     */
    static public final Locale JAPANESE = Locale.JAPANESE;

    /** Useful constant for language.
     */
    static public final Locale KOREAN = Locale.KOREAN;

    /** Useful constant for language.
     */
    static public final Locale CHINESE = Locale.CHINESE;

    /** Useful constant for language.
     */
    static public final Locale SIMPLIFIED_CHINESE = Locale.SIMPLIFIED_CHINESE;

    /** Useful constant for language.
     */
    static public final Locale TRADITIONAL_CHINESE = Locale.TRADITIONAL_CHINESE;

    /** Useful constant for language.
     */
    static public final Locale PORTUGUES = new Locale("pt", "BR");

    /** Useful constant for country.
     */
    static public final Locale FRANCE = Locale.FRANCE;

    /** Useful constant for country.
     */
    static public final Locale GERMANY = Locale.GERMANY;

    /** Useful constant for country.
     */
    static public final Locale ITALY = Locale.ITALY;

    /** Useful constant for country.
     */
    static public final Locale JAPAN = Locale.JAPAN;

    /** Useful constant for country.
     */
    static public final Locale KOREA = Locale.KOREA;

    /** Useful constant for country.
     */
    static public final Locale CHINA = SIMPLIFIED_CHINESE;

    /** Useful constant for country.
     */
    static public final Locale PRC = SIMPLIFIED_CHINESE;

    /** Useful constant for country.
     */
    static public final Locale TAIWAN = TRADITIONAL_CHINESE;

    /** Useful constant for country.
     */
    static public final Locale UK = Locale.UK;

    /** Useful constant for country.
     */
    static public final Locale US = Locale.US;

    /** Useful constant for country.
     */
    static public final Locale CANADA = Locale.CANADA;

    /** Useful constant for country.
     */
    static public final Locale CANADA_FRENCH = Locale.CANADA_FRENCH;

    /**
     * RETORNA O LOCALE PELO INT_LOCALE INFORMADO
     *
     * @param intLocale INT LOCALE DAS CONSTANTES
     */
    @NonNull
    public static Locale getLocaleByIntType(int intLocale) {
        switch (intLocale) {
            case (INT_LOCALE_PORTUGUES):
                return PORTUGUES;
            case (INT_LOCALE_ENGLISH):
                return ENGLISH;
            case (INT_LOCALE_FRENCH):
                return FRENCH;
            case (INT_LOCALE_GERMAN):
                return GERMAN;
            case (INT_LOCALE_ITALIAN):
                return ITALIAN;
            case (INT_LOCALE_JAPANESE):
                return JAPANESE;
            case (INT_LOCALE_KOREAN):
                return KOREAN;
            case (INT_LOCALE_CHINESE):
                return CHINESE;
            case (INT_LOCALE_SIMPLIFIED_CHINESE):
                return SIMPLIFIED_CHINESE;
            case (INT_LOCALE_TRADITIONAL_CHINESE):
                return TRADITIONAL_CHINESE;
            case (INT_LOCALE_UK):
                return UK;
            case (INT_LOCALE_US):
                return US;
            case (INT_LOCALE_CANADA):
                return CANADA;
            case (INT_LOCALE_CANADA_FRENCH):
                return CANADA_FRENCH;
            default:
                return Locale.getDefault();
        }
    }
}
