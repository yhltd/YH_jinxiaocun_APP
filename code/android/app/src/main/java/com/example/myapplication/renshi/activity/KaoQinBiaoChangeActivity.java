package com.example.myapplication.renshi.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.finance.activity.FaPiaoChangeActivity;
import com.example.myapplication.renshi.entity.YhRenShiKaoQinJiLu;
import com.example.myapplication.renshi.entity.YhRenShiPeiZhiBiao;
import com.example.myapplication.renshi.entity.YhRenShiUser;
import com.example.myapplication.renshi.service.YhRenShiKaoQinJiLuService;
import com.example.myapplication.renshi.service.YhRenShiPeiZhiBiaoService;
import com.example.myapplication.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KaoQinBiaoChangeActivity extends AppCompatActivity {

    private YhRenShiUser yhRenShiUser;
    private YhRenShiKaoQinJiLu yhRenShiKaoQinJiLu;
    private YhRenShiKaoQinJiLuService yhRenShiKaoQinJiLuService;

    private EditText year;
    private EditText moth;
    private EditText name;
    private Spinner E;
    private Spinner F;
    private Spinner G;
    private Spinner H;
    private Spinner I;
    private Spinner J;
    private Spinner K;
    private Spinner L;
    private Spinner M;
    private Spinner N;
    private Spinner O;
    private Spinner P;
    private Spinner Q;
    private Spinner RR;
    private Spinner S;
    private Spinner T;
    private Spinner U;
    private Spinner V;
    private Spinner W;
    private Spinner X;
    private Spinner Y;
    private Spinner Z;
    private Spinner AA;
    private Spinner AB;
    private Spinner AC;
    private Spinner AD;
    private Spinner AE;
    private Spinner AF;
    private Spinner AG;
    private Spinner AH;
    private Spinner AI;
    private EditText AJ;
    private EditText AK;
    private EditText AL;
    private EditText AM;
    private EditText AN;

    String[] kaoqin_typeArray;




    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kaoqinbiao_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        yhRenShiUser = myApplication.getYhRenShiUser();
        yhRenShiKaoQinJiLuService = new YhRenShiKaoQinJiLuService();

        year = findViewById(R.id.year);
        moth = findViewById(R.id.moth);
        name = findViewById(R.id.name);
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

        kaoqin_typeArray = getResources().getStringArray(R.array.kaoqin_type_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, kaoqin_typeArray);
        E.setAdapter(adapter);
        F.setAdapter(adapter);
        G.setAdapter(adapter);
        H.setAdapter(adapter);
        I.setAdapter(adapter);
        J.setAdapter(adapter);
        K.setAdapter(adapter);
        L.setAdapter(adapter);
        M.setAdapter(adapter);
        N.setAdapter(adapter);
        O.setAdapter(adapter);
        P.setAdapter(adapter);
        Q.setAdapter(adapter);
        RR.setAdapter(adapter);
        S.setAdapter(adapter);
        T.setAdapter(adapter);
        U.setAdapter(adapter);
        V.setAdapter(adapter);
        W.setAdapter(adapter);
        X.setAdapter(adapter);
        Y.setAdapter(adapter);
        Z.setAdapter(adapter);
        AA.setAdapter(adapter);
        AB.setAdapter(adapter);
        AC.setAdapter(adapter);
        AD.setAdapter(adapter);
        AE.setAdapter(adapter);
        AF.setAdapter(adapter);
        AG.setAdapter(adapter);
        AH.setAdapter(adapter);
        AI.setAdapter(adapter);

        Intent intent = getIntent();
        int id = intent.getIntExtra("type", 0);
        if (id == R.id.insert_btn) {
            yhRenShiKaoQinJiLu = new YhRenShiKaoQinJiLu();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
            Calendar now = Calendar.getInstance();
            year.setText(now.get(Calendar.YEAR) + "");
            moth.setText((now.get(Calendar.MONTH) + 1) + "");
            AJ.setText("0");
            AK.setText("0");
            AL.setText("0");
            AM.setText("0");
            AN.setText("0");
        } else if (id == R.id.update_btn) {
            yhRenShiKaoQinJiLu = (YhRenShiKaoQinJiLu) myApplication.getObj();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);

            year.setText(yhRenShiKaoQinJiLu.getYear());
            moth.setText(yhRenShiKaoQinJiLu.getMoth());
            name.setText(yhRenShiKaoQinJiLu.getName());
            E.setSelection(getType(yhRenShiKaoQinJiLu.getE()));
            F.setSelection(getType(yhRenShiKaoQinJiLu.getF()));
            G.setSelection(getType(yhRenShiKaoQinJiLu.getG()));
            H.setSelection(getType(yhRenShiKaoQinJiLu.getH()));
            I.setSelection(getType(yhRenShiKaoQinJiLu.getI()));
            J.setSelection(getType(yhRenShiKaoQinJiLu.getJ()));
            K.setSelection(getType(yhRenShiKaoQinJiLu.getK()));
            L.setSelection(getType(yhRenShiKaoQinJiLu.getL()));
            M.setSelection(getType(yhRenShiKaoQinJiLu.getM()));
            N.setSelection(getType(yhRenShiKaoQinJiLu.getN()));
            O.setSelection(getType(yhRenShiKaoQinJiLu.getO()));
            P.setSelection(getType(yhRenShiKaoQinJiLu.getP()));
            Q.setSelection(getType(yhRenShiKaoQinJiLu.getQ()));
            RR.setSelection(getType(yhRenShiKaoQinJiLu.getR()));
            S.setSelection(getType(yhRenShiKaoQinJiLu.getS()));
            T.setSelection(getType(yhRenShiKaoQinJiLu.getT()));
            U.setSelection(getType(yhRenShiKaoQinJiLu.getU()));
            V.setSelection(getType(yhRenShiKaoQinJiLu.getV()));
            W.setSelection(getType(yhRenShiKaoQinJiLu.getW()));
            X.setSelection(getType(yhRenShiKaoQinJiLu.getX()));
            Y.setSelection(getType(yhRenShiKaoQinJiLu.getY()));
            Z.setSelection(getType(yhRenShiKaoQinJiLu.getZ()));
            AA.setSelection(getType(yhRenShiKaoQinJiLu.getAa()));
            AB.setSelection(getType(yhRenShiKaoQinJiLu.getAb()));
            AC.setSelection(getType(yhRenShiKaoQinJiLu.getAc()));
            AD.setSelection(getType(yhRenShiKaoQinJiLu.getAd()));
            AE.setSelection(getType(yhRenShiKaoQinJiLu.getAe()));
            AF.setSelection(getType(yhRenShiKaoQinJiLu.getAf()));
            AG.setSelection(getType(yhRenShiKaoQinJiLu.getAg()));
            AH.setSelection(getType(yhRenShiKaoQinJiLu.getAh()));
            AI.setSelection(getType(yhRenShiKaoQinJiLu.getAi()));
            AJ.setText(yhRenShiKaoQinJiLu.getAj());
            AK.setText(yhRenShiKaoQinJiLu.getAk());
            AL.setText(yhRenShiKaoQinJiLu.getAl());
            AM.setText(yhRenShiKaoQinJiLu.getAm());
            AN.setText(yhRenShiKaoQinJiLu.getAn());


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
                    ToastUtil.show(KaoQinBiaoChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(KaoQinBiaoChangeActivity.this, "保存失败，请稍后再试");
                }

                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = yhRenShiKaoQinJiLuService.insert(yhRenShiKaoQinJiLu);
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
                    ToastUtil.show(KaoQinBiaoChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(KaoQinBiaoChangeActivity.this, "保存失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = yhRenShiKaoQinJiLuService.update(yhRenShiKaoQinJiLu);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    private int getType(String param) {
        if (kaoqin_typeArray != null) {
            for (int i = 0; i < kaoqin_typeArray.length; i++) {
                if (param.equals(kaoqin_typeArray[i])) {
                    return i;
                }
            }
        }
        return 0;
    }


    private boolean checkForm() {
        if ( year.getText().toString().equals("") || year.getText().toString().length() != 4) {
            ToastUtil.show(KaoQinBiaoChangeActivity.this, "年份不符合规则");
        }else{
            yhRenShiKaoQinJiLu.setYear(year.getText().toString());
        }
        if ( moth.getText().toString().equals("") || moth.getText().toString().length() < 1 || moth.getText().toString().length() > 2) {
            ToastUtil.show(KaoQinBiaoChangeActivity.this, "月份不符合规则");
        }else{
            int this_month = Integer.parseInt(moth.getText().toString());
            if(this_month < 1 || this_month > 12){
                ToastUtil.show(KaoQinBiaoChangeActivity.this, "月份不符合规则");
            }else{
                if(moth.getText().toString().length() == 1){
                    yhRenShiKaoQinJiLu.setMoth("0" + moth.getText().toString());
                }else{
                    yhRenShiKaoQinJiLu.setMoth(moth.getText().toString());
                }
            }
        }

        if (name.getText().toString().equals("")) {
            ToastUtil.show(KaoQinBiaoChangeActivity.this, "请输入姓名");
            return false;
        } else {
            yhRenShiKaoQinJiLu.setName(name.getText().toString());
        }

        yhRenShiKaoQinJiLu.setE(E.getSelectedItem().toString());
        yhRenShiKaoQinJiLu.setF(F.getSelectedItem().toString());
        yhRenShiKaoQinJiLu.setG(G.getSelectedItem().toString());
        yhRenShiKaoQinJiLu.setH(H.getSelectedItem().toString());
        yhRenShiKaoQinJiLu.setI(I.getSelectedItem().toString());
        yhRenShiKaoQinJiLu.setJ(J.getSelectedItem().toString());
        yhRenShiKaoQinJiLu.setK(K.getSelectedItem().toString());
        yhRenShiKaoQinJiLu.setL(L.getSelectedItem().toString());
        yhRenShiKaoQinJiLu.setM(M.getSelectedItem().toString());
        yhRenShiKaoQinJiLu.setN(N.getSelectedItem().toString());
        yhRenShiKaoQinJiLu.setO(O.getSelectedItem().toString());
        yhRenShiKaoQinJiLu.setP(P.getSelectedItem().toString());
        yhRenShiKaoQinJiLu.setQ(Q.getSelectedItem().toString());
        yhRenShiKaoQinJiLu.setR(RR.getSelectedItem().toString());
        yhRenShiKaoQinJiLu.setS(S.getSelectedItem().toString());
        yhRenShiKaoQinJiLu.setT(T.getSelectedItem().toString());
        yhRenShiKaoQinJiLu.setU(U.getSelectedItem().toString());
        yhRenShiKaoQinJiLu.setV(V.getSelectedItem().toString());
        yhRenShiKaoQinJiLu.setW(W.getSelectedItem().toString());
        yhRenShiKaoQinJiLu.setX(X.getSelectedItem().toString());
        yhRenShiKaoQinJiLu.setY(Y.getSelectedItem().toString());
        yhRenShiKaoQinJiLu.setZ(Z.getSelectedItem().toString());
        yhRenShiKaoQinJiLu.setAa(AA.getSelectedItem().toString());
        yhRenShiKaoQinJiLu.setAb(AB.getSelectedItem().toString());
        yhRenShiKaoQinJiLu.setAc(AC.getSelectedItem().toString());
        yhRenShiKaoQinJiLu.setAd(AD.getSelectedItem().toString());
        yhRenShiKaoQinJiLu.setAe(AE.getSelectedItem().toString());
        yhRenShiKaoQinJiLu.setAf(AF.getSelectedItem().toString());
        yhRenShiKaoQinJiLu.setAg(AG.getSelectedItem().toString());
        yhRenShiKaoQinJiLu.setAh(AH.getSelectedItem().toString());
        yhRenShiKaoQinJiLu.setAi(AI.getSelectedItem().toString());
        yhRenShiKaoQinJiLu.setAj(AJ.getText().toString());
        yhRenShiKaoQinJiLu.setAk(AK.getText().toString());
        yhRenShiKaoQinJiLu.setAl(AL.getText().toString());
        yhRenShiKaoQinJiLu.setAm(AM.getText().toString());
        yhRenShiKaoQinJiLu.setAn(AN.getText().toString());
        yhRenShiKaoQinJiLu.setAo(yhRenShiUser.getL().replace("_hr",""));
        return true;
    }

    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }

}
