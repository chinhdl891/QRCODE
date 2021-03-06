package com.example.qrscaner.view;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.qrscaner.models.QrGenerate;
import com.example.qrscaner.models.QrScan;
import com.example.qrscaner.R;
import com.example.qrscaner.view.fonts.TextViewPoppinBold;

public class ViewGenerateQRCode extends ConstraintLayout implements View.OnClickListener {
    private Context mContext;
    private View mRootView;
    private TextViewPoppinBold tvGenCategory, tvGenSave;
    private LinearLayout lnlGenContent;
    private ImageView imvGenBack;
    private String contentQr, name, pass, tel, content, email, subject, contentEmail;
    private String type = "Free";
    private int id;
    private EditText edtContent;
    private QrScan.QRType typeQR;
    private final boolean isCheckWifi = true;

    public ViewGenerateQRCode(@NonNull Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public ViewGenerateQRCode(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();

    }

    private void init() {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRootView = layoutInflater.inflate(R.layout.text_renerate_view, this, true);
        tvGenCategory = mRootView.findViewById(R.id.tv_generate_qr_category);
        lnlGenContent = mRootView.findViewById(R.id.lnl_generate_qr_content);
        imvGenBack = findViewById(R.id.imv_generate_qr_back);
        tvGenSave = findViewById(R.id.tv_generate_save);
        imvGenBack.setOnClickListener(this);
        tvGenSave.setOnClickListener(this);

    }

    public void setUpData(int id) {
        this.id = id;
        if (id == 0) {
            typeQR = QrScan.QRType.BAR39;
            tvGenCategory.setText(R.string.bar39);
            LinearLayout lnlContentText = new LinearLayout(mContext);
            TextViewPoppinBold tvCategoryText = new TextViewPoppinBold(mContext);
            EditText edtPhone = new EditText(mContext);
            tvCategoryText.setText(R.string.bar39);
            edtPhone.setInputType(InputType.TYPE_CLASS_NUMBER);
            lnlContentText.setOrientation(LinearLayout.VERTICAL);
            lnlContentText.addView(tvCategoryText);
            lnlContentText.addView(edtPhone);
            lnlGenContent.addView(lnlContentText);
            edtPhone.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    contentQr = editable.toString();
                }
            });

        } else if (id == 1) {
            typeQR = QrScan.QRType.BAR93;
            tvGenCategory.setText("BAR93");
            LinearLayout lnlContentText = new LinearLayout(mContext);
            TextViewPoppinBold tvCategoryText = new TextViewPoppinBold(mContext);
            EditText edtPhone = new EditText(mContext);
            edtPhone.setInputType(InputType.TYPE_CLASS_NUMBER);
            tvCategoryText.setText("BAR93");
            lnlContentText.setOrientation(LinearLayout.VERTICAL);
            lnlContentText.addView(tvCategoryText);
            lnlContentText.addView(edtPhone);
            lnlGenContent.addView(lnlContentText);
            edtPhone.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    contentQr = editable.toString();
                }
            });

        } else if (id == 13) {
            typeQR = QrScan.QRType.PHONE;
            tvGenCategory.setText(R.string.phone);
            LinearLayout lnlContentText = new LinearLayout(mContext);
            TextViewPoppinBold tvCategoryText = new TextViewPoppinBold(mContext);
            EditText edtPhone = new EditText(mContext);
            tvCategoryText.setText(R.string.phone);
            lnlContentText.setOrientation(LinearLayout.VERTICAL);
            lnlContentText.addView(tvCategoryText);
            lnlContentText.addView(edtPhone);
            lnlGenContent.addView(lnlContentText);
            edtPhone.setInputType(InputType.TYPE_CLASS_NUMBER);
            edtPhone.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    contentQr = "tel:" + editable.toString();
                }
            });

        } else if (id == 2) {
            typeQR = QrScan.QRType.BAR128;
            tvGenCategory.setText("BAR128");
            LinearLayout lnlContentText = new LinearLayout(mContext);
            TextViewPoppinBold tvCategoryText = new TextViewPoppinBold(mContext);
            EditText edtPhone = new EditText(mContext);
            tvCategoryText.setText("BAR128");
            lnlContentText.setOrientation(LinearLayout.VERTICAL);
            lnlContentText.addView(tvCategoryText);
            lnlContentText.addView(edtPhone);
            lnlGenContent.addView(lnlContentText);
            edtPhone.setInputType(InputType.TYPE_CLASS_NUMBER);
            edtPhone.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    contentQr = editable.toString();
                }
            });

        } else if (id == 13) {
            typeQR = QrScan.QRType.PHONE;
            tvGenCategory.setText(R.string.phone);
            LinearLayout lnlContentText = new LinearLayout(mContext);
            TextViewPoppinBold tvCategoryText = new TextViewPoppinBold(mContext);
            EditText edtPhone = new EditText(mContext);
            tvCategoryText.setText(R.string.phone_number);
            lnlContentText.setOrientation(LinearLayout.VERTICAL);
            lnlContentText.addView(tvCategoryText);
            lnlContentText.addView(edtPhone);
            lnlGenContent.addView(lnlContentText);
            edtPhone.setInputType(InputType.TYPE_CLASS_NUMBER);
            edtPhone.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    contentQr = "tel:" + editable.toString();
                }
            });

        } else if (id == 14) {
            typeQR = QrScan.QRType.EMAIL;
            tvGenCategory.setText("Email");
            LinearLayout lnlContentText = new LinearLayout(mContext);
            TextViewPoppinBold tvCategoryTextEmail = new TextViewPoppinBold(mContext);
            EditText edtEmail = new EditText(mContext);
            tvCategoryTextEmail.setText("Email");
            lnlContentText.setOrientation(LinearLayout.VERTICAL);
            lnlContentText.addView(tvCategoryTextEmail);
            lnlContentText.addView(edtEmail);
            lnlGenContent.addView(lnlContentText);
            LinearLayout lnlContentSub = new LinearLayout(mContext);
            TextViewPoppinBold tvCategorySub = new TextViewPoppinBold(mContext);
            EditText edtSub = new EditText(mContext);
            tvCategorySub.setText(R.string.subject);
            lnlContentSub.setOrientation(LinearLayout.VERTICAL);
            lnlContentSub.addView(tvCategorySub);
            lnlContentSub.addView(edtSub);
            lnlGenContent.addView(lnlContentSub);
            LinearLayout lnlContentContent = new LinearLayout(mContext);
            TextViewPoppinBold tvCategoryContent = new TextViewPoppinBold(mContext);
            edtContent = new EditText(mContext);
            tvCategoryContent.setText(R.string.content);
            lnlContentContent.setOrientation(LinearLayout.VERTICAL);
            lnlContentContent.addView(tvCategoryContent);
            lnlContentContent.addView(edtContent);
            lnlGenContent.addView(lnlContentContent);

            edtEmail.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    email = edtEmail.getText().toString();
                }
            });
            edtSub.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    subject = editable.toString();
                }
            });
            edtContent.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    contentEmail = edtContent.toString();
                }
            });

        } else if (id == 15) {
            typeQR = QrScan.QRType.URL;
            tvGenCategory.setText(R.string.uri);
            LinearLayout lnlContentText = new LinearLayout(mContext);
            TextViewPoppinBold tvCategoryText = new TextViewPoppinBold(mContext);
            EditText edtContent = new EditText(mContext);
            tvCategoryText.setText(R.string.uri);
            lnlContentText.setOrientation(LinearLayout.VERTICAL);
            lnlContentText.addView(tvCategoryText);
            lnlContentText.addView(edtContent);
            lnlGenContent.addView(lnlContentText);
            edtContent.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    contentQr = edtContent.getText().toString();
                }
            });
        } else if (id == 16) {
            typeQR = QrScan.QRType.SMS;
            tvGenCategory.setText(R.string.sms);
            LinearLayout lnlContentText = new LinearLayout(mContext);
            TextViewPoppinBold tvCategoryPhone = new TextViewPoppinBold(mContext);
            EditText edtPhone = new EditText(mContext);

            edtPhone.setInputType(InputType.TYPE_CLASS_NUMBER);
            tvCategoryPhone.setText(R.string.phone_number);
            lnlContentText.setOrientation(LinearLayout.VERTICAL);
            lnlContentText.addView(tvCategoryPhone);
            lnlContentText.addView(edtPhone);
            lnlGenContent.addView(lnlContentText);
            LinearLayout lnlContentSMS = new LinearLayout(mContext);
            TextViewPoppinBold tvCategoryContentSMS = new TextViewPoppinBold(mContext);
            EditText edtSMS = new EditText(mContext);
            tvCategoryContentSMS.setText(R.string.content);
            lnlContentSMS.setOrientation(LinearLayout.VERTICAL);
            lnlContentSMS.addView(tvCategoryContentSMS);
            lnlContentSMS.addView(edtSMS);
            lnlGenContent.addView(lnlContentSMS);
            edtPhone.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    tel = editable.toString();
                }
            });
            edtSMS.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    content = editable.toString();
                }
            });

        } else if (id == 18) {
            typeQR = QrScan.QRType.TEXT;
            tvGenCategory.setText(R.string.text);
            LinearLayout lnlContentText = new LinearLayout(mContext);
            TextViewPoppinBold tvCategoryText = new TextViewPoppinBold(mContext);
            EditText edtContent = new EditText(mContext);
            tvCategoryText.setText(R.string.content);
            lnlContentText.setOrientation(LinearLayout.VERTICAL);
            lnlContentText.addView(tvCategoryText);
            lnlContentText.addView(edtContent);
            lnlGenContent.addView(lnlContentText);
            edtContent.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    contentQr = editable.toString();
                }
            });


        } else if (id == 19) {
            typeQR = QrScan.QRType.WIFI;
            tvGenCategory.setText("Wifi");
            LinearLayout lnlTypeWifi = new LinearLayout(mContext);
            lnlTypeWifi.setOrientation(LinearLayout.HORIZONTAL);
            AppCompatButton acbFree = new AppCompatButton(mContext);
            acbFree.setBackgroundColor(Color.BLUE);
            acbFree.setBackgroundResource(R.drawable.corner_left);
            acbFree.setText("Free");
            AppCompatButton acbWpa = new AppCompatButton(mContext);
            acbWpa.setBackgroundResource(R.drawable.background_btn_wifi);
            acbWpa.setText("WPA/WPA2");
            AppCompatButton acbWep = new AppCompatButton(mContext);
            acbWep.setBackgroundResource(R.drawable.corner_right);
            acbWep.setText("WEP");
            lnlTypeWifi.setGravity(TEXT_ALIGNMENT_CENTER);
            lnlTypeWifi.addView(acbFree);
            lnlTypeWifi.addView(acbWpa);
            lnlTypeWifi.addView(acbWep);
            lnlGenContent.addView(lnlTypeWifi);
            LinearLayout lnlContentText = new LinearLayout(mContext);
            TextViewPoppinBold tvCategoryText = new TextViewPoppinBold(mContext);
            EditText edtNetWork = new EditText(mContext);
            tvCategoryText.setText(R.string.network);
            lnlContentText.setOrientation(LinearLayout.VERTICAL);
            lnlContentText.addView(tvCategoryText);
            lnlContentText.addView(edtNetWork);
            lnlGenContent.addView(lnlContentText);
            LinearLayout lnlContentPass = new LinearLayout(mContext);
            TextViewPoppinBold tvCategoryPass = new TextViewPoppinBold(mContext);
            EditText edtPass = new EditText(mContext);
            tvCategoryPass.setText(R.string.pass);
            lnlContentPass.setOrientation(LinearLayout.VERTICAL);
            lnlContentPass.addView(tvCategoryPass);
            lnlContentPass.addView(edtPass);
            lnlGenContent.addView(lnlContentPass);
            edtNetWork.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    name = editable.toString();
                }
            });
            edtPass.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    pass = editable.toString();
                }
            });
            acbFree.setTextColor(Color.BLUE);
            acbFree.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    type = "Free";
                    acbFree.setTextColor(Color.BLUE);
                    acbWep.setTextColor(Color.BLACK);
                    acbWpa.setTextColor(Color.BLACK);

                }
            });
            acbWep.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    type = "WEP";

                    acbWep.setTextColor(Color.BLUE);
                    acbFree.setTextColor(Color.BLACK);
                    acbWpa.setTextColor(Color.BLACK);

                }
            });
            acbWpa.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    type = "WPA";
                    acbWpa.setTextColor(Color.BLUE);
                    acbFree.setTextColor(Color.BLACK);
                    acbWep.setTextColor(Color.BLACK);
                }
            });

        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.imv_generate_qr_back:
                lnlGenContent.removeAllViews();
                backToGenerate.onBackItemToGenerate();
                setVisibility(View.GONE);
                break;
            case R.id.tv_generate_save:
                switch (id) {
                    case 0:
                    case 1:
                    case 3:
                        break;
                    case 15:

                        String[] url = contentQr.split(":");

                        if (url[0].equals("http") || url[0].equals("https")) {
                            break;
                        } else {
                            contentQr = "https:" + contentQr;
                        }

                        break;
                    case 19:

                        contentQr = "WIFI:T:" + type + ";S:" + name + ";P:" + pass + ";H:;";
                        break;
                    case 16:
                        contentQr = "SMSTO:" + tel + ":" + content;

                        break;
                    case 14:
                        contentQr = "MATMSG:TO:" + email + ";SUB:" + subject + ";BODY:" + edtContent.getText().toString() + ";;";

                        break;
                }
                if (contentQr == null) {
                    Toast.makeText(mContext, "Can Nhap Day Du Thong Tin", Toast.LENGTH_SHORT).show();
                    return;
                }
                setVisibility(View.GONE);
                lnlGenContent.removeAllViews();
                QrGenerate qrGenerate = new QrGenerate(System.currentTimeMillis(), contentQr, typeQR);
                saveQrGenerate.saveQr(qrGenerate);
                break;

        }
    }

    private ISaveQrGenerate saveQrGenerate;

    public void setInterface(ISaveQrGenerate saveQrGenerate) {
        this.saveQrGenerate = saveQrGenerate;
    }

    public interface ISaveQrGenerate {
        void saveQr(QrGenerate qrGenerate);
    }

    public interface IBackToGenerate {
        void onBackItemToGenerate();
    }

    private IBackToGenerate backToGenerate;

    public void setInterFace(IBackToGenerate backToGenerate) {
        this.backToGenerate = backToGenerate;
    }


}
