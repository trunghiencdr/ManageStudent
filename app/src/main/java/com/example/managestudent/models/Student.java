package com.example.managestudent.models;

import java.io.Serializable;
import java.util.Date;

public class Student implements Serializable {
    private String maHocSinh;
    private String ho;
    private String ten;
    private boolean phai;
    private String lop;
    private Date ngaySinh;

    public Student() {
    }

    public Student(String maHocSinh, String ho, String ten, boolean phai,Date ngaySinh, String lop) {
        this.maHocSinh = maHocSinh;
        this.ho = ho;
        this.ten = ten;
        this.phai = phai;
        this.lop = lop;
        this.ngaySinh = ngaySinh;
    }

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getMaHocSinh() {
        return maHocSinh;
    }

    public void setMaHocSinh(String maHocSinh) {
        this.maHocSinh = maHocSinh;
    }

    public String getHo() {
        return ho;
    }

    public void setHo(String ho) {
        this.ho = ho;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public boolean isPhai() {
        return phai;
    }

    public void setPhai(boolean phai) {
        this.phai = phai;
    }

    public String getLop() {
        return lop;
    }

    public void setLop(String lop) {
        this.lop = lop;
    }
    public String getHoTen(){
        return ho+" "+ten;
    }

    @Override
    public String toString() {
        return "Student{" +
                "maHocSinh='" + maHocSinh + '\'' +
                ", ho='" + ho + '\'' +
                ", ten='" + ten + '\'' +
                ", phai=" + phai +
                ", lop='" + lop + '\'' +
                ", ngaySinh=" + ngaySinh +
                '}';
    }
}
