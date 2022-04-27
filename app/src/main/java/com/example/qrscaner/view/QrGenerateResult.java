package com.example.qrscaner.view;


import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.qrscaner.Model.QrEmail;
import com.example.qrscaner.Model.QrGenerate;
import com.example.qrscaner.Model.QrMess;
import com.example.qrscaner.Model.QrProduct;
import com.example.qrscaner.Model.QrScan;
import com.example.qrscaner.Model.QrText;
import com.example.qrscaner.Model.QrUrl;
import com.example.qrscaner.Model.QrWifi;
import com.example.qrscaner.Model.QreTelephone;
import com.example.qrscaner.R;
import com.example.qrscaner.utils.BitMapUtils;
import com.example.qrscaner.utils.ShareUtils;
import com.example.qrscaner.view.fonts.TextViewPoppinBold;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;


public class QrGenerateResult extends ConstraintLayout implements View.OnClickListener {
    private QrScan mqrScan;
    private View mRootView;
    private Context mContext;
    private ImageView imvQrScanResultRender, imvQrScanResultIconCategory, imvQrScanResultBack;
    private TextView tvQrScanResultCategoryName, tvQrScanResultDate, tvQrScanResultCancel, tvQrScanResultShare;
    private ShowQrGenerate.BackToGenerate backToGenerate;

    private LinearLayout lnlResultInfo;
    private DrawView drawView;
    private QrScan.QRType type;
    private BitMapUtils bitMapUtils;


