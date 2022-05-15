package com.infinity.architecture.views.bindadapters;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;
import androidx.databinding.Observable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.infinity.architecture.utils.drawable.RippleEffect;
import com.infinity.architecture.utils.money.MoneyUtils;
import com.infinity.architecture.utils.reflection.TypeWrapper;
import com.infinity.architecture.utils.view.ViewUtils;
import com.infinity.architecture.views.CircleImageButton;
import com.infinity.architecture.views.CustomBottomNavigationView;
import com.infinity.architecture.views.CustomConstraintLayout;
import com.infinity.architecture.views.CustomNavigationView;
import com.infinity.architecture.views.CustomTabLayout;
import com.infinity.architecture.views.CustomTextView;
import com.infinity.architecture.views.FragmentContainerViewBind;
import com.infinity.architecture.views.InfinityButton;
import com.infinity.architecture.views.InfinityEditText;
import com.infinity.architecture.views.InfinityTextInputEditText;
import com.infinity.architecture.views.InfinityTextInputLayPicker;
import com.infinity.architecture.views.adapters.FrameLayoutAdapter;
import com.infinity.architecture.views.adapters.TabLayoutAdapter;
import com.infinity.architecture.views.adapters.ViewPagerFragmentStateAdapter;
import com.infinity.architecture.views.enums.EnEditTextSelection;
import com.infinity.architecture.views.model.CompoundDrawableClickInfo;
import com.infinity.architecture.views.model.DrawerStateInfo;
import com.infinity.architecture.views.model.EditTextSelectionInfo;
import com.infinity.architecture.views.model.MenuItemInfo;
import com.infinity.architecture.views.model.RvComputeLayout;
import com.infinity.architecture.views.model.RvLayoutComputedInfo;
import com.infinity.architecture.views.model.RvScrollEventInfo;
import com.infinity.architecture.views.model.ScrollTo;
import com.infinity.architecture.views.model.TabInfo;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class CustomViewBindingAdapter {
    private static final String TAG = "CViewBindingAdapter";

    @BindingAdapter(value = {"clickCountListener", "clickCountListenerIntervalMillis"}, requireAll = false)
    public static void clickCountListener(
        View view,
        ClickCountListener clickCountListener,
        Integer clickCountIntervalMillis
    ) {
        TypeWrapper<Integer> clickCount = TypeWrapper.getInstance(1);
        Handler handler = new Handler();
        TypeWrapper<Runnable> runnableResetCount = TypeWrapper.getInstance(null);
        TypeWrapper<Integer> mClickCountIntervalMillis = TypeWrapper.getInstance(300);
        if (clickCountIntervalMillis != null) {
            mClickCountIntervalMillis.setData(clickCountIntervalMillis);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (runnableResetCount.getData() != null) {
                    handler.removeCallbacks(runnableResetCount.getData());
                }

                clickCountListener.onClick(clickCount.getData());

                clickCount.setData(clickCount.getData() + 1);

                runnableResetCount.setData(new Runnable() {
                    @Override
                    public void run() {
                        clickCount.setData(1);
                    }
                });
                handler.postDelayed(runnableResetCount.getData(), mClickCountIntervalMillis.getData());
            }
        });
    }

    @BindingAdapter(value = {"throttleClickListener", "throttleLongClickListener", "throttleTimeMillis", "throttleEventListener"}, requireAll = false)
    public static void throttleClickListener(
        View view,
        @Nullable DefaultClickListener throttleClickListener,
        @Nullable DefaultClickListenerWithReturnBool throttleLongClickListener,
        @Nullable Integer throttleTimeMilis,
        @Nullable ThrottleEventListener throttleEventListener
    ) {
        if (throttleClickListener != null) {
            DefaultClickListenerWithReturnBool throttleHandlerClickListener = throttleClickListenerEventHandler(
                view, defaultThrottleClickListenerDispatcher(throttleClickListener), throttleTimeMilis, throttleEventListener
            );

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    throttleHandlerClickListener.onClick(null);
                }
            });
        }

        if (throttleLongClickListener != null) {
            DefaultClickListenerWithReturnBool throttleHandlerClickListener = throttleClickListenerEventHandler(
                view, throttleLongClickListener, throttleTimeMilis, throttleEventListener
            );

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    TypeWrapper<Boolean> clickHandled = TypeWrapper.getInstance(false);
                    throttleHandlerClickListener.onClick(new BindingReturnListener<Boolean>() {
                        @Override
                        public void onReturn(Boolean handled) {
                            clickHandled.setData(handled);
                        }
                    });
                    return clickHandled.getData();
                }
            });
        }
    }

    public static DefaultClickListenerWithReturnBool defaultThrottleClickListenerDispatcher(DefaultClickListener defaultClickListener) {
        return new DefaultClickListenerWithReturnBool() {
            @Override
            public void onClick(@Nullable BindingReturnListener<Boolean> bReturn) {
                defaultClickListener.onClick();
                if (bReturn != null) {
                    bReturn.onReturn(true);
                }
            }
        };
    }

    public static DefaultClickListenerWithReturnBool throttleClickListenerEventHandler(
        View view,
        @NonNull DefaultClickListenerWithReturnBool throttleClickListener,
        @Nullable Integer throttleTimeMilis,
        @Nullable ThrottleEventListener throttleEventListener
    ) {
        String settingThrottlingMsg = "throttleClickListener -> "+view.getClass().getSimpleName()+"({id=\""+ ViewUtils.getViewIdName(view)+"\"}) -> setting throttle click listener";

        Log.d(TAG, settingThrottlingMsg);

        TypeWrapper<Boolean> allowClick = TypeWrapper.getInstance(true);
        Integer finalThrottleTimeMilis = throttleTimeMilis != null ? throttleTimeMilis : 2000;
        TypeWrapper<Long> startTimeOfClick = TypeWrapper.getInstance(0L);

        return new DefaultClickListenerWithReturnBool() {
            @Override
            public void onClick(@Nullable BindingReturnListener<Boolean> bReturn) {
//                Log.d(TAG, "onClick: " + allowClick[0]);
                if (allowClick.getData()) {

                    if (finalThrottleTimeMilis > 0) {
                        allowClick.setData(false);
                        startTimeOfClick.setData(new Date().getTime());

                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                allowClick.setData(true);
                            }
                        }, finalThrottleTimeMilis);
                    }

                    throttleClickListener.onClick(new BindingReturnListener<Boolean>() {
                        @Override
                        public void onReturn(Boolean handled) {
                            if (bReturn != null) {
                                bReturn.onReturn(handled);
                            }
                        }
                    });
                } else {
                    long diffTime = new Date().getTime() - startTimeOfClick.getData();
                    String throttleMessage = "throttleClickListener -> "+view.getClass().getSimpleName()+"({id=\""+ViewUtils.getViewIdName(view)+"\"}) -> onClick() THROTTLING Must wait (elapsedTime: " + diffTime + " / waitTime:" + finalThrottleTimeMilis + "ms) and try again!";
                    if (throttleEventListener != null) {
                        throttleEventListener.throttleEventListener(diffTime, finalThrottleTimeMilis, (finalThrottleTimeMilis - diffTime), throttleMessage);
                    }
                    Log.d(TAG, throttleMessage);

                    if (bReturn != null) {
                        bReturn.onReturn(false);
                    }
                }
            }
        };
    }

    @BindingAdapter("layout_height")
    public static void setLayoutHeight(View view, int height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = height;
        view.setLayoutParams(layoutParams);
    }

    @BindingAdapter("layout_width")
    public static void setLayoutWidth(View view, int width) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = width;
        view.setLayoutParams(layoutParams);
    }

    @BindingAdapter(value= {"spannableStringBuilderText"})
    public static void setSpannableBuilderText(TextView textView, @NonNull SpannableStringBuilder spanText) {
        textView.setText(spanText);
    }

    @BindingAdapter(value = {"onClick"})
    public static void textViewClickListener(TextView textView, @NonNull DefaultClickListener clickListener) {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClick();
            }
        });
    }

    @BindingAdapter(value = {"textColor"})
    public static void textViewSetTextColor(TextView textView, int textColor) {
        textView.setTextColor(textColor);
    }

    @BindingAdapter(value = {"requestFocus"})
    public static void textViewRequestFocus(TextView textView, boolean requestFocus) {
        if (requestFocus) {
            textView.setFocusableInTouchMode(true);
            textView.requestFocus();
        }
    }

    @BindingAdapter(value = {"layoutParams"})
    public static void setTextViewLayoutParams(CustomTextView textView, ViewGroup.LayoutParams layoutParams) {
        Log.d(TAG, "setLayoutParams: " + layoutParams);
        if (layoutParams != null) {
            textView.setLayoutParams(layoutParams);
            textView.invalidate();
        }
    }

    @InverseBindingAdapter(attribute = "layoutParams")
    public static ViewGroup.LayoutParams getTextViewLayoutParams(CustomTextView textView) {
        return textView.getLayoutParams();
    }

    @BindingAdapter(value = {"layoutParamsAttrChanged"})
    public static void textViewLayoutParamsAttrChanged(CustomTextView textView, InverseBindingListener inverseBindingListener) {
        if (inverseBindingListener != null) {
            textView.getLayParamsState().addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(Observable sender, int propertyId) {
                    inverseBindingListener.onChange();
                }
            });
        }
    }

    @BindingAdapter(value = {"addNavigationItemSelectedListener"})
    public static void addNavigationItemSelectedListener(
        CustomNavigationView customNavigationView,
        @NonNull NavigationItemSelectedListener listener
    ) {
        if (listener != null) {
            customNavigationView.addNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    MenuItemInfo menuItemInfo = new MenuItemInfo();
                    menuItemInfo.setItemId(item.getItemId());

                    final boolean[] returnValue = {false};

                    listener.onNavigationItemSelected(menuItemInfo, new BindingReturnListener<Boolean>() {
                        @Override
                        public void onReturn(Boolean aBoolean) {
                            returnValue[0] = aBoolean;
                        }
                    });

                    return returnValue[0];
                }
            });
        }
    }

    @BindingAdapter(value = {"addBottomNavigationItemSelectedListener"})
    public static void addBottomNavigationItemSelectedListener(
        CustomBottomNavigationView customBottomNavigationView,
        @NonNull NavigationItemSelectedListener listener
    ) {
        if (listener != null) {
            customBottomNavigationView.addOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    MenuItemInfo menuItemInfo = new MenuItemInfo();
                    menuItemInfo.setItemId(item.getItemId());

                    final boolean[] returnValue = {false};

                    listener.onNavigationItemSelected(menuItemInfo, new BindingReturnListener<Boolean>() {
                        @Override
                        public void onReturn(Boolean aBoolean) {
                            returnValue[0] = aBoolean;
                        }
                    });

                    return returnValue[0];
                }
            });
        }
    }

    @BindingAdapter(value = {"menuItemChecked"})
    public static void bottomNavigationMenuItemChecked(
        CustomBottomNavigationView customBottomNavigationView,
       @Nullable int menuItemChecked
    ) {
        MenuItem menuItem = customBottomNavigationView.getMenu().findItem(menuItemChecked);
        if (menuItem != null) {
            menuItem.setChecked(true);
        }
    }

    @BindingAdapter(value = {"backgroundResource"})
    public static void constraintLayoutBgIntColor(ConstraintLayout constraintLayout, int color) {
        constraintLayout.setBackgroundResource(color);
    }

    @BindingAdapter(value = {"backgroundDrawable"})
    public static void constraintLayoutBgDrawable(ConstraintLayout constraintLayout, Drawable drawable) {
        constraintLayout.setBackground(drawable);
    }

    @BindingAdapter(value = {"webViewClientListener"})
    public static void webViewClientListener(WebView webView, WebViewClientListener webViewClientListener) {
        final WebViewClientBinding[] webViewClientBindingImpl = {null};

        // Notify the binder to get the client binding
        webViewClientListener.onSetWebViewClientListener(new BindingReturnListener<WebViewClientBindingImpl>() {
            @Override
            public void onReturn(WebViewClientBindingImpl webViewClientBinding) {
                webViewClientBindingImpl[0] = webViewClientBinding;
            }
        });

        if (webViewClientBindingImpl[0] != null) {
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    super.onReceivedError(view, request, error);
                    webViewClientBindingImpl[0].onReceivedError(request, error);
                }

                @Override
                public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                    super.onReceivedHttpError(view, request, errorResponse);
                    webViewClientBindingImpl[0].onReceivedHttpError(request, errorResponse);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    webViewClientBindingImpl[0].onPageFinished(url);
                }
            });
        } else {
            webView.setWebViewClient(null);
        }
    }

    @BindingAdapter(value = {"jsIsEnabled"})
    public static void webViewJsEnabled(WebView webView, boolean jsIsEnabled) {
        webView.getSettings().setJavaScriptEnabled(jsIsEnabled);
    }

    @BindingAdapter(value = {"url"})
    public static void loadWebViewUrl(WebView webView, String url) {
        webView.loadUrl(url);
    }

    @BindingAdapter(value = {"circle_imgbtn_color"})
    public static void setCircleImgBtnColor(CircleImageButton btn, int color) {
        btn.setBtnColor(color);
    }

    @BindingAdapter(value = {"circle_imgbtn_border_color"})
    public static void setCircleImgBorderColor(CircleImageButton btn, int color) {
        btn.setBorderColor(color);
    }

    @BindingAdapter(value = {"circle_imgbtn_border_dash_space"})
    public static void setCircleImgBorderDashSpace(CircleImageButton btn, int dashSpace) {
        btn.setBorderDashSpace(dashSpace);
    }

    @BindingAdapter(value = {"circle_imgbtn_border_width"})
    public static void setCircleImgBorderWidth(CircleImageButton btn, int borderWidth) {
        btn.setBorderWidth(borderWidth);
    }

    @BindingAdapter(value = {"coloredBtnColor"})
    public static void setColoredButtonColor(InfinityButton infinityButton, int color) {
        infinityButton.setBgColor(color);
    }

    @BindingAdapter(value = {"src"})
    public static void setImageViewDrawable(ImageView imgv, Object drawable) {
        if (drawable instanceof Drawable) {
            imgv.setImageDrawable((Drawable) drawable);
        } else if (drawable instanceof Integer) {
            imgv.setImageResource((Integer) drawable);
        } else if (drawable instanceof Bitmap) {
            imgv.setImageBitmap((Bitmap) drawable);
        }
    }

    @BindingAdapter(value = {"url", "errorImageRes", "placeholderImageRes", "shapeType"}, requireAll = false)
    public static void setImageViewImageUrl(
        ImageView imgv, String urlImage, @Nullable Object errorImageRes, @Nullable Object placeHolderImageRes, @Nullable Integer shapeType
    ) {
        // imgv.setImageResource(drawable);
        RequestCreator requestCreator =  Picasso.get().load(urlImage);
        if (errorImageRes != null) {
            if (errorImageRes instanceof Drawable) {
                requestCreator.error((Drawable) errorImageRes);
            } else if (errorImageRes instanceof Integer) {
                requestCreator.error((Integer) errorImageRes);
            }
        }
        if (placeHolderImageRes != null) {
            if (placeHolderImageRes instanceof Drawable) {
                requestCreator.placeholder((Drawable) placeHolderImageRes);
            }
            if (placeHolderImageRes instanceof Integer) {
                requestCreator.placeholder((Integer) placeHolderImageRes);
            }
        }
        if (shapeType != null) {
            if (shapeType == 2) {
                requestCreator.transform(new CropCircleTransformation());
            }
        }
        requestCreator.into(imgv);
    }

    @BindingAdapter(value = {"file", "errorImageRes", "placeholderImageRes", "shapeType"}, requireAll = false)
    public static void setImageViewImageFile(
        ImageView imgv, File fileImage, @Nullable Object errorImageRes, @Nullable Object placeHolderImageRes, @Nullable Integer shapeType
    ) {
        // imgv.setImageResource(drawable);
        RequestCreator requestCreator =  Picasso.get().load(fileImage);
        if (errorImageRes != null) {
            if (errorImageRes instanceof Drawable) {
                requestCreator.error((Drawable) errorImageRes);
            } else if (errorImageRes instanceof Integer) {
                requestCreator.error((Integer) errorImageRes);
            }
        }
        if (placeHolderImageRes != null) {
            if (placeHolderImageRes instanceof Drawable) {
                requestCreator.placeholder((Drawable) placeHolderImageRes);
            }
            if (placeHolderImageRes instanceof Integer) {
                requestCreator.placeholder((Integer) placeHolderImageRes);
            }
        }
        if (shapeType != null) {
            if (shapeType == 2) {
                requestCreator.transform(new CropCircleTransformation());
            }
        }
        requestCreator.into(imgv);
    }

    @BindingAdapter(value = {"layoutManager"})
    public static void setRvLayoutManager(RecyclerView rv, RecyclerView.LayoutManager layoutManager) {
        rv.setLayoutManager(layoutManager);
    }

