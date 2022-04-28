package com.example.qrscaner.models;

public class QreTelephone extends QrScan {
    //2022-03-18 15:39:20.639 28186-28186/com.example.qrscaner E/aaa: tel:0385154192
    private String tel;

    public String getTel() {
        return tel;
    }
    public String getShare() {
        return tel;
    }
    public void setTel(String tel) {
        this.tel = tel;
    }

    public void compile(String[] content) {
        this.tel = content[1];

    }


}
