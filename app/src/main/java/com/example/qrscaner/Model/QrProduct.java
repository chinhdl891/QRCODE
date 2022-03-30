package com.example.qrscaner.Model;

public class QrProduct extends QrScan {
    private long product;

    public void compileProduct(String s){
        try {
            this.product = Long.parseLong(s);
        }catch (Exception e){

        }

    }
    public long getProduct() {
        return product;
    }
    public String getShare() {
        return product+"";
    }

    public void setProduct(long product) {
        this.product = product;
    }
}
