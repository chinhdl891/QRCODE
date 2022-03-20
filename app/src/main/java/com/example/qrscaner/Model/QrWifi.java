package com.example.qrscaner.Model;

public class QrWifi extends QrScan {
    //WIFI:T:WPA;P:1111222212;S:BAZOOKA STUDIO 2.4G;H:false;
  private    String pass;
  private    String id;
   private String type;

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
