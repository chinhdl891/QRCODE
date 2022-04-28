package com.example.qrscaner.utils;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.example.qrscaner.models.QrScan;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

public class BitMapUtils {

    private static final int BITMAP_WIDTH = 512;
    private static final int BITMAP_HEIGHT = 512;


    public static Bitmap bitmapQR(QrScan.QRType type, String s, int color) {
          Bitmap bitmap = null;
        if (type == QrScan.QRType.BAR39) {
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            try {
                BitMatrix bitMatrix = multiFormatWriter.encode(s, BarcodeFormat.CODE_39, BITMAP_WIDTH, BITMAP_HEIGHT);
                bitmap = Bitmap.createBitmap(BITMAP_WIDTH, BITMAP_HEIGHT, Bitmap.Config.ARGB_8888);
                for (int i = 0; i < BITMAP_WIDTH; i++) {
                    for (int j = 0; j < BITMAP_HEIGHT; j++) {
                        bitmap.setPixel(i, j, bitMatrix.get(i, j) ? color : Color.WHITE);
                    }
                }

            } catch (Exception e) {

            }
            return bitmap;

        } else if (type == QrScan.QRType.BAR93) {
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            try {
                BitMatrix bitMatrix = multiFormatWriter.encode(s, BarcodeFormat.CODE_93, BITMAP_WIDTH, BITMAP_HEIGHT);
                bitmap = Bitmap.createBitmap(BITMAP_WIDTH, BITMAP_HEIGHT, Bitmap.Config.ARGB_8888);
                for (int i = 0; i < BITMAP_WIDTH; i++) {
                    for (int j = 0; j < BITMAP_HEIGHT; j++) {
                        bitmap.setPixel(i, j, bitMatrix.get(i, j) ? color : Color.WHITE);
                    }
                }

            } catch (Exception e) {

            }
            return bitmap;

        } else if (type == QrScan.QRType.BAR128) {
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            try {
                BitMatrix bitMatrix = multiFormatWriter.encode(s, BarcodeFormat.CODE_128, BITMAP_WIDTH, BITMAP_HEIGHT);
                bitmap = Bitmap.createBitmap(BITMAP_WIDTH, BITMAP_HEIGHT, Bitmap.Config.ARGB_8888);
                for (int i = 0; i < BITMAP_WIDTH; i++) {
                    for (int j = 0; j < BITMAP_HEIGHT; j++) {
                        bitmap.setPixel(i, j, bitMatrix.get(i, j) ? color : Color.WHITE);
                    }
                }

            } catch (Exception e) {

            }
            return bitmap;

        } else {
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            try {
                BitMatrix bitMatrix = multiFormatWriter.encode(s, BarcodeFormat.QR_CODE, BITMAP_WIDTH, BITMAP_HEIGHT);
                bitmap = Bitmap.createBitmap(BITMAP_WIDTH, BITMAP_HEIGHT, Bitmap.Config.ARGB_8888);
                for (int i = 0; i < BITMAP_WIDTH; i++) {
                    for (int j = 0; j < BITMAP_HEIGHT; j++) {
                        bitmap.setPixel(i, j, bitMatrix.get(i, j) ? color : Color.WHITE);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return bitmap;
        }

    }
}
