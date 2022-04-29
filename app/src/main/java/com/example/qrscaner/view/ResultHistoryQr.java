package com.example.qrscaner.view;

import static com.example.qrscaner.view.ShowResultScanQR.resizeImage;

import android.content.Context;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.qrscaner.models.QrEmail;
import com.example.qrscaner.models.QrMess;
import com.example.qrscaner.models.QrProduct;
import com.example.qrscaner.models.QrScan;
import com.example.qrscaner.models.QrText;
import com.example.qrscaner.models.QrUrl;
import com.example.qrscaner.models.QrWifi;
import com.example.qrscaner.models.QreTelephone;
import com.example.qrscaner.R;
import com.example.qrscaner.activity.MainActivity;
import com.example.qrscaner.utils.IntentUtils;
import com.example.qrscaner.view.fonts.TextViewPoppinBold;

import java.util.Date;

public class ResultHistoryQr extends ConstraintLayout implements View.OnClickListener {
    private Context mContext;
    private View mRootView;
    private ImageView imvResultHistoryBack, imvResultHistoryCategory;
    private ImageView imvShowHistoryItemBackGround;
    private TextView tvResultHistoryCategoryQR, tvResultHistoryDateCreate, tvResultHistoryCategory,
    tvResultHistoryShare, tvResultHistoryOptionOne;
    private LinearLayout lnlResultHistoryContent, lnlResultHistoryOption;
    private QrScan mqrScan;
    private MainActivity mainActivity;


    public ResultHistoryQr(@NonNull Context context) {
        super(context);
        mContext = context;
        init();
    }

    public ResultHistoryQr(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        init();
    }

    private void init() {

        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRootView = layoutInflater.inflate(R.layout.result_view_history, this, true);
        lnlResultHistoryOption = mRootView.findViewById(R.id.lnl_resultScan__action);

        imvResultHistoryBack = mRootView.findViewById(R.id.imv_scan_result_history__back);
        imvShowHistoryItemBackGround = mRootView.findViewById(R.id.imv_result_history_alert);
        resizeImage(imvShowHistoryItemBackGround, 288, 366);
        imvResultHistoryCategory = mRootView.findViewById(R.id.imv_result_history_qr__category);
        tvResultHistoryCategory = mRootView.findViewById(R.id.tv_result_history__category);
        tvResultHistoryDateCreate = mRootView.findViewById(R.id.tv_result_history_qr__dateCreate);
        lnlResultHistoryContent = mRootView.findViewById(R.id.lnl_result_history__contentHistory);
        tvResultHistoryOptionOne = mRootView.findViewById(R.id.tv_scanResult_optionOne);
        tvResultHistoryShare = mRootView.findViewById(R.id.tv_scanResult_share);
        tvResultHistoryCategoryQR = mRootView.findViewById(R.id.tv_result_history_qr__categoryName);
        lnlResultHistoryOption.getLayoutParams().width = imvShowHistoryItemBackGround.getWidth();
        imvResultHistoryBack.setOnClickListener(this);
        tvResultHistoryOptionOne.setOnClickListener(this);

    }


    public void setupData(QrScan qrScan, Context context) {
        mqrScan = qrScan;
        this.mainActivity = (MainActivity) context;

        String qrContent = qrScan.getScanText();
        String[] content = qrContent.split(":");


        switch (content[0]) {
            case "SMSTO":
                QrMess qrMess = new QrMess();
                qrMess.compileSMS(content);

                setContentMess(qrScan.getDate(), qrMess);

                break;
            case "Error":
                setContentError();


                break;
            case "http":
            case "https":
                QrUrl qrUrl = new QrUrl();
                qrUrl.compileUrl(content);
                setContentUrl(qrScan.getDate(), qrUrl);
                break;
            case "WIFI": {
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
                break;
            }
            case "MATMSG": {
                QrEmail qrEmail = new QrEmail();
                StringBuilder stringBuilder = new StringBuilder();
                for (String value : content) {
                    stringBuilder.append(value);
                }
                qrEmail.compileEmail(content);
                setContentMail(qrScan.getDate(), qrEmail);

                break;
            }
            case "tel":
                QreTelephone qreTelephone = new QreTelephone();
                qreTelephone.compile(content);
                setContentTel(qrScan.getDate(), qreTelephone);

                break;
            default:
                if (checkIsProduct(qrScan.getScanText())) {
                    QrProduct qrProduct = new QrProduct();
                    qrProduct.setProduct(Long.parseLong(qrScan.getScanText()));
                    setContentProduct(qrScan.getDate(), qrProduct);
                } else {
                    QrText qrText = new QrText();
                    qrText.setText(qrContent);
                    setContentText(qrScan.getDate(), qrText);
                }


                break;
        }

    }

