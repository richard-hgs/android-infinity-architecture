package com.infinity.architecture.utils.observable;

import androidx.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;

public class ObservableUtils {

    public static void runOnUiAfterDelay(@NonNull CompositeDisposable compositeDisposable, long delay, TimeUnit timeUnit, Action action) {
        Disposable disposable = Completable.timer(delay, timeUnit, AndroidSchedulers.mainThread())
            .subscribe(action);
        compositeDisposable.add(disposable);
    }
}
