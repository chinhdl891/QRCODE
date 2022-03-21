package com.example.qrscaner.Model;

import java.io.Serializable;

public class QrScan implements Serializable {
    private String scanText;
    private long date;


    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = System.currentTimeMillis();
    }

    public String getScanText() {
        return scanText;
    }

    public void setScanText(String scanText) {
        this.scanText = scanText;
    }
}

