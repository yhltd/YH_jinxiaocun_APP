package com.example.myapplication.finance.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.finance.entity.YhFinanceExpenditure;
import com.example.myapplication.finance.entity.YhFinanceQuanXian;
import com.example.myapplication.finance.entity.YhFinanceUser;
import com.example.myapplication.finance.entity.YhFinanceVoucherSummary;
import com.example.myapplication.finance.service.YhFinanceVoucherSummaryService;
import com.example.myapplication.finance.service.YhFinanceVoucherWordService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class VoucherSummaryActivity extends AppCompatActivity {

    private final static int REQUEST_CODE_CHANG = 1;

    MyApplication myApplication;

    private YhFinanceUser yhFinanceUser;
    private YhFinanceQuanXian yhFinanceQuanXian;
    private YhFinanceVoucherSummaryService yhFinanceVoucherSummaryService;
    private YhFinanceVoucherWordService yhFinanceVoucherWordService;
    private EditText start_date;
    private EditText stop_date;
    private ListView listView;
    private Spinner type_select;
    private String type_selectText;
    private String start_dateText;
    private String stop_dateText;
    private Button sel_button;

    List<YhFinanceVoucherSummary> list;

    List<String> new_list;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        myApplication = (MyApplication) getApplication();
        yhFinanceUser = myApplication.getYhFinanceUser();
        yhFinanceQuanXian = myApplication.getYhFinanceQuanXian();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voucher_summary);
        type_select = findViewById(R.id.type_select);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //初始化控件
        start_date = findViewById(R.id.start_date);
        stop_date = findViewById(R.id.stop_date);
        listView = findViewById(R.id.voucher_summary_list);
        sel_button = findViewById(R.id.sel_button);
        sel_button.setOnClickListener(selClick());
        sel_button.requestFocus();
        showDateOnClick(start_date);
        showDateOnClick(stop_date);
        selectListShow();
        type_select.setOnItemSelectedListener(new typeSelectSelectedListener());

    }


    private void selectListShow() {
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.obj != null) {
                    type_select.setAdapter((SpinnerAdapter) msg.obj);
                }
//                initList();
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                SpinnerAdapter adapter = null;
                try {
                    yhFinanceVoucherWordService = new YhFinanceVoucherWordService();
                    List<YhFinanceExpenditure> list = yhFinanceVoucherWordService.getList(yhFinanceUser.getCompany());
                    new_list = new ArrayList<>();
                    for(int i=0; i<list.size(); i++){
                        if(!list.get(i).getShouru().equals("")){
                            new_list.add(list.get(i).getShouru());
                        }
                    }
                    adapter = new ArrayAdapter<String>(VoucherSummaryActivity.this, android.R.layout.simple_spinner_dropdown_item, new_list);
                    if (list.size() > 0) {
                        msg.obj = adapter;
                    } else {
                        msg.obj = null;
                    }
                    listLoadHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }



    private class typeSelectSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //获取选择的项的值
            type_selectText = type_select.getItemAtPosition(position).toString();
            start_dateText = start_date.getText().toString();
            stop_dateText = stop_date.getText().toString();
            if(start_dateText.equals("")){
                start_dateText = "1900-01-01";
            }
            if(stop_dateText.equals("")){
                stop_dateText = "2100-12-31";
            }
            Handler listLoadHandler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(@NonNull Message msg) {
                    listView.setAdapter(StringUtils.cast(msg.obj));
                    return true;
                }
            });

            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<HashMap<String, Object>> data = new ArrayList<>();
