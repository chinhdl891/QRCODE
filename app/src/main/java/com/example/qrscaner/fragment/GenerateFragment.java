package com.example.qrscaner.fragment;

import static com.example.qrscaner.config.Constant.BITMAP_HEIGHT;
import static com.example.qrscaner.config.Constant.BITMAP_WIDTH;
import static com.example.qrscaner.config.Constant.REQUEST_READ_STORAGE;
import static com.example.qrscaner.config.Constant.REQUEST_WRITE_STORAGE;
import static com.example.qrscaner.fragment.ShowQrGenerateFragment.SEND_GEN_QR;
import static com.example.qrscaner.utils.ShareUtils.sharePalette;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
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

import com.example.qrscaner.databases.QrGenerateDataBase;
import com.example.qrscaner.models.GenerateItem;
import com.example.qrscaner.models.QrGenerate;
import com.example.qrscaner.models.QrScan;
import com.example.qrscaner.R;
import com.example.qrscaner.activity.MainActivity;
import com.example.qrscaner.adapter.BARCODEGenerateAdapter;
import com.example.qrscaner.adapter.GenerateHistoryAdapter;
import com.example.qrscaner.adapter.QrCodeGenerateAdapter;
import com.example.qrscaner.config.Constant;

import com.example.qrscaner.view.ShowQrGenerate;
import com.example.qrscaner.view.SaveQRGenerate;
import com.example.qrscaner.view.ViewGenerateQRCode;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;


import java.util.ArrayList;
import java.util.List;


public class GenerateFragment extends Fragment implements BARCODEGenerateAdapter.iCreateQr, SaveQRGenerate.SavaQr, ViewGenerateQRCode.IBackToGenerate, View.OnClickListener, ViewGenerateQRCode.ISaveQrGenerate, GenerateHistoryAdapter.EditGenerateListener, GenerateHistoryAdapter.ShowData, SaveQRGenerate.SaveBackToGenerate {


    private RecyclerView rcvGenerateFragmentQrCode, rcvGenerateFragmentBarCode, rcvGenerateFragmentHistory;
    private ViewGenerateQRCode viewGenerateQRCode;
    private Button btnGenerateGoTo;
    private LinearLayout lnlGenQrGotoCreate;
    private NestedScrollView nsvGenQrItem;
    private List<QrGenerate> qrGenerateList;
    private GenerateHistoryAdapter generateHistoryAdapter;
    private ImageView imvGenerateGotoCreate, imvGenerateEdit, imvGenerateBackground;
    private TextView tvGenerateCreate, tvGenerateCreateTitle;
    private boolean edit = false;
    private MainActivity mMainActivity;
    private int mSelected = 0;
    private GenerateReceiver generateReceiver;
    private Bitmap bmShare;
    private boolean isGenMenu;
    private ShowQrGenerate mResultHistoryGen;
    private SaveQRGenerate saveQRGenerate;


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

