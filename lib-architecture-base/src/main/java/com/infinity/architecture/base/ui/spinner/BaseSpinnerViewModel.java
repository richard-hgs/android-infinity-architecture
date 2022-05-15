package com.infinity.architecture.base.ui.spinner;

import androidx.lifecycle.ViewModel;

import com.infinity.architecture.base.models.adapter.BaseSpinnerItem;

import java.util.ArrayList;

public abstract class BaseSpinnerViewModel<BV, I extends BaseSpinnerItem> extends ViewModel {

    abstract protected void onBindView(BV binding, ArrayList<I> itemList, I item, int pos);
}
