package com.infinity.architecture.sampleapp.ui.screens.fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.infinity.architecture.base.models.ui.ActionBarInfo;
import com.infinity.architecture.base.models.ui.BackPressedInfo;
import com.infinity.architecture.base.models.ui.NavigationInfo;
import com.infinity.architecture.base.ui.recycler.BaseAdapterListenerImpl;
import com.infinity.architecture.base.ui.recycler.BaseRecyclerAdapter;
import com.infinity.architecture.base.ui.recycler.BaseRecyclerAdapterVH;
import com.infinity.architecture.base.ui.recycler.BaseRecyclerViewModel;
import com.infinity.architecture.utils.adapter.decorations.CustomRecyclerViewItemDecoration;
import com.infinity.architecture.sampleapp.R;
import com.infinity.architecture.sampleapp.backservices.AppApplication;
import com.infinity.architecture.sampleapp.databinding.RvInfinityViewsItemBinding;
import com.infinity.architecture.sampleapp.enums.EnScreen;
import com.infinity.architecture.sampleapp.model.RvAdptInfinityViewsItem;
import com.infinity.architecture.sampleapp.ui.adapters.RvAdptInfinityViewsVm;
import com.infinity.architecture.sampleapp.ui.screens.base.MyBaseFragmentViewModelOpt;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 *
 */
// Hilt - ViewModel injection
@HiltViewModel
public class FragmentInfinityViewsVm extends MyBaseFragmentViewModelOpt {

    private AppApplication appApplication;

    /**
     * {@link RecyclerView} without creating an adapter<br/>
     * {@link BaseRecyclerAdapter} will use reflection to take care of it. You only need to pass<br/>
     * {@link BaseRecyclerAdapter}&lt;</br>
     * <pre>
     * 1º Type -&gt; {@link BaseRecyclerAdapterVH}&lt;{@link ViewDataBinding}, {@link BaseRecyclerViewModel}&lt;{@link ViewDataBinding}, {@link Object}&gt;, {@link Object}&gt; -&gt; The holder for this adapter
     * 2º Type -&gt; {@link ViewDataBinding}, -> The binding class for item layout
     * 3º Type -&gt; {@link BaseRecyclerViewModel},  -> The view model to be instantiate for the itens of adapter to perform layout operations
     * 4º Type -&gt; {@link Object} -> The item class of the recycler
     * </pre>
     * &gt
     */
    public MutableLiveData<
        BaseRecyclerAdapter<
            BaseRecyclerAdapterVH<RvInfinityViewsItemBinding, RvAdptInfinityViewsVm, RvAdptInfinityViewsItem>,
            RvInfinityViewsItemBinding,
            RvAdptInfinityViewsVm,
            RvAdptInfinityViewsItem
        >
    > adptCv = new MutableLiveData<>();

    public MutableLiveData<RecyclerView.ItemDecoration> rvItemDecoration = new MutableLiveData<>(new CustomRecyclerViewItemDecoration(3, 10, true));

    private final ArrayList<RvAdptInfinityViewsItem> adptCvItems;

    // Hilt - Inject constructor
    @Inject
    public FragmentInfinityViewsVm(@NonNull AppApplication appApplication) {
        this.appApplication = appApplication;

        adptCvItems = new ArrayList<RvAdptInfinityViewsItem>() {{
            add(new RvAdptInfinityViewsItem(appApplication.getString(R.string.cv_title_infinity_button), EnScreen.CV_CUSTOM_BUTTONS));
            add(new RvAdptInfinityViewsItem(appApplication.getString(R.string.cv_title_infinity_image_button), EnScreen.CV_CUSTOM_IMAGE_BUTTONS));
            add(new RvAdptInfinityViewsItem(appApplication.getString(R.string.cv_title_infinity_image_view), EnScreen.CV_CUSTOM_IMAGE_VIEW));
            add(new RvAdptInfinityViewsItem(appApplication.getString(R.string.cv_title_infinity_edit_text), EnScreen.CV_CUSTOM_EDIT_TEXT));
            add(new RvAdptInfinityViewsItem(appApplication.getString(R.string.cv_title_infinity_text_input_edit_text), EnScreen.CV_CUSTOM_TEXT_INPUT_EDIT_TEXT));
        }};
    }

