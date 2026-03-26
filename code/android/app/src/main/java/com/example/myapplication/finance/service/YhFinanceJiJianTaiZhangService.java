package com.example.myapplication.finance.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.myapplication.CacheManager;
import com.example.myapplication.MyApplication;
import com.example.myapplication.finance.dao.financeBaseDao;
import com.example.myapplication.finance.entity.YhFinanceJiJianTaiZhang;

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

public class YhFinanceJiJianTaiZhangService {
    private financeBaseDao base;

    // 服务器配置
    private static final String UPLOAD_URL = "https://yhocn.cn:9097/file/upload";
    private static final String DELETE_URL = "https://yhocn.cn:9097/file/delete";
    private static final String FOLDER_SIZE_URL = "https://yhocn.cn:9097/file/getFolderSize";
    private static final String FILE_SERVER_URL = "http://yhocn.cn:9088";

    /**
     * 查询全部数据
     */
    public List<YhFinanceJiJianTaiZhang> getList(String company, String project, String start_date, String stop_date) {
        String sql = "select a.id,a.company,a.insert_date,a.project,a.kehu,a.receivable,a.receipts,a.cope,a.payment,a.accounting,a.nashuijine,a.yijiaoshuijine,a.zhaiyao,a.wenjian from (select row_number() over(order by id) as rownum,* from SimpleData where company = ? and project like '%'+ ? +'%') as a where a.insert_date >= ? and insert_date <= ?";
        base = new financeBaseDao();
        List<YhFinanceJiJianTaiZhang> list = base.query(YhFinanceJiJianTaiZhang.class, sql, company, project, start_date, stop_date);
        return list;
    }

    public List<YhFinanceJiJianTaiZhang> getList1(String company) {
        String sql = "select a.id,a.company,a.insert_date,a.project,a.kehu,a.receivable,a.receipts,a.cope,a.payment,a.accounting,a.nashuijine,a.yijiaoshuijine,a.zhaiyao,a.wenjian from (select row_number() over(order by id) as rownum,* from SimpleData where company = ?) as a ";
        base = new financeBaseDao();
        List<YhFinanceJiJianTaiZhang> list = base.query(YhFinanceJiJianTaiZhang.class, sql, company);
        return list;
    }

    /**
     * 新增
     */
    public boolean insert(YhFinanceJiJianTaiZhang entity) {
        String sql = "insert into SimpleData(company,project,receivable,receipts,cope,payment,accounting,insert_date,kehu,zhaiyao,nashuijine,yijiaoshuijine,wenjian) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        base = new financeBaseDao();
        long result = base.executeOfId(sql,
                entity.getCompany(),
                entity.getProject(),
                entity.getReceivable(),
                entity.getReceipts(),
                entity.getCope(),
                entity.getPayment(),
                entity.getAccounting(),
                entity.getInsert_date(),
                entity.getKehu(),
                entity.getZhaiyao(),
                entity.getNashuijine(),
                entity.getYijiaoshuijine(),
                entity.getWenjian());
        return result > 0;
    }

    /**
     * 修改
     */
    public boolean update(YhFinanceJiJianTaiZhang entity) {
        String sql = "update SimpleData set company=?,project=?,receivable=?,receipts=?,cope=?,payment=?,accounting=?,insert_date=?,kehu=?,zhaiyao=?,nashuijine=?,yijiaoshuijine=?,wenjian=? where id=? ";
        base = new financeBaseDao();
        boolean result = base.execute(sql,
                entity.getCompany(),
                entity.getProject(),
                entity.getReceivable(),
                entity.getReceipts(),
                entity.getCope(),
                entity.getPayment(),
                entity.getAccounting(),
                entity.getInsert_date(),
                entity.getKehu(),
                entity.getZhaiyao(),
                entity.getNashuijine(),
                entity.getYijiaoshuijine(),
                entity.getWenjian(),
                entity.getId());
        return result;
    }

    /**
     * 删除
     */
    public boolean delete(int id) {
        String sql = "delete from SimpleData where id = ?";
        base = new financeBaseDao();
        return base.execute(sql, id);
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
        try {
            Context context = MyApplication.getContext();
            if (context != null) {
                SharedPreferences prefs = context.getSharedPreferences("my_cache", Context.MODE_PRIVATE);
                return prefs.getFloat("storageSpace", 10 * 1024 * 1024);
            }
        } catch (Exception e) {
        }
        return 10 * 1024 * 1024;
    }

