package com.example.myapplication.renshi.entity;

import com.google.gson.annotations.SerializedName;

public class YhRenShiGongZuoShiJian {
    private int id;

    @SerializedName("gongzuoshijianks")
    private String gongzuoshijianks;  // 工作时间开始

    @SerializedName("gongzuoshijianjs")
    private String gongzuoshijianjs;  // 工作时间结束

    @SerializedName("wuxiushijianks")
    private String wuxiushijianks;    // 午休时间开始

    @SerializedName("wuxiushijianjs")
    private String wuxiushijianjs;    // 午休时间结束

    @SerializedName("year_month")
    private String yearMonth;         // 年月

    @SerializedName("riqi")
    private String riqi;              // 日期

    @SerializedName("gongsi")
    private String gongsi;            // 公司

    @SerializedName("work_days")
    private String workDays;          // 工作日

    @SerializedName("repeat_type")
    private String repeatType;        // 重复类型

    @SerializedName("schedule_title")
    private String scheduleTitle;     // 排班标题

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGongzuoshijianks() {
        return gongzuoshijianks;
    }

    public void setGongzuoshijianks(String gongzuoshijianks) {
        this.gongzuoshijianks = gongzuoshijianks;
    }

    public String getGongzuoshijianjs() {
        return gongzuoshijianjs;
    }

    public void setGongzuoshijianjs(String gongzuoshijianjs) {
        this.gongzuoshijianjs = gongzuoshijianjs;
    }

    public String getWuxiushijianks() {
        return wuxiushijianks;
    }

    public void setWuxiushijianks(String wuxiushijianks) {
        this.wuxiushijianks = wuxiushijianks;
    }

    public String getWuxiushijianjs() {
        return wuxiushijianjs;
    }

    public void setWuxiushijianjs(String wuxiushijianjs) {
        this.wuxiushijianjs = wuxiushijianjs;
    }

    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }

    public String getRiqi() {
        return riqi;
    }

    public void setRiqi(String riqi) {
        this.riqi = riqi;
    }

    public String getGongsi() {
        return gongsi;
    }

    public void setGongsi(String gongsi) {
        this.gongsi = gongsi;
    }

    public String getWorkDays() {
        return workDays;
    }

    public void setWorkDays(String workDays) {
        this.workDays = workDays;
    }

    public String getRepeatType() {
        return repeatType;
    }

    public void setRepeatType(String repeatType) {
        this.repeatType = repeatType;
    }

    public String getScheduleTitle() {
        return scheduleTitle;
    }

    public void setScheduleTitle(String scheduleTitle) {
        this.scheduleTitle = scheduleTitle;
    }

}