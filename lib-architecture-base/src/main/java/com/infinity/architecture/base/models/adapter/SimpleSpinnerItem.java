package com.infinity.architecture.base.models.adapter;

public class SimpleSpinnerItem extends BaseSpinnerItem {

    private String text;

    public SimpleSpinnerItem() {
    }

    public SimpleSpinnerItem(long id, String text) {
        super(id);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "SimpleSpinnerItem{" +
                "text='" + text + '\'' +
                "} " + super.toString();
    }
}
