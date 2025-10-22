package com.example.myapplication.fenquan.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.fenquan.entity.Copy2;
import com.example.myapplication.fenquan.entity.Renyuan;
import com.example.myapplication.fenquan.service.Copy2Service;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GongZuoTaiShiYongChangeActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_CHANG = 1;
    private Renyuan renyuan;
    private Copy2Service copy2Service;

    private EditText A;
    private EditText B;
    private EditText C;
    private EditText D;
    private EditText E;
    private EditText F;
    private EditText G;
    private EditText H;
    private EditText I;
    private EditText J;
    private EditText K;
    private EditText L;
    private EditText M;
    private EditText N;
    private EditText O;
    private EditText P;
    private EditText Q;
    private EditText RR;
    private EditText S;
    private EditText T;
    private EditText U;
    private EditText V;
    private EditText W;
    private EditText X;
    private EditText Y;
    private EditText Z;
    private EditText AA;
    private EditText AB;
    private EditText AC;
    private EditText AD;
    private EditText AE;
    private EditText AF;
    private EditText AG;
    private EditText AH;
    private EditText AI;
    private EditText AJ;
    private EditText AK;
    private EditText AL;
    private EditText AM;
    private EditText AN;
    private EditText AO;
    private EditText AP;
    private EditText AQ;
    private EditText AR;
    private EditText ASS;
    private EditText AT;
    private EditText AU;
    private EditText AV;
    private EditText AW;
    private EditText AX;
    private EditText AY;
    private EditText AZ;
    private EditText BA;
    private EditText BB;
    private EditText BC;
    private EditText BD;
    private EditText BE;
    private EditText BF;
    private EditText BG;
    private EditText BH;
    private EditText BI;
    private EditText BJ;
    private EditText BK;
    private EditText BL;
    private EditText BM;
    private EditText BN;
    private EditText BO;
    private EditText BP;
    private EditText BQ;
    private EditText BR;
    private EditText BS;
    private EditText BT;
    private EditText BU;
    private EditText BV;
    private EditText BW;
    private EditText BX;
    private EditText BYY;
    private EditText BZ;
    private EditText CA;
    private EditText CB;
    private EditText CC;
    private EditText CD;
    private EditText CE;
    private EditText CF;
    private EditText CG;
    private EditText CH;
    private EditText CI;
    private EditText CJ;
    private EditText CK;
    private EditText CL;
    private EditText CM;
    private EditText CN;
    private EditText CO;
    private EditText CP;
    private EditText CQ;
    private EditText CR;
    private EditText CS;
    private EditText CT;
    private EditText CU;
    private EditText CV;
    private Button jiechuClick;

    private List<Copy2> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gongzuotaishiyong_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        copy2Service = new Copy2Service();

        jiechuClick = findViewById(R.id.jiechuClick);
        jiechuClick.setOnClickListener(jiechuAll());
        jiechuClick.requestFocus();

        A = findViewById(R.id.A);
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

        A.setOnClickListener(onItemClick(A,"update baitaoquanxian_copy2 set A = '' where id=?"));
        B.setOnClickListener(onItemClick(B,"update baitaoquanxian_copy2 set B = '' where id=?"));
        C.setOnClickListener(onItemClick(C,"update baitaoquanxian_copy2 set C = '' where id=?"));
        D.setOnClickListener(onItemClick(D,"update baitaoquanxian_copy2 set D = '' where id=?"));
        E.setOnClickListener(onItemClick(E,"update baitaoquanxian_copy2 set E = '' where id=?"));
        F.setOnClickListener(onItemClick(F,"update baitaoquanxian_copy2 set F = '' where id=?"));
        G.setOnClickListener(onItemClick(G,"update baitaoquanxian_copy2 set G = '' where id=?"));
        H.setOnClickListener(onItemClick(H,"update baitaoquanxian_copy2 set H = '' where id=?"));
        I.setOnClickListener(onItemClick(I,"update baitaoquanxian_copy2 set I = '' where id=?"));
        J.setOnClickListener(onItemClick(J,"update baitaoquanxian_copy2 set J = '' where id=?"));
        K.setOnClickListener(onItemClick(K,"update baitaoquanxian_copy2 set K = '' where id=?"));
        L.setOnClickListener(onItemClick(L,"update baitaoquanxian_copy2 set L = '' where id=?"));
        M.setOnClickListener(onItemClick(M,"update baitaoquanxian_copy2 set M = '' where id=?"));
        N.setOnClickListener(onItemClick(N,"update baitaoquanxian_copy2 set N = '' where id=?"));
        O.setOnClickListener(onItemClick(O,"update baitaoquanxian_copy2 set O = '' where id=?"));
        P.setOnClickListener(onItemClick(P,"update baitaoquanxian_copy2 set P = '' where id=?"));
        Q.setOnClickListener(onItemClick(Q,"update baitaoquanxian_copy2 set Q = '' where id=?"));
        RR.setOnClickListener(onItemClick(RR,"update baitaoquanxian_copy2 set R = '' where id=?"));
        S.setOnClickListener(onItemClick(S,"update baitaoquanxian_copy2 set S = '' where id=?"));
        T.setOnClickListener(onItemClick(T,"update baitaoquanxian_copy2 set T = '' where id=?"));
        U.setOnClickListener(onItemClick(U,"update baitaoquanxian_copy2 set U = '' where id=?"));
        V.setOnClickListener(onItemClick(V,"update baitaoquanxian_copy2 set V = '' where id=?"));
        W.setOnClickListener(onItemClick(W,"update baitaoquanxian_copy2 set W = '' where id=?"));
        X.setOnClickListener(onItemClick(X,"update baitaoquanxian_copy2 set X = '' where id=?"));
        Y.setOnClickListener(onItemClick(Y,"update baitaoquanxian_copy2 set Y = '' where id=?"));
        Z.setOnClickListener(onItemClick(Z,"update baitaoquanxian_copy2 set Z = '' where id=?"));
        AA.setOnClickListener(onItemClick(AA,"update baitaoquanxian_copy2 set AA = '' where id=?"));
        AB.setOnClickListener(onItemClick(AB,"update baitaoquanxian_copy2 set AB = '' where id=?"));
        AC.setOnClickListener(onItemClick(AC,"update baitaoquanxian_copy2 set AC = '' where id=?"));
        AD.setOnClickListener(onItemClick(AD,"update baitaoquanxian_copy2 set AD = '' where id=?"));
        AE.setOnClickListener(onItemClick(AE,"update baitaoquanxian_copy2 set AE = '' where id=?"));
        AF.setOnClickListener(onItemClick(AF,"update baitaoquanxian_copy2 set AF = '' where id=?"));
        AG.setOnClickListener(onItemClick(AG,"update baitaoquanxian_copy2 set AG = '' where id=?"));
        AH.setOnClickListener(onItemClick(AH,"update baitaoquanxian_copy2 set AH = '' where id=?"));
        AI.setOnClickListener(onItemClick(AI,"update baitaoquanxian_copy2 set AI = '' where id=?"));
        AJ.setOnClickListener(onItemClick(AJ,"update baitaoquanxian_copy2 set AJ = '' where id=?"));
        AK.setOnClickListener(onItemClick(AK,"update baitaoquanxian_copy2 set AK = '' where id=?"));
        AL.setOnClickListener(onItemClick(AL,"update baitaoquanxian_copy2 set AL = '' where id=?"));
        AM.setOnClickListener(onItemClick(AM,"update baitaoquanxian_copy2 set AM = '' where id=?"));
        AN.setOnClickListener(onItemClick(AN,"update baitaoquanxian_copy2 set AN = '' where id=?"));
        AO.setOnClickListener(onItemClick(AO,"update baitaoquanxian_copy2 set AO = '' where id=?"));
        AP.setOnClickListener(onItemClick(AP,"update baitaoquanxian_copy2 set AP = '' where id=?"));
        AQ.setOnClickListener(onItemClick(AQ,"update baitaoquanxian_copy2 set AQ = '' where id=?"));
        AR.setOnClickListener(onItemClick(AR,"update baitaoquanxian_copy2 set AR = '' where id=?"));
        ASS.setOnClickListener(onItemClick(ASS,"update baitaoquanxian_copy2 set ASS = '' where id=?"));
        AT.setOnClickListener(onItemClick(AT,"update baitaoquanxian_copy2 set AT = '' where id=?"));
        AU.setOnClickListener(onItemClick(AU,"update baitaoquanxian_copy2 set AU = '' where id=?"));
        AV.setOnClickListener(onItemClick(AV,"update baitaoquanxian_copy2 set AV = '' where id=?"));
        AW.setOnClickListener(onItemClick(AW,"update baitaoquanxian_copy2 set AW = '' where id=?"));
        AX.setOnClickListener(onItemClick(AX,"update baitaoquanxian_copy2 set AX = '' where id=?"));
        AY.setOnClickListener(onItemClick(AY,"update baitaoquanxian_copy2 set AY = '' where id=?"));
        AZ.setOnClickListener(onItemClick(AZ,"update baitaoquanxian_copy2 set AZ = '' where id=?"));
        BA.setOnClickListener(onItemClick(BA,"update baitaoquanxian_copy2 set BA = '' where id=?"));
        BB.setOnClickListener(onItemClick(BB,"update baitaoquanxian_copy2 set BB = '' where id=?"));
        BC.setOnClickListener(onItemClick(BC,"update baitaoquanxian_copy2 set BC = '' where id=?"));
        BD.setOnClickListener(onItemClick(BD,"update baitaoquanxian_copy2 set BD = '' where id=?"));
        BE.setOnClickListener(onItemClick(BE,"update baitaoquanxian_copy2 set BE = '' where id=?"));
        BF.setOnClickListener(onItemClick(BF,"update baitaoquanxian_copy2 set BF = '' where id=?"));
        BG.setOnClickListener(onItemClick(BG,"update baitaoquanxian_copy2 set BG = '' where id=?"));
        BH.setOnClickListener(onItemClick(BH,"update baitaoquanxian_copy2 set BH = '' where id=?"));
        BI.setOnClickListener(onItemClick(BI,"update baitaoquanxian_copy2 set BI = '' where id=?"));
        BJ.setOnClickListener(onItemClick(BJ,"update baitaoquanxian_copy2 set BJ = '' where id=?"));
        BK.setOnClickListener(onItemClick(BK,"update baitaoquanxian_copy2 set BK = '' where id=?"));
        BL.setOnClickListener(onItemClick(BL,"update baitaoquanxian_copy2 set BL = '' where id=?"));
        BM.setOnClickListener(onItemClick(BM,"update baitaoquanxian_copy2 set BM = '' where id=?"));
        BN.setOnClickListener(onItemClick(BN,"update baitaoquanxian_copy2 set BN = '' where id=?"));
        BO.setOnClickListener(onItemClick(BO,"update baitaoquanxian_copy2 set BO = '' where id=?"));
        BP.setOnClickListener(onItemClick(BP,"update baitaoquanxian_copy2 set BP = '' where id=?"));
        BQ.setOnClickListener(onItemClick(BQ,"update baitaoquanxian_copy2 set BQ = '' where id=?"));
        BR.setOnClickListener(onItemClick(BR,"update baitaoquanxian_copy2 set BR = '' where id=?"));
        BS.setOnClickListener(onItemClick(BS,"update baitaoquanxian_copy2 set BS = '' where id=?"));
        BT.setOnClickListener(onItemClick(BT,"update baitaoquanxian_copy2 set BT = '' where id=?"));
        BU.setOnClickListener(onItemClick(BU,"update baitaoquanxian_copy2 set BU = '' where id=?"));
        BV.setOnClickListener(onItemClick(BV,"update baitaoquanxian_copy2 set BV = '' where id=?"));
        BW.setOnClickListener(onItemClick(BW,"update baitaoquanxian_copy2 set BW = '' where id=?"));
        BX.setOnClickListener(onItemClick(BX,"update baitaoquanxian_copy2 set BX = '' where id=?"));
        BYY.setOnClickListener(onItemClick(BYY,"update baitaoquanxian_copy2 set BYY = '' where id=?"));
        BZ.setOnClickListener(onItemClick(BZ,"update baitaoquanxian_copy2 set BZ = '' where id=?"));
        CA.setOnClickListener(onItemClick(CA,"update baitaoquanxian_copy2 set CA = '' where id=?"));
        CB.setOnClickListener(onItemClick(CB,"update baitaoquanxian_copy2 set CB = '' where id=?"));
        CC.setOnClickListener(onItemClick(CC,"update baitaoquanxian_copy2 set CC = '' where id=?"));
        CD.setOnClickListener(onItemClick(CD,"update baitaoquanxian_copy2 set CD = '' where id=?"));
        CE.setOnClickListener(onItemClick(CE,"update baitaoquanxian_copy2 set CE = '' where id=?"));
        CF.setOnClickListener(onItemClick(CF,"update baitaoquanxian_copy2 set CF = '' where id=?"));
        CG.setOnClickListener(onItemClick(CG,"update baitaoquanxian_copy2 set CG = '' where id=?"));
        CH.setOnClickListener(onItemClick(CH,"update baitaoquanxian_copy2 set CH = '' where id=?"));
        CI.setOnClickListener(onItemClick(CI,"update baitaoquanxian_copy2 set CI = '' where id=?"));
        CJ.setOnClickListener(onItemClick(CJ,"update baitaoquanxian_copy2 set CJ = '' where id=?"));
        CK.setOnClickListener(onItemClick(CK,"update baitaoquanxian_copy2 set CK = '' where id=?"));
        CL.setOnClickListener(onItemClick(CL,"update baitaoquanxian_copy2 set CL = '' where id=?"));
        CM.setOnClickListener(onItemClick(CM,"update baitaoquanxian_copy2 set CM = '' where id=?"));
        CN.setOnClickListener(onItemClick(CN,"update baitaoquanxian_copy2 set CN = '' where id=?"));
        CO.setOnClickListener(onItemClick(CO,"update baitaoquanxian_copy2 set CO = '' where id=?"));
        CP.setOnClickListener(onItemClick(CP,"update baitaoquanxian_copy2 set CP = '' where id=?"));
        CQ.setOnClickListener(onItemClick(CQ,"update baitaoquanxian_copy2 set CQ = '' where id=?"));
        CR.setOnClickListener(onItemClick(CR,"update baitaoquanxian_copy2 set CR = '' where id=?"));
        CS.setOnClickListener(onItemClick(CS,"update baitaoquanxian_copy2 set CS = '' where id=?"));
        CT.setOnClickListener(onItemClick(CT,"update baitaoquanxian_copy2 set CT = '' where id=?"));
        CU.setOnClickListener(onItemClick(CU,"update baitaoquanxian_copy2 set CU = '' where id=?"));
        CV.setOnClickListener(onItemClick(CV,"update baitaoquanxian_copy2 set CV = '' where id=?"));

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

    public View.OnClickListener jiechuAll() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(GongZuoTaiShiYongChangeActivity.this);
                Handler deleteHandler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message msg) {
                        if ((boolean) msg.obj) {
                            ToastUtil.show(GongZuoTaiShiYongChangeActivity.this, "解除成功");
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
                                msg.obj = copy2Service.updateAll(list.get(0).getId());
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

                builder.setMessage("确定解除此所有列的占用吗？");
                builder.setTitle("提示");
                builder.show();
            }
        };
    }

    private void initList() {
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if(list != null && !list.isEmpty() && list.get(0) != null){
                    A.setText(list.get(0).getA());
                    B.setText(list.get(0).getB());
                    C.setText(list.get(0).getC());
                    D.setText(list.get(0).getD());
                    E.setText(list.get(0).getE());
                    F.setText(list.get(0).getF());
                    G.setText(list.get(0).getG());
                    H.setText(list.get(0).getH());
                    I.setText(list.get(0).getI());
                    J.setText(list.get(0).getJ());
                    K.setText(list.get(0).getK());
                    L.setText(list.get(0).getL());
                    M.setText(list.get(0).getM());
                    N.setText(list.get(0).getN());
                    O.setText(list.get(0).getO());
                    P.setText(list.get(0).getP());
                    Q.setText(list.get(0).getQ());
                    RR.setText(list.get(0).getR());
                    S.setText(list.get(0).getS());
                    T.setText(list.get(0).getT());
                    U.setText(list.get(0).getU());
                    V.setText(list.get(0).getV());
                    W.setText(list.get(0).getW());
                    X.setText(list.get(0).getX());
                    Y.setText(list.get(0).getY());
                    Z.setText(list.get(0).getZ());
                    AA.setText(list.get(0).getAa());
                    AB.setText(list.get(0).getAb());
                    AC.setText(list.get(0).getAc());
                    AD.setText(list.get(0).getAd());
                    AE.setText(list.get(0).getAe());
                    AF.setText(list.get(0).getAf());
                    AG.setText(list.get(0).getAg());
                    AH.setText(list.get(0).getAh());
                    AI.setText(list.get(0).getAi());
                    AJ.setText(list.get(0).getAj());
                    AK.setText(list.get(0).getAk());
                    AL.setText(list.get(0).getAl());
                    AM.setText(list.get(0).getAm());
                    AN.setText(list.get(0).getAn());
                    AO.setText(list.get(0).getAo());
                    AP.setText(list.get(0).getAp());
                    AQ.setText(list.get(0).getAq());
                    AR.setText(list.get(0).getAr());
                    ASS.setText(list.get(0).getAss());
                    AT.setText(list.get(0).getAt());
                    AU.setText(list.get(0).getAu());
                    AV.setText(list.get(0).getAv());
                    AW.setText(list.get(0).getAw());
                    AX.setText(list.get(0).getAx());
                    AY.setText(list.get(0).getAy());
                    AZ.setText(list.get(0).getAz());
                    BA.setText(list.get(0).getBa());
                    BB.setText(list.get(0).getBb());
                    BC.setText(list.get(0).getBc());
                    BD.setText(list.get(0).getBd());
                    BE.setText(list.get(0).getBe());
                    BF.setText(list.get(0).getBf());
                    BG.setText(list.get(0).getBg());
                    BH.setText(list.get(0).getBh());
                    BI.setText(list.get(0).getBi());
                    BJ.setText(list.get(0).getBj());
                    BK.setText(list.get(0).getBk());
                    BL.setText(list.get(0).getBl());
                    BM.setText(list.get(0).getBm());
                    BN.setText(list.get(0).getBn());
                    BO.setText(list.get(0).getBo());
                    BP.setText(list.get(0).getBp());
                    BQ.setText(list.get(0).getBq());
                    BR.setText(list.get(0).getBr());
                    BS.setText(list.get(0).getBs());
                    BT.setText(list.get(0).getBt());
                    BU.setText(list.get(0).getBu());
                    BV.setText(list.get(0).getBv());
                    BW.setText(list.get(0).getBw());
                    BX.setText(list.get(0).getBx());
                    BYY.setText(list.get(0).getByy());
                    BZ.setText(list.get(0).getBz());
                    CA.setText(list.get(0).getCa());
                    CB.setText(list.get(0).getCb());
                    CC.setText(list.get(0).getCc());
                    CD.setText(list.get(0).getCd());
                    CE.setText(list.get(0).getCe());
                    CF.setText(list.get(0).getCf());
                    CG.setText(list.get(0).getCg());
                    CH.setText(list.get(0).getCh());
                    CI.setText(list.get(0).getCi());
                    CJ.setText(list.get(0).getCj());
                    CK.setText(list.get(0).getCk());
                    CL.setText(list.get(0).getCl());
                    CM.setText(list.get(0).getCm());
                    CN.setText(list.get(0).getCn());
                    CO.setText(list.get(0).getCo());
                    CP.setText(list.get(0).getCp());
                    CQ.setText(list.get(0).getCq());
                    CR.setText(list.get(0).getCr());
                    CS.setText(list.get(0).getCs());
                    CT.setText(list.get(0).getCt());
                    CU.setText(list.get(0).getCu());
                    CV.setText(list.get(0).getCv());
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<HashMap<String, Object>> data = new ArrayList<>();
                try {
                    copy2Service = new Copy2Service();
                    list = copy2Service.getList(renyuan.getB());
                    if (list == null) return;

                } catch (Exception e) {
                    e.printStackTrace();
                }

                Message msg = new Message();
                listLoadHandler.sendMessage(msg);
            }
        }).start();
    }

    public View.OnClickListener onItemClick(final EditText editText,String this_column) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setFocusableInTouchMode(true);
                editText.requestFocus();
                editText.requestFocusFromTouch();
                //你的处理逻辑
                AlertDialog.Builder builder = new AlertDialog.Builder(GongZuoTaiShiYongChangeActivity.this);
                Handler deleteHandler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message msg) {
                        if ((boolean) msg.obj) {
                            ToastUtil.show(GongZuoTaiShiYongChangeActivity.this, "解除成功");
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
                                msg.obj = copy2Service.update(list.get(0).getId(),this_column);
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

                builder.setMessage("确定解除此列占用吗？");
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
