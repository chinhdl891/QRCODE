package com.example.qrscaner.fragment;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrscaner.DataBase.QrGenerateDataBase;
import com.example.qrscaner.Model.GenerateItem;
import com.example.qrscaner.Model.QrGenerate;
import com.example.qrscaner.Model.QrScan;
import com.example.qrscaner.R;
import com.example.qrscaner.activity.MainActivity;
import com.example.qrscaner.adapter.BARCODEGenerateAdapter;
import com.example.qrscaner.adapter.GenerateHistoryAdapter;
import com.example.qrscaner.adapter.QrCodeGenerateAdapter;
import com.example.qrscaner.config.Constant;

import com.example.qrscaner.view.QrScanResult;
import com.example.qrscaner.view.generate.ResultScanQr;
import com.example.qrscaner.view.generate.ViewGenerateQRCode;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;


import java.util.ArrayList;
import java.util.List;


public class GenerateFragment extends Fragment implements BARCODEGenerateAdapter.iCreateQr, View.OnClickListener, ViewGenerateQRCode.ISaveQrGenerate, GenerateHistoryAdapter.EditGenerateListener, GenerateHistoryAdapter.ShowData, ResultScanQr.BackToGenerate {
    private static final int REQUEST_WRITE_STORAGE = 1000;
    private static final int REQUEST_READ_STORAGE = 999;
    private static final int BITMAP_WIDTH = 955;
    private static final int BITMAP_HEIGHT = 426;

