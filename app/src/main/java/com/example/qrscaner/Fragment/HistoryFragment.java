package com.example.qrscaner.Fragment;

import android.content.Intent;
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
import com.example.qrscaner.DataBase.QrHistoryDatabase;
import com.example.qrscaner.Model.QrScan;
import com.example.qrscaner.R;

import java.util.List;


public class HistoryFragment extends Fragment implements View.OnClickListener, HistoryAdapter.CallEditListener, HistoryAdapter.iShareData, HistoryAdapter.iDeleteQr {
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
        historyAdapter = new HistoryAdapter(getListScan(), isCheck, getActivity(), this, this, this, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rcvHistoryScan.setLayoutManager(layoutManager);
        rcvHistoryScan.setAdapter(historyAdapter);
        imvEdit = view.findViewById(R.id.imv_history_historyEdit);
        btnGotoScan.setOnClickListener(this);
        imvEdit.setOnClickListener(this);
        return view;
    }

    private List<QrScan> getListScan() {
        List<QrScan> qrScanList = QrHistoryDatabase.getInstance(getActivity()).qrDao().getListQrHistory();

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
                    historyAdapter = new HistoryAdapter(getListScan(), true, getActivity(), this, this, this, this);
                    rcvHistoryScan.setAdapter(historyAdapter);
                    imvEdit.setImageResource(R.drawable.ic_close);
                    isCheck = true;
                } else {
                    historyAdapter = new HistoryAdapter(getListScan(), false, getActivity(), this, this, this, this);
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
            historyAdapter = new HistoryAdapter(getListScan(), true, getActivity(), this, this, this, this);
            rcvHistoryScan.setAdapter(historyAdapter);
            imvEdit.setImageResource(R.drawable.ic_close);
            isCheck = true;
        } else {
            historyAdapter = new HistoryAdapter(getListScan(), false, getActivity(), this, this, this, this);
            rcvHistoryScan.setAdapter(historyAdapter);
            imvEdit.setImageResource(R.drawable.pen_edit_1);
            isCheck = false;
        }

    }

    @Override
    public void shareDataListener(String data) {
        Intent intentShare = new Intent(Intent.ACTION_SEND);
        intentShare.setType("text/plain");
        intentShare.putExtra(Intent.EXTRA_TEXT, data);
        getActivity().startActivity(intentShare);
    }

    @Override
    public void deleteListener(QrScan qrScan) {
        if (check(getListScan())==1){
            lnlHTRGotoScan.setVisibility(View.VISIBLE);
            rcvHistoryScan.setVisibility(View.GONE);
        }
        QrHistoryDatabase.getInstance(getActivity()).qrDao().deleteQr(qrScan);
        historyAdapter = new HistoryAdapter(getListScan(), false, getActivity(), this, this, this, this);
        rcvHistoryScan.setAdapter(historyAdapter);

    }
    public int check(List<QrScan> qrScanList){
        return qrScanList.size();
    }
}