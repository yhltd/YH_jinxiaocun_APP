package com.example.myapplication.finance.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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
import com.example.myapplication.finance.entity.YhFinanceYingShouBaoBiao;
import com.example.myapplication.finance.service.YhFinanceKehuPeizhiService;
import com.example.myapplication.finance.service.YhFinanceYingShouBaoBiaoService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class YingFuBaoBiaoActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_CHANG = 1;
    MyApplication myApplication;

    private YhFinanceUser yhFinanceUser;
    private YhFinanceYingShouBaoBiaoService yhFinanceYingShouBaoBiaoService;
    private YhFinanceKehuPeizhiService yhFinanceKehuPeizhiService;

    private Spinner class_select;
    private EditText start_date;
    private EditText stop_date;
    private ListView listView;

    private String class_selectText;
    private String start_dateText;
    private String stop_dateText;

    private Button sel_button;

    List<YhFinanceYingShouBaoBiao> list;
    List<YhFinanceYingShouBaoBiao> list1;
    List<YhFinanceYingShouBaoBiao> list2;
    List<String> kehu_array;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        myApplication = (MyApplication) getApplication();
        yhFinanceUser = myApplication.getYhFinanceUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yingfubaobiao);

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

        listView = findViewById(R.id.yingfubaobiao_list);
        sel_button = findViewById(R.id.sel_button);
        sel_button.setOnClickListener(selClick());
        sel_button.requestFocus();
        init_select();
//        initList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        LoadingDialog.getInstance(this).show();
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                class_select.setAdapter(StringUtils.cast(msg.obj));
                LoadingDialog.getInstance(getApplicationContext()).dismiss();
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
//                        kehu_array.add("");
                        for (int i = 0; i < kehuList.size(); i++) {
                            kehu_array.add(kehuList.get(i).getPeizhi());
                        }
                    }
                    adapter = new ArrayAdapter<String>(YingFuBaoBiaoActivity.this, android.R.layout.simple_spinner_dropdown_item, kehu_array);
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
        list1 = new ArrayList<>();
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
                    yhFinanceYingShouBaoBiaoService = new YhFinanceYingShouBaoBiaoService();
                    list1 = yhFinanceYingShouBaoBiaoService.getList_yingfu(yhFinanceUser.getCompany(),class_selectText,start_dateText,stop_dateText);
                    list2 = yhFinanceYingShouBaoBiaoService.getList_jinxiang(yhFinanceUser.getCompany(),class_selectText,start_dateText,stop_dateText);
                    if (list1 == null && list2 == null) return;

                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(list1 == null || list2 == null){
                    if(list1 == null){
                        list = list2;
                    }else{
                        list = list1;
                    }
                }else{
                    if(list1.size() > list2.size()){
                        for(int i=0; i<list2.size(); i++){
                            list1.get(i).setUnit(list2.get(i).getUnit());
                            list1.get(i).setInvoice_type(list2.get(i).getInvoice_type());
                            list1.get(i).setInvoice_no(list2.get(i).getInvoice_no());
                            list1.get(i).setJine2(list2.get(i).getJine2());
                        }
                        list = list1;
                    }else{
                        for(int i=0; i<list1.size(); i++){
                            list2.get(i).setKehu(list1.get(i).getKehu());
                            list2.get(i).setProject(list1.get(i).getProject());
                            list2.get(i).setZhaiyao(list1.get(i).getZhaiyao());
                            list2.get(i).setJine1(list1.get(i).getJine1());
                        }
                        list = list2;
                    }
                }



                for (int i = 0; i < list.size(); i++) {
                    HashMap<String, Object> item = new HashMap<>();
                    item.put("kehu", list.get(i).getKehu());
                    item.put("project", list.get(i).getKehu());
                    item.put("zhaiyao", list.get(i).getZhaiyao());
                    item.put("jine1", list.get(i).getJine1());
                    item.put("unit", list.get(i).getUnit());
                    item.put("invoice_type", list.get(i).getInvoice_type());
                    item.put("invoice_no", list.get(i).getInvoice_no());
                    item.put("jine2", list.get(i).getJine2());
                    data.add(item);
                }

                SimpleAdapter adapter = new SimpleAdapter(YingFuBaoBiaoActivity.this, data, R.layout.yingshoubaobiao_row, new String[]{"kehu","project","zhaiyao","jine1","unit","invoice_type","invoice_no","jine2"}, new int[]{R.id.kehu,R.id.project,R.id.zhaiyao,R.id.jine1,R.id.unit,R.id.invoice_type,R.id.invoice_no,R.id.jine2}) {
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(YingFuBaoBiaoActivity.this, new DatePickerDialog.OnDateSetListener() {
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


