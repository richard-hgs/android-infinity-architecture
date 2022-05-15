package com.infinity.architecture.views.adapters;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.infinity.architecture.views.R;

import java.lang.reflect.Method;

public class FrameLayoutAdapter {

    private FragmentManager fragmentManager;
    private int frameLayId = View.NO_ID;

    public FrameLayoutAdapter(@NonNull FragmentManager fragmentManager, int frameLayId) {
        this.fragmentManager = fragmentManager;
        this.frameLayId = frameLayId;
    }

    @SuppressWarnings("unchecked")
    public <F extends Fragment>  void replaceFragment(Class<F> fmClass, @Nullable Bundle args) throws Exception {
        if (fragmentManager != null) {
            if (frameLayId != View.NO_ID) {
                Method getInstMethod = fmClass.getDeclaredMethod("getInstance");
                F fm = (F) getInstMethod.invoke(null);

                if (fm == null) {
                    throw new Exception("Fragment couldn't be instantiated");
                }

                if (args != null) {
                    fm.setArguments(args);
                }

                FragmentTransaction fmTransaction = fragmentManager.beginTransaction();
                fmTransaction.replace(frameLayId, fm);
                fmTransaction.commit();
            } else {
                throw new Exception("Frame layout id must be informed");
            }
        }
    }

    public void clearInstances() {
        this.fragmentManager = null;
    }
}
