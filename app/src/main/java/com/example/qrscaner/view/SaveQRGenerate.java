package com.example.qrscaner.view;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
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

import com.example.qrscaner.databases.QrGenerateDataBase;
import com.example.qrscaner.models.ColorOption;
import com.example.qrscaner.models.QrGenerate;
import com.example.qrscaner.models.QrScan;
import com.example.qrscaner.R;
import com.example.qrscaner.activity.MainActivity;
import com.example.qrscaner.adapter.ColorAdapter;
import com.example.qrscaner.utils.BitMapUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SaveQRGenerate extends ConstraintLayout implements ColorAdapter.SelectColor, View.OnClickListener {
    private static final String IMAGES_FOLDER_NAME = "QR";
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
    private boolean isSelect;

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
        tvSaveDateCreate = mRootView.findViewById(R.id.tv_save_date_create);
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

    private List<ColorOption> colorList() {
        List<ColorOption> colorList = new ArrayList<>();
        colorList.add(new ColorOption(0, 0XFFDA4532));
        colorList.add(new ColorOption(1, 0xFFE07640));
        colorList.add(new ColorOption(2, 0xFFE7A04F));
        colorList.add(new ColorOption(3, 0xFFF2CE60));
        colorList.add(new ColorOption(4, 0xFFFFFD72));
        colorList.add(new ColorOption(5, 0xFF90C554));
        colorList.add(new ColorOption(6, 0xFF58933B));
        colorList.add(new ColorOption(7, 0xFF67AFBE));
        colorList.add(new ColorOption(8, 0xFF2555C5));
        colorList.add(new ColorOption(9, 0xFF55228D));
        colorList.add(new ColorOption(10, 0xFF812D8F));
        colorList.add(new ColorOption(11, 0xFFAD3B91));
        colorList.add(new ColorOption(12, 0xFFDA4532));
        colorList.add(new ColorOption(13, 0xFF000000));

        return colorList;
    }

    public void setUpdate(QrGenerate qrGenerate, SaveBackToGenerate saveBackToGenerate, SavaQr savaQr) {
        String date = DateFormat.format("dd/MM/yyyy", new Date(qrGenerate.getDate())).toString();
        tvSaveDateCreate.setText(date);
        type = qrGenerate.getQrType();
        imvSaveBarCode.setImageBitmap(bitMapUtils.bitmapQR(qrGenerate.getQrType(), qrGenerate.getContent(), android.graphics.Color.BLACK));
        this.saveBackToGenerate = saveBackToGenerate;
        this.savaQr = savaQr;
        qrContent = qrGenerate.getContent();
        if (type == QrScan.QRType.TEXT) {
            tvSaveTitleQR.setText(R.string.qr_code_title);
            tvSaveCateGoryName.setText(R.string.text);
            imvSaveCateGory.setImageResource(R.drawable.ic_add_text);
            setSizeQRCode();


        } else if (type == QrScan.QRType.PHONE) {

            tvSaveTitleQR.setText(R.string.qr_code_title);
            tvSaveCateGoryName.setText(R.string.phone);
            imvSaveCateGory.setImageResource(R.drawable.ic_add_call);
            setSizeQRCode();


        } else if (type == QrScan.QRType.EMAIL) {
            tvSaveTitleQR.setText(R.string.qr_code_title);
            tvSaveCateGoryName.setText(R.string.email);
            imvSaveCateGory.setImageResource(R.drawable.ic_add_email);
            setSizeQRCode();


        } else if (type == QrScan.QRType.SMS) {
            tvSaveTitleQR.setText(R.string.qr_code_title);
            tvSaveCateGoryName.setText(R.string.sms);
            setSizeQRCode();


            imvSaveCateGory.setImageResource(R.drawable.ic_add_sms);

        } else if (type == QrScan.QRType.WIFI) {
            tvSaveTitleQR.setText(R.string.qr_code_title);
            tvSaveCateGoryName.setText(R.string.wifi);
            setSizeQRCode();


            imvSaveCateGory.setImageResource(R.drawable.ic_add_wifi);
        } else if (type == QrScan.QRType.URL) {
            tvSaveTitleQR.setText(R.string.qr_code_title);
            tvSaveCateGoryName.setText(R.string.uri);
            setSizeQRCode();

            imvSaveCateGory.setImageResource(R.drawable.ic_add_uri);
        } else if (type == QrScan.QRType.PRODUCT) {
            tvSaveTitleQR.setText(R.string.qr_code_title);
            tvSaveCateGoryName.setText(R.string.product);
            setSizeQRCode();


            imvSaveCateGory.setImageResource(R.drawable.ic_product);
        } else if (type == QrScan.QRType.BAR39) {

            setSizeBARCODE();
            tvSaveTitleQR.setText("BAR CODE");
            tvSaveCateGoryName.setText("Bar 39");
            imvSaveCateGory.setImageResource(R.drawable.ic_product);

        } else if (type == QrScan.QRType.BAR93) {

            setSizeBARCODE();
            tvSaveTitleQR.setText("BAR CODE");
            tvSaveCateGoryName.setText("Bar 93");
            imvSaveCateGory.setImageResource(R.drawable.ic_product);

        } else if (type == QrScan.QRType.BAR128) {

            setSizeBARCODE();
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
            setVisibility(View.GONE);
            saveBackToGenerate.onSaveBackToGenerate();
        } else if (view == tvSave) {
            QrGenerateDataBase.getInstance(mContext).qrGenerateDao().insertQrGenerate(new QrGenerate(System.currentTimeMillis(), qrContent, type, color));
            savaQr.onSaveQr();
            saveImage(BitMapUtils.bitmapQR(type, qrContent, color));
            imvSaveBack.performClick();
        }
    }

    private void saveImage(Bitmap bitmapQR) {
        OutputStream fos = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentResolver resolver = mContext.getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, System.currentTimeMillis() + "");
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/" + IMAGES_FOLDER_NAME);
            Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            try {
                fos = resolver.openOutputStream(imageUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            String imagesDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM).toString() + File.separator + IMAGES_FOLDER_NAME;

            File file = new File(imagesDir);

            if (!file.exists()) {
                file.mkdir();
            }

            File image = new File(imagesDir, System.currentTimeMillis() + ".png");
            try {
                fos = new FileOutputStream(image);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
        try {
            bitmapQR.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public interface SaveBackToGenerate {

        void onSaveBackToGenerate();


    }

    private SavaQr savaQr;

    public interface SavaQr {
        void onSaveQr();
    }

    private void setSizeQRCode() {
        imvSaveBarCode.getLayoutParams().height = (int) (MainActivity.WIDTH * 0.4);
        imvSaveBarCode.getLayoutParams().width = (int) (MainActivity.WIDTH * 0.4);
    }

    private void setSizeBARCODE() {
        imvSaveBarCode.getLayoutParams().width = (int) (MainActivity.WIDTH * 0.68);
        imvSaveBarCode.getLayoutParams().height = (int) (MainActivity.WIDTH * 0.35);
    }


}