//    @BindingAdapter(value = {"adapter"})
//    public static void setRvAdapter(RecyclerView rv, RecyclerView.Adapter<?> adapter) {
//        rv.setAdapter(adapter);
////        rv.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
////            @Override
////            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
////                Log.d(TAG, String.format(LocaleHelper.PORTUGUES, "left: %d, top: %d, right: %d, bottom: %d", left, top, right, bottom));
////                Log.d(TAG, "height: " + rv.computeVerticalScrollRange());
////            }
////        });
////        if (adapter != null) {
////            adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
////                @Override
////                public void onChanged() {
////                    super.onChanged();
////                    Log.d(TAG, "height: " + rv.computeVerticalScrollRange());
////                }
////            });
////        }
//    }

    @BindingAdapter(value = {"computeLayout", "onLayoutComputed"})
    public static void setRvComputeLayout(RecyclerView rv, RvComputeLayout rvComputeLayout, OnRvLayoutComputedListener onRvLayoutComputedListener) {
        int vertScroll = rv.computeVerticalScrollOffset();
        if (rvComputeLayout != null) {
            int childSumHeight = 0;

            int oldScrollX = rv.getScrollX();
            int oldScrollY = rv.getScrollY();
            rv.scrollBy(oldScrollX, 1);

//            Log.d(TAG, "certScroll: " + vertScroll);

            for (int i = 0; i < rv.getChildCount(); i++) {
                childSumHeight += rv.getChildAt(i).getMeasuredHeight();
            }
            rv.scrollBy(oldScrollX, oldScrollY);

            RvLayoutComputedInfo rvLayoutComputedInfo = RvLayoutComputedInfo.getInstance(
                childSumHeight,
                vertScroll
            );
            onRvLayoutComputedListener.onLayoutComputed(rvLayoutComputedInfo);
        }
    }

    @BindingAdapter(value = {"itemDecoration"})
    public static void setRvItemDecoration(RecyclerView rv, RecyclerView.ItemDecoration itemDecoration) {
        for (int i=0; i<rv.getItemDecorationCount(); i++) {
            rv.removeItemDecorationAt(0);
        }
        if (itemDecoration != null) {
            rv.addItemDecoration(itemDecoration);
        }
    }

    @BindingAdapter(value = {"itemDecorations"})
    public static void setRvItemDecorations(RecyclerView rv, RecyclerView.ItemDecoration[] itemDecorations) {
        for (int i=0; i<rv.getItemDecorationCount(); i++) {
            rv.removeItemDecorationAt(0);
        }
        if (itemDecorations != null) {
            for (RecyclerView.ItemDecoration decorAt : itemDecorations) {
                rv.addItemDecoration(decorAt);
            }
        }
    }

    @BindingAdapter(value = {"scrollTo"})
    public static void setRvScrollTo(RecyclerView rv, ScrollTo scrollTo) {
        if (scrollTo != null) {
            if (scrollTo.getX() != null || scrollTo.getY() != null) {
                int xScroll = rv.computeHorizontalScrollOffset();
                int yScroll = rv.computeVerticalScrollOffset();

                if (scrollTo.getX() != null) {
                    xScroll = scrollTo.getX();
                }
                if (scrollTo.getY() != null) {
                    yScroll = scrollTo.getY();
                }

                if (scrollTo.getSide() != null && scrollTo.getSide() == ScrollTo.SIDE_BOTTOM) {
                    int yMaxScroll = rv.computeVerticalScrollRange();
                    yScroll = yMaxScroll;
                }

                if (scrollTo.getSmoothScroll() != null && scrollTo.getSmoothScroll()) {
                    rv.smoothScrollBy(xScroll, yScroll);
                } else {
                    rv.scrollBy(xScroll, yScroll);
                }
            } else if (scrollTo.getPosition() != null) {
                if (scrollTo.getSmoothScroll() != null && scrollTo.getSmoothScroll()) {
                    rv.smoothScrollToPosition(scrollTo.getPosition());
                } else {
                    rv.scrollToPosition(scrollTo.getPosition());
                }
            }
        }
    }

    @BindingAdapter(value = {"scrollListener"})
    public static void setRvScrollListener(
        RecyclerView rv,
        RecyclerViewScrollListener recyclerViewScrollListener
    ) {
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int oldx;
            int oldy;
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int x = recyclerView.computeHorizontalScrollOffset();
                int y = recyclerView.computeVerticalScrollOffset();

                recyclerViewScrollListener.onScroll(RvScrollEventInfo.getInstance(oldx, oldy, x, y));

                this.oldx = dx;
                this.oldy = dy;
            }
        });
    }

    @BindingAdapter(value = {"custom_constlay_bg_border_color"})
    public static void setCustomConstLayBorderColor(CustomConstraintLayout lay, int color) {
        lay.setBgBorderColor(color);
    }

    @BindingAdapter(value = {"custom_constlay_bg_color"})
    public static void setCustomConstLayBgColor(CustomConstraintLayout lay, int color) {
        lay.setBgColor(color);
    }


    @BindingAdapter(value = "constraintSet")
    public static void setConstLayConstraintSet(CustomConstraintLayout lay, ConstraintSet constraintSet) {
        if (constraintSet != null) {
            constraintSet.applyTo(lay);
        }
    }

    @InverseBindingAdapter(attribute = "constraintSet")
    public static ConstraintSet getConstLayConstraintSet(CustomConstraintLayout lay) {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(lay);
        return constraintSet;
    }

    @BindingAdapter(value = {"constraintSetAttrChanged"})
    public static void setConstLayConstraintSetChangeListener(CustomConstraintLayout lay, InverseBindingListener listener) {
        if (listener != null) {
            lay.getConstraintSetState().addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(Observable sender, int propertyId) {
                    listener.onChange();
                }
            });

