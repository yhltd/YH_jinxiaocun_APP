package com.example.myapplication.jiaowu.service;

import com.example.myapplication.CacheManager;
import com.example.myapplication.jiaowu.dao.JiaowuBaseDao;
import com.example.myapplication.jiaowu.dao.JiaowuServerDao;
import com.example.myapplication.jiaowu.entity.SheZhi;
import com.example.myapplication.jiaowu.entity.Student;

import java.util.List;

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
                    "CAST(ISNULL(p.total_paid, 0) AS DECIMAL(18,2)) as mall, " +  // BigDecimal → DECIMAL
                    "CAST(ISNULL(ISNULL(s.Fee, 0) - ISNULL(p.total_paid, 0), 0) AS FLOAT) as Nocost, " +  // float → FLOAT
                    "CAST(ISNULL(k.used_keshi, 0) AS DECIMAL(18,2)) as nall, " +  // BigDecimal → DECIMAL
                    "CAST(ISNULL(s.Allhour, 0) - ISNULL(k.used_keshi, 0) AS FLOAT) as Nohour, " +  // float → FLOAT
                    "s.Allhour, s.Type " +
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
            String sql = "select ID,RealName,Sex,rgdate,Course,Teacher,Classnum,phone,Fee,(select SUM(case when Company =? and realname=student.realname then paid + money else 0 end) from payment ) mall ,ifnull(ifnull(Fee,0) -ifnull((select SUM(case when Company =? and realname=student.realname then paid + money else 0 end) from payment ),0),0) as Nocost,(select SUM(case when Company=? and student_name=student.realname and course=student.Course then keshi else 0 end ) from keshi_detail ) nall,ifnull(Allhour,0) - ifnull((select SUM(case when Company=? and student_name=student.realname and course=student.Course then keshi else 0 end ) from keshi_detail ),0) as Nohour,Allhour,Type FROM student where RealName LIKE '%' ? '%' AND Teacher LIKE '%' ? '%' AND Course LIKE '%' ? '%' AND rgdate >= ? AND rgdate <= ?";
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
            String sql = "select ID,RealName,isnull(isnull(Fee,0) -isnull((select SUM(case when Company = ? and realname=student.realname then paid+money else 0 end) from payment ),0),0) as Nocost,rgdate,Course,Teacher,Classnum,phone,isnull(Allhour,0) - isnull((select SUM(case when Company= ? and student_name=student.realname and course=student.Course then keshi else 0 end ) from keshi_detail ),0) as Nohour FROM student where RealName like '%' + ? + '%' and isnull(isnull(Fee,0) -isnull((select SUM(case when Company = ? and realname=student.realname then paid+money else 0 end) from payment ),0),0) > 0";
            base1 = new JiaowuServerDao();
            List<Student> list = base1.query(Student.class, sql, s, teacherCompany, student, company);
            return list;

        } else {
            // MySQL 版本（原版不动）

            //        String sql = "select * from student where Nocost is not null and Nocost>0 and RealName like '%' ? '%' and Company= ?";
            String sql = "select ID,RealName,ifnull(ifnull(Fee,0) -ifnull((select SUM(case when Company = ? and realname=student.realname then paid+money else 0 end) from payment ),0),0) as Nocost,rgdate,Course,Teacher,Classnum,phone,ifnull(Allhour,0) - ifnull((select SUM(case when Company= ? and student_name=student.realname and course=student.Course then keshi else 0 end ) from keshi_detail ),0) as Nohour FROM student where RealName like '%' ? '%' and ifnull(ifnull(Fee,0) -ifnull((select SUM(case when Company = ? and realname=student.realname then paid+money else 0 end) from payment ),0),0) > 0";
            base = new JiaowuBaseDao();
            List<Student> list = base.query(Student.class, sql, s, teacherCompany, student, company);
            return list;
        }


    }

    /**
     * 新增
     */
