package com.infinity.architecture.base.ui.spinner;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;

import androidx.annotation.LayoutRes;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.infinity.architecture.base.R;
import com.infinity.architecture.base.databinding.LaySimpleSpinnerBinding;
import com.infinity.architecture.base.databinding.LaySimpleSpinnerDropdownBinding;
import com.infinity.architecture.base.models.adapter.BaseSpinnerItem;
import com.infinity.architecture.base.models.adapter.SimpleSpinnerItem;
import com.infinity.architecture.base.ui.adapter.AdapterRequires;
import com.infinity.architecture.utils.reflection.ReflectionUtils;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class BaseSpinnerAdapter<
    BV extends ViewDataBinding,
    BVD extends ViewDataBinding,
    VM extends BaseSpinnerViewModel<BV, I>,
    VMD extends BaseSpinnerViewModel<BVD, I>,
    I extends BaseSpinnerItem
> extends BaseAdapter implements SpinnerAdapter {
    private final String TAG = "BaseAdapter";

    private ArrayList<I> itemList;
    private int vLayoutToInflate;
    private int dLayoutToInflate;
    private Class<BV> vBindingClass;
    private Class<BVD> vdBindingClass;
    private Class<VM> vModelClass;
    private Class<VMD> vdModelClass;
    private AdapterRequires adapterRequires;

    private Method bvSetVmMethod;
    private Method bvdSetVmMethod;
    private Method bvGetVmMethod;
    private Method bvdGetVmMethod;

    public BaseSpinnerAdapter(
        ArrayList<I> itemList,
        @LayoutRes int vLayoutToInflate,
        @LayoutRes int dLayoutToInflate,
        Class<BV> vBindingClass,
        Class<BVD> vdBindingClass,
        Class<VM> vModelClass,
        Class<VMD> vdModelClass,
        AdapterRequires adapterRequires
    ) throws Exception {
        this.itemList = itemList;
        this.vLayoutToInflate = vLayoutToInflate;
        this.dLayoutToInflate = dLayoutToInflate;
        this.vBindingClass = vBindingClass;
        this.vdBindingClass = vdBindingClass;
        this.vModelClass = vModelClass;
        this.vdModelClass = vdModelClass;
        this.adapterRequires = adapterRequires;
        this.bvSetVmMethod = vBindingClass.getDeclaredMethod("setViewModel", vModelClass);
        this.bvdSetVmMethod = vdBindingClass.getDeclaredMethod("setViewModel", vdModelClass);
        this.bvGetVmMethod = vBindingClass.getDeclaredMethod("getViewModel");
        this.bvdGetVmMethod = vdBindingClass.getDeclaredMethod("getViewModel");

        Log.d(TAG, "itemList:" + itemList);
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public I getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @SuppressWarnings("unchecked")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            BV bindingInstance = null;
//            if (convertView == null) {
                bindingInstance = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), vLayoutToInflate, parent, false);
                convertView = bindingInstance.getRoot();

                VM vmInstance = (VM) adapterRequires.getViewModelInst(adapterRequires.getUniqueVmOwnerGuid(), vModelClass);
                bvSetVmMethod.invoke(bindingInstance, vmInstance);

                convertView.setTag(bindingInstance);
//            } else {
//                bindingInstance = (BV) convertView.getTag();
//            }
//
//            VM vmInstance = (VM) bvGetVmMethod.invoke(bindingInstance);

            vmInstance.onBindView(bindingInstance, itemList, getItem(position), position);

            return convertView;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        super.getDropDownView(position, convertView, parent);
        try {
            BVD bindingInstance = null;
//            if (convertView == null) {
                bindingInstance = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), dLayoutToInflate, parent, false);
                convertView = bindingInstance.getRoot();

                VMD vmInstance = (VMD) adapterRequires.getViewModelInst(adapterRequires.getUniqueVmOwnerGuid(), vdModelClass);
                bvdSetVmMethod.invoke(bindingInstance, vmInstance);

                convertView.setTag(bindingInstance);
//            } else {
//                bindingInstance = (BVD) convertView.getTag();
//
////                VMD vmInstance = (VMD) bvdGetVmMethod.invoke(bindingInstance);
////
////                vmInstance.onBindView(bindingInstance, itemList, getItem(position), position);
//            }
//
//            VMD vmInstance = (VMD) bvdGetVmMethod.invoke(bindingInstance);

            vmInstance.onBindView(bindingInstance, itemList, getItem(position), position);

            return convertView;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <I extends SimpleSpinnerItem> BaseSpinnerAdapter<LaySimpleSpinnerBinding, LaySimpleSpinnerDropdownBinding, SimpleSpinnerViewVM<I>, SimpleSpinnerDropdownVM<I>, I> getSimpleSpinnerAdapter(AdapterRequires adapterRequires, ArrayList<I> itemList) throws Exception {
        Type[] genericTypes = ReflectionUtils.getGenericClassTypes(itemList.getClass().getGenericSuperclass());
        Type arrayGenericType = genericTypes[0];

        Class<SimpleSpinnerViewVM<I>> vmClass = (Class<SimpleSpinnerViewVM<I>>) TypeToken.getParameterized(SimpleSpinnerViewVM.class, arrayGenericType).getRawType();
        Class<SimpleSpinnerDropdownVM<I>> vmdClass = (Class<SimpleSpinnerDropdownVM<I>>) TypeToken.getParameterized(SimpleSpinnerDropdownVM.class, arrayGenericType).getRawType();

        return new BaseSpinnerAdapter<>(itemList, R.layout.lay_simple_spinner, R.layout.lay_simple_spinner_dropdown, LaySimpleSpinnerBinding.class, LaySimpleSpinnerDropdownBinding.class, vmClass, vmdClass, adapterRequires);
    }

//    public static <BV extends ViewDataBinding, VM extends BaseSpinnerViewModel<BV, I>, I extends BaseSpinnerItem> Class<BaseSpinnerViewModel<BV, I>> getBaseViewHolderClass() {
//        return (Class<BaseRecyclerAdapterVH<BV, VM, I>>) new TypeToken<BaseRecyclerAdapterVH<B, VM, I>>(){}.getRawType();
//    }
}
