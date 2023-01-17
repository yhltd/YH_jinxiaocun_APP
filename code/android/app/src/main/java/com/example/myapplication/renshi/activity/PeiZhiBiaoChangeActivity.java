package com.example.myapplication.renshi.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.finance.entity.YhFinanceDepartment;
import com.example.myapplication.finance.entity.YhFinanceUser;
import com.example.myapplication.finance.service.YhFinanceDepartmentService;
import com.example.myapplication.renshi.entity.YhRenShiPeiZhiBiao;
import com.example.myapplication.renshi.entity.YhRenShiUser;
import com.example.myapplication.renshi.service.YhRenShiPeiZhiBiaoService;
import com.example.myapplication.utils.ToastUtil;

public class PeiZhiBiaoChangeActivity extends AppCompatActivity {

    private YhRenShiUser yhRenShiUser;
    private YhRenShiPeiZhiBiao yhRenShiPeiZhiBiao;
    private YhRenShiPeiZhiBiaoService yhRenShiPeiZhiBiaoService;

    private EditText kaoqin;
    private EditText kaoqin_peizhi;
    private EditText bumen;
    private EditText zhiwu;
    private EditText chidao_koukuan;
    private EditText geren_yiliao;
    private EditText qiye_yiliao;
    private EditText geren_shengyu;
    private EditText qiye_shengyu;
    private EditText geren_gongjijin;
    private EditText qiye_gongjijin;
    private EditText yiliao_jishu;
    private EditText geren_nianjin;
    private EditText qiye_nianjin;
    private EditText zhinajin;
    private EditText nianjin_jishu;
    private EditText lixi;
    private EditText geren_yanglao;
    private EditText qiye_yanglao;
    private EditText gangwei;
    private EditText gangwei_gongzi;
    private EditText geren_shiye;
    private EditText qiye_shiye;
    private EditText gongzi;
    private EditText shuilv;
    private EditText kuadu_gongzi;
    private EditText qiye_gongshang;
    private EditText jintie;
    private EditText nianjin1;
    private EditText jiabanfei;
    private EditText yansuangongshi;
    private EditText queqin_koukuan;

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.peizhibiao_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();
        yhRenShiUser = myApplication.getYhRenShiUser();
        yhRenShiPeiZhiBiaoService = new YhRenShiPeiZhiBiaoService();

        kaoqin = findViewById(R.id.kaoqin);
        kaoqin_peizhi = findViewById(R.id.kaoqin_peizhi);
        bumen = findViewById(R.id.bumen);
        zhiwu = findViewById(R.id.zhiwu);
        chidao_koukuan = findViewById(R.id.chidao_koukuan);
        geren_yiliao = findViewById(R.id.geren_yiliao);
        qiye_yiliao = findViewById(R.id.qiye_yiliao);
        geren_shengyu = findViewById(R.id.geren_shengyu);
        qiye_shengyu = findViewById(R.id.qiye_shengyu);
        geren_gongjijin = findViewById(R.id.geren_gongjijin);
        qiye_gongjijin = findViewById(R.id.qiye_gongjijin);
        yiliao_jishu = findViewById(R.id.yiliao_jishu);
        geren_nianjin = findViewById(R.id.geren_nianjin);
        qiye_nianjin = findViewById(R.id.qiye_nianjin);
        zhinajin = findViewById(R.id.zhinajin);
        nianjin_jishu = findViewById(R.id.nianjin_jishu);
        lixi = findViewById(R.id.lixi);
        geren_yanglao = findViewById(R.id.geren_yanglao);
        qiye_yanglao = findViewById(R.id.qiye_yanglao);
        gangwei = findViewById(R.id.gangwei);
        gangwei_gongzi = findViewById(R.id.gangwei_gongzi);
        geren_shiye = findViewById(R.id.geren_shiye);
        qiye_shiye = findViewById(R.id.qiye_shiye);
        gongzi = findViewById(R.id.gongzi);
        shuilv = findViewById(R.id.shuilv);
        kuadu_gongzi = findViewById(R.id.kuadu_gongzi);
        qiye_gongshang = findViewById(R.id.qiye_gongshang);
        jintie = findViewById(R.id.jintie);
        nianjin1 = findViewById(R.id.nianjin1);
        jiabanfei = findViewById(R.id.jiabanfei);
        yansuangongshi = findViewById(R.id.yansuangongshi);
        queqin_koukuan = findViewById(R.id.queqin_koukuan);

