package com.infinity.architecture.views;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * WHEN USING SWIPE REFRESH LAYOUT MUST STOP REFRESH ANIMATION AFTER ALL WORK DONE USING
 * {@link SwipeRefreshLayout#setRefreshing(boolean)}
 *
 * EACH REFRES WILL BE CALLED IN LISTENER ADDED BY
 * {@link SwipeRefreshLayout#setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener)}
 *
 * TO DEACTIVATE REFRESH USE
 */
public class CustomRecyclerViewRefreshLay extends SwipeRefreshLayout {
    private static final String TAG = "CustRecyclerViewRefLay";

    /**
     * ARMAZENA SE O SWIPE ESTÁ HABILITADO/DESABILITADO
     */
    private boolean swipeRefreshEnabled;

    /**
     * ARMAZENA O CUSTOM RECYCLER VIEW
     */
    private CustomRecyclerView customRecyclerView;

    /**
     * ARMAZENA O LISTENER DE SCROLL DO RECYCLER VIEW PARA USO DO SWIPE REFRESH LAYOUT
     */
    private RecyclerView.OnScrollListener recyclerScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if (swipeRefreshEnabled) {
                int topRowVerticalPosition =
                    (recyclerView == null || recyclerView.getChildCount() == 0)
                    ? 0
                    : recyclerView.getChildAt(0).getTop();

                RecyclerView.LayoutManager recyclerLayManager = customRecyclerView.getLayoutManager();
                if (recyclerLayManager != null) {
                    if (recyclerLayManager instanceof GridLayoutManager) {
                        GridLayoutManager recyclerGridLayManager = (GridLayoutManager) recyclerLayManager;
                        CustomRecyclerViewRefreshLay.this.setEnabled(recyclerGridLayManager.findFirstVisibleItemPosition() == 0 && topRowVerticalPosition >= 0);
                    } else if (recyclerLayManager instanceof LinearLayoutManager) {
                        LinearLayoutManager recyclerLinLayManager = (LinearLayoutManager) recyclerLayManager;
                        CustomRecyclerViewRefreshLay.this.setEnabled(recyclerLinLayManager.findFirstVisibleItemPosition() == 0 && topRowVerticalPosition >= 0);
                    } else {
                        CustomRecyclerViewRefreshLay.this.setEnabled(false);
                    }
                } else {
                    CustomRecyclerViewRefreshLay.this.setEnabled(false);
                }
            }
        }
    };

    /**
     * CONSTRUTOR 1
     * @param context   CONTEXT
     */
    public CustomRecyclerViewRefreshLay(@NonNull Context context) {
        super(context);
        init(null);
    }

    /**
     * CONSTRUTOR 2
     * @param context   CONTEXT
     * @param attrs     ATTRIBUTES
     */
    public CustomRecyclerViewRefreshLay(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    /**
     * INICIALIZA OS COMPONENTES
     * @param attrs     ATTRS
     */
    private void init(@Nullable AttributeSet attrs) {
        // INSTANCIA O CUSTOM RECYCLER VIEW
        customRecyclerView = new CustomRecyclerView(getContext());
        customRecyclerView.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        if (attrs != null) {
            /*try {
                Log.d(TAG, "" + attrs.getAttributeCount());
                for (int i=0; i<attrs.getAttributeCount(); i++) {
                    Log.d(TAG, "ATTR:" + attrs.getAttributeName(i) + " = " + attrs.getAttributeValue(i));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }*/
        }

        // QUANDO O RECYCLER VIEW FOR ROLADO PARA BAIXO
        customRecyclerView.addOnScrollListener(recyclerScrollListener);

        // ADICIONA O RECYCLER VIEW AO REFRESH LAYOUT
        addView(customRecyclerView);
    }

    /**
     * Set the {@link RecyclerView.LayoutManager} that this RecyclerView will use.
     *
     * <p>In contrast to other adapter-backed views such as {@link android.widget.ListView}
     * or {@link android.widget.GridView}, RecyclerView allows client code to provide custom
     * layout arrangements for child views. These arrangements are controlled by the
     * {@link RecyclerView.LayoutManager}. A LayoutManager must be provided for RecyclerView to function.</p>
     *
     * <p>Several default strategies are provided for common uses such as lists and grids.</p>
     *
     * @param layout LayoutManager to use
     */
    public void setLayoutManager(RecyclerView.LayoutManager layout) {
        customRecyclerView.setLayoutManager(layout);
    }

    /**
     * Return the {@link RecyclerView.LayoutManager} currently responsible for
     * layout policy for this RecyclerView.
     *
     * @return The currently bound LayoutManager
     */
    public RecyclerView.LayoutManager getLayoutManager() {
        return customRecyclerView.getLayoutManager();
    }

    /**
     * Set a new adapter to provide child views on demand.
     * <p>
     * When adapter is changed, all existing views are recycled back to the pool. If the pool has
     * only one adapter, it will be cleared.
     *
     * @param adapter The new adapter to set, or null to set no adapter.
     * //@see #swapAdapter(RecyclerView.Adapter, boolean)
     */
    public void setAdapter(RecyclerView.Adapter adapter) {
        customRecyclerView.setAdapter(adapter);
    }

    /**
     * Swaps the current adapter with the provided one. It is similar to
     * {@link #setAdapter(RecyclerView.Adapter)} but assumes existing adapter and the new adapter uses the same
     * {@link RecyclerView.ViewHolder} and does not clear the RecycledViewPool.
     * <p>
     * Note that it still calls onAdapterChanged callbacks.
     *
     * @param adapter The new adapter to set, or null to set no adapter.
     * @param removeAndRecycleExistingViews If set to true, RecyclerView will recycle all existing
     *                                      Views. If adapters have stable ids and/or you want to
     *                                      animate the disappearing views, you may prefer to set
     *                                      this to false.
     * @see #setAdapter(RecyclerView.Adapter)
     */
    public void swapAdapter(RecyclerView.Adapter adapter, boolean removeAndRecycleExistingViews) {
        customRecyclerView.swapAdapter(adapter, removeAndRecycleExistingViews);
    }

    /**
     * Add a listener that will be notified of any changes in scroll state or position.
     *
     * <p>Components that add a listener should take care to remove it when finished.
     * Other components that take ownership of a view may call {@link #clearOnScrollListeners()}
     * to remove all attached listeners.</p>
     *
     * @param listener listener to set or null to clear
     */
    public void addOnScrollListener(RecyclerView.OnScrollListener listener) {
        customRecyclerView.addOnScrollListener(listener);
    }

    /**
     * Remove a listener that was notified of any changes in scroll state or position.
     *
     * @param listener listener to set or null to clear
     */
    public void removeOnScrollListener(RecyclerView.OnScrollListener listener) {
        customRecyclerView.removeOnScrollListener(listener);
    }

    /**
     * Remove all secondary listener that were notified of any changes in scroll state or position.
     */
    public void clearOnScrollListeners() {
        customRecyclerView.clearOnScrollListeners();
        customRecyclerView.addOnScrollListener(recyclerScrollListener);
    }

    /**
     * HABILITA/DESABILITA OS EVENTOS DE REFRESH DO LAYOUT DO CUSTOM RECYCLER
     * @param enabled   HABILITA/DESABILITA
     */
    public void setSwipeRefreshEnabled(boolean enabled) {
        this.swipeRefreshEnabled = enabled;
    }
}
