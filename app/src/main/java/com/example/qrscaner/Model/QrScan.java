package com.example.qrscaner.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Entity(tableName = "qr_history")
public class QrScan implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "content")
    private String scanText;
    @ColumnInfo(name = "date")
    private long date;
    @Ignore
    private boolean isEdit;

    public QrScan() {
    }

    public QrScan(String scanText, long date) {
        this.scanText = scanText;
        this.date = date;
    }

    public boolean getIsEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    public long getId() {
        return id;
    }

    public boolean isEdit() {
        return isEdit;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setDate(long date) {
        this.date = date;
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

