package com.example.qrscaner.application;

import android.app.Application;

import com.example.qrscaner.myshareferences.MyDataLocal;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MyDataLocal.init(getApplicationContext());
    }
}
