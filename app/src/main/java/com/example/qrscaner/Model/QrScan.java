package com.example.qrscaner.Model;

import android.text.format.DateFormat;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class QrScan implements Serializable {
    private String scanText;
    private long date;
    private boolean isEdit;

    public QrScan() {
    }

    public boolean getIsEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    public QrScan(String scanText) {
        this.scanText = scanText;
    }

    public long getDate() {
        return date;
    }

    public void setDate() {
        this.date = System.currentTimeMillis();
    }

    public String getDateString() {
        Date date = new Date();
        date.setTime(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long month = calendar.getTimeInMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM,yyyy");
        String dateMonth = simpleDateFormat.format(month);
        return dateMonth;
    }

    public String getScanText() {
        return scanText;
    }

    public void setScanText(String scanText) {
        this.scanText = scanText;
    }
}

