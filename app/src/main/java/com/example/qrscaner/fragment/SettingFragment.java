package com.example.qrscaner.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qrscaner.R;
import com.example.qrscaner.myshareferences.MyDataLocal;
import com.example.qrscaner.myshareferences.MySharePreference;


public class SettingFragment extends Fragment implements View.OnClickListener{
    public final static String KEY_VIBRATE = "vibrate";
    public final static String KEY_BEEP = "beep";
    public final static String KEY_BARCODE = "auto_scan";
    public final static String KEY_HISTORY = "history_scan";
    Switch swSettingFragmentVibrate, swSettingFragmentBeep, swSettingFragmentHistory, swSettingFragmentBarcode;
    private RelativeLayout rltSettingSelectLan;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        init(view);
        swSettingFragmentVibrate.setChecked(MyDataLocal.getVibrate());
        swSettingFragmentBeep.setChecked(MyDataLocal.getBeep());
        swSettingFragmentHistory.setChecked(MyDataLocal.getShowHistory());
        swSettingFragmentVibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    swSettingFragmentBeep.setChecked(MyDataLocal.getBeep());
                }
                MyDataLocal.setVibrate(b);
            }
        });
        swSettingFragmentBeep.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
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

    private void init(View view) {
        swSettingFragmentVibrate = view.findViewById(R.id.sw_settingFragment_Vibrate);
        swSettingFragmentBeep = view.findViewById(R.id.sw_settingFragment_Beep);
        swSettingFragmentHistory = view.findViewById(R.id.sw_settingFragment_His);
        rltSettingSelectLan = view.findViewById(R.id.rlt_setting_lang);
        rltSettingSelectLan.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.rlt_setting_lang){
            showDialog();
        }
    }

    private void showDialog() {
        DialogFragment dialogFragment = new DialogFragment();
        dialogFragment.show(getActivity().getSupportFragmentManager(),"Select language");

    }
}