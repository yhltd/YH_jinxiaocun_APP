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
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class BaoPanActivity extends AppCompatActivity {

    private final static int REQUEST_CODE_CHANG = 1;

    private YhRenShiUser yhRenShiUser;
    private YhRenShiGongZiMingXiService yhRenShiGongZiMingXiService;
    private ListView listView;
    private EditText start_date;
    private EditText stop_date;
    private String start_dateText;
    private String stop_dateText;
    private Button sel_button;

    private String shifa_gongzi;
    private String geren_zhichu;
    private String qiye_zhichu;
    private String yuangong_renshu;
    private String quanqin_tianshu;

    List<YhRenShiGongZiMingXi> list;
    List<YhRenShiGongZiMingXi> end_list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baopan);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //初始化控件
        listView = findViewById(R.id.baopan_list);
        start_date = findViewById(R.id.start_date);
        stop_date = findViewById(R.id.stop_date);
        showDateOnClick(start_date);
        showDateOnClick(stop_date);

        sel_button = findViewById(R.id.sel_button);
        sel_button.setOnClickListener(selClick());
        sel_button.requestFocus();

        MyApplication myApplication = (MyApplication) getApplication();
        yhRenShiUser = myApplication.getYhRenShiUser();

        Button shenpijilu = findViewById(R.id.shenpijilu);
        shenpijilu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BaoPanActivity.this, BaoPanShenPiActivity.class);
                startActivity(intent);
            }
        });

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
        start_dateText = start_date.getText().toString();
        stop_dateText = stop_date.getText().toString();
        if(start_dateText.equals("")){
            start_dateText = "1900-01-01";
        }
        if(stop_dateText.equals("")){
            stop_dateText = "2100-12-31";
        }

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
                    shifa_gongzi = "0";
                    geren_zhichu = "0";
                    qiye_zhichu = "0";
                    yuangong_renshu = "0";
                    quanqin_tianshu = "0";
                    yhRenShiGongZiMingXiService = new YhRenShiGongZiMingXiService();
                    list = yhRenShiGongZiMingXiService.baopanQueryList(yhRenShiUser.getL(),start_dateText,stop_dateText);
                    if (list == null){
                        end_list = null;
                        return;
                    }
                    end_list = new ArrayList<>();
                    yuangong_renshu = String.valueOf(list.size());
                    for(int i=0; i< list.size(); i++){
                        double qiye = 0;
                        double geren = 0;
                        YhRenShiGongZiMingXi this_item = new YhRenShiGongZiMingXi();
                        this_item.setB(list.get(i).getB());//姓名
                        this_item.setC(list.get(i).getBc());//录入日期
                        this_item.setD(list.get(i).getAy());//实发工资
                        this_item.setE(list.get(i).getM());//全勤天数

                        if (!list.get(i).getAj().equals("") && list.get(i).getAj() != null)
                        {
                            geren = geren + Double.parseDouble(list.get(i).getAj());
                        }
                        if (!list.get(i).getAk().equals("") && list.get(i).getAk() != null)
                        {
                            geren = geren + Double.parseDouble(list.get(i).getAk());
                        }
                        if (!list.get(i).getAl().equals("") && list.get(i).getAl() != null)
                        {
                            geren = geren + Double.parseDouble(list.get(i).getAl());
                        }
                        if (!list.get(i).getAm().equals("") && list.get(i).getAm() != null)
                        {
                            geren = geren + Double.parseDouble(list.get(i).getAm());
                        }
                        if (!list.get(i).getAn().equals("") && list.get(i).getAn() != null)
                        {
                            geren = geren + Double.parseDouble(list.get(i).getAn());
                        }
                        if (!list.get(i).getAo().equals("") && list.get(i).getAo() != null)
                        {
                            geren = geren + Double.parseDouble(list.get(i).getAo());
                        }



                        if (!list.get(i).getZ().equals("") && list.get(i).getZ() != null)
                        {
                            qiye = qiye + Double.parseDouble(list.get(i).getZ());
                        }
                        if (!list.get(i).getAa().equals("") && list.get(i).getAa() != null)
                        {
                            qiye = qiye + Double.parseDouble(list.get(i).getAa());
                        }
                        if (!list.get(i).getAb().equals("") && list.get(i).getAb() != null)
                        {
                            qiye = qiye + Double.parseDouble(list.get(i).getAb());
                        }
                        if (!list.get(i).getAc().equals("") && list.get(i).getAc() != null)
                        {
                            qiye = qiye + Double.parseDouble(list.get(i).getAc());
                        }
                        if (!list.get(i).getAd().equals("") && list.get(i).getAd() != null)
                        {
                            qiye = qiye + Double.parseDouble(list.get(i).getAd());
                        }
                        if (!list.get(i).getAe().equals("") && list.get(i).getAe() != null)
                        {
                            qiye = qiye + Double.parseDouble(list.get(i).getAe());
                        }
                        if (!list.get(i).getAf().equals("") && list.get(i).getAf() != null)
                        {
                            qiye = qiye + Double.parseDouble(list.get(i).getAf());
                        }

                        this_item.setF(String.valueOf(geren));//个人支出
                        this_item.setG(String.valueOf(qiye));//企业支出
                        String this_shifa = list.get(i).getAy();
                        if(this_shifa.equals("")){
                            this_shifa = "0";
                        }
                        shifa_gongzi = String.valueOf(Double.parseDouble(shifa_gongzi) + Double.parseDouble(this_shifa));
                        geren_zhichu = String.valueOf(Double.parseDouble(geren_zhichu) + geren);
                        qiye_zhichu = String.valueOf(Double.parseDouble(qiye_zhichu) + qiye);
                        String this_quanqin = list.get(i).getAy();
                        if(this_quanqin.equals("")){
                            this_quanqin = "0";
                        }
                        quanqin_tianshu = String.valueOf(Double.parseDouble(quanqin_tianshu) + Double.parseDouble(this_quanqin));

                        end_list.add(this_item);
                    }

                    for (int i = 0; i < end_list.size(); i++) {
                        HashMap<String, Object> item = new HashMap<>();

                        item.put("B", end_list.get(i).getB());
                        item.put("C", end_list.get(i).getC());
                        item.put("D", end_list.get(i).getD());
                        item.put("E", end_list.get(i).getE());
                        item.put("F", end_list.get(i).getF());
                        item.put("G", end_list.get(i).getG());
                        data.add(item);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                SimpleAdapter adapter = new SimpleAdapter(BaoPanActivity.this, data, R.layout.baopan_row, new String[]{"B","C","D","E","F","G"}, new int[]{R.id.B, R.id.C, R.id.D, R.id.E, R.id.F, R.id.G}) {
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

    public void onInsertClick(View v) {
        Intent intent = new Intent(BaoPanActivity.this, BaoPanChangeActivity.class);
        intent.putExtra("shifa_gongzi", shifa_gongzi);
        intent.putExtra("geren_zhichu", geren_zhichu);
        intent.putExtra("qiye_zhichu", qiye_zhichu);
        intent.putExtra("yuangong_renshu", yuangong_renshu);
        intent.putExtra("quanqin_tianshu", quanqin_tianshu);
        intent.putExtra("type", R.id.insert_btn);
        startActivityForResult(intent, REQUEST_CODE_CHANG);
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(BaoPanActivity.this, new DatePickerDialog.OnDateSetListener() {
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
