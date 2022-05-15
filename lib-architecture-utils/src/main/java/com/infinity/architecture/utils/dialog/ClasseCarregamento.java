package com.infinity.architecture.utils.dialog;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.infinity.architecture.utils.R;
import com.infinity.architecture.utils.dialog.utils.CustomDialogBuilder;


public class ClasseCarregamento {

    private CustomDialogBuilder customDialogBuilder = null;

    private Activity activity;
    private boolean isCancelable = true;

    // private WeakReference<Activity> weakRefActivity;

    public ClasseCarregamento(Activity activity) {
        // weakRefActivity = new WeakReference<>(activity);
        this.activity = activity;
    }

    public View exibirCarregamento() {
        try {
            if (customDialogBuilder != null && !dialogVisivel()) {
                customDialogBuilder.getAlertDialog().show();
            } else {
                customDialogBuilder = new CustomDialogBuilder(activity, R.layout.lay_dialog_carregamento);
                customDialogBuilder.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                customDialogBuilder.setCancelable(isCancelable);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return customDialogBuilder.getViewLayout();
    }

    public boolean dialogVisivel() {
        AlertDialog alertDialog = null;
        try {
            if (customDialogBuilder != null) {
                alertDialog = customDialogBuilder.getAlertDialog();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return alertDialog != null && alertDialog.isShowing();
    }

    public void finalizarCarregamento() {
        try {
            if (customDialogBuilder != null && dialogVisivel()) {
                customDialogBuilder.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void podeSerCancelado(boolean cancelavel) {
        try {
            this.isCancelable = cancelavel;
            if (customDialogBuilder != null) {
                customDialogBuilder.setCancelable(this.isCancelable);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Nullable
    public TextView getTvTitulo() {
        return this.customDialogBuilder.findViewById(R.id.lay_dialog_carregamento_titulo);
    }

    @Nullable
    public TextView getTvDescricao() {
        return this.customDialogBuilder.findViewById(R.id.lay_dialog_carregamento_descricao);
    }

    public void setTxtCarregamento(String text) {
        try {
            TextView txtDesc = getTvDescricao();
            if (txtDesc != null) {
                txtDesc.setText(text);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * LIMPA AS REFERENCIAS
     */
    public void clearReferences() {
        this.activity = null;
        if (this.customDialogBuilder != null) {
            this.customDialogBuilder.clearReferences();
            this.customDialogBuilder = null;
        }
    }
}
