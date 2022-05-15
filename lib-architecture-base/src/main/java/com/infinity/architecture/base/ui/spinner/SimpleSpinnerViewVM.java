package com.infinity.architecture.base.ui.spinner;

import com.infinity.architecture.base.databinding.LaySimpleSpinnerBinding;
import com.infinity.architecture.base.models.adapter.SimpleSpinnerItem;

import java.util.ArrayList;

public class SimpleSpinnerViewVM<I extends SimpleSpinnerItem> extends BaseSpinnerViewModel<LaySimpleSpinnerBinding, I> {

    @Override
    protected void onBindView(LaySimpleSpinnerBinding binding, ArrayList<I> itemList, I item, int pos) {
        binding.laySimpleSpinnerTv.setText(item.getText());
    }
}
