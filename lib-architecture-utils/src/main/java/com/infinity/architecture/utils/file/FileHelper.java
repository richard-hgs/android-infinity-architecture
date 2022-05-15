package com.infinity.architecture.utils.file;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FileHelper {
    private static final String TAG = "FileHelper";

    private static ArrayList<ProvideUriToPathTranslator> uriToPathTranslators = new ArrayList<>();

    // public static final File DEFAULT_LOG_FILE = new File((ApplicationHelper.getContext() != null ? ApplicationHelper.getContext().getFilesDir() : "") + "/Logs/LogHelperFile.txt");

    public static void createFolderOrFileIfNotExists(File file) throws Exception {
        String filePath = file.getPath();

        File folderFile = null;
        if (filePath.contains("/")) {
            String strFolder = filePath.substring(0, filePath.lastIndexOf("/"));
            folderFile = new File(strFolder);
        }

        if (folderFile != null && !folderFile.exists()) {

            //noinspection ResultOfMethodCallIgnored
            folderFile.mkdirs();
        }

        if (!file.exists()) {
            //noinspection ResultOfMethodCallIgnored
            file.createNewFile();
        }
    }

    public static void deleteFile(File file) throws Exception {
        if (file.exists()){
            file.delete();
        }
    }

    //android upload file to server
    public static String uploadFileToUrl(String SERVER_URL, String selectedFilePath, @Nullable HashMap<String, String> headers) throws Exception {
        return uploadFileToUrl(SERVER_URL, selectedFilePath, headers, null);
    }

    //android upload file to server
    public static String uploadFileToUrl(String SERVER_URL, String selectedFilePath, @Nullable HashMap<String, String> headers, @Nullable HashMap<String, String> postParams) throws Exception {
        int serverResponseCode = 0;
        String serverResponseMessage = "Desconhecido";

        HttpURLConnection connection;
        DataOutputStream dataOutputStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        int bytesRead,bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File selectedFile = new File(selectedFilePath);

        if (selectedFilePath.contains("database")) {
            selectedFilePath = selectedFilePath + ".db";
        }

        String[] parts = selectedFilePath.split("/");
        final String fileName = parts[parts.length-1];

        if (!selectedFile.isFile()){
            return "Caminho informado não é um arquivo!";
        } else {
            try{
                FileInputStream fileInputStream = new FileInputStream(selectedFile);
                URL url = new URL(SERVER_URL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);//Allow Inputs
                connection.setDoOutput(true);//Allow Outputs
                connection.setUseCaches(false);//Don't use a cached Copy
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
                connection.setRequestProperty("uploaded_file", selectedFilePath);

                // SE POSSUIR CABECALHOS ADICIONA OS CABECALHOS Á REQUISICAO
                if (headers != null) {
                    for(Map.Entry<String, String> header : headers.entrySet()) {
                        connection.setRequestProperty(header.getKey(), header.getValue());
                    }
                }

                // Log.d(TAG, "data:" + finalString);

                //creating new dataoutputstream
                dataOutputStream = new DataOutputStream(connection.getOutputStream());

                // SE POSSUIR POST PARAMS ADICIONA OS PARAMS Á REQUISICAO
                if (postParams != null) {
                    for(Map.Entry<String, String> postParam : postParams.entrySet()) {
                        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + postParam.getKey() + "\"" + lineEnd + lineEnd);
                        dataOutputStream.writeBytes(postParam.getValue() + lineEnd);
                        dataOutputStream.flush();
                    }
                }

                // FILE PARAMS
                //writing bytes to data outputstream
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + selectedFilePath + "\"" + lineEnd);
                dataOutputStream.writeBytes(lineEnd);

                //returns no. of bytes present in fileInputStream
                bytesAvailable = fileInputStream.available();
                //selecting the buffer size as minimum of available bytes or 1 MB
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                //setting the buffer as byte array of size of bufferSize
                buffer = new byte[bufferSize];

                //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
                bytesRead = fileInputStream.read(buffer,0,bufferSize);

                //loop repeats till bytesRead = -1, i.e., no bytes are left to read
                while (bytesRead > 0){
                    //write the bytes read from inputstream
                    dataOutputStream.write(buffer,0,bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable,maxBufferSize);
                    bytesRead = fileInputStream.read(buffer,0,bufferSize);
                }

                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                // FILE PARAMS

                serverResponseCode = connection.getResponseCode();
                serverResponseMessage = connection.getResponseMessage();

                Log.i(TAG, "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);

                //response code of 200 indicates the server status OK
                if(serverResponseCode == 200) {

                }

                StringBuilder stringBuilderResp = new StringBuilder();
                String line;
                BufferedReader bfReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while((line = bfReader.readLine()) != null) {
                    stringBuilderResp.append(line).append("\n");
                }

                if (stringBuilderResp.toString().length() > 0) {
                    serverResponseMessage += " - " + stringBuilderResp.toString();
                }
                Log.d(TAG, "resp: " + stringBuilderResp.toString());

                //closing the input and output streams
                fileInputStream.close();
                dataOutputStream.flush();
                dataOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();

            } catch (MalformedURLException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return serverResponseMessage;
        }

    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            connection.disconnect();
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void getFileFromURL(Context context, String src, @Nullable String jsonSend, ArrayList<String[]> headers, String method, String fileName, DownloadListener downloadListener) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json");
            if (headers != null) {
                for (int i = 0; i < headers.size(); i++) {
                    String[] headerAt = headers.get(i);
                    String property = headerAt[0];
                    String value = headerAt[1];
                    connection.setRequestProperty(property, value);
                }
            }
            connection.setRequestMethod(method.toUpperCase());
            if (jsonSend != null) {
                connection.setDoOutput(true);
            }
            connection.setDoInput(true);
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(30000);
            connection.connect();

            if (jsonSend != null) {
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(jsonSend.getBytes());
                outputStream.close();
            }

            ContextWrapper cw = new ContextWrapper(context);

            File fileToSave = new File(cw.getFilesDir(), fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(fileToSave);
            InputStream in = connection.getInputStream();
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                fileOutputStream.write(buf, 0, len);
            }

            connection.disconnect();

            if (downloadListener != null) {
                downloadListener.onResponse();
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (downloadListener != null) {
                downloadListener.onResponse();
            }
        }
    }

    public static File createEmptyFileInFilesDir(Context context, String filePath) throws Exception {
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getFilesDir();
        File mFilePath = new File(directory, filePath);
        createFolderOrFileIfNotExists(mFilePath);
        return mFilePath;
    }

    public static File createEmptyFile(String filePath) throws Exception {
        File mFilePath = new File(filePath);
        createFolderOrFileIfNotExists(mFilePath);
        return mFilePath;
    }

//    public static void copyFile(Context context, String sourceFilePath, String destFilePath) throws Exception {
//        File sourceFile = new File(sourceFilePath);
//        File destFile = new File(destFilePath);
//        createFolderOrFileIfNotExists(destFile);
//
//
//    }

    public static void copyFileOrDirectory(String srcDir, String dstDir) throws Exception {
        File src = new File(srcDir);
        File dst = new File(dstDir, src.getName());

        if (src.isDirectory()) {

            String[] files = src.list();
            if (files != null) {
                int filesLength = files.length;
                for (int i = 0; i < filesLength; i++) {
                    String src1 = (new File(src, files[i]).getPath());
                    String dst1 = dst.getPath();
                    copyFileOrDirectory(src1, dst1);
                }
            }
        } else {
            copyFile(src, dst);
        }
    }

    public static void copyFile(@NonNull File sourceFile, @NonNull File destFile) throws IOException {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }

    public static void copyFile(Context context, Uri sourceUri, String destFile) {
        try (InputStream ins = context.getContentResolver().openInputStream(sourceUri)) {
            File dest = new File(destFile);
            createFileFromStream(ins, dest);
        } catch (Exception ex) {
            Log.e("Save File", ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void createFileFromStream(InputStream ins, File destination) {
        try (OutputStream os = new FileOutputStream(destination)) {
            byte[] buffer = new byte[4096];
            int length;
            while ((length = ins.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            os.flush();
        } catch (Exception ex) {
            Log.e("Save File", ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return file.delete();
        } else {
            return false;
        }
    }

    public static String getUriPath(@NonNull Uri uri) {
        return FileHelper.getUriPath(uri, null);
    }

    public static String getUriPath(@NonNull Uri uri, @Nullable ProvideUriToPathTranslator uriToPathTranslator) {
        String keyContentText = "content://";
        String uriToParse = uri.toString();
        if (uriToParse.contains(keyContentText)) {
            int contentStartIndex = uriToParse.indexOf(keyContentText);
            int contentEndIndex = contentStartIndex + keyContentText.length();
            String lastPart = uriToParse.substring(contentEndIndex);
            String[] lastPartSplit = lastPart.split("/");
            if (lastPartSplit.length > 2) {
                String providerPackageName = lastPartSplit[0];
                String providerPathName = lastPartSplit[1];
                String pathInPath = "";
                for (int i = 2; i < lastPartSplit.length; i++) {
                    pathInPath += (i > 2 ? File.pathSeparator : "") + lastPartSplit[i];
                }

                if (uriToPathTranslator != null) {
                    String tempReturnPath = uriToPathTranslator.translateUriBasePath(keyContentText, providerPackageName, providerPathName, pathInPath);
                    if (tempReturnPath != null) {
                        return new File(tempReturnPath, pathInPath).getPath();
                    }
                }

                for (ProvideUriToPathTranslator providerAt : uriToPathTranslators) {
                    String tempReturnPath = providerAt.translateUriBasePath(keyContentText, providerPackageName, providerPathName, pathInPath);
                    if (tempReturnPath != null) {
                        return new File(tempReturnPath, pathInPath).getPath();
                    }
                }
            }
        }
        return null;
    }

    public static void addUriToPathTranslator(@NonNull ProvideUriToPathTranslator uriToPathTranslator) {
        FileHelper.uriToPathTranslators.add(uriToPathTranslator);
    }

//    public static boolean saveFile(Context context, String name, Uri sourceuri, String destinationDir, String destFileName) {
//
//        BufferedInputStream bis = null;
//        BufferedOutputStream bos = null;
//        InputStream input = null;
//        boolean hasError = false;
//
//        try {
//            if (isVirtualFile(context, sourceuri)) {
//                input = getInputStreamForVirtualFile(context, sourceuri, getMimeType(name));
//            } else {
//                input = context.getContentResolver().openInputStream(sourceuri);
//            }
//
//            boolean directorySetupResult;
//            File destDir = new File(destinationDir);
//            if (!destDir.exists()) {
//                directorySetupResult = destDir.mkdirs();
//            } else if (!destDir.isDirectory()) {
//                directorySetupResult = replaceFileWithDir(destinationDir);
//            } else {
//                directorySetupResult = true;
//            }
//
//            if (!directorySetupResult) {
//                hasError = true;
//            } else {
//                String destination = destinationDir + File.separator + destFileName;
//                int originalsize = input.available();
//
//                bis = new BufferedInputStream(input);
//                bos = new BufferedOutputStream(new FileOutputStream(destination));
//                byte[] buf = new byte[originalsize];
//                bis.read(buf);
//                do {
//                    bos.write(buf);
//                } while (bis.read(buf) != -1);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            hasError = true;
//        } finally {
//            try {
//                if (bos != null) {
//                    bos.flush();
//                    bos.close();
//                }
//            } catch (Exception ignored) {
//            }
//        }
//
//        return !hasError;
//    }
//
//    private static boolean isVirtualFile(Context context, Uri uri) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            if (!DocumentsContract.isDocumentUri(context, uri)) {
//                return false;
//            }
//            Cursor cursor = context.getContentResolver().query(
//                    uri,
//                    new String[]{DocumentsContract.Document.COLUMN_FLAGS},
//                    null, null, null);
//            int flags = 0;
//            if (cursor.moveToFirst()) {
//                flags = cursor.getInt(0);
//            }
//            cursor.close();
//            return (flags & DocumentsContract.Document.FLAG_VIRTUAL_DOCUMENT) != 0;
//        } else {
//            return false;
//        }
//    }
//
//    private static InputStream getInputStreamForVirtualFile(Context context, Uri uri, String mimeTypeFilter)
//            throws IOException {
//
//        ContentResolver resolver = context.getContentResolver();
//        String[] openableMimeTypes = resolver.getStreamTypes(uri, mimeTypeFilter);
//        if (openableMimeTypes == null || openableMimeTypes.length < 1) {
//            throw new FileNotFoundException();
//        }
//        return resolver
//                .openTypedAssetFileDescriptor(uri, openableMimeTypes[0], null)
//                .createInputStream();
//    }
//
//    private static String getMimeType(String url) {
//        String type = null;
//        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
//        if (extension != null) {
//            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
//        }
//        return type;
//    }
//
//    private static boolean replaceFileWithDir(String path) {
//        File file = new File(path);
//        if (!file.exists()) {
//            if (file.mkdirs()) {
//                return true;
//            }
//        } else if (file.delete()) {
//            File folder = new File(path);
//            if (folder.mkdirs()) {
//                return true;
//            }
//        }
//        return false;
//    }

    @Nullable
    public static String storeImage(Context context, Bitmap image, String imgName) {
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getFilesDir();
        if (!directory.exists()) {
            directory.mkdir();
        }

        File pictureFile = new File(directory, imgName);
        if (imgName.contains("/")) {
            // File dirPart
            File pictureDir = pictureFile.getParentFile();
            if (pictureDir != null && !pictureDir.exists()) {
                pictureDir.mkdirs();
            }
        }

        if (pictureFile == null) {
            Log.d(TAG, "Error creating media file, check storage permissions: ");// e.getMessage());
            return null;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pictureFile.getPath();
    }

    public static Bitmap loadImage(Context context, String imgName) {
        Bitmap bitmap = null;
        try {
            ContextWrapper cw = new ContextWrapper(context);
            File path1 = cw.getFilesDir();
            File f = new File(path1, imgName);
            bitmap = BitmapFactory.decodeStream(new FileInputStream(f));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public static File loadFile(Context context, String fileName) {
        File f = null;

        ContextWrapper cw = new ContextWrapper(context);
        File path1 = cw.getFilesDir();
        f = new File(path1, fileName);

        return f;
    }

    public static byte[] fileToBytes(File file) throws IOException {
        int size = (int) file.length();
        byte[] bytes = new byte[size];
        BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
        buf.read(bytes, 0, bytes.length);
        buf.close();

        return bytes;
    }

    public interface DownloadListener {
        void onResponse();
    }

    public interface ProvideUriToPathTranslator {
        @Nullable
        String translateUriBasePath(@NonNull String startKey, @NonNull String fileProviderPackageName, @NonNull String providerPathName, @NonNull String pathInPath);
    }










    private static Uri contentUri = null;

    @SuppressLint("NewApi")
    public static String getPath(Context context, final Uri uri) {
        // check here to KITKAT or new version
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        String selection = null;
        String[] selectionArgs = null;
        // DocumentProvider
        if (isKitKat) {
            // ExternalStorageProvider

            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                String fullPath = getPathFromExtSD(split);
                if (fullPath != "") {
                    return fullPath;
                } else {
                    return null;
                }
            }


            // DownloadsProvider

            if (isDownloadsDocument(uri)) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    final String id;
                    Cursor cursor = null;
                    try {
                        cursor = context.getContentResolver().query(uri, new String[]{MediaStore.MediaColumns.DISPLAY_NAME}, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            String fileName = cursor.getString(0);
                            String path = Environment.getExternalStorageDirectory().toString() + "/Download/" + fileName;
                            if (!TextUtils.isEmpty(path)) {
                                return path;
                            }
                        }
                    }
                    finally {
                        if (cursor != null)
                            cursor.close();
                    }
                    id = DocumentsContract.getDocumentId(uri);
                    if (!TextUtils.isEmpty(id)) {
                        if (id.startsWith("raw:")) {
                            return id.replaceFirst("raw:", "");
                        }
                        String[] contentUriPrefixesToTry = new String[]{
                                "content://downloads/public_downloads",
                                "content://downloads/my_downloads"
                        };
                        for (String contentUriPrefix : contentUriPrefixesToTry) {
                            try {
                                final Uri contentUri = ContentUris.withAppendedId(Uri.parse(contentUriPrefix), Long.valueOf(id));


                                return getDataColumn(context, contentUri, null, null);
                            } catch (NumberFormatException e) {
                                //In Android 8 and Android P the id is not a number
                                return uri.getPath().replaceFirst("^/document/raw:", "").replaceFirst("^raw:", "");
                            }
                        }


                    }
                } else {
                    final String id = DocumentsContract.getDocumentId(uri);

                    if (id.startsWith("raw:")) {
                        return id.replaceFirst("raw:", "");
                    }
                    try {
                        contentUri = ContentUris.withAppendedId(
                                Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    if (contentUri != null) {
                        return getDataColumn(context, contentUri, null, null);
                    }
                }
            }


            // MediaProvider
            if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;

                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{split[1]};


                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }

            if (isGoogleDriveUri(uri)) {
                return getDriveFilePath(context, uri);
            }

            if(isWhatsAppFile(uri)){
                return getFilePathForWhatsApp(context, uri);
            }


            if ("content".equalsIgnoreCase(uri.getScheme())) {

                if (isGooglePhotosUri(uri)) {
                    return uri.getLastPathSegment();
                }
                if (isGoogleDriveUri(uri)) {
                    return getDriveFilePath(context, uri);
                }
                if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                {

                    // return getFilePathFromURI(context,uri);
                    return copyFileToInternalStorage(context, uri,"userfiles");
                    // return getRealPathFromURI(context,uri);
                }
                else
                {
                    return getDataColumn(context, uri, null, null);
                }

            }
            if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        }
        else {

            if(isWhatsAppFile(uri)){
                return getFilePathForWhatsApp(context, uri);
            }

            if ("content".equalsIgnoreCase(uri.getScheme())) {
                String[] projection = {
                        MediaStore.Images.Media.DATA
                };
                Cursor cursor = null;
                try {
                    cursor = context.getContentResolver()
                            .query(uri, projection, selection, selectionArgs, null);
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    if (cursor.moveToFirst()) {
                        return cursor.getString(column_index);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    private static boolean fileExists(String filePath) {
        File file = new File(filePath);

        return file.exists();
    }

    private static String getPathFromExtSD(String[] pathData) {
        final String type = pathData[0];
        final String relativePath = "/" + pathData[1];
        String fullPath = "";

        // on my Sony devices (4.4.4 & 5.1.1), `type` is a dynamic string
        // something like "71F8-2C0A", some kind of unique id per storage
        // don't know any API that can get the root path of that storage based on its id.
        //
        // so no "primary" type, but let the check here for other devices
        if ("primary".equalsIgnoreCase(type)) {
            fullPath = Environment.getExternalStorageDirectory() + relativePath;
            if (fileExists(fullPath)) {
                return fullPath;
            }
        }

        // Environment.isExternalStorageRemovable() is `true` for external and internal storage
        // so we cannot relay on it.
        //
        // instead, for each possible path, check if file exists
        // we'll start with secondary storage as this could be our (physically) removable sd card
        fullPath = System.getenv("SECONDARY_STORAGE") + relativePath;
        if (fileExists(fullPath)) {
            return fullPath;
        }

        fullPath = System.getenv("EXTERNAL_STORAGE") + relativePath;
        if (fileExists(fullPath)) {
            return fullPath;
        }

        return fullPath;
    }

    private static String getDriveFilePath(Context context, Uri uri) {
        Uri returnUri = uri;
        Cursor returnCursor = context.getContentResolver().query(returnUri, null, null, null, null);
        /*
         * Get the column indexes of the data in the Cursor,
         *     * move to the first row in the Cursor, get the data,
         *     * and display it.
         * */
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        String name = (returnCursor.getString(nameIndex));
        String size = (Long.toString(returnCursor.getLong(sizeIndex)));
        File file = new File(context.getCacheDir(), name);
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(file);
            int read = 0;
            int maxBufferSize = 1 * 1024 * 1024;
            int bytesAvailable = inputStream.available();

            //int bufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);

            final byte[] buffers = new byte[bufferSize];
            while ((read = inputStream.read(buffers)) != -1) {
                outputStream.write(buffers, 0, read);
            }
            Log.e("File Size", "Size " + file.length());
            inputStream.close();
            outputStream.close();
            Log.e("File Path", "Path " + file.getPath());
            Log.e("File Size", "Size " + file.length());
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
        return file.getPath();
    }

    /***
     * Used for Android Q+
     * @param uri
     * @param newDirName if you want to create a directory, you can set this variable
     * @return
     */
    private static String copyFileToInternalStorage(Context context, Uri uri, String newDirName) {
        Uri returnUri = uri;

        Cursor returnCursor = context.getContentResolver().query(returnUri, new String[]{
                OpenableColumns.DISPLAY_NAME,OpenableColumns.SIZE
        }, null, null, null);


        /*
         * Get the column indexes of the data in the Cursor,
         *     * move to the first row in the Cursor, get the data,
         *     * and display it.
         * */
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        String name = (returnCursor.getString(nameIndex));
        String size = (Long.toString(returnCursor.getLong(sizeIndex)));

        File output;
        if(!newDirName.equals("")) {
            File dir = new File(context.getFilesDir() + "/" + newDirName);
            if (!dir.exists()) {
                dir.mkdir();
            }
            output = new File(context.getFilesDir() + "/" + newDirName + "/" + name);
        }
        else{
            output = new File(context.getFilesDir() + "/" + name);
        }
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(output);
            int read = 0;
            int bufferSize = 1024;
            final byte[] buffers = new byte[bufferSize];
            while ((read = inputStream.read(buffers)) != -1) {
                outputStream.write(buffers, 0, read);
            }

            inputStream.close();
            outputStream.close();

        }
        catch (Exception e) {

            Log.e("Exception", e.getMessage());
        }

        return output.getPath();
    }

    private static String getFilePathForWhatsApp(Context context, Uri uri) {
        return  copyFileToInternalStorage(context, uri,"whatsapp");
    }

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);

            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        }
        finally {
            if (cursor != null)
                cursor.close();
        }

        return null;
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static boolean isWhatsAppFile(Uri uri){
        return "com.whatsapp.provider.media".equals(uri.getAuthority());
    }

    private static boolean isGoogleDriveUri(Uri uri) {
        return "com.google.android.apps.docs.storage".equals(uri.getAuthority()) || "com.google.android.apps.docs.storage.legacy".equals(uri.getAuthority());
    }
}
