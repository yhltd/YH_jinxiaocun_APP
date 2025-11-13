package com.example.myapplication.jxc.dao;

import android.annotation.SuppressLint;
import android.util.Log;

import com.example.myapplication.utils.StringUtils;

import org.apache.poi.ss.formula.functions.T;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JxcServerDao<T> {

    private static Connection conn;

    private static PreparedStatement preparedStatement;

    private static ResultSet resultSet;

    //服务器地址
    private static final String SERVER_NAME = "yhocn.cn";
    //数据库名称
    private static final String DATABASE_NAME = "yh_jinxiaocun_excel";
    //用户名
    private static final String USER_NAME = "sa";
    //密码
    private static final String PASSWORD = "Lyh07910_001";

    public JxcServerDao() {
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            StringBuffer stringBuffer = new StringBuffer("jdbc:jtds:sqlserver://");
            stringBuffer.append(SERVER_NAME);
            stringBuffer.append(":1433/");
            stringBuffer.append(DATABASE_NAME);
            stringBuffer.append(";characterEncoding=utf8");
            stringBuffer.append(";useLOBs=false");
            conn = DriverManager.getConnection(stringBuffer.toString(), USER_NAME, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public List<T> query(Class<T> tClass, String sql, Object... params) {
        List<T> list = new ArrayList<>();
        try {
            preparedStatement = conn.prepareStatement(sql);
            setParameters(preparedStatement, params);

            resultSet = preparedStatement.executeQuery();
            Field[] fields = tClass.getDeclaredFields();

            while (resultSet.next()) {
                T entity = tClass.newInstance();
                for (Field field : fields) {
                    field.setAccessible(true);
                    String columnName = StringUtils.humpToLine(field.getName());

                    if (isExistsColumn(resultSet, columnName)) {
                        Object value = resultSet.getObject(columnName);
                        if (value != null) {
                            Object processedValue = handlerResult(value, field);
                            field.set(entity, processedValue);
                        }
                    }
                }
                list.add(entity);
            }
        } catch (Exception e) {
            Log.e("JxcServerDao", "查询数据失败", e);
        } finally {
            close();
        }
        return list;
    }

    /**
     * 查询文件数据 - 保持原有功能不变
     */
    public byte[] queryFile(String sql, Object... params) {
        try {
            preparedStatement = conn.prepareStatement(sql);
            setParameters(preparedStatement, params);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getBytes("contract_doc");
            }
        } catch (Exception e) {
            Log.e("JxcServerDao", "查询文件失败", e);
        } finally {
            close();
        }
        return null;
    }

    /**
     * 执行更新操作 - 保持原有功能不变
     */
    public boolean execute(String sql, Object... params) {
        int result = 0;
        try {
            preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            setParameters(preparedStatement, params);

            result = preparedStatement.executeUpdate();
        } catch (Exception e) {
            Log.e("JxcServerDao", "执行SQL失败", e);
        } finally {
            close();
        }
        return result > 0;
    }

    /**
     * 执行并返回生成的主键 - 保持原有功能不变
     */
    public long executeOfId(String sql, Object... params) {
        long generatedKey = 0;
        try {
            preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            setParameters(preparedStatement, params);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    generatedKey = resultSet.getLong(1);
                }
            }
        } catch (Exception e) {
            Log.e("JxcServerDao", "执行SQL获取ID失败", e);
        } finally {
            close();
        }
        return generatedKey;
    }

    /**
     * 更新文件字段 - 保持原有功能不变
     */
    public boolean updateFile(String sql, int id, InputStream inputStream) {
        int result = 0;
        try {
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setBinaryStream(1, inputStream, inputStream.available());
            preparedStatement.setInt(2, id);

            result = preparedStatement.executeUpdate();
        } catch (SQLException | IOException e) {
            Log.e("JxcServerDao", "更新文件失败", e);
        } finally {
            close();
        }
        return result > 0;
    }

    /**
     * 参数处理 - 保持原有逻辑
     */
    private String handlerParam(Object obj) {
        if (obj == null) {
            return "";
        }

        if (obj instanceof java.util.Date) {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return simpleDateFormat.format((java.util.Date) obj);
        }
        return obj.toString();
    }

    /**
     * 结果集处理 - 保持原有逻辑
     */
    private Object handlerResult(Object obj, Field field) {
        if (obj == null) return null;

        try {
            String fieldType = field.getType().getName();
            switch (fieldType) {
                case "java.util.Date":
                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat simpleDateFormat = obj.toString().length() == 19 ?
                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") :
                            new SimpleDateFormat("yyyy-MM-dd");
                    return simpleDateFormat.parse(obj.toString());
                case "java.lang.String":
                    return obj.toString();
                case "float":
                    return Float.parseFloat(obj.toString());
                case "int":
                    return Integer.parseInt(obj.toString());
                case "long":
                    return Long.parseLong(obj.toString());
                case "double":
                    return Double.parseDouble(obj.toString());
                default:
                    return obj;
            }
        } catch (ParseException e) {
            Log.e("JxcServerDao", "结果集处理失败", e);
            return obj;
        }
    }

    /**
     * 设置预处理参数 - 新增方法提高代码复用
     */
    private void setParameters(PreparedStatement stmt, Object... params) throws SQLException {
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                String paramValue = params[i] != null ? handlerParam(params[i]) : "";
                stmt.setString(i + 1, paramValue);
            }
        }
    }

    /**
     * 检查列是否存在 - 优化实现
     */
    private boolean isExistsColumn(ResultSet rs, String columnName) {
        try {
            rs.findColumn(columnName);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * 关闭资源 - 保持原有逻辑
     */
    public void close() {
        try {
            if (resultSet != null && !resultSet.isClosed()) {
                resultSet.close();
            }
            if (preparedStatement != null && !preparedStatement.isClosed()) {
                preparedStatement.close();
            }
            // 注意：连接不要在这里关闭，应该由调用方管理连接生命周期
        } catch (SQLException e) {
            Log.e("JxcServerDao", "关闭资源失败", e);
        } finally {
            resultSet = null;
            preparedStatement = null;
        }
    }


}
