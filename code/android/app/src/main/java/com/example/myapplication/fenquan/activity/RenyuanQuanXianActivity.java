package com.example.myapplication.fenquan.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.fenquan.entity.Copy1;
import com.example.myapplication.fenquan.entity.Renyuan;
import com.example.myapplication.fenquan.service.Copy1Service;
import com.example.myapplication.fenquan.service.RenyuanService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RenyuanQuanXianActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_CHANG = 1;
    private Renyuan renyuan;
    private Copy1Service copy1Service;

    private EditText b_text;
    private ListView listView;

    private ListView listView_block;
    private HorizontalScrollView list_table;
    private SimpleAdapter adapter;
    private SimpleAdapter adapter_block;

    private Spinner quanxian_type;
    private Button sel_button;


    private List<Copy1> list;
    String[] quanxian_typeArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.renyuan_quanxian);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        copy1Service = new Copy1Service();

        b_text = findViewById(R.id.b_text);
        quanxian_type = findViewById(R.id.quanxian_type);
        listView = findViewById(R.id.list);

        listView_block = findViewById(R.id.list_block);
        list_table = findViewById(R.id.list_table);

        sel_button = findViewById(R.id.sel_button);

        quanxian_typeArray = getResources().getStringArray(R.array.quanxian_type_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, quanxian_typeArray);
        quanxian_type.setAdapter(adapter);

        MyApplication myApplication = (MyApplication) getApplication();
        renyuan = myApplication.getRenyuan();

        initList();
        sel_button.setOnClickListener(selClick());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("WrongConstant")
    public void switchClick(View v) {
        if(listView_block.getVisibility() == 0){
            listView_block.setVisibility(8);
            list_table.setVisibility(0);
        }else if(listView_block.getVisibility() == 8){
            listView_block.setVisibility(0);
            list_table.setVisibility(8);
        }

    }

    private void initList() {
        sel_button.setEnabled(false);
        listView.setAdapter(StringUtils.cast(adapter));
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                listView.setAdapter(StringUtils.cast(adapter));
                listView_block.setAdapter(StringUtils.cast(adapter_block));
                sel_button.setEnabled(true);
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<HashMap<String, Object>> data = new ArrayList<>();
                try {
                    copy1Service = new Copy1Service();
                    list = copy1Service.queryList(renyuan.getB(), quanxian_type.getSelectedItem().toString() ,b_text.getText().toString());
                    if (list == null) return;

                    for (int i = 0; i < list.size(); i++) {
                        HashMap<String, Object> item = new HashMap<>();
                        item.put("B", list.get(i).getB());
                        item.put("chashanquanxian", list.get(i).getChashanquanxian());
                        item.put("C", list.get(i).getC());
                        item.put("D", list.get(i).getD());
                        item.put("E", list.get(i).getE());
                        item.put("F", list.get(i).getF());
                        item.put("G", list.get(i).getG());
                        item.put("H", list.get(i).getH());
                        item.put("I", list.get(i).getI());
                        item.put("J", list.get(i).getJ());
                        item.put("K", list.get(i).getK());
                        item.put("L", list.get(i).getL());
                        item.put("M", list.get(i).getM());
                        item.put("N", list.get(i).getN());
                        item.put("O", list.get(i).getO());
                        item.put("P", list.get(i).getP());
                        item.put("Q", list.get(i).getQ());
                        item.put("RR", list.get(i).getR());
                        item.put("S", list.get(i).getS());
                        item.put("T", list.get(i).getT());
                        item.put("U", list.get(i).getU());
                        item.put("V", list.get(i).getV());
                        item.put("W", list.get(i).getW());
                        item.put("X", list.get(i).getX());
                        item.put("Y", list.get(i).getY());
                        item.put("Z", list.get(i).getZ());

                        item.put("AA", list.get(i).getAa());
                        item.put("AB", list.get(i).getAb());
                        item.put("AC", list.get(i).getAc());
                        item.put("AD", list.get(i).getAd());
                        item.put("AE", list.get(i).getAe());
                        item.put("AF", list.get(i).getAf());
                        item.put("AG", list.get(i).getAg());
                        item.put("AH", list.get(i).getAh());
                        item.put("AI", list.get(i).getAi());
                        item.put("AJ", list.get(i).getAj());
                        item.put("AK", list.get(i).getAk());
                        item.put("AL", list.get(i).getAl());
                        item.put("AM", list.get(i).getAm());
                        item.put("AN", list.get(i).getAn());
                        item.put("AO", list.get(i).getAo());
                        item.put("AP", list.get(i).getAp());
                        item.put("AQ", list.get(i).getAq());
                        item.put("AR", list.get(i).getAr());
                        item.put("ASS", list.get(i).getAss());
                        item.put("AT", list.get(i).getAt());
                        item.put("AU", list.get(i).getAu());
                        item.put("AV", list.get(i).getAv());
                        item.put("AW", list.get(i).getAw());
                        item.put("AX", list.get(i).getAx());
                        item.put("AY", list.get(i).getAy());
                        item.put("AZ", list.get(i).getAz());

                        item.put("BA", list.get(i).getBa());
                        item.put("BB", list.get(i).getBb());
                        item.put("BC", list.get(i).getBc());
                        item.put("BD", list.get(i).getBd());
                        item.put("BE", list.get(i).getBe());
                        item.put("BF", list.get(i).getBf());
                        item.put("BG", list.get(i).getBg());
                        item.put("BH", list.get(i).getBh());
                        item.put("BI", list.get(i).getBi());
                        item.put("BJ", list.get(i).getBj());
                        item.put("BK", list.get(i).getBk());
                        item.put("BL", list.get(i).getBl());
                        item.put("BM", list.get(i).getBm());
                        item.put("BN", list.get(i).getBn());
                        item.put("BO", list.get(i).getBo());
                        item.put("BP", list.get(i).getBp());
                        item.put("BQ", list.get(i).getBq());
                        item.put("BR", list.get(i).getBr());
                        item.put("BS", list.get(i).getBs());
                        item.put("BT", list.get(i).getBt());
                        item.put("BU", list.get(i).getBu());
                        item.put("BV", list.get(i).getBv());
                        item.put("BW", list.get(i).getBw());
                        item.put("BX", list.get(i).getBx());
                        item.put("BYY", list.get(i).getByy());
                        item.put("BZ", list.get(i).getBz());

                        item.put("CA", list.get(i).getCa());
                        item.put("CB", list.get(i).getCb());
                        item.put("CC", list.get(i).getCc());
                        item.put("CD", list.get(i).getCd());
                        item.put("CE", list.get(i).getCe());
                        item.put("CF", list.get(i).getCf());
                        item.put("CG", list.get(i).getCg());
                        item.put("CH", list.get(i).getCh());
                        item.put("CI", list.get(i).getCi());
                        item.put("CJ", list.get(i).getCj());
                        item.put("CK", list.get(i).getCk());
                        item.put("CL", list.get(i).getCl());
                        item.put("CM", list.get(i).getCm());
                        item.put("CN", list.get(i).getCn());
                        item.put("CO", list.get(i).getCo());
                        item.put("CP", list.get(i).getCp());
                        item.put("CQ", list.get(i).getCq());
                        item.put("CR", list.get(i).getCr());
                        item.put("CS", list.get(i).getCs());
                        item.put("CT", list.get(i).getCt());
                        item.put("CU", list.get(i).getCu());
                        item.put("CV", list.get(i).getCv());
                        item.put("CW", list.get(i).getCw());
                        item.put("CX", list.get(i).getCx());
                        data.add(item);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                adapter = new SimpleAdapter(RenyuanQuanXianActivity.this, data, R.layout.renyuan_quanxian_row, new String[]{"B", "chashanquanxian", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "RR", "S", "T", "U", "V", "W", "X", "Y", "Z","AA","AB" ,"AC", "AD", "AE", "AF", "AG", "AH", "AI", "AJ", "AK", "AL", "AM", "AN", "AO", "AP", "AQ", "AR", "ASS", "AT", "AU", "AV", "AW", "AX", "AY", "AZ","BA","BB","BC", "BD", "BE", "BF", "BG", "BH", "BI", "BJ", "BK", "BL", "BM", "BN", "BO", "BP", "BQ", "BR", "BS", "BT", "BU", "BV", "BW", "BX", "BYY", "BZ" ,"CA","CB", "CC", "CD", "CE", "CF", "CG", "CH", "CI", "CJ", "CK", "CL", "CM", "CN", "CO", "CP", "CQ", "CR", "CS", "CT", "CU", "CV", "CW", "CX"}, new int[]{R.id.B, R.id.chashanquanxian, R.id.C, R.id.D, R.id.E, R.id.F, R.id.G, R.id.H, R.id.I, R.id.J, R.id.K, R.id.L, R.id.M, R.id.N, R.id.O, R.id.P, R.id.Q, R.id.RR, R.id.S, R.id.T, R.id.U, R.id.V, R.id.W, R.id.X, R.id.Y, R.id.Z, R.id.AA, R.id.AB, R.id.AC, R.id.AD, R.id.AE, R.id.AF, R.id.AG, R.id.AH, R.id.AI, R.id.AJ, R.id.AK, R.id.AL, R.id.AM, R.id.AN, R.id.AO, R.id.AP, R.id.AQ, R.id.AR, R.id.ASS, R.id.AT, R.id.AU, R.id.AV, R.id.AW, R.id.AX, R.id.AY, R.id.AZ, R.id.BA, R.id.BB, R.id.BC, R.id.BD, R.id.BE, R.id.BF, R.id.BG, R.id.BH, R.id.BI, R.id.BJ, R.id.BK, R.id.BL, R.id.BM, R.id.BN, R.id.BO, R.id.BP, R.id.BQ, R.id.BR, R.id.BS, R.id.BT, R.id.BU, R.id.BV, R.id.BW, R.id.BX, R.id.BYY, R.id.BZ, R.id.CA, R.id.CB, R.id.CC, R.id.CD, R.id.CE, R.id.CF, R.id.CG, R.id.CH, R.id.CI, R.id.CJ, R.id.CK, R.id.CL, R.id.CM, R.id.CN, R.id.CO, R.id.CP, R.id.CQ, R.id.CR, R.id.CS, R.id.CT, R.id.CU, R.id.CV, R.id.CW, R.id.CX}) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        final LinearLayout view = (LinearLayout) super.getView(position, convertView, parent);
                        LinearLayout linearLayout = (LinearLayout) view.getChildAt(0);
                        linearLayout.setOnClickListener(updateClick());
                        linearLayout.setTag(position);
                        return view;
                    }
                };

                adapter_block = new SimpleAdapter(RenyuanQuanXianActivity.this, data, R.layout.renyuan_quanxian_row_block, new String[]{"B", "chashanquanxian", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "RR", "S", "T", "U", "V", "W", "X", "Y", "Z","AA","AB" ,"AC", "AD", "AE", "AF", "AG", "AH", "AI", "AJ", "AK", "AL", "AM", "AN", "AO", "AP", "AQ", "AR", "ASS", "AT", "AU", "AV", "AW", "AX", "AY", "AZ","BA","BB","BC", "BD", "BE", "BF", "BG", "BH", "BI", "BJ", "BK", "BL", "BM", "BN", "BO", "BP", "BQ", "BR", "BS", "BT", "BU", "BV", "BW", "BX", "BYY", "BZ" ,"CA","CB", "CC", "CD", "CE", "CF", "CG", "CH", "CI", "CJ", "CK", "CL", "CM", "CN", "CO", "CP", "CQ", "CR", "CS", "CT", "CU", "CV", "CW", "CX"}, new int[]{R.id.B, R.id.chashanquanxian, R.id.C, R.id.D, R.id.E, R.id.F, R.id.G, R.id.H, R.id.I, R.id.J, R.id.K, R.id.L, R.id.M, R.id.N, R.id.O, R.id.P, R.id.Q, R.id.RR, R.id.S, R.id.T, R.id.U, R.id.V, R.id.W, R.id.X, R.id.Y, R.id.Z, R.id.AA, R.id.AB, R.id.AC, R.id.AD, R.id.AE, R.id.AF, R.id.AG, R.id.AH, R.id.AI, R.id.AJ, R.id.AK, R.id.AL, R.id.AM, R.id.AN, R.id.AO, R.id.AP, R.id.AQ, R.id.AR, R.id.ASS, R.id.AT, R.id.AU, R.id.AV, R.id.AW, R.id.AX, R.id.AY, R.id.AZ, R.id.BA, R.id.BB, R.id.BC, R.id.BD, R.id.BE, R.id.BF, R.id.BG, R.id.BH, R.id.BI, R.id.BJ, R.id.BK, R.id.BL, R.id.BM, R.id.BN, R.id.BO, R.id.BP, R.id.BQ, R.id.BR, R.id.BS, R.id.BT, R.id.BU, R.id.BV, R.id.BW, R.id.BX, R.id.BYY, R.id.BZ, R.id.CA, R.id.CB, R.id.CC, R.id.CD, R.id.CE, R.id.CF, R.id.CG, R.id.CH, R.id.CI, R.id.CJ, R.id.CK, R.id.CL, R.id.CM, R.id.CN, R.id.CO, R.id.CP, R.id.CQ, R.id.CR, R.id.CS, R.id.CT, R.id.CU, R.id.CV, R.id.CW, R.id.CX}) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        final LinearLayout view = (LinearLayout) super.getView(position, convertView, parent);
                        LinearLayout linearLayout = (LinearLayout) view.getChildAt(0);
                        linearLayout.setOnClickListener(updateClick());
                        linearLayout.setTag(position);
                        return view;
                    }
                };

                Message msg = new Message();
                msg.obj = adapter;
                listLoadHandler.sendMessage(msg);
            }
        }).start();
    }

    public View.OnClickListener selClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initList();
            }
        };
    }

    public View.OnClickListener updateClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = Integer.parseInt(view.getTag().toString());
                Intent intent = new Intent(RenyuanQuanXianActivity.this, RenyuanQuanXianChangeActivity.class);
                intent.putExtra("type", R.id.update_btn);
                MyApplication myApplication = (MyApplication) getApplication();
                myApplication.setObj(list.get(position));
                startActivityForResult(intent, REQUEST_CODE_CHANG);
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
