package com.example.qrscaner.view;


import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.text.format.DateFormat;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import android.widget.ImageView;
import android.widget.TextView;

import android.content.Context;

import android.util.AttributeSet;
import android.util.Log;

import android.view.LayoutInflater;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.qrscaner.Model.QrEmail;
import com.example.qrscaner.Model.QrMess;
import com.example.qrscaner.Model.QrScan;
import com.example.qrscaner.Model.QrText;
import com.example.qrscaner.Model.QrUrl;
import com.example.qrscaner.Model.QrWifi;
import com.example.qrscaner.Model.QreTelephone;
import com.example.qrscaner.R;
import com.example.qrscaner.utils.QRGContents;
import com.example.qrscaner.utils.QRGEncoder;

import java.util.Date;


public class QrScanResult extends ConstraintLayout {
    private static final String TAG = "resultQRString";
    private View mRootView;
    private Context mContext;
    private ImageView imvQrScanResultRender, imvQrScanResultIconCategory;
    private TextView tvQrScanResultCategoryName, tvQrScanResultDate;
    private Bitmap bitmap;
    private QRGEncoder qrgEncoder;

    public QrScanResult(@NonNull Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public QrScanResult(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();

    }


    private void initView() {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRootView = layoutInflater.inflate(R.layout.result_scan_layout, this, true);
        imvQrScanResultRender = mRootView.findViewById(R.id.img_qrRender);
        imvQrScanResultIconCategory = mRootView.findViewById(R.id.img_item_qr);
        tvQrScanResultCategoryName = mRootView.findViewById(R.id.tv_category_dialog_fragmentScan);
        tvQrScanResultDate = mRootView.findViewById(R.id.tv_date_dialog_fragmentScan);

    }

    public void setupData(QrScan qrScan) {
        String s = qrScan.getScanText();
        String[] content = s.split(":");
        setImage(s);

        if (content[0].equals("SMSTO")) {

            QrMess qrMess = new QrMess();

            qrMess.compileSMS(content);

        } else if (content[0].equals("http") || content[0].equals("https")) {
            QrUrl qrUrl = new QrUrl();
            qrUrl.compileUrl(content);
            setContentUrl(qrScan.getDate(), qrUrl);
        } else if (content[0].equals("WIFI")) {
            StringBuilder stringBuilder = new StringBuilder();
            String[] contentWifi = s.split(";");
            for (String value : contentWifi) {
                stringBuilder.append(value);
            }
            String contentWifi2 = stringBuilder.toString();
            String[] contentWifi3 = contentWifi2.split(":");
            QrWifi qrWifi = new QrWifi();
            qrWifi.compileWifi(contentWifi, contentWifi3);


        } else if (content[0].equals("MATMSG")) {
            QrEmail qrEmail = new QrEmail();
            StringBuilder stringBuilder = new StringBuilder();
            for (String value : content) {
                stringBuilder.append(value);
            }
            String contentEmail = stringBuilder.toString();
            String[] contentEmailCompile = contentEmail.split(";");
            qrEmail.compileEmail(contentEmailCompile);

        } else if (content[0].equals("tel")) {
            QreTelephone qreTelephone = new QreTelephone();
            qreTelephone.compile(content);

        } else {
            Log.e(TAG, s);
            QrText qrText = new QrText();
            qrText.setText(s);
        }

    }


    private void setContentUrl(long date, QrUrl qrUrl) {
        imvQrScanResultIconCategory.setImageResource(R.drawable.add_uri);
        // or you already have long value of date, use this instead of milliseconds variable.
        String dateString = DateFormat.format("MM/dd/yyyy", new Date(date)).toString();
        tvQrScanResultDate.setText(dateString);
        tvQrScanResultCategoryName.setText("Uri");

    }

    private void setImage(String s) {
        WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = Math.min(width, height);
        smallerDimension = smallerDimension * 3 / 4;

        qrgEncoder = new QRGEncoder(
                s, null,
                QRGContents.Type.TEXT,
                smallerDimension);
        qrgEncoder.setColorBlack(Color.BLACK);
        qrgEncoder.setColorWhite(Color.WHITE);
        bitmap = qrgEncoder.getBitmap();
        imvQrScanResultRender.setImageBitmap(bitmap);
    }
}
