package com.example.qrscaner.Model;

public class Qr {
    private String contentQr;
    private int id;
    private int type;
    private long date;

    public String getContentQr() {
        return contentQr;
    }

    public void setContentQr(String contentQr) {
        this.contentQr = contentQr;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
