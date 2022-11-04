package com.example.myapplication.jxc.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.jxc.entity.YhJinXiaoCun;
import com.example.myapplication.jxc.entity.YhJinXiaoCunUser;
import com.example.myapplication.jxc.service.YhJinXiaoCunService;
import com.example.myapplication.utils.ExcelUtil;
import com.example.myapplication.utils.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class JinXiaoCunActivity extends AppCompatActivity {
    private YhJinXiaoCunUser yhJinXiaoCunUser;
    private YhJinXiaoCunService yhJinXiaoCunService;

    private EditText spDm_text;
    private EditText ks;
    private EditText js;
    private ListView listView;
    private Button sel_button;
    private Button export_button;

    List<YhJinXiaoCun> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jinxiaocun);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        spDm_text = findViewById(R.id.spDm_text);
        listView = findViewById(R.id.jinxiaocun_list);
        sel_button = findViewById(R.id.sel_button);
        export_button=findViewById(R.id.export);
        ks = findViewById(R.id.ks);
        js = findViewById(R.id.js);

        MyApplication myApplication = (MyApplication) getApplication();
        yhJinXiaoCunUser = myApplication.getYhJinXiaoCunUser();

        initList();
        sel_button.setOnClickListener(selClick());
        export_button.setOnClickListener(exportClick());

        sel_button.requestFocus();

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
        DatePickerDialog datePickerDialog = new DatePickerDialog(JinXiaoCunActivity.this, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                editText.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void initList() {
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                listView.setAdapter(StringUtils.cast(msg.obj));
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<HashMap<String, Object>> data = new ArrayList<>();
                try {
                    yhJinXiaoCunService = new YhJinXiaoCunService();
                    if (spDm_text.getText().toString().equals("") && ks.getText().toString().equals("") && js.getText().toString().equals("")) {
                        list = yhJinXiaoCunService.getJxc(yhJinXiaoCunUser.getGongsi());
                    } else {
                        list = yhJinXiaoCunService.queryJxc(yhJinXiaoCunUser.getGongsi(), ks.getText().toString(), js.getText().toString(), spDm_text.getText().toString());
                    }
                    if (list == null) return;

                    for (int i = 0; i < list.size(); i++) {
                        HashMap<String, Object> item = new HashMap<>();
                        item.put("spDm", list.get(i).getSp_dm());
                        item.put("name", list.get(i).getName());
                        item.put("leiBie", list.get(i).getLei_bie());
                        item.put("jq_cpsl", list.get(i).getJq_cpsl());
                        item.put("jq_price", list.get(i).getJq_price());
                        item.put("mx_ruku_cpsl", list.get(i).getMx_ruku_cpsl());
                        item.put("mx_ruku_price", list.get(i).getMx_ruku_price());
                        item.put("mx_chuku_cpsl", list.get(i).getMx_chuku_cpsl());
                        item.put("mx_chuku_price", list.get(i).getMx_chuku_price());
                        item.put("jc_sl", list.get(i).getJc_sl());
                        item.put("jc_price", list.get(i).getJc_price());

                        data.add(item);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                SimpleAdapter adapter = new SimpleAdapter(JinXiaoCunActivity.this, data, R.layout.jinxiaocun_row, new String[]{"spDm", "name", "leiBie", "jq_cpsl", "jq_price", "mx_ruku_cpsl", "mx_ruku_price", "mx_chuku_cpsl", "mx_chuku_price", "jc_sl", "jc_price"}, new int[]{R.id.spDm, R.id.name, R.id.leiBie, R.id.jq_cpsl, R.id.jq_price, R.id.mx_ruku_cpsl, R.id.mx_ruku_price, R.id.mx_chuku_cpsl, R.id.mx_chuku_price, R.id.jc_sl, R.id.jc_price}) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        final LinearLayout view = (LinearLayout) super.getView(position, convertView, parent);
                        LinearLayout linearLayout = (LinearLayout) view.getChildAt(0);
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

    public View.OnClickListener exportClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] title = { "商品代码", "商品名称", "商品类别", "期初数量", "期初金额", "入库数量", "入库金额", "出库数量","出库金额","结存数量","结存金额"};
                String fileName = "进销存" + System.currentTimeMillis() + ".xls";
                ExcelUtil.initExcel(fileName, "进销存", title);
                ExcelUtil.jinXiaoCunToExcel(list, fileName, MyApplication.getContext());
            }
        };
    }
}
