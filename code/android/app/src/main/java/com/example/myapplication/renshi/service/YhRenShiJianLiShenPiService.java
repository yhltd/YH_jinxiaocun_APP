package com.example.myapplication.renshi.service;

import android.util.Log;

import com.example.myapplication.renshi.dao.renshiBaseDao;
import com.example.myapplication.renshi.entity.YhRenShiJianLiGuanLi;

import org.json.JSONObject;

import java.io.File;
import java.net.URLEncoder;
import java.util.List;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class YhRenShiJianLiShenPiService {
    private renshiBaseDao base;

    /**
     * 查询简历管理列表
     */
    public List<YhRenShiJianLiGuanLi> getList(String company, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        String sql = "select * from gongzi_jianliguanli where gongsi = ? and zhuangtai = '待处理' ";

        base = new renshiBaseDao();
        return base.query(YhRenShiJianLiGuanLi.class, sql , company.replace("_hr",""));
    }

    /**
     * 根据状态筛选查询
     */
    public List<YhRenShiJianLiGuanLi> getListByStatus(String company, String status) {
        String sql;
        List<YhRenShiJianLiGuanLi> result;

        if (status == null || status.isEmpty() || status.equals("全部")) {
            // 查询所有记录
            sql = "select * from gongzi_jianliguanli where gongsi = ? and zhuangtai = '待处理' order by id desc";
            base = new renshiBaseDao();
            result = base.query(YhRenShiJianLiGuanLi.class, sql, company.replace("_hr",""));
        } else {
            // 按状态筛选
            sql = "select * from gongzi_jianliguanli where gongsi = ? and zhuangtai = ? order by id desc";
            base = new renshiBaseDao();
            result = base.query(YhRenShiJianLiGuanLi.class, sql, company.replace("_hr",""), status);
        }

        return result;
    }

    /**
     * 获取总记录数
     */
    public int getCount(String company) {
        String sql = "select count(id) as total from gongzi_jianliguanli where gongsi = ? " +
                "and zhuangtai = '待处理' ";
        base = new renshiBaseDao();
        List<Object[]> result = base.query(Object[].class, sql, company.replace("_hr",""));
        if (result != null && result.size() > 0) {
            return ((Number) result.get(0)[0]).intValue();
        }
        return 0;
    }

    /**
     * 新增简历记录
     */
    public boolean insert(YhRenShiJianLiGuanLi entity) {
        String sql = "insert into gongzi_jianliguanli (gongsi, touliren, xueli, mubiaogangwei, tijiaoshijian, beizhu, zhuangtai) " +
                "values(?, ?, ?, ?, ?, ?, ?)";
        base = new renshiBaseDao();
        return base.execute(sql,
                entity.getGongsi(),
                entity.getTouliren(),
                entity.getXueli(),
                entity.getMubiaogangwei(),
                entity.getTijiaoshijian(),
                entity.getBeizhu(),
                entity.getZhuangtai());
    }

    /**
     * 更新简历记录
     */
    public boolean update(YhRenShiJianLiGuanLi entity) {
        String sql = "update gongzi_jianliguanli set touliren=?, xueli=?, mubiaogangwei=?, tijiaoshijian=?, beizhu=?, zhuangtai=? where id=?";
        base = new renshiBaseDao();
        return base.execute(sql,
                entity.getTouliren(),
                entity.getXueli(),
                entity.getMubiaogangwei(),
                entity.getTijiaoshijian(),
                entity.getBeizhu(),
                entity.getZhuangtai(),
                entity.getId());
    }

    /**
     * 删除简历记录
     */
    public boolean canDelete(int id) {
        String checkSql = "select wenjian from gongzi_jianliguanli where id = ?";
        renshiBaseDao checkDao = new renshiBaseDao();
        List<YhRenShiJianLiGuanLi> result = checkDao.query(YhRenShiJianLiGuanLi.class, checkSql, id);

        if (result != null && result.size() > 0) {
            YhRenShiJianLiGuanLi entity = result.get(0);
            String wenjian = entity.getWenjian();
            return wenjian == null || wenjian.trim().isEmpty();
        }
        return false;
    }

    public boolean delete(int id) {
        renshiBaseDao deleteDao = new renshiBaseDao();
        String deletesql = "delete from gongzi_jianliguanli where id = ?";
        return deleteDao.execute(deletesql, id);
    }

    public boolean deleteWithCheck(int id) {
        if (!canDelete(id)) {
            return false;
        }
        return delete(id);
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
                try {
                    // 1. 构建最终文件名
                    String finalFileName = fileName;
                    if (userFileName != null && !userFileName.trim().isEmpty()) {
                        String baseName = userFileName.trim().replaceAll("[\\\\/:*?\"<>|]", "_");
                        if (baseName.contains(".")) {
                            baseName = baseName.substring(0, baseName.lastIndexOf('.'));
                        }
                        String extension = fileName.substring(fileName.lastIndexOf('.'));
                        finalFileName = baseName + extension;
                    }

                    Log.d("FileUpload", "最终文件名（中文）: " + finalFileName);

                    // 2. 创建连接
                    URL url = new URL("https://yhocn.cn:9097/file/upload");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setUseCaches(false);
                    conn.setRequestMethod("POST");
                    conn.setConnectTimeout(30000);
                    conn.setReadTimeout(60000);

                    // 3. 设置边界 - 重要：不要指定charset
                    String boundary = "----WebKitFormBoundary" + System.currentTimeMillis();
                    conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

                    // 4. 构建请求体
                    DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

                    // 5. 写入文件 - 关键修改：filename直接使用原始中文
                    dos.writeBytes("--" + boundary + "\r\n");
                    dos.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"" + finalFileName + "\"\r\n");
                    dos.writeBytes("Content-Type: application/octet-stream\r\n\r\n");

                    // 写入文件内容
                    FileInputStream fis = new FileInputStream(file);
                    byte[] buffer = new byte[8192];
                    int count;
                    while ((count = fis.read(buffer)) != -1) {
                        dos.write(buffer, 0, count);
                    }
                    fis.close();
                    dos.writeBytes("\r\n");

                    // 6. 写入其他参数 - 直接写入原始值
                    writeFormField(dos, boundary, "name", finalFileName);
                    writeFormField(dos, boundary, "path", path);
                    writeFormField(dos, boundary, "kongjian", kongjian);
                    writeFormField(dos, boundary, "fileType",
                            finalFileName.substring(finalFileName.lastIndexOf('.') + 1));
                    writeFormField(dos, boundary, "recordId", recordId);
                    writeFormField(dos, boundary, "recordName", recordName);
                    writeFormField(dos, boundary, "userFileName",
                            userFileName != null ? userFileName : "");
                    writeFormField(dos, boundary, "timestamp",
                            String.valueOf(System.currentTimeMillis()));

                    // 结束请求体
                    dos.writeBytes("--" + boundary + "--\r\n");
                    dos.flush();
                    dos.close();

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
                            String fileUrl = "http://yhocn.cn:9088" + path + finalFileName;
                            callback.onSuccess(fileUrl);
                        } else {
                            callback.onFailure(json.optString("msg", "上传失败"));
                        }
                    } else {
                        callback.onFailure("服务器错误: " + responseCode);
                    }

                    conn.disconnect();

                } catch (Exception e) {
                    callback.onFailure("上传失败: " + e.getMessage());
                }
            }
        }).start();
    }

    // 修改writeFormField方法 - 直接写入原始值
    private void writeFormField(DataOutputStream dos, String boundary,
                                String name, String value) throws Exception {
        dos.writeBytes("--" + boundary + "\r\n");
        dos.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"\r\n\r\n");

        dos.write(value.getBytes("UTF-8"));
        dos.writeBytes("\r\n");

        Log.d("FileUpload", "表单字段 - " + name + ": " + value);
    }


    /**
     * 从服务器删除文件（使用OkHttp）
     */
    public void deleteFileFromServer(String fileName, String path, DeleteCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 记录传入参数
                    Log.d("FileDelete", "开始删除文件");
                    Log.d("FileDelete", "原始文件名: " + fileName);
                    Log.d("FileDelete", "路径: " + path);

                    // 清理文件名
                    String cleanFileName = fileName;
                    if (fileName.contains(".")) {
                        cleanFileName = fileName.substring(0, fileName.lastIndexOf('.'));
                    }
                    Log.d("FileDelete", "清理后的文件名: " + cleanFileName);

                    // 创建连接
                    URL url = new URL("https://yhocn.cn:9097/file/delete");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                    conn.setConnectTimeout(30000);
                    conn.setReadTimeout(30000);

                    // 关键修改：对中文字符进行URL编码
                    String encodedOrderNumber = URLEncoder.encode(cleanFileName, "UTF-8");
                    String encodedPath = URLEncoder.encode(path, "UTF-8");
                    String formData = "order_number=" + encodedOrderNumber + "&path=" + encodedPath;

                    Log.d("FileDelete", "原始参数: order_number=" + cleanFileName + "&path=" + path);
                    Log.d("FileDelete", "编码后参数: " + formData);
                    Log.d("FileDelete", "请求URL: " + url.toString());

                    // 关键修改：使用getBytes("UTF-8")确保正确编码
                    DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                    dos.write(formData.getBytes("UTF-8")); // 不要用writeBytes()
                    dos.flush();
                    dos.close();

                    // 获取响应
                    int responseCode = conn.getResponseCode();
                    Log.d("FileDelete", "响应码: " + responseCode);

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        // 关键修改：设置响应流的字符编码
                        InputStream is = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();

                        // 记录完整响应
                        String responseStr = response.toString();
                        Log.d("FileDelete", "完整响应内容: " + responseStr);

                        // 解析响应
                        try {
                            JSONObject json = new JSONObject(responseStr);
                            int code = json.optInt("code", -1);
                            boolean success = json.optBoolean("success", false);
                            String msg = json.optString("msg", "无返回消息");

                            Log.d("FileDelete", "解析结果 - code: " + code + ", success: " + success + ", msg: " + msg);

                            if (code == 200 || success) {
                                Log.d("FileDelete", "删除成功");
                                callback.onSuccess();
                            } else {
                                Log.w("FileDelete", "删除失败: " + msg);
                                callback.onFailure(msg);
                            }
                        } catch (Exception e) {
                            Log.e("FileDelete", "JSON解析异常: " + e.getMessage());
                            callback.onFailure("响应解析失败");
                        }
                    } else {
                        // 获取错误流信息
                        InputStream errorStream = conn.getErrorStream();
                        if (errorStream != null) {
                            BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream, "UTF-8"));
                            StringBuilder errorResponse = new StringBuilder();
                            String errorLine;
                            while ((errorLine = errorReader.readLine()) != null) {
                                errorResponse.append(errorLine);
                            }
                            errorReader.close();
                            Log.w("FileDelete", "错误响应: " + errorResponse.toString());
                        }

                        String errorMsg = "服务器错误: " + responseCode;
                        Log.e("FileDelete", errorMsg);
                        callback.onFailure(errorMsg);
                    }

                    conn.disconnect();
                    Log.d("FileDelete", "连接已关闭");

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("FileDelete", "删除异常", e);
                    String errorMsg = "删除失败: " + e.getMessage();
                    callback.onFailure(errorMsg);
                }
            }
        }).start();
    }

    /**
     * 检查是否可以更新文件字段（验证记录是否存在）
     */
    public boolean canUpdateFiles(int id) {
        String selectSql = "select wenjian from gongzi_jianliguanli where id = ?";
        renshiBaseDao checkDao = new renshiBaseDao();
        List<YhRenShiJianLiGuanLi> result = checkDao.query(YhRenShiJianLiGuanLi.class, selectSql, id);

        // 记录存在即可更新
        return result != null && result.size() > 0;
    }

    /**
     * 获取当前文件字符串
     */
    public String getCurrentFilesadd(int id) {
        String selectSql = "select wenjian from gongzi_jianliguanli where id = ?";
        renshiBaseDao selectDao = new renshiBaseDao();
        List<YhRenShiJianLiGuanLi> result = selectDao.query(YhRenShiJianLiGuanLi.class, selectSql, id);

        if (result != null && result.size() > 0) {
            return result.get(0).getWenjian();
        }
        return null;
    }

    /**
     * 构建新的文件URL字符串
     */
    public String buildNewFilesStringadd(String currentFiles, String fileUrls) {
        if (currentFiles == null || currentFiles.trim().isEmpty()) {
            return fileUrls;
        }

        // 将新文件URL追加到现有文件URL后面，用逗号分隔
        return currentFiles + "," + fileUrls;
    }

    /**
     * 执行更新文件记录操作
     */
    public boolean updateFileRecordadd(int id, String newFileUrls) {
        String sql = "update gongzi_jianliguanli set wenjian = ? where id = ?";
        renshiBaseDao updateDao = new renshiBaseDao();
        return updateDao.execute(sql, newFileUrls, id);
    }

    /**
     * 更新文件字段 - 新增文件（整合所有步骤）
     */
    public boolean updateFilesWithCheck(int id, String fileUrls) {
        // 1. 检查是否可以更新
        if (!canUpdateFiles(id)) {
            return false; // 记录不存在，无法更新
        }

        // 2. 获取当前文件
        String currentFiles = getCurrentFilesadd(id);

        // 3. 构建新的文件字符串
        String newFileUrls = buildNewFilesStringadd(currentFiles, fileUrls);

        // 4. 更新数据库记录
        return updateFileRecordadd(id, newFileUrls);
    }

    /**
     * 获取当前文件列表
     */
    public String getCurrentFiles(int id) {
        String selectSql = "select wenjian from gongzi_jianliguanli where id = ?";
        renshiBaseDao base = new renshiBaseDao();
        List<YhRenShiJianLiGuanLi> result = base.query(YhRenShiJianLiGuanLi.class, selectSql, id);

        if (result != null && result.size() > 0) {
            return result.get(0).getWenjian();
        }
        return null;
    }

    /**
     * 构建新的文件字符串（移除指定文件URL）
     */
    public String buildNewFilesString(String currentFiles, String fileUrlToDelete) {
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
     * 更新文件记录
     */
    public boolean updateFileRecord(int id, String newFiles) {
        String sql = "update gongzi_jianliguanli set wenjian = ? where id = ?";
        renshiBaseDao base = new renshiBaseDao();
        return base.execute(sql, newFiles, id);
    }

    /**
     * 执行删除文件记录操作（删除指定文件URL，保留其他文件）
     */
    public boolean deleteFile(int id, String fileUrlToDelete) {
        // 1. 获取当前文件
        String currentFiles = getCurrentFiles(id);

        // 2. 构建新的文件字符串
        String newFiles = buildNewFilesString(currentFiles, fileUrlToDelete);

        // 3. 更新数据库记录
        return updateFileRecord(id, newFiles);
    }

    /**
     * 检查是否可以删除文件记录
     */
    public boolean canDeleteFile(int id, String fileUrlToDelete) {
        String currentFiles = getCurrentFiles(id);

        if (currentFiles == null || currentFiles.trim().isEmpty()) {
            return true;
        }

        String[] fileArray = currentFiles.split(",");
        for (String file : fileArray) {
            if (file.trim().equals(fileUrlToDelete.trim())) {
                return true; // 文件存在，可以执行删除
            }
        }
        return false; // 文件不存在，无需删除
    }

    /**
     * 删除数据库中的单个文件记录（整合检查和执行）
     */
    public boolean deleteFileWithCheck(int id, String fileUrlToDelete) {
        if (!canDeleteFile(id, fileUrlToDelete)) {
            return false; // 不能删除或文件不存在
        }
        return deleteFile(id, fileUrlToDelete);
    }

    /**
     * 清空所有文件
     */
    public boolean clearAllFiles(int id) {
        String sql = "update gongzi_jianliguanli set wenjian = null where id = ?";
        base = new renshiBaseDao();
        return base.execute(sql, id);
    }

    /**
     * 回调接口
     */
    public interface UploadCallback {
        void onSuccess(String fileUrl);
        void onFailure(String error);
    }

    public interface DeleteCallback {
        void onSuccess();
        void onFailure(String error);
    }
}