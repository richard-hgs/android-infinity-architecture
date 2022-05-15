package com.infinity.architecture.sampleapp.ui.adapters;

import androidx.recyclerview.widget.RecyclerView;

import com.infinity.architecture.base.ui.recycler.BaseRecyclerViewModel;
import com.infinity.architecture.sampleapp.databinding.RvInfinityViewsItemBinding;
import com.infinity.architecture.sampleapp.model.RvAdptInfinityViewsItem;

import java.util.ArrayList;

/**
 * Custom RecyclerAdapter customization to use view model with recycler items without the need of create an adapter and a
 * View holder, increasing the development speed. For each item a new view model instance will be created but only the ones
 * needed to scroll de recycler view. Depending of the size of the views in layout about a 10 - 19 BaseRecyclerViewModel unique
 * instances will be created with its own lifecycle owner.
 */
public class RvAdptInfinityViewsVm extends BaseRecyclerViewModel<RvInfinityViewsItemBinding, RvAdptInfinityViewsItem> {

    /**
     * When the vill is binded, equivalent to {@link androidx.recyclerview.widget.RecyclerView.Adapter#onBindViewHolder(RecyclerView.ViewHolder, int)}
     * @param binding       The viewDataBinding created for this item
     * @param itemList      The entire list of the recycler
     * @param item          The item for current view that will be binded
     * @param pos           The pos for current view that will be binded
     */
    @Override
    protected void onBindViewHolder(RvInfinityViewsItemBinding binding, ArrayList<RvAdptInfinityViewsItem> itemList, RvAdptInfinityViewsItem item, int pos) {
        binding.btnView.setText(item.getText());
    }
}
