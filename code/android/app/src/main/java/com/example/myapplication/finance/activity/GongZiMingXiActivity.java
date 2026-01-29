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

import com.example.myapplication.finance.entity.YhFinanceQuanXian;
import com.example.myapplication.finance.entity.YhFinanceUser;

import com.example.myapplication.finance.service.YhFinanceGongZiMingXiService;

import com.example.myapplication.jxc.activity.MingXiActivity;
import com.example.myapplication.jxc.activity.MingXiChangeActivity;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class GongZiMingXiActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_CHANG = 1;
    MyApplication myApplication;

    private YhFinanceUser yhFinanceUser;
    private YhFinanceQuanXian yhFinanceQuanXian;
    private YhFinanceGongZiMingXiService yhFinanceGongZiMingXiService;

    private EditText renming;
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
    private Button clear_button;
    List<YhFinanceGongZiMingXi> list;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        myApplication = (MyApplication) getApplication();
        yhFinanceUser = myApplication.getYhFinanceUser();
        yhFinanceQuanXian = myApplication.getYhFinanceQuanXian();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finance_gongzimingxi);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        renming = findViewById(R.id.renming);

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
        DatePickerDialog datePickerDialog = new DatePickerDialog(GongZiMingXiActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                renming.setText("");
            }
        };
    }

    private void initList() {
        Log.d("INIT_LIST", "开始重新加载数据...");
        sel_button.setEnabled(false);
        renmingText = renming.getText().toString();
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

                // 关键修改：将结束日期加一天，然后减一秒，确保包含当天的所有时间
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(sdf.parse(stop_dateText));
                calendar.add(Calendar.DAY_OF_MONTH, 1); // 加一天
                calendar.add(Calendar.SECOND, -1);      // 减一秒，得到 23:59:59

                Date endDate = calendar.getTime();
                if (startDate.after(endDate)) {
                    ToastUtil.show(GongZiMingXiActivity.this, "开始时间不能大于结束时间");
                    return;
                }

                // 重新格式化日期用于数据库查询
                SimpleDateFormat sdfForQuery = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                start_dateText = sdfForQuery.format(startDate); // 保持为 00:00:00
                stop_dateText = sdfForQuery.format(endDate);    // 修改为 23:59:59

            } catch (ParseException e) {
                e.printStackTrace();
                ToastUtil.show(GongZiMingXiActivity.this, "日期格式错误，请使用yyyy/MM/dd格式");
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
                    yhFinanceGongZiMingXiService = new YhFinanceGongZiMingXiService();
                    list = yhFinanceGongZiMingXiService.getList(yhFinanceUser.getCompany(),renmingText,start_dateText,stop_dateText);
                    if (list == null) return;

                } catch (Exception e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < list.size(); i++) {
                    HashMap<String, Object> item = new HashMap<>();
                    item.put("company", list.get(i).getCompany());
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date(list.get(i).getRiqi().getTime());
                    String java_date = sdf.format(date);
                    item.put("riqi", java_date);
                    item.put("renming",  list.get(i).getRenming());
                    item.put("yinhangzhanghu", list.get(i).getYinhangzhanghu());
                    item.put("koukuan", list.get(i).getKoukuan());
                    item.put("gongzi", list.get(i).getGongzi());
                    item.put("yifu", list.get(i).getYifu());
                    item.put("weifu", list.get(i).getGongzi().subtract(list.get(i).getYifu()));
                    item.put("beizhu", list.get(i).getBeizhu());
                    data.add(item);
                }

                adapter = new SimpleAdapter(GongZiMingXiActivity.this, data, R.layout.finance_gongzimingxi_row, new String[]{"renming","riqi","yinhangzhanghu","koukuan","gongzi","yifu","weifu","beizhu"}, new int[]{R.id.renming,R.id.riqi,R.id.yinhangzhanghu,R.id.koukuan,R.id.gongzi,R.id.yifu,R.id.weifu,R.id.beizhu}) {
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

                adapter_block = new SimpleAdapter(GongZiMingXiActivity.this, data, R.layout.finance_gongzimingxi_row_block, new String[]{"renming","riqi","yinhangzhanghu","koukuan","gongzi","yifu","weifu","beizhu"}, new int[]{R.id.renming,R.id.riqi,R.id.yinhangzhanghu,R.id.koukuan,R.id.gongzi,R.id.yifu,R.id.weifu,R.id.beizhu}) {
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
                AlertDialog.Builder builder = new AlertDialog.Builder(GongZiMingXiActivity.this);
                int position = Integer.parseInt(view.getTag().toString());
                Handler deleteHandler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message msg) {
                        if ((boolean) msg.obj) {
                            ToastUtil.show(GongZiMingXiActivity.this, "删除成功");
                            Log.d("DELETE_DEBUG", "删除结果: " );
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
                                msg.obj = yhFinanceGongZiMingXiService.delete(list.get(position).getId());
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

                        xiangQingYe.setA_title("姓名:");
                        xiangQingYe.setB_title("日期:");
                        xiangQingYe.setC_title("银行账户:");
                        xiangQingYe.setD_title("扣款:");
                        xiangQingYe.setE_title("工资:");
                        xiangQingYe.setF_title("已付:");
                        xiangQingYe.setG_title("未付:");
                        xiangQingYe.setH_title("备注:");

                        xiangQingYe.setA(list.get(position).getRenming());
                        xiangQingYe.setB(list.get(position).getRiqi().toString());
                        xiangQingYe.setC(list.get(position).getYinhangzhanghu());
                        xiangQingYe.setD(list.get(position).getKoukuan().toString());
                        xiangQingYe.setE(list.get(position).getGongzi().toString());
                        xiangQingYe.setF(list.get(position).getYifu().toString());
                        xiangQingYe.setG(list.get(position).getGongzi().subtract(list.get(position).getYifu()).toString());
                        xiangQingYe.setH(list.get(position).getBeizhu());

                        Intent intent = new Intent(GongZiMingXiActivity.this, XiangQingYeActivity.class);
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
                int position = Integer.parseInt(view.getTag().toString());
                Intent intent = new Intent(GongZiMingXiActivity.this, GongZiMingXiChangeActivity.class);
                intent.putExtra("type", R.id.update_btn);
                MyApplication myApplication = (MyApplication) getApplication();
                myApplication.setObj(list.get(position));
                startActivityForResult(intent, REQUEST_CODE_CHANG);
            }
        };
    }

    public void onInsertClick(View v) {
        Intent intent = new Intent(GongZiMingXiActivity.this, GongZiMingXiChangeActivity.class);
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


