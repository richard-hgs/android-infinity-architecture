package com.infinity.architecture.base;

import androidx.annotation.NonNull;

import com.infinity.architecture.utils.file.FileHelper;

public class ArchitectureBaseApplication extends BaseApplication {

    @NonNull
    @Override
    protected String[] retrieveFileProviders() {
        return new String[0];
    }

    @NonNull
    @Override
    protected FileHelper.ProvideUriToPathTranslator[] retrieveFileProviderTranslators() {
        return new FileHelper.ProvideUriToPathTranslator[0];
    }
}
