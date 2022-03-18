package com.example.managestudent.models;

public class Mark {
    private String maHS;
    private String maMH;
    private String diem;

    private Subject subject;

    public Mark() {
    }

    public String getMaHS() {
        return maHS;
    }

    public void setMaHS(String maHS) {
        this.maHS = maHS;
    }

    public String getMaMH() {
        return maMH;
    }

    public void setMaMH(String maMH) {
        this.maMH = maMH;
    }

    public String getDiem() {
        return diem;
    }

    public void setDiem(String diem) {
        this.diem = diem;
    }

    public Mark(String maHS, String maMH, String diem, Subject subject) {
        this.maHS = maHS;
        this.maMH = maMH;
        this.diem = diem;
        this.subject = subject;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return "Mark{" +
                "maHS='" + maHS + '\'' +
                ", maMH='" + maMH + '\'' +
                ", diem=" + diem +
                '}';
    }
}
