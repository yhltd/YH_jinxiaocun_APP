package com.example.myapplication.jxc.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.LoginActivity;
import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.jxc.entity.YhJinXiaoCunJiChuZiLiao;
import com.example.myapplication.jxc.entity.YhJinXiaoCunQiChuShu;
import com.example.myapplication.jxc.entity.YhJinXiaoCunUser;
import com.example.myapplication.jxc.service.YhJinXiaoCunJiChuZiLiaoService;
import com.example.myapplication.jxc.service.YhJinXiaoCunQiChuShuService;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.util.List;

public class QiChuChangeActivity extends AppCompatActivity {
    private YhJinXiaoCunUser yhJinXiaoCunUser;
    private YhJinXiaoCunQiChuShu yhJinXiaoCunQiChuShu;
    private YhJinXiaoCunQiChuShuService yhJinXiaoCunQiChuShuService;
    private YhJinXiaoCunJiChuZiLiaoService yhJinXiaoCunJiChuZiLiaoService;

    private EditText cpname;
    private Spinner cpid;
    private EditText cplb;
    private EditText cpsj;
    private EditText cpsl;

    private List<String> cpid_array;

    public QiChuChangeActivity() {
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qichu_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        yhJinXiaoCunUser = myApplication.getYhJinXiaoCunUser();
        yhJinXiaoCunQiChuShuService = new YhJinXiaoCunQiChuShuService();
        yhJinXiaoCunJiChuZiLiaoService = new YhJinXiaoCunJiChuZiLiaoService();

        cpname = findViewById(R.id.cpname);
        cpid = findViewById(R.id.cpid);
        cplb = findViewById(R.id.cplb);
        cpsj = findViewById(R.id.cpsj);
        cpsl = findViewById(R.id.cpsl);

        init();

        Intent intent = getIntent();
        int id = intent.getIntExtra("type", 0);
        if (id == R.id.insert_btn) {
            yhJinXiaoCunQiChuShu = new YhJinXiaoCunQiChuShu();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
        } else if (id == R.id.update_btn) {
            yhJinXiaoCunQiChuShu = (YhJinXiaoCunQiChuShu) myApplication.getObj();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);

            cpname.setText(yhJinXiaoCunQiChuShu.getCpname());
            cpid.setSelection(getPosition(yhJinXiaoCunQiChuShu.getCpid()));
            cplb.setText(yhJinXiaoCunQiChuShu.getCplb());
            cpsj.setText(yhJinXiaoCunQiChuShu.getCpsj());
            cpsl.setText(yhJinXiaoCunQiChuShu.getCpsl());
        }
        cpid.setOnItemSelectedListener(new cpidItemSelectedListener());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                cpid.setAdapter(StringUtils.cast(msg.obj));
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                SpinnerAdapter adapter = null;
                try {
                    cpid_array = yhJinXiaoCunJiChuZiLiaoService.getCpid(yhJinXiaoCunUser.getGongsi());
                    if (cpid_array == null) return;

                    adapter = new ArrayAdapter<String>(QiChuChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, cpid_array);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.obj = adapter;
                listLoadHandler.sendMessage(msg);
            }
        }).start();
    }

    public void insertClick(View v) {
        if (!checkForm()) return;

        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(QiChuChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(QiChuChangeActivity.this, "保存失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = yhJinXiaoCunQiChuShuService.insert(yhJinXiaoCunQiChuShu);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    public void updateClick(View v) {
        if (!checkForm()) return;

        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(QiChuChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(QiChuChangeActivity.this, "保存失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = yhJinXiaoCunQiChuShuService.update(yhJinXiaoCunQiChuShu);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    private boolean checkForm() {
        if (cpname.getText().toString().equals("")) {
            ToastUtil.show(QiChuChangeActivity.this, "请输入商品名称");
            return false;
        } else {
            yhJinXiaoCunQiChuShu.setCpname(cpname.getText().toString());
        }

        if (cpsj.getText().toString().equals("")) {
            ToastUtil.show(QiChuChangeActivity.this, "请输入商品售价");
            return false;
        } else {
            yhJinXiaoCunQiChuShu.setCpsj(cpsj.getText().toString());
        }

        if (cpsl.getText().toString().equals("")) {
            ToastUtil.show(QiChuChangeActivity.this, "请输入商品数量");
            return false;
        } else {
            yhJinXiaoCunQiChuShu.setCpsl(cpsl.getText().toString());
        }

        yhJinXiaoCunQiChuShu.setCpid(cpid.getSelectedItem().toString());
        yhJinXiaoCunQiChuShu.setCplb(cplb.getText().toString());
        yhJinXiaoCunQiChuShu.setGs_name(yhJinXiaoCunUser.getGongsi());


        return true;
    }

    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }

    private int getPosition(String cpid) {
        for (int i = 0; i < cpid_array.size(); i++) {
            if (cpid.equals(cpid_array.get(i))) {
                return i;
            }
        }
        return 0;
    }

    private class cpidItemSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Handler systemHandler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(@NonNull Message msg) {
                    return true;
                }
            });

            new Thread(new Runnable() {
                @Override
                public void run() {
                    //获取选择的项的值
                    String text = cpid.getItemAtPosition(position).toString();
                    try {
                        yhJinXiaoCunJiChuZiLiaoService = new YhJinXiaoCunJiChuZiLiaoService();
                        List<YhJinXiaoCunJiChuZiLiao> getList = yhJinXiaoCunJiChuZiLiaoService.getListByCpid(yhJinXiaoCunUser.getGongsi(), text);
                        if (getList.size() != 0) {
                            cpname.setText(getList.get(0).getName());
                            cplb.setText(getList.get(0).getLeiBie());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}
