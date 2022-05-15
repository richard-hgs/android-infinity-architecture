package com.infinity.architecture.base.ui.recycler;

public abstract class BaseAdapterListenerImpl<I> implements BaseAdapterListener<I> {

    @Override
    public void onItemClick(I item, int position) {

    }

    @Override
    public boolean onItemLongClick(I item, int position) {
        return false;
    }
}
