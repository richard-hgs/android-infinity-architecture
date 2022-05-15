package com.infinity.architecture.utils.dialog.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.util.Objects;


public class CustomDialogBuilder {
    // CONTEXTO
    private Activity activity;

    // DIALOG CUSTOMIZADO
    public View viewLayout;
    private AlertDialog alertDialog;

    // SALVAR INFOS PARA ACESSO
    private Object object1;
    private Object object2;

    //private boolean showKeyBoard;

    // Cria o dialog com o custom layout
    public CustomDialogBuilder(@NonNull Activity activity, int layout) {
        this.activity = activity;

        viewLayout = View.inflate(activity, layout, null);
        this.alertDialog = new AlertDialog.Builder(activity)
                .setView(viewLayout)
                .show();
    }

    public CustomDialogBuilder(@NonNull Activity activity, View view) {
        this.activity = activity;

        this.alertDialog = new AlertDialog.Builder(activity)
                .setView(view)
                .show();
    }

    // Cria o dialog com o custom layout
    public CustomDialogBuilder(@NonNull Activity activity, int layout, @Nullable CustomDialogListener dialogListener) {
        this.activity = activity;

        viewLayout = View.inflate(activity, layout, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setView(viewLayout);
        this.alertDialog = alertDialogBuilder.create();

        if (dialogListener != null){
            dialogListener.beforeDialogShow(this);
        }
        this.alertDialog.show();
    }

    public CustomDialogBuilder(@NonNull Activity activity, View view, @Nullable CustomDialogListener dialogListener) {
        this.activity = activity;

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setView(view);
        this.alertDialog = alertDialogBuilder.create();
        if (dialogListener != null){
            dialogListener.beforeDialogShow(this);
        }
        this.alertDialog.show();
    }

    // =================== CUSTOM DIALOG ===================
    // Recebe as views pelo id da view inflada
    public <T extends View> T findViewById(int id) {
        return this.viewLayout.findViewById(id);
    }

    // Finaliza o dialog
    public void dismiss() {
        if (activity != null && alertDialog != null && alertDialog.isShowing() && !activity.isDestroyed()) {
            alertDialog.dismiss();
        }
    }

    // Click botoes da custom view
    public void onClick(int id, final InterfaceCustomDialogClick interfaceCustomDialogClick) {
        if (interfaceCustomDialogClick != null) {
            viewLayout.findViewById(id).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    interfaceCustomDialogClick.onClick(v, alertDialog);
                    interfaceCustomDialogClick.onClick(v, alertDialog, object1, object2);
                }
            });
        }
    }

    // Cancelavel o dialog
    public void setCancelable(boolean cancelable) {
        if (alertDialog != null){
            alertDialog.setCancelable(cancelable);
        }
    }

    // Retorna a view do dialog
    public View getViewLayout(){
        return this.viewLayout;
    }

    // Retorna o dialog
    public AlertDialog getAlertDialog() {
        return this.alertDialog;
    }

    // Retorna a window
    public Window getWindow(){
        Window window = null;
        if (alertDialog.getWindow() != null){
            window = alertDialog.getWindow();
        }
        return window;
    }

    // Retorna visibilidade do dialog
    public boolean isShowing() {

        if (alertDialog != null){
            return alertDialog.isShowing();
        }

        return false;
    }

    // Seta o Background
    public void setBackgroundDrawable(Drawable drawable) {
        Objects.requireNonNull(this.getWindow()).setBackgroundDrawable(drawable);
    }

    // Exibe o dialog novamente
    public void show() {
        try{
            if (alertDialog != null) {
                alertDialog.show();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    // =================== SALVA INFOS PARA ACESSO CUSTOM DIALOG =====================
    // salva o objeto1
    public void setObj1(Object obj1){
        this.object1 = obj1;
    }
    // retorna o objeto1
    public Object getObj1(){
        return this.object1;
    }

    // salva o objeto2
    public void setObj2(Object obj2){
        this.object2 = obj2;
    }
    // retorna o objeto2
    public Object getObj2(){
        return this.object2;
    }

    // =================== DIALOG PADRAO ===================
    public static class CustomBuilder {
        // DIALOG PADRAO
        private AlertDialog.Builder alertDialogBuilder;
        private AlertDialog alertDialog;
        private Context context;

        public CustomBuilder(Context context) {
            this.context = context;
            this.alertDialogBuilder = new AlertDialog.Builder(context);
        }

        // Seta o titulo do dialog padrao
        public CustomBuilder setTitle(String titulo) {
            if (this.alertDialogBuilder != null) {
                this.alertDialogBuilder.setTitle(titulo);
            }
            return this;
        }
        // Seta mensagem do dialog padrao
        public CustomBuilder setMessage(String mensagem) {
            if (this.alertDialogBuilder != null){
                this.alertDialogBuilder.setMessage(mensagem);
            }
            return this;
        }

        // Seta o btnPositivo dialog padrao
        public CustomBuilder setBtnPositivo(String titulo, @Nullable final InterfaceCustomDialogBuilderClick customDialogBuidlerOnClick){
            if (this.alertDialogBuilder != null) {
                this.alertDialogBuilder.setPositiveButton(titulo, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (customDialogBuidlerOnClick != null) {
                            customDialogBuidlerOnClick.onClick(dialog);
                        }
                    }
                });
            }

            return this;
        }
        // Seta o btnNegativo dialog padrao
        public CustomBuilder setBtnNegativo(String titulo, @Nullable final InterfaceCustomDialogBuilderClick customDialogBuidlerOnClick){
            if (this.alertDialogBuilder != null) {
                this.alertDialogBuilder.setNegativeButton(titulo, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (customDialogBuidlerOnClick != null) {
                            customDialogBuidlerOnClick.onClick(dialog);
                        }
                    }
                });
            }

            return this;
        }

        // Seta o btnPositivo dialog titulo stringResource
        public CustomBuilder setBtnPositivo(Integer intTitulo, final InterfaceCustomDialogBuilderClick customDialogBuidlerOnClick){
            String titulo = context.getResources().getString(intTitulo);
            if (this.alertDialogBuilder != null) {
                this.alertDialogBuilder.setPositiveButton(titulo, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (customDialogBuidlerOnClick != null) {
                            customDialogBuidlerOnClick.onClick(dialog);
                        }
                    }
                });
            }

            return this;
        }
        // Seta o btnNegativo dialog titulo stringResource
        public CustomBuilder setBtnNegativo(Integer intTitulo, @Nullable final InterfaceCustomDialogBuilderClick customDialogBuidlerOnClick){
            String titulo = context.getResources().getString(intTitulo);
            if (this.alertDialogBuilder != null) {
                this.alertDialogBuilder.setNegativeButton(titulo, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (customDialogBuidlerOnClick != null) {
                            customDialogBuidlerOnClick.onClick(dialog);
                        }
                    }
                });
            }

            return this;
        }

        // Seta se é cancelavel ou nao
        public CustomBuilder setCancelable(boolean cancelavel) {
            if (this.alertDialogBuilder != null) {
                this.alertDialogBuilder.setCancelable(cancelavel);
            }
            return this;
        }
        // Seta para visualizar o dialog
        public void show() {
            try {
                if (this.alertDialogBuilder != null) {
                    this.alertDialog = this.alertDialogBuilder.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Seta dismiss listener
        public CustomBuilder setOnDismissListener(CustomDialogBuidlerOnDismiss customDialogBuidlerOnDismiss){
            if (this.alertDialogBuilder != null){
                this.alertDialogBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (customDialogBuidlerOnDismiss != null) {
                            customDialogBuidlerOnDismiss.onDismiss(dialog);
                        }
                    }
                });
            }
            return this;
        }

        public AlertDialog getAlertDialog() {
            return alertDialog;
        }
    }

    // Listener Click dialog geral
    public static abstract class CustomDialogOnClick implements InterfaceCustomDialogClick {

        @Override
        public void onClick(View v, AlertDialog dialog) {

        }

        @Override
        public void onClick(View v, AlertDialog dialog, Object obj1, Object obj2) {

        }
    }

    // Listener Click dialog Builder
    public static abstract class CustomDialogBuidlerOnClick implements InterfaceCustomDialogBuilderClick {
        @Override
        public void onClick(DialogInterface dialog) {

        }
    }

    // Listener Click dialog Builder
    public static abstract class CustomDialogBuidlerOnDismiss implements InterfaceCustomDialogBuilderDismissListener {
        @Override
        public void onDismiss(DialogInterface dialog) {

        }
    }

    public interface CustomDialogListener {
        void beforeDialogShow(CustomDialogBuilder customDialogBuilder);
    }

    /**
     * LIMPA AS REFERENCIAS EVITA MEMORY LEAK
     */
    public void clearReferences() {
        this.activity = null;
        this.viewLayout = null;
        this.alertDialog = null;
    }
}
