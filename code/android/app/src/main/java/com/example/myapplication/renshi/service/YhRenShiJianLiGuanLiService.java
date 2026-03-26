package com.example.myapplication.renshi.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.myapplication.CacheManager;
import com.example.myapplication.MyApplication;
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

public class YhRenShiJianLiGuanLiService {
    private renshiBaseDao base;

    // 服务器配置
    private static final String UPLOAD_URL = "https://yhocn.cn:9097/file/upload";
    private static final String DELETE_URL = "https://yhocn.cn:9097/file/delete";
    private static final String FOLDER_SIZE_URL = "https://yhocn.cn:9097/file/getFolderSize";
    private static final String FILE_SERVER_URL = "http://yhocn.cn:9088";

    /**
     * 查询简历管理列表
     */
    public List<YhRenShiJianLiGuanLi> getList(String company, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        String sql = "select * from gongzi_jianliguanli where gongsi = ? " +
                "and (zhuangtai is null or zhuangtai = '' or zhuangtai = '待处理' or zhuangtai = '驳回') ";

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
            sql = "select * from gongzi_jianliguanli where gongsi = ? and (zhuangtai is null or zhuangtai = '' or zhuangtai = '待处理' or zhuangtai = '驳回') order by id desc";
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
                "and (zhuangtai is null or zhuangtai = '' or zhuangtai = '待处理' or zhuangtai = '驳回')";
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

    // ==================== 空间检查相关方法 ====================

    /**
     * 从 Application 获取数据库大小（已在登录时保存）
     */
    private double getDbSizeFromCache() {
        try {
            Context context = MyApplication.getContext();
            if (context != null) {
                SharedPreferences prefs = context.getSharedPreferences("my_cache", Context.MODE_PRIVATE);
                return prefs.getFloat("dbSizeKB", 0);
            }
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * 从 Application 获取空间限制（mark4 解析后的值，已在登录时保存）
     */
    private double getStorageSpaceFromCache() {
        try {
            Object mark4Obj = CacheManager.getInstance().get("mark4");
            if (mark4Obj != null) {
                String mark4 = mark4Obj.toString();
                if (!mark4.isEmpty()) {
                    // 解析 mark4 获取空间限制（单位：KB）
                    if (mark4.contains(":")) {
                        String[] parts = mark4.split(":");
                        if (parts.length > 1) {
                            String value = parts[1].replace("(", "").replace(")", "").trim();
                            return Double.parseDouble(value);
                        }
                    } else {
                        return Double.parseDouble(mark4);
                    }
                }
            }
        } catch (Exception e) {
        }
        // 降级：从 SharedPreferences 获取
        try {
            Context context = MyApplication.getContext();
            if (context != null) {
                SharedPreferences prefs = context.getSharedPreferences("my_cache", Context.MODE_PRIVATE);
                return prefs.getFloat("storageSpace", 10 * 1024 * 1024);
            }
        } catch (Exception e) {
        }
        return 10 * 1024 * 1024; // 默认10GB
    }

    /**
     * 获取当前公司的文件夹大小（简历文件路径：/renshi/公司名/）
     */
    private double getFolderSize(String companyName) {
        double folderSizeKB = 0;
        HttpURLConnection conn = null;
        try {
            // 构建简历文件夹路径：/renshi/公司名/
            String path = "/renshi/" + companyName + "/";
            URL url = new URL(FOLDER_SIZE_URL + "?path=" + URLEncoder.encode(path, "UTF-8"));

            Log.d("FolderSize", "请求URL: " + url.toString());

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(30000);

            int responseCode = conn.getResponseCode();
            Log.d("FolderSize", "响应码: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                String responseStr = response.toString();
                Log.d("FolderSize", "原始响应: " + responseStr);

                JSONObject json = new JSONObject(responseStr);
                int code = json.optInt("code", -1);

                if (code == 200) {
                    JSONObject data = json.getJSONObject("data");
                    long sizeBytes = data.optLong("sizeBytes", 0);
                    folderSizeKB = sizeBytes / 1024.0;
                    Log.d("FolderSize", "文件夹大小 - bytes: " + sizeBytes + ", KB: " + folderSizeKB);
                    Log.d("FolderSize", "文件/文件夹数量: " + data.optInt("fileCount", 0));
                } else if (code == 500 && json.optString("msg").contains("不存在")) {
                    folderSizeKB = 0;
                    Log.d("FolderSize", "文件夹不存在，大小设为0");
                } else {
                    Log.d("FolderSize", "获取文件夹大小失败，msg: " + json.optString("msg"));
                }
            } else {
                Log.d("FolderSize", "HTTP请求失败，响应码: " + responseCode);
            }
        } catch (Exception e) {
            Log.e("FolderSize", "获取文件夹大小异常: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (conn != null) conn.disconnect();
        }

        Log.d("FolderSize", "最终返回文件夹大小: " + folderSizeKB + " KB");
        return folderSizeKB;
    }

    /**
     * 空间信息类
     */
    public static class SpaceInfo {
        public boolean canUpload;
        public boolean showWarning;
        public String message;
        public double usagePercent;
        public double estimatedUsagePercent;
        public double totalUsedKB;
        public double limitKB;
    }

    /**
     * 检查空间使用情况（使用真实保存的数据）
     * @param companyName 公司名称
     * @param fileSizeKB 要上传的文件大小(KB)
     */
    public void checkTotalSpace(String companyName, double fileSizeKB, SpaceCheckCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 从缓存获取数据库大小（登录时已保存）
                    double dbSizeKB = getDbSizeFromCache();

                    // 从缓存获取空间限制（mark4解析，登录时已保存）
                    double limitKB = getStorageSpaceFromCache();

                    // 获取文件夹大小（路径：/renshi/公司名/）
                    double folderSizeKB = getFolderSize(companyName);

                    // 总使用空间
                    double totalUsedKB = dbSizeKB + folderSizeKB;
                    double usagePercent = (totalUsedKB / limitKB) * 100;
                    double estimatedUsagePercent = ((totalUsedKB + fileSizeKB) / limitKB) * 100;

                    boolean canUpload = true;
                    boolean showWarning = false;
                    String message = "";

                    if (totalUsedKB >= limitKB * 1.1) {
                        canUpload = false;
                        message = "空间使用已超110%（" + String.format("%.2f", usagePercent) + "%），无法上传！";
                    } else if (totalUsedKB >= limitKB * 0.9) {
                        showWarning = true;
                        message = "空间使用已超90%（" + String.format("%.2f", usagePercent) + "%），请注意清理！";
                    } else if (estimatedUsagePercent > 110) {
                        canUpload = false;
                        message = "上传后空间使用率将达到 " + String.format("%.2f", estimatedUsagePercent) + "%，超过110%限制，无法上传！";
                    } else if (estimatedUsagePercent > 90) {
                        showWarning = true;
                        message = "上传后空间使用率将达到 " + String.format("%.2f", estimatedUsagePercent) + "%，请注意清理！";
                    }

                    SpaceInfo spaceInfo = new SpaceInfo();
                    spaceInfo.canUpload = canUpload;
                    spaceInfo.showWarning = showWarning;
                    spaceInfo.message = message;
                    spaceInfo.usagePercent = usagePercent;
                    spaceInfo.estimatedUsagePercent = estimatedUsagePercent;
                    spaceInfo.totalUsedKB = totalUsedKB;
                    spaceInfo.limitKB = limitKB;

                    callback.onResult(spaceInfo);

                } catch (Exception e) {
                    callback.onError("空间检查失败: " + e.getMessage());
                }
            }
        }).start();
    }

