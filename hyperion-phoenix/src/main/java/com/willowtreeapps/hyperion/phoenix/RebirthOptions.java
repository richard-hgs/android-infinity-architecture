package com.willowtreeapps.hyperion.phoenix;

final class RebirthOptions {

    static final RebirthOptions DEFAULT = new Builder().build();

    private final boolean clearCache;
    private final boolean clearData;
    private final boolean clearSharedPrefs;
    private final boolean restartSelf;

    private RebirthOptions(Builder builder) {
        this.clearCache = builder.clearCache;
        this.clearData = builder.clearData;
        this.restartSelf = builder.restartSelf;
        this.clearSharedPrefs = builder.clearSharedPrefs;
    }

    Builder builder() {
        return new Builder(this);
    }

    boolean shouldClearCache() {
        return clearCache;
    }

    boolean shouldClearData() {
        return clearData;
    }

    boolean shouldClearSharedPrefs() {
        return clearSharedPrefs;
    }

    boolean shouldRestartSelf() {
        return restartSelf;
    }

    static final class Builder {

        private boolean clearCache = true;
        private boolean clearData = true;
        private boolean clearSharedPrefs = true;
        private boolean restartSelf = false;

        Builder() {

        }

        Builder(RebirthOptions options) {
            this.clearCache = options.clearCache;
            this.clearData = options.clearData;
            this.clearSharedPrefs = options.clearSharedPrefs;
        }

        Builder clearCache(boolean clearCache) {
            this.clearCache = clearCache;
            return this;
        }

        Builder clearData(boolean clearData) {
            this.clearData = clearData;
            return this;
        }

        Builder clearSharedPrefs(boolean clearSharedPrefs){
            this.clearSharedPrefs = clearSharedPrefs;
            return this;
        }

        Builder restartSelf(boolean restartSelf) {
            this.restartSelf = restartSelf;
            return this;
        }

        RebirthOptions build() {
            return new RebirthOptions(this);
        }
    }
}