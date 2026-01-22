package com.example.myapplication.renshi.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.renshi.entity.YhRenShiDongTaiMingXi;
import com.example.myapplication.renshi.entity.YhRenShiGongShiGuanLi;
import com.example.myapplication.renshi.entity.YhRenShiUser;
import com.example.myapplication.renshi.service.YhRenShiDongTaiMingXiService;
import com.example.myapplication.utils.ToastUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DongTaiMingXiActivity extends AppCompatActivity {

    private YhRenShiUser yhRenShiUser;
    private YhRenShiDongTaiMingXiService dongTaiMingXiService;

    private ListView listView;
    private HorizontalScrollView list_table;
    private ListView listView_block;

    private List<YhRenShiDongTaiMingXi> dataList;
    private List<Map<String, Object>> processedData;
    private List<Map<String, Object>> dynamicTitles;
    private SimpleAdapter adapter;
    private SimpleAdapter adapter_block;

    private Button btnTitleConfig;
    private Button btnFormulaConfig;
    private FloatingActionButton btnAddRow;

    // 当前编辑的信息
    private int currentEditId = -1;
    private int currentEditColumn = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dongtaimingxi);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("动态工资明细");
        }

        initViews();

        MyApplication myApplication = (MyApplication) getApplication();
        yhRenShiUser = myApplication.getYhRenShiUser();

        dongTaiMingXiService = new YhRenShiDongTaiMingXiService();

        loadAllData();
    }

    private void initViews() {
        listView = findViewById(R.id.baopan_list);
        list_table = findViewById(R.id.list_table);
        listView_block = findViewById(R.id.list_block);


        btnTitleConfig = findViewById(R.id.xiujiaClick);
        btnFormulaConfig = findViewById(R.id.shenpijilu);
        btnAddRow = findViewById(R.id.jczl_insert);

        // 设置按钮文本
        btnTitleConfig.setText("配置标题");
        btnFormulaConfig.setText("公式配置");

        // 设置初始显示模式
        listView_block.setVisibility(View.VISIBLE);
        list_table.setVisibility(View.GONE);

        // 设置按钮点击事件


        btnTitleConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTitleConfigDialog();
            }
        });

        btnFormulaConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFormulaConfigDialog();
            }
        });

        btnAddRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewRow();
            }
        });
    }

    /**
     * 加载所有数据
     */
    private void loadAllData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String company = yhRenShiUser.getL().replace("_hr", "");

                    // 获取所有数据
                    List<YhRenShiDongTaiMingXi> allRecords = dongTaiMingXiService.getAllData(company);

                    // 分离标题配置和数据
                    dataList = new ArrayList<>();
                    YhRenShiDongTaiMingXi titleConfig = null;

                    for (YhRenShiDongTaiMingXi record : allRecords) {
                        if (record.getId() == 1) {
                            titleConfig = record;
                        } else {
                            dataList.add(record);
                        }
                    }

                    // 处理标题配置
                    if (titleConfig != null && titleConfig.getName() != null) {
                        dynamicTitles = dongTaiMingXiService.processTitleConfig(titleConfig.getName());
                    } else {
                        // 如果没有标题配置，创建默认
                        dynamicTitles = dongTaiMingXiService.processTitleConfig("");
                        // 保存默认配置到数据库
                        String defaultTitle = "字段1|||字段2|||字段3|||字段4|||字段5";
                        dongTaiMingXiService.saveTitleConfig(company, defaultTitle);
                    }

                    // 处理内容数据
                    processedData = dongTaiMingXiService.processContentData(dataList, dynamicTitles);

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
//                ToastUtil.show(DongTaiMingXiActivity.this, "数据加载失败");
            }
        }
    };

    /**
     * 更新列表显示
     */
    private void updateListView() {
        if (processedData == null || processedData.isEmpty()) {
            showEmptyView();
            return;
        }

        // 表格视图
        updateTableView();

        // 列表块视图
        updateBlockView();
    }

    /**
     * 显示空数据视图
     */
    private void showEmptyView() {
        List<HashMap<String, Object>> emptyList = new ArrayList<>();
        HashMap<String, Object> item = new HashMap<>();
        item.put("empty", "暂无数据");
        emptyList.add(item);

        adapter = new SimpleAdapter(this, emptyList,
                R.layout.empty_item, new String[]{"empty"}, new int[]{R.id.tv_empty});

        listView.setAdapter(adapter);
        listView_block.setAdapter(adapter);
    }

    /**
     * 更新表格视图
     */
    private void updateTableView() {
        List<HashMap<String, Object>> data = new ArrayList<>();

        for (int i = 0; i < processedData.size(); i++) {
            Map<String, Object> item = processedData.get(i);
            HashMap<String, Object> map = new HashMap<>();

            map.put("index", i + 1);
            map.put("id", item.get("id"));

            // 添加动态字段
            for (int j = 0; j < dynamicTitles.size(); j++) {
                String value = (String) item.get("col_" + j);
                map.put("col_" + j, value != null ? value : "");
            }

            data.add(map);
        }

        String[] from = new String[dynamicTitles.size() + 2];
        int[] to = new int[dynamicTitles.size() + 2];

        from[0] = "index";
        to[0] = R.id.B;
        from[1] = "id";
        to[1] = R.id.id_hidden;

        for (int i = 0; i < dynamicTitles.size(); i++) {
            from[i + 2] = "col_" + i;
            to[i + 2] = getColumnViewId(i);
        }

        adapter = new SimpleAdapter(this, data, R.layout.dongtaimingxi_row,
                from, to) {

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                // 获取隐藏的ID
                TextView tvId = view.findViewById(R.id.id_hidden);
                final int itemId = Integer.parseInt(tvId.getText().toString());

                // 为每个单元格设置点击事件
                for (int i = 0; i < dynamicTitles.size(); i++) {
                    int columnViewId = getColumnViewId(i);
                    TextView cell = view.findViewById(columnViewId);
                    final int columnIndex = i;

                    cell.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showCellEditDialog(itemId, columnIndex);
                        }
                    });
                }

                // 序号列点击删除
                TextView tvIndex = view.findViewById(R.id.B);
                tvIndex.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDeleteDialog(itemId, position + 1);
                    }
                });

                return view;
            }
        };

        listView.setAdapter(adapter);
    }

    /**
     * 更新列表块视图
     */
    private void updateBlockView() {
        List<HashMap<String, Object>> data = new ArrayList<>();

        for (int i = 0; i < processedData.size(); i++) {
            Map<String, Object> item = processedData.get(i);
            HashMap<String, Object> map = new HashMap<>();

            map.put("index", i + 1);
            map.put("id", item.get("id"));

            // 创建组合字段显示
            StringBuilder combined = new StringBuilder();
            for (int j = 0; j < dynamicTitles.size(); j++) {
                String title = (String) dynamicTitles.get(j).get("text");
                String value = (String) item.get("col_" + j);
                combined.append(title).append(": ").append(value != null ? value : "");
                if (j < dynamicTitles.size() - 1) {
                    combined.append("\n");
                }
            }
            map.put("combined", combined.toString());

            data.add(map);
        }

        adapter_block = new SimpleAdapter(this, data, R.layout.dongtaimingxi_row_block,
                new String[]{"index", "combined"},
                new int[]{R.id.B, R.id.C}) {

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                final Map<String, Object> item = processedData.get(position);
                final int itemId = (int) item.get("id");

                // 点击编辑
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showRowEditDialog(itemId);
                    }
                });

                // 序号点击删除
                TextView tvIndex = view.findViewById(R.id.B);
                tvIndex.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDeleteDialog(itemId, position + 1);
                    }
                });

                return view;
            }
        };

        listView_block.setAdapter(adapter_block);
    }

    /**
     * 获取列视图ID
     */
    private int getColumnViewId(int columnIndex) {
        int[] columnIds = {
                R.id.C, R.id.D, R.id.E, R.id.F, R.id.G,
                R.id.H, R.id.I, R.id.J, R.id.K, R.id.L,
                R.id.M, R.id.N, R.id.O, R.id.P, R.id.Q
        };

        if (columnIndex < columnIds.length) {
            return columnIds[columnIndex];
        }
        return R.id.C;
    }


    /**
     * 显示单元格编辑对话框
     */
    private void showCellEditDialog(final int itemId, final int columnIndex) {
        currentEditId = itemId;
        currentEditColumn = columnIndex;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("编辑单元格");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        // 获取当前值
        String currentValue = "";
        for (Map<String, Object> item : processedData) {
            if ((int) item.get("id") == itemId) {
                currentValue = (String) item.get("col_" + columnIndex);
                break;
            }
        }

        input.setText(currentValue);
        input.setSelection(input.getText().length());

        builder.setView(input);

        builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newValue = input.getText().toString();
                saveCellEdit(itemId, columnIndex, newValue);
            }
        });

        builder.setNegativeButton("取消", null);

        builder.show();
    }

    /**
     * 保存单元格编辑
     */
    private void saveCellEdit(final int itemId, final int columnIndex, final String newValue) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String company = yhRenShiUser.getL().replace("_hr", "");

                    // 找到对应的数据行
                    YhRenShiDongTaiMingXi targetItem = null;
                    for (YhRenShiDongTaiMingXi item : dataList) {
                        if (item.getId() == itemId) {
                            targetItem = item;
                            break;
                        }
                    }

                    if (targetItem != null) {
                        // 解析现有数据
                        String[] dataArray = dongTaiMingXiService.splitFieldValues(targetItem.getName());

                        // 确保数组长度足够
                        while (dataArray.length <= columnIndex) {
                            String[] newArray = new String[dataArray.length + 1];
                            System.arraycopy(dataArray, 0, newArray, 0, dataArray.length);
                            newArray[dataArray.length] = "";
                            dataArray = newArray;
                        }

                        // 更新值
                        dataArray[columnIndex] = newValue;

                        // 重新拼接
                        StringBuilder updatedData = new StringBuilder();
                        for (int i = 0; i < dataArray.length; i++) {
                            if (i > 0) {
                                updatedData.append("|||");
                            }
                            updatedData.append(dataArray[i] != null ? dataArray[i] : "");
                        }

                        // 更新数据库
                        boolean success = dongTaiMingXiService.update(itemId, company, updatedData.toString());

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (success) {
                                    ToastUtil.show(DongTaiMingXiActivity.this, "保存成功");
                                    loadAllData();

                                    // 重新计算公式
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            dongTaiMingXiService.calculateAndUpdateAllData(
                                                    company, dynamicTitles);
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    loadAllData();
                                                }
                                            });
                                        }
                                    }).start();
                                } else {
                                    ToastUtil.show(DongTaiMingXiActivity.this, "保存失败");
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.show(DongTaiMingXiActivity.this, "保存异常: " + e.getMessage());
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * 显示行编辑对话框
     */
    private void showRowEditDialog(final int itemId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("编辑整行数据");

        // 创建垂直布局
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(20, 20, 20, 20);

        // 为每个字段创建输入框
        final List<EditText> editTexts = new ArrayList<>();

        for (int i = 0; i < dynamicTitles.size(); i++) {
            String title = (String) dynamicTitles.get(i).get("text");

            TextView label = new TextView(this);
            label.setText(title + ":");
            label.setTextSize(16);
            label.setPadding(0, 10, 0, 5);
            layout.addView(label);

            EditText editText = new EditText(this);
            editText.setInputType(InputType.TYPE_CLASS_TEXT);

            // 设置当前值
            String currentValue = "";
            for (Map<String, Object> item : processedData) {
                if ((int) item.get("id") == itemId) {
                    currentValue = (String) item.get("col_" + i);
                    break;
                }
            }
            editText.setText(currentValue);

            layout.addView(editText);
            editTexts.add(editText);
        }

        ScrollView scrollView = new ScrollView(this);
        scrollView.addView(layout);

        builder.setView(scrollView);

        builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveRowEdit(itemId, editTexts);
            }
        });

        builder.setNegativeButton("取消", null);

        builder.create().show();
    }

    /**
     * 保存行编辑
     */
    private void saveRowEdit(final int itemId, List<EditText> editTexts) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String company = yhRenShiUser.getL().replace("_hr", "");

                    // 构建数据字符串
                    StringBuilder dataBuilder = new StringBuilder();
                    for (int i = 0; i < editTexts.size(); i++) {
                        if (i > 0) {
                            dataBuilder.append("|||");
                        }
                        dataBuilder.append(editTexts.get(i).getText().toString());
                    }

                    String updatedData = dataBuilder.toString();

                    // 更新数据库
                    boolean success = dongTaiMingXiService.update(itemId, company, updatedData);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (success) {
                                ToastUtil.show(DongTaiMingXiActivity.this, "保存成功");
                                loadAllData();

                                // 重新计算公式
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        dongTaiMingXiService.calculateAndUpdateAllData(
                                                company, dynamicTitles);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                loadAllData();
                                            }
                                        });
                                    }
                                }).start();
                            } else {
                                ToastUtil.show(DongTaiMingXiActivity.this, "保存失败");
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.show(DongTaiMingXiActivity.this, "保存异常: " + e.getMessage());
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * 显示删除对话框
     */
    private void showDeleteDialog(final int itemId, final int position) {
        new AlertDialog.Builder(this)
                .setTitle("确认删除")
                .setMessage("确定要删除第" + position + "行数据吗？")
                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteRecord(itemId);
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    /**
     * 删除记录
     */
    private void deleteRecord(final int itemId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String company = yhRenShiUser.getL().replace("_hr", "");
                    boolean success = dongTaiMingXiService.delete(itemId, company);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (success) {
                                ToastUtil.show(DongTaiMingXiActivity.this, "删除成功");
                                loadAllData();
                            } else {
                                ToastUtil.show(DongTaiMingXiActivity.this, "删除失败");
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.show(DongTaiMingXiActivity.this, "删除异常: " + e.getMessage());
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * 显示标题配置对话框
     */
    private void showTitleConfigDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("配置标题字段");

        // 使用自定义布局
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_title_config, null);
        builder.setView(dialogView);

        final LinearLayout fieldsLayout = dialogView.findViewById(R.id.fields_layout);
        final Button btnAddField = dialogView.findViewById(R.id.btn_add_field);

        // 清空现有字段
        fieldsLayout.removeAllViews();

        // 添加现有字段
        final List<EditText> fieldInputs = new ArrayList<>();
        for (Map<String, Object> title : dynamicTitles) {
            String fieldName = (String) title.get("text");
            addFieldInput(fieldsLayout, fieldInputs, fieldName);
        }

        // 如果还没有字段，添加一个空字段
        if (fieldInputs.isEmpty()) {
            addFieldInput(fieldsLayout, fieldInputs, "");
        }

        // 添加字段按钮事件
        btnAddField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFieldInput(fieldsLayout, fieldInputs, "");
            }
        });

        builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveTitleConfig(fieldInputs);
            }
        });

        builder.setNegativeButton("取消", null);

        builder.show();
    }

    /**
     * 添加字段输入框
     */
