package com.example.qrscaner.Model;

public class QrMess extends QrScan {
    private String sendBy;
    private String content;


    public String getSendBy() {
        return sendBy;
    }

    public void setSendBy(String sendBy) {
        this.sendBy = sendBy;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void compileSMS(String[] content) {
        StringBuilder contentSMS = new StringBuilder();

        this.sendBy = content[1];
        for (int i = 2; i < content.length; i++) {
            contentSMS.append(content[i]);
        }
        this.content = contentSMS.toString();
    }

    public String getShare() {
        String mess = "Number : " + sendBy + "\n Message : " + content;
        return mess;
    }
}

