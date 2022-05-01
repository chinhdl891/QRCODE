package com.example.qrscaner.utils;

import static com.example.qrscaner.config.Constant.BITMAP_HEIGHT_QR;
import static com.example.qrscaner.config.Constant.BITMAP_WIDTH_QR;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.example.qrscaner.models.QrScan;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

public class BitMapUtils {



    public static Bitmap bitmapQR(QrScan.QRType type, String s, int color) {
        Bitmap bitmap = null;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        BarcodeFormat format;

        switch (type) {
            case BAR39:
                format = BarcodeFormat.CODE_39;
                break;
            case BAR93:
                format = BarcodeFormat.CODE_93;
                break;
            case BAR128:
                format = BarcodeFormat.CODE_128;
                break;
            default:
                format = BarcodeFormat.QR_CODE;
                break;
        }

        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(s, format, BITMAP_WIDTH_QR, BITMAP_HEIGHT_QR);
            bitmap = Bitmap.createBitmap(BITMAP_WIDTH_QR, BITMAP_HEIGHT_QR, Bitmap.Config.ARGB_8888);
            for (int i = 0; i < BITMAP_WIDTH_QR; i++) {
                for (int j = 0; j < BITMAP_HEIGHT_QR; j++) {
                    bitmap.setPixel(i, j, bitMatrix.get(i, j) ? color : Color.WHITE);
                }
            }

        } catch (Exception e) {

        }
        return bitmap;

    }
}
