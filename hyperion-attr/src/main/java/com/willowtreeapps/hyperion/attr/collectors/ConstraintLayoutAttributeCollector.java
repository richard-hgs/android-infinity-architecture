package com.willowtreeapps.hyperion.attr.collectors;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import android.view.View;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.attr.ConstraintParamsHelper;
import com.willowtreeapps.hyperion.attr.ViewAttribute;
import com.willowtreeapps.hyperion.plugin.v1.AttributeTranslator;

import java.util.ArrayList;
import java.util.List;

@AutoService(TypedAttributeCollector.class)
public class ConstraintLayoutAttributeCollector extends TypedAttributeCollector<ConstraintLayout> {

    /**
     * CONSTRUTOR PADRAO
     */
    public ConstraintLayoutAttributeCollector() {
        super(ConstraintLayout.class);
    }

    /**
     * COLETA OS DADOS DO CONSTRAINT LAYOUT
     * @param view                  VIEW A SER COLETADA
     * @param attributeTranslator   TRADUTOR DE ATRIBUTOS
     * @return                      RETORNA A LISTA DE ATRIBUTOS
     */
    @NonNull
    @Override
    public List<ViewAttribute> collect(ConstraintLayout view, AttributeTranslator attributeTranslator,
                                       @Nullable Object... objs) {
        List<ViewAttribute> attributes = new ArrayList<>();

        try {
            if (objs != null && objs.length > 0) {
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(view);
                ConstraintParamsHelper constraintParamsHelper = new ConstraintParamsHelper();
                constraintParamsHelper.clone(constraintSet.getParameters(((View) objs[0]).getId()));

                attributes.add(new ViewAttribute<>(
                    "ConstraintSetIdStart",
                    getIdFromConnectionSide(
                        constraintParamsHelper.startToEnd, constraintParamsHelper.startToStart, view.getContext()
                    )));
                attributes.add(new ViewAttribute<>(
                    "ConstraintSetIdLeft",
                    getIdFromConnectionSide(
                        constraintParamsHelper.leftToRight, constraintParamsHelper.leftToLeft, view.getContext()
                    )));
                attributes.add(new ViewAttribute<>(
                    "ConstraintSetIdTop",
                    getIdFromConnectionSide(
                        constraintParamsHelper.topToBottom, constraintParamsHelper.topToTop, view.getContext()
                    )));
                attributes.add(new ViewAttribute<>(
                    "ConstraintSetIdEnd",
                    getIdFromConnectionSide(
                        constraintParamsHelper.endToStart, constraintParamsHelper.endToEnd, view.getContext()
                    )));
                attributes.add(new ViewAttribute<>(
                    "ConstraintSetIdRight",
                    getIdFromConnectionSide(
                        constraintParamsHelper.rightToLeft, constraintParamsHelper.rightToRight, view.getContext()
                    )));
                attributes.add(new ViewAttribute<>(
                    "ConstraintSetIdBottom",
                    getIdFromConnectionSide(
                        constraintParamsHelper.bottomToTop, constraintParamsHelper.bottomToBottom, view.getContext()
                    )));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return attributes;
    }

    private String getIdFromConnectionSide(int side1, int side2, Context context) throws Exception {
        if (side1 != -1) {
            if (side1 != 0) {
                return context.getResources().getResourceEntryName(side1);
            } else {
                return "PARENT_ID";
            }
        } else if (side2 != -1) {
            if (side2 != 0) {
                return context.getResources().getResourceEntryName(side2);
            } else {
                return "PARENT_ID";
            }
        } else {
            return "-1";
        }
    }
}
