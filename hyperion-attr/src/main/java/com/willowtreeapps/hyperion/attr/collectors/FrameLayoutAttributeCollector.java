package com.willowtreeapps.hyperion.attr.collectors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.FrameLayout;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.attr.MutableBooleanViewAttribute;
import com.willowtreeapps.hyperion.attr.ViewAttribute;
import com.willowtreeapps.hyperion.plugin.v1.AttributeTranslator;

import java.util.ArrayList;
import java.util.List;

@AutoService(TypedAttributeCollector.class)
public class FrameLayoutAttributeCollector extends TypedAttributeCollector<FrameLayout> {

    public FrameLayoutAttributeCollector() {
        super(FrameLayout.class);
    }

    @NonNull
    @Override
    public List<ViewAttribute> collect(final FrameLayout view, AttributeTranslator attributeTranslator,
                                       @Nullable Object... objs) {
        List<ViewAttribute> attributes = new ArrayList<>();
        attributes.add(new MutableBooleanViewAttribute("MeasureAllChildren", view.getMeasureAllChildren()) {
            @Override
            protected void mutate(Boolean value) throws Exception {
                view.setMeasureAllChildren(value);
            }
        });
        return attributes;
    }
}