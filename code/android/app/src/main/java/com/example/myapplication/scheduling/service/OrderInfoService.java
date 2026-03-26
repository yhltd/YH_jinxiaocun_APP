package com.example.myapplication.scheduling.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.myapplication.CacheManager;
import com.example.myapplication.MyApplication;
import com.example.myapplication.scheduling.dao.SchedulingDao;
import com.example.myapplication.scheduling.entity.LineChart;
import com.example.myapplication.scheduling.entity.OrderBom;
import com.example.myapplication.scheduling.entity.OrderInfo;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

public class OrderInfoService {
    private SchedulingDao base;

    // 服务器配置
    private static final String UPLOAD_URL = "https://yhocn.cn:9097/file/upload";
    private static final String DELETE_URL = "https://yhocn.cn:9097/file/delete";
    private static final String FOLDER_SIZE_URL = "https://yhocn.cn:9097/file/getFolderSize";
    private static final String FILE_SERVER_URL = "http://yhocn.cn:9088";

    /**
     * 刷新
     */
    public List<OrderInfo> getList(String company, String product_name, String order_id) {
        base = new SchedulingDao();
        String sql = "select * from order_info where company=? and product_name like '%' + ? + '%' and order_id like '%' + ? + '%'  ";
        List<OrderInfo> list = base.query(OrderInfo.class, sql, company, product_name, order_id);
        return list;
    }

    /**
     * 刷新
     */
    public List<OrderInfo> getLast() {
        base = new SchedulingDao();
        String sql = "select top 1 * from order_info order by id desc ";
        List<OrderInfo> list = base.query(OrderInfo.class, sql);
        return list;
    }

    /**
     * 新增
     */
    public boolean insert(OrderInfo orderInfo) {
        String sql = "insert into order_info(code,product_name,norms,order_id,set_date,set_num,company,is_complete,wenjian) values(?,?,?,?,?,?,?,?,?)";
        base = new SchedulingDao();
        long result = base.executeOfId(sql,
                orderInfo.getCode(),
                orderInfo.getProduct_name(),
                orderInfo.getNorms(),
                orderInfo.getOrder_id(),
                orderInfo.getSet_date(),
                orderInfo.getSet_num(),
                orderInfo.getCompany(),
                orderInfo.getIs_complete(),
                orderInfo.getWenjian());  // 新增文件字段
        return result > 0;
    }

    /**
     * 修改
     */
    public boolean update(OrderInfo orderInfo) {
        String sql = "update order_info set code=?,product_name=?,norms=?,order_id=?,set_date=?,set_num=?,company=?,is_complete=?,wenjian=? where id=? ";
        base = new SchedulingDao();
        boolean result = base.execute(sql,
                orderInfo.getCode(),
                orderInfo.getProduct_name(),
                orderInfo.getNorms(),
                orderInfo.getOrder_id(),
                orderInfo.getSet_date(),
                orderInfo.getSet_num(),
                orderInfo.getCompany(),
                orderInfo.getIs_complete(),
                orderInfo.getWenjian(),  // 新增文件字段
                orderInfo.getId());
        return result;
    }
    /**
     * 删除
     */
    public boolean delete(int id) {
        String sql = "delete from order_info where id = ?";
        base = new SchedulingDao();
        return base.execute(sql, id);
    }

    /**
     * 新增
     */
    public boolean insertOrderBom(OrderBom orderBom) {
        String sql = "insert into order_bom(order_id,bom_id,use_num) values(?,?,?)";
        base = new SchedulingDao();
        long result = base.executeOfId(sql, orderBom.getOrder_id(), orderBom.getBom_id(), orderBom.getUse_num());
        return result > 0;
    }

    /**
     * 删除
     */
    public boolean deleteOrderBom(int order_id) {
        String sql = "delete from order_bom where order_id = ?";
        base = new SchedulingDao();
        return base.execute(sql, order_id);
    }

//    public List<OrderInfo> getOrderId(){
//        String sql="select oi.id,oi.is_complete,oi.code,oi.product_name,oi.norms,oi.set_date,oi.company,oi.order_id,oi.set_num-sum(isnull(wd.work_num, 0)) as set_num from order_info as oi left join work_detail as wd on oi.id = wd.order_id group by oi.id,oi.code,oi.product_name,oi.norms,oi.set_date,oi.company,oi.order_id,oi.set_num,oi.is_complete having oi.set_num-sum(isnull(wd.work_num, 0)) > 0";
//        base = new SchedulingDao();
//        List<OrderInfo> list = base.query(OrderInfo.class, sql);
//        return list;
//    }
    public List<OrderInfo> getOrderId(String company){  // 添加 company 参数
        String sql = "select oi.id,oi.is_complete,oi.code,oi.product_name,oi.norms,oi.set_date,oi.company,oi.order_id,oi.set_num-sum(isnull(wd.work_num, 0)) as set_num " +
                "from order_info as oi " +
                "left join work_detail as wd on oi.id = wd.order_id " +
                "where oi.company = ? " +  // 只添加这一行公司条件
                "group by oi.id,oi.code,oi.product_name,oi.norms,oi.set_date,oi.company,oi.order_id,oi.set_num,oi.is_complete " +
                "having oi.set_num-sum(isnull(wd.work_num, 0)) > 0";

        base = new SchedulingDao();
        // 将 company 作为参数传入
        List<OrderInfo> list = base.query(OrderInfo.class, sql, company);
        return list;
    }

