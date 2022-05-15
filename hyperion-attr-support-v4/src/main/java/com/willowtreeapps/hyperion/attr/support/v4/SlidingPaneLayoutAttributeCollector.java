package com.willowtreeapps.hyperion.attr.support.v4;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.slidingpanelayout.widget.SlidingPaneLayout;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.attr.ViewAttribute;
import com.willowtreeapps.hyperion.attr.collectors.TypedAttributeCollector;
import com.willowtreeapps.hyperion.plugin.v1.AttributeTranslator;

import java.util.ArrayList;
import java.util.List;
// androidx.slidingpanelayout:slidingpanelayout:1.0.0
@AutoService(TypedAttributeCollector.class)
public class SlidingPaneLayoutAttributeCollector extends TypedAttributeCollector<SlidingPaneLayout> {

    public SlidingPaneLayoutAttributeCollector() {
        super(SlidingPaneLayout.class);
    }

    @NonNull
    @Override
    public List<ViewAttribute> collect(SlidingPaneLayout view, AttributeTranslator attributeTranslator,
                                       @Nullable Object... objs) {
        List<ViewAttribute> attributes = new ArrayList<>();
        // TODO fill
        return attributes;
    }
}