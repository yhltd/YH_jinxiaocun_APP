package com.example.myapplication.renshi.service;

import com.example.myapplication.renshi.dao.renshiBaseDao;
import com.example.myapplication.renshi.entity.YhRenShiGongZuoShiJian;
import com.example.myapplication.renshi.entity.YhRenShiKaoQinDaKa;
import com.example.myapplication.renshi.entity.YhRenShiKaoQinQingJia;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class YhRenShiKaoQinDaKaService {

    public YhRenShiKaoQinDaKaService() {
        // 构造函数不做任何事情
    }

    /**
     * 条件查询 - 根据年月和姓名查询
     */
    public List<YhRenShiKaoQinDaKa> nameMonthList(String company, String name, String this_date) {
        renshiBaseDao base = new renshiBaseDao();
        try {
            String sql = "select * from gongzi_kaoqinjilu where AO = ? and year+moth = ? and name = ?";
            return base.query(YhRenShiKaoQinDaKa.class, sql,
                    company.replace("_hr", ""), this_date, name);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (base != null) {
                try {
                    base.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取指定记录
     */
    public YhRenShiKaoQinDaKa getByNameAndMonth(String company, String name, String year, String moth) {
        String this_date = year + moth;
        List<YhRenShiKaoQinDaKa> list = nameMonthList(company, name, this_date);
        return list != null && !list.isEmpty() ? list.get(0) : null;
    }

    /**
     * 新增考勤记录
     */
    public boolean insert(YhRenShiKaoQinDaKa kaoQinDaKa) {
        renshiBaseDao base = new renshiBaseDao();
        try {
            String sql = "insert into gongzi_kaoqinjilu(year, moth, name, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, AA, AB, AC, AD, AE, AF, AG, AH, AI, AJ, AK, AL, AM, AN, AO) " +
                    "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            // 获取所有天的状态
            String[] dayStatus = new String[31];
            for (int i = 1; i <= 31; i++) {
                dayStatus[i-1] = kaoQinDaKa.getDayStatus(i);
                if (dayStatus[i-1] == null) {
                    dayStatus[i-1] = "";
                }
            }

            long result = base.executeOfId(sql,
                    kaoQinDaKa.getYear(), kaoQinDaKa.getMonth(), kaoQinDaKa.getName(),
                    dayStatus[0], dayStatus[1], dayStatus[2], dayStatus[3], dayStatus[4],
                    dayStatus[5], dayStatus[6], dayStatus[7], dayStatus[8], dayStatus[9],
                    dayStatus[10], dayStatus[11], dayStatus[12], dayStatus[13], dayStatus[14],
                    dayStatus[15], dayStatus[16], dayStatus[17], dayStatus[18], dayStatus[19],
                    dayStatus[20], dayStatus[21], dayStatus[22], dayStatus[23], dayStatus[24],
                    dayStatus[25], dayStatus[26], dayStatus[27], dayStatus[28], dayStatus[29],
                    dayStatus[30],
                    kaoQinDaKa.getAj(), kaoQinDaKa.getAk(), kaoQinDaKa.getAl(),
                    kaoQinDaKa.getAm(), kaoQinDaKa.getAn(), kaoQinDaKa.getAo());
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (base != null) {
                try {
                    base.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 修改考勤记录
     */
    public boolean update(YhRenShiKaoQinDaKa kaoQinDaKa) {
        renshiBaseDao base = new renshiBaseDao();
        try {
            String sql = "update gongzi_kaoqinjilu set year=?, moth=?, name=?, " +
                    "E=?, F=?, G=?, H=?, I=?, J=?, K=?, L=?, M=?, N=?, O=?, P=?, " +
                    "Q=?, R=?, S=?, T=?, U=?, V=?, W=?, X=?, Y=?, Z=?, AA=?, AB=?, " +
                    "AC=?, AD=?, AE=?, AF=?, AG=?, AH=?, AI=?, AJ=?, AK=?, AL=?, " +
                    "AM=?, AN=? where id=? and AO=?";

            // 获取所有天的状态
            String[] dayStatus = new String[31];
            for (int i = 1; i <= 31; i++) {
                dayStatus[i-1] = kaoQinDaKa.getDayStatus(i);
                if (dayStatus[i-1] == null) {
                    dayStatus[i-1] = "";
                }
            }

            return base.execute(sql,
                    kaoQinDaKa.getYear(), kaoQinDaKa.getMonth(), kaoQinDaKa.getName(),
                    dayStatus[0], dayStatus[1], dayStatus[2], dayStatus[3], dayStatus[4],
                    dayStatus[5], dayStatus[6], dayStatus[7], dayStatus[8], dayStatus[9],
                    dayStatus[10], dayStatus[11], dayStatus[12], dayStatus[13], dayStatus[14],
                    dayStatus[15], dayStatus[16], dayStatus[17], dayStatus[18], dayStatus[19],
                    dayStatus[20], dayStatus[21], dayStatus[22], dayStatus[23], dayStatus[24],
                    dayStatus[25], dayStatus[26], dayStatus[27], dayStatus[28], dayStatus[29],
                    dayStatus[30],
                    kaoQinDaKa.getAj(), kaoQinDaKa.getAk(), kaoQinDaKa.getAl(),
                    kaoQinDaKa.getAm(), kaoQinDaKa.getAn(),
                    kaoQinDaKa.getId(), kaoQinDaKa.getAo());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (base != null) {
                try {
                    base.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 更新指定日期状态
     */
    public boolean updateDayStatus(String company, String name, String year, String moth,
                                   int day, String status) {
        renshiBaseDao base = new renshiBaseDao();
        try {
            // 获取字段名
            String dayField = getDayField(day);
            if (dayField == null) return false;

            // 更新该天的状态
            String sql = String.format("update gongzi_kaoqinjilu set %s=? where AO=? and name=? and year=? and moth=?", dayField);
            boolean result = base.execute(sql, status, company.replace("_hr", ""), name, year, moth);

            System.out.println("DEBUG updateDayStatus: 更新状态结果=" + result + ", 状态=" + status);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (base != null) {
                try {
                    base.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 根据日期获取数据库字段名
     */
    private String getDayField(int day) {
        if (day < 1 || day > 31) return null;

        String[] fields = {"E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
                "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X",
                "Y", "Z", "AA", "AB", "AC", "AD", "AE", "AF", "AG", "AH", "AI"};
        return fields[day - 1];
    }

    /**
     * 签到操作 - 根据部门工作时间动态判断
     */
    public boolean signIn(String company, String name, String department, String signTime) {
        try {
            Calendar cal = Calendar.getInstance();
            String year = String.valueOf(cal.get(Calendar.YEAR));
            String moth = String.format("%02d", cal.get(Calendar.MONTH) + 1);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            // 1. 查询该部门的工作开始时间
            String workStartTime = "09:00"; // 默认值
            if (department != null && !department.isEmpty()) {
                String startTime = getWorkStartTime(company, department);
                if (startTime != null && !startTime.isEmpty()) {
                    workStartTime = startTime;
                }
            }

            // 获取时分
            SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
            String signHourMinute;

            // 处理时间格式
            if (signTime.contains(":")) {
                String[] parts = signTime.split(":");
                signHourMinute = parts[0] + ":" + parts[1];
            } else {
                // 如果格式不正确，使用当前时间
                signHourMinute = new SimpleDateFormat("HH:mm").format(new Date());
            }

            // 判断状态
            String status;
            Date currentTime = sdfTime.parse(signHourMinute);
            Date standardTime = sdfTime.parse(workStartTime);

            long diff = currentTime.getTime() - standardTime.getTime();
            long minutes = diff / (1000 * 60);

            if (minutes <= 0) {
                status = "早签";
            } else if (minutes <= 30) {
                status = "迟到";
            } else {
                status = "旷勤";
            }

            // 更新数据库
            // 更新数据库
            boolean result = updateDayStatus(company, name, year, moth, day, status);

            // 如果更新成功，重新统计
            if (result) {
                updateAttendanceStatistics(company, name, year, moth);
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 查询部门的工作开始时间
     */
    private String getWorkStartTime(String company, String department) {
        renshiBaseDao base = new renshiBaseDao();
        try {
            System.out.println("DEBUG: 查询工作开始时间 - 公司: " + company + ", 部门: " + department);
            String realCompany = company.replace("_hr", "");

            // 获取当前日期
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String currentDate = dateFormat.format(new Date());

            // 获取当前年月
            Calendar cal = Calendar.getInstance();
            String yearMonth = cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1);
            int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
            String todayDateStr = String.format("%s-%02d", yearMonth, dayOfMonth);

            // 1. 先查具体日期
            String sql = "select top 1 gongzuoshijianks from gongzi_gongzuoshijian " +
                    "where gongsi = ? and schedule_title = ? and riqi = ?";

            List<YhRenShiGongZuoShiJian> list = base.query(YhRenShiGongZuoShiJian.class, sql,
                    realCompany, department, currentDate);

            if (list != null && !list.isEmpty()) {
                String startTime = list.get(0).getGongzuoshijianks();
                System.out.println("DEBUG: 找到具体日期开始时间: " + startTime);
                return startTime != null ? startTime.trim() : "09:00";
            }

            // 2. 查当月安排，检查work_days
            sql = "select * from gongzi_gongzuoshijian " +
                    "where gongsi = ? and schedule_title = ? and year_month = ?";
            list = base.query(YhRenShiGongZuoShiJian.class, sql, realCompany, department, yearMonth);

            if (list != null && !list.isEmpty()) {
                for (YhRenShiGongZuoShiJian record : list) {
                    String workDays = record.getWorkDays();
                    if (workDays != null && workDays.contains(todayDateStr)) {
                        String startTime = record.getGongzuoshijianks();
                        System.out.println("DEBUG: 找到当月安排开始时间: " + startTime);
                        return startTime != null ? startTime.trim() : "09:00";
                    }
                }
            }

            // 3. 查最新安排
            sql = "select top 1 gongzuoshijianks from gongzi_gongzuoshijian " +
                    "where gongsi = ? and schedule_title = ? order by year_month desc, riqi desc";
            list = base.query(YhRenShiGongZuoShiJian.class, sql, realCompany, department);

            if (list != null && !list.isEmpty()) {
                String startTime = list.get(0).getGongzuoshijianks();
                System.out.println("DEBUG: 找到最新安排开始时间: " + startTime);
                return startTime != null ? startTime.trim() : "09:00";
            }

            System.out.println("DEBUG: 未找到开始时间，使用默认值");
            return "09:00"; // 默认值

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("DEBUG: 查询开始时间异常: " + e.getMessage());
            return "09:00"; // 默认值
        } finally {
            if (base != null) {
                try {
                    base.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 签退操作 - 根据部门工作时间动态判断
     */
    public boolean signOut(String company, String name, String department, String signTime) {
        try {
            Calendar cal = Calendar.getInstance();
            String year = String.valueOf(cal.get(Calendar.YEAR));
            String moth = String.format("%02d", cal.get(Calendar.MONTH) + 1);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            // 查询记录
            YhRenShiKaoQinDaKa record = getByNameAndMonth(company, name, year, moth);
            if (record == null) {
                return false; // 没有签到记录
            }

            // 获取当天当前状态
            String currentStatus = record.getDayStatus(day);
            if (currentStatus == null || currentStatus.isEmpty()) {
                return false; // 没有签到
            }

            // 1. 查询该部门的工作结束时间
            String workEndTime = "18:00"; // 默认值
            if (department != null && !department.isEmpty()) {
                String endTime = getWorkEndTime(company, department);
                if (endTime != null && !endTime.isEmpty()) {
                    workEndTime = endTime;
                }
            }

            // 获取时分
            SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
            String signHourMinute;

            // 处理时间格式
            if (signTime.contains(":")) {
                String[] parts = signTime.split(":");
                signHourMinute = parts[0] + ":" + parts[1];
            } else {
                // 如果格式不正确，使用当前时间
                signHourMinute = new SimpleDateFormat("HH:mm").format(new Date());
            }

            Date currentTime = sdfTime.parse(signHourMinute);
            Date standardTime = sdfTime.parse(workEndTime);

            String finalStatus = currentStatus;

            if (currentTime.before(standardTime)) {
                long diff = standardTime.getTime() - currentTime.getTime();
                long minutes = diff / (1000 * 60);

                if ("早签".equals(currentStatus) && minutes > 30) {
                    finalStatus = "旷勤";
                } else if (minutes > 0 && minutes <= 30) {
                    finalStatus = "早退";
                }
            }

            // 更新状态
            boolean result = updateDayStatus(company, name, year, moth, day, finalStatus);

            // 如果更新成功，重新统计
            if (result) {
                updateAttendanceStatistics(company, name, year, moth);
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 查询部门的工作结束时间
     */
    private String getWorkEndTime(String company, String department) {
        renshiBaseDao base = new renshiBaseDao();
        try {
            System.out.println("DEBUG: 查询工作结束时间 - 公司: " + company + ", 部门: " + department);
            String realCompany = company.replace("_hr", "");

            // 获取当前日期
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String currentDate = dateFormat.format(new Date());

            // 获取当前年月和今天日期
            Calendar cal = Calendar.getInstance();
            String yearMonth = cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1);
            int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
            String todayDateStr = String.format("%s-%02d", yearMonth, dayOfMonth);

            // 1. 先查具体日期（riqi字段）
            String sql = "select top 1 gongzuoshijianjs from gongzi_gongzuoshijian " +
                    "where gongsi = ? and schedule_title = ? and riqi = ?";

            List<YhRenShiGongZuoShiJian> list = base.query(YhRenShiGongZuoShiJian.class, sql,
                    realCompany, department, currentDate);

            if (list != null && !list.isEmpty()) {
                String endTime = list.get(0).getGongzuoshijianjs();
                System.out.println("DEBUG: 找到具体日期结束时间: " + endTime);
                return endTime != null ? endTime.trim() : "18:00";
            }

            // 2. 查当月安排，检查work_days是否包含今天
            sql = "select * from gongzi_gongzuoshijian " +
                    "where gongsi = ? and schedule_title = ? and year_month = ?";
            list = base.query(YhRenShiGongZuoShiJian.class, sql, realCompany, department, yearMonth);

            if (list != null && !list.isEmpty()) {
                for (YhRenShiGongZuoShiJian record : list) {
                    String workDays = record.getWorkDays();
                    if (workDays != null && workDays.contains(todayDateStr)) {
                        String endTime = record.getGongzuoshijianjs();
                        System.out.println("DEBUG: 找到当月安排结束时间: " + endTime);
                        return endTime != null ? endTime.trim() : "18:00";
                    }
                }
            }

            // 3. 查最新安排
            sql = "select top 1 gongzuoshijianjs from gongzi_gongzuoshijian " +
                    "where gongsi = ? and schedule_title = ? order by year_month desc, riqi desc";
            list = base.query(YhRenShiGongZuoShiJian.class, sql, realCompany, department);

            if (list != null && !list.isEmpty()) {
                String endTime = list.get(0).getGongzuoshijianjs();
                System.out.println("DEBUG: 找到最新安排结束时间: " + endTime);
                return endTime != null ? endTime.trim() : "18:00";
            }

            System.out.println("DEBUG: 未找到结束时间，使用默认值");
            return "18:00"; // 默认值

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("DEBUG: 查询结束时间异常: " + e.getMessage());
            return "18:00"; // 默认值
        } finally {
            if (base != null) {
                try {
                    base.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * 检查部门是否存在 - 简化版
     */
// 在YhRenShiKaoQinDaKaService.java中修改checkDepartmentExists方法
    public boolean checkDepartmentExists(String company, String department) {
        renshiBaseDao base = new renshiBaseDao(); // 需要先初始化
        try {
            String realCompany = company.replace("_hr", "");

            // 检查部门是否存在
            String sql = "select id from gongzi_gongzuoshijian where gongsi = ? and schedule_title = ?";

            System.out.println("DEBUG checkDepartmentExists: 公司=" + realCompany + ", 部门=" + department);

            List<YhRenShiGongZuoShiJian> list = base.query(YhRenShiGongZuoShiJian.class, sql,
                    realCompany, department);

            System.out.println("DEBUG checkDepartmentExists: 查询结果大小=" + (list != null ? list.size() : 0));

            return list != null && !list.isEmpty();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("DEBUG checkDepartmentExists: 异常=" + e.getMessage());
            return false;
        } finally {
            if (base != null) {
                try {
                    base.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * 获取今日状态
     */
//    public String getTodayStatus(String company, String name) {
//        try {
//            Calendar cal = Calendar.getInstance();
//            String year = String.valueOf(cal.get(Calendar.YEAR));
//            String moth = String.format("%02d", cal.get(Calendar.MONTH) + 1);
//            int day = cal.get(Calendar.DAY_OF_MONTH);
//
//            YhRenShiKaoQinDaKa record = getByNameAndMonth(company, name, year, moth);
//            if (record != null) {
//                String status = record.getDayStatus(day);
//                return status != null ? status : "未打卡";
//            }
//            return "未打卡";
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "未打卡";
//        }
//    }
    public String getTodayStatus(String company, String name) {
        try {
            Calendar cal = Calendar.getInstance();
            String year = String.valueOf(cal.get(Calendar.YEAR));
            String moth = String.format("%02d", cal.get(Calendar.MONTH) + 1);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            // 构建当前日期字符串
            String currentDate = String.format("%s-%s-%02d", year, moth, day);

            System.out.println("DEBUG getTodayStatus: 检查" + currentDate + "的考勤状态");

            // 1. 检查今天是否有已通过的请假
            boolean hasApprovedLeave = hasApprovedLeave(company, name, currentDate);
            if (hasApprovedLeave) {
                System.out.println("DEBUG: 今天有已通过的请假，状态为: 休");
                return "休";
            }

            // 2. 如果没有请假，查询考勤状态
            YhRenShiKaoQinDaKa record = getByNameAndMonth(company, name, year, moth);
            if (record != null) {
                String status = record.getDayStatus(day);
                System.out.println("DEBUG: 考勤记录状态为: " + status);
                return status != null && !status.trim().isEmpty() ? status.trim() : "未打卡";
            }

            System.out.println("DEBUG: 没有考勤记录，状态为: 未打卡");
            return "未打卡";
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("DEBUG getTodayStatus异常: " + e.getMessage());
            return "未打卡";
        }
    }

    /**
     * 检查某天是否有已通过的请假
     */
    private boolean hasApprovedLeave(String company, String name, String checkDate) {
        renshiBaseDao base = null;
        try {
            base = new renshiBaseDao();
            String realCompany = company.replace("_hr", "");

            System.out.println("DEBUG hasApprovedLeave: 检查" + checkDate + "是否有已通过的请假");

            // 简单查询方式
            String sql = "select count(*) from gongzi_qingjiashenpi " +
                    "where gongsi = ? and xingming = ? " +
                    "and ? >= qsqingjiashijian and ? <= jzqingjiashijan " +
                    "and zhuangtai = '通过'";

            // 使用base.query执行查询
            // 这里需要创建一个简单的实体类来接收count结果
            // 或者使用更简单的方式

            // 更简单的方式：直接查询是否有记录
            String querySql = "select id from gongzi_qingjiashenpi " +
                    "where gongsi = ? and xingming = ? " +
                    "and ? >= qsqingjiashijian and ? <= jzqingjiashijan " +
                    "and zhuangtai = '通过'";

            // 使用已有的查询方法
            List<YhRenShiKaoQinQingJia> list = base.query(YhRenShiKaoQinQingJia.class, querySql,
                    realCompany, name, checkDate, checkDate);

            boolean hasLeave = list != null && !list.isEmpty();
            System.out.println("DEBUG: 查询结果 - 是否有请假: " + hasLeave);

            return hasLeave;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("DEBUG hasApprovedLeave异常: " + e.getMessage());
            return false;
        } finally {
            if (base != null) {
                try {
                    base.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * 删除考勤记录
     */
    public boolean delete(int id) {
        renshiBaseDao base = new renshiBaseDao();
        try {
            String sql = "delete from gongzi_kaoqinjilu where id = ?";
            return base.execute(sql, id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (base != null) {
                try {
                    base.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 查询全部数据
     */
    public List<YhRenShiKaoQinDaKa> getList(String company) {
        renshiBaseDao base = new renshiBaseDao();
        try {
            String sql = "select * from gongzi_kaoqinjilu where AO = ? order by year desc, moth desc";
            return base.query(YhRenShiKaoQinDaKa.class, sql, company.replace("_hr", ""));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (base != null) {
                try {
                    base.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 条件查询（日期范围）
     */
    public List<YhRenShiKaoQinDaKa> queryList(String company, String start_date, String stop_date) {
        renshiBaseDao base = new renshiBaseDao();
        try {
            String sql = "select * from gongzi_kaoqinjilu where AO = ? and year+'-'+moth >= ? and year+'-'+moth <= ? order by year desc, moth desc";
            return base.query(YhRenShiKaoQinDaKa.class, sql, company.replace("_hr", ""), start_date, stop_date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (base != null) {
                try {
                    base.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public YhRenShiGongZuoShiJian getWorkTimeByDepartment(String department, String currentDate) {
        renshiBaseDao base = null;
        try {
            base = new renshiBaseDao();

            System.out.println("DEBUG getWorkTimeByDepartment: 部门=" + department + ", 日期=" + currentDate);

            // 1. 先查今天是否有具体安排 (riqi字段)
            String sql = "select top 1 * from gongzi_gongzuoshijian where schedule_title = ? and riqi = ?";
            System.out.println("DEBUG 查询SQL1: " + sql);

            List<YhRenShiGongZuoShiJian> list = base.query(YhRenShiGongZuoShiJian.class, sql, department, currentDate);

            System.out.println("DEBUG 查询结果1大小: " + (list != null ? list.size() : 0));

            if (list != null && !list.isEmpty()) {
                YhRenShiGongZuoShiJian result = list.get(0);
                System.out.println("DEBUG 找到具体安排: ");
                System.out.println("  id: " + result.getId());
                System.out.println("  schedule_title: " + result.getScheduleTitle());
                System.out.println("  gongzuoshijianks: " + result.getGongzuoshijianks());
                System.out.println("  gongzuoshijianjs: " + result.getGongzuoshijianjs());
                System.out.println("  wuxiushijianks: " + result.getWuxiushijianks());
                System.out.println("  wuxiushijianjs: " + result.getWuxiushijianjs());
                System.out.println("  riqi: " + result.getRiqi());
                return result;
            }

            // 2. 如果今天没有具体安排，查询这个部门最新的安排
            sql = "select top 1 * from gongzi_gongzuoshijian where schedule_title = ? order by id desc";
            System.out.println("DEBUG 查询SQL2: " + sql);
            list = base.query(YhRenShiGongZuoShiJian.class, sql, department);

            System.out.println("DEBUG 查询结果2大小: " + (list != null ? list.size() : 0));

            if (list != null && !list.isEmpty()) {
                YhRenShiGongZuoShiJian result = list.get(0);
                System.out.println("DEBUG 找到最新安排: ");
                System.out.println("  id: " + result.getId());
                System.out.println("  schedule_title: " + result.getScheduleTitle());
                System.out.println("  gongzuoshijianks: " + result.getGongzuoshijianks());
                System.out.println("  gongzuoshijianjs: " + result.getGongzuoshijianjs());
                System.out.println("  wuxiushijianks: " + result.getWuxiushijianks());
                System.out.println("  wuxiushijianjs: " + result.getWuxiushijianjs());
                System.out.println("  riqi: " + result.getRiqi());
                System.out.println("  year_month: " + result.getYearMonth());
                return result;
            }

            System.out.println("DEBUG 未找到任何安排");
            return null;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("DEBUG 查询异常: " + e.getMessage());
            return null;
        } finally {
            if (base != null) {
                try {
                    base.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public YhRenShiGongZuoShiJian getWorkSchedule(String company, String department, String userName, String dateStr) {
        renshiBaseDao base = null;
        try {
            base = new renshiBaseDao();
            String realCompany = company.replace("_hr", "");

            // 模仿后端SQL查询逻辑
            String sql = "SELECT TOP 1 * FROM gongzi_gongzuoshijian " +
                    "WHERE schedule_status = 'active' " +
                    "AND work_days LIKE '%' + ? + '%' " +
                    "AND (schedule_title LIKE '%' + ? + '%' OR schedule_title LIKE '%' + ? + '%') " +
                    "AND gongsi = ? " +
                    "ORDER BY id DESC";

            System.out.println("DEBUG getWorkSchedule SQL: " + sql);
            System.out.println("DEBUG 参数: dateStr=" + dateStr + ", department=" + department + ", userName=" + userName + ", company=" + realCompany);

            List<YhRenShiGongZuoShiJian> list = base.query(YhRenShiGongZuoShiJian.class, sql,
                    dateStr, department, userName, realCompany);

            System.out.println("DEBUG getWorkSchedule 查询结果大小: " + (list != null ? list.size() : 0));

            if (list != null && !list.isEmpty()) {
                YhRenShiGongZuoShiJian result = list.get(0);
                System.out.println("DEBUG 找到工作安排: ");
                System.out.println("  schedule_title: " + result.getScheduleTitle());
                System.out.println("  gongzuoshijianks: " + result.getGongzuoshijianks());
                System.out.println("  gongzuoshijianjs: " + result.getGongzuoshijianjs());
                System.out.println("  wuxiushijianks: " + result.getWuxiushijianks());
                System.out.println("  wuxiushijianjs: " + result.getWuxiushijianjs());
                return result;
            }

            System.out.println("DEBUG 未找到工作安排");
            return null;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("DEBUG getWorkSchedule 异常: " + e.getMessage());
            return null;
        } finally {
            if (base != null) {
                try {
                    base.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
//public YhRenShiGongZuoShiJian getWorkSchedule(String company, String department, String userName, String dateStr) {
//    renshiBaseDao base = null;
//    try {
//        base = new renshiBaseDao();
//        String realCompany = company.replace("_hr", "");
//
//        System.out.println("DEBUG getWorkSchedule: 公司=" + realCompany +
//                ", 部门=" + department +
//                ", 姓名=" + userName +
//                ", 日期=" + dateStr);
//
//        // 改进的查询逻辑 - 模仿ASP.NET代码
//        // 1. 先检查schedule_status = 'active'
//        // 2. 检查work_days是否包含当前日期
//        // 3. 或者schedule_title匹配部门/姓名
//
//        // 方法1：查询具体日期的安排 (riqi字段)
//        String sql1 = "SELECT TOP 1 * FROM gongzi_gongzuoshijian " +
//                "WHERE schedule_status = 'active' " +
//                "AND gongsi = ? " +
//                "AND schedule_title = ? " +
//                "AND riqi = ? " +
//                "ORDER BY id DESC";
//
//        System.out.println("DEBUG 查询SQL1: " + sql1);
//
//        List<YhRenShiGongZuoShiJian> list = base.query(YhRenShiGongZuoShiJian.class, sql1,
//                realCompany, department, dateStr);
//
//        System.out.println("DEBUG 查询结果1大小: " + (list != null ? list.size() : 0));
//
//        if (list != null && !list.isEmpty()) {
//            YhRenShiGongZuoShiJian result = list.get(0);
//            System.out.println("DEBUG 找到具体日期安排: ");
//            System.out.println("  schedule_title: " + result.getScheduleTitle());
//            System.out.println("  gongzuoshijianks: " + result.getGongzuoshijianks());
//            System.out.println("  gongzuoshijianjs: " + result.getGongzuoshijianjs());
//            System.out.println("  wuxiushijianks: " + result.getWuxiushijianks());
//            System.out.println("  wuxiushijianjs: " + result.getWuxiushijianjs());
//            System.out.println("  riqi: " + result.getRiqi());
//            return result;
//        }
//
//        // 方法2：查询work_days包含今天日期的安排
//        // 注意：work_days可能是JSON数组格式，如 ["2026-01-20", "2026-01-21"]
//        String sql2 = "SELECT TOP 1 * FROM gongzi_gongzuoshijian " +
//                "WHERE schedule_status = 'active' " +
//                "AND gongsi = ? " +
//                "AND schedule_title = ? " +
//                "AND work_days LIKE '%' + ? + '%' " +  // 模糊匹配
//                "ORDER BY id DESC";
//
//        System.out.println("DEBUG 查询SQL2: " + sql2);
//
//        list = base.query(YhRenShiGongZuoShiJian.class, sql2,
//                realCompany, department, dateStr);
//
//        System.out.println("DEBUG 查询结果2大小: " + (list != null ? list.size() : 0));
//
//        if (list != null && !list.isEmpty()) {
//            YhRenShiGongZuoShiJian result = list.get(0);
//            System.out.println("DEBUG 找到work_days安排: ");
//            System.out.println("  schedule_title: " + result.getScheduleTitle());
//            System.out.println("  gongzuoshijianks: " + result.getGongzuoshijianks());
//            System.out.println("  gongzuoshijianjs: " + result.getGongzuoshijianjs());
//            System.out.println("  wuxiushijianks: " + result.getWuxiushijianks());
//            System.out.println("  wuxiushijianjs: " + result.getWuxiushijianjs());
//            System.out.println("  work_days: " + result.getWorkDays());
//            return result;
//        }
//
//        // 方法3：查询最新的安排
//        String sql3 = "SELECT TOP 1 * FROM gongzi_gongzuoshijian " +
//                "WHERE schedule_status = 'active' " +
//                "AND gongsi = ? " +
//                "AND schedule_title = ? " +
//                "ORDER BY id DESC";
//
//        System.out.println("DEBUG 查询SQL3: " + sql3);
//
//        list = base.query(YhRenShiGongZuoShiJian.class, sql3,
//                realCompany, department);
//
//        System.out.println("DEBUG 查询结果3大小: " + (list != null ? list.size() : 0));
//
//        if (list != null && !list.isEmpty()) {
//            YhRenShiGongZuoShiJian result = list.get(0);
//            System.out.println("DEBUG 找到最新安排: ");
//            System.out.println("  schedule_title: " + result.getScheduleTitle());
//            System.out.println("  gongzuoshijianks: " + result.getGongzuoshijianks());
//            System.out.println("  gongzuoshijianjs: " + result.getGongzuoshijianjs());
//            System.out.println("  wuxiushijianks: " + result.getWuxiushijianks());
//            System.out.println("  wuxiushijianjs: " + result.getWuxiushijianjs());
//            return result;
//        }
//
//        System.out.println("DEBUG: 未找到任何工作安排");
//        return null;
//
//    } catch (Exception e) {
//        e.printStackTrace();
//        System.out.println("DEBUG getWorkSchedule 异常: " + e.getMessage());
//        return null;
//    } finally {
//        if (base != null) {
//            try {
//                base.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
    /**
     * 更新考勤统计信息
     */
    /**
     /**
     * 更新考勤统计信息 - 修复版
     */
    public boolean updateAttendanceStatistics(String company, String name, String year, String month) {
        renshiBaseDao base = new renshiBaseDao(); // 创建新的连接
        try {
            String realCompany = company.replace("_hr", "");

            System.out.println("DEBUG updateAttendanceStatistics: 公司=" + realCompany +
                    ", 姓名=" + name + ", 年月=" + year + "-" + month);

            // 1. 先查询考勤记录
            String querySql = "select * from gongzi_kaoqinjilu " +
                    "where AO = ? and name = ? and year = ? and moth = ?";

            List<YhRenShiKaoQinDaKa> list = base.query(YhRenShiKaoQinDaKa.class, querySql,
                    realCompany, name, year, month);

            if (list == null || list.isEmpty()) {
                System.out.println("DEBUG: 未找到考勤记录");
                return false;
            }

            YhRenShiKaoQinDaKa record = list.get(0);

            int workDaysCount = 0;    // AK: 所有工作天数（出勤+早签+迟到+早退+旷勤）
            int lateEarlyCount = 0;   // AN: 迟到和早退天数

            // 2. 统计31天的状态
            for (int day = 1; day <= 31; day++) {
                String status = record.getDayStatus(day);

                if (status != null && !status.trim().isEmpty()) {
                    String statusTrim = status.trim();

                    // 检查是否有请假（状态为"休"）
                    String currentDate = String.format("%s-%s-%02d", year, month, day);
                    boolean hasLeave = hasApprovedLeave(company, name, currentDate);

                    if (hasLeave) {
                        System.out.println("DEBUG: 第" + day + "天 - 有请假，不计入工作天数");
                        continue; // 有请假，跳过不计入
                    }

                    // AK: 出勤、早签、迟到、早退、旷勤都算工作天数
                    if (statusTrim.equals("出勤") || statusTrim.equals("早签") ||
                            statusTrim.equals("迟到") || statusTrim.equals("早退") ||
                            statusTrim.equals("旷勤")) {
                        workDaysCount++;
                    }

                    // AN: 只统计迟到和早退
                    if (statusTrim.equals("迟到") || statusTrim.equals("早退")) {
                        lateEarlyCount++;
                    }

                    System.out.println("DEBUG: 第" + day + "天 - 状态:" + statusTrim +
                            " - AK:" + (statusTrim.equals("出勤") || statusTrim.equals("早签") ||
                            statusTrim.equals("迟到") || statusTrim.equals("早退") ||
                            statusTrim.equals("旷勤") ? "1" : "0") +
                            " - AN:" + (statusTrim.equals("迟到") || statusTrim.equals("早退") ? "1" : "0"));
                }
            }

            System.out.println("DEBUG 统计结果:");
            System.out.println("  AK(工作天数): " + workDaysCount);
            System.out.println("  AN(迟到早退): " + lateEarlyCount);

            // 3. 关闭当前连接
            base.close();

            // 4. 创建新的数据库连接用于更新
            renshiBaseDao updateBase = new renshiBaseDao();
            try {
                String updateSql = "update gongzi_kaoqinjilu set AK = ?, AN = ? " +
                        "where id = ? and AO = ?";

                boolean result = updateBase.execute(updateSql,
                        String.valueOf(workDaysCount), // AK
                        String.valueOf(lateEarlyCount), // AN
                        record.getId(),
                        realCompany);

                System.out.println("DEBUG: 更新统计结果: " + result);
                return result;

            } finally {
                if (updateBase != null) {
                    updateBase.close();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("DEBUG: updateAttendanceStatistics异常: " + e.getMessage());
            return false;
        } finally {
            if (base != null) {
                try {
                    base.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 计算指定部门和年月的工作天数
     */
    public int calculateScheduledWorkDays(String company, String department, String year, String month) {
        renshiBaseDao base = null;
        try {
            base = new renshiBaseDao();
            String realCompany = company.replace("_hr", "");

            // 构建当前年月字符串，格式如：2024-01
            String currentYearMonth = year + "-" + month;

            System.out.println("DEBUG calculateScheduledWorkDays: 查询排班天数 - 公司=" + realCompany +
                    ", 部门=" + department + ", 年月=" + currentYearMonth);

            // 查询排班计划 - 根据schedule_title（部门）和year_month
            String sql = "SELECT work_days FROM gongzi_gongzuoshijian " +
                    "WHERE gongsi = ? AND schedule_title = ? AND year_month = ?";

            List<YhRenShiGongZuoShiJian> list = base.query(YhRenShiGongZuoShiJian.class, sql,
                    realCompany, department, currentYearMonth);

            if (list == null || list.isEmpty()) {
                System.out.println("DEBUG: 未找到" + currentYearMonth + "的排班计划");
                return 0;
            }

            YhRenShiGongZuoShiJian schedule = list.get(0);
            String workDaysStr = schedule.getWorkDays();

            if (workDaysStr == null || workDaysStr.trim().isEmpty()) {
                System.out.println("DEBUG: work_days字段为空");
                return 0;
            }

            System.out.println("DEBUG: work_days原始数据: " + workDaysStr);

            // 解析work_days字段，统计工作天数
            return parseWorkDaysCount(workDaysStr, currentYearMonth);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("DEBUG calculateScheduledWorkDays异常: " + e.getMessage());
            return 0;
        } finally {
            if (base != null) {
                try {
                    base.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * 根据请假记录更新考勤状态为"休"
     */
    public boolean updateLeaveStatusToAttendance(String company, String name) {
        System.out.println("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
        System.out.println("★★★★★ updateLeaveStatusToAttendance开始执行 ★★★★★");
        System.out.println("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");

        renshiBaseDao base = null;
        try {
            String realCompany = company.replace("_hr", "");

            System.out.println("DEBUG: 公司=" + realCompany + ", 姓名=" + name);

            // 1. 获取当前年月
            Calendar cal = Calendar.getInstance();
            String year = String.valueOf(cal.get(Calendar.YEAR));
            String month = String.format("%02d", cal.get(Calendar.MONTH) + 1);

            System.out.println("DEBUG: 当前年月=" + year + "-" + month);

            // 2. 查询所有已通过的请假记录
            base = new renshiBaseDao();
            String leaveSql = "select * from gongzi_qingjiashenpi " +
                    "where gongsi = ? and xingming = ? and zhuangtai = '通过'";

            List<YhRenShiKaoQinQingJia> leaveList = base.query(YhRenShiKaoQinQingJia.class, leaveSql, realCompany, name);

            System.out.println("DEBUG: 查询到已通过的请假记录数量: " + (leaveList != null ? leaveList.size() : 0));

            if (leaveList == null || leaveList.isEmpty()) {
                System.out.println("DEBUG: 没有已通过的请假记录");
                return false;
            }

            boolean hasUpdates = false;

            // 3. 处理每个请假记录
            for (YhRenShiKaoQinQingJia leave : leaveList) {
                String startDate = leave.getQsqingjiashijian();
                String endDate = leave.getJzqingjiashijian();

                System.out.println("DEBUG: 处理请假: " + startDate + " 至 " + endDate);

                if (startDate == null || endDate == null) {
                    System.out.println("DEBUG: 请假日期为空，继续处理下一条");
                    continue;
                }

                // 4. 解析请假日期范围
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date start = sdf.parse(startDate);
                    Date end = sdf.parse(endDate);

                    Calendar dateCal = Calendar.getInstance();
                    dateCal.setTime(start);

                    // 5. 遍历请假期间的每一天
                    while (!dateCal.getTime().after(end)) {
                        Date currentDate = dateCal.getTime();
                        String dateStr = sdf.format(currentDate);

                        // 提取年月日
                        String currentYear = dateStr.substring(0, 4);
                        String currentMonth = dateStr.substring(5, 7);
                        int day = Integer.parseInt(dateStr.substring(8, 10));

                        // 只处理当前月份的记录
                        if (currentYear.equals(year) && currentMonth.equals(month)) {
                            System.out.println("DEBUG: 更新 " + dateStr + " 为休");

                            // 6. 更新考勤状态
                            boolean updateResult = updateDayStatus(company, name, currentYear, currentMonth, day, "休");

                            if (updateResult) {
                                System.out.println("DEBUG: ✓ 成功更新 " + dateStr + " 为休");
                                hasUpdates = true;
                            } else {
                                System.out.println("DEBUG: ✗ 更新 " + dateStr + " 失败");
                            }
                        } else {
                            System.out.println("DEBUG: 跳过非当前月份的日期: " + dateStr);
                        }

                        // 下一天
                        dateCal.add(Calendar.DAY_OF_MONTH, 1);
                    }

                } catch (Exception e) {
                    System.out.println("DEBUG: 解析请假日期失败: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            // 7. 如果更新成功，重新统计考勤
            if (hasUpdates) {
                System.out.println("DEBUG: 请假状态更新完成，重新统计考勤数据");
                updateAttendanceStatistics(company, name, year, month);
            } else {
                System.out.println("DEBUG: 没有需要更新的请假记录");
            }

            System.out.println("★★★★★ updateLeaveStatusToAttendance执行完成 ★★★★★");
            return hasUpdates;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("DEBUG updateLeaveStatusToAttendance异常: " + e.getMessage());
            return false;
        } finally {
            if (base != null) {
                try {
                    base.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 解析work_days字段，统计指定年月的工作天数
     */
    private int parseWorkDaysCount(String workDaysStr, String currentYearMonth) {
        int count = 0;

        try {
            // 去除可能的空格和换行
            workDaysStr = workDaysStr.trim();

            System.out.println("DEBUG parseWorkDaysCount: 解析字符串=" + workDaysStr);

            // 情况1: JSON数组格式 ["2024-01-15", "2024-01-16"]
            if (workDaysStr.startsWith("[") && workDaysStr.endsWith("]")) {
                // 移除方括号
                String daysStr = workDaysStr.substring(1, workDaysStr.length() - 1);

                // 按逗号分割
                String[] days = daysStr.split(",");
                for (String day : days) {
                    String trimmedDay = day.trim().replace("\"", "").replace("'", "");
                    if (trimmedDay.startsWith(currentYearMonth + "-")) {
                        count++;
                        System.out.println("DEBUG: 找到工作日: " + trimmedDay);
                    }
                }
            }
            // 情况2: 逗号分隔的字符串 2024-01-15,2024-01-16
            else if (workDaysStr.contains(",")) {
                String[] days = workDaysStr.split(",");
                for (String day : days) {
                    String trimmedDay = day.trim();
                    if (trimmedDay.startsWith(currentYearMonth + "-")) {
                        count++;
                        System.out.println("DEBUG: 找到工作日: " + trimmedDay);
                    }
                }
            }
            // 情况3: 单个日期 2024-01-15
            else if (workDaysStr.contains(currentYearMonth + "-")) {
                count = 1;
                System.out.println("DEBUG: 找到单个工作日: " + workDaysStr);
            }

            System.out.println("DEBUG: 共找到" + count + "个" + currentYearMonth + "的工作日");
            return count;

        } catch (Exception e) {
            System.out.println("DEBUG: 解析work_days失败: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }
}