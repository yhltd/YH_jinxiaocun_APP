package com.example.myapplication.jiaowu.service;

import com.example.myapplication.CacheManager;
import com.example.myapplication.jiaowu.dao.JiaowuBaseDao;
import com.example.myapplication.jiaowu.dao.JiaowuServerDao;
import com.example.myapplication.jiaowu.entity.Student;

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

public class StudentService {
    private JiaowuBaseDao base;
    private JiaowuServerDao base1;

    /**
     * 查询全部数据
     */
    public List<Student> getList(String company, String student, String teacher, String kecheng, String start_date, String stop_date) {

        int shujukuValue = CacheManager.getInstance().getShujukuValue();

        if (shujukuValue == 1) {
            // SQL Server 版本（老版兼容）
            String sql = "SELECT s.ID, s.RealName, s.Sex, s.rgdate, s.Course, s.Teacher, s.Classnum, s.phone, s.Fee, " +
                    "CAST(ISNULL(p.total_paid, 0) AS DECIMAL(18,2)) as mall, " +
                    "CAST(ISNULL(ISNULL(s.Fee, 0) - ISNULL(p.total_paid, 0), 0) AS FLOAT) as Nocost, " +
                    "CAST(ISNULL(k.used_keshi, 0) AS DECIMAL(18,2)) as nall, " +
                    "CAST(ISNULL(s.Allhour, 0) - ISNULL(k.used_keshi, 0) AS FLOAT) as Nohour, " +
                    "s.Allhour, s.Type, s.wenjian " +  // 新增文件字段
                    "FROM student s " +
                    "LEFT JOIN (" +
                    "    SELECT realname, SUM(CAST(paid + money AS DECIMAL(18,2))) as total_paid " +
                    "    FROM payment " +
                    "    WHERE Company = ? " +
                    "    GROUP BY realname" +
                    ") p ON s.RealName = p.realname " +
                    "LEFT JOIN (" +
                    "    SELECT student_name, course, SUM(CAST(keshi AS DECIMAL(18,2))) as used_keshi " +
                    "    FROM keshi_detail " +
                    "    WHERE Company = ? " +
                    "    GROUP BY student_name, course" +
                    ") k ON s.RealName = k.student_name AND s.Course = k.course " +
                    "WHERE s.RealName LIKE '%' + ? + '%' " +
                    "AND s.Teacher LIKE '%' + ? + '%' " +
                    "AND s.Course LIKE '%' + ? + '%' " +
                    "AND s.rgdate >= ? " +
                    "AND s.rgdate <= ?";

            base1 = new JiaowuServerDao();
            List<Student> list = base1.query(Student.class, sql, company, company, student, teacher, kecheng, start_date, stop_date);
            return list;
        } else {
            // MySQL 版本（原版不动）
            String sql = "select ID,RealName,Sex,rgdate,Course,Teacher,Classnum,phone,Fee,(select SUM(case when Company =? and realname=student.realname then paid + money else 0 end) from payment ) mall ,ifnull(ifnull(Fee,0) -ifnull((select SUM(case when Company =? and realname=student.realname then paid + money else 0 end) from payment ),0),0) as Nocost,(select SUM(case when Company=? and student_name=student.realname and course=student.Course then keshi else 0 end ) from keshi_detail ) nall,ifnull(Allhour,0) - ifnull((select SUM(case when Company=? and student_name=student.realname and course=student.Course then keshi else 0 end ) from keshi_detail ),0) as Nohour,Allhour,Type,wenjian FROM student where RealName LIKE '%' ? '%' AND Teacher LIKE '%' ? '%' AND Course LIKE '%' ? '%' AND rgdate >= ? AND rgdate <= ?";
            base = new JiaowuBaseDao();
            List<Student> list = base.query(Student.class, sql, company,company,company,company,student,teacher,kecheng,start_date,stop_date);
            return list;
        }
    }

    /**
     * 查询全部数据
     */
    public List<Student> getListQianFei(String s, String teacherCompany, String student, String company) {
        int shujukuValue = CacheManager.getInstance().getShujukuValue();

        if (shujukuValue == 1) {
            // SQL Server 版本（老版兼容）
            String sql = "select ID,RealName,isnull(isnull(Fee,0) -isnull((select SUM(case when Company = ? and realname=student.realname then paid+money else 0 end) from payment ),0),0) as Nocost,rgdate,Course,Teacher,Classnum,phone,isnull(Allhour,0) - isnull((select SUM(case when Company= ? and student_name=student.realname and course=student.Course then keshi else 0 end ) from keshi_detail ),0) as Nohour,wenjian FROM student where RealName like '%' + ? + '%' and isnull(isnull(Fee,0) -isnull((select SUM(case when Company = ? and realname=student.realname then paid+money else 0 end) from payment ),0),0) > 0";
            base1 = new JiaowuServerDao();
            List<Student> list = base1.query(Student.class, sql, s, teacherCompany, student, company);
            return list;
        } else {
            // MySQL 版本（原版不动）
            String sql = "select ID,RealName,ifnull(ifnull(Fee,0) -ifnull((select SUM(case when Company = ? and realname=student.realname then paid+money else 0 end) from payment ),0),0) as Nocost,rgdate,Course,Teacher,Classnum,phone,ifnull(Allhour,0) - ifnull((select SUM(case when Company= ? and student_name=student.realname and course=student.Course then keshi else 0 end ) from keshi_detail ),0) as Nohour,wenjian FROM student where RealName like '%' ? '%' and ifnull(ifnull(Fee,0) -ifnull((select SUM(case when Company = ? and realname=student.realname then paid+money else 0 end) from payment ),0),0) > 0";
            base = new JiaowuBaseDao();
            List<Student> list = base.query(Student.class, sql, s, teacherCompany, student, company);
            return list;
        }
    }

