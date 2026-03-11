package com.example.myapplication.jiaowu.activity;

import android.annotation.SuppressLint;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.myapplication.jiaowu.entity.Quanxian;
import com.example.myapplication.jiaowu.entity.Teacher;
import com.example.myapplication.jiaowu.entity.TeacherInfo;
import com.example.myapplication.jiaowu.service.TeacherInfoService;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.ToastUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JiaoShiXinXiActivity extends AppCompatActivity {

    private final static int REQUEST_CODE_CHANG = 1;
    private final static int REQUEST_CODE_FILE = 1001;
    private final static int REQUEST_CODE_GALLERY = 1002;

    private Teacher teacher;
    private TeacherInfoService teacherInfoService;
    private ListView listView;

    private ListView listView_block;
    private HorizontalScrollView list_table;
    private SimpleAdapter adapter;
    private SimpleAdapter adapter_block;

    private EditText teacher_name;
    private String teacher_nameText;
    private Button sel_button;
    List<TeacherInfo> list;
    private Quanxian quanxian;

    // 文件上传相关
    private TeacherInfo currentRecordForUpload;
    private String currentUserFileName = "";
    private AlertDialog uploadProgressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jiaowu_jiaoshixinxi);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //初始化控件
        listView = findViewById(R.id.jiaoshixinxi_list);
        listView_block = findViewById(R.id.list_block);
        list_table = findViewById(R.id.list_table);
        teacher_name = findViewById(R.id.teacher_name);
        sel_button = findViewById(R.id.sel_button);
        sel_button.setOnClickListener(selClick());
        sel_button.requestFocus();

        MyApplication myApplication = (MyApplication) getApplication();
        teacher = myApplication.getTeacher();
        quanxian = myApplication.getQuanxian();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initList() {
        sel_button.setEnabled(false);
        teacher_nameText = teacher_name.getText().toString();
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
                    teacherInfoService = new TeacherInfoService();
                    list = teacherInfoService.getList(teacher.getCompany(), teacher_nameText);
                    if (list == null) return;

                    for (int i = 0; i < list.size(); i++) {
                        HashMap<String, Object> item = new HashMap<>();
                        item.put("t_name", list.get(i).getT_name());
                        item.put("sex", list.get(i).getSex());
                        item.put("id_code", list.get(i).getId_code());
                        item.put("minzu", list.get(i).getMinzu());
                        item.put("birthday", list.get(i).getBirthday());
                        item.put("post", list.get(i).getPost());
                        item.put("education", list.get(i).getEducation());
                        item.put("phone", list.get(i).getPhone());
                        item.put("rz_riqi", list.get(i).getRz_riqi());
                        item.put("state", list.get(i).getState());
                        item.put("shebao", list.get(i).getShebao());
                        item.put("address", list.get(i).getAddress());
                        data.add(item);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                adapter = new SimpleAdapter(JiaoShiXinXiActivity.this, data, R.layout.jiaowu_jiaoshixinxi_row,
                        new String[]{"t_name","sex","id_code","minzu","birthday","post","education","phone","rz_riqi","state","shebao","address"},
                        new int[]{R.id.t_name, R.id.sex, R.id.id_code, R.id.minzu, R.id.birthday, R.id.post, R.id.education, R.id.phone, R.id.rz_riqi, R.id.state, R.id.shebao, R.id.address}) {

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

                adapter_block = new SimpleAdapter(JiaoShiXinXiActivity.this, data, R.layout.jiaowu_jiaoshixinxi_row_block,
                        new String[]{"t_name","sex","id_code","minzu","birthday","post","education","phone","rz_riqi","state","shebao","address"},
                        new int[]{R.id.t_name, R.id.sex, R.id.id_code, R.id.minzu, R.id.birthday, R.id.post, R.id.education, R.id.phone, R.id.rz_riqi, R.id.state, R.id.shebao, R.id.address}) {

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

    public void onInsertClick(View v) {
        if(!quanxian.getAdd().equals("√")){
            ToastUtil.show(JiaoShiXinXiActivity.this, "无权限！");
            return;
        }
        Intent intent = new Intent(JiaoShiXinXiActivity.this, JiaoShiXinXiChangeActivity.class);
        intent.putExtra("type", R.id.insert_btn);
        startActivityForResult(intent, REQUEST_CODE_CHANG);
    }

    public View.OnClickListener updateClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!quanxian.getUpd().equals("√")){
                    ToastUtil.show(JiaoShiXinXiActivity.this, "无权限！");
                    return;
                }
                int position = Integer.parseInt(view.getTag().toString());
                Intent intent = new Intent(JiaoShiXinXiActivity.this, JiaoShiXinXiChangeActivity.class);
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
                if(!quanxian.getDel().equals("√")){
                    ToastUtil.show(JiaoShiXinXiActivity.this, "无权限！");
                    return true;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(JiaoShiXinXiActivity.this);
                int position = Integer.parseInt(view.getTag().toString());
                Handler deleteHandler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message msg) {
                        if ((boolean) msg.obj) {
                            ToastUtil.show(JiaoShiXinXiActivity.this, "删除成功");
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
                                msg.obj = teacherInfoService.delete(list.get(position).getId());
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

    // ==================== 文件上传相关方法 ====================

    private void handleFileAction(TeacherInfo record) {
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

    private void showFileUploadDialog(final TeacherInfo record) {
        currentRecordForUpload = record;
        currentUserFileName = "";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("上传文件 - " + record.getT_name());

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_file_upload_jiaowu, null);
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
        String recordName = currentRecordForUpload.getT_name() + "_" + currentRecordForUpload.getPhone();

        teacherInfoService.uploadFile(file, fileName, "", kongjian,
                recordId, recordName, currentUserFileName,
                new TeacherInfoService.UploadCallback() {
                    @Override
                    public void onSuccess(String fileUrl) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hideUploadProgressDialog();

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        boolean success = teacherInfoService.updateFilesWithCheck(
                                                currentRecordForUpload.getId(), fileUrl);

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (success) {
                                                    ToastUtil.show(JiaoShiXinXiActivity.this, "文件上传成功");
                                                    initList();  // 刷新列表
                                                } else {
                                                    ToastUtil.show(JiaoShiXinXiActivity.this, "文件信息保存失败");
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
                                ToastUtil.show(JiaoShiXinXiActivity.this, "文件上传失败: " + error);
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

    private void showFileListDialog(final TeacherInfo record) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("文件管理 - " + record.getT_name());

        String files = record.getWenjian();
        if (files == null || files.trim().isEmpty()) {
            ToastUtil.show(this, "没有文件");
            return;
        }

        final String[] fileArray = files.split(",");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_file_list_jiaowu, null);
        builder.setView(dialogView);

        final AlertDialog dialog = builder.create();

        LinearLayout fileListLayout = dialogView.findViewById(R.id.file_list_layout);
        Button btnUploadNew = dialogView.findViewById(R.id.btn_upload_new);

        fileListLayout.removeAllViews();

        for (int i = 0; i < fileArray.length; i++) {
            final String fileUrl = fileArray[i].trim();
            if (fileUrl.isEmpty()) continue;

            View fileItemView = getLayoutInflater().inflate(R.layout.item_file_list_jiaowu, null);

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
                    ToastUtil.show(JiaoShiXinXiActivity.this, "链接已复制到剪贴板");
                }
            });

            builder.setNegativeButton("取消", null);
            builder.show();
        }
    }

    private void deleteSingleFile(final TeacherInfo record, final String fileUrl, final AlertDialog parentDialog) {
        new AlertDialog.Builder(this)
                .setTitle("确认删除")
                .setMessage("确定要删除这个文件吗？\n" + extractFileName(fileUrl))
                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ToastUtil.show(JiaoShiXinXiActivity.this, "正在删除文件...");

                        String fileName = extractFileName(fileUrl);
                        String path = "/jiaowu/";

                        teacherInfoService.deleteFileFromServer(fileName, path,
                                new TeacherInfoService.DeleteCallback() {
                                    @Override
                                    public void onSuccess() {
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                boolean dbSuccess = teacherInfoService.deleteFileWithCheck(
                                                        record.getId(), fileUrl);

                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        parentDialog.dismiss();
                                                        if (dbSuccess) {
                                                            ToastUtil.show(JiaoShiXinXiActivity.this, "文件删除成功");
                                                            initList();  // 刷新列表
                                                        } else {
                                                            ToastUtil.show(JiaoShiXinXiActivity.this, "数据库更新失败");
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
                                                ToastUtil.show(JiaoShiXinXiActivity.this, "服务器删除失败: " + error);
                                            }
                                        });
                                    }
                                });
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }
}