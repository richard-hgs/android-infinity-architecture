package com.infinity.architecture.base.ui.recycler;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BaseRecyclerAdapterVH<B extends ViewDataBinding, VM extends BaseRecyclerViewModel<B, I>, I> extends RecyclerView.ViewHolder {

    private final String TAG = "BaseRecyclerAdapterVH";

    private B viewDataBinding;
    private VM viewModel;

    public BaseRecyclerAdapterVH(B viewDataBinding, VM viewModel) {
        super(viewDataBinding.getRoot());
        this.viewDataBinding = viewDataBinding;
        this.viewModel = viewModel;
    }

    public void onBindViewHolder(ArrayList<I> itemList, I item, int pos) {
        viewModel.onBindViewHolder(viewDataBinding, itemList, item, pos);
    }

    public B getViewDataBinding() {
        return viewDataBinding;
    }

    public VM getViewModel() {
        return viewModel;
    }
}
