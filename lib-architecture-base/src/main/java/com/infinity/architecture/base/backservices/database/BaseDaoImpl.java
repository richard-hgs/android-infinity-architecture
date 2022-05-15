package com.infinity.architecture.base.backservices.database;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public abstract class BaseDaoImpl {

    private RoomDatabase roomDatabase;

    public BaseDaoImpl(@NonNull RoomDatabase roomDatabase) {
        this.roomDatabase = roomDatabase;
    }

    protected void throwOnUncaughtOnCurrentThread(Throwable throwable) {
        Thread thread = Thread.currentThread();
        Thread.UncaughtExceptionHandler uncaughtExceptionHandler = Thread.currentThread().getUncaughtExceptionHandler();
        if (uncaughtExceptionHandler != null) {
            uncaughtExceptionHandler.uncaughtException(thread, throwable);
        }
    }

    public void notifyObserversByTableNames(String... tables) {
        try {
            notifyObserversByTableNames(roomDatabase, tables);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @NonNull
    public <T> LiveData<T> createLiveData(String[] tableNames, boolean inTransaction, Callable<T> computeFunction) {
        try {
            return createLiveData(roomDatabase, tableNames, inTransaction, computeFunction);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    @SuppressWarnings("unchecked")
    private static void notifyObserversByTableNames(@NonNull RoomDatabase roomDatabase, String... tables) throws Exception {
        InvalidationTracker invalidationTracker = roomDatabase.getInvalidationTracker();
        Method method = InvalidationTracker.class.getDeclaredMethod("notifyObserversByTableNames", String[].class);
        method.invoke(invalidationTracker, (Object) tables);
    }

    @SuppressWarnings("unchecked")
    private static <T> LiveData<T> createLiveData(@NonNull RoomDatabase roomDatabase, String[] tableNames, boolean inTransaction, Callable<T> computeFunction) throws Exception {
        InvalidationTracker invalidationTracker = roomDatabase.getInvalidationTracker();
        Method method = InvalidationTracker.class.getDeclaredMethod("createLiveData", String[].class, boolean.class, Callable.class);
        method.setAccessible(true);
        LiveData<T> result = (LiveData<T>) method.invoke(invalidationTracker, tableNames, inTransaction, computeFunction);
        return result;
    }
}

