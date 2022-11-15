package com.example.myapplication.finance.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.LoginActivity;
import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.finance.entity.YhFinanceDepartment;
import com.example.myapplication.finance.entity.YhFinanceExpenditure;
import com.example.myapplication.finance.entity.YhFinanceUser;
import com.example.myapplication.finance.service.YhFinanceDepartmentService;
import com.example.myapplication.finance.service.YhFinanceExpenditureService;
import com.example.myapplication.finance.service.YhFinanceIncomeService;
import com.example.myapplication.finance.service.YhFinanceInvestmentExpenditure;
import com.example.myapplication.finance.service.YhFinanceInvestmentIncome;
import com.example.myapplication.finance.service.YhFinanceManagementExpenditure;
import com.example.myapplication.finance.service.YhFinanceManagementIncome;
import com.example.myapplication.finance.service.YhFinanceUserService;
import com.example.myapplication.finance.service.YhFinanceVoucherWord;
import com.example.myapplication.jxc.service.YhJinXiaoCunUserService;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpenditureActivity extends AppCompatActivity {

    private final static int REQUEST_CODE_CHANG = 1;

    private YhFinanceUser yhFinanceUser;

    private YhFinanceExpenditureService yhFinanceExpenditureService;
    private YhFinanceIncomeService yhFinanceIncomeService;
    private YhFinanceInvestmentExpenditure yhFinanceInvestmentExpenditure;
    private YhFinanceInvestmentIncome yhFinanceInvestmentIncome;
    private YhFinanceManagementExpenditure yhFinanceManagementExpenditure;
    private YhFinanceManagementIncome yhFinanceManagementIncome;
    private YhFinanceVoucherWord yhFinanceVoucherWord;

    private ListView listView1;
    private Spinner type_select;
    private String type_selectText;

    private TextView textView1;

    List<YhFinanceExpenditure> list1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expenditure);
        type_select = findViewById(R.id.type_select);

        String[] type_selectArray = getResources().getStringArray(R.array.type);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, type_selectArray);
        type_select.setAdapter(adapter);

        type_select.setOnItemSelectedListener(new typeSelectSelectedListener());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //初始化控件
        listView1 = findViewById(R.id.jingyingshouru_list);
        textView1 = findViewById(R.id.jingyingshouru_insert);

        MyApplication myApplication = (MyApplication) getApplication();
        yhFinanceUser = myApplication.getYhFinanceUser();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class typeSelectSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //获取选择的项的值
            type_selectText = type_select.getItemAtPosition(position).toString();
            textView1.setText(type_selectText);
            Handler listLoadHandler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(@NonNull Message msg) {
                    listView1.setAdapter(StringUtils.cast(msg.obj));
                    return true;
                }
            });

            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<HashMap<String, Object>> data = new ArrayList<>();
                    if (type_selectText.equals("经营收入")) {
                        Message msg = new Message();
                        SpinnerAdapter adapter = null;
                        try {
                            yhFinanceExpenditureService = new YhFinanceExpenditureService();
                            list1 = yhFinanceExpenditureService.getList(yhFinanceUser.getCompany());
                            if (list1 == null) return;
                            for (int i = 0; i < list1.size(); i++) {
                                HashMap<String, Object> item = new HashMap<>();
                                item.put("shouru", list1.get(i).getShouru());
                                data.add(item);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else if (type_selectText.equals("经营支出")) {
                        Message msg = new Message();
                        SpinnerAdapter adapter = null;
                        try {
                            yhFinanceIncomeService = new YhFinanceIncomeService();
                            list1 = yhFinanceIncomeService.getList(yhFinanceUser.getCompany());
                            if (list1 == null) return;
                            for (int i = 0; i < list1.size(); i++) {
                                HashMap<String, Object> item = new HashMap<>();
                                item.put("shouru", list1.get(i).getShouru());
                                data.add(item);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else if (type_selectText.equals("筹资收入")) {
                        Message msg = new Message();
                        SpinnerAdapter adapter = null;
                        try {
                            yhFinanceInvestmentIncome = new YhFinanceInvestmentIncome();
                            list1 = yhFinanceInvestmentIncome.getList(yhFinanceUser.getCompany());
                            if (list1 == null) return;
                            for (int i = 0; i < list1.size(); i++) {
                                HashMap<String, Object> item = new HashMap<>();
                                item.put("shouru", list1.get(i).getShouru());
                                data.add(item);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else if (type_selectText.equals("筹资支出")) {
                        Message msg = new Message();
                        SpinnerAdapter adapter = null;
                        try {
                            yhFinanceInvestmentExpenditure = new YhFinanceInvestmentExpenditure();
                            list1 = yhFinanceInvestmentExpenditure.getList(yhFinanceUser.getCompany());
                            if (list1 == null) return;
                            for (int i = 0; i < list1.size(); i++) {
                                HashMap<String, Object> item = new HashMap<>();
                                item.put("shouru", list1.get(i).getShouru());
                                data.add(item);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else if (type_selectText.equals("投资收入")) {
                        Message msg = new Message();
                        SpinnerAdapter adapter = null;
                        try {
                            yhFinanceManagementExpenditure = new YhFinanceManagementExpenditure();
                            list1 = yhFinanceManagementExpenditure.getList(yhFinanceUser.getCompany());
                            if (list1 == null) return;
                            for (int i = 0; i < list1.size(); i++) {
                                HashMap<String, Object> item = new HashMap<>();
                                item.put("shouru", list1.get(i).getShouru());
                                data.add(item);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else if (type_selectText.equals("投资支出")) {
                        Message msg = new Message();
                        SpinnerAdapter adapter = null;
                        try {
                            yhFinanceManagementIncome = new YhFinanceManagementIncome();
                            list1 = yhFinanceManagementIncome.getList(yhFinanceUser.getCompany());
                            if (list1 == null) return;
                            for (int i = 0; i < list1.size(); i++) {
                                HashMap<String, Object> item = new HashMap<>();
                                item.put("shouru", list1.get(i).getShouru());
                                data.add(item);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else if (type_selectText.equals("凭证字")) {
                        Message msg = new Message();
                        SpinnerAdapter adapter = null;
                        try {
                            yhFinanceVoucherWord = new YhFinanceVoucherWord();
                            list1 = yhFinanceVoucherWord.getList(yhFinanceUser.getCompany());
                            if (list1 == null) return;
                            for (int i = 0; i < list1.size(); i++) {
                                HashMap<String, Object> item = new HashMap<>();
                                item.put("shouru", list1.get(i).getShouru());
                                data.add(item);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    SimpleAdapter adapter = new SimpleAdapter(ExpenditureActivity.this, data, R.layout.expenditure_jingyingshouru_row, new String[]{"shouru"}, new int[]{R.id.shouru}) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            final LinearLayout view = (LinearLayout) super.getView(position, convertView, parent);
                            LinearLayout linearLayout = (LinearLayout) view.getChildAt(0);
                            linearLayout.setOnLongClickListener(onItemLongClick1());
                            linearLayout.setOnClickListener(updateClick1());
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


    private void initList() {
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                listView1.setAdapter(StringUtils.cast(msg.obj));
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<HashMap<String, Object>> data = new ArrayList<>();
                try {
                    if(type_selectText.equals("经营收入")){
                        yhFinanceExpenditureService = new YhFinanceExpenditureService();
                        list1 = yhFinanceExpenditureService.getList(yhFinanceUser.getCompany());
                        if (list1 == null) return;
                        for (int i = 0; i < list1.size(); i++) {
                            HashMap<String, Object> item = new HashMap<>();
                            item.put("shouru", list1.get(i).getShouru());
                            data.add(item);
                        }
                    }else if(type_selectText.equals("经营支出")){
                        yhFinanceIncomeService = new YhFinanceIncomeService();
                        list1 = yhFinanceIncomeService.getList(yhFinanceUser.getCompany());
                        if (list1 == null) return;
                        for (int i = 0; i < list1.size(); i++) {
                            HashMap<String, Object> item = new HashMap<>();
                            item.put("shouru", list1.get(i).getShouru());
                            data.add(item);
                        }
                    }else if(type_selectText.equals("筹资收入")){
                        yhFinanceInvestmentIncome = new YhFinanceInvestmentIncome();
                        list1 = yhFinanceInvestmentIncome.getList(yhFinanceUser.getCompany());
                        if (list1 == null) return;
                        for (int i = 0; i < list1.size(); i++) {
                            HashMap<String, Object> item = new HashMap<>();
                            item.put("shouru", list1.get(i).getShouru());
                            data.add(item);
                        }
                    }else if(type_selectText.equals("筹资支出")){
                        yhFinanceInvestmentExpenditure = new YhFinanceInvestmentExpenditure();
                        list1 = yhFinanceInvestmentExpenditure.getList(yhFinanceUser.getCompany());
                        if (list1 == null) return;
                        for (int i = 0; i < list1.size(); i++) {
                            HashMap<String, Object> item = new HashMap<>();
                            item.put("shouru", list1.get(i).getShouru());
                            data.add(item);
                        }
                    }else if(type_selectText.equals("投资收入")){
                        yhFinanceManagementExpenditure = new YhFinanceManagementExpenditure();
                        list1 = yhFinanceManagementExpenditure.getList(yhFinanceUser.getCompany());
                        if (list1 == null) return;
                        for (int i = 0; i < list1.size(); i++) {
                            HashMap<String, Object> item = new HashMap<>();
                            item.put("shouru", list1.get(i).getShouru());
                            data.add(item);
                        }
                    }else if(type_selectText.equals("投资支出")){
                        yhFinanceManagementIncome = new YhFinanceManagementIncome();
                        list1 = yhFinanceManagementIncome.getList(yhFinanceUser.getCompany());
                        if (list1 == null) return;
                        for (int i = 0; i < list1.size(); i++) {
                            HashMap<String, Object> item = new HashMap<>();
                            item.put("shouru", list1.get(i).getShouru());
                            data.add(item);
                        }
                    }else if(type_selectText.equals("凭证字")){
                        yhFinanceVoucherWord = new YhFinanceVoucherWord();
                        list1 = yhFinanceVoucherWord.getList(yhFinanceUser.getCompany());
                        if (list1 == null) return;
                        for (int i = 0; i < list1.size(); i++) {
                            HashMap<String, Object> item = new HashMap<>();
                            item.put("shouru", list1.get(i).getShouru());
                            data.add(item);
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

                SimpleAdapter adapter = new SimpleAdapter(ExpenditureActivity.this, data, R.layout.expenditure_jingyingshouru_row, new String[]{"shouru"}, new int[]{R.id.shouru}) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        final LinearLayout view = (LinearLayout) super.getView(position, convertView, parent);
                        LinearLayout linearLayout = (LinearLayout) view.getChildAt(0);
                        linearLayout.setOnLongClickListener(onItemLongClick1());
                        linearLayout.setOnClickListener(updateClick1());
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


    public View.OnClickListener updateClick1() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = Integer.parseInt(view.getTag().toString());
                Intent intent = new Intent(ExpenditureActivity.this, ExpenditureChangeActivity.class);
                intent.putExtra("type", R.id.update_btn);
                intent.putExtra("service", type_selectText);
                MyApplication myApplication = (MyApplication) getApplication();
                myApplication.setObj(list1.get(position));
                startActivityForResult(intent, REQUEST_CODE_CHANG);
            }
        };
    }


    public View.OnLongClickListener onItemLongClick1() {
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ExpenditureActivity.this);
                int position = Integer.parseInt(view.getTag().toString());
                Handler deleteHandler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message msg) {
                        if ((boolean) msg.obj) {
                            ToastUtil.show(ExpenditureActivity.this, "删除成功");
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
                                if(type_selectText.equals("经营收入")){
                                    msg.obj = yhFinanceExpenditureService.delete(list1.get(position).getId());
                                }else if(type_selectText.equals("经营支出")){
                                    msg.obj = yhFinanceIncomeService.delete(list1.get(position).getId());
                                }else if(type_selectText.equals("筹资收入")){
                                    msg.obj = yhFinanceInvestmentIncome.delete(list1.get(position).getId());
                                }else if(type_selectText.equals("筹资支出")){
                                    msg.obj = yhFinanceInvestmentExpenditure.delete(list1.get(position).getId());
                                }else if(type_selectText.equals("投资收入")){
                                    msg.obj = yhFinanceManagementExpenditure.delete(list1.get(position).getId());
                                }else if(type_selectText.equals("投资支出")){
                                    msg.obj = yhFinanceManagementIncome.delete(list1.get(position).getId());
                                }else if(type_selectText.equals("凭证字")){
                                    msg.obj = yhFinanceVoucherWord.delete(list1.get(position).getId());
                                }

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

    public void onInsertClick(View v) {
        Intent intent = new Intent(ExpenditureActivity.this, ExpenditureChangeActivity.class);
        intent.putExtra("type", R.id.insert_btn);
        intent.putExtra("service", type_selectText);
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
