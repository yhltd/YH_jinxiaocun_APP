package com.example.myapplication.scheduling.entity;

public class TimeConfig {
    private int id;
    private int week;
    private String morning_start;
    private String morning_end;
    private String noon_start;
    private String noon_end;
    private String night_start;
    private String night_end;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public String getMorning_start() {
        return morning_start;
    }

    public void setMorning_start(String morning_start) {
        this.morning_start = morning_start;
    }

    public String getMorning_end() {
        return morning_end;
    }

    public void setMorning_end(String morning_end) {
        this.morning_end = morning_end;
    }

    public String getNoon_start() {
        return noon_start;
    }

    public void setNoon_start(String noon_start) {
        this.noon_start = noon_start;
    }

    public String getNoon_end() {
        return noon_end;
    }

    public void setNoon_end(String noon_end) {
        this.noon_end = noon_end;
    }

    public String getNight_start() {
        return night_start;
    }

    public void setNight_start(String night_start) {
        this.night_start = night_start;
    }

    public String getNight_end() {
        return night_end;
    }

    public void setNight_end(String night_end) {
        this.night_end = night_end;
    }
}
