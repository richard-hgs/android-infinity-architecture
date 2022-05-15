package com.infinity.architecture.utils.keyboard;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;

public class KeyboardUtils {

    /**
     * Open a keyboard when dialog gets opened for the first time
     * @param dialog    Dialog instance
     */
    public static void openKeyboardDialog(@NonNull Dialog dialog) {
        if (dialog.getWindow() != null) {
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
//        try {
//            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                @Override
//                public void onFocusChange(View v, boolean hasFocus) {
//                    try {
//                        if (hasFocus) {
//                            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//            editText.requestFocus();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    /**
     * Toggle the keyboard state
     *
     * If the input window is already displayed, it gets hidden.
     * If not the input window will be displayed.
     *
     * @param context       Context
     * @param showFlags     Show flags {@link InputMethodManager#SHOW_FORCED}, {@link InputMethodManager#SHOW_IMPLICIT}
     * @param hideFlags     Hide flags {@link InputMethodManager#HIDE_NOT_ALWAYS}, {@link InputMethodManager#HIDE_IMPLICIT_ONLY}
     */
    public static void toggleKeyboard(@NonNull Context context, int showFlags, int hideFlags) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(showFlags, hideFlags);
    }

    //função que fecha o teclado caso o menu abra ou fecha
    public static void fechaTeclado(View editProduto, Activity activity) {
        //se algum item estiver focalizado
        if (activity.getCurrentFocus() != null) {
            //recebe o focus atual
            if (activity.getCurrentFocus().getId() == editProduto.getId()) {
                InputMethodManager teclado = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                //se o teclado existir
                if (teclado != null) {
                    //verifica se ele está aberto
                    if (teclado.isActive()) {
                        //se estiver ele é fechado
                        teclado.hideSoftInputFromWindow(editProduto.getWindowToken(), 0);
                    }
                }
            }
        }
    }

    //função que abre o teclado
    public static void abreTecladoTela(final View editText, final Activity activity) {
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                editText.post(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                    }
                });
            }
        });
        editText.requestFocus();
    }

    // função abre teclado no edittext
    public static void abreTecladoEdit(View editText, Activity activity){
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    // função que fecha teclado no dialog
    public static void fechaTecladoDialog(Dialog dialog) {
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    // função abre teclado no edittext
    public static void abreTecladoEdit2(View editText, Activity activity){
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
        }
    }

    // função que fecha o teclado do edit text
    public static void fechaTecladoEdit(View editText, Activity activity){
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }

//    public static void fechaTecladoEdit(View editText, Activity activity){
//        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (imm != null) {
//            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
//        }
//    }

    // fecha teclado da activity
    public static void fechaTeclado(Activity activity) {
        View view = activity.findViewById(android.R.id.content);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static boolean keyboardIsOpen(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.isAcceptingText();
    }
}
