package com.infinity.architecture.base.ui.recycler;

interface BaseAdapterListener<I> {
    void onItemClick(int actionId, I item, int position);

    boolean onItemLongClick(int actionId, I item, int position);
}