package com.infinity.architecture.utils.bitmap;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Layout;

import androidx.annotation.ColorInt;

public class BmpCreatorElement {
    /**
     * TIPO DO ELEMENTO
     */
    private BmpCreatorElementType elementType;

    /**
     * DADOS DO ELEMENTO(BMP IMAGE, OU STRING TEXTO, ...)
     */
    private Object elementVal;

    /**
     * ALINHAMENTO
     */
    private BmpCreatorAlignment alignment;

    /**
     * ALINHAMENTO DO LAYOUT
     */
    private Layout.Alignment layAlignment;

    /**
     * TAMANHO DO ELEMENTO
     */
    private float size;

    /**
     * TIPO DE FONTE DO ELEMENTO(TEXTO)
     */
    private Typeface typeFace;

    /**
     * ture=BOLD_ENABLED false=BOLD_DISABLED DO ELEMENTO(TEXTO)
     */
    private boolean bold;

    /**
     * LARGURA DO ELEMENTO
     */
    private int width;

    /**
     * ALTURA DO ELEMENTO
     */
    private int height;

    /**
     * ADJUST Y TRANSLATION FOR CURRENT ELEMENT
     */
    private int translateYCorrection;

    /**
     * ADJUST X TRANSLATION FOR CURRENT ELEMENT
     */
    private int translateXCorrection;

    /**
     * COR DO ELEMENTO
     */
    @ColorInt
    private int color;

    /**
     * VALIDA SE SERÁ FEITA A TRANSLAÇÃO DO EIXO Y AO DESENHAR O ELEMENTO
     */
    private boolean translateY;

    /**
     * CONSTRUTOR PADRÃO
     * INICALIZA COMO ELEMENTO DO TIPO TEXTO
     */
    public BmpCreatorElement() {
        elementType = BmpCreatorElementType.TEXT;
        alignment = BmpCreatorAlignment.LEFT;
        layAlignment = Layout.Alignment.ALIGN_NORMAL;
        size = 24f;
        typeFace = Typeface.DEFAULT;
        width = BmpCreatorLayoutParams.WRAP_CONTENT;
        height = BmpCreatorLayoutParams.WRAP_CONTENT;
        translateYCorrection = 0;
        translateXCorrection = 0;
        color = Color.BLACK;
        translateY = true;
    }

    // GETTERS AND SETTERS
    public BmpCreatorElementType getElementType() {
        return elementType;
    }

    public void setElementType(BmpCreatorElementType elementType) {
        this.elementType = elementType;
    }

    public Object getElementVal() {
        return elementVal;
    }

    public void setElementVal(Object elementVal) {
        this.elementVal = elementVal;
    }

    public BmpCreatorAlignment getAlignment() {
        return alignment;
    }

    public void setAlignment(BmpCreatorAlignment alignment) {
        this.alignment = alignment;
    }

    public Paint.Align translateAlignmentToPaint() {
        if (getAlignment() != null && getAlignment() == BmpCreatorAlignment.CENTER) {
            return Paint.Align.CENTER;
        } else if (getAlignment() != null && getAlignment() == BmpCreatorAlignment.RIGHT) {
            return Paint.Align.RIGHT;
        } else {
            return Paint.Align.LEFT;
        }
    }

    public Layout.Alignment getLayAlignment() {
        return layAlignment;
    }

    public void setLayAlignment(Layout.Alignment layAlignment) {
        this.layAlignment = layAlignment;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public Typeface getTypeFace() {
        return typeFace;
    }

    public void setTypeFace(Typeface typeFace) {
        this.typeFace = typeFace;
    }

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getTranslateYCorrection() {
        return translateYCorrection;
    }

    public void setTranslateYCorrection(int translateYCorrection) {
        this.translateYCorrection = translateYCorrection;
    }

    public int getTranslateXCorrection() {
        return translateXCorrection;
    }

    public void setTranslateXCorrection(int translateXCorrection) {
        this.translateXCorrection = translateXCorrection;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isTranslateY() {
        return translateY;
    }

    public void setTranslateY(boolean translateY) {
        this.translateY = translateY;
    }
}
