package com.example.qrscaner.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.qrscaner.DataBase.QrGenerateDataBase;
import com.example.qrscaner.DataBase.QrHistoryDatabase;
import com.example.qrscaner.Model.QrGenerate;
import com.example.qrscaner.Model.QrScan;
import com.example.qrscaner.activity.MainActivity;
import com.example.qrscaner.adapter.BARCODEGenerateAdapter;
import com.example.qrscaner.adapter.GenerateHistoryAdapter;
import com.example.qrscaner.adapter.HistoryAdapter;
import com.example.qrscaner.adapter.QrCodeGenerateAdapter;
import com.example.qrscaner.Model.GenerateItem;
import com.example.qrscaner.R;
import com.example.qrscaner.utils.QRGContents;
import com.example.qrscaner.utils.QRGEncoder;
import com.example.qrscaner.view.generate.ViewGenerateQRCode;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class GenerateFragment extends Fragment implements BARCODEGenerateAdapter.iCreateQr, View.OnClickListener, ViewGenerateQRCode.ISaveQrGenerate, GenerateHistoryAdapter.IShare, GenerateHistoryAdapter.IDelete, GenerateHistoryAdapter.IEdit {
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
    private ImageView imvGenerateGotoCreate, imvGenerateEdit;
    private boolean edit;
    private MainActivity mMainActivity;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_generate, container, false);
        mMainActivity = (MainActivity) getActivity();
        rcvGenerateFragmentHistory = view.findViewById(R.id.rcv_generate_fragment_history);
        rcvGenerateFragmentBarCode = view.findViewById(R.id.rcv_generate_barcode);
        imvGenerateGotoCreate = view.findViewById(R.id.imv_generate_create_qr);
        nsvGenQrItem = view.findViewById(R.id.nsv_generate_create);
        lnlGenQrGotoCreate = view.findViewById(R.id.nsv_genFragment_gotoGenerate);
        rcvGenerateFragmentQrCode = view.findViewById(R.id.rcv_generate_qrcode);
        btnGenerateGoTo = view.findViewById(R.id.btn_generate_create);
        BARCODEGenerateAdapter barcodeAdapter = new BARCODEGenerateAdapter(getBarCode(), getActivity(), this);
        QrCodeGenerateAdapter adapterBarCode = new QrCodeGenerateAdapter(getQrCode(), getActivity(), this);
        rcvGenerateFragmentBarCode.setAdapter(barcodeAdapter);
        rcvGenerateFragmentQrCode.setAdapter(adapterBarCode);
        rcvGenerateFragmentQrCode.setLayoutManager(new GridLayoutManager(getActivity(), 3, RecyclerView.VERTICAL, false));
        rcvGenerateFragmentBarCode.setLayoutManager(new GridLayoutManager(getActivity(), 3, RecyclerView.VERTICAL, false));
        rcvGenerateFragmentHistory.setLayoutManager(new LinearLayoutManager(getActivity()));
        generateHistoryAdapter = new GenerateHistoryAdapter(getListQrHistory(), edit, this, this, this);
        rcvGenerateFragmentHistory.setAdapter(generateHistoryAdapter);
        viewGenerateQRCode = view.findViewById(R.id.vgq_generate_createQr);
        imvGenerateEdit = view.findViewById(R.id.imv_generate_edit);
        btnGenerateGoTo.setOnClickListener(this);
        imvGenerateGotoCreate.setOnClickListener(this);
        imvGenerateEdit.setOnClickListener(this);
        return view;
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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_generate_create:
                nsvGenQrItem.setVisibility(View.VISIBLE);
                lnlGenQrGotoCreate.setVisibility(View.GONE);
                break;
            case R.id.imv_generate_create_qr:
                nsvGenQrItem.setVisibility(View.VISIBLE);
                lnlGenQrGotoCreate.setVisibility(View.GONE);
                rcvGenerateFragmentHistory.setVisibility(View.GONE);
                break;
            case R.id.imv_generate_edit:
                if (!edit) {
                    edit = true;
                    generateHistoryAdapter = new GenerateHistoryAdapter(getListQrHistory(), edit, this, this, this);
                    rcvGenerateFragmentHistory.setAdapter(generateHistoryAdapter);
                    imvGenerateEdit.setImageResource(R.drawable.ic_close);

                } else {
                    imvGenerateEdit.setImageResource(R.drawable.pen_edit_1);
                    edit = false;
                    generateHistoryAdapter = new GenerateHistoryAdapter(getListQrHistory(), edit, this, this, this);
                    rcvGenerateFragmentHistory.setAdapter(generateHistoryAdapter);

                }
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
        generateHistoryAdapter = new GenerateHistoryAdapter(getListQrHistory(),edit,this,this, this);
        rcvGenerateFragmentHistory.setAdapter(generateHistoryAdapter);

    }

    private Bitmap setImage(String s) {
        WindowManager manager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = Math.min(width, height);
        smallerDimension = smallerDimension * 3 / 4;
        QRGEncoder qrgEncoder = new QRGEncoder(
                s, null,
                QRGContents.Type.TEXT,
                smallerDimension);
        qrgEncoder.setColorBlack(Color.BLACK);
        qrgEncoder.setColorWhite(Color.WHITE);
        Bitmap bitmap = qrgEncoder.getBitmap();
        return bitmap;

    }

    @Override
    public void share(String s, QrScan.QRType type) {
        checkPermissionRead();

        if (type == QrScan.QRType.TEXT) {
            Bitmap bitmap = setImage(s);
            sharePalette(bitmap);
        }
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
        }
        if (type == QrScan.QRType.BAR93) {
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
        }
        if (type == QrScan.QRType.BAR128) {
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

    private void sharePalette(Bitmap bitmap) {
        String bitmapPath = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, "palette", "share palette");
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
    public void delete(QrGenerate qrGenerate, int position) {
        if (check()==1){
            lnlGenQrGotoCreate.setVisibility(View.VISIBLE);
            rcvGenerateFragmentHistory.setVisibility(View.GONE);
        }
        QrGenerateDataBase.getInstance(getActivity()).qrGenerateDao().deleteQrGenerate(qrGenerate);
        generateHistoryAdapter = new GenerateHistoryAdapter(getListQrHistory(), false, this, this, this);
        rcvGenerateFragmentHistory.setAdapter(generateHistoryAdapter);

    }
    private int check(){
        int size = getListQrHistory().size();
        return size;
    }


    @Override
    public void edit(boolean isEdit) {
        if (isEdit) {
            edit = true;
            generateHistoryAdapter = new GenerateHistoryAdapter(getListQrHistory(), edit, this, this, this);
            rcvGenerateFragmentHistory.setAdapter(generateHistoryAdapter);
            imvGenerateEdit.setImageResource(R.drawable.pen_edit_1);
        } else {
            edit = true;
            generateHistoryAdapter = new GenerateHistoryAdapter(getListQrHistory(), edit, this, this, this);
            rcvGenerateFragmentHistory.setAdapter(generateHistoryAdapter);
            imvGenerateEdit.setImageResource(R.drawable.ic_close);
        }
    }

}