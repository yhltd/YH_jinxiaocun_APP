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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
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

public class RenYuanXinXiGuanLiChangeActivity extends AppCompatActivity {

    private YhRenShiUser yhRenShiUser;
    private YhRenShiUser this_renyuan;
    private YhRenShiUserService yhRenShiUserService;
    private YhRenShiPeiZhiBiaoService yhRenShiPeiZhiBiaoService;

    List<String> name_array;

    private EditText B;
    private Spinner C;
    private EditText D;
    private EditText E;
    private EditText F;
    private EditText G;
    private EditText H;
    private EditText I;
    private EditText J;
    private EditText K;
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

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.renyuanxinxiguanli_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        yhRenShiUser = myApplication.getYhRenShiUser();

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
        M = findViewById(R.id.M);
        N = findViewById(R.id.N);
        O = findViewById(R.id.O);
        P = findViewById(R.id.P);
        Q = findViewById(R.id.Q);

        showDateOnClick(Q);
        showDateOnClick(H);
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

        init_select();
    }

    public void init_select() {
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                SpinnerAdapter adapter = new ArrayAdapter<String>(RenYuanXinXiGuanLiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, name_array);
                C.setAdapter(StringUtils.cast(adapter));

                Intent intent = getIntent();
                int id = intent.getIntExtra("type", 0);
                if (id == R.id.insert_btn) {
                    this_renyuan = new YhRenShiUser();
                    Button btn = findViewById(id);
                    btn.setVisibility(View.VISIBLE);
                } else if (id == R.id.update_btn) {
                    MyApplication myApplication = (MyApplication) getApplication();
                    this_renyuan = (YhRenShiUser) myApplication.getObj();
                    Button btn = findViewById(id);
                    btn.setVisibility(View.VISIBLE);

                    B.setText(this_renyuan.getB());
                    C.setSelection(getName(this_renyuan.getC()));
                    D.setText(this_renyuan.getD());
                    E.setText(this_renyuan.getE());
                    F.setText(this_renyuan.getF());
                    G.setText(this_renyuan.getG());
                    H.setText(this_renyuan.getH());
                    I.setText(this_renyuan.getI());
                    J.setText(this_renyuan.getJ());
                    K.setText(this_renyuan.getK());
                    M.setText(this_renyuan.getM());
                    N.setText(this_renyuan.getN());
                    O.setText(this_renyuan.getO());
                    P.setText(this_renyuan.getP());
                    Q.setText(this_renyuan.getQ());
                    RR.setText(this_renyuan.getR());
                    S.setText(this_renyuan.getS());
                    T.setText(this_renyuan.getT());
                    U.setText(this_renyuan.getU());
                    V.setText(this_renyuan.getV());
                    W.setText(this_renyuan.getW());
                    X.setText(this_renyuan.getX());
                    Y.setText(this_renyuan.getY());
                    Z.setText(this_renyuan.getZ());
                    AA.setText(this_renyuan.getAa());
                    AB.setText(this_renyuan.getAb());
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

                    name_array = new ArrayList<>();
                    name_array.add("");
                    if (peizhiList.size() > 0) {
                        for (int i = 0; i < peizhiList.size(); i++) {
                            if(!peizhiList.get(i).getBumen().equals(""))
                                name_array.add(peizhiList.get(i).getBumen());
                        }
                    }

                    adapter = new ArrayAdapter<String>(RenYuanXinXiGuanLiChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, name_array);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.obj = adapter;
                listLoadHandler.sendMessage(msg);
            }
        }).start();
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
                    ToastUtil.show(RenYuanXinXiGuanLiChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(RenYuanXinXiGuanLiChangeActivity.this, "保存失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = yhRenShiUserService.insert(this_renyuan);
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
                    ToastUtil.show(RenYuanXinXiGuanLiChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(RenYuanXinXiGuanLiChangeActivity.this, "保存失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = yhRenShiUserService.update(this_renyuan);
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(RenYuanXinXiGuanLiChangeActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                if(dayOfMonth < 10){
                    day = "0" + String.valueOf(dayOfMonth );
                }else{
                    day = String.valueOf(dayOfMonth );
                }
                editText.setText(year + "-" + month + "-" + day);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private boolean checkForm() {
        if (B.getText().toString().equals("")) {
            ToastUtil.show(RenYuanXinXiGuanLiChangeActivity.this, "请输入姓名");
            return false;
        } else {
            this_renyuan.setB(B.getText().toString());
        }
        this_renyuan.setC(C.getSelectedItem().toString());
        this_renyuan.setD(D.getText().toString());
        this_renyuan.setE(E.getText().toString());
        this_renyuan.setF(F.getText().toString());
        this_renyuan.setG(G.getText().toString());
        this_renyuan.setH(H.getText().toString());
        this_renyuan.setI(I.getText().toString());
        this_renyuan.setJ(J.getText().toString());
        this_renyuan.setK(K.getText().toString());
        this_renyuan.setM(M.getText().toString());
        this_renyuan.setN(N.getText().toString());
        this_renyuan.setO(O.getText().toString());
        this_renyuan.setP(P.getText().toString());
        this_renyuan.setQ(Q.getText().toString());
        this_renyuan.setR(RR.getText().toString());
        this_renyuan.setS(S.getText().toString());
        this_renyuan.setT(T.getText().toString());
        this_renyuan.setU(U.getText().toString());
        this_renyuan.setV(V.getText().toString());
        this_renyuan.setW(W.getText().toString());
        this_renyuan.setX(X.getText().toString());
        this_renyuan.setY(Y.getText().toString());
        this_renyuan.setZ(Z.getText().toString());
        this_renyuan.setAa(AA.getText().toString());
        this_renyuan.setAb(AB.getText().toString());
        this_renyuan.setL(yhRenShiUser.getL());
        return true;
    }

    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }

}