//            guideline.getGuidePercentState().addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
//                override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
//                    listener.onChange()
//                }
//            })
        }
    }

    @BindingAdapter(value = {"drawerState"})
    public static void setDrawerState(
        DrawerLayout drawerLayout,
        DrawerStateInfo drawerStateInfo
    ) {
        if (drawerStateInfo != null) {
            if (drawerStateInfo.isOpen()) {
                drawerLayout.openDrawer(drawerStateInfo.getGravity());
            } else {
                drawerLayout.closeDrawer(drawerStateInfo.getGravity());
            }
        }

        Log.d(TAG, "setDrawerState: " + drawerStateInfo);
    }

    @BindingAdapter(value = {"fragmentContainerViewId", "onDestinationChange"})
    public static void fragmentContainerViewBindOnDestinationChangeListener(
        FragmentContainerViewBind fragmentContainerViewBind,
        int fragmentContainerViewId,
        OnDestinationChangedListener onDestinationChangedListener
    ) {
        if (onDestinationChangedListener != null) {
            FragmentManager fm = fragmentContainerViewBind.getSupportFragmentManager();
            if (fm != null) {
                NavHostFragment navHostFragment = (NavHostFragment) fm.findFragmentById(fragmentContainerViewId);
                if (navHostFragment != null) {
                    NavController navController = navHostFragment.getNavController();
                    navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
                        @Override
                        public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                            onDestinationChangedListener.onDestinationChanged(destination, arguments);
                        }
                    });
                }
            }

            Log.d(TAG, "fm: " + fm + " id: " + fragmentContainerViewId);
        }
    }

    @BindingAdapter(value = {"dateValue"})
    public static void customTextInputLayPickerDateValue(
        InfinityTextInputLayPicker customTextInputLayPicker,
        Date newDate
    ) {
        try {
            if (newDate == customTextInputLayPicker.getDate()) {
                return;
            }
            customTextInputLayPicker.setDate(newDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @BindingAdapter(value = {"dateValueAttrChanged"})
    public static void customTextInputLayPickerDateValueAttrChanged(
        InfinityTextInputLayPicker customTextInputLayPicker,
        InverseBindingListener inverseBindingListener
    ) {
        try {
            customTextInputLayPicker.addDatePickerListener(new InfinityTextInputLayPicker.DatePickerListener() {
                @Override
                public void onDateChange(@Nullable Date date, @Nullable String strDate) {
                    inverseBindingListener.onChange();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @InverseBindingAdapter(attribute = "dateValue")
    public static Date customTextInputLayPickerDateValueInverse(
        InfinityTextInputLayPicker customTextInputLayPicker
    ) {
        return customTextInputLayPicker.getDate();
    }

    @BindingAdapter(value = {"strDateValue"})
    public static void customTextInputLayPickerStrDateValue(
        InfinityTextInputLayPicker customTextInputLayPicker,
        String strNewDate
    ) {
        try {
            if (
                (strNewDate == null && customTextInputLayPicker.getStrDate() == null) ||
                (strNewDate != null && customTextInputLayPicker.getStrDate() != null && strNewDate.equals(customTextInputLayPicker.getStrDate()))
            ) {
                return;
            }
            customTextInputLayPicker.setStrDate(strNewDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @BindingAdapter(value = {"strDateValueAttrChanged"}, requireAll = false)
    public static void customTextInputLayPickerStrDateValueAttrChanged(
        InfinityTextInputLayPicker customTextInputLayPicker,
        InverseBindingListener inverseBindingListener
    ) {
        try {
            customTextInputLayPicker.addDatePickerListener(new InfinityTextInputLayPicker.DatePickerListener() {
                @Override
                public void onDateChange(@Nullable Date date, @Nullable String strDate) {
                    inverseBindingListener.onChange();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @InverseBindingAdapter(attribute = "strDateValue")
    public static String customTextInputLayPickerStrDateValueInverse(
        InfinityTextInputLayPicker customTextInputLayPicker
    ) {
        return customTextInputLayPicker.getStrDate();
    }

    @BindingAdapter(value = {"focusChangeListener"})
    public static void editTextFocusChangeListener(TextInputEditText editText, TextInputEditTextFocusChangeListener focusChangeListener) {
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                focusChangeListener.onFocusChange(hasFocus);
            }
        });
    }

    @BindingAdapter(value = {"textChangeListener"})
    public static void editTextChangeListener(TextInputEditText editText, TextInputEditTextTextChangeListener textChangeListener) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                textChangeListener.onTextChange(s != null ? s.toString() : null);
            }
        });
    }

    @BindingAdapter(value = {"editorActionListener"})
    public static void editTextEditorActionListener(
        EditText editText,
        EditTextEditorActionListener editTextEditorActionListener
    ) {
        if (editTextEditorActionListener != null) {
            editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    TypeWrapper<Boolean> bReturn = TypeWrapper.getInstance(false);

                    editTextEditorActionListener.onEditorAction(actionId, event, new BindingReturnListener<Boolean>() {
                        @Override
                        public void onReturn(Boolean aBoolean) {
                            bReturn.setData(aBoolean);
                        }
                    });

                    return bReturn.getData();
                }
            });
        } else {
            editText.setOnEditorActionListener(null);
        }
    }

    @BindingAdapter(value = {"textSelection"})
    public static void editTextSelection(
        EditText editText,
        EditTextSelectionInfo editTextSelectionInfo
    ) {
        if (editTextSelectionInfo != null) {
            if (editTextSelectionInfo.getEnEditTextSelection() == EnEditTextSelection.ALL) {
                editText.selectAll();
            } else if (editTextSelectionInfo.getEnEditTextSelection() == EnEditTextSelection.CUSTOM) {
                editText.setSelection(editTextSelectionInfo.getStartOffset(), editTextSelectionInfo.getEndOffset());
            } else if (editTextSelectionInfo.getEnEditTextSelection() == EnEditTextSelection.DESELECT) {
                editText.setSelection(editText.getText().length());
            } else if (editTextSelectionInfo.getEnEditTextSelection() == EnEditTextSelection.CARET_POS) {
                editText.setSelection(editTextSelectionInfo.getStartOffset());
            } else if (editTextSelectionInfo.getEnEditTextSelection() == EnEditTextSelection.MOVE_CARET_TO_END) {
                editText.setSelection(editText.getText().length());
            }
        }
    }

    @BindingAdapter(value = {"unmaskedText"})
    public static void customEditTextSetUnmaskedText(
        InfinityEditText infinityEditText,
        String unmaskedText
    ) {
        if (
            (unmaskedText == null && infinityEditText.getUnmaskedText() == null) ||
            (unmaskedText != null && infinityEditText.getUnmaskedText() != null && unmaskedText.equals(infinityEditText.getUnmaskedText()))
        ) {
            return;
        }

        infinityEditText.setText(unmaskedText);
        if (infinityEditText.getText() != null) {
            infinityEditText.setSelection(infinityEditText.getText().length());
        } else {
            infinityEditText.setSelection(0);
        }
    }

    @InverseBindingAdapter(attribute = "unmaskedText", event = "unmaskedTextAttrChanged")
    public static String customEditTextSetUnmaskedTextInv(
        InfinityEditText infinityEditText
    ) {
        return infinityEditText.getUnmaskedText();
    }

    @BindingAdapter(value = {"unmaskedTextAttrChanged"})
    public static void customEditTextSetUnmaskedTextAttrChanged(
        InfinityEditText infinityEditText,
        InverseBindingListener inverseBindingListener
    ) {
        infinityEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                inverseBindingListener.onChange();
            }
        });
    }

    @BindingAdapter(value = {"bigdecPriceText"})
    public static void customEditTextSetBigdecPriceText(
        InfinityEditText infinityEditText,
        BigDecimal bigDecimal
    ) {
        if (
            (bigDecimal == null && infinityEditText.getMonetaryMaskBigDec() == null) ||
            (bigDecimal != null && infinityEditText.getMonetaryMaskBigDec() != null && bigDecimal.compareTo(infinityEditText.getMonetaryMaskBigDec()) == 0)
        ) {
            return;
        }

        infinityEditText.setText(bigDecimal != null ? MoneyUtils.formatarDouble(bigDecimal.doubleValue(), 2, 2) : "0");
    }

    @BindingAdapter(value = {"bigdecPriceTextAttrChanged"})
    public static void customEditTextSetBigdecPriceTextAttrChanged(
        InfinityEditText infinityEditText,
        InverseBindingListener inverseBindingListener
    ) {
        infinityEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                inverseBindingListener.onChange();
            }
        });
    }

    @InverseBindingAdapter(attribute = "bigdecPriceText", event = "bigdecPriceTextAttrChanged")
    public static BigDecimal customEditTextSetBigdecPriceTextInv(
        InfinityEditText infinityEditText
    ) {
        return infinityEditText.getMonetaryMaskBigDec();
    }

    @BindingAdapter(value = {
        "dlThrottleClickListener", "dlThrottleTimeMillis", "dlThrottleEventListener",
        "dtThrottleClickListener", "dtThrottleTimeMillis", "dtThrottleEventListener",
        "drThrottleClickListener", "drThrottleTimeMillis", "drThrottleEventListener",
        "dbThrottleClickListener", "dbThrottleTimeMillis", "dbThrottleEventListener"
    }, requireAll = false)
    public static void customEditTextDrawableClickListener(
        InfinityEditText infinityEditText,
        @Nullable DefaultClickListenerWithReturnBool dlThrottleClickListener, @Nullable Integer dlThrottleTimeMilis, @Nullable ThrottleEventListener dlThrottleEventListener,
        @Nullable DefaultClickListenerWithReturnBool dtThrottleClickListener, @Nullable Integer dtThrottleTimeMilis, @Nullable ThrottleEventListener dtThrottleEventListener,
        @Nullable DefaultClickListenerWithReturnBool drThrottleClickListener, @Nullable Integer drThrottleTimeMilis, @Nullable ThrottleEventListener drThrottleEventListener,
        @Nullable DefaultClickListenerWithReturnBool dbThrottleClickListener, @Nullable Integer dbThrottleTimeMilis, @Nullable ThrottleEventListener dbThrottleEventListener
    ) {
        TypeWrapper<DefaultClickListenerWithReturnBool> dlThrottleHandlerClickListener = TypeWrapper.getInstance(null);
        TypeWrapper<DefaultClickListenerWithReturnBool> dtThrottleHandlerClickListener = TypeWrapper.getInstance(null);
        TypeWrapper<DefaultClickListenerWithReturnBool> drThrottleHandlerClickListener = TypeWrapper.getInstance(null);
        TypeWrapper<DefaultClickListenerWithReturnBool> dbThrottleHandlerClickListener = TypeWrapper.getInstance(null);

        if (dlThrottleClickListener != null) {
            dlThrottleHandlerClickListener.setData(
                throttleClickListenerEventHandler(
                    infinityEditText, dlThrottleClickListener, dlThrottleTimeMilis, dlThrottleEventListener
                )
            );
        }
        if (dtThrottleClickListener != null) {
            dtThrottleHandlerClickListener.setData(
                throttleClickListenerEventHandler(
                    infinityEditText, dtThrottleClickListener, dtThrottleTimeMilis, dtThrottleEventListener
                )
            );
        }
        if (drThrottleClickListener != null) {
            drThrottleHandlerClickListener.setData(
                throttleClickListenerEventHandler(
                    infinityEditText, drThrottleClickListener, drThrottleTimeMilis, drThrottleEventListener
                )
            );
        }
        if (dbThrottleClickListener != null) {
            dbThrottleHandlerClickListener.setData(
                throttleClickListenerEventHandler(
                    infinityEditText, dbThrottleClickListener, dbThrottleTimeMilis, dbThrottleEventListener
                )
            );
        }

        infinityEditText.setCompoundDrawablesClickListener(new InfinityEditText.DrawableClickListenerAbs() {
            @Override
            public boolean onDrawableLeftClick() {
                Log.d(TAG, "drawableLeftClick");
                TypeWrapper<Boolean> mHandled = TypeWrapper.getInstance(false);
                if (dlThrottleHandlerClickListener.getData() != null) {
                    dlThrottleHandlerClickListener.getData().onClick(new BindingReturnListener<Boolean>() {
                        @Override
                        public void onReturn(Boolean handled) {
                            mHandled.setData(handled);
                        }
                    });
                }
                return mHandled.getData();
            }

            @Override
            public boolean onDrawableTopClick() {
                TypeWrapper<Boolean> mHandled = TypeWrapper.getInstance(false);
                if (dtThrottleHandlerClickListener.getData() != null) {
                    dtThrottleHandlerClickListener.getData().onClick(new BindingReturnListener<Boolean>() {
                        @Override
                        public void onReturn(Boolean handled) {
                            mHandled.setData(handled);
                        }
                    });
                }
                return mHandled.getData();
            }

            @Override
            public boolean onDrawableRightClick() {
                TypeWrapper<Boolean> mHandled = TypeWrapper.getInstance(false);
                if (drThrottleHandlerClickListener.getData() != null) {
                    drThrottleHandlerClickListener.getData().onClick(new BindingReturnListener<Boolean>() {
                        @Override
                        public void onReturn(Boolean handled) {
                            mHandled.setData(handled);
                        }
                    });
                }
                return mHandled.getData();
            }

            @Override
            public boolean onDrawableBottomClick() {
                TypeWrapper<Boolean> mHandled = TypeWrapper.getInstance(false);
                if (dbThrottleHandlerClickListener.getData() != null) {
                    dbThrottleHandlerClickListener.getData().onClick(new BindingReturnListener<Boolean>() {
                        @Override
                        public void onReturn(Boolean handled) {
                            mHandled.setData(handled);
                        }
                    });
                }
                return mHandled.getData();
            }
        });
    }

    @BindingAdapter(value = {
        "dlThrottleClickListener", "dlThrottleTimeMillis", "dlThrottleEventListener",
        "dtThrottleClickListener", "dtThrottleTimeMillis", "dtThrottleEventListener",
        "drThrottleClickListener", "drThrottleTimeMillis", "drThrottleEventListener",
        "dbThrottleClickListener", "dbThrottleTimeMillis", "dbThrottleEventListener"
    }, requireAll = false)
    public static void customTextInputEditTextDrawablesThrottleClickListener(
        InfinityTextInputEditText infinityTextInputEditText,
        @Nullable DefaultClickListenerWithReturnBool dlThrottleClickListener, @Nullable Integer dlThrottleTimeMilis, @Nullable ThrottleEventListener dlThrottleEventListener,
        @Nullable DefaultClickListenerWithReturnBool dtThrottleClickListener, @Nullable Integer dtThrottleTimeMilis, @Nullable ThrottleEventListener dtThrottleEventListener,
        @Nullable DefaultClickListenerWithReturnBool drThrottleClickListener, @Nullable Integer drThrottleTimeMilis, @Nullable ThrottleEventListener drThrottleEventListener,
        @Nullable DefaultClickListenerWithReturnBool dbThrottleClickListener, @Nullable Integer dbThrottleTimeMilis, @Nullable ThrottleEventListener dbThrottleEventListener
    ) {

        TypeWrapper<DefaultClickListenerWithReturnBool> dlThrottleHandlerClickListener = TypeWrapper.getInstance(null);
        TypeWrapper<DefaultClickListenerWithReturnBool> dtThrottleHandlerClickListener = TypeWrapper.getInstance(null);
        TypeWrapper<DefaultClickListenerWithReturnBool> drThrottleHandlerClickListener = TypeWrapper.getInstance(null);
        TypeWrapper<DefaultClickListenerWithReturnBool> dbThrottleHandlerClickListener = TypeWrapper.getInstance(null);

        if (dlThrottleClickListener != null) {
            dlThrottleHandlerClickListener.setData(
                throttleClickListenerEventHandler(
                    infinityTextInputEditText, dlThrottleClickListener, dlThrottleTimeMilis, dlThrottleEventListener
                )
            );
        }
        if (dtThrottleClickListener != null) {
            dtThrottleHandlerClickListener.setData(
                throttleClickListenerEventHandler(
                    infinityTextInputEditText, dtThrottleClickListener, dtThrottleTimeMilis, dtThrottleEventListener
                )
            );
        }
        if (drThrottleClickListener != null) {
            drThrottleHandlerClickListener.setData(
                throttleClickListenerEventHandler(
                    infinityTextInputEditText, drThrottleClickListener, drThrottleTimeMilis, drThrottleEventListener
                )
            );
        }
        if (dbThrottleClickListener != null) {
            dbThrottleHandlerClickListener.setData(
                throttleClickListenerEventHandler(
                    infinityTextInputEditText, dbThrottleClickListener, dbThrottleTimeMilis, dbThrottleEventListener
                )
            );
        }

        infinityTextInputEditText.setCompoundDrawablesClickListener(new InfinityTextInputEditText.DrawableClickListenerAbs() {
            @Override
            public boolean onDrawableLeftClick() {
                TypeWrapper<Boolean> mHandled = TypeWrapper.getInstance(false);
                if (dlThrottleHandlerClickListener.getData() != null) {
                    dlThrottleHandlerClickListener.getData().onClick(new BindingReturnListener<Boolean>() {
                        @Override
                        public void onReturn(Boolean handled) {
                            mHandled.setData(handled);
                        }
                    });
                }
//                Log.d(TAG, "dlHandled: " + mHandled.getData());
                return mHandled.getData();
            }

            @Override
            public boolean onDrawableTopClick() {
                TypeWrapper<Boolean> mHandled = TypeWrapper.getInstance(false);
                if (dtThrottleHandlerClickListener.getData() != null) {
                    dtThrottleHandlerClickListener.getData().onClick(new BindingReturnListener<Boolean>() {
                        @Override
                        public void onReturn(Boolean handled) {
                            mHandled.setData(handled);
                        }
                    });
                }
                return mHandled.getData();
            }

            @Override
            public boolean onDrawableRightClick() {
                TypeWrapper<Boolean> mHandled = TypeWrapper.getInstance(false);
                if (drThrottleHandlerClickListener.getData() != null) {
                    drThrottleHandlerClickListener.getData().onClick(new BindingReturnListener<Boolean>() {
                        @Override
                        public void onReturn(Boolean handled) {
                            mHandled.setData(handled);
                        }
                    });
                }
                return mHandled.getData();
            }

            @Override
            public boolean onDrawableBottomClick() {
                TypeWrapper<Boolean> mHandled = TypeWrapper.getInstance(false);
                if (dbThrottleHandlerClickListener.getData() != null) {
                    dbThrottleHandlerClickListener.getData().onClick(new BindingReturnListener<Boolean>() {
                        @Override
                        public void onReturn(Boolean handled) {
                            mHandled.setData(handled);
                        }
                    });
                }
                return mHandled.getData();
            }
        });
    }

    @BindingAdapter(value = {"drawableLeft", "drawableTop", "drawableRight", "drawableBottom"}, requireAll = false)
    public static void textInputEditTextSetDrawables(TextInputEditText view, @Nullable Integer drawableLeft, @Nullable Integer drawableTop, @Nullable Integer drawableRight, @Nullable Integer drawableBottom) {
        view.setCompoundDrawablesWithIntrinsicBounds(
            drawableLeft != null ? AppCompatResources.getDrawable(view.getContext(), drawableLeft) : null,
            drawableTop != null ? AppCompatResources.getDrawable(view.getContext(), drawableTop) : null,
            drawableRight != null ? AppCompatResources.getDrawable(view.getContext(), drawableRight) : null,
            drawableBottom != null ? AppCompatResources.getDrawable(view.getContext(), drawableBottom) : null
        );
    }

    @BindingAdapter(value = {"unmaskedText"})
    public static void customTextInputEditTextSetUnmaskedText(
        InfinityTextInputEditText infinityTextInputEditText,
        String unmaskedText
    ) {
        if (
            (unmaskedText == null && infinityTextInputEditText.getUnmaskedText() == null) ||
            (unmaskedText != null && infinityTextInputEditText.getUnmaskedText() != null && unmaskedText.equals(infinityTextInputEditText.getUnmaskedText()))
        ) {
            return;
        }

        infinityTextInputEditText.setText(unmaskedText);
        infinityTextInputEditText.setSelection(infinityTextInputEditText.getText() != null ? infinityTextInputEditText.getText().length() : 0);
    }

    @BindingAdapter(value = {"unmaskedTextAttrChanged"})
    public static void customTextInputEditTextSetUnmaskedTextAttrChanged(
        InfinityTextInputEditText infinityTextInputEditText,
        InverseBindingListener inverseBindingListener
    ) {
        infinityTextInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (inverseBindingListener != null) {
                    inverseBindingListener.onChange();
                }
            }
        });
    }

    @InverseBindingAdapter(attribute = "unmaskedText", event = "unmaskedTextAttrChanged")
    public static String customTextInputEditTextSetUnmaskedTextInv(InfinityTextInputEditText infinityTextInputEditText) {
        return infinityTextInputEditText.getUnmaskedText();
    }


    @BindingAdapter(value = {"bigdecPriceText"})
    public static void customTextInputEditTextSetBigdecPriceText(
        InfinityTextInputEditText infinityTextInputEditText,
        BigDecimal bigDecimal
    ) {
        if (
            (bigDecimal == null && infinityTextInputEditText.getMonetaryMaskBigDec() == null) ||
            (bigDecimal != null && infinityTextInputEditText.getMonetaryMaskBigDec() != null && bigDecimal.compareTo(infinityTextInputEditText.getMonetaryMaskBigDec()) == 0)
        ) {
            return;
        }

        infinityTextInputEditText.setText(bigDecimal != null ? MoneyUtils.formatarDouble(bigDecimal.doubleValue(), 2, 2) : "0");
    }

    @BindingAdapter(value = {"bigdecPriceTextAttrChanged"})
    public static void customTextInputEditTextSetBigdecPriceTextAttrChanged(
        InfinityTextInputEditText infinityTextInputEditText,
        InverseBindingListener inverseBindingListener
    ) {
        infinityTextInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                inverseBindingListener.onChange();
            }
        });
    }

    @InverseBindingAdapter(attribute = "bigdecPriceText", event = "bigdecPriceTextAttrChanged")
    public static BigDecimal customTextInputEditTextSetBigdecPriceTextInv(
        InfinityTextInputEditText infinityTextInputEditText
    ) {
        return infinityTextInputEditText.getMonetaryMaskBigDec();
    }

    @BindingAdapter(value = {"selectedItemPosition"})
    public static void spinnerSelectedItemPosition(
        AppCompatSpinner pAppCompatSpinner,
        int newPosition
    ) {
        if (
            pAppCompatSpinner.getAdapter() == null ||
            pAppCompatSpinner.getAdapter().getCount() == 0 ||
            pAppCompatSpinner.getSelectedItemPosition() != newPosition
        ) {
            return;
        }
        pAppCompatSpinner.setSelection(newPosition, true);
    }

    @BindingAdapter(value = {"selectedItemPositionAttrChanged"}, requireAll = false)
    public static void spinnerSelectedItemPosition(AppCompatSpinner pAppCompatSpinner, final InverseBindingListener newTextAttrChanged) {
        pAppCompatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                newTextAttrChanged.onChange();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @InverseBindingAdapter(attribute = "selectedItemPosition", event = "selectedItemPositionAttrChanged")
    public static int spinnerSelectedItemPositionInv(AppCompatSpinner pAppCompatSpinner) {
        return pAppCompatSpinner.getSelectedItemPosition();
    }

    @BindingAdapter(value = {"adapter", "tabChangeDelay", "tabSelectedListener", "tabTouchListener"})
    public static <VH extends TabLayoutAdapter.ViewHolder> void tabLayoutAdapter(
        CustomTabLayout tabLayout,
        TabLayoutAdapter<VH> tabLayoutAdapter,
        Long tabChangeDelay,
        TabLayoutListener onTabSelectedListener,
        TabTouchListener tabTouchListener
    ) {
        tabLayout.setAdapter(tabLayoutAdapter);

        TypeWrapper<Long> mTabChangeDelay = TypeWrapper.getInstance(tabChangeDelay);
        if (mTabChangeDelay.getData() == null) {
            mTabChangeDelay.setData(400L);
        }

        if (tabLayoutAdapter != null) {

            if (tabLayoutAdapter.getViewHolderMap() != null) {
                tabLayoutAdapter.getViewHolderMap().clear();
            }

            tabLayoutAdapter.setInternalTabLayoutAdapterListener(new TabLayoutAdapter.InternalTabLayoutAdapterListener() {
                @Override
                public void onNotifyDataSetChanged() {
                    int selectedTabPos = tabLayout.getSelectedTabPosition();
                    if (selectedTabPos == -1) {
                        selectedTabPos = 0;
                    }

                    int itemCount = tabLayoutAdapter.getItemCount();
                    boolean shouldClearTabs = tabLayout.getTabCount() != itemCount;

                    if (shouldClearTabs) {
                        tabLayout.removeAllTabs();
                    }

                    for (int i = 0; i < itemCount; i++) {
                        TabLayout.Tab tab = tabLayout.getTabAt(i);
                        boolean creatingNewTab = tab == null;
                        if (creatingNewTab) {
                            tab = tabLayout.newTab();
                        }

                        VH holder = tabLayoutAdapter.getViewHolderMap().get(i);
                        if (holder == null) {
                            holder = tabLayoutAdapter.onCreateViewHolder(tabLayout, 0);
                            tabLayoutAdapter.getViewHolderMap().put(i, holder);
                        }

                        tabLayoutAdapter.onBindViewHolder(holder, i, i == selectedTabPos);

                        tab.setCustomView(holder.getView());

                        if (creatingNewTab) {
                            tabLayout.addTab(tab);
                        }
                    }
                }
            });

            tabLayoutAdapter.notifyDataSetChanged();

            final TypeWrapper<Runnable> runnable = TypeWrapper.getInstance(null);

            tabLayout.setOnMyTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                int lastTabSelection = tabLayout.getSelectedTabPosition();

                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    // Wait unitil animation finishs animation duration is 300ms
                    if (runnable.getData() != null) {
                        tabLayout.removeCallbacks(runnable.getData());
                    }
                    runnable.setData(
                        new Runnable() {
                            @Override
                            public void run() {
                                int itemCount = tabLayoutAdapter.getItemCount();
                                if (lastTabSelection >= 0 && lastTabSelection < itemCount) {
                                    VH lastVh = tabLayoutAdapter.getViewHolderMap().get(lastTabSelection);
                                    if (lastVh != null) {
                                        tabLayoutAdapter.onBindViewHolder(tabLayoutAdapter.getViewHolderMap().get(lastTabSelection), lastTabSelection, false);
                                    }
                                }
                                VH curVh = tabLayoutAdapter.getViewHolderMap().get(tab.getPosition());
                                if (curVh != null) {
                                    tabLayoutAdapter.onBindViewHolder(curVh, tab.getPosition(), true);
                                }

                                lastTabSelection = tab.getPosition();
                                runnable.setData(null);
                            }
                        }
                    );
                    tabLayout.postDelayed(runnable.getData(), mTabChangeDelay.getData());

                    if (onTabSelectedListener != null) {
                        onTabSelectedListener.onTabEvent(TabInfo.getInstance(tab.getPosition()));
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

            if (tabTouchListener != null) {
                for (int i = 0; i < tabLayout.getTabCount(); i++) {
                    TabLayout.Tab tabAt = tabLayout.getTabAt(i);
                    View tabViewAt = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(i);
                    if (tabViewAt != null && tabAt != null) {
                        tabViewAt.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                if (tabTouchListener != null) {
                                    TypeWrapper<Boolean> clickHandled = TypeWrapper.getInstance(false);
                                    tabTouchListener.onTabEvent(TabInfo.getInstance(tabAt.getPosition()), new BindingReturnListener<Boolean>() {
                                        @Override
                                        public void onReturn(Boolean mClickHandled) {
                                            clickHandled.setData(mClickHandled != null ? mClickHandled : false);
                                        }
                                    });
                                    if (clickHandled.getData()) {
                                        event.setAction(MotionEvent.ACTION_CANCEL);
                                        RippleEffect.forceRippleAnimation(tabViewAt);
                                    }
                                    return clickHandled.getData();
                                }
                                return false;
                            }
                        });
                    }
                }
            }
        }
    }

    @BindingAdapter(value = {"adapter", "tabLayout", "offscreenPageLimit"}, requireAll = false)
    public static void setViewPagerAdapter(
        ViewPager2 viewPager2,
        ViewPagerFragmentStateAdapter adapter,
        @Nullable Object objTabLayout,
        @Nullable Integer offScreenPageLimit
    ) {
        try {
            boolean adapterIsTheSame = true;
            if ((viewPager2.getAdapter() == null || adapter == null) || viewPager2.getAdapter().getClass().hashCode() != adapter.getClass().hashCode()) {
                adapterIsTheSame = false;
            }
            if (!adapterIsTheSame) {
                viewPager2.setAdapter(adapter);
            }
            TypeWrapper<CustomTabLayout> tabLayout = TypeWrapper.getInstance(null);
            if (objTabLayout != null) {
                if (objTabLayout instanceof Integer) {
                    tabLayout.setData(ViewUtils.searchViewParentsForViewWithId(viewPager2, (Integer) objTabLayout, 0));
                } else if (objTabLayout instanceof CustomTabLayout) {
                    tabLayout.setData((CustomTabLayout) objTabLayout);
                }
            }
            if (adapter != null && tabLayout.getData() != null) {
                tabLayout.getData().addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        viewPager2.setCurrentItem(tab.getPosition(), false);
                        if (tab.getPosition() != 0) {
                            tabLayout.getData().removeOnTabSelectedListener(this);
                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });
                if (tabLayout.getData().getTabLayoutMediator() != null && tabLayout.getData().getTabLayoutMediator().isAttached()) {
                    tabLayout.getData().getTabLayoutMediator().detach();
                }
                TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout.getData(), viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setText(adapter.getFragmentItem(position).getTitle());
                    }
                });
                tabLayoutMediator.attach();
                tabLayout.getData().setTabLayoutMediator(tabLayoutMediator);
                if (tabLayout.getData().getAdapter() != null) {
                    tabLayout.getData().getAdapter().notifyDataSetChanged();
                }
            }
            if (offScreenPageLimit != null) {
                viewPager2.setOffscreenPageLimit(offScreenPageLimit);
            } else {
                viewPager2.setOffscreenPageLimit(ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Maybe some errors are thrown here supress then since the errors may not affect anything
        }
    }

    @BindingAdapter(value = {"checkedId"})
    public static void radioGroupCheckedId(
        @NonNull RadioGroup radioGroup,
        int checkedId
    ) {
        if (radioGroup.getCheckedRadioButtonId() == checkedId) {
            return;
        }
        radioGroup.check(checkedId);
    }

    @BindingAdapter(value = {"checkedIdAttrChanged"})
    public static void radioGroupCheckedIdAttrChanged(
        @NonNull RadioGroup radioGroup,
        InverseBindingListener inverseBindingListener
    ) {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                inverseBindingListener.onChange();
            }
        });
    }

    @InverseBindingAdapter(attribute = "checkedId", event = "checkedIdAttrChanged")
    public static int radioGroupCheckedId(
        @NonNull RadioGroup radioGroup
    ) {
        return radioGroup.getCheckedRadioButtonId();
    }

    @BindingAdapter(value = {"adapter"})
    public static void setFrameLayoutAdapter(
        FrameLayout frameLayout,
        FrameLayoutAdapter frameLayoutAdapter
    ) {

    }

    @BindingAdapter(value = {"scrollTo"})
    public static void setNestedScrollViewScrollPosition(
        NestedScrollView nestedScrollView,
        ScrollTo scrollTo
    ) {
        if (scrollTo != null) {
            int xScroll = nestedScrollView.getScrollX();
            int yScroll = nestedScrollView.getScrollY();

            if (scrollTo.getX() != null) {
                xScroll = scrollTo.getX();
            }
            if (scrollTo.getY() != null) {
                yScroll = scrollTo.getY();
            }

            if (scrollTo.getSide() != null && scrollTo.getSide() == ScrollTo.SIDE_BOTTOM) {
                yScroll = nestedScrollView.getMaxScrollAmount();
            }

            if (scrollTo.getSmoothScroll() != null && scrollTo.getSmoothScroll()) {
                nestedScrollView.smoothScrollTo(xScroll, yScroll);
            } else {
                nestedScrollView.scrollTo(xScroll, yScroll);
            }
        }
    }

