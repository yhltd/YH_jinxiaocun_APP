package com.example.myapplication.finance.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.XiangQingYeActivity;
import com.example.myapplication.entity.XiangQingYe;
import com.example.myapplication.fenquan.activity.GongZuoTaiActivity;
import com.example.myapplication.finance.entity.YhFinanceBaoBiao;
import com.example.myapplication.finance.entity.YhFinanceUser;
import com.example.myapplication.finance.service.YhFinanceBaoBiaoService;
import com.example.myapplication.jxc.activity.JinXiaoCunActivity;
import com.example.myapplication.mendian.activity.ReportFormActivity;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class BaoBiaoActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_CHANG = 1;
    MyApplication myApplication;

    private YhFinanceUser yhFinanceUser;
    private YhFinanceBaoBiaoService yhFinanceBaoBiaoService;
    private Spinner class_select;
    private EditText this_date;
    private ListView listView;

    private ListView listView_block;
    private HorizontalScrollView list_table;
    private SimpleAdapter adapter;
    private SimpleAdapter adapter_block;

    private String this_dateText;
    private String class_selectText;
    private String start_date;
    private String stop_date;

    private Button sel_button;

    List<YhFinanceBaoBiao> list;
    List<YhFinanceBaoBiao> list1;
    List<YhFinanceBaoBiao> list2;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        myApplication = (MyApplication) getApplication();
        yhFinanceUser = myApplication.getYhFinanceUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baobiao);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //初始化控件
        this_date = findViewById(R.id.this_date);
        showDateOnClick(this_date);
        class_select = findViewById(R.id.class_select);

        String[] class_selectArray = getResources().getStringArray(R.array.baobiao_class_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, class_selectArray);
        class_select.setAdapter(adapter);

        listView = findViewById(R.id.baobiao_list);
        listView_block = findViewById(R.id.list_block);
        list_table = findViewById(R.id.list_table);

        sel_button = findViewById(R.id.sel_button);
        sel_button.setOnClickListener(selClick());
        sel_button.requestFocus();
        initList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
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

    public View.OnClickListener selClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initList();
            }
        };
    }



    private void initList() {
        sel_button.setEnabled(false);
        this_dateText = this_date.getText().toString();
        class_selectText = class_select.getSelectedItem().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("MM");
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String this_year = sdf.format(date);
        String this_month = sdf2.format(date);
        if(class_selectText.equals("年")){
            if(this_dateText.equals("")){
                start_date = this_year + "-01-01" + " 00:00:00";
                stop_date = this_year + "-12-31" + " 23:59:59";
            }else{
                this_year = this_dateText.split("-")[0];
                start_date = this_year + "-01-01" + " 00:00:00";
                stop_date = this_year + "-12-31" + " 23:59:59";
            }
        }else if(class_selectText.equals("月")){
            if(this_dateText.equals("")){
                Integer this_day = getDaysByYearMonth(Integer.valueOf(this_year),Integer.valueOf(this_month));
                String end_day = this_day.toString();
                start_date = this_year + "-" + this_month + "-01" + "-01" + " 00:00:00";
                stop_date = this_year + "-" + this_month + "-" + end_day + " 23:59:59";
            }else{
                this_year = this_dateText.split("-")[0];
                this_month = this_dateText.split("-")[1];
                Integer this_day = getDaysByYearMonth(Integer.valueOf(this_year),Integer.valueOf(this_month));
                String end_day = this_day.toString();
                this_year = this_dateText.split("-")[0];
                start_date = this_year + "-" + this_month + "-01" + " 00:00:00";
                stop_date = this_year + "-" + this_month + "-" + end_day + " 23:59:59";
            }
        }else if(class_selectText.equals("日")){
            if(this_dateText.equals("")){
                String this_day = sdf3.format(date);
                start_date = this_day + " 00:00:00";
                stop_date = this_day + " 23:59:59";
            }else{
                start_date = this_dateText + " 00:00:00";
                stop_date = this_dateText + " 23:59:59";
            }
        }

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
                    yhFinanceBaoBiaoService = new YhFinanceBaoBiaoService();
                    list1 = yhFinanceBaoBiaoService.getList_shou(yhFinanceUser.getCompany(),start_date,stop_date);
                    list2 = yhFinanceBaoBiaoService.getList_zhi(yhFinanceUser.getCompany(),start_date,stop_date);
                    if (list1 == null && list2 == null) return;

                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(list1.size() > list2.size()){
                    for(int i=0; i<list2.size(); i++){
                        list1.get(i).setKehu2(list2.get(i).getKehu2());
                        list1.get(i).setZhaiyao2(list2.get(i).getZhaiyao2());
                        list1.get(i).setCope(list2.get(i).getCope());
                    }
                    list = list1;
                }else{
                    for(int i=0; i<list1.size(); i++){
                        list2.get(i).setKehu(list1.get(i).getKehu());
                        list2.get(i).setZhaiyao(list1.get(i).getZhaiyao());
                        list2.get(i).setReceivable(list1.get(i).getReceivable());
                    }
                    list = list2;
                }

                for (int i = 0; i < list.size(); i++) {
                    HashMap<String, Object> item = new HashMap<>();
                    item.put("zhaiyao", list.get(i).getZhaiyao());
                    item.put("kehu", list.get(i).getKehu());
                    item.put("receivable", list.get(i).getReceivable());
                    item.put("zhaiyao2", list.get(i).getZhaiyao2());
                    item.put("kehu2", list.get(i).getKehu2());
                    item.put("cope", list.get(i).getCope());
                    data.add(item);
                }

                adapter = new SimpleAdapter(BaoBiaoActivity.this, data, R.layout.baobiao_row, new String[]{"zhaiyao","kehu","receivable","zhaiyao2","kehu2","cope"}, new int[]{R.id.zhaiyao,R.id.kehu,R.id.receivable,R.id.zhaiyao2,R.id.kehu2,R.id.cope}) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        final LinearLayout view = (LinearLayout) super.getView(position, convertView, parent);
                        LinearLayout linearLayout = (LinearLayout) view.getChildAt(0);
                        linearLayout.setOnClickListener(updateClick());
                        linearLayout.setTag(position);
                        return view;
                    }
                };

                adapter_block = new SimpleAdapter(BaoBiaoActivity.this, data, R.layout.baobiao_row_block, new String[]{"zhaiyao","kehu","receivable","zhaiyao2","kehu2","cope"}, new int[]{R.id.zhaiyao,R.id.kehu,R.id.receivable,R.id.zhaiyao2,R.id.kehu2,R.id.cope}) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        final LinearLayout view = (LinearLayout) super.getView(position, convertView, parent);
                        LinearLayout linearLayout = (LinearLayout) view.getChildAt(0);
                        linearLayout.setOnClickListener(updateClick());
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

    public static int getDaysByYearMonth(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    @SuppressLint("ClickableViewAccessibility")
    protected void showDateOnClick(final EditText editText) {
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePickDlg(editText);
                    return true;
                }
                return false;
            }
        });
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    showDatePickDlg(editText);
                }

            }
        });
    }

    protected void showDatePickDlg(final EditText editText) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(BaoBiaoActivity.this, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String mon = "";
                String day = "";
                if(monthOfYear + 1 < 10){
                    mon = "0" + (monthOfYear + 1);
                }else{
                    mon = "" + (monthOfYear + 1);
                }
                if(dayOfMonth < 10){
                    day = "0" + dayOfMonth;
                }else{
                    day = "" + dayOfMonth;
                }
                class_selectText = class_select.getSelectedItem().toString();
                if(class_selectText.equals("年")){
                    editText.setText(year + "");
                }else if(class_selectText.equals("月")){
                    editText.setText(year + "-" + mon);
                }else if(class_selectText.equals("日")){
                    editText.setText(year + "-" + mon + "-" + day);
                }
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public View.OnClickListener updateClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BaoBiaoActivity.this);
                int position = Integer.parseInt(view.getTag().toString());

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        XiangQingYe xiangQingYe = new XiangQingYe();

                        xiangQingYe.setA_title("摘要:");
                        xiangQingYe.setB_title("客户/供应商:");
                        xiangQingYe.setC_title("未收:");
                        xiangQingYe.setD_title("摘要:");
                        xiangQingYe.setE_title("客户/供应商:");
                        xiangQingYe.setF_title("未付:");

                        xiangQingYe.setA(list.get(position).getZhaiyao());
                        xiangQingYe.setB(list.get(position).getKehu());
                        xiangQingYe.setC(list.get(position).getReceivable().toString());
                        xiangQingYe.setD(list.get(position).getZhaiyao2());
                        xiangQingYe.setE(list.get(position).getKehu2());
                        xiangQingYe.setF(list.get(position).getCope().toString());

                        Intent intent = new Intent(BaoBiaoActivity.this, XiangQingYeActivity.class);
                        MyApplication myApplication = (MyApplication) getApplication();
                        myApplication.setObj(xiangQingYe);
                        startActivityForResult(intent, REQUEST_CODE_CHANG);
                    }
                });

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setMessage("确定查看明细？");
                builder.setTitle("提示");
                builder.show();
            }
        };
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

}


