package com.example.qrscaner.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.qrscaner.Model.QrScan;
import com.example.qrscaner.R;
import com.example.qrscaner.view.ResultHistoryQr;


public class ShowHistoryFragment extends Fragment {
    private ResultHistoryQr mResultHistoryQr;
    public static String SEND_DATA_SHOW="send_data_show";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_show_history, container, false);
        mResultHistoryQr = view.findViewById(R.id.rhq_result_fragment);
       Bundle bundle = getArguments();
       if (bundle!=null){
           QrScan qrScan = (QrScan) bundle.getSerializable(SEND_DATA_SHOW);
           mResultHistoryQr.setupData(qrScan);
       }
        return view;
    }
}