        saveQRGenerate = view.findViewById(R.id.save_generateFragment);
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
        generateItems.add(new GenerateItem(13, R.drawable.ic_add_call, "Phone"));
        generateItems.add(new GenerateItem(14, R.drawable.ic_add_email, "Email"));
        generateItems.add(new GenerateItem(15, R.drawable.ic_add_uri, "Uri"));
        generateItems.add(new GenerateItem(16, R.drawable.ic_add_sms, "SMS"));
        generateItems.add(new GenerateItem(18, R.drawable.ic_add_text, "Text"));
        generateItems.add(new GenerateItem(19, R.drawable.ic_add_wifi, "Wifi"));
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
        viewGenerateQRCode.setInterFace(this);
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
                    } else {
                        lnlGenQrGotoCreate.setVisibility(View.VISIBLE);

                    }
                }


                break;
            case R.id.imv_generate__edit:
                if (qrGenerateList.size() > 0) {
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
                        imvGenerateEdit.setImageResource(R.drawable.imv_pen_edit_1);
                        edit = false;
                        generateHistoryAdapter.setEdit(false);


                    }
                    generateHistoryAdapter.setEdit(edit);
                } else {
                    Toast.makeText(mMainActivity, "Khong co gi de chon", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    @Override
    public void saveQr(QrGenerate qrGenerate) {

        checkPermissionWrite();
        mMainActivity.getCtlMainEditItem().setVisibility(View.GONE);
        saveQRGenerate.setUpdate(qrGenerate, this, this);
        mMainActivity.getBottomNavigationView().setVisibility(View.GONE);
        saveQRGenerate.setVisibility(View.VISIBLE);

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
    public void onShareGenerate(String s, QrScan.QRType type, int color) {
        checkPermissionRead();
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        BitMatrix bitMatrix;
        BarcodeFormat format;
        switch (type) {

            case BAR39:
                format = BarcodeFormat.CODE_39;
                break;
            case BAR93:
                format = BarcodeFormat.CODE_93;
                break;
            case BAR128:
                format = BarcodeFormat.CODE_128;
                break;
            default:
                format = BarcodeFormat.QR_CODE;
                break;
        }

        try {
            bitMatrix = multiFormatWriter.encode(s, format, BITMAP_WIDTH, BITMAP_HEIGHT);
            Bitmap bitmap = Bitmap.createBitmap(BITMAP_WIDTH, BITMAP_HEIGHT, Bitmap.Config.RGB_565);
            for (int i = 0; i < BITMAP_WIDTH; i++) {
                for (int j = 0; j < BITMAP_HEIGHT; j++) {
                    bitmap.setPixel(i, j, bitMatrix.get(i, j) ? color : Color.WHITE);
                }
            }
            if (getActivity() != null) {
                sharePalette(getActivity(), bmShare);
            }

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void setImage(String s, int color) {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(s, BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? color : Color.WHITE);
                }
            }
            bmShare = bmp;

        } catch (WriterException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onDeleteGenerate(QrGenerate qrGenerate, int i) {
        if (getListQrHistory().size() == 1) {
            lnlGenQrGotoCreate.setVisibility(View.VISIBLE);
            rcvGenerateFragmentHistory.setVisibility(View.GONE);
            mMainActivity.getBottomNavigationView().setVisibility(View.VISIBLE);
            mMainActivity.getCtlMainEditItem().setVisibility(View.GONE);
            mSelected = 0;
        }
        QrGenerateDataBase.getInstance(getActivity()).qrGenerateDao().deleteQrGenerate(qrGenerate);

    }

    @Override
    public void onEditGenerate(boolean isEdit) {
        if (!isEdit) {
            edit = true;
            imvGenerateEdit.setImageResource(R.drawable.imv_pen_edit_1);
        } else {
            edit = false;
            imvGenerateEdit.setImageResource(R.drawable.ic_close);

        }
        generateHistoryAdapter = new GenerateHistoryAdapter(getListQrHistory(), isEdit, this, this);
        rcvGenerateFragmentHistory.setAdapter(generateHistoryAdapter);
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

        Bundle bundle = new Bundle();
        ShowQrGenerateFragment fragment = new ShowQrGenerateFragment();
        bundle.putSerializable(SEND_GEN_QR, qrGenerate);
        fragment.setArguments(bundle);
        mMainActivity.fragmentLoad(fragment, ShowQrGenerateFragment.class.getSimpleName());


    }

    @Override
    public void onSaveBackToGenerate() {

        imvGenerateGotoCreate.setImageResource(R.drawable.imv_history_plus_history);
        isGenMenu = false;

        if (qrGenerateList.size() > 0) {
            rcvGenerateFragmentHistory.setVisibility(View.VISIBLE);
            lnlGenQrGotoCreate.setVisibility(View.GONE);

        } else {
            lnlGenQrGotoCreate.setVisibility(View.VISIBLE);
            rcvGenerateFragmentHistory.setVisibility(View.VISIBLE);
        }
        mMainActivity.getBottomNavigationView().setVisibility(View.VISIBLE);

    }

    @Override
    public void onBackItemToGenerate() {
        mMainActivity.getBottomNavigationView().setVisibility(View.VISIBLE);
        isGenMenu = false;
        imvGenerateGotoCreate.setImageResource(R.drawable.imv_history_plus_history);
        if (qrGenerateList.size() > 0) {
            rcvGenerateFragmentHistory.setVisibility(View.VISIBLE);
        } else {
            rcvGenerateFragmentHistory.setVisibility(View.GONE);
            lnlGenQrGotoCreate.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onSaveQr() {
        isGenMenu = false;
        mMainActivity.getBottomNavigationView().setVisibility(View.VISIBLE);
        imvGenerateGotoCreate.setImageResource(R.drawable.imv_history_plus_history);
        saveQRGenerate.setVisibility(View.VISIBLE);
        generateHistoryAdapter = new GenerateHistoryAdapter(getListQrHistory(), false, this, this);
        rcvGenerateFragmentHistory.setAdapter(generateHistoryAdapter);
        lnlGenQrGotoCreate.setVisibility(View.GONE);
        rcvGenerateFragmentHistory.setVisibility(View.VISIBLE);

    }

    public class GenerateReceiver extends BroadcastReceiver {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constant.ACTION_DELETE_MULTIPLE_QRCODE)) {

                for (QrGenerate qrGenerate : qrGenerateList) {
                    if (qrGenerate.isEdit()) {
                        QrGenerateDataBase.getInstance(getActivity()).qrGenerateDao().deleteQrGenerate(qrGenerate);
                    }

                }

                generateHistoryAdapter.setGenerateItemList(getListQrHistory());
                mMainActivity.getCtlMainEditItem().setVisibility(View.GONE);
                mMainActivity.getBottomNavigationView().setVisibility(View.VISIBLE);

                if (qrGenerateList.size() == 0) {
                    imvGenerateEdit.setImageResource(R.drawable.imv_pen_edit_1);
                    rcvGenerateFragmentHistory.setVisibility(View.GONE);
                    lnlGenQrGotoCreate.setVisibility(View.VISIBLE);
                }
            }

            if (intent.getAction().equals(Constant.ACTION_SHARE_MULTIPLE_QRCODE_GEN)) {
                for (int i = 0; i < qrGenerateList.size(); i++) {
                    if (qrGenerateList.get(i).isEdit()) {
                        shareMulti(qrGenerateList.get(i).getContent(), qrGenerateList.get(i).getQrType(), qrGenerateList.get(i).getColor());
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

    private void shareMulti(String s, QrScan.QRType type, int color) {
        if (type != QrScan.QRType.BAR128 || type != QrScan.QRType.BAR93 || type != QrScan.QRType.BAR39) {
            setImage(s, color);
            if (getActivity() != null) {
                sharePalette(getActivity(), bmShare);
            }
        } else {
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            BitMatrix bitMatrix = null;
            if (type == QrScan.QRType.BAR39) {
                try {
                    bitMatrix = multiFormatWriter.encode(s, BarcodeFormat.CODE_39, BITMAP_WIDTH, BITMAP_HEIGHT);
                } catch (WriterException e) {
                    e.printStackTrace();
                }

            } else if (type == QrScan.QRType.BAR93) {
                try {
                    bitMatrix = multiFormatWriter.encode(s, BarcodeFormat.CODE_93, BITMAP_WIDTH, BITMAP_HEIGHT);
                } catch (WriterException e) {
                    e.printStackTrace();
                }

            } else if (type == QrScan.QRType.BAR128) {
                try {
                    bitMatrix = multiFormatWriter.encode(s, BarcodeFormat.CODE_128, BITMAP_WIDTH, BITMAP_HEIGHT);
                } catch (WriterException e) {
                    e.printStackTrace();
                }


            }
            try {
                Bitmap bitmap = Bitmap.createBitmap(BITMAP_WIDTH, BITMAP_HEIGHT, Bitmap.Config.RGB_565);
                for (int i = 0; i < BITMAP_WIDTH; i++) {
                    for (int j = 0; j < BITMAP_HEIGHT; j++) {
                        bitmap.setPixel(i, j, bitMatrix.get(i, j) ? Color.BLACK : Color.WHITE);
                    }
                }
                sharePalette(getActivity(), bitmap);
            } catch (Exception e) {

            }
        }
    }

    @Override
    public void onDestroy() {
        mMainActivity.unregisterReceiver(generateReceiver);
        super.onDestroy();
    }

}