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
import com.example.myapplication.renshi.entity.YhRenShiLiZhiShenPi;
import com.example.myapplication.renshi.entity.YhRenShiUser;
import com.example.myapplication.renshi.service.YhRenShiLiZhiShenPiService;
import com.example.myapplication.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class LiZhiShenPiActivity extends AppCompatActivity {

    private YhRenShiUser yhRenShiUser;
    private YhRenShiLiZhiShenPiService liZhiShenPiService;

    private ListView listView;
    private ListView listView_block;
    private Spinner spinnerShenpijieguo;
    private Button selButton;
    private HorizontalScrollView list_table;

    private List<YhRenShiLiZhiShenPi> dataList;
    private SimpleAdapter adapter;
    private SimpleAdapter adapter_block;

    private String[] shenpijieguoOptions = {"全部", "待审批", "通过", "驳回"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lizhishenpi);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("离职审批管理");
        }

        initViews();


        MyApplication myApplication = (MyApplication) getApplication();
        yhRenShiUser = myApplication.getYhRenShiUser();

        liZhiShenPiService = new YhRenShiLiZhiShenPiService();

        initShenpijieguoSpinner();

        loadData(null);
    }

    private void initViews() {
        listView = findViewById(R.id.baopan_list);
        listView_block = findViewById(R.id.list_block);
        list_table = findViewById(R.id.list_table);
        spinnerShenpijieguo = findViewById(R.id.spinner_shenpijieguo);
        selButton = findViewById(R.id.sel_button);

        // 设置查询按钮点击事件
        selButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedStatus = spinnerShenpijieguo.getSelectedItem().toString();
                loadData(selectedStatus.equals("全部") ? null : selectedStatus);
            }
        });

        listView_block.setVisibility(View.VISIBLE);
        list_table.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);
    }



    private void initShenpijieguoSpinner() {
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                shenpijieguoOptions
        );
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerShenpijieguo.setAdapter(statusAdapter);
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
                    dataList = liZhiShenPiService.getListByStatus(company, filterStatus);
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
                ToastUtil.show(LiZhiShenPiActivity.this, "数据加载失败");
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
            YhRenShiLiZhiShenPi item = dataList.get(i);
            HashMap<String, Object> map = new HashMap<>();
            map.put("index", i + 1);
            map.put("bumen", item.getBumen() != null ? item.getBumen() : "");
            map.put("xingming", item.getXingming() != null ? item.getXingming() : "");
            map.put("tijiaoriqi", item.getTijiaoriqi() != null ? item.getTijiaoriqi() : "");
            map.put("shenqingyuanyin", item.getShenqingyuanyin() != null ? item.getShenqingyuanyin() : "");
            map.put("shenpijieguo", item.getShenpijieguo() != null ? item.getShenpijieguo() : "待审批");
            map.put("shenpiyuanyin", item.getShenpiyuanyin() != null ? item.getShenpiyuanyin() : "");
            data.add(map);
        }

        // 为块状视图创建适配器
        adapter_block = new SimpleAdapter(this, data, R.layout.lizhishenpi_row_block,
                new String[]{"xingming", "bumen", "tijiaoriqi", "shenqingyuanyin", "shenpijieguo", "shenpiyuanyin"},
                new int[]{R.id.B, R.id.C, R.id.D, R.id.E, R.id.F, R.id.G}) {

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                // 设置审批结果的颜色
                TextView tvShenpijieguo = view.findViewById(R.id.F);
                HashMap<String, Object> item = data.get(position);
                String shenpijieguo = (String) item.get("shenpijieguo");

                setShenpijieguoColor(tvShenpijieguo, shenpijieguo);

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
        adapter = new SimpleAdapter(this, data, R.layout.lizhishenpi_row,
                new String[]{"index", "bumen", "xingming", "tijiaoriqi", "shenqingyuanyin", "shenpijieguo", "shenpiyuanyin"},
                new int[]{R.id.B, R.id.C, R.id.D, R.id.E, R.id.F, R.id.G, R.id.H}) {

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                // 设置审批结果的颜色
                TextView tvShenpijieguo = view.findViewById(R.id.G);
                HashMap<String, Object> item = data.get(position);
                String shenpijieguo = (String) item.get("shenpijieguo");

                setShenpijieguoColor(tvShenpijieguo, shenpijieguo);

                // 点击审批结果列可以选择
                tvShenpijieguo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showShenpijieguoDialog(position);
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

    private void setShenpijieguoColor(TextView tv, String shenpijieguo) {
        if ("通过".equals(shenpijieguo)) {
            tv.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        } else if ("驳回".equals(shenpijieguo)) {
            tv.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        } else {
            tv.setTextColor(getResources().getColor(android.R.color.darker_gray));
        }
    }

    public void onAddClick(View v) {
        showEditDialog(null);
    }

    private void showEditDialog(final YhRenShiLiZhiShenPi record) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(record == null ? "添加离职申请" : "编辑离职申请");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_lizhishenpi_edit, null);

        final EditText etBumen = dialogView.findViewById(R.id.et_bumen);
        final EditText etXingming = dialogView.findViewById(R.id.et_xingming);
        final TextView tvTijiaoriqi = dialogView.findViewById(R.id.tv_tijiaoriqi);
        final EditText etTijiaoriqi = dialogView.findViewById(R.id.et_tijiaoriqi);
        final EditText etShenqingyuanyin = dialogView.findViewById(R.id.et_shenqingyuanyin);
        final Spinner spinnerShenpijieguo = dialogView.findViewById(R.id.spinner_shenpijieguo);
        final EditText etShenpiyuanyin = dialogView.findViewById(R.id.et_shenpiyuanyin);

        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
        Button btnSave = dialogView.findViewById(R.id.btn_save);

        // 初始化审批结果下拉框
        String[] shenpijieguoOptions = liZhiShenPiService.getShenpijieguoOptions();
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                shenpijieguoOptions
        );
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerShenpijieguo.setAdapter(statusAdapter);

        if (record != null) {
            etBumen.setText(record.getBumen());
            etXingming.setText(record.getXingming());

            String tijiaoriqi = record.getTijiaoriqi();
            if (tijiaoriqi != null && !tijiaoriqi.isEmpty()) {
                tvTijiaoriqi.setText(tijiaoriqi);
                etTijiaoriqi.setText(tijiaoriqi);
            }

            etShenqingyuanyin.setText(record.getShenqingyuanyin());
            etShenpiyuanyin.setText(record.getShenpiyuanyin());

            // 设置审批结果
            String shenpijieguo = record.getShenpijieguo();
            if (shenpijieguo != null && !shenpijieguo.isEmpty()) {
                for (int i = 0; i < shenpijieguoOptions.length; i++) {
                    if (shenpijieguoOptions[i].equals(shenpijieguo)) {
                        spinnerShenpijieguo.setSelection(i);
                        break;
                    }
                }
            }
        } else {
            String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            tvTijiaoriqi.setText(currentDate);
            etTijiaoriqi.setText(currentDate);
            // 默认选择"待审批"
            spinnerShenpijieguo.setSelection(0);
        }

        tvTijiaoriqi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(tvTijiaoriqi, etTijiaoriqi);
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
                String selectedStatus = spinnerShenpijieguo.getSelectedItem().toString();
                saveRecord(record,
                        etBumen.getText().toString(),
                        etXingming.getText().toString(),
                        etTijiaoriqi.getText().toString(),
                        etShenqingyuanyin.getText().toString(),
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

    private void saveRecord(final YhRenShiLiZhiShenPi record,
                            final String bumen, final String xingming,
                            final String tijiaoriqi, final String shenqingyuanyin,
                            final String shenpijieguo, final String shenpiyuanyin) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    boolean success;
                    String company = yhRenShiUser.getL().replace("_hr", "");

                    if (record == null) {
                        YhRenShiLiZhiShenPi newRecord = new YhRenShiLiZhiShenPi();
                        newRecord.setGongsi(company);
                        newRecord.setBumen(bumen);
                        newRecord.setXingming(xingming);
                        newRecord.setTijiaoriqi(tijiaoriqi);
                        newRecord.setShenqingyuanyin(shenqingyuanyin);
                        newRecord.setShenpijieguo(shenpijieguo);
                        newRecord.setShenpiyuanyin(shenpiyuanyin);

                        success = liZhiShenPiService.insert(newRecord);
                    } else {
                        record.setBumen(bumen);
                        record.setXingming(xingming);
                        record.setTijiaoriqi(tijiaoriqi);
                        record.setShenqingyuanyin(shenqingyuanyin);
                        record.setShenpijieguo(shenpijieguo);
                        record.setShenpiyuanyin(shenpiyuanyin);

                        success = liZhiShenPiService.update(record);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (success) {
                                ToastUtil.show(LiZhiShenPiActivity.this, "保存成功");
                                loadData(null);
                            } else {
                                ToastUtil.show(LiZhiShenPiActivity.this, "保存失败");
                            }
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.show(LiZhiShenPiActivity.this, "保存异常: " + e.getMessage());
                        }
                    });
                }
            }
        }).start();
    }

    private void showRecordDetail(int position) {
        if (position >= dataList.size()) return;

        final YhRenShiLiZhiShenPi record = dataList.get(position);

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

        // 添加审批结果快速选择
        builder.setNeutralButton("审批", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showShenpijieguoDialog(position);
            }
        });

        builder.show();
    }

    private void deleteRecord(final YhRenShiLiZhiShenPi record, final int position) {
        new AlertDialog.Builder(this)
                .setTitle("确认删除")
                .setMessage("确定要删除这条离职申请记录吗？")
                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                boolean success = liZhiShenPiService.delete(record.getId());

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (success) {
                                            ToastUtil.show(LiZhiShenPiActivity.this, "删除成功");
                                            loadData(null);
                                        } else {
                                            ToastUtil.show(LiZhiShenPiActivity.this, "删除失败");
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

    private void showShenpijieguoDialog(final int position) {
        if (position >= dataList.size()) return;

        final YhRenShiLiZhiShenPi record = dataList.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择审批结果");

        final String[] options = liZhiShenPiService.getShenpijieguoOptions();
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String selectedResult = options[which];

                // 如果需要填写审批原因
                if ("待审批".equals(selectedResult)) {
                    updateShenpijieguo(record.getId(), selectedResult, "", position);
                } else {
                    showShenpiyuanyinDialog(record, selectedResult, position);
                }
            }
        });

        builder.setNegativeButton("取消", null);
        builder.show();
    }

    private void showShenpiyuanyinDialog(final YhRenShiLiZhiShenPi record, final String shenpijieguo, final int position) {
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
                updateShenpijieguo(record.getId(), shenpijieguo, shenpiyuanyin, position);
            }
        });

        builder.setNegativeButton("取消", null);
        builder.show();
    }

    private void updateShenpijieguo(final int id, final String shenpijieguo, final String shenpiyuanyin, final int position) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean success = liZhiShenPiService.updateShenpijieguo(id, shenpijieguo, shenpiyuanyin);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (success) {
                            ToastUtil.show(LiZhiShenPiActivity.this, "审批结果已更新");
                            // 更新本地数据并刷新列表
                            loadData(null);
                        } else {
                            ToastUtil.show(LiZhiShenPiActivity.this, "更新失败");
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