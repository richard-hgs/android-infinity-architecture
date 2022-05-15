package com.infinity.architecture.utils.permission;

import android.util.Log;

public abstract class AbstractPermissao implements InterfacePermissao {
    private String TAG = "AbstractPermissao";
    @Override
    public void statusPermissao(boolean status) {
        Log.d(TAG, "status: " + status);
    }
}
