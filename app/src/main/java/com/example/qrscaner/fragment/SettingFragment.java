package com.example.qrscaner.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.qrscaner.R;
import com.example.qrscaner.myshareferences.MyDataLocal;
import com.example.qrscaner.myshareferences.MySharePreference;


public class SettingFragment extends Fragment {
    public final static String KEY_VIBRATE = "vibrate";
    public final static String KEY_BEEP = "beep";
    public final static String KEY_BARCODE = "auto_scan";
    public final static String KEY_HISTORY = "history_scan";
    Switch swSettingFragmentVibrate, swSettingFragmentBeep, swSettingFragmentHistory, swSettingFragmentBarcode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        swSettingFragmentVibrate = view.findViewById(R.id.sw_settingFragment_Vibrate);
        swSettingFragmentBeep = view.findViewById(R.id.sw_settingFragment_Beep);
        swSettingFragmentBarcode = view.findViewById(R.id.sw_settingFragment_Barcode);
        swSettingFragmentHistory = view.findViewById(R.id.sw_settingFragment_His);
        swSettingFragmentVibrate.setChecked(MyDataLocal.getVibrate());
        swSettingFragmentBeep.setChecked(MyDataLocal.getBeep());
        swSettingFragmentHistory.setChecked(MyDataLocal.getShowHistory());
        swSettingFragmentVibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    MyDataLocal.setBeep(false);
                    swSettingFragmentBeep.setChecked(MyDataLocal.getBeep());
                }
                MyDataLocal.setVibrate(b);
            }
        });
        swSettingFragmentBeep.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    MyDataLocal.setVibrate(false);
                    swSettingFragmentVibrate.setChecked(MyDataLocal.getVibrate());
                }
                MyDataLocal.setBeep(b);
            }
        });
        swSettingFragmentHistory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                MyDataLocal.setShowHistory(b);
            }
        });
        return view;
    }
}