package com.example.qrscaner.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.qrscaner.Model.QrEmail;
import com.example.qrscaner.Model.QrGenerate;
import com.example.qrscaner.Model.QrMess;
import com.example.qrscaner.Model.QrProduct;
import com.example.qrscaner.Model.QrScan;
import com.example.qrscaner.Model.QrUrl;
import com.example.qrscaner.Model.QrWifi;
import com.example.qrscaner.Model.QreTelephone;

public class ShareUtils {

    public static void shareQR(Context context, QrScan qrCode){
        String[] content = qrCode.getScanText().split(":");
        String shareContent = "";
        switch (qrCode.getTypeQR()) {
            case WIFI:
                QrWifi qrWifi = new QrWifi();
                StringBuilder stringBuilder = new StringBuilder();
                String[] contentWifi = qrCode.getScanText().split(";");
                for (String value : contentWifi) {
                    stringBuilder.append(value);
                }
                String contentWifi2 = stringBuilder.toString();
                String[] contentWifi3 = contentWifi2.split(":");
                qrWifi.compileWifi(contentWifi, contentWifi3);
                shareContent = qrWifi.getShare();
                break;
            case TEXT:
                shareContent = qrCode.getScanText();
                break;
            case PHONE:
                QreTelephone qreTelephone = new QreTelephone();
                qreTelephone.compile(content);
                shareContent = qreTelephone.getShare();
                break;
            case EMAIL:
                QrEmail qrEmail = new QrEmail();
                qrEmail.compileEmail(content);
                shareContent = qrEmail.getShare();
                break;
            case SMS:
                QrMess qrMess = new QrMess();
                qrMess.compileSMS(content);
                shareContent = qrMess.getShare();
                break;
            case URL:
                QrUrl qrUrl = new QrUrl();
                qrUrl.compileUrl(content);
                shareContent = qrUrl.getShare();
                break;
            case PRODUCT:
                QrProduct qrProduct = new QrProduct();
                qrProduct.compileProduct(qrCode.getScanText());
                shareContent = qrProduct.getShare();
                break;
            case ERROR:
            case BAR39:
            case BAR93:
            case BAR128:
                break;
        }
        Intent intentShare = new Intent(Intent.ACTION_SEND);
        intentShare.setType("text/plain");
        intentShare.putExtra(Intent.EXTRA_TEXT, shareContent);
        context.startActivity(intentShare);
    }
    public static void shareGenQR(Context context, QrGenerate qrCode){
        String[] content = qrCode.getContent().split(":");
        String shareContent = "";
        switch (qrCode.getQrType()) {
            case WIFI:
                QrWifi qrWifi = new QrWifi();
                StringBuilder stringBuilder = new StringBuilder();
                String[] contentWifi = qrCode.getContent().split(";");
                for (String value : contentWifi) {
                    stringBuilder.append(value);
                }
                String contentWifi2 = stringBuilder.toString();
                String[] contentWifi3 = contentWifi2.split(":");
                qrWifi.compileWifi(contentWifi, contentWifi3);
                shareContent = qrWifi.getShare();
                break;
            case TEXT:
                shareContent = qrCode.getContent();
                break;
            case PHONE:
                QreTelephone qreTelephone = new QreTelephone();
                qreTelephone.compile(content);
                shareContent = qreTelephone.getShare();
                break;
            case EMAIL:
                QrEmail qrEmail = new QrEmail();
                qrEmail.compileEmail(content);
                shareContent = qrEmail.getShare();
                break;
            case SMS:
                QrMess qrMess = new QrMess();
                qrMess.compileSMS(content);
                shareContent = qrMess.getShare();
                break;
            case URL:
                QrUrl qrUrl = new QrUrl();
                qrUrl.compileUrl(content);
                shareContent = qrUrl.getShare();
                break;
            case PRODUCT:
                QrProduct qrProduct = new QrProduct();
                qrProduct.compileProduct(qrCode.getContent());
                shareContent = qrProduct.getShare();
                break;
            case ERROR:
            case BAR39:
            case BAR93:
            case BAR128:
                break;
        }
        Intent intentShare = new Intent(Intent.ACTION_SEND);
        intentShare.setType("text/plain");
        intentShare.putExtra(Intent.EXTRA_TEXT, shareContent);
        context.startActivity(intentShare);
    }

    public static void sharePalette(Bitmap bitmap,Context context) {

        String bitmapPath = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "palette", "share palette");
        Uri bitmapUri = Uri.parse(bitmapPath);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/png");
        intent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
        context.startActivity(Intent.createChooser(intent, "Share"));
    }


    public static void sharePalette(Context context,Bitmap bitmap) {

        String bitmapPath = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "" + System.currentTimeMillis(), null);
        Uri bitmapUri = Uri.parse(bitmapPath);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/png");
        intent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
        context.startActivity(Intent.createChooser(intent, "Share"));
    }

}
