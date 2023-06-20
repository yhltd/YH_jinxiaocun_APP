package com.example.myapplication.fenquan.activity;

import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.fenquan.entity.Department;
import com.example.myapplication.fenquan.entity.Gongsi;
import com.example.myapplication.fenquan.entity.Renyuan;
import com.example.myapplication.fenquan.service.GongsiService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GongZuoTaiQuanXianChangeActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_CHANG = 1;
    private Renyuan renyuan;
    private GongsiService gongsiService;
    private Gongsi gongsi;

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
    private Button jiechuClick;

    private List<Gongsi> list;
    String[] quanxian_typeArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gongzuotai_quanxian_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        gongsiService = new GongsiService();

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

        quanxian_typeArray = getResources().getStringArray(R.array.quanxian_select_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, quanxian_typeArray);

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

        gongsi = new Gongsi();

        MyApplication myApplication = (MyApplication) getApplication();
        renyuan = myApplication.getRenyuan();
        initList();
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
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if(list != null){
                    C.setSelection(getQuanXianTypePosition(list.get(0).getC()));
                    D.setSelection(getQuanXianTypePosition(list.get(0).getD()));
                    E.setSelection(getQuanXianTypePosition(list.get(0).getE()));
                    F.setSelection(getQuanXianTypePosition(list.get(0).getF()));
                    G.setSelection(getQuanXianTypePosition(list.get(0).getG()));
                    H.setSelection(getQuanXianTypePosition(list.get(0).getH()));
                    I.setSelection(getQuanXianTypePosition(list.get(0).getI()));
                    J.setSelection(getQuanXianTypePosition(list.get(0).getJ()));
                    K.setSelection(getQuanXianTypePosition(list.get(0).getK()));
                    L.setSelection(getQuanXianTypePosition(list.get(0).getL()));
                    M.setSelection(getQuanXianTypePosition(list.get(0).getM()));
                    N.setSelection(getQuanXianTypePosition(list.get(0).getN()));
                    O.setSelection(getQuanXianTypePosition(list.get(0).getO()));
                    P.setSelection(getQuanXianTypePosition(list.get(0).getP()));
                    Q.setSelection(getQuanXianTypePosition(list.get(0).getQ()));
                    RR.setSelection(getQuanXianTypePosition(list.get(0).getR()));
                    S.setSelection(getQuanXianTypePosition(list.get(0).getS()));
                    T.setSelection(getQuanXianTypePosition(list.get(0).getT()));
                    U.setSelection(getQuanXianTypePosition(list.get(0).getU()));
                    V.setSelection(getQuanXianTypePosition(list.get(0).getV()));
                    W.setSelection(getQuanXianTypePosition(list.get(0).getW()));
                    X.setSelection(getQuanXianTypePosition(list.get(0).getX()));
                    Y.setSelection(getQuanXianTypePosition(list.get(0).getY()));
                    Z.setSelection(getQuanXianTypePosition(list.get(0).getZ()));
                    AA.setSelection(getQuanXianTypePosition(list.get(0).getAa()));
                    AB.setSelection(getQuanXianTypePosition(list.get(0).getAb()));
                    AC.setSelection(getQuanXianTypePosition(list.get(0).getAc()));
                    AD.setSelection(getQuanXianTypePosition(list.get(0).getAd()));
                    AE.setSelection(getQuanXianTypePosition(list.get(0).getAe()));
                    AF.setSelection(getQuanXianTypePosition(list.get(0).getAf()));
                    AG.setSelection(getQuanXianTypePosition(list.get(0).getAg()));
                    AH.setSelection(getQuanXianTypePosition(list.get(0).getAh()));
                    AI.setSelection(getQuanXianTypePosition(list.get(0).getAi()));
                    AJ.setSelection(getQuanXianTypePosition(list.get(0).getAj()));
                    AK.setSelection(getQuanXianTypePosition(list.get(0).getAk()));
                    AL.setSelection(getQuanXianTypePosition(list.get(0).getAl()));
                    AM.setSelection(getQuanXianTypePosition(list.get(0).getAm()));
                    AN.setSelection(getQuanXianTypePosition(list.get(0).getAn()));
                    AO.setSelection(getQuanXianTypePosition(list.get(0).getAo()));
                    AP.setSelection(getQuanXianTypePosition(list.get(0).getAp()));
                    AQ.setSelection(getQuanXianTypePosition(list.get(0).getAq()));
                    AR.setSelection(getQuanXianTypePosition(list.get(0).getAr()));
                    ASS.setSelection(getQuanXianTypePosition(list.get(0).getAss()));
                    AT.setSelection(getQuanXianTypePosition(list.get(0).getAt()));
                    AU.setSelection(getQuanXianTypePosition(list.get(0).getAu()));
                    AV.setSelection(getQuanXianTypePosition(list.get(0).getAv()));
                    AW.setSelection(getQuanXianTypePosition(list.get(0).getAw()));
                    AX.setSelection(getQuanXianTypePosition(list.get(0).getAx()));
                    AY.setSelection(getQuanXianTypePosition(list.get(0).getAy()));
                    AZ.setSelection(getQuanXianTypePosition(list.get(0).getAz()));
                    BA.setSelection(getQuanXianTypePosition(list.get(0).getBa()));
                    BB.setSelection(getQuanXianTypePosition(list.get(0).getBb()));
                    BC.setSelection(getQuanXianTypePosition(list.get(0).getBc()));
                    BD.setSelection(getQuanXianTypePosition(list.get(0).getBd()));
                    BE.setSelection(getQuanXianTypePosition(list.get(0).getBe()));
                    BF.setSelection(getQuanXianTypePosition(list.get(0).getBf()));
                    BG.setSelection(getQuanXianTypePosition(list.get(0).getBg()));
                    BH.setSelection(getQuanXianTypePosition(list.get(0).getBh()));
                    BI.setSelection(getQuanXianTypePosition(list.get(0).getBi()));
                    BJ.setSelection(getQuanXianTypePosition(list.get(0).getBj()));
                    BK.setSelection(getQuanXianTypePosition(list.get(0).getBk()));
                    BL.setSelection(getQuanXianTypePosition(list.get(0).getBl()));
                    BM.setSelection(getQuanXianTypePosition(list.get(0).getBm()));
                    BN.setSelection(getQuanXianTypePosition(list.get(0).getBn()));
                    BO.setSelection(getQuanXianTypePosition(list.get(0).getBo()));
                    BP.setSelection(getQuanXianTypePosition(list.get(0).getBp()));
                    BQ.setSelection(getQuanXianTypePosition(list.get(0).getBq()));
                    BR.setSelection(getQuanXianTypePosition(list.get(0).getBr()));
                    BS.setSelection(getQuanXianTypePosition(list.get(0).getBs()));
                    BT.setSelection(getQuanXianTypePosition(list.get(0).getBt()));
                    BU.setSelection(getQuanXianTypePosition(list.get(0).getBu()));
                    BV.setSelection(getQuanXianTypePosition(list.get(0).getBv()));
                    BW.setSelection(getQuanXianTypePosition(list.get(0).getBw()));
                    BX.setSelection(getQuanXianTypePosition(list.get(0).getBx()));
                    BYY.setSelection(getQuanXianTypePosition(list.get(0).getByy()));
                    BZ.setSelection(getQuanXianTypePosition(list.get(0).getBz()));
                    CA.setSelection(getQuanXianTypePosition(list.get(0).getCa()));
                    CB.setSelection(getQuanXianTypePosition(list.get(0).getCb()));
                    CC.setSelection(getQuanXianTypePosition(list.get(0).getCc()));
                    CD.setSelection(getQuanXianTypePosition(list.get(0).getCd()));
                    CE.setSelection(getQuanXianTypePosition(list.get(0).getCe()));
                    CF.setSelection(getQuanXianTypePosition(list.get(0).getCf()));
                    CG.setSelection(getQuanXianTypePosition(list.get(0).getCg()));
                    CH.setSelection(getQuanXianTypePosition(list.get(0).getCh()));
                    CI.setSelection(getQuanXianTypePosition(list.get(0).getCi()));
                    CJ.setSelection(getQuanXianTypePosition(list.get(0).getCj()));
                    CK.setSelection(getQuanXianTypePosition(list.get(0).getCk()));
                    CL.setSelection(getQuanXianTypePosition(list.get(0).getCl()));
                    CM.setSelection(getQuanXianTypePosition(list.get(0).getCm()));
                    CN.setSelection(getQuanXianTypePosition(list.get(0).getCn()));
                    CO.setSelection(getQuanXianTypePosition(list.get(0).getCo()));
                    CP.setSelection(getQuanXianTypePosition(list.get(0).getCp()));
                    CQ.setSelection(getQuanXianTypePosition(list.get(0).getCq()));
                    CR.setSelection(getQuanXianTypePosition(list.get(0).getCr()));
                    CS.setSelection(getQuanXianTypePosition(list.get(0).getCs()));
                    CT.setSelection(getQuanXianTypePosition(list.get(0).getCt()));
                    CU.setSelection(getQuanXianTypePosition(list.get(0).getCu()));
                    CV.setSelection(getQuanXianTypePosition(list.get(0).getCv()));
                    CW.setSelection(getQuanXianTypePosition(list.get(0).getCw()));
                    CX.setSelection(getQuanXianTypePosition(list.get(0).getCx()));
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<HashMap<String, Object>> data = new ArrayList<>();
                try {
                    gongsiService = new GongsiService();
                    list = gongsiService.getList(renyuan.getB());
                    if (list == null) return;

                } catch (Exception e) {
                    e.printStackTrace();
                }

                Message msg = new Message();
                listLoadHandler.sendMessage(msg);
            }
        }).start();
    }

    public void updateClick(View v) {
        if (!checkForm()) return;
        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(GongZuoTaiQuanXianChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(GongZuoTaiQuanXianChangeActivity.this, "保存失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = gongsiService.update(gongsi);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }


    private int getQuanXianTypePosition(String param) {
        if (quanxian_typeArray != null) {
            for (int i = 0; i < quanxian_typeArray.length; i++) {
                if (param.equals(quanxian_typeArray[i])) {
                    return i;
                }
            }
        }
        return 0;
    }

    private boolean checkForm() {

        gongsi.setC(C.getSelectedItem().toString());
        gongsi.setD(D.getSelectedItem().toString());
        gongsi.setE(E.getSelectedItem().toString());
        gongsi.setF(F.getSelectedItem().toString());
        gongsi.setG(G.getSelectedItem().toString());
        gongsi.setH(H.getSelectedItem().toString());
        gongsi.setI(I.getSelectedItem().toString());
        gongsi.setJ(J.getSelectedItem().toString());
        gongsi.setK(K.getSelectedItem().toString());
        gongsi.setL(L.getSelectedItem().toString());
        gongsi.setM(M.getSelectedItem().toString());
        gongsi.setN(N.getSelectedItem().toString());
        gongsi.setO(O.getSelectedItem().toString());
        gongsi.setP(P.getSelectedItem().toString());
        gongsi.setQ(Q.getSelectedItem().toString());
        gongsi.setR(RR.getSelectedItem().toString());
        gongsi.setS(S.getSelectedItem().toString());
        gongsi.setT(T.getSelectedItem().toString());
        gongsi.setU(U.getSelectedItem().toString());
        gongsi.setV(V.getSelectedItem().toString());
        gongsi.setW(W.getSelectedItem().toString());
        gongsi.setX(X.getSelectedItem().toString());
        gongsi.setY(Y.getSelectedItem().toString());
        gongsi.setZ(Z.getSelectedItem().toString());

        gongsi.setAa(AA.getSelectedItem().toString());
        gongsi.setAb(AB.getSelectedItem().toString());
        gongsi.setAc(AC.getSelectedItem().toString());
        gongsi.setAd(AD.getSelectedItem().toString());
        gongsi.setAe(AE.getSelectedItem().toString());
        gongsi.setAf(AF.getSelectedItem().toString());
        gongsi.setAg(AG.getSelectedItem().toString());
        gongsi.setAh(AH.getSelectedItem().toString());
        gongsi.setAi(AI.getSelectedItem().toString());
        gongsi.setAj(AJ.getSelectedItem().toString());
        gongsi.setAk(AK.getSelectedItem().toString());
        gongsi.setAl(AL.getSelectedItem().toString());
        gongsi.setAm(AM.getSelectedItem().toString());
        gongsi.setAn(AN.getSelectedItem().toString());
        gongsi.setAo(AO.getSelectedItem().toString());
        gongsi.setAp(AP.getSelectedItem().toString());
        gongsi.setAq(AQ.getSelectedItem().toString());
        gongsi.setAr(AR.getSelectedItem().toString());
        gongsi.setAss(ASS.getSelectedItem().toString());
        gongsi.setAt(AT.getSelectedItem().toString());
        gongsi.setAu(AU.getSelectedItem().toString());
        gongsi.setAv(AV.getSelectedItem().toString());
        gongsi.setAw(AW.getSelectedItem().toString());
        gongsi.setAx(AX.getSelectedItem().toString());
        gongsi.setAy(AY.getSelectedItem().toString());
        gongsi.setAz(AZ.getSelectedItem().toString());

        gongsi.setBa(BA.getSelectedItem().toString());
        gongsi.setBb(BB.getSelectedItem().toString());
        gongsi.setBc(BC.getSelectedItem().toString());
        gongsi.setBd(BD.getSelectedItem().toString());
        gongsi.setBe(BE.getSelectedItem().toString());
        gongsi.setBf(BF.getSelectedItem().toString());
        gongsi.setBg(BG.getSelectedItem().toString());
        gongsi.setBh(BH.getSelectedItem().toString());
        gongsi.setBi(BI.getSelectedItem().toString());
        gongsi.setBj(BJ.getSelectedItem().toString());
        gongsi.setBk(BK.getSelectedItem().toString());
        gongsi.setBl(BL.getSelectedItem().toString());
        gongsi.setBm(BM.getSelectedItem().toString());
        gongsi.setBn(BN.getSelectedItem().toString());
        gongsi.setBo(BO.getSelectedItem().toString());
        gongsi.setBp(BP.getSelectedItem().toString());
        gongsi.setBq(BQ.getSelectedItem().toString());
        gongsi.setBr(BR.getSelectedItem().toString());
        gongsi.setBs(BS.getSelectedItem().toString());
        gongsi.setBt(BT.getSelectedItem().toString());
        gongsi.setBu(BU.getSelectedItem().toString());
        gongsi.setBv(BV.getSelectedItem().toString());
        gongsi.setBw(BW.getSelectedItem().toString());
        gongsi.setBx(BX.getSelectedItem().toString());
        gongsi.setByy(BYY.getSelectedItem().toString());
        gongsi.setBz(BZ.getSelectedItem().toString());

        gongsi.setCa(CA.getSelectedItem().toString());
        gongsi.setCb(CB.getSelectedItem().toString());
        gongsi.setCc(CC.getSelectedItem().toString());
        gongsi.setCd(CD.getSelectedItem().toString());
        gongsi.setCe(CE.getSelectedItem().toString());
        gongsi.setCf(CF.getSelectedItem().toString());
        gongsi.setCg(CG.getSelectedItem().toString());
        gongsi.setCh(CH.getSelectedItem().toString());
        gongsi.setCi(CI.getSelectedItem().toString());
        gongsi.setCj(CJ.getSelectedItem().toString());
        gongsi.setCk(CK.getSelectedItem().toString());
        gongsi.setCl(CL.getSelectedItem().toString());
        gongsi.setCm(CM.getSelectedItem().toString());
        gongsi.setCn(CN.getSelectedItem().toString());
        gongsi.setCo(CO.getSelectedItem().toString());
        gongsi.setCp(CP.getSelectedItem().toString());
        gongsi.setCq(CQ.getSelectedItem().toString());
        gongsi.setCr(CR.getSelectedItem().toString());
        gongsi.setCs(CS.getSelectedItem().toString());
        gongsi.setCt(CT.getSelectedItem().toString());
        gongsi.setCu(CU.getSelectedItem().toString());
        gongsi.setCv(CV.getSelectedItem().toString());
        gongsi.setCw(CW.getSelectedItem().toString());
        gongsi.setCx(CX.getSelectedItem().toString());

        gongsi.setB(renyuan.getB());

        return true;
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

    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }



}
