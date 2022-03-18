package com.example.qrscaner.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.qrscaner.Model.QrEmail;
import com.example.qrscaner.Model.QrMess;
import com.example.qrscaner.Model.QrText;
import com.example.qrscaner.Model.QrUrl;
import com.example.qrscaner.R;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.Arrays;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScannerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScannerFragment extends Fragment {
    ZXingScannerView zXingScannerView;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final String TAG = "aaa";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ScannerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScannerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScannerFragment newInstance(String param1, String param2) {
        ScannerFragment fragment = new ScannerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_scanner, container, false);
        zXingScannerView = view.findViewById(R.id.scannerView);
        Dexter.withContext(getActivity()).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                zXingScannerView.setResultHandler(new ZXingScannerView.ResultHandler() {
                    @Override
                    public void handleResult(Result result) {
//                        Toast.makeText(getActivity(), result.getText(), Toast.LENGTH_SHORT).show();
                        processQr(result.getText());

                    }
                });
                zXingScannerView.startCamera();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

            }
        }).check();


        return view;
    }

    private void processQr(String s) {

        String[] content = s.split(":");

        if (content[0].equals("SMSTO")) {
            StringBuilder contentSMS = new StringBuilder();
            QrMess qrMess = new QrMess();
            qrMess.nguoiGui = content[1];
            for (int i = 2; i < content.length; i++) {
                contentSMS.append(content[i]);
            }
            qrMess.content = contentSMS.toString();

        } else if (content[0].equals("http") || content[0].equals("https")) {
            QrUrl qrUrl = new QrUrl();
            StringBuilder link = new StringBuilder();
            for (int i = 0; i < content.length; i++) {
                if (i == 1) {
                    link.append(":");
                }
                link.append(content[i]);

            }
            Log.e("url", link.toString());
        } else if (content[0].equals("WIFI")) {
            StringBuilder stringBuilder = new StringBuilder();
            String[] contentWifi = s.split(";");
            String name = "";
            String pass = "";
            for (String value : contentWifi) {
                stringBuilder.append(value);
            }
            String contentWifi2 = stringBuilder.toString();
            String[] contentWifi3 = contentWifi2.split(":");
            name = contentWifi3[3].replace("P","").trim();
            pass = contentWifi3[4].replace("H","").trim();
            Log.e(TAG, "ID"+name+"   Pass"+pass );

        } else if (content[0].equals("MATMSG")){
            String email = "";
            String emailSubject = "";
            String contentEmailSend = "";
            StringBuilder stringBuilder = new StringBuilder();
            for (String value : content) {
//                stringBuilder.append(value);
//                Log.e(TAG, "processQr: ", );
            }
//            String contentEmail = stringBuilder.toString();
//            String[] contentEmail1 = contentEmail.split(";");
//

        }else {
            Log.e(TAG, s );
            QrText qrText = new QrText();
            qrText.text = s;
        }

    }

    @Override
    public void onDestroy() {
        zXingScannerView.stopCamera();
        super.onDestroy();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "camera permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }
}