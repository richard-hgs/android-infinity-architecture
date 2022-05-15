package com.willowtreeapps.hyperion.sqlite.presentation;

import androidx.lifecycle.ViewModel;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.annotation.NonNull;

import com.willowtreeapps.hyperion.sqlite.domain.usecase.UseCase;

import java.io.File;
import java.lang.ref.WeakReference;

import io.reactivex.disposables.CompositeDisposable;

public abstract class DatabaseViewModel<T extends UseCase> extends ViewModel {

    protected CompositeDisposable subscriptions = new CompositeDisposable();
    protected WeakReference<T> useCase;

    protected SQLiteDatabase db;

    public DatabaseViewModel() {

    }

    public void initDatabase(@NonNull File file) {
        db = SQLiteDatabase.openDatabase(file.getPath(),
                null, SQLiteDatabase.OPEN_READWRITE);
        useCase = new WeakReference<>(createUsecase(db));
    }

    public T getUseCase() {
        return useCase.get();
    }

    public void executeSql(String sql) throws Exception {
        db.execSQL(sql);
    }

    public Cursor carregarDadosSql(String sql) throws Exception {
        return db.rawQuery(sql, null);
    }

    public long getMaxRowId(String tableName) throws Exception {
        long retorno = 0;
        Cursor cursor = carregarDadosSql("SELECT MAX(ROWID) FROM " + tableName);
        if(cursor.moveToFirst()) {
            retorno = cursor.getLong(0);
        }
        return retorno;
    }

    public long getMinRowId(String tableName) throws Exception {
        long retorno = 0;
        Cursor cursor = carregarDadosSql("SELECT MIN(ROWID) FROM " + tableName);
        if(cursor.moveToFirst()) {
            retorno = cursor.getLong(0);
        }
        return retorno;
    }

    protected abstract T createUsecase(SQLiteDatabase db);

    @Override
    protected void onCleared() {
        subscriptions.clear();
        if (useCase.get() != null) {
            useCase.get().closeDatabase();
        }
        super.onCleared();
    }
}
