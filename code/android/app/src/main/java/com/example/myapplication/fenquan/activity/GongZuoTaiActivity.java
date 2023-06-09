package com.example.myapplication.fenquan.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.fenquan.entity.Copy1;
import com.example.myapplication.fenquan.entity.Renyuan;
import com.example.myapplication.fenquan.entity.Workbench;
import com.example.myapplication.fenquan.service.Copy1Service;
import com.example.myapplication.fenquan.service.WorkbenchService;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class GongZuoTaiActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_CHANG = 1;
    private Renyuan renyuan;
    private WorkbenchService workbenchService;

    private EditText start_date;
    private EditText stop_date;
    private ListView listView;
    private Button sel_button;

    private String start_dateText;
    private String stop_dateText;

    private List<Workbench> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gongzuotai);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        workbenchService = new WorkbenchService();

        start_date = findViewById(R.id.start_date);
        stop_date = findViewById(R.id.stop_date);
        listView = findViewById(R.id.list);
        sel_button = findViewById(R.id.sel_button);

        MyApplication myApplication = (MyApplication) getApplication();
        renyuan = myApplication.getRenyuan();

        initList();
        sel_button.setOnClickListener(selClick());
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
                    workbenchService = new WorkbenchService();
                    list = workbenchService.queryList(renyuan.getB(), start_dateText ,stop_dateText);
                    if (list == null) return;

                    for (int i = 0; i < list.size(); i++) {
                        HashMap<String, Object> item = new HashMap<>();
                        item.put("renyuan", list.get(i).get人员());
                        item.put("riqi", list.get(i).get日期());
                        item.put("lastriqi", list.get(i).getA最后修改日期());
                        item.put("A", list.get(i).getA());
                        item.put("B", list.get(i).getB());
                        item.put("C", list.get(i).getC());
                        item.put("D", list.get(i).getD());
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
                        item.put("RR", list.get(i).getR());
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
                        item.put("ASS", list.get(i).getAss());
                        item.put("AT", list.get(i).getAt());
                        item.put("AU", list.get(i).getAu());
                        item.put("AV", list.get(i).getAv());
                        item.put("AW", list.get(i).getAw());
                        item.put("AX", list.get(i).getAx());
                        item.put("AY", list.get(i).getAy());
                        item.put("AZ", list.get(i).getAz());

                        item.put("BA", list.get(i).getBa());
                        item.put("BB", list.get(i).getBb());
                        item.put("BC", list.get(i).getBc());
                        item.put("BD", list.get(i).getBd());
                        item.put("BE", list.get(i).getBe());
                        item.put("BF", list.get(i).getBf());
                        item.put("BG", list.get(i).getBg());
                        item.put("BH", list.get(i).getBh());
                        item.put("BI", list.get(i).getBi());
                        item.put("BJ", list.get(i).getBj());
                        item.put("BK", list.get(i).getBk());
                        item.put("BL", list.get(i).getBl());
                        item.put("BM", list.get(i).getBm());
                        item.put("BN", list.get(i).getBn());
                        item.put("BO", list.get(i).getBo());
                        item.put("BP", list.get(i).getBp());
                        item.put("BQ", list.get(i).getBq());
                        item.put("BR", list.get(i).getBr());
                        item.put("BS", list.get(i).getBs());
                        item.put("BT", list.get(i).getBt());
                        item.put("BU", list.get(i).getBu());
                        item.put("BV", list.get(i).getBv());
                        item.put("BW", list.get(i).getBw());
                        item.put("BX", list.get(i).getBx());
                        item.put("BYY", list.get(i).getByy());
                        item.put("BZ", list.get(i).getBz());

                        item.put("CA", list.get(i).getCa());
                        item.put("CB", list.get(i).getCb());
                        item.put("CC", list.get(i).getCc());
                        item.put("CD", list.get(i).getCd());
                        item.put("CE", list.get(i).getCe());
                        item.put("CF", list.get(i).getCf());
                        item.put("CG", list.get(i).getCg());
                        item.put("CH", list.get(i).getCh());
                        item.put("CI", list.get(i).getCi());
                        item.put("CJ", list.get(i).getCj());
                        item.put("CK", list.get(i).getCk());
                        item.put("CL", list.get(i).getCl());
                        item.put("CM", list.get(i).getCm());
                        item.put("CN", list.get(i).getCn());
                        item.put("CO", list.get(i).getCo());
                        item.put("CP", list.get(i).getCp());
                        item.put("CQ", list.get(i).getCq());
                        item.put("CR", list.get(i).getCr());
                        item.put("CS", list.get(i).getCs());
                        item.put("CT", list.get(i).getCt());
                        item.put("CU", list.get(i).getCu());
                        item.put("CV", list.get(i).getCv());
                        data.add(item);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                SimpleAdapter adapter = new SimpleAdapter(GongZuoTaiActivity.this, data, R.layout.gongzuotai_row, new String[]{"renyuan", "riqi","lastriqi","A","B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "RR", "S", "T", "U", "V", "W", "X", "Y", "Z","AA","AB" ,"AC", "AD", "AE", "AF", "AG", "AH", "AI", "AJ", "AK", "AL", "AM", "AN", "AO", "AP", "AQ", "AR", "ASS", "AT", "AU", "AV", "AW", "AX", "AY", "AZ","BA","BB","BC", "BD", "BE", "BF", "BG", "BH", "BI", "BJ", "BK", "BL", "BM", "BN", "BO", "BP", "BQ", "BR", "BS", "BT", "BU", "BV", "BW", "BX", "BYY", "BZ" ,"CA","CB", "CC", "CD", "CE", "CF", "CG", "CH", "CI", "CJ", "CK", "CL", "CM", "CN", "CO", "CP", "CQ", "CR", "CS", "CT", "CU", "CV"}, new int[]{R.id.renyuan, R.id.riqi, R.id.lastriqi, R.id.A, R.id.B, R.id.C, R.id.D, R.id.E, R.id.F, R.id.G, R.id.H, R.id.I, R.id.J, R.id.K, R.id.L, R.id.M, R.id.N, R.id.O, R.id.P, R.id.Q, R.id.RR, R.id.S, R.id.T, R.id.U, R.id.V, R.id.W, R.id.X, R.id.Y, R.id.Z, R.id.AA, R.id.AB, R.id.AC, R.id.AD, R.id.AE, R.id.AF, R.id.AG, R.id.AH, R.id.AI, R.id.AJ, R.id.AK, R.id.AL, R.id.AM, R.id.AN, R.id.AO, R.id.AP, R.id.AQ, R.id.AR, R.id.ASS, R.id.AT, R.id.AU, R.id.AV, R.id.AW, R.id.AX, R.id.AY, R.id.AZ, R.id.BA, R.id.BB, R.id.BC, R.id.BD, R.id.BE, R.id.BF, R.id.BG, R.id.BH, R.id.BI, R.id.BJ, R.id.BK, R.id.BL, R.id.BM, R.id.BN, R.id.BO, R.id.BP, R.id.BQ, R.id.BR, R.id.BS, R.id.BT, R.id.BU, R.id.BV, R.id.BW, R.id.BX, R.id.BYY, R.id.BZ, R.id.CA, R.id.CB, R.id.CC, R.id.CD, R.id.CE, R.id.CF, R.id.CG, R.id.CH, R.id.CI, R.id.CJ, R.id.CK, R.id.CL, R.id.CM, R.id.CN, R.id.CO, R.id.CP, R.id.CQ, R.id.CR, R.id.CS, R.id.CT, R.id.CU, R.id.CV}) {
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

    public View.OnClickListener selClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initList();
            }
        };
    }

    public View.OnClickListener updateClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = Integer.parseInt(view.getTag().toString());
                Intent intent = new Intent(GongZuoTaiActivity.this, GongZuoTaiChangeActivity.class);
                intent.putExtra("type", R.id.update_btn);
                MyApplication myApplication = (MyApplication) getApplication();
                myApplication.setObj(list.get(position));
                startActivityForResult(intent, REQUEST_CODE_CHANG);
            }
        };
    }

    public void onInsertClick(View v) {
        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(GongZuoTaiActivity.this, "添加成功");
                    initList();
                } else {
                    ToastUtil.show(GongZuoTaiActivity.this, "添加失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                Workbench workbench = new Workbench();
                workbench.set人员(renyuan.getC());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
                Date date = new Date();
                String this_year = sdf.format(date);
                workbench.set日期(this_year);
                workbench.setA最后修改日期(this_year);
                workbench.set公司(renyuan.getB());
                msg.obj = workbenchService.insert(workbench);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    public View.OnLongClickListener onItemLongClick() {
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(GongZuoTaiActivity.this);
                int position = Integer.parseInt(view.getTag().toString());
                Handler deleteHandler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message msg) {
                        if ((boolean) msg.obj) {
                            ToastUtil.show(GongZuoTaiActivity.this, "删除成功");
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
                                msg.obj = workbenchService.delete(list.get(position).getId());
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