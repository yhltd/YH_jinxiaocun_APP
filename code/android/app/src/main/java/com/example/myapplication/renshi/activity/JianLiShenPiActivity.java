package com.example.myapplication.renshi.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.renshi.entity.YhRenShiJianLiGuanLi;
import com.example.myapplication.renshi.entity.YhRenShiUser;
import com.example.myapplication.renshi.service.YhRenShiJianLiShenPiService;
import com.example.myapplication.utils.ToastUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;

public class JianLiShenPiActivity extends AppCompatActivity {

    private YhRenShiUser yhRenShiUser;
    private YhRenShiJianLiShenPiService jianLiGuanLiService;

    private ListView listView;
    private ListView listView_block;
    private HorizontalScrollView list_table;
    private Spinner spinnerStatus;
    private Button selButton;

    private List<YhRenShiJianLiGuanLi> dataList;
    private SimpleAdapter adapter;
    private SimpleAdapter adapter_block;

    private String[] statusOptions = {"全部","驳回", "待处理"};

    // 文件选择相关
    private static final int REQUEST_CODE_FILE = 1001;
    private static final int REQUEST_CODE_CAMERA = 1002;
    private static final int REQUEST_CODE_GALLERY = 1003;

    private YhRenShiJianLiGuanLi currentRecordForUpload;
    private String currentUserFileName = "";
    private AlertDialog uploadProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jianlishenpi);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("简历审批");
        }

        initViews();

        MyApplication myApplication = (MyApplication) getApplication();
        yhRenShiUser = myApplication.getYhRenShiUser();

        jianLiGuanLiService = new YhRenShiJianLiShenPiService();

        initStatusSpinner();

        loadData(null);
    }

    private void initViews() {
        listView = findViewById(R.id.baopan_list);
        listView_block = findViewById(R.id.list_block);
        list_table = findViewById(R.id.list_table);
        spinnerStatus = findViewById(R.id.spinner_status);
        selButton = findViewById(R.id.sel_button);

        selButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedStatus = spinnerStatus.getSelectedItem().toString();
                loadData(selectedStatus.equals("全部") ? null : selectedStatus);
            }
        });

        listView_block.setVisibility(View.VISIBLE);
        list_table.setVisibility(View.GONE);
    }

    private void initStatusSpinner() {
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                statusOptions
        );
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(statusAdapter);
    }

    public void switchClick(View v) {
        if (listView_block.getVisibility() == View.VISIBLE) {
            listView_block.setVisibility(View.GONE);
            list_table.setVisibility(View.VISIBLE);
        } else {
            listView_block.setVisibility(View.VISIBLE);
            list_table.setVisibility(View.GONE);
        }
    }

    private void loadData(final String filterStatus) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String company = yhRenShiUser.getL();
                    dataList = jianLiGuanLiService.getListByStatus(company, filterStatus);
                    handler.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(1);
                }
            }
        }).start();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            if (msg.what == 0) {
                updateListView();
            } else {
                ToastUtil.show(JianLiShenPiActivity.this, "数据加载失败");
            }
        }
    };

    private void updateListView() {
        if (dataList == null || dataList.isEmpty()) {
            List<HashMap<String, Object>> emptyList = new ArrayList<>();
            HashMap<String, Object> item = new HashMap<>();
            item.put("empty", "暂无数据");
            emptyList.add(item);

            adapter = new SimpleAdapter(this, emptyList,
                    R.layout.empty_item, new String[]{"empty"}, new int[]{R.id.tv_empty});

            listView.setAdapter(adapter);
            listView_block.setAdapter(adapter);
            return;
        }

        List<HashMap<String, Object>> data = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            YhRenShiJianLiGuanLi item = dataList.get(i);
            HashMap<String, Object> map = new HashMap<>();
            map.put("index", i + 1);
            map.put("touliren", item.getTouliren() != null ? item.getTouliren() : "");
            map.put("xueli", item.getXueli() != null ? item.getXueli() : "");
            map.put("mubiaogangwei", item.getMubiaogangwei() != null ? item.getMubiaogangwei() : "");
            map.put("tijiaoshijian", item.getTijiaoshijian() != null ? item.getTijiaoshijian() : "");

            String files = item.getWenjian();
            if (files == null || files.trim().isEmpty()) {
                map.put("wenjian", "上传文件");
                map.put("wenjian_color", "#4CAF50");
            } else {
                map.put("wenjian", "查看文件");
                map.put("wenjian_color", "#2196F3");
            }

            map.put("beizhu", item.getBeizhu() != null ? item.getBeizhu() : "");
            map.put("zhuangtai", item.getZhuangtai() != null ? item.getZhuangtai() : "");
            data.add(map);
        }

        adapter = new SimpleAdapter(this, data, R.layout.jianlishenpi_row,
                new String[]{"index", "touliren", "xueli", "mubiaogangwei", "tijiaoshijian", "wenjian", "beizhu", "zhuangtai"},
                new int[]{R.id.B, R.id.C, R.id.D, R.id.E, R.id.F, R.id.G, R.id.H, R.id.I}) {

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView tvWenjian = view.findViewById(R.id.G);
                HashMap<String, Object> item = data.get(position);

                String color = (String) item.get("wenjian_color");
                if (color != null) {
                    tvWenjian.setTextColor(android.graphics.Color.parseColor(color));
                }

                tvWenjian.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handleFileClick(position);
                    }
                });

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showRecordDetail(position);
                    }
                });

                return view;
            }
        };

        adapter_block = new SimpleAdapter(this, data, R.layout.jianlishenpi_row_block,
                new String[]{"touliren", "xueli", "mubiaogangwei", "tijiaoshijian", "wenjian", "beizhu", "zhuangtai"},
                new int[]{R.id.B, R.id.C, R.id.D, R.id.E, R.id.F, R.id.G, R.id.H}) {

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView tvWenjian = view.findViewById(R.id.F);
                HashMap<String, Object> item = data.get(position);

                String color = (String) item.get("wenjian_color");
                if (color != null) {
                    tvWenjian.setTextColor(android.graphics.Color.parseColor(color));
                }

                tvWenjian.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handleFileClick(position);
                    }
                });

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showRecordDetail(position);
                    }
                });

                return view;
            }
        };

        listView.setAdapter(adapter);
        listView_block.setAdapter(adapter_block);
    }

    public void onAddClick(View v) {
        showEditDialog(null);
    }

    private void showEditDialog(final YhRenShiJianLiGuanLi record) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(record == null ? "添加简历" : "编辑简历");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_jianli_edit, null);

        final EditText etTouliren = dialogView.findViewById(R.id.et_touliren);
        final EditText etXueli = dialogView.findViewById(R.id.et_xueli);
        final EditText etMubiaogangwei = dialogView.findViewById(R.id.et_mubiaogangwei);
        final TextView tvTijiaoshijian = dialogView.findViewById(R.id.tv_tijiaoshijian);
        final EditText etTijiaoshijian = dialogView.findViewById(R.id.et_tijiaoshijian);
        final EditText etBeizhu = dialogView.findViewById(R.id.et_beizhu);
        final Spinner spinnerZhuangtai = dialogView.findViewById(R.id.spinner_zhuangtai);
        final EditText etZhuangtai = dialogView.findViewById(R.id.et_zhuangtai);

        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
        Button btnSave = dialogView.findViewById(R.id.btn_save);

        String[] statusOptions = {"待处理","驳回","通过"};
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                statusOptions
        );
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerZhuangtai.setAdapter(statusAdapter);

        if (record != null) {
            etTouliren.setText(record.getTouliren());
            etXueli.setText(record.getXueli());
            etMubiaogangwei.setText(record.getMubiaogangwei());

            String tijiaoshijian = record.getTijiaoshijian();
            if (tijiaoshijian != null && !tijiaoshijian.isEmpty()) {
                tvTijiaoshijian.setText(tijiaoshijian);
                etTijiaoshijian.setText(tijiaoshijian);
            }

            etBeizhu.setText(record.getBeizhu());

            String zhuangtai = record.getZhuangtai();
            if (zhuangtai != null && !zhuangtai.isEmpty()) {
                for (int i = 0; i < statusOptions.length; i++) {
                    if (statusOptions[i].equals(zhuangtai)) {
                        spinnerZhuangtai.setSelection(i);
                        etZhuangtai.setText(zhuangtai);
                        break;
                    }
                }
            } else {
                spinnerZhuangtai.setSelection(0);
                etZhuangtai.setText("待处理");
            }
        } else {
            String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            tvTijiaoshijian.setText(currentDate);
            etTijiaoshijian.setText(currentDate);
            spinnerZhuangtai.setSelection(0);
            etZhuangtai.setText("待处理");
        }

        tvTijiaoshijian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(tvTijiaoshijian, etTijiaoshijian);
            }
        });

        spinnerZhuangtai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedStatus = statusOptions[position];
                etZhuangtai.setText(selectedStatus);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                etZhuangtai.setText("待处理");
            }
        });

        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRecord(record,
                        etTouliren.getText().toString(),
                        etXueli.getText().toString(),
                        etMubiaogangwei.getText().toString(),
                        etTijiaoshijian.getText().toString(),
                        etBeizhu.getText().toString(),
                        etZhuangtai.getText().toString()
                );
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showDatePicker(final TextView displayView, final EditText valueView) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                        displayView.setText(selectedDate);
                        valueView.setText(selectedDate);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void saveRecord(final YhRenShiJianLiGuanLi record,
                            final String touliren, final String xueli,
                            final String mubiaogangwei, final String tijiaoshijian,
                            final String beizhu, final String zhuangtai) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    boolean success;
                    String company = yhRenShiUser.getL().replace("_hr", "");

                    if (record == null) {
                        YhRenShiJianLiGuanLi newRecord = new YhRenShiJianLiGuanLi();
                        newRecord.setGongsi(company);
                        newRecord.setTouliren(touliren);
                        newRecord.setXueli(xueli);
                        newRecord.setMubiaogangwei(mubiaogangwei);
                        newRecord.setTijiaoshijian(tijiaoshijian);
                        newRecord.setBeizhu(beizhu);
                        newRecord.setZhuangtai(zhuangtai);

                        success = jianLiGuanLiService.insert(newRecord);
                    } else {
                        record.setTouliren(touliren);
                        record.setXueli(xueli);
                        record.setMubiaogangwei(mubiaogangwei);
                        record.setTijiaoshijian(tijiaoshijian);
                        record.setBeizhu(beizhu);
                        record.setZhuangtai(zhuangtai);

                        success = jianLiGuanLiService.update(record);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (success) {
                                ToastUtil.show(JianLiShenPiActivity.this, "保存成功");
                                loadData(null);
                            } else {
                                ToastUtil.show(JianLiShenPiActivity.this, "保存失败");
                            }
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.show(JianLiShenPiActivity.this, "保存异常: " + e.getMessage());
                        }
                    });
                }
            }
        }).start();
    }

    private void showRecordDetail(int position) {
        if (position >= dataList.size()) return;

        YhRenShiJianLiGuanLi record = dataList.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("操作选择");
        builder.setMessage("请选择要执行的操作");

        builder.setPositiveButton("编辑", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showEditDialog(record);
            }
        });

        builder.setNegativeButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteRecord(record, position);
            }
        });

        builder.setNeutralButton("取消", null);
        builder.show();
    }

    private void deleteRecord(final YhRenShiJianLiGuanLi record, final int position) {
        new AlertDialog.Builder(this)
                .setTitle("确认删除")
                .setMessage("确定要删除这条简历记录吗？")
                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                boolean success = jianLiGuanLiService.deleteWithCheck(record.getId());

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (success) {
                                            ToastUtil.show(JianLiShenPiActivity.this, "删除成功");
                                            loadData(null);
                                        } else {
                                            ToastUtil.show(JianLiShenPiActivity.this, "删除失败，请先删除关联的文件");
                                        }
                                    }
                                });
                            }
                        }).start();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void handleFileClick(int position) {
        if (position >= dataList.size()) return;

        YhRenShiJianLiGuanLi record = dataList.get(position);
        String currentFiles = record.getWenjian();

        if (currentFiles == null || currentFiles.trim().isEmpty()) {
            showFileUploadDialog(record);
        } else {
            showFileListDialog(record);
        }
    }

    private void showFileUploadDialog(final YhRenShiJianLiGuanLi record) {
        currentRecordForUpload = record;
        currentUserFileName = "";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("上传文件 - " + (record.getTouliren() != null ? record.getTouliren() : "未命名"));

        // 创建输入文件名的布局
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_file_upload, null);
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
                            case 0: // 文件管理器
                                openFilePicker();
                                break;
                            case 1: // 相册
                                openGallery();
                                break;
                            case 2: // 拍照
                                openCamera();
                                break;
                            case 3: // 输入链接
                                showInputFileUrlDialog(currentRecordForUpload);
                                break;
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*"); // 所有文件
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "选择文件"), REQUEST_CODE_FILE);
    }

    private void openCamera() {
        // 需要相机权限和文件权限
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && currentRecordForUpload != null) {
            if (requestCode == REQUEST_CODE_FILE && data != null) {
                Uri uri = data.getData();
                if (uri != null) {
                    // 获取文件路径
                    String filePath = getPathFromUri(uri);
                    if (filePath != null) {
                        uploadFile(new File(filePath));
                    }
                }
            } else if (requestCode == REQUEST_CODE_CAMERA && data != null) {
                // 处理相机拍摄的照片
                // 这里需要保存照片到临时文件
                // uploadFile(tempFile);
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

    private String getPathFromUri(Uri uri) {
        String filePath = null;

        try {
            if ("content".equalsIgnoreCase(uri.getScheme())) {
                // 从Content URI获取文件
                ContentResolver resolver = getContentResolver();

                String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DISPLAY_NAME};
                Cursor cursor = resolver.query(uri, projection, null, null, null);

                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                    filePath = cursor.getString(columnIndex);

                    if (filePath == null) {
                        // 尝试从DISPLAY_NAME获取文件名
                        int nameIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);
                        String fileName = cursor.getString(nameIndex);

                        // 创建临时文件
                        File tempFile = new File(getCacheDir(), fileName);
                        copyFileFromUri(uri, tempFile);
                        filePath = tempFile.getAbsolutePath();
                    }

                    cursor.close();
                }
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                // 从File URI获取文件
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

        // 显示上传进度对话框
        showUploadProgressDialog();

        String fileName = file.getName();
        String path = "/人事系统/简历文件/";
        String kongjian = "3";
        String recordId = String.valueOf(currentRecordForUpload.getId());
        String recordName = currentRecordForUpload.getTouliren() != null ?
                currentRecordForUpload.getTouliren() : "未知人员";

        // 调用上传服务
        jianLiGuanLiService.uploadFile(file, fileName, path, kongjian,
                recordId, recordName, currentUserFileName,
                new YhRenShiJianLiShenPiService.UploadCallback() {
                    @Override
                    public void onSuccess(String fileUrl) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hideUploadProgressDialog();

                                // 保存文件URL到数据库
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        boolean success = jianLiGuanLiService.updateFilesWithCheck(
                                                currentRecordForUpload.getId(), fileUrl);

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (success) {
                                                    ToastUtil.show(JianLiShenPiActivity.this, "文件上传成功");
                                                    loadData(null);
                                                } else {
                                                    ToastUtil.show(JianLiShenPiActivity.this, "文件信息保存失败");
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
                                ToastUtil.show(JianLiShenPiActivity.this, "文件上传失败: " + error);
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

    private void showInputFileUrlDialog(final YhRenShiJianLiGuanLi record) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("输入文件链接");

        final EditText input = new EditText(this);
        input.setHint("请输入文件URL链接");
        input.setPadding(20, 20, 20, 20);
        builder.setView(input);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String fileUrl = input.getText().toString().trim();
                if (!fileUrl.isEmpty()) {
                    saveFileUrl(record, fileUrl);
                } else {
                    ToastUtil.show(JianLiShenPiActivity.this, "请输入有效的文件链接");
                }
            }
        });

        builder.setNegativeButton("取消", null);
        builder.show();
    }

    private void saveFileUrl(final YhRenShiJianLiGuanLi record, final String fileUrl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 使用整合方法
                boolean success = jianLiGuanLiService.updateFilesWithCheck(record.getId(), fileUrl);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (success) {
                            ToastUtil.show(JianLiShenPiActivity.this, "文件链接保存成功");
                            loadData(null);
                        } else {
                            ToastUtil.show(JianLiShenPiActivity.this, "文件链接保存失败");
                        }
                    }
                });
            }
        }).start();
    }

    private void showFileListDialog(final YhRenShiJianLiGuanLi record) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("文件列表 - " + (record.getTouliren() != null ? record.getTouliren() : "未命名"));

        String files = record.getWenjian();
        if (files == null || files.trim().isEmpty()) {
            ToastUtil.show(this, "没有文件");
            return;
        }

        final String[] fileArray = files.split(",");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_file_list, null);
        builder.setView(dialogView);

        final AlertDialog dialog = builder.create();

        LinearLayout fileListLayout = dialogView.findViewById(R.id.file_list_layout);
        Button btnUploadNew = dialogView.findViewById(R.id.btn_upload_new);

        fileListLayout.removeAllViews();

        for (int i = 0; i < fileArray.length; i++) {
            final String fileUrl = fileArray[i].trim();
            if (fileUrl.isEmpty()) continue;

            View fileItemView = getLayoutInflater().inflate(R.layout.item_file_list, null);

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
                currentUserFileName = "";  // 重置文件名
                showFileUploadDialog(record);  // 改为显示文件名输入弹窗
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
                    ToastUtil.show(JianLiShenPiActivity.this, "链接已复制到剪贴板");
                }
            });

            builder.setNegativeButton("取消", null);
            builder.show();
        }
    }

    private void deleteSingleFile(final YhRenShiJianLiGuanLi record, final String fileUrl, final AlertDialog parentDialog) {
        new AlertDialog.Builder(this)
                .setTitle("确认删除")
                .setMessage("确定要删除这个文件吗？\n" + extractFileName(fileUrl))
                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 显示删除进度
                        ToastUtil.show(JianLiShenPiActivity.this, "正在删除文件...");

                        // 从URL中提取文件名
                        String fileName = extractFileName(fileUrl);
                        String path = "/人事系统/简历文件/";

                        // 先删除服务器上的文件
                        jianLiGuanLiService.deleteFileFromServer(fileName, path,
                                new YhRenShiJianLiShenPiService.DeleteCallback() {
                                    @Override
                                    public void onSuccess() {
                                        // 服务器删除成功后，删除数据库记录
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                boolean dbSuccess = jianLiGuanLiService.deleteFileWithCheck(
                                                        record.getId(), fileUrl);

                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        parentDialog.dismiss();
                                                        if (dbSuccess) {
                                                            ToastUtil.show(JianLiShenPiActivity.this, "文件删除成功");
                                                            // 重新显示文件列表
//                                                            showFileListDialog(record);
                                                            loadData(null);
                                                        } else {
                                                            ToastUtil.show(JianLiShenPiActivity.this, "数据库更新失败");
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
                                                ToastUtil.show(JianLiShenPiActivity.this, "服务器删除失败: " + error);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}