package com.example.myapplication.jiaowu.entity;

import java.io.Serializable;

public class KeShiDetail implements Serializable {
    private int id;
    private String riqi;
    private String student_name;
    private String course;
    private int keshi;
    private String teacher_name;
    private float jine;
    private String company;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRiqi() {
        return riqi;
    }

    public void setRiqi(String riqi) {
        this.riqi = riqi;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public int getKeshi() {
        return keshi;
    }

    public void setKeshi(int keshi) {
        this.keshi = keshi;
    }

    public float getJine() {
        return jine;
    }

    public void setJine(float jine) {
        this.jine = jine;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
