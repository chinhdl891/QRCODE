package com.example.qrscaner.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
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

import java.util.ArrayList;
import java.util.List;

public class GenerateFragment extends Fragment {
    private RecyclerView rcvGenerateFragmentQrCode, rcvGenerateFragmentBarCode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_generate, container, false);
        rcvGenerateFragmentBarCode = view.findViewById(R.id.rcv_generate_barcode);
        rcvGenerateFragmentQrCode = view.findViewById(R.id.rcv_generate_qrcode);
        BARCODEAdapter barcodeAdapter = new BARCODEAdapter(getBarCode(), getActivity());
        QrCodeAdapter adapterBarCode = new QrCodeAdapter(getQrCode(), getActivity());
        rcvGenerateFragmentBarCode.setAdapter(barcodeAdapter);
        rcvGenerateFragmentQrCode.setAdapter(adapterBarCode);
        rcvGenerateFragmentQrCode.setLayoutManager(new GridLayoutManager(getActivity(), 3, RecyclerView.VERTICAL, false));
        rcvGenerateFragmentBarCode.setLayoutManager(new GridLayoutManager(getActivity(), 3, RecyclerView.VERTICAL, false));

        return view;
    }

    private List<GenerateItem> getBarCode() {
        List<GenerateItem> generateItems = new ArrayList<>();
        generateItems.add(new GenerateItem(0, getResources().getIdentifier("com.example.qrscaner:drawable/ic_barcoder3996128", null, null), "Code 39"));
        generateItems.add(new GenerateItem(1, R.drawable.ic_barcoder3996128, "Code 93"));
        generateItems.add(new GenerateItem(2, R.drawable.ic_barcoder3996128, "Code 128"));
        generateItems.add(new GenerateItem(3, R.drawable.ic_barcoder3996128, "Pdf 417"));
        generateItems.add(new GenerateItem(4, R.drawable.ic_barcoder3996128, "Coda bar"));
        generateItems.add(new GenerateItem(5, R.drawable.ic_data_matrix, "Data Matrix"));
        generateItems.add(new GenerateItem(6, R.drawable.ic_aztec, "AZTEC"));
        generateItems.add(new GenerateItem(7, R.drawable.ic_barcoder3996128, "EAN8"));
        generateItems.add(new GenerateItem(8, R.drawable.ic_barcoder3996128, "EAN13"));
        generateItems.add(new GenerateItem(9, R.drawable.ic_barcoder3996128, "UPC A"));
        generateItems.add(new GenerateItem(10, R.drawable.ic_barcoder3996128, "UPC E"));
        generateItems.add(new GenerateItem(11, R.drawable.ic_barcoder3996128, "ITF"));
        generateItems.add(new GenerateItem(12, R.drawable.ic_maxi_code, "MAXI CODE"));
        return generateItems;
    }

    private List<GenerateItem> getQrCode() {
        List<GenerateItem> generateItems = new ArrayList<>();
        generateItems.add(new GenerateItem(1, R.drawable.add_call, "Phone"));
        generateItems.add(new GenerateItem(2, R.drawable.add_email, "Email"));
        generateItems.add(new GenerateItem(3, R.drawable.add_uri, "Uri"));
        generateItems.add(new GenerateItem(4, R.drawable.add_sms, "SMS"));
        generateItems.add(new GenerateItem(5, R.drawable.add_contact, "Contact"));
        generateItems.add(new GenerateItem(6, R.drawable.add_text, "Text"));
        generateItems.add(new GenerateItem(7, R.drawable.add_wifi, "Wifi"));
        generateItems.add(new GenerateItem(8, R.drawable.add_calendar, "Calendar"));
        generateItems.add(new GenerateItem(9, R.drawable.add_location, "Location"));
        return generateItems;
    }


}