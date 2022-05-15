package com.infinity.architecture.utils.backservices.api;


import com.google.gson.annotations.SerializedName;

@GsonConfiguration(ignoreWriteEntireObject = true)
public class BaseRequestResponse {
    @SerializedName("status")
    private Integer status;
    @GsonNullable
    @SerializedName("msn")
    private String errorMsg;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public String toString() {
        return
            "BaseRequestResponse{" +
                "status=" + status +
                ", errorMsg=" + errorMsg +
            '}';
    }
}
