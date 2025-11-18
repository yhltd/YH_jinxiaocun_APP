//package com.example.myapplication.jiaowu.activity;
//
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Spinner;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.RequiresApi;
//import androidx.appcompat.app.ActionBar;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.myapplication.MyApplication;
//import com.example.myapplication.R;
//import com.example.myapplication.jiaowu.entity.AccountManagement;
//import com.example.myapplication.jiaowu.entity.Quanxian;
//import com.example.myapplication.jiaowu.entity.Teacher;
//import com.example.myapplication.jiaowu.service.AccountManagementService;
//import com.example.myapplication.jiaowu.service.QuanxianService;
//import com.example.myapplication.utils.LoadingDialog;
//import com.example.myapplication.utils.ToastUtil;
//
//public class QuanxianChangeActivity extends AppCompatActivity {
//    private Teacher teacher;
//    private Quanxian quanxian;
//    private QuanxianService quanxianService;
//
//    private EditText Realname;
//    private EditText view_name;
//    private Spinner add;
//    private Spinner del;
//    private Spinner upd;
//    private Spinner sel;
//
//    String[] add_array;
//    String[] del_array;
//    String[] upd_array;
//    String[] sel_array;
//
//    @SuppressLint("NonConstantResourceId")
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.jiaowu_account_quanxian_change);
//
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setHomeButtonEnabled(true);
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }
//
//        MyApplication myApplication = (MyApplication) getApplication();
//        teacher = myApplication.getTeacher();
//        quanxianService = new QuanxianService();
//
//        Realname = findViewById(R.id.Realname);
//        view_name = findViewById(R.id.view_name);
//        add = findViewById(R.id.add);
//        del = findViewById(R.id.Del);
//        upd = findViewById(R.id.Upd);
//        sel = findViewById(R.id.Sel);
//
//        String[] add_selectArray = getResources().getStringArray(R.array.jiaowu_quanxian_list);
//        String[] Del_selectArray = getResources().getStringArray(R.array.jiaowu_quanxian_list);
//        String[] Upd_selectArray = getResources().getStringArray(R.array.jiaowu_quanxian_list);
//        String[] Sel_selectArray = getResources().getStringArray(R.array.jiaowu_quanxian_list);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, add_selectArray);
//        add.setAdapter(adapter);
//        del.setAdapter(adapter);
//        upd.setAdapter(adapter);
//        sel.setAdapter(adapter);
//
//        Intent intent = getIntent();
//        int id = intent.getIntExtra("type", 0);
//        if (id == R.id.insert_btn) {
//            quanxian = new Quanxian();
//            Button btn = findViewById(id);
//            btn.setVisibility(View.VISIBLE);
//        } else if (id == R.id.update_btn) {
//            quanxian = (Quanxian) myApplication.getObj();
//            Button btn = findViewById(id);
//            btn.setVisibility(View.VISIBLE);
//
//            Realname.setText(quanxian.getRealname());
//            view_name.setText(quanxian.getView_name());
////            add.setText(quanxian.getAdd());
////            del.setText(quanxian.getDel());
////            upd.setText(quanxian.getUpd());
////            sel.setText(quanxian.getSel());
//        }
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            this.finish();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    public void insertClick(View v) {
//        if (!checkForm()) return;
//
//        Handler saveHandler = new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(@NonNull Message msg) {
//                if ((boolean) msg.obj) {
//                    ToastUtil.show(QuanxianChangeActivity.this, "保存成功");
//                    back();
//                } else {
//                    ToastUtil.show(QuanxianChangeActivity.this, "保存失败，请稍后再试");
//                }
//
//                return true;
//            }
//        });
//
//        new Thread(new Runnable() {
//            @RequiresApi(api = Build.VERSION_CODES.O)
//            @Override
//            public void run() {
//                Message msg = new Message();
//                msg.obj = quanxianService.insert(quanxian);
//                saveHandler.sendMessage(msg);
//            }
//        }).start();
//    }
//
//    public void updateClick(View v) {
//        if (!checkForm()) return;
//
//        Handler saveHandler = new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(@NonNull Message msg) {
//                if ((boolean) msg.obj) {
//                    ToastUtil.show(QuanxianChangeActivity.this, "保存成功");
//                    back();
//                } else {
//                    ToastUtil.show(QuanxianChangeActivity.this, "保存失败，请稍后再试");
//                }
//
//                return true;
//            }
//        });
//
//        new Thread(new Runnable() {
//            @RequiresApi(api = Build.VERSION_CODES.O)
//            @Override
//            public void run() {
//                Message msg = new Message();
//                msg.obj = quanxianService.update(quanxian);
//                saveHandler.sendMessage(msg);
//            }
//        }).start();
//    }
//
//    private boolean checkForm() {
//
//        quanxian.setRealname(Realname.getText().toString());
//        quanxian.setView_name(view_name.getText().toString());
////        quanxian.setAdd(add.getText().toString());
////        quanxian.setDel(del.getText().toString());
////        quanxian.setUpd(upd.getText().toString());
////        quanxian.setSel(sel.getText().toString());
//        quanxian.setAdd(add.getSelectedItem().toString());
//        quanxian.setDel(del.getSelectedItem().toString());
//        quanxian.setUpd(upd.getSelectedItem().toString());
//        quanxian.setSel(sel.getSelectedItem().toString());
//        quanxian.setCompany(teacher.getCompany());
//        return true;
//    }
//
//    private void back() {
//        setResult(RESULT_OK, new Intent());
//        finish();
//    }
//}


package com.example.myapplication.jiaowu.activity;

import android.annotation.SuppressLint;
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

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.jiaowu.entity.Quanxian;
import com.example.myapplication.jiaowu.entity.Teacher;
import com.example.myapplication.jiaowu.service.QuanxianService;
import com.example.myapplication.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class QuanxianChangeActivity extends AppCompatActivity {
    private Teacher teacher;
    private Quanxian quanxian;
    private QuanxianService quanxianService;

    private Spinner Realname; // 修改为 Spinner
    private EditText view_name;
    private Spinner add;
    private Spinner del;
    private Spinner upd;
    private Spinner sel;

    private List<Teacher> teacherList = new ArrayList<>(); // 存储教师列表
    private ArrayAdapter<String> teacherAdapter; // 教师下拉框适配器
    private ArrayAdapter<String> quanxianAdapter; // 权限下拉框适配器

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jiaowu_account_quanxian_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        teacher = myApplication.getTeacher();
        quanxianService = new QuanxianService();

        // 初始化视图 - 注意 Realname 现在是 Spinner
        Realname = findViewById(R.id.Realname);
        view_name = findViewById(R.id.view_name);
        add = findViewById(R.id.add);
        del = findViewById(R.id.Del);
        upd = findViewById(R.id.Upd);
        sel = findViewById(R.id.Sel);

        // 初始化权限下拉框
        initQuanxianSpinners();

        // 初始化教师下拉框（先设为空，等数据加载后填充）
        teacherAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<String>());
        teacherAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Realname.setAdapter(teacherAdapter);

        // 设置教师下拉框选择监听器
        Realname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0 && teacherList != null && position - 1 < teacherList.size()) {
                    // 获取选中的教师对象
                    Teacher selectedTeacher = teacherList.get(position - 1);
                    // 设置 t_id
                    quanxian.setT_id(selectedTeacher.getId());
                    // 可以在界面上显示选中的教师姓名（可选）
                    ToastUtil.show(QuanxianChangeActivity.this, "已选择: " + selectedTeacher.getRealname());
                } else if (position == 0) {
                    // 选择了"请选择"选项，清空 t_id
                    quanxian.setT_id(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 什么都不选时清空 t_id
                quanxian.setT_id(0);
            }
        });

        Intent intent = getIntent();
        int id = intent.getIntExtra("type", 0);
        if (id == R.id.insert_btn) {
            quanxian = new Quanxian();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
            // 新增时加载教师数据
            loadTeacherData();
        } else if (id == R.id.update_btn) {
            quanxian = (Quanxian) myApplication.getObj();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);

            // 修改时设置已选中的值
            view_name.setText(quanxian.getView_name());
            setSpinnerSelection(add, quanxian.getAdd());
            setSpinnerSelection(del, quanxian.getDel());
            setSpinnerSelection(upd, quanxian.getUpd());
            setSpinnerSelection(sel, quanxian.getSel());

            // 修改时也需要加载教师数据，并设置当前选中的教师
            loadTeacherDataForUpdate();
        }
    }

    /**
     * 初始化权限下拉框
     */
    private void initQuanxianSpinners() {
        String[] quanxianArray = getResources().getStringArray(R.array.jiaowu_quanxian_list);
        quanxianAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, quanxianArray);
        quanxianAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        add.setAdapter(quanxianAdapter);
        del.setAdapter(quanxianAdapter);
        upd.setAdapter(quanxianAdapter);
        sel.setAdapter(quanxianAdapter);
    }

    /**
     * 设置下拉框选中项
     */
    private void setSpinnerSelection(Spinner spinner, String value) {
        if (value != null && spinner.getAdapter() != null) {
            for (int i = 0; i < spinner.getCount(); i++) {
                if (spinner.getItemAtPosition(i).toString().equals(value)) {
                    spinner.setSelection(i);
                    break;
                }
            }
        }
    }

    /**
     * 新增时加载教师数据
     */
    private void loadTeacherData() {
        Handler teacherHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                List<Teacher> teachers = (List<Teacher>) msg.obj;
                if (teachers != null && !teachers.isEmpty()) {
                    teacherList.clear();
                    teacherList.addAll(teachers);

                    // 更新下拉框数据
                    List<String> displayNames = new ArrayList<>();
                    displayNames.add("请选择教师"); // 第一项为提示
                    for (Teacher teacher : teachers) {
                        displayNames.add(teacher.getRealname());
                    }

                    teacherAdapter.clear();
                    teacherAdapter.addAll(displayNames);
                    teacherAdapter.notifyDataSetChanged();

//                    ToastUtil.show(QuanxianChangeActivity.this, "加载了 " + teachers.size() + " 位教师");
                } else {
//                    ToastUtil.show(QuanxianChangeActivity.this, "没有获取到教师数据");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = quanxianService.getXL(teacher.getCompany());
                teacherHandler.sendMessage(msg);
            }
        }).start();
    }

    /**
     * 修改时加载教师数据并设置当前选中的教师
     */
    private void loadTeacherDataForUpdate() {
        Handler teacherHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                List<Teacher> teachers = (List<Teacher>) msg.obj;
                if (teachers != null && !teachers.isEmpty()) {
                    teacherList.clear();
                    teacherList.addAll(teachers);

                    // 更新下拉框数据
                    List<String> displayNames = new ArrayList<>();
                    displayNames.add("请选择教师");
                    int selectedPosition = 0;

                    for (int i = 0; i < teachers.size(); i++) {
                        Teacher t = teachers.get(i);
                        displayNames.add(t.getRealname());
                        // 如果当前教师的ID与quanxian中的t_id匹配，记录位置
                        if (t.getId() == quanxian.getT_id()) {
                            selectedPosition = i + 1; // +1 因为第一项是"请选择"
                        }
                    }

                    teacherAdapter.clear();
                    teacherAdapter.addAll(displayNames);
                    teacherAdapter.notifyDataSetChanged();

                    // 设置选中的教师
                    if (selectedPosition > 0) {
                        Realname.setSelection(selectedPosition);
                    }

                    ToastUtil.show(QuanxianChangeActivity.this, "加载了 " + teachers.size() + " 位教师");
                } else {
                    ToastUtil.show(QuanxianChangeActivity.this, "没有获取到教师数据");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = quanxianService.getXL(teacher.getCompany());
                teacherHandler.sendMessage(msg);
            }
        }).start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void insertClick(View v) {
        if (!checkForm()) return;

        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(QuanxianChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(QuanxianChangeActivity.this, "保存失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = quanxianService.insert(quanxian);
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
                    ToastUtil.show(QuanxianChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(QuanxianChangeActivity.this, "保存失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = quanxianService.update(quanxian);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    private boolean checkForm() {
        // 验证必填字段
        if (quanxian.getT_id() == 0) {
            ToastUtil.show(this, "请选择教师");
            return false;
        }

        if (view_name.getText().toString().trim().isEmpty()) {
            ToastUtil.show(this, "请输入页面名称");
            view_name.requestFocus();
            return false;
        }

        // 设置表单数据 - 直接使用选中的符号值
        quanxian.setView_name(view_name.getText().toString());
        quanxian.setAdd(add.getSelectedItem().toString());
        quanxian.setDel(del.getSelectedItem().toString());
        quanxian.setUpd(upd.getSelectedItem().toString());
        quanxian.setSel(sel.getSelectedItem().toString());
        quanxian.setCompany(teacher.getCompany());

        return true;
    }

    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }
}