    /**
     * When the view model super functions are ready to be called, works like the onCreate
     * @param lifecycleOwner    The {@link LifecycleOwner} of this model
     * @param fragArgs          null or {@link Fragment} arguments {@link Bundle}
     * @param actArgs           null or {@link Activity} arguments {@link Bundle}
     */
    @Override
    protected void safeConstructor(@NonNull LifecycleOwner lifecycleOwner, @Nullable Bundle fragArgs, @Nullable Bundle actArgs) {
        try {
            setActionBar(ActionBarInfo.getInstance(appApplication.getString(R.string.cv_title), "", true, true));

            // Instantiates the adapter
            // The adapter will infer the arguments
            adptCv.setValue(
                new BaseRecyclerAdapter<>(
                    getAdapterRequires(),
                    adptCvItems,
                    R.layout.rv_infinity_views_item,
                    null,
                    true,
                    BaseRecyclerAdapter.getBaseViewHolderClass(),
                    RvInfinityViewsItemBinding.class,
                    RvAdptInfinityViewsVm.class,
                    new BaseAdapterListenerImpl<RvAdptInfinityViewsItem>() {
                        @Override
                        public void onItemClick(int actionId, RvAdptInfinityViewsItem item, int position) {
                            super.onItemClick(actionId, item, position);
                            onAdapterItemClick(item);
                        }
                    }
                )
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * When used inside a fragment view model you need to tell the fragment that this fragment will use options menu.
     * For this place -> {@link androidx.fragment.app.Fragment#setHasOptionsMenu(boolean)} inside the {@link androidx.fragment.app.Fragment#onCreate(Bundle)} with true value
     * @param menuItemId    The menu item id
     * @return              true=implemented, false=notImplemented
     */
    @Override
    protected boolean onOptionsItemSelected(int menuItemId) {
        if (menuItemId == android.R.id.home) {
            performBackPressed(BackPressedInfo.defaultBackPressed());
            return true;
        }
        return super.onOptionsItemSelected(menuItemId);
    }

    /**
     * Navigate to another screen
     * @param id    The id of navAction
     */
    private void navigateTo(int id) {
        navigate(NavigationInfo.defNavigation(id, false, null), null);
    }

    /**
     * Navigate to screen when {@link RvAdptInfinityViewsVm} item is clicked
     *
     * @param item {@link RvAdptInfinityViewsItem} item information
     */
    private void onAdapterItemClick(@NonNull RvAdptInfinityViewsItem item) {
        // When adapter item is clicked navigate to its screen
        if (item.getScreenType() == EnScreen.CV_CUSTOM_BUTTONS) {
            navigateTo(R.id.action_fragment_custom_views_to_fragment_cv_custom_button);
        } else if (item.getScreenType() == EnScreen.CV_CUSTOM_IMAGE_BUTTONS) {
            navigateTo(R.id.action_fragment_custom_views_to_fragment_cv_custom_image_button);
        } else if (item.getScreenType() == EnScreen.CV_CUSTOM_IMAGE_VIEW) {
            navigateTo(R.id.action_fragment_custom_views_to_fragment_cv_custom_image_view);
        } else if (item.getScreenType() == EnScreen.CV_CUSTOM_EDIT_TEXT) {
            navigateTo(R.id.action_fragment_custom_views_to_fragment_cv_custom_edit_text);
        } else if (item.getScreenType() == EnScreen.CV_CUSTOM_TEXT_INPUT_EDIT_TEXT) {
            navigateTo(R.id.action_fragment_custom_views_to_fragment_cv_custom_text_input_edit_text);
        }
    }
}
