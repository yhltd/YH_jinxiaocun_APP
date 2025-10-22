package com.example.myapplication.mendian.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.example.myapplication.mendian.entity.YhMendianMemberinfo;
import com.example.myapplication.mendian.entity.YhMendianOrders;
import com.example.myapplication.mendian.entity.YhMendianUser;
import com.example.myapplication.mendian.service.YhMendianMemberinfoService;
import com.example.myapplication.mendian.service.YhMendianOrdersService;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class OrdersActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_CHANG = 1;

    MyApplication myApplication;

    private YhMendianUser yhMendianUser;
    private YhMendianOrders yhMendianOrders;
    private YhMendianOrdersService yhMendianOrdersService;
    private EditText ddh;
    private EditText syy;
    private EditText hyxm;
    private EditText startdate;
    private EditText enddate;
    private String ddhText;
    private String syyText;
    private String hyxmText;
    private String startdateText;
    private String enddateText;
    private ListView orders_list;
    private Button sel_button;

    List<YhMendianOrders> list;

    private boolean isExpanded = true;
    private LinearLayout seleOut;
    private Button toggleButton;
    private int originalHeight;
    private boolean isAnimating = false; // 防止重复点击

    @Override
    public void onCreate(Bundle savedInstanceState) {

        myApplication = (MyApplication) getApplication();
        yhMendianUser = myApplication.getYhMendianUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orders);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        yhMendianOrdersService = new YhMendianOrdersService();

        //初始化控件
        ddh = findViewById(R.id.ddh);
        syy = findViewById(R.id.syy);
        hyxm = findViewById(R.id.hyxm);
        startdate = findViewById(R.id.startdate);
        enddate = findViewById(R.id.enddate);
        showDateOnClick(startdate);
        showDateOnClick(enddate);
        orders_list = findViewById(R.id.orders_list);
        sel_button = findViewById(R.id.sel_button);
        sel_button.setOnClickListener(selClick());
        sel_button.requestFocus();
        initList();

        seleOut = findViewById(R.id.sele_out);
        toggleButton = findViewById(R.id.toggle_button);

        // 获取原始高度
        seleOut.post(new Runnable() {
            @Override
            public void run() {
                originalHeight = seleOut.getHeight();
                updateButtonText();
            }
        });
        // 或者通过代码设置点击监听
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAnimating) {
                    toggleContainer();
                }
            }
        });
    }

    private void toggleContainer() {
        if (isAnimating) return;

        isAnimating = true;
        int startHeight = seleOut.getHeight();
        int endHeight = isExpanded ? 0 : originalHeight;

        ValueAnimator animator = ValueAnimator.ofInt(startHeight, endHeight);
        animator.setDuration(300);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int val = (int) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = seleOut.getLayoutParams();
                layoutParams.height = val;
                seleOut.setLayoutParams(layoutParams);
            }
        });

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isAnimating = false;
                isExpanded = !isExpanded;
                updateButtonText();

                // 动画结束后，如果完全收起，可以设置visibility为GONE（可选）
                if (seleOut.getHeight() == 0) {
                    // seleOut.setVisibility(View.GONE);
                } else {
                    // seleOut.setVisibility(View.VISIBLE);
                }
            }
        });

        animator.start();

        // 立即更新按钮文本，提供即时反馈
        updateButtonText();
    }

    private void updateButtonText() {
        if (toggleButton != null) {
            toggleButton.setText(isExpanded ? "收起" : "展开");
        }
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
        ddhText = ddh.getText().toString();
        syyText = syy.getText().toString();
        hyxmText = hyxm.getText().toString();
        startdateText = startdate.getText().toString();
        enddateText = enddate.getText().toString();

        if(startdateText.equals("")){
            startdateText = "1900-01-01";
        }
        if(enddateText.equals("")){
            enddateText = "2100-12-31";
        }

        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                orders_list.setAdapter(StringUtils.cast(msg.obj));
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<HashMap<String, Object>> data = new ArrayList<>();
                try {
                    yhMendianOrdersService = new YhMendianOrdersService();
                    list = yhMendianOrdersService.getList(ddhText,syyText,hyxmText,startdateText,enddateText,yhMendianUser.getCompany());
                    if (list == null) return;

                } catch (Exception e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < list.size(); i++) {
                    HashMap<String, Object> item = new HashMap<>();
                    item.put("riqi", list.get(i).getRiqi());
                    item.put("ddh", list.get(i).getDdh());
                    item.put("hyzh", list.get(i).getHyzh());
                    item.put("hyxm", list.get(i).getHyxm());
                    item.put("yhfa", list.get(i).getYhfa());
                    item.put("xfje", list.get(i).getXfje());
                    item.put("ssje", list.get(i).getSsje());
                    item.put("yhje", list.get(i).getYhje());
                    item.put("syy", list.get(i).getSyy());

                    data.add(item);
                }

                SimpleAdapter adapter = new SimpleAdapter(OrdersActivity.this, data, R.layout.orders_row, new String[]{"riqi","ddh","hyzh","hyxm","yhfa","xfje","ssje","yhje","syy",}, new int[]{R.id.riqi,R.id.ddh,R.id.hyzh,R.id.hyxm,R.id.yhfa,R.id.xfje,R.id.ssje,R.id.yhje,R.id.syy}) {
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
                AlertDialog.Builder builder = new AlertDialog.Builder(OrdersActivity.this);
                int position = Integer.parseInt(view.getTag().toString());
                Handler deleteHandler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message msg) {
                        if ((boolean) msg.obj) {
                            ToastUtil.show(OrdersActivity.this, "删除成功");
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
                                msg.obj = yhMendianOrdersService.deleteByOrders(list.get(position).getId());
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

    public View.OnClickListener updateClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = Integer.parseInt(view.getTag().toString());
                Intent intent = new Intent(OrdersActivity.this, OrdersChangeActivity.class);
                intent.putExtra("type", R.id.update_btn);
                MyApplication myApplication = (MyApplication) getApplication();
                myApplication.setObj(list.get(position));
                startActivityForResult(intent, REQUEST_CODE_CHANG);
            }
        };
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(OrdersActivity.this, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String this_month = "";
                if (monthOfYear + 1 < 10){
                    this_month = "0" + String.format("%s",monthOfYear + 1);
                }else{
                    this_month = String.format("%s",monthOfYear + 1);
                }

                String this_day = "";
                if (dayOfMonth + 1 < 10){
                    this_day = "0" + String.format("%s",dayOfMonth);
                }else{
                    this_day = String.format("%s",dayOfMonth);
                }
                editText.setText(year + "-" + this_month + "-" + this_day);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
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
