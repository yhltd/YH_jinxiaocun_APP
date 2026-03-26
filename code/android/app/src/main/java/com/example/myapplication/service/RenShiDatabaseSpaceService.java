package com.example.myapplication.service;

import android.util.Log;

import com.example.myapplication.renshi.dao.renshiBaseDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class RenShiDatabaseSpaceService {
    private static final String TAG = "RenShiDatabaseSpaceService";

    private com.example.myapplication.renshi.dao.renshiBaseDao<?> renshiBaseDao;

    public RenShiDatabaseSpaceService() {
        renshiBaseDao = new renshiBaseDao<>();
    }

    /**
     * 获取人事系统数据库中各表按公司统计的使用空间
     * @param companyName 公司名称
     * @return 总使用空间（KB）
     */
    public double getDatabaseSizeByCompany(String companyName) {
        double totalSizeKB = 0;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;


        // 🆕 处理公司名称，去掉 _hr 后缀
        String processedCompanyName = companyName;
        if (companyName != null && companyName.endsWith("_hr")) {
            processedCompanyName = companyName.substring(0, companyName.length() - 3);
        } else {
        }

        // 定义人事系统的表名和对应的公司字段名
        Map<String, String> tables = new HashMap<>();
        tables.put("gongzi_dongtaimingxi", "gongsi");
        tables.put("gongzi_gongzimingxi", "BD");
        tables.put("gongzi_jianliguanli", "gongsi");
        tables.put("gongzi_kaoqinjilu", "AO");
        tables.put("gongzi_kaoqinmingxi", "K");
        tables.put("gongzi_lizhishenpi", "gongsi");
        tables.put("gongzi_shenpi", "gongsi");
        tables.put("gongzi_shezhi", "gongsi");
        tables.put("gongzi_renyuan", "L");
        tables.put("gongzi_qingjiashenpi", "gongsi");

        try {
            // 通过反射获取 renshiBaseDao 中的静态连接
            Connection conn = getConnectionFromDao();

            if (conn == null || conn.isClosed()) {
                return 0;
            }

            for (Map.Entry<String, String> entry : tables.entrySet()) {
                String tableName = entry.getKey();
                String companyColumn = entry.getValue();

                try {

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
                        continue;
                    }

                    // 🆕 使用处理后的公司名称进行查询
                    String countSql = "SELECT COUNT(*) FROM " + tableName + " WHERE " + companyColumn + " LIKE ?";
                    preparedStatement = conn.prepareStatement(countSql);
                    preparedStatement.setString(1, "%" + processedCompanyName + "%");
                    resultSet = preparedStatement.executeQuery();

                    int rowCount = 0;
                    if (resultSet.next()) {
                        rowCount = resultSet.getInt(1);
                    }
                    resultSet.close();
                    preparedStatement.close();

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

                            double totalSizeKB_Table = parseSizeToKB(dataSize);
                            int totalRowCount = parseRowCount(rows);

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
                // 注意：不要关闭 conn，因为它是 renshiBaseDao 共享的
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        return totalSizeKB;
    }

    /**
     * 通过反射获取 renshiBaseDao 中的静态连接
     */
    private Connection getConnectionFromDao() {
        try {
            java.lang.reflect.Field field = renshiBaseDao.class.getDeclaredField("conn");
            field.setAccessible(true);
            return (Connection) field.get(null);
        } catch (Exception e) {
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
            // "8 KB" 是空表的最小空间，按0处理
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
        }

        return 0;
    }
}

