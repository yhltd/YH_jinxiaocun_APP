package com.example.myapplication.scheduling.service;

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

    public List<OrderInfo> getOrderId(){
        String sql="select oi.id,oi.is_complete,oi.code,oi.product_name,oi.norms,oi.set_date,oi.company,oi.order_id,oi.set_num-sum(isnull(wd.work_num, 0)) as set_num from order_info as oi left join work_detail as wd on oi.id = wd.order_id group by oi.id,oi.code,oi.product_name,oi.norms,oi.set_date,oi.company,oi.order_id,oi.set_num,oi.is_complete having oi.set_num-sum(isnull(wd.work_num, 0)) > 0";
        base = new SchedulingDao();
        List<OrderInfo> list = base.query(OrderInfo.class, sql);
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
                    String orderPath = "/paichan/";
                    writeFormField(dos, boundary, "name", finalFileName);
                    writeFormField(dos, boundary, "path", orderPath);
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
                            String fileUrl = "http://yhocn.cn:9088" + orderPath + finalFileName;
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
