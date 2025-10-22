package com.example.myapplication.mendian.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
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
import com.example.myapplication.mendian.entity.YhMendianMemberinfo;
import com.example.myapplication.mendian.entity.YhMendianUser;
import com.example.myapplication.mendian.service.YhMendianKehuService;
import com.example.myapplication.mendian.service.YhMendianMemberinfoService;
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

public class MemberinfoActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_CHANG = 1;

    MyApplication myApplication;

    private YhMendianUser yhMendianUser;
    private YhMendianMemberinfo yhMendianMemberinfo;
    private YhMendianMemberinfoService yhMendianMemberinfoService;
    private EditText name;
    private EditText phone;
    private String nameText;
    private String phoneText;
    private ListView huiyuan_list;
    private Button sel_button;

    List<YhMendianMemberinfo> list;

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
        setContentView(R.layout.memberinfo);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        yhMendianMemberinfoService = new YhMendianMemberinfoService();

        //初始化控件
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        huiyuan_list = findViewById(R.id.huiyuan_list);
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
        nameText = name.getText().toString();
        phoneText = phone.getText().toString();

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
                    list = yhMendianMemberinfoService.getList(nameText,phoneText,yhMendianUser.getCompany());
                    if (list == null) return;

                } catch (Exception e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < list.size(); i++) {
                    HashMap<String, Object> item = new HashMap<>();
                    item.put("username", list.get(i).getUsername());
                    item.put("password", list.get(i).getPassword());
                    item.put("name", list.get(i).getName());
                    item.put("gender", list.get(i).getGender());
                    item.put("state", list.get(i).getState());
                    item.put("phone", list.get(i).getPhone());
                    item.put("birthday", list.get(i).getBirthday());
                    item.put("points", list.get(i).getPoints());

                    data.add(item);
                }

                SimpleAdapter adapter = new SimpleAdapter(MemberinfoActivity.this, data, R.layout.memberinfo_row, new String[]{"username","password","name","gender","state","phone","birthday","points"}, new int[]{R.id.username,R.id.password,R.id.name,R.id.gender,R.id.state,R.id.phone,R.id.birthday,R.id.points}) {
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
                AlertDialog.Builder builder = new AlertDialog.Builder(MemberinfoActivity.this);
                int position = Integer.parseInt(view.getTag().toString());
                Handler deleteHandler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message msg) {
                        if ((boolean) msg.obj) {
                            ToastUtil.show(MemberinfoActivity.this, "删除成功");
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
                                msg.obj = yhMendianMemberinfoService.deleteByMember(list.get(position).getId());
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
                Intent intent = new Intent(MemberinfoActivity.this, MemberinfoChangeActivity.class);
                intent.putExtra("type", R.id.update_btn);
                MyApplication myApplication = (MyApplication) getApplication();
                myApplication.setObj(list.get(position));
                startActivityForResult(intent, REQUEST_CODE_CHANG);
            }
        };
    }

    public void onInsertClick(View v) {
        Intent intent = new Intent(MemberinfoActivity.this, MemberinfoChangeActivity.class);
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
