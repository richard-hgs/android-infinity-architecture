package com.infinity.architecture.views.adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.infinity.architecture.utils.base.FragmentListener;
import com.infinity.architecture.utils.base.viewpager.ViewPagerFragmentStateAdapterCom;

import java.lang.reflect.Method;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class ViewPagerFragmentStateAdapter extends FragmentStateAdapter {

    private final ArrayList<FragmentItem<? extends Fragment>> fragmentItemList;

    @Nullable
    private final FragmentListener fragmentListener;

    public ViewPagerFragmentStateAdapter(
        @NonNull FragmentManager fragmentManager,
        @NonNull Lifecycle lifecycle,
        @NonNull ArrayList<FragmentItem<? extends Fragment>> fragmentItemList,
        @Nullable FragmentListener fragmentListener
    ) {
        super(fragmentManager, lifecycle);
        this.fragmentItemList = fragmentItemList;
        this.fragmentListener = fragmentListener;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        try {
            FragmentItem<?> fragmentItem = fragmentItemList.get(position);
            Class<?> fragClass = fragmentItem.getFragmentClass();
            Method getInstMethod = fragClass.getDeclaredMethod("getInstance");
            fragment = (Fragment) getInstMethod.invoke(null);

            if (fragment != null && fragmentItem.getArgs() != null) {
                fragment.setArguments(fragmentItem.getArgs());
            }

            // Setup listener to retrieve instanced ViewPager fragment view models when they are created
            if (fragment instanceof ViewPagerFragmentStateAdapterCom) {
                ((ViewPagerFragmentStateAdapterCom) fragment).setFragmentListener(fragmentListener);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (fragment == null) {
            throw new RuntimeException("Fragment can't be null");
        }

        return fragment;
    }

    @Override
    public int getItemCount() {
        return fragmentItemList.size();
    }

    public <F extends Fragment> void addFragmentItem(@NonNull FragmentItem<F> fragmentItem) {
        fragmentItemList.add(fragmentItem);
    }

    @NonNull
    public <F extends Fragment> FragmentItem<F> getFragmentItem(int position) {
        //noinspection unchecked
        return (FragmentItem<F>) fragmentItemList.get(position);
    }

    public static class FragmentItem<F extends Fragment> {
        private Class<F> fragmentClass;
        private String title;
        private Bundle args;

        private FragmentItem() {}

        @NonNull
        public static <F extends Fragment> FragmentItem<F> getInstance(
            @NonNull String title,
            @NonNull Class<F> fragment,
            @Nullable Bundle args
        ) {
            FragmentItem<F> fragmentItem = new FragmentItem<>();
            fragmentItem.fragmentClass = fragment;
            fragmentItem.title = title;
            fragmentItem.args = args;
            return fragmentItem;
        }

        @NonNull
        public Class<F> getFragmentClass() {
            return fragmentClass;
        }

        @NonNull
        public String getTitle() {
            return title;
        }

        @Nullable
        public Bundle getArgs() {
            return args;
        }
    }
}
