package com.infinity.architecture.base.ui.recycler;

interface BaseAdapterListener<I> {
    void onItemClick(I item, int position);

    boolean onItemLongClick(I item, int position);
}