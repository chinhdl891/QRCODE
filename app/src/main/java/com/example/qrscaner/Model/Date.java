package com.example.qrscaner.Model;

import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.List;

public class Date {
    private int id;
    private long date;
    private List<QrScan> qrList;

    public List<QrScan> getQrList() {
        return qrList;
    }

    public void setQrList(List<QrScan> qrList) {
        this.qrList = qrList;
    }

    public Date(int id, long date, List<QrScan> qrList) {
        this.id = id;
        this.date = date;
        this.qrList = qrList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        String dateString = DateFormat.format("MMM,yyyy", new java.util.Date(date)).toString();
        return dateString;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
