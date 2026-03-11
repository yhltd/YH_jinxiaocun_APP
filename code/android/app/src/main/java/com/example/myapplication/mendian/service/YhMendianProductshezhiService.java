//package com.example.myapplication.mendian.service;
//
//import com.example.myapplication.mendian.dao.MendianDao;
//import com.example.myapplication.mendian.entity.YhMendianMemberinfo;
//import com.example.myapplication.mendian.entity.YhMendianProductshezhi;
//import com.example.myapplication.mendian.entity.YhMendianUsers;
//
//import java.util.List;
//
//public class YhMendianProductshezhiService {
//    private MendianDao base;
//
//    /**
//     * 查询全部商品信息
//     */
//    public List<YhMendianProductshezhi> getList(String produce_name, String type,String company) {
//        String sql = "select * from product where company = ? and product_name like '%' ? '%' and type like '%' ? '%' ";
//        base = new MendianDao();
//        List<YhMendianProductshezhi> list = base.query(YhMendianProductshezhi.class, sql, company, produce_name,type);
//        return list;
//    }
//
//    /**
//     * 查询商品详情
//     */
//
//    public List<YhMendianProductshezhi> getProduct(String produce_name) {
//        String sql = "select * from product where product_name = ? ";
//        base = new MendianDao();
//        List<YhMendianProductshezhi> list = base.query(YhMendianProductshezhi.class, sql,produce_name);
//        return list;
//    }
//
//    /**
//     * 查询全部商品类别
//     */
//    public List<YhMendianProductshezhi> getTypeList(String company) {
//        String sql = "select type from product where company = ? group by type";
//        base = new MendianDao();
//        List<YhMendianProductshezhi> list = base.query(YhMendianProductshezhi.class, sql, company);
//        return list;
//    }
//
//    /**
//     * 查询单种类别商品
//     */
//    public List<YhMendianProductshezhi> getListByType(String company,String type) {
//        String sql = "select * from product where company = ? and type = ?";
//        base = new MendianDao();
//        List<YhMendianProductshezhi> list = base.query(YhMendianProductshezhi.class, sql, company,type);
//        return list;
//    }
//
//    /**
//     * 新增员工
//     */
//    public boolean insertByProductshezhi(YhMendianProductshezhi yhMendianProductshezhi) {
//        String sql = "insert into product(company,product_bianhao,type,product_name,unit,price,chengben,specifications,practice,tingyong,xiangqing,photo,photo1,photo2,beizhu1) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//        base = new MendianDao();
//        long result = base.executeOfId(sql,
//                yhMendianProductshezhi.getCompany(),
//                yhMendianProductshezhi.getProduct_bianhao(),
//                yhMendianProductshezhi.getType(),
//                yhMendianProductshezhi.getProduct_name(),
//                yhMendianProductshezhi.getUnit(),
//                yhMendianProductshezhi.getPrice(),
//                yhMendianProductshezhi.getChengben(),
//                yhMendianProductshezhi.getSpecifications(),
//                yhMendianProductshezhi.getPractice(),
//                yhMendianProductshezhi.getTingyong(),
//                yhMendianProductshezhi.getXiangqing(),  // 新增详情字段
//                yhMendianProductshezhi.getPhoto(),      // 新增图片1字段
//                yhMendianProductshezhi.getPhoto1(),     // 新增图片2字段
//                yhMendianProductshezhi.getPhoto2() ,
//                yhMendianProductshezhi.getBeizhu1()
//                );
//        return result > 0;
//    }
//
//    /**
//     * 修改商品设置
//     */
//    public boolean updateByProductshezhi(YhMendianProductshezhi yhMendianProductshezhi) {
//        String sql = "update product set product_bianhao=?,type=?,product_name=?,unit=?,price=?,chengben=?,specifications=?,practice=?,tingyong=?,xiangqing=?,photo=?,photo1=?,photo2=?,beizhu1=? where id=? ";
//        base = new MendianDao();
//        boolean result = base.execute(sql,
//                yhMendianProductshezhi.getProduct_bianhao(),
//                yhMendianProductshezhi.getType(),
//                yhMendianProductshezhi.getProduct_name(),
//                yhMendianProductshezhi.getUnit(),
//                yhMendianProductshezhi.getPrice(),
//                yhMendianProductshezhi.getChengben(),
//                yhMendianProductshezhi.getSpecifications(),
//                yhMendianProductshezhi.getPractice(),
//                yhMendianProductshezhi.getTingyong(),
//                yhMendianProductshezhi.getXiangqing(),  // 新增详情字段
//                yhMendianProductshezhi.getPhoto(),      // 新增图片1字段
//                yhMendianProductshezhi.getPhoto1(),     // 新增图片2字段
//                yhMendianProductshezhi.getPhoto2(),     // 新增图片3字段
//                yhMendianProductshezhi.getBeizhu1(),
//                yhMendianProductshezhi.getId()
//        );
//        return result;
//    }
//
//    /**
//     * 删除员工
//     */
//    public boolean deleteByProductshezhi(int id) {
//        String sql = "delete from product where id = ?";
//        base = new MendianDao();
//        return base.execute(sql, id);
//    }
//
//
//    public List<YhMendianProductshezhi> getActiveList(String produce_name, String type, String company) {
//        String sql = "select * from product where company = ? and (tingyong != '是' or tingyong is null or tingyong = '') and product_name like '%' ? '%' and type like '%' ? '%' ";
//        base = new MendianDao();
//        List<YhMendianProductshezhi> list = base.query(YhMendianProductshezhi.class, sql, company, produce_name, type);
//        return list;
//    }
//}

