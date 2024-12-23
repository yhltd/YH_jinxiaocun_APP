package com.example.myapplication.jxc.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.XiangQingYeActivity;
import com.example.myapplication.entity.XiangQingYe;
import com.example.myapplication.jxc.entity.YhJinXiaoCun;
import com.example.myapplication.jxc.entity.YhJinXiaoCunMingXi;
import com.example.myapplication.jxc.entity.YhJinXiaoCunUser;
import com.example.myapplication.jxc.service.YhJinXiaoCunMingXiService;
import com.example.myapplication.jxc.service.YhJinXiaoCunService;
import com.example.myapplication.utils.ExcelUtil;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MingXiActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_CHANG = 1;
    //商品二维码
    private static final int REQUEST_CODE_SCAN = 101;
    private YhJinXiaoCunUser yhJinXiaoCunUser;
    private YhJinXiaoCunMingXiService yhJinXiaoCunMingXiService;

    private EditText ks;
    private EditText js;
//    private EditText ye;
//    private EditText yea;
    private ListView listView;

    private ListView listView_block;
    private HorizontalScrollView list_table;
    private SimpleAdapter adapter;
    private SimpleAdapter adapter_block;

    private Button sel_button;
    private Button export_button;
//    private Button up_button;
//    private Button down_button;
    List<YhJinXiaoCunMingXi> list;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mingxi);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        listView = findViewById(R.id.mingxi_list);
        listView_block = findViewById(R.id.list_block);
        list_table = findViewById(R.id.list_table);
        sel_button = findViewById(R.id.sel_button);
//        up_button = findViewById(R.id.up_button);
//        down_button=findViewById(R.id.down_button);
        export_button = findViewById(R.id.export_button);
        ks = findViewById(R.id.ks);
        js = findViewById(R.id.js);
//        ye = findViewById(R.id.ye);
//        yea=findViewById(R.id.yea);
        MyApplication myApplication = (MyApplication) getApplication();
        yhJinXiaoCunUser = myApplication.getYhJinXiaoCunUser();

        initList();
        sel_button.setOnClickListener(selClick());
        export_button.setOnClickListener(exportClick());
