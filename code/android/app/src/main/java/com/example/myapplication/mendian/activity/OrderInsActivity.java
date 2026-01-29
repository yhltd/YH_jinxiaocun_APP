package com.example.myapplication.mendian.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.mendian.entity.YhMendianMemberinfo;
import com.example.myapplication.mendian.entity.YhMendianOrderDetail;
import com.example.myapplication.mendian.entity.YhMendianOrders;
import com.example.myapplication.mendian.entity.YhMendianUser;
import com.example.myapplication.mendian.service.YhMendianMemberinfoService;
import com.example.myapplication.mendian.service.YhMendianOrdersDetailsService;
import com.example.myapplication.mendian.service.YhMendianOrdersService;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class OrderInsActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_CHANG = 1;

    MyApplication myApplication;

    private YhMendianUser yhMendianUser;
    private YhMendianMemberinfoService yhMendianMemberinfoService;
    private YhMendianOrdersService yhMendianOrdersService;
    private YhMendianOrdersDetailsService yhMendianOrdersDetailsService;
    private EditText this_where;
    private String this_whereText;
    private ListView huiyuan_list;
    private Button sel_button;
    private YhMendianOrders yhMendianOrders;
    private YhMendianOrderDetail yhMendianOrderDetail;
    List<YhMendianOrderDetail> order_detail_list;
    List<YhMendianMemberinfo> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        myApplication = (MyApplication) getApplication();
        yhMendianUser = myApplication.getYhMendianUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_ins);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        yhMendianMemberinfoService = new YhMendianMemberinfoService();
        yhMendianOrdersService = new YhMendianOrdersService();
        yhMendianOrdersDetailsService = new YhMendianOrdersDetailsService();

        //初始化控件
        this_where = findViewById(R.id.this_where);
        huiyuan_list = findViewById(R.id.huiyuan_list);
        sel_button = findViewById(R.id.sel_button);
        sel_button.setOnClickListener(selClick());
        sel_button.requestFocus();
        // 添加非会员结算按钮---0129
        Button nonMemberBtn = findViewById(R.id.non_member_btn);
        if (nonMemberBtn == null) {
            // 如果没有这个按钮，需要在布局中添加
            // 或者动态创建一个按钮
            nonMemberBtn = new Button(this);
            nonMemberBtn.setText("非会员结算");
            nonMemberBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    processNonMemberOrder();
                }
            });
            // 添加到布局中...
        } else {
            nonMemberBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    processNonMemberOrder();
                }
            });
        }//----------0129
        initList();
    }

    public View.OnClickListener selClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initList();
            }
        };
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
        this_whereText = this_where.getText().toString();

        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                huiyuan_list.setAdapter(StringUtils.cast(msg.obj));
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<HashMap<String, Object>> data = new ArrayList<>();
                try {
                    yhMendianMemberinfoService = new YhMendianMemberinfoService();
                    list = yhMendianMemberinfoService.getListThree(yhMendianUser.getCompany(),this_whereText);
                    if (list == null) return;

                } catch (Exception e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < list.size(); i++) {
                    HashMap<String, Object> item = new HashMap<>();
                    item.put("username", list.get(i).getUsername());
                    item.put("name", list.get(i).getName());
                    item.put("gender", list.get(i).getGender());
                    item.put("phone", list.get(i).getPhone());
                    item.put("state", list.get(i).getState());
                    data.add(item);
                }

                SimpleAdapter adapter = new SimpleAdapter(OrderInsActivity.this, data, R.layout.order_ins_row, new String[]{"username","name","gender","phone","state"}, new int[]{R.id.username,R.id.name,R.id.gender,R.id.phone,R.id.state}) {
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

    // 非会员结算方法------0129
    private void processNonMemberOrder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(OrderInsActivity.this);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            MyApplication myApplication = (MyApplication) getApplication();
                            order_detail_list = myApplication.getOrderDetails();
                            yhMendianOrders = new YhMendianOrders();

                            // 设置日期
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            String format = sdf.format(new Date());
                            yhMendianOrders.setRiqi(format);

                            // 设置订单号
                            yhMendianOrders.setDdh(order_detail_list.get(0).getDdid());

                            // 非会员结算 - 会员信息留空
                            yhMendianOrders.setHyzh("");        // 会员号为空
                            yhMendianOrders.setHyxm("");        // 会员姓名为空
                            yhMendianOrders.setYhfa("");        // 优惠方案为空

                            // 设置其他信息
                            yhMendianOrders.setSyy(yhMendianUser.getUname());
                            yhMendianOrders.setCompany(yhMendianUser.getCompany());

                            // 计算金额（需要根据实际情况实现）
                            double totalAmount = calculateTotalAmount(order_detail_list);
                            yhMendianOrders.setXfje(String.valueOf(totalAmount));   // 消费金额
                            yhMendianOrders.setSsje(String.valueOf(totalAmount));   // 实收金额（非会员无优惠）
                            yhMendianOrders.setYhje("0");                          // 优惠金额为0

                            // 保存订单
                            boolean orderResult = yhMendianOrdersService.insertByOrders(yhMendianOrders);

                            // 保存订单详情
                            if (orderResult && order_detail_list != null) {
                                for (int i = 0; i < order_detail_list.size(); i++) {
                                    YhMendianOrderDetail detail = order_detail_list.get(i);
                                    detail.setCompany(yhMendianUser.getCompany());
                                    detail.setDdid(yhMendianOrders.getDdh());
                                    yhMendianOrdersDetailsService.insert(detail);
                                }
                            }

                            // 回到主线程显示结果
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (orderResult) {
                                        ToastUtil.show(OrderInsActivity.this, "非会员结算成功");
                                        back();
                                    } else {
                                        ToastUtil.show(OrderInsActivity.this, "结算失败");
                                    }
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.show(OrderInsActivity.this, "结算出错: " + e.getMessage());
                                }
                            });
                        }
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

        builder.setMessage("确定进行非会员结算？");
        builder.setTitle("提示");
        builder.show();
    }
    //-----------0129
