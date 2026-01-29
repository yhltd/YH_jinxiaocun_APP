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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.XiangQingYeActivity;
import com.example.myapplication.entity.XiangQingYe;
import com.example.myapplication.finance.entity.YhFinanceJiJianTaiZhang;
import com.example.myapplication.finance.entity.YhFinanceQuanXian;
import com.example.myapplication.finance.entity.YhFinanceUser;
import com.example.myapplication.finance.entity.YhFinanceVoucherSummary;
import com.example.myapplication.finance.service.YhFinanceJiJianTaiZhangService;
import com.example.myapplication.finance.service.YhFinanceVoucherSummaryService;
import com.example.myapplication.jxc.activity.MingXiActivity;
import com.example.myapplication.jxc.activity.MingXiChangeActivity;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class JiJianTaiZhangActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_CHANG = 1;
    MyApplication myApplication;

    private YhFinanceUser yhFinanceUser;
    private YhFinanceQuanXian yhFinanceQuanXian;
    private YhFinanceJiJianTaiZhangService yhFinanceJiJianTaiZhangService;

    private EditText project;
    private EditText start_date;
    private EditText stop_date;
    private ListView listView;

    private ListView listView_block;
    private HorizontalScrollView list_table;
    private SimpleAdapter adapter;
    private SimpleAdapter adapter_block;

    private String projectText;
    private String start_dateText;
    private String stop_dateText;

    private Button sel_button;
    private Button clear_button;
    List<YhFinanceJiJianTaiZhang> list;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        myApplication = (MyApplication) getApplication();
        yhFinanceUser = myApplication.getYhFinanceUser();
        yhFinanceQuanXian = myApplication.getYhFinanceQuanXian();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jijiantaizhang);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //初始化控件
        project = findViewById(R.id.project);
        start_date = findViewById(R.id.start_date);
        stop_date = findViewById(R.id.stop_date);
        listView = findViewById(R.id.jijiantaizhang_list);
        listView_block = findViewById(R.id.list_block);
        list_table = findViewById(R.id.list_table);
        sel_button = findViewById(R.id.sel_button);
        sel_button.setOnClickListener(selClick());
        sel_button.requestFocus();
        clear_button = findViewById(R.id.clear_button);
        clear_button.setOnClickListener(clearClick());
        showDateOnClick(start_date);
        showDateOnClick(stop_date);
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(JiJianTaiZhangActivity.this, new DatePickerDialog.OnDateSetListener() {
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

    public View.OnClickListener clearClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 清空搜索框的值
                start_date.setText("");
                stop_date.setText("");
            }
        };
    }

    private void initList() {
        sel_button.setEnabled(false);
        projectText = project.getText().toString();
        start_dateText = start_date.getText().toString();
        stop_dateText = stop_date.getText().toString();
        if(start_dateText.equals("")){
            start_dateText = "1900/01/01";
        }
        if(stop_dateText.equals("")){
            stop_dateText = "2100/12/31";
        }
        if (!start_dateText.isEmpty() && !stop_dateText.isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                Date startDate = sdf.parse(start_dateText);
                Date endDate = sdf.parse(stop_dateText);

                if (startDate.after(endDate)) {
                    ToastUtil.show(JiJianTaiZhangActivity.this, "开始时间不能大于结束时间");
                    return; // 验证不通过，直接返回，不执行查询
                }
            } catch (ParseException e) {
                e.printStackTrace();
                ToastUtil.show(JiJianTaiZhangActivity.this, "日期格式错误，请使用yyyy/MM/dd格式");
                return;
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
                    yhFinanceJiJianTaiZhangService = new YhFinanceJiJianTaiZhangService();
                    list = yhFinanceJiJianTaiZhangService.getList(yhFinanceUser.getCompany(),projectText,start_dateText,stop_dateText);
                    if (list == null) return;

                } catch (Exception e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < list.size(); i++) {
                    HashMap<String, Object> item = new HashMap<>();
                    item.put("company", list.get(i).getCompany());
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date(list.get(i).getInsert_date().getTime());
                    String java_date = sdf.format(date);
                    item.put("insert_date", java_date);
                    item.put("project", list.get(i).getProject());
                    item.put("kehu", list.get(i).getKehu());
                    item.put("receipts", list.get(i).getReceipts());
                    item.put("receivable", list.get(i).getReceivable());
                    item.put("weishou", list.get(i).getReceivable().subtract(list.get(i).getReceipts()));
                    item.put("cope", list.get(i).getCope());
                    item.put("payment", list.get(i).getPayment());
                    item.put("weifu", list.get(i).getCope().subtract(list.get(i).getPayment()));
                    item.put("accounting", list.get(i).getAccounting());
                    item.put("nashuijine", list.get(i).getNashuijine());
                    item.put("yijiaoshuijine", list.get(i).getYijiaoshuijine());
                    BigDecimal nashuijine = list.get(i).getNashuijine();
                    BigDecimal yijiaoshuijine = list.get(i).getYijiaoshuijine();
                    BigDecimal safeNashuijine = nashuijine != null ? nashuijine : BigDecimal.ZERO;
                    BigDecimal safeYijiaoshuijine = yijiaoshuijine != null ? yijiaoshuijine : BigDecimal.ZERO;

                    BigDecimal weijiaoshuijine = safeNashuijine.subtract(safeYijiaoshuijine);
                    item.put("weijiaoshuijine", weijiaoshuijine);
                    item.put("zhaiyao", list.get(i).getZhaiyao());
                    data.add(item);
                }

                adapter = new SimpleAdapter(JiJianTaiZhangActivity.this, data, R.layout.jijiantaizhang_row, new String[]{"insert_date","kehu","project","receivable","receipts","weishou","cope","payment","weifu","accounting","nashuijine","yijiaoshuijine","weijiaoshuijine","zhaiyao"}, new int[]{R.id.insert_date,R.id.kehu,R.id.project,R.id.receivable,R.id.receipts,R.id.weishou,R.id.cope,R.id.payment,R.id.weifu,R.id.accounting,R.id.nashuijine,R.id.yijiaoshuijine,R.id.weijiaoshuijine,R.id.zhaiyao}) {
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

                adapter_block = new SimpleAdapter(JiJianTaiZhangActivity.this, data, R.layout.jijiantaizhang_row_block, new String[]{"insert_date","kehu","project","receivable","receipts","weishou","cope","payment","weifu","accounting","nashuijine","yijiaoshuijine","weijiaoshuijine","zhaiyao"}, new int[]{R.id.insert_date,R.id.kehu,R.id.project,R.id.receivable,R.id.receipts,R.id.weishou,R.id.cope,R.id.payment,R.id.weifu,R.id.accounting,R.id.nashuijine,R.id.yijiaoshuijine,R.id.weijiaoshuijine,R.id.zhaiyao}) {
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
                if(!yhFinanceQuanXian.getJjtzDelete().equals("是")){
                    ToastUtil.show(JiJianTaiZhangActivity.this, "无权限！");
                    return true;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(JiJianTaiZhangActivity.this);
                int position = Integer.parseInt(view.getTag().toString());
                Handler deleteHandler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message msg) {
                        if ((boolean) msg.obj) {
                            ToastUtil.show(JiJianTaiZhangActivity.this, "删除成功");
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
                                msg.obj = yhFinanceJiJianTaiZhangService.delete(list.get(position).getId());
                                deleteHandler.sendMessage(msg);
                            }
                        }).start();
                    }
                });

                builder.setNeutralButton("查看详情", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        XiangQingYe xiangQingYe = new XiangQingYe();

                        xiangQingYe.setA_title("日期:");
                        xiangQingYe.setB_title("客户/供应商:");
                        xiangQingYe.setC_title("项目名称:");
                        xiangQingYe.setD_title("应收:");
                        xiangQingYe.setE_title("实收:");
                        xiangQingYe.setF_title("未收:");
                        xiangQingYe.setG_title("应付:");
                        xiangQingYe.setH_title("实付:");
                        xiangQingYe.setI_title("未付:");
                        xiangQingYe.setJ_title("科目:");
                        xiangQingYe.setK_title("纳税金额:");
                        xiangQingYe.setL_title("已交税金额:");
                        xiangQingYe.setM_title("未交税金额:");
                        xiangQingYe.setN_title("摘要:");

                        xiangQingYe.setA(list.get(position).getInsert_date().toString());
                        xiangQingYe.setB(list.get(position).getProject());
                        xiangQingYe.setC(list.get(position).getKehu());
                        xiangQingYe.setD(list.get(position).getReceipts().toString());
                        xiangQingYe.setE(list.get(position).getReceivable().toString());
                        xiangQingYe.setF(list.get(position).getReceivable().subtract(list.get(position).getReceipts()).toString());
                        xiangQingYe.setG(list.get(position).getCope().toString());
                        xiangQingYe.setH(list.get(position).getPayment().toString());
                        xiangQingYe.setI(list.get(position).getCope().subtract(list.get(position).getPayment()).toString());
                        xiangQingYe.setJ(list.get(position).getAccounting());
                        xiangQingYe.setK(
                                list.get(position).getNashuijine() != null ?
                                        list.get(position).getNashuijine().toString() :
                                        "0"
                        );

// 修复第2行：yijiaoshuijine
                        xiangQingYe.setL(
                                list.get(position).getYijiaoshuijine() != null ?
                                        list.get(position).getYijiaoshuijine().toString() :
                                        "0"
                        );

// 修复第3行：减法计算
                        BigDecimal nashuijine = list.get(position).getNashuijine();
                        BigDecimal yijiaoshuijine = list.get(position).getYijiaoshuijine();

                        BigDecimal safeNashuijine = nashuijine != null ? nashuijine : BigDecimal.ZERO;
                        BigDecimal safeYijiaoshuijine = yijiaoshuijine != null ? yijiaoshuijine : BigDecimal.ZERO;

                        BigDecimal result = safeNashuijine.subtract(safeYijiaoshuijine);
                        xiangQingYe.setM(result.toString());
                        xiangQingYe.setN(list.get(position).getZhaiyao());

                        Intent intent = new Intent(JiJianTaiZhangActivity.this, XiangQingYeActivity.class);
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
                if(!yhFinanceQuanXian.getJjtzUpdate().equals("是")){
                    ToastUtil.show(JiJianTaiZhangActivity.this, "无权限！");
                    return;
                }
                int position = Integer.parseInt(view.getTag().toString());
                Intent intent = new Intent(JiJianTaiZhangActivity.this, JiJianTaiZhangChangeActivity.class);
                intent.putExtra("type", R.id.update_btn);
                MyApplication myApplication = (MyApplication) getApplication();
                myApplication.setObj(list.get(position));
                startActivityForResult(intent, REQUEST_CODE_CHANG);
            }
        };
    }

    public void onInsertClick(View v) {
        if(!yhFinanceQuanXian.getJjtzAdd().equals("是")){
            ToastUtil.show(JiJianTaiZhangActivity.this, "无权限！");
            return;
        }
        Intent intent = new Intent(JiJianTaiZhangActivity.this, JiJianTaiZhangChangeActivity.class);
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


