package com.willowtreeapps.hyperion.attr;

import android.util.Log;
import android.view.View;

import com.willowtreeapps.hyperion.attr.collectors.TypedAttributeCollector;
import com.willowtreeapps.hyperion.plugin.v1.AttributeTranslator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

class AttributeLoader {

    private final AttributeTranslator attributeTranslator;
    private final List<TypedAttributeCollector> typedCollectors;

    @Inject
    AttributeLoader(AttributeTranslator attributeTranslator) {
        this.attributeTranslator = attributeTranslator;
        this.typedCollectors = AttributeCollectors.get();
    }

    @SuppressWarnings("unchecked")
    List<Section<ViewAttribute>> getAttributesForView(View view) {
        //noinspection SSBasedInspection
        Log.d("AttributeLoader", "parentView: " + view.getParent().getClass());
        //noinspection SSBasedInspection
        Log.d("AttributeLoader", "superView: " + view.getParent().getClass().getSuperclass());

        // ARMAZENA AS SECOES DE ATRIBUTOS
        List<Section<ViewAttribute>> sections = new ArrayList<>(12);

        // PARA CADA COLETOR DE ATRIBUTOS
        for (TypedAttributeCollector aggregator : typedCollectors) {
            // SE O COLETOR ACEITAR A VIEW
            if (aggregator.acceptsType(view.getClass())) {
                // COLETA OS ATRIBUTOS E ADICIONA A SECTION
                List<ViewAttribute> attributes = aggregator.collect(view, attributeTranslator);
                Section<ViewAttribute> section = new Section<>(aggregator.getType(), attributes);
                sections.add(section);
            }

            // SE O COLETOR ACEITAR A SUPER VIEW DO PARENT VIEW DA VIEW
            if (view.getParent().getClass().getSuperclass() != null && aggregator.acceptsType(view.getParent().getClass().getSuperclass())) {
                // COLETA OS ATRIBUTOS DO PAI DA VIEW E ADICIONA A SECTION
                List<ViewAttribute> attributes = aggregator.collect((View) view.getParent(), attributeTranslator, view);
                Section<ViewAttribute> section = new Section<>(aggregator.getType(), attributes);
                section.setCustomName("(PARENT)");

                sections.add(section);

                //noinspection SSBasedInspection
                Log.d("AttributeLoader", section.getName());
            } else if (aggregator.acceptsType(view.getParent().getClass())) {
                // COLETA OS ATRIBUTOS DO PAI DA VIEW E ADICIONA A SECTION
                List<ViewAttribute> attributes = aggregator.collect((View) view.getParent(), attributeTranslator, view);
                Section<ViewAttribute> section = new Section<>(aggregator.getType(), attributes);
                section.setCustomName("(PARENT)");

                sections.add(section);

                //noinspection SSBasedInspection
                Log.d("AttributeLoader", section.getName());
            }
        }

        // ORGANIZA A LISTA
        Collections.sort(sections);

        for (Section<ViewAttribute> cursor : sections) {
            //noinspection SSBasedInspection
            Log.d("AttributeLoader", "sections: " + cursor.getName());
        }
        return sections;
    }

}