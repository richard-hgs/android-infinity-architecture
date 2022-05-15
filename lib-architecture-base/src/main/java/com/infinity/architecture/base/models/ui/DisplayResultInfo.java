package com.infinity.architecture.base.models.ui;

public class DisplayResultInfo {

    private int statusBarHeightPx;
    private int clientHeightPx;
    private int clientWidthPx;

    private int clientTopPx;
    private int clientBottomPx;
    private int clientLeftPx;
    private int clientRightPx;

    private DisplayResultInfo() { }

    public static DisplayResultInfo getInstance(
        int statusBarHeightPx,
        int heightPx,
        int widthPx,
        int clientTopPx,
        int clientBottomPx,
        int clientLeftPx,
        int clientRightPx
    ) {
        DisplayResultInfo displayResultInfo = new DisplayResultInfo();
        displayResultInfo.statusBarHeightPx = statusBarHeightPx;
        displayResultInfo.clientHeightPx = heightPx;
        displayResultInfo.clientWidthPx = widthPx;
        displayResultInfo.clientTopPx = clientTopPx;
        displayResultInfo.clientBottomPx = clientBottomPx;
        displayResultInfo.clientLeftPx = clientLeftPx;
        displayResultInfo.clientRightPx = clientRightPx;
        return displayResultInfo;
    }

    public int getStatusBarHeightPx() {
        return statusBarHeightPx;
    }

    public int getClientHeightPx() {
        return clientHeightPx;
    }

    public int getClientWidthPx() {
        return clientWidthPx;
    }

    public int getClientTopPx() {
        return clientTopPx;
    }

    public int getClientBottomPx() {
        return clientBottomPx;
    }

    public int getClientLeftPx() {
        return clientLeftPx;
    }

    public int getClientRightPx() {
        return clientRightPx;
    }

    @Override
    public String toString() {
        return "DisplayResultInfo{" +
                "statusBarHeightPx=" + statusBarHeightPx +
                ", clientHeightPx=" + clientHeightPx +
                ", clientWidthPx=" + clientWidthPx +
                ", clientTopPx=" + clientTopPx +
                ", clientBottomPx=" + clientBottomPx +
                ", clientLeftPx=" + clientLeftPx +
                ", clientRightPx=" + clientRightPx +
                '}';
    }
}
