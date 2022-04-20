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

import com.example.qrscaner.DataBase.QrHistoryDatabase;
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
import com.example.qrscaner.activity.MainActivity;
import com.example.qrscaner.fragment.ScannerFragment;
import com.example.qrscaner.utils.IntentUtils;
import com.example.qrscaner.utils.ShareUtils;
import com.example.qrscaner.view.ResultHistoryQr;
import com.example.qrscaner.view.fonts.TextViewPoppinBold;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ResultScanQr extends ConstraintLayout implements View.OnClickListener {
    private Context mContext;
    private View mRootView;
    private ImageView imvResultGenerateBack, imvResultHistoryCategory;
    private TextView tvResultHistoryCategoryQR, tvResultHistoryDateCreate, tvResultHistoryCategory, tvResultHistoryShare, tvResultHistoryOptionOne;
    private LinearLayout lnlResultHistoryContent;
    private BackToScan backToScan;
    private QrGenerate mQrGenerate;
    private QrScan qrScan;
    private String[] content;
    private String qrContent;
    private QrScan.QRType type;
    private MainActivity mMainActivity;

    public ResultScanQr(@NonNull Context context) {
        super(context);
        mContext = context;
        init();
    }

    public ResultScanQr(@NonNull Context context, @Nullable AttributeSet attrs) {
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

    public void setupData(QrScan qrScan, BackToScan backToScan) {

        this.backToScan = backToScan;
        this.qrScan = qrScan;
        qrContent = qrScan.getScanText();
        content = qrScan.getScanText().split(":");
        if (checkIsProduct(content[0])) {
            setContentProduct(qrScan.getDate(), qrScan);
            type = QrScan.QRType.PRODUCT;
            qrScan.setTypeQR(type);

        } else {

            if (content[0].equals("SMSTO")) {
                QrMess qrMess = new QrMess();
                qrMess.compileSMS(content);

                setContentMess(qrScan.getDate(), qrMess);
                type = QrScan.QRType.SMS;
                qrScan.setTypeQR(type);

            } else if (content[0].equals("http") || content[0].equals("https")) {
                QrUrl qrUrl = new QrUrl();
                qrUrl.compileUrl(content);
                type = QrScan.QRType.URL;
                qrScan.setTypeQR(type);
                setContentUrl(qrScan.getDate(), qrUrl);

            } else if (content[0].equals("WIFI")) {
                type = QrScan.QRType.WIFI;
                qrScan.setTypeQR(type);
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
                type = QrScan.QRType.EMAIL;
                qrScan.setTypeQR(type);
                QrEmail qrEmail = new QrEmail();
                StringBuilder stringBuilder = new StringBuilder();
                for (String value : content) {
                    stringBuilder.append(value);
                }
                qrEmail.compileEmail(content);
                setContentMail(qrScan.getDate(), qrEmail);

            } else if (content[0].equals("tel")) {
                type = QrScan.QRType.PHONE;
                qrScan.setTypeQR(type);
                QreTelephone qreTelephone = new QreTelephone();
                qreTelephone.compile(content);
                setContentTel(qrScan.getDate(), qreTelephone);

            } else {
                type = QrScan.QRType.TEXT;
                qrScan.setTypeQR(type);
                QrText qrText = new QrText();
                qrText.setText(qrContent);
                setContentText(qrScan.getDate(), qrText);
            }

        }
        QrHistoryDatabase.getInstance(mContext).qrDao().insertQr(new QrScan(type,qrContent,System.currentTimeMillis()));

    }

    private void setContentProduct(long date, QrScan qrProduct) {
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
            tvContentText.setText("    " + qrProduct.getScanText());
            tvContentText.setBackgroundResource(R.drawable.corner_tv);
            lnlContent.addView(tvContent);
            lnlContent.addView(tvContentText);
            lnlResultHistoryContent.addView(lnlContent);
            tvResultHistoryOptionOne.setText("Open Browser");

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
            tvResultHistoryOptionOne.setText("Open Browser");

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
            tvResultHistoryOptionOne.setText("Send Email");

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
            backToScan.onBackScan();
            lnlResultHistoryContent.removeAllViews();
        } else if (view == tvResultHistoryShare) {
            ShareUtils.shareGenQR(mContext, mQrGenerate);
        } else if (view == tvResultHistoryOptionOne) {
            IntentUtils intentUtils = new IntentUtils();
            intentUtils.IntentAction(mContext, qrContent, type);
        }


    }

    public interface BackToScan {
        void onBackScan();
    }

    public interface BackToGenerate {
        void onBackGenerate();
    }

}
