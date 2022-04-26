package com.example.qrscaner.fragment;

import static com.example.qrscaner.fragment.ScannerFragment.SEND_QR_SCAN;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.qrscaner.Model.QrScan;
import com.example.qrscaner.R;
import com.example.qrscaner.activity.MainActivity;
import com.example.qrscaner.view.ResultScanQr;


public class ResultScanFragment extends Fragment {

    private ResultScanQr mResultScanQr;
    private QrScan mQrScan;

//    public static ResultScanFragment newInstance(String data){
//        Bundle args = new Bundle();
//        args.putString("data",);
//        ResultScanFragment fragment = new ResultScanFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // this.getArguments().getSerializable(SEND_QR_SCAN);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_result_scan, container, false);
        mResultScanQr = view.findViewById(R.id.rhs_result_fragment_Qr);
        Bundle bundle = getArguments();

        if (bundle != null) {
            mQrScan = (QrScan) bundle.getSerializable(SEND_QR_SCAN);
            mResultScanQr.setupData(mQrScan,getActivity());
        }

        return view;
    }


}