    private void setContentError() {
    }

    private void setContentWifi(long date, QrWifi qrWifi) {
        lnlResultHistoryContent.setOrientation(LinearLayout.VERTICAL);
        imvResultHistoryCategory.setImageResource(R.drawable.ic_add_wifi);
        String dateString = DateFormat.format("MM/dd/yyyy", new Date(date)).toString();
        tvResultHistoryDateCreate.setText(dateString);
        tvResultHistoryCategory.setText(R.string.qr_code_title);
        tvResultHistoryCategoryQR.setText(R.string.wifi);
        LinearLayout lnlNetWork = new LinearLayout(mContext);
        lnlNetWork.setOrientation(LinearLayout.HORIZONTAL);
        TextViewPoppinBold tvWifiCategoryName = new TextViewPoppinBold(mContext);
        TextView tvWifiName = new TextView(mContext);
        tvWifiCategoryName.setText(R.string.network);
        tvWifiName.setText(qrWifi.getWifiName());
        lnlNetWork.addView(tvWifiCategoryName);
        lnlNetWork.addView(tvWifiName);
        lnlResultHistoryContent.addView(lnlNetWork);

        LinearLayout lnlWifiPass = new LinearLayout(mContext);
        lnlWifiPass.setOrientation(LinearLayout.HORIZONTAL);
        TextViewPoppinBold tvWifiCategoryPass = new TextViewPoppinBold(mContext);
        TextView tvWifiPass = new TextView(mContext);
        tvWifiCategoryPass.setText(R.string.pass);
        tvWifiPass.setText(qrWifi.getPass());
        lnlWifiPass.addView(tvWifiCategoryPass);
        lnlWifiPass.addView(tvWifiPass);
        lnlResultHistoryContent.addView(lnlWifiPass);


        LinearLayout lnlWifiEAP = new LinearLayout(mContext);
        lnlWifiEAP.setOrientation(LinearLayout.HORIZONTAL);
        TextViewPoppinBold tvWifiCategoryEAP = new TextViewPoppinBold(mContext);
        TextView tvWifiEAP = new TextView(mContext);
        tvWifiCategoryEAP.setText(R.string.epa);
        tvWifiEAP.setText(qrWifi.getType());
        lnlWifiEAP.addView(tvWifiCategoryEAP);
        lnlWifiEAP.addView(tvWifiEAP);
        lnlResultHistoryContent.addView(lnlWifiEAP);
        tvResultHistoryOptionOne.setText("Connect to \n Network");


    }

    private void setContentMail(long date, QrEmail qrEmail) {
        lnlResultHistoryContent.setOrientation(LinearLayout.VERTICAL);
        imvResultHistoryCategory.setImageResource(R.drawable.ic_add_email);
        String dateString = DateFormat.format("MM/dd/yyyy", new Date(date)).toString();
        tvResultHistoryDateCreate.setText(dateString);
        tvResultHistoryCategory.setText(R.string.qr_code_title);
        tvResultHistoryCategoryQR.setText(R.string.email);

        LinearLayout lnlEmail = new LinearLayout(mContext);
        lnlEmail.setOrientation(LinearLayout.HORIZONTAL);
        TextViewPoppinBold tvWifiCategoryName = new TextViewPoppinBold(mContext);
        TextView tvEmailName = new TextView(mContext);
        tvWifiCategoryName.setText(R.string.email);
        tvEmailName.setText(qrEmail.getSendBy());
        tvEmailName.setMaxLines(1);
        tvEmailName.setEllipsize(TextUtils.TruncateAt.END);
        lnlEmail.addView(tvWifiCategoryName);
        lnlEmail.addView(tvEmailName);
        lnlResultHistoryContent.addView(lnlEmail);

        LinearLayout lnlSendTo = new LinearLayout(mContext);
        lnlSendTo.setOrientation(LinearLayout.HORIZONTAL);
        TextViewPoppinBold tvEmailSenTo = new TextViewPoppinBold(mContext);
        TextView tvEmailNameSendTo = new TextView(mContext);
        tvEmailSenTo.setText(R.string.subject);
        tvEmailNameSendTo.setText(qrEmail.getSendTo());
        tvEmailNameSendTo.setMaxLines(1);
        tvEmailNameSendTo.setEllipsize(TextUtils.TruncateAt.END);
        lnlSendTo.addView(tvEmailSenTo);
        lnlSendTo.addView(tvEmailNameSendTo);
        lnlResultHistoryContent.addView(lnlSendTo);

        LinearLayout lnlContent = new LinearLayout(mContext);
        lnlContent.setOrientation(LinearLayout.VERTICAL);
        TextViewPoppinBold tvContent = new TextViewPoppinBold(mContext);
        TextView tvContentText = new TextView(mContext);
        tvContent.setText(R.string.content);
        tvContentText.setText(qrEmail.getContent());
        lnlContent.addView(tvContent);
        lnlContent.addView(tvContentText);
        lnlResultHistoryContent.addView(lnlContent);
        tvResultHistoryOptionOne.setText(R.string.send_email);

    }

