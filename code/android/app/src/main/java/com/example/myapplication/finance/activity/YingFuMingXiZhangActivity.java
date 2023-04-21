package com.example.myapplication.finance.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.finance.entity.YhFinanceJiJianPeiZhi;
import com.example.myapplication.finance.entity.YhFinanceUser;
import com.example.myapplication.finance.entity.YhFinanceYingShouMingXiZhang;
import com.example.myapplication.finance.service.YhFinanceKehuPeizhiService;
import com.example.myapplication.finance.service.YhFinanceYingShouMingXiZhangService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class YingFuMingXiZhangActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_CHANG = 1;
    MyApplication myApplication;

    private YhFinanceUser yhFinanceUser;
    private YhFinanceYingShouMingXiZhangService yhFinanceYingShouMingXiZhangService;
    private YhFinanceKehuPeizhiService yhFinanceKehuPeizhiService;

    private Spinner class_select;
    private EditText start_date;
    private EditText stop_date;
    private ListView listView;

    private String class_selectText;
    private String start_dateText;
    private String stop_dateText;

    private Button sel_button;

    List<YhFinanceYingShouMingXiZhang> list;
    List<YhFinanceYingShouMingXiZhang> list1;
    List<YhFinanceYingShouMingXiZhang> list2;
    List<YhFinanceYingShouMingXiZhang> list3;
    List<String> kehu_array;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        myApplication = (MyApplication) getApplication();
        yhFinanceUser = myApplication.getYhFinanceUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yingfumingxizhang);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        yhFinanceKehuPeizhiService = new YhFinanceKehuPeizhiService();

        //初始化控件
        start_date = findViewById(R.id.start_date);
        stop_date = findViewById(R.id.stop_date);
        showDateOnClick(start_date);
        showDateOnClick(stop_date);
        class_select = findViewById(R.id.kehu_select);

        listView = findViewById(R.id.yingfumingxizhang_list);
        sel_button = findViewById(R.id.sel_button);
        sel_button.setOnClickListener(selClick());
        sel_button.requestFocus();
        init_select();
//        initList();
    }

    public View.OnClickListener selClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initList();
            }
        };
    }

    public void init_select() {
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                class_select.setAdapter(StringUtils.cast(msg.obj));
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                SpinnerAdapter adapter = null;
                try {
                    List<YhFinanceJiJianPeiZhi> kehuList = yhFinanceKehuPeizhiService.getList(yhFinanceUser.getCompany());
                    if (kehuList.size() > 0) {
                        kehu_array = new ArrayList<>();
                        for (int i = 0; i < kehuList.size(); i++) {
                            kehu_array.add(kehuList.get(i).getPeizhi());
                        }
                    }
                    adapter = new ArrayAdapter<String>(YingFuMingXiZhangActivity.this, android.R.layout.simple_spinner_dropdown_item, kehu_array);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.obj = adapter;
                listLoadHandler.sendMessage(msg);
            }
        }).start();
    }



    private void initList() {
        LoadingDialog.getInstance(this).show();
        start_dateText = start_date.getText().toString();
        stop_dateText = stop_date.getText().toString();
        class_selectText = class_select.getSelectedItem().toString();
        if(start_dateText.equals("")){
            start_dateText = "1900-01-01";
        }
        if(stop_dateText.equals("")){
            stop_dateText = "2100-12-31";
        }

        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                listView.setAdapter(StringUtils.cast(msg.obj));
                LoadingDialog.getInstance(getApplicationContext()).dismiss();
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<HashMap<String, Object>> data = new ArrayList<>();
                try {
                    list = new ArrayList<>();
                    yhFinanceYingShouMingXiZhangService = new YhFinanceYingShouMingXiZhangService();
                    list1 = yhFinanceYingShouMingXiZhangService.getList_fuLast(yhFinanceUser.getCompany(),class_selectText,start_dateText,stop_dateText);
                    list2 = yhFinanceYingShouMingXiZhangService.getList_fu(yhFinanceUser.getCompany(),class_selectText,start_dateText,stop_dateText);
                    list3 = yhFinanceYingShouMingXiZhangService.getList_fuSecond(yhFinanceUser.getCompany(),class_selectText,start_dateText,stop_dateText);
//                    if (list1 == null && list2 == null) return;

                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(list1.size() == 0){
                    YhFinanceYingShouMingXiZhang this_start = new YhFinanceYingShouMingXiZhang();
                    this_start.setProject("上期合计");
                    this_start.setKehu(class_selectText);
                    BigDecimal price = new BigDecimal("0");
                    this_start.setCope(price);
                    this_start.setPayment(price);
                    this_start.setWeifu(price);
                    list.add(this_start);
                }else{
                    list1.get(0).setProject("上期合计");
                    list.add(list1.get(0));
                }

                if(list2 != null){
                    for(int i=0; i<list2.size(); i++){
                        list.add(list2.get(i));
                    }
                }

                if(list3.size() == 0){
                    YhFinanceYingShouMingXiZhang this_start = new YhFinanceYingShouMingXiZhang();
                    this_start.setProject("结余合计");
                    this_start.setKehu(class_selectText);
                    BigDecimal price = new BigDecimal("0");
                    this_start.setCope(price);
                    this_start.setPayment(price);
                    this_start.setWeifu(price);
                    list.add(this_start);
                }else{
                    list3.get(0).setProject("结余合计");
                    list.add(list3.get(0));
                }

                for (int i = 0; i < list.size(); i++) {
                    HashMap<String, Object> item = new HashMap<>();
                    item.put("insert_date", list.get(i).getInsert_date());
                    item.put("kehu", list.get(i).getKehu());
                    item.put("accounting", list.get(i).getAccounting());
                    item.put("project", list.get(i).getProject());
                    item.put("cope", list.get(i).getCope());
                    item.put("payment", list.get(i).getPayment());
                    item.put("weifu", list.get(i).getWeifu());
                    data.add(item);
                }

                SimpleAdapter adapter = new SimpleAdapter(YingFuMingXiZhangActivity.this, data, R.layout.yingfumingxizhang_row, new String[]{"insert_date","kehu","accounting","project","cope","payment","weifu",}, new int[]{R.id.insert_date,R.id.kehu,R.id.accounting,R.id.project,R.id.cope,R.id.payment,R.id.weifu}) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        final LinearLayout view = (LinearLayout) super.getView(position, convertView, parent);
                        LinearLayout linearLayout = (LinearLayout) view.getChildAt(0);
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(YingFuMingXiZhangActivity.this, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                editText.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
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


