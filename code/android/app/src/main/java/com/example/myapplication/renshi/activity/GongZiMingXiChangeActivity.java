package com.example.myapplication.renshi.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.finance.activity.VoucherSummaryActivity;
import com.example.myapplication.finance.entity.YhFinanceVoucherSummary;
import com.example.myapplication.finance.service.YhFinanceVoucherSummaryService;
import com.example.myapplication.renshi.entity.YhRenShiGongZiMingXi;
import com.example.myapplication.renshi.entity.YhRenShiKaoQinJiLu;
import com.example.myapplication.renshi.entity.YhRenShiPeiZhiBiao;
import com.example.myapplication.renshi.entity.YhRenShiUser;
import com.example.myapplication.renshi.service.YhRenShiGongZiMingXiService;
import com.example.myapplication.renshi.service.YhRenShiKaoQinJiLuService;
import com.example.myapplication.renshi.service.YhRenShiPeiZhiBiaoService;
import com.example.myapplication.renshi.service.YhRenShiUserService;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class GongZiMingXiChangeActivity extends AppCompatActivity {

    private YhRenShiUser yhRenShiUser;
    private YhRenShiUser this_renyuan;
    private YhRenShiUserService yhRenShiUserService;
    private YhRenShiKaoQinJiLuService yhRenShiKaoQinJiLuService;
    private YhRenShiKaoQinJiLu yhRenShiKaoQinJiLu;
    private YhRenShiGongZiMingXi yhRenShiGongZiMingXi;
    private YhRenShiGongZiMingXiService yhRenShiGongZiMingXiService;
    private YhRenShiPeiZhiBiaoService yhRenShiPeiZhiBiaoService;
    List<YhRenShiUser> user_list;
    List<YhRenShiKaoQinJiLu> kaoqin_list;
    List<YhRenShiPeiZhiBiao> peizhi_list;

    List<String> name_array;
    List<String> shebao_jishu_array;
    List<String> yiliao_jishu_array;
    List<String> gongjijin_jishu_array;
    List<String> nianjin_jinjishu_array;
    List<String> zhinajin_array;
    List<String> lixi_array;
    List<String> kuadu_gongzi_array;
    List<String> zhicheng_jintie_array;
    List<String> nianjin1_array;
    List<String> yansuan_gongshi_array;

    private String B_selectText;
    private String BC_Text;

    private Spinner B;
    private EditText C;
    private EditText D;
    private EditText E;
    private EditText F;
    private EditText G;
    private EditText H;
    private EditText I;
    private EditText J;
    private Spinner K;
    private Spinner L;
    private EditText M;
    private EditText N;
    private EditText O;
    private EditText P;
    private EditText Q;
    private EditText RR;
    private EditText S;
    private EditText T;
    private EditText U;
    private Spinner V;
    private Spinner W;
    private Spinner X;
    private Spinner Y;
    private EditText Z;
    private EditText AA;
    private EditText AB;
    private EditText AC;
    private EditText AD;
    private EditText AE;
    private EditText AF;
    private Spinner AG;
    private Spinner AH;
    private EditText AI;
    private EditText AJ;
    private EditText AK;
    private EditText AL;
    private EditText AM;
    private EditText AN;
    private EditText AO;
    private Spinner AP;
    private Spinner AQ;
    private EditText AR;
    private EditText ASA;
    private EditText ATA;
    private EditText AU;
    private EditText AV;
    private EditText AW;
    private Spinner AX;
    private EditText AY;
    private Spinner AZ;
    private EditText BA;
    private EditText BB;
    private EditText BC;

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gongzimingxi_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        yhRenShiUser = myApplication.getYhRenShiUser();
        yhRenShiGongZiMingXiService = new YhRenShiGongZiMingXiService();

        B = findViewById(R.id.B);
        C = findViewById(R.id.C);
        D = findViewById(R.id.D);
        E = findViewById(R.id.E);
        F = findViewById(R.id.F);
        G = findViewById(R.id.G);
        H = findViewById(R.id.H);
        I = findViewById(R.id.I);
        J = findViewById(R.id.J);
        K = findViewById(R.id.K);
        L = findViewById(R.id.L);
        M = findViewById(R.id.M);
        N = findViewById(R.id.N);
        O = findViewById(R.id.O);
        P = findViewById(R.id.P);
        Q = findViewById(R.id.Q);
        RR = findViewById(R.id.RR);
        S = findViewById(R.id.S);
        T = findViewById(R.id.T);
        U = findViewById(R.id.U);
        V = findViewById(R.id.V);
        W = findViewById(R.id.W);
        X = findViewById(R.id.X);
        Y = findViewById(R.id.Y);
        Z = findViewById(R.id.Z);
        AA = findViewById(R.id.AA);
        AB = findViewById(R.id.AB);
        AC = findViewById(R.id.AC);
        AD = findViewById(R.id.AD);
        AE = findViewById(R.id.AE);
        AF = findViewById(R.id.AF);
        AG = findViewById(R.id.AG);
        AH = findViewById(R.id.AH);
        AI = findViewById(R.id.AI);
        AJ = findViewById(R.id.AJ);
        AK = findViewById(R.id.AK);
        AL = findViewById(R.id.AL);
        AM = findViewById(R.id.AM);
        AN = findViewById(R.id.AN);
        AO = findViewById(R.id.AO);
        AP = findViewById(R.id.AP);
        AQ = findViewById(R.id.AQ);
        AR = findViewById(R.id.AR);
        ASA = findViewById(R.id.ASA);
        ATA = findViewById(R.id.ATA);
        AU = findViewById(R.id.AU);
        AV = findViewById(R.id.AV);
        AW = findViewById(R.id.AW);
        AX = findViewById(R.id.AX);
        AY = findViewById(R.id.AY);
        AZ = findViewById(R.id.AZ);
        BA = findViewById(R.id.BA);
        BB = findViewById(R.id.BB);
        BC = findViewById(R.id.BC);

        init_select();
    }

    public void clearClick(View v) {
        C.setText("");
        D.setText("");
        E.setText("");
        F.setText("");
        G.setText("");
        H.setText("");
        I.setText("");
        J.setText("");
        M.setText("");
        N.setText("");
        O.setText("");
        P.setText("");
        Q.setText("");
        S.setText("");
        T.setText("");
        U.setText("");
        Z.setText("");
        AA.setText("");
        AB.setText("");
        AC.setText("");
        AD.setText("");
        AE.setText("");
        AF.setText("");
        AI.setText("");
        AJ.setText("");
        AK.setText("");
        AL.setText("");
        AM.setText("");
        AN.setText("");
        AO.setText("");
        AR.setText("");
        ASA.setText("");
        ATA.setText("");
        AU.setText("");
        AV.setText("");
        AW.setText("");
        AY.setText("");
        BA.setText("");
        BB.setText("");
        BC.setText("");
    }

    public void init_select() {
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                SpinnerAdapter adapter = new ArrayAdapter<String>(GongZiMingXiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, name_array);
                B.setAdapter(StringUtils.cast(adapter));

                adapter = new ArrayAdapter<String>(GongZiMingXiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, shebao_jishu_array);
                V.setAdapter(StringUtils.cast(adapter));

                adapter = new ArrayAdapter<String>(GongZiMingXiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, yiliao_jishu_array);
                W.setAdapter(StringUtils.cast(adapter));

                adapter = new ArrayAdapter<String>(GongZiMingXiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, gongjijin_jishu_array);
                X.setAdapter(StringUtils.cast(adapter));

                adapter = new ArrayAdapter<String>(GongZiMingXiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, nianjin_jinjishu_array);
                Y.setAdapter(StringUtils.cast(adapter));

                adapter = new ArrayAdapter<String>(GongZiMingXiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, zhinajin_array);
                AG.setAdapter(StringUtils.cast(adapter));
                AP.setAdapter(StringUtils.cast(adapter));

                adapter = new ArrayAdapter<String>(GongZiMingXiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, lixi_array);
                AH.setAdapter(StringUtils.cast(adapter));
                AQ.setAdapter(StringUtils.cast(adapter));

                adapter = new ArrayAdapter<String>(GongZiMingXiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, kuadu_gongzi_array);
                K.setAdapter(StringUtils.cast(adapter));

                adapter = new ArrayAdapter<String>(GongZiMingXiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, zhicheng_jintie_array);
                L.setAdapter(StringUtils.cast(adapter));

                adapter = new ArrayAdapter<String>(GongZiMingXiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, nianjin1_array);
                AX.setAdapter(StringUtils.cast(adapter));

                adapter = new ArrayAdapter<String>(GongZiMingXiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, yansuan_gongshi_array);
                AZ.setAdapter(StringUtils.cast(adapter));

                B.setOnItemSelectedListener(new typeSelectSelectedListener());

                Intent intent = getIntent();
                int id = intent.getIntExtra("type", 0);
                if (id == R.id.insert_btn) {
                    yhRenShiGongZiMingXi = new YhRenShiGongZiMingXi();
                    Button btn = findViewById(id);
                    btn.setVisibility(View.VISIBLE);
                    Calendar now = Calendar.getInstance();
                    String year = now.get(Calendar.YEAR) + "";
                    String moth = (now.get(Calendar.MONTH) + 1) + "";
                    String day = now.get(Calendar.DAY_OF_MONTH) + "";
                    if(moth.length() == 1){
                        moth = "0" + moth;
                    }
                    if(day.length() == 1){
                        day = "0" + day;
                    }
                    BC.setText(year + "-" + moth + "-" + day);
                } else if (id == R.id.update_btn) {
                    MyApplication myApplication = (MyApplication) getApplication();
                    yhRenShiGongZiMingXi = (YhRenShiGongZiMingXi) myApplication.getObj();
                    Button btn = findViewById(id);
                    btn.setVisibility(View.VISIBLE);

                    B.setSelection(getName(yhRenShiGongZiMingXi.getB()));
                    C.setText(yhRenShiGongZiMingXi.getC());
                    D.setText(yhRenShiGongZiMingXi.getD());
                    E.setText(yhRenShiGongZiMingXi.getE());
                    F.setText(yhRenShiGongZiMingXi.getF());
                    G.setText(yhRenShiGongZiMingXi.getG());
                    H.setText(yhRenShiGongZiMingXi.getH());
                    I.setText(yhRenShiGongZiMingXi.getI());
                    J.setText(yhRenShiGongZiMingXi.getJ());
                    K.setSelection(getKuaDu(yhRenShiGongZiMingXi.getK()));
                    L.setSelection(getZhiCheng(yhRenShiGongZiMingXi.getL()));
                    M.setText(yhRenShiGongZiMingXi.getM());
                    N.setText(yhRenShiGongZiMingXi.getN());
                    O.setText(yhRenShiGongZiMingXi.getO());
                    P.setText(yhRenShiGongZiMingXi.getP());
                    Q.setText(yhRenShiGongZiMingXi.getQ());
                    RR.setText(yhRenShiGongZiMingXi.getR());
                    S.setText(yhRenShiGongZiMingXi.getS());
                    T.setText(yhRenShiGongZiMingXi.getT());
                    U.setText(yhRenShiGongZiMingXi.getU());
                    V.setSelection(getSheBao(yhRenShiGongZiMingXi.getV()));
                    W.setSelection(getYiLiao(yhRenShiGongZiMingXi.getW()));
                    X.setSelection(getGongJiJin(yhRenShiGongZiMingXi.getX()));
                    Y.setSelection(getNianJin(yhRenShiGongZiMingXi.getY()));
                    Z.setText(yhRenShiGongZiMingXi.getZ());
                    AA.setText(yhRenShiGongZiMingXi.getAa());
                    AB.setText(yhRenShiGongZiMingXi.getAb());
                    AC.setText(yhRenShiGongZiMingXi.getAc());
                    AD.setText(yhRenShiGongZiMingXi.getAd());
                    AE.setText(yhRenShiGongZiMingXi.getAe());
                    AF.setText(yhRenShiGongZiMingXi.getAf());
                    AG.setSelection(getZhiNaJin(yhRenShiGongZiMingXi.getAg()));
                    AH.setSelection(getLiXi(yhRenShiGongZiMingXi.getAh()));
                    AI.setText(yhRenShiGongZiMingXi.getAi());
                    AJ.setText(yhRenShiGongZiMingXi.getAj());
                    AK.setText(yhRenShiGongZiMingXi.getAk());
                    AL.setText(yhRenShiGongZiMingXi.getAl());
                    AM.setText(yhRenShiGongZiMingXi.getAm());
                    AN.setText(yhRenShiGongZiMingXi.getAn());
                    AO.setText(yhRenShiGongZiMingXi.getAo());
                    AP.setSelection(getZhiNaJin(yhRenShiGongZiMingXi.getAp()));
                    AQ.setSelection(getLiXi(yhRenShiGongZiMingXi.getAq()));
                    AR.setText(yhRenShiGongZiMingXi.getAr());
                    ASA.setText(yhRenShiGongZiMingXi.getAsa());
                    ATA.setText(yhRenShiGongZiMingXi.getAta());
                    AU.setText(yhRenShiGongZiMingXi.getAu());
                    AV.setText(yhRenShiGongZiMingXi.getAv());
                    AW.setText(yhRenShiGongZiMingXi.getAw());
                    AX.setSelection(getNianJin1(yhRenShiGongZiMingXi.getAx()));
                    AY.setText(yhRenShiGongZiMingXi.getAy());
                    AZ.setSelection(getYanSuan(yhRenShiGongZiMingXi.getAz()));
                    BA.setText(yhRenShiGongZiMingXi.getBa());
                    BB.setText(yhRenShiGongZiMingXi.getBb());
                    BC.setText(yhRenShiGongZiMingXi.getBc());
                }

                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                SpinnerAdapter adapter = null;
                try {
                    yhRenShiUserService = new YhRenShiUserService();
                    yhRenShiPeiZhiBiaoService = new YhRenShiPeiZhiBiaoService();
                    List<YhRenShiPeiZhiBiao> peizhiList = yhRenShiPeiZhiBiaoService.getList(yhRenShiUser.getL());
                    List<YhRenShiUser> nameList = yhRenShiUserService.getList(yhRenShiUser.getL(),"","");

                    name_array = new ArrayList<>();
                    name_array.add("");
                    if (nameList.size() > 0) {
                        for (int i = 0; i < nameList.size(); i++) {
                            if(!nameList.get(i).getB().equals(""))
                                name_array.add(nameList.get(i).getB());
                        }
                    }

                    shebao_jishu_array = new ArrayList<>();
                    if (peizhiList.size() > 0) {
                        for (int i = 0; i < peizhiList.size(); i++) {
                            if(!peizhiList.get(i).getKaoqin().equals(""))
                                shebao_jishu_array.add(peizhiList.get(i).getKaoqin());
                        }
                    }

                    yiliao_jishu_array = new ArrayList<>();
                    if (peizhiList.size() > 0) {
                        for (int i = 0; i < peizhiList.size(); i++) {
                            if(!peizhiList.get(i).getYiliao_jishu().equals(""))
                                yiliao_jishu_array.add(peizhiList.get(i).getYiliao_jishu());
                        }
                    }

                    gongjijin_jishu_array = new ArrayList<>();
                    if (peizhiList.size() > 0) {
                        for (int i = 0; i < peizhiList.size(); i++) {
                            if(!peizhiList.get(i).getKaoqin_peizhi().equals(""))
                                gongjijin_jishu_array.add(peizhiList.get(i).getKaoqin_peizhi());
                        }
                    }

                    nianjin_jinjishu_array = new ArrayList<>();
                    if (peizhiList.size() > 0) {
                        for (int i = 0; i < peizhiList.size(); i++) {
                            if(!peizhiList.get(i).getNianjin_jishu().equals(""))
                                nianjin_jinjishu_array.add(peizhiList.get(i).getNianjin_jishu());
                        }
                    }

                    zhinajin_array = new ArrayList<>();
                    if (peizhiList.size() > 0) {
                        for (int i = 0; i < peizhiList.size(); i++) {
                            if(!peizhiList.get(i).getZhinajin().equals(""))
                                zhinajin_array.add(peizhiList.get(i).getZhinajin());
                        }
                    }

                    lixi_array = new ArrayList<>();
                    if (peizhiList.size() > 0) {
                        for (int i = 0; i < peizhiList.size(); i++) {
                            if(!peizhiList.get(i).getLixi().equals(""))
                                lixi_array.add(peizhiList.get(i).getLixi());
                        }
                    }

                    kuadu_gongzi_array = new ArrayList<>();
                    if (peizhiList.size() > 0) {
                        for (int i = 0; i < peizhiList.size(); i++) {
                            if(!peizhiList.get(i).getKuadu_gongzi().equals(""))
                                kuadu_gongzi_array.add(peizhiList.get(i).getKuadu_gongzi());
                        }
                    }

                    zhicheng_jintie_array = new ArrayList<>();
                    if (peizhiList.size() > 0) {
                        for (int i = 0; i < peizhiList.size(); i++) {
                            if(!peizhiList.get(i).getJintie().equals(""))
                                zhicheng_jintie_array.add(peizhiList.get(i).getJintie());
                        }
                    }

                    nianjin1_array = new ArrayList<>();
                    if (peizhiList.size() > 0) {
                        for (int i = 0; i < peizhiList.size(); i++) {
                            if(!peizhiList.get(i).getNianjin1().equals(""))
                                nianjin1_array.add(peizhiList.get(i).getNianjin1());
                        }
                    }

                    yansuan_gongshi_array = new ArrayList<>();
                    if (peizhiList.size() > 0) {
                        for (int i = 0; i < peizhiList.size(); i++) {
                            if(!peizhiList.get(i).getYansuangongshi().equals(""))
                                yansuan_gongshi_array.add(peizhiList.get(i).getNianjin1());
                        }
                    }

                    adapter = new ArrayAdapter<String>(GongZiMingXiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, shebao_jishu_array);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.obj = adapter;
                listLoadHandler.sendMessage(msg);
            }
        }).start();
    }


    private class typeSelectSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //获取选择的项的值
            B_selectText = B.getItemAtPosition(position).toString();
            BC_Text = BC.getText().toString();
            Handler listLoadHandler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(@NonNull Message msg) {
                    if(this_renyuan != null){
                        C.setText(this_renyuan.getC());
                        D.setText(this_renyuan.getD());
                        E.setText(this_renyuan.getE());
                        F.setText(this_renyuan.getH());
                        G.setText(this_renyuan.getF());
                        H.setText(this_renyuan.getAc());
                        BA.setText(this_renyuan.getG());
                    }
                    if(yhRenShiKaoQinJiLu != null){
                        M.setText(yhRenShiKaoQinJiLu.getAk());
                        Q.setText(yhRenShiKaoQinJiLu.getAl());
                        S.setText(yhRenShiKaoQinJiLu.getAm());
                        N.setText(yhRenShiKaoQinJiLu.getAn());
                    }
                    return true;
                }
            });

            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<HashMap<String, Object>> data = new ArrayList<>();
                    try {
                        yhRenShiUserService = new YhRenShiUserService();
                        user_list = yhRenShiUserService.getList(yhRenShiUser.getL(),B_selectText,"");
                        if (user_list == null){
                            this_renyuan = null;
                        }

                        yhRenShiKaoQinJiLuService = new YhRenShiKaoQinJiLuService();
                        kaoqin_list = yhRenShiKaoQinJiLuService.nameMonthList(yhRenShiUser.getL(),B_selectText,BC_Text.split("-")[0] + BC_Text.split("-")[1]);
                        if (kaoqin_list == null){
                            yhRenShiKaoQinJiLu = null;
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    this_renyuan = new YhRenShiUser();
                    yhRenShiKaoQinJiLu = new YhRenShiKaoQinJiLu();
                    if(user_list != null){
                        for (int i = 0; i < user_list.size(); i++) {
                            if(user_list.get(i).getB().equals(B_selectText)){
                                this_renyuan.setC(user_list.get(i).getC());
                                this_renyuan.setD(user_list.get(i).getD());
                                this_renyuan.setE(user_list.get(i).getE());
                                this_renyuan.setF(user_list.get(i).getF());
                                this_renyuan.setG(user_list.get(i).getG());
                                this_renyuan.setH(user_list.get(i).getH());
                                this_renyuan.setAc(user_list.get(i).getAc());
                            }
                        }
                    }

                    if(kaoqin_list != null){
                        for (int i = 0; i < kaoqin_list.size(); i++) {
                            if(kaoqin_list.get(i).getName().equals(B_selectText)){
                                yhRenShiKaoQinJiLu.setAk(kaoqin_list.get(i).getAk());
                                yhRenShiKaoQinJiLu.setAl(Integer.parseInt(kaoqin_list.get(i).getAj()) - Integer.parseInt(kaoqin_list.get(i).getAk()) + "");
                                yhRenShiKaoQinJiLu.setAm(kaoqin_list.get(i).getAm());
                                yhRenShiKaoQinJiLu.setAn(kaoqin_list.get(i).getAn());
                            }
                        }
                    }

                    Message msg = new Message();
                    msg.obj = "";
                    listLoadHandler.sendMessage(msg);

                }
            }).start();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void insertClick(View v) {
        if (!checkForm()) return;

        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(GongZiMingXiChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(GongZiMingXiChangeActivity.this, "保存失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = yhRenShiGongZiMingXiService.insert(yhRenShiGongZiMingXi);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    public void jisuanClick(View v) {
        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {



                if(peizhi_list != null){

                    double chidao_koukuan = 0;
                    double jiabanfei = 0;
                    double queqin_koukuan = 0;
                    double geren_yiliao = 0;
                    double qiye_yiliao = 0;
                    double geren_shengyu = 0;
                    double qiye_shengyu = 0;
                    double geren_yanglao = 0;
                    double qiye_yanglao = 0;
                    double geren_shiye = 0;
                    double qiye_shiye = 0;
                    double qiye_gongshang = 0;
                    double geren_nianjin = 0;
                    double qiye_nianjin = 0;
                    double geren_gongjijin = 0;
                    double qiye_gongjijin = 0;

                    double yingshui_gongzi = 0;
                    double shifa_gongzi = 0;
                    double daikou_geshui = 0;

                    //迟到扣款
                    if(!S.getText().toString().equals("") && peizhi_list.get(0).getChidao_koukuan() != null && !peizhi_list.get(0).getChidao_koukuan().equals("")){
                        chidao_koukuan = Float.parseFloat(S.getText().toString()) * Float.parseFloat(peizhi_list.get(0).getChidao_koukuan());
                        T.setText(String.valueOf(chidao_koukuan));
                    }

                    //加班费
                    if(!AF.getText().toString().equals("") && peizhi_list.get(0).getJiabanfei() != null && !peizhi_list.get(0).getJiabanfei().equals("")){
                        jiabanfei = Float.parseFloat(AF.getText().toString()) * Float.parseFloat(peizhi_list.get(0).getJiabanfei());
                        O.setText(String.valueOf(jiabanfei));
                    }

                    //缺勤扣款
                    if(!Q.getText().toString().equals("") && peizhi_list.get(0).getQueqin_koukuan() != null && !peizhi_list.get(0).getQueqin_koukuan().equals("")){
                        queqin_koukuan = Float.parseFloat(Q.getText().toString()) * Float.parseFloat(peizhi_list.get(0).getQueqin_koukuan());
                        RR.setText(String.valueOf(queqin_koukuan));
                    }

                    //个人医疗
                    if(!G.getText().toString().equals("") && peizhi_list.get(0).getGeren_yiliao() != null && !peizhi_list.get(0).getGeren_yiliao().equals("")){
                        geren_yiliao = Float.parseFloat(G.getText().toString()) * Float.parseFloat(peizhi_list.get(0).getGeren_yiliao());
                        AL.setText(String.valueOf(geren_yiliao));
                    }

                    //企业医疗
                    if(!G.getText().toString().equals("") && peizhi_list.get(0).getQiye_yiliao() != null && !peizhi_list.get(0).getQiye_yiliao().equals("")){
                        qiye_yiliao = Float.parseFloat(G.getText().toString()) * Float.parseFloat(peizhi_list.get(0).getQiye_yiliao());
                        AB.setText(String.valueOf(qiye_yiliao));
                    }

                    //个人生育
                    if(!G.getText().toString().equals("") && peizhi_list.get(0).getGeren_shengyu() != null && !peizhi_list.get(0).getGeren_shengyu().equals("")){
                        geren_shengyu = Float.parseFloat(G.getText().toString()) * Float.parseFloat(peizhi_list.get(0).getGeren_shengyu());
                        AM.setText(String.valueOf(geren_shengyu));
                    }

                    //企业生育
                    if(!G.getText().toString().equals("") && peizhi_list.get(0).getQiye_shengyu() != null && !peizhi_list.get(0).getQiye_shengyu().equals("")){
                        qiye_shengyu = Float.parseFloat(G.getText().toString()) * Float.parseFloat(peizhi_list.get(0).getQiye_shengyu());
                        AD.setText(String.valueOf(qiye_shengyu));
                    }

                    //个人养老
                    if(!G.getText().toString().equals("") && peizhi_list.get(0).getGeren_yanglao() != null && !peizhi_list.get(0).getGeren_yanglao().equals("")){
                        geren_yanglao = Float.parseFloat(G.getText().toString()) * Float.parseFloat(peizhi_list.get(0).getGeren_yanglao());
                        AJ.setText(String.valueOf(geren_yanglao));
                    }

                    //企业养老
                    if(!G.getText().toString().equals("") && peizhi_list.get(0).getQiye_yanglao() != null && !peizhi_list.get(0).getQiye_yanglao().equals("")){
                        qiye_yanglao = Float.parseFloat(G.getText().toString()) * Float.parseFloat(peizhi_list.get(0).getQiye_yanglao());
                        Z.setText(String.valueOf(qiye_yanglao));
                    }

                    //个人失业
                    if(!G.getText().toString().equals("") && peizhi_list.get(0).getGeren_shiye() != null && !peizhi_list.get(0).getGeren_shiye().equals("")){
                        geren_shiye = Float.parseFloat(G.getText().toString()) * Float.parseFloat(peizhi_list.get(0).getGeren_shiye());
                        AK.setText(String.valueOf(geren_shiye));
                    }

                    //企业失业
                    if(!G.getText().toString().equals("") && peizhi_list.get(0).getQiye_shiye() != null && !peizhi_list.get(0).getQiye_shiye().equals("")){
                        qiye_shiye = Float.parseFloat(G.getText().toString()) * Float.parseFloat(peizhi_list.get(0).getQiye_shiye());
                        AA.setText(String.valueOf(qiye_shiye));
                    }

                    //企业工伤
                    if(!G.getText().toString().equals("") && peizhi_list.get(0).getQiye_gongshang() != null && !peizhi_list.get(0).getQiye_gongshang().equals("")){
                        qiye_gongshang = Float.parseFloat(G.getText().toString()) * Float.parseFloat(peizhi_list.get(0).getQiye_gongshang());
                        AC.setText(String.valueOf(qiye_gongshang));
                    }

                    //个人年金
                    if(!G.getText().toString().equals("") && peizhi_list.get(0).getGeren_nianjin() != null && !peizhi_list.get(0).getGeren_nianjin().equals("")){
                        geren_nianjin = Float.parseFloat(G.getText().toString()) * Float.parseFloat(peizhi_list.get(0).getGeren_nianjin());
                        AO.setText(String.valueOf(geren_nianjin));
                    }

                    //企业年金
                    if(!G.getText().toString().equals("") && peizhi_list.get(0).getQiye_nianjin() != null && !peizhi_list.get(0).getQiye_nianjin().equals("")){
                        qiye_nianjin = Float.parseFloat(G.getText().toString()) * Float.parseFloat(peizhi_list.get(0).getQiye_nianjin());
                        AF.setText(String.valueOf(qiye_nianjin));
                    }

                    //个人公积金
                    if(!G.getText().toString().equals("") && peizhi_list.get(0).getGeren_gongjijin() != null && !peizhi_list.get(0).getGeren_gongjijin().equals("")){
                        geren_gongjijin = Float.parseFloat(G.getText().toString()) * Float.parseFloat(peizhi_list.get(0).getGeren_gongjijin());
                        AN.setText(String.valueOf(geren_gongjijin));
                    }

                    //企业公积金
                    if(!G.getText().toString().equals("") && peizhi_list.get(0).getQiye_gongjijin() != null && !peizhi_list.get(0).getQiye_gongjijin().equals("")){
                        qiye_gongjijin = Float.parseFloat(G.getText().toString()) * Float.parseFloat(peizhi_list.get(0).getQiye_gongjijin());
                        AE.setText(String.valueOf(qiye_gongjijin));
                    }

                    //税前工资
                    if(!G.getText().toString().equals("") && !H.getText().toString().equals("")){
                        jiabanfei = Float.parseFloat(G.getText().toString()) + Float.parseFloat(H.getText().toString());
                        ASA.setText(String.valueOf(jiabanfei));
                    }

                    for (int i = 0; i < peizhi_list.size(); i++){
                        //岗位工资
                        if(D.getText().toString().equals(peizhi_list.get(i).getGangwei())){
                            I.setText(peizhi_list.get(i).getGangwei_gongzi());
                        }

                        if(!peizhi_list.get(i).getGongzi().equals("") && peizhi_list.get(i).getGongzi() != null && peizhi_list.get(i).getGongzi().split("-").length == 2){
                            if( Float.parseFloat(G.getText().toString()) + Float.parseFloat(H.getText().toString()) >= Float.parseFloat(peizhi_list.get(i).getGongzi().split("-")[0]) && Float.parseFloat(G.getText().toString()) + Float.parseFloat(H.getText().toString()) <= Float.parseFloat(peizhi_list.get(i).getGongzi().split("-")[1]) ){
                                if(!peizhi_list.get(i).getShuilv().equals("") && peizhi_list.get(i).getShuilv() != null){
                                    yingshui_gongzi = (Float.parseFloat(G.getText().toString()) + Float.parseFloat(H.getText().toString())) - (Float.parseFloat(G.getText().toString()) + Float.parseFloat(H.getText().toString())) *Float.parseFloat(peizhi_list.get(i).getShuilv());
                                    ATA.setText(String.valueOf(yingshui_gongzi));
                                    AU.setText(peizhi_list.get(i).getShuilv());
                                    daikou_geshui = (Float.parseFloat(G.getText().toString()) + Float.parseFloat(H.getText().toString())) *Float.parseFloat(peizhi_list.get(i).getShuilv());
                                }
                            }
                        }

                    }

                    AR.setText(String.valueOf(geren_gongjijin + geren_nianjin + geren_shengyu + geren_shiye + geren_yanglao + geren_yiliao + Float.parseFloat(G.getText().toString()) + Float.parseFloat(H.getText().toString())));
                    AI.setText(String.valueOf(qiye_gongjijin + qiye_gongshang + qiye_nianjin + qiye_shengyu + qiye_shiye + qiye_yanglao + qiye_yiliao));
                    AY.setText(String.valueOf(yingshui_gongzi - geren_gongjijin + geren_nianjin + geren_shengyu + geren_shiye + geren_yanglao + geren_yiliao + Float.parseFloat(G.getText().toString()) + Float.parseFloat(H.getText().toString())));
                    shifa_gongzi = yingshui_gongzi - geren_gongjijin + geren_nianjin + geren_shengyu + geren_shiye + geren_yanglao + geren_yiliao + Float.parseFloat(G.getText().toString()) + Float.parseFloat(H.getText().toString());
                    U.setText(String.valueOf(Float.parseFloat(G.getText().toString()) + Float.parseFloat(H.getText().toString()) - (geren_gongjijin + geren_nianjin + geren_shengyu + geren_shiye + geren_yanglao + geren_yiliao)));
                    J.setText(String.valueOf(shifa_gongzi + jiabanfei - queqin_koukuan - chidao_koukuan));
                    AW.setText(String.valueOf(daikou_geshui));

                }

                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                peizhi_list = yhRenShiPeiZhiBiaoService.getList(yhRenShiUser.getL());
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
                    ToastUtil.show(GongZiMingXiChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(GongZiMingXiChangeActivity.this, "保存失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = yhRenShiGongZiMingXiService.update(yhRenShiGongZiMingXi);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    private int getName(String param) {
        if (name_array != null) {
            for (int i = 0; i < name_array.size(); i++) {
                if (param.equals(name_array.get(i))) {
                    return i;
                }
            }
        }
        return 0;
    }

    private int getKuaDu(String param) {
        if (kuadu_gongzi_array != null) {
            for (int i = 0; i < kuadu_gongzi_array.size(); i++) {
                if (param.equals(kuadu_gongzi_array.get(i))) {
                    return i;
                }
            }
        }
        return 0;
    }

    private int getZhiCheng(String param) {
        if (zhicheng_jintie_array != null) {
            for (int i = 0; i < zhicheng_jintie_array.size(); i++) {
                if (param.equals(zhicheng_jintie_array.get(i))) {
                    return i;
                }
            }
        }
        return 0;
    }

    private int getSheBao(String param) {
        if (shebao_jishu_array != null) {
            for (int i = 0; i < shebao_jishu_array.size(); i++) {
                if (param.equals(shebao_jishu_array.get(i))) {
                    return i;
                }
            }
        }
        return 0;
    }

    private int getYiLiao(String param) {
        if (yiliao_jishu_array != null) {
            for (int i = 0; i < yiliao_jishu_array.size(); i++) {
                if (param.equals(yiliao_jishu_array.get(i))) {
                    return i;
                }
            }
        }
        return 0;
    }

    private int getGongJiJin(String param) {
        if (gongjijin_jishu_array != null) {
            for (int i = 0; i < gongjijin_jishu_array.size(); i++) {
                if (param.equals(gongjijin_jishu_array.get(i))) {
                    return i;
                }
            }
        }
        return 0;
    }

    private int getNianJin(String param) {
        if (nianjin_jinjishu_array != null) {
            for (int i = 0; i < nianjin_jinjishu_array.size(); i++) {
                if (param.equals(nianjin_jinjishu_array.get(i))) {
                    return i;
                }
            }
        }
        return 0;
    }

    private int getZhiNaJin(String param) {
        if (zhinajin_array != null) {
            for (int i = 0; i < zhinajin_array.size(); i++) {
                if (param.equals(zhinajin_array.get(i))) {
                    return i;
                }
            }
        }
        return 0;
    }

    private int getLiXi(String param) {
        if (lixi_array != null) {
            for (int i = 0; i < lixi_array.size(); i++) {
                if (param.equals(lixi_array.get(i))) {
                    return i;
                }
            }
        }
        return 0;
    }

    private int getNianJin1(String param) {
        if (nianjin1_array != null) {
            for (int i = 0; i < nianjin1_array.size(); i++) {
                if (param.equals(nianjin1_array.get(i))) {
                    return i;
                }
            }
        }
        return 0;
    }

    private int getYanSuan(String param) {
        if (yansuan_gongshi_array != null) {
            for (int i = 0; i < yansuan_gongshi_array.size(); i++) {
                if (param.equals(yansuan_gongshi_array.get(i))) {
                    return i;
                }
            }
        }
        return 0;
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(GongZiMingXiChangeActivity.this, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String month = "";
                if(monthOfYear + 1 < 10){
                    month = "0" + String.valueOf(monthOfYear + 1);
                }else{
                    month = String.valueOf(monthOfYear + 1);
                }

                String day = "";
                if(dayOfMonth + 1 < 10){
                    day = "0" + String.valueOf(dayOfMonth + 1);
                }else{
                    day = String.valueOf(dayOfMonth + 1);
                }
                editText.setText(year + "-" + month + "-" + day);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private boolean checkForm() {
        if (B.getSelectedItem().toString().equals("")) {
            ToastUtil.show(GongZiMingXiChangeActivity.this, "请输入姓名");
            return false;
        } else {
            yhRenShiGongZiMingXi.setB(B.getSelectedItem().toString());
        }

        yhRenShiGongZiMingXi.setE(E.getText().toString());
        yhRenShiGongZiMingXi.setF(F.getText().toString());
        yhRenShiGongZiMingXi.setG(G.getText().toString());
        yhRenShiGongZiMingXi.setH(H.getText().toString());
        yhRenShiGongZiMingXi.setI(I.getText().toString());
        yhRenShiGongZiMingXi.setJ(J.getText().toString());
        yhRenShiGongZiMingXi.setK(K.getSelectedItem().toString());
        yhRenShiGongZiMingXi.setL(L.getSelectedItem().toString());
        yhRenShiGongZiMingXi.setM(M.getText().toString());
        yhRenShiGongZiMingXi.setN(N.getText().toString());
        yhRenShiGongZiMingXi.setO(O.getText().toString());
        yhRenShiGongZiMingXi.setP(P.getText().toString());
        yhRenShiGongZiMingXi.setQ(Q.getText().toString());
        yhRenShiGongZiMingXi.setR(RR.getText().toString());
        yhRenShiGongZiMingXi.setS(S.getText().toString());
        yhRenShiGongZiMingXi.setT(T.getText().toString());
        yhRenShiGongZiMingXi.setU(U.getText().toString());
        yhRenShiGongZiMingXi.setV(V.getSelectedItem().toString());
        yhRenShiGongZiMingXi.setW(W.getSelectedItem().toString());
        yhRenShiGongZiMingXi.setX(X.getSelectedItem().toString());
        yhRenShiGongZiMingXi.setY(Y.getSelectedItem().toString());
        yhRenShiGongZiMingXi.setZ(Z.getText().toString());
        yhRenShiGongZiMingXi.setAa(AA.getText().toString());
        yhRenShiGongZiMingXi.setAb(AB.getText().toString());
        yhRenShiGongZiMingXi.setAc(AC.getText().toString());
        yhRenShiGongZiMingXi.setAd(AD.getText().toString());
        yhRenShiGongZiMingXi.setAe(AE.getText().toString());
        yhRenShiGongZiMingXi.setAf(AF.getText().toString());
        yhRenShiGongZiMingXi.setAg(AG.getSelectedItem().toString());
        yhRenShiGongZiMingXi.setAh(AH.getSelectedItem().toString());
        yhRenShiGongZiMingXi.setAi(AI.getText().toString());
        yhRenShiGongZiMingXi.setAj(AJ.getText().toString());
        yhRenShiGongZiMingXi.setAk(AK.getText().toString());
        yhRenShiGongZiMingXi.setAl(AL.getText().toString());
        yhRenShiGongZiMingXi.setAm(AM.getText().toString());
        yhRenShiGongZiMingXi.setAn(AN.getText().toString());
        yhRenShiGongZiMingXi.setAo(AO.getText().toString());
        yhRenShiGongZiMingXi.setAp(AP.getSelectedItem().toString());
        yhRenShiGongZiMingXi.setAq(AQ.getSelectedItem().toString());
        yhRenShiGongZiMingXi.setAr(AR.getText().toString());
        yhRenShiGongZiMingXi.setAsa(ASA.getText().toString());
        yhRenShiGongZiMingXi.setAta(ATA.getText().toString());
        yhRenShiGongZiMingXi.setAu(AU.getText().toString());
        yhRenShiGongZiMingXi.setAv(AV.getText().toString());
        yhRenShiGongZiMingXi.setAw(AW.getText().toString());
        yhRenShiGongZiMingXi.setAx(AX.getSelectedItem().toString());
        yhRenShiGongZiMingXi.setAy(AY.getText().toString());
        yhRenShiGongZiMingXi.setAz(AZ.getSelectedItem().toString());
        yhRenShiGongZiMingXi.setBa(BA.getText().toString());
        yhRenShiGongZiMingXi.setBb(BB.getText().toString());
        yhRenShiGongZiMingXi.setBc(BC.getText().toString());
        yhRenShiGongZiMingXi.setBd(yhRenShiUser.getL().replace("_hr",""));
        return true;
    }

    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }

}
