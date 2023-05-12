package com.example.myapplication.renshi.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.XiangQingYeActivity;
import com.example.myapplication.entity.XiangQingYe;
import com.example.myapplication.renshi.entity.YhRenShiGongZiMingXi;
import com.example.myapplication.renshi.entity.YhRenShiUser;
import com.example.myapplication.renshi.service.YhRenShiGongZiMingXiService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class GeRenSuoDeShuiActivity extends AppCompatActivity {

    private final static int REQUEST_CODE_CHANG = 1;

    private YhRenShiUser yhRenShiUser;
    private YhRenShiGongZiMingXiService yhRenShiGongZiMingXiService;
    private ListView listView;
    List<YhRenShiGongZiMingXi> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gerensuodeshui);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //初始化控件
        listView = findViewById(R.id.gerensuodeshui_list);

        MyApplication myApplication = (MyApplication) getApplication();
        yhRenShiUser = myApplication.getYhRenShiUser();
        initList();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    private void initList() {
        LoadingDialog.getInstance(this).show();
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                listView.setAdapter(StringUtils.cast(msg.obj));
                LoadingDialog.getInstance(getApplicationContext()).dismiss();
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<HashMap<String, Object>> data = new ArrayList<>();
                try {
                    yhRenShiGongZiMingXiService = new YhRenShiGongZiMingXiService();
                    list = yhRenShiGongZiMingXiService.gerensuodeshuiQueryList(yhRenShiUser.getL());
                    if (list == null) return;

                    for (int i = 0; i < list.size(); i++) {
                        HashMap<String, Object> item = new HashMap<>();
                        item.put("B", list.get(i).getB());
                        item.put("C", list.get(i).getC());
                        item.put("D", list.get(i).getD());
                        item.put("E", list.get(i).getE());
                        data.add(item);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                SimpleAdapter adapter = new SimpleAdapter(GeRenSuoDeShuiActivity.this, data, R.layout.gerensuodeshui_row, new String[]{"B","C","D","E"}, new int[]{R.id.B, R.id.C, R.id.D, R.id.E}) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        final LinearLayout view = (LinearLayout) super.getView(position, convertView, parent);
                        LinearLayout linearLayout = (LinearLayout) view.getChildAt(0);
                        linearLayout.setOnClickListener(updateClick());
                        linearLayout.setTag(position);
                        return view;
                    }
                };
                Message msg = new Message();
                msg.obj = adapter;
                listLoadHandler.sendMessage(msg);

            }
        }).start();
    }

    public View.OnClickListener updateClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(GeRenSuoDeShuiActivity.this);
                int position = Integer.parseInt(view.getTag().toString());

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        XiangQingYe xiangQingYe = new XiangQingYe();

                        xiangQingYe.setA_title("姓名:");
                        xiangQingYe.setB_title("部门:");
                        xiangQingYe.setC_title("岗位:");
                        xiangQingYe.setD_title("基本工资:");

                        xiangQingYe.setA(list.get(position).getB());
                        xiangQingYe.setB(list.get(position).getC());
                        xiangQingYe.setC(list.get(position).getD());
                        xiangQingYe.setD(list.get(position).getG());

                        Intent intent = new Intent(GeRenSuoDeShuiActivity.this, XiangQingYeActivity.class);
                        MyApplication myApplication = (MyApplication) getApplication();
                        myApplication.setObj(xiangQingYe);
                        startActivityForResult(intent, REQUEST_CODE_CHANG);
                    }
                });

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setMessage("确定查看明细？");
                builder.setTitle("提示");
                builder.show();
            }
        };
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHANG) {
            if (resultCode == RESULT_OK) {
                initList();
            }
        }
    }


}
