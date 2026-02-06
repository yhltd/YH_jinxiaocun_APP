package com.example.myapplication.scheduling.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.scheduling.entity.BomInfo;
import com.example.myapplication.scheduling.entity.ModuleInfo;
import com.example.myapplication.scheduling.entity.OrderBom;
import com.example.myapplication.scheduling.entity.OrderGongXu;
import com.example.myapplication.scheduling.entity.OrderInfo;
import com.example.myapplication.scheduling.entity.PaibanInfo;
import com.example.myapplication.scheduling.entity.UserInfo;
import com.example.myapplication.scheduling.service.BomInfoService;
import com.example.myapplication.scheduling.service.ModuleInfoService;
import com.example.myapplication.scheduling.service.OrderGongXuService;
import com.example.myapplication.scheduling.service.OrderInfoService;
import com.example.myapplication.scheduling.service.PaibanRenyuanService;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderChangeActivity extends AppCompatActivity {
    private UserInfo userInfo;
    private OrderInfo orderInfo;
    private OrderInfoService orderInfoService;
    private BomInfoService bomInfoService;
    private ListView moduleListView;
    private List<ModuleInfo> moduleList;
    private ModuleInfoService moduleInfoService;
    private OrderGongXuService orderGongXuService;

    private EditText order_id;
    private EditText code;
    private EditText product_name;
    private EditText norms;
    private EditText set_num;
    private Spinner is_complete;

    private ListView listView;
    private List<String> is_complete_list;

    private List<BomInfo> list;

    int typeId;

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_change);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        MyApplication myApplication = (MyApplication) getApplication();
        userInfo = myApplication.getUserInfo();

        orderInfoService = new OrderInfoService();
        bomInfoService = new BomInfoService();
        orderGongXuService = new OrderGongXuService();

        order_id = findViewById(R.id.order_id);
        code = findViewById(R.id.code);
        product_name = findViewById(R.id.product_name);
        norms = findViewById(R.id.norms);
        set_num = findViewById(R.id.set_num);
        is_complete = findViewById(R.id.is_complete);
        listView = findViewById(R.id.bom_list);

        moduleListView = findViewById(R.id.module_list);
        moduleInfoService = new ModuleInfoService();

        is_complete_list = new ArrayList<>();
        is_complete_list.add("否");
        is_complete_list.add("是");

        SpinnerAdapter adapter = new ArrayAdapter<String>(OrderChangeActivity.this, android.R.layout.simple_spinner_dropdown_item, is_complete_list);
        is_complete.setAdapter(adapter);

        initList();

        Intent intent = getIntent();
        typeId = intent.getIntExtra("type", 0);
        if (typeId == R.id.insert_btn) {
            orderInfo = new OrderInfo();
            Button btn = findViewById(typeId);
            btn.setVisibility(View.VISIBLE);
        } else if (typeId == R.id.update_btn) {
            orderInfo = (OrderInfo) myApplication.getObj();
            Button btn = findViewById(typeId);
            btn.setVisibility(View.VISIBLE);

            order_id.setText(orderInfo.getOrder_id());
            code.setText(orderInfo.getCode());
            product_name.setText(orderInfo.getProduct_name());
            norms.setText(orderInfo.getNorms());
            set_num.setText(orderInfo.getSet_num() + "");
            if (orderInfo.getIs_complete().equals("是")) {
                is_complete.setSelection(1);
            } else {
                is_complete.setSelection(0);
            }
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

        Handler listLoadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                MyApplication myApplication = (MyApplication) getApplication();
                listView.setAdapter(new BomAdapter(myApplication.getApplicationContext()));

                // 添加工序列表适配器
                moduleListView.setAdapter(new ModuleAdapter(myApplication.getApplicationContext()));

                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                bomInfoService = new BomInfoService();
                if (typeId == R.id.insert_btn) {
                    list = bomInfoService.getAddList(userInfo.getCompany());
                } else if (typeId == R.id.update_btn) {
                    list = bomInfoService.getAddList(userInfo.getCompany());
                    List<BomInfo> updlist = bomInfoService.getUpdOrderBom(orderInfo.getId());
                    for (BomInfo bomInfo : list) {
                        for (BomInfo bomInfo2 : updlist) {
                            if (bomInfo.getId() == bomInfo2.getId()) {
                                bomInfo.setCheck(true);
                                bomInfo.setUse_num(bomInfo2.getUse_num());
                            }
                        }
                    }
                }

                // 加载工序列表
                moduleList = moduleInfoService.getList(userInfo.getCompany(), "全部");
                if (moduleList == null) {
                    moduleList = new ArrayList<>();
                }

                if (typeId == R.id.update_btn) {
                    List<ModuleInfo> existingModules = orderGongXuService.getModuleListByOrderId(orderInfo.getId());
                    if (existingModules != null && existingModules.size() > 0) {
                        // 将已有的工序数据合并到工序列表中
                        for (ModuleInfo module : moduleList) {
                            for (ModuleInfo existingModule : existingModules) {
                                if (module.getId() == existingModule.getId()) {
                                    module.setEstimatedTime(existingModule.getEstimatedTime());
                                    // 如果已有工时数据，设置为选中状态
                                    if (existingModule.getEstimatedTime() > 0) {
                                        module.setCheck(true);
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }

                Message msg = new Message();
                msg.obj = list;
                listLoadHandler.sendMessage(msg);
            }
        }).start();
    }

    // 修改ModuleViewHolder类
    class ModuleViewHolder {
        public TextView name;
        public TextView efficiency;
        public EditText estimatedTime;
        public CheckBox cb; // 添加复选框
    }

    // 添加工序列表的Adapter
    class ModuleAdapter extends BaseAdapter {

        Context context;
        private LayoutInflater inflater = null;

        public ModuleAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return moduleList.size();
        }

        @Override
        public Object getItem(int position) {
            return moduleList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ModuleViewHolder holder = null;
            View view;

            if (convertView == null) {
                holder = new ModuleViewHolder();
                view = inflater.inflate(R.layout.module_list_row, null);
                holder.name = view.findViewById(R.id.module_name);
                holder.efficiency = view.findViewById(R.id.module_efficiency);
                holder.estimatedTime = view.findViewById(R.id.estimated_time);
                holder.cb = view.findViewById(R.id.cb);
                holder.cb.setVisibility(View.VISIBLE);
                view.setTag(holder);
            } else {
                view = convertView;
                holder = (ModuleViewHolder) view.getTag();
            }

            // 设置工序数据
            ModuleInfo module = moduleList.get(position);
            holder.name.setText(module.getName());
            holder.efficiency.setText(module.getNum() + "");

            // 设置预计工时输入框
            holder.estimatedTime.setTag(position);
            holder.estimatedTime.clearFocus();

            // 如果有已有数据，设置初始值
            if (module.getEstimatedTime() > 0) {
                holder.estimatedTime.setText(module.getEstimatedTime() + "");
            } else {
                holder.estimatedTime.setText("");
            }

            // 设置复选框状态
            holder.cb.setChecked(module.isCheck());
            holder.cb.setTag(position);

            // 复选框点击监听
            holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    int pos = (int) compoundButton.getTag();
                    moduleList.get(pos).setCheck(b);
                }
            });

            // 将holder复制到final变量中，解决内部类访问问题
            final ModuleViewHolder finalHolder = holder;

            // 添加预计工时输入监听
            final EditText timeInput = holder.estimatedTime;
            holder.estimatedTime.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    int pos = (int) timeInput.getTag();
                    if (!s.toString().trim().equals("")) {
                        try {
                            double estimatedTime = Double.parseDouble(s.toString().trim());
                            moduleList.get(pos).setEstimatedTime(estimatedTime);
                            // 如果输入了工时，自动选中复选框
                            moduleList.get(pos).setCheck(true);
                            finalHolder.cb.setChecked(true);
                        } catch (NumberFormatException e) {
                            moduleList.get(pos).setEstimatedTime(0);
                            moduleList.get(pos).setCheck(false);
                            finalHolder.cb.setChecked(false);
                        }
                    } else {
                        moduleList.get(pos).setEstimatedTime(0);
                        // 如果清空工时，取消选中复选框
                        moduleList.get(pos).setCheck(false);
                        finalHolder.cb.setChecked(false);
                    }
                }
            });

            return view;
        }
    }

    // 创建一个 ViewHolder 类
    // 用来存储 list_item_layout.xml中下义的View控件.
    // 并把这个ViewHolder对象放到Item的Tag中,
    // 这样做极大了方便我们之后对Item中的控件的修改操作
    // 并大大的减少了 findViewById的次数,提高效率
    class ViewHolder {
        public TextView code;
        public TextView name;
        public TextView comment;
        public EditText use_num;
        public CheckBox cb;
    }

    class BomAdapter extends BaseAdapter {

        Context context;
        private LayoutInflater inflater = null;

        public BomAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // 声明一个 ViewHolder对象
            ViewHolder holder = null;
            View view;
            // convertView .就是上一次使用的Item的View对象, 参数View convertView 而这个convertView其实就是最关键的部分了 原理上讲
            // 当ListView滑动的过程中 会有item被滑出屏幕 而不再被使用 这时候Android会回收这个条目的view ,当item1被移除屏幕的时候 我们会重新new一个View给新显示的item_new
            // 而如果使用了这个convertView 我们其实可以复用它 这样就省去了new View的大量开销
            // 如果没有可用的 convertView 那么就要创建它
            if (convertView == null) {
                //view = LayoutInflater.from(context).inflate(R.layout.ruku_row,null);
                // 创建一个 ViewHolder对象,来保存这个 View中的了控件,这样,我们就不需要每都用 使用findViewById了.
                holder = new ViewHolder();
                // 使用 LayoutInflater 创建View
                view = inflater.inflate(R.layout.order_bom_row, null);
                // 将,View 的了控件保存到 holder 中.
                holder.code = view.findViewById(R.id.code);
                holder.name = view.findViewById(R.id.name);
                holder.comment = view.findViewById(R.id.comment);
                holder.use_num = view.findViewById(R.id.use_num);
                holder.cb = view.findViewById(R.id.cb);
                holder.cb.setVisibility(View.VISIBLE);
                // 将Hodler 存放在 convertView 的 Tag 中.
                view.setTag(holder);
            } else {
                view = convertView;
                // 如果有可用的 convertView.那么就得到存在它Tag中的 Holder对象
                holder = (ViewHolder) view.getTag();
            }

            // 对Holder对象中的控制设置属性或绑定事件
            holder.code.setText(list.get(position).getCode());
            holder.name.setText(list.get(position).getName());
            holder.comment.setText(list.get(position).getComment());

            holder.use_num.setTag(position);
            holder.use_num.clearFocus();
            if (typeId == R.id.update_btn) {
                holder.use_num.setText(((int) list.get(position).getUse_num()) + "");
            }


            final EditText num = holder.use_num;
            holder.use_num.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    int a = (int) num.getTag();
                    if (!s.toString().equals("")) {
                        list.get(a).setUse_num(Double.parseDouble(s.toString()));
                    } else {
                        list.get(a).setUse_num(0);
                    }
                }
            });


            holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    list.get(position).setCheck(b);
                }
            });
            holder.cb.setChecked(list.get(position).isCheck());

            // 将这个 view 返回
            return view;
        }
    }

    public void insertClick(View v) {
        if (!checkForm()) return;

        List<BomInfo> bmList = new ArrayList<>();
        for (BomInfo bomInfo : list) {
            if (bomInfo.isCheck()) {
                bmList.add(bomInfo);
            }
        }
        if (bmList.size() == 0) {
            ToastUtil.show(OrderChangeActivity.this, "请选择物料！");
            return;
        }

        // 检查是否有选中的工序
        List<ModuleInfo> selectedModules = new ArrayList<>();
        for (ModuleInfo module : moduleList) {
            if (module.isCheck() && module.getEstimatedTime() > 0) {
                selectedModules.add(module);
            }
        }
        if (selectedModules.size() == 0) {
            ToastUtil.show(OrderChangeActivity.this, "请选择工序并填写预计工时！");
            return;
        }

        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(OrderChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(OrderChangeActivity.this, "保存失败，请稍后再试");
                }

                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                boolean insertResult = orderInfoService.insert(orderInfo);
                if (!insertResult) {
                    msg.obj = false;
                    saveHandler.sendMessage(msg);
                    return;
                }

                List<OrderInfo> getLast = orderInfoService.getLast();
                if (getLast == null || getLast.size() == 0) {
                    msg.obj = false;
                    saveHandler.sendMessage(msg);
                    return;
                }

                int newOrderId = getLast.get(0).getId();

                // 保存物料信息 - 循环中单个插入
                boolean bomSuccess = true;
                for (BomInfo bomInfo : bmList) {
                    OrderBom orderBom = new OrderBom();
                    orderBom.setOrder_id(newOrderId);
                    orderBom.setBom_id(bomInfo.getId());
                    orderBom.setUse_num((int) bomInfo.getUse_num());
                    if (!orderInfoService.insertOrderBom(orderBom)) {
                        bomSuccess = false;
                    }
                }

                // 保存工序信息 - 改为循环中单个插入（和物料一样）
                boolean moduleSuccess = true;
                for (ModuleInfo module : selectedModules) {
                    OrderGongXu orderGongXu = new OrderGongXu();
                    orderGongXu.setOrder_id(newOrderId);
                    orderGongXu.setModule_id(module.getId());
                    orderGongXu.setModule_num((int) module.getEstimatedTime());
                    if (!orderGongXuService.insert(orderGongXu)) {
                        moduleSuccess = false;
                    }
                }

                msg.obj = bomSuccess && moduleSuccess;
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    public void updateClick(View v) {
        if (!checkForm()) return;

        List<BomInfo> bmList = new ArrayList<>();
        for (BomInfo bomInfo : list) {
            if (bomInfo.isCheck()) {
                bmList.add(bomInfo);
            }
        }
        if (bmList.size() == 0) {
            ToastUtil.show(OrderChangeActivity.this, "请选择物料！");
            return;
        }

        // 检查是否有选中的工序
        List<ModuleInfo> selectedModules = new ArrayList<>();
        for (ModuleInfo module : moduleList) {
            if (module.isCheck() && module.getEstimatedTime() > 0) {
                selectedModules.add(module);
            }
        }
        if (selectedModules.size() == 0) {
            ToastUtil.show(OrderChangeActivity.this, "请选择工序并填写预计工时！");
            return;
        }

        Handler saveHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if ((boolean) msg.obj) {
                    ToastUtil.show(OrderChangeActivity.this, "保存成功");
                    back();
                } else {
                    ToastUtil.show(OrderChangeActivity.this, "保存失败，请稍后再试");
                }

                return true;
            }
        });

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg = new Message();
                boolean updateResult = orderInfoService.update(orderInfo);
                if (!updateResult) {
                    msg.obj = false;
                    saveHandler.sendMessage(msg);
                    return;
                }

                // 删除原有的物料信息
                orderInfoService.deleteOrderBom(orderInfo.getId());
                // 删除原有的工序信息
                orderGongXuService.deleteByOrderId(orderInfo.getId());

                // 保存物料信息 - 循环中单个插入
                boolean bomSuccess = true;
                for (BomInfo bomInfo : bmList) {
                    OrderBom orderBom = new OrderBom();
                    orderBom.setOrder_id(orderInfo.getId());
                    orderBom.setBom_id(bomInfo.getId());
                    orderBom.setUse_num((int) bomInfo.getUse_num());
                    if (!orderInfoService.insertOrderBom(orderBom)) {
                        bomSuccess = false;
                    }
                }

                // 保存工序信息 - 改为循环中单个插入（和物料一样）
                boolean moduleSuccess = true;
                for (ModuleInfo module : selectedModules) {
                    OrderGongXu orderGongXu = new OrderGongXu();
                    orderGongXu.setOrder_id(orderInfo.getId());
                    orderGongXu.setModule_id(module.getId());
                    orderGongXu.setModule_num((int) module.getEstimatedTime());
                    if (!orderGongXuService.insert(orderGongXu)) {
                        moduleSuccess = false;
                    }
                }

                msg.obj = bomSuccess && moduleSuccess;
                saveHandler.sendMessage(msg);
            }
        }).start();
    }

    private boolean checkForm() {
        if (order_id.getText().toString().equals("")) {
            ToastUtil.show(OrderChangeActivity.this, "请输入订单号");
            return false;
        } else {
            orderInfo.setOrder_id(order_id.getText().toString());
        }
        if (code.getText().toString().equals("")) {
            ToastUtil.show(OrderChangeActivity.this, "请输入产品编码");
            return false;
        } else {
            orderInfo.setCode(code.getText().toString());
        }
        if (product_name.getText().toString().equals("")) {
            ToastUtil.show(OrderChangeActivity.this, "请输入产品名称");
            return false;
        } else {
            orderInfo.setProduct_name(product_name.getText().toString());
        }
        if (set_num.getText().toString().equals("")) {
            ToastUtil.show(OrderChangeActivity.this, "请输入下单数量");
            return false;
        } else {
            orderInfo.setSet_num(Integer.parseInt(set_num.getText().toString()));
        }
        if (typeId == R.id.insert_btn) {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat spd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date date = new Date();
            orderInfo.setSet_date(spd.format(date));
        }
        orderInfo.setNorms(norms.getText().toString());
        orderInfo.setIs_complete(is_complete.getSelectedItem().toString());
        orderInfo.setCompany(userInfo.getCompany());

        return true;
    }

    private void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }

}
