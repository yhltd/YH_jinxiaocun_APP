package com.example.myapplication.scheduling.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.entity.SystemBanner;
import com.example.myapplication.jxc.activity.JxcActivity;
import com.example.myapplication.jxc.activity.RukuActivity;
import com.example.myapplication.scheduling.entity.Department;
import com.example.myapplication.scheduling.entity.PaibanInfo;
import com.example.myapplication.scheduling.entity.PaibanRenyuan;
import com.example.myapplication.scheduling.entity.UserInfo;
import com.example.myapplication.scheduling.service.DepartmentService;
import com.example.myapplication.service.SystemService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.MarqueeTextView;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SchedulingActivity extends AppCompatActivity {
    private UserInfo userInfo;
    private DepartmentService departmentService;
    private List<Department> list;
    private boolean pd;

    private SystemService systemService;

    private Banner banner;
    private List<SystemBanner> list1;
    private List<SystemBanner> list2;
    private List<Integer> banner_data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scheduling_main);

        MyApplication myApplication = (MyApplication) getApplication();
        userInfo = myApplication.getUserInfo();
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

        init();
        systeminit();

        LinearLayout module = findViewById(R.id.module);
        module.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = false;
                for (Department department : list) {
                    if (department.getView_name().equals("模块单位") && department.getSel().equals("是")) {
                        myApplication.setPcDepartment(department);
                        pd = true;
                    }
                }
                if (!pd) {
                    ToastUtil.show(SchedulingActivity.this, "无权限！");
                } else {
                    Intent intent = new Intent(SchedulingActivity.this, ModuleActivity.class);
                    startActivity(intent);
                }
            }
        });

        LinearLayout time = findViewById(R.id.time);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = false;
                for (Department department : list) {
                    if (department.getView_name().equals("工作时间及休息日") && department.getSel().equals("是")) {
                        myApplication.setPcDepartment(department);
                        pd = true;
                    }
                }
                if (!pd) {
                    ToastUtil.show(SchedulingActivity.this, "无权限！");
                } else {
                    Intent intent = new Intent(SchedulingActivity.this, TimeConfigActivity.class);
                    startActivity(intent);
                }
            }
        });

        LinearLayout bom = findViewById(R.id.bom);
        bom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = false;
                for (Department department : list) {
                    if (department.getView_name().equals("BOM") && department.getSel().equals("是")) {
                        myApplication.setPcDepartment(department);
                        pd = true;
                    }
                }
                if (!pd) {
                    ToastUtil.show(SchedulingActivity.this, "无权限！");
                } else {
                    Intent intent = new Intent(SchedulingActivity.this, BomActivity.class);
                    startActivity(intent);
                }
            }
        });

        LinearLayout order = findViewById(R.id.order);
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = false;
                for (Department department : list) {
                    if (department.getView_name().equals("订单") && department.getSel().equals("是")) {
                        myApplication.setPcDepartment(department);
                        pd = true;
                    }
                }
                if (!pd) {
                    ToastUtil.show(SchedulingActivity.this, "无权限！");
                } else {
                    Intent intent = new Intent(SchedulingActivity.this, OrderActivity.class);
                    startActivity(intent);
                }
            }
        });

        LinearLayout orderCheck = findViewById(R.id.work_check);
        orderCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = false;
                for (Department department : list) {
                    if (department.getView_name().equals("排产核对") && department.getSel().equals("是")) {
                        myApplication.setPcDepartment(department);
                        pd = true;
                    }
                }
                if (!pd) {
                    ToastUtil.show(SchedulingActivity.this, "无权限！");
                } else {
                    Intent intent = new Intent(SchedulingActivity.this, OrderCheckActivity.class);
                    startActivity(intent);
                }
            }
        });

        LinearLayout department = findViewById(R.id.department);
        department.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = false;
                for (Department department : list) {
                    if (department.getView_name().equals("部门") && department.getSel().equals("是")) {
                        myApplication.setPcDepartment(department);
                        pd = true;
                    }
                }
                if (!pd) {
                    ToastUtil.show(SchedulingActivity.this, "无权限！");
                } else {
                    Intent intent = new Intent(SchedulingActivity.this, DepartmentActivity.class);
                    startActivity(intent);
                }
            }
        });

        LinearLayout renyuan = findViewById(R.id.paiban_renyuan);
        renyuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = false;
                for (Department department : list) {
                    if (department.getView_name().equals("人员信息") && department.getSel().equals("是")) {
                        myApplication.setPcDepartment(department);
                        pd = true;
                    }
                }
                if (!pd) {
                    ToastUtil.show(SchedulingActivity.this, "无权限！");
                } else {
                    Intent intent = new Intent(SchedulingActivity.this, PaibanRenyuanActivity.class);
                    startActivity(intent);
                }
            }
        });

        LinearLayout paiban = findViewById(R.id.paiban);
        paiban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = false;
                for (Department department : list) {
                    if (department.getView_name().equals("排班") && department.getSel().equals("是")) {
                        myApplication.setPcDepartment(department);
                        pd = true;
                    }
                }
                if (!pd) {
                    ToastUtil.show(SchedulingActivity.this, "无权限！");
                } else {
                    Intent intent = new Intent(SchedulingActivity.this, PaibanInfoActivity.class);
                    startActivity(intent);
                }
            }
        });

        LinearLayout paiban_detail = findViewById(R.id.paiban_detail);
        paiban_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = false;
                for (Department department : list) {
                    if (department.getView_name().equals("排班明细") && department.getSel().equals("是")) {
                        myApplication.setPcDepartment(department);
                        pd = true;
                    }
                }
                if (!pd) {
                    ToastUtil.show(SchedulingActivity.this, "无权限！");
                } else {
                    Intent intent = new Intent(SchedulingActivity.this, PaibanDetailActivity.class);
                    startActivity(intent);
                }
            }
        });

        LinearLayout user = findViewById(R.id.user_info);
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = false;
                for (Department department : list) {
                    if (department.getView_name().equals("账号管理") && department.getSel().equals("是")) {
                        myApplication.setPcDepartment(department);
                        pd = true;
                    }
                }
                if (!pd) {
                    ToastUtil.show(SchedulingActivity.this, "无权限！");
                } else {
                    Intent intent = new Intent(SchedulingActivity.this, UserActivity.class);
                    startActivity(intent);
                }
            }
        });

        LinearLayout work = findViewById(R.id.work);
        work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = false;
                for (Department department : list) {
                    if (department.getView_name().equals("排产") && department.getSel().equals("是")) {
                        myApplication.setPcDepartment(department);
                        pd = true;
                    }
                }
                if (!pd) {
                    ToastUtil.show(SchedulingActivity.this, "无权限！");
                } else {
                    Intent intent = new Intent(SchedulingActivity.this, WorkActivity.class);
                    startActivity(intent);
                }
            }
        });

        LinearLayout summary = findViewById(R.id.summary);
        summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = false;
                for (Department department : list) {
                    if (department.getView_name().equals("汇总") && department.getSel().equals("是")) {
                        myApplication.setPcDepartment(department);
                        pd = true;
                    }
                }
                if (!pd) {
                    ToastUtil.show(SchedulingActivity.this, "无权限！");
                } else {
                    Intent intent = new Intent(SchedulingActivity.this, SummaryActivity.class);
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

    private void initData(){
        banner_data = new ArrayList<>();
        banner_data.add(R.drawable.paichan_banner_01);
        banner_data.add(R.drawable.paichan_banner_01);
        banner_data.add(R.drawable.paichan_banner_01);
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
                    list1 = systemService.getList("排产",userInfo.getCompany());
                    list2 = systemService.getTongYongList("排产");
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
                    departmentService = new DepartmentService();
                    list = departmentService.getList(userInfo.getCompany(), userInfo.getDepartment_name(), "");
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
