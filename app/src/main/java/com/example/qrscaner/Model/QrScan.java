package com.example.qrscaner.Model;

import android.text.format.DateFormat;

import java.io.Serializable;
import java.util.Date;

public class QrScan implements Serializable {
    private String scanText;
    private long date;

    public QrScan(String scanText, long date) {
        this.scanText = scanText;
        this.date = date;
    }

    public QrScan() {
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = System.currentTimeMillis();
    }
    public String getDateString(){
        String dateString = DateFormat.format("MMM,yyyy", new Date(date)).toString();
        return dateString;
    }

    public String getScanText() {
        return scanText;
    }

    public void setScanText(String scanText) {
        this.scanText = scanText;
    }
}