    /**
     * 获取当前公司的文件夹大小（财务文件路径：/caiwu/公司名/）
     */
    private double getFolderSize(String companyName) {
        double folderSizeKB = 0;
        HttpURLConnection conn = null;
        try {
            String path = "/caiwu/" + companyName + "/";
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
                } else if (code == 500 && json.optString("msg").contains("不存在")) {
                    folderSizeKB = 0;
                    Log.d("FolderSize", "文件夹不存在，大小设为0");
                }
            }
        } catch (Exception e) {
            Log.e("FolderSize", "获取文件夹大小异常: " + e.getMessage());
        } finally {
            if (conn != null) conn.disconnect();
        }
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
     * 空间检查回调接口
     */
    public interface SpaceCheckCallback {
        void onResult(SpaceInfo spaceInfo);
        void onError(String error);
    }

    /**
     * 检查空间使用情况
     */
    public void checkTotalSpace(String companyName, double fileSizeKB, SpaceCheckCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    double dbSizeKB = getDbSizeFromCache();
                    double limitKB = getStorageSpaceFromCache();
                    double folderSizeKB = getFolderSize(companyName);

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

    // ==================== 文件上传相关方法 ====================

    /**
     * 检查是否可以更新文件字段
     */
    public boolean canUpdateFiles(int id) {
        String selectSql = "select wenjian from SimpleData where id = ?";
        financeBaseDao checkDao = new financeBaseDao();
        List<YhFinanceJiJianTaiZhang> result = checkDao.query(YhFinanceJiJianTaiZhang.class, selectSql, id);
        return result != null && result.size() > 0;
    }

    /**
     * 获取当前文件字符串
     */
    public String getCurrentFiles(int id) {
        String selectSql = "select wenjian from SimpleData where id = ?";
        financeBaseDao selectDao = new financeBaseDao();
        List<YhFinanceJiJianTaiZhang> result = selectDao.query(YhFinanceJiJianTaiZhang.class, selectSql, id);
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
        String sql = "update SimpleData set wenjian = ? where id = ?";
        financeBaseDao updateDao = new financeBaseDao();
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
     * 上传文件到服务器（带空间检查和文件大小限制）
     */
    public void uploadFileWithCheck(File file, String fileName, String companyName,
                                    String recordId, String recordName, String userFileName,
                                    UploadCallback callback) {
        final long fileSize = file.length();
        final double fileSizeKB = fileSize / 1024.0;

        // 检查文件大小是否超过500MB
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
                    // 构建最终文件名
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

                    // 构建动态路径：/caiwu/公司名/
                    String dynamicPath = "/caiwu/" + companyName + "/";

                    URL url = new URL(UPLOAD_URL);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setUseCaches(false);
                    conn.setRequestMethod("POST");
                    conn.setConnectTimeout(30000);
                    conn.setReadTimeout(600000);

                    String boundary = "----WebKitFormBoundary" + System.currentTimeMillis();
                    conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

                    dos = new DataOutputStream(conn.getOutputStream());

                    // 写入文件
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

                    // 写入表单参数
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

                    dos.writeBytes("--" + boundary + "--\r\n");
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
     * 从服务器删除文件（使用动态路径：/caiwu/公司名/）
     */
    public void deleteFileFromServer(String fileName, String companyName, DeleteCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                DataOutputStream dos = null;
                try {
                    String cleanFileName = fileName;
                    if (fileName.contains(".")) {
                        cleanFileName = fileName.substring(0, fileName.lastIndexOf('.'));
                    }

                    String dynamicPath = "/caiwu/" + companyName + "/";

                    URL url = new URL(DELETE_URL);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                    conn.setConnectTimeout(30000);
                    conn.setReadTimeout(30000);

                    String encodedOrderNumber = URLEncoder.encode(cleanFileName, "UTF-8");
                    String encodedPath = URLEncoder.encode(dynamicPath, "UTF-8");
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

    // 回调接口（带警告信息）
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