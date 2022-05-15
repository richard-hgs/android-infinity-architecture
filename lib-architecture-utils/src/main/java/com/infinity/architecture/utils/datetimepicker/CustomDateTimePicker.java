package com.infinity.architecture.utils.datetimepicker;

import android.content.Context;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;

import com.infinity.architecture.utils.datepicker.CustomDatePicker;
import com.infinity.architecture.utils.datetime.DataHoraHelper;
import com.infinity.architecture.utils.datetime.DateFormatAllowed;
import com.infinity.architecture.utils.locale.LocaleHelper;
import com.infinity.architecture.utils.timepicker.CustomTimePicker;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;


public class CustomDateTimePicker {

    /**
     * DIALOG DO DATEPICKER
     */
    private CustomDatePicker customDatePicker;

    /**
     * DIALOG DO TIMEPICKER
     */
    private CustomTimePicker customTimePicker;

    /**
     * DATEPICKER CUSTOM DIALOG LISTENER
     */
    private Listener customListener;

    private Date datePicked;
    private String strDatePicked;

    private Date timePicked;
    private String strTimePicked;

    private boolean clearReferencesOnClose = true;

    public CustomDateTimePicker(Context context,
                                @Nullable Locale datePickerLocale,
                                @Nullable Locale timePickerLocale,
                                @NonNull @DateFormatAllowed String datePickerFormat,
                                @NonNull @DateFormatAllowed String timePickerFormat,
                                @NonNull @DateFormatAllowed String dateSetFormat,
                                @NonNull Listener customListener) {
        this(context, 0, 0, datePickerLocale, timePickerLocale, datePickerFormat, timePickerFormat, dateSetFormat, null, customListener);
    }

    public CustomDateTimePicker(Context context,
                                @StyleRes int dateThemeResId,
                                @StyleRes int timeThemeResId,
                                @Nullable Locale datePickerLocale,
                                @Nullable Locale timePickerLocale,
                                @NonNull @DateFormatAllowed String datePickerFormat,
                                @NonNull @DateFormatAllowed String timePickerFormat,
                                @NonNull @DateFormatAllowed String dateSetFormat,
                                @NonNull Listener customListener) {
        this(context, dateThemeResId, timeThemeResId, datePickerLocale, timePickerLocale, datePickerFormat, timePickerFormat, dateSetFormat, null, customListener);
    }

    /**
     * CONSTRUTOR DO DATEPICKER PARA O LOCALE INFORMADO
     * @param context              CONTEXTO DA APLICACAO
     * @param datePickerLocale      LOCALIDADE DO DATEPICKER (US, BR, ...) SE null ENTAO DEFAULT
     * @param dateSetFormat         FORMATO DA SAIDA DA DATA PASSADA AO LISTENER EM FORMA DE STRING
     * @param customListener        LISTENER DO DATETIMEPICKER CUSTOM
     */
    public CustomDateTimePicker(Context context,
                                @StyleRes int dateThemeResId,
                                @StyleRes int timeThemeResId,
                                @Nullable Locale datePickerLocale,
                                @Nullable Locale timePickerLocale,
                                @NonNull @DateFormatAllowed String datePickerFormat,
                                @NonNull @DateFormatAllowed String timePickerFormat,
                                @NonNull @DateFormatAllowed String dateSetFormat,
                                @Nullable Date currentDateTime,
                                @NonNull Listener customListener) {

        customDatePicker = new CustomDatePicker(context, dateThemeResId, datePickerLocale, datePickerFormat, false, currentDateTime, new CustomDatePicker.Listener() {
            @Override
            public boolean onDateSet(DatePicker view, Date date, String dateStr) {
                // ATUALIZA A DATA RECEBIDA
                datePicked = date;
                strDatePicked = dateStr;

                // EXIBE TIMEPICKER DIALOG
                customTimePicker.show();

                return true;
            }

            @Override
            public boolean onCanceled() {
                return true;
            }

            @Override
            public void onParseException(ParseException e) {
                customListener.onParseException(e);
            }
        });

        customTimePicker = new CustomTimePicker(context, timeThemeResId, timePickerLocale, timePickerFormat, currentDateTime, new CustomTimePicker.Listener() {
            @Override
            public boolean onTimeSet(TimePicker view, Date date, String dateStr) {

                try {
                    // ATUALIZA A HORA RECEBIDA
                    timePicked = date;
                    strTimePicked = dateStr;

                    // RECEBE A NOVA DATA COM DATA/HORA NO FORMATO DATE E STRING
                    String strDateTime = DataHoraHelper.formatDate(
                        datePickerFormat + " " + timePickerFormat,
                        strDatePicked + " " + strTimePicked,
                        dateSetFormat,
                        LocaleHelper.PORTUGUES
                    );

                    Date dateTime = DataHoraHelper.getDateFromString(LocaleHelper.PORTUGUES, dateSetFormat, strDateTime);

                    // NOTIFICA O LISTENER
                    customListener.onDateSet(dateTime, strDateTime);
                } catch (ParseException e) {
                    customListener.onParseException(e);
                }

                return true;
            }

            @Override
            public boolean onCanceled() {
                // EXIBE O DATEPICKER NOVAMENTE PARA UPDATE OU CANCELAMENTO...
                customDatePicker.show();

                return true;
            }

            @Override
            public void onParseException(ParseException e) {
                customListener.onParseException(e);
            }
        });

        // EXIBE O DATEPICKER DIALOG INICIALMENTE
        customDatePicker.show();
    }

    /**
     * EXIBE O DATETIME PICKER DIALOG
     */
    public void show() {
        if (customDatePicker != null) {
            customDatePicker.show();
        }
    }

    /**
     * FECHA O DATETIME PICKER DIALOG
     */
    public void close() {
        if (customDatePicker != null) {
            customDatePicker.close();
            if (clearReferencesOnClose) {
                customDatePicker = null;
            }
        }

        if (customTimePicker != null) {
            customTimePicker.close();
            if (clearReferencesOnClose) {
                customTimePicker = null;
            }
        }
    }

    /**
     * LISTENER DO DATE PICKER DIALOG
     */
    public interface Listener {
        /**
         * LISTENER NOTIFICADO QUANDO UMA DATA SELECIONADA NO DATEPICKER
         * @param date      DATA EM FORMATO DE {@link Date} SELECIONADA
         * @param dateStr   DATA EM FORMATO DE STRING NO FORMATO ESCOLHIDO
         * @return          true=FECHA DIALOG; false=NAO FECHA O DIALOG
         */
        boolean onDateSet(Date date, String dateStr);

        /**
         * EXCEÇÃO CASO HAJA
         * @param e         ERRO AO FORMATAR DATA
         */
        void onParseException(ParseException e);
    }
}
