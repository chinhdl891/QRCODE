package com.example.qrscaner.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.qrscaner.DataBase.QrGenerateDataBase;
import com.example.qrscaner.Model.QrGenerate;
import com.example.qrscaner.adapter.BARCODEGenerateAdapter;
import com.example.qrscaner.adapter.GenerateHistoryAdapter;
import com.example.qrscaner.adapter.QrCodeGenerateAdapter;
import com.example.qrscaner.Model.GenerateItem;
import com.example.qrscaner.R;
import com.example.qrscaner.view.generate.ViewGenerateQRCode;

import java.util.ArrayList;
import java.util.List;

public class GenerateFragment extends Fragment implements BARCODEGenerateAdapter.iCreateQr, View.OnClickListener, ViewGenerateQRCode.ISaveQrGenerate, GenerateHistoryAdapter.IShare, GenerateHistoryAdapter.IDelete, GenerateHistoryAdapter.IEdit {
    private RecyclerView rcvGenerateFragmentQrCode, rcvGenerateFragmentBarCode, rcvGenerateFragmentHistory;
    private ViewGenerateQRCode viewGenerateQRCode;
    private Button btnGenerateGoTo;
    private LinearLayout lnlGenQrGotoCreate;
    private NestedScrollView nsvGenQrItem;
    private List<QrGenerate> qrGenerateList;
    private GenerateHistoryAdapter generateHistoryAdapter;
    private ImageView imvGenerateGotoCreate,imvGenerateEdit;
    private boolean edit;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_generate, container, false);
        rcvGenerateFragmentHistory = view.findViewById(R.id.rcv_generate_fragment_history);
        rcvGenerateFragmentBarCode = view.findViewById(R.id.rcv_generate_barcode);
        imvGenerateGotoCreate = view.findViewById(R.id.imv_generate_create_qr);
        nsvGenQrItem = view.findViewById(R.id.nsv_generate_create);
        lnlGenQrGotoCreate = view.findViewById(R.id.nsv_genFragment_gotoGenerate);
        rcvGenerateFragmentQrCode = view.findViewById(R.id.rcv_generate_qrcode);
        btnGenerateGoTo = view.findViewById(R.id.btn_generate_create);
        BARCODEGenerateAdapter barcodeAdapter = new BARCODEGenerateAdapter(getBarCode(), getActivity(), this);
        QrCodeGenerateAdapter adapterBarCode = new QrCodeGenerateAdapter(getQrCode(), getActivity(), this);
        rcvGenerateFragmentBarCode.setAdapter(barcodeAdapter);
        rcvGenerateFragmentQrCode.setAdapter(adapterBarCode);
        rcvGenerateFragmentQrCode.setLayoutManager(new GridLayoutManager(getActivity(), 3, RecyclerView.VERTICAL, false));
        rcvGenerateFragmentBarCode.setLayoutManager(new GridLayoutManager(getActivity(), 3, RecyclerView.VERTICAL, false));
        rcvGenerateFragmentHistory.setLayoutManager(new LinearLayoutManager(getActivity()));
        generateHistoryAdapter = new GenerateHistoryAdapter(getListQrHistory(),  edit,this,this,this);
        rcvGenerateFragmentHistory.setAdapter(generateHistoryAdapter);
        viewGenerateQRCode = view.findViewById(R.id.vgq_generate_createQr);
        imvGenerateEdit = view.findViewById(R.id.imv_generate_edit);
        btnGenerateGoTo.setOnClickListener(this);
        imvGenerateGotoCreate.setOnClickListener(this);
        imvGenerateEdit.setOnClickListener(this);
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
            case R.id.imv_generate_create_qr:
                nsvGenQrItem.setVisibility(View.VISIBLE);
                lnlGenQrGotoCreate.setVisibility(View.GONE);
                rcvGenerateFragmentHistory.setVisibility(View.GONE);
                break;
            case R.id.imv_generate_edit:
                if (!edit){
                    edit = true;
                    generateHistoryAdapter = new GenerateHistoryAdapter(getListQrHistory(),  edit,this,this,this);
                    rcvGenerateFragmentHistory.setAdapter(generateHistoryAdapter);
                    imvGenerateEdit.setImageResource(R.drawable.ic_close);

                }else {
                    imvGenerateEdit.setImageResource(R.drawable.pen_edit_1);
                    edit = false;
                    generateHistoryAdapter = new GenerateHistoryAdapter(getListQrHistory(),  edit,this,this,this);
                    rcvGenerateFragmentHistory.setAdapter(generateHistoryAdapter);

                }
                break;

        }
    }

    @Override
    public void saveQr(QrGenerate qrGenerate) {
        QrGenerateDataBase.getInstance(getActivity()).qrGenerateDao().insertQrGenerate(qrGenerate);
        lnlGenQrGotoCreate.setVisibility(View.GONE);
        rcvGenerateFragmentHistory.setVisibility(View.VISIBLE);
        qrGenerateList.add(qrGenerate);
        generateHistoryAdapter.notifyDataSetChanged();
        nsvGenQrItem.setVisibility(View.GONE);
    }

    @Override
    public void share(String s) {
        Intent intentShare = new Intent(Intent.ACTION_SEND);
        intentShare.setType("text/plain");
        intentShare.putExtra(Intent.EXTRA_TEXT, s);
        getActivity().startActivity(intentShare);
    }

    @Override
    public void delete(QrGenerate qrGenerate, int position) {
        QrGenerateDataBase.getInstance(getActivity()).qrGenerateDao().deleteQrGenerate(qrGenerate);
        qrGenerateList.remove(position);
        generateHistoryAdapter.notifyDataSetChanged();
        if (qrGenerateList.size() == 0) {
            lnlGenQrGotoCreate.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void edit(boolean isEdit) {
        if (isEdit){
            edit = true;
            generateHistoryAdapter = new GenerateHistoryAdapter(getListQrHistory(),  edit,this,this,this);
            rcvGenerateFragmentHistory.setAdapter(generateHistoryAdapter);
            imvGenerateEdit.setImageResource(R.drawable.pen_edit_1);
        }else {
            edit = true;
            generateHistoryAdapter = new GenerateHistoryAdapter(getListQrHistory(),  edit,this,this,this);
            rcvGenerateFragmentHistory.setAdapter(generateHistoryAdapter);
            imvGenerateEdit.setImageResource(R.drawable.ic_close);
        }
    }
}