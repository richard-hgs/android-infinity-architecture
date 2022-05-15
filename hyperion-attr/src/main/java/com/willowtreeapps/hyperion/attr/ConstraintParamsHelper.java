package com.willowtreeapps.hyperion.attr;

import java.lang.reflect.Field;

public class ConstraintParamsHelper {

    boolean mIsGuideline;
    public int mWidth;
    public int mHeight;
    int mViewId;
    static final int UNSET = -1;
    public int guideBegin;
    public int guideEnd;
    public float guidePercent;
    public int leftToLeft;
    public int leftToRight;
    public int rightToLeft;
    public int rightToRight;
    public int topToTop;
    public int topToBottom;
    public int bottomToTop;
    public int bottomToBottom;
    public int baselineToBaseline;
    public int startToEnd;
    public int startToStart;
    public int endToStart;
    public int endToEnd;
    public float horizontalBias;
    public float verticalBias;
    public String dimensionRatio;
    public int circleConstraint;
    public int circleRadius;
    public float circleAngle;
    public int editorAbsoluteX;
    public int editorAbsoluteY;
    public int orientation;
    public int leftMargin;
    public int rightMargin;
    public int topMargin;
    public int bottomMargin;
    public int endMargin;
    public int startMargin;
    public int visibility;
    public int goneLeftMargin;
    public int goneTopMargin;
    public int goneRightMargin;
    public int goneBottomMargin;
    public int goneEndMargin;
    public int goneStartMargin;
    public float verticalWeight;
    public float horizontalWeight;
    public int horizontalChainStyle;
    public int verticalChainStyle;
    public float alpha;
    public boolean applyElevation;
    public float elevation;
    public float rotation;
    public float rotationX;
    public float rotationY;
    public float scaleX;
    public float scaleY;
    public float transformPivotX;
    public float transformPivotY;
    public float translationX;
    public float translationY;
    public float translationZ;
    public boolean constrainedWidth;
    public boolean constrainedHeight;
    public int widthDefault;
    public int heightDefault;
    public int widthMax;
    public int heightMax;
    public int widthMin;
    public int heightMin;
    public float widthPercent;
    public float heightPercent;
    public boolean mBarrierAllowsGoneWidgets;
    public int mBarrierDirection;
    public int mHelperType;
    public int[] mReferenceIds;
    public String mReferenceIdString;

    public ConstraintParamsHelper() {
    }

    /**
     * CLONA AS CONSTRAINTS
     * @param constraintParams ConstraintSet.Constraint -> é o objeto a ser passado
     */
    public void clone(Object constraintParams) throws Exception {
        extractConstraintParams(constraintParams);
    }

    /**
     * EXTRAI OS PARAMETROS DA CLASSE E OS TORNA ACESSIVEIS NO PROJETO
     * @param constraintParams ConstraintSet.Constraint -> é o objeto a ser passado
     */
    private void extractConstraintParams(Object constraintParams) throws Exception {
        // RECEBE OS PARAMETROS DA CLASSE ATUAL
        Field[] camposClasseAtual = getClass().getDeclaredFields();
        for (Field campoClasseAtual : camposClasseAtual){
            campoClasseAtual.setAccessible(true);

            String nomeCampoAtual = campoClasseAtual.getName();

            if (
                !nomeCampoAtual.equals("$change") &&
                !nomeCampoAtual.equals("serialVersionUID")
            ) {

                // EXTRAI O PARAMETRO DA CLASSE CONSTRAINT E ARMAZENA NESSA CLASSE
                Field campoConstraintParams = constraintParams.getClass().getDeclaredField(nomeCampoAtual);
                //noinspection ConstantConditions
                if (campoConstraintParams != null) {
                    campoConstraintParams.setAccessible(true);
                    campoClasseAtual.set(this, campoConstraintParams.get(constraintParams));
                }
            }
        }

        /*Field[] campos = constraintParams.getClass().getDeclaredFields();
        for (Field campo : campos) {
            campo.setAccessible(true);

            String nomeCampo = campo.getName();
            Object valorCampo = campo.get(constraintParams);

            LogHelper.d(nomeCampo + ": " + valorCampo);
        }*/
    }
}
