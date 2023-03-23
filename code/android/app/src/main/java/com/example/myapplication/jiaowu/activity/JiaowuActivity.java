package com.example.myapplication.jiaowu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.jiaowu.entity.KeShiDetail;
import com.example.myapplication.jiaowu.entity.Quanxian;
import com.example.myapplication.jiaowu.entity.Teacher;
import com.example.myapplication.jiaowu.entity.TeacherSal;
import com.example.myapplication.jiaowu.service.QuanxianService;
import com.example.myapplication.jxc.activity.BiJiActivity;
import com.example.myapplication.jxc.activity.JiChuZiLiaoActivity;
import com.example.myapplication.jxc.activity.JinXiaoCunActivity;
import com.example.myapplication.jxc.activity.KehuActivity;
import com.example.myapplication.jxc.activity.KehuQueryActivity;
import com.example.myapplication.jxc.activity.MingXiActivity;
import com.example.myapplication.jxc.activity.ProductQueryActivity;
import com.example.myapplication.jxc.activity.QiChuActivity;
import com.example.myapplication.jxc.activity.RukuActivity;
import com.example.myapplication.jxc.activity.UserActivity;
import com.example.myapplication.jxc.entity.YhJinXiaoCunUser;
import com.example.myapplication.scheduling.activity.SchedulingActivity;
import com.example.myapplication.scheduling.entity.Department;
import com.example.myapplication.scheduling.service.DepartmentService;
import com.example.myapplication.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JiaowuActivity extends AppCompatActivity {
    private Teacher teacher;
    private QuanxianService quanxianService;
    private List<Quanxian> list;
    private boolean pd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jiaowu_main);

        MyApplication myApplication = (MyApplication) getApplication();
        teacher = myApplication.getTeacher();

        init();

        LinearLayout shezhi = findViewById(R.id.shezhi);
        shezhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = false;
                for (Quanxian quanxian : list) {
                    if (quanxian.getView_name().equals("设置") && quanxian.getSel().equals("√")) {
                        myApplication.setQuanxian(quanxian);
                        pd = true;
                    }
                }
                if (!pd) {
                    ToastUtil.show(JiaowuActivity.this, "无权限！");
                } else {
                    Intent intent = new Intent(JiaowuActivity.this, SheZhiActivity.class);
                    startActivity(intent);
                }
            }
        });

        LinearLayout xueshengxinxi = findViewById(R.id.xueshengxinxi);
        xueshengxinxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = false;
                for (Quanxian quanxian : list) {
                    if (quanxian.getView_name().equals("学生信息") && quanxian.getSel().equals("√")) {
                        myApplication.setQuanxian(quanxian);
                        pd = true;
                    }
                }
                if (!pd) {
                    ToastUtil.show(JiaowuActivity.this, "无权限！");
                } else {
                    Intent intent = new Intent(JiaowuActivity.this, XueShengXinXiActivity.class);
                    startActivity(intent);
                }
            }
        });

        LinearLayout jiaoshixinxi = findViewById(R.id.jiaoshixinxi);
        jiaoshixinxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = false;
                for (Quanxian quanxian : list) {
                    if (quanxian.getView_name().equals("教师信息") && quanxian.getSel().equals("√")) {
                        myApplication.setQuanxian(quanxian);
                        pd = true;
                    }
                }
                if (!pd) {
                    ToastUtil.show(JiaowuActivity.this, "无权限！");
                } else {
                    Intent intent = new Intent(JiaowuActivity.this, JiaoShiXinXiActivity.class);
                    startActivity(intent);
                }
            }
        });

        LinearLayout jiaofeijilu = findViewById(R.id.jiaofeijilu);
        jiaofeijilu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = false;
                for (Quanxian quanxian : list) {
                    if (quanxian.getView_name().equals("缴费记录") && quanxian.getSel().equals("√")) {
                        myApplication.setQuanxian(quanxian);
                        pd = true;
                    }
                }
                if (!pd) {
                    ToastUtil.show(JiaowuActivity.this, "无权限！");
                } else {
                    Intent intent = new Intent(JiaowuActivity.this, JiaoFeiJiLuActivity.class);
                    startActivity(intent);
                }
            }
        });

        LinearLayout keshitongji = findViewById(R.id.keshitongji);
        keshitongji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = false;
                for (Quanxian quanxian : list) {
                    if (quanxian.getView_name().equals("课时统计") && quanxian.getSel().equals("√")) {
                        myApplication.setQuanxian(quanxian);
                        pd = true;
                    }
                }
                if (!pd) {
                    ToastUtil.show(JiaowuActivity.this, "无权限！");
                } else {
                    Intent intent = new Intent(JiaowuActivity.this, KeShiTongJiActivity.class);
                    startActivity(intent);
                }
            }
        });

        LinearLayout shouzhimingxi = findViewById(R.id.shouzhimingxi);
        shouzhimingxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = false;
                for (Quanxian quanxian : list) {
                    if (quanxian.getView_name().equals("收支明细") && quanxian.getSel().equals("√")) {
                        myApplication.setQuanxian(quanxian);
                        pd = true;
                    }
                }
                if (!pd) {
                    ToastUtil.show(JiaowuActivity.this, "无权限！");
                } else {
                    Intent intent = new Intent(JiaowuActivity.this, ShouZhiMingXiActivity.class);
                    startActivity(intent);
                }
            }
        });

        LinearLayout qianfeixueyuan = findViewById(R.id.qianfeixueyuan);
        qianfeixueyuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = false;
                for (Quanxian quanxian : list) {
                    if (quanxian.getView_name().equals("欠费学员") && quanxian.getSel().equals("√")) {
                        myApplication.setQuanxian(quanxian);
                        pd = true;
                    }
                }
                if (!pd) {
                    ToastUtil.show(JiaowuActivity.this, "无权限！");
                } else {
                    Intent intent = new Intent(JiaowuActivity.this, QianFeiXueYuanActivity.class);
                    startActivity(intent);
                }
            }
        });

        LinearLayout jiaoshigongzi = findViewById(R.id.jiaoshigongzi);
        jiaoshigongzi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = false;
                for (Quanxian quanxian : list) {
                    if (quanxian.getView_name().equals("教师工资") && quanxian.getSel().equals("√")) {
                        myApplication.setQuanxian(quanxian);
                        pd = true;
                    }
                }
                if (!pd) {
                    ToastUtil.show(JiaowuActivity.this, "无权限！");
                } else {
                    Intent intent = new Intent(JiaowuActivity.this, TeacherSalActivity.class);
                    startActivity(intent);
                }
            }
        });

        LinearLayout jiaoshikeshitongji = findViewById(R.id.jiaoshikeshitongji);
        jiaoshikeshitongji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = false;
                for (Quanxian quanxian : list) {
                    if (quanxian.getView_name().equals("教师课时统计") && quanxian.getSel().equals("√")) {
                        myApplication.setQuanxian(quanxian);
                        pd = true;
                    }
                }
                if (!pd) {
                    ToastUtil.show(JiaowuActivity.this, "无权限！");
                } else {
                    Intent intent = new Intent(JiaowuActivity.this, JiaoShiKeShiActivity.class);
                    startActivity(intent);
                }
            }
        });

        LinearLayout yonghuguanli = findViewById(R.id.yonghuguanli);
        yonghuguanli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = false;
                for (Quanxian quanxian : list) {
                    if (quanxian.getView_name().equals("用户管理") && quanxian.getSel().equals("√")) {
                        myApplication.setQuanxian(quanxian);
                        pd = true;
                    }
                }
                if (!pd) {
                    ToastUtil.show(JiaowuActivity.this, "无权限！");
                } else {
                    Intent intent = new Intent(JiaowuActivity.this, AccountManagementActivity.class);
                    startActivity(intent);
                }
            }
        });

        LinearLayout kaoqinbiao = findViewById(R.id.kaoqinbiao);
        kaoqinbiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = false;
                for (Quanxian quanxian : list) {
                    if (quanxian.getView_name().equals("考勤表") && quanxian.getSel().equals("√")) {
                        myApplication.setQuanxian(quanxian);
                        pd = true;
                    }
                }
                if (!pd) {
                    ToastUtil.show(JiaowuActivity.this, "无权限！");
                } else {
                    Intent intent = new Intent(JiaowuActivity.this, KaoqinActivity.class);
                    startActivity(intent);
                }
            }
        });

        LinearLayout jiaoshikebiao = findViewById(R.id.jiaoshikebiao);
        jiaoshikebiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = false;
                for (Quanxian quanxian : list) {
                    if (quanxian.getView_name().equals("教师课表") && quanxian.getSel().equals("√")) {
                        myApplication.setQuanxian(quanxian);
                        pd = true;
                    }
                }
                if (!pd) {
                    ToastUtil.show(JiaowuActivity.this, "无权限！");
                } else {
                    Intent intent = new Intent(JiaowuActivity.this, TeacherCurriculumActivity.class);
                    startActivity(intent);
                }
            }
        });

        LinearLayout quanxianguanli = findViewById(R.id.quanxianguanli);
        quanxianguanli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = false;
                for (Quanxian quanxian : list) {
                    if (quanxian.getView_name().equals("权限管理") && quanxian.getSel().equals("√")) {
                        myApplication.setQuanxian(quanxian);
                        pd = true;
                    }
                }
                if (!pd) {
                    ToastUtil.show(JiaowuActivity.this, "无权限！");
                } else {
                    Intent intent = new Intent(JiaowuActivity.this, QuanxianActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                //弹出提示，可以有多种方式
                Toast.makeText(getApplicationContext(), "再按一次退出到登录页面", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private void init() {
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<HashMap<String, Object>> data = new ArrayList<>();
                try {
                    quanxianService = new QuanxianService();
                    list = quanxianService.getListQuanXian(teacher.getId(), "");
                    if (list == null) return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.obj = null;
                listLoadHandler.sendMessage(msg);
            }
        }).start();
    }
}
