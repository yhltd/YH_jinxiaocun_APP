package com.example.myapplication.renshi.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.renshi.entity.YhRenShiQingJiaShenPi;
import com.example.myapplication.renshi.entity.YhRenShiUser;
import com.example.myapplication.renshi.service.YhRenShiQingJiaShenPiService;
import com.example.myapplication.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class QingJiaShenPiActivity extends AppCompatActivity {

    private YhRenShiUser yhRenShiUser;
    private YhRenShiQingJiaShenPiService qingJiaShenPiService;

    private ListView listView;
    private ListView listView_block;
    private Spinner spinnerZhuangtai;
    private Button selButton;

    private List<YhRenShiQingJiaShenPi> dataList;
    private SimpleAdapter adapter;
    private SimpleAdapter adapter_block;
    private HorizontalScrollView list_table;

    private String[] zhuangtaiOptions = {"全部", "待审批", "通过", "驳回"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qingjiashenpi);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("请假审批管理");
        }

        // 初始化视图控件
        initViews();

        MyApplication myApplication = (MyApplication) getApplication();
        yhRenShiUser = myApplication.getYhRenShiUser();

        qingJiaShenPiService = new YhRenShiQingJiaShenPiService();

        initZhuangtaiSpinner();

        loadData(null);
    }

    private void initViews() {
        listView = findViewById(R.id.baopan_list);
        listView_block = findViewById(R.id.list_block);
        list_table = findViewById(R.id.list_table);
        spinnerZhuangtai = findViewById(R.id.spinner_zhuangtai);
        selButton = findViewById(R.id.sel_button);

        // 设置查询按钮点击事件
        selButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedStatus = spinnerZhuangtai.getSelectedItem().toString();
                loadData(selectedStatus.equals("全部") ? null : selectedStatus);
            }
        });

        listView_block.setVisibility(View.VISIBLE);
        list_table.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);
    }

    private void initZhuangtaiSpinner() {
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                zhuangtaiOptions
        );
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerZhuangtai.setAdapter(statusAdapter);
    }

    public void switchClick(View v) {
        if (listView_block.getVisibility() == View.VISIBLE) {
            // 隐藏块状视图，显示表格视图
            listView_block.setVisibility(View.GONE);
            list_table.setVisibility(View.VISIBLE);  // 改为显示 HorizontalScrollView

            // 确保 ListView 在表格模式中可见
            listView.setVisibility(View.VISIBLE);
        } else {
            // 隐藏表格视图，显示块状视图
            list_table.setVisibility(View.GONE);
            listView_block.setVisibility(View.VISIBLE);

            // 隐藏 ListView
            listView.setVisibility(View.GONE);
        }
    }

    private void loadData(final String filterStatus) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String company = yhRenShiUser.getL();
                    dataList = qingJiaShenPiService.getListByStatus(company, filterStatus);
                    handler.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(1);
                }
            }
        }).start();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            if (msg.what == 0) {
                updateListView();
            } else {
                ToastUtil.show(QingJiaShenPiActivity.this, "数据加载失败");
            }
        }
    };

    private void updateListView() {
        if (dataList == null || dataList.isEmpty()) {
            List<HashMap<String, Object>> emptyList = new ArrayList<>();
            HashMap<String, Object> item = new HashMap<>();
            item.put("empty", "暂无数据");
            emptyList.add(item);

            adapter = new SimpleAdapter(this, emptyList,
                    R.layout.empty_item, new String[]{"empty"}, new int[]{R.id.tv_empty});

            adapter_block = new SimpleAdapter(this, emptyList,
                    R.layout.empty_item, new String[]{"empty"}, new int[]{R.id.tv_empty});

            listView.setAdapter(adapter);
            listView_block.setAdapter(adapter_block);
            return;
        }

        List<HashMap<String, Object>> data = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            YhRenShiQingJiaShenPi item = dataList.get(i);
            HashMap<String, Object> map = new HashMap<>();
            map.put("index", i + 1);
            map.put("bumen", item.getBumen() != null ? item.getBumen() : "");
            map.put("xingming", item.getXingming() != null ? item.getXingming() : "");
            map.put("tijiaoshijian", item.getTijiaoshijian() != null ? item.getTijiaoshijian() : "");
            map.put("qsqingjiashijian", item.getQsqingjiashijian() != null ? item.getQsqingjiashijian() : "");
            map.put("jzqingjiashijan", item.getJzqingjiashijan() != null ? item.getJzqingjiashijan() : "");
            map.put("qingjiayuanyin", item.getQingjiayuanyin() != null ? item.getQingjiayuanyin() : "");
            map.put("zhuangtai", item.getZhuangtai() != null ? item.getZhuangtai() : "待审批");
            map.put("shenpiyuanyin", item.getShenpiyuanyin() != null ? item.getShenpiyuanyin() : "");
            data.add(map);
        }

        // 为块状视图创建适配器
        adapter_block = new SimpleAdapter(this, data, R.layout.qingjiashenpi_row_block,
                new String[]{"xingming", "bumen", "tijiaoshijian", "qsqingjiashijian", "jzqingjiashijan", "qingjiayuanyin", "zhuangtai", "shenpiyuanyin"},
                new int[]{R.id.B, R.id.C, R.id.D, R.id.E, R.id.F, R.id.G, R.id.H, R.id.I}) {

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                // 设置审批结果的颜色
                TextView tvZhuangtai = view.findViewById(R.id.H);
                HashMap<String, Object> item = data.get(position);
                String zhuangtai = (String) item.get("zhuangtai");

                setZhuangtaiColor(tvZhuangtai, zhuangtai);

                // 点击整行显示详情
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showRecordDetail(position);
                    }
                });

                return view;
            }
        };

        // 为表格视图创建适配器
        adapter = new SimpleAdapter(this, data, R.layout.qingjiashenpi_row,
                new String[]{"index", "bumen", "xingming", "tijiaoshijian", "qsqingjiashijian", "jzqingjiashijan", "qingjiayuanyin", "zhuangtai", "shenpiyuanyin"},
                new int[]{R.id.B, R.id.C, R.id.D, R.id.E, R.id.F, R.id.G, R.id.H, R.id.I, R.id.J}) {

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                // 设置审批结果的颜色
                TextView tvZhuangtai = view.findViewById(R.id.H);
                HashMap<String, Object> item = data.get(position);
                String zhuangtai = (String) item.get("zhuangtai");

                setZhuangtaiColor(tvZhuangtai, zhuangtai);

                // 点击审批结果列可以选择
                tvZhuangtai.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showZhuangtaiDialog(position);
                    }
                });

                // 点击整行显示详情
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showRecordDetail(position);
                    }
                });

                return view;
            }
        };

        listView.setAdapter(adapter);
        listView_block.setAdapter(adapter_block);
    }

    private void setZhuangtaiColor(TextView tv, String zhuangtai) {
        if ("通过".equals(zhuangtai)) {
            tv.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        } else if ("驳回".equals(zhuangtai)) {
            tv.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        } else {
            tv.setTextColor(getResources().getColor(android.R.color.darker_gray));
        }
    }

    public void onAddClick(View v) {
        showEditDialog(null);
    }

    private void showEditDialog(final YhRenShiQingJiaShenPi record) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(record == null ? "添加请假申请" : "编辑请假申请");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_qingjiashenpi_edit, null);

        final EditText etBumen = dialogView.findViewById(R.id.et_bumen);
        final EditText etXingming = dialogView.findViewById(R.id.et_xingming);
        final TextView tvTijiaoshijian = dialogView.findViewById(R.id.tv_tijiaoshijian);
        final EditText etTijiaoshijian = dialogView.findViewById(R.id.et_tijiaoshijian);
        final TextView tvQsqingjiashijian = dialogView.findViewById(R.id.tv_qsqingjiashijian);
        final EditText etQsqingjiashijian = dialogView.findViewById(R.id.et_qsqingjiashijian);
        final TextView tvJzqingjiashijan = dialogView.findViewById(R.id.tv_jzqingjiashijan);
        final EditText etJzqingjiashijan = dialogView.findViewById(R.id.et_jzqingjiashijan);
        final EditText etQingjiayuanyin = dialogView.findViewById(R.id.et_qingjiayuanyin);
        final Spinner spinnerZhuangtai = dialogView.findViewById(R.id.spinner_zhuangtai);
        final EditText etShenpiyuanyin = dialogView.findViewById(R.id.et_shenpiyuanyin);

        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
        Button btnSave = dialogView.findViewById(R.id.btn_save);

        // 初始化审批状态下拉框
        String[] zhuangtaiOptions = qingJiaShenPiService.getZhuangtaiOptions();
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                zhuangtaiOptions
        );
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerZhuangtai.setAdapter(statusAdapter);

        if (record != null) {
            etBumen.setText(record.getBumen());
            etXingming.setText(record.getXingming());

            String tijiaoshijian = record.getTijiaoshijian();
            if (tijiaoshijian != null && !tijiaoshijian.isEmpty()) {
                tvTijiaoshijian.setText(tijiaoshijian);
                etTijiaoshijian.setText(tijiaoshijian);
            }

            String qsqingjiashijian = record.getQsqingjiashijian();
            if (qsqingjiashijian != null && !qsqingjiashijian.isEmpty()) {
                tvQsqingjiashijian.setText(qsqingjiashijian);
                etQsqingjiashijian.setText(qsqingjiashijian);
            }

            String jzqingjiashijan = record.getJzqingjiashijan();
            if (jzqingjiashijan != null && !jzqingjiashijan.isEmpty()) {
                tvJzqingjiashijan.setText(jzqingjiashijan);
                etJzqingjiashijan.setText(jzqingjiashijan);
            }

            etQingjiayuanyin.setText(record.getQingjiayuanyin());
            etShenpiyuanyin.setText(record.getShenpiyuanyin());

            // 设置审批状态
            String zhuangtai = record.getZhuangtai();
            if (zhuangtai != null && !zhuangtai.isEmpty()) {
                for (int i = 0; i < zhuangtaiOptions.length; i++) {
                    if (zhuangtaiOptions[i].equals(zhuangtai)) {
                        spinnerZhuangtai.setSelection(i);
                        break;
                    }
                }
            }
        } else {
            String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            tvTijiaoshijian.setText(currentDate);
            etTijiaoshijian.setText(currentDate);
            tvQsqingjiashijian.setText(currentDate);
            etQsqingjiashijian.setText(currentDate);
            tvJzqingjiashijan.setText(currentDate);
            etJzqingjiashijan.setText(currentDate);
            // 默认选择"待审批"
            spinnerZhuangtai.setSelection(0);
        }

        // 设置日期选择器点击事件
        tvTijiaoshijian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(tvTijiaoshijian, etTijiaoshijian);
            }
        });

        tvQsqingjiashijian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(tvQsqingjiashijian, etQsqingjiashijian);
            }
        });

        tvJzqingjiashijan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(tvJzqingjiashijan, etJzqingjiashijan);
            }
        });

        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedStatus = spinnerZhuangtai.getSelectedItem().toString();
                saveRecord(record,
                        etBumen.getText().toString(),
                        etXingming.getText().toString(),
                        etTijiaoshijian.getText().toString(),
                        etQsqingjiashijian.getText().toString(),
                        etJzqingjiashijan.getText().toString(),
                        etQingjiayuanyin.getText().toString(),
                        selectedStatus,
                        etShenpiyuanyin.getText().toString()
                );
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showDatePicker(final TextView displayView, final EditText valueView) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                        displayView.setText(selectedDate);
                        valueView.setText(selectedDate);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void saveRecord(final YhRenShiQingJiaShenPi record,
                            final String bumen, final String xingming,
                            final String tijiaoshijian, final String qsqingjiashijian,
                            final String jzqingjiashijan, final String qingjiayuanyin,
                            final String zhuangtai, final String shenpiyuanyin) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    boolean success;
                    String company = yhRenShiUser.getL().replace("_hr", "");

                    if (record == null) {
                        YhRenShiQingJiaShenPi newRecord = new YhRenShiQingJiaShenPi();
                        newRecord.setGongsi(company);
                        newRecord.setBumen(bumen);
                        newRecord.setXingming(xingming);
                        newRecord.setTijiaoshijian(tijiaoshijian);
                        newRecord.setQsqingjiashijian(qsqingjiashijian);
                        newRecord.setJzqingjiashijan(jzqingjiashijan);
                        newRecord.setQingjiayuanyin(qingjiayuanyin);
                        newRecord.setZhuangtai(zhuangtai);
                        newRecord.setShenpiyuanyin(shenpiyuanyin);

                        success = qingJiaShenPiService.insert(newRecord);
                    } else {
                        record.setBumen(bumen);
                        record.setXingming(xingming);
                        record.setTijiaoshijian(tijiaoshijian);
                        record.setQsqingjiashijian(qsqingjiashijian);
                        record.setJzqingjiashijan(jzqingjiashijan);
                        record.setQingjiayuanyin(qingjiayuanyin);
                        record.setZhuangtai(zhuangtai);
                        record.setShenpiyuanyin(shenpiyuanyin);

                        success = qingJiaShenPiService.update(record);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (success) {
                                ToastUtil.show(QingJiaShenPiActivity.this, "保存成功");
                                loadData(null);
                            } else {
                                ToastUtil.show(QingJiaShenPiActivity.this, "保存失败");
                            }
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.show(QingJiaShenPiActivity.this, "保存异常: " + e.getMessage());
                        }
                    });
                }
            }
        }).start();
    }

    private void showRecordDetail(int position) {
        if (position >= dataList.size()) return;

        final YhRenShiQingJiaShenPi record = dataList.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("操作选择");
        builder.setMessage("请选择要执行的操作");

        builder.setPositiveButton("编辑", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showEditDialog(record);
            }
        });

        builder.setNegativeButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteRecord(record, position);
            }
        });

        // 添加审批状态快速选择
        builder.setNeutralButton("审批", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showZhuangtaiDialog(position);
            }
        });

        builder.show();
    }

    private void deleteRecord(final YhRenShiQingJiaShenPi record, final int position) {
        new AlertDialog.Builder(this)
                .setTitle("确认删除")
                .setMessage("确定要删除这条请假申请记录吗？")
                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                boolean success = qingJiaShenPiService.delete(record.getId());

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (success) {
                                            ToastUtil.show(QingJiaShenPiActivity.this, "删除成功");
                                            loadData(null);
                                        } else {
                                            ToastUtil.show(QingJiaShenPiActivity.this, "删除失败");
                                        }
                                    }
                                });
                            }
                        }).start();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void showZhuangtaiDialog(final int position) {
        if (position >= dataList.size()) return;

        final YhRenShiQingJiaShenPi record = dataList.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择审批结果");

        final String[] options = qingJiaShenPiService.getZhuangtaiOptions();
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String selectedResult = options[which];

                // 如果需要填写审批原因
                if ("待审批".equals(selectedResult)) {
                    updateZhuangtai(record.getId(), selectedResult, "", position);
                } else {
                    showShenpiyuanyinDialog(record, selectedResult, position);
                }
            }
        });

        builder.setNegativeButton("取消", null);
        builder.show();
    }

    private void showShenpiyuanyinDialog(final YhRenShiQingJiaShenPi record, final String zhuangtai, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请输入审批原因");

        final EditText input = new EditText(this);
        input.setHint("请输入审批原因");
        input.setPadding(20, 20, 20, 20);
        builder.setView(input);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String shenpiyuanyin = input.getText().toString().trim();
                updateZhuangtai(record.getId(), zhuangtai, shenpiyuanyin, position);
            }
        });

        builder.setNegativeButton("取消", null);
        builder.show();
    }

    private void updateZhuangtai(final int id, final String zhuangtai, final String shenpiyuanyin, final int position) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean success = qingJiaShenPiService.updateShenpijieguo(id, zhuangtai, shenpiyuanyin);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (success) {
                            ToastUtil.show(QingJiaShenPiActivity.this, "审批结果已更新");
                            // 更新本地数据并刷新列表
                            loadData(null);
                        } else {
                            ToastUtil.show(QingJiaShenPiActivity.this, "更新失败");
                        }
                    }
                });
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
}