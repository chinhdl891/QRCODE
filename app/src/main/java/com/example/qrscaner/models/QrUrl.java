package com.example.qrscaner.models;

import java.io.Serializable;

public class QrUrl extends QrScan implements Serializable {
    //2022-03-18 15:38:18.111 28186-28186/com.example.qrscaner E/aaa: https://www.youtube.com/watch?v=MegowI4T_L8
    private String url;

    public String getUrl() {
        return url;
    }
    public String getShare() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public void compileUrl(String[] content) {

        StringBuilder link = new StringBuilder();
        for (int i = 0; i < content.length; i++) {
            if (i == 1) {
                link.append(":");
            }
            link.append(content[i]);

        }
        this.url = link.toString();

    }

}
