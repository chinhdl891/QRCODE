package com.example.qrscaner.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.qrscaner.Model.QrEmail;
import com.example.qrscaner.Model.QrMess;
import com.example.qrscaner.Model.QrProduct;
import com.example.qrscaner.Model.QrText;
import com.example.qrscaner.Model.QrUrl;
import com.example.qrscaner.Model.QrWifi;
import com.example.qrscaner.Model.QreTelephone;
import com.example.qrscaner.activity.MainActivity;
import com.example.qrscaner.adapter.HistoryAdapter;
import com.example.qrscaner.DataBase.QrHistoryDatabase;
import com.example.qrscaner.Model.QrScan;
import com.example.qrscaner.R;
import com.example.qrscaner.myshareferences.MyDataLocal;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;


public class HistoryFragment extends Fragment implements View.OnClickListener, HistoryAdapter.ISelectItem, HistoryAdapter.CallEditListener, HistoryAdapter.IShareData, HistoryAdapter.IDeleteQr {
    private Button btnGotoScan;
    private RecyclerView rcvHistoryScan;
    private LinearLayout lnlHTRGotoScan;
    private ImageView imvEdit;
    private HistoryAdapter historyAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private MainActivity mMainActivity;
    private boolean isCheck = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_history, container, false);
        lnlHTRGotoScan = view.findViewById(R.id.lnl_historyFragment_gotoScan);
        btnGotoScan = view.findViewById(R.id.btn_historyFragment_gotoScan);
        rcvHistoryScan = view.findViewById(R.id.rcv_historyFragment_qrScan);
        mMainActivity = (MainActivity) getActivity();
        historyAdapter = new HistoryAdapter(getListScan(), this, isCheck, getActivity(), this, this, this, this);
        if (MyDataLocal.getShowHistory()) {
            layoutManager = new LinearLayoutManager(getActivity());
            rcvHistoryScan.setLayoutManager(layoutManager);
        } else {
            lnlHTRGotoScan.setVisibility(View.VISIBLE);
        }

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
                    historyAdapter = new HistoryAdapter(getListScan(), this, true, getActivity(), this, this, this, this);
                    rcvHistoryScan.setAdapter(historyAdapter);
                    imvEdit.setImageResource(R.drawable.ic_close);
                    isCheck = true;
                } else {
                    historyAdapter = new HistoryAdapter(getListScan(), this, false, getActivity(), this, this, this, this);
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
            historyAdapter = new HistoryAdapter(getListScan(), this, true, getActivity(), this, this, this, this);
            rcvHistoryScan.setAdapter(historyAdapter);
            imvEdit.setImageResource(R.drawable.ic_close);
            isCheck = true;
        } else {
            historyAdapter = new HistoryAdapter(getListScan(), this, false, getActivity(), this, this, this, this);
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
        if (check(getListScan()) == 1) {
            lnlHTRGotoScan.setVisibility(View.VISIBLE);
            rcvHistoryScan.setVisibility(View.GONE);
        }
        QrHistoryDatabase.getInstance(getActivity()).qrDao().deleteQr(qrScan);
        historyAdapter = new HistoryAdapter(getListScan(), this, false, getActivity(), this, this, this, this);
        rcvHistoryScan.setAdapter(historyAdapter);

    }

    public int check(List<QrScan> qrScanList) {
        return qrScanList.size();
    }

    @Override
    public void selectItemListener(List<QrScan> qrScanList) {

        if (qrScanList.size() > 0) {
            mMainActivity.getBottomNavigationView().setVisibility(View.GONE);
            mMainActivity.getCtlMainEditItem().setVisibility(View.VISIBLE);
            mMainActivity.getTvMainNumItem().setText(qrScanList.size() + "");
            mMainActivity.getImvMainItemShare().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int i = 0; i < qrScanList.size(); i++) {
                        QrScan qrScan = qrScanList.get(i);
                        String[] content = qrScan.getScanText().split(":");
                        if (qrScan.getTypeQR() == QrScan.QRType.EMAIL) {
                            QrEmail qrEmail = new QrEmail();
                            qrEmail.compileEmail(content);
                            shareDataListener(qrEmail.getShare());
//                shareDataListener.shareDataListener(qrEmail.getShare());
                        } else if (qrScan.getTypeQR() == QrScan.QRType.SMS) {
                            QrMess qrMess = new QrMess();
                            qrMess.compileSMS(content);
                            shareDataListener(qrMess.getShare());
//                shareDataListener.shareDataListener(qrMess.getShare());
                        } else if (qrScan.getTypeQR() == QrScan.QRType.PRODUCT) {
                            QrProduct qrProduct = (QrProduct) qrScan;
                            qrProduct.compileProduct(qrScan.getScanText());
                            shareDataListener(qrProduct.getShare());
//                shareDataListener.shareDataListener(qrProduct.getShare());
                        } else if (qrScan.getTypeQR() == QrScan.QRType.WIFI) {
                            QrWifi qrWifi = new QrWifi();
                            StringBuilder stringBuilder = new StringBuilder();
                            String[] contentWifi = qrScan.getScanText().split(";");
                            for (String value : contentWifi) {
                                stringBuilder.append(value);
                            }
                            String contentWifi2 = stringBuilder.toString();
                            String[] contentWifi3 = contentWifi2.split(":");
                            qrWifi.compileWifi(contentWifi, contentWifi3);
                            shareDataListener(qrWifi.getShare());
//                shareDataListener.shareDataListener(qrWifi.getShare());
                        } else if (qrScan.getTypeQR() == QrScan.QRType.PHONE) {
                            QreTelephone qreTelephone = new QreTelephone();
                            qreTelephone.compile(content);
                            shareDataListener(qreTelephone.getShare());
//                shareDataListener.shareDataListener(qreTelephone.getShare());
                        } else if (qrScan.getTypeQR() == QrScan.QRType.TEXT) {
                            QrText qrText = new QrText();
                            shareDataListener(qrText.getShare());
//                shareDataListener.shareDataListener(qrText.getShare());
                        } else if (qrScan.getTypeQR() == QrScan.QRType.URL) {
                            QrUrl qrUrl = new QrUrl();
                            shareDataListener(qrUrl.getShare());
                        }
                    }
                }
            });
            mMainActivity.getImvMainItemDelete().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int i = 0; i < qrScanList.size() ; i++) {
                       QrHistoryDatabase.getInstance(getActivity()).qrDao().deleteQr(qrScanList.get(i));
                    }
                   if (getListScan().size()==0){
                       mMainActivity.getBottomNavigationView().setVisibility(View.VISIBLE);
                       mMainActivity.getCtlMainEditItem().setVisibility(View.GONE);
                       lnlHTRGotoScan.setVisibility(View.VISIBLE);

                   }
                   updateData();
                }
            });

        } else {
            mMainActivity.getBottomNavigationView().setVisibility(View.VISIBLE);
            mMainActivity.getCtlMainEditItem().setVisibility(View.GONE);
        }


    }
public void updateData(){
    historyAdapter = new HistoryAdapter(getListScan(), this, isCheck, getActivity(), this, this, this, this);
    rcvHistoryScan.setAdapter(historyAdapter);
}

}