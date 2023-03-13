package com.example.myapplication.mendian.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.mendian.entity.YhMendianUser;

public class MendianActivity extends AppCompatActivity {

    private YhMendianUser yhMendianUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        MyApplication myApplication = (MyApplication) getApplication();
        yhMendianUser = myApplication.getYhMendianUser();


        LinearLayout Kehuinfo = findViewById(R.id.Kehuinfo);
        Kehuinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MendianActivity.this, KehuinfoActivity.class);
                startActivity(intent);
            }
        });
        LinearLayout Jiaoyizonge = findViewById(R.id.Jiaoyizonge);
        Jiaoyizonge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MendianActivity.this, ZongjiaoActivity.class);
                startActivity(intent);
            }
        });
        LinearLayout rijiao = findViewById(R.id.rijiao);
        rijiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MendianActivity.this, rijiaoActivity.class);
                startActivity(intent);
            }
        });
        LinearLayout Yuejiao = findViewById(R.id.Yuejiao);
        Yuejiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MendianActivity.this, YuejiaoActivity.class);
                startActivity(intent);
            }
        });
        LinearLayout Yuangong = findViewById(R.id.Yuangong);
        Yuangong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MendianActivity.this, UsersActivity.class);
                startActivity(intent);
            }
        });
        LinearLayout Shangpinshezhi = findViewById(R.id.Shangpinshezhi);
        Shangpinshezhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MendianActivity.this, ShangpinshezhiActivity.class);
                startActivity(intent);
            }
        });
        LinearLayout Huiyuanguanli = findViewById(R.id.Huiyuanguanli);
        Huiyuanguanli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MendianActivity.this, MemberinfoActivity.class);
                startActivity(intent);
            }
        });
        LinearLayout Huiyuanlevel = findViewById(R.id.Huiyuanlevel);
        Huiyuanlevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MendianActivity.this, MemberlevelActivity.class);
                startActivity(intent);
            }
        });
        LinearLayout Diandan = findViewById(R.id.Diandan);
        Diandan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MendianActivity.this, DiandanActivity.class);
                startActivity(intent);
            }
        });
        LinearLayout Dingdan = findViewById(R.id.Dingdan);
        Dingdan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MendianActivity.this, DingdanActivity.class);
                startActivity(intent);
            }
        });
        LinearLayout Tongji = findViewById(R.id.Tongji);
        Tongji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MendianActivity.this, TongjiActivity.class);
                startActivity(intent);
            }
        });



    }
}
