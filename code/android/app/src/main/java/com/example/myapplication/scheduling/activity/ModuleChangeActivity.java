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

        // 设置监听器（先设置，不影响）
        type.setOnItemSelectedListener(new typeSelectedListener());
        parent.setOnItemSelectedListener(new parentSelectedListener());

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

            // 先设置EditText的值（这些可以立即生效）
            name.setText(moduleInfo.getName());
            String num_text = moduleInfo.getNum() + "";
            num.setText(num_text);

            // 注意：现在不在这里设置下拉框选中项
            // type.setSelection(getTypePosition(moduleInfo.getType())); // 删除这行
            // parent.setSelection(getParentPosition(moduleInfo.getParent())); // 删除这行
        }

        // 最后调用init，并在init的Handler中处理下拉框设置
        init();
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
                // 1. 先设置Adapter
                type.setAdapter(typeAdapter);
                parent.setAdapter(parentAdapter);

                // 2. 在Adapter设置完成后，再设置下拉框选中项
                setSpinnerSelections();
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
                    type_idList = new ArrayList<>(); // 确保初始化
                    for (ModuleType moduleType : moduleTypeList) {
                        typeList.add(moduleType.getName());
                        type_idList.add(moduleType.getId());
                    }
                    typeAdapter = new ArrayAdapter<String>(ModuleChangeActivity.this,
                            android.R.layout.simple_spinner_dropdown_item, typeList);

                    moduleInfoService = new ModuleInfoService();
                    moduleInfoList = moduleInfoService.getList(userInfo.getCompany(), "全部");

                    parentList = new ArrayList<>(); // 确保重新初始化，避免重复添加
                    parent_idList = new ArrayList<>();
                    parentList.add("");
                    parent_idList.add(0);
                    for (ModuleInfo moduleInfo : moduleInfoList) {
                        parentList.add(moduleInfo.getName());
                        parent_idList.add(moduleInfo.getId());
                    }
                    parentAdapter = new ArrayAdapter<String>(ModuleChangeActivity.this,
                            android.R.layout.simple_spinner_dropdown_item, parentList);

                    listLoadHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // 3. 添加设置下拉框选中项的方法
    private void setSpinnerSelections() {
        if (moduleInfo != null) {
            // 如果是更新操作，设置type下拉框选中项
            int typePosition = getTypePosition(moduleInfo.getType());
            if (typePosition >= 0 && typePosition < typeAdapter.getCount()) {
                type.setSelection(typePosition, false); // false表示不触发onItemSelected
            } else {
                type.setSelection(0, false); // 默认选中第一项
            }

            // 设置parent下拉框选中项
            int parentPosition = getParentPosition(moduleInfo.getParent());
            if (parentPosition >= 0 && parentPosition < parentAdapter.getCount()) {
                parent.setSelection(parentPosition, false);
            } else {
                parent.setSelection(0, false); // 默认选中第一项（空项）
            }
        } else {
            // 如果是新增操作，设置默认选中项
            type.setSelection(0, false);
            parent.setSelection(0, false);
        }
    }


    public void insertClick(View v) {
        if (!checkForm()) return;

        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(ModuleChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(ModuleChangeActivity.this, "保存失败，请稍后再试");
                }

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

        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(ModuleChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(ModuleChangeActivity.this, "保存失败，请稍后再试");
                }

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
        if (param == null || typeList == null || typeList.isEmpty()) {
            return 0;
        }
        for (int i = 0; i < typeList.size(); i++) {
            if (param.equals(typeList.get(i))) {
                return i;
            }
        }
        return 0; // 没找到返回第一项
    }


    private int getParentPosition(String param) {
        if (param == null || parentList == null || parentList.isEmpty()) {
            return 0;
        }
        for (int i = 0; i < parentList.size(); i++) {
            if (param.equals(parentList.get(i))) {
                return i;
            }
        }
        return 0; // 没找到返回第一项（空项）
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
