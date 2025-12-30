package com.example.myapplication.finance.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
import com.example.myapplication.finance.entity.YhFinanceGongZiMingXi;

import com.example.myapplication.finance.entity.YhFinanceLiRun;
import com.example.myapplication.finance.entity.YhFinanceQuanXian;
import com.example.myapplication.finance.entity.YhFinanceUser;

import com.example.myapplication.finance.service.YhFinanceGongZiMingXiService;

import com.example.myapplication.finance.service.YhFinanceLiRunService;
import com.example.myapplication.jxc.activity.MingXiActivity;
import com.example.myapplication.jxc.activity.MingXiChangeActivity;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class LiRunActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_CHANG = 1;
    MyApplication myApplication;

    private YhFinanceUser yhFinanceUser;
    private YhFinanceQuanXian yhFinanceQuanXian;
    private YhFinanceLiRunService yhFinanceLiRunService;

    private EditText start_date;
    private EditText stop_date;
    private ListView listView;

    private ListView listView_block;
    private HorizontalScrollView list_table;
    private SimpleAdapter adapter;
    private SimpleAdapter adapter_block;

    private String renmingText;
    private String start_dateText;
    private String stop_dateText;

    private Button sel_button;

    List<YhFinanceLiRun> list;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        myApplication = (MyApplication) getApplication();
        yhFinanceUser = myApplication.getYhFinanceUser();
        yhFinanceQuanXian = myApplication.getYhFinanceQuanXian();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finance_lirun);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        start_date = findViewById(R.id.start_date);
        stop_date = findViewById(R.id.stop_date);
        listView = findViewById(R.id.yingshoubaobiao_list);
        listView_block = findViewById(R.id.list_block);
        list_table = findViewById(R.id.list_table);
        sel_button = findViewById(R.id.sel_button);
        sel_button.setOnClickListener(selClick());
        sel_button.requestFocus();
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(LiRunActivity.this, new DatePickerDialog.OnDateSetListener() {
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
        Log.d("INIT_LIST", "开始重新加载数据...");
        sel_button.setEnabled(false);

        // 获取当前日期
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);

        start_dateText = start_date.getText().toString();
        stop_dateText = stop_date.getText().toString();

// 如果开始时间为空，设置为当前年份的1月1日
        if(start_dateText.equals("")){
            start_dateText = currentYear + "/01/01";  // 格式：2024/01/01
        }

// 如果结束时间为空，设置为当前年份的12月31日
        if(stop_dateText.equals("")){
            stop_dateText = currentYear + "/12/31";  // 格式：2024/12/31
        }

        // 验证日期是否跨年
        try {
            // 获取开始年份
            String startYearStr = start_dateText.substring(0, 4);
            int startYear = Integer.parseInt(startYearStr);

            // 获取结束年份
            String stopYearStr = stop_dateText.substring(0, 4);
            int stopYear = Integer.parseInt(stopYearStr);

            // 检查是否跨年
            if (startYear != stopYear) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.show(LiRunActivity.this, "禁止跨年查询！请选择同一年份的日期范围。");
                        sel_button.setEnabled(true);
                    }
                });
                return;
            }

            // 验证结束日期不能小于开始日期（通过字符串比较，格式为yyyy/MM/dd）
            if (start_dateText.compareTo(stop_dateText) > 0) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.show(LiRunActivity.this, "结束日期不能早于开始日期！");
                        sel_button.setEnabled(true);
                    }
                });
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.show(LiRunActivity.this, "日期格式错误！");
                    sel_button.setEnabled(true);
                }
            });
            return;
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
                    yhFinanceLiRunService   = new YhFinanceLiRunService();
                    list = yhFinanceLiRunService.getList_shou(yhFinanceUser.getCompany(),start_dateText,stop_dateText);
                    if (list == null) return;

                } catch (Exception e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < list.size(); i++) {
                    HashMap<String, Object> item = new HashMap<>();
                    item.put("company", list.get(i).getCompany());
                    item.put("xiangmu",  list.get(i).getXiangmu());
                    item.put("kemu",  list.get(i).getKemu());
                    item.put("zbenqijine", list.get(i).getZbenqijine());
                    item.put("zbennianleiji", list.get(i).getZbennianleiji());
                    item.put("zshangqijine", list.get(i).getZshangqijine());
                    item.put("benqijine", list.get(i).getBenqijine());
                    item.put("bennianleiji", list.get(i).getBennianleiji());
                    item.put("shangqijine", list.get(i).getShangqijine());
                    data.add(item);
                }

                adapter = new SimpleAdapter(LiRunActivity.this, data, R.layout.finance_lirun_row, new String[]{"xiangmu","zbenqijine","zbennianleiji","zshangqijine","kemu","benqijine","bennianleiji","shangqijine"}, new int[]{R.id.xiangmu,R.id.zbenqijine,R.id.zbennianleiji,R.id.zshangqijine,R.id.kemu,R.id.benqijine,R.id.bennianleiji,R.id.shangqijine}) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        final LinearLayout view = (LinearLayout) super.getView(position, convertView, parent);
                        LinearLayout linearLayout = (LinearLayout) view.getChildAt(0);
                        linearLayout.setOnLongClickListener(onItemLongClick());
                        linearLayout.setTag(position);
                        return view;
                    }
                };

                adapter_block = new SimpleAdapter(LiRunActivity.this, data, R.layout.finance_lirun_row_block, new String[]{"xiangmu","zbenqijine","zbennianleiji","zshangqijine","kemu","benqijine","bennianleiji","shangqijine"}, new int[]{R.id.xiangmu,R.id.zbenqijine,R.id.zbennianleiji,R.id.zshangqijine,R.id.kemu,R.id.benqijine,R.id.bennianleiji,R.id.shangqijine}) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        final LinearLayout view = (LinearLayout) super.getView(position, convertView, parent);
                        LinearLayout linearLayout = (LinearLayout) view.getChildAt(0);
                        linearLayout.setOnLongClickListener(onItemLongClick());
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
                AlertDialog.Builder builder = new AlertDialog.Builder(LiRunActivity.this);
                int position = Integer.parseInt(view.getTag().toString());

                // 删除原来的 PositiveButton（删除确定按钮）
                // 将原来的 NeutralButton（查看详情）改为 PositiveButton（确定）
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        XiangQingYe xiangQingYe = new XiangQingYe();

                        // 保留原有的详情字段设置
                        xiangQingYe.setA_title("项目:");
                        xiangQingYe.setB_title("总本期金额:");
                        xiangQingYe.setC_title("总本年累计:");
                        xiangQingYe.setD_title("总上期金额:");
                        xiangQingYe.setE_title("科目:");
                        xiangQingYe.setF_title("本期金额:");
                        xiangQingYe.setG_title("本年累计:");
                        xiangQingYe.setH_title("上期金额:");

                        xiangQingYe.setA(list.get(position).getXiangmu());
                        xiangQingYe.setB(list.get(position).getZbenqijine().toString());
                        xiangQingYe.setC(list.get(position).getZbennianleiji().toString());
                        xiangQingYe.setD(list.get(position).getZshangqijine().toString());
                        xiangQingYe.setE(list.get(position).getKemu());
                        xiangQingYe.setF(list.get(position).getBenqijine().toString());
                        xiangQingYe.setG(list.get(position).getBennianleiji().toString());
                        xiangQingYe.setH(list.get(position).getShangqijine().toString());

                        Intent intent = new Intent(LiRunActivity.this, XiangQingYeActivity.class);
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

                builder.setMessage("确定查看明细吗？");
                builder.setTitle("提示");
                builder.show();
                return true;
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


