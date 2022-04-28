package com.example.qrscaner.models;

public class QrText extends QrScan {
    //2022-03-18 15:40:28.448 28186-28186/com.example.qrscaner E/aaa: heloworl
  private    String text;

    public String getText() {
        return text;
    }
    public String getShare() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
