package com.example.myapplication;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.entity.XiangQingYe;
import com.example.myapplication.jxc.entity.YhJinXiaoCunUser;
import com.example.myapplication.jxc.entity.YhJinXiaoCunZhengLi;
import com.example.myapplication.jxc.service.YhJinXiaoCunZhengLiService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.ToastUtil;

public class XiangQingYeActivity extends AppCompatActivity {
    private XiangQingYe xiangQingYe;

    private TextView a_title;
    private TextView b_title;
    private TextView c_title;
    private TextView d_title;
    private TextView e_title;
    private TextView f_title;
    private TextView g_title;
    private TextView h_title;
    private TextView i_title;
    private TextView j_title;
    private TextView k_title;
    private TextView l_title;
    private TextView m_title;
    private TextView n_title;
    private TextView o_title;
    private TextView p_title;
    private TextView q_title;
    private TextView r_title;
    private TextView s_title;
    private TextView t_title;
    private TextView u_title;
    private TextView v_title;
    private TextView w_title;
    private TextView x_title;
    private TextView y_title;
    private TextView z_title;
    private TextView aa_title;
    private TextView ab_title;
    private TextView ac_title;
    private TextView ad_title;
    private TextView ae_title;
    private TextView af_title;
    private TextView ag_title;
    private TextView ah_title;

    private TextView a_text;
    private TextView b_text;
    private TextView c_text;
    private TextView d_text;
    private TextView e_text;
    private TextView f_text;
    private TextView g_text;
    private TextView h_text;
    private TextView i_text;
    private TextView j_text;
    private TextView k_text;
    private TextView l_text;
    private TextView m_text;
    private TextView n_text;
    private TextView o_text;
    private TextView p_text;
    private TextView q_text;
    private TextView r_text;
    private TextView s_text;
    private TextView t_text;
    private TextView u_text;
    private TextView v_text;
    private TextView w_text;
    private TextView x_text;
    private TextView y_text;
    private TextView z_text;
    private TextView aa_text;
    private TextView ab_text;
    private TextView ac_text;
    private TextView ad_text;
    private TextView ae_text;
    private TextView af_text;
    private TextView ag_text;
    private TextView ah_text;


