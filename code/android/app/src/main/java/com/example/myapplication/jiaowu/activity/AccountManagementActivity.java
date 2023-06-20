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
import com.example.myapplication.jiaowu.entity.Quanxian;
import com.example.myapplication.jiaowu.entity.SheZhi;
import com.example.myapplication.jiaowu.entity.Teacher;
import com.example.myapplication.jiaowu.service.AccountManagementService;
import com.example.myapplication.jiaowu.service.SheZhiService;
import com.example.myapplication.scheduling.activity.BomActivity;
import com.example.myapplication.utils.ExcelUtil;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AccountManagementActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_CHANG = 1;
    private Teacher teacher;
    private AccountManagement accountManagement;
    private AccountManagementService accountManagementService;
    private ListView listView;

    private ListView listView_block;
    private HorizontalScrollView list_table;
    private SimpleAdapter adapter;
    private SimpleAdapter adapter_block;

    private EditText uname;
    private EditText realname;
    private EditText phone;
    private String unameText;
    private String realnameText;
    private String phoneText;
    private Button sel_button;
    private Button export_button;
    private Quanxian quanxian;

    List<AccountManagement> list;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accountmanagement);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        listView = findViewById(R.id.accountManagement_list);
        listView_block = findViewById(R.id.list_block);
        list_table = findViewById(R.id.list_table);

        uname = findViewById(R.id.uname);
        realname = findViewById(R.id.realname);
        phone = findViewById(R.id.phone);

        MyApplication myApplication = (MyApplication) getApplication();
        teacher = myApplication.getTeacher();
        quanxian = myApplication.getQuanxian();

        sel_button = findViewById(R.id.sel_button);
        export_button = findViewById(R.id.export_button);
        sel_button.setOnClickListener(selClick());
        export_button.setOnClickListener(exportClick());
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
        unameText = uname.getText().toString();
        realnameText = realname.getText().toString();
        phoneText = phone.getText().toString();

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
                    accountManagementService = new AccountManagementService();
                    list = accountManagementService.getList(unameText,realnameText,phoneText, teacher.getCompany());
                    if (list == null) return;

                    for (int i = 0; i < list.size(); i++) {
                        HashMap<String, Object> item = new HashMap<>();
                        item.put("username", list.get(i).getUsername());
                        item.put("password", list.get(i).getPassword());
                        item.put("realname", list.get(i).getRealname());
                        item.put("useType", list.get(i).getUsetype());
                        item.put("age", list.get(i).getAge());
                        item.put("phone", list.get(i).getPhone());
                        item.put("home", list.get(i).getHome());
                        item.put("photo", list.get(i).getPhoto());
                        item.put("education", list.get(i).getEducation());
                        item.put("state", list.get(i).getState());
                        data.add(item);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                adapter = new SimpleAdapter(AccountManagementActivity.this, data, R.layout.accountmanagement_row, new String[]{"username", "password", "realname", "useType", "age", "phone","home","photo","education","state"}, new int[]{R.id.username, R.id.password, R.id.realName, R.id.useType, R.id.age, R.id.phone, R.id.home,R.id.photo, R.id.education, R.id.state}) {
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

                adapter_block = new SimpleAdapter(AccountManagementActivity.this, data, R.layout.accountmanagement_row_block, new String[]{"username", "password", "realname", "useType", "age", "phone","home","photo","education","state"}, new int[]{R.id.username, R.id.password, R.id.realName, R.id.useType, R.id.age, R.id.phone, R.id.home,R.id.photo, R.id.education, R.id.state}) {
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

    public View.OnClickListener exportClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] title = {"登录名", "密码", "姓名", "用户类别", "年龄", "电话", "家庭住址", "身份证号", "学历", "状态"};
                String fileName = "用户管理" + System.currentTimeMillis() + ".xls";
                ExcelUtil.initExcel(fileName, "用户管理", title);
                ExcelUtil.jiaowu_zhanghaoToExcel(list, fileName, MyApplication.getContext());
            }
        };
    }

    public void onInsertClick(View v) {
        if(!quanxian.getAdd().equals("√")){
            ToastUtil.show(AccountManagementActivity.this, "无权限！");
            return;
        }
        Intent intent = new Intent(AccountManagementActivity.this, AccountManagementChangeActivity.class);
        intent.putExtra("type", R.id.insert_btn);
        startActivityForResult(intent, REQUEST_CODE_CHANG);
    }

    public View.OnClickListener updateClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!quanxian.getUpd().equals("√")){
                    ToastUtil.show(AccountManagementActivity.this, "无权限！");
                    return;
                }
                int position = Integer.parseInt(view.getTag().toString());
                Intent intent = new Intent(AccountManagementActivity.this, AccountManagementChangeActivity.class);
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
                    ToastUtil.show(AccountManagementActivity.this, "无权限！");
                    return true;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(AccountManagementActivity.this);
                int position = Integer.parseInt(view.getTag().toString());
                Handler deleteHandler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message msg) {
                        if ((boolean) msg.obj) {
                            ToastUtil.show(AccountManagementActivity.this, "删除成功");
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
                                msg.obj = accountManagementService.delete(list.get(position).getId());
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
