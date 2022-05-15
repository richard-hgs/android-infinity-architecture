package com.willowtreeapps.hyperion.attr.support.v4;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.attr.ViewAttribute;
import com.willowtreeapps.hyperion.attr.collectors.TypedAttributeCollector;
import com.willowtreeapps.hyperion.plugin.v1.AttributeTranslator;

import java.util.ArrayList;
import java.util.List;

@AutoService(TypedAttributeCollector.class)
public class SwipeRefreshLayoutAttributeCollector extends TypedAttributeCollector<SwipeRefreshLayout> {

    public SwipeRefreshLayoutAttributeCollector() {
        super(SwipeRefreshLayout.class);
    }

    @NonNull
    @Override
    public List<ViewAttribute> collect(SwipeRefreshLayout view, AttributeTranslator attributeTranslator,
                                       @Nullable Object... objs) {
        List<ViewAttribute> attributes = new ArrayList<>();
        // TODO fill
        return attributes;
    }
}