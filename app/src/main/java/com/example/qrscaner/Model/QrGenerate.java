package com.example.qrscaner.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Entity(tableName = "qr_generate")
public class QrGenerate {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "date")
    private long date;
    @ColumnInfo(name = "content")
    private String content;
    @ColumnInfo(name = "qrType")
    private QrScan.QRType qrType;
    @ColumnInfo(name = "color")
    private int color;
    @Ignore
    private boolean isEdit;

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    public QrGenerate() {
    }

    public QrGenerate(long date, String content, QrScan.QRType qrType) {
        this.date = date;
        this.content = content;
        this.qrType = qrType;
    }

    public QrGenerate(long date, String content, QrScan.QRType qrType, int color) {
        this.date = date;
        this.content = content;
        this.qrType = qrType;
        this.color = color;
    }

    public QrScan.QRType getQrType() {
        return qrType;
    }

    public void setQrType(QrScan.QRType qrType) {
        this.qrType = qrType;
    }

    public String getStringDate() {
        Date datetime = new Date();
        datetime.setTime(this.date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(datetime);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        long month = calendar.getTimeInMillis();
        String dateString = new SimpleDateFormat("MMM,yyyy").format(month);
        return dateString;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
