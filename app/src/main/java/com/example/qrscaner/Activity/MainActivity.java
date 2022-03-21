package com.example.qrscaner.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.qrscaner.Fragment.GenerateFragment;
import com.example.qrscaner.Fragment.HistoryFragment;
import com.example.qrscaner.Fragment.ScannerFragment;
import com.example.qrscaner.Fragment.SettingFragment;
import com.example.qrscaner.Model.Qr;
import com.example.qrscaner.Model.QrScan;
import com.example.qrscaner.Model.QrUrl;
import com.example.qrscaner.Model.QrWifi;
import com.example.qrscaner.R;
import com.example.qrscaner.SendData;
import com.example.qrscaner.view.QrScanResult;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity implements SendData {
    BottomNavigationView bottomNavigationView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
     QrScanResult conActivityMainResultView;
     RelativeLayout rrlMainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rrlMainActivity = findViewById(R.id.rll_main_activity);
        bottomNavigationView = findViewById(R.id.navigationMenu);
        conActivityMainResultView = findViewById(R.id.con_activitiMain_resultView);
        ScannerFragment scannerFragment = new ScannerFragment();
        fragmentLoad(scannerFragment);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.history:
                        fragment = new HistoryFragment();
                        fragmentLoad(fragment);
                        break;
                    case R.id.generate:
                        fragment = new GenerateFragment();
                        fragmentLoad(fragment);
                        break;
                    case R.id.setting:
                        fragment = new SettingFragment();
                        fragmentLoad(fragment);
                        break;
                    case R.id.scan:
                        fragment = new ScannerFragment();
                        fragmentLoad(fragment);
                        break;
                }
                return true;
            }
        });


    }
    private void fragmentLoad(Fragment fragment) {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_content, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }


    @Override
    public void sendQr(QrScan qr) {
        rrlMainActivity.setVisibility(View.GONE);
        conActivityMainResultView.setVisibility(View.VISIBLE);
        conActivityMainResultView.setupData(qr);

    }
}