package com.example.qrscaner.Model;

public class QrMess extends QrScan {
  private    String nguoiGui;
  private    String content;
  private String nguoiNhan;

  public String getNguoiNhan() {
    return nguoiNhan;
  }

  public void setNguoiNhan(String nguoiNhan) {
    this.nguoiNhan = nguoiNhan;
  }

  public String getNguoiGui() {
    return nguoiGui;
  }

  public void setNguoiGui(String nguoiGui) {
    this.nguoiGui = nguoiGui;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }
}

