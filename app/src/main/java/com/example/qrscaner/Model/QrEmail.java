package com.example.qrscaner.Model;

public class QrEmail extends QrScan {
    //2022-03-18 15:46:12.262 28186-28186/com.example.qrscaner E/aaa: MATMSG:TO:vuquocchinh891@gmail.com;SUB:chinh;BODY:chinh;;
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