//    @InverseBindingAdapter(attribute = "dateValue")
//    public static String customTextInputLayPickerStrDateValueInverse(
//        InfinityTextInputLayPicker customTextInputLayPicker
//    ) {
//        return customTextInputLayPicker.getStrDate();
//    }

//    public static void setTabLayoutAdapter() {
//
//    }

//    @InverseBindingAdapter(attribute = "jsEnabled")
//    public static boolean setWebViewJsEnabled(WebView webView) {
//        webView.getSettings().setJavaScriptEnabled(jsEnabled);
//    }

    public interface DefaultClickListener {
        void onClick();
    }

    public interface ClickCountListener {
        void onClick(int count);
    }

    public interface DefaultClickListenerWithReturnBool {
        void onClick(@Nullable BindingReturnListener<Boolean> bReturn);
    }

    public interface ThrottleEventListener {
        void throttleEventListener(long elapsedTime, long totalWaitTime, long remainingTime, @NonNull String message);
    }

    public interface NavigationItemSelectedListener {
        void onNavigationItemSelected(MenuItemInfo menuItemInfo, BindingReturnListener<Boolean> returnListener);
    }

    public interface BindingReturnListener<T> {
        void onReturn(T t);
    }

    public interface WebViewClientListener {
        void onSetWebViewClientListener(BindingReturnListener<WebViewClientBindingImpl> listener);
    }

    public interface TextInputEditTextFocusChangeListener {
        void onFocusChange(boolean hasFocus);
    }

    public interface TextInputEditTextTextChangeListener {
        void onTextChange(String newText);
    }

    public interface EditTextEditorActionListener {
        void onEditorAction(int actionId, KeyEvent event, BindingReturnListener<Boolean> bReturnListener);
    }

    public interface FocusChangeListener {
        void onFocusChange(boolean hasFocus);
    }

    public interface OnDestinationChangedListener {
        void onDestinationChanged(@NonNull NavDestination destination, @Nullable Bundle arguments);
    }

    public interface OnRvLayoutComputedListener {
        void onLayoutComputed(RvLayoutComputedInfo rvLayoutComputedInfo);
    }

    public interface TabLayoutListener {
        void onTabEvent(@NonNull TabInfo tabInfo);
    }

    public interface TabTouchListener {
        void onTabEvent(@NonNull TabInfo tabInfo, BindingReturnListener<Boolean> touchHandledListener);
    }

    public interface RecyclerViewScrollListener {
        void onScroll(@NonNull RvScrollEventInfo rvScrollEventInfo);
    }

    public interface CompoundDrawableClickListener {
        void onClick(@NonNull CompoundDrawableClickInfo compoundDrawableClickInfo);
    }
}
