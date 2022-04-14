package com.example.qrscaner.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import com.example.qrscaner.Model.QrEmail;
import com.example.qrscaner.Model.QrMess;
import com.example.qrscaner.Model.QrProduct;
import com.example.qrscaner.Model.QrUrl;
import com.example.qrscaner.Model.QrWifi;
import com.example.qrscaner.Model.QreTelephone;
import com.example.qrscaner.activity.MainActivity;
import com.example.qrscaner.adapter.HistoryAdapter;
import com.example.qrscaner.DataBase.QrHistoryDatabase;
import com.example.qrscaner.Model.QrScan;
import com.example.qrscaner.R;
import com.example.qrscaner.config.Constant;
import com.example.qrscaner.myshareferences.MyDataLocal;

import java.util.ArrayList;
import java.util.List;


public class HistoryFragment extends Fragment implements View.OnClickListener, HistoryAdapter.HistoryAdapterListener {
    private Button btnGotoScan;
    private RecyclerView rcvHistoryScan;
    private LinearLayout lnlHTRGotoScan;
    private ImageView imvEdit;
    private HistoryAdapter historyAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private MainActivity mMainActivity;
    private QRHistoryReceiver qrHistoryReceiver;
    private List<QrScan> mQRScannedList = new ArrayList<>();
    private boolean isEditable = false;
    private int mNumQRSelect = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_history, container, false);
        init(view);
        getListScan();
        historyAdapter = new HistoryAdapter(getActivity(), mQRScannedList, this);

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
        qrHistoryReceiver = new QRHistoryReceiver();
        mMainActivity.registerReceiver(qrHistoryReceiver, new IntentFilter(Constant.ACTION_DELETE_MULTIPLE_QRCODE));
        mMainActivity.registerReceiver(qrHistoryReceiver, new IntentFilter(Constant.ACTION_SHARE_MULTIPLE_QRCODE_GEN));

        return view;
    }

    private void init(View view) {
        lnlHTRGotoScan = view.findViewById(R.id.lnl_historyFragment__gotoScan);
        btnGotoScan = view.findViewById(R.id.btn_historyFragment__gotoScan);
        rcvHistoryScan = view.findViewById(R.id.rcv_historyFragment__qrScan);
        mMainActivity = (MainActivity) getActivity();
    }

    private void getListScan() {
        mQRScannedList = QrHistoryDatabase.getInstance(getActivity()).qrDao().getListQrHistory();
        if (mQRScannedList.size() > 0) {
            lnlHTRGotoScan.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View view) {
        if (view == btnGotoScan) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            ScannerFragment scannerFragment = new ScannerFragment();
            fragmentTransaction.replace(R.id.frame_main__content, scannerFragment);
            fragmentTransaction.commit();
        } else if (view == imvEdit) {
            if (!isEditable) {
                imvEdit.setImageResource(R.drawable.ic_close);
                isEditable = true;
            } else {
                imvEdit.setImageResource(R.drawable.pen_edit_1);
                isEditable = false;
                mMainActivity.getBottomNavigationView().setVisibility(View.VISIBLE);
                mMainActivity.getCtlMainEditItem().setVisibility(View.GONE);
                mNumQRSelect = 0;
                for (QrScan qrScan : mQRScannedList) {
                    qrScan.setEdit(false);
                }
                if (mQRScannedList.size() == 0) {
                    rcvHistoryScan.setVisibility(View.GONE);
                    lnlHTRGotoScan.setVisibility(View.VISIBLE);
                }
            }
            historyAdapter.setEdit(isEditable);
        }

    }

    @Override
    public void onDestroyView() {
        mMainActivity.unregisterReceiver(qrHistoryReceiver);
        super.onDestroyView();
    }

    @Override
    public void onShareQRSelected(QrScan qrCode) {
        String[] content = qrCode.getScanText().split(":");
        String shareContent = "";
        switch (qrCode.getTypeQR()) {
            case WIFI:
                QrWifi qrWifi = new QrWifi();
                StringBuilder stringBuilder = new StringBuilder();
                String[] contentWifi = qrCode.getScanText().split(";");
                for (String value : contentWifi) {
                    stringBuilder.append(value);
                }
                String contentWifi2 = stringBuilder.toString();
                String[] contentWifi3 = contentWifi2.split(":");
                qrWifi.compileWifi(contentWifi, contentWifi3);
                shareContent = qrWifi.getShare();
                break;
            case TEXT:
                shareContent = qrCode.getScanText();
                break;
            case PHONE:
                QreTelephone qreTelephone = new QreTelephone();
                qreTelephone.compile(content);
                shareContent = qreTelephone.getShare();
                break;
            case EMAIL:
                QrEmail qrEmail = new QrEmail();
                qrEmail.compileEmail(content);
                shareContent = qrEmail.getShare();
                break;
            case SMS:
                QrMess qrMess = new QrMess();
                qrMess.compileSMS(content);
                shareContent = qrMess.getShare();
                break;
            case URL:
                QrUrl qrUrl = new QrUrl();
                qrUrl.compileUrl(content);
                shareContent = qrUrl.getShare();
                break;
            case PRODUCT:
                QrProduct qrProduct = (QrProduct) qrCode;
                qrProduct.compileProduct(qrCode.getScanText());
                shareContent = qrProduct.getShare();
                break;
            case ERROR:
                break;
            case BAR39:
                break;
            case BAR93:
                break;
            case BAR128:
                break;
            default:
                break;
        }
        Intent intentShare = new Intent(Intent.ACTION_SEND);
        intentShare.setType("text/plain");
        intentShare.putExtra(Intent.EXTRA_TEXT, shareContent);
        getActivity().startActivity(intentShare);
    }

    @Override
    public void onEditHistory(boolean isEdit) {
        if (isEdit) {
            imvEdit.setImageResource(R.drawable.ic_close);
            isEditable = isEdit;
        }
    }

    @Override
    public void onDeleteQRSelected(QrScan qrScan) {
        deleteQR(qrScan);
        if (mQRScannedList.size() == 0) {
            rcvHistoryScan.setVisibility(View.GONE);
            lnlHTRGotoScan.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemSelected(boolean isSelect) {
        if (isSelect) {
            mNumQRSelect++;
        } else {
            mNumQRSelect--;
        }

        if (mNumQRSelect > 0) {
            mMainActivity.getBottomNavigationView().setVisibility(View.GONE);
            mMainActivity.getCtlMainEditItem().setVisibility(View.VISIBLE);
            mMainActivity.getTvMainNumItem().setText(mNumQRSelect + "");
        } else {
            imvEdit.performClick();
        }
    }

    private void deleteQR(QrScan qrScan) {
        if (mQRScannedList != null) {
            QrHistoryDatabase.getInstance(getActivity()).qrDao().deleteQr(qrScan);
            mQRScannedList.remove(qrScan);
        }
    }

    public class QRHistoryReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constant.ACTION_DELETE_MULTIPLE_QRCODE)) {
                for (int i = 0; i < mQRScannedList.size(); i++) {
                    if (mQRScannedList.get(i).isChecked()) {
                        deleteQR(mQRScannedList.get(i));
                        i--;
                    }
                }
                imvEdit.performClick();
            }
            if (intent.getAction().equals(Constant.ACTION_SHARE_MULTIPLE_QRCODE_GEN)) {
                for (int i = 0; i < mQRScannedList.size(); i++) {
                    if (mQRScannedList.get(i).isChecked()) {
                        QrScan qrScan = mQRScannedList.get(i);

                        onShareHistory(qrScan);
                        i--;
                        mNumQRSelect--;
                        if (mNumQRSelect == 0){
                            imvEdit.performClick();
                        }
                    }
                }
            }
        }
    }

    private void onShareHistory(QrScan qrCode) {
        String[] content = qrCode.getScanText().split(":");
        String shareContent = "";
        switch (qrCode.getTypeQR()) {
            case WIFI:
                QrWifi qrWifi = new QrWifi();
                StringBuilder stringBuilder = new StringBuilder();
                String[] contentWifi = qrCode.getScanText().split(";");
                for (String value : contentWifi) {
                    stringBuilder.append(value);
                }
                String contentWifi2 = stringBuilder.toString();
                String[] contentWifi3 = contentWifi2.split(":");
                qrWifi.compileWifi(contentWifi, contentWifi3);
                shareContent = qrWifi.getShare();
                break;
            case TEXT:
                shareContent = qrCode.getScanText();
                break;
            case PHONE:
                QreTelephone qreTelephone = new QreTelephone();
                qreTelephone.compile(content);
                shareContent = qreTelephone.getShare();
                break;
            case EMAIL:
                QrEmail qrEmail = new QrEmail();
                qrEmail.compileEmail(content);
                shareContent = qrEmail.getShare();
                break;
            case SMS:
                QrMess qrMess = new QrMess();
                qrMess.compileSMS(content);
                shareContent = qrMess.getShare();
                break;
            case URL:
                QrUrl qrUrl = new QrUrl();
                qrUrl.compileUrl(content);
                shareContent = qrUrl.getShare();
                break;
            case PRODUCT:
                QrProduct qrProduct = (QrProduct) qrCode;
                qrProduct.compileProduct(qrCode.getScanText());
                shareContent = qrProduct.getShare();
                break;
            case ERROR:

            case BAR39:

            case BAR93:

            case BAR128:

            default:
                break;
        }
        Intent intentShare = new Intent(Intent.ACTION_SEND);
        intentShare.setType("text/plain");
        intentShare.putExtra(Intent.EXTRA_TEXT, shareContent);
        getActivity().startActivity(intentShare);
    }


}