package com.infinity.architecture.utils.dialog.utils;

import android.view.View;

import androidx.appcompat.app.AlertDialog;

public interface InterfaceCustomDialogClick {
    void onClick(View v, AlertDialog dialog);

    void onClick(View v, AlertDialog dialog, Object obj1, Object obj2);
}
