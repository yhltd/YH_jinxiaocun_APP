package com.example.myapplication.service;

import android.util.Log;

import com.example.myapplication.scheduling.dao.SchedulingDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class DatabaseSpaceService {
    private static final String TAG = "DatabaseSpaceService";

    private SchedulingDao<?> schedulingDao;

    public DatabaseSpaceService() {
        schedulingDao = new SchedulingDao<>();
    }

    /**
     * 获取排产系统数据库中各表按公司统计的使用空间
     * @param companyName 公司名称
     * @return 总使用空间（KB）
     */
    public double getDatabaseSizeByCompany(String companyName) {
        double totalSizeKB = 0;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Log.d(TAG, "========== 开始计算数据库空间 ==========");
        Log.d(TAG, "公司名称: " + companyName);

        // 定义表名和对应的公司字段名
        Map<String, String> tables = new HashMap<>();
        tables.put("bom_info", "company");
        tables.put("department", "company");
        tables.put("holiday_config", "company");
        tables.put("module_type", "company");
        tables.put("order_check", "company");
        tables.put("order_info", "company");
        tables.put("paibanbiao_info", "remarks1");
        tables.put("paibanbiao_renyuan", "company");
        tables.put("paibanbiao_detail", "company");
        tables.put("shengchanxian", "gongsi");
        tables.put("time_config", "company");
        tables.put("user_info", "company");
        tables.put("work_detail", "company");

        try {
            Connection conn = getConnectionFromDao();

            if (conn == null || conn.isClosed()) {
                Log.e(TAG, "数据库连接不可用");
                return 0;
            }

            Log.d(TAG, "获取数据库连接成功");

            for (Map.Entry<String, String> entry : tables.entrySet()) {
                String tableName = entry.getKey();
                String companyColumn = entry.getValue();

                try {
                    Log.d(TAG, "处理表: " + tableName);

                    // 检查表是否存在
                    String checkTableSql = "SELECT COUNT(*) FROM sysobjects WHERE xtype='U' AND name=?";
                    preparedStatement = conn.prepareStatement(checkTableSql);
                    preparedStatement.setString(1, tableName);
                    resultSet = preparedStatement.executeQuery();

                    boolean tableExists = false;
                    if (resultSet.next()) {
                        tableExists = resultSet.getInt(1) > 0;
                    }
                    resultSet.close();
                    preparedStatement.close();

                    if (!tableExists) {
                        Log.d(TAG, String.format("表 %s 不存在，跳过", tableName));
                        continue;
                    }

                    // 查询该公司的数据行数
                    String countSql = "SELECT COUNT(*) FROM " + tableName + " WHERE " + companyColumn + " LIKE ?";
                    preparedStatement = conn.prepareStatement(countSql);
                    preparedStatement.setString(1, "%" + companyName + "%");
                    resultSet = preparedStatement.executeQuery();

                    int rowCount = 0;
                    if (resultSet.next()) {
                        rowCount = resultSet.getInt(1);
                    }
                    resultSet.close();
                    preparedStatement.close();

                    Log.d(TAG, String.format("表 %s: 公司 %s 的行数 = %d", tableName, companyName, rowCount));

                    if (rowCount > 0) {
                        // 获取表的总大小
                        String spaceSql = "EXEC sp_spaceused ?";
                        preparedStatement = conn.prepareStatement(spaceSql);
                        preparedStatement.setString(1, tableName);
                        resultSet = preparedStatement.executeQuery();

                        if (resultSet.next()) {
                            // 获取表总大小
                            String dataSize = resultSet.getString("data");
                            String rows = resultSet.getString("rows");

                            Log.d(TAG, String.format("表 %s - data: %s, rows: %s", tableName, dataSize, rows));

                            double totalSizeKB_Table = parseSizeToKB(dataSize);

                            // 修复：正确解析行数，处理可能包含逗号的情况
                            int totalRowCount = parseRowCount(rows);

                            Log.d(TAG, String.format("表 %s - 解析后: data=%.2f KB, rows=%d",
                                    tableName, totalSizeKB_Table, totalRowCount));

                            if (totalRowCount > 0) {
                                double ratio = (double) rowCount / totalRowCount;
                                double companySizeKB = totalSizeKB_Table * ratio;
                                totalSizeKB += companySizeKB;

                                Log.d(TAG, String.format(
                                        "表 %s: 公司占比=%.2f%%, 公司大小=%.2f KB",
                                        tableName, ratio * 100, companySizeKB));
                            }
                        }
                        resultSet.close();
                        preparedStatement.close();
                    }

                } catch (Exception e) {
                    Log.e(TAG, String.format("获取表 %s 大小失败: %s", tableName, e.getMessage()));
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            Log.e(TAG, "获取数据库大小失败: " + e.getMessage());
            e.printStackTrace();
            totalSizeKB = 0;
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Log.d(TAG, "========================================");
        Log.d(TAG, String.format("公司 %s 总使用空间: %.2f KB (%.2f MB)", companyName, totalSizeKB, totalSizeKB / 1024));
        Log.d(TAG, "========================================");

        return totalSizeKB;
    }

    /**
     * 通过反射获取 SchedulingDao 中的静态连接
     */
    private Connection getConnectionFromDao() {
        try {
            java.lang.reflect.Field field = SchedulingDao.class.getDeclaredField("conn");
            field.setAccessible(true);
            return (Connection) field.get(null);
        } catch (Exception e) {
            Log.e(TAG, "获取数据库连接失败: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解析行数字符串，处理可能包含逗号的情况
     * 例如: "12,345" -> 12345
     */
    private int parseRowCount(String rowsStr) {
        if (rowsStr == null || rowsStr.isEmpty()) {
            return 0;
        }

        try {
            // 移除所有逗号和空格
            String cleanStr = rowsStr.replaceAll(",", "").replaceAll(" ", "").trim();
            return Integer.parseInt(cleanStr);
        } catch (Exception e) {
            Log.e(TAG, "解析行数失败: " + rowsStr, e);
            return 0;
        }
    }

    /**
     * 解析大小字符串为 KB
     */
    private double parseSizeToKB(String sizeStr) {
        if (sizeStr == null || sizeStr.isEmpty()) {
            return 0;
        }

        sizeStr = sizeStr.trim();

        try {
            // 如果是 "8 KB"，按 0 处理（空表）
            if ("8 KB".equals(sizeStr)) {
                return 0;
            }

            String[] parts = sizeStr.split(" ");
            if (parts.length == 2) {
                double value = Double.parseDouble(parts[0]);
                String unit = parts[1].toUpperCase();

                switch (unit) {
                    case "KB":
                        return value;
                    case "MB":
                        return value * 1024;
                    case "GB":
                        return value * 1024 * 1024;
                    case "TB":
                        return value * 1024 * 1024 * 1024;
                    default:
                        return value;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "解析大小字符串失败: " + sizeStr, e);
        }

        return 0;
    }
}