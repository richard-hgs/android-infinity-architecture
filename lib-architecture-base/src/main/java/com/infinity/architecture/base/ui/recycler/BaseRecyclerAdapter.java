package com.infinity.architecture.base.ui.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.infinity.architecture.base.ui.adapter.AdapterRequires;
import com.infinity.architecture.utils.reflection.ReflectionUtils;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class BaseRecyclerAdapter<
    H extends BaseRecyclerAdapterVH<B, VM, I>,
    B extends ViewDataBinding,
    VM extends BaseRecyclerViewModel<B, I>,
    I
> extends RecyclerView.Adapter<H> {
    private final String TAG = "BaseRecyclerAdapter";

    private AdapterRequires adapterRequires;
    private ArrayList<I> itemList;
    @LayoutRes
    private int layoutToInflate;
    private Class<H> holderClass;
    private Class<B> bindingClass;
    private Class<VM> viewModelClass;

    private Constructor<?> holderCtor = null;
    // private Constructor<?> viewModelCtor = null;
    private Method bindingSetVmMethod;

    private Integer viewIdToBindClick = null;
    private boolean isClickBindEnabled = false;

    private Object[] params = new Object[]{};

    @Nullable
    private BaseAdapterListener<I> baseAdapterListener;

    public BaseRecyclerAdapter(
        AdapterRequires adapterRequires,
        @NonNull ArrayList<I> itemList,
        @LayoutRes int layoutToInflate,
        Class<H> holderClass,
        Class<B> bindingClass,
        Class<VM> viewModelClass,
        @Nullable BaseAdapterListenerImpl<I> baseAdapterListener,
        Object ...params
    ) throws Exception {
        this(adapterRequires, itemList, layoutToInflate, null, true, holderClass, bindingClass, viewModelClass, baseAdapterListener, params);
    }

    public BaseRecyclerAdapter(
        AdapterRequires adapterRequires,
        @NonNull ArrayList<I> itemList,
        @LayoutRes int layoutToInflate,
        @Nullable Integer viewIdToBindClick,
        boolean isClickBindEnabled,
        Class<H> holderClass,
        Class<B> bindingClass,
        Class<VM> viewModelClass,
        @Nullable BaseAdapterListenerImpl<I> baseAdapterListener,
        Object ...params
    ) throws Exception {
        this.adapterRequires = adapterRequires;
        this.itemList = itemList;
        this.layoutToInflate = layoutToInflate;

        this.holderClass = holderClass;
        this.bindingClass = bindingClass;
        this.viewModelClass = viewModelClass;

        this.baseAdapterListener = baseAdapterListener;
        this.viewIdToBindClick = viewIdToBindClick;

        this.bindingSetVmMethod = bindingClass.getDeclaredMethod("setViewModel", viewModelClass);
        if (params != null) {
            this.params = params;
        }

        Constructor<?>[] constructors = holderClass.getDeclaredConstructors();
        for (Constructor<?> constructorAt : constructors) {
            // Log.d(TAG, "constructorAt: ");
            boolean param1Match = false;
            boolean param2Match = false;
            int paramIndex = 0;
            Class<?>[] ctorParamTypes = constructorAt.getParameterTypes();
            for (Class<?> paramAt : ctorParamTypes) {
                // Log.d(TAG, "constructorAt - paramAt: " + paramAt.getSimpleName() + " - bindingClass: " + bindingClass.getSimpleName() + "("+ ReflectionUtils.isInstanceOf(paramAt, bindingClass)+") - vmClass: " + viewModelClass.getSimpleName() + "("+ ReflectionUtils.isInstanceOf(paramAt, viewModelClass)+")");
                if (paramIndex == 0 && ReflectionUtils.isInstanceOf(paramAt, bindingClass)) {
                    param1Match = true;
                } else if (paramIndex == 1 && ReflectionUtils.isInstanceOf(paramAt, viewModelClass)) {
                    param2Match = true;
                }

                if (param1Match && param2Match) {
                    this.holderCtor = constructorAt;
                    break;
                }
                paramIndex++;
            }
        }

//        Constructor<?>[] constructors2 = viewModelClass.getDeclaredConstructors();
//        for (Constructor<?> constructorAt : constructors2) {
//            // Log.d(TAG, "constructorAt: ");
//            boolean param1Match = false;
//            int paramIndex = 0;
//            Class<?>[] ctorParamTypes = constructorAt.getParameterTypes();
//            for (Class<?> paramAt : ctorParamTypes) {
//                if (paramIndex == 0 && ReflectionUtils.isInstanceOf(paramAt, Application.class)) {
//                    param1Match = true;
//                }
//                if (param1Match) {
//                    this.viewModelCtor = constructorAt;
//                    break;
//                }
//                paramIndex++;
//            }
//        }
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public H onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        try {
            // Constructor<?> holderCtor = holderClass.getDeclaredConstructor(bindingClass, viewModelClass);
            B bindingInstance = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), layoutToInflate, parent, false);
            VM vmInstance = (VM) adapterRequires.getViewModelInst(adapterRequires.getUniqueVmOwnerGuid(), viewModelClass);
            vmInstance.setBaseActivityViewModel(adapterRequires.getBaseActivityViewModel());
            vmInstance.setCompositeDisposable(adapterRequires.getCompositeDisposable());
            vmInstance.setItemList(itemList);
            vmInstance.setParams(params);

            // Log.d(TAG, "vmInstance: " + vmInstance);
            // VM vmInstance = (VM) viewModelClass.getConstructor().newInstance();
            bindingSetVmMethod.invoke(bindingInstance, vmInstance);

            H holderInstance = (H) holderCtor.newInstance(bindingInstance, vmInstance);

            return holderInstance;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull H holder, int position) {
        I itemAt = itemList.get(position);
        holder.getViewModel().setPosition(position);
        holder.getViewModel().setBaseAdapterListener(baseAdapterListener);
        holder.onBindViewHolder(itemList, itemAt, position);

        if (baseAdapterListener != null && isClickBindEnabled) {
            if (viewIdToBindClick == null) {
                holder.getViewDataBinding().getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        notifyClick(holder, v.getId(), itemAt);
                    }
                });

                holder.getViewDataBinding().getRoot().setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return notifyLongClick(holder, v.getId(), itemAt);
                    }
                });
            } else {
                holder.itemView.findViewById(viewIdToBindClick).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        notifyClick(holder, v.getId(), itemAt);
                    }
                });

                holder.itemView.findViewById(viewIdToBindClick).setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return notifyLongClick(holder, v.getId(), itemAt);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    private void notifyClick(@NonNull H holder, int actionId, I itemAt) {
        holder.getViewModel().onItemClick(actionId, itemAt, holder.getAdapterPosition());

        if (baseAdapterListener != null) {
            baseAdapterListener.onItemClick(actionId, itemAt, holder.getAdapterPosition());
        }
    }

    private boolean notifyLongClick(@NonNull H holder, int actionId, I itemAt) {
        holder.getViewModel().onItemLongClick(actionId, itemAt, holder.getAdapterPosition());

        if (baseAdapterListener != null) {
            return baseAdapterListener.onItemLongClick(actionId, itemAt, holder.getAdapterPosition());
        }

        return false;
    }

    public void setItemList(@NonNull ArrayList<I> itemList) {
        this.itemList.clear();
        this.itemList.addAll(itemList);
    }

    @SuppressWarnings("unchecked")
    public static <B extends ViewDataBinding, VM extends BaseRecyclerViewModel<B, I>, I> Class<BaseRecyclerAdapterVH<B, VM, I>> getBaseViewHolderClass() {
        return (Class<BaseRecyclerAdapterVH<B, VM, I>>) new TypeToken<BaseRecyclerAdapterVH<B, VM, I>>(){}.getRawType();
    }
}
