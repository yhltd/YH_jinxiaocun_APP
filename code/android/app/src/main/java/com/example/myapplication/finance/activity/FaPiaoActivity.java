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
import com.example.myapplication.finance.entity.YhFinanceFaPiao;
import com.example.myapplication.finance.entity.YhFinanceJiJianPeiZhi;
import com.example.myapplication.finance.entity.YhFinanceUser;
import com.example.myapplication.finance.entity.YhFinanceVoucherSummary;
import com.example.myapplication.finance.service.YhFinanceFaPiaoService;
import com.example.myapplication.finance.service.YhFinanceKehuPeizhiService;
import com.example.myapplication.finance.service.YhFinanceVoucherSummaryService;
import com.example.myapplication.finance.service.YhFinanceVoucherWordService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class FaPiaoActivity extends AppCompatActivity {

    private final static int REQUEST_CODE_CHANG = 1;

    MyApplication myApplication;

    private YhFinanceUser yhFinanceUser;
    private YhFinanceFaPiaoService yhFinanceFaPiaoService;
    private YhFinanceKehuPeizhiService yhFinanceKehuPeizhiService;
    private EditText start_date;
    private EditText stop_date;
    private ListView listView;
    private Spinner type_select;
    private String type_selectText;
    private String start_dateText;
    private String stop_dateText;
    private Button sel_button;

    List<YhFinanceFaPiao> list;

    List<String> new_list;
    String[] class_selectArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        myApplication = (MyApplication) getApplication();
        yhFinanceUser = myApplication.getYhFinanceUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fapiao);
        type_select = findViewById(R.id.type_select);
//        class_selectArray = getResources().getStringArray(R.array.fapiao_type_list);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, class_selectArray);
//        type_select.setAdapter(adapter);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //初始化控件
        start_date = findViewById(R.id.start_date);
        stop_date = findViewById(R.id.stop_date);
        listView = findViewById(R.id.fapiao_list);
        sel_button = findViewById(R.id.sel_button);
        sel_button.setOnClickListener(selClick());
        sel_button.requestFocus();
        showDateOnClick(start_date);
        showDateOnClick(stop_date);
        selectListShow();
    }


    private void selectListShow() {
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.obj != null) {
                    type_select.setAdapter((SpinnerAdapter) msg.obj);
                }
                initList();
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                SpinnerAdapter adapter = null;
                try {
                    yhFinanceKehuPeizhiService = new YhFinanceKehuPeizhiService();
                    List<YhFinanceJiJianPeiZhi> list = yhFinanceKehuPeizhiService.getList(yhFinanceUser.getCompany());
                    new_list = new ArrayList<>();
                    new_list.add("");
                    for(int i=0; i<list.size(); i++){
                        if(!list.get(i).getPeizhi().equals("")){
                            new_list.add(list.get(i).getPeizhi());
                        }
                    }
                    adapter = new ArrayAdapter<String>(FaPiaoActivity.this, android.R.layout.simple_spinner_dropdown_item, new_list);
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(FaPiaoActivity.this, new DatePickerDialog.OnDateSetListener() {
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
        type_selectText = type_select.getSelectedItem().toString();
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
                    yhFinanceFaPiaoService = new YhFinanceFaPiaoService();
                    list = yhFinanceFaPiaoService.getList(yhFinanceUser.getCompany(),type_selectText,start_dateText,stop_dateText);
                    if (list == null) return;

                } catch (Exception e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < list.size(); i++) {
                    HashMap<String, Object> item = new HashMap<>();
                    item.put("type", list.get(i).getType());
                    item.put("riqi", list.get(i).getRiqi());
                    item.put("zhaiyao", list.get(i).getZhaiyao());
                    item.put("unit", list.get(i).getUnit());
                    item.put("invoice_type", list.get(i).getInvoice_type());
                    item.put("invoice_no", list.get(i).getInvoice_no());
                    item.put("jine", list.get(i).getJine());
                    item.put("remarks", list.get(i).getRemarks());
                    data.add(item);
                }

                SimpleAdapter adapter = new SimpleAdapter(FaPiaoActivity.this, data, R.layout.fapiao_row, new String[]{"type","riqi","zhaiyao","unit","invoice_type","invoice_no","jine","remarks"}, new int[]{R.id.type,R.id.riqi,R.id.zhaiyao,R.id.unit,R.id.invoice_type,R.id.invoice_no,R.id.jine,R.id.remarks}) {
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

    public View.OnLongClickListener onItemLongClick() {
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FaPiaoActivity.this);
                int position = Integer.parseInt(view.getTag().toString());
                Handler deleteHandler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message msg) {
                        if ((boolean) msg.obj) {
                            ToastUtil.show(FaPiaoActivity.this, "删除成功");
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
                                msg.obj = yhFinanceFaPiaoService.delete(list.get(position).getId());
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


    public View.OnClickListener updateClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = Integer.parseInt(view.getTag().toString());
                Intent intent = new Intent(FaPiaoActivity.this, FaPiaoChangeActivity.class);
                intent.putExtra("type", R.id.update_btn);
                MyApplication myApplication = (MyApplication) getApplication();
                myApplication.setObj(list.get(position));
                startActivityForResult(intent, REQUEST_CODE_CHANG);
            }
        };
    }

    public void onInsertClick(View v) {
        Intent intent = new Intent(FaPiaoActivity.this, FaPiaoChangeActivity.class);
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
