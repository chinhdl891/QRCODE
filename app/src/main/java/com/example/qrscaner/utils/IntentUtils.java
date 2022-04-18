package com.example.qrscaner.utils;

import static android.content.Context.WIFI_SERVICE;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.widget.Toast;

import com.example.qrscaner.Model.QrEmail;
import com.example.qrscaner.Model.QrGenerate;
import com.example.qrscaner.Model.QrMess;
import com.example.qrscaner.Model.QrScan;
import com.example.qrscaner.Model.QrUrl;
import com.example.qrscaner.Model.QrWifi;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class IntentUtils {
    private final QrGenerate mQrGenerate = new QrGenerate();
    private final QrScan qrScan = new QrScan();
    private Context mContext;



    public void IntentAction(Context mContext,  String content , QrScan.QRType type){
        this.mContext = mContext;
        mQrGenerate.setContent(content);
        qrScan.setScanText(content);
        mQrGenerate.setQrType(type);
        qrScan.setTypeQR(type);
        if (mQrGenerate.getQrType() == QrScan.QRType.PHONE) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse(mQrGenerate.getContent()));
            mContext.startActivity(intent);
        } else if (mQrGenerate.getQrType() == QrScan.QRType.TEXT) {

            ClipData clipData = ClipData.newPlainText("text", mQrGenerate.getContent());

            ClipboardManager clipboardManager = (ClipboardManager)mContext.getSystemService(Context.CLIPBOARD_SERVICE);

            clipboardManager.setPrimaryClip(clipData);

            Toast.makeText(mContext, "Data Copied to Clipboard", Toast.LENGTH_SHORT).show();
        } else if (mQrGenerate.getQrType() == QrScan.QRType.SMS) {
            QrMess qrMess = new QrMess();
            String[] mess = qrScan.getScanText().split(":");
            qrMess.compileSMS(mess);
            Uri uri = Uri.parse("smsto:" + qrMess.getSendBy());
            Intent it = new Intent(Intent.ACTION_SENDTO, uri);
            it.putExtra("sms_body", qrMess.getContent());
            mContext.startActivity(it);
        } else if (mQrGenerate.getQrType() == QrScan.QRType.URL) {
            QrUrl qrUrl = new QrUrl();
            String[] url = qrScan.getScanText().split(":");
            qrUrl.compileUrl(url);

            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(qrUrl.getUrl()));
            mContext.startActivity(i);
        } else if (mQrGenerate.getQrType() == QrScan.QRType.WIFI) {

            StringBuilder stringBuilder = new StringBuilder();
            String[] contentWifi = qrScan.getScanText().split(";");
            for (String value : contentWifi) {
                stringBuilder.append(value);
            }
            String contentWifi2 = stringBuilder.toString();
            String[] contentWifi3 = contentWifi2.split(":");
            QrWifi qrWifi = new QrWifi();
            qrWifi.compileWifi(contentWifi, contentWifi3);
            connectWifi(qrWifi.getWifiName(),qrWifi.getPass());
        }
        else if (mQrGenerate.getQrType()== QrScan.QRType.EMAIL){
            QrEmail qrEmail = new QrEmail();
            String[] email = qrScan.getScanText().split(":");
            qrEmail.compileEmail(email);

            Uri uri = Uri.parse("mailto:" + qrEmail.getSendBy())
                    .buildUpon()
                    .appendQueryParameter("subject", qrEmail.getSendTo())
                    .appendQueryParameter("body", qrEmail.getContent())
                    .build();

            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, uri);
            mContext.startActivity(Intent.createChooser(emailIntent, "email"));
        }else if (mQrGenerate.getQrType()== QrScan.QRType.BAR128 || mQrGenerate.getQrType()== QrScan.QRType.BAR93 || mQrGenerate.getQrType()== QrScan.QRType.BAR39 || qrScan.getTypeQR() == QrScan.QRType.PRODUCT){
            String escapedQuery = null;
            try {
                escapedQuery = URLEncoder.encode(mQrGenerate.getContent(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Uri uri = Uri.parse("http://www.google.com/search?q=" + escapedQuery);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            mContext.startActivity(intent);
        }
    }

    private void  connectWifi(String ssid, String password){
        WifiConfiguration wifiConfig = new WifiConfiguration();
        wifiConfig.SSID = String.format("\"%s\"", ssid);
        wifiConfig.preSharedKey = String.format("\"%s\"", password);
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(WIFI_SERVICE);
        //remember id
        int netId = wifiManager.addNetwork(wifiConfig);
        wifiManager.disconnect();
        wifiManager.enableNetwork(netId, true);

        boolean isConnectionSuccessful = wifiManager.reconnect();

        if(isConnectionSuccessful){
            Toast.makeText(mContext, "Connection successful", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(mContext, "Invalid credential", Toast.LENGTH_SHORT).show();
        }
    }
}
