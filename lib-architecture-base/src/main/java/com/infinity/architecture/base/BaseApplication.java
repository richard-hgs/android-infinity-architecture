package com.infinity.architecture.base;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.infinity.architecture.utils.file.FileHelper;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class BaseApplication extends Application {
    private static final String TAG = "BaseApplication";

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    private static ArrayList<String> fileProvidersNames = new ArrayList<>();

    private static ArrayList<FileHelper.ProvideUriToPathTranslator> fileProviderTranslators = new ArrayList<FileHelper.ProvideUriToPathTranslator>(){{
        add(new FileHelper.ProvideUriToPathTranslator() {
            @Nullable
            @Override
            public String translateUriBasePath(@NonNull String startKey, @NonNull String fileProviderPackageName, @NonNull String providerPathName, @NonNull String pathInPath) {
                if (fileProviderPackageName.equals(BaseApplication.getAppContext().getPackageName() + ".fileprovider")) {
                    if (providerPathName.equals("my_files")) {
                        return context.getFilesDir().getPath();
                    }
                }
                return null;
            }
        });
    }};

    @NonNull
    protected abstract String[] retrieveFileProviders();

    @NonNull
    protected abstract FileHelper.ProvideUriToPathTranslator[] retrieveFileProviderTranslators();

    public void onCreate() {
        super.onCreate();
        // Customize the picasso instance to use OKHttp3 to support image redirections
        BaseApplication.context = getApplicationContext();
        BaseApplication.fileProvidersNames.add(BaseApplication.getAppContext().getPackageName() + ".fileprovider");
        BaseApplication.fileProvidersNames.addAll(Arrays.asList(retrieveFileProviders()));
        BaseApplication.fileProviderTranslators.addAll(Arrays.asList(retrieveFileProviderTranslators()));

        for (FileHelper.ProvideUriToPathTranslator translatorAt : BaseApplication.fileProviderTranslators) {
            FileHelper.addUriToPathTranslator(translatorAt);
        }


//        if (BaseApplication.fileProvidersNames == null) {
//            throw new RuntimeException("providerNames cannot be null");
//        } else if (BaseApplication.fileProvidersNames.length == 0) {
//            throw new RuntimeException("providerNames list must have size > 0");
//        } else {
//            for (String providerNameAt : BaseApplication.fileProvidersNames) {
//                if (providerNameAt == null) {
//                    throw new RuntimeException("providerName cannot be null");
//                }
//            }
//        }
//
//        for (FileHelper.ProvideUriToPathTranslator providerTranslatorAt : BaseApplication.fileProviderTranslators) {
//            if (providerTranslatorAt == null) {
//                throw new RuntimeException("providerTranslator cannot be null");
//            }
//            FileHelper.addUriToPathTranslator(providerTranslatorAt);
//        }

        // FileHelper.addUriToPathTranslator(baseUriToPathProvider);
    }

    public static Context getAppContext() {
        return BaseApplication.context;
    }

    public static String getBaseFileProviderName() {
        return BaseApplication.fileProvidersNames.get(0);
    }
}
