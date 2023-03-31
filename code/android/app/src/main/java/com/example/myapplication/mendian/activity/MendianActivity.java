package com.example.myapplication.mendian.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.mendian.entity.YhMendianOrderDetail;
import com.example.myapplication.mendian.entity.YhMendianUser;

import java.util.ArrayList;
import java.util.List;

public class MendianActivity extends AppCompatActivity {

    private YhMendianUser yhMendianUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        MyApplication myApplication = (MyApplication) getApplication();
        yhMendianUser = myApplication.getYhMendianUser();
        myApplication.setYhMendianOrderDetail(new YhMendianOrderDetail());
        myApplication.setOrderDetails(new ArrayList<>());

        LinearLayout Kehuinfo = findViewById(R.id.Kehuinfo);
        Kehuinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MendianActivity.this, KehuinfoActivity.class);
                startActivity(intent);
            }
        });
        LinearLayout Zongjiao = findViewById(R.id.Zongjiao);
        Zongjiao.setOnClickListener(new View.OnClickListener() {
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
        LinearLayout Users = findViewById(R.id.Users);
        Users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MendianActivity.this, UsersActivity.class);
                startActivity(intent);
            }
        });
        LinearLayout Productshezhi = findViewById(R.id.Productshezhi);
        Productshezhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MendianActivity.this, ProductshezhiActivity.class);
                startActivity(intent);
            }
        });
        LinearLayout Memberinfo = findViewById(R.id.Memberinfo);
        Memberinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MendianActivity.this, MemberinfoActivity.class);
                startActivity(intent);
            }
        });
        LinearLayout Memberlevel = findViewById(R.id.Memberlevel);
        Memberlevel.setOnClickListener(new View.OnClickListener() {
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
                Intent intent = new Intent(MendianActivity.this, OrderPanelActivity.class);
                startActivity(intent);
            }
        });
        LinearLayout Orders = findViewById(R.id.Orders);
        Orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MendianActivity.this, OrdersActivity.class);
                startActivity(intent);
            }
        });
        LinearLayout Reportform = findViewById(R.id.Reportform);
        Reportform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MendianActivity.this, ReportFormActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        //重新获取数据的逻辑，此处根据自己的要求回去
        MyApplication myApplication = (MyApplication) getApplication();
        myApplication.setYhMendianOrderDetail(new YhMendianOrderDetail());
        myApplication.setOrderDetails(new ArrayList<>());
    }

}
