package com.infinity.architecture.utils.bundle_utils;

import android.os.Bundle;

import androidx.annotation.NonNull;

public class BundleUtils {

    /**
     * Concats multiples bundle into one bundle
     * @param bundleArgs    N Bundles
     * @return              Bundle
     */
    @NonNull
    public static Bundle concat(@NonNull Bundle ...bundleArgs) {
        Bundle bundle = new Bundle();
        for (Bundle bdArg : bundleArgs) {
            bundle.putAll(bdArg);
        }
        return bundle;
    }
}