    /**
     * 新增
     */
    public boolean insert(Student student) {
        int shujukuValue = CacheManager.getInstance().getShujukuValue();

        if (shujukuValue == 1) {
            // SQL Server 版本
            String sql = "insert into student(RealName,Sex,rgdate,Course,Teacher,Classnum,phone,Fee,Allhour,Type,Company,wenjian)values(?,?,?,?,?,?,?,?,?,?,?,?)";
            base1 = new JiaowuServerDao();
            long result = base1.executeOfId(sql,
                    student.getRealname(),
                    student.getSex(),
                    student.getRgdate(),
                    student.getCourse(),
                    student.getTeacher(),
                    student.getClassnum(),
                    student.getPhone(),
                    student.getFee(),
                    student.getAllhour(),
                    student.getType(),
                    student.getCompany(),
                    student.getWenjian());  // 新增文件字段
            return result > 0;
        } else {
            // MySQL 版本
            String sql = "insert into student(RealName,Sex,rgdate,Course,Teacher,Classnum,phone,Fee,Allhour,Type,Company,wenjian)values(?,?,?,?,?,?,?,?,?,?,?,?)";
            base = new JiaowuBaseDao();
            long result = base.executeOfId(sql,
                    student.getRealname(),
                    student.getSex(),
                    student.getRgdate(),
                    student.getCourse(),
                    student.getTeacher(),
                    student.getClassnum(),
                    student.getPhone(),
                    student.getFee(),
                    student.getAllhour(),
                    student.getType(),
                    student.getCompany(),
                    student.getWenjian());  // 新增文件字段
            return result > 0;
        }
    }

    /**
     * 修改
     */
    public boolean update(Student student) {
        int shujukuValue = CacheManager.getInstance().getShujukuValue();

        if (shujukuValue == 1) {
            // SQL Server 版本
            String sql = "update student set RealName=?,Sex=?,rgdate=?,Course=?,Teacher=?,Classnum=?,phone=?,Fee=?,Allhour=?,Type=?,wenjian=? where ID=?";
            base1 = new JiaowuServerDao();
            boolean result = base1.execute(sql,
                    student.getRealname(),
                    student.getSex(),
                    student.getRgdate(),
                    student.getCourse(),
                    student.getTeacher(),
                    student.getClassnum(),
                    student.getPhone(),
                    student.getFee(),
                    student.getAllhour(),
                    student.getType(),
                    student.getWenjian(),  // 新增文件字段
                    student.getId());
            return result;
        } else {
            // MySQL 版本
            String sql = "update student set RealName=?,Sex=?,rgdate=?,Course=?,Teacher=?,Classnum=?,phone=?,Fee=?,Allhour=?,Type=?,wenjian=? where ID=?";
            base = new JiaowuBaseDao();
            boolean result = base.execute(sql,
                    student.getRealname(),
                    student.getSex(),
                    student.getRgdate(),
                    student.getCourse(),
                    student.getTeacher(),
                    student.getClassnum(),
                    student.getPhone(),
                    student.getFee(),
                    student.getAllhour(),
                    student.getType(),
                    student.getWenjian(),  // 新增文件字段
                    student.getId());
            return result;
        }
    }

    /**
     * 删除
     */
    public boolean delete(int id) {
        int shujukuValue = CacheManager.getInstance().getShujukuValue();

        if (shujukuValue == 1) {
            // SQL Server 版本
            String sql = "delete from student where ID = ?";
            base1 = new JiaowuServerDao();
            return base1.execute(sql, id);
        } else {
            // MySQL 版本
            String sql = "delete from student where ID = ?";
            base = new JiaowuBaseDao();
            return base.execute(sql, id);
        }
    }

    // ==================== 文件上传相关方法 ====================

    /**
     * 检查是否可以更新文件字段
     */
    public boolean canUpdateFiles(int id) {
        int shujukuValue = CacheManager.getInstance().getShujukuValue();
        String selectSql = "select wenjian from student where ID = ?";

        if (shujukuValue == 1) {
            base1 = new JiaowuServerDao();
            List<Student> result = base1.query(Student.class, selectSql, id);
            return result != null && result.size() > 0;
        } else {
            base = new JiaowuBaseDao();
            List<Student> result = base.query(Student.class, selectSql, id);
            return result != null && result.size() > 0;
        }
    }

    /**
     * 获取当前文件字符串
     */
    public String getCurrentFiles(int id) {
        int shujukuValue = CacheManager.getInstance().getShujukuValue();
        String selectSql = "select wenjian from student where ID = ?";

        if (shujukuValue == 1) {
            base1 = new JiaowuServerDao();
            List<Student> result = base1.query(Student.class, selectSql, id);
            if (result != null && result.size() > 0) {
                return result.get(0).getWenjian();
            }
        } else {
            base = new JiaowuBaseDao();
            List<Student> result = base.query(Student.class, selectSql, id);
            if (result != null && result.size() > 0) {
                return result.get(0).getWenjian();
            }
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
        int shujukuValue = CacheManager.getInstance().getShujukuValue();
        String sql = "update student set wenjian = ? where ID = ?";

        if (shujukuValue == 1) {
            base1 = new JiaowuServerDao();
            return base1.execute(sql, newFileUrls, id);
        } else {
            base = new JiaowuBaseDao();
            return base.execute(sql, newFileUrls, id);
        }
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
                    String jiaowuPath = "/jiaowu/";
                    writeFormField(dos, boundary, "name", finalFileName);
                    writeFormField(dos, boundary, "path", jiaowuPath);
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
                            String fileUrl = "http://yhocn.cn:9088" + jiaowuPath + finalFileName;
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