package com.example.myapplication.renshi.activity;

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
import com.example.myapplication.XiangQingYeActivity;
import com.example.myapplication.entity.XiangQingYe;
import com.example.myapplication.renshi.entity.YhRenShiGongZiMingXi;
import com.example.myapplication.renshi.entity.YhRenShiUser;
import com.example.myapplication.renshi.service.YhRenShiGongZiMingXiService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GongZiMingXiActivity extends AppCompatActivity {

    private final static int REQUEST_CODE_CHANG = 1;

    private YhRenShiUser yhRenShiUser;
    private YhRenShiGongZiMingXiService yhRenShiGongZiMingXiService;
    private ListView listView;
    private EditText yuangong_name;
    private String yuangong_nameText;
    private Spinner type_select;
    private String type_selectText;
    private Button sel_button;
    List<YhRenShiGongZiMingXi> list;

    String[] gongzimingxi_typeArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gongzimingxi);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //初始化控件
        listView = findViewById(R.id.gongzimingxi_list);
        yuangong_name = findViewById(R.id.yuangong_name);

        type_select = findViewById(R.id.type_select);
        gongzimingxi_typeArray = getResources().getStringArray(R.array.gongzimingxi_type_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, gongzimingxi_typeArray);
        type_select.setAdapter(adapter);

        sel_button = findViewById(R.id.sel_button);
        sel_button.setOnClickListener(selClick());
        sel_button.requestFocus();

        MyApplication myApplication = (MyApplication) getApplication();
        yhRenShiUser = myApplication.getYhRenShiUser();
        initList();
    }

    public View.OnClickListener selClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initList();
            }
        };
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
        LoadingDialog.getInstance(this).show();
        yuangong_nameText = yuangong_name.getText().toString();
        type_selectText = type_select.getSelectedItem().toString();
        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                listView.setAdapter(StringUtils.cast(msg.obj));
                LoadingDialog.getInstance(getApplicationContext()).dismiss();
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<HashMap<String, Object>> data = new ArrayList<>();
                try {
                    yhRenShiGongZiMingXiService = new YhRenShiGongZiMingXiService();
                    list = yhRenShiGongZiMingXiService.gongzimingxiQueryList(yhRenShiUser.getL(),type_selectText,yuangong_nameText);
                    if (list == null) return;

                    for (int i = 0; i < list.size(); i++) {
                        HashMap<String, Object> item = new HashMap<>();
                        item.put("B", list.get(i).getB());
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
                        item.put("R", list.get(i).getR());
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
                        item.put("ASA", list.get(i).getAsa());
                        item.put("ATA", list.get(i).getAta());
                        item.put("AU", list.get(i).getAu());
                        item.put("AV", list.get(i).getAv());
                        item.put("AW", list.get(i).getAw());
                        item.put("AX", list.get(i).getAx());
                        item.put("AY", list.get(i).getAy());
                        item.put("AZ", list.get(i).getAz());
                        item.put("BA", list.get(i).getBa());
                        item.put("BB", list.get(i).getBb());
                        item.put("BC", list.get(i).getBc());
                        data.add(item);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                SimpleAdapter adapter = new SimpleAdapter(GongZiMingXiActivity.this, data, R.layout.gongzimingxi_row, new String[]{"B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","AA","AB","AC","AD","AE","AF","AG","AH","AI","AJ","AK","AL","AM","AN","AO","AP","AQ","AR","ASA","ATA","AU","AV","AW","AX","AY","AZ","BA","BB","BC"}, new int[]{R.id.B,R.id.C, R.id.D, R.id.E, R.id.F, R.id.G, R.id.H, R.id.I, R.id.J, R.id.K, R.id.L, R.id.M, R.id.N, R.id.O, R.id.P, R.id.Q, R.id.R, R.id.S, R.id.T, R.id.U, R.id.V, R.id.W, R.id.X, R.id.Y, R.id.Z, R.id.AA, R.id.AB, R.id.AC, R.id.AD, R.id.AE, R.id.AF, R.id.AG, R.id.AH, R.id.AI, R.id.AJ, R.id.AK, R.id.AL, R.id.AM, R.id.AN, R.id.AO, R.id.AP, R.id.AQ, R.id.AR, R.id.ASA, R.id.ATA, R.id.AU, R.id.AV, R.id.AW, R.id.AX, R.id.AY, R.id.AZ, R.id.BA, R.id.BB, R.id.BC}) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        final LinearLayout view = (LinearLayout) super.getView(position, convertView, parent);
                        LinearLayout linearLayout = (LinearLayout) view.getChildAt(0);
                        linearLayout.setOnLongClickListener(onItemLongClick());
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

    public void onInsertClick(View v) {
        Intent intent = new Intent(GongZiMingXiActivity.this, GongZiMingXiChangeActivity.class);
        intent.putExtra("type", R.id.insert_btn);
        startActivityForResult(intent, REQUEST_CODE_CHANG);
    }

    public View.OnClickListener updateClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = Integer.parseInt(view.getTag().toString());
                Intent intent = new Intent(GongZiMingXiActivity.this, GongZiMingXiChangeActivity.class);
                intent.putExtra("type", R.id.update_btn);
                MyApplication myApplication = (MyApplication) getApplication();
                myApplication.setObj(list.get(position));
                startActivityForResult(intent, REQUEST_CODE_CHANG);
            }
        };
    }

    public View.OnLongClickListener onItemLongClick() {
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(GongZiMingXiActivity.this);
                int position = Integer.parseInt(view.getTag().toString());
                Handler deleteHandler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message msg) {
                        if ((boolean) msg.obj) {
                            ToastUtil.show(GongZiMingXiActivity.this, "删除成功");
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
                                msg.obj = yhRenShiGongZiMingXiService.delete(list.get(position).getId());
                                deleteHandler.sendMessage(msg);
                            }
                        }).start();
                    }
                });

                builder.setNeutralButton("查看详情", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        XiangQingYe xiangQingYe = new XiangQingYe();

                        xiangQingYe.setA_title("姓名:");
                        xiangQingYe.setB_title("部门:");
                        xiangQingYe.setC_title("岗位:");
                        xiangQingYe.setD_title("身份证号:");
                        xiangQingYe.setE_title("入职时间:");
                        xiangQingYe.setF_title("基本工资:");
                        xiangQingYe.setG_title("绩效工资:");
                        xiangQingYe.setH_title("岗位工资:");
                        xiangQingYe.setI_title("标准工资:");
                        xiangQingYe.setJ_title("跨度工资:");
                        xiangQingYe.setK_title("职称津贴:");
                        xiangQingYe.setL_title("月出勤天数:");
                        xiangQingYe.setM_title("加班时间:");
                        xiangQingYe.setN_title("加班费:");
                        xiangQingYe.setO_title("全勤应发:");
                        xiangQingYe.setP_title("缺勤天数:");
                        xiangQingYe.setQ_title("缺勤扣款:");
                        xiangQingYe.setR_title("迟到天数:");
                        xiangQingYe.setS_title("迟到扣款:");
                        xiangQingYe.setT_title("应发工资:");
                        xiangQingYe.setU_title("社保基数:");
                        xiangQingYe.setV_title("医疗基数:");
                        xiangQingYe.setW_title("公积金基数:");
                        xiangQingYe.setX_title("年金基数:");
                        xiangQingYe.setY_title("企业养老:");
                        xiangQingYe.setZ_title("企业失业:");
                        xiangQingYe.setAa_title("企业医疗:");
                        xiangQingYe.setAb_title("企业工伤:");
                        xiangQingYe.setAc_title("企业生育:");
                        xiangQingYe.setAd_title("企业公积金:");
                        xiangQingYe.setAe_title("企业年金:");
                        xiangQingYe.setAf_title("滞纳金:");
                        xiangQingYe.setAg_title("利息:");
                        xiangQingYe.setAh_title("企业小计:");

                        xiangQingYe.setA(list.get(position).getB());
                        xiangQingYe.setB(list.get(position).getC());
                        xiangQingYe.setC(list.get(position).getD());
                        xiangQingYe.setD(list.get(position).getE());
                        xiangQingYe.setE(list.get(position).getF());
                        xiangQingYe.setF(list.get(position).getG());
                        xiangQingYe.setG(list.get(position).getH());
                        xiangQingYe.setH(list.get(position).getI());
                        xiangQingYe.setI(list.get(position).getJ());
                        xiangQingYe.setJ(list.get(position).getK());
                        xiangQingYe.setK(list.get(position).getL());
                        xiangQingYe.setL(list.get(position).getM());
                        xiangQingYe.setM(list.get(position).getN());
                        xiangQingYe.setN(list.get(position).getO());
                        xiangQingYe.setO(list.get(position).getP());
                        xiangQingYe.setP(list.get(position).getQ());
                        xiangQingYe.setQ(list.get(position).getR());
                        xiangQingYe.setR(list.get(position).getS());
                        xiangQingYe.setS(list.get(position).getT());
                        xiangQingYe.setT(list.get(position).getU());
                        xiangQingYe.setU(list.get(position).getV());
                        xiangQingYe.setV(list.get(position).getW());
                        xiangQingYe.setW(list.get(position).getX());
                        xiangQingYe.setX(list.get(position).getY());
                        xiangQingYe.setY(list.get(position).getZ());
                        xiangQingYe.setZ(list.get(position).getAa());
                        xiangQingYe.setAa(list.get(position).getAb());
                        xiangQingYe.setAb(list.get(position).getAc());
                        xiangQingYe.setAc(list.get(position).getAd());
                        xiangQingYe.setAd(list.get(position).getAe());
                        xiangQingYe.setAe(list.get(position).getAf());
                        xiangQingYe.setAf(list.get(position).getAg());
                        xiangQingYe.setAg(list.get(position).getAh());
                        xiangQingYe.setAh(list.get(position).getAi());

                        Intent intent = new Intent(GongZiMingXiActivity.this, XiangQingYeActivity.class);
                        MyApplication myApplication = (MyApplication) getApplication();
                        myApplication.setObj(xiangQingYe);
                        startActivityForResult(intent, REQUEST_CODE_CHANG);
                    }
                });

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setMessage("确定删除吗？");
                builder.setTitle("提示");
                builder.show();
                return true;
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
