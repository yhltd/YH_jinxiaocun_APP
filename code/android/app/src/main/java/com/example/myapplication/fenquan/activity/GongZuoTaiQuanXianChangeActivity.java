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
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.fenquan.entity.Copy2;
import com.example.myapplication.fenquan.entity.Gongsi;
import com.example.myapplication.fenquan.entity.Renyuan;
import com.example.myapplication.fenquan.service.Copy2Service;
import com.example.myapplication.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GongZuoTaiQuanXianChangeActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_CHANG = 1;
    private Renyuan renyuan;
    private Copy2Service copy2Service;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gongzuotai_quanxian_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        copy2Service = new Copy2Service();

        jiechuClick = findViewById(R.id.jiechuClick);
        jiechuClick.setOnClickListener(jiechuAll());
        jiechuClick.requestFocus();

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
                AlertDialog.Builder builder = new AlertDialog.Builder(GongZuoTaiQuanXianChangeActivity.this);
                Handler deleteHandler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message msg) {
                        if ((boolean) msg.obj) {
                            ToastUtil.show(GongZuoTaiQuanXianChangeActivity.this, "解除成功");
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
                if(list != null){
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
                AlertDialog.Builder builder = new AlertDialog.Builder(GongZuoTaiQuanXianChangeActivity.this);
                Handler deleteHandler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message msg) {
                        if ((boolean) msg.obj) {
                            ToastUtil.show(GongZuoTaiQuanXianChangeActivity.this, "解除成功");
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
