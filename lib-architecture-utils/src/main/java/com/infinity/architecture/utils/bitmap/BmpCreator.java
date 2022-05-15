package com.infinity.architecture.utils.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import androidx.annotation.ColorInt;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class BmpCreator {

    // ROTATION SIZES OF A SQUARE
    // https://en.wikipedia.org/wiki/Rotation_matrix
    // https://stackoverflow.com/questions/3231176/how-to-get-size-of-a-rotated-rectangle
    /*
     * A1(x1, y1) -> x1 = cos a e y1 = sen a -> a = arccos x1 e b = arcsen y1
     *
     * B1(x2, y2) -> x2 = cos (a+b) e y2 = sen (a+b) -> x2 = cos(arccos(x1) + b) e y2 = sen(arcsen(y1) + b)
     */


    /**
     * CRIA A IMAGEM BITMAP
     * @param bmpWidth      LARGURA DA IMAGEM
     * @param bmpHeight     ALTURA DA IMAGEM
     * @param bgColor       COR DE FUNDO DA IMAGEM
     * @param elements      ELEMENTOS QUE COMPOEM A IMAGEM
     * @return              BITMAP DA IMAGEM
     */
    @Deprecated
    public static Bitmap generateBitmap(int bmpWidth, int bmpHeight, @ColorInt int bgColor, @NonNull ArrayList<BmpCreatorElement> elements) {
        return generateBitmap(bmpWidth, bmpHeight, bgColor, 100, elements);
    }


    /**
     * CRIA A IMAGEM BITMAP
     * @param bmpWidth      LARGURA DA IMAGEM
     * @param bmpHeight     ALTURA DA IMAGEM
     * @param bgColor       COR DE FUNDO DA IMAGEM
     * @param bmpQuality    QUALIDADE DO BITMAP 1-100 | 1=BAIXA QUALIDADE, 100=ALTA QUALIDADE
     * @param elements      ELEMENTOS QUE COMPOEM A IMAGEM
     * @return              BITMAP DA IMAGEM
     */
    public static Bitmap generateBitmap(int bmpWidth, int bmpHeight, @ColorInt int bgColor, @IntRange(from = 1, to = 100) int bmpQuality, @NonNull ArrayList<BmpCreatorElement> elements) {
        //String text = "TITULO DE TESTE BEM GRANDE PARA DESCOBRIR O QUE IRÁ ACONTECER COM BLABLABLALBALBA";

        // DIMENSÕES CALCULADAS
        int measuredBmpHeight = bmpHeight;
        int measuredBmpWidth = bmpWidth;

        // PARA CADA ELEMENTO DA LISTA MEASURA O LAYOUT
        for (int i=0; i<elements.size(); i++) {
            // RECEBE O ELEMENTO DA POSICAO ATUAL
            BmpCreatorElement elementAt = elements.get(i);

            // SE O ELEMENTO FOR DO TIPO TEXTO
            if (elementAt.getElementType() == BmpCreatorElementType.TEXT) {
                // RECEBE LARGURA E ALTURA DO ELEMENTO
                int elementAtWidth = 0;
                int elementAtHeight = 0;

                // CONFIGURACOES DO DESENHO PARA TEXTO
                TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
                paint.setTextSize(elementAt.getSize());
                paint.setColor(elementAt.getColor());
                paint.setTextAlign(elementAt.translateAlignmentToPaint());
                if (elementAt.isBold()) {
                    Typeface tfBold = Typeface.create(elementAt.getTypeFace(), Typeface.BOLD);
                    paint.setTypeface(tfBold);
                } else {
                    paint.setTypeface(elementAt.getTypeFace());
                }



                //paint.setTypeface()

                // RECEBE A LARGURA DO ELEMENTO
                if (elementAt.getWidth() == BmpCreatorLayoutParams.WRAP_CONTENT && bmpWidth > 0) {
                    elementAtWidth = bmpWidth;
                } else if (elementAt.getWidth() == BmpCreatorLayoutParams.WRAP_CONTENT && bmpWidth == BmpCreatorLayoutParams.WRAP_CONTENT) {
                    elementAtWidth = (int) paint.measureText((String) elementAt.getElementVal());
                } else if (elementAt.getWidth() > 0) {
                    elementAtWidth = elementAt.getWidth();
                } else {
                    elementAtWidth = bmpWidth;
                }

                // LAYOUT PARA REALIZAR O WRAP DO TEXTO
                StaticLayout textLayout = new StaticLayout(
                    (String) elementAt.getElementVal(),
                    paint,
                    elementAtWidth,
                    elementAt.getLayAlignment(),
                    1.0f,
                    0.0f,
                    false
                );

                // RECEBE A ALTURA CALCULADA DO ELEMENTO
                elementAtHeight = textLayout.getHeight();

                // SE FOR PARA MOVIMENTAR O EIXO Y
                if (elementAt.isTranslateY()) {
                    // SOMA A ALTURA
                    measuredBmpHeight += elementAtHeight;
                }
                // RECEBE A LARGURA SE ELA FOR MAIOR QUE A LARGURA DO BMP
                if (bmpWidth < elementAtWidth) {
                    measuredBmpWidth = elementAtWidth;
                }
            } else if (elementAt.getElementType() == BmpCreatorElementType.IMAGE_BMP) {
                // SE O ELEMENTO FOR DO TIPO BITMAP
                // RECEBE LARGURA E ALTURA DO ELEMENTO
                int elementAtWidth = 0;
                int elementAtHeight = 0;
                int elementAtTranslateYCorrection = elementAt.getTranslateYCorrection();
                int elementAtTranslateXCorrection = elementAt.getTranslateXCorrection();

                // RECEBE A LARGURA DO ELEMENTO
                if (elementAt.getWidth() == BmpCreatorLayoutParams.WRAP_CONTENT && bmpWidth > 0) {
                    elementAtWidth = bmpWidth;
                } else if (elementAt.getWidth() == BmpCreatorLayoutParams.WRAP_CONTENT && bmpWidth == BmpCreatorLayoutParams.WRAP_CONTENT) {
                    elementAtWidth = ((Bitmap) elementAt.getElementVal()).getWidth();
                } else if (elementAt.getWidth() > 0) {
                    elementAtWidth = elementAt.getWidth();
                } else {
                    elementAtWidth = bmpWidth;
                }

                // RECEBE A ALTURA DO ELEMENTO
                elementAtHeight = ((Bitmap) elementAt.getElementVal()).getHeight();

                // SE FOR PARA MOVIMENTAR O EIXO Y
                if (elementAt.isTranslateY()) {
                    // SOMA A ALTURA
                    measuredBmpHeight += (elementAtHeight + elementAtTranslateYCorrection);
                }
                // RECEBE A LARGURA SE ELA FOR MAIOR QUE A LARGURA DO BMP
                if (bmpWidth < elementAtWidth) {
                    measuredBmpWidth = (elementAtWidth + elementAtTranslateXCorrection);
                }
            }
        }

        // CRIA O BITMAP
        Bitmap bmpImage = Bitmap.createBitmap(
            bmpWidth != BmpCreatorLayoutParams.WRAP_CONTENT ? bmpWidth : measuredBmpWidth,
            bmpHeight != BmpCreatorLayoutParams.WRAP_CONTENT ? bmpHeight : measuredBmpHeight,
            Bitmap.Config.ARGB_8888
        );
        // CANVAS QUE REALIZA O DESENHO NA IMAGEM
        Canvas canvas = new Canvas(bmpImage);

        // SE COR DE FUNDO DIFERENTE DE TRANSPARENTE
        if (bgColor != Color.TRANSPARENT) {
            // RECEBE O DESENHO DO QUADRADO DO FUNDO
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(bgColor);

            // DESENHA O QUADRADO DO FUNDO NO BITMAP
            canvas.drawRect(
                0,
                0,
                bmpWidth != BmpCreatorLayoutParams.WRAP_CONTENT ? bmpWidth : measuredBmpWidth,
                bmpHeight != BmpCreatorLayoutParams.WRAP_CONTENT ? bmpHeight : measuredBmpHeight,
                paint
            );
        }


        int currentCanvasTranslationY = 0;
        int currentCanvasTranslationX = 0;

        // PARA CADA ELEMENTO DA LISTA DESENHA NO LAYOUT
        for (int i=0; i<elements.size(); i++) {
            // RECEBE O ELEMENTO DA POSICAO ATUAL
            BmpCreatorElement elementAt = elements.get(i);

            // SE O ELEMENTO FOR DO TIPO TEXTO
            if (elementAt.getElementType() == BmpCreatorElementType.TEXT) {
                // RECEBE LARGURA E ALTURA DO ELEMENTO
                int elementAtWidth = 0;
                int elementAtHeight = 0;

                // CONFIGURACOES DO DESENHO PARA TEXTO
                TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
                paint.setTextSize(elementAt.getSize());
                paint.setColor(elementAt.getColor());
                paint.setTextAlign(elementAt.translateAlignmentToPaint());
                if (elementAt.isBold()) {
                    Typeface tfBold = Typeface.create(elementAt.getTypeFace(), Typeface.BOLD);
                    paint.setTypeface(tfBold);
                } else {
                    paint.setTypeface(elementAt.getTypeFace());
                }

                // RECEBE A LARGURA DO ELEMENTO
                if (elementAt.getWidth() == BmpCreatorLayoutParams.WRAP_CONTENT && bmpWidth > 0) {
                    elementAtWidth = bmpWidth;
                } else if (elementAt.getWidth() == BmpCreatorLayoutParams.WRAP_CONTENT && bmpWidth == BmpCreatorLayoutParams.WRAP_CONTENT) {
                    elementAtWidth = (int) paint.measureText((String) elementAt.getElementVal());
                } else if (elementAt.getWidth() > 0) {
                    elementAtWidth = elementAt.getWidth();
                } else {
                    elementAtWidth = bmpWidth;
                }

                // LAYOUT PARA REALIZAR O WRAP DO TEXTO
                StaticLayout textLayout = new StaticLayout(
                    (String) elementAt.getElementVal(),
                    paint,
                    elementAtWidth,
                    elementAt.getLayAlignment(),
                    1.0f,
                    0.0f,
                    false
                );

                // RECEBE A ALTURA CALCULADA DO ELEMENTO
                elementAtHeight = textLayout.getHeight();

                // DESENHA NO LAYOUT
                textLayout.draw(canvas);

                if (elementAt.isTranslateY()) {
                    // AVANCA O CANVAS PARA A PRÓXIMA LINHA
                    currentCanvasTranslationY += elementAtHeight;
                    currentCanvasTranslationX = 0;
                    canvas.translate(0, elementAtHeight);
                }
            } else if (elementAt.getElementType() == BmpCreatorElementType.IMAGE_BMP) {
                // SE O ELEMENTO FOR DO TIPO BITMAP
                // RECEBE LARGURA E ALTURA DO ELEMENTO
                int elementAtWidth = 0;
                int elementAtHeight = 0;

                // RECEBE A LARGURA DO ELEMENTO
                if (elementAt.getWidth() == BmpCreatorLayoutParams.WRAP_CONTENT && bmpWidth > 0) {
                    elementAtWidth = bmpWidth;
                } else if (elementAt.getWidth() == BmpCreatorLayoutParams.WRAP_CONTENT && bmpWidth == BmpCreatorLayoutParams.WRAP_CONTENT) {
                    elementAtWidth = ((Bitmap) elementAt.getElementVal()).getWidth();
                } else if (elementAt.getWidth() > 0) {
                    elementAtWidth = elementAt.getWidth();
                } else {
                    elementAtWidth = bmpWidth;
                }

                // RECEBE A ALTURA DO ELEMENTO
                elementAtHeight = ((Bitmap) elementAt.getElementVal()).getHeight();

                // RECEBE O ALINHAMENTO
                float bmpLeft = 0;
                float bmpTop = 0;
                // DESENHA O BITMAP NO LAYOUT
                if (elementAt.getLayAlignment() == Layout.Alignment.ALIGN_CENTER) {
                    int elementAtBmpWidth = ((Bitmap) elementAt.getElementVal()).getWidth();
                    int elementAtBmpHeight = ((Bitmap) elementAt.getElementVal()).getHeight();

                    if (elementAtBmpWidth < measuredBmpWidth) {
                        bmpLeft = (measuredBmpWidth - elementAtBmpWidth) / 2;
                    }
                } else if (elementAt.getLayAlignment() == Layout.Alignment.ALIGN_OPPOSITE) {
                    int elementAtBmpWidth = ((Bitmap) elementAt.getElementVal()).getWidth();
                    int elementAtBmpHeight = ((Bitmap) elementAt.getElementVal()).getHeight();

                    if (elementAtBmpWidth < measuredBmpWidth) {
                        bmpLeft = (measuredBmpWidth - elementAtBmpWidth);
                    }
                }

                if (elementAt.getTranslateYCorrection() != 0) {
                    canvas.translate(0, elementAt.getTranslateYCorrection());
                }

                // DESENHA NO LAYOUT
                canvas.drawBitmap((Bitmap) elementAt.getElementVal(), bmpLeft, bmpTop, null);

                if (elementAt.isTranslateY()) {
                    // AVANCA O CANVAS PARA A PRÓXIMA LINHA
                    currentCanvasTranslationY += elementAtHeight;
                    currentCanvasTranslationX = 0;
                    canvas.translate(0, elementAtHeight);
                }
            }
        }

        // ALINHAMENTO BASE
        //float baseline = -paint.ascent(); // ascent() is negative

        // LARGURA E ALTURA DO BITMAP
        //int width = (int) (paint.measureText(text) + 0.5f); // round
        //int width = (int) 60; // round
        //int height = (int) (baseline + paint.descent() + 0.5f);
        //int height = textLayout.getHeight();

        //canvas.drawText(text, 0, baseline, paint);

        if (bmpQuality > 0 && bmpQuality < 100) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bmpImage.compress(Bitmap.CompressFormat.JPEG, bmpQuality, out);
            bmpImage = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
        }

        return bmpImage;
    }

    public static long bitmapSize(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] imageInByte = stream.toByteArray();
        long lengthbmp = imageInByte.length;

        return lengthbmp;
    }
}
