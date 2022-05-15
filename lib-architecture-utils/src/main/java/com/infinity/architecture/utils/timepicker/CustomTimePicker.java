package com.infinity.architecture.utils.timepicker;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.TimePicker;

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


public class CustomTimePicker {

    /**
     * TIMEPICKER DIALOG
     */
    private TimePickerDialog timePickerDialog;

    /**
     * TIMEPICKER DIALOG LISTENER
     */
    private TimePickerDialog.OnTimeSetListener timeSetListener;

    /**
     * TIMEPICKER CUSTOM DIALOG LISTENER
     */
    private Listener customListener;

    private boolean clearReferencesOnClose = true;

    public CustomTimePicker(Context context, @Nullable Locale timePickerLocale,
                            @NonNull @DateFormatAllowed String timeSetFormat,
                            @NonNull Listener customListener) {
        this(context, 0, timePickerLocale, timeSetFormat, null, customListener);
    }


    public CustomTimePicker(Context context, @StyleRes int timeThemeResId, @Nullable Locale timePickerLocale,
                            @NonNull @DateFormatAllowed String timeSetFormat,
                            @NonNull Listener customListener) {
        this(context, timeThemeResId, timePickerLocale, timeSetFormat, null, customListener);
    }

    /**
     * CONSTRUTOR DO TIMEPICKER PARA O LOCALE INFORMADO
     * @param context               CONTEXTO DA APLICACAO
     * @param timePickerLocale      LOCALIDADE DO TIMEPICKER (US, BR, ...) SE null ENTAO DEFAULT
     * @param timeSetFormat         FORMATO DA SAIDA DA HORA PASSADA AO LISTENER EM FORMA DE STRING
     * @param customListener        LISTENER DO TIMEPICKER CUSTOM
     */
    public CustomTimePicker(Context context, @StyleRes int timeThemeResId, @Nullable Locale timePickerLocale,
                            @NonNull @DateFormatAllowed String timeSetFormat, @Nullable Date currentTime,
                            @NonNull Listener customListener) {
        this.customListener = customListener;
        /*
         * RECEBE O CALENDARIO PELA LOCALIDADE
         */
        Calendar calendar = null;
        if (timePickerLocale != null) {
            calendar = Calendar.getInstance(timePickerLocale);
        } else {
            calendar = Calendar.getInstance(LocaleHelper.PORTUGUES);
        }

        if (currentTime != null) {
            calendar.setTime(currentTime);
        }

        /*
         * RECEBE O DIA/MES/ANO INICAL DO DATE PICKER EM QUE JÁ ESTARÁ SETADO
         */
        int startHour   = calendar.get(Calendar.HOUR_OF_DAY);
        int startMinute  = calendar.get(Calendar.MINUTE);

        /*
         * LISTENER CUSTOMIZADO DO TIMEPICKER CUSTOM DIALOG
         */

        /*
         * LISTENER DO TIMEPICKER DIALOG
         */
        timeSetListener = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                try {
                    /*
                     * FORMATA A DATA RECEBIDA
                     */
                    String stringDate = String.format(LocaleHelper.PORTUGUES, "%02d", hourOfDay) + ":" + String.format(LocaleHelper.PORTUGUES, "%02d", minute);
                    Date date = DataHoraHelper.getDateFromString(LocaleHelper.PORTUGUES, DateFormats.EN_US_6, stringDate);
                    String dataPtBr = DataHoraHelper.formatDate(date, timeSetFormat, LocaleHelper.PORTUGUES);

                    /*
                     * NOTIFICA O LISTENER DA MUDANCA PASSANDO A DATA JÁ FORMATADA
                     */
                    boolean fechaDialog = customListener.onTimeSet(view, date, dataPtBr);
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
        timePickerDialog = new TimePickerDialog(context, timeThemeResId, timeSetListener, startHour, startMinute, true) {

            boolean closeDialog = true;

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
                if (closeDialog){
                    super.dismiss();
                }
            }
        };
    }

    /**
     * EXIBE O TIMEPICKER DIALOG
     */
    public void show() {
        if (timePickerDialog != null && !timePickerDialog.isShowing()) {
            timePickerDialog.show();
        }
    }

    /**
     * FECHA O TIMEPICKER DIALOG
     */
    public void close() {
        if (timePickerDialog != null && timePickerDialog.isShowing()) {
            timePickerDialog.dismiss();
            if (clearReferencesOnClose) {
                timePickerDialog = null;
            }
        }
    }

    /**
     * LISTENER DO TIMEPICKER DIALOG
     */
    public interface Listener {
        /**
         * LISTENER NOTIFICADO QUANDO UMA DATA SELECIONADA NO TIMEPICKER
         * @param view      VIEW DO TIMEPICKER DIALOG
         * @param date      DATA EM FORMATO DE {@link Date} SELECIONADA
         * @param dateStr   DATA EM FORMATO DE STRING NO FORMATO ESCOLHIDO
         * @return          true=FECHA DIALOG; false=NAO FECHA O DIALOG
         */
        boolean onTimeSet(TimePicker view, Date date, String dateStr);

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