//    public boolean insert(Student student) {
//        String sql = "insert into student(RealName,Sex,rgdate,Course,Teacher,Classnum,phone,Fee,Allhour,Type,Company)values(?,?,?,?,?,?,?,?,?,?,?)";
//        base = new JiaowuBaseDao();
//        long result = base.executeOfId(sql, student.getRealname(), student.getSex(), student.getRgdate(), student.getCourse(), student.getTeacher(), student.getClassnum(), student.getPhone(),student.getFee(),student.getAllhour(),student.getType(),student.getCompany());
//        return result > 0;
//    }
    public boolean insert(Student student) {
        int shujukuValue = CacheManager.getInstance().getShujukuValue();

        if (shujukuValue == 1) {
            // SQL Server 版本
            String sql = "insert into student(RealName,Sex,rgdate,Course,Teacher,Classnum,phone,Fee,Allhour,Type,Company)values(?,?,?,?,?,?,?,?,?,?,?)";
            base1 = new JiaowuServerDao();
            long result = base1.executeOfId(sql, student.getRealname(), student.getSex(), student.getRgdate(), student.getCourse(), student.getTeacher(), student.getClassnum(), student.getPhone(),student.getFee(),student.getAllhour(),student.getType(),student.getCompany());
            return result > 0;
        } else {
            // MySQL 版本
            String sql = "insert into student(RealName,Sex,rgdate,Course,Teacher,Classnum,phone,Fee,Allhour,Type,Company)values(?,?,?,?,?,?,?,?,?,?,?)";
            base = new JiaowuBaseDao();
            long result = base.executeOfId(sql, student.getRealname(), student.getSex(), student.getRgdate(), student.getCourse(), student.getTeacher(), student.getClassnum(), student.getPhone(),student.getFee(),student.getAllhour(),student.getType(),student.getCompany());
            return result > 0;
        }
    }

    /**
     * 修改
     */
//    public boolean update(Student student) {
//        String sql = "update student set RealName=?,Sex=?,rgdate=?,Course=?,Teacher=?,Classnum=?,phone=?,Fee=?,Allhour=?,Type=? where ID=? ";
//
//        base = new JiaowuBaseDao();
//        boolean result = base.execute(sql, student.getRealname(), student.getSex(), student.getRgdate(), student.getCourse(), student.getTeacher(), student.getClassnum(), student.getPhone(),student.getFee(),student.getAllhour(),student.getType(),student.getId());
//        return result;
//    }
    public boolean update(Student student) {
        int shujukuValue = CacheManager.getInstance().getShujukuValue();

        if (shujukuValue == 1) {
            // SQL Server 版本
            String sql = "update student set RealName=?,Sex=?,rgdate=?,Course=?,Teacher=?,Classnum=?,phone=?,Fee=?,Allhour=?,Type=? where ID=?";
            base1 = new JiaowuServerDao();
            boolean result = base1.execute(sql, student.getRealname(), student.getSex(), student.getRgdate(), student.getCourse(), student.getTeacher(), student.getClassnum(), student.getPhone(), student.getFee(), student.getAllhour(), student.getType(), student.getId());
            return result;
        } else {
            // MySQL 版本
            String sql = "update student set RealName=?,Sex=?,rgdate=?,Course=?,Teacher=?,Classnum=?,phone=?,Fee=?,Allhour=?,Type=? where ID=?";
            base = new JiaowuBaseDao();
            boolean result = base.execute(sql, student.getRealname(), student.getSex(), student.getRgdate(), student.getCourse(), student.getTeacher(), student.getClassnum(), student.getPhone(), student.getFee(), student.getAllhour(), student.getType(), student.getId());
            return result;
        }
    }

    /**
     * 删除
     */
//    public boolean delete(int id) {
//        String sql = "delete from student where ID = ?";
//        base = new JiaowuBaseDao();
//        return base.execute(sql, id);
//    }
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
}
