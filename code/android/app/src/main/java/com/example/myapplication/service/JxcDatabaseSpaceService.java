package com.example.myapplication.service;

import android.util.Log;

import com.example.myapplication.jxc.dao.JxcBaseDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class JxcDatabaseSpaceService {
    private static final String TAG = "JxcDatabaseSpaceService";

    private JxcBaseDao<?> jxcBaseDao;

    public JxcDatabaseSpaceService() {
        jxcBaseDao = new JxcBaseDao<>();
    }

    /**
     * 获取进销存系统数据库中各表按公司统计的使用空间（MySQL版本）
     * @param companyName 公司名称
     * @return 总使用空间（KB）
     */
    public double getDatabaseSizeByCompany(String companyName) {
        double totalSizeKB = 0;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Log.d(TAG, "========== 开始计算进销存系统数据库空间 ==========");
        Log.d(TAG, "公司名称: " + companyName);

        // 定义进销存系统的表名和对应的公司字段名
        Map<String, String> tables = new HashMap<>();
        tables.put("yh_jinxiaocun_cangku", "gongsi");
        tables.put("yh_jinxiaocun_chuhuofang", "gongsi");
        tables.put("yh_jinxiaocun_jichuziliao", "gs_name");
        tables.put("yh_jinxiaocun_jinhuofang", "gongsi");
        tables.put("yh_jinxiaocun_mingxi", "gs_name");
        tables.put("yh_jinxiaocun_qichushu", "gs_name");
        tables.put("yh_jinxiaocun_tuihuomingxi", "gs_name");
        tables.put("yh_jinxiaocun_user", "gongsi");
        tables.put("yh_jinxiaocun_zhengli", "gs_name");

        try {
            // 通过反射获取 JxcBaseDao 中的静态连接
            Connection conn = getConnectionFromDao();

            if (conn == null || conn.isClosed()) {
                Log.e(TAG, "进销存系统数据库连接不可用");
                return 0;
            }

            Log.d(TAG, "获取进销存系统数据库连接成功");

            for (Map.Entry<String, String> entry : tables.entrySet()) {
                String tableName = entry.getKey();
                String companyColumn = entry.getValue();

                try {
                    Log.d(TAG, "处理表: " + tableName);

                    // MySQL检查表是否存在
                    String checkTableSql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = ?";
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
                        // MySQL获取表的大小
                        String spaceSql = "SELECT " +
                                "ROUND(((data_length + index_length) / 1024), 2) AS size_kb, " +
                                "table_rows AS row_count " +
                                "FROM information_schema.tables " +
                                "WHERE table_schema = DATABASE() AND table_name = ?";

                        preparedStatement = conn.prepareStatement(spaceSql);
                        preparedStatement.setString(1, tableName);
                        resultSet = preparedStatement.executeQuery();

                        if (resultSet.next()) {
                            double totalSizeKB_Table = resultSet.getDouble("size_kb");
                            int totalRowCount = resultSet.getInt("row_count");

                            Log.d(TAG, String.format("表 %s - 总大小: %.2f KB, 总行数: %d",
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
            Log.e(TAG, "获取进销存系统数据库大小失败: " + e.getMessage());
            e.printStackTrace();
            totalSizeKB = 0;
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                // 注意：不要关闭 conn，因为它是 JxcBaseDao 共享的
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Log.d(TAG, "========================================");
        Log.d(TAG, String.format("进销存系统 - 公司 %s 总使用空间: %.2f KB (%.2f MB)", companyName, totalSizeKB, totalSizeKB / 1024));
        Log.d(TAG, "========================================");

        return totalSizeKB;
    }

    /**
     * 通过反射获取 JxcBaseDao 中的静态连接
     */
    private Connection getConnectionFromDao() {
        try {
            java.lang.reflect.Field field = JxcBaseDao.class.getDeclaredField("conn");
            field.setAccessible(true);
            return (Connection) field.get(null);
        } catch (Exception e) {
            Log.e(TAG, "获取进销存系统数据库连接失败: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}