    private LinearLayout a_line;
    private LinearLayout b_line;
    private LinearLayout c_line;
    private LinearLayout d_line;
    private LinearLayout e_line;
    private LinearLayout f_line;
    private LinearLayout g_line;
    private LinearLayout h_line;
    private LinearLayout i_line;
    private LinearLayout j_line;
    private LinearLayout k_line;
    private LinearLayout l_line;
    private LinearLayout m_line;
    private LinearLayout n_line;
    private LinearLayout o_line;
    private LinearLayout p_line;
    private LinearLayout q_line;
    private LinearLayout r_line;
    private LinearLayout s_line;
    private LinearLayout t_line;
    private LinearLayout u_line;
    private LinearLayout v_line;
    private LinearLayout w_line;
    private LinearLayout x_line;
    private LinearLayout y_line;
    private LinearLayout z_line;
    private LinearLayout aa_line;
    private LinearLayout ab_line;
    private LinearLayout ac_line;
    private LinearLayout ad_line;
    private LinearLayout ae_line;
    private LinearLayout af_line;
    private LinearLayout ag_line;
    private LinearLayout ah_line;

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xiangqingye);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyApplication myApplication = (MyApplication) getApplication();

        a_title= findViewById(R.id.a_title);
        b_title= findViewById(R.id.b_title);
        c_title= findViewById(R.id.c_title);
        d_title= findViewById(R.id.d_title);
        e_title= findViewById(R.id.e_title);
        f_title= findViewById(R.id.f_title);
        g_title= findViewById(R.id.g_title);
        h_title= findViewById(R.id.h_title);
        i_title= findViewById(R.id.i_title);
        j_title= findViewById(R.id.j_title);
        k_title= findViewById(R.id.k_title);
        l_title= findViewById(R.id.l_title);
        m_title= findViewById(R.id.m_title);
        n_title= findViewById(R.id.n_title);
        o_title= findViewById(R.id.o_title);
        p_title= findViewById(R.id.p_title);
        q_title= findViewById(R.id.q_title);
        r_title= findViewById(R.id.r_title);
        s_title= findViewById(R.id.s_title);
        t_title= findViewById(R.id.t_title);
        u_title= findViewById(R.id.u_title);
        v_title= findViewById(R.id.v_title);
        w_title= findViewById(R.id.w_title);
        x_title= findViewById(R.id.x_title);
        y_title= findViewById(R.id.y_title);
        z_title= findViewById(R.id.z_title);
        aa_title= findViewById(R.id.aa_title);
        ab_title= findViewById(R.id.ab_title);
        ac_title= findViewById(R.id.ac_title);
        ad_title= findViewById(R.id.ad_title);
        ae_title= findViewById(R.id.ae_title);
        af_title= findViewById(R.id.af_title);
        ag_title= findViewById(R.id.ag_title);
        ah_title= findViewById(R.id.ah_title);

        a_text= findViewById(R.id.a_text);
        b_text= findViewById(R.id.b_text);
        c_text= findViewById(R.id.c_text);
        d_text= findViewById(R.id.d_text);
        e_text= findViewById(R.id.e_text);
        f_text= findViewById(R.id.f_text);
        g_text= findViewById(R.id.g_text);
        h_text= findViewById(R.id.h_text);
        i_text= findViewById(R.id.i_text);
        j_text= findViewById(R.id.j_text);
        k_text= findViewById(R.id.k_text);
        l_text= findViewById(R.id.l_text);
        m_text= findViewById(R.id.m_text);
        n_text= findViewById(R.id.n_text);
        o_text= findViewById(R.id.o_text);
        p_text= findViewById(R.id.p_text);
        q_text= findViewById(R.id.q_text);
        r_text= findViewById(R.id.r_text);
        s_text= findViewById(R.id.s_text);
        t_text= findViewById(R.id.t_text);
        u_text= findViewById(R.id.u_text);
        v_text= findViewById(R.id.v_text);
        w_text= findViewById(R.id.w_text);
        x_text= findViewById(R.id.x_text);
        y_text= findViewById(R.id.y_text);
        z_text= findViewById(R.id.z_text);
        aa_text= findViewById(R.id.aa_text);
        ab_text= findViewById(R.id.ab_text);
        ac_text= findViewById(R.id.ac_text);
        ad_text= findViewById(R.id.ad_text);
        ae_text= findViewById(R.id.ae_text);
        af_text= findViewById(R.id.af_text);
        ag_text= findViewById(R.id.ag_text);
        ah_text= findViewById(R.id.ah_text);

        a_line= findViewById(R.id.a_line);
        b_line= findViewById(R.id.b_line);
        c_line= findViewById(R.id.c_line);
        d_line= findViewById(R.id.d_line);
        e_line= findViewById(R.id.e_line);
        f_line= findViewById(R.id.f_line);
        g_line= findViewById(R.id.g_line);
        h_line= findViewById(R.id.h_line);
        i_line= findViewById(R.id.i_line);
        j_line= findViewById(R.id.j_line);
        k_line= findViewById(R.id.k_line);
        l_line= findViewById(R.id.l_line);
        m_line= findViewById(R.id.m_line);
        n_line= findViewById(R.id.n_line);
        o_line= findViewById(R.id.o_line);
        p_line= findViewById(R.id.p_line);
        q_line= findViewById(R.id.q_line);
        r_line= findViewById(R.id.r_line);
        s_line= findViewById(R.id.s_line);
        t_line= findViewById(R.id.t_line);
        u_line= findViewById(R.id.u_line);
        v_line= findViewById(R.id.v_line);
        w_line= findViewById(R.id.w_line);
        x_line= findViewById(R.id.x_line);
        y_line= findViewById(R.id.y_line);
        z_line= findViewById(R.id.z_line);
        aa_line= findViewById(R.id.aa_line);
        ab_line= findViewById(R.id.ab_line);
        ac_line= findViewById(R.id.ac_line);
        ad_line= findViewById(R.id.ad_line);
        ae_line= findViewById(R.id.ae_line);
        af_line= findViewById(R.id.af_line);
        ag_line= findViewById(R.id.ag_line);
        ah_line= findViewById(R.id.ah_line);

        Intent intent = getIntent();
        xiangQingYe = (XiangQingYe) myApplication.getObj();

        if(!(xiangQingYe.getA_title() == null)){
            a_line.setVisibility(View.VISIBLE);
            a_text.setText(xiangQingYe.getA());
            a_title.setText(xiangQingYe.getA_title());
        }

        if(!(xiangQingYe.getB_title() == null)){
            b_line.setVisibility(View.VISIBLE);
            b_text.setText(xiangQingYe.getB());
            b_title.setText(xiangQingYe.getB_title());
        }

        if(!(xiangQingYe.getC_title() == null)){
            c_line.setVisibility(View.VISIBLE);
            c_text.setText(xiangQingYe.getC());
            c_title.setText(xiangQingYe.getC_title());
        }

        if(!(xiangQingYe.getD_title() == null)){
            d_line.setVisibility(View.VISIBLE);
            d_text.setText(xiangQingYe.getD());
            d_title.setText(xiangQingYe.getD_title());
        }

        if(!(xiangQingYe.getE_title() == null)){
            e_line.setVisibility(View.VISIBLE);
            e_text.setText(xiangQingYe.getE());
            e_title.setText(xiangQingYe.getE_title());
        }

        if(!(xiangQingYe.getF_title() == null)){
            f_line.setVisibility(View.VISIBLE);
            f_text.setText(xiangQingYe.getF());
            f_title.setText(xiangQingYe.getF_title());
        }

        if(!(xiangQingYe.getG_title() == null)){
            g_line.setVisibility(View.VISIBLE);
            g_text.setText(xiangQingYe.getG());
            g_title.setText(xiangQingYe.getG_title());
        }

        if(!(xiangQingYe.getH_title() == null)){
            h_line.setVisibility(View.VISIBLE);
            h_text.setText(xiangQingYe.getH());
            h_title.setText(xiangQingYe.getH_title());
        }

        if(!(xiangQingYe.getI_title() == null)){
            i_line.setVisibility(View.VISIBLE);
            i_text.setText(xiangQingYe.getI());
            i_title.setText(xiangQingYe.getI_title());
        }

        if(!(xiangQingYe.getJ_title() == null)){
            j_line.setVisibility(View.VISIBLE);
            j_text.setText(xiangQingYe.getJ());
            j_title.setText(xiangQingYe.getJ_title());
        }

        if(!(xiangQingYe.getK_title() == null)){
            k_line.setVisibility(View.VISIBLE);
            k_text.setText(xiangQingYe.getK());
            k_title.setText(xiangQingYe.getK_title());
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

    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }

}
