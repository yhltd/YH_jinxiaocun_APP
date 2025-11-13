package com.example.myapplication.jiaowu.dao;

import android.annotation.SuppressLint;
import android.util.Log;

import com.example.myapplication.utils.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JiaowuServerDao<T> {

    private static Connection conn;

    private static PreparedStatement preparedStatement;

    private static ResultSet resultSet;

    //服务器地址
    private static final String SERVER_NAME = "yhocn.cn";
    //数据库名称
    private static final String DATABASE_NAME = "xueshengguanlixitong_excel";
    //用户名
    private static final String USER_NAME = "sa";
    //密码
    private static final String PASSWORD = "Lyh07910_001";

    public JiaowuServerDao() {
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
                // SQL Server 参数设置
                if (params[i] instanceof Integer) {
                    preparedStatement.setInt(i + 1, (Integer) params[i]);
                } else if (params[i] instanceof Long) {
                    preparedStatement.setLong(i + 1, (Long) params[i]);
                } else if (params[i] instanceof Date) {
                    preparedStatement.setTimestamp(i + 1, new Timestamp(((Date) params[i]).getTime()));
                } else {
                    preparedStatement.setString(i + 1, params[i] != null ? handlerParam(params[i]) : "");
                }
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

    public byte[] queryFile(String sql, Object... params) {
        try {
            preparedStatement = conn.prepareStatement(sql);

            if (params != null) for (int i = 0; i < params.length; i++) {
                // SQL Server 参数设置
                if (params[i] instanceof Integer) {
                    preparedStatement.setInt(i + 1, (Integer) params[i]);
                } else if (params[i] instanceof Long) {
                    preparedStatement.setLong(i + 1, (Long) params[i]);
                } else {
                    preparedStatement.setString(i + 1, params[i] != null ? params[i].toString() : "");
                }
            }

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                return resultSet.getBytes("contract_doc");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }

        return null;
    }

    public boolean execute(String sql, Object... params) {
        int result = 0;
        try {
            // SQL Server 支持 RETURN_GENERATED_KEYS
            preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            if (params != null) for (int i = 0; i < params.length; i++) {
                // SQL Server 参数设置
                if (params[i] instanceof Integer) {
                    preparedStatement.setInt(i + 1, (Integer) params[i]);
                } else if (params[i] instanceof Long) {
                    preparedStatement.setLong(i + 1, (Long) params[i]);
                } else if (params[i] instanceof Date) {
                    preparedStatement.setTimestamp(i + 1, new Timestamp(((Date) params[i]).getTime()));
                } else {
                    preparedStatement.setString(i + 1, params[i] != null ? handlerParam(params[i]) : "");
                }
            }

            result = preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return result > 0;
    }

    public long executeOfId(String sql, Object... params) {
        long result = 0;
        try {
            // SQL Server 支持 RETURN_GENERATED_KEYS
            preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            if (params != null) for (int i = 0; i < params.length; i++) {
                // SQL Server 参数设置
                if (params[i] instanceof Integer) {
                    preparedStatement.setInt(i + 1, (Integer) params[i]);
                } else if (params[i] instanceof Long) {
                    preparedStatement.setLong(i + 1, (Long) params[i]);
                } else if (params[i] instanceof Date) {
                    preparedStatement.setTimestamp(i + 1, new Timestamp(((Date) params[i]).getTime()));
                } else {
                    preparedStatement.setString(i + 1, params[i] != null ? handlerParam(params[i]) : "");
                }
            }

            result = preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                result = resultSet.getLong(1);
            }
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return result;
    }

    public boolean updateFile(String sql, int id, InputStream inputStream) {
        int result = 0;

        try {
            preparedStatement = conn.prepareStatement(sql);

            // SQL Server 的二进制流处理
            preparedStatement.setBinaryStream(1, inputStream, inputStream.available());
            preparedStatement.setInt(2, id);

            result = preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        } finally {
            close();
        }

        return result > 0;
    }

    private String handlerParam(Object obj) {
        String tName = obj.getClass().getName();
        if ("java.util.Date".equals(obj.getClass().getName())) {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // SQL Server 时间格式
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
                                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") : // SQL Server 时间格式
                                    new SimpleDateFormat("yyyy-MM-dd");
                    obj = simpleDateFormat.parse(obj.toString());
                    break;
                case "java.lang.String":
                    obj = obj.toString();
                    break;
                case "float":
                    obj = Float.parseFloat(obj.toString());
                    break;
                case "int":
                    obj = Integer.parseInt(obj.toString());
                    break;
                case "long":
                    obj = Long.parseLong(obj.toString());
                    break;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return obj;
    }

    private boolean isExistsColumn(ResultSet rs, String columnName) {
        try {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                if (columnName.equalsIgnoreCase(metaData.getColumnName(i))) {
                    return true;
                }
            }
            return false;
        } catch (SQLException e) {
            return false;
        }
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
