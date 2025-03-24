package com.example.myapplication.fenquan.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.fenquan.entity.Jisuan;
import com.example.myapplication.fenquan.entity.Renyuan;
import com.example.myapplication.fenquan.entity.Workbench;
import com.example.myapplication.fenquan.service.JisuanService;
import com.example.myapplication.fenquan.service.WorkbenchService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.ToastUtil;

import java.util.Date;

public class GongZuoTaiChangeActivity extends AppCompatActivity {
    private Renyuan renyuan;
    private Workbench workbench;
    private WorkbenchService workbenchService;

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

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gongzuotai_change);

        EditText editTextA = findViewById(R.id.A);
        EditText editTextB = findViewById(R.id.B);
        EditText editTextC = findViewById(R.id.C);
        EditText editTextD = findViewById(R.id.D);
        EditText editTextE = findViewById(R.id.E);
        EditText editTextF = findViewById(R.id.F);
        EditText editTextG = findViewById(R.id.G);
        EditText editTextH = findViewById(R.id.H);
        EditText editTextI = findViewById(R.id.I);
        EditText editTextJ = findViewById(R.id.J);
        EditText editTextK = findViewById(R.id.K);
        EditText editTextL = findViewById(R.id.L);
        EditText editTextM = findViewById(R.id.M);
        EditText editTextN = findViewById(R.id.N);
        EditText editTextO = findViewById(R.id.O);
        EditText editTextP = findViewById(R.id.P);
        EditText editTextQ = findViewById(R.id.Q);
        EditText editTextRR = findViewById(R.id.RR);
        EditText editTextS = findViewById(R.id.S);
        EditText editTextT = findViewById(R.id.T);
        EditText editTextU = findViewById(R.id.U);
        EditText editTextV = findViewById(R.id.V);
        EditText editTextW = findViewById(R.id.W);
        EditText editTextX = findViewById(R.id.X);
        EditText editTextY = findViewById(R.id.Y);
        EditText editTextZ = findViewById(R.id.Z);
        EditText editTextAA = findViewById(R.id.AA);
        EditText editTextAB = findViewById(R.id.AB);
        EditText editTextAC = findViewById(R.id.AC);
        EditText editTextAD = findViewById(R.id.AD);
        EditText editTextAE = findViewById(R.id.AE);
        EditText editTextAF = findViewById(R.id.AF);
        EditText editTextAG = findViewById(R.id.AG);
        EditText editTextAH = findViewById(R.id.AH);
        EditText editTextAI = findViewById(R.id.AI);
        EditText editTextAJ = findViewById(R.id.AJ);
        EditText editTextAK = findViewById(R.id.AK);
        EditText editTextAL = findViewById(R.id.AL);
        EditText editTextAM = findViewById(R.id.AM);
        EditText editTextAN = findViewById(R.id.AN);
        EditText editTextAO = findViewById(R.id.AO);
        EditText editTextAP = findViewById(R.id.AP);
        EditText editTextAQ = findViewById(R.id.AQ);
        EditText editTextAR = findViewById(R.id.AR);
        EditText editTextASS = findViewById(R.id.ASS);
        EditText editTextAT = findViewById(R.id.AT);
        EditText editTextAU = findViewById(R.id.AU);
        EditText editTextAV = findViewById(R.id.AV);
        EditText editTextAW = findViewById(R.id.AW);
        EditText editTextAX = findViewById(R.id.AX);
        EditText editTextAY = findViewById(R.id.AY);
        EditText editTextAZ = findViewById(R.id.AZ);
        EditText editTextBA = findViewById(R.id.BA);
        EditText editTextBB = findViewById(R.id.BB);
        EditText editTextBC = findViewById(R.id.BC);
        EditText editTextBD = findViewById(R.id.BD);
        EditText editTextBE = findViewById(R.id.BE);
        EditText editTextBF = findViewById(R.id.BF);
        EditText editTextBG = findViewById(R.id.BG);
        EditText editTextBH = findViewById(R.id.BH);
        EditText editTextBI = findViewById(R.id.BI);
        EditText editTextBJ = findViewById(R.id.BJ);
        EditText editTextBK = findViewById(R.id.BK);
        EditText editTextBL = findViewById(R.id.BL);
        EditText editTextBM = findViewById(R.id.BM);
        EditText editTextBN = findViewById(R.id.BN);
        EditText editTextBO = findViewById(R.id.BO);
        EditText editTextBP = findViewById(R.id.BP);
        EditText editTextBQ = findViewById(R.id.BQ);
        EditText editTextBR = findViewById(R.id.BR);
        EditText editTextBS = findViewById(R.id.BS);
        EditText editTextBT = findViewById(R.id.BT);
        EditText editTextBU = findViewById(R.id.BU);
        EditText editTextBV = findViewById(R.id.BV);
        EditText editTextBW = findViewById(R.id.BW);
        EditText editTextBX = findViewById(R.id.BX);
        EditText editTextBYY = findViewById(R.id.BYY);
        EditText editTextBZ = findViewById(R.id.BZ);
        EditText editTextCA = findViewById(R.id.CA);
        EditText editTextCB = findViewById(R.id.CB);
        EditText editTextCC = findViewById(R.id.CC);
        EditText editTextCD = findViewById(R.id.CD);
        EditText editTextCE = findViewById(R.id.CE);
        EditText editTextCF = findViewById(R.id.CF);
        EditText editTextCG = findViewById(R.id.CG);
        EditText editTextCH = findViewById(R.id.CH);
        EditText editTextCI = findViewById(R.id.CI);
        EditText editTextCJ = findViewById(R.id.CJ);
        EditText editTextCK = findViewById(R.id.CK);
        EditText editTextCL = findViewById(R.id.CL);
        EditText editTextCM = findViewById(R.id.CM);
        EditText editTextCN = findViewById(R.id.CN);
        EditText editTextCO = findViewById(R.id.CO);
        EditText editTextCP = findViewById(R.id.CP);
        EditText editTextCQ = findViewById(R.id.CQ);
        EditText editTextCR = findViewById(R.id.CR);
        EditText editTextCS = findViewById(R.id.CS);
        EditText editTextCT = findViewById(R.id.CT);
        EditText editTextCU = findViewById(R.id.CU);
        EditText editTextCV = findViewById(R.id.CV);

        editTextA.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyA(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyA(workbench);
                    }).start();
                }
            }
        });

        editTextB.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyB(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyB(workbench);
                    }).start();
                }
            }
        });

        editTextC.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyC(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyC(workbench);
                    }).start();
                }
            }
        });

        editTextD.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyD(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyD(workbench);
                    }).start();
                }
            }
        });

        editTextE.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyE(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyE(workbench);
                    }).start();
                }
            }
        });

        editTextF.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyF(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyF(workbench);
                    }).start();
                }
            }
        });

        editTextG.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyG(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyG(workbench);
                    }).start();
                }
            }
        });

        editTextH.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyH(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyH(workbench);
                    }).start();
                }
            }
        });

        editTextI.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyI(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyI(workbench);
                    }).start();
                }
            }
        });

        editTextJ.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyJ(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyJ(workbench);
                    }).start();
                }
            }
        });

        editTextK.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyK(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyK(workbench);
                    }).start();
                }
            }
        });

        editTextL.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyL(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyL(workbench);
                    }).start();
                }
            }
        });

        editTextM.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyM(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyM(workbench);
                    }).start();
                }
            }
        });

        editTextN.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyN(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyN(workbench);
                    }).start();
                }
            }
        });

        editTextO.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyO(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyO(workbench);
                    }).start();
                }
            }
        });

        editTextP.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyP(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyP(workbench);
                    }).start();
                }
            }
        });

        editTextQ.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyQ(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyQ(workbench);
                    }).start();
                }
            }
        });

        editTextRR.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyR(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyR(workbench);
                    }).start();
                }
            }
        });

        editTextS.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyS(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyS(workbench);
                    }).start();
                }
            }
        });

        editTextT.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyT(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyT(workbench);
                    }).start();
                }
            }
        });

        editTextU.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyU(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyU(workbench);
                    }).start();
                }
            }
        });

        editTextV.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyV(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyV(workbench);
                    }).start();
                }
            }
        });

        editTextW.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyW(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyW(workbench);
                    }).start();
                }
            }
        });

        editTextX.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyX(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyX(workbench);
                    }).start();
                }
            }
        });

        editTextY.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyY(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyY(workbench);
                    }).start();
                }
            }
        });

        editTextZ.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyZ(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyZ(workbench);
                    }).start();
                }
            }
        });

        editTextAA.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyAA(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyAA(workbench);
                    }).start();
                }
            }
        });

        editTextAB.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyAB(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyAB(workbench);
                    }).start();
                }
            }
        });

        editTextAC.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyAC(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyAC(workbench);
                    }).start();
                }
            }
        });

        editTextAD.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyAD(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyAD(workbench);
                    }).start();
                }
            }
        });

        editTextAE.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyAE(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyAE(workbench);
                    }).start();
                }
            }
        });

        editTextAF.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyAF(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyAF(workbench);
                    }).start();
                }
            }
        });

        editTextAG.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyAG(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyAG(workbench);
                    }).start();
                }
            }
        });

        editTextAH.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyAH(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyAH(workbench);
                    }).start();
                }
            }
        });

        editTextAI.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyAI(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyAI(workbench);
                    }).start();
                }
            }
        });

        editTextAJ.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyAJ(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyAJ(workbench);
                    }).start();
                }
            }
        });

        editTextAK.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyAK(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyAK(workbench);
                    }).start();
                }
            }
        });

        editTextAL.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyAL(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyAL(workbench);
                    }).start();
                }
            }
        });

        editTextAM.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyAM(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyAM(workbench);
                    }).start();
                }
            }
        });

        editTextAN.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyAN(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyAN(workbench);
                    }).start();
                }
            }
        });

        editTextAO.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyAO(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyAO(workbench);
                    }).start();
                }
            }
        });

        editTextAP.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyAP(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyAP(workbench);
                    }).start();
                }
            }
        });

        editTextAQ.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyAQ(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyAQ(workbench);
                    }).start();
                }
            }
        });

        editTextAR.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyAR(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyAR(workbench);
                    }).start();
                }
            }
        });

        editTextASS.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyASS(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyASS(workbench);
                    }).start();
                }
            }
        });

        editTextAT.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyAT(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyAT(workbench);
                    }).start();
                }
            }
        });

        editTextAU.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyAU(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyAU(workbench);
                    }).start();
                }
            }
        });

        editTextAV.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyAV(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyAV(workbench);
                    }).start();
                }
            }
        });

        editTextAW.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyAW(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyAW(workbench);
                    }).start();
                }
            }
        });

        editTextAX.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyAX(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyAX(workbench);
                    }).start();
                }
            }
        });

        editTextAY.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyAY(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyAY(workbench);
                    }).start();
                }
            }
        });

        editTextAZ.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyAZ(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyAZ(workbench);
                    }).start();
                }
            }
        });

        editTextBA.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyBA(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyBA(workbench);
                    }).start();
                }
            }
        });

        editTextBB.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyBB(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyBB(workbench);
                    }).start();
                }
            }
        });

        editTextBC.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyBC(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyBC(workbench);
                    }).start();
                }
            }
        });

        editTextBD.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyBD(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyBD(workbench);
                    }).start();
                }
            }
        });

        editTextBE.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyBE(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyBE(workbench);
                    }).start();
                }
            }
        });

        editTextBF.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyBF(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyBF(workbench);
                    }).start();
                }
            }
        });

        editTextBG.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyBG(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyBG(workbench);
                    }).start();
                }
            }
        });

        editTextBH.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyBH(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyBH(workbench);
                    }).start();
                }
            }
        });

        editTextBI.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyBI(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyBI(workbench);
                    }).start();
                }
            }
        });

        editTextBJ.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyBJ(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyBJ(workbench);
                    }).start();
                }
            }
        });

        editTextBK.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyBK(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyBK(workbench);
                    }).start();
                }
            }
        });

        editTextBL.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyBL(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyBL(workbench);
                    }).start();
                }
            }
        });

        editTextBM.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyBM(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyBM(workbench);
                    }).start();
                }
            }
        });

        editTextBN.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyBN(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyBN(workbench);
                    }).start();
                }
            }
        });

        editTextBO.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyBO(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyBO(workbench);
                    }).start();
                }
            }
        });

        editTextBP.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyBP(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyBP(workbench);
                    }).start();
                }
            }
        });

        editTextBQ.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyBQ(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyBQ(workbench);
                    }).start();
                }
            }
        });

        editTextBR.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyBR(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyBR(workbench);
                    }).start();
                }
            }
        });

        editTextBS.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyBS(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyBS(workbench);
                    }).start();
                }
            }
        });

        editTextBT.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyBT(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyBT(workbench);
                    }).start();
                }
            }
        });

        editTextBU.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyBU(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyBU(workbench);
                    }).start();
                }
            }
        });

        editTextBV.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyBV(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyBV(workbench);
                    }).start();
                }
            }
        });

        editTextBW.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyBW(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyBW(workbench);
                    }).start();
                }
            }
        });

        editTextBX.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyBX(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyBX(workbench);
                    }).start();
                }
            }
        });

        editTextBYY.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyBYY(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyBYY(workbench);
                    }).start();
                }
            }
        });

        editTextBZ.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyBZ(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyBZ(workbench);
                    }).start();
                }
            }
        });

        editTextCA.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyCA(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyCA(workbench);
                    }).start();
                }
            }
        });

        editTextCB.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyCB(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyCB(workbench);
                    }).start();
                }
            }
        });

        editTextCC.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyCC(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyCC(workbench);
                    }).start();
                }
            }
        });

        editTextCD.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyCD(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyCD(workbench);
                    }).start();
                }
            }
        });

        editTextCE.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyCE(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyCE(workbench);
                    }).start();
                }
            }
        });

        editTextCF.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyCF(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyCF(workbench);
                    }).start();
                }
            }
        });

        editTextCG.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyCG(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyCG(workbench);
                    }).start();
                }
            }
        });

        editTextCH.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyCH(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyCH(workbench);
                    }).start();
                }
            }
        });

        editTextCI.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyCI(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyCI(workbench);
                    }).start();
                }
            }
        });

        editTextCJ.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyCJ(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyCJ(workbench);
                    }).start();
                }
            }
        });

        editTextCK.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyCK(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyCK(workbench);
                    }).start();
                }
            }
        });

        editTextCL.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyCL(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyCL(workbench);
                    }).start();
                }
            }
        });

        editTextCM.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyCM(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyCM(workbench);
                    }).start();
                }
            }
        });

        editTextCN.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyCN(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyCN(workbench);
                    }).start();
                }
            }
        });

        editTextCO.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyCO(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyCO(workbench);
                    }).start();
                }
            }
        });

        editTextCP.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyCP(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyCP(workbench);
                    }).start();
                }
            }
        });

        editTextCQ.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyCQ(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyCQ(workbench);
                    }).start();
                }
            }
        });

        editTextCR.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyCR(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyCR(workbench);
                    }).start();
                }
            }
        });

        editTextCS.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyCS(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyCS(workbench);
                    }).start();
                }
            }
        });

        editTextCT.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyCT(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyCT(workbench);
                    }).start();
                }
            }
        });

        editTextCU.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyCU(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyCU(workbench);
                    }).start();
                }
            }
        });

        editTextCV.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点时的处理
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updatezyCV(workbench);
                    }).start();
                } else {
                    new Thread(() -> {
                        Message msg = new Message();
                        workbenchService.updateqxzyCV(workbench);
                    }).start();
                }
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        workbenchService = new WorkbenchService();

        MyApplication myApplication = (MyApplication) getApplication();
        renyuan = myApplication.getRenyuan();

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

        Intent intent = getIntent();
        int id = intent.getIntExtra("type", 0);
        if (id == R.id.insert_btn) {
            workbench = new Workbench();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
        } else if (id == R.id.update_btn) {
            workbench = (Workbench) myApplication.getObj();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
            A.setText(workbench.getA());
            B.setText(workbench.getB());
            C.setText(workbench.getC());
            D.setText(workbench.getD());
            E.setText(workbench.getE());
            F.setText(workbench.getF());
            G.setText(workbench.getG());
            H.setText(workbench.getH());
            I.setText(workbench.getI());
            J.setText(workbench.getJ());
            K.setText(workbench.getK());
            L.setText(workbench.getL());
            M.setText(workbench.getM());
            N.setText(workbench.getN());
            O.setText(workbench.getO());
            P.setText(workbench.getP());
            Q.setText(workbench.getQ());
            RR.setText(workbench.getR());
            S.setText(workbench.getS());
            T.setText(workbench.getT());
            U.setText(workbench.getU());
            V.setText(workbench.getV());
            W.setText(workbench.getW());
            X.setText(workbench.getX());
            Y.setText(workbench.getY());
            Z.setText(workbench.getZ());
            AA.setText(workbench.getAa());
            AB.setText(workbench.getAb());
            AC.setText(workbench.getAc());
            AD.setText(workbench.getAd());
            AE.setText(workbench.getAe());
            AF.setText(workbench.getAf());
            AG.setText(workbench.getAg());
            AH.setText(workbench.getAh());
            AI.setText(workbench.getAi());
            AJ.setText(workbench.getAj());
            AK.setText(workbench.getAk());
            AL.setText(workbench.getAl());
            AM.setText(workbench.getAm());
            AN.setText(workbench.getAn());
            AO.setText(workbench.getAo());
            AP.setText(workbench.getAp());
            AQ.setText(workbench.getAq());
            AR.setText(workbench.getAr());
            ASS.setText(workbench.getAss());
            AT.setText(workbench.getAt());
            AU.setText(workbench.getAu());
            AV.setText(workbench.getAv());
            AW.setText(workbench.getAw());
            AX.setText(workbench.getAx());
            AY.setText(workbench.getAy());
            AZ.setText(workbench.getAz());
            BA.setText(workbench.getBa());
            BB.setText(workbench.getBb());
            BC.setText(workbench.getBc());
            BD.setText(workbench.getBd());
            BE.setText(workbench.getBe());
            BF.setText(workbench.getBf());
            BG.setText(workbench.getBg());
            BH.setText(workbench.getBh());
            BI.setText(workbench.getBi());
            BJ.setText(workbench.getBj());
            BK.setText(workbench.getBk());
            BL.setText(workbench.getBl());
            BM.setText(workbench.getBm());
            BN.setText(workbench.getBn());
            BO.setText(workbench.getBo());
            BP.setText(workbench.getBp());
            BQ.setText(workbench.getBq());
            BR.setText(workbench.getBr());
            BS.setText(workbench.getBs());
            BT.setText(workbench.getBt());
            BU.setText(workbench.getBu());
            BV.setText(workbench.getBv());
            BW.setText(workbench.getBw());
            BX.setText(workbench.getBx());
            BYY.setText(workbench.getByy());
            BZ.setText(workbench.getBz());
            CA.setText(workbench.getCa());
            CB.setText(workbench.getCb());
            CC.setText(workbench.getCc());
            CD.setText(workbench.getCd());
            CE.setText(workbench.getCe());
            CF.setText(workbench.getCf());
            CG.setText(workbench.getCg());
            CH.setText(workbench.getCh());
            CI.setText(workbench.getCi());
            CJ.setText(workbench.getCj());
            CK.setText(workbench.getCk());
            CL.setText(workbench.getCl());
            CM.setText(workbench.getCm());
            CN.setText(workbench.getCn());
            CO.setText(workbench.getCo());
            CP.setText(workbench.getCp());
            CQ.setText(workbench.getCq());
            CR.setText(workbench.getCr());
            CS.setText(workbench.getCs());
            CT.setText(workbench.getCt());
            CU.setText(workbench.getCu());
            CV.setText(workbench.getCv());
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
                    ToastUtil.show(GongZuoTaiChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(GongZuoTaiChangeActivity.this, "保存失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = workbenchService.insert(workbench);
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
                    ToastUtil.show(GongZuoTaiChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(GongZuoTaiChangeActivity.this, "保存失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = workbenchService.update(workbench);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    private boolean checkForm() {
        Date date = new Date();
        workbench.setA最后修改日期(date);
        workbench.setA(A.getText().toString());
        workbench.setB(B.getText().toString());
        workbench.setC(C.getText().toString());
        workbench.setD(D.getText().toString());
        workbench.setE(E.getText().toString());
        workbench.setF(F.getText().toString());
        workbench.setG(G.getText().toString());
        workbench.setH(H.getText().toString());
        workbench.setI(I.getText().toString());
        workbench.setJ(J.getText().toString());
        workbench.setK(K.getText().toString());
        workbench.setL(L.getText().toString());
        workbench.setM(M.getText().toString());
        workbench.setN(N.getText().toString());
        workbench.setO(O.getText().toString());
        workbench.setP(P.getText().toString());
        workbench.setQ(Q.getText().toString());
        workbench.setR(RR.getText().toString());
        workbench.setS(S.getText().toString());
        workbench.setT(T.getText().toString());
        workbench.setU(U.getText().toString());
        workbench.setV(V.getText().toString());
        workbench.setW(W.getText().toString());
        workbench.setX(X.getText().toString());
        workbench.setY(Y.getText().toString());
        workbench.setZ(Z.getText().toString());

        workbench.setAa(AA.getText().toString());
        workbench.setAb(AB.getText().toString());
        workbench.setAc(AC.getText().toString());
        workbench.setAd(AD.getText().toString());
        workbench.setAe(AE.getText().toString());
        workbench.setAf(AF.getText().toString());
        workbench.setAg(AG.getText().toString());
        workbench.setAh(AH.getText().toString());
        workbench.setAi(AI.getText().toString());
        workbench.setAj(AJ.getText().toString());
        workbench.setAk(AK.getText().toString());
        workbench.setAl(AL.getText().toString());
        workbench.setAm(AM.getText().toString());
        workbench.setAn(AN.getText().toString());
        workbench.setAo(AO.getText().toString());
        workbench.setAp(AP.getText().toString());
        workbench.setAq(AQ.getText().toString());
        workbench.setAr(AR.getText().toString());
        workbench.setAss(ASS.getText().toString());
        workbench.setAt(AT.getText().toString());
        workbench.setAu(AU.getText().toString());
        workbench.setAv(AV.getText().toString());
        workbench.setAw(AW.getText().toString());
        workbench.setAx(AX.getText().toString());
        workbench.setAy(AY.getText().toString());
        workbench.setAz(AZ.getText().toString());

        workbench.setBa(BA.getText().toString());
        workbench.setBb(BB.getText().toString());
        workbench.setBc(BC.getText().toString());
        workbench.setBd(BD.getText().toString());
        workbench.setBe(BE.getText().toString());
        workbench.setBf(BF.getText().toString());
        workbench.setBg(BG.getText().toString());
        workbench.setBh(BH.getText().toString());
        workbench.setBi(BI.getText().toString());
        workbench.setBj(BJ.getText().toString());
        workbench.setBk(BK.getText().toString());
        workbench.setBl(BL.getText().toString());
        workbench.setBm(BM.getText().toString());
        workbench.setBn(BN.getText().toString());
        workbench.setBo(BO.getText().toString());
        workbench.setBp(BP.getText().toString());
        workbench.setBq(BQ.getText().toString());
        workbench.setBr(BR.getText().toString());
        workbench.setBs(BS.getText().toString());
        workbench.setBt(BT.getText().toString());
        workbench.setBu(BU.getText().toString());
        workbench.setBv(BV.getText().toString());
        workbench.setBw(BW.getText().toString());
        workbench.setBx(BX.getText().toString());
        workbench.setByy(BYY.getText().toString());
        workbench.setBz(BZ.getText().toString());

        workbench.setCa(CA.getText().toString());
        workbench.setCb(CB.getText().toString());
        workbench.setCc(CC.getText().toString());
        workbench.setCd(CD.getText().toString());
        workbench.setCe(CE.getText().toString());
        workbench.setCf(CF.getText().toString());
        workbench.setCg(CG.getText().toString());
        workbench.setCh(CH.getText().toString());
        workbench.setCi(CI.getText().toString());
        workbench.setCj(CJ.getText().toString());
        workbench.setCk(CK.getText().toString());
        workbench.setCl(CL.getText().toString());
        workbench.setCm(CM.getText().toString());
        workbench.setCn(CN.getText().toString());
        workbench.setCo(CO.getText().toString());
        workbench.setCp(CP.getText().toString());
        workbench.setCq(CQ.getText().toString());
        workbench.setCr(CR.getText().toString());
        workbench.setCs(CS.getText().toString());
        workbench.setCt(CT.getText().toString());
        workbench.setCu(CU.getText().toString());
        workbench.setCv(CV.getText().toString());

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 在这里执行你的方法
        new Thread(() -> {
            Message msg = new Message();
            workbenchService.updateClearAll(workbench);
        }).start();
    }

    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }


}
