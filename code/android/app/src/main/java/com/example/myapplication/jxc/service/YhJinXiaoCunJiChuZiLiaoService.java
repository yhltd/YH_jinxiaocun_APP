package com.example.myapplication.jxc.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.myapplication.CacheManager;
import com.example.myapplication.MyApplication;
import com.example.myapplication.jxc.dao.JxcBaseDao;
import com.example.myapplication.jxc.dao.JxcServerDao;
import com.example.myapplication.jxc.entity.YhJinXiaoCunJiChuZiLiao;

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

public class YhJinXiaoCunJiChuZiLiaoService {
    private JxcBaseDao base;
    private JxcServerDao base2;

    // 服务器配置
    private static final String UPLOAD_URL = "https://yhocn.cn:9097/file/upload";
    private static final String DELETE_URL = "https://yhocn.cn:9097/file/delete";
    private static final String FOLDER_SIZE_URL = "https://yhocn.cn:9097/file/getFolderSize";
    private static final String FILE_SERVER_URL = "http://yhocn.cn:9088";

    /**
     * 查询全部数据
     */
    public List<YhJinXiaoCunJiChuZiLiao> getList(String company, String cpname) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            Log.e("MyService", "当前shujuku状态: " + shujukuValue);

            if (shujukuValue == 1) {
                String sql = "select * from yh_jinxiaocun_jichuziliao_mssql where gs_name = ? and name like '%' + ? + '%' order by id";
                base2 = new JxcServerDao();
                List<YhJinXiaoCunJiChuZiLiao> list = base2.query(YhJinXiaoCunJiChuZiLiao.class, sql, company, cpname);
                return list != null ? list : new ArrayList<>();

            } else {
                String sql = "select * from yh_jinxiaocun_jichuziliao where gs_name = ? and name like concat('%', ?, '%') order by id";
                base = new JxcBaseDao();
                List<YhJinXiaoCunJiChuZiLiao> list = base.query(YhJinXiaoCunJiChuZiLiao.class, sql, company, cpname);
                return list != null ? list : new ArrayList<>();
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "获取基础资料列表过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 商品代码下拉
     */
    public List<String> getCpid(String company) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();

            if (shujukuValue == 1) {
                String sql = "select sp_dm from yh_jinxiaocun_jichuziliao_mssql where gs_name = ? group by sp_dm";
                base2 = new JxcServerDao();
                List<YhJinXiaoCunJiChuZiLiao> list = base2.query(YhJinXiaoCunJiChuZiLiao.class, sql, company);
                List<String> cpidList = new ArrayList<>();
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        cpidList.add(list.get(i).getSpDm());
                    }
                }
                return cpidList.size() > 0 ? cpidList : null;

            } else {
                String sql = "select sp_dm from yh_jinxiaocun_jichuziliao where gs_name = ? group by sp_dm";
                base = new JxcBaseDao();
                List<YhJinXiaoCunJiChuZiLiao> list = base.query(YhJinXiaoCunJiChuZiLiao.class, sql, company);
                List<String> cpidList = new ArrayList<>();
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        cpidList.add(list.get(i).getSpDm());
                    }
                }
                return cpidList.size() > 0 ? cpidList : null;
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "获取产品ID列表过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据商品代码查询
     */
    public List<YhJinXiaoCunJiChuZiLiao> getListByCpid(String company, String cpid) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();

            if (shujukuValue == 1) {
                String sql = "select * from yh_jinxiaocun_jichuziliao_mssql where gs_name = ? and sp_dm=?";
                base2 = new JxcServerDao();
                List<YhJinXiaoCunJiChuZiLiao> list = base2.query(YhJinXiaoCunJiChuZiLiao.class, sql, company, cpid);
                return list != null ? list : new ArrayList<>();

            } else {
                String sql = "select * from yh_jinxiaocun_jichuziliao where gs_name = ? and sp_dm=?";
                base = new JxcBaseDao();
                List<YhJinXiaoCunJiChuZiLiao> list = base.query(YhJinXiaoCunJiChuZiLiao.class, sql, company, cpid);
                return list != null ? list : new ArrayList<>();
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "根据产品ID获取基础资料列表过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 新增
     */
    public boolean insert(YhJinXiaoCunJiChuZiLiao yhJinXiaoCunJiChuZiLiao) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();

            if (shujukuValue == 1) {
                String sql = "insert into yh_jinxiaocun_jichuziliao_mssql (sp_dm,name,lei_bie,dan_wei,shou_huo,gong_huo,gs_name,mark1) values(?,?,?,?,?,?,?,?)";
                base2 = new JxcServerDao();
                long result = base2.executeOfId(sql, yhJinXiaoCunJiChuZiLiao.getSpDm(), yhJinXiaoCunJiChuZiLiao.getName(), yhJinXiaoCunJiChuZiLiao.getLeiBie(), yhJinXiaoCunJiChuZiLiao.getDanWei(), yhJinXiaoCunJiChuZiLiao.getShouHuo(), yhJinXiaoCunJiChuZiLiao.getGongHuo(), yhJinXiaoCunJiChuZiLiao.getGsName(), yhJinXiaoCunJiChuZiLiao.getMark1());
                return result > 0;

            } else {
                String sql = "insert into yh_jinxiaocun_jichuziliao (sp_dm,name,lei_bie,dan_wei,shou_huo,gong_huo,gs_name,mark1) values(?,?,?,?,?,?,?,?)";
                base = new JxcBaseDao();
                long result = base.executeOfId(sql, yhJinXiaoCunJiChuZiLiao.getSpDm(), yhJinXiaoCunJiChuZiLiao.getName(), yhJinXiaoCunJiChuZiLiao.getLeiBie(), yhJinXiaoCunJiChuZiLiao.getDanWei(), yhJinXiaoCunJiChuZiLiao.getShouHuo(), yhJinXiaoCunJiChuZiLiao.getGongHuo(), yhJinXiaoCunJiChuZiLiao.getGsName(), yhJinXiaoCunJiChuZiLiao.getMark1());
                return result > 0;
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "插入基础资料数据过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 修改
     */
    public boolean update(YhJinXiaoCunJiChuZiLiao yhJinXiaoCunJiChuZiLiao) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();

            if (shujukuValue == 1) {
                String sql = "update yh_jinxiaocun_jichuziliao_mssql set sp_dm=?,name=?,lei_bie=?,dan_wei=?,shou_huo=?,gong_huo=?,mark1=? where id=?";
                base2 = new JxcServerDao();
                boolean result = base2.execute(sql, yhJinXiaoCunJiChuZiLiao.getSpDm(), yhJinXiaoCunJiChuZiLiao.getName(), yhJinXiaoCunJiChuZiLiao.getLeiBie(), yhJinXiaoCunJiChuZiLiao.getDanWei(), yhJinXiaoCunJiChuZiLiao.getShouHuo(), yhJinXiaoCunJiChuZiLiao.getGongHuo(), yhJinXiaoCunJiChuZiLiao.getMark1(), yhJinXiaoCunJiChuZiLiao.getId());
                return result;

            } else {
                String sql = "update yh_jinxiaocun_jichuziliao set sp_dm=?,name=?,lei_bie=?,dan_wei=?,shou_huo=?,gong_huo=?,mark1=? where id=?";
                base = new JxcBaseDao();
                boolean result = base.execute(sql, yhJinXiaoCunJiChuZiLiao.getSpDm(), yhJinXiaoCunJiChuZiLiao.getName(), yhJinXiaoCunJiChuZiLiao.getLeiBie(), yhJinXiaoCunJiChuZiLiao.getDanWei(), yhJinXiaoCunJiChuZiLiao.getShouHuo(), yhJinXiaoCunJiChuZiLiao.getGongHuo(), yhJinXiaoCunJiChuZiLiao.getMark1(), yhJinXiaoCunJiChuZiLiao.getId());
                return result;
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "更新基础资料数据过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除
     */
    public boolean delete(int id) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();

            if (shujukuValue == 1) {
                String sql = "delete from yh_jinxiaocun_jichuziliao_mssql where id = ?";
                base2 = new JxcServerDao();
                return base2.execute(sql, id);

            } else {
                String sql = "delete from yh_jinxiaocun_jichuziliao where id = ?";
                base = new JxcBaseDao();
                return base.execute(sql, id);
            }
        } catch (Exception e) {
            Log.e("SQLDebug", "删除基础资料数据过程发生异常: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
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
     * 获取当前公司的文件夹大小（进销存图片路径：/jinxiaocun/公司名/）
     */
    private double getFolderSize(String companyName) {
        double folderSizeKB = 0;
        HttpURLConnection conn = null;
        try {
            String path = "/jinxiaocun/" + companyName + "/";
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
     * 获取当前文件URL（mark1字段）
     */
    public String getCurrentFile(int id) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            String selectSql = "select mark1 from " + (shujukuValue == 1 ? "yh_jinxiaocun_jichuziliao_mssql" : "yh_jinxiaocun_jichuziliao") + " where id = ?";

            if (shujukuValue == 1) {
                base2 = new JxcServerDao();
                List<YhJinXiaoCunJiChuZiLiao> result = base2.query(YhJinXiaoCunJiChuZiLiao.class, selectSql, id);
                if (result != null && result.size() > 0) {
                    return result.get(0).getMark1();
                }
            } else {
                base = new JxcBaseDao();
                List<YhJinXiaoCunJiChuZiLiao> result = base.query(YhJinXiaoCunJiChuZiLiao.class, selectSql, id);
                if (result != null && result.size() > 0) {
                    return result.get(0).getMark1();
                }
            }
            return null;
        } catch (Exception e) {
            Log.e("FileUpload", "获取当前文件失败: " + e.getMessage());
            return null;
        }
    }

    /**
     * 更新文件记录（mark1字段）
     */
    public boolean updateFileRecord(int id, String fileUrl) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            String sql = "update " + (shujukuValue == 1 ? "yh_jinxiaocun_jichuziliao_mssql" : "yh_jinxiaocun_jichuziliao") + " set mark1 = ? where id = ?";

            if (shujukuValue == 1) {
                base2 = new JxcServerDao();
                return base2.execute(sql, fileUrl, id);
            } else {
                base = new JxcBaseDao();
                return base.execute(sql, fileUrl, id);
            }
        } catch (Exception e) {
            Log.e("FileUpload", "更新文件记录失败: " + e.getMessage());
            return false;
        }
    }

    /**
     * 清空文件记录（mark1字段）
     */
    public boolean clearFileRecord(int id) {
        try {
            int shujukuValue = CacheManager.getInstance().getShujukuValue();
            String sql = "update " + (shujukuValue == 1 ? "yh_jinxiaocun_jichuziliao_mssql" : "yh_jinxiaocun_jichuziliao") + " set mark1 = null where id = ?";

            if (shujukuValue == 1) {
                base2 = new JxcServerDao();
                return base2.execute(sql, id);
            } else {
                base = new JxcBaseDao();
                return base.execute(sql, id);
            }
        } catch (Exception e) {
            Log.e("FileUpload", "清空文件记录失败: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
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

                    // 构建动态路径：/jinxiaocun/公司名/
                    String dynamicPath = "/jinxiaocun/" + companyName + "/";

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
     * 从服务器删除文件（使用动态路径：/jinxiaocun/公司名/）
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

                    String dynamicPath = "/jinxiaocun/" + companyName + "/";

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