    /**
     * 空间检查回调接口
     */
    public interface SpaceCheckCallback {
        void onResult(SpaceInfo spaceInfo);
        void onError(String error);
    }

    // ==================== 文件上传相关方法 ====================

    /**
     * 上传文件到服务器（带空间检查和文件大小限制）
     */
    public void uploadFileWithCheck(File file, String fileName, String companyName,
                                    String recordId, String recordName, String userFileName,
                                    UploadCallback callback) {
        final long fileSize = file.length();
        final double fileSizeKB = fileSize / 1024.0;

        // 先检查文件大小是否超过500MB
        if (fileSize > 500 * 1024 * 1024) {
            callback.onFailure("文件超过500MB限制，无法上传！");
            return;
        }

        // 检查空间
        checkTotalSpace(companyName, fileSizeKB, new SpaceCheckCallback() {
            @Override
            public void onResult(SpaceInfo spaceInfo) {
                if (!spaceInfo.canUpload) {
                    callback.onFailure(spaceInfo.message);
                    return;
                }

                if (spaceInfo.showWarning && spaceInfo.message != null && !spaceInfo.message.isEmpty()) {
                    callback.onWarning(spaceInfo.message, spaceInfo.usagePercent, spaceInfo.estimatedUsagePercent);
                }

                // 空间充足，执行上传
                doUploadFile(file, fileName, companyName, recordId, recordName, userFileName, callback);
            }

            @Override
            public void onError(String error) {
                callback.onFailure(error);
            }
        });
    }

