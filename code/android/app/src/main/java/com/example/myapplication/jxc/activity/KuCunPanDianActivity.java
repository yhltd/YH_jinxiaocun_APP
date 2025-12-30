package com.example.myapplication.jxc.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.XiangQingYeActivity;
import com.example.myapplication.entity.XiangQingYe;
import com.example.myapplication.jxc.entity.YhJinXiaoCun;
import com.example.myapplication.jxc.entity.YhJinXiaoCunMingXi;
import com.example.myapplication.jxc.entity.YhJinXiaoCunUser;
import com.example.myapplication.jxc.entity.YhJinXiaoCunCangKu;
import com.example.myapplication.jxc.service.YhJinXiaoCunMingXiService;
import com.example.myapplication.jxc.service.YhJinXiaoCunService;
import com.example.myapplication.jxc.service.YhJinXiaoCunCangKuService;
import com.example.myapplication.utils.ExcelUtil;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class KuCunPanDianActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_CHANG = 100;
    //商品二维码
    private static final int REQUEST_CODE_SCAN = 101;
    //订单二维码
    private static final int ORDER_CODE_SCAN = 102;

    private YhJinXiaoCunUser yhJinXiaoCunUser;
    private YhJinXiaoCunMingXi YhJinXiaoCunMingxi;
    private YhJinXiaoCunMingXiService yhJinXiaoCunMingXiService;
    private YhJinXiaoCunCangKuService yhJinXiaoCunCangKuService;
    private String stop_dateText;
    private List<YhJinXiaoCunMingXi> list;
    private List<YhJinXiaoCunMingXi> order_list;
    private List<YhJinXiaoCunCangKu> cangkuList; // 新增：仓库列表
    private EditText js;
    private String churuku;
    private ListView listView;
    private Button sel_button;
    private Button product_qr;
    private EditText search_text;
    private Spinner cangkuSpinner; // 改为Spinner

    // 添加标志位，防止仓库下拉框触发查询
    private boolean isFirstLoad = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kucunpandian);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        yhJinXiaoCunUser = myApplication.getYhJinXiaoCunUser();

        listView = findViewById(R.id.product_list);
        listView.setCacheColorHint(Color.TRANSPARENT);
        sel_button = findViewById(R.id.sel_button);
        product_qr = findViewById(R.id.product_qr);
        search_text = findViewById(R.id.product_search);
        cangkuSpinner = findViewById(R.id.cangku); // 改为Spinner
        js = findViewById(R.id.js);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        // 页面加载时查询一次数据（初始化）
        initList();

        sel_button.setOnClickListener(selClick());

        // 设置仓库下拉框的监听 - 选择时不触发查询
        cangkuSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 第一次加载时不处理（防止初始化触发查询）
                if (isFirstLoad) {
                    isFirstLoad = false;
                    return;
                }

                // 选择仓库时不触发查询，只有点击查询按钮才触发
                // 这里可以什么都不做，或者只记录选择状态
                Log.d("Cangku", "仓库选择改变，但不会触发查询，需点击查询按钮");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 什么都不选时的处理
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // ViewHolder类
    class ViewHolder {
        public TextView name;
        public TextView spDm;
        public TextView leiBie;
        public TextView danWei;
        public TextView jc_sl;
        public EditText panku_sl;
        public TextView pankui_sl;
        public TextView cangku;
        public TextWatcher textWatcher;
    }

    class MyAdapter extends BaseAdapter {
        Context context;
        private LayoutInflater inflater = null;

        public MyAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            View view;

            if (convertView == null) {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.kucunpandian_row, null);

                holder.name = view.findViewById(R.id.name);
                holder.spDm = view.findViewById(R.id.spDm);
                holder.leiBie = view.findViewById(R.id.leiBie);
                holder.jc_sl = view.findViewById(R.id.jc_sl);
                holder.cangku = view.findViewById(R.id.cangku);
                holder.panku_sl = view.findViewById(R.id.panku_sl);
                holder.pankui_sl = view.findViewById(R.id.pankui_sl);

                view.setTag(holder);
            } else {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            }

            // 设置数据
            holder.name.setText(list.get(position).getCpname());
            holder.spDm.setText(list.get(position).getSpDm());
            holder.leiBie.setText(list.get(position).getCplb());
            holder.jc_sl.setText(list.get(position).getkucun_sl());
            holder.cangku.setText(list.get(position).getcangku());

            // 移除旧的TextWatcher
            if (holder.textWatcher != null) {
                holder.panku_sl.removeTextChangedListener(holder.textWatcher);
            }

            // 设置盘库数量
            holder.panku_sl.setText(list.get(position).getpankusl());

            // 计算并设置盘亏数量
            calculateAndSetPankui(holder, position);

            // 方法1：使用final变量
            final ViewHolder finalHolder = holder;
            final int finalPosition = position;

            // 创建新的TextWatcher
            holder.textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    list.get(finalPosition).setpankusl(s.toString());
                    calculateAndSetPankui(finalHolder, finalPosition);
                }
            };

            holder.panku_sl.addTextChangedListener(holder.textWatcher);

            return view;
        }
    }

    // 计算盘亏数量的方法
    private void calculateAndSetPankui(ViewHolder holder, int position) {
        try {
            String kucunStr = list.get(position).getkucun_sl();
            String pankuStr = list.get(position).getpankusl();

            double kucunSl = TextUtils.isEmpty(kucunStr) ? 0 : Double.parseDouble(kucunStr);
            double pankuSl = TextUtils.isEmpty(pankuStr) ? 0 : Double.parseDouble(pankuStr);

            double pankuiSl = kucunSl - pankuSl;

            holder.pankui_sl.setText(String.valueOf(pankuiSl));
            list.get(position).setpankuisl(String.valueOf(pankuiSl));
        } catch (NumberFormatException e) {
            holder.pankui_sl.setText("0");
            list.get(position).setpankuisl("0");
        }
    }

    public void onScanClick(View view) {
        Intent intent = new Intent(this, ScanActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SCAN);
    }

    public void onOrderScanClick(View view) {
        Intent intent = new Intent(this, ScanActivity.class);
        startActivityForResult(intent, ORDER_CODE_SCAN);
    }

    private void initList() {
        sel_button.setEnabled(false);
        stop_dateText = js.getText().toString();

        if(stop_dateText.equals("")){
            stop_dateText = "2100/12/31";
        }

        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                // 初始化仓库下拉框
                initCangkuSpinner();
                // 使用Activity的context
                listView.setAdapter(new MyAdapter(KuCunPanDianActivity.this));
                sel_button.setEnabled(true);
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                yhJinXiaoCunMingXiService = new YhJinXiaoCunMingXiService();
                yhJinXiaoCunCangKuService = new YhJinXiaoCunCangKuService();

                // 获取仓库列表
                cangkuList = yhJinXiaoCunCangKuService.getList(yhJinXiaoCunUser.getGongsi());

                // 获取选中的仓库 - 第一次加载时使用默认值
                String selectedCangku = "";
                if (!isFirstLoad && cangkuSpinner.getSelectedItemPosition() > 0) {
                    selectedCangku = cangkuSpinner.getSelectedItem().toString();
                }

                // 获取库存盘点数据
                list = yhJinXiaoCunMingXiService.kcpdQuery(yhJinXiaoCunUser.getGongsi(), stop_dateText,
                        search_text.getText().toString(), selectedCangku);

                order_list = yhJinXiaoCunMingXiService.getList(yhJinXiaoCunUser.getGongsi());

                Message msg = new Message();
                msg.obj = list;
                listLoadHandler.sendMessage(msg);
            }
        }).start();
    }

    // 初始化仓库下拉框
    private void initCangkuSpinner() {
        if (cangkuList != null && !cangkuList.isEmpty()) {
            List<String> cangkuNames = new ArrayList<>();
            cangkuNames.add("全部仓库"); // 添加默认选项

            for (YhJinXiaoCunCangKu cangku : cangkuList) {
                if (cangku.getcangku() != null && !cangku.getcangku().isEmpty()) {
                    cangkuNames.add(cangku.getcangku());
                }
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_item,
                    cangkuNames
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            cangkuSpinner.setAdapter(adapter);
        }
    }

    public View.OnClickListener selClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击查询按钮时触发查询
                queryData();
            }
        };
    }

    // 查询数据的方法（点击查询按钮时调用）
    private void queryData() {
        sel_button.setEnabled(false);
        stop_dateText = js.getText().toString();

        if(stop_dateText.equals("")){
            stop_dateText = "2100/12/31";
        }

        Handler queryHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                // 刷新列表数据
                listView.setAdapter(new MyAdapter(KuCunPanDianActivity.this));
                sel_button.setEnabled(true);
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                // 获取当前选中的仓库
                String selectedCangku = "";
                if (cangkuSpinner.getSelectedItemPosition() > 0) {
                    selectedCangku = cangkuSpinner.getSelectedItem().toString();
                }

                // 重新查询盘点数据
                list = yhJinXiaoCunMingXiService.kcpdQuery(yhJinXiaoCunUser.getGongsi(), stop_dateText,
                        search_text.getText().toString(), selectedCangku);

                Message msg = new Message();
                msg.obj = list;
                queryHandler.sendMessage(msg);
            }
        }).start();
    }

    public void onInsertClick(View v) {
        List<YhJinXiaoCunMingXi> jczlList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getpankusl() != null && !list.get(i).getpankusl().equals("") && !list.get(i).getpankuisl().equals("0.0")) {
                jczlList.add(list.get(i));
            }
        }

        if (jczlList.size() == 0) {
            ToastUtil.show(KuCunPanDianActivity.this, "失败,未填写盘库数据或盘亏数量为零！");
            return;
        }

        // 打印最终传递的数据
        Log.e("CangkuDebug", "=== 最终传递的数据 ===");
        Log.e("CangkuDebug", "传递的记录数量: " + jczlList.size());
        for (int i = 0; i < jczlList.size(); i++) {
            YhJinXiaoCunMingXi item = jczlList.get(i);
            Log.e("CangkuDebug", "传递的第" + (i+1) + "条 - cangku: " + item.getcangku() +
                    ", 商品: " + item.getCpname() +
                    ", 盘库数量: " + item.getpankusl());
        }

        Intent intent = new Intent(KuCunPanDianActivity.this, KuCunPanDianChangeActivity.class);
        intent.putExtra("jczlList", (Serializable) jczlList);
        intent.putExtra("churuku", churuku);
        startActivityForResult(intent, REQUEST_CODE_CHANG);
    }
}