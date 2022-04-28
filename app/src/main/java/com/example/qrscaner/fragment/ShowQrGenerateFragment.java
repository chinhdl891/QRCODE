package com.example.qrscaner.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.qrscaner.models.QrGenerate;
import com.example.qrscaner.R;
import com.example.qrscaner.view.ShowQrGenerate;


public class ShowQrGenerateFragment extends Fragment {

    public static final String SEND_GEN_QR = "send_qr_gen";
    private ShowQrGenerate showQrHistory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_qr_generate, container, false);
        showQrHistory = view.findViewById(R.id.sqr_show_qr_generate);
        savedInstanceState = getArguments();
        if (savedInstanceState != null) {
            QrGenerate qrGenerate = (QrGenerate) savedInstanceState.getSerializable(SEND_GEN_QR);
            showQrHistory.setupData(qrGenerate, getActivity());

        }

        return view;
    }
}