    /**
     * 实际执行上传
     */
    private void doUploadFile(File file, String fileName, String companyName,
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

                    Log.d("FileUpload", "最终文件名: " + finalFileName);

                    // 2. 构建动态路径：/renshi/公司名/
                    String dynamicPath = "/renshi/" + companyName + "/";

                    // 3. 创建连接
                    URL url = new URL(UPLOAD_URL);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setUseCaches(false);
                    conn.setRequestMethod("POST");
                    conn.setConnectTimeout(30000);
                    conn.setReadTimeout(600000); // 10分钟超时

                    // 4. 设置边界
                    String boundary = "----WebKitFormBoundary" + System.currentTimeMillis();
                    conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

                    // 5. 构建请求体
                    dos = new DataOutputStream(conn.getOutputStream());

                    // 6. 写入文件
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

                    // 7. 写入表单参数
                    writeFormField(dos, boundary, "name", finalFileName);
                    writeFormField(dos, boundary, "path", dynamicPath);
                    writeFormField(dos, boundary, "kongjian", "3");
                    writeFormField(dos, boundary, "fileType",
                            fileName.contains(".") ? fileName.substring(fileName.lastIndexOf('.') + 1) : "");
                    writeFormField(dos, boundary, "recordId", recordId);
                    writeFormField(dos, boundary, "recordName", recordName);
                    writeFormField(dos, boundary, "userFileName", userFileName != null ? userFileName : "");
                    writeFormField(dos, boundary, "timestamp", String.valueOf(System.currentTimeMillis()));
                    writeFormField(dos, boundary, "fileSize", String.valueOf(file.length()));

                    // 结束请求体
                    dos.writeBytes("--" + boundary + "--\r\n");
                    dos.flush();

                    // 8. 处理响应
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
                        if (json.optInt("code", -1) == 200 || json.optBoolean("success", false)) {
                            String fileUrl = FILE_SERVER_URL + dynamicPath + finalFileName;
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
     * 从服务器删除文件（使用动态路径：/renshi/公司名/）
     */
    public void deleteFileFromServer(String fileName, String companyName, DeleteCallback callback) {
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

                    // 构建动态路径：/renshi/公司名/
                    String dynamicPath = "/renshi/" + companyName + "/";

                    // 创建连接
                    URL url = new URL(DELETE_URL);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                    conn.setConnectTimeout(30000);
                    conn.setReadTimeout(30000);

                    // 对中文字符进行URL编码
                    String encodedOrderNumber = URLEncoder.encode(cleanFileName, "UTF-8");
                    String encodedPath = URLEncoder.encode(dynamicPath, "UTF-8");
                    String formData = "order_number=" + encodedOrderNumber + "&path=" + encodedPath;

                    Log.d("FileDelete", "删除参数 - order_number: " + cleanFileName + ", path: " + dynamicPath);

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

    /**
     * 检查是否可以更新文件字段
     */
    public boolean canUpdateFiles(int id) {
        String selectSql = "select wenjian from gongzi_jianliguanli where id = ?";
        renshiBaseDao checkDao = new renshiBaseDao();
        List<YhRenShiJianLiGuanLi> result = checkDao.query(YhRenShiJianLiGuanLi.class, selectSql, id);

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
        if (!canUpdateFiles(id)) {
            return false;
        }

        String currentFiles = getCurrentFilesadd(id);
        String newFileUrls = buildNewFilesStringadd(currentFiles, fileUrls);
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
        String currentFiles = getCurrentFiles(id);
        String newFiles = buildNewFilesString(currentFiles, fileUrlToDelete);
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
                return true;
            }
        }
        return false;
    }

    /**
     * 删除数据库中的单个文件记录（整合检查和执行）
     */
    public boolean deleteFileWithCheck(int id, String fileUrlToDelete) {
        if (!canDeleteFile(id, fileUrlToDelete)) {
            return false;
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
     * 回调接口（带警告信息）
     */
    public interface UploadCallback {
        void onSuccess(String fileUrl);
        void onFailure(String error);
        void onWarning(String message, double usagePercent, double estimatedUsagePercent);
    }

    public interface DeleteCallback {
        void onSuccess();
        void onFailure(String error);
    }
}