//    private void addFieldInput(LinearLayout layout, List<EditText> inputs, String initialValue) {
//        LinearLayout rowLayout = new LinearLayout(this);
//        rowLayout.setOrientation(LinearLayout.HORIZONTAL);
//        rowLayout.setPadding(0, 5, 0, 5);
//        rowLayout.setLayoutParams(new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT));
//
//        EditText editText = new EditText(this);
//        LinearLayout.LayoutParams editParams = new LinearLayout.LayoutParams(
//                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
//        editParams.setMargins(0, 0, 10, 0);
//        editText.setLayoutParams(editParams);
//        editText.setText(initialValue);
//        editText.setHint("请输入字段名");
//
//        Button deleteButton = new Button(this);
//        deleteButton.setText("删除");
//        deleteButton.setBackgroundResource(R.drawable.newbtn_style);
//        deleteButton.setTextColor(Color.WHITE);
//        deleteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String fieldName = editText.getText().toString().trim();
//                if (!fieldName.isEmpty()) {
//                    // 检查字段是否在公式中被使用
//                    if (dongTaiMingXiService.isFieldUsedInFormula(fieldName,
//                            yhRenShiUser.getL().replace("_hr", ""))) {
//                        ToastUtil.show(DongTaiMingXiActivity.this,
//                                "字段'" + fieldName + "'已在公式中使用，请先删除相关公式");
//                        return;
//                    }
//                }
//                layout.removeView(rowLayout);
//                inputs.remove(editText);
//            }
//        });
//
//        rowLayout.addView(editText);
//        rowLayout.addView(deleteButton);
//
//        layout.addView(rowLayout, layout.getChildCount() - 2); // 添加到添加按钮之前
//        inputs.add(editText);
//    }
    private void addFieldInput(LinearLayout layout, List<EditText> inputs, String initialValue) {
        LinearLayout rowLayout = new LinearLayout(this);
        rowLayout.setOrientation(LinearLayout.HORIZONTAL);
        rowLayout.setPadding(0, 5, 0, 5);
        rowLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        EditText editText = new EditText(this);
        LinearLayout.LayoutParams editParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        editParams.setMargins(0, 0, 10, 0);
        editText.setLayoutParams(editParams);
        editText.setText(initialValue);
        editText.setHint("请输入字段名");

        Button deleteButton = new Button(this);
        deleteButton.setText("删除");
        deleteButton.setBackgroundResource(R.drawable.newbtn_style);
        deleteButton.setTextColor(Color.WHITE);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fieldName = editText.getText().toString().trim();
                if (!fieldName.isEmpty()) {
                    // 在子线程中检查字段是否在公式中被使用
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final boolean isUsed = dongTaiMingXiService.isFieldUsedInFormula(fieldName,
                                    yhRenShiUser.getL().replace("_hr", ""));

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (isUsed) {
                                        ToastUtil.show(DongTaiMingXiActivity.this,
                                                "字段'" + fieldName + "'已在公式中使用，请先删除相关公式");
                                    } else {
                                        // 安全删除字段
                                        layout.removeView(rowLayout);
                                        inputs.remove(editText);
                                    }
                                }
                            });
                        }
                    }).start();
                } else {
                    // 如果是空字段，直接删除
                    layout.removeView(rowLayout);
                    inputs.remove(editText);
                }
            }
        });

        rowLayout.addView(editText);
        rowLayout.addView(deleteButton);

        layout.addView(rowLayout, layout.getChildCount() - 2); // 添加到添加按钮之前
        inputs.add(editText);
    }

    /**
     * 保存标题配置
     */
    private void saveTitleConfig(List<EditText> fieldInputs) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String company = yhRenShiUser.getL().replace("_hr", "");

                    // 收集字段名
                    List<String> fields = new ArrayList<>();
                    for (EditText editText : fieldInputs) {
                        String field = editText.getText().toString().trim();
                        if (!field.isEmpty()) {
                            fields.add(field);
                        }
                    }

                    if (fields.isEmpty()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.show(DongTaiMingXiActivity.this, "请至少输入一个字段");
                            }
                        });
                        return;
                    }

                    // 构建标题字符串
                    StringBuilder titleBuilder = new StringBuilder();
                    for (int i = 0; i < fields.size(); i++) {
                        if (i > 0) {
                            titleBuilder.append("|||");
                        }
                        titleBuilder.append(fields.get(i));
                    }
                    String titleStr = titleBuilder.toString();

                    // 保存到数据库
                    boolean success = dongTaiMingXiService.saveTitleConfig(company, titleStr);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (success) {
                                ToastUtil.show(DongTaiMingXiActivity.this, "标题配置保存成功");
                                loadAllData();
                            } else {
                                ToastUtil.show(DongTaiMingXiActivity.this, "保存失败");
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.show(DongTaiMingXiActivity.this, "保存异常: " + e.getMessage());
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * 显示公式配置对话框
     */
    private void showFormulaConfigDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("配置计算公式");

        // 使用自定义布局
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_formula_config, null);
        builder.setView(dialogView);

        final Spinner spinnerTargetField = dialogView.findViewById(R.id.spinner_target_field);
        final EditText etFormula = dialogView.findViewById(R.id.et_formula);
        final LinearLayout operatorsLayout = dialogView.findViewById(R.id.operators_layout);
        final LinearLayout fieldsLayout = dialogView.findViewById(R.id.fields_layout);
        final LinearLayout existingFormulasLayout = dialogView.findViewById(R.id.existing_formulas_layout);

        // 设置目标字段下拉列表
        List<String> fieldNames = new ArrayList<>();
        for (Map<String, Object> title : dynamicTitles) {
            fieldNames.add((String) title.get("text"));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, fieldNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTargetField.setAdapter(adapter);

        // 设置运算符按钮
        String[] operators = {"+", "-", "×", "÷", "(", ")"};

        // 为每个运算符创建按钮
        for (final String op : operators) {
            Button btn = createOperatorButton(op);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    insertIntoFormula(etFormula, op);
                }
            });
            operatorsLayout.addView(btn);
        }

        // 设置字段按钮
        for (final String fieldName : fieldNames) {
            Button btn = createFieldButton(fieldName);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    insertIntoFormula(etFormula, fieldName);
                }
            });
            fieldsLayout.addView(btn);
        }

        // 添加常用函数按钮
        String[] functions = {"SUM", "AVG", "MAX", "MIN", "COUNT", "IF", "ROUND", "ABS", "POW"};
        for (final String func : functions) {
            Button btn = createFunctionButton(func);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    insertIntoFormula(etFormula, func + "()");
                    // 将光标移动到括号内
                    int position = etFormula.getText().length() - 1;
                    etFormula.setSelection(position);
                }
            });
            operatorsLayout.addView(btn);
        }

        // 加载现有公式
        loadExistingFormulas(existingFormulasLayout);

        builder.setPositiveButton("保存公式", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String targetField = (String) spinnerTargetField.getSelectedItem();
                String formula = etFormula.getText().toString();
                saveFormula(targetField, formula);
            }
        });

        builder.setNegativeButton("取消", null);

        builder.show();
    }

    /**
     * 创建运算符按钮
     */
    private Button createOperatorButton(String text) {
        Button btn = new Button(this);
        btn.setText(text);
        btn.setMinWidth(80); // 设置最小宽度
        btn.setHeight(80); // 设置固定高度
        btn.setTextSize(18); // 设置大一点的字体
        btn.setPadding(8, 8, 8, 8);

        // 设置圆角样式
        btn.setTextColor(Color.WHITE);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(4, 4, 4, 4);
        btn.setLayoutParams(params);

        return btn;
    }

    /**
     * 创建字段按钮
     */
    private Button createFieldButton(String text) {
        Button btn = new Button(this);
        btn.setText(text);
        btn.setMinWidth(120); // 设置最小宽度
        btn.setHeight(60); // 设置固定高度
        btn.setTextSize(14);
        btn.setPadding(12, 8, 12, 8);
        btn.setSingleLine();
        btn.setEllipsize(TextUtils.TruncateAt.END);

        // 设置字段按钮样式
        btn.setTextColor(Color.WHITE);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(4, 4, 4, 4);
        btn.setLayoutParams(params);

        return btn;
    }

    /**
     * 创建函数按钮
     */
    private Button createFunctionButton(String text) {
        Button btn = new Button(this);
        btn.setText(text);
        btn.setMinWidth(100);
        btn.setHeight(60);
        btn.setTextSize(14);
        btn.setPadding(8, 8, 8, 8);

        // 设置函数按钮样式
        btn.setTextColor(Color.WHITE);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(4, 4, 4, 4);
        btn.setLayoutParams(params);

        return btn;
    }

    /**
     * 插入内容到公式输入框
     */
    private void insertIntoFormula(EditText etFormula, String content) {
        String current = etFormula.getText().toString();
        int cursorPosition = etFormula.getSelectionStart();

        if (cursorPosition >= 0) {
            String before = current.substring(0, cursorPosition);
            String after = current.substring(cursorPosition);
            String newText = before + content + after;
            etFormula.setText(newText);
            etFormula.setSelection(cursorPosition + content.length());
        } else {
            etFormula.setText(current + content);
            etFormula.setSelection(etFormula.getText().length());
        }
    }


    /**
     * 加载现有公式
     */
    private void loadExistingFormulas(final LinearLayout layout) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String company = yhRenShiUser.getL().replace("_hr", "");
                    final List<YhRenShiGongShiGuanLi> formulaList =
                            dongTaiMingXiService.getFormulaList(company);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            layout.removeAllViews();

                            if (formulaList.isEmpty()) {
                                TextView noFormulaText = new TextView(DongTaiMingXiActivity.this);
                                noFormulaText.setText("暂无公式配置");
                                noFormulaText.setTextColor(Color.GRAY);
                                noFormulaText.setTextSize(16);
                                noFormulaText.setGravity(Gravity.CENTER);
                                noFormulaText.setPadding(16, 32, 16, 32);
                                layout.addView(noFormulaText);
                                return;
                            }

                            // 创建水平布局容器
                            LinearLayout horizontalLayout = new LinearLayout(DongTaiMingXiActivity.this);
                            horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
                            horizontalLayout.setLayoutParams(new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT));

                            for (final YhRenShiGongShiGuanLi formula : formulaList) {
                                // 创建公式卡片
                                LinearLayout formulaCard = createFormulaCard(formula);
                                horizontalLayout.addView(formulaCard);
                            }

                            layout.addView(horizontalLayout);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 创建公式卡片
     */
    private LinearLayout createFormulaCard(final YhRenShiGongShiGuanLi formula) {
        // 创建卡片容器
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        card.setPadding(16, 16, 16, 16);

        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                320, // 固定宽度
                LinearLayout.LayoutParams.WRAP_CONTENT);
        cardParams.setMargins(8, 8, 8, 8);
        card.setLayoutParams(cardParams);

        // 目标字段标题
        TextView targetLabel = new TextView(this);
        targetLabel.setText("目标字段：");
        targetLabel.setTextSize(12);
        targetLabel.setTextColor(Color.GRAY);
        card.addView(targetLabel);

        // 目标字段值
        TextView targetField = new TextView(this);
        targetField.setText(formula.getZhuziduan());
        targetField.setTextSize(16);
        targetField.setTextColor(Color.BLACK);
        targetField.setTypeface(null, android.graphics.Typeface.BOLD);
        targetField.setPadding(0, 0, 0, 8);
        card.addView(targetField);

        // 公式标题
        TextView formulaLabel = new TextView(this);
        formulaLabel.setText("计算公式：");
        formulaLabel.setTextSize(12);
        formulaLabel.setTextColor(Color.GRAY);
        card.addView(formulaLabel);

        // 公式内容
        TextView formulaText = new TextView(this);
        formulaText.setText(formula.getGongshi());
        formulaText.setTextSize(14);
        formulaText.setTextColor(Color.DKGRAY);
        formulaText.setPadding(0, 0, 0, 12);
        card.addView(formulaText);

        // 删除按钮
        Button deleteBtn = new Button(this);
        deleteBtn.setText("删除公式");
        deleteBtn.setTextColor(Color.WHITE);
        deleteBtn.setTextSize(12);
        deleteBtn.setPadding(8, 8, 8, 8);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFormula(formula.getId());
            }
        });
        card.addView(deleteBtn);

        return card;
    }

    /**
     * 保存公式
     */
    private void saveFormula(final String targetField, final String formula) {
        if (targetField == null || targetField.isEmpty()) {
            ToastUtil.show(this, "请选择目标字段");
            return;
        }

        if (formula == null || formula.trim().isEmpty()) {
            ToastUtil.show(this, "请输入计算公式");
            return;
        }

        // 显示进度对话框
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在保存公式...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean success = false;
                String errorMessage = null;

                try {
                    String company = yhRenShiUser.getL().replace("_hr", "");
                    success = dongTaiMingXiService.saveFormula(targetField, formula, company);
                } catch (Exception e) {
                    e.printStackTrace();
                    errorMessage = e.getMessage();
                    success = false;
                }

                final boolean finalSuccess = success;
                final String finalErrorMessage = errorMessage;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        if (finalSuccess) {
                            ToastUtil.show(DongTaiMingXiActivity.this, "公式保存成功");

                            // 延迟后重新加载数据
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // 在新的线程中计算公式并重新加载
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                String company = yhRenShiUser.getL().replace("_hr", "");
                                                dongTaiMingXiService.calculateAndUpdateAllData(
                                                        company, dynamicTitles);

                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        loadAllData();
                                                    }
                                                });
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        ToastUtil.show(DongTaiMingXiActivity.this,
                                                                "计算数据时出错: " + e.getMessage());
                                                        loadAllData(); // 仍然尝试加载数据
                                                    }
                                                });
                                            }
                                        }
                                    }).start();
                                }
                            }, 300);
                        } else {
                            String message = "公式保存失败";
                            if (finalErrorMessage != null) {
                                message += ": " + finalErrorMessage;
                            }
                            ToastUtil.show(DongTaiMingXiActivity.this, message);
                        }
                    }
                });
            }
        }).start();
    }

    /**
     * 删除公式
     */
    private void deleteFormula(final int formulaId) {
        new AlertDialog.Builder(this)
                .setTitle("确认删除")
                .setMessage("确定要删除这个公式吗？")
                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String company = yhRenShiUser.getL().replace("_hr", "");
                                    boolean success = dongTaiMingXiService.deleteFormula(formulaId, company);

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (success) {
                                                ToastUtil.show(DongTaiMingXiActivity.this, "公式已删除");

                                                // 重新计算公式
                                                new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        dongTaiMingXiService.calculateAndUpdateAllData(
                                                                company, dynamicTitles);
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                loadAllData();
                                                            }
                                                        });
                                                    }
                                                }).start();
                                            } else {
                                                ToastUtil.show(DongTaiMingXiActivity.this, "删除失败");
                                            }
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    /**
     * 添加新行
     */
    private void addNewRow() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String company = yhRenShiUser.getL().replace("_hr", "");

                    // 创建空数据
                    StringBuilder dataBuilder = new StringBuilder();
                    for (int i = 0; i < dynamicTitles.size(); i++) {
                        if (i > 0) {
                            dataBuilder.append("|||");
                        }
                        dataBuilder.append("");
                    }
                    String dataStr = dataBuilder.toString();

                    boolean success = dongTaiMingXiService.insert(company, dataStr);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (success) {
                                ToastUtil.show(DongTaiMingXiActivity.this, "添加成功");
                                loadAllData();
                            } else {
                                ToastUtil.show(DongTaiMingXiActivity.this, "添加失败");
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.show(DongTaiMingXiActivity.this, "添加异常: " + e.getMessage());
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * 显示操作说明
     */
    public void showGuide(View v) {
        String message = "操作步骤说明：\n" +
                "1. 页面中点击配置标题按钮，在弹窗中配置需要列内容；\n" +
                "2. 点击公式配置按钮，目标字段内容为计算结果赋值字段；\n" +
                "3. 在计算公式中选择已有的计算符，选择下方遍历字段或手动输入数字生成对应计算公式；\n" +
                "4. 在已保存公式中可以删除配置好的内容；\n\n" +
                "注意事项：\n" +
                "1. 在配置标题中删除字段时需要先解除该字段涉及公式内容；\n" +
                "2. 公式设计建议从基础字段到高级字段顺序设置；\n" +
                "3. 涉及到计算的内容要输入数值内容；";

        new AlertDialog.Builder(this)
                .setTitle("操作说明")
                .setMessage(message)
                .setPositiveButton("知道了", null)
                .show();
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