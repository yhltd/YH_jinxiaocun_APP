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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.XiangQingYeActivity;
import com.example.myapplication.entity.XiangQingYe;
import com.example.myapplication.finance.entity.YhFinanceJiJianPeiZhi;
import com.example.myapplication.finance.entity.YhFinanceJiJianTaiZhang;
import com.example.myapplication.finance.entity.YhFinanceQuanXian;
import com.example.myapplication.finance.entity.YhFinanceUser;
import com.example.myapplication.finance.service.YhFinanceJiJianZongZhangService;
import com.example.myapplication.finance.service.YhFinanceKehuPeizhiService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class JiJianZongZhangActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_CHANG = 1;
    MyApplication myApplication;

    private YhFinanceUser yhFinanceUser;
    private YhFinanceQuanXian yhFinanceQuanXian;
    private YhFinanceJiJianZongZhangService yhFinanceJiJianZongZhangService;
    private YhFinanceKehuPeizhiService yhFinanceKehuPeizhiService;
    private Spinner kehu_select;
    private EditText project;
    private ListView listView;

    private ListView listView_block;
    private HorizontalScrollView list_table;
    private SimpleAdapter adapter;
    private SimpleAdapter adapter_block;

    private String kehu_selectText;
    private String projectText;

    private Button sel_button;

    List<YhFinanceJiJianTaiZhang> list;
    List<String> kehu_array;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        myApplication = (MyApplication) getApplication();
        yhFinanceUser = myApplication.getYhFinanceUser();
        yhFinanceQuanXian = myApplication.getYhFinanceQuanXian();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jijianzongzhang);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        yhFinanceKehuPeizhiService=new YhFinanceKehuPeizhiService();


        //初始化控件
        project = findViewById(R.id.project);
        kehu_select = findViewById(R.id.kehu_select);
        listView = findViewById(R.id.jijianzongzhang_list);
        listView_block = findViewById(R.id.list_block);
        list_table = findViewById(R.id.list_table);
        sel_button = findViewById(R.id.sel_button);
        sel_button.setOnClickListener(selClick());
        sel_button.requestFocus();
        init_select();

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

    public void init_select() {
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                kehu_select.setAdapter(StringUtils.cast(msg.obj));
                initList();
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
                        kehu_array.add("");
                        for (int i = 0; i < kehuList.size(); i++) {
                            if(!kehuList.get(i).getPeizhi().equals("")){
                                kehu_array.add(kehuList.get(i).getPeizhi());
                            }
                        }
                    }
                    adapter = new ArrayAdapter<String>(JiJianZongZhangActivity.this, android.R.layout.simple_spinner_dropdown_item, kehu_array);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.obj = adapter;
                listLoadHandler.sendMessage(msg);
            }
        }).start();
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
        projectText = project.getText().toString();
        kehu_selectText = kehu_select.getSelectedItem().toString();
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
                    yhFinanceJiJianZongZhangService = new YhFinanceJiJianZongZhangService();
                    list = yhFinanceJiJianZongZhangService.getList(yhFinanceUser.getCompany(),kehu_selectText,projectText);
                    if (list == null) return;

                } catch (Exception e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < list.size(); i++) {
                    HashMap<String, Object> item = new HashMap<>();
                    item.put("project", list.get(i).getProject());
                    item.put("kehu", list.get(i).getKehu());
                    item.put("receipts", list.get(i).getReceipts());
                    item.put("receivable", list.get(i).getReceivable());
                    item.put("weishou", list.get(i).getReceivable().subtract(list.get(i).getReceipts()));
                    item.put("cope", list.get(i).getCope());
                    item.put("payment", list.get(i).getPayment());
                    item.put("weifu", list.get(i).getCope().subtract(list.get(i).getPayment()));
                    data.add(item);
                }

                adapter = new SimpleAdapter(JiJianZongZhangActivity.this, data, R.layout.jijianzongzhang_row, new String[]{"kehu","project","receivable","receipts","weishou","cope","payment","weifu"}, new int[]{R.id.kehu,R.id.project,R.id.receivable,R.id.receipts,R.id.weishou,R.id.cope,R.id.payment,R.id.weifu}) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        final LinearLayout view = (LinearLayout) super.getView(position, convertView, parent);
                        LinearLayout linearLayout = (LinearLayout) view.getChildAt(0);
                        linearLayout.setOnClickListener(updateClick());
                        linearLayout.setTag(position);
                        return view;
                    }
                };

                adapter_block = new SimpleAdapter(JiJianZongZhangActivity.this, data, R.layout.jijianzongzhang_row_block, new String[]{"kehu","project","receivable","receipts","weishou","cope","payment","weifu"}, new int[]{R.id.kehu,R.id.project,R.id.receivable,R.id.receipts,R.id.weishou,R.id.cope,R.id.payment,R.id.weifu}) {
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

    public View.OnClickListener updateClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(JiJianZongZhangActivity.this);
                int position = Integer.parseInt(view.getTag().toString());

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        XiangQingYe xiangQingYe = new XiangQingYe();

                        xiangQingYe.setA_title("客户/供应商:");
                        xiangQingYe.setB_title("项目名称:");
                        xiangQingYe.setC_title("应收:");
                        xiangQingYe.setD_title("实收:");
                        xiangQingYe.setE_title("未收:");
                        xiangQingYe.setF_title("应付:");
                        xiangQingYe.setG_title("实付:");
                        xiangQingYe.setH_title("未付:");

                        xiangQingYe.setA(list.get(position).getProject());
                        xiangQingYe.setB(list.get(position).getKehu());
                        xiangQingYe.setC(list.get(position).getReceipts().toString());
                        xiangQingYe.setD(list.get(position).getReceivable().toString());
                        xiangQingYe.setE(list.get(position).getReceivable().subtract(list.get(position).getReceipts()).toString());
                        xiangQingYe.setF(list.get(position).getCope().toString());
                        xiangQingYe.setG(list.get(position).getPayment().toString());
                        xiangQingYe.setH(list.get(position).getCope().subtract(list.get(position).getPayment()).toString());

                        Intent intent = new Intent(JiJianZongZhangActivity.this, XiangQingYeActivity.class);
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


