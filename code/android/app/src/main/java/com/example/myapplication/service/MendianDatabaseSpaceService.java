package com.example.myapplication.service;

import android.util.Log;

import com.example.myapplication.mendian.dao.MendianDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class MendianDatabaseSpaceService {
    private static final String TAG = "MendianDatabaseSpaceService";

    private MendianDao<?> mendianDao;

    public MendianDatabaseSpaceService() {
        mendianDao = new MendianDao<>();
    }

    /**
     * 获取门店系统数据库中各表按公司统计的使用空间（MySQL版本）
     * @param companyName 公司名称
     * @return 总使用空间（KB）
     */
    public double getDatabaseSizeByCompany(String companyName) {
        double totalSizeKB = 0;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        // 定义门店系统的表名和对应的公司字段名
        Map<String, String> tables = new HashMap<>();
        tables.put("customer", "gongsi");
        tables.put("day_trading", "gongsi");
        tables.put("member_info", "company");
        tables.put("member_jibie", "company");
        tables.put("order_table", "company");
        tables.put("orders", "company");
        tables.put("orders_details", "company");
        tables.put("product", "company");
        tables.put("users", "company");

        try {
            // 通过反射获取 MendianDao 中的静态连接
            Connection conn = getConnectionFromDao();

            if (conn == null || conn.isClosed()) {
                return 0;
            }


            for (Map.Entry<String, String> entry : tables.entrySet()) {
                String tableName = entry.getKey();
                String companyColumn = entry.getValue();

                try {

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

                            if (totalRowCount > 0) {
                                double ratio = (double) rowCount / totalRowCount;
                                double companySizeKB = totalSizeKB_Table * ratio;
                                totalSizeKB += companySizeKB;

                            }
                        }
                        resultSet.close();
                        preparedStatement.close();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            totalSizeKB = 0;
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                // 注意：不要关闭 conn，因为它是 MendianDao 共享的
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return totalSizeKB;
    }

    /**
     * 通过反射获取 MendianDao 中的静态连接
     */
    private Connection getConnectionFromDao() {
        try {
            java.lang.reflect.Field field = MendianDao.class.getDeclaredField("conn");
            field.setAccessible(true);
            return (Connection) field.get(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
