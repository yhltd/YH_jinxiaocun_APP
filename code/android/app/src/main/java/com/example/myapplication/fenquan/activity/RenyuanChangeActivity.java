package com.example.myapplication.fenquan.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
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
import com.example.myapplication.fenquan.entity.Department;
import com.example.myapplication.fenquan.entity.Renyuan;
import com.example.myapplication.fenquan.service.Copy1Service;
import com.example.myapplication.fenquan.service.DepartmentService;
import com.example.myapplication.fenquan.service.RenyuanService;
import com.example.myapplication.jxc.activity.BiJiChangeActivity;
import com.example.myapplication.jxc.entity.YhJinXiaoCunZhengLi;
import com.example.myapplication.renshi.activity.GongZiMingXiChangeActivity;
import com.example.myapplication.renshi.entity.YhRenShiGongZiMingXi;
import com.example.myapplication.renshi.entity.YhRenShiPeiZhiBiao;
import com.example.myapplication.renshi.entity.YhRenShiUser;
import com.example.myapplication.renshi.service.YhRenShiPeiZhiBiaoService;
import com.example.myapplication.renshi.service.YhRenShiUserService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RenyuanChangeActivity extends AppCompatActivity {
    private Renyuan renyuan;
    private Renyuan ry;
    private RenyuanService renyuanService;
    private DepartmentService departmentService;
    private Copy1Service copy1Service;

    private EditText c;
    private EditText d;
    private EditText e;
    private Spinner bumen;
    private Spinner zhuangtai;
    private EditText email;
    private EditText phone;
    private EditText bianhao;

    List<String> department_array;
    String[] zhanghao_typeArray;

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.renyuan_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        renyuanService = new RenyuanService();
        copy1Service = new Copy1Service();

        MyApplication myApplication = (MyApplication) getApplication();
        renyuan = myApplication.getRenyuan();

        c = findViewById(R.id.c);
        d = findViewById(R.id.d);
        e = findViewById(R.id.e);
        bumen = findViewById(R.id.bumen);
        zhuangtai = findViewById(R.id.zhuangtai);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        bianhao = findViewById(R.id.bianhao);

        zhanghao_typeArray = getResources().getStringArray(R.array.zhanghao_type_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, zhanghao_typeArray);
        zhuangtai.setAdapter(adapter);
        init_select();

    }

    public void init_select() {
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                SpinnerAdapter adapter = new ArrayAdapter<String>(RenyuanChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, department_array);
                bumen.setAdapter(StringUtils.cast(adapter));

                Intent intent = getIntent();
                MyApplication myApplication = (MyApplication) getApplication();
                int id = intent.getIntExtra("type", 0);
                if (id == R.id.insert_btn) {
                    ry = new Renyuan();
                    Button btn = findViewById(id);
                    btn.setVisibility(View.VISIBLE);
                } else if (id == R.id.update_btn) {
                    ry = (Renyuan) myApplication.getObj();
                    Button btn = findViewById(id);
                    btn.setVisibility(View.VISIBLE);
                    c.setText(ry.getC());
                    d.setText(ry.getD());
                    e.setText(ry.getE());
                    email.setText(ry.getEmail());
                    phone.setText(ry.getPhone());
                    bianhao.setText(ry.getBianhao());
                    bumen.setSelection(getDepartmentPosition(ry.getBumen()));

                    if (ry.getZhuangtai().equals("正常")) {
                        zhuangtai.setSelection(0);
                    } else {
                        zhuangtai.setSelection(1);
                    }

                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                SpinnerAdapter adapter = null;
                try {
                    departmentService = new DepartmentService();
                    List<Department> departmentList = departmentService.getDepartment(renyuan.getB());
                    department_array = new ArrayList<>();
                    if (departmentList.size() > 0) {
                        for (int i = 0; i < departmentList.size(); i++) {
                            if(!departmentList.get(i).getDepartment_name().equals(""))
                                department_array.add(departmentList.get(i).getDepartment_name());
                        }
                    }

                    adapter = new ArrayAdapter<String>(RenyuanChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, department_array);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.obj = adapter;
                listLoadHandler.sendMessage(msg);
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
                    ToastUtil.show(RenyuanChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(RenyuanChangeActivity.this, "保存失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                copy1Service.insert(ry.getB(),ry.getC(),ry.getRenyuan_id(),"查询");
                copy1Service.insert(ry.getB(),ry.getC(),ry.getRenyuan_id(),"修改");
                msg.obj = renyuanService.insert(ry);
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
                    ToastUtil.show(RenyuanChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(RenyuanChangeActivity.this, "保存失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = renyuanService.update(ry);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    private boolean checkForm() {
        if (c.getText().toString().equals("")) {
            ToastUtil.show(RenyuanChangeActivity.this, "请输入姓名");
            return false;
        } else {
            ry.setC(c.getText().toString());
        }

        if (d.getText().toString().equals("")) {
            ToastUtil.show(RenyuanChangeActivity.this, "请输入账号");
            return false;
        } else {
            ry.setD(d.getText().toString());
        }

        if (e.getText().toString().equals("")) {
            ToastUtil.show(RenyuanChangeActivity.this, "请输入密码");
            return false;
        } else {
            ry.setE(e.getText().toString());
        }

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat spd = new SimpleDateFormat("yyyyMMddhhmmss");
        Date date = new Date();
        ry.setBumen(bumen.getSelectedItem().toString());
        ry.setZhuangtai(zhuangtai.getSelectedItem().toString());
        ry.setEmail(email.getText().toString());
        ry.setPhone(phone.getText().toString());
        ry.setBianhao(bianhao.getText().toString());
        ry.setRenyuan_id(spd.format(date));
        ry.setB(renyuan.getB());
        return true;
    }

    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }

    private int getDepartmentPosition(String param) {
        if (department_array != null) {
            for (int i = 0; i < department_array.size(); i++) {
                if (param.equals(department_array.get(i))) {
                    return i;
                }
            }
        }
        return 0;
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

}
