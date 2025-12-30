package com.example.myapplication.finance.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.finance.entity.YhFinanceJiJianPeiZhi;
import com.example.myapplication.finance.entity.YhFinanceUser;
import com.example.myapplication.finance.entity.YhFinanceShuiLv;
import com.example.myapplication.finance.entity.YhFinanceWaiBi;
import com.example.myapplication.finance.service.YhFinanceInvoicePeizhiService;
import com.example.myapplication.finance.service.YhFinanceKehuPeizhiService;
import com.example.myapplication.finance.service.YhFinanceSimpleAccountingService;
import com.example.myapplication.finance.service.YhFinanceShuiLvService;
import com.example.myapplication.finance.service.YhFinanceWaiBiService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JiJianPeiZhiActivity extends AppCompatActivity {

    private final static int REQUEST_CODE_CHANG = 1;

    private YhFinanceUser yhFinanceUser;
    private YhFinanceSimpleAccountingService yhFinanceSimpleAccountingService;
    private YhFinanceKehuPeizhiService yhFinanceKehuPeizhiService;
    private YhFinanceInvoicePeizhiService yhFinanceInvoicePeizhiService;
    private YhFinanceShuiLvService yhFinanceShuiLvService;
    private YhFinanceWaiBiService yhFinanceWaiBiService; // 添加外币Service

    private ListView listView1;
    private Spinner type_select;
    private String type_selectText;

    private TextView column1Title;
    private TextView column2Title;

    List<YhFinanceJiJianPeiZhi> list1;
    List<YhFinanceShuiLv> shuiLvList;
    List<YhFinanceWaiBi> waiBiList; // 添加外币列表

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jijianpeizhi);

        // 初始化控件
        type_select = findViewById(R.id.type_select);
        listView1 = findViewById(R.id.jingyingshouru_list);
        column1Title = findViewById(R.id.column1_title);
        column2Title = findViewById(R.id.column2_title);

        // 设置Spinner适配器
        String[] type_selectArray = getResources().getStringArray(R.array.jijian_type);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, type_selectArray);
        type_select.setAdapter(adapter);
        type_select.setOnItemSelectedListener(new typeSelectSelectedListener());

        // 设置ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // 获取用户信息
        MyApplication myApplication = (MyApplication) getApplication();
        yhFinanceUser = myApplication.getYhFinanceUser();

        // 默认选择第一项并加载数据
        if (type_selectArray.length > 0) {
            type_selectText = type_selectArray[0];
            updateTableHeader();
            initList();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class typeSelectSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            type_selectText = type_select.getItemAtPosition(position).toString();
            updateTableHeader();
            initList();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }

    // 更新表头显示
    private void updateTableHeader() {
        if (type_selectText.equals("税率")) {
            column1Title.setText("税率");
            column2Title.setVisibility(View.VISIBLE);
            column2Title.setText("临界值");
        } else if (type_selectText.equals("外币")) {
            column1Title.setText("币种");
            column2Title.setVisibility(View.VISIBLE);
            column2Title.setText("汇率");
        } else {
            column1Title.setText(type_selectText);
            column2Title.setVisibility(View.GONE);
        }
    }

    private void initList() {
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                LoadingDialog.getInstance(JiJianPeiZhiActivity.this).dismiss();

                if (msg.obj != null) {
                    listView1.setAdapter(StringUtils.cast(msg.obj));
                }

                // 检查数据是否为空
                if ((type_selectText.equals("税率") && (shuiLvList == null || shuiLvList.isEmpty())) ||
                        (type_selectText.equals("外币") && (waiBiList == null || waiBiList.isEmpty())) ||
                        (!type_selectText.equals("税率") && !type_selectText.equals("外币") &&
                                (list1 == null || list1.isEmpty()))) {
                    ToastUtil.show(JiJianPeiZhiActivity.this, "暂无数据");
                }

                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<HashMap<String, Object>> data = new ArrayList<>();

                try {
                    if (type_selectText.equals("科目")) {
                        yhFinanceSimpleAccountingService = new YhFinanceSimpleAccountingService();
                        list1 = yhFinanceSimpleAccountingService.getList(yhFinanceUser.getCompany());
                        if (list1 == null) list1 = new ArrayList<>();
                        for (int i = 0; i < list1.size(); i++) {
                            HashMap<String, Object> item = new HashMap<>();
                            item.put("column1", list1.get(i).getPeizhi());
                            data.add(item);
                        }
                    } else if (type_selectText.equals("客户/供应商/往来单位")) {
                        yhFinanceKehuPeizhiService = new YhFinanceKehuPeizhiService();
                        list1 = yhFinanceKehuPeizhiService.getList(yhFinanceUser.getCompany());
                        if (list1 == null) list1 = new ArrayList<>();
                        for (int i = 0; i < list1.size(); i++) {
                            HashMap<String, Object> item = new HashMap<>();
                            item.put("column1", list1.get(i).getPeizhi());
                            data.add(item);
                        }
                    } else if (type_selectText.equals("发票种类")) {
                        yhFinanceInvoicePeizhiService = new YhFinanceInvoicePeizhiService();
                        list1 = yhFinanceInvoicePeizhiService.getList(yhFinanceUser.getCompany());
                        if (list1 == null) list1 = new ArrayList<>();
                        for (int i = 0; i < list1.size(); i++) {
                            HashMap<String, Object> item = new HashMap<>();
                            item.put("column1", list1.get(i).getPeizhi());
                            data.add(item);
                        }
                    } else if (type_selectText.equals("税率")) {
                        if (yhFinanceShuiLvService == null) {
                            yhFinanceShuiLvService = new YhFinanceShuiLvService();
                        }
                        shuiLvList = yhFinanceShuiLvService.getList(yhFinanceUser.getCompany());
                        if (shuiLvList == null) shuiLvList = new ArrayList<>();
                        for (int i = 0; i < shuiLvList.size(); i++) {
                            HashMap<String, Object> item = new HashMap<>();
                            item.put("column1", shuiLvList.get(i).getShuilv() != null ? shuiLvList.get(i).getShuilv() : "");
                            item.put("column2", formatLinjiezhi(shuiLvList.get(i).getLinjiezhi()));
                            data.add(item);
                        }
                    } else if (type_selectText.equals("外币")) {
                        if (yhFinanceWaiBiService == null) {
                            yhFinanceWaiBiService = new YhFinanceWaiBiService();
                        }
                        waiBiList = yhFinanceWaiBiService.getList(yhFinanceUser.getCompany());
                        if (waiBiList == null) waiBiList = new ArrayList<>();
                        for (int i = 0; i < waiBiList.size(); i++) {
                            HashMap<String, Object> item = new HashMap<>();
                            item.put("column1", waiBiList.get(i).getBizhong() != null ? waiBiList.get(i).getBizhong() : "");
                            item.put("column2", formatHuilv(waiBiList.get(i).getHuilv()));
                            data.add(item);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.show(JiJianPeiZhiActivity.this, "数据加载失败: " + e.getMessage());
                        }
                    });
                }

                // 根据选择的类型使用不同的布局
                int layoutResId;
                String[] from;
                int[] to;

                if (type_selectText.equals("税率") || type_selectText.equals("外币")) {
                    layoutResId = R.layout.jijianpeizhi_row_shuilv;
                    from = new String[]{"column1", "column2"};
                    to = new int[]{R.id.column1, R.id.column2};
                } else {
                    layoutResId = R.layout.expenditure_jingyingshouru_row;
                    from = new String[]{"column1"};
                    to = new int[]{R.id.shouru};
                }

                SimpleAdapter adapter = new SimpleAdapter(JiJianPeiZhiActivity.this,
                        data, layoutResId, from, to) {

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        final LinearLayout view = (LinearLayout) super.getView(position, convertView, parent);
                        LinearLayout linearLayout = (LinearLayout) view.getChildAt(0);
                        linearLayout.setOnLongClickListener(onItemLongClick1());
                        linearLayout.setOnClickListener(updateClick1());
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

    // 格式化临界值显示
    private String formatLinjiezhi(String linjiezhi) {
        if (linjiezhi == null || linjiezhi.trim().isEmpty()) {
            return "0.00";
        }
        try {
            Double value = Double.parseDouble(linjiezhi);
            return String.format("%.4f", value);
        } catch (NumberFormatException e) {
            return linjiezhi;
        }
    }

    // 格式化汇率显示
    private String formatHuilv(String huilv) {
        if (huilv == null || huilv.trim().isEmpty()) {
            return "0.0000";
        }
        try {
            Double value = Double.parseDouble(huilv);
            return String.format("%.4f", value);
        } catch (NumberFormatException e) {
            return huilv;
        }
    }

    public View.OnClickListener updateClick1() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = Integer.parseInt(view.getTag().toString());
                Intent intent = new Intent(JiJianPeiZhiActivity.this, JiJianPeiZhiChangeActivity.class);
                intent.putExtra("type", R.id.update_btn);
                intent.putExtra("service", type_selectText);

                MyApplication myApplication = (MyApplication) getApplication();

                if (type_selectText.equals("税率")) {
                    if (shuiLvList != null && position < shuiLvList.size()) {
                        myApplication.setObj(shuiLvList.get(position));
                    } else {
                        ToastUtil.show(JiJianPeiZhiActivity.this, "数据异常，请刷新重试");
                        return;
                    }
                } else if (type_selectText.equals("外币")) {
                    if (waiBiList != null && position < waiBiList.size()) {
                        myApplication.setObj(waiBiList.get(position));
                    } else {
                        ToastUtil.show(JiJianPeiZhiActivity.this, "数据异常，请刷新重试");
                        return;
                    }
                } else {
                    if (list1 != null && position < list1.size()) {
                        myApplication.setObj(list1.get(position));
                    } else {
                        ToastUtil.show(JiJianPeiZhiActivity.this, "数据异常，请刷新重试");
                        return;
                    }
                }

                startActivityForResult(intent, REQUEST_CODE_CHANG);
            }
        };
    }

    public View.OnLongClickListener onItemLongClick1() {
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int position = Integer.parseInt(view.getTag().toString());

                // 检查数据有效性
                if (type_selectText.equals("税率")) {
                    if (shuiLvList == null || position >= shuiLvList.size()) {
                        ToastUtil.show(JiJianPeiZhiActivity.this, "数据异常");
                        return true;
                    }
                } else if (type_selectText.equals("外币")) {
                    if (waiBiList == null || position >= waiBiList.size()) {
                        ToastUtil.show(JiJianPeiZhiActivity.this, "数据异常");
                        return true;
                    }
                } else {
                    if (list1 == null || position >= list1.size()) {
                        ToastUtil.show(JiJianPeiZhiActivity.this, "数据异常");
                        return true;
                    }
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(JiJianPeiZhiActivity.this);

                String itemName = "";
                if (type_selectText.equals("税率")) {
                    itemName = shuiLvList.get(position).getShuilv();
                } else if (type_selectText.equals("外币")) {
                    itemName = waiBiList.get(position).getBizhong();
                } else {
                    itemName = list1.get(position).getPeizhi();
                }

                Handler deleteHandler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message msg) {
                        LoadingDialog.getInstance(JiJianPeiZhiActivity.this).dismiss();
                        if ((boolean) msg.obj) {
                            ToastUtil.show(JiJianPeiZhiActivity.this, "删除成功");
                            initList();
                        } else {
                            ToastUtil.show(JiJianPeiZhiActivity.this, "删除失败");
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
                                boolean result = false;
                                try {
                                    if (type_selectText.equals("科目")) {
                                        result = yhFinanceSimpleAccountingService.delete(list1.get(position).getId());
                                    } else if (type_selectText.equals("客户/供应商/往来单位")) {
                                        result = yhFinanceKehuPeizhiService.delete(list1.get(position).getId());
                                    } else if (type_selectText.equals("发票种类")) {
                                        result = yhFinanceInvoicePeizhiService.delete(list1.get(position).getId());
                                    } else if (type_selectText.equals("税率")) {
                                        result = yhFinanceShuiLvService.delete(shuiLvList.get(position).getId());
                                    } else if (type_selectText.equals("外币")) {
                                        result = yhFinanceWaiBiService.delete(waiBiList.get(position).getId());
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    result = false;
                                }
                                msg.obj = result;
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

                builder.setMessage("确定删除 '" + itemName + "' 吗？");
                builder.setTitle("删除确认");
                builder.show();
                return true;
            }
        };
    }

    public void onInsertClick(View v) {
        Intent intent = new Intent(JiJianPeiZhiActivity.this, JiJianPeiZhiChangeActivity.class);
        intent.putExtra("type", R.id.insert_btn);
        intent.putExtra("service", type_selectText);
        startActivityForResult(intent, REQUEST_CODE_CHANG);
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

    @Override
    protected void onResume() {
        super.onResume();
        // 当Activity重新显示时，刷新数据
        if (type_selectText != null) {
            initList();
        }
    }
}