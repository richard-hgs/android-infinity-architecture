package com.infinity.architecture.utils.datetime;


import android.app.Activity;

import androidx.annotation.NonNull;

import com.infinity.architecture.utils.locale.LocaleHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DataHoraHelper {
    private static final String TAG = "DataHoraHelper";

    /*
     * DIFERENCA DO TEMPO DA INTERNET PARA O TEMPO DO PC PARA CORRECAO DO TEMPO DO PC
     * TEMPO CORRETO = TEMPO_PC + TEMPO_DIFF
     */
    public static final String CACHE_TIME = "time";
    public static final String CACHE_TIME_DIFF = "time_diff";

    /*
     * CONVERSAO DE MILLIS PARA UNIDADES SELECONADAS ABAIXO (INICIO)
     */
    public static final long MILLIS_SEGUNDOS = 1000;
    public static final long MILLIS_MINUTOS = 60000;
    /*
     * CONVERSAO DE MILLIS PARA UNIDADES SELECONADAS ABAIXO (FIM)
     */

    /*
     * LOCAIS DATAS
     */

    // CONSTRUTOR
    public DataHoraHelper() {
    }

    // HORARIO DA INTERNET
    public static class InternetTime {

        // RECEBE HORA DA INTERNET
        public static void getInternetTime(Activity activity, @NonNull final Listener listener) {
            String urlHoraInternet = "http://worldtimeapi.org/api/timezone/America/Sao_Paulo";

//            HashMap<String,String> headers = new HashMap<>();
//            headers.put("Content-Type", "application/json");
//            headers.put("Cache-Control", "no-cache");
//
//            EnvJsonRequestHeaders[] envJsonRequestHeaders = new EnvJsonRequestHeaders[3];
//            envJsonRequestHeaders[0] = new EnvJsonRequestHeaders("Content-Type", "application/json");
//            envJsonRequestHeaders[1] = new EnvJsonRequestHeaders("Accept", "*/*");
//            envJsonRequestHeaders[2] = new EnvJsonRequestHeaders("Accept-Encoding", "gzip, deflate, br");
//
//            EnviarJson2 enviarJson2 = new EnviarJson2(activity, urlHoraInternet, null, EnvJsonRequestMethod.METHOD_GET, envJsonRequestHeaders, false, true, true, new BaseInterfaceEnviarJson(TAG, "internetTime") {
//                @Override
//                public void respostaOk(String resposta) {
//                    super.respostaOk(resposta);
//                    Log.d(TAG, "resposta: " + resposta);
//                    try {
//                        JSONObject jsonObject = new JSONObject(resposta);
//
//                        String internetTime = jsonObject.getString("datetime");
//
//                        internetTime = internetTime.substring(0,internetTime.lastIndexOf(':'))+internetTime.substring(internetTime.lastIndexOf(':')+1);
//
//                        internetTime = internetTime.substring(0, internetTime.lastIndexOf(".")+ 3) + internetTime.substring(internetTime.lastIndexOf("-"));
//
//                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.ENGLISH);
//                        Date dateInternet = simpleDateFormat.parse(internetTime);
//
//                        listener.onTimeReceived(dateInternet);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        listener.onTimeError(e);
//                    }
//                }
//
//                @Override
//                public void respostaErro(String erro, String erroJson) {
//                    super.respostaErro(erro, erroJson);
//                    listener.onTimeError(new Exception(erro));
//                }
//            });
        }

        public interface Listener {
            void onTimeReceived(Date date);

            void onTimeError(Throwable t);
        }
    }

    // CONVERTE DATE PARA DATA E HORA PARA O FORMATO SELECIONADO
    public static String formatDate(@NonNull Date date, @DateFormatAllowed @NonNull String format, @NonNull Locale locale) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, locale);
        return simpleDateFormat.format(date);
    }

    // CONVERTE DATE PARA DATA E HORA PARA O FORMATO SELECIONADO
    public static String formatDate(@NonNull @DateFormatAllowed String format, @NonNull String date, @DateFormatAllowed @NonNull String format2, @NonNull Locale locale) throws java.text.ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, locale);

        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(format2, locale);
        return simpleDateFormat2.format(simpleDateFormat.parse(date));
    }

    public static Date getDateFromString(Locale locale, String format, String str) throws java.text.ParseException {
        SimpleDateFormat formatter1 = new SimpleDateFormat(format, locale);
        return formatter1.parse(str);
    }

    // CONVERTE MILLIS PARA DATE
    public static int[] millisToDateValues(long millis, boolean startFromZero) throws Exception {


        /*Log.d(TAG, "dias: " + dia);
        Log.d(TAG, "mes: " + mes);
        Log.d(TAG, "ano: " + ano);
        Log.d(TAG, "horas: " + hora);
        Log.d(TAG, "minutos: " + minuto);
        Log.d(TAG, "segundos: " + segundo);*/
        long currentTime = new Date().getTime();

        if (startFromZero) {
            millis += currentTime;
        }

        Calendar calendar = Calendar.getInstance(LocaleHelper.PORTUGUES);
        calendar.setTimeInMillis(millis);

        int[] dateValues = new int[] {
            calendar.get(Calendar.DAY_OF_MONTH),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            calendar.get(Calendar.SECOND),
        };

        if (startFromZero) {
            int[] dateValues2 = DataHoraHelper.millisToDateValues(currentTime, false);

            for (int i = 0; i < dateValues.length; i++) {
                dateValues[i] = dateValues[i] - dateValues2[i];
            }
        }

        for (int at: dateValues) {
            System.out.println("" + at);
        }
        System.out.println("-------------");

        //String.format("%02d-%02d-%04d %02d:%02d:%02d", dia, mes, ano, hora, minuto, segundo);

        return dateValues;
    }

    // VERIFICA A HORA LOCAL COM A DA INTERNET
    public static void verificaHoraLocal(Activity activity, final long milisToUnidadeSelec, final long diffMaxDate, @NonNull final ListenerHoraLocal listenerHoraLocal) {
        InternetTime.getInternetTime(activity, new InternetTime.Listener() {
            @Override
            public void onTimeReceived(Date dateInternet) {
                try {
                    Date dateLocal = new Date();

                    long dateInternetMilis = dateInternet.getTime();
                    long dateLocalMilis = dateLocal.getTime();
                    long dateDiff = dateInternetMilis - dateLocalMilis;

                    long diffUnidadeSelecionada = dateDiff / milisToUnidadeSelec;
                    if (diffUnidadeSelecionada > diffMaxDate || diffUnidadeSelecionada < -diffMaxDate) {
                        listenerHoraLocal.onDateErrada(dateInternet, dateLocal);
                    } else {
                        listenerHoraLocal.onDateOk(dateInternet, dateLocal);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    listenerHoraLocal.onErro(e);
                }
            }

            @Override
            public void onTimeError(Throwable t) {
                listenerHoraLocal.onErro(t);
            }
        });
    }

    /**
     * CONVERTE A DATA PARA INTEIRO
     * @param strDate                   DATA EM FORMATO DE STRING
     * @return                          DATA EM FORMATO DE INTEGER
     * @throws NumberFormatException    EXCEPTION CASO HAJA
     */
    public static int dateToInt(String strDate) throws NumberFormatException {
        String strFormatada = strDate.replaceAll("[^0-9]+", "");

        //Log.d(TAG, "stringFormatada: " + strFormatada);

        return Integer.parseInt(strFormatada);
    }

    /**
     * CONVERTE A DATA PARA INTEIRO
     * @param strDate                   DATA EM FORMATO DE STRING
     * @return                          DATA EM FORMATO DE INTEGER
     * @throws NumberFormatException    EXCEPTION CASO HAJA
     */
    public static long dateToLong(String strDate) throws NumberFormatException {
        String strFormatada = strDate.replaceAll("[^0-9]+", "");

        //Log.d(TAG, "stringFormatada: " + strFormatada);

        return Long.parseLong(strFormatada);
    }

    /**
     * Compare two dates using day comparison of first date minus second date
     * @param date1 First date
     * @param date2 Second date
     * @return int days
     */
    public static long dateDaysDiff(Date date1, Date date2) {
        long days1 = (long) (date1.getTime() / (1000*60*60*24));
        long days2 = (long) (date2.getTime() / (1000*60*60*24));

        return days1 - days2;
    }

    /**
     * Compare two dates using hour comparison of first date minus second date
     * @param date1 First date
     * @param date2 Second date
     * @return int days
     */
    public static long dateHoursDiff(Date date1, Date date2) {
        long hours1 = (long) (date1.getTime() / (1000 * 60 * 60));
        long hours2 = (long) (date2.getTime() / (1000 * 60 * 60));

        return hours1 - hours2;
    }

    /**
     * Compare two dates using minute comparison of first date minus second date
     * @param date1 First date
     * @param date2 Second date
     * @return int days
     */
    public static long dateMinutesDiff(Date date1, Date date2) {
        long minutes1 = (long) (date1.getTime() / (1000 * 60));
        long minutes2 = (long) (date2.getTime() / (1000 * 60));

        return minutes1 - minutes2;
    }

    /**
     * CONVERTE A DATA PARA CALENDARIO
     * @param date  DATA A SER CONVERTIDA
     * @return      CALENDARIO
     */
    public static Calendar dateToCalendar(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    //função que retorna a data atual string
    public static String receberDataHoraAtualEnglish() {
        SimpleDateFormat formataData = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date data = new Date();
        String dataAtual = formataData.format(data);
        //Log.i("data_atual", dataFormatada);
        return dataAtual;
    }

    //função que retorna a data atual string
    public static String receberDataHoraAtualBR() {
        SimpleDateFormat formataData = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
        Date data = new Date();
        String dataAtual = formataData.format(data);
        //Log.i("data_atual", dataFormatada);
        return dataAtual;
    }

    public static String getTimestampDate(Date date) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);
        return timeStamp;
    }

    public interface ListenerHoraLocal {
        void onDateOk(Date dateInternet, Date dateLocal);

        void onDateErrada(Date dateInternet, Date dateLocal);

        void onErro(Throwable t);
    }
}
