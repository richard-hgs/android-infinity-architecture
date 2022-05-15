package com.infinity.architecture.utils.bitmap;

import android.graphics.Bitmap;
import android.graphics.Color;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.EnumMap;
import java.util.Map;

public class BmpUtils {
    //gera qr code
    public static Bitmap generateQrCode(String txt, int qrWidth, int qrHeight) {
        Bitmap bmp = null;
        QRCodeWriter writer = new QRCodeWriter();

        try {
            BitMatrix bitMatrix = writer.encode(txt, BarcodeFormat.QR_CODE, qrWidth, qrHeight);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }

        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bmp;
    }

    /*
    new EnumMap<EncodeHintType, Object>(EncodeHintType.class) {{
        put(EncodeHintType.CHARACTER_SET, "UTF-8");
        put(EncodeHintType.MARGIN, 0);
    }}
     */
    public static Bitmap generateQrCode(String txt, int qrWidth, int qrHeight, @ColorInt int qrColor, @ColorInt int qrBgColor, @Nullable Map<EncodeHintType, Object> hints) {
        Bitmap bmp = null;

        if (hints == null) {
            hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.MARGIN, 1); /* default = 4 */
        }

        QRCodeWriter writer = new QRCodeWriter();

        try {
            BitMatrix bitMatrix = writer.encode(txt, BarcodeFormat.QR_CODE, qrWidth, qrHeight, hints);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? qrColor : qrBgColor);
                }
            }

        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bmp;
    }

    //gera codigo de barras
    public static Bitmap generateBarCode(String barcode, int width, int height) throws Exception {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

        BitMatrix bitMatrix = multiFormatWriter.encode(barcode, BarcodeFormat.CODE_128, width, height);
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        return barcodeEncoder.createBitmap(bitMatrix);
    }

    //gera codigo pdf417
    public static Bitmap generatePdf417(String pdf417Code, int width, int height) throws Exception {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

        BitMatrix bitMatrix = multiFormatWriter.encode(pdf417Code, BarcodeFormat.PDF_417, width,height);
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        return barcodeEncoder.createBitmap(bitMatrix);
    }

    //gera codigo pdf417
    public static Bitmap generatePdf417(String pdf417Code, int cWidth, int cHeight, @ColorInt int color, @ColorInt int bgColor, @Nullable Map<EncodeHintType, Object> hints) {
        Bitmap bmp = null;

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

        if (hints == null) {
            hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.MARGIN, 4); /* default = 4 */
        }

        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(pdf417Code, BarcodeFormat.PDF_417, cWidth, cHeight, hints);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? color : bgColor);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bmp;
    }
}
