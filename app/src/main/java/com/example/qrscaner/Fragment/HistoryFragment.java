package com.example.qrscaner.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.qrscaner.Adapter.HistoryAdapter;
import com.example.qrscaner.Adapter.HistoryAdapterItemQr;
import com.example.qrscaner.Model.QrScan;
import com.example.qrscaner.R;

import java.util.ArrayList;
import java.util.List;


public class HistoryFragment extends Fragment implements View.OnClickListener, HistoryAdapterItemQr.CallEditListener {
    private Button btnGotoScan;
    private RecyclerView rcvHistoryScan;
    private LinearLayout lnlHTRGotoScan;
    private ImageView imvEdit;
    private HistoryAdapter historyAdapter;
    private boolean isCheck = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_history, container, false);
        lnlHTRGotoScan = view.findViewById(R.id.lnl_historyFragment_gotoScan);
        btnGotoScan = view.findViewById(R.id.btn_historyFragment_gotoScan);
        rcvHistoryScan = view.findViewById(R.id.rcv_historyFragment_qrScan);
        historyAdapter = new HistoryAdapter(getListScan(), this, isCheck);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rcvHistoryScan.setLayoutManager(layoutManager);
        rcvHistoryScan.setAdapter(historyAdapter);
        imvEdit = view.findViewById(R.id.imv_history_historyEdit);
        btnGotoScan.setOnClickListener(this);
        imvEdit.setOnClickListener(this);
        return view;
    }

    private List<QrScan> getListScan() {
        List<QrScan> qrScanList = new ArrayList<>();
        qrScanList.add(new QrScan(""));
        qrScanList.add(new QrScan(""));
        qrScanList.add(new QrScan(""));
        qrScanList.add(new QrScan(""));

        if (qrScanList.size() > 0) {
            lnlHTRGotoScan.setVisibility(View.GONE);
        }
        return qrScanList;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_historyFragment_gotoScan:
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                ScannerFragment scannerFragment = new ScannerFragment();
                fragmentTransaction.replace(R.id.frame_content, scannerFragment);
                fragmentTransaction.commit();
                break;
            case R.id.imv_history_historyEdit:
                if (!isCheck) {
                    historyAdapter = new HistoryAdapter(getListScan(), this, true);
                    rcvHistoryScan.setAdapter(historyAdapter);
                    imvEdit.setImageResource(R.drawable.ic_close);
                    isCheck = true;
                } else {
                    historyAdapter = new HistoryAdapter(getListScan(), this, false);
                    rcvHistoryScan.setAdapter(historyAdapter);
                    imvEdit.setImageResource(R.drawable.pen_edit_1);
                    isCheck = false;
                }
                break;
        }
    }


    @Override
    public void edit(Boolean isEdit) {
        if (!isEdit) {
            historyAdapter = new HistoryAdapter(getListScan(), this, true);
            rcvHistoryScan.setAdapter(historyAdapter);
            imvEdit.setImageResource(R.drawable.ic_close);
            isCheck = true;
        } else {
            historyAdapter = new HistoryAdapter(getListScan(), this, false);
            rcvHistoryScan.setAdapter(historyAdapter);
            imvEdit.setImageResource(R.drawable.pen_edit_1);
            isCheck = false;
        }

    }
}