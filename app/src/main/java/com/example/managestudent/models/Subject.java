package com.example.managestudent.models;

public class Subject {
    private String maMH,tenMH;
    private int heSo;

    public Subject(String maMH) {
        this.maMH = maMH;
    }

    public Subject(String maMH, String tenMH, int heSo) {
        this.maMH = maMH;
        this.tenMH = tenMH;
        this.heSo = heSo;
    }

    public String getMaMH() {
        return maMH;
    }

    public void setMaMH(String maMH) {
        this.maMH = maMH;
    }

    public String getTenMH() {
        return tenMH;
    }

    public void setTenMH(String tenMH) {
        this.tenMH = tenMH;
    }

    public int getHeSo() {
        return heSo;
    }

    public void setHeSo(int heSo) {
        this.heSo = heSo;
    }

    @Override
    public String toString() {
        return maMH + "-" + tenMH;
    }
}
