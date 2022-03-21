package com.example.qrscaner.Model;

import android.app.AlertDialog;
import android.util.Log;

public class QreTelephone extends QrScan {
    //2022-03-18 15:39:20.639 28186-28186/com.example.qrscaner E/aaa: tel:0385154192
    private String tel;

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void compile(String[] content) {
        this.tel = content[1];


    }
}
