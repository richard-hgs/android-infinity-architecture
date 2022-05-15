package com.infinity.architecture.base.utils;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.lang.reflect.Constructor;

public class BaseViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        T instance = null;
        Exception exception = null;
        try {
            instance = modelClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            exception = e;
        }

        if (exception != null) {
            throw new RuntimeException("Cannot create an instance of " + modelClass, exception);
        } else if (instance == null) {
            throw new RuntimeException("Cannot create an instance of " + modelClass);
        }

        return instance;
    }

    private boolean hasNoArgConstructor(Class<?> klass) {
        for(Constructor<?> c : klass.getDeclaredConstructors()) {
            if(c.getParameterTypes().length == 0) return true;
        }
        return false;
    }
}
