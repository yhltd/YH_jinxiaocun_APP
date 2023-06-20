package com.example.myapplication.finance.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.XiangQingYeActivity;
import com.example.myapplication.entity.XiangQingYe;
import com.example.myapplication.finance.entity.YhFinanceFaPiao;
import com.example.myapplication.finance.entity.YhFinanceJiJianPeiZhi;
import com.example.myapplication.finance.entity.YhFinanceQuanXian;
import com.example.myapplication.finance.entity.YhFinanceUser;
import com.example.myapplication.finance.service.YhFinanceFaPiaoService;
import com.example.myapplication.finance.service.YhFinanceKehuPeizhiService;
import com.example.myapplication.finance.service.YhFinanceQuanXianService;
import com.example.myapplication.finance.service.YhFinanceUserService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ZhangHaoGuanLiActivity extends AppCompatActivity {

    private final static int REQUEST_CODE_CHANG = 1;

    MyApplication myApplication;

    private YhFinanceUser yhFinanceUser;
    private YhFinanceQuanXian yhFinanceQuanXian;
    private YhFinanceUserService yhFinanceUserService;
    private YhFinanceQuanXianService yhFinanceQuanXianService;
    private EditText username;
    private ListView listView;

    private ListView listView_block;
    private HorizontalScrollView list_table;
    private SimpleAdapter adapter;
    private SimpleAdapter adapter_block;

    private String usernameText;
    private Button sel_button;

    List<YhFinanceUser> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        myApplication = (MyApplication) getApplication();
        yhFinanceUser = myApplication.getYhFinanceUser();
        yhFinanceQuanXian = myApplication.getYhFinanceQuanXian();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhanghaoguanli);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        yhFinanceUserService = new YhFinanceUserService();
        yhFinanceQuanXianService = new YhFinanceQuanXianService();
        //初始化控件
        username = findViewById(R.id.username);
        listView = findViewById(R.id.zhanghaoguanli_list);
        listView_block = findViewById(R.id.list_block);
        list_table = findViewById(R.id.list_table);
        sel_button = findViewById(R.id.sel_button);
        sel_button.setOnClickListener(selClick());
        sel_button.requestFocus();
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

    @SuppressLint("WrongConstant")
    public void switchClick(View v) {
        if(listView_block.getVisibility() == 0){
            listView_block.setVisibility(8);
            list_table.setVisibility(0);
        }else if(listView_block.getVisibility() == 8){
            listView_block.setVisibility(0);
            list_table.setVisibility(8);
        }

    }


    public View.OnClickListener selClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initList();
            }
        };
    }


    private void initList() {
        sel_button.setEnabled(false);
        usernameText = username.getText().toString();
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                listView.setAdapter(StringUtils.cast(adapter));
                listView_block.setAdapter(StringUtils.cast(adapter_block));
                sel_button.setEnabled(true);
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<HashMap<String, Object>> data = new ArrayList<>();
                try {
                    yhFinanceUserService = new YhFinanceUserService();
                    list = yhFinanceUserService.getList(yhFinanceUser.getCompany(),usernameText);
                    if (list == null) return;

                } catch (Exception e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < list.size(); i++) {
                    HashMap<String, Object> item = new HashMap<>();
                    item.put("name", list.get(i).getName());
                    item.put("pwd", list.get(i).getPwd());
                    item.put("doo", list.get(i).getDoo());
                    data.add(item);
                }

                adapter = new SimpleAdapter(ZhangHaoGuanLiActivity.this, data, R.layout.zhanghaoguanli_row, new String[]{"name","pwd","doo"}, new int[]{R.id.name,R.id.pwd,R.id.doo}) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        final LinearLayout view = (LinearLayout) super.getView(position, convertView, parent);
                        LinearLayout linearLayout = (LinearLayout) view.getChildAt(0);
                        linearLayout.setOnLongClickListener(onItemLongClick());
                        linearLayout.setOnClickListener(updateClick());
                        linearLayout.setTag(position);
                        return view;
                    }
                };

                adapter_block = new SimpleAdapter(ZhangHaoGuanLiActivity.this, data, R.layout.zhanghaoguanli_row_block, new String[]{"name","pwd","doo"}, new int[]{R.id.name,R.id.pwd,R.id.doo}) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        final LinearLayout view = (LinearLayout) super.getView(position, convertView, parent);
                        LinearLayout linearLayout = (LinearLayout) view.getChildAt(0);
                        linearLayout.setOnLongClickListener(onItemLongClick());
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

    public View.OnLongClickListener onItemLongClick() {
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(!yhFinanceQuanXian.getZhglDelete().equals("是")){
                    ToastUtil.show(ZhangHaoGuanLiActivity.this, "无权限！");
                    return true;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(ZhangHaoGuanLiActivity.this);
                int position = Integer.parseInt(view.getTag().toString());
                Handler deleteHandler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message msg) {
                        if ((boolean) msg.obj) {
                            ToastUtil.show(ZhangHaoGuanLiActivity.this, "删除成功");
                            initList();
                        }
                        return true;
                    }
                });

                builder.setNeutralButton("查看详情", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        XiangQingYe xiangQingYe = new XiangQingYe();

                        xiangQingYe.setA_title("账号:");
                        xiangQingYe.setB_title("密码:");
                        xiangQingYe.setC_title("操作密码:");

                        xiangQingYe.setA(list.get(position).getName());
                        xiangQingYe.setB(list.get(position).getPwd());
                        xiangQingYe.setC(list.get(position).getDoo());

                        Intent intent = new Intent(ZhangHaoGuanLiActivity.this, XiangQingYeActivity.class);
                        MyApplication myApplication = (MyApplication) getApplication();
                        myApplication.setObj(xiangQingYe);
                        startActivityForResult(intent, REQUEST_CODE_CHANG);
                    }
                });

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Message msg = new Message();
                                msg.obj = yhFinanceQuanXianService.delete(list.get(position).getBianhao());
                                msg.obj = yhFinanceUserService.delete(list.get(position).getId());
                                deleteHandler.sendMessage(msg);
                            }
                        }).start();
                    }
                });

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setMessage("确定删除吗？");
                builder.setTitle("提示");
                builder.show();
                return true;
            }
        };
    }


    public View.OnClickListener updateClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!yhFinanceQuanXian.getZhglUpdate().equals("是")){
                    ToastUtil.show(ZhangHaoGuanLiActivity.this, "无权限！");
                    return;
                }
                int position = Integer.parseInt(view.getTag().toString());
                Intent intent = new Intent(ZhangHaoGuanLiActivity.this, ZhangHaoGuanLiChangeActivity.class);
                intent.putExtra("type", R.id.update_btn);
                MyApplication myApplication = (MyApplication) getApplication();
                myApplication.setObj(list.get(position));
                startActivityForResult(intent, REQUEST_CODE_CHANG);
            }
        };
    }

    public void onInsertClick(View v) {
        if(!yhFinanceQuanXian.getZhglAdd().equals("是")){
            ToastUtil.show(ZhangHaoGuanLiActivity.this, "无权限！");
            return;
        }
        Intent intent = new Intent(ZhangHaoGuanLiActivity.this, ZhangHaoGuanLiChangeActivity.class);
        intent.putExtra("type", R.id.insert_btn);
        startActivityForResult(intent, REQUEST_CODE_CHANG);
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
