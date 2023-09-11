package com.example.myapplication;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.widget.EditText;

import com.example.myapplication.fenquan.entity.Renyuan;
import com.example.myapplication.finance.entity.YhFinanceQuanXian;
import com.example.myapplication.jiaowu.entity.AccountManagement;
import com.example.myapplication.jiaowu.entity.Quanxian;
import com.example.myapplication.jiaowu.entity.Teacher;
import com.example.myapplication.jxc.entity.YhJinXiaoCunMingXi;
import com.example.myapplication.jxc.entity.YhJinXiaoCunUser;
import com.example.myapplication.finance.entity.YhFinanceUser;
import com.example.myapplication.mendian.entity.YhMendianOrderDetail;
import com.example.myapplication.mendian.entity.YhMendianProductshezhi;
import com.example.myapplication.mendian.entity.YhMendianUser;
import com.example.myapplication.renshi.entity.YhRenShiUser;
import com.example.myapplication.scheduling.entity.Department;
import com.example.myapplication.scheduling.entity.UserInfo;

import java.util.List;

public class MyApplication extends Application {
    private YhJinXiaoCunUser yhJinXiaoCunUser;
    private YhFinanceUser yhFinanceUser;
    private YhRenShiUser yhRenShiUser;
    private YhMendianProductshezhi yhMendianProductshezhi;
    private YhMendianOrderDetail yhMendianOrderDetail;
    private List<YhMendianOrderDetail> orderDetails;
    private UserInfo userInfo;
    private Department pcDepartment;
    private YhMendianUser yhMendianUser;
    private Renyuan renyuan;
    private Teacher teacher;
    private AccountManagement accountManagement;
    private Quanxian quanxian;
    private YhFinanceQuanXian yhFinanceQuanXian;
    private List<YhJinXiaoCunMingXi> mingxiList;
    private String userNum;

    public String getUserNum() {
        return userNum;
    }

    public void setUserNum(String userNum) {
        this.userNum = userNum;
    }

    public List<YhJinXiaoCunMingXi> getMingxiList() {
        return mingxiList;
    }

    public void setMingxiList(List<YhJinXiaoCunMingXi> mingxiList) {
        this.mingxiList = mingxiList;
    }

    public YhFinanceQuanXian getYhFinanceQuanXian() {
        return yhFinanceQuanXian;
    }

    public void setYhFinanceQuanXian(YhFinanceQuanXian yhFinanceQuanXian) {
        this.yhFinanceQuanXian = yhFinanceQuanXian;
    }

    public YhMendianOrderDetail getYhMendianOrderDetail() {
        return yhMendianOrderDetail;
    }

    public void setYhMendianOrderDetail(YhMendianOrderDetail yhMendianOrderDetail) {
        this.yhMendianOrderDetail = yhMendianOrderDetail;
    }

    public List<YhMendianOrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<YhMendianOrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public YhMendianProductshezhi getYhMendianProductshezhi() {
        return yhMendianProductshezhi;
    }

    public void setYhMendianProductshezhi(YhMendianProductshezhi yhMendianProductshezhi) {
        this.yhMendianProductshezhi = yhMendianProductshezhi;
    }

    public Quanxian getQuanxian() {
        return quanxian;
    }

    public void setQuanxian(Quanxian quanxian) {
        this.quanxian = quanxian;
    }

    public AccountManagement getAccountManagement() {
        return accountManagement;
    }

    public void setAccountManagement(AccountManagement accountManagement) {
        this.accountManagement = accountManagement;
    }

    public YhMendianUser getYhMendianUser() {
        return yhMendianUser;
    }

    public void setYhMendianUser(YhMendianUser yhMendianUser) {
        this.yhMendianUser = yhMendianUser;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public static void setContext(Context context) {
        MyApplication.context = context;
    }

    private static Context context;

    public YhJinXiaoCunUser getYhJinXiaoCunUser() {
        return yhJinXiaoCunUser;
    }

    public void setYhJinXiaoCunUser(YhJinXiaoCunUser yhJinXiaoCunUser) {
        this.yhJinXiaoCunUser = yhJinXiaoCunUser;
    }

    public YhFinanceUser getYhFinanceUser() {
        return yhFinanceUser;
    }

    public void setYhFinanceUser(YhFinanceUser yhFinanceUser) {
        this.yhFinanceUser = yhFinanceUser;
    }

    public YhRenShiUser getYhRenShiUser() {
        return yhRenShiUser;
    }

    public void setYhRenShiUser(YhRenShiUser yhRenShiUser) {
        this.yhRenShiUser = yhRenShiUser;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public Department getPcDepartment() {
        return pcDepartment;
    }

    public void setPcDepartment(Department pcDepartment) {
        this.pcDepartment = pcDepartment;
    }

    public Renyuan getRenyuan() {
        return renyuan;
    }

    public void setRenyuan(Renyuan renyuan) {
        this.renyuan = renyuan;
    }

    private Object obj;

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
    }

    public static void setEditTextReadOnly(EditText... editTexts) {
        for (EditText editText : editTexts) {
            editText.setCursorVisible(false);
            editText.setFocusable(false);
            editText.setFocusableInTouchMode(false);
        }
    }

    public static Context getContext() {
        return context;
    }

}
