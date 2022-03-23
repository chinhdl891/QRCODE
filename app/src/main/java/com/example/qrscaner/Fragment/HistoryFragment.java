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
import android.widget.LinearLayout;

import com.example.qrscaner.Adapter.HistoryAdapterDate;
import com.example.qrscaner.Model.Date;
import com.example.qrscaner.Model.QrScan;
import com.example.qrscaner.R;

import java.util.ArrayList;
import java.util.List;


public class HistoryFragment extends Fragment {
    private Button btnGotoScan;
    private RecyclerView rcvHistoryScan;
    private LinearLayout lnlHTRGotoScan;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        lnlHTRGotoScan = view.findViewById(R.id.lnl_historyFragment_gotoScan);

        rcvHistoryScan = view.findViewById(R.id.rcv_historyFragment_qrScan);
        HistoryAdapterDate historyAdapterDate = new HistoryAdapterDate(getDateList(), getActivity());
        rcvHistoryScan.setAdapter(historyAdapterDate);
        rcvHistoryScan.setLayoutManager(new LinearLayoutManager(getActivity()));
        btnGotoScan = view.findViewById(R.id.btn_historyFragment_gotoScan);
        btnGotoScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScannerFragment scannerFragment = new ScannerFragment();
                fragmentTransaction.replace(R.id.frame_content, scannerFragment);
                fragmentTransaction.commit();
            }
        });

        return view;
    }

    private List<Date> getDateList() {
        List<Date> dateList = new ArrayList<>();
        dateList.add(new Date(1, 1646102494, getListQr()));
        dateList.add(new Date(2, 1646102494, getListQr()));
        dateList.add(new Date(3, 1646102494, getListQr()));
        if (dateList.size()>0){
            lnlHTRGotoScan.setVisibility(View.GONE);
        }
        return dateList;
    }

    private List<QrScan> getListQr() {
        List<QrScan> qrScanList = new ArrayList<>();
        qrScanList.add(new QrScan("https://www.qr-code-generator.com/solutions/wifi-qr-code/", 1646102494));
        qrScanList.add(new QrScan("https://www.qr-code-generator.com/solutions/wifi-qr-code/", 1646102494));
        qrScanList.add(new QrScan("https://www.qr-code-generator.com/solutions/wifi-qr-code/", 1646102494));
        qrScanList.add(new QrScan("https://www.qr-code-generator.com/solutions/wifi-qr-code/", 1646102494));
        qrScanList.add(new QrScan("https://www.qr-code-generator.com/solutions/wifi-qr-code/", 1646102494));
        qrScanList.add(new QrScan("https://www.qr-code-generator.com/solutions/wifi-qr-code/", 1646102494));
        qrScanList.add(new QrScan("https://www.qr-code-generator.com/solutions/wifi-qr-code/", 1646102494));
        qrScanList.add(new QrScan("https://www.qr-code-generator.com/solutions/wifi-qr-code/", 1646102494));
        qrScanList.add(new QrScan("https://www.qr-code-generator.com/solutions/wifi-qr-code/", 1646102494));
        qrScanList.add(new QrScan("https://www.qr-code-generator.com/solutions/wifi-qr-code/", 1646102494));
        qrScanList.add(new QrScan("https://www.qr-code-generator.com/solutions/wifi-qr-code/", 1646102494));
        qrScanList.add(new QrScan("https://www.qr-code-generator.com/solutions/wifi-qr-code/", 1646102494));
        qrScanList.add(new QrScan("https://www.qr-code-generator.com/solutions/wifi-qr-code/", 1646102494));
        qrScanList.add(new QrScan("https://www.qr-code-generator.com/solutions/wifi-qr-code/", 1646102494));
        return qrScanList;
    }
}