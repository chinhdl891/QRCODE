package com.example.qrscaner.fragment;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.qrscaner.Model.QrScan;
import com.example.qrscaner.R;
import com.example.qrscaner.SendData;
import com.example.qrscaner.activity.MainActivity;
import com.example.qrscaner.myshareferences.MyDataLocal;
import com.example.qrscaner.view.ShowQrGenerate;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.io.IOException;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class ScannerFragment extends Fragment implements ZXingScannerView.ResultHandler {
    public static ZXingScannerView zXingScannerView;

    private SendData sendData;
    private String strImgFromPhoto, mCameraId;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private ImageView imvScanFragmentOpenCam, imvScanFragmentSwitchFlash;
    private boolean isFlash;
    private Vibrator vibrator;
    private ShowQrGenerate resultScan;
    private LinearLayout lnlScanFragmentZoom;
    private CardView cvScanFragmentMenu;
    private MainActivity mMainActivity;
    private SeekBar mSkbScannerFragmentZoom;
    public static String SEND_QR_SCAN = "send_obj_scan";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_scanner, container, false);
        init(view);


        imvScanFragmentSwitchFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFlashLight(isFlash);
            }
        });
        imvScanFragmentOpenCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity()
                            , new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                } else {
                    selectImage();
                }

            }
        });

        return view;
    }

    private void init(View view) {
        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        imvScanFragmentOpenCam = view.findViewById(R.id.imv_ScanFragment_openPhoto);
        imvScanFragmentSwitchFlash = view.findViewById(R.id.imv_scanFragment_openFlash);
        zXingScannerView = view.findViewById(R.id.scv_ScanFragment_view);
        resultScan = view.findViewById(R.id.rhs_scanFragment_show);
        lnlScanFragmentZoom = view.findViewById(R.id.lnl_scanFragment_zoom);
        cvScanFragmentMenu = view.findViewById(R.id.cv_fragment_scanner_menu);
        mMainActivity = (MainActivity) getActivity();


    }


    public void switchFlashLight(boolean status) {
        if (!status) {
            zXingScannerView.setFlash(true);
            isFlash = true;
        } else {
            zXingScannerView.setFlash(false);
            isFlash = false;
        }
    }

    private void selectImage() {
        zXingScannerView.stopCameraPreview();
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Image"), 100);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectImage();
        } else {
            Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                resumeCamera();
                Bitmap bMap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                int[] intArray = new int[bMap.getWidth() * bMap.getHeight()];
                bMap.getPixels(intArray, 0, bMap.getWidth(), 0, 0, bMap.getWidth(), bMap.getHeight());
                LuminanceSource source = new RGBLuminanceSource(bMap.getWidth(), bMap.getHeight(), intArray);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                Reader reader = new MultiFormatReader();
                Result result = reader.decode(bitmap);
                strImgFromPhoto = result.getText();
                processQr(strImgFromPhoto);

            } catch (IOException | ChecksumException | NotFoundException | FormatException e) {
                e.printStackTrace();
                processQr("Error");
            }
        }
    }

    private void processQr(String s) {
        QrScan qrScan = new QrScan();
        qrScan.setScanText(s);
        qrScan.setDate();
        zXingScannerView.setVisibility(View.GONE);
        lnlScanFragmentZoom.setVisibility(View.GONE);
        cvScanFragmentMenu.setVisibility(View.GONE);


        ResultScanFragment fragment = new ResultScanFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(SEND_QR_SCAN,qrScan);
        fragment.setArguments(bundle);
        mMainActivity.fragmentLoad(fragment,ResultScanFragment.class.getSimpleName());

    }

    @Override
    public void handleResult(Result result) {
        if (MyDataLocal.getVibrate()) {
            VibrationEffect vibrationEffect;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                vibrationEffect = VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE);
                vibrator.cancel();
                vibrator.vibrate(vibrationEffect);
            }
        } else if (MyDataLocal.getBeep()) {
            MediaPlayer mediaPlayer = MediaPlayer.create(getActivity(), R.raw.beep);
            mediaPlayer.start();
        }
        processQr(result.getText());

    }


    @Override
    public void onResume() {
        super.onResume();
        resumeCamera();
    }


    @Override
    public void onPause() {
        super.onPause();
        zXingScannerView.stopCameraPreview();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        sendData = ((SendData) context);
    }


    public  void resumeCamera() {

        zXingScannerView.startCamera();
        zXingScannerView.setResultHandler(this);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        zXingScannerView.stopCameraPreview();

    }



}