    private void setContentTel(long date, QreTelephone qreTelephone) {
        lnlResultHistoryContent.setOrientation(LinearLayout.VERTICAL);
        imvResultHistoryCategory.setImageResource(R.drawable.ic_add_call);
        String dateString = DateFormat.format("MM/dd/yyyy", new Date(date)).toString();
        tvResultHistoryDateCreate.setText(dateString);
        tvResultHistoryCategory.setText(R.string.qr_code_title);
        tvResultHistoryCategoryQR.setText(R.string.phone);

        LinearLayout lnlContent = new LinearLayout(mContext);
        lnlContent.setOrientation(LinearLayout.HORIZONTAL);
        TextViewPoppinBold tvContent = new TextViewPoppinBold(mContext);
        TextView tvContentText = new TextView(mContext);
        tvContent.setText(R.string.phone_number);
        tvContentText.setText(qreTelephone.getTel());
        lnlContent.addView(tvContent);
        lnlContent.addView(tvContentText);

        lnlResultHistoryContent.addView(lnlContent);
        tvResultHistoryOptionOne.setText(R.string.call);

    }

    private void setContentProduct(long date, QrProduct qrProduct) {
        lnlResultHistoryContent.setOrientation(LinearLayout.VERTICAL);
        imvResultHistoryCategory.setImageResource(R.drawable.ic_product);
        String dateString = DateFormat.format("MM/dd/yyyy", new Date(date)).toString();
        tvResultHistoryDateCreate.setText(dateString);
        tvResultHistoryCategory.setText(R.string.qr_code_title);
        tvResultHistoryCategoryQR.setText(R.string.product);

        LinearLayout lnlContent = new LinearLayout(mContext);
        lnlContent.setOrientation(LinearLayout.VERTICAL);
        TextViewPoppinBold tvContent = new TextViewPoppinBold(mContext);
        TextView tvContentText = new TextView(mContext);
        tvContent.setText(R.string.number);
        tvContentText.setText(String.valueOf(qrProduct.getProduct()));
        tvContentText.setBackgroundResource(R.drawable.corner_tv);
        lnlContent.addView(tvContent);
        lnlContent.addView(tvContentText);
        lnlResultHistoryContent.addView(lnlContent);
        tvResultHistoryOptionOne.setText(R.string.search);

    }

    private void setContentText(long date, QrText qrText) {
        lnlResultHistoryContent.setOrientation(LinearLayout.VERTICAL);
        imvResultHistoryCategory.setImageResource(R.drawable.ic_add_text);
        String dateString = DateFormat.format("MM/dd/yyyy", new Date(date)).toString();
        tvResultHistoryDateCreate.setText(dateString);
        tvResultHistoryCategory.setText(R.string.qr_code_title);
        tvResultHistoryCategoryQR.setText(R.string.text);

        LinearLayout lnlContent = new LinearLayout(mContext);
        lnlContent.setOrientation(LinearLayout.VERTICAL);
        TextViewPoppinBold tvContent = new TextViewPoppinBold(mContext);
        TextView tvContentText = new TextView(mContext);
        tvContent.setText(R.string.text);
        tvContentText.setText(qrText.getText());
        tvContentText.setMaxLines(5);
        tvContent.setEllipsize(TextUtils.TruncateAt.END);
        lnlContent.addView(tvContent);
        lnlContent.addView(tvContentText);
        TextView tvMore = new TextView(mContext);
        tvMore.setText(R.string.more);
        tvMore.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down, 0);
        tvMore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                tvContentText.setMaxLines(10);

