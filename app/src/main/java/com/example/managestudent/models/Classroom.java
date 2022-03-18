package com.example.managestudent.models;

public class Classroom {
    private String LOP;
    private String CHUNHIEM;

    public Classroom() {
    }

    public Classroom(String LOP, String CHUNHIEM) {
        this.LOP = LOP;
        this.CHUNHIEM = CHUNHIEM;
    }

    public String getLOP() {
        return LOP;
    }

    public void setLOP(String LOP) {
        this.LOP = LOP;
    }

    public String getCHUNHIEM() {
        return CHUNHIEM;
    }

    public void setCHUNHIEM(String CHUNHIEM) {
        this.CHUNHIEM = CHUNHIEM;
    }

    @Override
    public String toString() {
        return
                 LOP + "-" + CHUNHIEM;
    }
}
