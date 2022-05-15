package com.infinity.architecture.base.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;


public abstract class BaseAppCompatActivityOpt extends BaseAppCompatActivity {
    @Nullable
    @Override
    public NavController retrieveNavController() {
        return null;
    }

    @Override
    public void onBaseAppCompatException(@NonNull Throwable t) {
        t.printStackTrace();
        throw new RuntimeException(t);
    }
}
