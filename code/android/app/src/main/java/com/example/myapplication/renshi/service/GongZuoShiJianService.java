package com.example.myapplication.renshi.service;

import com.example.myapplication.renshi.dao.renshiBaseDao;
import com.example.myapplication.renshi.entity.GongZuoShiJian;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GongZuoShiJianService {
    private renshiBaseDao base;

    /**
     * 获取工作安排列表
     */
    public List<GongZuoShiJian> getList(String company) {
        List<GongZuoShiJian> list = new ArrayList<>();
        try {
            String sql = "select * from gongzi_gongzuoshijian where gongsi = ? and schedule_status = 'active' order by id desc";
            System.out.println("执行SQL：" + sql + ", 参数：" + company.replace("_hr", ""));

            base = new renshiBaseDao();
            list = base.query(GongZuoShiJian.class, sql, company.replace("_hr", ""));

            System.out.println("查询结果数量：" + list.size());
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Service查询异常：" + e.getMessage());
            return list;
        }
    }

    /**
     * 根据部门筛选查询
     */
    public List<GongZuoShiJian> getListByDepartment(String company, String department) {
        List<GongZuoShiJian> result;

        if (department == null || department.isEmpty() || department.equals("全部")) {
            String sql = "select * from gongzi_gongzuoshijian where gongsi = ? and schedule_status = 'active' order by id desc";
            base = new renshiBaseDao();
            result = base.query(GongZuoShiJian.class, sql, company.replace("_hr", ""));
        } else {
            String sql = "select * from gongzi_gongzuoshijian where gongsi = ? and schedule_title = ? and schedule_status = 'active' order by id desc";
            base = new renshiBaseDao();
            result = base.query(GongZuoShiJian.class, sql, company.replace("_hr", ""), department);
        }

        return result;
    }

    /**
     * 根据年月筛选查询
     */
    public List<GongZuoShiJian> getListByYearMonth(String company, String yearMonth) {
        String sql = "select * from gongzi_gongzuoshijian where gongsi = ? and year_month = ? and schedule_status = 'active' order by id desc";
        base = new renshiBaseDao();
        return base.query(GongZuoShiJian.class, sql, company.replace("_hr", ""), yearMonth);
    }

    /**
     * 根据日期查询工作安排
     */
    public List<GongZuoShiJian> getListByDate(String company, String date) {
        String sql = "select * from gongzi_gongzuoshijian where gongsi = ? and schedule_status = 'active' " +
                "and work_days like ? order by id desc";
        base = new renshiBaseDao();
        return base.query(GongZuoShiJian.class, sql, company.replace("_hr", ""), "%" + date + "%");
    }

    /**
     * 新增工作安排记录
     */
    public boolean insert(GongZuoShiJian entity) {
        String sql = "insert into gongzi_gongzuoshijian (gongsi, schedule_title, gongzuoshijianks, gongzuoshijianjs, " +
                "wuxiushijianks, wuxiushijianjs, year_month, riqi, work_days, repeat_type, schedule_status) " +
                "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        base = new renshiBaseDao();
        return base.execute(sql,
                entity.getGongsi(),
                entity.getScheduleTitle(),
                entity.getGongZuoShiJianKs(),
                entity.getGongZuoShiJianJs(),
                entity.getWuXiuShiJianKs(),
                entity.getWuXiuShiJianJs(),
                entity.getYearMonth(),
                entity.getRiqi(),
                entity.getWorkDays(), // 直接使用getWorkDays()
                entity.getRepeatType(),
                entity.getScheduleStatus());
    }

    /**
     * 更新工作安排记录
     */
    public boolean update(GongZuoShiJian entity) {
        String sql = "update gongzi_gongzuoshijian set schedule_title=?, gongzuoshijianks=?, gongzuoshijianjs=?, " +
                "wuxiushijianks=?, wuxiushijianjs=?, year_month=?, riqi=?, work_days=?, repeat_type=?, " +
                "schedule_status=? where id=?";
        base = new renshiBaseDao();
        return base.execute(sql,
                entity.getScheduleTitle(),
                entity.getGongZuoShiJianKs(),
                entity.getGongZuoShiJianJs(),
                entity.getWuXiuShiJianKs(),
                entity.getWuXiuShiJianJs(),
                entity.getYearMonth(),
                entity.getRiqi(),
                entity.getWorkDays(),
                entity.getRepeatType(),
                entity.getScheduleStatus(),
                entity.getId());
    }

    /**
     * 更新工作时间
     */
    public boolean updateWorkTime(int id, String startTime, String endTime) {
        String sql = "update gongzi_gongzuoshijian set gongzuoshijianks = ?, gongzuoshijianjs = ? where id = ?";
        base = new renshiBaseDao();
        return base.execute(sql, startTime, endTime, id);
    }

    /**
     * 更新午休时间
     */
    public boolean updateBreakTime(int id, String startTime, String endTime) {
        String sql = "update gongzi_gongzuoshijian set wuxiushijianks = ?, wuxiushijianjs = ? where id = ?";
        base = new renshiBaseDao();
        return base.execute(sql, startTime, endTime, id);
    }

    /**
     * 更新工作日期
     */
    public boolean updateWorkDays(int id, String workDays) {
        String sql = "update gongzi_gongzuoshijian set work_days = ? where id = ?";
        base = new renshiBaseDao();
        return base.execute(sql, workDays, id);
    }

    /**
     * 删除工作安排记录
     */
    public boolean delete(int id) {
        String deletesql = "delete from gongzi_gongzuoshijian where id = ?";
        base = new renshiBaseDao();
        return base.execute(deletesql, id);
    }

    /**
     * 软删除（更新状态为inactive）
     */
    public boolean softDelete(int id) {
        String sql = "update gongzi_gongzuoshijian set schedule_status = 'inactive' where id = ?";
        base = new renshiBaseDao();
        return base.execute(sql, id);
    }

    /**
     * 获取重复类型选项
     */
    public String[] getRepeatTypeOptions() {
        return new String[]{"不重复", "每天", "每周", "每月", "工作日（周一至周五）", "自定义"};
    }

    /**
     * 获取部门筛选选项
     */
    public String[] getDepartmentOptions() {
        return new String[]{"全部", "技术部", "销售部", "人事部", "财务部", "行政部"};
    }

    /**
     * 根据日期范围查询工作安排
     */
    public List<GongZuoShiJian> getListByDateRange(String company, String startDate, String endDate) {
        String sql = "select * from gongzi_gongzuoshijian where gongsi = ? and schedule_status = 'active' " +
                "and (work_days like ? or work_days like ? or work_days like ?) order by id desc";
        base = new renshiBaseDao();
        return base.query(GongZuoShiJian.class, sql,
                company.replace("_hr", ""),
                "%" + startDate + "%",
                "%" + endDate + "%",
                "%" + startDate.substring(0, 7) + "%"); // 年月匹配
    }

    /**
     * 获取日期范围内的所有日期（用于日期范围选择）
     */
    public List<String> getDateRange(String startDate, String endDate, String filterOption) {
        List<String> dates = new ArrayList<>();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date start = sdf.parse(startDate);
            Date end = sdf.parse(endDate);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(start);

            while (!calendar.getTime().after(end)) {
                String currentDate = sdf.format(calendar.getTime());
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

                boolean shouldInclude = true;

                switch (filterOption) {
                    case "excludeSat":
                        shouldInclude = dayOfWeek != Calendar.SATURDAY;
                        break;
                    case "excludeSun":
                        shouldInclude = dayOfWeek != Calendar.SUNDAY;
                        break;
                    case "weekends":
                        shouldInclude = dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY;
                        break;
                    case "weekdays":
                        shouldInclude = dayOfWeek >= Calendar.MONDAY && dayOfWeek <= Calendar.FRIDAY;
                        break;
                    case "all":
                    default:
                        shouldInclude = true;
                        break;
                }

                if (shouldInclude) {
                    dates.add(currentDate);
                }

                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dates;
    }
}