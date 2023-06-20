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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
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
import com.example.myapplication.XiangQingYeActivity;
import com.example.myapplication.entity.XiangQingYe;
import com.example.myapplication.fenquan.activity.GongZuoTaiActivity;
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

    private ListView listView_block;
    private HorizontalScrollView list_table;
    private SimpleAdapter adapter;
    private SimpleAdapter adapter_block;

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
        listView_block = findViewById(R.id.list_block);
        list_table = findViewById(R.id.list_table);
        sel_button = findViewById(R.id.sel_button);
        sel_button.setOnClickListener(selClick());
        sel_button.requestFocus();
        showDateOnClick(start_date);
        showDateOnClick(stop_date);
        selectListShow();
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
                editText.setText(year + "-" + mon + "-" + day);
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
        sel_button.setEnabled(false);
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

                adapter = new SimpleAdapter(FaPiaoActivity.this, data, R.layout.fapiao_row, new String[]{"type","riqi","zhaiyao","unit","invoice_type","invoice_no","jine","remarks"}, new int[]{R.id.type,R.id.riqi,R.id.zhaiyao,R.id.unit,R.id.invoice_type,R.id.invoice_no,R.id.jine,R.id.remarks}) {
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

                adapter_block = new SimpleAdapter(FaPiaoActivity.this, data, R.layout.fapiao_row_block, new String[]{"type","riqi","zhaiyao","unit","invoice_type","invoice_no","jine","remarks"}, new int[]{R.id.type,R.id.riqi,R.id.zhaiyao,R.id.unit,R.id.invoice_type,R.id.invoice_no,R.id.jine,R.id.remarks}) {
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

                builder.setNeutralButton("查看详情", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        XiangQingYe xiangQingYe = new XiangQingYe();

                        xiangQingYe.setA_title("类型:");
                        xiangQingYe.setB_title("日期:");
                        xiangQingYe.setC_title("摘要:");
                        xiangQingYe.setD_title("往来单位:");
                        xiangQingYe.setE_title("发票种类:");
                        xiangQingYe.setF_title("发票号码:");
                        xiangQingYe.setG_title("金额:");
                        xiangQingYe.setH_title("备注:");

                        xiangQingYe.setA(list.get(position).getType());
                        xiangQingYe.setB(list.get(position).getRiqi());
                        xiangQingYe.setC(list.get(position).getZhaiyao());
                        xiangQingYe.setD(list.get(position).getUnit());
                        xiangQingYe.setE(list.get(position).getInvoice_type());
                        xiangQingYe.setF(list.get(position).getInvoice_no());
                        xiangQingYe.setG(list.get(position).getJine());
                        xiangQingYe.setH(list.get(position).getRemarks());

                        Intent intent = new Intent(FaPiaoActivity.this, XiangQingYeActivity.class);
                        MyApplication myApplication = (MyApplication) getApplication();
                        myApplication.setObj(xiangQingYe);
                        startActivityForResult(intent, REQUEST_CODE_CHANG);
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