//                    Message msg = new Message();
//                    SpinnerAdapter adapter = null;
                    try {
                        yhFinanceVoucherSummaryService = new YhFinanceVoucherSummaryService();
                        list = yhFinanceVoucherSummaryService.getList(type_selectText,yhFinanceUser.getCompany(),start_dateText,stop_dateText);
                        if (list == null) return;

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    for (int i = 0; i < list.size(); i++) {
                        HashMap<String, Object> item = new HashMap<>();
                        item.put("word", list.get(i).getWord());
                        item.put("no", list.get(i).getNo());
                        item.put("insert_date", list.get(i).getInsert_date());
                        item.put("zhaiyao", list.get(i).getZhaiyao());
                        item.put("code", list.get(i).getCode());
                        item.put("full_name", list.get(i).getFull_name());
                        item.put("load", list.get(i).getLoad());
                        item.put("borrowed", list.get(i).getBorrowed());
                        item.put("department", list.get(i).getDepartment());
                        item.put("expenditure", list.get(i).getExpenditure());
                        item.put("note", list.get(i).getNote());
                        item.put("man", list.get(i).getMan());
                        item.put("money", list.get(i).getMoney());
                        item.put("real", list.get(i).getReal());
                        item.put("not_get", list.get(i).getMoney().subtract(list.get(i).getReal()));
                        data.add(item);
                    }


                    SimpleAdapter adapter = new SimpleAdapter(VoucherSummaryActivity.this, data, R.layout.voucher_summary_row, new String[]{"word","no","insert_date","zhaiyao","code","full_name","load","borrowed","department","expenditure","note","man","money","real","not_get"}, new int[]{R.id.word,R.id.no,R.id.insert_date,R.id.zhaiyao,R.id.code,R.id.full_name,R.id.load,R.id.borrowed,R.id.department,R.id.expenditure,R.id.note,R.id.man,R.id.money,R.id.real,R.id.not_get}) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            final LinearLayout view = (LinearLayout) super.getView(position, convertView, parent);
                            LinearLayout linearLayout = (LinearLayout) view.getChildAt(0);
                            linearLayout.setOnLongClickListener(onItemLongClick());
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

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    }

    public View.OnLongClickListener onItemLongClick() {
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(!yhFinanceQuanXian.getPzhzDelete().equals("是")){
                    ToastUtil.show(VoucherSummaryActivity.this, "无权限！");
                    return true;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(VoucherSummaryActivity.this);
                int position = Integer.parseInt(view.getTag().toString());
                Handler deleteHandler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message msg) {
                        if ((boolean) msg.obj) {
                            ToastUtil.show(VoucherSummaryActivity.this, "删除成功");
                            initList();
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
                                msg.obj = yhFinanceVoucherSummaryService.delete(list.get(position).getId());
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

                builder.setMessage("确定删除吗？");
                builder.setTitle("提示");
                builder.show();
                return true;
            }
        };
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

    public View.OnClickListener updateClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!yhFinanceQuanXian.getPzhzUpdate().equals("是")){
                    ToastUtil.show(VoucherSummaryActivity.this, "无权限！");
                    return;
                }
                int position = Integer.parseInt(view.getTag().toString());
                Intent intent = new Intent(VoucherSummaryActivity.this, VoucherSummaryChangeActivity.class);
                intent.putExtra("type", R.id.update_btn);
                MyApplication myApplication = (MyApplication) getApplication();
                myApplication.setObj(list.get(position));
                startActivityForResult(intent, REQUEST_CODE_CHANG);
            }
        };
    }

    protected void showDatePickDlg(final EditText editText) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(VoucherSummaryActivity.this, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                editText.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
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
        LoadingDialog.getInstance(this).show();
        start_dateText = start_date.getText().toString();
        stop_dateText = stop_date.getText().toString();
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
                    yhFinanceVoucherSummaryService = new YhFinanceVoucherSummaryService();
                    list = yhFinanceVoucherSummaryService.getList(type_selectText,yhFinanceUser.getCompany(),start_dateText,stop_dateText);
                    if (list == null) return;

                } catch (Exception e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < list.size(); i++) {
                    HashMap<String, Object> item = new HashMap<>();
                    item.put("word", list.get(i).getWord());
                    item.put("no", list.get(i).getNo());
                    item.put("insert_date", list.get(i).getInsert_date());
                    item.put("zhaiyao", list.get(i).getZhaiyao());
                    item.put("code", list.get(i).getCode());
                    item.put("full_name", list.get(i).getFull_name());
                    item.put("load", list.get(i).getLoad());
                    item.put("borrowed", list.get(i).getBorrowed());
                    item.put("department", list.get(i).getDepartment());
                    item.put("expenditure", list.get(i).getExpenditure());
                    item.put("note", list.get(i).getNote());
                    item.put("man", list.get(i).getMan());
                    item.put("money", list.get(i).getMoney());
                    item.put("real", list.get(i).getReal());
                    item.put("not_get", list.get(i).getMoney().subtract(list.get(i).getReal()));
                    data.add(item);
                }

                SimpleAdapter adapter = new SimpleAdapter(VoucherSummaryActivity.this, data, R.layout.voucher_summary_row, new String[]{"word","no","insert_date","zhaiyao","code","full_name","load","borrowed","department","expenditure","note","man","money","real","not_get"}, new int[]{R.id.word,R.id.no,R.id.insert_date,R.id.zhaiyao,R.id.code,R.id.full_name,R.id.load,R.id.borrowed,R.id.department,R.id.expenditure,R.id.note,R.id.man,R.id.money,R.id.real,R.id.not_get}) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        final LinearLayout view = (LinearLayout) super.getView(position, convertView, parent);
                        LinearLayout linearLayout = (LinearLayout) view.getChildAt(0);
                        linearLayout.setOnLongClickListener(onItemLongClick());
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

    public void onInsertClick(View v) {
        if(!yhFinanceQuanXian.getPzhzAdd().equals("是")){
            ToastUtil.show(VoucherSummaryActivity.this, "无权限！");
            return;
        }
        Intent intent = new Intent(VoucherSummaryActivity.this, VoucherSummaryChangeActivity.class);
        intent.putExtra("type", R.id.insert_btn);
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

}
