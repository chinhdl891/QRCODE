package com.example.qrscaner.fragment;

import static com.example.qrscaner.fragment.ScannerFragment.SEND_QR_SCAN;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.qrscaner.models.QrScan;
import com.example.qrscaner.R;
import com.example.qrscaner.view.ShowResultScanQR;


public class ResultScanFragment extends Fragment {

    private ShowResultScanQR mShowQrHistory;
    private QrScan mQrScan;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_result_scan, container, false);
        mShowQrHistory = view.findViewById(R.id.rhs_result_fragment_Qr);
        Bundle bundle = getArguments();

        if (bundle != null) {
            mQrScan = (QrScan) bundle.getSerializable(SEND_QR_SCAN);
            mShowQrHistory.setupData(mQrScan,getActivity());
        }

        return view;
    }


}