    public QrGenerateResult(@NonNull Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public QrGenerateResult(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();

    }


    private void initView() {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRootView = layoutInflater.inflate(R.layout.result_scan_layout, this, true);

        imvQrScanResultRender = mRootView.findViewById(R.id.imv_result_qr_render);
        imvQrScanResultIconCategory = mRootView.findViewById(R.id.imv_result_qr_category);
        tvQrScanResultCategoryName = mRootView.findViewById(R.id.tv_result_qr_category_name);
        tvQrScanResultDate = mRootView.findViewById(R.id.tv_save_date_create);
        lnlResultInfo = mRootView.findViewById(R.id.ll_result_qr_string);
        imvQrScanResultBack = mRootView.findViewById(R.id.imv_scanResult_back);
        tvQrScanResultCancel = mRootView.findViewById(R.id.tv_scanResult_cancel);
        tvQrScanResultShare = findViewById(R.id.tv_scanResult_save);
        imvQrScanResultBack.setOnClickListener(this);
        tvQrScanResultCancel.setOnClickListener(this);
        bitMapUtils = new BitMapUtils();
        tvQrScanResultShare.setOnClickListener(this);


        mRootView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        drawView = new DrawView(mContext);
        drawView.setBackgroundColor(Color.GRAY);
    }


    public void setupData(QrGenerate qrGenerate) {

        mqrScan = new QrScan();
        mqrScan.setScanText(qrGenerate.getContent());
        mqrScan.setTypeQR(qrGenerate.getQrType());
        mqrScan.setDate(qrGenerate.getDate());
        String s = mqrScan.getScanText();
        String[] content = s.split(":");
        if (content[0].equals("SMSTO")) {
            QrMess qrMess = new QrMess();
            qrMess.compileSMS(content);
            setContentMess(qrGenerate.getDate(), qrMess);
            imvQrScanResultRender.setImageBitmap(bitMapUtils.bitmapQR(QrScan.QRType.TEXT, qrGenerate.getContent(), qrGenerate.getColor()));


        } else if (content[0].equals("http") || content[0].equals("https")) {
            QrUrl qrUrl = new QrUrl();
            qrUrl.compileUrl(content);
            setContentUrl(qrGenerate.getDate(), qrUrl);
            imvQrScanResultRender.setImageBitmap(bitMapUtils.bitmapQR(QrScan.QRType.TEXT, qrGenerate.getContent(), qrGenerate.getColor()));
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
            setContentWifi(qrGenerate.getDate(), qrWifi);
            imvQrScanResultRender.setImageBitmap(bitMapUtils.bitmapQR(QrScan.QRType.TEXT, qrGenerate.getContent(), qrGenerate.getColor()));
        } else if (content[0].equals("MATMSG")) {
            QrEmail qrEmail = new QrEmail();
            StringBuilder stringBuilder = new StringBuilder();
            for (String value : content) {
                stringBuilder.append(value);
            }

            qrEmail.compileEmail(content);
            setContentMail(qrGenerate.getDate(), qrEmail);
            imvQrScanResultRender.setImageBitmap(bitMapUtils.bitmapQR(QrScan.QRType.TEXT, qrGenerate.getContent(), qrGenerate.getColor()));
        } else if (content[0].equals("tel")) {
            QreTelephone qreTelephone = new QreTelephone();
            qreTelephone.compile(content);
            setContentTel(qrGenerate.getDate(), qreTelephone);
            imvQrScanResultRender.setImageBitmap(bitMapUtils.bitmapQR(QrScan.QRType.TEXT, qrGenerate.getContent(), qrGenerate.getColor()));
        } else {
            if (checkIsProduct(qrGenerate.getContent())) {
                QrProduct qrProduct = new QrProduct();
                qrProduct.setProduct(Long.parseLong(qrGenerate.getContent()));
                setContentProduct(qrGenerate.getDate(), qrProduct);
                if (qrGenerate.getQrType() == QrScan.QRType.BAR39) {
                    imvQrScanResultRender.setImageBitmap(bitMapUtils.bitmapQR(QrScan.QRType.BAR39, qrGenerate.getContent(), qrGenerate.getColor()));
                } else if (qrGenerate.getQrType() == QrScan.QRType.BAR93) {
                    imvQrScanResultRender.setImageBitmap(bitMapUtils.bitmapQR(QrScan.QRType.BAR39, qrGenerate.getContent(), qrGenerate.getColor()));
                } else if (qrGenerate.getQrType() == QrScan.QRType.BAR128) {
                    imvQrScanResultRender.setImageBitmap(bitMapUtils.bitmapQR(QrScan.QRType.BAR128, qrGenerate.getContent(), qrGenerate.getColor()));
                }


            } else {
                imvQrScanResultRender.setImageBitmap(bitMapUtils.bitmapQR(QrScan.QRType.TEXT, qrGenerate.getContent(), qrGenerate.getColor()));
                QrText qrText = new QrText();
                qrText.setText(s);
                setContentText(qrGenerate.getDate(), qrText);

            }


        }

    }

    private void setContentProduct(long date, QrProduct qrProduct) {
        type = QrScan.QRType.PRODUCT;
        mqrScan.setTypeQR(type);
        lnlResultInfo.setOrientation(LinearLayout.VERTICAL);
        imvQrScanResultIconCategory.setImageResource(R.drawable.ic_product);
        String dateString = DateFormat.format("MM/dd/yyyy", new Date(date)).toString();
        tvQrScanResultDate.setText(dateString);
        tvQrScanResultCategoryName.setText("Product");
        LinearLayout linearLayoutText = new LinearLayout(mContext);
        TextViewPoppinBold tvNameCategoryText = new TextViewPoppinBold(mContext);
        tvNameCategoryText.setText("Product");
        tvNameCategoryText.setTextColor(Color.WHITE);
        tvNameCategoryText.setTextSize(13);
        TextView tvTextContent = new TextView(mContext);
        tvTextContent.setText(qrProduct.getProduct() + "");
        tvTextContent.setTextColor(Color.WHITE);
        tvTextContent.setTextSize(13);
        linearLayoutText.setOrientation(LinearLayout.VERTICAL);
        linearLayoutText.addView(tvNameCategoryText);
        linearLayoutText.addView(tvTextContent);
        lnlResultInfo.addView(linearLayoutText);
    }

    public boolean checkIsProduct(String qr) {
        long id;
        try {
            id = Long.parseLong(qr);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void setContentError() {
        type = QrScan.QRType.ERROR;
        mqrScan.setTypeQR(type);
        lnlResultInfo.setVisibility(GONE);
        imvQrScanResultIconCategory.setVisibility(GONE);
        imvQrScanResultRender.setImageResource(R.drawable.ic_error);
        tvQrScanResultDate.setText("########");
        tvQrScanResultCategoryName.setText("ERROR");

    }

    private void setContentText(long date, QrText qrText) {
        type = QrScan.QRType.TEXT;
        mqrScan.setTypeQR(type);
        lnlResultInfo.setOrientation(LinearLayout.VERTICAL);
        imvQrScanResultIconCategory.setImageResource(R.drawable.add_text);
        String dateString = DateFormat.format("MM/dd/yyyy", new Date(date)).toString();
        tvQrScanResultDate.setText(dateString);
        tvQrScanResultCategoryName.setText("Text");
        LinearLayout lnlText = new LinearLayout(mContext);
        TextViewPoppinBold tvNameCategoryText = new TextViewPoppinBold(mContext);
        tvNameCategoryText.setText("Text");
        tvNameCategoryText.setTextColor(Color.WHITE);
        tvNameCategoryText.setTextSize(13);
        TextView tvTextContent = new TextView(mContext);
        tvTextContent.setText(qrText.getText());
        tvTextContent.setTextColor(Color.WHITE);
        tvTextContent.setTextSize(13);
        lnlText.setOrientation(LinearLayout.VERTICAL);
        lnlText.addView(tvNameCategoryText);
        lnlText.addView(tvTextContent);
        lnlResultInfo.addView(lnlText);
    }

    private void setContentMess(long date, QrMess qrMess) {
        type = QrScan.QRType.SMS;
        mqrScan.setTypeQR(type);
        lnlResultInfo.setOrientation(LinearLayout.VERTICAL);
        imvQrScanResultIconCategory.setImageResource(R.drawable.add_sms);
        String dateString = DateFormat.format("MM/dd/yyyy", new Date(date)).toString();
        tvQrScanResultDate.setText(dateString);
        tvQrScanResultCategoryName.setText("Mess");
        LinearLayout linearLayoutPhone = new LinearLayout(mContext);
        TextViewPoppinBold tvNameCategory = new TextViewPoppinBold(mContext);
        tvNameCategory.setText("Phone      ");
        tvNameCategory.setTextColor(Color.WHITE);
        tvNameCategory.setTextSize(13);
        TextView tvPhoneNumber = new TextView(mContext);
        tvPhoneNumber.setText(qrMess.getSendBy());
        tvPhoneNumber.setTextColor(Color.WHITE);
        tvPhoneNumber.setTextSize(13);
        linearLayoutPhone.setOrientation(LinearLayout.HORIZONTAL);
        linearLayoutPhone.addView(tvNameCategory);
        linearLayoutPhone.addView(tvPhoneNumber);
        lnlResultInfo.addView(linearLayoutPhone);
//        lnlResultInfo.addView(drawView);
        LinearLayout lnlMess = new LinearLayout(mContext);
        TextViewPoppinBold tvNameCategoryMess = new TextViewPoppinBold(mContext);
        tvNameCategoryMess.setText("Body      ");
        tvNameCategoryMess.setTextColor(Color.WHITE);
        tvNameCategoryMess.setTextSize(13);
        TextView tvContent = new TextView(mContext);
        tvContent.setText(qrMess.getContent());
        tvContent.setTextColor(Color.WHITE);
        lnlMess.setOrientation(LinearLayout.VERTICAL);
        lnlMess.addView(tvNameCategoryMess);
        lnlMess.addView(tvContent);
        lnlResultInfo.addView(lnlMess);

    }

    private void setContentWifi(long date, QrWifi qrWifi) {
        type = QrScan.QRType.WIFI;
        mqrScan.setTypeQR(type);
        lnlResultInfo.setOrientation(LinearLayout.VERTICAL);
        imvQrScanResultIconCategory.setImageResource(R.drawable.add_wifi);
        String dateString = DateFormat.format("MM/dd/yyyy", new Date(date)).toString();
        tvQrScanResultDate.setText(dateString);
        tvQrScanResultCategoryName.setText("Wifi");
        LinearLayout lnlPhone = new LinearLayout(mContext);
        TextViewPoppinBold tvNameWifi = new TextViewPoppinBold(mContext);
        tvNameWifi.setText("NetWork:     ");
        tvNameWifi.setTextColor(Color.WHITE);
        tvNameWifi.setTextSize(13);
        TextView tvWifiName = new TextView(mContext);
        tvWifiName.setText(qrWifi.getWifiName());
        tvWifiName.setTextColor(Color.WHITE);
        tvWifiName.setTextSize(13);
        lnlPhone.setOrientation(LinearLayout.HORIZONTAL);
        lnlPhone.addView(tvNameWifi);
        lnlPhone.addView(tvWifiName);
        lnlResultInfo.addView(lnlPhone);

        LinearLayout lnlPass = new LinearLayout(mContext);
        TextViewPoppinBold tvNameCategoryPass = new TextViewPoppinBold(mContext);
        tvNameCategoryPass.setText("Password:   ");
        tvNameCategoryPass.setTextColor(Color.WHITE);
        tvNameCategoryPass.setTextSize(13);
        TextView tvPassContent = new TextView(mContext);
        tvPassContent.setText(qrWifi.getPass());
        tvPassContent.setTextColor(Color.WHITE);
        lnlPass.setOrientation(LinearLayout.HORIZONTAL);
        lnlPass.addView(tvNameCategoryPass);
        lnlPass.addView(tvPassContent);
        lnlResultInfo.addView(lnlPass);

        LinearLayout lnlType = new LinearLayout(mContext);
        TextViewPoppinBold tvNameCategoryType = new TextViewPoppinBold(mContext);
        tvNameCategoryType.setText("EAP:      ");
        tvNameCategoryType.setTextColor(Color.WHITE);
        tvNameCategoryType.setTextSize(13);
        TextView tvEAP = new TextView(mContext);
        tvEAP.setText(qrWifi.getType());
        tvEAP.setTextColor(Color.WHITE);
        lnlType.setOrientation(LinearLayout.HORIZONTAL);
        lnlType.addView(tvNameCategoryType);
        lnlType.addView(tvEAP);
        lnlResultInfo.addView(lnlType);

    }

    private void setContentMail(long date, QrEmail qrEmail) {
        type = QrScan.QRType.EMAIL;
        mqrScan.setTypeQR(type);
        imvQrScanResultIconCategory.setImageResource(R.drawable.add_email);
        String dateString = DateFormat.format("MM/dd/yyyy", new Date(date)).toString();
        tvQrScanResultDate.setText(dateString);
        tvQrScanResultCategoryName.setText("Email");
        LinearLayout linearLayoutEmail = new LinearLayout(mContext);
        TextViewPoppinBold tvNameCategoryEmail = new TextViewPoppinBold(mContext);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvNameCategoryEmail.setText("Email");
        }
        tvNameCategoryEmail.setTextColor(Color.WHITE);
        tvNameCategoryEmail.setTextSize(13);
        TextView tvEmailName = new TextView(mContext);
        tvEmailName.setText(qrEmail.getSendBy());
        tvEmailName.setTextColor(Color.WHITE);
        tvEmailName.setTextSize(13);
        linearLayoutEmail.setOrientation(LinearLayout.HORIZONTAL);
        linearLayoutEmail.addView(tvNameCategoryEmail);
        linearLayoutEmail.addView(tvEmailName);
        lnlResultInfo.addView(linearLayoutEmail);
        lnlResultInfo.addView(drawView);
        //pass
        LinearLayout lnlSub = new LinearLayout(mContext);
        TextViewPoppinBold tvNameCategorySub = new TextViewPoppinBold(mContext);
        tvNameCategorySub.setText("Subject:   ");
        tvNameCategorySub.setTextColor(Color.WHITE);
        tvNameCategorySub.setTextSize(13);
        TextView tvContent = new TextView(mContext);
        tvContent.setText(qrEmail.getSendTo());
        tvContent.setTextColor(Color.WHITE);
        lnlSub.setOrientation(LinearLayout.HORIZONTAL);
        lnlSub.addView(tvNameCategorySub);
        lnlSub.addView(tvContent);
        lnlResultInfo.addView(lnlSub);

//type
        LinearLayout lnlContent = new LinearLayout(mContext);
        TextViewPoppinBold tvNameCategoryContent = new TextViewPoppinBold(mContext);
        tvNameCategoryContent.setText("Content:   ");
        tvNameCategoryContent.setTextColor(Color.WHITE);
        tvNameCategoryContent.setTextSize(13);
        TextView tvContentEmail = new TextView(mContext);
        tvContentEmail.setText(qrEmail.getContent());
        tvContentEmail.setTextColor(Color.WHITE);
        lnlContent.setOrientation(LinearLayout.VERTICAL);
        lnlContent.addView(tvNameCategoryContent);
        lnlContent.addView(tvContentEmail);
        lnlResultInfo.addView(lnlContent);

    }

    private void setContentTel(long date, QreTelephone qreTelephone) {
        type = QrScan.QRType.PHONE;
        mqrScan.setTypeQR(type);
        imvQrScanResultIconCategory.setImageResource(R.drawable.add_call);
        // or you already have long value of date, use this instead of milliseconds variable.
        String dateString = DateFormat.format("MM/dd/yyyy", new Date(date)).toString();
        tvQrScanResultDate.setText(dateString);
        tvQrScanResultCategoryName.setText("Phone");
        TextViewPoppinBold tvNameCategory = new TextViewPoppinBold(mContext);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvNameCategory.setText("Phone       ");
        }
        tvNameCategory.setTextColor(Color.WHITE);
        tvNameCategory.setTextSize(13);
        TextView tvTel = new TextView(mContext);
        tvTel.setText(qreTelephone.getTel());
        tvTel.setTextColor(Color.WHITE);
        tvTel.setTextSize(13);
        lnlResultInfo.addView(tvNameCategory);
        lnlResultInfo.addView(tvTel);


    }


    private void setContentUrl(long date, QrUrl qrUrl) {
        type = QrScan.QRType.URL;
        mqrScan.setTypeQR(type);
        imvQrScanResultIconCategory.setImageResource(R.drawable.add_uri);
        String dateString = DateFormat.format("MM/dd/yyyy", new Date(date)).toString();
        tvQrScanResultDate.setText(dateString);
        tvQrScanResultCategoryName.setText("Uri");
        TextViewPoppinBold tvNameCategory = new TextViewPoppinBold(mContext);
        tvNameCategory.setText("Uri");
        tvNameCategory.setTextColor(Color.WHITE);
        tvNameCategory.setTextSize(13);
        TextView tvUrl = new TextView(mContext);
        tvUrl.setText(qrUrl.getUrl());
        tvUrl.setTextColor(Color.WHITE);
        tvUrl.setTextSize(13);
        lnlResultInfo.addView(tvNameCategory);
        lnlResultInfo.addView(tvUrl);


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imv_scanResult_back:
            case R.id.tv_scanResult_cancel:
                setVisibility(GONE);
                lnlResultInfo.removeAllViews();
                backToGenerate.onBackGenerate();
                break;
            case R.id.tv_scanResult_save:
                setVisibility(View.GONE);
                BitmapDrawable drawable = (BitmapDrawable) imvQrScanResultRender.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
//                saveImage(bitmap);
                ShareUtils.sharePalette(mContext,bitmap);
                imvQrScanResultBack.performClick();

                break;

        }

    }

    public interface iSaveQrScan {
        void saveQr(QrScan qrScan);
    }

    private iSaveQrScan saveQrScanListener;





    private void saveImage(Bitmap bitmap) {
        if (android.os.Build.VERSION.SDK_INT >= 29) {
            ContentValues values = contentValues();
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/" + mContext.getString(R.string.app_name));
            values.put(MediaStore.Images.Media.IS_PENDING, true);

            Uri uri = mContext.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            if (uri != null) {
                try {
                    saveImageToStream(bitmap, mContext.getContentResolver().openOutputStream(uri));
                    values.put(MediaStore.Images.Media.IS_PENDING, false);
                    mContext.getContentResolver().update(uri, values, null, null);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        } else {
            File directory = new File(Environment.getExternalStorageDirectory().toString() + '/' + mContext.getString(R.string.app_name));

            if (!directory.exists()) {
                directory.mkdirs();
            }
            String fileName = System.currentTimeMillis() + ".png";
            File file = new File(directory, fileName);
            try {
                saveImageToStream(bitmap, new FileOutputStream(file));
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
                mContext.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    private ContentValues contentValues() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        }
        return values;
    }

    private void saveImageToStream(Bitmap bitmap, OutputStream outputStream) {
        if (outputStream != null) {
            try {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
