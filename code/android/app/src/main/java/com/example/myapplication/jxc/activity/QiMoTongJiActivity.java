package com.example.myapplication.jxc.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.XiangQingYeActivity;
import com.example.myapplication.entity.XiangQingYe;
import com.example.myapplication.jxc.entity.YhJinXiaoCun;
import com.example.myapplication.jxc.entity.YhJinXiaoCunCangKu;
import com.example.myapplication.jxc.entity.YhJinXiaoCunMingXi;
import com.example.myapplication.jxc.entity.YhJinXiaoCunUser;
import com.example.myapplication.jxc.service.YhJinXiaoCunCangKuService;
import com.example.myapplication.jxc.service.YhJinXiaoCunMingXiService;
import com.example.myapplication.jxc.service.YhJinXiaoCunService;
import com.example.myapplication.utils.ExcelUtil;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class QiMoTongJiActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_CHANG = 1;

    //订单二维码
    private static final int ORDER_CODE_SCAN = 102;

    private YhJinXiaoCunUser yhJinXiaoCunUser;
    private YhJinXiaoCunService yhJinXiaoCunService;
    private YhJinXiaoCunMingXiService yhJinXiaoCunMingXiService;
    private YhJinXiaoCunCangKuService yhJinXiaoCunCangKuService; // 新增仓库服务
    private List<YhJinXiaoCunMingXi> order_list;
    private List<YhJinXiaoCunCangKu> cangkuList; // 从数据库获取的仓库列表

    private String where_sql;
    private Spinner cangku_text; // 改为Spinner
    private EditText ks;
    private EditText js;
    private String start_dateText;
    private String stop_dateText;
    private ListView listView;
    private ListView listView_block;
    private HorizontalScrollView list_table;
    private SimpleAdapter adapter;
    private SimpleAdapter adapter_block;
    private Button sel_button;
    private Button export_button;

    List<YhJinXiaoCun> list;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qimotongi);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        cangku_text = findViewById(R.id.cangku_text); // 现在这是Spinner
        listView = findViewById(R.id.jinxiaocun_list);
        listView_block = findViewById(R.id.list_block);
        list_table = findViewById(R.id.list_table);
        sel_button = findViewById(R.id.sel_button);
        export_button=findViewById(R.id.export);
        ks = findViewById(R.id.ks);
        js = findViewById(R.id.js);

        MyApplication myApplication = (MyApplication) getApplication();
        yhJinXiaoCunUser = myApplication.getYhJinXiaoCunUser();

        // 初始化仓库服务
        yhJinXiaoCunCangKuService = new YhJinXiaoCunCangKuService();

        // 初始化仓库下拉框数据（从数据库获取）
        initCangkuDataFromDB();

        initList();
        sel_button.setOnClickListener(selClick());
        export_button.setOnClickListener(exportClick());

        sel_button.requestFocus();

        showDateOnClick(ks);
        showDateOnClick(js);


    }

    // 从数据库获取仓库数据填充下拉框
    private void initCangkuDataFromDB() {
        Handler cangkuHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (cangkuList != null && !cangkuList.isEmpty()) {
                    List<String> cangkuNames = new ArrayList<>();
                    cangkuNames.add("全部仓库"); // 添加默认选项

                    for (YhJinXiaoCunCangKu cangku : cangkuList) {
                        if (cangku.getcangku() != null && !cangku.getcangku().isEmpty()) {
                            cangkuNames.add(cangku.getcangku());
                        }
                    }

                    // 设置适配器
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            QiMoTongJiActivity.this,
                            android.R.layout.simple_spinner_item,
                            cangkuNames
                    );
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    cangku_text.setAdapter(adapter);

                    Log.d("QiMoTongJi", "成功加载仓库数据: " + cangkuNames.size() + " 个仓库");
                } else {
                    Log.e("QiMoTongJi", "未获取到仓库数据");
                    // 如果数据库没有数据，设置一个默认的适配器
                    List<String> defaultCangku = new ArrayList<>();
                    defaultCangku.add("全部仓库");
                    ArrayAdapter<String> defaultAdapter = new ArrayAdapter<>(
                            QiMoTongJiActivity.this,
                            android.R.layout.simple_spinner_item,
                            defaultCangku
                    );
                    defaultAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    cangku_text.setAdapter(defaultAdapter);
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 从yh_jinxiaocun_cangku表获取仓库数据
                    cangkuList = yhJinXiaoCunCangKuService.getList(yhJinXiaoCunUser.getGongsi());
                    Message msg = new Message();
                    msg.obj = cangkuList;
                    cangkuHandler.sendMessage(msg);
                } catch (Exception e) {
                    Log.e("QiMoTongJi", "获取仓库数据异常: " + e.getMessage());
                    e.printStackTrace();
                    cangkuHandler.sendEmptyMessage(0);
                }
            }
        }).start();
    }

    @SuppressLint("WrongConstant")
    public void switchClick(View v) {
        if(listView_block.getVisibility() == 0){
            listView_block.setVisibility(8);
            list_table.setVisibility(0);
        }else if(listView_block.getVisibility() == 8){
            listView_block.setVisibility(0);
            list_table.setVisibility(8);
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

    @SuppressLint("ClickableViewAccessibility")
    protected void showDateOnClick(final EditText editText) {
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    showYearMonthPickDlg(editText);
                    return true;
                }
                return false;
            }
        });
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    showYearMonthPickDlg(editText);
                }

            }
        });
    }

    protected void showYearMonthPickDlg(final EditText editText) {
        Calendar calendar = Calendar.getInstance();
        final int currentYear = calendar.get(Calendar.YEAR);
        final int currentMonth = calendar.get(Calendar.MONTH) + 1;

        // 创建包含两个NumberPicker的布局
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER);
        layout.setPadding(50, 30, 50, 30);

        final NumberPicker yearPicker = new NumberPicker(this);
        final NumberPicker monthPicker = new NumberPicker(this);

        // 设置年份（最近10年）
        int startYear = 1900;
        int endYear = 2100;
        String[] years = new String[endYear - startYear + 1];
        for (int i = 0; i < years.length; i++) {
            years[i] = String.valueOf(startYear + i);
        }

        yearPicker.setMinValue(0);
        yearPicker.setMaxValue(years.length - 1);
        yearPicker.setDisplayedValues(years);
        yearPicker.setValue(5); // 默认当前年份

        // 设置月份
        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);
        monthPicker.setValue(currentMonth);

        // 设置布局参数
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1
        );
        params.setMargins(10, 0, 10, 0);

        layout.addView(yearPicker, params);

        TextView yearText = new TextView(this);
        yearText.setText("年");
        yearText.setTextSize(16);
        layout.addView(yearText);

        layout.addView(monthPicker, params);

        TextView monthText = new TextView(this);
        monthText.setText("月");
        monthText.setTextSize(16);
        layout.addView(monthText);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(layout)
                .setTitle("选择年月")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int selectedYear = Integer.parseInt(years[yearPicker.getValue()]);
                        int selectedMonth = monthPicker.getValue();
                        editText.setText(selectedYear + "/" + selectedMonth);
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }



    // 工具方法：将年月转换为完整日期
    private String convertYearMonthToFullDate(String yearMonth, boolean isStart) {
        if (yearMonth.equals("")) {
            return isStart ? "1900-01-01 00:00:00" : "2100-12-31 23:59:59";
        }

        String[] parts = yearMonth.split("/");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);

        if (isStart) {
            // 开始日期：当月1号
            return String.format("%d-%02d-01 00:00:00", year, month);
        } else {
            // 结束日期：下个月1号（用于BETWEEN查询）
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month - 1, 1);
            calendar.add(Calendar.MONTH, 1);
            return String.format("%d-%02d-01 00:00:00",
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1);
        }
    }
    private void initList() {
        sel_button.setEnabled(false);
        start_dateText = ks.getText().toString();
        stop_dateText = js.getText().toString();
        if(start_dateText.equals("")){
            start_dateText = "1900/01";
        }
        if(stop_dateText.equals("")){
            stop_dateText = "2100/12";
        }
        String dbStartDate = convertYearMonthToFullDate(start_dateText, true);
        String dbEndDate = convertYearMonthToFullDate(stop_dateText, false);

        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                listView.setAdapter(StringUtils.cast(adapter));
                listView_block.setAdapter(StringUtils.cast(adapter_block));
                sel_button.setEnabled(true);
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<HashMap<String, Object>> data = new ArrayList<>();
                try {
                    yhJinXiaoCunService = new YhJinXiaoCunService();
                    yhJinXiaoCunMingXiService = new YhJinXiaoCunMingXiService();

                    // 获取选中的仓库值
                    String selectedCangku = "";
                    if (cangku_text.getSelectedItem() != null) {
                        String selected = cangku_text.getSelectedItem().toString();
                        if (!"全部仓库".equals(selected)) { // 如果选择的是"全部仓库"，传空字符串
                            selectedCangku = selected;
                        }
                    }

                    list = yhJinXiaoCunService.qimotongji_shaixuan(yhJinXiaoCunUser.getGongsi(), dbStartDate, dbEndDate, selectedCangku);
                    Log.e("SQLDebug", "查询统计SQL - 公司: " + yhJinXiaoCunUser.getGongsi() +
                            ", 开始: " + start_dateText +
                            ", 结束: " + stop_dateText +
                            ", 仓库: " + selectedCangku);
                    order_list = yhJinXiaoCunMingXiService.getList(yhJinXiaoCunUser.getGongsi());
                    if (list == null) return;

                    for (int i = 0; i < list.size(); i++) {
                        HashMap<String, Object> item = new HashMap<>();
                        item.put("cangku", list.get(i).getcangku());
                        item.put("month", list.get(i).getmonth());
                        item.put("mx_ruku_cpsl", list.get(i).getMx_ruku_cpsl());
                        item.put("mx_ruku_price", list.get(i).getMx_ruku_price());
                        item.put("mx_chuku_cpsl", list.get(i).getMx_chuku_cpsl());
                        item.put("mx_chuku_price", list.get(i).getMx_chuku_price());
                        item.put("jc_sl", list.get(i).getJc_sl());
                        item.put("jc_price", list.get(i).getJc_price());

                        data.add(item);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                adapter = new SimpleAdapter(QiMoTongJiActivity.this, data, R.layout.qimotongji_row, new String[]{"cangku", "month", "mx_ruku_cpsl", "mx_ruku_price", "mx_chuku_cpsl", "mx_chuku_price", "jc_sl", "jc_price"}, new int[]{R.id.cangku, R.id.month, R.id.mx_ruku_cpsl, R.id.mx_ruku_price, R.id.mx_chuku_cpsl, R.id.mx_chuku_price, R.id.jc_sl, R.id.jc_price}) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        final LinearLayout view = (LinearLayout) super.getView(position, convertView, parent);
                        LinearLayout linearLayout = (LinearLayout) view.getChildAt(0);
//                        linearLayout.setOnClickListener(updateClick());
                        linearLayout.setTag(position);
                        return view;
                    }
                };

                adapter_block = new SimpleAdapter(QiMoTongJiActivity.this, data, R.layout.qimotongji_row_block, new String[]{"cangku", "month",  "mx_ruku_cpsl", "mx_ruku_price", "mx_chuku_cpsl", "mx_chuku_price", "jc_sl", "jc_price"}, new int[]{R.id.cangku, R.id.month, R.id.mx_ruku_cpsl, R.id.mx_ruku_price, R.id.mx_chuku_cpsl, R.id.mx_chuku_price, R.id.jc_sl, R.id.jc_price}) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        final LinearLayout view = (LinearLayout) super.getView(position, convertView, parent);
                        LinearLayout linearLayout = (LinearLayout) view.getChildAt(0);
//                        linearLayout.setOnClickListener(updateClick());
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

    public View.OnClickListener selClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initList();
            }
        };
    }

    public View.OnClickListener exportClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] title = { "仓库", "时间",  "入库数量", "入库金额", "出库数量","出库金额","结存数量","结存金额"};
                String fileName = "期末统计" + System.currentTimeMillis() + ".xls";
                ExcelUtil.initExcel(fileName, "期末统计", title);
                ExcelUtil.qimotongjiToExcel(list, fileName, MyApplication.getContext());
                String filePath = null;
                try {
                    filePath = Environment.getExternalStorageDirectory().getCanonicalPath()+"/云合未来一体化系统/" + fileName;
                    Uri uri = Uri.parse("file://" + filePath);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(uri);
                    startActivity(intent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }
}