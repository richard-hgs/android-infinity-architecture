package com.infinity.architecture.utils.log;

import android.util.Log;

public class LogUtils {
    private static final String TAG = "Log";

    public static void d(String message) {
        try {
            Thread thread = Thread.currentThread();
            StackTraceElement[] stackTraceElements = thread.getStackTrace();
            StackTraceElement stackTraceElement = stackTraceElements[3];
            int lineNumber = stackTraceElement.getLineNumber();
            String className = stackTraceElement.getClassName();
            String methodName = stackTraceElement.getMethodName();
            Class<?> myClass = stackTraceElement.getClass();
            String classSimpleName = myClass.getSimpleName();

            if (classSimpleName.length() > 19){
                classSimpleName = classSimpleName.substring(0, 19);
            }

            Log.d(classSimpleName, " \nMethod:"+methodName+"\n Line: " + lineNumber + "\n Message:\n");

            int maxLogSize = 2000;
            for (int i = 0; i <= message.length() / maxLogSize; i++) {
                int start = i * maxLogSize;
                int end = (i + 1) * maxLogSize;
                end = end > message.length() ? message.length() : end;
                Log.d(classSimpleName, message.substring(start, end));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void d2(String tag, String message) {
        try {

            int maxLogSize = 2000;
            for (int i = 0; i <= message.length() / maxLogSize; i++) {
                int start = i * maxLogSize;
                int end = (i + 1) * maxLogSize;
                end = end > message.length() ? message.length() : end;
                Log.d(tag, message.substring(start, end));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

//    public static void file(File logFile, String logMessage, String uploadUrl, boolean uploadEnabled) throws Exception {
//        Thread thread = Thread.currentThread();
//        StackTraceElement[] stackTraceElements = thread.getStackTrace();
//        StackTraceElement stackTraceElement = stackTraceElements[3];
//        int lineNumber = stackTraceElement.getLineNumber();
//        String className = stackTraceElement.getClassName();
//        String methodName = stackTraceElement.getMethodName();
//
//        String separador = "--------------------";
//
//        StringBuilder strLog = new StringBuilder();
//        // SEPARADOR INICIO
//        strLog.append(separador).append(Reutilizaveis.receberDataHoraAtualBR()).append(" (INICIO)").append(separador).append("\r\n");
//
//        strLog.append("className : ").append(className).append("\r\n");
//        strLog.append("methodName: ").append(methodName).append("(").append(lineNumber).append(")").append("\r\n");
//        strLog.append("message   : ").append("\r\n");
//
//        List<String> splitMsgSameLenght = StringHelper.splitEqually(logMessage, ((separador.length() * 2) + Reutilizaveis.receberDataHoraAtualBR().length() + 9));
//        for (String line : splitMsgSameLenght) {
//            strLog.append(line).append("\r\n");
//        }
//
//        // SEPARADOR FIM
//        strLog.append(separador).append(Reutilizaveis.receberDataHoraAtualBR()).append(" (FIM)").append(separador).append("---").append("\r\n\n");
//
//        FileHelper.createFolderOrFileIfNotExists(logFile);
//
//        int bufferSize = 1024;
//        char[] charBuff = new char[bufferSize];
//        StringBuilder previousFileStr = new StringBuilder();
//
//        // SE O ARQUIVO FOR MENOR DO QUE 1MB ENTÃO COPIA O CONTEUDO ANTERIOR
//        if (logFile.length() < 1000 * 1000) {
//            FileReader fileReader = new FileReader(logFile);
//            while (fileReader.read(charBuff, 0, bufferSize) != -1) {
//                previousFileStr.append(charBuff);
//            }
//            fileReader.close();
//        }
//
//        FileWriter fileWriter = new FileWriter(logFile, false);
//        fileWriter.append(strLog.toString());
//        fileWriter.append(previousFileStr.toString());
//        fileWriter.flush();
//        fileWriter.close();
//
//        if (uploadUrl != null && uploadEnabled) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        Reutilizaveis.uploadFileToUrl(uploadUrl, logFile.getPath());
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }).start();
//        }
//    }
}
