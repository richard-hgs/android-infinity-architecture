package com.infinity.architecture.views;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class FragmentContainerViewBind extends View {

    public FragmentContainerViewBind(Context context) {
        super(context);
    }

    public FragmentContainerViewBind(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FragmentContainerViewBind(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FragmentContainerViewBind(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Nullable
    public FragmentManager getSupportFragmentManager() {
        Context context = getContext();
        if (context instanceof AppCompatActivity) {
            return ((AppCompatActivity) context).getSupportFragmentManager();
        }

        return null;
    }
}
