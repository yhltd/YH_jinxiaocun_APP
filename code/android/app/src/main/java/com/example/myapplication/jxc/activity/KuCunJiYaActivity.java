package com.example.myapplication.jxc.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.XiangQingYeActivity;
import com.example.myapplication.entity.XiangQingYe;
import com.example.myapplication.jxc.entity.YhJinXiaoCun;
import com.example.myapplication.jxc.entity.YhJinXiaoCunMingXi;
import com.example.myapplication.jxc.entity.YhJinXiaoCunUser;
import com.example.myapplication.jxc.service.YhJinXiaoCunMingXiService;
import com.example.myapplication.jxc.service.YhJinXiaoCunService;
import com.example.myapplication.utils.ExcelUtil;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class KuCunJiYaActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_CHANG = 1;

    //订单二维码
    private static final int ORDER_CODE_SCAN = 102;

    private YhJinXiaoCunUser yhJinXiaoCunUser;
    private YhJinXiaoCunService yhJinXiaoCunService;
    private YhJinXiaoCunMingXiService yhJinXiaoCunMingXiService;
    private List<YhJinXiaoCunMingXi> order_list;

    private String where_sql;
    private EditText cangku_text;
    private EditText ks;
    private EditText js;
    private String start_dateText;
    private String stop_dateText;
    private ListView listView;
    private ListView listView_block;
    private HorizontalScrollView list_table;
    private SimpleAdapter adapter;
    private SimpleAdapter adapter_block;
    private Button sel_button;
    private Button export_button;

    List<YhJinXiaoCun> list;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kucunjiya);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        cangku_text = findViewById(R.id.cangku_text);
        listView = findViewById(R.id.jinxiaocun_list);
        listView_block = findViewById(R.id.list_block);
        list_table = findViewById(R.id.list_table);
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

    @SuppressLint("WrongConstant")
    public void switchClick(View v) {
        if(listView_block.getVisibility() == 0){
            listView_block.setVisibility(8);
            list_table.setVisibility(0);
        }else if(listView_block.getVisibility() == 8){
            listView_block.setVisibility(0);
            list_table.setVisibility(8);
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(KuCunJiYaActivity.this, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                editText.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }




    private void initList() {
        sel_button.setEnabled(false);
        start_dateText = ks.getText().toString();
        stop_dateText = js.getText().toString();
        if(start_dateText.equals("")){
            start_dateText = "1900/01/01";
        }
        if(stop_dateText.equals("")){
            stop_dateText = "2100/12/31";
        }


        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                listView.setAdapter(StringUtils.cast(adapter));
                listView_block.setAdapter(StringUtils.cast(adapter_block));
                sel_button.setEnabled(true);
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<HashMap<String, Object>> data = new ArrayList<>();
                try {
                    yhJinXiaoCunService = new YhJinXiaoCunService();
                    yhJinXiaoCunMingXiService = new YhJinXiaoCunMingXiService();
                    // 设置仓库默认值
                    String cangkuValue = cangku_text.getText().toString().trim().isEmpty() ? "99999" : cangku_text.getText().toString().trim();
                    list = yhJinXiaoCunService.kucunjiya(yhJinXiaoCunUser.getGongsi(), start_dateText, stop_dateText,cangku_text.getText().toString());
                    Log.e("SQLDebug", "=== 查询结果 ===");
                    Log.e("SQLDebug", "返回列表对象: " + (list != null ? list : "null"));
                    Log.e("SQLDebug", "列表大小: " + (list != null ? list.size() : 0));
//                    order_list = yhJinXiaoCunMingXiService.getList(yhJinXiaoCunUser.getGongsi());


                    if (list == null) return;

                    for (int i = 0; i < list.size(); i++) {
                        HashMap<String, Object> item = new HashMap<>();
                        item.put("spname", list.get(i).getName());
                        item.put("spdm", list.get(i).getSp_dm());
                        item.put("splb", list.get(i).getLei_bie());
                        item.put("cangku", list.get(i).getcangku());
                        item.put("mx_chuku_cpsl", list.get(i).getMx_chuku_cpsl());
                        item.put("jc_sl", list.get(i).getJc_sl());
                        item.put("jc_price", list.get(i).getJc_price());
//                        item.put("zhouzhuan", list.get(i).getzhouzhuan());
//                        item.put("yjdj", list.get(i).getyjdj());
                        data.add(item);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                adapter = new SimpleAdapter(KuCunJiYaActivity.this, data, R.layout.kucunjiya_row, new String[]{"spname", "spdm", "splb", "cangku", "mx_chuku_cpsl", "jc_sl", "jc_price"}, new int[]{R.id.spname, R.id.spdm, R.id.splb, R.id.cangku, R.id.mx_chuku_cpsl, R.id.jc_sl, R.id.jc_price}) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        final LinearLayout view = (LinearLayout) super.getView(position, convertView, parent);
                        LinearLayout linearLayout = (LinearLayout) view.getChildAt(0);
//                        linearLayout.setOnClickListener(updateClick());
                        linearLayout.setTag(position);
                        return view;
                    }
                };

                adapter_block = new SimpleAdapter(KuCunJiYaActivity.this, data, R.layout.kucunjiya_row_block, new String[]{"spname", "spdm", "splb", "cangku", "mx_chuku_cpsl", "jc_sl", "jc_price"}, new int[]{R.id.spname, R.id.spdm, R.id.splb, R.id.cangku, R.id.mx_chuku_cpsl, R.id.jc_sl, R.id.jc_price}) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        final LinearLayout view = (LinearLayout) super.getView(position, convertView, parent);
                        LinearLayout linearLayout = (LinearLayout) view.getChildAt(0);
//                        linearLayout.setOnClickListener(updateClick());
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

//    private void orderList() {
//        Handler listLoadHandler = new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(Message msg) {
//                listView.setAdapter(StringUtils.cast(adapter));
//                listView_block.setAdapter(StringUtils.cast(adapter_block));
//                return true;
//            }
//        });
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                List<HashMap<String, Object>> data = new ArrayList<>();
//                try {
//                    yhJinXiaoCunService = new YhJinXiaoCunService();
//                    list = yhJinXiaoCunService.orderQueryJxc(yhJinXiaoCunUser.getGongsi(), where_sql);
//                    if (list == null) return;
//
//                    for (int i = 0; i < list.size(); i++) {
//                        HashMap<String, Object> item = new HashMap<>();
//                        item.put("cangku", list.get(i).getcangku());
//                        item.put("month", list.get(i).getmonth());
//                        item.put("mx_ruku_cpsl", list.get(i).getMx_ruku_cpsl());
//                        item.put("mx_ruku_price", list.get(i).getMx_ruku_price());
//                        item.put("mx_chuku_cpsl", list.get(i).getMx_chuku_cpsl());
//                        item.put("mx_chuku_price", list.get(i).getMx_chuku_price());
//                        item.put("jc_sl", list.get(i).getJc_sl());
//                        item.put("jc_price", list.get(i).getJc_price());
//                        data.add(item);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                adapter = new SimpleAdapter(QiMoTongJiActivity.this, data, R.layout.jinxiaocun_row, new String[]{"cangku", "month",  "mx_ruku_cpsl", "mx_ruku_price", "mx_chuku_cpsl", "mx_chuku_price", "jc_sl", "jc_price"}, new int[]{R.id.cangku, R.id.name, R.id.leiBie, R.id.jq_cpsl, R.id.jq_price, R.id.mx_ruku_cpsl, R.id.mx_ruku_price, R.id.mx_chuku_cpsl, R.id.mx_chuku_price, R.id.jc_sl, R.id.jc_price,R.id.cangku,R.id.zzl}) {
//                    @Override
//                    public View getView(int position, View convertView, ViewGroup parent) {
//                        final LinearLayout view = (LinearLayout) super.getView(position, convertView, parent);
//                        LinearLayout linearLayout = (LinearLayout) view.getChildAt(0);
////                        linearLayout.setOnClickListener(updateClick());
//                        linearLayout.setTag(position);
//                        return view;
//                    }
//                };
//
//                adapter_block = new SimpleAdapter(QiMoTongJiActivity.this, data, R.layout.jinxiaocun_row_block, new String[]{"spDm", "name", "leiBie", "jq_cpsl", "jq_price", "mx_ruku_cpsl", "mx_ruku_price", "mx_chuku_cpsl", "mx_chuku_price", "jc_sl", "jc_price","cangku","zzl"}, new int[]{R.id.spDm, R.id.name, R.id.leiBie, R.id.jq_cpsl, R.id.jq_price, R.id.mx_ruku_cpsl, R.id.mx_ruku_price, R.id.mx_chuku_cpsl, R.id.mx_chuku_price, R.id.jc_sl, R.id.jc_price,R.id.cangku,R.id.zzl}) {
//                    @Override
//                    public View getView(int position, View convertView, ViewGroup parent) {
//                        final LinearLayout view = (LinearLayout) super.getView(position, convertView, parent);
//                        LinearLayout linearLayout = (LinearLayout) view.getChildAt(0);
////                        linearLayout.setOnClickListener(updateClick());
//                        linearLayout.setTag(position);
//                        return view;
//                    }
//                };
//
//                Message msg = new Message();
//                msg.obj = adapter;
//                listLoadHandler.sendMessage(msg);
//            }
//        }).start();
//    }

//    public View.OnClickListener updateClick() {
//        return new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(QiMoTongJiActivity.this);
//                int position = Integer.parseInt(view.getTag().toString());
//
//                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        XiangQingYe xiangQingYe = new XiangQingYe();
//
//                        xiangQingYe.setA_title("商品代码:");
//                        xiangQingYe.setB_title("商品名称:");
//                        xiangQingYe.setC_title("商品类别:");
//                        xiangQingYe.setD_title("期初数量:");
//                        xiangQingYe.setE_title("期初金额:");
//                        xiangQingYe.setF_title("入库数量:");
//                        xiangQingYe.setG_title("入库金额:");
//                        xiangQingYe.setH_title("出库数量:");
//                        xiangQingYe.setI_title("出库金额:");
//                        xiangQingYe.setJ_title("结存数量:");
//                        xiangQingYe.setK_title("结存金额:");
//                        xiangQingYe.setL_title("所属仓库:");
//                        xiangQingYe.setM_title("周转率:");
//
//                        xiangQingYe.setA(list.get(position).getSp_dm());
//                        xiangQingYe.setB(list.get(position).getName());
//                        xiangQingYe.setC(list.get(position).getLei_bie());
//                        xiangQingYe.setD(list.get(position).getJq_cpsl());
//                        xiangQingYe.setE(list.get(position).getJq_price());
//                        xiangQingYe.setF(list.get(position).getMx_ruku_cpsl());
//                        xiangQingYe.setG(list.get(position).getMx_ruku_price());
//                        xiangQingYe.setH(list.get(position).getMx_chuku_cpsl());
//                        xiangQingYe.setI(list.get(position).getMx_chuku_price());
//                        xiangQingYe.setJ(list.get(position).getJc_sl());
//                        xiangQingYe.setK(list.get(position).getJc_price());
//                        xiangQingYe.setL(list.get(position).getcangku());
//                        xiangQingYe.setM(list.get(position).getzzl());
//                        Intent intent = new Intent(QiMoTongJiActivity.this, XiangQingYeActivity.class);
//                        MyApplication myApplication = (MyApplication) getApplication();
//                        myApplication.setObj(xiangQingYe);
//                        startActivityForResult(intent, REQUEST_CODE_CHANG);
//                    }
//                });
//
//                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//                builder.setMessage("确定查看明细？");
//                builder.setTitle("提示");
//                builder.show();
//            }
//        };
//    }

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
                String[] title = { "商品名称", "商品代码",  "商品类别", "所属仓库", "出库数量","库存数量","库存金额"};
                String fileName = "积压统计" + System.currentTimeMillis() + ".xls";
                ExcelUtil.initExcel(fileName, "积压统计", title);
                ExcelUtil.jiyatongjiToExcel(list, fileName, MyApplication.getContext());
                String filePath = null;
                try {
                    filePath = Environment.getExternalStorageDirectory().getCanonicalPath()+"/云合未来一体化系统/" + fileName;
                    Uri uri = Uri.parse("file://" + filePath);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(uri);
                    startActivity(intent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
