package com.example.myapplication.jiaowu.entity;

import java.io.Serializable;

public class JiaoShiKeShi implements Serializable {

    private String teacher_name;
    private String course;
    private String keshi;
    private String jine;
    private String gongzihesuan;

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getKeshi() {
        return keshi;
    }

    public void setKeshi(String keshi) {
        this.keshi = keshi;
    }

    public String getJine() {
        return jine;
    }

    public void setJine(String jine) {
        this.jine = jine;
    }

    public String getGongzihesuan() {
        return gongzihesuan;
    }

    public void setGongzihesuan(String gongzihesuan) {
        this.gongzihesuan = gongzihesuan;
    }

}
