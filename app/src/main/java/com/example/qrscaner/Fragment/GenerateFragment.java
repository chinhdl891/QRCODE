package com.example.qrscaner.Fragment;

import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.qrscaner.Adapter.BARCODEAdapter;
import com.example.qrscaner.Adapter.QrCodeAdapter;
import com.example.qrscaner.Model.GenerateItem;
import com.example.qrscaner.R;
import com.example.qrscaner.view.generate.ViewGenerateQRCode;

import java.util.ArrayList;
import java.util.List;

public class GenerateFragment extends Fragment implements BARCODEAdapter.iCreateQr, View.OnClickListener {
    private RecyclerView rcvGenerateFragmentQrCode, rcvGenerateFragmentBarCode;
    private ViewGenerateQRCode viewGenerateQRCode;
    private Button btnGenerateGoTo;
    private LinearLayout lnlGenQrGotoCreate;
    private NestedScrollView nsvGenQrItem;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_generate, container, false);
        rcvGenerateFragmentBarCode = view.findViewById(R.id.rcv_generate_barcode);
        nsvGenQrItem = view.findViewById(R.id.nsv_generate_create);
        lnlGenQrGotoCreate = view.findViewById(R.id.nsv_genFragment_gotoGenerate);
        rcvGenerateFragmentQrCode = view.findViewById(R.id.rcv_generate_qrcode);
        btnGenerateGoTo = view.findViewById(R.id.btn_generate_create);
        BARCODEAdapter barcodeAdapter = new BARCODEAdapter(getBarCode(), getActivity(), this);
        QrCodeAdapter adapterBarCode = new QrCodeAdapter(getQrCode(), getActivity(), this);
        rcvGenerateFragmentBarCode.setAdapter(barcodeAdapter);
        rcvGenerateFragmentQrCode.setAdapter(adapterBarCode);
        rcvGenerateFragmentQrCode.setLayoutManager(new GridLayoutManager(getActivity(), 3, RecyclerView.VERTICAL, false));
        rcvGenerateFragmentBarCode.setLayoutManager(new GridLayoutManager(getActivity(), 3, RecyclerView.VERTICAL, false));
        viewGenerateQRCode = view.findViewById(R.id.vgq_generate_createQr);
        btnGenerateGoTo.setOnClickListener(this);
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


    @Override
    public void createListener(int id) {

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
        }
    }
}