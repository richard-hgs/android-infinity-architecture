package com.infinity.architecture.utils.backservices.api;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Method;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.gsonparserfactory.GsonParserFactory;
import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.DownloadProgressListener;
import com.androidnetworking.interfaces.OkHttpResponseListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.infinity.architecture.utils.backservices.Resource;
import com.infinity.architecture.utils.backservices.api.utils.CustomGsonTypeAdapterFactory;
import com.infinity.architecture.utils.reflection.TypeWrapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.org.json.JSONObject;
import com.org.json.XML;

import org.conscrypt.Conscrypt;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.security.Security;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;

public class ApiServiceBase {
    private static final String TAG = "ApiServiceBase";

    // CONNECT TIMEOUT IN SECCONDS
    private static long CONNECT_TIMEOUT = 60;
    private static long WRITE_TIMEOUT = CONNECT_TIMEOUT;
    private static long READ_TIMEOUT = CONNECT_TIMEOUT;
    private static long CALL_TIMEOUT = CONNECT_TIMEOUT;
    private static boolean PRINT_EXCEPTIONS = true;
    private static Gson gson = buildGson();

    /**
     * Initialize the api service.
     * Must be called inside the onCreate method of the Application class
     * @param appContext    The application context
     */
    public static void initializeApiService(Context appContext) {
        // Enable support for TLS_V1.3
        Security.insertProviderAt(Conscrypt.newProvider(), 1);

        AndroidNetworking.initialize(appContext, buildOkHttpClient());
        AndroidNetworking.setParserFactory(new GsonParserFactory(gson));

        // Gson gson = new GsonBuilder().registerTypeAdapterFactory();
    }

    private <T2> void processJsonObjectRequestResponse(
        Class<T2> responseClass,
        String response,
        @NonNull RequestType requestType,
        @NonNull ApiServiceBase.RequestListener<T2> requestListener
    ) {
        Exception exception = null;
        T2 respObj = null;
        String requestErrorMsg = getRequestErrorMsg(response);

        try {
            if (responseClass == String.class || requestType == RequestType.HTML) {
                //noinspection unchecked
                respObj = (T2) response;
            } else if (requestType == RequestType.JSON) {
                respObj = gson.fromJson(response, responseClass);
            } else if (requestType == RequestType.X_FORM_URL_ENCODED) {
//                ArrayList<RegexMatchInfo> regexMatchInfos = RegexUtils.searchPatternInStr(
//                    "\\<+\\?+[a-zA-Z\\\"\\s=0-9.-]*\\?+\\>+",
//                    response
//                );
//
//                if (regexMatchInfos.size() > 0) {
//                    int replacedLength = 0;
//                    for(int i=0; i<regexMatchInfos.size(); i++) {
//                        RegexMatchInfo regexMatchInfoAt = regexMatchInfos.get(i);
//                        Log.d(TAG, "RespFieldsReplaced: " + regexMatchInfoAt.getValue() + " - start:" + regexMatchInfoAt.getStart() + " - end:" + regexMatchInfoAt.getEnd());
//
//                        response = StringUtils.replaceByIndex(regexMatchInfoAt.getStart() - replacedLength, regexMatchInfoAt.getEnd() - replacedLength, response, "");
//
//                        replacedLength = regexMatchInfoAt.getValue().length();
//                    }
//                }
                JSONObject jsonObjectResponse = XML.toJSONObject(response);
                respObj = gson.fromJson(jsonObjectResponse.toString(), responseClass);
            } else {
                throw new Exception("RequestType - " + requestType + " is not implemented.");
            }
        } catch (Exception e) {
            exception = e;
            if (PRINT_EXCEPTIONS) {
                e.printStackTrace();
            }
        }

        if (respObj != null) {
            if (respObj instanceof BaseRequestResponse) {
                BaseRequestResponse baseRespObj = (BaseRequestResponse) respObj;
                // Log.d(TAG, "className: " + baseRespObj + " - " + baseRespObj.getStatus());
                if (baseRespObj.getStatus() != null && baseRespObj.getStatus() == 200) {
                    requestListener.onResponse(response, respObj);
                } else {
                    String strErrorMsg = "Não foi possível converter a resposta da requisição no Objeto especificado. Algum parâmetro do JSON está incorreto.";
                    if (requestErrorMsg != null) {
                        strErrorMsg = requestErrorMsg;
                    }
                    String[] errorMsgs = buildErrorMsgs(null, strErrorMsg);

                    requestListener.onError(errorMsgs[0], errorMsgs[1], null);
                }
            } else {
                requestListener.onResponse(response, respObj);
            }
        } else {
            String strErrorMsg = "Não foi possível converter a resposta da requisição no Objeto especificado. Algum parâmetro do JSON está incorreto.";
            if (requestErrorMsg != null) {
                strErrorMsg = requestErrorMsg;
            }
            String[] errorMsgs = buildErrorMsgs(exception, strErrorMsg);

            requestListener.onError(errorMsgs[0], errorMsgs[1], null);
        }
    }

