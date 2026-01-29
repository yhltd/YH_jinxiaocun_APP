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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.XiangQingYeActivity;
import com.example.myapplication.entity.XiangQingYe;
import com.example.myapplication.renshi.entity.YhRenShiGongZiMingXi;
import com.example.myapplication.renshi.entity.YhRenShiUser;
import com.example.myapplication.renshi.service.YhRenShiGongZiMingXiService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class BuMenHuiZongActivity extends AppCompatActivity {

    private final static int REQUEST_CODE_CHANG = 1;

    private YhRenShiUser yhRenShiUser;
    private YhRenShiGongZiMingXiService yhRenShiGongZiMingXiService;
    private ListView listView;

    private ListView listView_block;
    private HorizontalScrollView list_table;
    private SimpleAdapter adapter;
    private SimpleAdapter adapter_block;

    private Spinner bumen_select;
    private String bumen_selectText;
    private EditText start_date;
    private EditText stop_date;
    private String start_dateText;
    private String stop_dateText;
    private Button sel_button;
    List<YhRenShiGongZiMingXi> list;

    List<String> bumen_array;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bumenhuizong);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //初始化控件
        listView = findViewById(R.id.bumenhuizong_list);

        listView_block = findViewById(R.id.list_block);
        list_table = findViewById(R.id.list_table);

        bumen_select = findViewById(R.id.bumen_select);

        start_date = findViewById(R.id.start_date);
        stop_date = findViewById(R.id.stop_date);
        showDateOnClick(start_date);
        showDateOnClick(stop_date);

        sel_button = findViewById(R.id.sel_button);
        sel_button.setOnClickListener(selClick());
        sel_button.requestFocus();
        Button clear_button = findViewById(R.id.clear_button);
        clear_button.setOnClickListener(clearClick());
        MyApplication myApplication = (MyApplication) getApplication();
        yhRenShiUser = myApplication.getYhRenShiUser();
        init_select();
    }

    public View.OnClickListener selClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initList();
            }
        };
    }
    public View.OnClickListener clearClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_date.setText("");
                stop_date.setText("");
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

    public void init_select() {
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                SpinnerAdapter adapter = new ArrayAdapter<String>(BuMenHuiZongActivity.this, android.R.layout.simple_spinner_dropdown_item, bumen_array);
                bumen_select.setAdapter(StringUtils.cast(adapter));
                initList();
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                SpinnerAdapter adapter = null;
                try {
                    yhRenShiGongZiMingXiService = new YhRenShiGongZiMingXiService();
                    List<YhRenShiGongZiMingXi> bumenList = yhRenShiGongZiMingXiService.getBuMenList(yhRenShiUser.getL());
                    bumen_array = new ArrayList<>();
                    bumen_array.add("");
                    if (bumenList.size() > 0) {
                        for (int i = 0; i < bumenList.size(); i++) {
                            bumen_array.add(bumenList.get(i).getC());
                        }
                    }

                    adapter = new ArrayAdapter<String>(BuMenHuiZongActivity.this, android.R.layout.simple_spinner_dropdown_item, bumen_array);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.obj = adapter;
                listLoadHandler.sendMessage(msg);
            }
        }).start();
    }


    private void initList() {
        bumen_selectText = bumen_select.getSelectedItem().toString();
        start_dateText = start_date.getText().toString();
        stop_dateText = stop_date.getText().toString();
        if(start_dateText.equals("")){
            start_dateText = "1900-01-01";
        }
        if(stop_dateText.equals("")){
            stop_dateText = "2100-12-31";
        }

        if(start_dateText.compareTo(stop_dateText) > 0){
            ToastUtil.show(BuMenHuiZongActivity.this, "开始日期不能晚于结束日期");
            return;
        }

        sel_button.setEnabled(false);

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
                    yhRenShiGongZiMingXiService = new YhRenShiGongZiMingXiService();
                    list = yhRenShiGongZiMingXiService.bumenhuizongQueryList(yhRenShiUser.getL(),bumen_selectText,start_dateText,stop_dateText);
                    if (list == null) return;

                    for (int i = 0; i < list.size(); i++) {
                        HashMap<String, Object> item = new HashMap<>();
                        item.put("C", list.get(i).getC());
                        item.put("D", list.get(i).getD());
                        item.put("G", list.get(i).getG());
                        item.put("H", list.get(i).getH());
                        item.put("I", list.get(i).getI());
                        item.put("J", list.get(i).getJ());
                        item.put("K", list.get(i).getK());
                        item.put("L", list.get(i).getL());
                        item.put("M", list.get(i).getM());
                        item.put("N", list.get(i).getN());
                        item.put("O", list.get(i).getO());
                        item.put("P", list.get(i).getP());
                        item.put("Q", list.get(i).getQ());
                        item.put("R", list.get(i).getR());
                        item.put("S", list.get(i).getS());
                        item.put("T", list.get(i).getT());
                        item.put("U", list.get(i).getU());
                        item.put("V", list.get(i).getV());
                        item.put("W", list.get(i).getW());
                        item.put("X", list.get(i).getX());
                        item.put("Y", list.get(i).getY());
                        item.put("Z", list.get(i).getZ());
                        item.put("AA", list.get(i).getAa());
                        item.put("AB", list.get(i).getAb());
                        item.put("AC", list.get(i).getAc());
                        item.put("AD", list.get(i).getAd());
                        item.put("AE", list.get(i).getAe());
                        item.put("AF", list.get(i).getAf());
                        item.put("AG", list.get(i).getAg());
                        item.put("AH", list.get(i).getAh());
                        item.put("AI", list.get(i).getAi());
                        item.put("AJ", list.get(i).getAj());
                        item.put("AK", list.get(i).getAk());
                        item.put("AL", list.get(i).getAl());
                        item.put("AM", list.get(i).getAm());
                        item.put("AN", list.get(i).getAn());
                        item.put("AO", list.get(i).getAo());
                        item.put("AP", list.get(i).getAp());
                        item.put("AQ", list.get(i).getAq());
                        item.put("AR", list.get(i).getAr());
                        item.put("ASA", list.get(i).getAsa());
                        item.put("ATA", list.get(i).getAta());
                        item.put("AU", list.get(i).getAu());
                        item.put("AV", list.get(i).getAv());
                        item.put("AW", list.get(i).getAw());
                        item.put("AX", list.get(i).getAx());
                        item.put("AY", list.get(i).getAy());
                        data.add(item);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                adapter = new SimpleAdapter(BuMenHuiZongActivity.this, data, R.layout.bumenhuizong_row, new String[]{"C","D","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","AA","AB","AC","AD","AE","AF","AG","AH","AI","AJ","AK","AL","AM","AN","AO","AP","AQ","AR","ASA","ATA","AU","AV","AW","AX","AY"}, new int[]{R.id.C, R.id.D, R.id.G, R.id.H, R.id.I, R.id.J, R.id.K, R.id.L, R.id.M, R.id.N, R.id.O, R.id.P, R.id.Q, R.id.R, R.id.S, R.id.T, R.id.U, R.id.V, R.id.W, R.id.X, R.id.Y, R.id.Z, R.id.AA, R.id.AB, R.id.AC, R.id.AD, R.id.AE, R.id.AF, R.id.AG, R.id.AH, R.id.AI, R.id.AJ, R.id.AK, R.id.AL, R.id.AM, R.id.AN, R.id.AO, R.id.AP, R.id.AQ, R.id.AR, R.id.ASA, R.id.ATA, R.id.AU, R.id.AV, R.id.AW, R.id.AX, R.id.AY}) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        final LinearLayout view = (LinearLayout) super.getView(position, convertView, parent);
                        LinearLayout linearLayout = (LinearLayout) view.getChildAt(0);
                        linearLayout.setOnClickListener(updateClick());
                        linearLayout.setTag(position);
                        return view;
                    }
                };

                adapter_block = new SimpleAdapter(BuMenHuiZongActivity.this, data, R.layout.bumenhuizong_row_block, new String[]{"C","D","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","AA","AB","AC","AD","AE","AF","AG","AH","AI","AJ","AK","AL","AM","AN","AO","AP","AQ","AR","ASA","ATA","AU","AV","AW","AX","AY"}, new int[]{R.id.C, R.id.D, R.id.G, R.id.H, R.id.I, R.id.J, R.id.K, R.id.L, R.id.M, R.id.N, R.id.O, R.id.P, R.id.Q, R.id.R, R.id.S, R.id.T, R.id.U, R.id.V, R.id.W, R.id.X, R.id.Y, R.id.Z, R.id.AA, R.id.AB, R.id.AC, R.id.AD, R.id.AE, R.id.AF, R.id.AG, R.id.AH, R.id.AI, R.id.AJ, R.id.AK, R.id.AL, R.id.AM, R.id.AN, R.id.AO, R.id.AP, R.id.AQ, R.id.AR, R.id.ASA, R.id.ATA, R.id.AU, R.id.AV, R.id.AW, R.id.AX, R.id.AY}) {
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

    public View.OnClickListener updateClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BuMenHuiZongActivity.this);
                int position = Integer.parseInt(view.getTag().toString());

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        XiangQingYe xiangQingYe = new XiangQingYe();

                        xiangQingYe.setA_title("部门:");
                        xiangQingYe.setB_title("人数:");
                        xiangQingYe.setC_title("基本工资:");
                        xiangQingYe.setD_title("绩效工资:");
                        xiangQingYe.setE_title("岗位工资:");
                        xiangQingYe.setF_title("标准工资:");
                        xiangQingYe.setG_title("跨度工资:");
                        xiangQingYe.setH_title("职称津贴:");
                        xiangQingYe.setI_title("月出勤天数:");
                        xiangQingYe.setJ_title("加班时间:");
                        xiangQingYe.setK_title("加班费:");
                        xiangQingYe.setL_title("全勤应发:");
                        xiangQingYe.setM_title("缺勤天数:");
                        xiangQingYe.setN_title("缺勤扣款:");
                        xiangQingYe.setO_title("迟到天数:");
                        xiangQingYe.setP_title("迟到扣款:");
                        xiangQingYe.setQ_title("应发工资:");
                        xiangQingYe.setR_title("社保基数:");
                        xiangQingYe.setS_title("医疗基数:");
                        xiangQingYe.setT_title("公积金基数:");
                        xiangQingYe.setU_title("年金基数:");
                        xiangQingYe.setV_title("企业养老:");
                        xiangQingYe.setW_title("企业失业:");
                        xiangQingYe.setX_title("企业医疗:");
                        xiangQingYe.setY_title("企业工伤:");
                        xiangQingYe.setZ_title("企业生育:");
                        xiangQingYe.setAa_title("企业公积金:");
                        xiangQingYe.setAb_title("企业年金:");
                        xiangQingYe.setAc_title("滞纳金:");
                        xiangQingYe.setAd_title("利息:");
                        xiangQingYe.setAe_title("企业小计:");
                        xiangQingYe.setAf_title("个人养老:");
                        xiangQingYe.setAg_title("个人失业:");
                        xiangQingYe.setAh_title("个人医疗:");

                        xiangQingYe.setA(list.get(position).getC());
                        xiangQingYe.setB(list.get(position).getD());
                        xiangQingYe.setC(list.get(position).getG());
                        xiangQingYe.setD(list.get(position).getH());
                        xiangQingYe.setE(list.get(position).getI());
                        xiangQingYe.setF(list.get(position).getJ());
                        xiangQingYe.setG(list.get(position).getK());
                        xiangQingYe.setH(list.get(position).getL());
                        xiangQingYe.setI(list.get(position).getM());
                        xiangQingYe.setJ(list.get(position).getN());
                        xiangQingYe.setK(list.get(position).getO());
                        xiangQingYe.setL(list.get(position).getP());
                        xiangQingYe.setM(list.get(position).getQ());
                        xiangQingYe.setN(list.get(position).getR());
                        xiangQingYe.setO(list.get(position).getS());
                        xiangQingYe.setP(list.get(position).getT());
                        xiangQingYe.setQ(list.get(position).getU());
                        xiangQingYe.setR(list.get(position).getV());
                        xiangQingYe.setS(list.get(position).getW());
                        xiangQingYe.setT(list.get(position).getX());
                        xiangQingYe.setU(list.get(position).getY());
                        xiangQingYe.setV(list.get(position).getZ());
                        xiangQingYe.setW(list.get(position).getAa());
                        xiangQingYe.setX(list.get(position).getAb());
                        xiangQingYe.setY(list.get(position).getAc());
                        xiangQingYe.setZ(list.get(position).getAd());
                        xiangQingYe.setAa(list.get(position).getAe());
                        xiangQingYe.setAb(list.get(position).getAf());
                        xiangQingYe.setAc(list.get(position).getAg());
                        xiangQingYe.setAd(list.get(position).getAh());
                        xiangQingYe.setAe(list.get(position).getAi());
                        xiangQingYe.setAf(list.get(position).getAj());
                        xiangQingYe.setAg(list.get(position).getAk());
                        xiangQingYe.setAh(list.get(position).getAl());


                        Intent intent = new Intent(BuMenHuiZongActivity.this, XiangQingYeActivity.class);
                        MyApplication myApplication = (MyApplication) getApplication();
                        myApplication.setObj(xiangQingYe);
                        startActivityForResult(intent, REQUEST_CODE_CHANG);
                    }
                });

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setMessage("确定查看明细？");
                builder.setTitle("提示");
                builder.show();
            }
        };
    }

    public void onInsertClick(View v) {
        Intent intent = new Intent(BuMenHuiZongActivity.this, BuMenHuiZongItemActivity.class);
        bumen_selectText = bumen_select.getSelectedItem().toString();
        if(bumen_selectText.equals("")){
            ToastUtil.show(BuMenHuiZongActivity.this, "未选择部门");
        }else{
            intent.putExtra("type", bumen_selectText);
            startActivityForResult(intent, REQUEST_CODE_CHANG);
        }
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(BuMenHuiZongActivity.this, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String mon = "";
                String day = "";
                if(monthOfYear + 1 < 10){
                    mon = "0" + (monthOfYear + 1);
                }else{
                    mon = "" + (monthOfYear + 1);
                }
                if(dayOfMonth < 10){
                    day = "0" + dayOfMonth;
                }else{
                    day = "" + dayOfMonth;
                }
                editText.setText(year + "-" + mon + "-" + day);
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