package com.example.myapplication.mendian.service;

import android.util.Log;

import com.example.myapplication.mendian.dao.MendianDao;
import com.example.myapplication.mendian.entity.YhMendianMemberinfo;
import com.example.myapplication.mendian.entity.YhMendianProductshezhi;
import com.example.myapplication.mendian.entity.YhMendianUsers;

import java.io.File;
import java.net.URLEncoder;
import java.util.List;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class YhMendianProductshezhiService {
    private MendianDao base;

    /**
     * 查询全部商品信息
     */
    public List<YhMendianProductshezhi> getList(String produce_name, String type, String company) {
        String sql = "select * from product where company = ? and product_name like '%' ? '%' and type like '%' ? '%' ";
        base = new MendianDao();
        List<YhMendianProductshezhi> list = base.query(YhMendianProductshezhi.class, sql, company, produce_name, type);
        return list;
    }

    /**
     * 查询商品详情
     */
    public List<YhMendianProductshezhi> getProduct(String produce_name) {
        String sql = "select * from product where product_name = ? ";
        base = new MendianDao();
        List<YhMendianProductshezhi> list = base.query(YhMendianProductshezhi.class, sql, produce_name);
        return list;
    }

    /**
     * 查询全部商品类别
     */
    public List<YhMendianProductshezhi> getTypeList(String company) {
        String sql = "select type from product where company = ? group by type";
        base = new MendianDao();
        List<YhMendianProductshezhi> list = base.query(YhMendianProductshezhi.class, sql, company);
        return list;
    }

    /**
     * 查询单种类别商品
     */
    public List<YhMendianProductshezhi> getListByType(String company, String type) {
        String sql = "select * from product where company = ? and type = ?";
        base = new MendianDao();
        List<YhMendianProductshezhi> list = base.query(YhMendianProductshezhi.class, sql, company, type);
        return list;
    }

    /**
     * 新增员工
     */
    public boolean insertByProductshezhi(YhMendianProductshezhi yhMendianProductshezhi) {
        String sql = "insert into product(company,product_bianhao,type,product_name,unit,price,chengben,specifications,practice,tingyong,xiangqing,photo,photo1,photo2,beizhu1) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        base = new MendianDao();
        long result = base.executeOfId(sql,
                yhMendianProductshezhi.getCompany(),
                yhMendianProductshezhi.getProduct_bianhao(),
                yhMendianProductshezhi.getType(),
                yhMendianProductshezhi.getProduct_name(),
                yhMendianProductshezhi.getUnit(),
                yhMendianProductshezhi.getPrice(),
                yhMendianProductshezhi.getChengben(),
                yhMendianProductshezhi.getSpecifications(),
                yhMendianProductshezhi.getPractice(),
                yhMendianProductshezhi.getTingyong(),
                yhMendianProductshezhi.getXiangqing(),
                yhMendianProductshezhi.getPhoto(),
                yhMendianProductshezhi.getPhoto1(),
                yhMendianProductshezhi.getPhoto2(),
                yhMendianProductshezhi.getBeizhu1()
        );
        return result > 0;
    }

    /**
     * 修改商品设置
     */
    public boolean updateByProductshezhi(YhMendianProductshezhi yhMendianProductshezhi) {
        String sql = "update product set product_bianhao=?,type=?,product_name=?,unit=?,price=?,chengben=?,specifications=?,practice=?,tingyong=?,xiangqing=?,photo=?,photo1=?,photo2=?,beizhu1=? where id=? ";
        base = new MendianDao();
        boolean result = base.execute(sql,
                yhMendianProductshezhi.getProduct_bianhao(),
                yhMendianProductshezhi.getType(),
                yhMendianProductshezhi.getProduct_name(),
                yhMendianProductshezhi.getUnit(),
                yhMendianProductshezhi.getPrice(),
                yhMendianProductshezhi.getChengben(),
                yhMendianProductshezhi.getSpecifications(),
                yhMendianProductshezhi.getPractice(),
                yhMendianProductshezhi.getTingyong(),
                yhMendianProductshezhi.getXiangqing(),
                yhMendianProductshezhi.getPhoto(),
                yhMendianProductshezhi.getPhoto1(),
                yhMendianProductshezhi.getPhoto2(),
                yhMendianProductshezhi.getBeizhu1(),
                yhMendianProductshezhi.getId()
        );
        return result;
    }

    /**
     * 删除员工
     */
    public boolean deleteByProductshezhi(int id) {
        String sql = "delete from product where id = ?";
        base = new MendianDao();
        return base.execute(sql, id);
    }

    public List<YhMendianProductshezhi> getActiveList(String produce_name, String type, String company) {
        String sql = "select * from product where company = ? and (tingyong != '是' or tingyong is null or tingyong = '') and product_name like '%' ? '%' and type like '%' ? '%' ";
        base = new MendianDao();
        List<YhMendianProductshezhi> list = base.query(YhMendianProductshezhi.class, sql, company, produce_name, type);
        return list;
    }

    // ==================== 文件上传相关方法 ====================

    /**
     * 获取当前图片URL
     */
    public String getCurrentImage(int id, String fieldName) {
        String selectSql = "select " + fieldName + " from product where id = ?";
        MendianDao selectDao = new MendianDao();
        List<YhMendianProductshezhi> result = selectDao.query(YhMendianProductshezhi.class, selectSql, id);
        if (result != null && result.size() > 0) {
            switch (fieldName) {
                case "photo": return result.get(0).getPhoto();
                case "photo1": return result.get(0).getPhoto1();
                case "photo2": return result.get(0).getPhoto2();
            }
        }
        return null;
    }

    /**
     * 更新图片记录
     */
    public boolean updateImageRecord(int id, String fieldName, String imageUrl) {
        String sql = "update product set " + fieldName + " = ? where id = ?";
        MendianDao updateDao = new MendianDao();
        return updateDao.execute(sql, imageUrl, id);
    }

    /**
     * 清空图片记录
     */
    public boolean clearImageRecord(int id, String fieldName) {
        String sql = "update product set " + fieldName + " = null where id = ?";
        MendianDao updateDao = new MendianDao();
        return updateDao.execute(sql, id);
    }

    /**
     * 上传文件到服务器
     */
    public void uploadFile(File file, String fileName, String path, String kongjian,
                           String recordId, String recordName, String userFileName,
                           UploadCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                DataOutputStream dos = null;
                FileInputStream fis = null;
                try {
                    // 1. 构建最终文件名
                    String finalFileName = fileName;
                    if (userFileName != null && !userFileName.trim().isEmpty()) {
                        String baseName = userFileName.trim().replaceAll("[\\\\/:*?\"<>|]", "_");
                        if (fileName.contains(".")) {
                            String extension = fileName.substring(fileName.lastIndexOf('.'));
                            finalFileName = baseName + extension;
                        } else {
                            finalFileName = baseName;
                        }
                    }

                    // 2. 创建连接到9097端口
                    URL url = new URL("https://yhocn.cn:9097/file/upload");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setUseCaches(false);
                    conn.setRequestMethod("POST");
                    conn.setConnectTimeout(30000);
                    conn.setReadTimeout(60000);

                    // 3. 设置边界
                    String boundary = "----WebKitFormBoundary" + System.currentTimeMillis();
                    conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

                    // 4. 构建请求体
                    dos = new DataOutputStream(conn.getOutputStream());

                    // 5. 写入文件
                    dos.writeBytes("--" + boundary + "\r\n");
                    dos.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"" + finalFileName + "\"\r\n");
                    dos.writeBytes("Content-Type: application/octet-stream\r\n\r\n");

                    fis = new FileInputStream(file);
                    byte[] buffer = new byte[8192];
                    int count;
                    while ((count = fis.read(buffer)) != -1) {
                        dos.write(buffer, 0, count);
                    }
                    dos.writeBytes("\r\n");

                    // 6. 写入表单参数
                    String mendianPath = "/mendian/";
                    writeFormField(dos, boundary, "name", finalFileName);
                    writeFormField(dos, boundary, "path", mendianPath);
                    writeFormField(dos, boundary, "kongjian", kongjian);
                    writeFormField(dos, boundary, "fileType",
                            fileName.contains(".") ? fileName.substring(fileName.lastIndexOf('.') + 1) : "");
                    writeFormField(dos, boundary, "recordId", recordId);
                    writeFormField(dos, boundary, "recordName", recordName);
                    writeFormField(dos, boundary, "userFileName", userFileName != null ? userFileName : "");
                    writeFormField(dos, boundary, "timestamp", String.valueOf(System.currentTimeMillis()));

                    // 结束请求体
                    dos.writeBytes("--" + boundary + "--\r\n");
                    dos.flush();

                    // 7. 处理响应
                    int responseCode = conn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        InputStream is = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();

                        JSONObject json = new JSONObject(response.toString());
                        if (json.optInt("code", -1) == 200) {
                            String fileUrl = "http://yhocn.cn:9088" + mendianPath + finalFileName;
                            callback.onSuccess(fileUrl);
                        } else {
                            callback.onFailure(json.optString("msg", "上传失败"));
                        }
                    } else {
                        callback.onFailure("服务器错误: " + responseCode);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFailure("上传失败: " + e.getMessage());
                } finally {
                    try {
                        if (fis != null) fis.close();
                        if (dos != null) dos.close();
                        if (conn != null) conn.disconnect();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * 从服务器删除文件
     */
    public void deleteFileFromServer(String fileName, String path, DeleteCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                DataOutputStream dos = null;
                try {
                    // 清理文件名
                    String cleanFileName = fileName;
                    if (fileName.contains(".")) {
                        cleanFileName = fileName.substring(0, fileName.lastIndexOf('.'));
                    }

                    // 创建连接
                    URL url = new URL("https://yhocn.cn:9097/file/delete");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                    conn.setConnectTimeout(30000);
                    conn.setReadTimeout(30000);

                    // 对中文字符进行URL编码
                    String encodedOrderNumber = URLEncoder.encode(cleanFileName, "UTF-8");
                    String encodedPath = URLEncoder.encode(path, "UTF-8");
                    String formData = "order_number=" + encodedOrderNumber + "&path=" + encodedPath;

                    dos = new DataOutputStream(conn.getOutputStream());
                    dos.write(formData.getBytes("UTF-8"));
                    dos.flush();

                    int responseCode = conn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        InputStream is = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();

                        JSONObject json = new JSONObject(response.toString());
                        int code = json.optInt("code", -1);
                        boolean success = json.optBoolean("success", false);

                        if (code == 200 || success) {
                            callback.onSuccess();
                        } else {
                            callback.onFailure(json.optString("msg", "删除失败"));
                        }
                    } else {
                        callback.onFailure("服务器错误: " + responseCode);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFailure("删除失败: " + e.getMessage());
                } finally {
                    try {
                        if (dos != null) dos.close();
                        if (conn != null) conn.disconnect();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void writeFormField(DataOutputStream dos, String boundary, String name, String value) throws Exception {
        dos.writeBytes("--" + boundary + "\r\n");
        dos.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"\r\n\r\n");
        dos.write(value.getBytes("UTF-8"));
        dos.writeBytes("\r\n");
    }

    // 回调接口
    public interface UploadCallback {
        void onSuccess(String fileUrl);
        void onFailure(String error);
    }

    public interface DeleteCallback {
        void onSuccess();
        void onFailure(String error);
    }
}