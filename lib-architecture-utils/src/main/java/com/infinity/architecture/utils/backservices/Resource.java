package com.infinity.architecture.utils.backservices;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Resource<T> {
    @NonNull
    private Status status = Status.ERROR;
    @Nullable
    private T data = null;
    @Nullable
    private String loadingMessage;
    @Nullable
    private String loadingProgressMessage;
    @Nullable
    private Throwable errorThrowable;
    @Nullable
    private String errorMsg;
    @Nullable
    private String errorMsgFull;
    @Nullable
    private String errorBody;

    /**
     * Private constructor should never be instantiated
     */
    private Resource() { }

    /**
     * Get success instance of {@link Resource}
     * @param data  Data instance
     * @param <T>   Type of {@link Resource#data} instance
     * @return      {@link Resource} instance
     */
    public static <T> Resource<T> success(@NonNull T data) {
        Resource<T> myResource = new Resource<T>();
        myResource.status = Status.SUCCESS;
        myResource.data = data;
        return myResource;
    }

    /**
     * Get loading instance of {@link Resource}
     * @param loadingMessage            Loading message
     * @param loadingProgressMessage    Loading progress message
     * @param <T>                       Type of {@link Resource#data} instance
     * @return                          {@link Resource} instance
     */
    public static <T> Resource<T> loading(@Nullable String loadingMessage, @Nullable String loadingProgressMessage) {
        Resource<T> myResource = new Resource<T>();
        myResource.status = Status.LOADING;
        myResource.loadingMessage = loadingMessage;
        myResource.loadingProgressMessage = loadingProgressMessage;
        return myResource;
    }

    /**
     * Get error instance of {@link Resource}
     * @param errorMsg          {@link String} simple error msg that will be displayed to user
     * @param errorMsgFull      {@link String} full error msg that will be logged
     * @param throwable         {@link Throwable} throwable error msg that will be used by internals
     * @param <T>               Type of {@link Resource#data} instance
     * @return                  {@link Resource} instance
     */
    public static <T> Resource<T> error(@NonNull String errorMsg, @Nullable String errorMsgFull, @Nullable Throwable throwable) {
        return error(errorMsg, errorMsgFull, null, throwable);
    }

    /**
     * Get error instance of {@link Resource}
     * @param errorMsg          {@link String} simple error msg that will be displayed to user
     * @param errorMsgFull      {@link String} full error msg that will be logged
     * @param errorBody         {@link String} json OR error body of the request
     * @param throwable         {@link Throwable} throwable error msg that will be used by internals
     * @param <T>               Type of {@link Resource#data} instance
     * @return                  {@link Resource} instance
     */
    public static <T> Resource<T> error(@NonNull String errorMsg, @Nullable String errorMsgFull, @Nullable String errorBody, @Nullable Throwable throwable) {
        Resource<T> myResource = new Resource<T>();
        myResource.status = Status.ERROR;
        myResource.errorMsg = errorMsg;
        myResource.errorMsgFull = errorMsgFull;
        myResource.errorBody = errorBody;
        myResource.errorThrowable = throwable;
        return myResource;
    }

    @NonNull
    public Status getStatus() {
        return status;
    }

    @Nullable
    public T getData() {
        return data;
    }

    @Nullable
    public String getLoadingMessage() {
        return loadingMessage;
    }

    @Nullable
    public String getLoadingProgressMessage() {
        return loadingProgressMessage;
    }

    @Nullable
    public Throwable getErrorThrowable() {
        return errorThrowable;
    }

    @Nullable
    public String getErrorMsg() {
        return errorMsg;
    }

    @Nullable
    public String getErrorMsgFull() {
        return errorMsgFull;
    }

    @Nullable
    public String getErrorBody() {
        return errorBody;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "status=" + status +
                ", data=" + data +
                ", loadingMessage='" + loadingMessage + '\'' +
                ", loadingProgressMessage='" + loadingProgressMessage + '\'' +
                ", errorThrowable=" + errorThrowable +
                ", errorMsg='" + errorMsg + '\'' +
                ", errorMsgFull='" + errorMsgFull + '\'' +
                ", errorBody='" + errorBody + '\'' +
                '}';
    }
}