//        up_button.setOnClickListener(upClick());
//        down_button.setOnClickListener(downClick());
        showDateOnClick(ks);
        showDateOnClick(js);
       

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
        DatePickerDialog datePickerDialog = new DatePickerDialog(MingXiActivity.this, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                editText.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void initList() {
        sel_button.setEnabled(false);
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

//                int a1 = Integer.parseInt(ye.getText().toString());
//                if(a1!=1){
//                    a1=a1+5;
//                }

                try {
                    yhJinXiaoCunMingXiService = new YhJinXiaoCunMingXiService();
                    if (ks.getText().toString().equals("") && js.getText().toString().equals("")) {
                        if(yhJinXiaoCunUser.getGongsi()!=null) {
                            list = yhJinXiaoCunMingXiService.getList(yhJinXiaoCunUser.getGongsi());
                        }
                    } else {
                        if(yhJinXiaoCunUser.getGongsi()!=null) {
                        list = yhJinXiaoCunMingXiService.getQuery(yhJinXiaoCunUser.getGongsi(), ks.getText().toString(), js.getText().toString());
                        }
                    }
                    if (list == null) return;

                    for (int i = 0; i < list.size(); i++) {
//                    for (int i = a1;  i < a1+5; i++) {
                        HashMap<String, Object> item = new HashMap<>();
                        item.put("orderid", list.get(i).getOrderid());
                        item.put("spDm", list.get(i).getSpDm());
                        item.put("cpname", list.get(i).getCpname());
                        item.put("cplb", list.get(i).getCplb());
                        item.put("cpsj", list.get(i).getCpsj());
                        item.put("cpsl", list.get(i).getCpsl());
                        item.put("mxtype", list.get(i).getMxtype());
                        item.put("shijian", list.get(i).getShijian());
                        item.put("gs_name", list.get(i).getGsName());
                        item.put("shou_h", list.get(i).getShou_h());

                        data.add(item);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                adapter = new SimpleAdapter(MingXiActivity.this, data, R.layout.mingxi_row, new String[]{"orderid", "spDm", "cpname", "cplb", "cpsj", "cpsl", "mxtype", "shijian", "gs_name", "shou_h"}, new int[]{R.id.orderid, R.id.spDm, R.id.cpname, R.id.cplb, R.id.cpsj, R.id.cpsl, R.id.mxtype, R.id.shijian, R.id.gs_name, R.id.shou_h}) {
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

                adapter_block = new SimpleAdapter(MingXiActivity.this, data, R.layout.mingxi_row_block, new String[]{"orderid", "spDm", "cpname", "cplb", "cpsj", "cpsl", "mxtype", "shijian", "gs_name", "shou_h"}, new int[]{R.id.orderid, R.id.spDm, R.id.cpname, R.id.cplb, R.id.cpsj, R.id.cpsl, R.id.mxtype, R.id.shijian, R.id.gs_name, R.id.shou_h}) {
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

    public View.OnClickListener selClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initList();
            }
        };
    }
//    public View.OnClickListener downClick() {
//        return new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int currentValue = Integer.parseInt(ye.getText().toString());
//                ye.setText(String.valueOf(currentValue + 1));
//
//                initList();
//            }
//        };
//    }
//    public View.OnClickListener upClick() {
//        return new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int currentValue = Integer.parseInt(ye.getText().toString());
//                if (currentValue > 0) {
//                    ye.setText(String.valueOf(currentValue - 1));
//                    initList();
//                }
//            }
//        };
//    }
    public View.OnClickListener exportClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] title = {"订单号", "商品代码", "商品名称", "商品类别", "价格", "数量", "明细类型", "时间", "公司名","收/进货方"};
                String fileName = "明细" + System.currentTimeMillis() + ".xls";
                ExcelUtil.initExcel(fileName, "明细", title);
                ExcelUtil.mingxiToExcel(list, fileName, MyApplication.getContext());
                String filePath = null;
                try {
                    filePath = Environment.getExternalStorageDirectory().getCanonicalPath()+"/云合未来一体化系统/" + fileName;
                    Uri uri = Uri.parse("file://" + filePath);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(uri);
                    startActivity(intent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private String filePath;

    private File createFile() {
        //全路径文件
        File file = null;
        //写入文件名
        String fileName = "明细-" + System.currentTimeMillis() + ".xls";
        //实际写入路径
        filePath = "云合未来一体化系统/导出Excel/" + fileName;
        // 创建FileOutputStream对象
        FileOutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            //写入路径
            String path = Environment.getExternalStorageDirectory().getCanonicalPath() + "/云合未来一体化系统/导出Excel/";
            //创建路径
            File pathFile = new File(path);
            if (!pathFile.exists()) {
                pathFile.mkdirs();
            }





            //全路径文件
            file = new File(path, fileName);
            //创建一个空文件
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

    public View.OnClickListener updateClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = Integer.parseInt(view.getTag().toString());
                Intent intent = new Intent(MingXiActivity.this, MingXiChangeActivity.class);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(MingXiActivity.this);
                int position = Integer.parseInt(view.getTag().toString());
                Handler deleteHandler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message msg) {
                        if ((boolean) msg.obj) {
                            ToastUtil.show(MingXiActivity.this, "删除成功");
                            initList();
                        }
                        return true;
                    }
                });

                builder.setNeutralButton("查看详情", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        XiangQingYe xiangQingYe = new XiangQingYe();

                        xiangQingYe.setA_title("订单号:");
                        xiangQingYe.setB_title("商品代码:");
                        xiangQingYe.setC_title("商品名称:");
                        xiangQingYe.setD_title("商品类别:");
                        xiangQingYe.setE_title("价格:");
                        xiangQingYe.setF_title("数量:");
                        xiangQingYe.setG_title("明细类型:");
                        xiangQingYe.setH_title("时间:");
                        xiangQingYe.setI_title("公司名:");
                        xiangQingYe.setJ_title("收/进货方:");

                        xiangQingYe.setA(list.get(position).getOrderid());
                        xiangQingYe.setB(list.get(position).getSpDm());
                        xiangQingYe.setC(list.get(position).getCpname());
                        xiangQingYe.setD(list.get(position).getCplb());
                        xiangQingYe.setE(list.get(position).getCpsj());
                        xiangQingYe.setF(list.get(position).getCpsl());
                        xiangQingYe.setG(list.get(position).getMxtype());
                        xiangQingYe.setH(list.get(position).getShijian());
                        xiangQingYe.setI(list.get(position).getGsName());
                        xiangQingYe.setJ(list.get(position).getShou_h());

                        Intent intent = new Intent(MingXiActivity.this, XiangQingYeActivity.class);
                        MyApplication myApplication = (MyApplication) getApplication();
                        myApplication.setObj(xiangQingYe);
                        startActivityForResult(intent, REQUEST_CODE_CHANG);
                    }
                });

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Message msg = new Message();
                                msg.obj = yhJinXiaoCunMingXiService.delete(list.get(position).get_id());
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

                builder.setMessage("确定删除吗？");
                builder.setTitle("提示");
                builder.show();
                return true;
            }
        };
    }

    public void onOrderScanClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        MyApplication myApplication = (MyApplication) getApplication();
        List<YhJinXiaoCunMingXi> mingxi_list = new ArrayList<>();
        for(int i=0; i<list.size(); i++){
            YhJinXiaoCunMingXi this_mingxi = new YhJinXiaoCunMingXi();
            this_mingxi.setSpDm(list.get(i).getOrderid());
            this_mingxi.setCpname(list.get(i).getShijian());
            mingxi_list.add(this_mingxi);
        }
        if(mingxi_list.size()>0){
            myApplication.setMingxiList(mingxi_list);
            intent.putExtra("type", "qrcode");
            intent.putExtra("title1", "订单号：");
            intent.putExtra("title2", "时间：");
            startActivityForResult(intent, REQUEST_CODE_SCAN);
        }
    }

    public void onOrderScanClick2(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        MyApplication myApplication = (MyApplication) getApplication();
        List<YhJinXiaoCunMingXi> mingxi_list = new ArrayList<>();
        for(int i=0; i<list.size(); i++){
            YhJinXiaoCunMingXi this_mingxi = new YhJinXiaoCunMingXi();
            this_mingxi.setSpDm(list.get(i).getOrderid());
            this_mingxi.setCpname(list.get(i).getShijian());
            mingxi_list.add(this_mingxi);
        }
        if(mingxi_list.size()>0){
            myApplication.setMingxiList(mingxi_list);
            intent.putExtra("type", "barcode");
            intent.putExtra("title1", "订单号：");
            intent.putExtra("title2", "时间：");
            startActivityForResult(intent, REQUEST_CODE_SCAN);
        }
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

    public static String convertTime(long time, String patter) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat(patter);
        return sdf.format(new Date(time));
    }

}
