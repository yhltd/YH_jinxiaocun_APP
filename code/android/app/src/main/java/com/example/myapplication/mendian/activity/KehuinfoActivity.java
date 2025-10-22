package com.example.myapplication.mendian.activity;

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
import com.example.myapplication.mendian.entity.YhMendianKeHu;
import com.example.myapplication.mendian.entity.YhMendianUser;
import com.example.myapplication.mendian.service.YhMendianKehuService;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.widget.LinearLayout;
import android.view.View;

import android.util.TypedValue;
public class KehuinfoActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_CHANG = 1;

    MyApplication myApplication;

    private YhMendianUser yhMendianUser;
    private YhMendianKehuService yhMedianKehuService;
    private EditText shouka;
    private EditText fukuan;
    private EditText chika;
    private String shoukaText;
    private String fukuanText;
    private String chikaText;
    private ListView kehu_list;
    private Button sel_button;

    List<YhMendianKeHu> list;
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
        setContentView(R.layout.kehuinfo);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        yhMedianKehuService = new YhMendianKehuService();

        //初始化控件
        shouka = findViewById(R.id.shouka);
        fukuan = findViewById(R.id.fukuan);
        chika = findViewById(R.id.chika);
        kehu_list = findViewById(R.id.kehu_list);
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
        shoukaText = shouka.getText().toString();
        fukuanText = fukuan.getText().toString();
        chikaText = chika.getText().toString();

        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                kehu_list.setAdapter(StringUtils.cast(msg.obj));
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<HashMap<String, Object>> data = new ArrayList<>();
                try {
                    yhMedianKehuService = new YhMendianKehuService();
                    list = yhMedianKehuService.getList(yhMendianUser.getCompany(),shoukaText,fukuanText,chikaText);
                    if (list == null) return;

                } catch (Exception e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < list.size(); i++) {
                    HashMap<String, Object> item = new HashMap<>();
                    item.put("recipient", list.get(i).getRecipient());
                    item.put("cardholder", list.get(i).getCardholder());
                    item.put("drawee", list.get(i).getDrawee());
                    item.put("issuing_bank", list.get(i).getIssuing_bank());
                    item.put("bill_day", list.get(i).getBill_day());
                    item.put("repayment_date", list.get(i).getRepayment_date());
                    item.put("total", list.get(i).getTotal());
                    item.put("repayable", list.get(i).getRepayable());
                    item.put("balance", list.get(i).getBalance());
                    item.put("loan", list.get(i).getLoan());
                    item.put("service_charge", list.get(i).getService_charge());
                    item.put("telephone", list.get(i).getTelephone());
                    item.put("password", list.get(i).getPassword());
                    item.put("staff", list.get(i).getStaff());
                    data.add(item);
                }

                SimpleAdapter adapter = new SimpleAdapter(KehuinfoActivity.this, data, R.layout.kehuinfo_row, new String[]{"recipient","cardholder","drawee","issuing_bank","bill_day","repayment_date","total","repayable","balance","loan","service_charge","telephone","password","staff"}, new int[]{R.id.recipient,R.id.cardholder,R.id.drawee,R.id.issuing_bank,R.id.bill_day,R.id.repayment_date,R.id.total,R.id.repayable,R.id.balance,R.id.loan,R.id.service_charge,R.id.telephone,R.id.password,R.id.staff}) {
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
                AlertDialog.Builder builder = new AlertDialog.Builder(KehuinfoActivity.this);
                int position = Integer.parseInt(view.getTag().toString());
                Handler deleteHandler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message msg) {
                        if ((boolean) msg.obj) {
                            ToastUtil.show(KehuinfoActivity.this, "删除成功");
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
                                msg.obj = yhMedianKehuService.deleteByKehu(list.get(position).getId());
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
                Intent intent = new Intent(KehuinfoActivity.this, KehuinfoChangeActivity.class);
                intent.putExtra("type", R.id.update_btn);
                MyApplication myApplication = (MyApplication) getApplication();
                myApplication.setObj(list.get(position));
                startActivityForResult(intent, REQUEST_CODE_CHANG);
            }
        };
    }

    public void onInsertClick(View v) {
        Intent intent = new Intent(KehuinfoActivity.this, KehuinfoChangeActivity.class);
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
