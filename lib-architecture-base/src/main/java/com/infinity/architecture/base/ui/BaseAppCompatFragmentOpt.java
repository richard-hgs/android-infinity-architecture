package com.infinity.architecture.base.ui;

public abstract class BaseAppCompatFragmentOpt extends BaseAppCompatFragment {
    @Override
    protected boolean isDispatchVmSafeConstructorManually() {
        return false;
    }
}
