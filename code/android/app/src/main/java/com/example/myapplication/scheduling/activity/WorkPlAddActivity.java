package com.example.myapplication.scheduling.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.scheduling.entity.ModuleInfo;
import com.example.myapplication.scheduling.entity.OrderInfo;
import com.example.myapplication.scheduling.entity.UserInfo;
import com.example.myapplication.scheduling.entity.WorkDetail;
import com.example.myapplication.scheduling.entity.WorkModule;
import com.example.myapplication.scheduling.service.ModuleInfoService;
import com.example.myapplication.scheduling.service.OrderInfoService;
import com.example.myapplication.scheduling.service.WorkDetailService;
import com.example.myapplication.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class WorkPlAddActivity extends AppCompatActivity {
    private UserInfo userInfo;
    private ModuleInfoService moduleInfoService;
    private OrderInfoService orderInfoService;
    private WorkDetailService workDetailService;

    private EditText ks_riqi;
    private Spinner module;
    private ListView listView;
    private SpinnerAdapter moduleAdapter;

    private List<ModuleInfo> moduleInfoList;
    private List<OrderInfo> orderInfoList;

    private String type;

    private int module_id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work_pl_add);

        MyApplication myApplication = (MyApplication) getApplication();
        userInfo = myApplication.getUserInfo();

        moduleInfoService = new ModuleInfoService();
        orderInfoService = new OrderInfoService();
        workDetailService = new WorkDetailService();

        ks_riqi = findViewById(R.id.riqi);
        module = findViewById(R.id.module);
        listView = findViewById(R.id.order_list);

        Intent intent = getIntent();
        type = intent.getStringExtra("type");

        initList();
        showDateOnClick(ks_riqi);

        module.setOnItemSelectedListener(new moduleSelectedListener());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initList() {
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                module.setAdapter(moduleAdapter);
                MyApplication myApplication = (MyApplication) getApplication();
                listView.setAdapter(new MyAdapter(myApplication.getApplicationContext()));
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                try {
                    List<String> mList = new ArrayList<>();
                    moduleInfoList = moduleInfoService.getList(userInfo.getCompany(), "??????");
                    if (moduleInfoList != null) {
                        for (ModuleInfo moduleInfo : moduleInfoList) {
                            mList.add(moduleInfo.getName());
                        }
                    }
                    orderInfoList = orderInfoService.getOrderId();
                    moduleAdapter = new ArrayAdapter<String>(WorkPlAddActivity.this, android.R.layout.simple_spinner_dropdown_item, mList);
                    listLoadHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private class moduleSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //????????????????????????
            if (moduleInfoList != null) {
                module_id = moduleInfoList.get(position).getId();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(WorkPlAddActivity.this, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                editText.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    // ???????????? ViewHolder ???
    // ???????????? list_item_layout.xml????????????View??????.
    // ????????????ViewHolder????????????Item???Tag???,
    // ???????????????????????????????????????Item???????????????????????????
    // ????????????????????? findViewById?????????,????????????
    class ViewHolder {
        public TextView order_number;
        public TextView num;
        public CheckBox cb;
    }

    class MyAdapter extends BaseAdapter {
        Context context;
        private LayoutInflater inflater = null;

        public MyAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return orderInfoList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // ???????????? ViewHolder??????
            ViewHolder holder = null;
            View view;
            // convertView .????????????????????????Item???View??????, ??????View convertView ?????????convertView????????????????????????????????? ????????????
            // ???ListView?????????????????? ??????item??????????????? ?????????????????? ?????????Android????????????????????????view ,???item1???????????????????????? ???????????????new??????View???????????????item_new
            // ????????????????????????convertView ??????????????????????????? ??????????????????new View???????????????
            // ????????????????????? convertView ?????????????????????
            if (convertView == null) {
                //view = LayoutInflater.from(context).inflate(R.layout.ruku_row,null);
                // ???????????? ViewHolder??????,??????????????? View???????????????,??????,??????????????????????????? ??????findViewById???.
                holder = new ViewHolder();
                // ?????? LayoutInflater ??????View
                view = inflater.inflate(R.layout.pl_order_row, null);
                // ???,View ????????????????????? holder ???.
                holder.order_number = view.findViewById(R.id.order_number);
                holder.num = view.findViewById(R.id.num);
                holder.cb = view.findViewById(R.id.cb);
                holder.cb.setVisibility(View.VISIBLE);
                // ???Hodler ????????? convertView ??? Tag ???.
                view.setTag(holder);
            } else {
                view = convertView;
                // ?????????????????? convertView.????????????????????????Tag?????? Holder??????
                holder = (ViewHolder) view.getTag();
            }

            // ???Holder?????????????????????????????????????????????
            holder.order_number.setText(orderInfoList.get(position).getOrder_id());
            holder.num.setText(orderInfoList.get(position).getSet_num() + "");

            holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    orderInfoList.get(position).setCheck(b);
                }
            });
            holder.cb.setChecked(orderInfoList.get(position).isCheck());

            // ????????? view ??????
            return view;
        }
    }

    public void plClick(View v) {
        List<OrderInfo> orderList = new ArrayList<>();
        for (int i = 0; i < orderInfoList.size(); i++) {
            if (orderInfoList.get(i).isCheck()) {
                orderList.add(orderInfoList.get(i));
            }
        }
        if (orderList.size() == 0) {
            ToastUtil.show(WorkPlAddActivity.this, "??????????????????");
            return;
        }
        if (ks_riqi.getText().toString().equals("")) {
            ToastUtil.show(WorkPlAddActivity.this, "????????????????????????");
            return;
        }

        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                ToastUtil.show(WorkPlAddActivity.this, "????????????");
                back();
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                try {
                    for (OrderInfo orderInfo : orderList) {
                        WorkDetail workDetail = new WorkDetail();
                        workDetail.setOrder_id(orderInfo.getId());
                        workDetail.setWork_num(orderInfo.getSet_num());
                        workDetail.setWork_start_date(ks_riqi.getText().toString());
                        workDetail.setCompany(userInfo.getCompany());
                        workDetail.setIs_insert(0);
                        workDetail.setType(type);

                        workDetailService.insert(workDetail);

                        List<WorkDetail> wdList = workDetailService.getLastList(userInfo.getCompany());
                        if (wdList != null) {
                            workDetail.setRow_num(wdList.get(0).getId() + 1);
                            workDetail.setId(wdList.get(0).getId());
                            workDetailService.update(workDetail);

                            WorkModule workModule = new WorkModule();
                            workModule.setWork_id(wdList.get(0).getId());
                            workModule.setModule_id(module_id);
                            workDetailService.insertModule(workModule);
                        }
                    }
                    listLoadHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


}
