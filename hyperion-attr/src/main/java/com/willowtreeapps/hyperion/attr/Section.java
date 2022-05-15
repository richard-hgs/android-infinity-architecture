package com.willowtreeapps.hyperion.attr;

import androidx.annotation.NonNull;

import java.util.List;

class Section<T> implements Comparable<Section<?>> {

    private Class<?> type;
    private List<T> list;
    private String customName;

    Section(Class<?> type, List<T> list) {
        this.type = type;
        this.list = list;
    }

    String getName() {
        return customName != null ? customName : this.type.getSimpleName();
    }

    List<T> getList() {
        return this.list;
    }

    @Override
    public int compareTo(@NonNull Section<?> o) {
        if (customName != null && o.getCustomName() == null) {
            return -1;
        } else if (customName == null && o.getCustomName() != null) {
            return 1;
        }
        if (type.isAssignableFrom(o.type)) {
            return 1;
        }
        if (o.type.isAssignableFrom(type)) {
            return -1;
        }
        return 0;
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = this.type.getSimpleName();
        this.customName += " " + customName;
    }
}