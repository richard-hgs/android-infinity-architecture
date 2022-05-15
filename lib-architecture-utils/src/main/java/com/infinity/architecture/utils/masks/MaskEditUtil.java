package com.infinity.architecture.utils.masks;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

@SuppressWarnings({ "unused", "IfCanBeSwitch", "StatementWithEmptyBody", "ForLoopReplaceableByForEach", "FieldMayBeFinal", "ConstantConditions", "CommentedOutCode" })
public class MaskEditUtil implements TextWatcher {

    private static final String TAG = "MaskEditUtil";

    public static final String FORMAT_CPF               = "###.###.###-##";
    public static final String FORMAT_CELLFONE_1        = "(##)####-#####";
    public static final String FORMAT_PHONE_1           = "(##)####-####";
    public static final String FORMAT_CEP               = "#####-###";
    public static final String FORMAT_DATE_1            = "##/##/####";
    public static final String FORMAT_HOUR_1            = "##:##:##";
    public static final String FORMAT_CNPJ              = "##.###.###/####-##";
    public static final String FORMAT_RG                = "##.###.###-#";
    public static final String FORMAT_BANK_AGENCY_1     = "####-#";
    public static final String FORMAT_BANK_ACCOUNT_1    = "##.###-#";
    public static final String FORMAT_MONETARY          = "$#.##";              // SHOULD NOT BE USED, USE INT FORMAT INSTEAD
    public static final String FORMAT_CREDIT_CARD       = "####-####-####-####";
    public static final String FORMAT_CREDIT_CARD2      = "#### #### #### ####";
    public static final String FORMAT_DEBIT_CARD        = "######-####-#####-####";
    public static final String FORMAT_VALIDITY_CARD     = "##/####";

    private WeakReference<EditText> editTextWeakReference;

    // General mask configs
    private MaskEditType maskType;
    private String mask;

    // Monatary mask configs
    private int monetaryMaskDecimalPlaces;
    private Locale     monetaryMaskLocale;
    private boolean    monetaryMaskShowCurrency;
    private BigDecimal monetaryBigDec;
    private String     customMonetaryCurrency;

    private int maxLength = -1;                 // Mask max length
    private boolean selfChange;
    private boolean reverseMode;
    private boolean allowPassMaskLength;        // false=Disallowed true=Allowed to bypass max mask length

    private TextWatcher textChangedListener = null;

    /**
     * Constructor 1
     *
     * @param editText EditText to be formatted
     * @param mask     Custom mask to be used to format
     */
    public MaskEditUtil(EditText editText, String mask) {
        this(editText, mask, MaskEditType.NONE, false);
    }

    /**
     * Constructor 2
     *
     * @param editText EditText to be formatted
     * @param mask     Default mask type to be used to format
     */
    public MaskEditUtil(EditText editText, String mask, @NonNull MaskEditType maskType) {
        this(editText, mask, maskType, false);
    }

