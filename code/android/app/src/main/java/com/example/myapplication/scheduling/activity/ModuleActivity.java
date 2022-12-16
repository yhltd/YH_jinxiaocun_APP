package com.example.myapplication.scheduling.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.example.myapplication.scheduling.entity.ModuleInfo;
import com.example.myapplication.scheduling.entity.ModuleType;
import com.example.myapplication.scheduling.entity.UserInfo;
import com.example.myapplication.scheduling.service.ModuleInfoService;
import com.example.myapplication.scheduling.service.ModuleTypeService;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ModuleActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_CHANG = 1;
    private UserInfo userInfo;

    private ModuleInfoService moduleInfoService;
    private ModuleTypeService moduleTypeService;

    private Spinner type_spinner;
    private ListView listView;

    private List<ModuleInfo> list;
    private List<ModuleType> moduleTypeList;
    private List<String> spinnerList;

    private String type_text;

    private int pd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        userInfo = myApplication.getUserInfo();

        type_spinner = findViewById(R.id.type_spinner);
        listView = findViewById(R.id.module_list);

        initList();

        type_spinner.setOnItemSelectedListener(new typeSelectedListener());

        pd = 0;

    }

    private void initList2() {
        Handler kehuHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                listView.setAdapter(StringUtils.cast(msg.obj));
                return true;
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                moduleInfoService = new ModuleInfoService();
                List<HashMap<String, Object>> data = new ArrayList<>();
                try {
                    list = moduleInfoService.getList(userInfo.getCompany(), type_text);
                    if (list == null) return;

                    for (int i = 0; i < list.size(); i++) {
                        HashMap<String, Object> item = new HashMap<>();
                        item.put("type", list.get(i).getType());
                        item.put("name", list.get(i).getName());
                        item.put("num", list.get(i).getNum());
                        item.put("parent", list.get(i).getParent());
                        data.add(item);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                SimpleAdapter adapter = new SimpleAdapter(ModuleActivity.this, data, R.layout.module_row, new String[]{"type", "name", "num", "parent"}, new int[]{R.id.type, R.id.name, R.id.num, R.id.parent}) {
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
                kehuHandler.sendMessage(msg);

            }
        }).start();
    }

    private class typeSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //获取选择的项的值
            type_text = spinnerList.get(position);
            Handler kehuHandler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(@NonNull Message msg) {
                    listView.setAdapter(StringUtils.cast(msg.obj));
                    return true;
                }
            });
            new Thread(new Runnable() {
                @Override
                public void run() {
                    moduleInfoService = new ModuleInfoService();
                    List<HashMap<String, Object>> data = new ArrayList<>();
                    try {
                        list = moduleInfoService.getList(userInfo.getCompany(), type_text);
                        if (list == null) return;

                        for (int i = 0; i < list.size(); i++) {
                            HashMap<String, Object> item = new HashMap<>();
                            item.put("type", list.get(i).getType());
                            item.put("name", list.get(i).getName());
                            item.put("num", list.get(i).getNum());
                            item.put("parent", list.get(i).getParent());
                            data.add(item);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    SimpleAdapter adapter = new SimpleAdapter(ModuleActivity.this, data, R.layout.module_row, new String[]{"type", "name", "num", "parent"}, new int[]{R.id.type, R.id.name, R.id.num, R.id.parent}) {
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
                    kehuHandler.sendMessage(msg);

                }
            }).start();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private void initList() {
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                type_spinner.setAdapter(StringUtils.cast(msg.obj));
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                SpinnerAdapter adapter = null;
                try {
                    moduleTypeService = new ModuleTypeService();
                    moduleTypeList = moduleTypeService.getList(userInfo.getCompany());
                    spinnerList = new ArrayList<>();
                    spinnerList.add("全部");
                    for (ModuleType moduleType : moduleTypeList) {
                        spinnerList.add(moduleType.getName());
                    }
                    adapter = new ArrayAdapter<String>(ModuleActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerList);
                    if (spinnerList.size() > 0) {
                        msg.obj = adapter;
                    } else {
                        msg.obj = null;
                    }
                    listLoadHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }


    public View.OnLongClickListener onItemLongClick() {
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ModuleActivity.this);
                int position = Integer.parseInt(view.getTag().toString());
                Handler deleteHandler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message msg) {
                        if ((boolean) msg.obj) {
                            ToastUtil.show(ModuleActivity.this, "删除成功");
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
                                msg.obj = moduleInfoService.delete(list.get(position).getId());
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

    public void onInsertClick(View v) {
        Intent intent = new Intent(ModuleActivity.this, ModuleChangeActivity.class);
        intent.putExtra("type", R.id.insert_btn);
        startActivityForResult(intent, REQUEST_CODE_CHANG);
    }

    public void typeInsertClick(View v) {
        Intent intent = new Intent(ModuleActivity.this, ModuleTypeActivity.class);
        startActivityForResult(intent, REQUEST_CODE_CHANG);
    }

    public View.OnClickListener updateClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = Integer.parseInt(view.getTag().toString());
                Intent intent = new Intent(ModuleActivity.this, ModuleChangeActivity.class);
                intent.putExtra("type", R.id.update_btn);
                MyApplication myApplication = (MyApplication) getApplication();
                myApplication.setObj(list.get(position));
                startActivityForResult(intent, REQUEST_CODE_CHANG);
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHANG) {
            if (resultCode == RESULT_OK) {
                initList2();
            }
        }
    }
}
