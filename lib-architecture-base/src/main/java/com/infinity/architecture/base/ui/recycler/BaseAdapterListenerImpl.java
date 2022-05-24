package com.infinity.architecture.base.ui.recycler;

public abstract class BaseAdapterListenerImpl<I> implements BaseAdapterListener<I> {

    @Override
    public void onItemClick(int actionId, I item, int position) {

    }

    @Override
    public boolean onItemLongClick(int actionId, I item, int position) {
        return false;
    }
}
