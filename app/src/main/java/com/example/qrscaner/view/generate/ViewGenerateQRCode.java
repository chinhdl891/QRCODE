package com.example.qrscaner.view.generate;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

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
        if (id == 13) {
            tvGenCategory.setText("Phone");
            LinearLayout lnlContentText = new LinearLayout(mContext);
            TextViewPoppinBold tvCategoryText = new TextViewPoppinBold(mContext);
            EditText edtPhone = new EditText(mContext);
            tvCategoryText.setText("Phone");
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
                    contentQr = "tel:" + editable.toString();
                }
            });

        } else if (id == 14) {

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
            tvCategorySub.setText("Subject");
            lnlContentSub.setOrientation(LinearLayout.VERTICAL);
            lnlContentSub.addView(tvCategorySub);
            lnlContentSub.addView(edtSub);
            lnlGenContent.addView(lnlContentSub);
            LinearLayout lnlContentContent = new LinearLayout(mContext);
            TextViewPoppinBold tvCategoryContent = new TextViewPoppinBold(mContext);
             edtContent = new EditText(mContext);
            tvCategoryContent.setText("Content");
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
            //
            //mailto:chinh@gmail.com?subject=yl@gmail.com&body=Chinh1234567890


        } else if (id == 15) {
            tvGenCategory.setText("Uri");
            LinearLayout lnlContentText = new LinearLayout(mContext);
            TextViewPoppinBold tvCategoryText = new TextViewPoppinBold(mContext);
            EditText edtContent = new EditText(mContext);
            tvCategoryText.setText("Uri");
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

            tvGenCategory.setText("SMS");
            LinearLayout lnlContentText = new LinearLayout(mContext);
            TextViewPoppinBold tvCategoryPhone = new TextViewPoppinBold(mContext);
            EditText edtPhone = new EditText(mContext);
            tvCategoryPhone.setText("Phone");
            lnlContentText.setOrientation(LinearLayout.VERTICAL);
            lnlContentText.addView(tvCategoryPhone);
            lnlContentText.addView(edtPhone);
            lnlGenContent.addView(lnlContentText);
            LinearLayout lnlContentSMS = new LinearLayout(mContext);
            TextViewPoppinBold tvCategoryContentSMS = new TextViewPoppinBold(mContext);
            EditText edtSMS = new EditText(mContext);
            tvCategoryContentSMS.setText("Phone");
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
            //SMSTO:0385154192:kikikikik helo helo


        } else if (id == 17) {

        } else if (id == 18) {
            tvGenCategory.setText("Text");
            LinearLayout lnlContentText = new LinearLayout(mContext);
            TextViewPoppinBold tvCategoryText = new TextViewPoppinBold(mContext);
            EditText edtContent = new EditText(mContext);
            tvCategoryText.setText("Text");
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
            //WIFI:S:congty;T:nopass;P:12345678;;
            //WIFI:S:congty;T:WEP;P:12345678;;
            //WIFI:S:congty;T:WPA;P:12345678;;

            tvGenCategory.setText("Wifi");
            LinearLayout lnlTypeWifi = new LinearLayout(mContext);
            lnlTypeWifi.setOrientation(LinearLayout.HORIZONTAL);
            AppCompatButton acbFree = new AppCompatButton(mContext);
            acbFree.setBackgroundResource(R.drawable.corner_left);
            acbFree.setText("Free");
            AppCompatButton acbWpa = new AppCompatButton(mContext);
            acbWpa.setBackgroundResource(R.drawable.background_btn_wifi);
            acbWpa.setText("WPA/WPA2");
            AppCompatButton acbWep = new AppCompatButton(mContext);
            acbWep.setBackgroundResource(R.drawable.corner_right);
            acbWep.setText("WEP");
            lnlTypeWifi.addView(acbFree);
            lnlTypeWifi.addView(acbWpa);
            lnlTypeWifi.addView(acbWep);
            lnlGenContent.addView(lnlTypeWifi);
            LinearLayout lnlContentText = new LinearLayout(mContext);
            TextViewPoppinBold tvCategoryText = new TextViewPoppinBold(mContext);
            EditText edtNetWork = new EditText(mContext);
            tvCategoryText.setText("Network");
            lnlContentText.setOrientation(LinearLayout.VERTICAL);
            lnlContentText.addView(tvCategoryText);
            lnlContentText.addView(edtNetWork);
            lnlGenContent.addView(lnlContentText);
            LinearLayout lnlContentPass = new LinearLayout(mContext);
            TextViewPoppinBold tvCategoryPass = new TextViewPoppinBold(mContext);
            EditText edtPass = new EditText(mContext);
            tvCategoryPass.setText("Pass");
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
            acbFree.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    type = "nopass";
                }
            });
            acbWep.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    type = "WEP";
                }
            });
            acbWpa.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    type = "WPA";
                }
            });


        } else if (id == 20) {

        } else if (id == 21) {

        }
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.imv_generate_qr_back:
                lnlGenContent.removeAllViews();
                setVisibility(View.GONE);
                break;
            case R.id.tv_generate_save:
                switch (id) {
                    case 19:
                        //WIFI:T:WPA;S:Cong ty 333;P:1111222212;H:;
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
                break;

        }
    }
}