    /**
     * Constructor 3
     *
     * @param editText    EditText to be formatted
     * @param mask        Custom mask to be used to format
     * @param maskType    Default mask type to be used to format
     * @param reverseMode SE MODO REVERSO HABILITADO OU NAO
     */
    public MaskEditUtil(EditText editText, String mask, @NonNull MaskEditType maskType, boolean reverseMode) {
        super();
        this.editTextWeakReference = new WeakReference<>(editText);
        this.maskType = maskType;
        this.mask = mask;
        this.reverseMode = reverseMode;
        this.monetaryMaskDecimalPlaces = 2;
        this.monetaryMaskLocale       = Locale.getDefault();
        this.monetaryMaskShowCurrency = true;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (textChangedListener != null) {
            textChangedListener.beforeTextChanged(s, start, count, after);
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (textChangedListener != null) {
            textChangedListener.onTextChanged(s, start, before, count);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (maskType == MaskEditType.MONETARY) {
            // Monetary mask formatter
            if (selfChange) return;
            selfChange = true;

            // Monetary mask
            EditText editText = editTextWeakReference.get();
            if (editText == null) {
                selfChange = false;
                if (textChangedListener != null) {
                    textChangedListener.afterTextChanged(s);
                }
                return;
            }

            //editText.removeTextChangedListener(this);

            if (s != null && s.length() > 0) {
                if (maxLength == -1 || s.length() <= maxLength) {
                    try {
                        monetaryBigDec = parseToBigDecimal(s.toString());
                    } catch (Exception e) {
                        // If text field is empty and a letter is input in field
                        // an error is thrown since is not a numeric value can't be parsed to BigDecimal
                        monetaryBigDec = BigDecimal.ZERO;
                    }

                    NumberFormat numberFormat = NumberFormat.getCurrencyInstance(monetaryMaskLocale);
                    numberFormat.setMaximumFractionDigits(monetaryMaskDecimalPlaces);
                    numberFormat.setMinimumFractionDigits(monetaryMaskDecimalPlaces);

                    String formatted = numberFormat.format(monetaryBigDec);
                    if (!monetaryMaskShowCurrency) {
                        formatted = numberFormat.format(monetaryBigDec).replace((numberFormat.getCurrency() != null ? numberFormat.getCurrency().getSymbol() : ""), "");
                    } else if (monetaryMaskShowCurrency && customMonetaryCurrency != null) {
                        //int monetaryMaskPos = formatted.indexOf(numberFormat.getCurrency().getSymbol());
                        formatted = formatted.replace(numberFormat.getCurrency() != null ? numberFormat.getCurrency().getSymbol() : "", "");
                        // Monetary mask symbol at start of mask
                        formatted = customMonetaryCurrency + formatted;
                    }
                    // Replace blank char
                    formatted = formatted.replace(String.valueOf((char) 160), "");

                    int previousLength = s.length();
                    int currentLength = formatted.length();

                    // Remove edit text filters
                    InputFilter[] editableFilters = s.getFilters();
                    s.setFilters(new InputFilter[]{});

                    // Replace the text
                    s.replace(0, previousLength, formatted, 0, currentLength);

                    // restore input filters
                    s.setFilters(editableFilters);

                    if (textChangedListener != null) {
                        textChangedListener.afterTextChanged(Editable.Factory.getInstance().newEditable(formatted));
                    }
                } else {
                    CharSequence newStr = s.subSequence(0, s.length() - 1);
                    editText.setText(newStr);
                    editText.setSelection(newStr.length());

                    if (textChangedListener != null) {
                        textChangedListener.afterTextChanged(Editable.Factory.getInstance().newEditable(newStr));
                    }
                }
            } else {
                if (textChangedListener != null) {
                    textChangedListener.afterTextChanged(s);
                }
            }
            //editText.addTextChangedListener(this);
            selfChange = false;
        } else {
            // General custom mask formatter
            if (selfChange) return;
            selfChange = true;
            if (!reverseMode) {
                format(s);
            } else {
                //format2(s);
            }
            if (textChangedListener != null) {
                textChangedListener.afterTextChanged(s);
            }
            selfChange = false;
        }
    }

    /**
     * Format EditText text to the mask selected
     * @param text      Text to be formatted
     */
    private void format(Editable text) {
        if (isNullOrEmpty(text)) {
            return;
        }

        // Remove edit text filters
        InputFilter[] editableFilters = text.getFilters();
        text.setFilters(new InputFilter[]{});

        // Holds formatted text
        StringBuilder formatted = new StringBuilder();

        // Get edit text
        char[] chars = text.toString().toCharArray();

        // Store the text to formatted string builder
        List<Character> list = new ArrayList<>();
        for (char c : chars) {
            list.add(c);
        }

        // RECEBE OS CARACTERES DA MASCARA
        char[] maskChars = mask.toCharArray();

        // PARA CADA CARACTERE DA MASCARA
        for (int i=0; i<maskChars.length; i++){
            if (list.isEmpty()) {

            } else {
                // SE O CAMPO POSSUIR TEXTO

                Character c = list.get(0);
                if (isPlaceHolder(maskChars[i])) {
                    if (!Character.isLetterOrDigit(c)) {
                        // find next letter or digit
                        Iterator<Character> iterator = list.iterator();
                        while (iterator.hasNext()) {
                            c = iterator.next();
                            if (Character.isLetterOrDigit(c)) break;
                            iterator.remove();
                        }
                    }
                    if (list.isEmpty()) {

                    } else {
                        formatted.append(c);
                        list.remove(0);
                    }
                } else {
                    formatted.append(maskChars[i]);
                    if (maskChars[i] == c) {
                        list.remove(0);
                    }
                }
            }
        }
        int previousLength = text.length();
        int currentLength = formatted.length();

        // Log.d(TAG, "previousLength: " + previousLength);
        // Log.d(TAG, "currentLength: " + currentLength);

        // Log.d(TAG, "formatted: " + formatted.toString());
        if (previousLength > currentLength && allowPassMaskLength) {

        } else {
            if (maxLength == -1 || currentLength <= maxLength) {
                text.replace(0, previousLength, formatted, 0, currentLength);
            }
        }

        // set correct cursor position when editing
        if (currentLength < previousLength) {
            EditText editText = editTextWeakReference.get();
            if (editText != null) {
                int currentSelection = findCursorPosition(text, editText.getSelectionStart());
                editText.setSelection(currentSelection);
            }
        }

        // restore input filters
        text.setFilters(editableFilters);
    }

    /**
     * Unformat the EditText text if not Monetary mask format
     * @param text  Formatted text
     * @return Unformatted text
     */
    public String unformat(Editable text) {
        if (isNullOrEmpty(text)) return null;
        StringBuilder unformatted = new StringBuilder();
        int textLength = text.length();

        char[] maskChars = mask.toCharArray();

        for (int i = 0; i<maskChars.length; i++) {
            if (i >= textLength) {

            } else if (isPlaceHolder(maskChars[i])) {
                unformatted.append(text.charAt(i));
            }
        }
        return unformatted.toString();
    }


    // Utilitaries to help formattin text
    private int findCursorPosition(Editable text, int start) {
        if (isNullOrEmpty(text)) return start;
        int textLength = text.length();
        int maskLength = mask.length();
        int position = start;

        for (int i = start; i<maskLength; i++) {
            if (isPlaceHolder(mask.charAt(i))) {
                break;
            }
            position++;
        }
        position++;
        return Math.min(position, textLength);
    }

    private static boolean isNullOrEmpty(Editable text) {
        return text == null || text.length() == 0;
    }

    private static boolean isPlaceHolder(char caractere) {
        return caractere == '#';
    }

    public boolean isAllowPassMaskLength() {
        return allowPassMaskLength;
    }

    private BigDecimal parseToBigDecimal(String value) {
        //String replaceable = String.format("[%s,.\\s]", NumberFormat.getCurrencyInstance(locale).getCurrency().getSymbol());

        String cleanString = value.replaceAll("[^0-9]", "");

        double doubleDecimalPlaces = Math.pow(10, monetaryMaskDecimalPlaces > 0 ? monetaryMaskDecimalPlaces : 1);

        return new BigDecimal(cleanString).setScale(
                monetaryMaskDecimalPlaces, BigDecimal.ROUND_FLOOR).divide(new BigDecimal(doubleDecimalPlaces), BigDecimal.ROUND_FLOOR
        );
    }


    // GETTERS AND SETTERS
    public void setAllowPassMaskLength(boolean allowPassMaskLength) {
        this.allowPassMaskLength = allowPassMaskLength;
    }

    /**
     * Get monetary mask locale
     *
     * @return locale
     */
    public Locale getMonetaryMaskLocale() {
        return monetaryMaskLocale;
    }

    /**
     * Set monetary mask locale
     *
     * @param monetaryMaskLocale locale
     */
    public void setMonetaryMaskLocale(Locale monetaryMaskLocale) {
        this.monetaryMaskLocale = monetaryMaskLocale;
    }

    /**
     * Return monetary mask format decimal places max length
     * Or -1 if mask format isn't monetary
     *
     * @return QUANTIDADE DE CASAS DECIMAIS
     */
    public int getMonetaryMaskDecimalPlaces() {
        return monetaryMaskDecimalPlaces;
    }

    /**
     * Change decimal places of monetary mask
     *
     * @param monetaryMaskDecimalPlaces Decimal places
     */
    public void setMonetaryMaskDecimalPlaces(int monetaryMaskDecimalPlaces) {
        this.monetaryMaskDecimalPlaces = monetaryMaskDecimalPlaces;
    }

    /**
     * Return if monetary symbol is visible
     *
     * @return true=Show monetary symbol false=Don't show
     */
    public boolean isMonetaryMaskShowCurrency() {
        return monetaryMaskShowCurrency;
    }

    /**
     * Show/hide monetary symbol in money mask
     *
     * @param monetaryMaskShowCurrency true=Show monetary symbol false=Don't show
     */
    public void setMonetaryMaskShowCurrency(boolean monetaryMaskShowCurrency) {
        this.monetaryMaskShowCurrency = monetaryMaskShowCurrency;
    }

    /**
     * Return the custom monetary mask symbol
     *
     * @return String of monetary symbol
     */
    @Nullable
    public String getCustomMonetaryCurrency() {
        return customMonetaryCurrency;
    }

    /**
     * Update/Change monetary mask symbol
     *
     * @param customMonetaryCurrency String of monetary symbol
     */
    public void setCustomMonetaryCurrency(String customMonetaryCurrency) {
        this.customMonetaryCurrency = customMonetaryCurrency;
    }

    /**
     * Get mask format for mask type
     *
     * @param maskType Mask type
     * @return String mask format
     */
    @Nullable
    public static String getMaskFormatFromMaskType(@NonNull MaskEditType maskType) {
        String maskFormat = null;
        if (maskType == MaskEditType.CPF) {
            maskFormat = MaskEditUtil.FORMAT_CPF;
        } else if (maskType == MaskEditType.CELLFONE_1) {
            maskFormat = MaskEditUtil.FORMAT_CELLFONE_1;
        } else if (maskType == MaskEditType.PHONE_1) {
            maskFormat = MaskEditUtil.FORMAT_PHONE_1;
        } else if (maskType == MaskEditType.CEP) {
            maskFormat = MaskEditUtil.FORMAT_CEP;
        } else if (maskType == MaskEditType.DATE_1) {
            maskFormat = MaskEditUtil.FORMAT_DATE_1;
        } else if (maskType == MaskEditType.HOUR_1) {
            maskFormat = MaskEditUtil.FORMAT_HOUR_1;
        } else if (maskType == MaskEditType.CNPJ) {
            maskFormat = MaskEditUtil.FORMAT_CNPJ;
        } else if (maskType == MaskEditType.RG) {
            maskFormat = MaskEditUtil.FORMAT_RG;
        } else if (maskType == MaskEditType.BANK_AGENCY_1) {
            maskFormat = MaskEditUtil.FORMAT_BANK_AGENCY_1;
        } else if (maskType == MaskEditType.BANK_ACCOUNT_1) {
            maskFormat = MaskEditUtil.FORMAT_BANK_ACCOUNT_1;
        } else if (maskType == MaskEditType.MONETARY) {
            maskFormat = MaskEditUtil.FORMAT_MONETARY;
        } else if (maskType == MaskEditType.CREDIT_CARD) {
            maskFormat = MaskEditUtil.FORMAT_CREDIT_CARD;
        } else if (maskType == MaskEditType.DEBIT_CARD) {
            maskFormat = MaskEditUtil.FORMAT_DEBIT_CARD;
        } else if (maskType == MaskEditType.VALIDITY_CARD) {
            maskFormat = MaskEditUtil.FORMAT_VALIDITY_CARD;
        }

        return maskFormat;
    }

    /**
     * Get mask MaskEditType for string mask
     *
     * @param maskFormat Mask format
     * @return Mask type
     */
    @NonNull
    public static MaskEditType getMaskTypeFromStrMask(String maskFormat) {
        MaskEditType maskType = MaskEditType.NONE;
        if (maskFormat.equals(MaskEditUtil.FORMAT_CPF)) {
            maskType = MaskEditType.CPF;
        } else if (maskFormat.equals(MaskEditUtil.FORMAT_CELLFONE_1)) {
            maskType = MaskEditType.CELLFONE_1;
        } else if (maskFormat.equals(MaskEditUtil.FORMAT_PHONE_1)) {
            maskType = MaskEditType.PHONE_1;
        } else if (maskFormat.equals(MaskEditUtil.FORMAT_CEP)) {
            maskType = MaskEditType.CEP;
        } else if (maskFormat.equals(MaskEditUtil.FORMAT_DATE_1)) {
            maskType = MaskEditType.DATE_1;
        } else if (maskFormat.equals(MaskEditUtil.FORMAT_HOUR_1)) {
            maskType = MaskEditType.HOUR_1;
        } else if (maskFormat.equals(MaskEditUtil.FORMAT_CNPJ)) {
            maskType = MaskEditType.CNPJ;
        } else if (maskFormat.equals(MaskEditUtil.FORMAT_RG)) {
            maskType = MaskEditType.RG;
        } else if (maskFormat.equals(MaskEditUtil.FORMAT_BANK_AGENCY_1)) {
            maskType = MaskEditType.BANK_AGENCY_1;
        } else if (maskFormat.equals(MaskEditUtil.FORMAT_BANK_ACCOUNT_1)) {
            maskType = MaskEditType.BANK_ACCOUNT_1;
        } else if (maskFormat.equals(MaskEditUtil.FORMAT_MONETARY)) {
            maskType = MaskEditType.MONETARY;
        } else if (maskFormat.equals(MaskEditUtil.FORMAT_CREDIT_CARD)) {
            maskType = MaskEditType.CREDIT_CARD;
        } else if (maskFormat.equals(MaskEditUtil.FORMAT_DEBIT_CARD)) {
            maskType = MaskEditType.DEBIT_CARD;
        } else if (maskFormat.equals(MaskEditUtil.FORMAT_VALIDITY_CARD)) {
            maskType = MaskEditType.VALIDITY_CARD;
        }

        return maskType;
    }

    /**
     * Set/Change mask format type
     *
     * @param maskType Mask format type
     */
    public void setMaskType(@NonNull MaskEditType maskType) {
        this.maskType = maskType;
        this.mask = getMaskFormatFromMaskType(maskType);
        EditText editTextRef = editTextWeakReference.get();
        if (editTextRef != null) {
            this.format(editTextRef.getText());
        }
    }

    /**
     * Get current mask type
     *
     * @return mask type
     */
    @NonNull
    public MaskEditType getMaskType() {
        return maskType;
    }

    /**
     * Set a custom mask format for this EditText
     * Ex: setMaskFormat("##.###.##.##-##")
     *
     * @param maskFormat    The format
     */
    public void setMaskFormat(@NonNull String maskFormat) {
        this.maskType = MaskEditType.NONE;
        this.mask = maskFormat;
        EditText editTextRef = editTextWeakReference.get();
        if (editTextRef != null) {
            this.format(editTextRef.getText());
        }
    }

    public String getMaskFormat() {
        return this.mask;
    }

    /**
     * Get current mask max length
     *
     * @return  length
     */
    public int getMaxLength() {
        return maxLength;
    }

    /**
     * Set mask format max length
     *
     * @param maxLength The max length
     */
    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    /**
     * Notify mask formatter that text has changed
     */
    public void notifyChange() {
        EditText editTextRef = editTextWeakReference.get();
        if (editTextRef != null) {
            format(editTextRef.getText());
        }
    }

    /**
     * Get the monetary mask BigDecimal value
     *
     * @return BigDecimal
     */
    public BigDecimal getMonetaryBigDec() {
        return monetaryBigDec;
    }

    /**
     * Get current text watcher for this EditText to provide retro-compatibility with mask formatter
     *
     * @return  {@link TextWatcher}
     */
    @Nullable
    public TextWatcher getTextChangedListener() {
        return textChangedListener;
    }

    /**
     * Set/Change current text watcher for this EditText to provide retro-compatibility with mask formatter
     *
     * @param textChangedListener {@link TextWatcher}
     */
    public void setTextChangedListener(@Nullable TextWatcher textChangedListener) {
        this.textChangedListener = textChangedListener;
    }

    /**
     * Format the string with given custom mask format
     * @param strText               String be formatted
     * @param mask                  Mask to format
     * @param allowPassMaskLength   Allow to pass maskLength
     * @return  Formatted text
     */
    @Nullable
    public static String formatStr(String strText, String mask, boolean allowPassMaskLength) {
        int maxLength = -1;
        Editable text = Editable.Factory.getInstance().newEditable(strText);
        if (isNullOrEmpty(text)) {
            return null;
        }

        // REMOVE OS FILTROS DO EDIT
        InputFilter[] editableFilters = text.getFilters();
        text.setFilters(new InputFilter[]{});

        // ARMAZENA A STRING FORMATADA
        StringBuilder formatted = new StringBuilder();

        // RECEBE OS CARACTERES DO CAMPO
        char[] chars = text.toString().toCharArray();

        // ARMAZENA OS CARACTERES DO CAMPO NA LISTA
        List<Character> list = new ArrayList<>();
        for (char c : chars) {
            list.add(c);
        }

        // RECEBE OS CARACTERES DA MASCARA
        char[] maskChars = mask.toCharArray();

        // PARA CADA CARACTERE DA MASCARA
        for (int i=0; i<maskChars.length; i++){
            if (list.isEmpty()) {

            } else {
                // SE O CAMPO POSSUIR TEXTO

                Character c = list.get(0);
                if (isPlaceHolder(maskChars[i])) {
                    if (!Character.isLetterOrDigit(c)) {
                        // find next letter or digit
                        Iterator<Character> iterator = list.iterator();
                        while (iterator.hasNext()) {
                            c = iterator.next();
                            if (Character.isLetterOrDigit(c)) break;
                            iterator.remove();
                        }
                    }
                    if (list.isEmpty()) {

                    } else {
                        formatted.append(c);
                        list.remove(0);
                    }
                } else {
                    formatted.append(maskChars[i]);
                    if (maskChars[i] == c) {
                        list.remove(0);
                    }
                }
            }
        }
        int previousLength = text.length();
        int currentLength = formatted.length();

        //Log.d(TAG, "previousLength: " + previousLength);
        //Log.d(TAG, "currentLength: " + currentLength);

        //Log.d(TAG, "formatted: " + formatted.toString());
        if (previousLength > currentLength && allowPassMaskLength) {

        } else {
            if (maxLength == -1 || currentLength <= maxLength) {
                text.replace(0, previousLength, formatted, 0, currentLength);
            }
        }

        // set correct cursor position when editing
        if (currentLength < previousLength) {
//            EditText editText = editTextWeakReference.get();
//            if (editText != null) {
//                int currentSelection = findCursorPosition(text, editText.getSelectionStart());
//                editText.setSelection(currentSelection);
//            }
        }

        // restore input filters
        text.setFilters(editableFilters);

        return text.toString();
    }
}
