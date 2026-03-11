package com.example.myapplication.finance.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.XiangQingYeActivity;
import com.example.myapplication.entity.XiangQingYe;
import com.example.myapplication.finance.entity.YhFinanceJiJianTaiZhang;
import com.example.myapplication.finance.entity.YhFinanceQuanXian;
import com.example.myapplication.finance.entity.YhFinanceUser;
import com.example.myapplication.finance.service.YhFinanceJiJianTaiZhangService;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class JiJianTaiZhangActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_CHANG = 1;
    private final static int REQUEST_CODE_FILE = 1001;
    private final static int REQUEST_CODE_GALLERY = 1002;

    MyApplication myApplication;

    private YhFinanceUser yhFinanceUser;
    private YhFinanceQuanXian yhFinanceQuanXian;
    private YhFinanceJiJianTaiZhangService yhFinanceJiJianTaiZhangService;

    private EditText project;
    private EditText start_date;
    private EditText stop_date;
    private ListView listView;

    private ListView listView_block;
    private HorizontalScrollView list_table;
    private SimpleAdapter adapter;
    private SimpleAdapter adapter_block;

    private String projectText;
    private String start_dateText;
    private String stop_dateText;

    private Button sel_button;
    private Button clear_button;
    List<YhFinanceJiJianTaiZhang> list;

    // 文件上传相关
    private YhFinanceJiJianTaiZhang currentRecordForUpload;
    private String currentUserFileName = "";
    private AlertDialog uploadProgressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        myApplication = (MyApplication) getApplication();
        yhFinanceUser = myApplication.getYhFinanceUser();
        yhFinanceQuanXian = myApplication.getYhFinanceQuanXian();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jijiantaizhang);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //初始化控件
        project = findViewById(R.id.project);
        start_date = findViewById(R.id.start_date);
        stop_date = findViewById(R.id.stop_date);
        listView = findViewById(R.id.jijiantaizhang_list);
        listView_block = findViewById(R.id.list_block);
        list_table = findViewById(R.id.list_table);
        sel_button = findViewById(R.id.sel_button);
        sel_button.setOnClickListener(selClick());
        sel_button.requestFocus();
        clear_button = findViewById(R.id.clear_button);
        clear_button.setOnClickListener(clearClick());
        showDateOnClick(start_date);
        showDateOnClick(stop_date);
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(JiJianTaiZhangActivity.this, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                editText.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public View.OnClickListener selClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initList();
            }
        };
    }

    public View.OnClickListener clearClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 清空搜索框的值
                start_date.setText("");
                stop_date.setText("");
            }
        };
    }

    private void initList() {
        sel_button.setEnabled(false);
        projectText = project.getText().toString();
        start_dateText = start_date.getText().toString();
        stop_dateText = stop_date.getText().toString();
        if(start_dateText.equals("")){
            start_dateText = "1900/01/01";
        }
        if(stop_dateText.equals("")){
            stop_dateText = "2100/12/31";
        }
        if (!start_dateText.isEmpty() && !stop_dateText.isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                Date startDate = sdf.parse(start_dateText);
                Date endDate = sdf.parse(stop_dateText);

                if (startDate.after(endDate)) {
                    ToastUtil.show(JiJianTaiZhangActivity.this, "开始时间不能大于结束时间");
                    return;
                }
            } catch (ParseException e) {
                e.printStackTrace();
                ToastUtil.show(JiJianTaiZhangActivity.this, "日期格式错误，请使用yyyy/MM/dd格式");
                return;
            }
        }
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
                    yhFinanceJiJianTaiZhangService = new YhFinanceJiJianTaiZhangService();
                    list = yhFinanceJiJianTaiZhangService.getList(yhFinanceUser.getCompany(),projectText,start_dateText,stop_dateText);
                    if (list == null) return;

                } catch (Exception e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < list.size(); i++) {
                    HashMap<String, Object> item = new HashMap<>();
                    item.put("company", list.get(i).getCompany());
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date(list.get(i).getInsert_date().getTime());
                    String java_date = sdf.format(date);
                    item.put("insert_date", java_date);
                    item.put("project", list.get(i).getProject());
                    item.put("kehu", list.get(i).getKehu());
                    item.put("receipts", list.get(i).getReceipts());
                    item.put("receivable", list.get(i).getReceivable());
                    item.put("weishou", list.get(i).getReceivable().subtract(list.get(i).getReceipts()));
                    item.put("cope", list.get(i).getCope());
                    item.put("payment", list.get(i).getPayment());
                    item.put("weifu", list.get(i).getCope().subtract(list.get(i).getPayment()));
                    item.put("accounting", list.get(i).getAccounting());
                    item.put("nashuijine", list.get(i).getNashuijine());
                    item.put("yijiaoshuijine", list.get(i).getYijiaoshuijine());
                    BigDecimal nashuijine = list.get(i).getNashuijine();
                    BigDecimal yijiaoshuijine = list.get(i).getYijiaoshuijine();
                    BigDecimal safeNashuijine = nashuijine != null ? nashuijine : BigDecimal.ZERO;
                    BigDecimal safeYijiaoshuijine = yijiaoshuijine != null ? yijiaoshuijine : BigDecimal.ZERO;
                    BigDecimal weijiaoshuijine = safeNashuijine.subtract(safeYijiaoshuijine);
                    item.put("weijiaoshuijine", weijiaoshuijine);
                    item.put("zhaiyao", list.get(i).getZhaiyao());
                    data.add(item);
                }

                adapter = new SimpleAdapter(JiJianTaiZhangActivity.this, data, R.layout.jijiantaizhang_row,
                        new String[]{"insert_date","kehu","project","receivable","receipts","weishou","cope","payment","weifu","accounting","nashuijine","yijiaoshuijine","weijiaoshuijine","zhaiyao"},
                        new int[]{R.id.insert_date,R.id.kehu,R.id.project,R.id.receivable,R.id.receipts,R.id.weishou,R.id.cope,R.id.payment,R.id.weifu,R.id.accounting,R.id.nashuijine,R.id.yijiaoshuijine,R.id.weijiaoshuijine,R.id.zhaiyao}) {

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        final LinearLayout view = (LinearLayout) super.getView(position, convertView, parent);
                        LinearLayout linearLayout = (LinearLayout) view.getChildAt(0);

                        // 获取文件图标并设置点击事件
                        ImageView fileIcon = view.findViewById(R.id.file_icon);
                        if (fileIcon != null) {
                            fileIcon.setTag(position);
                            fileIcon.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    int pos = (int) v.getTag();
                                    handleFileAction(list.get(pos));
                                }
                            });
                        }

                        linearLayout.setOnLongClickListener(onItemLongClick());
                        linearLayout.setOnClickListener(updateClick());
                        linearLayout.setTag(position);
                        return view;
                    }
                };

                adapter_block = new SimpleAdapter(JiJianTaiZhangActivity.this, data, R.layout.jijiantaizhang_row_block,
                        new String[]{"insert_date","kehu","project","receivable","receipts","weishou","cope","payment","weifu","accounting","nashuijine","yijiaoshuijine","weijiaoshuijine","zhaiyao"},
                        new int[]{R.id.insert_date,R.id.kehu,R.id.project,R.id.receivable,R.id.receipts,R.id.weishou,R.id.cope,R.id.payment,R.id.weifu,R.id.accounting,R.id.nashuijine,R.id.yijiaoshuijine,R.id.weijiaoshuijine,R.id.zhaiyao}) {

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        final LinearLayout view = (LinearLayout) super.getView(position, convertView, parent);

                        // 获取文件图标并设置点击事件
                        ImageView fileIcon = view.findViewById(R.id.file_icon);
                        if (fileIcon != null) {
                            fileIcon.setTag(position);
                            fileIcon.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    int pos = (int) v.getTag();
                                    handleFileAction(list.get(pos));
                                }
                            });
                        }

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

    public View.OnLongClickListener onItemLongClick() {
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(!yhFinanceQuanXian.getJjtzDelete().equals("是")){
                    ToastUtil.show(JiJianTaiZhangActivity.this, "无权限！");
                    return true;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(JiJianTaiZhangActivity.this);
                int position = Integer.parseInt(view.getTag().toString());
                Handler deleteHandler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message msg) {
                        if ((boolean) msg.obj) {
                            ToastUtil.show(JiJianTaiZhangActivity.this, "删除成功");
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
                                msg.obj = yhFinanceJiJianTaiZhangService.delete(list.get(position).getId());
                                deleteHandler.sendMessage(msg);
                            }
                        }).start();
                    }
                });

                builder.setNeutralButton("查看详情", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        showDetail(position);
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

    private void showDetail(int position) {
        XiangQingYe xiangQingYe = new XiangQingYe();

        xiangQingYe.setA_title("日期:");
        xiangQingYe.setB_title("客户/供应商:");
        xiangQingYe.setC_title("项目名称:");
        xiangQingYe.setD_title("应收:");
        xiangQingYe.setE_title("实收:");
        xiangQingYe.setF_title("未收:");
        xiangQingYe.setG_title("应付:");
        xiangQingYe.setH_title("实付:");
        xiangQingYe.setI_title("未付:");
        xiangQingYe.setJ_title("科目:");
        xiangQingYe.setK_title("纳税金额:");
        xiangQingYe.setL_title("已交税金额:");
        xiangQingYe.setM_title("未交税金额:");
        xiangQingYe.setN_title("摘要:");

        xiangQingYe.setA(list.get(position).getInsert_date().toString());
        xiangQingYe.setB(list.get(position).getProject());
        xiangQingYe.setC(list.get(position).getKehu());
        xiangQingYe.setD(list.get(position).getReceipts().toString());
        xiangQingYe.setE(list.get(position).getReceivable().toString());
        xiangQingYe.setF(list.get(position).getReceivable().subtract(list.get(position).getReceipts()).toString());
        xiangQingYe.setG(list.get(position).getCope().toString());
        xiangQingYe.setH(list.get(position).getPayment().toString());
        xiangQingYe.setI(list.get(position).getCope().subtract(list.get(position).getPayment()).toString());
        xiangQingYe.setJ(list.get(position).getAccounting());
        xiangQingYe.setK(
                list.get(position).getNashuijine() != null ?
                        list.get(position).getNashuijine().toString() :
                        "0"
        );
        xiangQingYe.setL(
                list.get(position).getYijiaoshuijine() != null ?
                        list.get(position).getYijiaoshuijine().toString() :
                        "0"
        );
        BigDecimal nashuijine = list.get(position).getNashuijine();
        BigDecimal yijiaoshuijine = list.get(position).getYijiaoshuijine();
        BigDecimal safeNashuijine = nashuijine != null ? nashuijine : BigDecimal.ZERO;
        BigDecimal safeYijiaoshuijine = yijiaoshuijine != null ? yijiaoshuijine : BigDecimal.ZERO;
        BigDecimal result = safeNashuijine.subtract(safeYijiaoshuijine);
        xiangQingYe.setM(result.toString());
        xiangQingYe.setN(list.get(position).getZhaiyao());

        Intent intent = new Intent(JiJianTaiZhangActivity.this, XiangQingYeActivity.class);
        MyApplication myApplication = (MyApplication) getApplication();
        myApplication.setObj(xiangQingYe);
        startActivityForResult(intent, REQUEST_CODE_CHANG);
    }

    public View.OnClickListener updateClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!yhFinanceQuanXian.getJjtzUpdate().equals("是")){
                    ToastUtil.show(JiJianTaiZhangActivity.this, "无权限！");
                    return;
                }
                int position = Integer.parseInt(view.getTag().toString());
                Intent intent = new Intent(JiJianTaiZhangActivity.this, JiJianTaiZhangChangeActivity.class);
                intent.putExtra("type", R.id.update_btn);
                MyApplication myApplication = (MyApplication) getApplication();
                myApplication.setObj(list.get(position));
                startActivityForResult(intent, REQUEST_CODE_CHANG);
            }
        };
    }

    public void onInsertClick(View v) {
        if(!yhFinanceQuanXian.getJjtzAdd().equals("是")){
            ToastUtil.show(JiJianTaiZhangActivity.this, "无权限！");
            return;
        }
        Intent intent = new Intent(JiJianTaiZhangActivity.this, JiJianTaiZhangChangeActivity.class);
        intent.putExtra("type", R.id.insert_btn);
        startActivityForResult(intent, REQUEST_CODE_CHANG);
    }

    // ==================== 文件上传相关方法 ====================

    private void handleFileAction(YhFinanceJiJianTaiZhang record) {
        String files = record.getWenjian();
        if (files == null || files.trim().isEmpty()) {
            // 没有文件，显示上传对话框
            currentRecordForUpload = record;
            currentUserFileName = "";
            showFileUploadDialog(record);
        } else {
            // 有文件，显示文件管理对话框
            showFileListDialog(record);
        }
    }

    private void showFileUploadDialog(final YhFinanceJiJianTaiZhang record) {
        currentRecordForUpload = record;
        currentUserFileName = "";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("上传文件 - " + record.getProject());

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_file_upload_finance, null);
        final EditText etFileName = dialogView.findViewById(R.id.et_file_name);

        builder.setView(dialogView);

        builder.setPositiveButton("选择文件", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                currentUserFileName = etFileName.getText().toString().trim();
                showFileSourceDialog();
            }
        });

        builder.setNegativeButton("取消", null);
        builder.show();
    }

    private void showFileSourceDialog() {
        String[] options = {"从文件管理器选择", "从相册选择"};

        new AlertDialog.Builder(this)
                .setTitle("选择文件来源")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                openFilePicker();
                                break;
                            case 1:
                                openGallery();
                                break;
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "选择文件"), REQUEST_CODE_FILE);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_CHANG) {
                initList();
            } else if (currentRecordForUpload != null) {
                if (requestCode == REQUEST_CODE_FILE && data != null) {
                    Uri uri = data.getData();
                    if (uri != null) {
                        String filePath = getPathFromUri(uri);
                        if (filePath != null) {
                            uploadFile(new File(filePath));
                        }
                    }
                } else if (requestCode == REQUEST_CODE_GALLERY && data != null) {
                    Uri uri = data.getData();
                    if (uri != null) {
                        String filePath = getPathFromUri(uri);
                        if (filePath != null) {
                            uploadFile(new File(filePath));
                        }
                    }
                }
            }
        }
    }

    private String getPathFromUri(Uri uri) {
        String filePath = null;

        try {
            if ("content".equalsIgnoreCase(uri.getScheme())) {
                ContentResolver resolver = getContentResolver();

                String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DISPLAY_NAME};
                Cursor cursor = resolver.query(uri, projection, null, null, null);

                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                    filePath = cursor.getString(columnIndex);

                    if (filePath == null) {
                        int nameIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);
                        String fileName = cursor.getString(nameIndex);

                        File tempFile = new File(getCacheDir(), fileName);
                        copyFileFromUri(uri, tempFile);
                        filePath = tempFile.getAbsolutePath();
                    }

                    cursor.close();
                }
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                filePath = uri.getPath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return filePath;
    }

    private void copyFileFromUri(Uri uri, File destFile) {
        try {
            InputStream in = getContentResolver().openInputStream(uri);
            OutputStream out = new FileOutputStream(destFile);

            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }

            in.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void uploadFile(final File file) {
        if (file == null || !file.exists()) {
            ToastUtil.show(this, "文件不存在");
            return;
        }

        showUploadProgressDialog();

        String fileName = file.getName();
        String kongjian = "3";  // 空间标识
        String recordId = String.valueOf(currentRecordForUpload.getId());
        String recordName = currentRecordForUpload.getProject() + "_" + currentRecordForUpload.getKehu();

        yhFinanceJiJianTaiZhangService.uploadFile(file, fileName, "", kongjian,
                recordId, recordName, currentUserFileName,
                new YhFinanceJiJianTaiZhangService.UploadCallback() {
                    @Override
                    public void onSuccess(String fileUrl) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hideUploadProgressDialog();

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        boolean success = yhFinanceJiJianTaiZhangService.updateFilesWithCheck(
                                                currentRecordForUpload.getId(), fileUrl);

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (success) {
                                                    ToastUtil.show(JiJianTaiZhangActivity.this, "文件上传成功");
                                                    initList();  // 刷新列表
                                                } else {
                                                    ToastUtil.show(JiJianTaiZhangActivity.this, "文件信息保存失败");
                                                }
                                            }
                                        });
                                    }
                                }).start();
                            }
                        });
                    }

                    @Override
                    public void onFailure(String error) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hideUploadProgressDialog();
                                ToastUtil.show(JiJianTaiZhangActivity.this, "文件上传失败: " + error);
                            }
                        });
                    }
                });
    }

    private void showUploadProgressDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("上传中...");
        builder.setMessage("请稍候");
        builder.setCancelable(false);

        View progressView = getLayoutInflater().inflate(R.layout.dialog_progress, null);
        builder.setView(progressView);

        uploadProgressDialog = builder.create();
        uploadProgressDialog.show();
    }

    private void hideUploadProgressDialog() {
        if (uploadProgressDialog != null && uploadProgressDialog.isShowing()) {
            uploadProgressDialog.dismiss();
            uploadProgressDialog = null;
        }
    }

    private void showFileListDialog(final YhFinanceJiJianTaiZhang record) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("文件管理 - " + record.getProject());

        String files = record.getWenjian();
        if (files == null || files.trim().isEmpty()) {
            ToastUtil.show(this, "没有文件");
            return;
        }

        final String[] fileArray = files.split(",");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_file_list_finance, null);
        builder.setView(dialogView);

        final AlertDialog dialog = builder.create();

        LinearLayout fileListLayout = dialogView.findViewById(R.id.file_list_layout);
        Button btnUploadNew = dialogView.findViewById(R.id.btn_upload_new);

        fileListLayout.removeAllViews();

        for (int i = 0; i < fileArray.length; i++) {
            final String fileUrl = fileArray[i].trim();
            if (fileUrl.isEmpty()) continue;

            View fileItemView = getLayoutInflater().inflate(R.layout.item_file_list_finance, null);

            TextView tvFileName = fileItemView.findViewById(R.id.tv_file_name);
            Button btnPreview = fileItemView.findViewById(R.id.btn_preview);
            Button btnDelete = fileItemView.findViewById(R.id.btn_delete);

            String fileName = extractFileName(fileUrl);
            tvFileName.setText((i + 1) + ". " + fileName);

            btnPreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    previewFile(fileUrl);
                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteSingleFile(record, fileUrl, dialog);
                }
            });

            fileItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    previewFile(fileUrl);
                }
            });

            fileListLayout.addView(fileItemView);
        }

        btnUploadNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                currentRecordForUpload = record;
                currentUserFileName = "";
                showFileUploadDialog(record);
            }
        });

        dialog.show();
    }

    private String extractFileName(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return "未知文件";
        }

        int lastSlash = fileUrl.lastIndexOf('/');
        if (lastSlash != -1 && lastSlash < fileUrl.length() - 1) {
            return fileUrl.substring(lastSlash + 1);
        }

        return fileUrl;
    }

    private void previewFile(String fileUrl) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(fileUrl));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("文件预览");
            builder.setMessage("文件链接：\n" + fileUrl);

            builder.setPositiveButton("复制链接", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    android.content.ClipboardManager clipboard =
                            (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData
                            .newPlainText("文件链接", fileUrl);
                    clipboard.setPrimaryClip(clip);
                    ToastUtil.show(JiJianTaiZhangActivity.this, "链接已复制到剪贴板");
                }
            });

            builder.setNegativeButton("取消", null);
            builder.show();
        }
    }

    private void deleteSingleFile(final YhFinanceJiJianTaiZhang record, final String fileUrl, final AlertDialog parentDialog) {
        new AlertDialog.Builder(this)
                .setTitle("确认删除")
                .setMessage("确定要删除这个文件吗？\n" + extractFileName(fileUrl))
                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ToastUtil.show(JiJianTaiZhangActivity.this, "正在删除文件...");

                        String fileName = extractFileName(fileUrl);
                        String path = "/caiwu/";

                        yhFinanceJiJianTaiZhangService.deleteFileFromServer(fileName, path,
                                new YhFinanceJiJianTaiZhangService.DeleteCallback() {
                                    @Override
                                    public void onSuccess() {
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                boolean dbSuccess = yhFinanceJiJianTaiZhangService.deleteFileWithCheck(
                                                        record.getId(), fileUrl);

                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        parentDialog.dismiss();
                                                        if (dbSuccess) {
                                                            ToastUtil.show(JiJianTaiZhangActivity.this, "文件删除成功");
                                                            initList();  // 刷新列表
                                                        } else {
                                                            ToastUtil.show(JiJianTaiZhangActivity.this, "数据库更新失败");
                                                        }
                                                    }
                                                });
                                            }
                                        }).start();
                                    }

                                    @Override
                                    public void onFailure(String error) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                ToastUtil.show(JiJianTaiZhangActivity.this, "服务器删除失败: " + error);
                                            }
                                        });
                                    }
                                });
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}