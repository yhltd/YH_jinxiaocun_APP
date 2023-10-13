package com.example.myapplication.jiaowu.activity;

import android.content.Intent;
import android.graphics.Color;
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
import com.example.myapplication.entity.SystemBanner;
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
import com.example.myapplication.service.SystemService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.MarqueeTextView;
import com.example.myapplication.utils.ToastUtil;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JiaowuActivity extends AppCompatActivity {
    private Teacher teacher;
    private QuanxianService quanxianService;
    private List<Quanxian> list;
    private boolean pd;

    private SystemService systemService;

    private Banner banner;
    private List<SystemBanner> list1;
    private List<SystemBanner> list2;
    private List<Integer> banner_data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jiaowu_main);

        MyApplication myApplication = (MyApplication) getApplication();
        teacher = myApplication.getTeacher();

        init();
        systemService = new SystemService();
        initData();
        banner = findViewById(R.id.main_banner);

        banner.setAdapter(new BannerImageAdapter<Integer>(banner_data) {
            @Override
            public void onBindView(BannerImageHolder holder, Integer data, int position, int size) {
                holder.imageView.setImageResource(data);
            }
        });

        // 开启循环轮播
        banner.isAutoLoop(true);
        banner.setIndicator(new CircleIndicator(this));
        banner.setScrollBarFadeDuration(1000);
        // 设置指示器颜色(TODO 即选中时那个小点的颜色)
        banner.setIndicatorSelectedColor(Color.GREEN);
        // 开始轮播
        banner.start();

        systeminit();

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
                    startActivityForResult(intent,0);
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
                    startActivityForResult(intent,0);
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
                    startActivityForResult(intent,0);
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
                    startActivityForResult(intent,0);
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
                    startActivityForResult(intent,0);
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
                    startActivityForResult(intent,0);
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
                    startActivityForResult(intent,0);
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
                    startActivityForResult(intent,0);
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
                    startActivityForResult(intent,0);
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
                    startActivityForResult(intent,0);
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
                    startActivityForResult(intent,0);
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
                    startActivityForResult(intent,0);
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
                    startActivityForResult(intent,0);
                }
            }
        });
    }

    private long exitTime = 0;

    private void initData(){
        banner_data = new ArrayList<>();
        banner_data.add(R.drawable.jiaowu_banner_01);
        banner_data.add(R.drawable.jiaowu_banner_01);
        banner_data.add(R.drawable.jiaowu_banner_01);
    }

    private void systeminit() {
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                MarqueeTextView marqueeTextView = findViewById(R.id.marquee);
                if(list1.size() > 0){
                    marqueeTextView.setText(list1.get(0).getText());
                }else if(list2.size() > 0){
                    marqueeTextView.setText(list2.get(0).getText());
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<HashMap<String, Object>> data = new ArrayList<>();
                try {
                    systemService = new SystemService();
                    list1 = systemService.getList("教务",teacher.getCompany());
                    list2 = systemService.getTongYongList("教务");
                    if (list1 == null && list2 == null) return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.obj = null;
                listLoadHandler.sendMessage(msg);
            }
        }).start();
    }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        init();
    }
}
