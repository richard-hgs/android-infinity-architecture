package com.infinity.architecture.base.ui.viewpager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.infinity.architecture.base.ui.adapter.AdapterRequires;
import com.infinity.architecture.views.adapters.ViewPagerFragmentStateAdapter;

import java.util.ArrayList;

public class BaseViewPagerFragmentStateAdapter extends ViewPagerFragmentStateAdapter {

    public BaseViewPagerFragmentStateAdapter(@NonNull AdapterRequires adapterRequires, @NonNull ArrayList<FragmentItem<? extends Fragment>> fragmentItemList) {
        super(
            adapterRequires.getFragChildFragManager() != null ? adapterRequires.getFragChildFragManager() : adapterRequires.getActFragManager(),
            adapterRequires.getLifecycle(),
            fragmentItemList
        );
    }
}