//----------0129
    // 计算总金额的方法
    private double calculateTotalAmount(List<YhMendianOrderDetail> orderList) {
        double total = 0.0;
        if (orderList != null) {
            for (YhMendianOrderDetail detail : orderList) {
                try {
                    double price = Double.parseDouble(detail.getDj());
                    double quantity = Double.parseDouble(detail.getGs());
                    total += price * quantity;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return total;
    }
    public View.OnClickListener updateClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(OrderInsActivity.this);
                int position = Integer.parseInt(view.getTag().toString());
                Handler deleteHandler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message msg) {
                        if ((boolean) msg.obj) {
                            ToastUtil.show(OrderInsActivity.this, "添加成功");
                            back();
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
                                MyApplication myApplication = (MyApplication) getApplication();
                                order_detail_list = myApplication.getOrderDetails();
                                yhMendianOrders = new YhMendianOrders();

                                // 替换 LocalDate 为 SimpleDateFormat
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                String format = sdf.format(new Date());
                                yhMendianOrders.setRiqi(format);

                                yhMendianOrders.setDdh(order_detail_list.get(0).getDdid());
                                yhMendianOrders.setHyzh(list.get(position).getUsername());
                                yhMendianOrders.setHyxm(list.get(position).getName());
                                yhMendianOrders.setYhfa("1");
                                yhMendianOrders.setSyy(yhMendianUser.getUname());
                                yhMendianOrders.setCompany(yhMendianUser.getCompany());
                                yhMendianOrders.setXfje("");
                                yhMendianOrders.setSsje("");
                                yhMendianOrders.setYhje("");
                                yhMendianOrdersService.insertByOrders(yhMendianOrders);

                                for(int i=0; i<order_detail_list.size(); i++){
                                    yhMendianOrdersDetailsService.insert(order_detail_list.get(i));
                                }
                                msg.obj = true;
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

                builder.setMessage("确定添加订单？");
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

    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }

}
