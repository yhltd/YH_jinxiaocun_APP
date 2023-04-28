package com.example.myapplication.scheduling.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.scheduling.entity.ModuleInfo;
import com.example.myapplication.scheduling.entity.ModuleType;
import com.example.myapplication.scheduling.entity.UserInfo;
import com.example.myapplication.scheduling.service.ModuleInfoService;
import com.example.myapplication.scheduling.service.ModuleTypeService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class ModuleChangeActivity extends AppCompatActivity {
    private UserInfo userInfo;
    private ModuleInfo moduleInfo;
    private ModuleInfoService moduleInfoService;
    private ModuleTypeService moduleTypeService;

    private Spinner type;
    private EditText name;
    private EditText num;
    private Spinner parent;

    private int type_id;
    private int parent_id;
    private List<Integer> type_idList;
    private List<Integer> parent_idList;
    private List<String> typeList;
    private List<String> parentList;

    SpinnerAdapter typeAdapter;
    SpinnerAdapter parentAdapter;

    private List<ModuleType> moduleTypeList;
    private List<ModuleInfo> moduleInfoList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_change);

        type_idList = new ArrayList<>();
        typeList = new ArrayList<>();
        parentList = new ArrayList<>();
        parent_idList = new ArrayList<>();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        userInfo = myApplication.getUserInfo();

        type = findViewById(R.id.type);
        name = findViewById(R.id.name);
        num = findViewById(R.id.num);
        parent = findViewById(R.id.parent);

        init();
        //init2();

        Intent intent = getIntent();
        int id = intent.getIntExtra("type", 0);
        if (id == R.id.insert_btn) {
            moduleInfo = new ModuleInfo();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
        } else if (id == R.id.update_btn) {
            moduleInfo = (ModuleInfo) myApplication.getObj();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);

            type.setSelection(getTypePosition(moduleInfo.getType()));
            name.setText(moduleInfo.getName());
            String num_text = moduleInfo.getNum() + "";
            num.setText(num_text);
            parent.setSelection(getParentPosition(moduleInfo.getParent()));
        }
        type.setOnItemSelectedListener(new typeSelectedListener());
        parent.setOnItemSelectedListener(new parentSelectedListener());
    }


    public void clearClick(View v) {
        name.setText("");
        num.setText("");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void init() {
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                type.setAdapter(typeAdapter);
                parent.setAdapter(parentAdapter);
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
                    typeList = new ArrayList<>();
                    for (ModuleType moduleType : moduleTypeList) {
                        typeList.add(moduleType.getName());
                        type_idList.add(moduleType.getId());
                    }
                    typeAdapter = new ArrayAdapter<String>(ModuleChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, typeList);

                    moduleInfoService = new ModuleInfoService();
                    moduleInfoList = moduleInfoService.getList(userInfo.getCompany(), "全部");
                    parentList.add("");
                    parent_idList.add(0);
                    for (ModuleInfo moduleInfo : moduleInfoList) {
                        parentList.add(moduleInfo.getName());
                        parent_idList.add(moduleInfo.getId());
                    }
                    parentAdapter = new ArrayAdapter<String>(ModuleChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, parentList);

                    listLoadHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    public void insertClick(View v) {
        if (!checkForm()) return;
        LoadingDialog.getInstance(this).show();
        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(ModuleChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(ModuleChangeActivity.this, "保存失败，请稍后再试");
                }
                LoadingDialog.getInstance(getApplicationContext()).dismiss();
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = moduleInfoService.insert(moduleInfo);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    public void updateClick(View v) {
        if (!checkForm()) return;
        LoadingDialog.getInstance(this).show();
        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(ModuleChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(ModuleChangeActivity.this, "保存失败，请稍后再试");
                }
                LoadingDialog.getInstance(getApplicationContext()).dismiss();
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = moduleInfoService.update(moduleInfo);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    private boolean checkForm() {
        moduleInfo.setType_id(type_id);

        if (name.getText().toString().equals("")) {
            ToastUtil.show(ModuleChangeActivity.this, "请输入名称");
            return false;
        } else {
            moduleInfo.setName(name.getText().toString());
        }

        if (num.getText().toString().equals("")) {
            ToastUtil.show(ModuleChangeActivity.this, "请输入效率");
            return false;
        } else {
            moduleInfo.setNum(Double.parseDouble(num.getText().toString()));
        }
        moduleInfo.setParent_id(parent_id);
        moduleInfo.setCompany(userInfo.getCompany());
        return true;
    }

    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }

    private int getTypePosition(String param) {
        if (typeList != null) {
            for (int i = 0; i < typeList.size(); i++) {
                if (param.equals(typeList.get(i))) {
                    return i;
                }
            }
        }
        return 0;
    }

    private int getParentPosition(String param) {
        if (parentList != null) {
            for (int i = 0; i < parentList.size(); i++) {
                if (param.equals(parentList.get(i))) {
                    return i;
                }
            }
        }
        return 0;
    }

    private class typeSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //获取选择的项的值
            type_id = type_idList.get(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class parentSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //获取选择的项的值
            parent_id = parent_idList.get(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}
