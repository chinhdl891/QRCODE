package com.example.qrscaner.activity;

import static com.example.qrscaner.fragment.ScannerFragment.zXingScannerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
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

import com.example.qrscaner.databases.QrHistoryDatabase;
import com.example.qrscaner.models.QrScan;
import com.example.qrscaner.R;
import com.example.qrscaner.SendData;
import com.example.qrscaner.config.Constant;
import com.example.qrscaner.fragment.LanguageDialogFragment;
import com.example.qrscaner.fragment.GenerateFragment;
import com.example.qrscaner.fragment.HistoryFragment;
import com.example.qrscaner.fragment.ResultScanFragment;
import com.example.qrscaner.fragment.ScannerFragment;
import com.example.qrscaner.fragment.SettingFragment;
import com.example.qrscaner.fragment.ShowHistoryFragment;
import com.example.qrscaner.fragment.ShowQrGenerateFragment;
import com.example.qrscaner.myshareferences.MyDataLocal;
import com.example.qrscaner.view.ShowQrGenerate;
import com.example.qrscaner.view.fonts.TextViewPoppinBold;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ShowQrGenerate.iSaveQrScan, LanguageDialogFragment.SelectLanguage {
    private BottomNavigationView bottomNavigationView;
    public static FragmentManager fragmentManager;
    private Fragment fragment;
    private ConstraintLayout ctlMainEditItem;
    private TextViewPoppinBold tvMainNumItem;
    private ImageView imvMainItemShare, imvMainItemDelete, imvMainQrTest;
    private FrameLayout mFrlMainContent, mFmlMainResultQR;

    public final static int REQUEST_CAM = 100;
    public final static int REQUEST_WRITE = 100;
    private static final int TIME_WAIT = 3000;
    private long time;
    public static int WIDTH = 0;
    public static int HEIGHT = 0;
    private String tag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkPermission();
        if (!MyDataLocal.getFistInstall()) {
            MyDataLocal.setShowHistory(true);
        }
        if (MyDataLocal.getLang() != null) {
            setLang(MyDataLocal.getLang());
        }


        getInfoDisPlay();
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
                    default:
                        fragment = new ScannerFragment();
                        fragmentLoad(fragment, ScannerFragment.class.getSimpleName());
                        break;

                }
                return true;
            }
        });

    }

    private void getInfoDisPlay() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        HEIGHT = displayMetrics.heightPixels;
        WIDTH = displayMetrics.widthPixels;
    }

    private void init() {

        mFmlMainResultQR = findViewById(R.id.fml_main_qrScanResult);
        mFrlMainContent = findViewById(R.id.frame_main__content);
        tvMainNumItem = findViewById(R.id.tv_main__numSelect);
        imvMainItemDelete = findViewById(R.id.imv_main__delete);
        imvMainItemShare = findViewById(R.id.imv_main__share);
        ctlMainEditItem = findViewById(R.id.csl_main__edit);
        RelativeLayout rrlMainActivity = findViewById(R.id.rll_main_activity);
        bottomNavigationView = findViewById(R.id.nv_activityMain__menu);
        ShowQrGenerate conActivityMainResultView = findViewById(R.id.qrs_activityMain__resultView);


    }


    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAM);
        }


    }

    public void fragmentLoad(Fragment fragment, String tag) {

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (tag.equals(ResultScanFragment.class.getSimpleName()) || tag.equals(ShowHistoryFragment.class.getSimpleName()) || tag.equals(ShowQrGenerateFragment.class.getSimpleName())) {
            fragmentTransaction.replace(R.id.fml_main_qrScanResult, fragment, tag);
            mFmlMainResultQR.setVisibility(View.VISIBLE);
        } else {
            fragmentTransaction.replace(R.id.frame_main__content, fragment, tag);
        }
        fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
        fragmentTransaction.commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
        zXingScannerView.startCamera();
    }



    public FrameLayout getmFmlMainResultQR() {
        return mFmlMainResultQR;
    }

    public void setmFmlMainResultQR(FrameLayout mFmlMainResultQR) {
        this.mFmlMainResultQR = mFmlMainResultQR;
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
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    if (i == 0) {
                        Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
                    } else {
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


        if (getTopFragment().getTag().equals(ResultScanFragment.class.getSimpleName())) {
            fragmentManager.beginTransaction().remove(Objects.requireNonNull(getSupportFragmentManager().findFragmentByTag(ResultScanFragment.class.getSimpleName()))).commit();
            getSupportFragmentManager().popBackStack();
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(ScannerFragment.class.getSimpleName());
            if (fragment instanceof ScannerFragment) {
                ((ScannerFragment) fragment).resumeCamera();

            }
            mFmlMainResultQR.setVisibility(View.GONE);


        } else if (getTopFragment().getTag().equals(ShowHistoryFragment.class.getSimpleName())) {
            fragmentManager.beginTransaction().remove(Objects.requireNonNull(getSupportFragmentManager().findFragmentByTag(ShowHistoryFragment.class.getSimpleName()))).commit();
            getSupportFragmentManager().popBackStack();
            mFmlMainResultQR.setVisibility(View.GONE);

        } else if (getTopFragment().getTag().equals(ShowQrGenerateFragment.class.getSimpleName())) {
            fragmentManager.beginTransaction().remove(Objects.requireNonNull(getSupportFragmentManager().findFragmentByTag(ShowQrGenerateFragment.class.getSimpleName()))).commit();
            getSupportFragmentManager().popBackStack();
            mFmlMainResultQR.setVisibility(View.GONE);
            bottomNavigationView.setVisibility(View.VISIBLE);
        } else if (getTopFragment().getTag().equals(ScannerFragment.class.getSimpleName()) || getTopFragment().getTag().equals(GenerateFragment.class.getSimpleName()) || getTopFragment().getTag().equals(SettingFragment.class.getSimpleName()) || getTopFragment().getTag().equals(HistoryFragment.class.getSimpleName())) {
            if (time + TIME_WAIT > System.currentTimeMillis()) {
                System.exit(0);
            } else {
                time = System.currentTimeMillis();
                Toast.makeText(this, "Back again to exit", Toast.LENGTH_SHORT).show();
            }
        }


    }

    public Fragment getTopFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            return null;
        }
        String fragmentTag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
        return getSupportFragmentManager().findFragmentByTag(fragmentTag);
    }

    private void setIdBottom(String tag) {
        if (tag.equals(ScannerFragment.class.getSimpleName())) {
            zXingScannerView.startCamera();
            updateNavigationBarState(R.id.scan);
        } else if (tag.equals(HistoryFragment.class.getSimpleName())) {
            updateNavigationBarState(R.id.history);
        } else if (tag.equals(SettingFragment.class.getSimpleName())) {
            updateNavigationBarState(R.id.setting);
        } else if (tag.equals(GenerateFragment.class.getSimpleName())) {
            updateNavigationBarState(R.id.generate);
        }
    }


    private void updateNavigationBarState(int actionId) {
        Menu menu = bottomNavigationView.getMenu();
        for (int i = 0, size = menu.size(); i < size; i++) {
            MenuItem item = menu.getItem(i);
            if (item.getItemId() == actionId) {
                item.setChecked(true);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        zXingScannerView.stopCameraPreview();
    }

    @Override
    protected void onDestroy() {
        zXingScannerView.stopCamera();
        super.onDestroy();

    }


    @Override
    public void onSelectListener(int i) {

        if (i == 0) {
            setLang("vi");
            recreate();

            bottomNavigationView.setSelectedItemId(R.id.scan);
        } else if (i == 1) {
            setLang("en");
            recreate();
            bottomNavigationView.setSelectedItemId(R.id.scan);
        }

    }

    public void setLang(String lang) {
        Resources res = MainActivity.this.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(lang.toLowerCase()));
        MyDataLocal.setLanguage(lang);
        res.updateConfiguration(conf, dm);

    }


}
