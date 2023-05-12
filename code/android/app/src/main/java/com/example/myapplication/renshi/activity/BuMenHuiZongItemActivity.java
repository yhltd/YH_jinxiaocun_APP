package com.example.myapplication.renshi.activity;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BuMenHuiZongItemActivity extends AppCompatActivity {

    private final static int REQUEST_CODE_CHANG = 1;

    private YhRenShiUser yhRenShiUser;
    private YhRenShiGongZiMingXiService yhRenShiGongZiMingXiService;
    private ListView listView;
    private EditText yuangong_name;
    private String yuangong_nameText;
    private Button sel_button;
    private String department;
    List<YhRenShiGongZiMingXi> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bumenhuizongitem);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //初始化控件
        listView = findViewById(R.id.bumenhuizongitem_list);
        yuangong_name = findViewById(R.id.yuangong_name);

        sel_button = findViewById(R.id.sel_button);
        sel_button.setOnClickListener(selClick());
        sel_button.requestFocus();

        Intent intent = getIntent();
        department = intent.getStringExtra("type");

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
        yuangong_nameText = yuangong_name.getText().toString();
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
                    yhRenShiGongZiMingXiService = new YhRenShiGongZiMingXiService();
                    list = yhRenShiGongZiMingXiService.bumenhuizongItemQueryList(yhRenShiUser.getL(),yuangong_nameText,department);
                    if (list == null) return;

                    for (int i = 0; i < list.size(); i++) {
                        HashMap<String, Object> item = new HashMap<>();
                        item.put("B", list.get(i).getB());
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
                        item.put("BC", list.get(i).getBc());
                        data.add(item);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                SimpleAdapter adapter = new SimpleAdapter(BuMenHuiZongItemActivity.this, data, R.layout.bumenhuizongitem_row, new String[]{"B","C","D","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","AA","AB","AC","AD","AE","AF","AG","AH","AI","AJ","AK","AL","AM","AN","AO","AP","AQ","AR","ASA","ATA","AU","AV","AW","AX","AY","BC"}, new int[]{R.id.B,R.id.C, R.id.D, R.id.G, R.id.H, R.id.I, R.id.J, R.id.K, R.id.L, R.id.M, R.id.N, R.id.O, R.id.P, R.id.Q, R.id.R, R.id.S, R.id.T, R.id.U, R.id.V, R.id.W, R.id.X, R.id.Y, R.id.Z, R.id.AA, R.id.AB, R.id.AC, R.id.AD, R.id.AE, R.id.AF, R.id.AG, R.id.AH, R.id.AI, R.id.AJ, R.id.AK, R.id.AL, R.id.AM, R.id.AN, R.id.AO, R.id.AP, R.id.AQ, R.id.AR, R.id.ASA, R.id.ATA, R.id.AU, R.id.AV, R.id.AW, R.id.AX, R.id.AY, R.id.BC}) {
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
                AlertDialog.Builder builder = new AlertDialog.Builder(BuMenHuiZongItemActivity.this);
                int position = Integer.parseInt(view.getTag().toString());

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        XiangQingYe xiangQingYe = new XiangQingYe();

                        xiangQingYe.setA_title("姓名:");
                        xiangQingYe.setB_title("部门:");
                        xiangQingYe.setC_title("岗位:");
                        xiangQingYe.setD_title("基本工资:");
                        xiangQingYe.setE_title("绩效工资:");
                        xiangQingYe.setF_title("岗位工资:");
                        xiangQingYe.setG_title("标准工资:");
                        xiangQingYe.setH_title("跨度工资:");
                        xiangQingYe.setI_title("职称津贴:");
                        xiangQingYe.setJ_title("月出勤天数:");
                        xiangQingYe.setK_title("加班时间:");
                        xiangQingYe.setL_title("加班费:");
                        xiangQingYe.setM_title("全勤应发:");
                        xiangQingYe.setN_title("缺勤天数:");
                        xiangQingYe.setO_title("缺勤扣款:");
                        xiangQingYe.setP_title("迟到天数:");
                        xiangQingYe.setQ_title("迟到扣款:");
                        xiangQingYe.setR_title("应发工资:");
                        xiangQingYe.setS_title("社保基数:");
                        xiangQingYe.setT_title("医疗基数:");
                        xiangQingYe.setU_title("公积金基数:");
                        xiangQingYe.setV_title("年金基数:");
                        xiangQingYe.setW_title("企业养老:");
                        xiangQingYe.setX_title("企业失业:");
                        xiangQingYe.setY_title("企业医疗:");
                        xiangQingYe.setZ_title("企业工伤:");
                        xiangQingYe.setAa_title("企业生育:");
                        xiangQingYe.setAb_title("企业公积金:");
                        xiangQingYe.setAc_title("企业年金:");
                        xiangQingYe.setAd_title("滞纳金:");
                        xiangQingYe.setAe_title("利息:");
                        xiangQingYe.setAf_title("企业小计:");
                        xiangQingYe.setAg_title("个人养老:");
                        xiangQingYe.setAh_title("个人失业:");

                        xiangQingYe.setA(list.get(position).getB());
                        xiangQingYe.setB(list.get(position).getC());
                        xiangQingYe.setC(list.get(position).getD());
                        xiangQingYe.setD(list.get(position).getG());
                        xiangQingYe.setE(list.get(position).getH());
                        xiangQingYe.setF(list.get(position).getI());
                        xiangQingYe.setG(list.get(position).getJ());
                        xiangQingYe.setH(list.get(position).getK());
                        xiangQingYe.setI(list.get(position).getL());
                        xiangQingYe.setJ(list.get(position).getM());
                        xiangQingYe.setK(list.get(position).getN());
                        xiangQingYe.setL(list.get(position).getO());
                        xiangQingYe.setM(list.get(position).getP());
                        xiangQingYe.setN(list.get(position).getQ());
                        xiangQingYe.setO(list.get(position).getR());
                        xiangQingYe.setP(list.get(position).getS());
                        xiangQingYe.setQ(list.get(position).getT());
                        xiangQingYe.setR(list.get(position).getU());
                        xiangQingYe.setS(list.get(position).getV());
                        xiangQingYe.setT(list.get(position).getW());
                        xiangQingYe.setU(list.get(position).getX());
                        xiangQingYe.setV(list.get(position).getY());
                        xiangQingYe.setW(list.get(position).getZ());
                        xiangQingYe.setX(list.get(position).getAa());
                        xiangQingYe.setY(list.get(position).getAb());
                        xiangQingYe.setZ(list.get(position).getAc());
                        xiangQingYe.setAa(list.get(position).getAd());
                        xiangQingYe.setAb(list.get(position).getAe());
                        xiangQingYe.setAc(list.get(position).getAf());
                        xiangQingYe.setAd(list.get(position).getAg());
                        xiangQingYe.setAe(list.get(position).getAh());
                        xiangQingYe.setAf(list.get(position).getAi());
                        xiangQingYe.setAg(list.get(position).getAj());
                        xiangQingYe.setAh(list.get(position).getAk());


                        Intent intent = new Intent(BuMenHuiZongItemActivity.this, XiangQingYeActivity.class);
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
