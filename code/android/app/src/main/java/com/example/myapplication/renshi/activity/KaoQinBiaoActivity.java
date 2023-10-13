package com.example.myapplication.renshi.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
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
import android.widget.HorizontalScrollView;
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
import com.example.myapplication.XiangQingYeActivity;
import com.example.myapplication.entity.XiangQingYe;
import com.example.myapplication.fenquan.activity.GongZuoTaiActivity;
import com.example.myapplication.renshi.entity.YhRenShiKaoQinJiLu;
import com.example.myapplication.renshi.entity.YhRenShiUser;
import com.example.myapplication.renshi.service.YhRenShiKaoQinJiLuService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KaoQinBiaoActivity extends AppCompatActivity {

    private final static int REQUEST_CODE_CHANG = 1;

    private YhRenShiUser yhRenShiUser;
    private YhRenShiKaoQinJiLuService yhRenShiKaoQinJiLuService;
    private ListView listView;

    private ListView listView_block;
    private HorizontalScrollView list_table;
    private SimpleAdapter adapter;
    private SimpleAdapter adapter_block;

    private EditText start_date;
    private EditText stop_date;
    private String start_dateText;
    private String stop_dateText;
    private Button sel_button;

    List<YhRenShiKaoQinJiLu> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kaoqinbiao);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        start_date = findViewById(R.id.start_date);
        stop_date = findViewById(R.id.stop_date);
        showDateOnClick(start_date);
        showDateOnClick(stop_date);
        sel_button = findViewById(R.id.sel_button);
        sel_button.setOnClickListener(selClick());
        sel_button.requestFocus();
        //初始化控件
        listView = findViewById(R.id.kaoqinbiao_list);

        listView_block = findViewById(R.id.list_block);
        list_table = findViewById(R.id.list_table);

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

    private void initList() {

        start_dateText = start_date.getText().toString();
        stop_dateText = stop_date.getText().toString();
        if(start_dateText.equals("")){
            start_dateText = "1900-01";
        }
        if(stop_dateText.equals("")){
            stop_dateText = "2100-12";
        }

        if(start_dateText.compareTo(stop_dateText) > 0){
            ToastUtil.show(KaoQinBiaoActivity.this, "开始日期不能晚于结束日期");
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
                    yhRenShiKaoQinJiLuService = new YhRenShiKaoQinJiLuService();
                    list = yhRenShiKaoQinJiLuService.queryList(yhRenShiUser.getL(),start_dateText,stop_dateText);
                    if (list == null) return;

                    for (int i = 0; i < list.size(); i++) {
                        HashMap<String, Object> item = new HashMap<>();
                        item.put("year", list.get(i).getYear());
                        item.put("moth", list.get(i).getMoth());
                        item.put("name", list.get(i).getName());
                        item.put("E", list.get(i).getE());
                        item.put("F", list.get(i).getF());
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
                        data.add(item);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                adapter = new SimpleAdapter(KaoQinBiaoActivity.this, data, R.layout.kaoqinbiao_row, new String[]{"year","moth","name","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","AA","AB","AC","AD","AE","AF","AG","AH","AI","AJ","AK","AL","AM","AN"}, new int[]{ R.id.year, R.id.moth, R.id.name, R.id.E, R.id.F, R.id.G, R.id.H, R.id.I, R.id.J, R.id.K, R.id.L, R.id.M, R.id.N, R.id.O, R.id.P, R.id.Q, R.id.R, R.id.S, R.id.T, R.id.U, R.id.V, R.id.W, R.id.X, R.id.Y, R.id.Z, R.id.AA, R.id.AB, R.id.AC, R.id.AD, R.id.AE, R.id.AF, R.id.AG, R.id.AH, R.id.AI, R.id.AJ, R.id.AK, R.id.AL, R.id.AM, R.id.AN}) {
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

                adapter_block = new SimpleAdapter(KaoQinBiaoActivity.this, data, R.layout.kaoqinbiao_row_block, new String[]{"year","moth","name","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","AA","AB","AC","AD","AE","AF","AG","AH","AI","AJ","AK","AL","AM","AN"}, new int[]{ R.id.year, R.id.moth, R.id.name, R.id.E, R.id.F, R.id.G, R.id.H, R.id.I, R.id.J, R.id.K, R.id.L, R.id.M, R.id.N, R.id.O, R.id.P, R.id.Q, R.id.R, R.id.S, R.id.T, R.id.U, R.id.V, R.id.W, R.id.X, R.id.Y, R.id.Z, R.id.AA, R.id.AB, R.id.AC, R.id.AD, R.id.AE, R.id.AF, R.id.AG, R.id.AH, R.id.AI, R.id.AJ, R.id.AK, R.id.AL, R.id.AM, R.id.AN}) {
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

    public View.OnClickListener updateClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = Integer.parseInt(view.getTag().toString());
                Intent intent = new Intent(KaoQinBiaoActivity.this, KaoQinBiaoChangeActivity.class);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(KaoQinBiaoActivity.this);
                int position = Integer.parseInt(view.getTag().toString());
                Handler deleteHandler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message msg) {
                        if ((boolean) msg.obj) {
                            ToastUtil.show(KaoQinBiaoActivity.this, "删除成功");
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
                                msg.obj = yhRenShiKaoQinJiLuService.delete(list.get(position).getId());
                                deleteHandler.sendMessage(msg);
                            }
                        }).start();
                    }
                });

                builder.setNeutralButton("查看详情", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        XiangQingYe xiangQingYe = new XiangQingYe();

                        xiangQingYe.setA_title("年:");
                        xiangQingYe.setB_title("月:");
                        xiangQingYe.setC_title("姓名:");
                        xiangQingYe.setD_title("1:");
                        xiangQingYe.setE_title("2:");
                        xiangQingYe.setF_title("3:");
                        xiangQingYe.setG_title("4:");
                        xiangQingYe.setH_title("5:");
                        xiangQingYe.setI_title("6:");
                        xiangQingYe.setJ_title("7:");
                        xiangQingYe.setK_title("8:");
                        xiangQingYe.setL_title("9:");
                        xiangQingYe.setM_title("10:");
                        xiangQingYe.setN_title("11:");
                        xiangQingYe.setO_title("12:");
                        xiangQingYe.setP_title("13:");
                        xiangQingYe.setQ_title("14:");
                        xiangQingYe.setR_title("15:");
                        xiangQingYe.setS_title("16:");
                        xiangQingYe.setT_title("17:");
                        xiangQingYe.setU_title("18:");
                        xiangQingYe.setV_title("19:");
                        xiangQingYe.setW_title("20:");
                        xiangQingYe.setX_title("21:");
                        xiangQingYe.setY_title("22:");
                        xiangQingYe.setZ_title("23:");
                        xiangQingYe.setAa_title("24:");
                        xiangQingYe.setAb_title("25:");
                        xiangQingYe.setAc_title("26:");
                        xiangQingYe.setAd_title("27:");
                        xiangQingYe.setAe_title("28:");
                        xiangQingYe.setAf_title("29:");
                        xiangQingYe.setAg_title("30:");
                        xiangQingYe.setAh_title("31:");

                        xiangQingYe.setA(list.get(position).getYear());
                        xiangQingYe.setB(list.get(position).getMoth());
                        xiangQingYe.setC(list.get(position).getName());
                        xiangQingYe.setD(list.get(position).getE());
                        xiangQingYe.setE(list.get(position).getF());
                        xiangQingYe.setF(list.get(position).getG());
                        xiangQingYe.setG(list.get(position).getH());
                        xiangQingYe.setH(list.get(position).getI());
                        xiangQingYe.setI(list.get(position).getJ());
                        xiangQingYe.setJ(list.get(position).getK());
                        xiangQingYe.setK(list.get(position).getL());
                        xiangQingYe.setL(list.get(position).getM());
                        xiangQingYe.setM(list.get(position).getN());
                        xiangQingYe.setN(list.get(position).getO());
                        xiangQingYe.setO(list.get(position).getP());
                        xiangQingYe.setP(list.get(position).getQ());
                        xiangQingYe.setQ(list.get(position).getR());
                        xiangQingYe.setR(list.get(position).getS());
                        xiangQingYe.setS(list.get(position).getT());
                        xiangQingYe.setT(list.get(position).getU());
                        xiangQingYe.setU(list.get(position).getV());
                        xiangQingYe.setV(list.get(position).getW());
                        xiangQingYe.setW(list.get(position).getX());
                        xiangQingYe.setX(list.get(position).getY());
                        xiangQingYe.setY(list.get(position).getZ());
                        xiangQingYe.setZ(list.get(position).getAa());
                        xiangQingYe.setAa(list.get(position).getAb());
                        xiangQingYe.setAb(list.get(position).getAc());
                        xiangQingYe.setAc(list.get(position).getAd());
                        xiangQingYe.setAd(list.get(position).getAe());
                        xiangQingYe.setAe(list.get(position).getAf());
                        xiangQingYe.setAf(list.get(position).getAg());
                        xiangQingYe.setAg(list.get(position).getAh());
                        xiangQingYe.setAh(list.get(position).getAi());

                        Intent intent = new Intent(KaoQinBiaoActivity.this, XiangQingYeActivity.class);
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

                builder.setMessage("确定删除吗？");
                builder.setTitle("提示");
                builder.show();
                return true;
            }
        };
    }

    public void onInsertClick(View v) {
        Intent intent = new Intent(KaoQinBiaoActivity.this, KaoQinBiaoChangeActivity.class);
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(KaoQinBiaoActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                editText.setText(year + "-" + mon);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public void xiujiaClick(View v) {

        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(KaoQinBiaoActivity.this, "保存成功");
                    initList();
                } else {
                    ToastUtil.show(KaoQinBiaoActivity.this, "保存失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                String sql = "update gongzi_kaoqinjilu set ";
                String sql2 = "";
                Calendar now = Calendar.getInstance();
                String this_date = now.get(Calendar.YEAR) + "-" + (now.get(Calendar.MONTH) + 1) + "-01";
                List<Map<String, String>> list = getWeekDayInMonth(this_date);
                int i = 0;
                String[] this_day = { "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "AA", "AB", "AC", "AD", "AE", "AF", "AG", "AH", "AI" };
                for(Map<String, String> li:list){
                    System.out.println(li.get("weekday"));
                    if(li.get("weekday").equals("星期六") || li.get("weekday").equals("星期日")){
                        if(sql2.equals("")){
                            sql2 = sql2 +  " " + this_day[i] + "='休' ";
                        }else{
                            sql2 = sql2 +  " ," + this_day[i] + "='休' ";
                        }
                    }
                    i = i + 1;
                }
                if(!sql2.equals("")){
                    String moth = (now.get(Calendar.MONTH) + 1) + "";
                    if(moth.length() == 1){
                        moth = "0" + moth;
                    }
                    sql = sql + sql2 + " where year ='" + now.get(Calendar.YEAR) + "' and moth ='" + moth + "' and AO='" + yhRenShiUser.getL().replace("_hr","") + "'";
                    yhRenShiKaoQinJiLuService = new YhRenShiKaoQinJiLuService();
                    msg.obj = yhRenShiKaoQinJiLuService.update_xiujia(sql);
                    saveHandler.sendMessage(msg);
                }
            }
        }).start();
    }


    public void refreshClick(View v) {

        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(KaoQinBiaoActivity.this, "保存成功");
                    initList();
                } else {
                    ToastUtil.show(KaoQinBiaoActivity.this, "保存失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                String sql = "update gongzi_kaoqinjilu set E='',F='',G='',H='',I='',J='',K='',L='',M='',N='',O='',P='',Q='',R='',S='',T='',U='',V='',W='',X='',Y='',Z='',AA='',AB='',AC='',AD='',AE='',AF='',AG='',AH='',AI='' ";
                Calendar now = Calendar.getInstance();
                String moth = (now.get(Calendar.MONTH) + 1) + "";
                if(moth.length() == 1){
                    moth = "0" + moth;
                }
                sql = sql + " where year ='" + now.get(Calendar.YEAR) + "' and moth ='" + moth + "' and AO='" + yhRenShiUser.getL().replace("_hr","") + "'";
                yhRenShiKaoQinJiLuService = new YhRenShiKaoQinJiLuService();
                msg.obj = yhRenShiKaoQinJiLuService.update_xiujia(sql);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }


    public static List<Map<String, String>> getWeekDayInMonth(String date)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<Map<String, String>> resultList = new ArrayList<>();
        String[] weeks = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        Calendar calendar = Calendar.getInstance();
        try
        {
            calendar.setTime(sdf.parse(date));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int weekday = calendar.get(Calendar.DAY_OF_WEEK) - 1;// 1是星期日
        String cWeekDay = weeks[weekday];
        Map<String, String> fristDate = new HashMap<>();
        fristDate.put("code", weekday + "");
        fristDate.put("weekday", cWeekDay);
        fristDate.put("date", sdf.format(calendar.getTime()));
        resultList.add(fristDate);
        for (int i = 1; i < days; i++)
        {
            calendar.add(calendar.DATE, 1);
            int weekday2 = calendar.get(Calendar.DAY_OF_WEEK) - 1;// 1是星期日
            String cWeekDay2 = weeks[weekday2];
            Map<String, String> map = new HashMap<>();
            map.put("code", weekday2 + "");
            map.put("weekday", cWeekDay2);
            map.put("date", sdf.format(calendar.getTime()));
            resultList.add(map);
        }
        return resultList;
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
