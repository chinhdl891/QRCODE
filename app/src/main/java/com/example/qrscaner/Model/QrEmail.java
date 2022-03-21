package com.example.qrscaner.Model;

public class QrEmail extends QrScan {
    //2022-03-18 15:46:12.262 28186-28186/com.example.qrscaner E/aaa: MATMSG:TO:vuquocchinh891@gmail.com;SUB:chinh;BODY:chinh;;
    private String content;
    private String sendBy;
    private String sendTo;

    public String getSendBy() {
        return sendBy;
    }

    public void setSendBy(String sendBy) {
        this.sendBy = sendBy;
    }

    public String getSendTo() {
        return sendTo;
    }

    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public void compileEmail(String[] contentEmail) {
        this.content = contentEmail[0].replace("MATMSGTO", "").trim();
        this.sendTo = contentEmail[1].replace("SUB", "");
        this.content = contentEmail[2].replace("BODY", "");

    }
}
