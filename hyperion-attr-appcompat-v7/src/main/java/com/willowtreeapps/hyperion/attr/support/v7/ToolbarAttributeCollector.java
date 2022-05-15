package com.willowtreeapps.hyperion.attr.support.v7;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.attr.ViewAttribute;
import com.willowtreeapps.hyperion.attr.collectors.TypedAttributeCollector;
import com.willowtreeapps.hyperion.plugin.v1.AttributeTranslator;

import java.util.ArrayList;
import java.util.List;

@AutoService(TypedAttributeCollector.class)
public class ToolbarAttributeCollector extends TypedAttributeCollector<Toolbar> {

    public ToolbarAttributeCollector() {
        super(Toolbar.class);
    }

    @NonNull
    @Override
    public List<ViewAttribute> collect(Toolbar view, AttributeTranslator attributeTranslator,
                                       @Nullable Object... objs) {
        List<ViewAttribute> attributes = new ArrayList<>();
        // TODO fill
        return attributes;
    }
}