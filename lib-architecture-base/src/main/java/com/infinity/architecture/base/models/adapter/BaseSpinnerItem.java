package com.infinity.architecture.base.models.adapter;

public class BaseSpinnerItem {
    private long id;

    public BaseSpinnerItem() {
    }

    public BaseSpinnerItem(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "BaseSpinnerItem{" +
                "id=" + id +
                '}';
    }
}