        Intent intent = getIntent();
        int id = intent.getIntExtra("type", 0);
        if (id == R.id.insert_btn) {
            yhRenShiPeiZhiBiao = new YhRenShiPeiZhiBiao();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);
        } else if (id == R.id.update_btn) {
            yhRenShiPeiZhiBiao = (YhRenShiPeiZhiBiao) myApplication.getObj();
            Button btn = findViewById(id);
            btn.setVisibility(View.VISIBLE);

            kaoqin.setText(yhRenShiPeiZhiBiao.getKaoqin());
            kaoqin_peizhi.setText(yhRenShiPeiZhiBiao.getKaoqin_peizhi());
            bumen.setText(yhRenShiPeiZhiBiao.getBumen());
            zhiwu.setText(yhRenShiPeiZhiBiao.getZhiwu());
            chidao_koukuan.setText(yhRenShiPeiZhiBiao.getChidao_koukuan());
            geren_yiliao.setText(yhRenShiPeiZhiBiao.getGeren_yiliao());
            qiye_yiliao.setText(yhRenShiPeiZhiBiao.getQiye_yiliao());
            geren_shengyu.setText(yhRenShiPeiZhiBiao.getGeren_shengyu());
            qiye_shengyu.setText(yhRenShiPeiZhiBiao.getQiye_shengyu());
            geren_gongjijin.setText(yhRenShiPeiZhiBiao.getGeren_gongjijin());
            qiye_gongjijin.setText(yhRenShiPeiZhiBiao.getQiye_gongjijin());
            yiliao_jishu.setText(yhRenShiPeiZhiBiao.getYiliao_jishu());
            geren_nianjin.setText(yhRenShiPeiZhiBiao.getGeren_nianjin());
            qiye_nianjin.setText(yhRenShiPeiZhiBiao.getQiye_nianjin());
            zhinajin.setText(yhRenShiPeiZhiBiao.getZhinajin());
            nianjin_jishu.setText(yhRenShiPeiZhiBiao.getNianjin_jishu());
            lixi.setText(yhRenShiPeiZhiBiao.getLixi());
            geren_yanglao.setText(yhRenShiPeiZhiBiao.getGeren_yanglao());
            qiye_yanglao.setText(yhRenShiPeiZhiBiao.getQiye_yanglao());
            gangwei.setText(yhRenShiPeiZhiBiao.getGangwei());
            gangwei_gongzi.setText(yhRenShiPeiZhiBiao.getGangwei_gongzi());
            geren_shiye.setText(yhRenShiPeiZhiBiao.getGeren_shiye());
            qiye_shiye.setText(yhRenShiPeiZhiBiao.getQiye_shiye());
            gongzi.setText(yhRenShiPeiZhiBiao.getGongzi());
            shuilv.setText(yhRenShiPeiZhiBiao.getShuilv());
            kuadu_gongzi.setText(yhRenShiPeiZhiBiao.getKuadu_gongzi());
            qiye_gongshang.setText(yhRenShiPeiZhiBiao.getQiye_gongshang());
            jintie.setText(yhRenShiPeiZhiBiao.getJintie());
            nianjin1.setText(yhRenShiPeiZhiBiao.getNianjin1());
            jiabanfei.setText(yhRenShiPeiZhiBiao.getJiabanfei());
            yansuangongshi.setText(yhRenShiPeiZhiBiao.getYansuangongshi());
            queqin_koukuan.setText(yhRenShiPeiZhiBiao.getQueqin_koukuan());

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
                    ToastUtil.show(PeiZhiBiaoChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(PeiZhiBiaoChangeActivity.this, "保存失败，请稍后再试");
                }

                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = yhRenShiPeiZhiBiaoService.insert(yhRenShiPeiZhiBiao);
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
                    ToastUtil.show(PeiZhiBiaoChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(PeiZhiBiaoChangeActivity.this, "保存失败，请稍后再试");
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = yhRenShiPeiZhiBiaoService.update(yhRenShiPeiZhiBiao);
                saveHandler.sendMessage(msg);
            }
        }).start();
    }


    private boolean checkForm() {

        yhRenShiPeiZhiBiao.setKaoqin(kaoqin.getText().toString());
        yhRenShiPeiZhiBiao.setKaoqin_peizhi(kaoqin_peizhi.getText().toString());
        yhRenShiPeiZhiBiao.setBumen(bumen.getText().toString());
        yhRenShiPeiZhiBiao.setZhiwu(zhiwu.getText().toString());
        yhRenShiPeiZhiBiao.setChidao_koukuan(chidao_koukuan.getText().toString());
        yhRenShiPeiZhiBiao.setGeren_yiliao(geren_yiliao.getText().toString());
        yhRenShiPeiZhiBiao.setQiye_yiliao(qiye_yiliao.getText().toString());
        yhRenShiPeiZhiBiao.setGeren_shengyu(geren_shengyu.getText().toString());
        yhRenShiPeiZhiBiao.setQiye_shengyu(qiye_shengyu.getText().toString());
        yhRenShiPeiZhiBiao.setGeren_gongjijin(geren_gongjijin.getText().toString());
        yhRenShiPeiZhiBiao.setQiye_gongjijin(qiye_gongjijin.getText().toString());
        yhRenShiPeiZhiBiao.setYiliao_jishu(yiliao_jishu.getText().toString());
        yhRenShiPeiZhiBiao.setGeren_nianjin(geren_nianjin.getText().toString());
        yhRenShiPeiZhiBiao.setQiye_nianjin(qiye_nianjin.getText().toString());
        yhRenShiPeiZhiBiao.setZhinajin(zhinajin.getText().toString());
        yhRenShiPeiZhiBiao.setNianjin_jishu(nianjin_jishu.getText().toString());
        yhRenShiPeiZhiBiao.setLixi(lixi.getText().toString());
        yhRenShiPeiZhiBiao.setGeren_yanglao(geren_yanglao.getText().toString());
        yhRenShiPeiZhiBiao.setQiye_yanglao(qiye_yanglao.getText().toString());
        yhRenShiPeiZhiBiao.setGangwei(gangwei.getText().toString());
        yhRenShiPeiZhiBiao.setGangwei_gongzi(gangwei_gongzi.getText().toString());
        yhRenShiPeiZhiBiao.setGeren_shiye(geren_shiye.getText().toString());
        yhRenShiPeiZhiBiao.setQiye_shiye(qiye_shiye.getText().toString());
        yhRenShiPeiZhiBiao.setGongzi(gongzi.getText().toString());
        yhRenShiPeiZhiBiao.setShuilv(shuilv.getText().toString());
        yhRenShiPeiZhiBiao.setKuadu_gongzi(kuadu_gongzi.getText().toString());
        yhRenShiPeiZhiBiao.setQiye_gongshang(qiye_gongshang.getText().toString());
        yhRenShiPeiZhiBiao.setJintie(jintie.getText().toString());
        yhRenShiPeiZhiBiao.setNianjin1(nianjin1.getText().toString());
        yhRenShiPeiZhiBiao.setJiabanfei(jiabanfei.getText().toString());
        yhRenShiPeiZhiBiao.setYansuangongshi(yansuangongshi.getText().toString());
        yhRenShiPeiZhiBiao.setQueqin_koukuan(queqin_koukuan.getText().toString());
        yhRenShiPeiZhiBiao.setGongsi(yhRenShiUser.getL().replace("_hr",""));
        return true;
    }

    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }

}
