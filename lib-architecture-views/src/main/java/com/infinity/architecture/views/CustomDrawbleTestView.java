package com.infinity.architecture.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.infinity.architecture.utils.drawable.shapes.MultiEdgeDrawable;

import java.util.ArrayList;

public class CustomDrawbleTestView extends View {

    public CustomDrawbleTestView(Context context) {
        super(context);
        init(null);
    }

    public CustomDrawbleTestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomDrawbleTestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomDrawbleTestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        int edgeCount = 4;
        ArrayList<Float> edgeCornerRadiusArrayList = new ArrayList<Float>(){{
//            add(30f);
//            add(30f);
//            add(30f);
//            add(30f);
        }};
        float cornerRadius = 300;

        MultiEdgeDrawable multiEdgeDrawable = new MultiEdgeDrawable(edgeCount, 0, cornerRadius, edgeCornerRadiusArrayList, 0, 0f, 0, 0, Color.parseColor("#330000FF"), Paint.Style.FILL, 1);
        MultiEdgeDrawable multiEdgeDrawable2 = new MultiEdgeDrawable(edgeCount, 0, cornerRadius, edgeCornerRadiusArrayList, 10, 0f, 2.5f, 5, Color.RED, Paint.Style.STROKE, 5);

        InsetDrawable insetDrawable = new InsetDrawable(multiEdgeDrawable2, 5 / 2);

        Drawable[] drawables = new Drawable[]{multiEdgeDrawable, multiEdgeDrawable2};
        LayerDrawable layerDrawable = new LayerDrawable(drawables);

        setBackground(layerDrawable);
    }
}
