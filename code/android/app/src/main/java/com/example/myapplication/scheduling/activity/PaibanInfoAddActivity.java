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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.scheduling.entity.PaibanDetail;
import com.example.myapplication.scheduling.entity.PaibanInfo;
import com.example.myapplication.scheduling.entity.PaibanRenyuan;
import com.example.myapplication.scheduling.entity.UserInfo;
import com.example.myapplication.scheduling.service.DepartmentService;
import com.example.myapplication.scheduling.service.PaibanDetailService;
import com.example.myapplication.scheduling.service.PaibanInfoService;
import com.example.myapplication.scheduling.service.PaibanRenyuanService;
import com.example.myapplication.utils.ToastUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class PaibanInfoAddActivity extends AppCompatActivity {
    private UserInfo userInfo;
    private PaibanInfoService paibanInfoService;
    private DepartmentService departmentService;
    private PaibanRenyuanService paibanRenyuanService;
    private PaibanDetailService paibanDetailService;

    private EditText ks;
    private EditText js;
    private Spinner department;
    private EditText banci;
    private EditText xiuxiri;
    private Spinner lunhuanfangshi;
    private EditText plan;
    private LinearLayout islunhuan;
    private EditText jiangeshu;
    private EditText lunhuanshuliang;
    private ListView listView;

    private String department_text;
    private List<String> departmentList;
    private String lunhuanfangshi_text;
    private List<String> lunhuanfangshiList;

    private List<PaibanRenyuan> list;

    private String type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paiban_info_add);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        MyApplication myApplication = (MyApplication) getApplication();
        userInfo = myApplication.getUserInfo();

        paibanInfoService = new PaibanInfoService();
        departmentService = new DepartmentService();
        paibanRenyuanService = new PaibanRenyuanService();
        paibanDetailService = new PaibanDetailService();

        ks = findViewById(R.id.ks);
        js = findViewById(R.id.js);
        department = findViewById(R.id.department);
        banci = findViewById(R.id.banci);
        xiuxiri = findViewById(R.id.xiuxiri);
        lunhuanfangshi = findViewById(R.id.lunhuanfangshi);
        plan = findViewById(R.id.plan);
        islunhuan = findViewById(R.id.islunhuan);
        jiangeshu = findViewById(R.id.jiangeshu);
        lunhuanshuliang = findViewById(R.id.lunhuanshuliang);
        listView = findViewById(R.id.renyuan_list);
        islunhuan = findViewById(R.id.islunhuan);

        lunhuanfangshiList = new ArrayList<>();
        lunhuanfangshiList.add("???");
        lunhuanfangshiList.add("???");

        SpinnerAdapter lunhuanfangshiAdapter = new ArrayAdapter<String>(PaibanInfoAddActivity.this, android.R.layout.simple_spinner_dropdown_item, lunhuanfangshiList);
        lunhuanfangshi.setAdapter(lunhuanfangshiAdapter);

        initList();

        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        if (type.equals("?????????")) {
            xiuxiri.setVisibility(View.VISIBLE);
        } else {
            lunhuanfangshi.setVisibility(View.VISIBLE);
            islunhuan.setVisibility(View.VISIBLE);
        }

        department.setOnItemSelectedListener(new departmentSelectedListener());
        lunhuanfangshi.setOnItemSelectedListener(new lunhuanfangshiSelectedListener());

        showDateOnClick(ks);
        showDateOnClick(js);
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
                if (msg.obj != null) {
                    department.setAdapter((SpinnerAdapter) msg.obj);
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                SpinnerAdapter adapter = null;
                try {
                    departmentService = new DepartmentService();
                    departmentList = new ArrayList<>();
                    departmentList = departmentService.getDepartment(userInfo.getCompany());
                    adapter = new ArrayAdapter<String>(PaibanInfoAddActivity.this, android.R.layout.simple_spinner_dropdown_item, departmentList);
                    if (departmentList.size() > 0) {
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

    private class departmentSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //????????????????????????
            department_text = departmentList.get(position);
            init();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private void init() {
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                MyApplication myApplication = (MyApplication) getApplication();
                listView.setAdapter(new MyAdapter(myApplication.getApplicationContext()));
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                paibanRenyuanService = new PaibanRenyuanService();
                list = paibanRenyuanService.getListByDepartment(userInfo.getCompany(), department_text);
                Message msg = new Message();
                msg.obj = list;
                listLoadHandler.sendMessage(msg);
            }
        }).start();
    }

    private class lunhuanfangshiSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //????????????????????????
            lunhuanfangshi_text = lunhuanfangshiList.get(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

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
        DatePickerDialog datePickerDialog = new DatePickerDialog(PaibanInfoAddActivity.this, new DatePickerDialog.OnDateSetListener() {
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
        public TextView staffName;
        public TextView phoneNumber;
        public TextView idNumber;
        public TextView departmentName;
        public TextView banci;
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
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

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
                view = inflater.inflate(R.layout.paiban_renyuan_row, null);
                // ???,View ????????????????????? holder ???.
                holder.staffName = view.findViewById(R.id.staffName);
                holder.phoneNumber = view.findViewById(R.id.phoneNumber);
                holder.idNumber = view.findViewById(R.id.idNumber);
                holder.departmentName = view.findViewById(R.id.departmentName);
                holder.banci = view.findViewById(R.id.banci);
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
            holder.staffName.setText(list.get(position).getStaff_name());
            holder.phoneNumber.setText(list.get(position).getPhone_number());
            holder.idNumber.setText(list.get(position).getId_number());
            holder.departmentName.setText(list.get(position).getDepartment_name());
            holder.banci.setText(list.get(position).getBanci());

            holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    list.get(position).setCheckbox(b);
                }
            });
            holder.cb.setChecked(list.get(position).isCheckbox());

            // ????????? view ??????
            return view;
        }
    }

    public void paibanClick(View v) {
        insert();
    }

    public static String getWeekOfDate(Date date) {
        String[] weekDaysCode = {"7", "1", "2", "3", "4", "5", "6"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return weekDaysCode[intWeek];
    }

    private void insert() {
        Button submit_button = findViewById(R.id.submit_button);
        submit_button.setEnabled(false);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddss");
        Date date = new Date();
        String paibanbiao_id = sdf2.format(date);

        List<PaibanRenyuan> ryList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isCheckbox()) {
                ryList.add(list.get(i));
            }
        }
        if (ryList.size() == 0) {
            ToastUtil.show(PaibanInfoAddActivity.this, "??????????????????");
            submit_button.setEnabled(true);
            return;
        }

        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                Button submit_button = findViewById(R.id.submit_button);
                submit_button.setEnabled(true);
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                try {
                    boolean isTianjia = false;
                    if (type.equals("?????????")) {
                        if (ks.getText().toString().equals("") || js.getText().toString().equals("") || banci.getText().toString().equals("")) {
                            ToastUtil.show(PaibanInfoAddActivity.this, "???????????????????????????????????????????????????");
                            Button submit_button = findViewById(R.id.submit_button);
                            submit_button.setEnabled(true);
                            return;
                        }
                        if (Integer.parseInt(banci.getText().toString()) > ryList.size()) {
                            ToastUtil.show(PaibanInfoAddActivity.this, "?????????????????????????????????");
                            Button submit_button = findViewById(R.id.submit_button);
                            submit_button.setEnabled(true);
                            return;
                        }
                        String xiuxi = xiuxiri.getText().toString().replace("???", ",");

                        int zu = 1;
                        for (int i = 0; i < ryList.size(); i++) {
                            ryList.get(i).setN("??????" + zu);
                            ryList.get(i).setM(paibanbiao_id);
                            if (zu == Integer.parseInt(banci.getText().toString())) {
                                zu = 1;
                            } else {
                                zu += 1;
                            }
                        }

                        @SuppressLint("SimpleDateFormat")
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                        int days = 0;
                        days = (int) ((sdf.parse(js.getText().toString()).getTime() - sdf.parse(ks.getText().toString()).getTime()) / (24 * 60 * 60 * 1000));
                        Date riqi = sdf.parse(ks.getText().toString());

                        for (int i = 0; i <= days; i++) {
                            boolean pd = true;
                            for (int j = 0; j < xiuxi.split(",").length; j++) {
                                if (xiuxi.split(",")[j].equals(getWeekOfDate(riqi))) {
                                    pd = false;
                                }
                            }
                            if (pd) {
                                for (int j = 0; j < ryList.size(); j++) {
                                    PaibanDetail paibanDetail = new PaibanDetail();
                                    paibanDetail.setStaff_name(ryList.get(j).getStaff_name());
                                    paibanDetail.setPhone_number(ryList.get(j).getPhone_number());
                                    paibanDetail.setBanci(ryList.get(j).getBanci());
                                    paibanDetail.setDepartment_name(ryList.get(j).getDepartment_name());
                                    paibanDetail.setId_number(ryList.get(j).getId_number());
                                    paibanDetail.setCompany(ryList.get(j).getCompany());
                                    paibanDetail.setB(ryList.get(j).getN());
                                    paibanDetail.setC(sdf.format(riqi));
                                    paibanDetail.setE(ryList.get(j).getM());
                                    paibanDetailService.insert(paibanDetail);
                                    isTianjia = true;
                                }
                            }
                            Calendar calendar = new GregorianCalendar();
                            calendar.setTime(riqi);
                            calendar.add(Calendar.DATE, 1);
                            riqi = calendar.getTime();
                        }
                        if (isTianjia) {
                            PaibanInfo paibanInfo = new PaibanInfo();
                            Date date = new Date();
                            @SuppressLint("SimpleDateFormat")
                            SimpleDateFormat spd = new SimpleDateFormat("yyyy-MM-dd");
                            paibanInfo.setRiqi(spd.format(date));
                            paibanInfo.setPaibanbiao_detail_id(paibanbiao_id);
                            paibanInfo.setRenshu(ryList.size() + "");
                            paibanInfo.setPlan_name(plan.getText().toString());
                            paibanInfo.setDepartment_name(department_text);
                            paibanInfo.setRemarks1(userInfo.getCompany());
                            paibanInfo.setRemarks2(paibanbiao_id);
                            paibanInfoService.insert(paibanInfo);
                        }
                    } else if (type.equals("??????")) {
                        if (ks.getText().toString().equals("") || js.getText().toString().equals("") || banci.getText().toString().equals("") || jiangeshu.getText().toString().equals("") || lunhuanshuliang.getText().toString().equals("")) {
                            ToastUtil.show(PaibanInfoAddActivity.this, "??????????????????????????????????????????????????????????????????????????????");
                            Button submit_button = findViewById(R.id.submit_button);
                            submit_button.setEnabled(true);
                            return;
                        }
                        if (ryList.size() < Integer.parseInt(banci.getText().toString()) * Integer.parseInt(lunhuanshuliang.getText().toString())) {
                            ToastUtil.show(PaibanInfoAddActivity.this, "??????????????????????????????");
                            Button submit_button = findViewById(R.id.submit_button);
                            submit_button.setEnabled(true);
                            return;
                        }

                        if (lunhuanfangshi_text.equals("???")) {
                            int zu = 1;
                            int dui = 1;
                            for (int i = 0; i < ryList.size(); i++) {
                                ryList.get(i).setN("??????" + zu);
                                ryList.get(i).setL("??????" + dui);
                                ryList.get(i).setM(paibanbiao_id);
                                if (dui == Integer.parseInt(lunhuanshuliang.getText().toString())) {
                                    dui = 1;
                                    if (zu == Integer.parseInt(banci.getText().toString())) {
                                        zu = 1;
                                    } else {
                                        zu += 1;
                                    }
                                } else {
                                    dui += 1;
                                }
                            }

                            @SuppressLint("SimpleDateFormat")
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                            int days = 0;
                            days = (int) ((sdf.parse(js.getText().toString()).getTime() - sdf.parse(ks.getText().toString()).getTime()) / (24 * 60 * 60 * 1000));
                            Date riqi = sdf.parse(ks.getText().toString());

                            int lh = 1;
                            int lh_ts = 0;

                            for (int i = 0; i <= days; i++) {
                                for (int j = 0; j < ryList.size(); j++) {
                                    if (ryList.get(j).getL().equals("??????" + lh)) {
                                        PaibanDetail paibanDetail = new PaibanDetail();
                                        paibanDetail.setStaff_name(ryList.get(j).getStaff_name());
                                        paibanDetail.setPhone_number(ryList.get(j).getPhone_number());
                                        paibanDetail.setBanci(ryList.get(j).getBanci());
                                        paibanDetail.setDepartment_name(ryList.get(j).getDepartment_name());
                                        paibanDetail.setId_number(ryList.get(j).getId_number());
                                        paibanDetail.setCompany(ryList.get(j).getCompany());
                                        paibanDetail.setB(ryList.get(j).getN());
                                        paibanDetail.setC(sdf.format(riqi));
                                        paibanDetail.setE(ryList.get(j).getM());
                                        paibanDetail.setF("??????" + lh);
                                        paibanDetailService.insert(paibanDetail);
                                        isTianjia = true;
                                    }
                                }
                                lh_ts += 1;
                                if (lh_ts == Integer.parseInt(jiangeshu.getText().toString())) {
                                    lh_ts = 0;
                                    lh += 1;
                                    if (lh > Integer.parseInt(lunhuanshuliang.getText().toString())) {
                                        lh = 1;
                                    }
                                }
                                Calendar calendar = new GregorianCalendar();
                                calendar.setTime(riqi);
                                calendar.add(Calendar.DATE, 1);
                                riqi = calendar.getTime();
                            }
                        } else {
                            int zu = 1;
                            int dui = 1;
                            for (int i = 0; i < ryList.size(); i++) {
                                ryList.get(i).setN("??????" + zu);
                                ryList.get(i).setL("??????" + dui);
                                ryList.get(i).setM(paibanbiao_id);
                                if (dui == Integer.parseInt(lunhuanshuliang.getText().toString())) {
                                    dui = 1;
                                    if (zu == Integer.parseInt(banci.getText().toString())) {
                                        zu = 1;
                                    } else {
                                        zu += 1;
                                    }
                                } else {
                                    dui += 1;
                                }
                            }
                            int lh = 1;
                            int zhou = 0;

                            @SuppressLint("SimpleDateFormat")
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                            int days = 0;
                            days = (int) ((sdf.parse(js.getText().toString()).getTime() - sdf.parse(ks.getText().toString()).getTime()) / (24 * 60 * 60 * 1000));
                            Date riqi = sdf.parse(ks.getText().toString());

                            for (int i = 0; i <= days; i++) {
                                for (int j = 0; j < ryList.size(); j++) {
                                    if (ryList.get(j).getL().equals("??????" + lh)) {
                                        PaibanDetail paibanDetail = new PaibanDetail();
                                        paibanDetail.setStaff_name(ryList.get(j).getStaff_name());
                                        paibanDetail.setPhone_number(ryList.get(j).getPhone_number());
                                        paibanDetail.setBanci(ryList.get(j).getBanci());
                                        paibanDetail.setDepartment_name(ryList.get(j).getDepartment_name());
                                        paibanDetail.setId_number(ryList.get(j).getId_number());
                                        paibanDetail.setCompany(ryList.get(j).getCompany());
                                        paibanDetail.setB(ryList.get(j).getN());
                                        paibanDetail.setC(sdf.format(riqi));
                                        paibanDetail.setE(ryList.get(j).getM());
                                        paibanDetail.setF("??????" + lh);
                                        paibanDetailService.insert(paibanDetail);
                                        isTianjia = true;
                                    }
                                }
                                if (getWeekOfDate(riqi).equals("7")) {
                                    zhou += 1;
                                    if (zhou == Integer.parseInt(jiangeshu.getText().toString())) {
                                        zhou = 0;
                                        lh += 1;
                                        if (lh > Integer.parseInt(lunhuanshuliang.getText().toString())) {
                                            lh = 1;
                                        }
                                    }
                                }
                                Calendar calendar = new GregorianCalendar();
                                calendar.setTime(riqi);
                                calendar.add(Calendar.DATE, 1);
                                riqi = calendar.getTime();
                            }
                        }
                        if (isTianjia) {
                            PaibanInfo paibanInfo = new PaibanInfo();
                            Date date = new Date();
                            @SuppressLint("SimpleDateFormat")
                            SimpleDateFormat spd = new SimpleDateFormat("yyyy-MM-dd");
                            paibanInfo.setRiqi(spd.format(date));
                            paibanInfo.setPaibanbiao_detail_id(paibanbiao_id);
                            paibanInfo.setRenshu(ryList.size() + "");
                            paibanInfo.setPlan_name(plan.getText().toString());
                            paibanInfo.setDepartment_name(department_text);
                            paibanInfo.setRemarks1(userInfo.getCompany());
                            paibanInfo.setRemarks2(paibanbiao_id);
                            paibanInfoService.insert(paibanInfo);
                            ToastUtil.show(PaibanInfoAddActivity.this, "???????????????????????????????????????");
                        }
                    }
                    msg.obj = null;
                    listLoadHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
