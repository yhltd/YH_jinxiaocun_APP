package com.example.myapplication.scheduling.activity;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.jxc.activity.BiJiActivity;
import com.example.myapplication.jxc.activity.BiJiChangeActivity;
import com.example.myapplication.jxc.service.YhJinXiaoCunZhengLiService;
import com.example.myapplication.scheduling.entity.BomInfo;
import com.example.myapplication.scheduling.entity.Department;
import com.example.myapplication.scheduling.entity.UserInfo;
import com.example.myapplication.scheduling.service.BomInfoService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BomActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_CHANG = 1;
    private UserInfo userInfo;
    private Department department;
    private BomInfoService bomInfoService;

    private EditText code_text;
    private EditText name_text;
    private EditText type_text;

    private ListView listView;
    private Button sel_button;

    List<BomInfo> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bom);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        listView = findViewById(R.id.bom_list);
        code_text = findViewById(R.id.code_text);
        name_text = findViewById(R.id.name_text);
        type_text = findViewById(R.id.type_text);
        sel_button = findViewById(R.id.sel_button);

        MyApplication myApplication = (MyApplication) getApplication();
        userInfo = myApplication.getUserInfo();
        department = myApplication.getPcDepartment();

        initList();
        sel_button.setOnClickListener(selClick());
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
                    bomInfoService = new BomInfoService();
                    list = bomInfoService.getList(userInfo.getCompany(), code_text.getText().toString(), name_text.getText().toString(), type_text.getText().toString());
                    if (list == null) return;

                    for (int i = 0; i < list.size(); i++) {
                        HashMap<String, Object> item = new HashMap<>();
                        item.put("code", list.get(i).getCode());
                        item.put("name", list.get(i).getName());
                        item.put("type", list.get(i).getType());
                        item.put("norms", list.get(i).getNorms());
                        item.put("comment", list.get(i).getComment());
                        item.put("size", list.get(i).getSize());
                        item.put("unit", list.get(i).getUnit());
                        item.put("use_num", list.get(i).getUse_num());
                        data.add(item);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                SimpleAdapter adapter = new SimpleAdapter(BomActivity.this, data, R.layout.bom_row, new String[]{"code", "name", "type", "norms", "comment", "size", "unit", "use_num"}, new int[]{R.id.code, R.id.name, R.id.type, R.id.norms, R.id.comment, R.id.size, R.id.unit, R.id.use_num}) {
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

    public View.OnClickListener selClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initList();
            }
        };
    }

    public void onInsertClick(View v) {
        if(!department.getAdd().equals("是")){
            ToastUtil.show(BomActivity.this, "无权限！");
            return;
        }
        Intent intent = new Intent(BomActivity.this, BomChangeActivity.class);
        intent.putExtra("type", R.id.insert_btn);
        startActivityForResult(intent, REQUEST_CODE_CHANG);
    }

    public View.OnClickListener updateClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!department.getUpd().equals("是")){
                    ToastUtil.show(BomActivity.this, "无权限！");
                    return;
                }
                int position = Integer.parseInt(view.getTag().toString());
                Intent intent = new Intent(BomActivity.this, BomChangeActivity.class);
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
                if(!department.getDel().equals("是")){
                    ToastUtil.show(BomActivity.this, "无权限！");
                    return true;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(BomActivity.this);
                int position = Integer.parseInt(view.getTag().toString());
                Handler deleteHandler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message msg) {
                        if ((boolean) msg.obj) {
                            ToastUtil.show(BomActivity.this, "删除成功");
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
                                msg.obj = bomInfoService.delete(list.get(position).getId());
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
