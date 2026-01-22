package com.example.myapplication.renshi.entity;

import java.util.List;

public class GongZuoTongJi {
    private int id;
    private String gongsi;
    private String department;
    private String yearMonth;
    private int totalDays;
    private int workDays;
    private int restDays;
    private double avgWorkHours;
    private double totalWorkHours;
    private String workTimeRange;
    private String breakTimeRange;

    // 图表数据字段
    private List<Double> dailyWorkHours;  // 每日工作小时数
    private List<String> dailyDates;      // 每日日期
    private List<Integer> departmentWorkDays; // 各部门工作天数

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getGongsi() { return gongsi; }
    public void setGongsi(String gongsi) { this.gongsi = gongsi; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getYearMonth() { return yearMonth; }
    public void setYearMonth(String yearMonth) { this.yearMonth = yearMonth; }

    public int getTotalDays() { return totalDays; }
    public void setTotalDays(int totalDays) { this.totalDays = totalDays; }

    public int getWorkDays() { return workDays; }
    public void setWorkDays(int workDays) { this.workDays = workDays; }

    public int getRestDays() { return restDays; }
    public void setRestDays(int restDays) { this.restDays = restDays; }

    public double getAvgWorkHours() { return avgWorkHours; }
    public void setAvgWorkHours(double avgWorkHours) { this.avgWorkHours = avgWorkHours; }

    public double getTotalWorkHours() { return totalWorkHours; }
    public void setTotalWorkHours(double totalWorkHours) { this.totalWorkHours = totalWorkHours; }

    public String getWorkTimeRange() { return workTimeRange; }
    public void setWorkTimeRange(String workTimeRange) { this.workTimeRange = workTimeRange; }

    public String getBreakTimeRange() { return breakTimeRange; }
    public void setBreakTimeRange(String breakTimeRange) { this.breakTimeRange = breakTimeRange; }

    public List<Double> getDailyWorkHours() { return dailyWorkHours; }
    public void setDailyWorkHours(List<Double> dailyWorkHours) { this.dailyWorkHours = dailyWorkHours; }

    public List<String> getDailyDates() { return dailyDates; }
    public void setDailyDates(List<String> dailyDates) { this.dailyDates = dailyDates; }

    public List<Integer> getDepartmentWorkDays() { return departmentWorkDays; }
    public void setDepartmentWorkDays(List<Integer> departmentWorkDays) { this.departmentWorkDays = departmentWorkDays; }
}