package com.infinity.architecture.base.ui.spinner;

import com.infinity.architecture.base.databinding.LaySimpleSpinnerDropdownBinding;
import com.infinity.architecture.base.models.adapter.SimpleSpinnerItem;

import java.util.ArrayList;

public class SimpleSpinnerDropdownVM<I extends SimpleSpinnerItem> extends BaseSpinnerViewModel<LaySimpleSpinnerDropdownBinding, I> {

    @Override
    protected void onBindView(LaySimpleSpinnerDropdownBinding binding, ArrayList<I> itemList, I item, int pos) {
        binding.laySimpleSpinnerDropdownTv.setText(item.getText());
    }
}
