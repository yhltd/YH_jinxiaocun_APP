package com.example.myapplication.fenquan.service;

import com.example.myapplication.fenquan.dao.FenquanDao;
import com.example.myapplication.fenquan.entity.Renyuan;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RenyuanService {
    private FenquanDao base;

    public Renyuan login(String username, String password, String company) {
        String sql = "select * from baitaoquanxian_renyun where D = ? and E = ? and B = ? ";
        base = new FenquanDao();
        List<Renyuan> list = base.query(Renyuan.class, sql, username, password, company);
        return list != null && list.size() > 0 ? list.get(0) : null;
    }

    public List<String> getCompany() {
        String sql = "select B from baitaoquanxian_renyun group by B";
        base = new FenquanDao();
        List<Renyuan> list = base.query(Renyuan.class, sql);
        List<String> companyList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            companyList.add(list.get(i).getB());
        }
        return companyList != null && companyList.size() > 0 ? companyList : null;
    }

    public List<Renyuan> getList(String company, String c) {
        String sql = "select * from baitaoquanxian_renyun where B=? and C like '%'+ ? +'%' ";
        base = new FenquanDao();
        List<Renyuan> list = base.query(Renyuan.class, sql, company, c);
        return list;
    }

    public boolean insert(Renyuan renyuan) {
        String sql = "insert into baitaoquanxian_renyun(B,C,D,E,renyuan_id,zhuangtai,email,phone,bianhao,bumen,wenjian) values(?,?,?,?,?,?,?,?,?,?,?)";
        base = new FenquanDao();
        boolean result = base.execute(sql,
                renyuan.getB(),
                renyuan.getC(),
                renyuan.getD(),
                renyuan.getE(),
                renyuan.getRenyuan_id(),
                renyuan.getZhuangtai(),
                renyuan.getEmail(),
                renyuan.getPhone(),
                renyuan.getBianhao(),
                renyuan.getBumen(),
                renyuan.getWenjian());  // 新增文件字段
        return result;
    }

    public boolean update(Renyuan renyuan) {
        String sql = "update baitaoquanxian_renyun set B=?,C=?,D=?,E=?,renyuan_id=?,zhuangtai=?,email=?,phone=?,bianhao=?,bumen=?,wenjian=? where id=? ";
        base = new FenquanDao();
        boolean result = base.execute(sql,
                renyuan.getB(),
                renyuan.getC(),
                renyuan.getD(),
                renyuan.getE(),
                renyuan.getRenyuan_id(),
                renyuan.getZhuangtai(),
                renyuan.getEmail(),
                renyuan.getPhone(),
                renyuan.getBianhao(),
                renyuan.getBumen(),
                renyuan.getWenjian(),  // 新增文件字段
                renyuan.getId());
        return result;
    }

    public boolean delete(int id) {
        String sql = "delete from baitaoquanxian_renyun where id = ?";
        base = new FenquanDao();
        return base.execute(sql, id);
    }

    // ==================== 文件上传相关方法 ====================

    /**
     * 检查是否可以更新文件字段
     */
    public boolean canUpdateFiles(int id) {
        String selectSql = "select wenjian from baitaoquanxian_renyun where id = ?";
        FenquanDao checkDao = new FenquanDao();
        List<Renyuan> result = checkDao.query(Renyuan.class, selectSql, id);
        return result != null && result.size() > 0;
    }

    /**
     * 获取当前文件字符串
     */
    public String getCurrentFiles(int id) {
        String selectSql = "select wenjian from baitaoquanxian_renyun where id = ?";
        FenquanDao selectDao = new FenquanDao();
        List<Renyuan> result = selectDao.query(Renyuan.class, selectSql, id);
        if (result != null && result.size() > 0) {
            return result.get(0).getWenjian();
        }
        return null;
    }

    /**
     * 构建新的文件URL字符串（追加方式）
     */
    public String buildNewFilesString(String currentFiles, String fileUrls) {
        if (currentFiles == null || currentFiles.trim().isEmpty()) {
            return fileUrls;
        }
        return currentFiles + "," + fileUrls;
    }

    /**
     * 构建新的文件字符串（移除指定文件URL）
     */
    public String buildNewFilesString(String currentFiles, String fileUrlToDelete, boolean isDelete) {
        if (currentFiles == null || currentFiles.trim().isEmpty()) {
            return "";
        }
        String[] fileArray = currentFiles.split(",");
        StringBuilder newFiles = new StringBuilder();
        for (String file : fileArray) {
            if (!file.trim().equals(fileUrlToDelete.trim())) {
                if (newFiles.length() > 0) {
                    newFiles.append(",");
                }
                newFiles.append(file.trim());
            }
        }
        return newFiles.toString();
    }

    /**
     * 执行更新文件记录操作
     */
    public boolean updateFileRecord(int id, String newFileUrls) {
        String sql = "update baitaoquanxian_renyun set wenjian = ? where id = ?";
        FenquanDao updateDao = new FenquanDao();
        return updateDao.execute(sql, newFileUrls, id);
    }

    /**
     * 更新文件字段 - 新增文件（整合所有步骤）
     */
    public boolean updateFilesWithCheck(int id, String fileUrls) {
        if (!canUpdateFiles(id)) {
            return false;
        }
        String currentFiles = getCurrentFiles(id);
        String newFileUrls = buildNewFilesString(currentFiles, fileUrls);
        return updateFileRecord(id, newFileUrls);
    }

    /**
     * 删除数据库中的单个文件记录（整合检查和执行）
     */
    public boolean deleteFileWithCheck(int id, String fileUrlToDelete) {
        String currentFiles = getCurrentFiles(id);
        if (currentFiles == null || currentFiles.trim().isEmpty()) {
            return false;
        }
        String newFiles = buildNewFilesString(currentFiles, fileUrlToDelete, true);
        return updateFileRecord(id, newFiles);
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
                    String renyuanPath = "/fenquan/";
                    writeFormField(dos, boundary, "name", finalFileName);
                    writeFormField(dos, boundary, "path", renyuanPath);
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
                            String fileUrl = "http://yhocn.cn:9088" + renyuanPath + finalFileName;
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