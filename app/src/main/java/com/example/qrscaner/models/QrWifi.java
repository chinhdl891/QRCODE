package com.example.qrscaner.models;

public class QrWifi extends QrScan {
    //WIFI:T:WPA;P:1111222212;S:BAZOOKA STUDIO 2.4G;H:false;
    private String pass;
    private String wifiName;
    private String type;

    public String getPass() {
        return pass;
    }
    public String getWifiName() {
        return wifiName;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void compileWifi(String[] contentWifi, String[] contentWifi3) {
        this.type = contentWifi[0].replace("WIFI:T:", "");
        this.wifiName = contentWifi3[3].replace("P", "").trim();
        this.pass = contentWifi3[4].replace("H", "").trim();
    }

    public String getShare() {
        String share = "ID : " + wifiName + "\n Password :  " + pass + "\n Encryption:" + type;
        return share;
    }

}
