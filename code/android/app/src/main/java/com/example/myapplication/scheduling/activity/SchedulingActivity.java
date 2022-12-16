package com.example.myapplication.scheduling.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.jxc.activity.JxcActivity;
import com.example.myapplication.jxc.activity.RukuActivity;
import com.example.myapplication.scheduling.entity.PaibanInfo;
import com.example.myapplication.scheduling.entity.PaibanRenyuan;
import com.example.myapplication.scheduling.entity.UserInfo;
import com.example.myapplication.utils.ToastUtil;

public class SchedulingActivity extends AppCompatActivity {
    private UserInfo userInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.scheduling_main);

        MyApplication myApplication = (MyApplication) getApplication();
        userInfo = myApplication.getUserInfo();


        LinearLayout module = findViewById(R.id.module);
        module.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SchedulingActivity.this, ModuleActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout time = findViewById(R.id.time);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SchedulingActivity.this, TimeConfigActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout bom = findViewById(R.id.bom);
        bom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SchedulingActivity.this, BomActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout order = findViewById(R.id.order);
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SchedulingActivity.this, OrderActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout orderCheck = findViewById(R.id.work_check);
        orderCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SchedulingActivity.this, OrderCheckActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout department = findViewById(R.id.department);
        department.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SchedulingActivity.this, DepartmentActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout renyuan = findViewById(R.id.paiban_renyuan);
        renyuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SchedulingActivity.this, PaibanRenyuanActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout paiban = findViewById(R.id.paiban);
        paiban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SchedulingActivity.this, PaibanInfoActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout paiban_detail = findViewById(R.id.paiban_detail);
        paiban_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SchedulingActivity.this, PaibanDetailActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout user = findViewById(R.id.user_info);
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SchedulingActivity.this, UserActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout work = findViewById(R.id.work);
        work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SchedulingActivity.this, WorkActivity.class);
                startActivity(intent);
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
}
