package com.example.myapplication.jxc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.jxc.entity.YhJinXiaoCunUser;
import com.example.myapplication.utils.ToastUtil;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class JxcActivity extends AppCompatActivity {
    private YhJinXiaoCunUser yhJinXiaoCunUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jxc_main);

        MyApplication myApplication = (MyApplication) getApplication();
        yhJinXiaoCunUser = myApplication.getYhJinXiaoCunUser();


        LinearLayout ruku = findViewById(R.id.ruku);
        ruku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yhJinXiaoCunUser.getAdminis().equals("true")) {
                    Intent intent = new Intent(JxcActivity.this, RukuActivity.class);
                    intent.putExtra("churuku", "入库");
                    startActivity(intent);
                } else {
                    ToastUtil.show(JxcActivity.this, "无权限！");
                }
            }
        });

        LinearLayout chuku = findViewById(R.id.chuku);
        chuku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yhJinXiaoCunUser.getAdminis().equals("true")) {
                    Intent intent = new Intent(JxcActivity.this, RukuActivity.class);
                    intent.putExtra("churuku", "出库");
                    startActivity(intent);
                } else {
                    ToastUtil.show(JxcActivity.this, "无权限！");
                }
            }
        });

        LinearLayout qichu = findViewById(R.id.qichu);
        qichu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yhJinXiaoCunUser.getAdminis().equals("true")) {
                    Intent intent = new Intent(JxcActivity.this, QiChuActivity.class);
                    startActivity(intent);
                } else {
                    ToastUtil.show(JxcActivity.this, "无权限！");
                }
            }
        });

        LinearLayout mingxi = findViewById(R.id.mingxi);
        mingxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yhJinXiaoCunUser.getAdminis().equals("true")) {
                    Intent intent = new Intent(JxcActivity.this, MingXiActivity.class);
                    startActivity(intent);
                } else {
                    ToastUtil.show(JxcActivity.this, "无权限！");
                }
            }
        });

        LinearLayout jinxiaocun = findViewById(R.id.jxc);
        jinxiaocun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JxcActivity.this, JinXiaoCunActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout productQuery = findViewById(R.id.product_query);
        productQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JxcActivity.this, ProductQueryActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout kehuQuery = findViewById(R.id.kehu_query);
        kehuQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JxcActivity.this, KehuQueryActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout jichuziliao = findViewById(R.id.jczl);
        jichuziliao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yhJinXiaoCunUser.getAdminis().equals("true")) {
                    Intent intent = new Intent(JxcActivity.this, JiChuZiLiaoActivity.class);
                    startActivity(intent);
                } else {
                    ToastUtil.show(JxcActivity.this, "无权限！");
                }
            }
        });

        LinearLayout biji = findViewById(R.id.biji);
        biji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yhJinXiaoCunUser.getAdminis().equals("true")) {
                    Intent intent = new Intent(JxcActivity.this, BiJiActivity.class);
                    startActivity(intent);
                } else {
                    ToastUtil.show(JxcActivity.this, "无权限！");
                }
            }
        });

        LinearLayout kehu = findViewById(R.id.kehu);
        kehu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yhJinXiaoCunUser.getAdminis().equals("true")) {
                    Intent intent = new Intent(JxcActivity.this, KehuActivity.class);
                    intent.putExtra("kehu_type", "客户");
                    startActivity(intent);
                } else {
                    ToastUtil.show(JxcActivity.this, "无权限！");
                }
            }
        });

        LinearLayout gongyingshang = findViewById(R.id.gongyingshang);
        gongyingshang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yhJinXiaoCunUser.getAdminis().equals("true")) {
                    Intent intent = new Intent(JxcActivity.this, KehuActivity.class);
                    intent.putExtra("kehu_type", "供应商");
                    startActivity(intent);
                } else {
                    ToastUtil.show(JxcActivity.this, "无权限！");
                }
            }
        });

        LinearLayout user = findViewById(R.id.user);
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yhJinXiaoCunUser.getAdminis().equals("true")) {
                    Intent intent = new Intent(JxcActivity.this, UserActivity.class);
                    startActivity(intent);
                } else {
                    ToastUtil.show(JxcActivity.this, "无权限！");
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
}
