package com.example.myapplication.renshi.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
import android.widget.Toast;

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
        if (C != null) C.setText("");
        if (D != null) D.setText("");
        if (E != null) E.setText("");
        if (F != null) F.setText("");
        if (G != null) G.setText("");
        if (H != null) H.setText("");
        if (I != null) I.setText("");
        if (J != null) J.setText("");
        if (M != null) M.setText("");
        if (N != null) N.setText("");
        if (O != null) O.setText("");
        if (P != null) P.setText("");
        if (Q != null) Q.setText("");
        if (S != null) S.setText("");
        if (T != null) T.setText("");
        if (U != null) U.setText("");
        if (Z != null) Z.setText("");
        if (AA != null) AA.setText("");
        if (AB != null) AB.setText("");
        if (AC != null) AC.setText("");
        if (AD != null) AD.setText("");
        if (AE != null) AE.setText("");
        if (AF != null) AF.setText("");
        if (AI != null) AI.setText("");
        if (AJ != null) AJ.setText("");
        if (AK != null) AK.setText("");
        if (AL != null) AL.setText("");
        if (AM != null) AM.setText("");
        if (AN != null) AN.setText("");
        if (AO != null) AO.setText("");
        if (AR != null) AR.setText("");
        if (ASA != null) ASA.setText("");
        if (ATA != null) ATA.setText("");
        if (AU != null) AU.setText("");
        if (AV != null) AV.setText("");
        if (AW != null) AW.setText("");
        if (AY != null) AY.setText("");
        if (BA != null) BA.setText("");
        if (BB != null) BB.setText("");
        if (BC != null) BC.setText("");
    }

    public void init_select() {
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                try {
                    if (name_array != null && !name_array.isEmpty()) {
                        SpinnerAdapter adapter = new ArrayAdapter<String>(GongZiMingXiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, name_array);
                        B.setAdapter(StringUtils.cast(adapter));
                    }

                    if (shebao_jishu_array != null && !shebao_jishu_array.isEmpty()) {
                        SpinnerAdapter adapter = new ArrayAdapter<String>(GongZiMingXiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, shebao_jishu_array);
                        V.setAdapter(StringUtils.cast(adapter));
                    }

                    if (yiliao_jishu_array != null && !yiliao_jishu_array.isEmpty()) {
                        SpinnerAdapter adapter = new ArrayAdapter<String>(GongZiMingXiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, yiliao_jishu_array);
                        W.setAdapter(StringUtils.cast(adapter));
                    }

                    if (gongjijin_jishu_array != null && !gongjijin_jishu_array.isEmpty()) {
                        SpinnerAdapter adapter = new ArrayAdapter<String>(GongZiMingXiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, gongjijin_jishu_array);
                        X.setAdapter(StringUtils.cast(adapter));
                    }

                    if (nianjin_jinjishu_array != null && !nianjin_jinjishu_array.isEmpty()) {
                        SpinnerAdapter adapter = new ArrayAdapter<String>(GongZiMingXiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, nianjin_jinjishu_array);
                        Y.setAdapter(StringUtils.cast(adapter));
                    }

                    if (zhinajin_array != null && !zhinajin_array.isEmpty()) {
                        SpinnerAdapter adapter = new ArrayAdapter<String>(GongZiMingXiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, zhinajin_array);
                        AG.setAdapter(StringUtils.cast(adapter));
                        AP.setAdapter(StringUtils.cast(adapter));
                    }

                    if (lixi_array != null && !lixi_array.isEmpty()) {
                        SpinnerAdapter adapter = new ArrayAdapter<String>(GongZiMingXiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, lixi_array);
                        AH.setAdapter(StringUtils.cast(adapter));
                        AQ.setAdapter(StringUtils.cast(adapter));
                    }

                    if (kuadu_gongzi_array != null && !kuadu_gongzi_array.isEmpty()) {
                        SpinnerAdapter adapter = new ArrayAdapter<String>(GongZiMingXiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, kuadu_gongzi_array);
                        K.setAdapter(StringUtils.cast(adapter));
                    }

                    if (zhicheng_jintie_array != null && !zhicheng_jintie_array.isEmpty()) {
                        SpinnerAdapter adapter = new ArrayAdapter<String>(GongZiMingXiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, zhicheng_jintie_array);
                        L.setAdapter(StringUtils.cast(adapter));
                    }

                    if (nianjin1_array != null && !nianjin1_array.isEmpty()) {
                        SpinnerAdapter adapter = new ArrayAdapter<String>(GongZiMingXiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, nianjin1_array);
                        AX.setAdapter(StringUtils.cast(adapter));
                    }

                    if (yansuan_gongshi_array != null && !yansuan_gongshi_array.isEmpty()) {
                        SpinnerAdapter adapter = new ArrayAdapter<String>(GongZiMingXiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, yansuan_gongshi_array);
                        AZ.setAdapter(StringUtils.cast(adapter));
                    }

                    B.setOnItemSelectedListener(new typeSelectSelectedListener());

                    Intent intent = getIntent();
                    int id = intent.getIntExtra("type", 0);
                    if (id == R.id.insert_btn) {
                        yhRenShiGongZiMingXi = new YhRenShiGongZiMingXi();
                        Button btn = findViewById(id);
                        if (btn != null) btn.setVisibility(View.VISIBLE);
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
                        if (BC != null) BC.setText(year + "-" + moth + "-" + day);
                    } else if (id == R.id.update_btn) {
                        MyApplication myApplication = (MyApplication) getApplication();
                        yhRenShiGongZiMingXi = (YhRenShiGongZiMingXi) myApplication.getObj();
                        Button btn = findViewById(id);
                        if (btn != null) btn.setVisibility(View.VISIBLE);

                        // ⭐⭐⭐ 安全设置下拉选项 ⭐⭐⭐
                        int nameIndex = getNameSafe(yhRenShiGongZiMingXi.getB());
                        if (nameIndex >= 0) {
                            B.setSelection(nameIndex);
                        }

                        if (C != null) C.setText(yhRenShiGongZiMingXi.getC());
                        if (D != null) D.setText(yhRenShiGongZiMingXi.getD());
                        if (E != null) E.setText(yhRenShiGongZiMingXi.getE());
                        if (F != null) F.setText(yhRenShiGongZiMingXi.getF());
                        if (G != null) G.setText(yhRenShiGongZiMingXi.getG());
                        if (H != null) H.setText(yhRenShiGongZiMingXi.getH());
                        if (I != null) I.setText(yhRenShiGongZiMingXi.getI());
                        if (J != null) J.setText(yhRenShiGongZiMingXi.getJ());

                        int kuaDuIndex = getKuaDuSafe(yhRenShiGongZiMingXi.getK());
                        if (kuaDuIndex >= 0) K.setSelection(kuaDuIndex);

                        int zhiChengIndex = getZhiChengSafe(yhRenShiGongZiMingXi.getL());
                        if (zhiChengIndex >= 0) L.setSelection(zhiChengIndex);

                        if (M != null) M.setText(yhRenShiGongZiMingXi.getM());
                        if (N != null) N.setText(yhRenShiGongZiMingXi.getN());
                        if (O != null) O.setText(yhRenShiGongZiMingXi.getO());
                        if (P != null) P.setText(yhRenShiGongZiMingXi.getP());
                        if (Q != null) Q.setText(yhRenShiGongZiMingXi.getQ());
                        if (RR != null) RR.setText(yhRenShiGongZiMingXi.getR());
                        if (S != null) S.setText(yhRenShiGongZiMingXi.getS());
                        if (T != null) T.setText(yhRenShiGongZiMingXi.getT());
                        if (U != null) U.setText(yhRenShiGongZiMingXi.getU());

                        int sheBaoIndex = getSheBaoSafe(yhRenShiGongZiMingXi.getV());
                        if (sheBaoIndex >= 0) V.setSelection(sheBaoIndex);

                        int yiLiaoIndex = getYiLiaoSafe(yhRenShiGongZiMingXi.getW());
                        if (yiLiaoIndex >= 0) W.setSelection(yiLiaoIndex);

                        int gongJiJinIndex = getGongJiJinSafe(yhRenShiGongZiMingXi.getX());
                        if (gongJiJinIndex >= 0) X.setSelection(gongJiJinIndex);

                        int nianJinIndex = getNianJinSafe(yhRenShiGongZiMingXi.getY());
                        if (nianJinIndex >= 0) Y.setSelection(nianJinIndex);

                        if (Z != null) Z.setText(yhRenShiGongZiMingXi.getZ());
                        if (AA != null) AA.setText(yhRenShiGongZiMingXi.getAa());
                        if (AB != null) AB.setText(yhRenShiGongZiMingXi.getAb());
                        if (AC != null) AC.setText(yhRenShiGongZiMingXi.getAc());
                        if (AD != null) AD.setText(yhRenShiGongZiMingXi.getAd());
                        if (AE != null) AE.setText(yhRenShiGongZiMingXi.getAe());
                        if (AF != null) AF.setText(yhRenShiGongZiMingXi.getAf());

                        int zhiNaJinIndex = getZhiNaJinSafe(yhRenShiGongZiMingXi.getAg());
                        if (zhiNaJinIndex >= 0) AG.setSelection(zhiNaJinIndex);

                        int liXiIndex = getLiXiSafe(yhRenShiGongZiMingXi.getAh());
                        if (liXiIndex >= 0) AH.setSelection(liXiIndex);

                        if (AI != null) AI.setText(yhRenShiGongZiMingXi.getAi());
                        if (AJ != null) AJ.setText(yhRenShiGongZiMingXi.getAj());
                        if (AK != null) AK.setText(yhRenShiGongZiMingXi.getAk());
                        if (AL != null) AL.setText(yhRenShiGongZiMingXi.getAl());
                        if (AM != null) AM.setText(yhRenShiGongZiMingXi.getAm());
                        if (AN != null) AN.setText(yhRenShiGongZiMingXi.getAn());
                        if (AO != null) AO.setText(yhRenShiGongZiMingXi.getAo());

                        int zhiNaJin2Index = getZhiNaJinSafe(yhRenShiGongZiMingXi.getAp());
                        if (zhiNaJin2Index >= 0) AP.setSelection(zhiNaJin2Index);

                        int liXi2Index = getLiXiSafe(yhRenShiGongZiMingXi.getAq());
                        if (liXi2Index >= 0) AQ.setSelection(liXi2Index);

                        if (AR != null) AR.setText(yhRenShiGongZiMingXi.getAr());
                        if (ASA != null) ASA.setText(yhRenShiGongZiMingXi.getAsa());
                        if (ATA != null) ATA.setText(yhRenShiGongZiMingXi.getAta());
                        if (AU != null) AU.setText(yhRenShiGongZiMingXi.getAu());
                        if (AV != null) AV.setText(yhRenShiGongZiMingXi.getAv());
                        if (AW != null) AW.setText(yhRenShiGongZiMingXi.getAw());

                        int nianJin1Index = getNianJin1Safe(yhRenShiGongZiMingXi.getAx());
                        if (nianJin1Index >= 0) AX.setSelection(nianJin1Index);

                        if (AY != null) AY.setText(yhRenShiGongZiMingXi.getAy());

                        int yanSuanIndex = getYanSuanSafe(yhRenShiGongZiMingXi.getAz());
                        if (yanSuanIndex >= 0) AZ.setSelection(yanSuanIndex);

                        if (BA != null) BA.setText(yhRenShiGongZiMingXi.getBa());
                        if (BB != null) BB.setText(yhRenShiGongZiMingXi.getBb());
                        if (BC != null) BC.setText(yhRenShiGongZiMingXi.getBc());
                    }

                } catch (Exception e) {
                    Log.e("init_select", "初始化异常", e);
                }

                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    yhRenShiUserService = new YhRenShiUserService();
                    yhRenShiPeiZhiBiaoService = new YhRenShiPeiZhiBiaoService();
                    List<YhRenShiPeiZhiBiao> peizhiList = yhRenShiPeiZhiBiaoService.getList(yhRenShiUser.getL());
                    List<YhRenShiUser> nameList = yhRenShiUserService.getList(yhRenShiUser.getL(),"","");

                    name_array = new ArrayList<>();
                    name_array.add("");
                    if (nameList != null && nameList.size() > 0) {
                        for (int i = 0; i < nameList.size(); i++) {
                            if(nameList.get(i).getB() != null && !nameList.get(i).getB().equals(""))
                                name_array.add(nameList.get(i).getB());
                        }
                    }

                    shebao_jishu_array = new ArrayList<>();
                    if (peizhiList != null && peizhiList.size() > 0) {
                        for (int i = 0; i < peizhiList.size(); i++) {
                            if(peizhiList.get(i).getKaoqin() != null && !peizhiList.get(i).getKaoqin().equals(""))
                                shebao_jishu_array.add(peizhiList.get(i).getKaoqin());
                        }
                    }

                    yiliao_jishu_array = new ArrayList<>();
                    if (peizhiList != null && peizhiList.size() > 0) {
                        for (int i = 0; i < peizhiList.size(); i++) {
                            if(peizhiList.get(i).getYiliao_jishu() != null && !peizhiList.get(i).getYiliao_jishu().equals(""))
                                yiliao_jishu_array.add(peizhiList.get(i).getYiliao_jishu());
                        }
                    }

                    gongjijin_jishu_array = new ArrayList<>();
                    if (peizhiList != null && peizhiList.size() > 0) {
                        for (int i = 0; i < peizhiList.size(); i++) {
                            if(peizhiList.get(i).getKaoqin_peizhi() != null && !peizhiList.get(i).getKaoqin_peizhi().equals(""))
                                gongjijin_jishu_array.add(peizhiList.get(i).getKaoqin_peizhi());
                        }
                    }

                    nianjin_jinjishu_array = new ArrayList<>();
                    if (peizhiList != null && peizhiList.size() > 0) {
                        for (int i = 0; i < peizhiList.size(); i++) {
                            if(peizhiList.get(i).getNianjin_jishu() != null && !peizhiList.get(i).getNianjin_jishu().equals(""))
                                nianjin_jinjishu_array.add(peizhiList.get(i).getNianjin_jishu());
                        }
                    }

                    zhinajin_array = new ArrayList<>();
                    if (peizhiList != null && peizhiList.size() > 0) {
                        for (int i = 0; i < peizhiList.size(); i++) {
                            if(peizhiList.get(i).getZhinajin() != null && !peizhiList.get(i).getZhinajin().equals(""))
                                zhinajin_array.add(peizhiList.get(i).getZhinajin());
                        }
                    }

                    lixi_array = new ArrayList<>();
                    if (peizhiList != null && peizhiList.size() > 0) {
                        for (int i = 0; i < peizhiList.size(); i++) {
                            if(peizhiList.get(i).getLixi() != null && !peizhiList.get(i).getLixi().equals(""))
                                lixi_array.add(peizhiList.get(i).getLixi());
                        }
                    }

                    kuadu_gongzi_array = new ArrayList<>();
                    if (peizhiList != null && peizhiList.size() > 0) {
                        for (int i = 0; i < peizhiList.size(); i++) {
                            if(peizhiList.get(i).getKuadu_gongzi() != null && !peizhiList.get(i).getKuadu_gongzi().equals(""))
                                kuadu_gongzi_array.add(peizhiList.get(i).getKuadu_gongzi());
                        }
                    }

                    zhicheng_jintie_array = new ArrayList<>();
                    if (peizhiList != null && peizhiList.size() > 0) {
                        for (int i = 0; i < peizhiList.size(); i++) {
                            if(peizhiList.get(i).getJintie() != null && !peizhiList.get(i).getJintie().equals(""))
                                zhicheng_jintie_array.add(peizhiList.get(i).getJintie());
                        }
                    }

                    nianjin1_array = new ArrayList<>();
                    if (peizhiList != null && peizhiList.size() > 0) {
                        for (int i = 0; i < peizhiList.size(); i++) {
                            if(peizhiList.get(i).getNianjin1() != null && !peizhiList.get(i).getNianjin1().equals(""))
                                nianjin1_array.add(peizhiList.get(i).getNianjin1());
                        }
                    }

                    yansuan_gongshi_array = new ArrayList<>();
                    if (peizhiList != null && peizhiList.size() > 0) {
                        for (int i = 0; i < peizhiList.size(); i++) {
                            if(peizhiList.get(i).getYansuangongshi() != null && !peizhiList.get(i).getYansuangongshi().equals(""))
                                yansuan_gongshi_array.add(peizhiList.get(i).getNianjin1());
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                Message msg = new Message();
                listLoadHandler.sendMessage(msg);
            }
        }).start();
    }


    private class typeSelectSelectedListener implements AdapterView.OnItemSelectedListener {

        private boolean isProgrammatic = false; // ⭐⭐⭐ 防止递归调用 ⭐⭐⭐

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (isProgrammatic) return; // ⭐⭐⭐ 防止递归 ⭐⭐⭐

            B_selectText = B.getItemAtPosition(position).toString();

            // ⭐⭐⭐ 1. 安全获取 BC 的值 ⭐⭐⭐
            String bcText = "";
            if (BC != null) {
                bcText = BC.getText().toString().trim();
            }

            if (B_selectText == null || B_selectText.equals("")) { // ⭐⭐⭐ 如果选中空项，直接返回 ⭐⭐⭐
                return;
            }

            Handler listLoadHandler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(@NonNull Message msg) {
                    try {
                        // ⭐⭐⭐ 2. 安全设置数据 ⭐⭐⭐
                        if (this_renyuan != null) {
                            if (C != null) C.setText(this_renyuan.getC() != null ? this_renyuan.getC() : "");
                            if (D != null) D.setText(this_renyuan.getD() != null ? this_renyuan.getD() : "");
                            if (E != null) E.setText(this_renyuan.getE() != null ? this_renyuan.getE() : "");
                            if (F != null) F.setText(this_renyuan.getF() != null ? this_renyuan.getF() : "");
                            if (G != null) G.setText(this_renyuan.getG() != null ? this_renyuan.getG() : "");
                            if (H != null) H.setText(this_renyuan.getAc() != null ? this_renyuan.getAc() : "");
                            if (BA != null) BA.setText(this_renyuan.getG() != null ? this_renyuan.getG() : "");
                        }

                        if (yhRenShiKaoQinJiLu != null) {
                            if (M != null) M.setText(yhRenShiKaoQinJiLu.getAk() != null ? yhRenShiKaoQinJiLu.getAk() : "");
                            if (Q != null) Q.setText(yhRenShiKaoQinJiLu.getAl() != null ? yhRenShiKaoQinJiLu.getAl() : "");
                            if (S != null) S.setText(yhRenShiKaoQinJiLu.getAm() != null ? yhRenShiKaoQinJiLu.getAm() : "");
                            if (N != null) N.setText(yhRenShiKaoQinJiLu.getAn() != null ? yhRenShiKaoQinJiLu.getAn() : "");
                        }
                    } catch (Exception e) {
                        Log.e("OnItemSelected", "设置数据异常", e);
                    }
                    return true;
                }
            });

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        yhRenShiUserService = new YhRenShiUserService();
                        user_list = yhRenShiUserService.getList(yhRenShiUser.getL(), B_selectText, "");

                        // ⭐⭐⭐ 3. 安全处理 user_list ⭐⭐⭐
                        if (user_list == null || user_list.isEmpty()) {
                            this_renyuan = null;
                        } else {
                            this_renyuan = new YhRenShiUser();
                            for (int i = 0; i < user_list.size(); i++) {
                                if (B_selectText.equals(user_list.get(i).getB())) {
                                    this_renyuan.setC(user_list.get(i).getC());
                                    this_renyuan.setD(user_list.get(i).getD());
                                    this_renyuan.setE(user_list.get(i).getE());
                                    this_renyuan.setF(user_list.get(i).getF());
                                    this_renyuan.setG(user_list.get(i).getG());
                                    this_renyuan.setH(user_list.get(i).getH());
                                    this_renyuan.setAc(user_list.get(i).getAc());
                                    break;
                                }
                            }
                        }

                        yhRenShiKaoQinJiLuService = new YhRenShiKaoQinJiLuService();

                        // ⭐⭐⭐ 4. 安全处理日期分割 ⭐⭐⭐
                        String year = "", month = "";
                        String bcText = "";
                        if (BC != null) {
                            bcText = BC.getText().toString().trim();
                        }
                        if (bcText.contains("-")) {
                            String[] dateParts = bcText.split("-");
                            if (dateParts.length >= 2) {
                                year = dateParts[0];
                                month = dateParts[1];
                            }
                        }

                        kaoqin_list = yhRenShiKaoQinJiLuService.nameMonthList(yhRenShiUser.getL(), B_selectText, year + month);

                        // ⭐⭐⭐ 5. 安全处理 kaoqin_list ⭐⭐⭐
                        if (kaoqin_list == null || kaoqin_list.isEmpty()) {
                            yhRenShiKaoQinJiLu = null;
                        } else {
                            yhRenShiKaoQinJiLu = new YhRenShiKaoQinJiLu();
                            for (int i = 0; i < kaoqin_list.size(); i++) {
                                if (B_selectText.equals(kaoqin_list.get(i).getName())) {
                                    yhRenShiKaoQinJiLu.setAk(kaoqin_list.get(i).getAk());
                                    try {
                                        int aj = Integer.parseInt(kaoqin_list.get(i).getAj());
                                        int ak = Integer.parseInt(kaoqin_list.get(i).getAk());
                                        yhRenShiKaoQinJiLu.setAl(String.valueOf(aj - ak));
                                    } catch (NumberFormatException e) {
                                        yhRenShiKaoQinJiLu.setAl("0");
                                    }
                                    yhRenShiKaoQinJiLu.setAm(kaoqin_list.get(i).getAm());
                                    yhRenShiKaoQinJiLu.setAn(kaoqin_list.get(i).getAn());
                                    break;
                                }
                            }
                        }

                        listLoadHandler.sendEmptyMessage(0);

                    } catch (Exception e) {
                        Log.e("OnItemSelected", "数据加载异常", e);
                        listLoadHandler.sendEmptyMessage(0);
                    }
                }
            }).start();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // ⭐⭐⭐ 6. 空选择时清空数据 ⭐⭐⭐
            isProgrammatic = true;
            B.setSelection(0);
            isProgrammatic = false;
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
                try {
                    // ⭐⭐⭐ 1. 添加空值检查 ⭐⭐⭐
                    if (peizhi_list == null || peizhi_list.isEmpty()) {
                        return true;
                    }

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

                    // ⭐⭐⭐ 2. 安全获取 EditText 值（防空指针）⭐⭐⭐
                    String sText = S != null ? S.getText().toString().trim() : "";
                    String afText = AF != null ? AF.getText().toString().trim() : "";
                    String qText = Q != null ? Q.getText().toString().trim() : "";
                    String gText = G != null ? G.getText().toString().trim() : "";
                    String hText = H != null ? H.getText().toString().trim() : "";
                    String dText = D != null ? D.getText().toString().trim() : "";

                    // 迟到扣款
                    if (!sText.isEmpty() && peizhi_list.get(0).getChidao_koukuan() != null
                            && !peizhi_list.get(0).getChidao_koukuan().isEmpty()) {
                        try {
                            chidao_koukuan = Float.parseFloat(sText) * Float.parseFloat(peizhi_list.get(0).getChidao_koukuan());
                            if (T != null) T.setText(String.valueOf(chidao_koukuan));
                        } catch (NumberFormatException e) {
                            if (T != null) T.setText("0");
                        }
                    }

                    // 加班费
                    if (!afText.isEmpty() && peizhi_list.get(0).getJiabanfei() != null
                            && !peizhi_list.get(0).getJiabanfei().isEmpty()) {
                        try {
                            jiabanfei = Float.parseFloat(afText) * Float.parseFloat(peizhi_list.get(0).getJiabanfei());
                            if (O != null) O.setText(String.valueOf(jiabanfei));
                        } catch (NumberFormatException e) {
                            if (O != null) O.setText("0");
                        }
                    }

                    // 缺勤扣款
                    if (!qText.isEmpty() && peizhi_list.get(0).getQueqin_koukuan() != null
                            && !peizhi_list.get(0).getQueqin_koukuan().isEmpty()) {
                        try {
                            queqin_koukuan = Float.parseFloat(qText) * Float.parseFloat(peizhi_list.get(0).getQueqin_koukuan());
                            if (RR != null) RR.setText(String.valueOf(queqin_koukuan));
                        } catch (NumberFormatException e) {
                            if (RR != null) RR.setText("0");
                        }
                    }

                    // 个人医疗
                    if (!gText.isEmpty() && peizhi_list.get(0).getGeren_yiliao() != null
                            && !peizhi_list.get(0).getGeren_yiliao().isEmpty()) {
                        try {
                            geren_yiliao = Float.parseFloat(gText) * Float.parseFloat(peizhi_list.get(0).getGeren_yiliao());
                            if (AL != null) AL.setText(String.valueOf(geren_yiliao));
                        } catch (NumberFormatException e) {
                            if (AL != null) AL.setText("0");
                        }
                    }

                    // 企业医疗
                    if (!gText.isEmpty() && peizhi_list.get(0).getQiye_yiliao() != null
                            && !peizhi_list.get(0).getQiye_yiliao().isEmpty()) {
                        try {
                            qiye_yiliao = Float.parseFloat(gText) * Float.parseFloat(peizhi_list.get(0).getQiye_yiliao());
                            if (AB != null) AB.setText(String.valueOf(qiye_yiliao));
                        } catch (NumberFormatException e) {
                            if (AB != null) AB.setText("0");
                        }
                    }

                    // 个人生育
                    if (!gText.isEmpty() && peizhi_list.get(0).getGeren_shengyu() != null
                            && !peizhi_list.get(0).getGeren_shengyu().isEmpty()) {
                        try {
                            geren_shengyu = Float.parseFloat(gText) * Float.parseFloat(peizhi_list.get(0).getGeren_shengyu());
                            if (AM != null) AM.setText(String.valueOf(geren_shengyu));
                        } catch (NumberFormatException e) {
                            if (AM != null) AM.setText("0");
                        }
                    }

                    // 企业生育
                    if (!gText.isEmpty() && peizhi_list.get(0).getQiye_shengyu() != null
                            && !peizhi_list.get(0).getQiye_shengyu().isEmpty()) {
                        try {
                            qiye_shengyu = Float.parseFloat(gText) * Float.parseFloat(peizhi_list.get(0).getQiye_shengyu());
                            if (AD != null) AD.setText(String.valueOf(qiye_shengyu));
                        } catch (NumberFormatException e) {
                            if (AD != null) AD.setText("0");
                        }
                    }

                    // 个人养老
                    if (!gText.isEmpty() && peizhi_list.get(0).getGeren_yanglao() != null
                            && !peizhi_list.get(0).getGeren_yanglao().isEmpty()) {
                        try {
                            geren_yanglao = Float.parseFloat(gText) * Float.parseFloat(peizhi_list.get(0).getGeren_yanglao());
                            if (AJ != null) AJ.setText(String.valueOf(geren_yanglao));
                        } catch (NumberFormatException e) {
                            if (AJ != null) AJ.setText("0");
                        }
                    }

                    // 企业养老
                    if (!gText.isEmpty() && peizhi_list.get(0).getQiye_yanglao() != null
                            && !peizhi_list.get(0).getQiye_yanglao().isEmpty()) {
                        try {
                            qiye_yanglao = Float.parseFloat(gText) * Float.parseFloat(peizhi_list.get(0).getQiye_yanglao());
                            if (Z != null) Z.setText(String.valueOf(qiye_yanglao));
                        } catch (NumberFormatException e) {
                            if (Z != null) Z.setText("0");
                        }
                    }

                    // 个人失业
                    if (!gText.isEmpty() && peizhi_list.get(0).getGeren_shiye() != null
                            && !peizhi_list.get(0).getGeren_shiye().isEmpty()) {
                        try {
                            geren_shiye = Float.parseFloat(gText) * Float.parseFloat(peizhi_list.get(0).getGeren_shiye());
                            if (AK != null) AK.setText(String.valueOf(geren_shiye));
                        } catch (NumberFormatException e) {
                            if (AK != null) AK.setText("0");
                        }
                    }

                    // 企业失业
                    if (!gText.isEmpty() && peizhi_list.get(0).getQiye_shiye() != null
                            && !peizhi_list.get(0).getQiye_shiye().isEmpty()) {
                        try {
                            qiye_shiye = Float.parseFloat(gText) * Float.parseFloat(peizhi_list.get(0).getQiye_shiye());
                            if (AA != null) AA.setText(String.valueOf(qiye_shiye));
                        } catch (NumberFormatException e) {
                            if (AA != null) AA.setText("0");
                        }
                    }

                    // 企业工伤
                    if (!gText.isEmpty() && peizhi_list.get(0).getQiye_gongshang() != null
                            && !peizhi_list.get(0).getQiye_gongshang().isEmpty()) {
                        try {
                            qiye_gongshang = Float.parseFloat(gText) * Float.parseFloat(peizhi_list.get(0).getQiye_gongshang());
                            if (AC != null) AC.setText(String.valueOf(qiye_gongshang));
                        } catch (NumberFormatException e) {
                            if (AC != null) AC.setText("0");
                        }
                    }

                    // 个人年金
                    if (!gText.isEmpty() && peizhi_list.get(0).getGeren_nianjin() != null
                            && !peizhi_list.get(0).getGeren_nianjin().isEmpty()) {
                        try {
                            geren_nianjin = Float.parseFloat(gText) * Float.parseFloat(peizhi_list.get(0).getGeren_nianjin());
                            if (AO != null) AO.setText(String.valueOf(geren_nianjin));
                        } catch (NumberFormatException e) {
                            if (AO != null) AO.setText("0");
                        }
                    }

                    // 企业年金
                    if (!gText.isEmpty() && peizhi_list.get(0).getQiye_nianjin() != null
                            && !peizhi_list.get(0).getQiye_nianjin().isEmpty()) {
                        try {
                            qiye_nianjin = Float.parseFloat(gText) * Float.parseFloat(peizhi_list.get(0).getQiye_nianjin());
                            if (AF != null) AF.setText(String.valueOf(qiye_nianjin));
                        } catch (NumberFormatException e) {
                            if (AF != null) AF.setText("0");
                        }
                    }

                    // 个人公积金
                    if (!gText.isEmpty() && peizhi_list.get(0).getGeren_gongjijin() != null
                            && !peizhi_list.get(0).getGeren_gongjijin().isEmpty()) {
                        try {
                            geren_gongjijin = Float.parseFloat(gText) * Float.parseFloat(peizhi_list.get(0).getGeren_gongjijin());
                            if (AN != null) AN.setText(String.valueOf(geren_gongjijin));
                        } catch (NumberFormatException e) {
                            if (AN != null) AN.setText("0");
                        }
                    }

                    // 企业公积金
                    if (!gText.isEmpty() && peizhi_list.get(0).getQiye_gongjijin() != null
                            && !peizhi_list.get(0).getQiye_gongjijin().isEmpty()) {
                        try {
                            qiye_gongjijin = Float.parseFloat(gText) * Float.parseFloat(peizhi_list.get(0).getQiye_gongjijin());
                            if (AE != null) AE.setText(String.valueOf(qiye_gongjijin));
                        } catch (NumberFormatException e) {
                            if (AE != null) AE.setText("0");
                        }
                    }

                    // 税前工资
                    if (!gText.isEmpty() && !hText.isEmpty()) {
                        try {
                            double totalGongzi = Float.parseFloat(gText) + Float.parseFloat(hText);
                            if (ASA != null) ASA.setText(String.valueOf(totalGongzi));
                        } catch (NumberFormatException e) {
                            if (ASA != null) ASA.setText("0");
                        }
                    }

                    // ⭐⭐⭐ 3. 遍历配置（添加安全检查）⭐⭐⭐
                    for (int i = 0; i < peizhi_list.size(); i++) {
                        try {
                            // 岗位工资
                            if (dText.equals(peizhi_list.get(i).getGangwei())) {
                                if (I != null) I.setText(peizhi_list.get(i).getGangwei_gongzi());
                            }

                            // 工资区间判断
                            String gongzi = peizhi_list.get(i).getGongzi();
                            if (gongzi != null && !gongzi.isEmpty() && gongzi.contains("-")) {
                                String[] range = gongzi.split("-");
                                if (range.length == 2) {  // ⭐⭐⭐ 防止数组越界 ⭐⭐⭐
                                    try {
                                        float lower = Float.parseFloat(range[0]);
                                        float upper = Float.parseFloat(range[1]);
                                        float totalSalary = Float.parseFloat(gText) + Float.parseFloat(hText);

                                        if (totalSalary >= lower && totalSalary <= upper) {
                                            String shuilv = peizhi_list.get(i).getShuilv();
                                            if (shuilv != null && !shuilv.isEmpty()) {
                                                try {
                                                    float rate = Float.parseFloat(shuilv);
                                                    yingshui_gongzi = totalSalary - totalSalary * rate;
                                                    if (ATA != null) ATA.setText(String.valueOf(yingshui_gongzi));
                                                    if (AU != null) AU.setText(shuilv);
                                                    daikou_geshui = totalSalary * rate;
                                                } catch (NumberFormatException e) {
                                                    if (ATA != null) ATA.setText("0");
                                                    if (AU != null) AU.setText("0");
                                                }
                                            }
                                        }
                                    } catch (NumberFormatException e) {
                                        // 忽略解析错误
                                    }
                                }
                            }
                        } catch (Exception e) {
                            // 忽略单个配置项的错误
                        }
                    }

                    // ⭐⭐⭐ 4. 最终计算（添加安全检查）⭐⭐⭐
                    try {
                        double personalTotal = geren_gongjijin + geren_nianjin + geren_shengyu +
                                geren_shiye + geren_yanglao + geren_yiliao +
                                (gText.isEmpty() ? 0 : Float.parseFloat(gText)) +
                                (hText.isEmpty() ? 0 : Float.parseFloat(hText));
                        if (AR != null) AR.setText(String.valueOf(personalTotal));

                        double companyTotal = qiye_gongjijin + qiye_gongshang + qiye_nianjin +
                                qiye_shengyu + qiye_shiye + qiye_yanglao + qiye_yiliao;
                        if (AI != null) AI.setText(String.valueOf(companyTotal));

                        double shifa = yingshui_gongzi - geren_gongjijin + geren_nianjin + geren_shengyu +
                                geren_shiye + geren_yanglao + geren_yiliao +
                                (gText.isEmpty() ? 0 : Float.parseFloat(gText)) +
                                (hText.isEmpty() ? 0 : Float.parseFloat(hText));
                        if (AY != null) AY.setText(String.valueOf(shifa));

                        shifa_gongzi = shifa;
                        if (U != null) U.setText(String.valueOf((gText.isEmpty() ? 0 : Float.parseFloat(gText)) +
                                (hText.isEmpty() ? 0 : Float.parseFloat(hText)) -
                                (geren_gongjijin + geren_nianjin + geren_shengyu + geren_shiye + geren_yanglao + geren_yiliao)));

                        if (J != null) J.setText(String.valueOf(shifa_gongzi + jiabanfei - queqin_koukuan - chidao_koukuan));
                        if (AW != null) AW.setText(String.valueOf(daikou_geshui));

                    } catch (NumberFormatException e) {
                        // 忽略解析错误
                    }

                } catch (Exception e) {
                    // ⭐⭐⭐ 5. 捕获所有异常，防止崩溃 ⭐⭐⭐
                    Log.e("JisuanClick", "计算异常", e);
                }

                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Message msg = new Message();
                    peizhi_list = yhRenShiPeiZhiBiaoService.getList(yhRenShiUser.getL());
                    saveHandler.sendMessage(msg);
                } catch (Exception e) {
                    Log.e("JisuanClick", "数据加载异常", e);
                }
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

    // ⭐⭐⭐ 安全方法：找不到返回-1 ⭐⭐⭐
    private int getNameSafe(String param) {
        if (name_array == null || param == null) return -1;
        for (int i = 0; i < name_array.size(); i++) {
            if (param.equals(name_array.get(i))) {
                return i;
            }
        }
        return -1;
    }

    private int getKuaDuSafe(String param) {
        if (kuadu_gongzi_array == null || param == null) return -1;
        for (int i = 0; i < kuadu_gongzi_array.size(); i++) {
            if (param.equals(kuadu_gongzi_array.get(i))) {
                return i;
            }
        }
        return -1;
    }

    private int getZhiChengSafe(String param) {
        if (zhicheng_jintie_array == null || param == null) return -1;
        for (int i = 0; i < zhicheng_jintie_array.size(); i++) {
            if (param.equals(zhicheng_jintie_array.get(i))) {
                return i;
            }
        }
        return -1;
    }

    private int getSheBaoSafe(String param) {
        if (shebao_jishu_array == null || param == null) return -1;
        for (int i = 0; i < shebao_jishu_array.size(); i++) {
            if (param.equals(shebao_jishu_array.get(i))) {
                return i;
            }
        }
        return -1;
    }

    private int getYiLiaoSafe(String param) {
        if (yiliao_jishu_array == null || param == null) return -1;
        for (int i = 0; i < yiliao_jishu_array.size(); i++) {
            if (param.equals(yiliao_jishu_array.get(i))) {
                return i;
            }
        }
        return -1;
    }

    private int getGongJiJinSafe(String param) {
        if (gongjijin_jishu_array == null || param == null) return -1;
        for (int i = 0; i < gongjijin_jishu_array.size(); i++) {
            if (param.equals(gongjijin_jishu_array.get(i))) {
                return i;
            }
        }
        return -1;
    }

    private int getNianJinSafe(String param) {
        if (nianjin_jinjishu_array == null || param == null) return -1;
        for (int i = 0; i < nianjin_jinjishu_array.size(); i++) {
            if (param.equals(nianjin_jinjishu_array.get(i))) {
                return i;
            }
        }
        return -1;
    }

    private int getZhiNaJinSafe(String param) {
        if (zhinajin_array == null || param == null) return -1;
        for (int i = 0; i < zhinajin_array.size(); i++) {
            if (param.equals(zhinajin_array.get(i))) {
                return i;
            }
        }
        return -1;
    }

    private int getLiXiSafe(String param) {
        if (lixi_array == null || param == null) return -1;
        for (int i = 0; i < lixi_array.size(); i++) {
            if (param.equals(lixi_array.get(i))) {
                return i;
            }
        }
        return -1;
    }

    private int getNianJin1Safe(String param) {
        if (nianjin1_array == null || param == null) return -1;
        for (int i = 0; i < nianjin1_array.size(); i++) {
            if (param.equals(nianjin1_array.get(i))) {
                return i;
            }
        }
        return -1;
    }

    private int getYanSuanSafe(String param) {
        if (yansuan_gongshi_array == null || param == null) return -1;
        for (int i = 0; i < yansuan_gongshi_array.size(); i++) {
            if (param.equals(yansuan_gongshi_array.get(i))) {
                return i;
            }
        }
        return -1;
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
        // 检查姓名 Spinner
        if (B == null || B.getSelectedItem() == null || B.getSelectedItem().toString().equals("")) {
            ToastUtil.show(GongZiMingXiChangeActivity.this, "请选择姓名");
            return false;
        } else {
            yhRenShiGongZiMingXi.setB(B.getSelectedItem().toString());
        }

        yhRenShiGongZiMingXi.setC(C.getText().toString());
        yhRenShiGongZiMingXi.setD(D.getText().toString());
        yhRenShiGongZiMingXi.setE(E.getText().toString());
        yhRenShiGongZiMingXi.setF(F.getText().toString());
        yhRenShiGongZiMingXi.setG(G.getText().toString());
        yhRenShiGongZiMingXi.setH(H.getText().toString());
        yhRenShiGongZiMingXi.setI(I.getText().toString());
        yhRenShiGongZiMingXi.setJ(J.getText().toString());

        // 检查跨渡工资 Spinner
        if (K != null && K.getSelectedItem() != null) {
            yhRenShiGongZiMingXi.setK(K.getSelectedItem().toString());
        } else {
            yhRenShiGongZiMingXi.setK("");
        }

        // 检查职称津贴 Spinner
        if (L != null && L.getSelectedItem() != null) {
            yhRenShiGongZiMingXi.setL(L.getSelectedItem().toString());
        } else {
            yhRenShiGongZiMingXi.setL("");
        }

        yhRenShiGongZiMingXi.setM(M.getText().toString());
        yhRenShiGongZiMingXi.setN(N.getText().toString());
        yhRenShiGongZiMingXi.setO(O.getText().toString());
        yhRenShiGongZiMingXi.setP(P.getText().toString());
        yhRenShiGongZiMingXi.setQ(Q.getText().toString());
        yhRenShiGongZiMingXi.setR(RR.getText().toString());
        yhRenShiGongZiMingXi.setS(S.getText().toString());
        yhRenShiGongZiMingXi.setT(T.getText().toString());
        yhRenShiGongZiMingXi.setU(U.getText().toString());

        // 检查社保基数 Spinner
        if (V != null && V.getSelectedItem() != null) {
            yhRenShiGongZiMingXi.setV(V.getSelectedItem().toString());
        } else {
            yhRenShiGongZiMingXi.setV("");
        }

        // 检查医疗基数 Spinner
        if (W != null && W.getSelectedItem() != null) {
            yhRenShiGongZiMingXi.setW(W.getSelectedItem().toString());
        } else {
            yhRenShiGongZiMingXi.setW("");
        }

        // 检查公积金基数 Spinner
        if (X != null && X.getSelectedItem() != null) {
            yhRenShiGongZiMingXi.setX(X.getSelectedItem().toString());
        } else {
            yhRenShiGongZiMingXi.setX("");
        }

        // 检查年金基数 Spinner
        if (Y != null && Y.getSelectedItem() != null) {
            yhRenShiGongZiMingXi.setY(Y.getSelectedItem().toString());
        } else {
            yhRenShiGongZiMingXi.setY("");
        }

        yhRenShiGongZiMingXi.setZ(Z.getText().toString());
        yhRenShiGongZiMingXi.setAa(AA.getText().toString());
        yhRenShiGongZiMingXi.setAb(AB.getText().toString());
        yhRenShiGongZiMingXi.setAc(AC.getText().toString());
        yhRenShiGongZiMingXi.setAd(AD.getText().toString());
        yhRenShiGongZiMingXi.setAe(AE.getText().toString());
        yhRenShiGongZiMingXi.setAf(AF.getText().toString());

        // 检查滞纳金 Spinner
        if (AG != null && AG.getSelectedItem() != null) {
            yhRenShiGongZiMingXi.setAg(AG.getSelectedItem().toString());
        } else {
            yhRenShiGongZiMingXi.setAg("");
        }

        // 检查利息 Spinner
        if (AH != null && AH.getSelectedItem() != null) {
            yhRenShiGongZiMingXi.setAh(AH.getSelectedItem().toString());
        } else {
            yhRenShiGongZiMingXi.setAh("");
        }

        yhRenShiGongZiMingXi.setAi(AI.getText().toString());
        yhRenShiGongZiMingXi.setAj(AJ.getText().toString());
        yhRenShiGongZiMingXi.setAk(AK.getText().toString());
        yhRenShiGongZiMingXi.setAl(AL.getText().toString());
        yhRenShiGongZiMingXi.setAm(AM.getText().toString());
        yhRenShiGongZiMingXi.setAn(AN.getText().toString());
        yhRenShiGongZiMingXi.setAo(AO.getText().toString());

        // 检查滞纳金2 Spinner
        if (AP != null && AP.getSelectedItem() != null) {
            yhRenShiGongZiMingXi.setAp(AP.getSelectedItem().toString());
        } else {
            yhRenShiGongZiMingXi.setAp("");
        }

        // 检查利息2 Spinner
        if (AQ != null && AQ.getSelectedItem() != null) {
            yhRenShiGongZiMingXi.setAq(AQ.getSelectedItem().toString());
        } else {
            yhRenShiGongZiMingXi.setAq("");
        }

        yhRenShiGongZiMingXi.setAr(AR.getText().toString());
        yhRenShiGongZiMingXi.setAsa(ASA.getText().toString());
        yhRenShiGongZiMingXi.setAta(ATA.getText().toString());
        yhRenShiGongZiMingXi.setAu(AU.getText().toString());
        yhRenShiGongZiMingXi.setAv(AV.getText().toString());
        yhRenShiGongZiMingXi.setAw(AW.getText().toString());

        // 检查年金1 Spinner
        if (AX != null && AX.getSelectedItem() != null) {
            yhRenShiGongZiMingXi.setAx(AX.getSelectedItem().toString());
        } else {
            yhRenShiGongZiMingXi.setAx("");
        }

        yhRenShiGongZiMingXi.setAy(AY.getText().toString());

        // 检查演算公式 Spinner
        if (AZ != null && AZ.getSelectedItem() != null) {
            yhRenShiGongZiMingXi.setAz(AZ.getSelectedItem().toString());
        } else {
            yhRenShiGongZiMingXi.setAz("");
        }

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
