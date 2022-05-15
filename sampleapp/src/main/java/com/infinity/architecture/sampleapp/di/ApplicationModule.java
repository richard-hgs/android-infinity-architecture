package com.infinity.architecture.sampleapp.di;

import android.content.Context;

import com.infinity.architecture.sampleapp.backservices.AppApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class ApplicationModule {
    /**
     * Provides the application instance context for repositories that needs context
     * @param context   The application context
     * @return {@link AppApplication} context component
     */
    @Provides
    @Singleton
    public AppApplication providesAppInstance(@ApplicationContext Context context) {
        return (AppApplication) context;
    }
//
//    /**
//     * Provides the {@link ApiService} instance for injection
//     * @return  {@link ApiService} instance
//     */
//    @Provides
//    @Singleton
//    public ApiService providesApiService() {
//        return new ApiServiceImpl();
//    }
//
//    /**
//     * Provides the {@link SharedPrefsService} instance for injection
//     * @return  {@link SharedPrefsService} instance
//     */
//    @Provides
//    @Singleton
//    public SharedPrefsService providesSharedPrefsService(@NonNull MyApplication myApplication) {
//        return new SharedPrefsServiceImpl(myApplication);
//    }
//
//    /**
//     * Provides the {@link VerificaPermissoes} instance for injection
//     * @return  {@link VerificaPermissoes} instance
//     */
//    @Provides
//    @Singleton
//    public VerificaPermissoes providePermissionCheck() {
//        return new VerificaPermissoes();
//    }
//
//    /**
//     * Provides the {@link DbService} instance for injection
//     * @return  {@link DbService} instance
//     */
//    @Provides
//    @Singleton
//    public DbService provideDbService(@NonNull MyApplication myApplication) {
//        return new DbServiceImpl(myApplication);
//    }
}
