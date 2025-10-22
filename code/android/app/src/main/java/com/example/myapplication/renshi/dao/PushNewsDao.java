package com.example.myapplication.renshi.dao;

import android.annotation.SuppressLint;
import android.util.Log;

import com.example.myapplication.utils.StringUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PushNewsDao<T> {
    private static Connection conn;

    private static PreparedStatement preparedStatement;

    private static ResultSet resultSet;

    //服务器地址
    private static final String SERVER_NAME = "yhocn.cn";
    //数据库名称
    private static final String DATABASE_NAME = "yh_notice";
    //用户名
    private static final String USER_NAME = "sa";
    //密码
    private static final String PASSWORD = "Lyh07910_001";

    public PushNewsDao() {
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
        List<T> list = null;
        try {
            list = new ArrayList<>();
            preparedStatement = conn.prepareStatement(sql);

            if (params != null) for (int i = 0; i < params.length; i++) {
                preparedStatement.setString(i + 1, params[i] != null ? handlerParam(params[i]) : "");
            }

            resultSet = preparedStatement.executeQuery();

            Field[] fields = tClass.getDeclaredFields();
            while (resultSet.next()) {
                T entity = tClass.newInstance();
                for (Field field : fields) {
                    field.setAccessible(true);
                    String columnName = StringUtils.humpToLine(field.getName());

                    if (isExistsColumn(resultSet, columnName)) {
                        Object obj = resultSet.getObject(columnName);
                        if (obj != null)
                            field.set(entity, handlerResult(StringUtils.cast(obj), field));
                    }
                }
                list.add(entity);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            close();
        }
    }

    private String handlerParam(Object obj) {
        String tName = obj.getClass().getName();
        if ("java.util.Date".equals(obj.getClass().getName())) {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            obj = simpleDateFormat.format((Date) obj);
        }
        return obj.toString();
    }

    private Object handlerResult(Object obj, Field field) {
        Log.d("fieldName", field.getType().getName());
        try {
            switch (field.getType().getName()) {
                case "java.util.Date":
                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat simpleDateFormat =
                            obj.toString().length() == 19 ?
                                    new SimpleDateFormat("yyyy-MM-dd hh:mm:ss") :
                                    new SimpleDateFormat("yyyy-MM-dd");
                    obj = simpleDateFormat.parse(obj.toString());
                    break;
                case "java.lang.String":
                    obj = obj.toString();
                    break;
                case "float":
                    obj = Float.parseFloat(obj.toString());
                    break;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return obj;
    }

    private boolean isExistsColumn(ResultSet rs, String columnName) {
        try {
            if (rs.findColumn(columnName) > 0) {
                return true;
            }
        } catch (SQLException e) {
            return false;
        }
        return false;
    }

    public void close() {
        try {
            if (resultSet != null && !resultSet.isClosed()) {
                resultSet.close();
            }
            if (preparedStatement != null && !preparedStatement.isClosed()) {
                preparedStatement.close();
            }
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
