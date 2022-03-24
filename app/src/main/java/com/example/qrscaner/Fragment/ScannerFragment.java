package com.example.qrscaner.Fragment;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.CAMERA_SERVICE;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Color;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.qrscaner.Model.QrScan;
import com.example.qrscaner.R;
import com.example.qrscaner.SendData;
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
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.io.IOException;
import java.security.Policy;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


///**
// * A simple {@link Fragment} subclass.
// * Use the {@link ScannerFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class ScannerFragment extends Fragment {
    public static ZXingScannerView zXingScannerView;

    private SendData sendData;
    private String strImgFromPhoto, mCameraId;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final String TAG = "scanLog";
    private ImageView imvScanFragmentOpenCam, imvScanFragmentSwitchFlash;
    private boolean isFlash = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scanner, container, false);
        zXingScannerView = view.findViewById(R.id.scv_ScanFragment_view);
      zXingScannerView.setResultHandler(new ZXingScannerView.ResultHandler() {
          @Override
          public void handleResult(Result result) {
              processQr(result.getText());
          }
      });
        imvScanFragmentOpenCam = view.findViewById(R.id.imv_ScanFragment_openPhoto);
        imvScanFragmentSwitchFlash = view.findViewById(R.id.imv_scanFragment_openFlash);
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
        Log.e(TAG, s);
        qrScan.setDate();
        sendData.sendQr(qrScan);

    }

    @Override
    public void onDestroy() {
        zXingScannerView.stopCamera();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        zXingScannerView.startCamera();
        super.onResume();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        sendData = ((SendData) context);
    }


}