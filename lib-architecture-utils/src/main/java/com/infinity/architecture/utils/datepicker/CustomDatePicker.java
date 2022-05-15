package com.infinity.architecture.utils.datepicker;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;

import com.infinity.architecture.utils.datetime.DataHoraHelper;
import com.infinity.architecture.utils.datetime.DateFormatAllowed;
import com.infinity.architecture.utils.datetime.DateFormats;
import com.infinity.architecture.utils.locale.LocaleHelper;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CustomDatePicker {

    /**
     * DATEPICKER DIALOG
     */
    private DatePickerDialog datePickerDialog;

    /**
     * DATEPICKER DIALOG LISTENER
     */
    private DatePickerDialog.OnDateSetListener datePickerListener;

    /**
     * DATEPICKER CUSTOM DIALOG LISTENER
     */
    private Listener customListener;


    private boolean clearReferencesOnClose = true;

    /**
     * CONSTRUTOR DO DATEPICKER PARA O LOCALE INFORMADO
     * @param context               CONTEXTO DA APLICACAO
     * @param datePickerLocale      LOCALIDADE DO DATEPICKER (US, BR, ...) SE null ENTAO DEFAULT
     * @param dateSetFormat         FORMATO DA SAIDA DA DATA PASSADA AO LISTENER EM FORMA DE STRING
     * @param customListener        LISTENER DO DATEPICKER CUSTOM
     */
    public CustomDatePicker(Context context, @Nullable Locale datePickerLocale,
                            @NonNull @DateFormatAllowed String dateSetFormat,
                            @NonNull Listener customListener) {
        this(context, 0, datePickerLocale, dateSetFormat, false, null, customListener);
    }

    /**
     * CONSTRUTOR DO DATEPICKER PARA O LOCALE INFORMADO
     * @param context               CONTEXTO DA APLICACAO
     * @param datePickerLocale      LOCALIDADE DO DATEPICKER (US, BR, ...) SE null ENTAO DEFAULT
     * @param dateSetFormat         FORMATO DA SAIDA DA DATA PASSADA AO LISTENER EM FORMA DE STRING
     * @param customListener        LISTENER DO DATEPICKER CUSTOM
     */
    public CustomDatePicker(Context context, @StyleRes int themeResId, @Nullable Locale datePickerLocale,
                            @NonNull @DateFormatAllowed String dateSetFormat,
                            @NonNull Listener customListener) {
        this(context, themeResId, datePickerLocale, dateSetFormat, false, null, customListener);
    }

    /**
     * CONSTRUTOR DO DATEPICKER PARA O LOCALE INFORMADO
     * @param context               CONTEXTO DA APLICACAO
     * @param datePickerLocale      LOCALIDADE DO DATEPICKER (US, BR, ...) SE null ENTAO DEFAULT
     * @param dateSetFormat         FORMATO DA SAIDA DA DATA PASSADA AO LISTENER EM FORMA DE STRING
     * @param customListener        LISTENER DO DATEPICKER CUSTOM
     */
    public CustomDatePicker(Context context, @StyleRes int themeResId, @Nullable Locale datePickerLocale,
                            @NonNull @DateFormatAllowed String dateSetFormat,
                            boolean hideDays, @Nullable Date currentTime,
                            @NonNull Listener customListener) {
        this.customListener = customListener;
        /*
         * RECEBE O CALENDARIO PELA LOCALIDADE
         */
        Calendar calendar = null;
        if (datePickerLocale != null){
            calendar = Calendar.getInstance(datePickerLocale);
        } else {
            calendar = Calendar.getInstance(LocaleHelper.PORTUGUES);
        }

        if (currentTime != null) {
            calendar.setTime(currentTime);
        }

        /*
         * RECEBE O DIA/MES/ANO INICAL DO DATE PICKER EM QUE JÁ ESTARÁ SETADO
         */
        int startYear   = calendar.get(Calendar.YEAR);
        int startMonth  = calendar.get(Calendar.MONTH);
        int startDay    = calendar.get(Calendar.DAY_OF_MONTH);

        /*
         * LISTENER CUSTOMIZADO DO DATEPICKER CUSTOM DIALOG
         */

        /*
         * LISTENER DO DATEPICKER DIALOG
         */
        datePickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                try {
                    /*
                     * FORMATA A DATA RECEBIDA
                     */
                    String stringDate = year + "-" + String.format(LocaleHelper.PORTUGUES, "%02d", (month + 1)) + "-" + String.format(LocaleHelper.PORTUGUES, "%02d", dayOfMonth);
                    Date date = DataHoraHelper.getDateFromString(LocaleHelper.PORTUGUES, DateFormats.EN_US_3, stringDate);
                    String dataPtBr = DataHoraHelper.formatDate(date, dateSetFormat, LocaleHelper.PORTUGUES);

                    /*
                     * NOTIFICA O LISTENER DA MUDANCA PASSANDO A DATA JÁ FORMATADA
                     */
                    boolean fechaDialog = customListener.onDateSet(view, date, dataPtBr);
                    if (fechaDialog){
                        close();
                    }
                } catch (java.text.ParseException e) {
                    //e.printStackTrace();
                    customListener.onParseException(e);
                }
            }
        };

        /*
         * INSTANCIA DATE PICKER DIALOG
         */
        datePickerDialog = new DatePickerDialog(context, themeResId, datePickerListener, startYear, startMonth, startDay) {
            boolean closeDialog = false;

            @Override
            public void onClick(@NonNull DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_NEGATIVE) {
                    // FINALIZA DIALOG SEGUNDO O PARAMETRO
                    closeDialog = customListener.onCanceled();
                } else {
                    closeDialog = true;
                }
                super.onClick(dialog, which);
            }

            @Override
            public void dismiss() {
                // EVITA
                if (closeDialog) {
                    super.dismiss();
                }
            }
        };
    }

    /**
     * EXIBE O DATE PICKER DIALOG
     */
    public void show() {
        if (datePickerDialog != null && !datePickerDialog.isShowing()) {
            datePickerDialog.show();
        }
    }

    /**
     * FECHA O DATE PICKER DIALOG
     */
    public void close() {
        if (datePickerDialog != null && datePickerDialog.isShowing()){
            datePickerDialog.dismiss();
            if (clearReferencesOnClose) {
                datePickerDialog = null;
            }
        }
    }

    /**
     * LISTENER DO DATE PICKER DIALOG
     */
    public interface Listener {
        /**
         * LISTENER NOTIFICADO QUANDO UMA DATA SELECIONADA NO DATEPICKER
         * @param view      VIEW DO DATEPICKER DIALOG
         * @param date      DATA EM FORMATO DE {@link Date} SELECIONADA
         * @param dateStr   DATA EM FORMATO DE STRING NO FORMATO ESCOLHIDO
         * @return          true=FECHA DIALOG; false=NAO FECHA O DIALOG
         */
        boolean onDateSet(DatePicker view, Date date, String dateStr);

        /**
         * LISTENER NOTIFICADO QUANDO BOTAO CANCELAR PRESSIONADO
         * @return  true=FECHA DIALOG; false=NAO FECHA O DIALOG
         */
        boolean onCanceled();

        /**
         * EXCEÇÃO CASO HAJA
         * @param e         ERRO AO FORMATAR DATA
         */
        void onParseException(java.text.ParseException e);
    }
}
