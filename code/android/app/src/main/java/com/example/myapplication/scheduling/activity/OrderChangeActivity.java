package com.example.myapplication.scheduling.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.scheduling.entity.BomInfo;
import com.example.myapplication.scheduling.entity.OrderBom;
import com.example.myapplication.scheduling.entity.OrderInfo;
import com.example.myapplication.scheduling.entity.PaibanInfo;
import com.example.myapplication.scheduling.entity.UserInfo;
import com.example.myapplication.scheduling.service.BomInfoService;
import com.example.myapplication.scheduling.service.OrderInfoService;
import com.example.myapplication.scheduling.service.PaibanRenyuanService;
import com.example.myapplication.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderChangeActivity extends AppCompatActivity {
    private UserInfo userInfo;
    private OrderInfo orderInfo;
    private OrderInfoService orderInfoService;
    private BomInfoService bomInfoService;

    private EditText order_id;
    private EditText code;
    private EditText product_name;
    private EditText norms;
    private EditText set_num;
    private Spinner is_complete;

    private ListView listView;
    private List<String> is_complete_list;

    private List<BomInfo> list;

    int typeId;

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        MyApplication myApplication = (MyApplication) getApplication();
        userInfo = myApplication.getUserInfo();

        orderInfoService = new OrderInfoService();
        bomInfoService = new BomInfoService();

        order_id = findViewById(R.id.order_id);
        code = findViewById(R.id.code);
        product_name = findViewById(R.id.product_name);
        norms = findViewById(R.id.norms);
        set_num = findViewById(R.id.set_num);
        is_complete = findViewById(R.id.is_complete);
        listView = findViewById(R.id.bom_list);

        is_complete_list = new ArrayList<>();
        is_complete_list.add("???");
        is_complete_list.add("???");

        SpinnerAdapter adapter = new ArrayAdapter<String>(OrderChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, is_complete_list);
        is_complete.setAdapter(adapter);

        initList();

        Intent intent = getIntent();
        typeId = intent.getIntExtra("type", 0);
        if (typeId == R.id.insert_btn) {
            orderInfo = new OrderInfo();
            Button btn = findViewById(typeId);
            btn.setVisibility(View.VISIBLE);
        } else if (typeId == R.id.update_btn) {
            orderInfo = (OrderInfo) myApplication.getObj();
            Button btn = findViewById(typeId);
            btn.setVisibility(View.VISIBLE);

            order_id.setText(orderInfo.getOrder_id());
            code.setText(orderInfo.getCode());
            product_name.setText(orderInfo.getProduct_name());
            norms.setText(orderInfo.getNorms());
            set_num.setText(orderInfo.getSet_num() + "");
            if (orderInfo.getIs_complete().equals("???")) {
                is_complete.setSelection(1);
            } else {
                is_complete.setSelection(0);
            }
        }

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
                MyApplication myApplication = (MyApplication) getApplication();
                listView.setAdapter(new MyAdapter(myApplication.getApplicationContext()));
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                bomInfoService = new BomInfoService();
                if (typeId == R.id.insert_btn) {
                    list = bomInfoService.getAddList(userInfo.getCompany());
                } else if (typeId == R.id.update_btn) {
                    list = bomInfoService.getAddList(userInfo.getCompany());
                    List<BomInfo> updlist = bomInfoService.getUpdOrderBom(orderInfo.getId());
                    for (BomInfo bomInfo : list) {
                        for (BomInfo bomInfo2 : updlist) {
                            if (bomInfo.getId() == bomInfo2.getId()) {
                                bomInfo.setCheck(true);
                                bomInfo.setUse_num(bomInfo2.getUse_num());
                            }
                        }
                    }
                }
                Message msg = new Message();
                msg.obj = list;
                listLoadHandler.sendMessage(msg);
            }
        }).start();
    }

    // ???????????? ViewHolder ???
    // ???????????? list_item_layout.xml????????????View??????.
    // ????????????ViewHolder????????????Item???Tag???,
    // ???????????????????????????????????????Item???????????????????????????
    // ????????????????????? findViewById?????????,????????????
    class ViewHolder {
        public TextView code;
        public TextView name;
        public TextView comment;
        public EditText use_num;
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
                view = inflater.inflate(R.layout.order_bom_row, null);
                // ???,View ????????????????????? holder ???.
                holder.code = view.findViewById(R.id.code);
                holder.name = view.findViewById(R.id.name);
                holder.comment = view.findViewById(R.id.comment);
                holder.use_num = view.findViewById(R.id.use_num);
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
            holder.code.setText(list.get(position).getCode());
            holder.name.setText(list.get(position).getName());
            holder.comment.setText(list.get(position).getComment());

            holder.use_num.setTag(position);
            holder.use_num.clearFocus();
            if (typeId == R.id.update_btn) {
                holder.use_num.setText(((int) list.get(position).getUse_num()) + "");
            }


            final EditText num = holder.use_num;
            holder.use_num.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    int a = (int) num.getTag();
                    if (!s.toString().equals("")) {
                        list.get(a).setUse_num(Double.parseDouble(s.toString()));
                    } else {
                        list.get(a).setUse_num(0);
                    }
                }
            });


            holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    list.get(position).setCheck(b);
                }
            });
            holder.cb.setChecked(list.get(position).isCheck());

            // ????????? view ??????
            return view;
        }
    }

    public void insertClick(View v) {
        if (!checkForm()) return;
        List<BomInfo> bmList = new ArrayList<>();
        for (BomInfo bomInfo : list) {
            if (bomInfo.isCheck()) {
                bmList.add(bomInfo);
            }
        }
        if (bmList.size() == 0) {
            ToastUtil.show(OrderChangeActivity.this, "??????????????????");
            return;
        }
        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(OrderChangeActivity.this, "????????????");
                    back();
                } else {
                    ToastUtil.show(OrderChangeActivity.this, "??????????????????????????????");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = orderInfoService.insert(orderInfo);
                List<OrderInfo> getLast = orderInfoService.getLast();
                for (BomInfo bomInfo : bmList) {
                    OrderBom orderBom = new OrderBom();
                    orderBom.setOrder_id(getLast.get(0).getId());
                    orderBom.setBom_id(bomInfo.getId());
                    orderBom.setUse_num((int) bomInfo.getUse_num());
                    orderInfoService.insertOrderBom(orderBom);
                }
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    public void updateClick(View v) {
        if (!checkForm()) return;
        List<BomInfo> bmList = new ArrayList<>();
        for (BomInfo bomInfo : list) {
            if (bomInfo.isCheck()) {
                bmList.add(bomInfo);
            }
        }
        if (bmList.size() == 0) {
            ToastUtil.show(OrderChangeActivity.this, "??????????????????");
            return;
        }
        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(OrderChangeActivity.this, "????????????");
                    back();
                } else {
                    ToastUtil.show(OrderChangeActivity.this, "??????????????????????????????");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = orderInfoService.update(orderInfo);
                List<OrderInfo> getLast = orderInfoService.getLast();
                orderInfoService.deleteOrderBom(orderInfo.getId());
                for (BomInfo bomInfo : bmList) {
                    OrderBom orderBom = new OrderBom();
                    orderBom.setOrder_id(getLast.get(0).getId());
                    orderBom.setBom_id(bomInfo.getId());
                    orderBom.setUse_num((int) bomInfo.getUse_num());
                    orderInfoService.insertOrderBom(orderBom);
                }
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    private boolean checkForm() {
        if (order_id.getText().toString().equals("")) {
            ToastUtil.show(OrderChangeActivity.this, "??????????????????");
            return false;
        } else {
            orderInfo.setOrder_id(order_id.getText().toString());
        }
        if (code.getText().toString().equals("")) {
            ToastUtil.show(OrderChangeActivity.this, "?????????????????????");
            return false;
        } else {
            orderInfo.setCode(code.getText().toString());
        }
        if (product_name.getText().toString().equals("")) {
            ToastUtil.show(OrderChangeActivity.this, "?????????????????????");
            return false;
        } else {
            orderInfo.setProduct_name(product_name.getText().toString());
        }
        if (set_num.getText().toString().equals("")) {
            ToastUtil.show(OrderChangeActivity.this, "?????????????????????");
            return false;
        } else {
            orderInfo.setSet_num(Integer.parseInt(set_num.getText().toString()));
        }
        if (typeId == R.id.insert_btn) {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat spd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date date = new Date();
            orderInfo.setSet_date(spd.format(date));
        }
        orderInfo.setNorms(norms.getText().toString());
        orderInfo.setIs_complete(is_complete.getSelectedItem().toString());
        orderInfo.setCompany(userInfo.getCompany());

        return true;
    }

    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }

}
