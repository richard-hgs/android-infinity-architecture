package com.infinity.architecture.base.utils;

import android.app.Application;
import android.content.Intent;

public interface ViewModelStoreOwnerProvider {
    Application getAppContext();

    Intent getIntent();
}
