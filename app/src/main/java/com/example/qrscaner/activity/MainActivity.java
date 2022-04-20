package com.example.qrscaner.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.qrscaner.DataBase.QrHistoryDatabase;
import com.example.qrscaner.config.Constant;
import com.example.qrscaner.fragment.GenerateFragment;
import com.example.qrscaner.fragment.HistoryFragment;
import com.example.qrscaner.fragment.ScannerFragment;
import com.example.qrscaner.fragment.SettingFragment;
import com.example.qrscaner.Model.QrScan;
import com.example.qrscaner.R;
import com.example.qrscaner.SendData;
import com.example.qrscaner.myshareferences.MyDataLocal;
import com.example.qrscaner.view.QrScanResult;
import com.example.qrscaner.view.fonts.TextViewPoppinBold;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SendData, QrScanResult.iSaveQrScan {
    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private QrScanResult conActivityMainResultView;
    private RelativeLayout rrlMainActivity;
    private Fragment fragment;
    private ConstraintLayout ctlMainEditItem;
    private TextViewPoppinBold tvMainNumItem;
    private ImageView imvMainItemShare, imvMainItemDelete, imvMainQrTest;
    public final static int REQUEST_CAM = 100;
    public final static int REQUEST_WRITE = 100;
    private static int TIME_WAIT = 3000;
    private long time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission();
        if (!MyDataLocal.getFistInstall()) {
            MyDataLocal.setShowHistory(true);
        }

        setContentView(R.layout.activity_main);
        init();
        imvMainItemDelete.setOnClickListener(this);
        imvMainItemShare.setOnClickListener(this);
        ScannerFragment scannerFragment = new ScannerFragment();
        fragmentLoad(scannerFragment, ScannerFragment.class.getSimpleName());
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.history:
                        fragment = new HistoryFragment();
                        fragmentLoad(fragment, HistoryFragment.class.getSimpleName());
                        break;
                    case R.id.generate:
                        fragment = new GenerateFragment();
                        fragmentLoad(fragment, GenerateFragment.class.getSimpleName());
                        break;
                    case R.id.setting:
                        fragment = new SettingFragment();
                        fragmentLoad(fragment, SettingFragment.class.getSimpleName());
                        break;
                    default :
                        fragment = new ScannerFragment();
                        fragmentLoad(fragment, ScannerFragment.class.getSimpleName());
                        break;

                }
                return true;
            }
        });

        conActivityMainResultView.setCallbackCancelResult(() -> {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(ScannerFragment.class.getSimpleName());
            if (fragment instanceof ScannerFragment) {
                ((ScannerFragment) fragment).resumeCamera();

            }
        });
    }

    private void init() {
        tvMainNumItem = findViewById(R.id.tv_main__numSelect);
        imvMainItemDelete = findViewById(R.id.imv_main__delete);
        imvMainItemShare = findViewById(R.id.imv_main__share);
        ctlMainEditItem = findViewById(R.id.csl_main__edit);
        rrlMainActivity = findViewById(R.id.rll_main_activity);
        bottomNavigationView = findViewById(R.id.nv_activityMain__menu);
        conActivityMainResultView = findViewById(R.id.con_activityMain__resultView);

    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAM);
        }


    }

    public void fragmentLoad(Fragment fragment, String tag) {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_main__content, fragment, tag);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    @Override
    public void sendQr(QrScan qr) {
//        conActivityMainResultView.setVisibility(View.VISIBLE);
//        conActivityMainResultView.setupData(qr, this);

    }

    public ConstraintLayout getCtlMainEditItem() {
        return ctlMainEditItem;
    }


    public TextViewPoppinBold getTvMainNumItem() {
        return tvMainNumItem;
    }


    public ImageView getImvMainItemShare() {
        return imvMainItemShare;
    }


    public ImageView getImvMainItemDelete() {
        return imvMainItemDelete;
    }


    public BottomNavigationView getBottomNavigationView() {
        return bottomNavigationView;
    }

    public ImageView getImvMainQrTest() {
        return imvMainQrTest;
    }

    public void setImvMainQrTest(ImageView imvMainQrTest) {
        this.imvMainQrTest = imvMainQrTest;
    }

    @Override
    public void saveQr(QrScan qrScan) {

        QrHistoryDatabase.getInstance(this).qrDao().insertQr(qrScan);
        Toast.makeText(this, "Save successful", Toast.LENGTH_SHORT).show();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(ScannerFragment.class.getSimpleName());
        if (fragment instanceof ScannerFragment) {
            ((ScannerFragment) fragment).resumeCamera();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAM) {
            for (int i = 0; i < permissions.length ; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED){
                    if (i==0){
                        Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(this, "Write permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }


    @Override
    public void onClick(View view) {
        if (view == imvMainItemDelete) {
            sendBroadcast(new Intent(Constant.ACTION_DELETE_MULTIPLE_QRCODE));
        } else if (view == imvMainItemShare) {
            sendBroadcast(new Intent(Constant.ACTION_SHARE_MULTIPLE_QRCODE_GEN));
        }
    }

    @Override
    public void onBackPressed() {

        if (time + TIME_WAIT > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            time = System.currentTimeMillis();
            Toast.makeText(this, "Back again to exit", Toast.LENGTH_SHORT).show();
        }


    }
}