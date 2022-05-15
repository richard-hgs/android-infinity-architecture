package com.infinity.architecture.views.adapters;

import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import java.util.HashMap;

public abstract class TabLayoutAdapter<VH extends TabLayoutAdapter.ViewHolder> {

    private InternalTabLayoutAdapterListener internalTabLayoutAdapterListener;
    private TabLayout.OnTabSelectedListener onTabSelectedListener;

    private HashMap<Integer, VH> viewHolderMap = new HashMap<>();

    public abstract VH onCreateViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindViewHolder(VH holder, int position, boolean isTabSelected);

    public abstract int getItemCount();

    public void notifyDataSetChanged() {
        if (internalTabLayoutAdapterListener != null) {
            internalTabLayoutAdapterListener.onNotifyDataSetChanged();
        }
    }

    public void setInternalTabLayoutAdapterListener(InternalTabLayoutAdapterListener internalTabLayoutAdapterListener) {
        this.internalTabLayoutAdapterListener = internalTabLayoutAdapterListener;
    }

    public InternalTabLayoutAdapterListener getInternalTabLayoutAdapterListener() {
        return internalTabLayoutAdapterListener;
    }

    public TabLayout.OnTabSelectedListener getOnTabSelectedListener() {
        return onTabSelectedListener;
    }

    public void setOnTabSelectedListener(TabLayout.OnTabSelectedListener onTabSelectedListener) {
        this.onTabSelectedListener = onTabSelectedListener;
    }

    public HashMap<Integer, VH> getViewHolderMap() {
        return viewHolderMap;
    }

    public interface InternalTabLayoutAdapterListener {
        void onNotifyDataSetChanged();
    }

    public static abstract class ViewHolder {
        private View view;

        public ViewHolder(View view) {
            this.view = view;
        }

        public View getView() {
            return view;
        }
    }
}