    public List<LineChart> getLineChart(String date, String company){
        String sql="select sum(case when set_date like ? + '-01%' then set_num else 0 end) as month1,sum(case when set_date like ? + '-02%' then set_num else 0 end) as month2,sum(case when set_date like ? + '-03%' then set_num else 0 end) as month3,sum(case when set_date like ? + '-04%' then set_num else 0 end) as month4,sum(case when set_date like ? + '-05%' then set_num else 0 end) as month5,sum(case when set_date like ? + '-06%' then set_num else 0 end) as month6,sum(case when set_date like ? + '-07%' then set_num else 0 end) as month7,sum(case when set_date like ? + '-08%' then set_num else 0 end) as month8,sum(case when set_date like ? + '-09%' then set_num else 0 end) as month9,sum(case when set_date like ? + '-10%' then set_num else 0 end) as month10,sum(case when set_date like ? + '-11%' then set_num else 0 end) as month11,sum(case when set_date like ? + '-12%' then set_num else 0 end) as month12 from order_info where company = ?";
        base = new SchedulingDao();
        List<LineChart> list = base.query(LineChart.class, sql,date,date,date,date,date,date,date,date,date,date,date,date,company);
        return list;
    }

    // ==================== 文件上传相关方法 ====================
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
     * 获取当前公司的文件夹大小
     */
    private double getFolderSize(String companyName) {
        double folderSizeKB = 0;
        HttpURLConnection conn = null;
        try {
            String path = "/paichan/" + companyName + "/";
            URL url = new URL(FOLDER_SIZE_URL + "?path=" + URLEncoder.encode(path, "UTF-8"));

            // 🆕 添加日志：打印请求URL
            android.util.Log.d("FolderSize", "请求URL: " + url.toString());

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(30000);

            int responseCode = conn.getResponseCode();

            // 🆕 添加日志：打印响应码
            android.util.Log.d("FolderSize", "响应码: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                String responseStr = response.toString();

                // 🆕 添加日志：打印原始响应数据
                android.util.Log.d("FolderSize", "原始响应: " + responseStr);

                JSONObject json = new JSONObject(responseStr);
                int code = json.optInt("code", -1);

                // 🆕 添加日志：打印code
                android.util.Log.d("FolderSize", "返回code: " + code);

                if (code == 200) {
                    JSONObject data = json.getJSONObject("data");
                    long sizeBytes = data.optLong("sizeBytes", 0);
                    folderSizeKB = sizeBytes / 1024.0;

                    // 🆕 添加日志：打印sizeBytes和转换后的KB
                    android.util.Log.d("FolderSize", "文件夹大小 - bytes: " + sizeBytes + ", KB: " + folderSizeKB);
                    android.util.Log.d("FolderSize", "文件/文件夹数量: " + data.optInt("fileCount", 0));

                } else if (code == 500 && json.optString("msg").contains("不存在")) {
                    folderSizeKB = 0;
                    android.util.Log.d("FolderSize", "文件夹不存在，大小设为0");
                } else {
                    android.util.Log.d("FolderSize", "获取文件夹大小失败，msg: " + json.optString("msg"));
                }
            } else {
                android.util.Log.d("FolderSize", "HTTP请求失败，响应码: " + responseCode);
            }
        } catch (Exception e) {
            android.util.Log.e("FolderSize", "获取文件夹大小异常: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (conn != null) conn.disconnect();
        }

        android.util.Log.d("FolderSize", "最终返回文件夹大小: " + folderSizeKB + " KB");
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

                    // 获取文件夹大小
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





    /**
     * 检查是否可以更新文件字段
     */
    public boolean canUpdateFiles(int id) {
        String selectSql = "select wenjian from order_info where id = ?";
        SchedulingDao checkDao = new SchedulingDao();
        List<OrderInfo> result = checkDao.query(OrderInfo.class, selectSql, id);
        return result != null && result.size() > 0;
    }

    /**
     * 获取当前文件字符串
     */
    public String getCurrentFiles(int id) {
        String selectSql = "select wenjian from order_info where id = ?";
        SchedulingDao selectDao = new SchedulingDao();
        List<OrderInfo> result = selectDao.query(OrderInfo.class, selectSql, id);
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
        String sql = "update order_info set wenjian = ? where id = ?";
        SchedulingDao updateDao = new SchedulingDao();
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
     * 上传文件到服务器（带空间检查和动态路径）
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

                    // 2. 构建动态路径
                    String dynamicPath = "/paichan/" + companyName + "/";

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
     * 从服务器删除文件（使用动态路径）
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

                    // 构建动态路径
                    String dynamicPath = "/paichan/" + companyName + "/";

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
        void onWarning(String message, double usagePercent, double estimatedUsagePercent);
    }

    public interface DeleteCallback {
        void onSuccess();
        void onFailure(String error);
    }

}
