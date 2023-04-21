package com.example.myapplication.renshi.activity;

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
import com.example.myapplication.renshi.entity.YhRenShiGongZiMingXi;
import com.example.myapplication.renshi.entity.YhRenShiUser;
import com.example.myapplication.renshi.service.YhRenShiGongZiMingXiService;
import com.example.myapplication.renshi.service.YhRenShiUserService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class RenYuanXinXiGuanLiActivity extends AppCompatActivity {

    private final static int REQUEST_CODE_CHANG = 1;

    private YhRenShiUser yhRenShiUser;
    private YhRenShiUserService yhRenShiUserService;
    private ListView listView;
    private EditText name;
    private EditText phone;
    private String nameText;
    private String phoneText;
    private Button sel_button;

    List<YhRenShiUser> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.renyuanxinxiguanli);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //初始化控件
        listView = findViewById(R.id.renyuanxinxiguanli_list);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);

        sel_button = findViewById(R.id.sel_button);
        sel_button.setOnClickListener(selClick());
        sel_button.requestFocus();

        MyApplication myApplication = (MyApplication) getApplication();
        yhRenShiUser = myApplication.getYhRenShiUser();

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
        LoadingDialog.getInstance(this).show();
        nameText = name.getText().toString();
        phoneText = phone.getText().toString();

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
                    yhRenShiUserService = new YhRenShiUserService();
                    list = yhRenShiUserService.getList(yhRenShiUser.getL(),nameText,phoneText);
                    if (list == null){
                        return;
                    }

                    for (int i = 0; i < list.size(); i++) {
                        HashMap<String, Object> item = new HashMap<>();
                        item.put("B", list.get(i).getB());
                        item.put("C", list.get(i).getC());
                        item.put("D", list.get(i).getD());
                        data.add(item);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                SimpleAdapter adapter = new SimpleAdapter(RenYuanXinXiGuanLiActivity.this, data, R.layout.renyuanxinxiguanli_row, new String[]{"B","C","D"}, new int[]{R.id.B, R.id.C, R.id.D}) {
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


    public void onInsertClick(View v) {
        Intent intent = new Intent(RenYuanXinXiGuanLiActivity.this, GongZiMingXiChangeActivity.class);
        intent.putExtra("type", R.id.insert_btn);
        startActivityForResult(intent, REQUEST_CODE_CHANG);
    }

    public View.OnClickListener updateClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = Integer.parseInt(view.getTag().toString());
                Intent intent = new Intent(RenYuanXinXiGuanLiActivity.this, GongZiMingXiChangeActivity.class);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(RenYuanXinXiGuanLiActivity.this);
                int position = Integer.parseInt(view.getTag().toString());
                Handler deleteHandler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message msg) {
                        if ((boolean) msg.obj) {
                            ToastUtil.show(RenYuanXinXiGuanLiActivity.this, "删除成功");
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
                                msg.obj = yhRenShiUserService.delete(list.get(position).getId());
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(RenYuanXinXiGuanLiActivity.this, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                editText.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
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
