package com.example.myapplication.fenquan.activity;

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
import android.widget.SpinnerAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.fenquan.entity.Copy1;
import com.example.myapplication.fenquan.entity.Department;
import com.example.myapplication.fenquan.entity.Renyuan;
import com.example.myapplication.fenquan.service.Copy1Service;
import com.example.myapplication.fenquan.service.DepartmentService;
import com.example.myapplication.fenquan.service.RenyuanService;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RenyuanQuanXianChangeActivity extends AppCompatActivity {
    private Renyuan renyuan;
    private Copy1 copy1;
    private Copy1Service copy1Service;

    private Spinner C;
    private Spinner D;
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
    private Spinner AJ;
    private Spinner AK;
    private Spinner AL;
    private Spinner AM;
    private Spinner AN;
    private Spinner AO;
    private Spinner AP;
    private Spinner AQ;
    private Spinner AR;
    private Spinner ASS;
    private Spinner AT;
    private Spinner AU;
    private Spinner AV;
    private Spinner AW;
    private Spinner AX;
    private Spinner AY;
    private Spinner AZ;

    private Spinner BA;
    private Spinner BB;
    private Spinner BC;
    private Spinner BD;
    private Spinner BE;
    private Spinner BF;
    private Spinner BG;
    private Spinner BH;
    private Spinner BI;
    private Spinner BJ;
    private Spinner BK;
    private Spinner BL;
    private Spinner BM;
    private Spinner BN;
    private Spinner BO;
    private Spinner BP;
    private Spinner BQ;
    private Spinner BR;
    private Spinner BS;
    private Spinner BT;
    private Spinner BU;
    private Spinner BV;
    private Spinner BW;
    private Spinner BX;
    private Spinner BYY;
    private Spinner BZ;

    private Spinner CA;
    private Spinner CB;
    private Spinner CC;
    private Spinner CD;
    private Spinner CE;
    private Spinner CF;
    private Spinner CG;
    private Spinner CH;
    private Spinner CI;
    private Spinner CJ;
    private Spinner CK;
    private Spinner CL;
    private Spinner CM;
    private Spinner CN;
    private Spinner CO;
    private Spinner CP;
    private Spinner CQ;
    private Spinner CR;
    private Spinner CS;
    private Spinner CT;
    private Spinner CU;
    private Spinner CV;
    private Spinner CW;
    private Spinner CX;

    String[] quanxian_selArray;

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.renyuan_quanxian_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        copy1Service = new Copy1Service();

        MyApplication myApplication = (MyApplication) getApplication();
        renyuan = myApplication.getRenyuan();

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
        ASS = findViewById(R.id.ASS);
        AT = findViewById(R.id.AT);
        AU = findViewById(R.id.AU);
        AV = findViewById(R.id.AV);
        AW = findViewById(R.id.AW);
        AX = findViewById(R.id.AX);
        AY = findViewById(R.id.AY);
        AZ = findViewById(R.id.AZ);
        BA = findViewById(R.id.BA);
        BB = findViewById(R.id.BB);
        BC = findViewById(R.id.BC);
        BD = findViewById(R.id.BD);
        BE = findViewById(R.id.BE);
        BF = findViewById(R.id.BF);
        BG = findViewById(R.id.BG);
        BH = findViewById(R.id.BH);
        BI = findViewById(R.id.BI);
        BJ = findViewById(R.id.BJ);
        BK = findViewById(R.id.BK);
        BL = findViewById(R.id.BL);
        BM = findViewById(R.id.BM);
        BN = findViewById(R.id.BN);
        BO = findViewById(R.id.BO);
        BP = findViewById(R.id.BP);
        BQ = findViewById(R.id.BQ);
        BR = findViewById(R.id.BR);
        BS = findViewById(R.id.BS);
        BT = findViewById(R.id.BT);
        BU = findViewById(R.id.BU);
        BV = findViewById(R.id.BV);
        BW = findViewById(R.id.BW);
        BX = findViewById(R.id.BX);
        BYY = findViewById(R.id.BYY);
        BZ = findViewById(R.id.BZ);
        CA = findViewById(R.id.CA);
        CB = findViewById(R.id.CB);
        CC = findViewById(R.id.CC);
        CD = findViewById(R.id.CD);
        CE = findViewById(R.id.CE);
        CF = findViewById(R.id.CF);
        CG = findViewById(R.id.CG);
        CH = findViewById(R.id.CH);
        CI = findViewById(R.id.CI);
        CJ = findViewById(R.id.CJ);
        CK = findViewById(R.id.CK);
        CL = findViewById(R.id.CL);
        CM = findViewById(R.id.CM);
        CN = findViewById(R.id.CN);
        CO = findViewById(R.id.CO);
        CP = findViewById(R.id.CP);
        CQ = findViewById(R.id.CQ);
        CR = findViewById(R.id.CR);
        CS = findViewById(R.id.CS);
        CT = findViewById(R.id.CT);
        CU = findViewById(R.id.CU);
        CV = findViewById(R.id.CV);
        CW = findViewById(R.id.CW);
        CX = findViewById(R.id.CX);

        quanxian_selArray = getResources().getStringArray(R.array.quanxian_select_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, quanxian_selArray);
        C.setAdapter(adapter);
        D.setAdapter(adapter);
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
        AJ.setAdapter(adapter);
        AK.setAdapter(adapter);
        AL.setAdapter(adapter);
        AM.setAdapter(adapter);
        AN.setAdapter(adapter);
        AO.setAdapter(adapter);
        AP.setAdapter(adapter);
        AQ.setAdapter(adapter);
        AR.setAdapter(adapter);
        ASS.setAdapter(adapter);
        AT.setAdapter(adapter);
        AU.setAdapter(adapter);
        AV.setAdapter(adapter);
        AW.setAdapter(adapter);
        AX.setAdapter(adapter);
        AY.setAdapter(adapter);
        AZ.setAdapter(adapter);

        BA.setAdapter(adapter);
        BB.setAdapter(adapter);
        BC.setAdapter(adapter);
        BD.setAdapter(adapter);
        BE.setAdapter(adapter);
        BF.setAdapter(adapter);
        BG.setAdapter(adapter);
        BH.setAdapter(adapter);
        BI.setAdapter(adapter);
        BJ.setAdapter(adapter);
        BK.setAdapter(adapter);
        BL.setAdapter(adapter);
        BM.setAdapter(adapter);
        BN.setAdapter(adapter);
        BO.setAdapter(adapter);
        BP.setAdapter(adapter);
        BQ.setAdapter(adapter);
        BR.setAdapter(adapter);
        BS.setAdapter(adapter);
        BT.setAdapter(adapter);
        BU.setAdapter(adapter);
        BV.setAdapter(adapter);
        BW.setAdapter(adapter);
        BX.setAdapter(adapter);
        BYY.setAdapter(adapter);
        BZ.setAdapter(adapter);

        CA.setAdapter(adapter);
        CB.setAdapter(adapter);
        CC.setAdapter(adapter);
        CD.setAdapter(adapter);
        CE.setAdapter(adapter);
        CF.setAdapter(adapter);
        CG.setAdapter(adapter);
        CH.setAdapter(adapter);
        CI.setAdapter(adapter);
        CJ.setAdapter(adapter);
        CK.setAdapter(adapter);
        CL.setAdapter(adapter);
        CM.setAdapter(adapter);
        CN.setAdapter(adapter);
        CO.setAdapter(adapter);
        CP.setAdapter(adapter);
        CQ.setAdapter(adapter);
        CR.setAdapter(adapter);
        CS.setAdapter(adapter);
        CT.setAdapter(adapter);
        CU.setAdapter(adapter);
        CV.setAdapter(adapter);
        CW.setAdapter(adapter);
        CX.setAdapter(adapter);

        Intent intent = getIntent();
        int id = intent.getIntExtra("type", 0);
        if (id == R.id.insert_btn) {
            copy1 = new Copy1();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
        } else if (id == R.id.update_btn) {
            copy1 = (Copy1) myApplication.getObj();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
            C.setSelection(getSelectPosition(copy1.getC()));
            D.setSelection(getSelectPosition(copy1.getD()));
            E.setSelection(getSelectPosition(copy1.getE()));
            F.setSelection(getSelectPosition(copy1.getF()));
            G.setSelection(getSelectPosition(copy1.getG()));
            H.setSelection(getSelectPosition(copy1.getH()));
            I.setSelection(getSelectPosition(copy1.getI()));
            J.setSelection(getSelectPosition(copy1.getJ()));
            K.setSelection(getSelectPosition(copy1.getK()));
            L.setSelection(getSelectPosition(copy1.getL()));
            M.setSelection(getSelectPosition(copy1.getM()));
            N.setSelection(getSelectPosition(copy1.getN()));
            O.setSelection(getSelectPosition(copy1.getO()));
            P.setSelection(getSelectPosition(copy1.getP()));
            Q.setSelection(getSelectPosition(copy1.getQ()));
            RR.setSelection(getSelectPosition(copy1.getR()));
            S.setSelection(getSelectPosition(copy1.getS()));
            T.setSelection(getSelectPosition(copy1.getT()));
            U.setSelection(getSelectPosition(copy1.getU()));
            V.setSelection(getSelectPosition(copy1.getV()));
            W.setSelection(getSelectPosition(copy1.getW()));
            X.setSelection(getSelectPosition(copy1.getX()));
            Y.setSelection(getSelectPosition(copy1.getY()));
            Z.setSelection(getSelectPosition(copy1.getZ()));

            AA.setSelection(getSelectPosition(copy1.getAa()));
            AB.setSelection(getSelectPosition(copy1.getAb()));
            AC.setSelection(getSelectPosition(copy1.getAc()));
            AD.setSelection(getSelectPosition(copy1.getAd()));
            AE.setSelection(getSelectPosition(copy1.getAe()));
            AF.setSelection(getSelectPosition(copy1.getAf()));
            AG.setSelection(getSelectPosition(copy1.getAg()));
            AH.setSelection(getSelectPosition(copy1.getAh()));
            AI.setSelection(getSelectPosition(copy1.getAi()));
            AJ.setSelection(getSelectPosition(copy1.getAj()));
            AK.setSelection(getSelectPosition(copy1.getAk()));
            AL.setSelection(getSelectPosition(copy1.getAl()));
            AM.setSelection(getSelectPosition(copy1.getAm()));
            AN.setSelection(getSelectPosition(copy1.getAn()));
            AO.setSelection(getSelectPosition(copy1.getAo()));
            AP.setSelection(getSelectPosition(copy1.getAp()));
            AQ.setSelection(getSelectPosition(copy1.getAq()));
            AR.setSelection(getSelectPosition(copy1.getAr()));
            ASS.setSelection(getSelectPosition(copy1.getAss()));
            AT.setSelection(getSelectPosition(copy1.getAt()));
            AU.setSelection(getSelectPosition(copy1.getAu()));
            AV.setSelection(getSelectPosition(copy1.getAv()));
            AW.setSelection(getSelectPosition(copy1.getAw()));
            AX.setSelection(getSelectPosition(copy1.getAx()));
            AY.setSelection(getSelectPosition(copy1.getAy()));
            AZ.setSelection(getSelectPosition(copy1.getAz()));

            BA.setSelection(getSelectPosition(copy1.getBa()));
            BB.setSelection(getSelectPosition(copy1.getBb()));
            BC.setSelection(getSelectPosition(copy1.getBc()));
            BD.setSelection(getSelectPosition(copy1.getBd()));
            BE.setSelection(getSelectPosition(copy1.getBe()));
            BF.setSelection(getSelectPosition(copy1.getBf()));
            BG.setSelection(getSelectPosition(copy1.getBg()));
            BH.setSelection(getSelectPosition(copy1.getBh()));
            BI.setSelection(getSelectPosition(copy1.getBi()));
            BJ.setSelection(getSelectPosition(copy1.getBj()));
            BK.setSelection(getSelectPosition(copy1.getBk()));
            BL.setSelection(getSelectPosition(copy1.getBl()));
            BM.setSelection(getSelectPosition(copy1.getBm()));
            BN.setSelection(getSelectPosition(copy1.getBn()));
            BO.setSelection(getSelectPosition(copy1.getBo()));
            BP.setSelection(getSelectPosition(copy1.getBp()));
            BQ.setSelection(getSelectPosition(copy1.getBq()));
            BR.setSelection(getSelectPosition(copy1.getBr()));
            BS.setSelection(getSelectPosition(copy1.getBs()));
            BT.setSelection(getSelectPosition(copy1.getBt()));
            BU.setSelection(getSelectPosition(copy1.getBu()));
            BV.setSelection(getSelectPosition(copy1.getBv()));
            BW.setSelection(getSelectPosition(copy1.getBw()));
            BX.setSelection(getSelectPosition(copy1.getBx()));
            BYY.setSelection(getSelectPosition(copy1.getByy()));
            BZ.setSelection(getSelectPosition(copy1.getBz()));

            CA.setSelection(getSelectPosition(copy1.getCa()));
            CB.setSelection(getSelectPosition(copy1.getCb()));
            CC.setSelection(getSelectPosition(copy1.getCc()));
            CD.setSelection(getSelectPosition(copy1.getCd()));
            CE.setSelection(getSelectPosition(copy1.getCe()));
            CF.setSelection(getSelectPosition(copy1.getCf()));
            CG.setSelection(getSelectPosition(copy1.getCg()));
            CH.setSelection(getSelectPosition(copy1.getCh()));
            CI.setSelection(getSelectPosition(copy1.getCi()));
            CJ.setSelection(getSelectPosition(copy1.getCj()));
            CK.setSelection(getSelectPosition(copy1.getCk()));
            CL.setSelection(getSelectPosition(copy1.getCl()));
            CM.setSelection(getSelectPosition(copy1.getCm()));
            CN.setSelection(getSelectPosition(copy1.getCn()));
            CO.setSelection(getSelectPosition(copy1.getCo()));
            CP.setSelection(getSelectPosition(copy1.getCp()));
            CQ.setSelection(getSelectPosition(copy1.getCq()));
            CR.setSelection(getSelectPosition(copy1.getCr()));
            CS.setSelection(getSelectPosition(copy1.getCs()));
            CT.setSelection(getSelectPosition(copy1.getCt()));
            CU.setSelection(getSelectPosition(copy1.getCu()));
            CV.setSelection(getSelectPosition(copy1.getCv()));
            CW.setSelection(getSelectPosition(copy1.getCw()));
            CX.setSelection(getSelectPosition(copy1.getCx()));
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

    public void updateClick(View v) {
        if (!checkForm()) return;

        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(RenyuanQuanXianChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(RenyuanQuanXianChangeActivity.this, "保存失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = copy1Service.update(copy1);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    private boolean checkForm() {
        copy1.setC(C.getSelectedItem().toString());
        copy1.setD(D.getSelectedItem().toString());
        copy1.setE(E.getSelectedItem().toString());
        copy1.setF(F.getSelectedItem().toString());
        copy1.setG(G.getSelectedItem().toString());
        copy1.setH(H.getSelectedItem().toString());
        copy1.setI(I.getSelectedItem().toString());
        copy1.setJ(J.getSelectedItem().toString());
        copy1.setK(K.getSelectedItem().toString());
        copy1.setL(L.getSelectedItem().toString());
        copy1.setM(M.getSelectedItem().toString());
        copy1.setN(N.getSelectedItem().toString());
        copy1.setO(O.getSelectedItem().toString());
        copy1.setP(P.getSelectedItem().toString());
        copy1.setQ(Q.getSelectedItem().toString());
        copy1.setR(RR.getSelectedItem().toString());
        copy1.setS(S.getSelectedItem().toString());
        copy1.setT(T.getSelectedItem().toString());
        copy1.setU(U.getSelectedItem().toString());
        copy1.setV(V.getSelectedItem().toString());
        copy1.setW(W.getSelectedItem().toString());
        copy1.setX(X.getSelectedItem().toString());
        copy1.setY(Y.getSelectedItem().toString());
        copy1.setZ(Z.getSelectedItem().toString());

        copy1.setAa(AA.getSelectedItem().toString());
        copy1.setAb(AB.getSelectedItem().toString());
        copy1.setAc(AC.getSelectedItem().toString());
        copy1.setAd(AD.getSelectedItem().toString());
        copy1.setAe(AE.getSelectedItem().toString());
        copy1.setAf(AF.getSelectedItem().toString());
        copy1.setAg(AG.getSelectedItem().toString());
        copy1.setAh(AH.getSelectedItem().toString());
        copy1.setAi(AI.getSelectedItem().toString());
        copy1.setAj(AJ.getSelectedItem().toString());
        copy1.setAk(AK.getSelectedItem().toString());
        copy1.setAl(AL.getSelectedItem().toString());
        copy1.setAm(AM.getSelectedItem().toString());
        copy1.setAn(AN.getSelectedItem().toString());
        copy1.setAo(AO.getSelectedItem().toString());
        copy1.setAp(AP.getSelectedItem().toString());
        copy1.setAq(AQ.getSelectedItem().toString());
        copy1.setAr(AR.getSelectedItem().toString());
        copy1.setAss(ASS.getSelectedItem().toString());
        copy1.setAt(AT.getSelectedItem().toString());
        copy1.setAu(AU.getSelectedItem().toString());
        copy1.setAv(AV.getSelectedItem().toString());
        copy1.setAw(AW.getSelectedItem().toString());
        copy1.setAx(AX.getSelectedItem().toString());
        copy1.setAy(AY.getSelectedItem().toString());
        copy1.setAz(AZ.getSelectedItem().toString());

        copy1.setBa(BA.getSelectedItem().toString());
        copy1.setBb(BB.getSelectedItem().toString());
        copy1.setBc(BC.getSelectedItem().toString());
        copy1.setBd(BD.getSelectedItem().toString());
        copy1.setBe(BE.getSelectedItem().toString());
        copy1.setBf(BF.getSelectedItem().toString());
        copy1.setBg(BG.getSelectedItem().toString());
        copy1.setBh(BH.getSelectedItem().toString());
        copy1.setBi(BI.getSelectedItem().toString());
        copy1.setBj(BJ.getSelectedItem().toString());
        copy1.setBk(BK.getSelectedItem().toString());
        copy1.setBl(BL.getSelectedItem().toString());
        copy1.setBm(BM.getSelectedItem().toString());
        copy1.setBn(BN.getSelectedItem().toString());
        copy1.setBo(BO.getSelectedItem().toString());
        copy1.setBp(BP.getSelectedItem().toString());
        copy1.setBq(BQ.getSelectedItem().toString());
        copy1.setBr(BR.getSelectedItem().toString());
        copy1.setBs(BS.getSelectedItem().toString());
        copy1.setBt(BT.getSelectedItem().toString());
        copy1.setBu(BU.getSelectedItem().toString());
        copy1.setBv(BV.getSelectedItem().toString());
        copy1.setBw(BW.getSelectedItem().toString());
        copy1.setBx(BX.getSelectedItem().toString());
        copy1.setByy(BYY.getSelectedItem().toString());
        copy1.setBz(BZ.getSelectedItem().toString());

        copy1.setCa(CA.getSelectedItem().toString());
        copy1.setCb(CB.getSelectedItem().toString());
        copy1.setCc(CC.getSelectedItem().toString());
        copy1.setCd(CD.getSelectedItem().toString());
        copy1.setCe(CE.getSelectedItem().toString());
        copy1.setCf(CF.getSelectedItem().toString());
        copy1.setCg(CG.getSelectedItem().toString());
        copy1.setCh(CH.getSelectedItem().toString());
        copy1.setCi(CI.getSelectedItem().toString());
        copy1.setCj(CJ.getSelectedItem().toString());
        copy1.setCk(CK.getSelectedItem().toString());
        copy1.setCl(CL.getSelectedItem().toString());
        copy1.setCm(CM.getSelectedItem().toString());
        copy1.setCn(CN.getSelectedItem().toString());
        copy1.setCo(CO.getSelectedItem().toString());
        copy1.setCp(CP.getSelectedItem().toString());
        copy1.setCq(CQ.getSelectedItem().toString());
        copy1.setCr(CR.getSelectedItem().toString());
        copy1.setCs(CS.getSelectedItem().toString());
        copy1.setCt(CT.getSelectedItem().toString());
        copy1.setCu(CU.getSelectedItem().toString());
        copy1.setCv(CV.getSelectedItem().toString());
        copy1.setCw(CW.getSelectedItem().toString());
        copy1.setCx(CX.getSelectedItem().toString());

        return true;
    }

    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }

    private int getSelectPosition(String param) {
        if (quanxian_selArray != null) {
            for (int i = 0; i < quanxian_selArray.length; i++) {
                if (param.equals(quanxian_selArray[i])) {
                    return i;
                }
            }
        }
        return 0;
    }

}
