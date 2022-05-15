package com.willowtreeapps.hyperion.core.internal;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.motion.utils.ViewState;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.willowtreeapps.hyperion.core.BuildConfig;
import com.willowtreeapps.hyperion.core.R;
import com.willowtreeapps.hyperion.plugin.v1.HyperionMenu;
import com.willowtreeapps.hyperion.plugin.v1.MenuState;
import com.willowtreeapps.hyperion.plugin.v1.PluginModule;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;

public class HyperionPluginView extends FrameLayout {

    private final LinearLayout pluginListContainer;
    private final PluginExtensionImpl pluginExtension;

    @Inject
    PluginRepository pluginRepository;

    private Set<PluginModule> modules;
    private MenuState menuState = MenuState.CLOSE;

    public HyperionPluginView(@NonNull Context context) {
        this(context, null);
    }

    public HyperionPluginView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HyperionPluginView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        CoreComponent component = ComponentProvider.get(context);
        component.inject(this);

        inflate(context, R.layout.hype_view_plugin, this);
        final TextView versionText = findViewById(R.id.version_text);
        versionText.setText(context.getString(R.string.hype_version_text, BuildConfig.versionName));

        pluginListContainer = findViewById(R.id.plugin_list_container);
        pluginExtension = new PluginExtensionImpl(component);
        setFitsSystemWindows(true);
        setId(R.id.hyperion_plugins);
        ViewCompat.setImportantForAccessibility(
                this, ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_NO);

        setOnApplyInsetsListener(this, new OnApplyInsetsListener() {
            @Override
            public void onApplyInsets(View view, WindowInsetsCompat insets, ViewState initialState) {
                Insets mInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                int bottom = mInsets.bottom;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    int softKeysHeight = getSoftKeysHeight(((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)));
                    if (softKeysHeight > 0) {
                        // Fix hyperion menu itens below soft keys bar since fitSystemWindows don't work in FrameLayout
                        bottom = softKeysHeight;
                    }
                }

                setPadding(
                    mInsets.left,
                    mInsets.top,
                    mInsets.right,
                    bottom
                );
            }
        });
    }

    public static int getSoftKeysHeight(@NonNull WindowManager windowManager) {
        Display d = windowManager.getDefaultDisplay();

        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        d.getRealMetrics(realDisplayMetrics);

        int realHeight = realDisplayMetrics.heightPixels;
        int realWidth = realDisplayMetrics.widthPixels;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        d.getMetrics(displayMetrics);

        int displayHeight = displayMetrics.heightPixels;
        int displayWidth = displayMetrics.widthPixels;

        return (realWidth - displayWidth) > 0 ? (realWidth - displayWidth) : Math.max((realHeight - displayHeight), 0);
    }

    /**
     * https://github.com/chrisbanes/insetter
     * A wrapper around [ViewCompat.setOnApplyWindowInsetsListener] which stores the initial view state, and provides them whenever a
     * [android.view.WindowInsets] instance is dispatched to the listener provided.
     *
     *
     * This allows the listener to be able to append inset values to any existing view state
     * properties, rather than overwriting them.
     */
    void setOnApplyInsetsListener(@NonNull View view, OnApplyInsetsListener listener) {
        ViewState tagState = (ViewState) view.getTag(R.id.hyperion_insetter_initial_state);
        ViewState initialState;

        if (tagState != null) {
            initialState = tagState;
        } else {
            initialState = new ViewState();
            initialState.getState(view);
            view.setTag(R.id.hyperion_insetter_initial_state, initialState);
        }

        if (ViewCompat.isAttachedToWindow(view)) {
            registerForWindowInsets(view, getMostParentView(view), initialState, listener);
        } else {
            view.addOnAttachStateChangeListener(new OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View v) {
                    v.removeOnAttachStateChangeListener(this);
                    registerForWindowInsets(v, getMostParentView(v), initialState, listener);
                }

                @Override
                public void onViewDetachedFromWindow(View v) {

                }
            });
        }
    }

    /**
     * Register for window insets.
     * Commits result through wrapper.
     */
    private void registerForWindowInsets(
        View view,
        View mostParent,
        ViewState initialState,
        OnApplyInsetsListener listener
    ) {
        ViewCompat.setOnApplyWindowInsetsListener(this, new androidx.core.view.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                listener.onApplyInsets(view, insets, initialState);
                // Always return the initial insets instance
                return insets;
            }
        });

        ViewCompat.requestApplyInsets(mostParent);
    }

    /**
     * Returns the most parent view in its hierarchy
     */
    @NonNull
    private View getMostParentView(@NonNull View view) {
        ViewParent viewParent = view.getParent();
        if (viewParent != null && viewParent instanceof View && ((View) viewParent).getId() != android.R.id.content) {
            return getMostParentView(((View) viewParent));
        } else {
            return view;
        }
    }

    void setMenuState(MenuState menuState) {
        this.menuState = menuState;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // block touches while the menu is closed.
        return menuState == MenuState.CLOSE || super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        pluginExtension.setHyperionMenu((HyperionMenu) getParent());
        populatePluginList(pluginRepository.getPlugins());
    }

    private void populatePluginList(Plugins plugins) {
        final Comparator<PluginModule> comparator = new AlphabeticalComparator(getContext());
        final Set<PluginModule> sortedModules = new TreeSet<>(comparator);
        sortedModules.addAll(plugins.createModules());
        this.modules = sortedModules;

        final Context context = new PluginExtensionContextWrapper(
                getContext(), pluginExtension);
        final LayoutInflater inflater = LayoutInflater.from(context);
        for (PluginModule module : modules) {
            module.create(pluginExtension, context);
            View view = module.createPluginView(inflater, pluginListContainer);
            pluginListContainer.addView(view);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (modules != null) {
            for (PluginModule module : modules) {
                module.destroy();
            }
        }
        pluginExtension.setHyperionMenu(null);
    }

    private static final class AlphabeticalComparator implements Comparator<PluginModule> {

        private final Context context;

        private AlphabeticalComparator(Context context) {
            this.context = context;
        }

        @Override
        public int compare(PluginModule left, PluginModule right) {
            String leftName = getName(left);
            String rightName = getName(right);
            return leftName.compareTo(rightName);
        }

        private String getName(PluginModule pluginModule) {
            int resName = pluginModule.getName();
            if (resName == R.string.hype_module_name) return pluginModule.getClass().getSimpleName();
            else return context.getString(resName);
        }
    }

    public interface OnApplyInsetsListener {
        /**
         * When [set][Insetter.Builder.setOnApplyInsetsListener] on a View,
         * this listener method will be called instead of the view's own `onApplyWindowInsets`
         * method.
         *
         * @param view The view applying window insets
         * @param insets The insets to apply
         * @param initialState A snapshot of the view's padding/margin state when this listener was set.
         */
        void onApplyInsets(
            View view,
            WindowInsetsCompat insets,
            ViewState initialState
        );
    }
}