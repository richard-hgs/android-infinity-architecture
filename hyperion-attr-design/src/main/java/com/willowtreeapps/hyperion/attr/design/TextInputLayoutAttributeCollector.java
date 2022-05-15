package com.willowtreeapps.hyperion.attr.design;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.attr.ViewAttribute;
import com.willowtreeapps.hyperion.attr.collectors.TypedAttributeCollector;
import com.willowtreeapps.hyperion.plugin.v1.AttributeTranslator;

import java.util.ArrayList;
import java.util.List;

@AutoService(TypedAttributeCollector.class)
public class TextInputLayoutAttributeCollector extends TypedAttributeCollector<TextInputLayout> {

    public TextInputLayoutAttributeCollector() {
        super(TextInputLayout.class);
    }

    @NonNull
    @Override
    public List<ViewAttribute> collect(TextInputLayout view, AttributeTranslator attributeTranslator,
                                       @Nullable Object... objs) {
        List<ViewAttribute> attributes = new ArrayList<>();
        attributes.add(new ViewAttribute<>("PasswordToggleDrawable",
                view.getPasswordVisibilityToggleDrawable()));
        attributes.add(new ViewAttribute<>("PasswordToggleContentDisc",
                nonNull(view.getPasswordVisibilityToggleContentDescription())));
        attributes.add(new ViewAttribute<>("CounterMaxLength", view.getCounterMaxLength()));
        attributes.add(new ViewAttribute<>("Hint", nonNull(view.getHint())));
        attributes.add(new ViewAttribute<>("CounterEnabled", view.isCounterEnabled()));
        attributes.add(new ViewAttribute<>("ErrorEnabled", view.isErrorEnabled()));
        attributes.add(new ViewAttribute<>("HintAnimationEnabled", view.isHintAnimationEnabled()));
        attributes.add(new ViewAttribute<>("HintEnabled", view.isHintEnabled()));
        attributes.add(new ViewAttribute<>("PasswordToggleEnabled",
                view.isPasswordVisibilityToggleEnabled()));
        return attributes;
    }
}