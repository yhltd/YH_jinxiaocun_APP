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

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.finance.entity.YhFinanceExpenditure;
import com.example.myapplication.finance.entity.YhFinanceUser;
import com.example.myapplication.finance.service.YhFinanceExpenditureService;
import com.example.myapplication.finance.service.YhFinanceIncomeService;
import com.example.myapplication.finance.service.YhFinanceInvestmentExpenditureService;
import com.example.myapplication.finance.service.YhFinanceInvestmentIncomeService;
import com.example.myapplication.finance.service.YhFinanceManagementExpenditureService;
import com.example.myapplication.finance.service.YhFinanceManagementIncomeService;
import com.example.myapplication.finance.service.YhFinanceVoucherWordService;
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
    private YhFinanceInvestmentExpenditureService yhFinanceInvestmentExpenditureService;
    private YhFinanceInvestmentIncomeService yhFinanceInvestmentIncomeService;
    private YhFinanceManagementExpenditureService yhFinanceManagementExpenditureService;
    private YhFinanceManagementIncomeService yhFinanceManagementIncomeService;
    private YhFinanceVoucherWordService yhFinanceVoucherWordService;

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

        //???????????????
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
            //????????????????????????
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
                    if (type_selectText.equals("????????????")) {
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
                    }else if (type_selectText.equals("????????????")) {
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
                    }else if (type_selectText.equals("????????????")) {
                        Message msg = new Message();
                        SpinnerAdapter adapter = null;
                        try {
                            yhFinanceInvestmentIncomeService = new YhFinanceInvestmentIncomeService();
                            list1 = yhFinanceInvestmentIncomeService.getList(yhFinanceUser.getCompany());
                            if (list1 == null) return;
                            for (int i = 0; i < list1.size(); i++) {
                                HashMap<String, Object> item = new HashMap<>();
                                item.put("shouru", list1.get(i).getShouru());
                                data.add(item);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else if (type_selectText.equals("????????????")) {
                        Message msg = new Message();
                        SpinnerAdapter adapter = null;
                        try {
                            yhFinanceInvestmentExpenditureService = new YhFinanceInvestmentExpenditureService();
                            list1 = yhFinanceInvestmentExpenditureService.getList(yhFinanceUser.getCompany());
                            if (list1 == null) return;
                            for (int i = 0; i < list1.size(); i++) {
                                HashMap<String, Object> item = new HashMap<>();
                                item.put("shouru", list1.get(i).getShouru());
                                data.add(item);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else if (type_selectText.equals("????????????")) {
                        Message msg = new Message();
                        SpinnerAdapter adapter = null;
                        try {
                            yhFinanceManagementExpenditureService = new YhFinanceManagementExpenditureService();
                            list1 = yhFinanceManagementExpenditureService.getList(yhFinanceUser.getCompany());
                            if (list1 == null) return;
                            for (int i = 0; i < list1.size(); i++) {
                                HashMap<String, Object> item = new HashMap<>();
                                item.put("shouru", list1.get(i).getShouru());
                                data.add(item);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else if (type_selectText.equals("????????????")) {
                        Message msg = new Message();
                        SpinnerAdapter adapter = null;
                        try {
                            yhFinanceManagementIncomeService = new YhFinanceManagementIncomeService();
                            list1 = yhFinanceManagementIncomeService.getList(yhFinanceUser.getCompany());
                            if (list1 == null) return;
                            for (int i = 0; i < list1.size(); i++) {
                                HashMap<String, Object> item = new HashMap<>();
                                item.put("shouru", list1.get(i).getShouru());
                                data.add(item);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else if (type_selectText.equals("?????????")) {
                        Message msg = new Message();
                        SpinnerAdapter adapter = null;
                        try {
                            yhFinanceVoucherWordService = new YhFinanceVoucherWordService();
                            list1 = yhFinanceVoucherWordService.getList(yhFinanceUser.getCompany());
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
                    if(type_selectText.equals("????????????")){
                        yhFinanceExpenditureService = new YhFinanceExpenditureService();
                        list1 = yhFinanceExpenditureService.getList(yhFinanceUser.getCompany());
                        if (list1 == null) return;
                        for (int i = 0; i < list1.size(); i++) {
                            HashMap<String, Object> item = new HashMap<>();
                            item.put("shouru", list1.get(i).getShouru());
                            data.add(item);
                        }
                    }else if(type_selectText.equals("????????????")){
                        yhFinanceIncomeService = new YhFinanceIncomeService();
                        list1 = yhFinanceIncomeService.getList(yhFinanceUser.getCompany());
                        if (list1 == null) return;
                        for (int i = 0; i < list1.size(); i++) {
                            HashMap<String, Object> item = new HashMap<>();
                            item.put("shouru", list1.get(i).getShouru());
                            data.add(item);
                        }
                    }else if(type_selectText.equals("????????????")){
                        yhFinanceInvestmentIncomeService = new YhFinanceInvestmentIncomeService();
                        list1 = yhFinanceInvestmentIncomeService.getList(yhFinanceUser.getCompany());
                        if (list1 == null) return;
                        for (int i = 0; i < list1.size(); i++) {
                            HashMap<String, Object> item = new HashMap<>();
                            item.put("shouru", list1.get(i).getShouru());
                            data.add(item);
                        }
                    }else if(type_selectText.equals("????????????")){
                        yhFinanceInvestmentExpenditureService = new YhFinanceInvestmentExpenditureService();
                        list1 = yhFinanceInvestmentExpenditureService.getList(yhFinanceUser.getCompany());
                        if (list1 == null) return;
                        for (int i = 0; i < list1.size(); i++) {
                            HashMap<String, Object> item = new HashMap<>();
                            item.put("shouru", list1.get(i).getShouru());
                            data.add(item);
                        }
                    }else if(type_selectText.equals("????????????")){
                        yhFinanceManagementExpenditureService = new YhFinanceManagementExpenditureService();
                        list1 = yhFinanceManagementExpenditureService.getList(yhFinanceUser.getCompany());
                        if (list1 == null) return;
                        for (int i = 0; i < list1.size(); i++) {
                            HashMap<String, Object> item = new HashMap<>();
                            item.put("shouru", list1.get(i).getShouru());
                            data.add(item);
                        }
                    }else if(type_selectText.equals("????????????")){
                        yhFinanceManagementIncomeService = new YhFinanceManagementIncomeService();
                        list1 = yhFinanceManagementIncomeService.getList(yhFinanceUser.getCompany());
                        if (list1 == null) return;
                        for (int i = 0; i < list1.size(); i++) {
                            HashMap<String, Object> item = new HashMap<>();
                            item.put("shouru", list1.get(i).getShouru());
                            data.add(item);
                        }
                    }else if(type_selectText.equals("?????????")){
                        yhFinanceVoucherWordService = new YhFinanceVoucherWordService();
                        list1 = yhFinanceVoucherWordService.getList(yhFinanceUser.getCompany());
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
                            ToastUtil.show(ExpenditureActivity.this, "????????????");
                            initList();
                        }
                        return true;
                    }
                });

                builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Message msg = new Message();
                                if(type_selectText.equals("????????????")){
                                    msg.obj = yhFinanceExpenditureService.delete(list1.get(position).getId());
                                }else if(type_selectText.equals("????????????")){
                                    msg.obj = yhFinanceIncomeService.delete(list1.get(position).getId());
                                }else if(type_selectText.equals("????????????")){
                                    msg.obj = yhFinanceInvestmentIncomeService.delete(list1.get(position).getId());
                                }else if(type_selectText.equals("????????????")){
                                    msg.obj = yhFinanceInvestmentExpenditureService.delete(list1.get(position).getId());
                                }else if(type_selectText.equals("????????????")){
                                    msg.obj = yhFinanceManagementExpenditureService.delete(list1.get(position).getId());
                                }else if(type_selectText.equals("????????????")){
                                    msg.obj = yhFinanceManagementIncomeService.delete(list1.get(position).getId());
                                }else if(type_selectText.equals("?????????")){
                                    msg.obj = yhFinanceVoucherWordService.delete(list1.get(position).getId());
                                }

                                deleteHandler.sendMessage(msg);
                            }
                        }).start();
                    }
                });

                builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setMessage("??????????????????");
                builder.setTitle("??????");
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
