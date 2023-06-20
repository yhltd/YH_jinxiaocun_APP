package com.example.myapplication.jiaowu.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.jiaowu.entity.AccountManagement;
import com.example.myapplication.jiaowu.entity.Kaoqin;
import com.example.myapplication.jiaowu.entity.Quanxian;
import com.example.myapplication.jiaowu.entity.Teacher;
import com.example.myapplication.jiaowu.service.AccountManagementService;
import com.example.myapplication.jiaowu.service.KaoqinService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KaoqinActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_CHANG = 1;

    private Teacher teacher;
    private KaoqinService kaoqinService;
    private ListView listView;

    private ListView listView_block;
    private HorizontalScrollView list_table;
    private SimpleAdapter adapter;
    private SimpleAdapter adapter_block;

    private EditText s_name;
    private String s_nameText;
    private Button sel_button;

    List<Kaoqin> list;

    private Quanxian quanxian;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jiaowu_kaoqin);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        listView = findViewById(R.id.kaoqin_list);
        listView_block = findViewById(R.id.list_block);
        list_table = findViewById(R.id.list_table);
        MyApplication myApplication = (MyApplication) getApplication();
        teacher = myApplication.getTeacher();
        quanxian = myApplication.getQuanxian();

        initList();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initList() {
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                listView.setAdapter(StringUtils.cast(adapter));
                listView_block.setAdapter(StringUtils.cast(adapter_block));
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<HashMap<String, Object>> data = new ArrayList<>();
                try {
                    kaoqinService = new KaoqinService();
                    list = kaoqinService.getList(s_nameText, teacher.getCompany());
                    if (list == null) return;

                    for (int i = 0; i < list.size(); i++) {
                        HashMap<String, Object> item = new HashMap<>();
                        item.put("s_name", list.get(i).getS_name());
                        item.put("nian", list.get(i).getNian());
                        item.put("yue", list.get(i).getYue());
                        item.put("ri1", list.get(i).getRi1());
                        item.put("ri2", list.get(i).getRi2());
                        item.put("ri3", list.get(i).getRi3());
                        item.put("ri4", list.get(i).getRi4());
                        item.put("ri5", list.get(i).getRi5());
                        item.put("ri6", list.get(i).getRi6());
                        item.put("ri7", list.get(i).getRi7());
                        item.put("ri8", list.get(i).getRi8());
                        item.put("ri9", list.get(i).getRi9());
                        item.put("ri10", list.get(i).getRi10());
                        item.put("ri11", list.get(i).getRi11());
                        item.put("ri12", list.get(i).getRi12());
                        item.put("ri13", list.get(i).getRi13());
                        item.put("ri14", list.get(i).getRi14());
                        item.put("ri15", list.get(i).getRi15());
                        item.put("ri16", list.get(i).getRi16());
                        item.put("ri17", list.get(i).getRi17());
                        item.put("ri18", list.get(i).getRi18());
                        item.put("ri19", list.get(i).getRi19());
                        item.put("ri20", list.get(i).getRi20());
                        item.put("ri21", list.get(i).getRi21());
                        item.put("ri22", list.get(i).getRi22());
                        item.put("ri23", list.get(i).getRi23());
                        item.put("ri24", list.get(i).getRi24());
                        item.put("ri25", list.get(i).getRi25());
                        item.put("ri26", list.get(i).getRi26());
                        item.put("ri27", list.get(i).getRi27());
                        item.put("ri28", list.get(i).getRi28());
                        item.put("ri29", list.get(i).getRi29());
                        item.put("ri30", list.get(i).getRi30());
                        item.put("ri31", list.get(i).getRi31());
                        data.add(item);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                adapter = new SimpleAdapter(KaoqinActivity.this, data, R.layout.jiaowu_kaoqin_row, new String[]{"s_name", "nian", "yue", "ri1", "ri2", "ri3","ri4","ri5","ri6","ri7","ri8","ri9","ri10","ri11","ri12","ri13","ri14","ri15","ri16","ri17","ri18","ri19","ri20","ri21","ri22","ri23","ri24","ri25","ri26","ri27","ri28","ri29","ri30","ri31"}, new int[]{R.id.s_name, R.id.nian, R.id.yue, R.id.ri1, R.id.ri2, R.id.ri3, R.id.ri4,R.id.ri5, R.id.ri6, R.id.ri7, R.id.ri8, R.id.ri9, R.id.ri10, R.id.ri11, R.id.ri12, R.id.ri13, R.id.ri14, R.id.ri15, R.id.ri16, R.id.ri17, R.id.ri18, R.id.ri19, R.id.ri20, R.id.ri21, R.id.ri22, R.id.ri23, R.id.ri24, R.id.ri25, R.id.ri26, R.id.ri27, R.id.ri28, R.id.ri29, R.id.ri30, R.id.ri31}) {
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

                adapter_block = new SimpleAdapter(KaoqinActivity.this, data, R.layout.jiaowu_kaoqin_row_block, new String[]{"s_name", "nian", "yue", "ri1", "ri2", "ri3","ri4","ri5","ri6","ri7","ri8","ri9","ri10","ri11","ri12","ri13","ri14","ri15","ri16","ri17","ri18","ri19","ri20","ri21","ri22","ri23","ri24","ri25","ri26","ri27","ri28","ri29","ri30","ri31"}, new int[]{R.id.s_name, R.id.nian, R.id.yue, R.id.ri1, R.id.ri2, R.id.ri3, R.id.ri4,R.id.ri5, R.id.ri6, R.id.ri7, R.id.ri8, R.id.ri9, R.id.ri10, R.id.ri11, R.id.ri12, R.id.ri13, R.id.ri14, R.id.ri15, R.id.ri16, R.id.ri17, R.id.ri18, R.id.ri19, R.id.ri20, R.id.ri21, R.id.ri22, R.id.ri23, R.id.ri24, R.id.ri25, R.id.ri26, R.id.ri27, R.id.ri28, R.id.ri29, R.id.ri30, R.id.ri31}) {
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

    public void onInsertClick(View v) {
        if(!quanxian.getAdd().equals("√")){
            ToastUtil.show(KaoqinActivity.this, "无权限！");
            return;
        }
        Intent intent = new Intent(KaoqinActivity.this, KaoqinChangeActivity.class);
        intent.putExtra("type", R.id.insert_btn);
        startActivityForResult(intent, REQUEST_CODE_CHANG);
    }

    public View.OnClickListener updateClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!quanxian.getUpd().equals("√")){
                    ToastUtil.show(KaoqinActivity.this, "无权限！");
                    return;
                }
                int position = Integer.parseInt(view.getTag().toString());
                Intent intent = new Intent(KaoqinActivity.this, KaoqinChangeActivity.class);
                intent.putExtra("type", R.id.update_btn);
                MyApplication myApplication = (MyApplication) getApplication();
                myApplication.setObj(list.get(position));
                startActivityForResult(intent, REQUEST_CODE_CHANG);
            }
        };
    }

    public View.OnLongClickListener onItemLongClick() {
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(!quanxian.getDel().equals("√")){
                    ToastUtil.show(KaoqinActivity.this, "无权限！");
                    return true;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(KaoqinActivity.this);
                int position = Integer.parseInt(view.getTag().toString());
                Handler deleteHandler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message msg) {
                        if ((boolean) msg.obj) {
                            ToastUtil.show(KaoqinActivity.this, "删除成功");
                            initList();
                        }
                        return true;
                    }
                });

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Message msg = new Message();
                                msg.obj = kaoqinService.delete(list.get(position).getId());
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