    private <T2> void onAnRequestError(boolean respOnThread, ANError anError, TypeWrapper<Boolean> respReceived, TypeWrapper<Exception> exception, @NonNull ApiServiceBase.RequestListener<T2> requestListener) {
        try {
            if (respOnThread) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String[] errorMsgs = buildErrorMsgs(anError.getCause(), "Ops não foi possível receber a resposta da requisição.");

                            requestListener.onError(errorMsgs[0], errorMsgs[1], anError);
                            respReceived.setData(true);
                        } catch (Exception e) {
                            respReceived.setData(true);
                            exception.setData(e);
                        }
                    }
                }).start();
            } else {
                String[] errorMsgs = buildErrorMsgs(anError.getCause(), "Ops não foi possível receber a resposta da requisição.");

                Log.d(TAG, "ANError: " + anError.getErrorDetail() + " - " + anError.getErrorCode());
                anError.printStackTrace();

                requestListener.onError(errorMsgs[0], errorMsgs[1], anError);
                respReceived.setData(true);
            }
        } catch (Exception e) {
            respReceived.setData(true);
            exception.setData(e);
        }
    }

    private <T2> void onAnRequestResponse(boolean respOnThread, String response, Class<T2> responseClass, RequestType requestType, TypeWrapper<Boolean> respReceived, TypeWrapper<Exception> exception, @NonNull ApiServiceBase.RequestListener<T2> requestListener) {
        try {
            if (respOnThread) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            processJsonObjectRequestResponse(responseClass, response, requestType, requestListener);
                            respReceived.setData(true);
                        } catch (Exception e) {
                            respReceived.setData(true);
                            exception.setData(e);
                        }
                    }
                }).start();
            } else {
                processJsonObjectRequestResponse(responseClass, response, requestType, requestListener);
                respReceived.setData(true);
            }
        } catch (Exception e) {
            respReceived.setData(true);
            exception.setData(e);
        }
    }

    protected <T, T2> void objectRequest(
        ApiServiceRequestMethod method,
        @NonNull String tag,
        @NonNull String url,
        @Nullable T bodyParameter,
        @Nullable Map<String, String> queryParameter,
        @Nullable Map<String, String> headers,
        @NonNull Class<T2> responseClass,
        boolean respOnThread,
        @NonNull RequestType requestType,
        @NonNull ApiServiceBase.RequestListener<T2> requestListener
    ) {
        TypeWrapper<Boolean> respReceived = TypeWrapper.getInstance(false);
        TypeWrapper<Exception> exception = TypeWrapper.getInstance(null);

        ANRequest.DynamicRequestBuilder requestBuilder = AndroidNetworking.request(url, method == ApiServiceRequestMethod.GET ? Method.GET : Method.POST);
        requestBuilder.setTag(tag);
        if (bodyParameter != null) {
            if (bodyParameter instanceof String) {
                requestBuilder.addStringBody((String) bodyParameter);
            } else if (requestType == RequestType.JSON) {
                requestBuilder.addStringBody(gson.toJson(bodyParameter));
            } else if (requestType == RequestType.X_FORM_URL_ENCODED) {
                try {
                    String strGson = gson.toJson(bodyParameter);
                    JSONObject jsonObject = new JSONObject(strGson);
                    requestBuilder.addStringBody(XML.toString(jsonObject));
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            } else {
                throw new RuntimeException("RequestType - " + requestType + " is not implemented.");
            }
        }
        if (queryParameter != null) {
            requestBuilder.addQueryParameter(queryParameter);
        }
        requestBuilder.addHeaders("Connection", "keep-alive");
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                if (entry.getKey().toLowerCase().equals("content-type")) {
                    requestBuilder.setContentType(entry.getValue());
                } else {
                    requestBuilder.addHeaders(entry.getKey(), entry.getValue());
                }
            }
        }
        requestBuilder.setPriority(Priority.MEDIUM);
        ANRequest<?> anRequest =  requestBuilder.build();

        if (requestType == RequestType.JSON || requestType == RequestType.X_FORM_URL_ENCODED) {
            anRequest.getAsString(new StringRequestListener() {
                @Override
                public void onResponse(String response) {
                    onAnRequestResponse(respOnThread, response, responseClass, requestType, respReceived, exception, requestListener);
//                    try {
//                        if (respOnThread) {
//                            new Thread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    try {
//                                        processJsonObjectRequestResponse(responseClass, response, requestType, requestListener);
//                                        respReceived.setData(true);
//                                    } catch (Exception e) {
//                                        respReceived.setData(true);
//                                        exception.setData(e);
//                                    }
//                                }
//                            }).start();
//                        } else {
//                            processJsonObjectRequestResponse(responseClass, response, requestType, requestListener);
//                            respReceived.setData(true);
//                        }
//                    } catch (Exception e) {
//                        respReceived.setData(true);
//                        exception.setData(e);
//                    }
                }

                @Override
                public void onError(ANError anError) {
                    onAnRequestError(respOnThread, anError, respReceived, exception, requestListener);
//                    try {
//                        if (respOnThread) {
//                            new Thread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    try {
//                                        String[] errorMsgs = buildErrorMsgs(anError.getCause(), "Ops não foi possível receber a resposta da requisição.");
//
//                                        requestListener.onError(errorMsgs[0], errorMsgs[1], anError);
//                                        respReceived.setData(true);
//                                    } catch (Exception e) {
//                                        respReceived.setData(true);
//                                        exception.setData(e);
//                                    }
//                                }
//                            }).start();
//                        } else {
//                            String[] errorMsgs = buildErrorMsgs(anError.getCause(), "Ops não foi possível receber a resposta da requisição.");
//
//                            requestListener.onError(errorMsgs[0], errorMsgs[1], anError);
//                            respReceived.setData(true);
//                        }
//                    } catch (Exception e) {
//                        respReceived.setData(true);
//                        exception.setData(e);
//                    }
                }
            });
        } else {
            anRequest.getAsOkHttpResponse(new OkHttpResponseListener() {
                @Override
                public void onResponse(Response response) {
                    try {
                        String strResponse = "";
                        if (response != null) {
                            ResponseBody responseBody = response.body();
                            if (responseBody != null) {
                                strResponse = responseBody.string();
                            }
                        }

                        onAnRequestResponse(respOnThread, strResponse, responseClass, requestType, respReceived, exception, requestListener);
                    } catch (Exception e) {
                        respReceived.setData(true);
                        exception.setData(e);
                    }
                }

                @Override
                public void onError(ANError anError) {
                    onAnRequestError(respOnThread, anError, respReceived, exception, requestListener);
                }
            });
        }

        try {
            // Lock main thread until receive a response
            while (!(respReceived.getData())) {
                //noinspection BusyWait
                Thread.sleep(50);
            }
        } catch(InterruptedException e) {
            Log.d(TAG, "Request interrupted -> TAG: " + tag);
            anRequest.cancel(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (exception.getData() != null) {
            throw new RuntimeException(exception.getData());
        }
    }

    protected <T, T2> Single<Resource<T2>> buildSingleJsonObjectRequest(
            ApiServiceRequestMethod method,
            @NonNull String tag,
            @NonNull String url,
            @Nullable T bodyParameter,
            @NonNull Class<T2> responseClass
    ) {
        return buildSingleJsonObjectRequest(method, tag, url, bodyParameter, null, responseClass);
    }

    protected <T, T2> Single<Resource<T2>> buildSingleJsonObjectRequest(
            ApiServiceRequestMethod method,
            @NonNull String tag,
            @NonNull String url,
            @Nullable T bodyParameter,
            @Nullable HashMap<String, String> queryParameter,
            @NonNull Class<T2> responseClass
    ) {
        return buildSingleObjectRequest(method, tag, url, bodyParameter, queryParameter, null, responseClass, RequestType.JSON);
    }

    protected <T, T2> Single<Resource<T2>> buildSingleObjectRequest(
            ApiServiceRequestMethod method,
            @NonNull String tag,
            @NonNull String url,
            @Nullable T bodyParameter,
            @Nullable HashMap<String, String> queryParameter,
            @Nullable HashMap<String, String> headers,
            @NonNull Class<T2> responseClass,
            @NonNull RequestType requestType
    ) {
        return Single.create(new SingleOnSubscribe<Resource<T2>>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Resource<T2>> emitter) throws Exception {
                objectRequest(
                        method, tag, url, bodyParameter, queryParameter, headers, responseClass, false, requestType, new ApiServiceBase.RequestListener<T2>() {
                            @Override
                            public void onResponse(String strResponse, T2 objResponse) {
                                emitter.onSuccess(Resource.success(objResponse));
                            }

                            @Override
                            public void onError(@NonNull String strErrorMsg, @Nullable String strErrorFull, @Nullable ANError anError) {
                                emitter.onSuccess(requestErrorToResource(strErrorMsg, strErrorFull, anError));
                            }

                        @Override
                        public void downloadProgressListener(long bytesDownloaded, long totalBytes) {

                        }
                    }
                );
            }
        });
    }

    protected<T, T2> void buildListenerJsonObjectRequest(
            ApiServiceRequestMethod method,
            @NonNull String tag,
            @NonNull String url,
            @Nullable T bodyParameter,
            @NonNull Class<T2> responseClass,
            boolean respOnThread,
            ApiServiceListener<T2> listener
    ) {
        buildListenerObjectRequest(method, tag, url, bodyParameter, null, null, responseClass, respOnThread, RequestType.JSON, listener);
    }

    protected<T, T2> void buildListenerJsonObjectRequest(
            ApiServiceRequestMethod method,
            @NonNull String tag,
            @NonNull String url,
            @Nullable T bodyParameter,
            @Nullable Map<String, String> queryParameter,
            @NonNull Class<T2> responseClass,
            boolean respOnThread,
            ApiServiceListener<T2> listener
    ) {
        buildListenerObjectRequest(method, tag, url, bodyParameter, queryParameter, null, responseClass, respOnThread, RequestType.JSON, listener);
    }

    protected<T, T2> void buildListenerObjectRequest(
            ApiServiceRequestMethod method,
            @NonNull String tag,
            @NonNull String url,
            @Nullable T bodyParameter,
            @Nullable Map<String, String> queryParameter,
            @Nullable Map<String, String> headers,
            @NonNull Class<T2> responseClass,
            boolean respOnThread,
            @NonNull RequestType requestType,
            ApiServiceListener<T2> listener
    ) {
        objectRequest(
            method, tag, url, bodyParameter, queryParameter, headers, responseClass, respOnThread, requestType, new ApiServiceBase.RequestListener<T2>() {
                @Override
                public void onResponse(String strResponse, T2 objResponse) {
                    listener.onResponse(Resource.success(objResponse));
                    // emitter.onSuccess(Resource.success(objResponse));
                }

                @Override
                public void onError(@NonNull String strErrorMsg, @Nullable String strErrorFull, @Nullable ANError anError) {
                    listener.onResponse(requestErrorToResource(strErrorMsg, strErrorFull, anError));
                }

                @Override
                public void downloadProgressListener(long bytesDownloaded, long totalBytes) {

                }
            }
        );
    }

    protected <S, R> void buildDisposableRequestOrThreadRequest(@Nullable CompositeDisposable disposable, ApiServiceRequestMethod method, String tag, String url, S sendObj, Class<R> respClass, boolean respOnThread, @NonNull BiConsumer<Resource<R>, Throwable> listener) {
        buildDisposableRequestOrThreadRequest(disposable, method, tag, url, sendObj, null, null, respClass, respOnThread, RequestType.JSON, listener);
    }

    protected <S, R> void buildDisposableRequestOrThreadRequest(@Nullable CompositeDisposable disposable, ApiServiceRequestMethod method, String tag, String url, S sendObj, @Nullable HashMap<String, String> queryParams, @Nullable HashMap<String, String> headers, Class<R> respClass, boolean respOnThread, @NonNull BiConsumer<Resource<R>, Throwable> listener) {
        buildDisposableRequestOrThreadRequest(disposable, method, tag, url, sendObj, queryParams, headers, respClass, respOnThread, RequestType.JSON, listener);
    }

    protected <S, R> void buildDisposableRequestOrThreadRequest(@Nullable CompositeDisposable disposable, ApiServiceRequestMethod method, String tag, String url, S sendObj, @Nullable HashMap<String, String> queryParams, @Nullable HashMap<String, String> headers, Class<R> respClass, boolean respOnThread, @NonNull RequestType requestType, @NonNull BiConsumer<Resource<R>, Throwable> listener) {
        if (disposable != null) {
            disposable.add(
                Single.create(new SingleOnSubscribe<Resource<R>>() {
                    @Override
                    public void subscribe(@NotNull SingleEmitter<Resource<R>> emitter) throws Exception {
                        buildListenerObjectRequest(method, tag, url, sendObj, queryParams, headers, respClass, false, requestType, new ApiServiceListener<R>() {
                            @Override
                            public void onResponse(Resource<R> resource) {
                                emitter.onSuccess(resource);
                            }
                        });
                    }
                }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BiConsumer<Resource<R>, Throwable>() {
                    @Override
                    public void accept(Resource<R> rResource, Throwable throwable) throws Exception {
                        if (respOnThread) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        listener.accept(rResource, throwable);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        } else {
                            listener.accept(rResource, throwable);
                        }
                    }
                })
            );
        } else {
            buildListenerObjectRequest(method, tag, url, sendObj, queryParams, headers, respClass, respOnThread, requestType, new ApiServiceListener<R>() {
                @Override
                public void onResponse(Resource<R> resource) {
                    try {
                        listener.accept(resource, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    protected <T> void downloadRequest(
        ApiServiceRequestMethod method,
        @NonNull String tag,
        @NonNull String url,
        @NonNull String fileDirToSave,
        @NonNull String fileNameToSave,
        @Nullable T bodyParameter,
        @Nullable Map<String, String> queryParameter,
        @Nullable Map<String, String> headers,
        boolean respOnThread,
        @NonNull ApiServiceBase.RequestListener<File> requestListener
    ) {
        TypeWrapper<Boolean> respReceived = TypeWrapper.getInstance(false);
        TypeWrapper<Exception> exception = TypeWrapper.getInstance(null);

        AndroidNetworking.download(url, fileDirToSave, fileNameToSave)
            .setTag(tag)
            .setPriority(Priority.MEDIUM)
            .build()
            .setDownloadProgressListener(new DownloadProgressListener() {
                @Override
                public void onProgress(long bytesDownloaded, long totalBytes) {
                    // do anything with progress
                }
            })
            .startDownload(new DownloadListener() {
                @Override
                public void onDownloadComplete() {
                    // do anything after completion
                    try {
                        if (respOnThread) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        File file = new File(fileDirToSave, fileNameToSave);
                                        requestListener.onResponse(null, file);
                                        respReceived.setData(true);
                                    } catch (Exception e) {
                                        respReceived.setData(true);
                                        exception.setData(e);
                                    }
                                }
                            }).start();
                        } else {
                            File file = new File(fileDirToSave, fileNameToSave);
                            requestListener.onResponse(null, file);
                            respReceived.setData(true);
                        }
                    } catch (Exception e) {
                        respReceived.setData(true);
                        exception.setData(e);
                    }
                }
                @Override
                public void onError(ANError anError) {
                    // handle error
                    onAnRequestError(respOnThread, anError, respReceived, exception, requestListener);
                }
            });
    }

    protected<T> void buildListenerDownloadRequest(
        ApiServiceRequestMethod method,
        @NonNull String tag,
        @NonNull String url,
        @NonNull String fileDirToSave,
        @NonNull String fileNameToSave,
        @Nullable T bodyParameter,
        @Nullable Map<String, String> queryParameter,
        @Nullable Map<String, String> headers,
        boolean respOnThread,
        ApiServiceListener<File> listener
    ) {
        downloadRequest(
            method, tag, url, fileDirToSave, fileNameToSave, bodyParameter, queryParameter, headers, respOnThread, new ApiServiceBase.RequestListener<File>() {
                @Override
                public void onResponse(String strResponse, File objResponse) {
                    listener.onResponse(Resource.success(objResponse));
                }

                @Override
                public void onError(@NonNull String strErrorMsg, @Nullable String strErrorFull, @Nullable ANError anError) {
                    listener.onResponse(requestErrorToResource(strErrorMsg, strErrorFull, anError));
                }

                @Override
                public void downloadProgressListener(long bytesDownloaded, long totalBytes) {

                }
            }
        );
    }

    protected <S> void buildDisposableDownloadRequestOrThreadDownloadRequest(
        @Nullable CompositeDisposable disposable,
        ApiServiceRequestMethod method,
        String tag,
        String url,
        @NonNull String fileDirToSave,
        @NonNull String fileNameToSave,
        S sendObj,
        @Nullable HashMap<String, String> queryParams,
        @Nullable HashMap<String, String> headers,
        boolean respOnThread,
        @NonNull BiConsumer<Resource<File>, Throwable> listener
    ) {
        if (disposable != null) {
            disposable.add(
                Single.create(new SingleOnSubscribe<Resource<File>>() {
                    @Override
                    public void subscribe(@NotNull SingleEmitter<Resource<File>> emitter) throws Exception {
                        buildListenerDownloadRequest(method, tag, url, fileDirToSave, fileNameToSave, sendObj, queryParams, headers, false, new ApiServiceListener<File>() {
                            @Override
                            public void onResponse(Resource<File> resource) {
                                emitter.onSuccess(resource);
                            }
                        });
                    }
                }).subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BiConsumer<Resource<File>, Throwable>() {
                        @Override
                        public void accept(Resource<File> rResource, Throwable throwable) throws Exception {
                            if (respOnThread) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            listener.accept(rResource, throwable);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();
                            } else {
                                listener.accept(rResource, throwable);
                            }
                        }
                    })
            );
        } else {
            buildListenerDownloadRequest(method, tag, url, fileDirToSave, fileNameToSave, sendObj, queryParams, headers, respOnThread, new ApiServiceListener<File>() {
                @Override
                public void onResponse(Resource<File> resource) {
                    try {
                        listener.accept(resource, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private static <T> Resource<T> requestErrorToResource(@NonNull String errorMsg, @Nullable String errorMsgFull, @Nullable ANError anError) {
        return Resource.error(errorMsg, errorMsgFull, anError != null ? anError.getErrorBody() : null, anError != null ? anError.getCause() : null);
    }

    @Nullable
    private static String getRequestErrorMsg(@Nullable String requestResponse) {
        String requestErrorMsg = null;
        try {
            if (requestResponse != null) {
                if (requestResponse.toLowerCase().contains("failed to connect to") || requestResponse.toLowerCase().contains("after " + CONNECT_TIMEOUT + "ms") || requestResponse.toLowerCase().contains("connection timed out") || requestResponse.toLowerCase().contains("unable to resolve host")) {
                    requestErrorMsg = "Sem conexão a internet ou internet lenta!";
                }

                JSONObject jsonObject = new JSONObject(requestResponse);
                if (jsonObject.has("retorno")) {
                    requestErrorMsg = jsonObject.getString("retorno");
                } else if (jsonObject.has("msn")) {
                    requestErrorMsg = jsonObject.getString("msn");
                } else if (jsonObject.has("message")) {
                    requestErrorMsg = jsonObject.getString("message");
                }
            }
        } catch (Exception e) {

        }

        return requestErrorMsg;
    }

    private static String[] buildErrorMsgs(@Nullable Throwable throwable, @NonNull String errorMsg) {
        String strExceptionMsg = throwable != null ? throwable.getMessage() : null;
        String strInternetErrorMsg = getRequestErrorMsg(strExceptionMsg);
        String strErrorMsg = errorMsg + (errorMsg.trim().length() > 0 && (strExceptionMsg != null || strInternetErrorMsg != null) ? "\n\n | " : "") + (strInternetErrorMsg != null ? strInternetErrorMsg : (strExceptionMsg != null ? "Exception: " + strExceptionMsg : ""));
        String strLastLogMsg = ApiLog.getAvailableLogMsgsAsStr(1);
        String strErrorFull = strErrorMsg + "\n\n | OKHttpError: " + strLastLogMsg;

        return new String[]{strErrorMsg, strErrorFull};
    }

    private static OkHttpClient buildOkHttpClient() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new ApiLogger());
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .callTimeout(CALL_TIMEOUT, TimeUnit.SECONDS)
            .followRedirects(true)
            .followSslRedirects(true)
            .retryOnConnectionFailure(true)
            .cache(null);

//        try {
//            // Create a trust manager that does not validate certificate chains
//            final TrustManager[] trustAllCerts = new TrustManager[] {
//                new X509TrustManager() {
//                    @Override
//                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
//                        // Log.d(TAG, "checkClientTrusted - authType: " + authType);
//                    }
//                    @Override
//                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
//                        // Log.d(TAG, "checkServerTrusted - authType: " + authType);
//                    }
//                    @Override
//                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
//                        return new java.security.cert.X509Certificate[]{};
//                    }
//                }
//            };
//
//            //Using TLS 1_3 & 1_1 for HTTP/2 Server requests
//            // Note : The following is suitable for my Server. Please change accordingly
//            ConnectionSpec specTls = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
//                .tlsVersions(TlsVersion.TLS_1_3)
//                .allEnabledCipherSuites()
//            .build();
//
//            // Install the all-trusting trust manager
//             final SSLContext sslContext = SSLContext.getInstance("TLS", "Conscrypt");
//
//            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
//            // Create an ssl socket factory with our all-trusting manager
//            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
//
//            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
//            HttpsURLConnection.setDefaultSSLSocketFactory(sslSocketFactory);
//            builder.connectionSpecs(new ArrayList<ConnectionSpec>() {{
//                add(specTls);
//                add(ConnectionSpec.COMPATIBLE_TLS);
//                add(ConnectionSpec.CLEARTEXT);
//            }});
//            builder.hostnameVerifier(new HostnameVerifier() {
//                @Override
//                public boolean verify(String hostname, SSLSession session) {
//                    return true;
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return builder.build();
    }

    private static Gson buildGson() {
        return new GsonBuilder()
                .registerTypeAdapterFactory(new CustomGsonTypeAdapterFactory())
                .serializeNulls()
                .create();
    }

    public static Gson getGsonInstance() {
        return gson;
    }

    public interface RequestListener<T> {
        void onResponse(String strResponse, T objResponse);

        void onError(@NonNull String strErrorMsg, @Nullable String strErrorFull, @Nullable ANError anError);

        void downloadProgressListener(long bytesDownloaded, long totalBytes);
    }
}