    private RecyclerView rcvGenerateFragmentQrCode, rcvGenerateFragmentBarCode, rcvGenerateFragmentHistory;
    private ViewGenerateQRCode viewGenerateQRCode;
    private Button btnGenerateGoTo;
    private LinearLayout lnlGenQrGotoCreate;
    private NestedScrollView nsvGenQrItem;
    private List<QrGenerate> qrGenerateList;
    private GenerateHistoryAdapter generateHistoryAdapter;
    private ImageView imvGenerateGotoCreate, imvGenerateEdit, imvGenerateBackground;
    private TextView tvGenerateCreate, tvGenerateCreateTitle;
    private boolean edit;
    private MainActivity mMainActivity;
    private int mSelected = 0;
    private GenerateReceiver generateReceiver;
    private Bitmap bmShare;
    private boolean isGenMenu;
    private QrScanResult mResultHistoryGen;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_generate, container, false);
        init(view);
        qrGenerateList = new ArrayList<>();
        generateReceiver = new GenerateReceiver();
        mMainActivity.registerReceiver(generateReceiver, new IntentFilter(Constant.ACTION_DELETE_MULTIPLE_QRCODE));
        mMainActivity.registerReceiver(generateReceiver, new IntentFilter(Constant.ACTION_SHARE_MULTIPLE_QRCODE_GEN));


        BARCODEGenerateAdapter barcodeAdapter = new BARCODEGenerateAdapter(getBarCode(), getActivity(), this);
        QrCodeGenerateAdapter adapterBarCode = new QrCodeGenerateAdapter(getQrCode(), getActivity(), this);
        rcvGenerateFragmentBarCode.setAdapter(barcodeAdapter);
        rcvGenerateFragmentQrCode.setAdapter(adapterBarCode);
        rcvGenerateFragmentBarCode.setNestedScrollingEnabled(true);
        rcvGenerateFragmentQrCode.setNestedScrollingEnabled(true);

        rcvGenerateFragmentQrCode.setLayoutManager(new GridLayoutManager(getActivity(), 3, RecyclerView.VERTICAL, false));
        rcvGenerateFragmentBarCode.setLayoutManager(new GridLayoutManager(getActivity(), 3, RecyclerView.VERTICAL, false));
        rcvGenerateFragmentHistory.setLayoutManager(new LinearLayoutManager(getActivity()));
        generateHistoryAdapter = new GenerateHistoryAdapter(getListQrHistory(), edit, this, this);
        rcvGenerateFragmentHistory.setAdapter(generateHistoryAdapter);


        btnGenerateGoTo.setOnClickListener(this);
        imvGenerateGotoCreate.setOnClickListener(this);
        imvGenerateEdit.setOnClickListener(this);

        return view;
    }

    private void init(View view) {
        mResultHistoryGen = view.findViewById(R.id.rhs_generateFragment_show);
        mMainActivity = (MainActivity) getActivity();
        tvGenerateCreate = view.findViewById(R.id.tv_generate_create);
        imvGenerateEdit = view.findViewById(R.id.imv_generate__edit);
        imvGenerateBackground = view.findViewById(R.id.imv_generate__background);
        rcvGenerateFragmentHistory = view.findViewById(R.id.rcv_generate_fragment__history);
        rcvGenerateFragmentBarCode = view.findViewById(R.id.rcv_generate__barcode);
        imvGenerateGotoCreate = view.findViewById(R.id.imv_generate__createQr);
        nsvGenQrItem = view.findViewById(R.id.nsv_generate__create);
        lnlGenQrGotoCreate = view.findViewById(R.id.nsv_genFragment__gotoGenerate);
        rcvGenerateFragmentQrCode = view.findViewById(R.id.rcv_generate__qrcode);
        btnGenerateGoTo = view.findViewById(R.id.btn_generate_create);
        viewGenerateQRCode = view.findViewById(R.id.vgq_generate__createQr);
        tvGenerateCreateTitle = view.findViewById(R.id.tv_generate_title);
    }


    private List<GenerateItem> getBarCode() {
        List<GenerateItem> generateItems = new ArrayList<>();
        generateItems.add(new GenerateItem(0, getResources().getIdentifier("com.example.qrscaner:drawable/ic_barcoder3996128", null, null), "Code 39"));
        generateItems.add(new GenerateItem(1, R.drawable.ic_barcoder3996128, "Code 93"));
        generateItems.add(new GenerateItem(2, R.drawable.ic_barcoder3996128, "Code 128"));
        return generateItems;
    }

    private List<GenerateItem> getQrCode() {
        List<GenerateItem> generateItems = new ArrayList<>();
        generateItems.add(new GenerateItem(13, R.drawable.add_call, "Phone"));
        generateItems.add(new GenerateItem(14, R.drawable.add_email, "Email"));
        generateItems.add(new GenerateItem(15, R.drawable.add_uri, "Uri"));
        generateItems.add(new GenerateItem(16, R.drawable.add_sms, "SMS"));
        generateItems.add(new GenerateItem(18, R.drawable.add_text, "Text"));
        generateItems.add(new GenerateItem(19, R.drawable.add_wifi, "Wifi"));
        return generateItems;
    }

    private List<QrGenerate> getListQrHistory() {
        qrGenerateList = QrGenerateDataBase.getInstance(getActivity()).qrGenerateDao().getListQrGenerate();
        if (qrGenerateList.size() > 0) {
            lnlGenQrGotoCreate.setVisibility(View.GONE);
        }
        return qrGenerateList;
    }

    @Override
    public void createListener(int id) {
        viewGenerateQRCode.setInterface(this);
        viewGenerateQRCode.setVisibility(View.VISIBLE);
        viewGenerateQRCode.setUpData(id);
        nsvGenQrItem.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_generate_create:

            case R.id.imv_generate__createQr:
                if (!isGenMenu) {
                    imvGenerateGotoCreate.setImageResource(R.drawable.ic_close);
                    isGenMenu = true;
                    nsvGenQrItem.setVisibility(View.VISIBLE);
                    lnlGenQrGotoCreate.setVisibility(View.GONE);
                    rcvGenerateFragmentHistory.setVisibility(View.GONE);
                } else {

                    isGenMenu = false;
                    imvGenerateGotoCreate.setImageResource(R.drawable.imv_history_plus_history);
                    nsvGenQrItem.setVisibility(View.GONE);
                    if (qrGenerateList.size() > 0) {
                        lnlGenQrGotoCreate.setVisibility(View.GONE);
                        rcvGenerateFragmentHistory.setVisibility(View.VISIBLE);
                    }else {
                        lnlGenQrGotoCreate.setVisibility(View.VISIBLE);

                    }
                }


                break;
            case R.id.imv_generate__edit:
                if (!edit) {
                    edit = true;
                    generateHistoryAdapter.setEdit(true);
                    imvGenerateEdit.setImageResource(R.drawable.ic_close);

                } else {
                    mMainActivity.getBottomNavigationView().setVisibility(View.VISIBLE);
                    mMainActivity.getCtlMainEditItem().setVisibility(View.GONE);
                    mSelected = 0;
                    for (QrGenerate qrScan : qrGenerateList) {
                        qrScan.setEdit(false);
                    }
                    if (qrGenerateList.size() == 0) {
                        rcvGenerateFragmentHistory.setVisibility(View.GONE);
                        lnlGenQrGotoCreate.setVisibility(View.VISIBLE);
                    }
                    imvGenerateEdit.setImageResource(R.drawable.pen_edit_1);
                    edit = false;
                    generateHistoryAdapter.setEdit(false);


                }
                generateHistoryAdapter.setEdit(edit);
                break;
        }
    }

    @Override
    public void saveQr(QrGenerate qrGenerate) {

        checkPermissionWrite();
        QrGenerateDataBase.getInstance(getActivity()).qrGenerateDao().insertQrGenerate(qrGenerate);
        lnlGenQrGotoCreate.setVisibility(View.GONE);
        rcvGenerateFragmentHistory.setVisibility(View.VISIBLE);
        nsvGenQrItem.setVisibility(View.GONE);
        generateHistoryAdapter = new GenerateHistoryAdapter(getListQrHistory(), edit, this, this);
        rcvGenerateFragmentHistory.setAdapter(generateHistoryAdapter);


    }


    private void sharePalette(Bitmap bitmap) {

        String bitmapPath = MediaStore.Images.Media.insertImage(mMainActivity.getContentResolver(), bitmap, "" + System.currentTimeMillis(), null);
        Uri bitmapUri = Uri.parse(bitmapPath);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/png");
        intent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
        startActivity(Intent.createChooser(intent, "Share"));
    }


    private void checkPermissionWrite() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);

        }

    }

    private void checkPermissionRead() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(getActivity(), "Storage permission denied", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == REQUEST_READ_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(getActivity(), "Storage permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void onShareGenerate(String s, QrScan.QRType type) {
        checkPermissionRead();
        if (type == QrScan.QRType.TEXT) {
            setImage(s);
            sharePalette(bmShare);
        } else {
            if (type == QrScan.QRType.BAR39) {
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                try {
                    BitMatrix bitMatrix = multiFormatWriter.encode(s, BarcodeFormat.CODE_39, BITMAP_WIDTH, BITMAP_HEIGHT);
                    Bitmap bitmap = Bitmap.createBitmap(BITMAP_WIDTH, BITMAP_HEIGHT, Bitmap.Config.RGB_565);
                    for (int i = 0; i < BITMAP_WIDTH; i++) {
                        for (int j = 0; j < BITMAP_HEIGHT; j++) {
                            bitmap.setPixel(i, j, bitMatrix.get(i, j) ? Color.BLACK : Color.WHITE);
                        }
                    }
                    sharePalette(bitmap);
                } catch (Exception e) {

                }
            } else if (type == QrScan.QRType.BAR93) {
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                try {
                    BitMatrix bitMatrix = multiFormatWriter.encode(s, BarcodeFormat.CODE_93, BITMAP_WIDTH, BITMAP_HEIGHT);
                    Bitmap bitmap = Bitmap.createBitmap(BITMAP_WIDTH, BITMAP_HEIGHT, Bitmap.Config.RGB_565);
                    for (int i = 0; i < BITMAP_WIDTH; i++) {
                        for (int j = 0; j < BITMAP_HEIGHT; j++) {
                            bitmap.setPixel(i, j, bitMatrix.get(i, j) ? Color.BLACK : Color.WHITE);
                        }
                    }
                    sharePalette(bitmap);
                } catch (Exception e) {

                }
            } else if (type == QrScan.QRType.BAR128) {
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                try {
                    BitMatrix bitMatrix = multiFormatWriter.encode(s, BarcodeFormat.CODE_128, BITMAP_WIDTH, BITMAP_HEIGHT);
                    Bitmap bitmap = Bitmap.createBitmap(BITMAP_WIDTH, BITMAP_HEIGHT, Bitmap.Config.RGB_565);
                    for (int i = 0; i < BITMAP_WIDTH; i++) {
                        for (int j = 0; j < BITMAP_HEIGHT; j++) {
                            bitmap.setPixel(i, j, bitMatrix.get(i, j) ? Color.BLACK : Color.WHITE);
                        }
                    }
                    sharePalette(bitmap);
                } catch (Exception e) {

                }
            }
        }

    }

    private void setImage(String s) {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(s, BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            bmShare = bmp;

        } catch (WriterException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onDeleteGenerate(QrGenerate qrGenerate, int i) {
        QrGenerateDataBase.getInstance(getActivity()).qrGenerateDao().deleteQrGenerate(qrGenerate);
        generateHistoryAdapter.notifyDataSetChanged();
        qrGenerateList.remove(i);
        if (qrGenerateList.size() == 0) {
            lnlGenQrGotoCreate.setVisibility(View.VISIBLE);
            mMainActivity.getBottomNavigationView().setVisibility(View.VISIBLE);
            mMainActivity.getCtlMainEditItem().setVisibility(View.GONE);
            mSelected = 0;
        }
    }

    @Override
    public void onEditGenerate(boolean isEdit) {
        if (isEdit) {
            edit = true;
            rcvGenerateFragmentHistory.setAdapter(generateHistoryAdapter);
            imvGenerateEdit.setImageResource(R.drawable.pen_edit_1);
        } else {
            edit = true;
            rcvGenerateFragmentHistory.setAdapter(generateHistoryAdapter);
            imvGenerateEdit.setImageResource(R.drawable.ic_close);
        }
        generateHistoryAdapter.setEdit(edit);
    }

    @Override
    public void onSelectedItem(boolean isSelect) {
        if (isSelect) {
            mSelected++;
        } else {
            mSelected--;
        }
        if (mSelected > 0) {
            mMainActivity.getBottomNavigationView().setVisibility(View.GONE);
            mMainActivity.getCtlMainEditItem().setVisibility(View.VISIBLE);
            mMainActivity.getTvMainNumItem().setText("" + mSelected);
        } else {
            imvGenerateEdit.performClick();
        }
    }

    @Override
    public void onShowListener(QrGenerate qrGenerate) {
        mMainActivity.getBottomNavigationView().setVisibility(View.GONE);
        imvGenerateGotoCreate.setVisibility(View.GONE);
        imvGenerateBackground.setVisibility(View.GONE);
        tvGenerateCreate.setVisibility(View.GONE);
        tvGenerateCreateTitle.setVisibility(View.GONE);
        mResultHistoryGen.setVisibility(View.VISIBLE);
        mResultHistoryGen.setupData(qrGenerate,this);


    }

    @Override
    public void onBackGenerate() {
        isGenMenu = false;
        imvGenerateGotoCreate.setImageResource(R.drawable.imv_history_plus_history);

        isGenMenu = false;
        imvGenerateGotoCreate.setImageResource(R.drawable.imv_history_plus_history);
        if (qrGenerateList.size() > 0) {
            lnlGenQrGotoCreate.setVisibility(View.GONE);
            rcvGenerateFragmentHistory.setVisibility(View.VISIBLE);

        }else {
            lnlGenQrGotoCreate.setVisibility(View.VISIBLE);
            nsvGenQrItem.setVisibility(View.GONE);
        }
        imvGenerateGotoCreate.setVisibility(View.VISIBLE);
        imvGenerateBackground.setVisibility(View.VISIBLE);
        tvGenerateCreate.setVisibility(View.VISIBLE);
        tvGenerateCreateTitle.setVisibility(View.VISIBLE);
        mMainActivity.getBottomNavigationView().setVisibility(View.VISIBLE);
        mResultHistoryGen.setVisibility(View.GONE);
    }

    public class GenerateReceiver extends BroadcastReceiver {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constant.ACTION_DELETE_MULTIPLE_QRCODE)) {
                for (int i = 0; i < qrGenerateList.size(); i++) {
                    if (qrGenerateList.get(i).isEdit()) {
                        onDeleteGenerate(qrGenerateList.get(i), i);
                        i--;
                    }
                }
            }

            if (intent.getAction().equals(Constant.ACTION_SHARE_MULTIPLE_QRCODE_GEN)) {
                for (int i = 0; i < qrGenerateList.size(); i++) {
                    if (qrGenerateList.get(i).isEdit()) {
                        shareMulti(qrGenerateList.get(i).getContent(), qrGenerateList.get(i).getQrType());
                        i--;
                        mSelected--;
                        if (mSelected == 0) {
                            imvGenerateEdit.performClick();
                        }
                    }
                }
            }
        }
    }

    private void shareMulti(String s, QrScan.QRType type) {
        if (type != QrScan.QRType.BAR128 || type != QrScan.QRType.BAR93 || type != QrScan.QRType.BAR39) {
            setImage(s);
            sharePalette(bmShare);
        } else {
            if (type == QrScan.QRType.BAR39) {
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                try {
                    BitMatrix bitMatrix = multiFormatWriter.encode(s, BarcodeFormat.CODE_39, BITMAP_WIDTH, BITMAP_HEIGHT);
                    Bitmap bitmap = Bitmap.createBitmap(BITMAP_WIDTH, BITMAP_HEIGHT, Bitmap.Config.RGB_565);
                    for (int i = 0; i < BITMAP_WIDTH; i++) {
                        for (int j = 0; j < BITMAP_HEIGHT; j++) {
                            bitmap.setPixel(i, j, bitMatrix.get(i, j) ? Color.BLACK : Color.WHITE);
                        }
                    }
                    sharePalette(bitmap);
                } catch (Exception e) {

                }
            } else if (type == QrScan.QRType.BAR93) {
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                try {
                    BitMatrix bitMatrix = multiFormatWriter.encode(s, BarcodeFormat.CODE_93, BITMAP_WIDTH, BITMAP_HEIGHT);
                    Bitmap bitmap = Bitmap.createBitmap(BITMAP_WIDTH, BITMAP_HEIGHT, Bitmap.Config.RGB_565);
                    for (int i = 0; i < BITMAP_WIDTH; i++) {
                        for (int j = 0; j < BITMAP_HEIGHT; j++) {
                            bitmap.setPixel(i, j, bitMatrix.get(i, j) ? Color.BLACK : Color.WHITE);
                        }
                    }
                    sharePalette(bitmap);
                } catch (Exception e) {

                }
            } else if (type == QrScan.QRType.BAR128) {
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                try {
                    BitMatrix bitMatrix = multiFormatWriter.encode(s, BarcodeFormat.CODE_128, BITMAP_WIDTH, BITMAP_HEIGHT);
                    Bitmap bitmap = Bitmap.createBitmap(BITMAP_WIDTH, BITMAP_HEIGHT, Bitmap.Config.RGB_565);
                    for (int i = 0; i < BITMAP_WIDTH; i++) {
                        for (int j = 0; j < BITMAP_HEIGHT; j++) {
                            bitmap.setPixel(i, j, bitMatrix.get(i, j) ? Color.BLACK : Color.WHITE);
                        }
                    }
                    sharePalette(bitmap);
                } catch (Exception e) {

                }
            }
        }
    }

    @Override
    public void onDestroy() {
        mMainActivity.unregisterReceiver(generateReceiver);
        super.onDestroy();
    }

}