                tvContentText.setText(qrText.getText());
            }
        });
        lnlContent.addView(tvMore);
        lnlResultHistoryContent.addView(lnlContent);
        tvResultHistoryOptionOne.setText(R.string.copy);

    }

    private void setContentMess(long date, QrMess qrMess) {
        lnlResultHistoryContent.setOrientation(LinearLayout.VERTICAL);
        imvResultHistoryCategory.setImageResource(R.drawable.ic_add_sms);
        String dateString = DateFormat.format("MM/dd/yyyy", new Date(date)).toString();
        tvResultHistoryDateCreate.setText(dateString);
        tvResultHistoryCategory.setText(R.string.qr_code_title);
        tvResultHistoryCategoryQR.setText(R.string.sms);

        LinearLayout lnlEmail = new LinearLayout(mContext);
        lnlEmail.setOrientation(LinearLayout.HORIZONTAL);
        TextViewPoppinBold tvWifiCategoryName = new TextViewPoppinBold(mContext);
        TextView tvEmailName = new TextView(mContext);
        tvWifiCategoryName.setText(R.string.phone_number);
        tvEmailName.setText(qrMess.getSendBy());
        tvEmailName.setMaxLines(1);
        tvEmailName.setEllipsize(TextUtils.TruncateAt.END);
        lnlEmail.addView(tvWifiCategoryName);
        lnlEmail.addView(tvEmailName);
        lnlResultHistoryContent.addView(lnlEmail);

        LinearLayout lnlSub = new LinearLayout(mContext);
        lnlSub.setOrientation(LinearLayout.HORIZONTAL);
        TextViewPoppinBold tvSMSSub = new TextViewPoppinBold(mContext);
        TextView tvSubContent = new TextView(mContext);
        tvSMSSub.setText(R.string.content);
        tvSubContent.setText(qrMess.getContent());
        tvSubContent.setMaxLines(1);
        tvSubContent.setEllipsize(TextUtils.TruncateAt.END);
        lnlSub.addView(tvSMSSub);
        lnlSub.addView(tvSubContent);
        lnlResultHistoryContent.addView(lnlSub);
        tvResultHistoryOptionOne.setText(R.string.send_mess);


    }

    private void setContentUrl(long date, QrUrl qrUrl) {
        lnlResultHistoryContent.setOrientation(LinearLayout.VERTICAL);
        imvResultHistoryCategory.setImageResource(R.drawable.ic_add_text);
        String dateString = DateFormat.format("MM/dd/yyyy", new Date(date)).toString();
        tvResultHistoryDateCreate.setText(dateString);
        tvResultHistoryCategory.setText(R.string.qr_code_title);
        tvResultHistoryCategoryQR.setText(R.string.uri);

        LinearLayout lnlEmail = new LinearLayout(mContext);
        lnlEmail.setOrientation(LinearLayout.VERTICAL);
        TextViewPoppinBold tvWifiCategoryName = new TextViewPoppinBold(mContext);
        TextView tvEmailName = new TextView(mContext);
        tvWifiCategoryName.setText(R.string.uri);
        tvEmailName.setText(qrUrl.getUrl());
        tvEmailName.setBackgroundResource(R.drawable.corner_tv);
        tvEmailName.setMaxLines(1);
        tvEmailName.setEllipsize(TextUtils.TruncateAt.END);
        lnlEmail.addView(tvWifiCategoryName);
        lnlEmail.addView(tvEmailName);
        lnlResultHistoryContent.addView(lnlEmail);
        tvResultHistoryOptionOne.setText(R.string.open_browser);

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

    @Override
    public void onClick(View view) {
        if (view == imvResultHistoryBack) {
            mainActivity.onBackPressed();
        }
        if (view == tvResultHistoryOptionOne) {
            IntentUtils intentUtils = new IntentUtils();
            intentUtils.IntentAction(mContext, mqrScan.getScanText(), mqrScan.getTypeQR());
        }
    }


}
