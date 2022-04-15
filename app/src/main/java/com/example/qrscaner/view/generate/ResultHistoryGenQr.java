package com.example.qrscaner.view.generate;

import static android.content.Context.WIFI_SERVICE;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.qrscaner.utils.ShareUtils;
import com.example.qrscaner.view.fonts.TextViewPoppinBold;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ResultHistoryGenQr extends ConstraintLayout implements View.OnClickListener {
    private Context mContext;
    private View mRootView;
    private ImageView imvResultGenerateBack, imvResultHistoryCategory;
    private TextView tvResultHistoryCategoryQR, tvResultHistoryDateCreate, tvResultHistoryCategory, tvResultHistoryShare, tvResultHistoryOptionOne;
    private LinearLayout lnlResultHistoryContent;
    private BackToGenerate backToGenerate;
    private QrGenerate mQrGenerate;
    private QrScan qrScan;


    public ResultHistoryGenQr(@NonNull Context context) {
        super(context);
        mContext = context;
        init();
    }

    public ResultHistoryGenQr(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {

        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRootView = layoutInflater.inflate(R.layout.result_view_history, this, true);
        imvResultGenerateBack = mRootView.findViewById(R.id.imv_scan_result_history__back);
        imvResultHistoryCategory = mRootView.findViewById(R.id.imv_result_history_qr__category);
        tvResultHistoryCategory = mRootView.findViewById(R.id.tv_result_history__category);
        tvResultHistoryDateCreate = mRootView.findViewById(R.id.tv_result_history_qr__dateCreate);
        lnlResultHistoryContent = mRootView.findViewById(R.id.lnl_result_history__contentHistory);
        tvResultHistoryOptionOne = mRootView.findViewById(R.id.tv_scanResult_optionOne);
        tvResultHistoryShare = mRootView.findViewById(R.id.tv_scanResult_share);
        tvResultHistoryCategoryQR = mRootView.findViewById(R.id.tv_result_history_qr__categoryName);
        imvResultGenerateBack.setOnClickListener(this);
        tvResultHistoryShare.setOnClickListener(this);
        tvResultHistoryOptionOne.setOnClickListener(this);

    }

    public void setupData(QrGenerate qrGenerate, BackToGenerate backToGenerate) {
        qrScan = new QrScan();
        this.backToGenerate = backToGenerate;
        mQrGenerate = qrGenerate;
        String qrContent = qrGenerate.getContent();
        String[] content = qrContent.split(":");
        if (checkIsProduct(content[0])) {

            if (qrGenerate.getQrType() == QrScan.QRType.BAR39) {
                QrProduct qrProduct = new QrProduct();

                qrProduct.setProduct(Long.parseLong(qrGenerate.getContent()));
                setContentProduct(qrScan.getDate(), qrProduct);
            }


        } else {
            qrScan.setScanText(qrGenerate.getContent());
            qrScan.setDate(qrGenerate.getDate());


            if (content[0].equals("SMSTO")) {
                QrMess qrMess = new QrMess();
                qrMess.compileSMS(content);

                setContentMess(qrScan.getDate(), qrMess);

            } else if (content[0].equals("http") || content[0].equals("https")) {
                QrUrl qrUrl = new QrUrl();
                qrUrl.compileUrl(content);
                setContentUrl(qrScan.getDate(), qrUrl);
            } else if (content[0].equals("WIFI")) {
                StringBuilder stringBuilder = new StringBuilder();
                String[] contentWifi = qrContent.split(";");
                for (String value : contentWifi) {
                    stringBuilder.append(value);
                }
                String contentWifi2 = stringBuilder.toString();
                String[] contentWifi3 = contentWifi2.split(":");
                QrWifi qrWifi = new QrWifi();
                qrWifi.compileWifi(contentWifi, contentWifi3);
                setContentWifi(qrScan.getDate(), qrWifi);
            } else if (content[0].equals("MATMSG")) {
                QrEmail qrEmail = new QrEmail();
                StringBuilder stringBuilder = new StringBuilder();
                for (String value : content) {
                    stringBuilder.append(value);
                }
                qrEmail.compileEmail(content);
                setContentMail(qrScan.getDate(), qrEmail);

            } else if (content[0].equals("tel")) {
                QreTelephone qreTelephone = new QreTelephone();
                qreTelephone.compile(content);
                setContentTel(qrScan.getDate(), qreTelephone);

            } else {
                QrText qrText = new QrText();
                qrText.setText(qrContent);
                setContentText(qrScan.getDate(), qrText);
            }

        }

    }

    private void setContentProduct(long date, QrProduct qrProduct) {
        {
            lnlResultHistoryContent.setOrientation(LinearLayout.VERTICAL);
            imvResultHistoryCategory.setImageResource(R.drawable.ic_product);
            String dateString = DateFormat.format("MM/dd/yyyy", new Date(date)).toString();
            tvResultHistoryDateCreate.setText(dateString);
            tvResultHistoryCategory.setText("QR CODE");
            tvResultHistoryCategoryQR.setText("Product");

            LinearLayout lnlContent = new LinearLayout(mContext);
            lnlContent.setOrientation(LinearLayout.VERTICAL);
            TextViewPoppinBold tvContent = new TextViewPoppinBold(mContext);
            TextView tvContentText = new TextView(mContext);
            tvContent.setText("Number:");
            tvContentText.setText("    " + qrProduct.getProduct());
            tvContentText.setBackgroundResource(R.drawable.corner_tv);
            lnlContent.addView(tvContent);
            lnlContent.addView(tvContentText);
            lnlResultHistoryContent.addView(lnlContent);

        }
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

    private void setContentUrl(long date, QrUrl qrUrl) {
        {

            lnlResultHistoryContent.setOrientation(LinearLayout.VERTICAL);
            imvResultHistoryCategory.setImageResource(R.drawable.add_text);
            String dateString = DateFormat.format("MM/dd/yyyy", new Date(date)).toString();
            tvResultHistoryDateCreate.setText(dateString);
            tvResultHistoryCategory.setText("QR CODE");
            tvResultHistoryCategoryQR.setText("Uri");

            LinearLayout lnlEmail = new LinearLayout(mContext);
            lnlEmail.setOrientation(LinearLayout.VERTICAL);
            TextViewPoppinBold tvWifiCategoryName = new TextViewPoppinBold(mContext);
            TextView tvEmailName = new TextView(mContext);
            tvWifiCategoryName.setText("Uri: ");
            tvEmailName.setText("    " + qrUrl.getUrl());
            tvEmailName.setBackgroundResource(R.drawable.corner_tv);
            tvEmailName.setMaxLines(1);
            tvEmailName.setEllipsize(TextUtils.TruncateAt.END);
            lnlEmail.addView(tvWifiCategoryName);
            lnlEmail.addView(tvEmailName);
            lnlResultHistoryContent.addView(lnlEmail);


        }
    }

    private void setContentMail(long date, QrEmail qrEmail) {
        {
            lnlResultHistoryContent.setOrientation(LinearLayout.VERTICAL);
            imvResultHistoryCategory.setImageResource(R.drawable.add_email);
            String dateString = DateFormat.format("MM/dd/yyyy", new Date(date)).toString();
            tvResultHistoryDateCreate.setText(dateString);
            tvResultHistoryCategory.setText("QR CODE");
            tvResultHistoryCategoryQR.setText("Email");

            LinearLayout lnlEmail = new LinearLayout(mContext);
            lnlEmail.setOrientation(LinearLayout.HORIZONTAL);
            TextViewPoppinBold tvWifiCategoryName = new TextViewPoppinBold(mContext);
            TextView tvEmailName = new TextView(mContext);
            tvWifiCategoryName.setText("Email:");
            tvEmailName.setText("    " + qrEmail.getSendBy());
            tvEmailName.setMaxLines(1);
            tvEmailName.setEllipsize(TextUtils.TruncateAt.END);
            lnlEmail.addView(tvWifiCategoryName);
            lnlEmail.addView(tvEmailName);
            lnlResultHistoryContent.addView(lnlEmail);

            LinearLayout lnlSendTo = new LinearLayout(mContext);
            lnlSendTo.setOrientation(LinearLayout.HORIZONTAL);
            TextViewPoppinBold tvEmailSenTo = new TextViewPoppinBold(mContext);
            TextView tvEmailNameSendTo = new TextView(mContext);
            tvEmailSenTo.setText("Subject:");
            tvEmailNameSendTo.setText("    " + qrEmail.getSendTo());
            tvEmailNameSendTo.setMaxLines(1);
            tvEmailNameSendTo.setEllipsize(TextUtils.TruncateAt.END);
            lnlSendTo.addView(tvEmailSenTo);
            lnlSendTo.addView(tvEmailNameSendTo);
            lnlResultHistoryContent.addView(lnlSendTo);

            LinearLayout lnlContent = new LinearLayout(mContext);
            lnlContent.setOrientation(LinearLayout.VERTICAL);
            TextViewPoppinBold tvContent = new TextViewPoppinBold(mContext);
            TextView tvContentText = new TextView(mContext);
            tvContent.setText("Content:");
            tvContentText.setText(qrEmail.getContent());
            lnlContent.addView(tvContent);
            lnlContent.addView(tvContentText);
            lnlResultHistoryContent.addView(lnlContent);


        }
    }

    private void setContentTel(long date, QreTelephone qreTelephone) {
        {
            lnlResultHistoryContent.setOrientation(LinearLayout.VERTICAL);
            imvResultHistoryCategory.setImageResource(R.drawable.add_call);
            String dateString = DateFormat.format("MM/dd/yyyy", new Date(date)).toString();
            tvResultHistoryDateCreate.setText(dateString);
            tvResultHistoryCategory.setText("QR CODE");
            tvResultHistoryCategoryQR.setText("Phone");

            LinearLayout lnlContent = new LinearLayout(mContext);
            lnlContent.setOrientation(LinearLayout.HORIZONTAL);
            TextViewPoppinBold tvContent = new TextViewPoppinBold(mContext);
            TextView tvContentText = new TextView(mContext);
            tvContent.setText("Number:");
            tvContentText.setText("    " + qreTelephone.getTel());
            lnlContent.addView(tvContent);
            lnlContent.addView(tvContentText);
            lnlResultHistoryContent.addView(lnlContent);
            tvResultHistoryOptionOne.setText("Call");
            tvResultHistoryOptionOne.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse(qrScan.getScanText()));
                    mContext.startActivity(intent);
                }
            });
        }
    }

    private void setContentWifi(long date, QrWifi qrWifi) {
        lnlResultHistoryContent.setOrientation(LinearLayout.VERTICAL);
        imvResultHistoryCategory.setImageResource(R.drawable.add_wifi);
        String dateString = DateFormat.format("MM/dd/yyyy", new Date(date)).toString();
        tvResultHistoryDateCreate.setText(dateString);
        tvResultHistoryCategory.setText("QR CODE");
        tvResultHistoryCategoryQR.setText("WIFI");
        LinearLayout lnlNetWork = new LinearLayout(mContext);
        lnlNetWork.setOrientation(LinearLayout.HORIZONTAL);
        TextViewPoppinBold tvWifiCategoryName = new TextViewPoppinBold(mContext);
        TextView tvWifiName = new TextView(mContext);
        tvWifiCategoryName.setText("Network:");
        tvWifiName.setText("    " + qrWifi.getWifiName());
        lnlNetWork.addView(tvWifiCategoryName);
        lnlNetWork.addView(tvWifiName);
        lnlResultHistoryContent.addView(lnlNetWork);

        LinearLayout lnlWifiPass = new LinearLayout(mContext);
        lnlWifiPass.setOrientation(LinearLayout.HORIZONTAL);
        TextViewPoppinBold tvWifiCategoryPass = new TextViewPoppinBold(mContext);
        TextView tvWifiPass = new TextView(mContext);
        tvWifiCategoryPass.setText("Pass:");
        tvWifiPass.setText("    " + qrWifi.getPass());
        lnlWifiPass.addView(tvWifiCategoryPass);
        lnlWifiPass.addView(tvWifiPass);
        lnlResultHistoryContent.addView(lnlWifiPass);


        LinearLayout lnlWifiEAP = new LinearLayout(mContext);
        lnlWifiEAP.setOrientation(LinearLayout.HORIZONTAL);
        TextViewPoppinBold tvWifiCategoryEAP = new TextViewPoppinBold(mContext);
        TextView tvWifiEAP = new TextView(mContext);
        tvWifiCategoryEAP.setText("EAP:");
        tvWifiEAP.setText("    " + qrWifi.getType());
        lnlWifiEAP.addView(tvWifiCategoryEAP);
        lnlWifiEAP.addView(tvWifiEAP);
        lnlResultHistoryContent.addView(lnlWifiEAP);


        tvResultHistoryOptionOne.setText("Connect to \n Network");

    }


    private void setContentText(long date, QrText qrText) {
        lnlResultHistoryContent.setOrientation(LinearLayout.VERTICAL);
        imvResultHistoryCategory.setImageResource(R.drawable.add_text);
        String dateString = DateFormat.format("MM/dd/yyyy", new Date(date)).toString();
        tvResultHistoryDateCreate.setText(dateString);
        tvResultHistoryCategory.setText("QR CODE");
        tvResultHistoryCategoryQR.setText("Text");

        LinearLayout lnlContent = new LinearLayout(mContext);
        lnlContent.setOrientation(LinearLayout.VERTICAL);
        TextViewPoppinBold tvContent = new TextViewPoppinBold(mContext);
        TextView tvContentText = new TextView(mContext);
        tvContent.setText("Text");
        tvContentText.setText(qrText.getText());
        tvContentText.setMaxLines(5);
        tvContent.setEllipsize(TextUtils.TruncateAt.END);

        lnlContent.addView(tvContent);
        lnlContent.addView(tvContentText);
        lnlResultHistoryContent.addView(lnlContent);
        tvResultHistoryOptionOne.setText("Copy");
    }

    private void setContentMess(long date, QrMess qrMess) {
        lnlResultHistoryContent.setOrientation(LinearLayout.VERTICAL);
        imvResultHistoryCategory.setImageResource(R.drawable.add_sms);
        String dateString = DateFormat.format("MM/dd/yyyy", new Date(date)).toString();
        tvResultHistoryDateCreate.setText(dateString);
        tvResultHistoryCategory.setText("QR CODE");
        tvResultHistoryCategoryQR.setText("SMS");

        LinearLayout lnlEmail = new LinearLayout(mContext);
        lnlEmail.setOrientation(LinearLayout.HORIZONTAL);
        TextViewPoppinBold tvWifiCategoryName = new TextViewPoppinBold(mContext);
        TextView tvEmailName = new TextView(mContext);
        tvWifiCategoryName.setText("Phone: ");
        tvEmailName.setText("    " + qrMess.getSendBy());
        tvEmailName.setMaxLines(1);
        tvEmailName.setEllipsize(TextUtils.TruncateAt.END);
        lnlEmail.addView(tvWifiCategoryName);
        lnlEmail.addView(tvEmailName);
        lnlResultHistoryContent.addView(lnlEmail);

        LinearLayout lnlSub = new LinearLayout(mContext);
        lnlSub.setOrientation(LinearLayout.HORIZONTAL);
        TextViewPoppinBold tvSMSSub = new TextViewPoppinBold(mContext);
        TextView tvSubContent = new TextView(mContext);
        tvSMSSub.setText("Content: ");
        tvSubContent.setText("    " + qrMess.getContent());
        tvSubContent.setMaxLines(1);
        tvSubContent.setEllipsize(TextUtils.TruncateAt.END);
        lnlSub.addView(tvSMSSub);
        lnlSub.addView(tvSubContent);
        lnlResultHistoryContent.addView(lnlSub);
        tvResultHistoryOptionOne.setText("Send Mess");
    }


    @Override
    public void onClick(View view) {
        if (view == imvResultGenerateBack) {
            backToGenerate.onBackGenerate();
            lnlResultHistoryContent.removeAllViews();
        } else if (view == tvResultHistoryShare) {
            ShareUtils.shareGenQR(mContext, mQrGenerate);
        } else if (view == tvResultHistoryOptionOne) {
            if (mQrGenerate.getQrType() == QrScan.QRType.PHONE) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(mQrGenerate.getContent()));
                mContext.startActivity(intent);
            } else if (mQrGenerate.getQrType() == QrScan.QRType.TEXT) {

                ClipData clipData = ClipData.newPlainText("text", mQrGenerate.getContent());

                ClipboardManager clipboardManager = null;

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
                String[] url = qrUrl.getScanText().split(":");
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
            }
        }




    }
    public interface BackToGenerate {
        void onBackGenerate();
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
