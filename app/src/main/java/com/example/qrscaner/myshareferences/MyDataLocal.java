package com.example.qrscaner.myshareferences;

import static com.example.qrscaner.fragment.SettingFragment.KEY_BEEP;
import static com.example.qrscaner.fragment.SettingFragment.KEY_HISTORY;
import static com.example.qrscaner.fragment.SettingFragment.KEY_VIBRATE;

import android.content.Context;

public class MyDataLocal {
    private static final String KEY_PREFERENCE_FIRST = "first_install";
    private static MyDataLocal instance;
    private MySharePreference mySharePreference;
    public static void init(Context context){
        instance = new MyDataLocal();
        instance.mySharePreference = new MySharePreference(context);

    }
    public static MyDataLocal getInstance(){
        if (instance==null){
            instance = new MyDataLocal();
        }
            return instance;
    }
    public static void setFirstInstall(Boolean isFirst){
        MyDataLocal.getInstance().mySharePreference.pushBooleanValue(KEY_PREFERENCE_FIRST,isFirst);


    }
    public static boolean getFistInstall(){
      return   MyDataLocal.getInstance().mySharePreference.getData(KEY_PREFERENCE_FIRST);
    }
    public static void setVibrate(Boolean vibrate){
        MyDataLocal.getInstance().mySharePreference.pushBooleanValue(KEY_VIBRATE,vibrate);

    }
    public static boolean getVibrate(){
        return   MyDataLocal.getInstance().mySharePreference.getData(KEY_VIBRATE);

    }
    public static void setBeep(Boolean beep){
        MyDataLocal.getInstance().mySharePreference.pushBooleanValue(KEY_BEEP,beep);

    }
    public static boolean getBeep(){
        return   MyDataLocal.getInstance().mySharePreference.getData(KEY_BEEP);

    }
    public static void setShowHistory(Boolean showHistory){
        MyDataLocal.getInstance().mySharePreference.pushBooleanValue(KEY_HISTORY,showHistory);

    }
    public static boolean getShowHistory(){
        return  MyDataLocal.getInstance().mySharePreference.getData(KEY_HISTORY);

    }
}
