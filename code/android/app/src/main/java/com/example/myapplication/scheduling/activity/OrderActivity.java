package com.example.myapplication.scheduling.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.myapplication.scheduling.entity.Department;
import com.example.myapplication.scheduling.entity.OrderInfo;
import com.example.myapplication.scheduling.entity.UserInfo;
import com.example.myapplication.scheduling.service.OrderCheckService;
import com.example.myapplication.scheduling.service.OrderInfoService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class OrderActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_CHANG = 1;
    private UserInfo userInfo;
    private Department department;
    private OrderInfoService orderInfoService;

    private EditText product_name_text;
    private EditText order_id_text;


    private ListView listView;
    private Button sel_button;

    List<OrderInfo> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        userInfo = myApplication.getUserInfo();
        department = myApplication.getPcDepartment();

        listView = findViewById(R.id.order_list);
        product_name_text = findViewById(R.id.product_name_text);
        order_id_text = findViewById(R.id.order_id_text);

        sel_button = findViewById(R.id.sel_button);

        initList();
        sel_button.setOnClickListener(selClick());
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
        LoadingDialog.getInstance(this).show();
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                listView.setAdapter(StringUtils.cast(msg.obj));
                LoadingDialog.getInstance(getApplicationContext()).dismiss();
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<HashMap<String, Object>> data = new ArrayList<>();
                try {
                    orderInfoService = new OrderInfoService();
                    list = orderInfoService.getList(userInfo.getCompany(), product_name_text.getText().toString(), order_id_text.getText().toString());
                    if (list == null) return;

                    for (int i = 0; i < list.size(); i++) {
                        HashMap<String, Object> item = new HashMap<>();
                        item.put("order_id", list.get(i).getOrder_id());
                        item.put("code", list.get(i).getCode());
                        item.put("product_name", list.get(i).getProduct_name());
                        item.put("norms", list.get(i).getNorms());
                        item.put("set_date", list.get(i).getSet_date());
                        item.put("set_num", list.get(i).getSet_num());
                        item.put("is_complete", list.get(i).getIs_complete());
                        data.add(item);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                SimpleAdapter adapter = new SimpleAdapter(OrderActivity.this, data, R.layout.order_row, new String[]{"order_id", "code", "product_name", "norms", "set_date", "set_num", "is_complete"}, new int[]{R.id.order_id, R.id.code, R.id.product_name, R.id.norms, R.id.set_date, R.id.set_num, R.id.is_complete}) {
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

    public View.OnClickListener selClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initList();
            }
        };
    }

    public void onInsertClick(View v) {
        if(!department.getAdd().equals("是")){
            ToastUtil.show(OrderActivity.this, "无权限！");
            return;
        }
        Intent intent = new Intent(OrderActivity.this, OrderChangeActivity.class);
        intent.putExtra("type", R.id.insert_btn);
        startActivityForResult(intent, REQUEST_CODE_CHANG);
    }

    public View.OnClickListener updateClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!department.getUpd().equals("是")){
                    ToastUtil.show(OrderActivity.this, "无权限！");
                    return;
                }
                int position = Integer.parseInt(view.getTag().toString());
                Intent intent = new Intent(OrderActivity.this, OrderChangeActivity.class);
                intent.putExtra("type", R.id.update_btn);
                MyApplication myApplication = (MyApplication) getApplication();
                myApplication.setObj(list.get(position));
                startActivityForResult(intent, REQUEST_CODE_CHANG);
            }
        };
    }

    public View.OnLongClickListener onItemLongClick() {
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(!department.getDel().equals("是")){
                    ToastUtil.show(OrderActivity.this, "无权限！");
                    return true;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this);
                int position = Integer.parseInt(view.getTag().toString());
                Handler deleteHandler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message msg) {
                        if ((boolean) msg.obj) {
                            ToastUtil.show(OrderActivity.this, "删除成功");
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

                        xiangQingYe.setA_title("订单号:");
                        xiangQingYe.setB_title("产品编码:");
                        xiangQingYe.setC_title("产品名称:");
                        xiangQingYe.setD_title("规格:");
                        xiangQingYe.setE_title("下单日期:");
                        xiangQingYe.setF_title("是否完成:");

                        xiangQingYe.setA(list.get(position).getCode());
                        xiangQingYe.setB(list.get(position).getProduct_name());
                        xiangQingYe.setC(list.get(position).getNorms());
                        xiangQingYe.setD(list.get(position).getSet_date());
                        xiangQingYe.setE(String.valueOf(list.get(position).getSet_num()));
                        xiangQingYe.setF(list.get(position).getIs_complete());

                        Intent intent = new Intent(OrderActivity.this, XiangQingYeActivity.class);
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
                                orderInfoService.deleteOrderBom(list.get(position).getId());
                                msg.obj = orderInfoService.delete(list.get(position).getId());
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
