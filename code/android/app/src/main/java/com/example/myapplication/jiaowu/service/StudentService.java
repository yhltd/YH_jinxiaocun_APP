package com.example.myapplication.jiaowu.service;

import com.example.myapplication.jiaowu.dao.JiaowuBaseDao;
import com.example.myapplication.jiaowu.entity.SheZhi;
import com.example.myapplication.jiaowu.entity.Student;

import java.util.List;

public class StudentService {
    private JiaowuBaseDao base;

    /**
     * 查询全部数据
     */
    public List<Student> getList(String company, String student, String teacher, String kecheng, String start_date, String stop_date) {
        String sql = "select ID,RealName,Sex,rgdate,Course,Teacher,Classnum,phone,Fee,(select SUM(case when Company =? and realname=student.realname then paid + money else 0 end) from payment ) mall ,ifnull(ifnull(Fee,0) -ifnull((select SUM(case when Company =? and realname=student.realname then paid + money else 0 end) from payment ),0),0) as Nocost,(select SUM(case when Company=? and student_name=student.realname and course=student.Course then keshi else 0 end ) from keshi_detail ) nall,ifnull(Allhour,0) - ifnull((select SUM(case when Company=? and student_name=student.realname and course=student.Course then keshi else 0 end ) from keshi_detail ),0) as Nohour,Allhour,Type FROM student where RealName LIKE '%' ? '%' AND Teacher LIKE '%' ? '%' AND Course LIKE '%' ? '%' AND rgdate >= ? AND rgdate <= ?";
        base = new JiaowuBaseDao();
        List<Student> list = base.query(Student.class, sql, company,company,company,company,student,teacher,kecheng,start_date,stop_date);
        return list;
    }

    /**
     * 查询全部数据
     */
    public List<Student> getListQianFei(String company, String student) {
        String sql = "select * from student where Nocost is not null and Nocost>0 and RealName like '%' ? '%' and Company= ?";
        base = new JiaowuBaseDao();
        List<Student> list = base.query(Student.class, sql, student,company);
        return list;
    }

    /**
     * 新增
     */
    public boolean insert(Student student) {
        String sql = "insert into student(RealName,Sex,rgdate,Course,Teacher,Classnum,phone,Fee,Allhour,Type,Company)values(?,?,?,?,?,?,?,?,?,?,?)";
        base = new JiaowuBaseDao();
        long result = base.executeOfId(sql, student.getRealname(), student.getSex(), student.getRgdate(), student.getCourse(), student.getTeacher(), student.getClassnum(), student.getPhone(),student.getFee(),student.getAllhour(),student.getType(),student.getCompany());
        return result > 0;
    }

    /**
     * 修改
     */
    public boolean update(Student student) {
        String sql = "update student set RealName=?,Sex=?,rgdate=?,Course=?,Teacher=?,Classnum=?,phone=?,Fee=?,Allhour=?,Type=? where ID=? ";

        base = new JiaowuBaseDao();
        boolean result = base.execute(sql, student.getRealname(), student.getSex(), student.getRgdate(), student.getCourse(), student.getTeacher(), student.getClassnum(), student.getPhone(),student.getFee(),student.getAllhour(),student.getType(),student.getId());
        return result;
    }

    /**
     * 删除
     */
    public boolean delete(int id) {
        String sql = "delete from student where ID = ?";
        base = new JiaowuBaseDao();
        return base.execute(sql, id);
    }
}
