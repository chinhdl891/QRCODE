package com.example.qrscaner.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrscaner.DataBase.QrGenerateDataBase;
import com.example.qrscaner.Model.Color;
import com.example.qrscaner.Model.QrGenerate;
import com.example.qrscaner.Model.QrScan;
import com.example.qrscaner.R;
import com.example.qrscaner.adapter.ColorAdapter;
import com.example.qrscaner.utils.BitMapUtils;

import java.util.ArrayList;
import java.util.List;

public class SaveQRGenerate extends ConstraintLayout implements ColorAdapter.SelectColor, View.OnClickListener {
    private Context mContext;
    private View mRootView;
    private RecyclerView mRcvSaveColor;
    private ImageView imvSaveCateGory, imvSaveBarCode, imvSaveBack;
    private TextView tvSaveCateGoryName, tvSaveDateCreate, tvSaveTitleQR;
    private TextView tvSave, tvSaveCancel;
    private int color = android.graphics.Color.BLACK;
    private QrScan.QRType type;
    private String qrContent;
    private BitMapUtils bitMapUtils;
    private SaveBackToGenerate saveBackToGenerate;

    public SaveQRGenerate(@NonNull Context context) {
        super(context);
        mContext = context;
        init();
    }

    public SaveQRGenerate(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRootView = layoutInflater.inflate(R.layout.save_item_custom, this, true);
        mRcvSaveColor = mRootView.findViewById(R.id.rcv_save__selectColor);
        imvSaveBarCode = mRootView.findViewById(R.id.imv_save_qr_render);
        imvSaveCateGory = mRootView.findViewById(R.id.imv_save_qr_category);
        imvSaveBack = mRootView.findViewById(R.id.imv_save_back);
        tvSaveCancel = mRootView.findViewById(R.id.tv_save_cancel);
        tvSave = mRootView.findViewById(R.id.tv_save);
        tvSaveCateGoryName = mRootView.findViewById(R.id.tv_save_category_name);
        tvSaveTitleQR = mRootView.findViewById(R.id.tv_save_category);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mContext, 6, RecyclerView.VERTICAL, false);
        mRcvSaveColor.setLayoutManager(layoutManager);
        ColorAdapter colorAdapter = new ColorAdapter(colorList(), this);
        mRcvSaveColor.setAdapter(colorAdapter);
        bitMapUtils = new BitMapUtils();
        imvSaveBack.setOnClickListener(this);
        tvSaveCancel.setOnClickListener(this);
        tvSave.setOnClickListener(this);


    }

    private List<Color> colorList() {
        List<Color> colorList = new ArrayList<>();
        colorList.add(new Color(0, 0XFFDA4532));
        colorList.add(new Color(1, 0xFFE07640));
        colorList.add(new Color(2, 0xFFE7A04F));
        colorList.add(new Color(3, 0xFFF2CE60));
        colorList.add(new Color(4, 0xFFFFFD72));
        colorList.add(new Color(5, 0xFF90C554));
        colorList.add(new Color(6, 0xFF58933B));
        colorList.add(new Color(7, 0xFF67AFBE));
        colorList.add(new Color(8, 0xFF2555C5));
        colorList.add(new Color(9, 0xFF55228D));
        colorList.add(new Color(10, 0xFF812D8F));
        colorList.add(new Color(11, 0xFFAD3B91));
        colorList.add(new Color(12, 0xFFDA4532));
        colorList.add(new Color(13, 0xFF000000));

        return colorList;
    }

    public void setUpdate(QrGenerate qrGenerate, SaveBackToGenerate saveBackToGenerate , SavaQr savaQr) {
        type = qrGenerate.getQrType();
        this.saveBackToGenerate = saveBackToGenerate;
        this.savaQr = savaQr;
        qrContent = qrGenerate.getContent();
        if (type == QrScan.QRType.TEXT) {

            tvSaveTitleQR.setText("QR CODE");
            tvSaveCateGoryName.setText("Text");
            imvSaveCateGory.setImageResource(R.drawable.add_text);
            imvSaveBarCode.setImageBitmap(bitMapUtils.bitmapQR(qrGenerate.getQrType(), qrGenerate.getContent(), android.graphics.Color.RED));
            imvSaveBarCode.getLayoutParams().height = 100;
            imvSaveBarCode.getLayoutParams().width = 100;


        } else if (type == QrScan.QRType.PHONE) {
            tvSaveTitleQR.setText("QR CODE");
            tvSaveCateGoryName.setText("Phone");
            imvSaveCateGory.setImageResource(R.drawable.add_call);
            imvSaveBarCode.setImageBitmap(bitMapUtils.bitmapQR(qrGenerate.getQrType(), qrGenerate.getContent(), android.graphics.Color.BLACK));
            imvSaveBarCode.getLayoutParams().width = 300;
            imvSaveBarCode.getLayoutParams().height = 300;

        } else if (type == QrScan.QRType.EMAIL) {
            tvSaveTitleQR.setText("QR CODE");
            tvSaveCateGoryName.setText("Phone");
            imvSaveBarCode.setImageBitmap(bitMapUtils.bitmapQR(qrGenerate.getQrType(), qrGenerate.getContent(), android.graphics.Color.BLACK));
            imvSaveBarCode.getLayoutParams().width = 300;
            imvSaveBarCode.getLayoutParams().height = 300;
            imvSaveCateGory.setImageResource(R.drawable.add_email);
        } else if (type == QrScan.QRType.SMS) {
            tvSaveTitleQR.setText("QR CODE");
            tvSaveCateGoryName.setText("SMS");
            imvSaveBarCode.setImageBitmap(bitMapUtils.bitmapQR(qrGenerate.getQrType(), qrGenerate.getContent(), android.graphics.Color.BLACK));
            imvSaveBarCode.getLayoutParams().width = 300;
            imvSaveBarCode.getLayoutParams().height = 300;
            imvSaveCateGory.setImageResource(R.drawable.add_sms);

        } else if (type == QrScan.QRType.WIFI) {
            tvSaveTitleQR.setText("QR CODE");
            tvSaveCateGoryName.setText("WIFI");
            imvSaveBarCode.setImageBitmap(bitMapUtils.bitmapQR(qrGenerate.getQrType(), qrGenerate.getContent(), android.graphics.Color.BLACK));
            imvSaveBarCode.getLayoutParams().width = 300;
            imvSaveBarCode.getLayoutParams().height = 300;
            imvSaveCateGory.setImageResource(R.drawable.add_wifi);
        } else if (type == QrScan.QRType.URL) {
            tvSaveTitleQR.setText("QR CODE");
            tvSaveCateGoryName.setText("Uri");
            imvSaveBarCode.setImageBitmap(bitMapUtils.bitmapQR(qrGenerate.getQrType(), qrGenerate.getContent(), android.graphics.Color.BLACK));
            imvSaveBarCode.getLayoutParams().width = 300;
            imvSaveBarCode.getLayoutParams().height = 300;
            imvSaveCateGory.setImageResource(R.drawable.add_uri);
        } else if (type == QrScan.QRType.PRODUCT) {
            tvSaveTitleQR.setText("QR CODE");
            tvSaveCateGoryName.setText("Product");
            imvSaveBarCode.setImageBitmap(bitMapUtils.bitmapQR(qrGenerate.getQrType(), qrGenerate.getContent(), android.graphics.Color.BLACK));
            imvSaveBarCode.getLayoutParams().width = 300;
            imvSaveBarCode.getLayoutParams().height = 300;
            imvSaveCateGory.setImageResource(R.drawable.ic_product);
        } else if (type == QrScan.QRType.BAR39) {
            imvSaveBarCode.setImageBitmap(bitMapUtils.bitmapQR(qrGenerate.getQrType(), qrGenerate.getContent(), android.graphics.Color.BLACK));
            imvSaveBarCode.getLayoutParams().width = 404;
            imvSaveBarCode.getLayoutParams().height = 250;
            tvSaveTitleQR.setText("BAR CODE");
            tvSaveCateGoryName.setText("Bar 39");
            imvSaveCateGory.setImageResource(R.drawable.ic_product);

        } else if (type == QrScan.QRType.BAR93) {
            imvSaveBarCode.setImageBitmap(bitMapUtils.bitmapQR(qrGenerate.getQrType(), qrGenerate.getContent(), android.graphics.Color.BLACK));
            imvSaveBarCode.getLayoutParams().width = 404;
            imvSaveBarCode.getLayoutParams().height = 250;
            tvSaveTitleQR.setText("BAR CODE");
            tvSaveCateGoryName.setText("Bar 93");
            imvSaveCateGory.setImageResource(R.drawable.ic_product);

        } else if (type == QrScan.QRType.BAR128) {
            imvSaveBarCode.setImageBitmap(bitMapUtils.bitmapQR(qrGenerate.getQrType(), qrGenerate.getContent(), android.graphics.Color.BLACK));
            imvSaveBarCode.getLayoutParams().width = 404;
            imvSaveBarCode.getLayoutParams().height = 250;

            tvSaveTitleQR.setText("BAR CODE");
            tvSaveCateGoryName.setText("Bar 128");
            imvSaveCateGory.setImageResource(R.drawable.ic_product);

        }


    }

    @Override
    public void onSelectColor(int color) {
        this.color = color;
        imvSaveBarCode.setImageBitmap(bitMapUtils.bitmapQR(type, qrContent, color));

    }

    @Override
    public void onClick(View view) {
        if (view == imvSaveBack || view == tvSaveCancel) {
            saveBackToGenerate.onSaveBackToGenerate();
        }
        else if (view == tvSave){
            setVisibility(View.GONE);
            QrGenerateDataBase.getInstance(mContext).qrGenerateDao().insertQrGenerate(new QrGenerate(System.currentTimeMillis(),qrContent,type,color));
            savaQr.onSaveQr(new QrGenerate(System.currentTimeMillis(),qrContent,type,color));
        }
    }

    public interface SaveBackToGenerate {

        void onSaveBackToGenerate();


    }
    private SavaQr savaQr;

    public interface SavaQr{
        void onSaveQr(QrGenerate qrGenerate);
    }


}
