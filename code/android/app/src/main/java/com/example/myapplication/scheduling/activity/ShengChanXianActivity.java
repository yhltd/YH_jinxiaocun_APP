package com.example.myapplication.scheduling.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.scheduling.entity.Department;
import com.example.myapplication.scheduling.entity.ShengChanXian;
import com.example.myapplication.scheduling.entity.UserInfo;
import com.example.myapplication.scheduling.service.ShengChanXianService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShengChanXianActivity extends AppCompatActivity {

    private UserInfo userInfo;
    private Department department;
    private ShengChanXianService shengChanXianService;

    private EditText gongxuEt;
    private EditText mingchengEt;
    private EditText xiaolvEt;
    private Button queryBtn;
    private Button refreshBtn;
    private Button addBtn;
    private ListView dataList;
    private ProgressBar loadingPb;

    private List<ShengChanXian> list;
    private SimpleAdapter adapter;
    private LoadingDialog loadingDialog;

    // 查询条件
    private String gongxu = "";
    private String mingcheng = "";
    private String xiaolv = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shengchanxian);

        getSupportActionBar().setTitle("生产线管理");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();
        initData();
        loadData();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void initViews() {
        gongxuEt = findViewById(R.id.gongxu_et);
        mingchengEt = findViewById(R.id.mingcheng_et);
        xiaolvEt = findViewById(R.id.xiaolv_et);
        queryBtn = findViewById(R.id.query_btn);
        refreshBtn = findViewById(R.id.refresh_btn);
        addBtn = findViewById(R.id.add_btn);
        dataList = findViewById(R.id.data_list);
        loadingPb = findViewById(R.id.loading_pb);

        queryBtn.setOnClickListener(v -> queryData());
        refreshBtn.setOnClickListener(v -> refreshData());
        addBtn.setOnClickListener(v -> showAddDialog());
    }

    private void initData() {
        MyApplication myApplication = (MyApplication) getApplication();
        userInfo = myApplication.getUserInfo();
        department = myApplication.getPcDepartment();

        loadingDialog = LoadingDialog.create(this);
        shengChanXianService = new ShengChanXianService();
    }

    private void loadData() {
        loadingPb.setVisibility(View.VISIBLE);

        new Thread(() -> {
            try {
                // 获取所有数据（不带分页）
                list = shengChanXianService.getList(userInfo.getCompany(),
                        gongxu, mingcheng, xiaolv);

                runOnUiThread(() -> {
                    updateDataList();
                    loadingPb.setVisibility(View.GONE);
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    loadingPb.setVisibility(View.GONE);
                    ToastUtil.show(this, "加载数据失败");
                });
            }
        }).start();
    }

    private void updateDataList() {
        List<Map<String, Object>> data = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            ShengChanXian item = list.get(i);
            Map<String, Object> map = new HashMap<>();
            map.put("row_num", i + 1);  // 直接使用行号，不需要页码计算
            map.put("gongxu", item.getGongxu() != null ? item.getGongxu() : "");
            map.put("mingcheng", item.getMingcheng() != null ? item.getMingcheng() : "");
            map.put("xiaolv", item.getXiaolv() != null ? item.getXiaolv() : "");
            data.add(map);
        }

        adapter = new SimpleAdapter(this, data, R.layout.shengchanxian_row,
                new String[]{"row_num", "gongxu", "mingcheng", "xiaolv"},
                new int[]{R.id.row_num, R.id.gongxu_tv, R.id.mingcheng_tv, R.id.xiaolv_tv}) {

            @Override
            public View getView(int position, View convertView, android.view.ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                Button editBtn = view.findViewById(R.id.edit_btn);
                Button deleteBtn = view.findViewById(R.id.delete_btn);

                editBtn.setTag(position);
                deleteBtn.setTag(position);

                editBtn.setOnClickListener(v -> {
                    int pos = (int) v.getTag();
                    showEditDialog(list.get(pos));
                });

                deleteBtn.setOnClickListener(v -> {
                    int pos = (int) v.getTag();
                    deleteShengChanXian(list.get(pos));
                });

                return view;
            }
        };

        dataList.setAdapter(adapter);

        // 显示数据条数
        ToastUtil.show(this, "共加载 " + list.size() + " 条记录");
    }

    private void queryData() {
        gongxu = gongxuEt.getText().toString().trim();
        mingcheng = mingchengEt.getText().toString().trim();
        xiaolv = xiaolvEt.getText().toString().trim();
        loadData();
    }

    private void refreshData() {
        gongxuEt.setText("");
        mingchengEt.setText("");
        xiaolvEt.setText("");
        gongxu = "";
        mingcheng = "";
        xiaolv = "";
        loadData();
    }

    private void showAddDialog() {
        if (!department.getAdd().equals("是")) {
            ToastUtil.show(this, "无权限！");
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("新增生产线");

        View dialogView = getLayoutInflater().inflate(R.layout.shengchanxian_edit_dialog, null);
        builder.setView(dialogView);

        EditText gongxuDialogEt = dialogView.findViewById(R.id.gongxu_et);
        EditText mingchengDialogEt = dialogView.findViewById(R.id.mingcheng_et);
        EditText xiaolvDialogEt = dialogView.findViewById(R.id.xiaolv_et);

        builder.setPositiveButton("保存", null);
        builder.setNegativeButton("取消", null);

        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v -> {
            String gongxuInput = gongxuDialogEt.getText().toString().trim();
            String mingchengInput = mingchengDialogEt.getText().toString().trim();
            String xiaolvInput = xiaolvDialogEt.getText().toString().trim();

            if (mingchengInput.isEmpty()) {
                ToastUtil.show(this, "生产线名称不能为空");
                return;
            }

            if (gongxuInput.isEmpty()) {
                ToastUtil.show(this, "工序不能为空");
                return;
            }

            if (mingchengInput.length() > 100) {
                ToastUtil.show(this, "生产线名称长度不能超过100个字符");
                return;
            }

            if (gongxuInput.length() > 100) {
                ToastUtil.show(this, "工序长度不能超过100个字符");
                return;
            }

            if (xiaolvInput.length() > 100) {
                ToastUtil.show(this, "效率长度不能超过100个字符");
                return;
            }

            ShengChanXian shengChanXian = new ShengChanXian();
            shengChanXian.setGongxu(gongxuInput);
            shengChanXian.setMingcheng(mingchengInput);
            shengChanXian.setXiaolv(xiaolvInput);
            shengChanXian.setGongsi(userInfo.getCompany());

            saveShengChanXian(shengChanXian);
            dialog.dismiss();
        });
    }

    private void showEditDialog(ShengChanXian shengChanXian) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("编辑生产线");

        View dialogView = getLayoutInflater().inflate(R.layout.shengchanxian_edit_dialog, null);
        builder.setView(dialogView);

        EditText gongxuDialogEt = dialogView.findViewById(R.id.gongxu_et);
        EditText mingchengDialogEt = dialogView.findViewById(R.id.mingcheng_et);
        EditText xiaolvDialogEt = dialogView.findViewById(R.id.xiaolv_et);

        // 设置原有数据
        gongxuDialogEt.setText(shengChanXian.getGongxu());
        mingchengDialogEt.setText(shengChanXian.getMingcheng());
        xiaolvDialogEt.setText(shengChanXian.getXiaolv());

        builder.setPositiveButton("保存", null);
        builder.setNegativeButton("取消", null);

        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v -> {
            String gongxuInput = gongxuDialogEt.getText().toString().trim();
            String mingchengInput = mingchengDialogEt.getText().toString().trim();
            String xiaolvInput = xiaolvDialogEt.getText().toString().trim();

            if (mingchengInput.isEmpty()) {
                ToastUtil.show(this, "生产线名称不能为空");
                return;
            }

            if (gongxuInput.isEmpty()) {
                ToastUtil.show(this, "工序不能为空");
                return;
            }

            if (mingchengInput.length() > 100) {
                ToastUtil.show(this, "生产线名称长度不能超过100个字符");
                return;
            }

            if (gongxuInput.length() > 100) {
                ToastUtil.show(this, "工序长度不能超过100个字符");
                return;
            }

            if (xiaolvInput.length() > 100) {
                ToastUtil.show(this, "效率长度不能超过100个字符");
                return;
            }

            shengChanXian.setGongxu(gongxuInput);
            shengChanXian.setMingcheng(mingchengInput);
            shengChanXian.setXiaolv(xiaolvInput);

            updateShengChanXian(shengChanXian);
            dialog.dismiss();
        });
    }

    private void saveShengChanXian(ShengChanXian shengChanXian) {

        new Thread(() -> {
            try {
                boolean success = shengChanXianService.insert(shengChanXian);

                runOnUiThread(() -> {
                    loadingDialog.dismiss();
                    if (success) {
                        ToastUtil.show(this, "保存成功");
                        loadData();
                    } else {
                        ToastUtil.show(this, "保存失败");
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    loadingDialog.dismiss();
                    ToastUtil.show(this, "保存失败：" + e.getMessage());
                });
            }
        }).start();
    }

    private void updateShengChanXian(ShengChanXian shengChanXian) {

        new Thread(() -> {
            try {
                boolean success = shengChanXianService.update(shengChanXian);

                runOnUiThread(() -> {
                    loadingDialog.dismiss();
                    if (success) {
                        ToastUtil.show(this, "更新成功");
                        loadData();
                    } else {
                        ToastUtil.show(this, "更新失败");
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    loadingDialog.dismiss();
                    ToastUtil.show(this, "更新失败：" + e.getMessage());
                });
            }
        }).start();
    }

    private void deleteShengChanXian(ShengChanXian shengChanXian) {
        new AlertDialog.Builder(this)
                .setTitle("确认删除")
                .setMessage("确定要删除这条生产线吗？")
                .setPositiveButton("确定", (dialog, which) -> {

                    new Thread(() -> {
                        try {
                            boolean success = shengChanXianService.delete(shengChanXian.getId(),
                                    userInfo.getCompany());

                            runOnUiThread(() -> {
                                loadingDialog.dismiss();
                                if (success) {
                                    ToastUtil.show(this, "删除成功");
                                    loadData();
                                } else {
                                    ToastUtil.show(this, "删除失败");
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                            runOnUiThread(() -> {
                                loadingDialog.dismiss();
                                ToastUtil.show(this, "删除失败：" + e.getMessage());
                            });
                        }
                    }).start();
                })
                .setNegativeButton("取消", null)
                .show();
    }
}