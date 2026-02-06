package com.example.myapplication.scheduling.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.scheduling.entity.ShengChanXian;
import com.example.myapplication.scheduling.entity.TimeConfig;
import com.example.myapplication.scheduling.entity.UserInfo;
import com.example.myapplication.scheduling.entity.WorkDetailDTO;
import com.example.myapplication.scheduling.service.PaiChanService;
import com.example.myapplication.scheduling.service.ShengChanXianService;
import com.example.myapplication.scheduling.service.TimeConfigService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PaiChanResultActivity extends AppCompatActivity {
    private UserInfo userInfo;
    private PaiChanService paiChanService;
    private ShengChanXianService shengChanXianService;
    private TimeConfigService timeConfigService;

    private Button backBtn;
    private ListView resultListView;

    private List<WorkDetailDTO> paiChanList;
    private List<ShengChanXian> shengChanXianList;
    private List<TimeConfig> timeConfigList;
    private SimpleAdapter adapter;

    // 排产结果数据
    private List<PaichanResult> paichanResultList = new ArrayList<>();

    // 并行处理配置
    private ParallelConfig parallelConfig;

    // 是否使用插单逻辑
    private boolean useInsertLogic = false;
    private boolean hasInsertOrders = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paichan_result);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("排产结果");
        }

        MyApplication myApplication = (MyApplication) getApplication();
        userInfo = myApplication.getUserInfo();

        paiChanService = new PaiChanService();
        shengChanXianService = new ShengChanXianService();
        timeConfigService = new TimeConfigService();

        // 初始化并行配置
        parallelConfig = new ParallelConfig();

        initViews();
        initListeners();
        loadData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        backBtn = findViewById(R.id.back_btn);
        resultListView = findViewById(R.id.paichan_result_listview);
    }

    private void initListeners() {
        backBtn.setOnClickListener(v -> finish());
    }

    private void loadData() {
        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if (msg.what == 0) {
                    // 添加调试日志
                    Log.d("PaichanDebug", "开始显示排产结果，共" + paichanResultList.size() + "条数据");

                    // 显示排产结果
                    List<HashMap<String, Object>> resultData = new ArrayList<>();
                    for (PaichanResult result : paichanResultList) {
                        HashMap<String, Object> item = new HashMap<>();
                        item.put("orderId", result.orderId);
                        item.put("processName", result.processName);
                        item.put("quantity", result.quantity);
                        item.put("processEfficiency", result.processEfficiency);
                        item.put("productionLine", result.productionLine);
                        item.put("lineEfficiency", result.lineEfficiency);

                        // 调试日志：检查开始时间
                        String formattedTime = formatDateTime(result.startTime);
                        Log.d("PaichanDebug", "订单" + result.orderId + "开始时间: " + result.startTime + " -> " + formattedTime);

                        item.put("startTime", formattedTime);
                        item.put("endTime", formatDateTime(result.endTime));
                        item.put("requiredHours", String.format("%.2f", result.requiredHours));
                        item.put("isInsertOrder", result.isInsertOrder ? "是" : "否");
                        item.put("warning", result.warning ? "异常" : "正常");
                        item.put("parallelCount", result.parallelCount > 1 ? result.parallelCount + "条线并行" : "单线");

                        // 关键修改：获取订单类型并转换为中文显示
                        String orderTypeDisplay = "正常"; // 默认值

                        // 1. 首先尝试从 WorkDetailDTO 中获取 dingdanleixing
                        for (WorkDetailDTO work : paiChanList) {
                            if (work.getDingdanhao() != null && work.getDingdanhao().equals(result.orderId)) {
                                String dingdanleixing = work.getDingdanleixing();
                                if (dingdanleixing != null) {
                                    if ("urgent".equals(dingdanleixing)) {
                                        orderTypeDisplay = "优先";
                                    } else if ("normal".equals(dingdanleixing)) {
                                        orderTypeDisplay = "正常";
                                    } else {
                                        orderTypeDisplay = dingdanleixing; // 其他类型直接显示
                                    }
                                }
                                break;
                            }
                        }

                        // 2. 如果没有找到，使用 isInsertOrder 作为备选
                        if ("正常".equals(orderTypeDisplay) && result.isInsertOrder) {
                            orderTypeDisplay = "优先";
                        }

                        item.put("orderType", orderTypeDisplay); // 订单类型字段

                        resultData.add(item);
                    }

                    adapter = new SimpleAdapter(PaiChanResultActivity.this, resultData, R.layout.paichan_result_detail_row,
                            new String[]{
                                    "orderId",           // 对应 dingdanhao
                                    "processName",       // 新增：对应 gongxumingcheng
                                    "quantity",          // 对应 gongxushuliang
                                    "processEfficiency", // 对应 gongxuxiaolv
                                    "productionLine",    // 对应 gongxumingcheng
                                    "lineEfficiency",    // 对应 lineEfficiency（需要新字段）
                                    "startTime",         // 对应 kaishishijian
                                    "endTime",           // 对应 jiezhishijian
                                    "requiredHours",     // 对应 requiredHours（需要新字段）
                                    "isInsertOrder",     // 对应 charu
                                    "warning",           // 对应 warning（正确映射）
                                    "parallelCount",     // 对应 parallelCount（需要新字段）
                                    "orderType"          // 新增：对应 dingdanleixing
                            },
                            new int[]{
                                    R.id.dingdanhao,      // orderId -> dingdanhao
                                    R.id.processName,   // processName -> gongxumingcheng（需要layout中有这个id）
                                    R.id.gongxushuliang,  // quantity -> gongxushuliang
                                    R.id.gongxuxiaolv,    // processEfficiency -> gongxuxiaolv
                                    R.id.gongxumingcheng, // productionLine -> gongxumingcheng
                                    0,                    // lineEfficiency无对应控件
                                    R.id.kaishishijian,   // startTime -> kaishishijian
                                    R.id.jiezhishijian,   // endTime -> jiezhishijian
                                    0,                    // requiredHours无对应控件
                                    R.id.charu,           // isInsertOrder -> charu
                                    0,                    // warning 无对应控件
                                    0,                    // parallelCount无对应控件
                                    R.id.dingdanleixing   // orderType -> dingdanleixing
                            });
                    resultListView.setAdapter(adapter);

                    // 显示统计信息
                    int urgentCount = 0;
                    int insertCount = 0;
                    int parallelCount = 0;
                    for (PaichanResult result : paichanResultList) {
                        if (result.isInsertOrder) insertCount++;
                        if (result.parallelCount > 1) parallelCount++;

                        // 统计优先订单
                        for (WorkDetailDTO work : paiChanList) {
                            if (work.getDingdanhao() != null && work.getDingdanhao().equals(result.orderId)) {
                                if ("urgent".equals(work.getDingdanleixing())) {
                                    urgentCount++;
                                }
                                break;
                            }
                        }
                    }

                    String stats = String.format("共排产%d个任务（%s模式）\n优先订单：%d个\n插单：%d个\n并行：%d个",
                            paichanResultList.size(),
                            useInsertLogic ? "插单" : "普通",
                            urgentCount,
                            insertCount,
                            parallelCount);
                    Toast.makeText(PaiChanResultActivity.this, stats, Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });

        new Thread(() -> {
            try {
                // 1. 查询排产数据
                paiChanList = paiChanService.getList(userInfo.getCompany());

                // 2. 查询生产线数据
                shengChanXianList = shengChanXianService.getList(userInfo.getCompany(),"","","");

                // 3. 查询时间配置数据
                timeConfigList = timeConfigService.getList(userInfo.getCompany());

                // 检查是否有插单订单
                checkInsertOrders();

                // 执行排产计算
                if (useInsertLogic) {
                    performInsertPaichanCalculation();
                } else {
                    performNormalPaichanCalculation();
                }

                // 排序结果 - 修改这里！！！
                // 不仅按开始时间排序，还要考虑订单类型
                Collections.sort(paichanResultList, (a, b) -> {
                    // 先按开始时间排序
                    int timeCompare = a.startTime.compareTo(b.startTime);
                    if (timeCompare != 0) {
                        return timeCompare;
                    }

                    // 开始时间相同，按是否是插单/紧急订单排序
                    // isInsertOrder 为 true 表示是 urgent 订单
                    if (a.isInsertOrder && !b.isInsertOrder) {
                        return -1; // a 是 urgent，排在前面
                    } else if (!a.isInsertOrder && b.isInsertOrder) {
                        return 1;  // b 是 urgent，排在前面
                    }

                    return 0;
                });

                handler.sendEmptyMessage(0);

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(PaiChanResultActivity.this,
                        "排产计算失败：" + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }

    // 添加在类开始部分，或者在内部类定义之前
    private Date parseStringToDate(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.isEmpty()) {
            return null;
        }
        try {
            SimpleDateFormat sdf;
            String cleanStr = dateTimeStr;

            // 去掉毫秒部分
            if (cleanStr.contains(".")) {
                cleanStr = cleanStr.substring(0, cleanStr.indexOf("."));
            }

            // 判断格式
            if (cleanStr.contains(" ")) {
                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            } else {
                sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
            }

            return sdf.parse(cleanStr);
        } catch (Exception e) {
            Log.e("PaichanDebug", "解析日期失败: " + dateTimeStr, e);
            return null;
        }
    }

    // 添加计算时间差的辅助方法
    private long calculateTimeDifference(String startTimeStr, String endTimeStr) {
        if (startTimeStr == null || endTimeStr == null ||
                startTimeStr.isEmpty() || endTimeStr.isEmpty()) {
            return 0;
        }

        try {
            Date startDate = parseStringToDate(startTimeStr);
            Date endDate = parseStringToDate(endTimeStr);

            if (startDate == null || endDate == null) {
                return 0;
            }

            return endDate.getTime() - startDate.getTime();
        } catch (Exception e) {
            Log.e("PaichanDebug", "计算时间差失败", e);
            return 0;
        }
    }

    // 检查是否有插单订单
    private void checkInsertOrders() {
        if (paiChanList != null && paiChanList.size() > 0) {
            // 检查是否有 is_insert 或 charu 字段
            for (WorkDetailDTO work : paiChanList) {
                if (work.getCharu() != null && work.getCharu() == 1) {
                    hasInsertOrders = true;
                    break;
                }
                if (work.getDingdanleixing() != null && "urgent".equals(work.getDingdanleixing())) {
                    hasInsertOrders = true;
                    break;
                }
            }

            useInsertLogic = hasInsertOrders;
        }
    }

    // 普通排产逻辑
    private void performNormalPaichanCalculation() {
        try {
            // 1. 按订单号分组
            Map<String, List<WorkDetailDTO>> orderGroups = new HashMap<>();
            for (WorkDetailDTO work : paiChanList) {
                String key = work.getDingdanhao();
                if (!orderGroups.containsKey(key)) {
                    orderGroups.put(key, new ArrayList<>());
                }
                orderGroups.get(key).add(work);
            }

            // 2. 准备生产线数据（按工序分组）
            Map<String, List<ProductionLine>> productionLines = new HashMap<>();
            for (ShengChanXian line : shengChanXianList) {
                String process = line.getGongxu();
                if (!productionLines.containsKey(process)) {
                    productionLines.put(process, new ArrayList<>());
                }

                ProductionLine pl = new ProductionLine();
                pl.id = line.getId();
                pl.name = line.getMingcheng();
                pl.processName = line.getGongxu();
                pl.efficiency = convertToDouble(line.getXiaolv());
                pl.availableFrom = null;
                pl.schedule = new ArrayList<>();

                productionLines.get(process).add(pl);
            }

            // 3. 创建订单列表用于排序 - 修改排序策略
            List<OrderForSorting> orderListForSorting = new ArrayList<>();
            for (Map.Entry<String, List<WorkDetailDTO>> entry : orderGroups.entrySet()) {
                String orderId = entry.getKey();
                List<WorkDetailDTO> works = entry.getValue();

                // 判断是否为紧急订单
                boolean isUrgentOrder = false;
                for (WorkDetailDTO work : works) {
                    if ("urgent".equals(work.getDingdanleixing())) {
                        isUrgentOrder = true;
                        break;
                    }
                }

                // 找到最早的起始日期
                String earliestStartTime = null;
                for (WorkDetailDTO work : works) {
                    String currentStartTime = work.getKaishishijian();
                    if (currentStartTime != null && !currentStartTime.isEmpty()) {
                        if (earliestStartTime == null || currentStartTime.compareTo(earliestStartTime) < 0) {
                            earliestStartTime = currentStartTime;
                        }
                    }
                }

                orderListForSorting.add(new OrderForSorting(orderId, works,
                        earliestStartTime != null ? earliestStartTime : "",
                        isUrgentOrder));
            }

            // 4. 修改排序逻辑：优先按订单类型排序，再按时间排序
            Collections.sort(orderListForSorting, (a, b) -> {
                // 首先：urgent订单永远优先
                if (a.isUrgent && !b.isUrgent) {
                    return -1; // a是urgent，排在前面
                } else if (!a.isUrgent && b.isUrgent) {
                    return 1;  // b是urgent，排在前面
                }

                // 其次：都是urgent或都不是urgent，按开始时间排序
                if (a.earliestStartTime != null && !a.earliestStartTime.isEmpty() &&
                        b.earliestStartTime != null && !b.earliestStartTime.isEmpty()) {

                    Date dateA = parseStringToDate(a.earliestStartTime);
                    Date dateB = parseStringToDate(b.earliestStartTime);

                    if (dateA != null && dateB != null) {
                        return dateA.compareTo(dateB);
                    } else {
                        return a.earliestStartTime.compareTo(b.earliestStartTime);
                    }
                } else if (a.earliestStartTime != null && !a.earliestStartTime.isEmpty()) {
                    return -1;
                } else if (b.earliestStartTime != null && !b.earliestStartTime.isEmpty()) {
                    return 1;
                }

                return 0;
            });

            // 调试日志：查看排序后的结果
            Log.d("PaichanSort", "排序后的订单顺序:");
            for (int i = 0; i < orderListForSorting.size(); i++) {
                OrderForSorting order = orderListForSorting.get(i);
                Log.d("PaichanSort", (i+1) + ". 订单: " + order.orderId +
                        ", 开始时间: " + order.earliestStartTime +
                        ", urgent: " + order.isUrgent);
            }

            // 5. 处理每个订单
            for (OrderForSorting orderItem : orderListForSorting) {
                // 对订单内的工序排序
                Collections.sort(orderItem.works, (a, b) -> {
                    boolean aUrgent = "urgent".equals(a.getDingdanleixing());
                    boolean bUrgent = "urgent".equals(b.getDingdanleixing());

                    // 先按优先级排序：urgent优先
                    if (aUrgent && !bUrgent) {
                        return -1;
                    } else if (!aUrgent && bUrgent) {
                        return 1;
                    } else {
                        // 优先级相同，按开始时间排序
                        String aTime = a.getKaishishijian();
                        String bTime = b.getKaishishijian();

                        if (aTime != null && bTime != null) {
                            return aTime.compareTo(bTime);
                        } else if (aTime != null) {
                            return -1;
                        } else if (bTime != null) {
                            return 1;
                        }
                        return 0;
                    }
                });

                // 处理订单内的每个工序
                for (WorkDetailDTO work : orderItem.works) {
                    PaichanResult result = processWorkItemNormal(work, productionLines);
                    if (result != null) {
                        paichanResultList.add(result);

                        // 调试日志：记录排产结果
                        Log.d("PaichanResult", "排产结果 - 订单: " + result.orderId +
                                ", 工序: " + result.processName +
                                ", 开始时间: " + result.startTime +
                                ", 是否urgent: " + result.isInsertOrder);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("普通排产计算失败: " + e.getMessage());
        }
    }

    // 处理单个工作项（普通模式）
    private PaichanResult processWorkItemNormal(WorkDetailDTO work, Map<String, List<ProductionLine>> productionLines) {
        String processName = work.getGongxumingcheng();
        double processEfficiency = work.getGongxuxiaolv();
        double quantity = work.getGongxushuliang();

        List<ProductionLine> suitableLines = productionLines.get(processName);
        if (suitableLines == null || suitableLines.isEmpty()) {
            // 没有对应的生产线
            return createUnassignedResult(work);
        }

        // 过滤符合效率条件的生产线
        List<ProductionLine> matchedLines = new ArrayList<>();
        for (ProductionLine line : suitableLines) {
            if (Math.abs(line.efficiency - processEfficiency) < 0.01) {
                matchedLines.add(line);
            }
        }

        if (matchedLines.isEmpty()) {
            return createUnmatchedResult(work);
        }

        // 决定是否并行处理
        boolean needParallel = false;
        int parallelLinesCount = 1;
        boolean isUrgent = "urgent".equals(work.getDingdanleixing());

        if (parallelConfig.enable && matchedLines.size() > 1) {
            if (isUrgent) {
                // 紧急订单处理
                if (quantity >= parallelConfig.minQuantityForParallel * 0.7) {
                    parallelLinesCount = Math.min(
                            matchedLines.size(),
                            Math.min(parallelConfig.maxParallelLines + 1, (int) Math.ceil(quantity / 800))
                    );
                    needParallel = parallelLinesCount > 1;
                }

                // 时间压力检查
                if (!needParallel) {
                    String startTimeStr = work.getKaishishijian();
                    String endTimeStr = work.getJiezhishijian();

                    if (startTimeStr != null && !startTimeStr.isEmpty() &&
                            endTimeStr != null && !endTimeStr.isEmpty()) {

                        long availableTime = calculateTimeDifference(startTimeStr, endTimeStr);

                        if (availableTime > 0) {
                            double requiredTime = (quantity / processEfficiency) * 60 * 60 * 1000;

                            if (requiredTime > availableTime * 0.72) { // 0.8 * 0.9
                                parallelLinesCount = Math.min(
                                        matchedLines.size(),
                                        (int) Math.ceil(requiredTime / (availableTime * 0.72))
                                );
                                needParallel = parallelLinesCount > 1;
                            }
                        }
                    }
                }
            } else {
                // 普通订单
                if (quantity >= parallelConfig.minQuantityForParallel) {
                    parallelLinesCount = Math.min(
                            matchedLines.size(),
                            Math.min(parallelConfig.maxParallelLines, (int) Math.ceil(quantity / 1000))
                    );
                    needParallel = parallelLinesCount > 1;
                }

                // 时间压力检查
                if (!needParallel) {
                    String startTimeStr = work.getKaishishijian();
                    String endTimeStr = work.getJiezhishijian();

                    if (startTimeStr != null && !startTimeStr.isEmpty() &&
                            endTimeStr != null && !endTimeStr.isEmpty()) {

                        long availableTime = calculateTimeDifference(startTimeStr, endTimeStr);

                        if (availableTime > 0) {
                            double requiredTime = (quantity / processEfficiency) * 60 * 60 * 1000;

                            if (requiredTime > availableTime * parallelConfig.timePressureFactor) {
                                parallelLinesCount = Math.min(
                                        matchedLines.size(),
                                        (int) Math.ceil(requiredTime / (availableTime * parallelConfig.timePressureFactor))
                                );
                                needParallel = parallelLinesCount > 1;
                            }
                        }
                    }
                }
            }
        }

        if (needParallel && parallelLinesCount > 1) {
            return processParallelWorkNormal(work, matchedLines, parallelLinesCount);
        } else {
            return processSingleWorkNormal(work, matchedLines);
        }
    }

    // 处理并行工作（普通模式）
    private PaichanResult processParallelWorkNormal(WorkDetailDTO work, List<ProductionLine> matchedLines, int parallelLinesCount) {
        // 按可用时间排序
        Collections.sort(matchedLines, (a, b) -> {
            Date timeA = a.availableFrom != null ? a.availableFrom : parseStringToDate(work.getKaishishijian());
            Date timeB = b.availableFrom != null ? b.availableFrom : parseStringToDate(work.getKaishishijian());

            if (timeA == null && timeB == null) return 0;
            if (timeA == null) return 1;
            if (timeB == null) return -1;

            return timeA.compareTo(timeB);
        });

        // 选择最早可用的生产线
        List<ProductionLine> selectedLines = matchedLines.subList(0, Math.min(parallelLinesCount, matchedLines.size()));

        // 计算总效率
        double totalEfficiency = 0;
        for (ProductionLine line : selectedLines) {
            totalEfficiency += line.efficiency;
        }

        double parallelRequiredHours = work.getGongxushuliang() / totalEfficiency;

        // 确定开始时间（取最晚的可用时间）
        Date workStartTime = parseStringToDate(work.getKaishishijian());
        Date parallelStartTime = workStartTime != null ? workStartTime : new Date();
        for (ProductionLine line : selectedLines) {
            Date lineStartTime = line.availableFrom != null ? line.availableFrom : workStartTime;
            if (lineStartTime != null && lineStartTime.after(parallelStartTime)) {
                parallelStartTime = lineStartTime;
            }
        }

        // 计算实际结束时间
        Date parallelEndTime = calculateWorkTime(parallelStartTime, parallelRequiredHours);

        // 按效率比例分配数量
        List<Integer> allocatedQuantities = new ArrayList<>();
        int allocatedTotal = 0;

        for (ProductionLine line : selectedLines) {
            double proportion = line.efficiency / totalEfficiency;
            int allocatedQty = (int) Math.round(work.getGongxushuliang() * proportion);
            allocatedQuantities.add(allocatedQty);
            allocatedTotal += allocatedQty;
        }

        // 调整数量
        int quantityDiff = (int) work.getGongxushuliang() - allocatedTotal;
        if (quantityDiff != 0) {
            allocatedQuantities.set(0, allocatedQuantities.get(0) + quantityDiff);
        }

        // 更新生产线状态
        for (int i = 0; i < selectedLines.size(); i++) {
            ProductionLine line = selectedLines.get(i);
            line.availableFrom = parallelEndTime;

            // 添加到排班计划
            ScheduleItem scheduleItem = new ScheduleItem();
            scheduleItem.orderId = work.getDingdanhao();
            scheduleItem.startTime = parallelStartTime;
            scheduleItem.endTime = parallelEndTime;
            scheduleItem.quantity = allocatedQuantities.get(i);
            scheduleItem.isParallel = true;
            line.schedule.add(scheduleItem);
        }

        // 构建生产线名称字符串
        StringBuilder linesBuilder = new StringBuilder();
        for (ProductionLine line : selectedLines) {
            if (linesBuilder.length() > 0) linesBuilder.append("、");
            linesBuilder.append(line.name);
        }

        // 创建结果
        PaichanResult result = new PaichanResult();
        result.orderId = work.getDingdanhao();
        result.processName = work.getGongxumingcheng();
        result.quantity = work.getGongxushuliang();
        result.processEfficiency = work.getGongxuxiaolv();
        result.productionLine = linesBuilder.toString();
        result.lineEfficiency = totalEfficiency;
        result.startTime = parallelStartTime;
        result.endTime = parallelEndTime;
        result.requiredHours = parallelRequiredHours;
        result.isInsertOrder = "urgent".equals(work.getDingdanleixing());
        result.warning = false;
        result.parallelCount = selectedLines.size();

        return result;
    }

    // 处理单线工作（普通模式）
    private PaichanResult processSingleWorkNormal(WorkDetailDTO work, List<ProductionLine> suitableLines) {
        // 按可用时间排序
        Collections.sort(suitableLines, (a, b) -> {
            Date workStartTime = parseStringToDate(work.getKaishishijian());
            Date timeA = a.availableFrom != null ? a.availableFrom : workStartTime;
            Date timeB = b.availableFrom != null ? b.availableFrom : workStartTime;

            if (timeA == null && timeB == null) return 0;
            if (timeA == null) return 1;
            if (timeB == null) return -1;

            return timeA.compareTo(timeB);
        });

        ProductionLine selectedLine = suitableLines.get(0);
        Date workStartTime = parseStringToDate(work.getKaishishijian());
        Date startTime = selectedLine.availableFrom != null ?
                selectedLine.availableFrom : workStartTime;

        if (workStartTime != null && startTime != null && startTime.before(workStartTime)) {
            startTime = workStartTime;
        }


        double requiredHours = work.getGongxushuliang() / work.getGongxuxiaolv();
        Date endTime = calculateWorkTime(startTime, requiredHours);

        // 更新生产线状态
        selectedLine.availableFrom = endTime;

        // 添加到排班计划
        ScheduleItem scheduleItem = new ScheduleItem();
        scheduleItem.orderId = work.getDingdanhao();
        scheduleItem.startTime = startTime;
        scheduleItem.endTime = endTime;
        scheduleItem.quantity = work.getGongxushuliang();
        scheduleItem.isParallel = false;
        selectedLine.schedule.add(scheduleItem);

        // 创建结果
        PaichanResult result = new PaichanResult();
        result.orderId = work.getDingdanhao();
        result.processName = work.getGongxumingcheng();
        result.quantity = work.getGongxushuliang();
        result.processEfficiency = work.getGongxuxiaolv();
        result.productionLine = selectedLine.name;
        result.lineEfficiency = selectedLine.efficiency;
        result.startTime = startTime;
        result.endTime = endTime;
        result.requiredHours = requiredHours;
        result.isInsertOrder = "urgent".equals(work.getDingdanleixing());
        result.warning = false;
        result.parallelCount = 1;

        return result;
    }

    // 创建未分配结果
    private PaichanResult createUnassignedResult(WorkDetailDTO work) {
        PaichanResult result = new PaichanResult();
        result.orderId = work.getDingdanhao();
        result.processName = work.getGongxumingcheng();
        result.quantity = work.getGongxushuliang();
        result.processEfficiency = work.getGongxuxiaolv();
        result.productionLine = "未分配";
        result.lineEfficiency = 0;

        // 修改这里：解析字符串时间为 Date 类型
        result.startTime = parseStringToDate(work.getKaishishijian());
        result.endTime = parseStringToDate(work.getJiezhishijian());

        result.requiredHours = 0;
        result.isInsertOrder = "urgent".equals(work.getDingdanleixing());
        result.warning = true;
        result.parallelCount = 0;
        return result;
    }

    // 创建不匹配结果
    private PaichanResult createUnmatchedResult(WorkDetailDTO work) {
        PaichanResult result = new PaichanResult();
        result.orderId = work.getDingdanhao();
        result.processName = work.getGongxumingcheng();
        result.quantity = work.getGongxushuliang();
        result.processEfficiency = work.getGongxuxiaolv();
        result.productionLine = "无生产线";
        result.lineEfficiency = 0;
        result.startTime = parseStringToDate(work.getKaishishijian());
        result.endTime = parseStringToDate(work.getJiezhishijian());
        result.requiredHours = 0;
        result.isInsertOrder = "urgent".equals(work.getDingdanleixing());
        result.warning = true;
        result.parallelCount = 0;
        return result;
    }

    // 插单排产逻辑
    private void performInsertPaichanCalculation() {
        try {

            // 检查数据是否为空
            if (paiChanList == null) {
                throw new RuntimeException("排产数据为空");
            }
            if (shengChanXianList == null) {
                throw new RuntimeException("生产线数据为空");
            }

            // 检查是否有数据
            if (paiChanList.isEmpty()) {
                Log.w("Paichan", "排产数据列表为空，跳过插单计算");
                return;
            }
            if (shengChanXianList.isEmpty()) {
                Log.w("Paichan", "生产线数据为空，跳过插单计算");
                return;
            }
            // 1. 按订单号分组
            Map<String, List<WorkDetailDTO>> orderGroups = new HashMap<>();
            for (WorkDetailDTO work : paiChanList) {
                String key = work.getDingdanhao();
                if (!orderGroups.containsKey(key)) {
                    orderGroups.put(key, new ArrayList<>());
                }
                orderGroups.get(key).add(work);
            }

            // 2. 准备生产线数据
            Map<String, List<ProductionLine>> productionLines = new HashMap<>();
            for (ShengChanXian line : shengChanXianList) {
                String process = line.getGongxu();
                if (!productionLines.containsKey(process)) {
                    productionLines.put(process, new ArrayList<>());
                }

                ProductionLine pl = new ProductionLine();
                pl.id = line.getId();
                pl.name = line.getMingcheng();
                pl.processName = line.getGongxu();
                pl.efficiency = convertToDouble(line.getXiaolv());
                pl.availableFrom = null;
                pl.schedule = new ArrayList<>();
                pl.pausedTasks = new ArrayList<>();
                pl.activeTask = null;
                pl.isPaused = false;
                pl.pausedAt = null;

                productionLines.get(process).add(pl);
            }

            // 3. 分离插单和普通订单
            List<InsertOrder> insertOrders = new ArrayList<>();
            List<NormalOrder> normalOrders = new ArrayList<>();

            for (Map.Entry<String, List<WorkDetailDTO>> entry : orderGroups.entrySet()) {
                String orderId = entry.getKey();
                List<WorkDetailDTO> works = entry.getValue();

                // 判断是否为插单订单
                boolean isInsertOrder = false;
                Date insertStartTime = null;

                for (WorkDetailDTO work : works) {
                    if ((work.getCharu() != null && work.getCharu() == 1) ||
                            "urgent".equals(work.getDingdanleixing())) {
                        isInsertOrder = true;

                        // 解析开始时间
                        Date currentStartTime = parseStringToDate(work.getKaishishijian());
                        if (insertStartTime == null ||
                                (currentStartTime != null && currentStartTime.before(insertStartTime))) {
                            insertStartTime = currentStartTime;
                        }
                    }
                }

                if (isInsertOrder) {
                    insertOrders.add(new InsertOrder(orderId, works, insertStartTime));
                } else {
                    normalOrders.add(new NormalOrder(orderId, works));
                }
            }

            // 4. 按插单开始时间排序
            Collections.sort(insertOrders, (a, b) -> {
                if (a.insertStartTime != null && b.insertStartTime != null) {
                    return a.insertStartTime.compareTo(b.insertStartTime);
                }
                return 0;
            });

            // 5. 如果没有插单订单，使用普通逻辑
            if (insertOrders.isEmpty()) {
                performNormalPaichanCalculation();
                return;
            }

            // 6. 获取第一个插单开始时间
            Date globalInsertStartTime = insertOrders.get(0).insertStartTime;

            // 7. 处理普通订单（插单前）
            for (NormalOrder orderItem : normalOrders) {
                for (WorkDetailDTO work : orderItem.works) {
                    processWorkBeforeInsert(work, productionLines, globalInsertStartTime);
                }
            }

            // 8. 处理插单订单
            for (InsertOrder insertOrder : insertOrders) {
                processInsertOrder(insertOrder, productionLines);
            }

            // 9. 处理剩余的普通订单
            for (NormalOrder orderItem : normalOrders) {
                for (WorkDetailDTO work : orderItem.works) {
                    processWorkAfterInsert(work, productionLines);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("插单排产计算失败: " + e.getMessage());
        }
    }

    // 处理插单前的工作
//    private void processWorkBeforeInsert(WorkDetailDTO work, Map<String, List<ProductionLine>> productionLines, Date insertStartTime) {
//        // 简化的插单前处理逻辑
//        // 实际实现需要更复杂的逻辑来处理部分执行和暂停
//
//        String processName = work.getGongxumingcheng();
//        List<ProductionLine> lines = productionLines.get(processName);
//
//        if (lines != null) {
//            // 解析工作开始时间 - 修改这里
//            Date workStartTime = parseStringToDate(work.getKaishishijian());
//
//            for (ProductionLine line : lines) {
//                if (Math.abs(line.efficiency - work.getGongxuxiaolv()) < 0.01 && !line.isBusy) {
//                    // 修改这里：使用解析后的时间
//                    Date startTime = line.availableFrom != null ? line.availableFrom : workStartTime;
//
//                    // 添加空值检查
//                    if (startTime != null && insertStartTime != null && startTime.before(insertStartTime)) {
//                        // 计算在插单开始前能完成多少
//                        double workableHours = calculateWorkableHours(startTime, insertStartTime);
//                        double workableQuantity = workableHours * work.getGongxuxiaolv();
//
//                        if (workableQuantity > 0) {
//                            PaichanResult result = new PaichanResult();
//                            result.orderId = work.getDingdanhao();
//                            result.processName = processName;
//                            result.quantity = Math.min(workableQuantity, work.getGongxushuliang());
//                            result.processEfficiency = work.getGongxuxiaolv();
//                            result.productionLine = line.name;
//                            result.lineEfficiency = line.efficiency;
//                            result.startTime = startTime;
//                            result.endTime = insertStartTime;
//                            result.requiredHours = result.quantity / work.getGongxuxiaolv();
//                            result.isInsertOrder = false;
//                            result.warning = false;
//                            result.parallelCount = 1;
//                            result.note = "部分执行（插单前）";
//
//                            paichanResultList.add(result);
//
//                            // 标记生产线为暂停状态
//                            line.isPaused = true;
//                            line.pausedAt = insertStartTime;
//                        }
//                    }
//                }
//            }
//        }
//    }

    // 处理插单前的工作
    private void processWorkBeforeInsert(WorkDetailDTO work, Map<String, List<ProductionLine>> productionLines, Date insertStartTime) {
        String processName = work.getGongxumingcheng();
        List<ProductionLine> lines = productionLines.get(processName);

        if (lines != null && insertStartTime != null) {
            // 解析工作开始时间
            Date workStartTime = parseStringToDate(work.getKaishishijian());

            // 如果订单没有开始时间，使用当前时间
            if (workStartTime == null) {
                workStartTime = new Date();
                Log.d("PaichanTimeDebug", "订单 " + work.getDingdanhao() + " 没有开始时间，使用当前时间");
            }

            // 如果订单开始时间晚于插单时间，直接跳过
            if (workStartTime.after(insertStartTime)) {
                Log.d("PaichanTimeDebug", "订单开始时间晚于插单时间，不进行插单前处理");
                return;
            }

            for (ProductionLine line : lines) {
                if (Math.abs(line.efficiency - work.getGongxuxiaolv()) < 0.01 && !line.isBusy) {
                    // 确定实际开始生产的时间
                    Date actualStartTime;

                    // 1. 生产线可用时间
                    if (line.availableFrom != null) {
                        actualStartTime = line.availableFrom;
                        Log.d("PaichanTimeDebug", "生产线 " + line.name + " 可用时间: " + formatDateTime(line.availableFrom));
                    }
                    // 2. 订单计划开始时间
                    else {
                        actualStartTime = workStartTime;
                    }

                    // 确保开始时间不晚于插单开始时间
                    if (actualStartTime.after(insertStartTime)) {
                        Log.d("PaichanTimeDebug", "开始时间晚于插单时间，跳过处理");
                        continue;
                    }

                    // 关键修改：计算从 actualStartTime 到 insertStartTime 的实际可用生产时间
                    double actualAvailableHours = calculateWorkableHours(actualStartTime, insertStartTime);

                    // 调试：记录可用时间
                    Log.d("PaichanTimeDebug", "从 " + formatDateTime(actualStartTime) +
                            " 到 " + formatDateTime(insertStartTime) +
                            " 的可用工时: " + actualAvailableHours + " 小时");

                    // 计算在这段时间内能生产的数量
                    double producibleQuantity = actualAvailableHours * work.getGongxuxiaolv();
                    double totalQuantity = work.getGongxushuliang();
                    double processedQuantity = Math.min(producibleQuantity, totalQuantity);
                    double remainingQuantity = totalQuantity - processedQuantity;

                    // 确保最小处理数量大于0
                    if (processedQuantity > 0.01) {
                        // 关键修改：直接使用插单开始时间作为结束时间
                        Date actualEndTime = insertStartTime;  // 固定为14点

                        // 重新计算实际需要的生产时间
                        double actualRequiredHours = processedQuantity / work.getGongxuxiaolv();

                        // 验证：实际需要的生产时间是否超过可用时间
                        if (actualRequiredHours > actualAvailableHours) {
                            // 如果需要的生产时间超过可用时间，按比例减少处理数量
                            processedQuantity = actualAvailableHours * work.getGongxuxiaolv();
                            remainingQuantity = totalQuantity - processedQuantity;
                            actualRequiredHours = processedQuantity / work.getGongxuxiaolv();

                            Log.d("PaichanTimeDebug", "调整处理数量为: " + processedQuantity +
                                    "，可用工时不足");
                        }

                        // 调试：记录最终时间
                        Log.d("PaichanTimeDebug", "最终 - 开始时间: " + formatDateTime(actualStartTime) +
                                ", 结束时间（插单时间）: " + formatDateTime(actualEndTime) +
                                ", 处理数量: " + processedQuantity +
                                ", 剩余数量: " + remainingQuantity);

                        PaichanResult result = new PaichanResult();
                        result.orderId = work.getDingdanhao();
                        result.processName = processName;
                        result.quantity = processedQuantity;
                        result.processEfficiency = work.getGongxuxiaolv();
                        result.productionLine = line.name;
                        result.lineEfficiency = line.efficiency;
                        result.startTime = actualStartTime;
                        result.endTime = actualEndTime;  // 固定为14点（插单开始时间）
                        result.requiredHours = actualRequiredHours;
                        result.isInsertOrder = false;
                        result.warning = false;
                        result.parallelCount = 1;
                        result.note = "部分执行（插单前） 处理数量：" + String.format("%.2f", processedQuantity) +
                                "/" + String.format("%.2f", totalQuantity) +
                                "，暂停于：" + formatDateTime(actualEndTime);

                        paichanResultList.add(result);

                        // 保存剩余数量到生产线状态中
                        ActiveTask activeTask = new ActiveTask();
                        activeTask.orderId = work.getDingdanhao();
                        activeTask.processName = processName;
                        activeTask.startTime = actualStartTime;
                        activeTask.endTime = actualEndTime;  // 固定为14点
                        activeTask.plannedQuantity = totalQuantity;
                        activeTask.processedQuantity = processedQuantity;
                        activeTask.remainingQuantity = remainingQuantity;
                        activeTask.efficiency = work.getGongxuxiaolv();

                        // 创建暂停任务记录
                        PausedTask pausedTask = new PausedTask();
                        pausedTask.task = activeTask;
                        pausedTask.pauseTime = actualEndTime;  // 暂停时间就是插单开始时间（14点）
                        pausedTask.insertOrderId = null;

                        if (line.pausedTasks == null) {
                            line.pausedTasks = new ArrayList<>();
                        }
                        line.pausedTasks.add(pausedTask);

                        // 标记生产线为暂停状态
                        line.isPaused = true;
                        line.pausedAt = actualEndTime;  // 暂停时间就是插单开始时间（14点）
                        line.activeTask = activeTask;
                        line.availableFrom = actualEndTime;  // 生产线从14点开始可用（插单后）

                        // 添加到排班计划
                        ScheduleItem scheduleItem = new ScheduleItem();
                        scheduleItem.orderId = work.getDingdanhao();
                        scheduleItem.startTime = actualStartTime;
                        scheduleItem.endTime = actualEndTime;  // 固定为14点
                        scheduleItem.quantity = processedQuantity;
                        scheduleItem.isParallel = false;

                        if (line.schedule == null) {
                            line.schedule = new ArrayList<>();
                        }
                        line.schedule.add(scheduleItem);

                        // 调试日志
                        Log.d("PaichanPause", "订单 " + work.getDingdanhao() +
                                " 在生产线 " + line.name + " 上被暂停于 " + formatDateTime(actualEndTime) +
                                "（插单开始时间）。已处理：" + processedQuantity +
                                "，剩余：" + remainingQuantity);
                    }
                }
            }
        }
    }

    // 处理插单订单
//    private void processInsertOrder(InsertOrder insertOrder, Map<String, List<ProductionLine>> productionLines) {
//        // 简化的插单处理逻辑
//        for (WorkDetailDTO work : insertOrder.works) {
//            String processName = work.getGongxumingcheng();
//            List<ProductionLine> lines = productionLines.get(processName);
//
//            if (lines != null) {
//                List<ProductionLine> matchedLines = new ArrayList<>();
//                for (ProductionLine line : lines) {
//                    if (Math.abs(line.efficiency - work.getGongxuxiaolv()) < 0.01) {
//                        matchedLines.add(line);
//                    }
//                }
//
//                if (!matchedLines.isEmpty()) {
//                    // 使用所有匹配的生产线并行处理插单
//                    double totalEfficiency = 0;
//                    for (ProductionLine line : matchedLines) {
//                        totalEfficiency += line.efficiency;
//                    }
//
//                    double requiredHours = work.getGongxushuliang() / totalEfficiency;
//                    Date startTime = insertOrder.insertStartTime;
//                    Date endTime = calculateWorkTime(startTime, requiredHours);
//
//                    // 创建结果
//                    PaichanResult result = new PaichanResult();
//                    result.orderId = work.getDingdanhao();
//                    result.processName = processName;
//                    result.quantity = work.getGongxushuliang();
//                    result.processEfficiency = work.getGongxuxiaolv();
//
//                    StringBuilder linesBuilder = new StringBuilder();
//                    for (ProductionLine line : matchedLines) {
//                        if (linesBuilder.length() > 0) linesBuilder.append("、");
//                        linesBuilder.append(line.name);
//                    }
//                    result.productionLine = linesBuilder.toString();
//                    result.lineEfficiency = totalEfficiency;
//                    result.startTime = startTime;
//                    result.endTime = endTime;
//                    result.requiredHours = requiredHours;
//                    result.isInsertOrder = true;
//                    result.warning = false;
//                    result.parallelCount = matchedLines.size();
//                    result.note = "插单执行";
//
//                    paichanResultList.add(result);
//
//                    // 更新生产线状态
//                    for (ProductionLine line : matchedLines) {
//                        line.availableFrom = endTime;
//                        line.isPaused = false;
//                        line.pausedAt = null;
//                    }
//                }
//            }
//        }
//    }

    // 处理插单订单
    private void processInsertOrder(InsertOrder insertOrder, Map<String, List<ProductionLine>> productionLines) {
        // 记录所有被此插单中断的任务
        List<PausedTask> interruptedTasks = new ArrayList<>();

        // 先收集所有被中断的生产线
        for (String processName : productionLines.keySet()) {
            List<ProductionLine> lines = productionLines.get(processName);
            if (lines != null) {
                for (ProductionLine line : lines) {
                    if (line.isPaused && line.pausedTasks != null && !line.pausedTasks.isEmpty()) {
                        // 更新暂停任务，记录是哪个插单订单导致的中断
                        for (PausedTask pausedTask : line.pausedTasks) {
                            pausedTask.insertOrderId = insertOrder.orderId;
                            interruptedTasks.add(pausedTask);

                            // 调试日志
                            Log.d("PaichanInsert", "插单 " + insertOrder.orderId +
                                    " 中断了订单 " + pausedTask.task.orderId +
                                    " 在生产线 " + line.name);
                        }
                    }
                }
            }
        }

        // 处理插单订单
        for (WorkDetailDTO work : insertOrder.works) {
            String processName = work.getGongxumingcheng();
            List<ProductionLine> lines = productionLines.get(processName);

            if (lines != null) {
                List<ProductionLine> matchedLines = new ArrayList<>();
                for (ProductionLine line : lines) {
                    if (Math.abs(line.efficiency - work.getGongxuxiaolv()) < 0.01) {
                        matchedLines.add(line);
                    }
                }

                if (!matchedLines.isEmpty()) {
                    // 使用所有匹配的生产线并行处理插单
                    double totalEfficiency = 0;
                    for (ProductionLine line : matchedLines) {
                        totalEfficiency += line.efficiency;
                    }

                    double requiredHours = work.getGongxushuliang() / totalEfficiency;
                    Date startTime = insertOrder.insertStartTime != null ?
                            insertOrder.insertStartTime : new Date();
                    Date endTime = calculateWorkTime(startTime, requiredHours);

                    // 创建结果
                    PaichanResult result = new PaichanResult();
                    result.orderId = work.getDingdanhao();
                    result.processName = processName;
                    result.quantity = work.getGongxushuliang();
                    result.processEfficiency = work.getGongxuxiaolv();

                    StringBuilder linesBuilder = new StringBuilder();
                    for (ProductionLine line : matchedLines) {
                        if (linesBuilder.length() > 0) linesBuilder.append("、");
                        linesBuilder.append(line.name);
                    }
                    result.productionLine = linesBuilder.toString();
                    result.lineEfficiency = totalEfficiency;
                    result.startTime = startTime;
                    result.endTime = endTime;
                    result.requiredHours = requiredHours;
                    result.isInsertOrder = true;
                    result.warning = false;
                    result.parallelCount = matchedLines.size();

                    // 记录中断了多少任务
                    String interruptNote = interruptedTasks.isEmpty() ? "" :
                            "（中断了" + interruptedTasks.size() + "个任务）";
                    result.note = "插单执行" + interruptNote;

                    paichanResultList.add(result);

                    // 更新生产线状态
                    for (ProductionLine line : matchedLines) {
                        line.availableFrom = endTime;
                        line.isPaused = false;  // 生产线被插单占用，不再处于暂停状态
                        line.pausedAt = null;

                        // 添加到排班计划
                        ScheduleItem scheduleItem = new ScheduleItem();
                        scheduleItem.orderId = work.getDingdanhao();
                        scheduleItem.startTime = startTime;
                        scheduleItem.endTime = endTime;
                        scheduleItem.quantity = work.getGongxushuliang();
                        scheduleItem.isParallel = matchedLines.size() > 1;

                        if (line.schedule == null) {
                            line.schedule = new ArrayList<>();
                        }
                        line.schedule.add(scheduleItem);

                        // 调试日志
                        Log.d("PaichanInsert", "插单 " + work.getDingdanhao() +
                                " 在生产线 " + line.name + " 上执行，" +
                                "开始时间：" + formatDateTime(startTime) +
                                "，结束时间：" + formatDateTime(endTime));
                    }
                }
            }
        }
    }

    // 处理插单后的工作
//    private void processWorkAfterInsert(WorkDetailDTO work, Map<String, List<ProductionLine>> productionLines) {
//        // 简化的插单后处理逻辑
//        String processName = work.getGongxumingcheng();
//        List<ProductionLine> lines = productionLines.get(processName);
//
//        if (lines != null) {
//            // 解析工作开始时间 - 修改这里
//            Date workStartTime = parseStringToDate(work.getKaishishijian());
//
//            for (ProductionLine line : lines) {
//                if (Math.abs(line.efficiency - work.getGongxuxiaolv()) < 0.01 &&
//                        (line.isPaused || !line.isBusy)) {
//
//                    // 修改这里：使用解析后的时间
//                    Date startTime = line.availableFrom != null ? line.availableFrom : workStartTime;
//                    if (line.isPaused) {
//                        startTime = line.pausedAt;
//                    }
//
//                    // 添加空值检查
//                    if (startTime != null) {
//                        double requiredHours = work.getGongxushuliang() / work.getGongxuxiaolv();
//                        Date endTime = calculateWorkTime(startTime, requiredHours);
//
//                        PaichanResult result = new PaichanResult();
//                        result.orderId = work.getDingdanhao();
//                        result.processName = processName;
//                        result.quantity = work.getGongxushuliang();
//                        result.processEfficiency = work.getGongxuxiaolv();
//                        result.productionLine = line.name;
//                        result.lineEfficiency = line.efficiency;
//                        result.startTime = startTime;
//                        result.endTime = endTime;
//                        result.requiredHours = requiredHours;
//                        result.isInsertOrder = false;
//                        result.warning = false;
//                        result.parallelCount = 1;
//                        result.note = line.isPaused ? "恢复执行（插单后）" : "延迟执行（插单后）";
//
//                        paichanResultList.add(result);
//
//                        // 更新生产线状态
//                        line.availableFrom = endTime;
//                        line.isPaused = false;
//                        line.pausedAt = null;
//
//                        break;
//                    }
//                }
//            }
//        }
//    }

    // 处理插单后的工作
    private void processWorkAfterInsert(WorkDetailDTO work, Map<String, List<ProductionLine>> productionLines) {
        String processName = work.getGongxumingcheng();
        List<ProductionLine> lines = productionLines.get(processName);

        if (lines != null) {
            // 解析工作开始时间
            Date workStartTime = parseStringToDate(work.getKaishishijian());

            for (ProductionLine line : lines) {
                if (Math.abs(line.efficiency - work.getGongxuxiaolv()) < 0.01) {
                    double quantityToProcess = work.getGongxushuliang();  // 默认使用工单数量
                    Date startTime = line.availableFrom != null ? line.availableFrom : workStartTime;
                    String note = "";

                    // 检查是否有暂停的任务需要继续
                    if (line.pausedTasks != null && !line.pausedTasks.isEmpty()) {
                        // 找到这个订单的暂停任务
                        for (PausedTask pausedTask : line.pausedTasks) {
                            if (pausedTask.task.orderId.equals(work.getDingdanhao())) {
                                // 使用剩余数量继续处理
                                quantityToProcess = pausedTask.task.remainingQuantity;
                                startTime = line.pausedAt != null ? line.pausedAt : line.availableFrom;

                                // 记录暂停信息
                                note = "恢复执行（剩余" + String.format("%.2f", quantityToProcess) +
                                        "，已处理" + String.format("%.2f", pausedTask.task.processedQuantity) + "）";

                                // 移除已处理的暂停任务
                                line.pausedTasks.remove(pausedTask);

                                // 调试日志
                                Log.d("PaichanResume", "订单 " + work.getDingdanhao() +
                                        " 恢复执行，剩余数量：" + quantityToProcess);
                                break;
                            }
                        }
                    }

                    // 如果没有找到暂停任务，则是普通任务
                    if (note.isEmpty()) {
                        note = "延迟执行（插单后）";
                    }

                    if (startTime != null && quantityToProcess > 0.01) {
                        double requiredHours = quantityToProcess / work.getGongxuxiaolv();
                        Date endTime = calculateWorkTime(startTime, requiredHours);

                        PaichanResult result = new PaichanResult();
                        result.orderId = work.getDingdanhao();
                        result.processName = processName;
                        result.quantity = quantityToProcess;  // 使用正确的数量（可能是剩余数量）
                        result.processEfficiency = work.getGongxuxiaolv();
                        result.productionLine = line.name;
                        result.lineEfficiency = line.efficiency;
                        result.startTime = startTime;
                        result.endTime = endTime;
                        result.requiredHours = requiredHours;
                        result.isInsertOrder = false;
                        result.warning = false;
                        result.parallelCount = 1;
                        result.note = note;

                        paichanResultList.add(result);

                        // 更新生产线状态
                        line.availableFrom = endTime;
                        line.isPaused = false;
                        line.pausedAt = null;

                        // 添加到排班计划
                        ScheduleItem scheduleItem = new ScheduleItem();
                        scheduleItem.orderId = work.getDingdanhao();
                        scheduleItem.startTime = startTime;
                        scheduleItem.endTime = endTime;
                        scheduleItem.quantity = quantityToProcess;
                        scheduleItem.isParallel = false;

                        if (line.schedule == null) {
                            line.schedule = new ArrayList<>();
                        }
                        line.schedule.add(scheduleItem);

                        break;  // 找到一个合适的生产线后就退出循环
                    }
                }
            }
        }
    }

    // 计算两个时间点之间的有效工作时间
    private double calculateWorkableHours(Date startTime, Date endTime) {
        if (timeConfigList == null || timeConfigList.isEmpty()) {
            // 没有时间配置，按连续时间计算
            return (endTime.getTime() - startTime.getTime()) / (1000.0 * 60 * 60);
        }

        Calendar current = Calendar.getInstance();
        current.setTime(startTime);
        Calendar target = Calendar.getInstance();
        target.setTime(endTime);

        double totalHours = 0;

        while (current.before(target)) {
            int week = current.get(Calendar.DAY_OF_WEEK);
            int weekNum = (week == Calendar.SUNDAY) ? 7 : week - 1;

            TimeConfig config = null;
            for (TimeConfig tc : timeConfigList) {
                if (tc.getWeek() == weekNum) {
                    config = tc;
                    break;
                }
            }

            if (config != null) {
                // 计算当天的工作时间
                totalHours += calculateDailyWorkHours(current, target, config);
            }

            // 移动到下一天
            current.add(Calendar.DATE, 1);
            current.set(Calendar.HOUR_OF_DAY, 0);
            current.set(Calendar.MINUTE, 0);
            current.set(Calendar.SECOND, 0);
        }

        return totalHours;
    }

    // 计算当天的工作时间
    private double calculateDailyWorkHours(Calendar current, Calendar target, TimeConfig config) {
        double hours = 0;

        // 上午时间段
        hours += calculateTimeSegmentHours(current, target, config.getMorning_start(), config.getMorning_end());

        // 中午时间段
        hours += calculateTimeSegmentHours(current, target, config.getNoon_start(), config.getNoon_end());

        // 晚上时间段
        hours += calculateTimeSegmentHours(current, target, config.getNight_start(), config.getNight_end());

        return hours;
    }

    // 计算时间段内的工作时间
    private double calculateTimeSegmentHours(Calendar current, Calendar target, String startStr, String endStr) {
        if (startStr == null || endStr == null) return 0;

        String[] startParts = startStr.split(":");
        String[] endParts = endStr.split(":");

        if (startParts.length < 2 || endParts.length < 2) return 0;

        int startHour = Integer.parseInt(startParts[0]);
        int startMinute = Integer.parseInt(startParts[1]);
        int endHour = Integer.parseInt(endParts[0]);
        int endMinute = Integer.parseInt(endParts[1]);

        // 创建时间段的开始和结束时间
        Calendar segmentStart = (Calendar) current.clone();
        segmentStart.set(Calendar.HOUR_OF_DAY, startHour);
        segmentStart.set(Calendar.MINUTE, startMinute);
        segmentStart.set(Calendar.SECOND, 0);

        Calendar segmentEnd = (Calendar) current.clone();
        segmentEnd.set(Calendar.HOUR_OF_DAY, endHour);
        segmentEnd.set(Calendar.MINUTE, endMinute);
        segmentEnd.set(Calendar.SECOND, 0);

        // 计算重叠部分
        Calendar overlapStart = current.after(segmentStart) ? current : segmentStart;
        Calendar overlapEnd = target.before(segmentEnd) ? target : segmentEnd;

        if (overlapStart.before(overlapEnd)) {
            long overlapMillis = overlapEnd.getTimeInMillis() - overlapStart.getTimeInMillis();
            return overlapMillis / (1000.0 * 60 * 60);
        }

        return 0;
    }

    // 修复：计算工作时间（考虑工作日时间段）
    private Date calculateWorkTime(Date startTime, double requiredHours) {
        if (timeConfigList == null || timeConfigList.isEmpty()) {
            // 没有时间配置，按连续时间计算
            return new Date(startTime.getTime() + (long)(requiredHours * 60 * 60 * 1000));
        }

        Calendar current = Calendar.getInstance();
        current.setTime(startTime);

        // 确保当前时间在工作时间段内开始
        adjustToWorkTime(current);

        double remainingHours = requiredHours;
        int dayCount = 0;
        int maxDays = 365; // 防止无限循环

        while (remainingHours > 0 && dayCount < maxDays) {
            // 获取星期几（1-7，1=周一，7=周日）
            int weekDay = getWeekDay(current);

            // 查找对应的工作日配置
            TimeConfig config = getTimeConfigForWeekDay(weekDay);

            if (config != null && config.isWorkDay()) {
                // 当天是工作日，计算当天可用的工作时间
                double availableHoursToday = calculateAvailableHoursToday(current, config);

                if (availableHoursToday > 0) {
                    double workToday = Math.min(availableHoursToday, remainingHours);
                    remainingHours -= workToday;

                    if (remainingHours <= 0) {
                        // 今天内就能完成，直接在当前时间上增加工作时间
                        current.add(Calendar.MILLISECOND, (int)(workToday * 60 * 60 * 1000));
                        break;
                    }
                }
            }

            // 移动到下一天的开始（0点）
            moveToNextDayStart(current);
            dayCount++;
        }

        return current.getTime();
    }

    // 新增：获取星期几（1=周一，2=周二，...，7=周日）
    private int getWeekDay(Calendar calendar) {
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        // Calendar: 1=周日, 2=周一, ..., 7=周六
        // 转换为：1=周一, 2=周二, ..., 7=周日
        int weekDay = dayOfWeek - 1;
        if (weekDay == 0) weekDay = 7; // 周日
        return weekDay;
    }

    // 新增：根据星期获取时间配置
    private TimeConfig getTimeConfigForWeekDay(int weekDay) {
        if (timeConfigList == null) return null;

        for (TimeConfig config : timeConfigList) {
            if (config.getWeek() == weekDay) {
                return config;
            }
        }
        return null; // 没有找到配置，可能是休息日
    }

    // 新增：调整到工作时间段内
    private void adjustToWorkTime(Calendar current) {
        int weekDay = getWeekDay(current);
        TimeConfig config = getTimeConfigForWeekDay(weekDay);

        if (config == null || !config.isWorkDay()) {
            // 非工作日，直接移动到下一个工作日的开始
            moveToNextWorkDayStart(current);
            return;
        }

        // 检查当前时间是否在工作时间内
        double currentHour = getCurrentHour(current);

        // 计算当前在哪个时间段
        boolean inWorkTime = false;

        // 检查上午时间段
        if (isInTimeSegment(currentHour, config.getMorning_start(), config.getMorning_end())) {
            inWorkTime = true;
        }
        // 检查中午时间段
        else if (isInTimeSegment(currentHour, config.getNoon_start(), config.getNoon_end())) {
            inWorkTime = true;
        }
        // 检查晚上时间段
        else if (isInTimeSegment(currentHour, config.getNight_start(), config.getNight_end())) {
            inWorkTime = true;
        }

        if (!inWorkTime) {
            // 不在工作时间，调整到下一个工作时间段的开始
            adjustToNextWorkTime(current, config);
        }
    }

    // 新增：判断是否在时间段内
    private boolean isInTimeSegment(double currentHour, String startStr, String endStr) {
        if (startStr == null || endStr == null) return false;

        double startHour = timeToHours(startStr);
        double endHour = timeToHours(endStr);

        return currentHour >= startHour && currentHour < endHour;
    }

    // 新增：调整到下一个工作时间段
    private void adjustToNextWorkTime(Calendar current, TimeConfig config) {
        double currentHour = getCurrentHour(current);

        // 尝试找下一个工作时间段
        String[][] timeSegments = {
                {config.getMorning_start(), config.getMorning_end()},
                {config.getNoon_start(), config.getNoon_end()},
                {config.getNight_start(), config.getNight_end()}
        };

        for (String[] segment : timeSegments) {
            if (segment[0] != null && segment[1] != null) {
                double startHour = timeToHours(segment[0]);
                if (currentHour < startHour) {
                    // 设置到该时间段的开始
                    setTimeToSegmentStart(current, segment[0]);
                    return;
                }
            }
        }

        // 当天没有更晚的工作时间了，移动到下一天的第一个工作时间段
        moveToNextDayStart(current);
        adjustToWorkTime(current); // 递归调用
    }

    // 新增：计算当天可用的工作时间
    private double calculateAvailableHoursToday(Calendar current, TimeConfig config) {
        double currentHour = getCurrentHour(current);
        double availableHours = 0;

        // 计算剩余工作时间段
        String[][] timeSegments = {
                {config.getMorning_start(), config.getMorning_end()},
                {config.getNoon_start(), config.getNoon_end()},
                {config.getNight_start(), config.getNight_end()}
        };

        for (String[] segment : timeSegments) {
            if (segment[0] != null && segment[1] != null) {
                double startHour = timeToHours(segment[0]);
                double endHour = timeToHours(segment[1]);

                if (currentHour < endHour) {
                    // 当前时间在该时间段结束之前
                    double segmentStart = Math.max(startHour, currentHour);
                    availableHours += Math.max(0, endHour - segmentStart);
                }
            }
        }

        return availableHours;
    }

    // 新增：获取当前时间的小时数（含分钟）
    private double getCurrentHour(Calendar calendar) {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return hour + minute / 60.0;
    }

    // 新增：设置时间到时间段的开始
    private void setTimeToSegmentStart(Calendar calendar, String startTimeStr) {
        if (startTimeStr == null) return;

        String[] parts = startTimeStr.split(":");
        if (parts.length >= 2) {
            int hour = Integer.parseInt(parts[0]);
            int minute = Integer.parseInt(parts[1]);

            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
        }
    }

    // 新增：移动到下一天开始（0点）
    private void moveToNextDayStart(Calendar calendar) {
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    // 新增：移动到下一个工作日的开始
    private void moveToNextWorkDayStart(Calendar calendar) {
        int maxDays = 365;
        for (int i = 0; i < maxDays; i++) {
            moveToNextDayStart(calendar);
            int weekDay = getWeekDay(calendar);
            TimeConfig config = getTimeConfigForWeekDay(weekDay);

            if (config != null && config.isWorkDay()) {
                // 设置到第一个工作时间段的开始
                if (config.getMorning_start() != null) {
                    setTimeToSegmentStart(calendar, config.getMorning_start());
                }
                break;
            }
        }
    }

    // 计算当天可用的工作时间
    private double calculateAvailableDayHours(Calendar current, TimeConfig config) {
        double hours = 0;

        // 当前时间
        int currentHour = current.get(Calendar.HOUR_OF_DAY);
        int currentMinute = current.get(Calendar.MINUTE);
        double currentTimeInHours = currentHour + currentMinute / 60.0;

        // 计算上午时间段
        hours += calculateSegmentAvailableHours(currentTimeInHours, config.getMorning_start(), config.getMorning_end());

        // 计算中午时间段（如果上午已过）
        if (currentTimeInHours >= timeToHours(config.getMorning_end())) {
            hours += calculateSegmentAvailableHours(currentTimeInHours, config.getNoon_start(), config.getNoon_end());
        }

        // 计算晚上时间段（如果中午已过）
        if (currentTimeInHours >= timeToHours(config.getNoon_end())) {
            hours += calculateSegmentAvailableHours(currentTimeInHours, config.getNight_start(), config.getNight_end());
        }

        return hours;
    }

    // 计算时间段内的可用时间
    private double calculateSegmentAvailableHours(double currentTime, String startStr, String endStr) {
        if (startStr == null || endStr == null) return 0;

        double startHours = timeToHours(startStr);
        double endHours = timeToHours(endStr);

        if (currentTime < startHours) {
            return endHours - startHours;
        } else if (currentTime >= startHours && currentTime < endHours) {
            return endHours - currentTime;
        }

        return 0;
    }

    // 时间字符串转换为小时数
    private double timeToHours(String timeStr) {
        if (timeStr == null) return 0;
        String[] parts = timeStr.split(":");
        if (parts.length < 2) return 0;
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        return hours + minutes / 60.0;
    }

    private String formatDateTime(Date date) {
        if (date == null) return "";
        try {
            // 使用正确的时区和格式
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            String result = sdf.format(date);
            Log.d("PaichanDebug", "格式化日期: " + date + " -> " + result);
            return result;
        } catch (Exception e) {
            Log.e("PaichanDebug", "日期格式化异常: " + e.getMessage());
            return date.toString();
        }
    }

    // ============== 内部类定义 ==============

    // 并行处理配置类
    class ParallelConfig {
        boolean enable = true;
        int minQuantityForParallel = 500;
        int maxParallelLines = 3;
        double efficiencyThreshold = 0.01;
        double timePressureFactor = 0.8;
    }

    // 排产结果类
    class PaichanResult {
        String orderId;
        String processName;
        double quantity;
        double processEfficiency;
        String productionLine;
        double lineEfficiency;
        Date startTime;
        Date endTime;
        double requiredHours;
        boolean isInsertOrder;
        boolean warning;
        int parallelCount;
        String note; // 备注信息
    }

    // 生产线类
    class ProductionLine {
        int id;
        String name;
        String processName;
        double efficiency;
        Date availableFrom;
        List<ScheduleItem> schedule;
        boolean isBusy;
        List<PausedTask> pausedTasks;
        ActiveTask activeTask;
        boolean isPaused;
        Date pausedAt;
    }

    // 排班计划项
    class ScheduleItem {
        String orderId;
        Date startTime;
        Date endTime;
        double quantity;
        boolean isParallel;
    }

    // 暂停的任务
    class PausedTask {
        ActiveTask task;
        Date pauseTime;
        String insertOrderId;
    }

    // 活动任务
//    class ActiveTask {
//        String orderId;
//        String processName;
//        Date startTime;
//        Date endTime;
//        double plannedQuantity;
//        double efficiency;
//    }

    // 活动任务
    class ActiveTask {
        String orderId;
        String processName;
        Date startTime;
        Date endTime;
        double plannedQuantity;     // 总计划数量
        double processedQuantity;   // 已处理数量
        double remainingQuantity;   // 剩余需要处理的数量
        double efficiency;

        // 添加构造函数和辅助方法
        ActiveTask() {
            this.plannedQuantity = 0;
            this.processedQuantity = 0;
            this.remainingQuantity = 0;
        }

        // 计算进度百分比
        double getProgressPercentage() {
            if (plannedQuantity <= 0) return 0;
            return (processedQuantity / plannedQuantity) * 100;
        }

        // 更新处理数量
        void updateProcessedQuantity(double newProcessed) {
            this.processedQuantity = newProcessed;
            this.remainingQuantity = this.plannedQuantity - this.processedQuantity;
        }
    }

    // 用于排序的订单类
    class OrderForSorting {
        String orderId;
        List<WorkDetailDTO> works;
        String earliestStartTime;  // 从 Date 改为 String
        boolean isUrgent;

        OrderForSorting(String orderId, List<WorkDetailDTO> works, String earliestStartTime, boolean isUrgent) {
            this.orderId = orderId;
            this.works = works;
            this.earliestStartTime = earliestStartTime;
            this.isUrgent = isUrgent;
        }
    }

    // 插单订单
    class InsertOrder {
        String orderId;
        List<WorkDetailDTO> works;
        Date insertStartTime;

        InsertOrder(String orderId, List<WorkDetailDTO> works, Date insertStartTime) {
            this.orderId = orderId;
            this.works = works;
            this.insertStartTime = insertStartTime;
        }
    }

    // 普通订单
    class NormalOrder {
        String orderId;
        List<WorkDetailDTO> works;

        NormalOrder(String orderId, List<WorkDetailDTO> works) {
            this.orderId = orderId;
            this.works = works;
        }
    }

    // 在 PaiChanResultActivity 中添加：
    private Date parseDateTimeFromString(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.isEmpty()) {
            return null;
        }
        try {
            // 处理带毫秒的格式
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

            // 去掉毫秒部分
            if (dateTimeStr.contains(".")) {
                dateTimeStr = dateTimeStr.substring(0, dateTimeStr.indexOf("."));
            }

            return sdf.parse(dateTimeStr);
        } catch (Exception e) {
            Log.e("PaichanDebug", "解析日期时间失败: " + dateTimeStr, e);
            return null;
        }
    }

    // 类型转换辅助方法
    private double convertToDouble(Object value) {
        if (value == null) {
            return 0.0;
        }

        if (value instanceof Double) {
            return (Double) value;
        } else if (value instanceof Integer) {
            return ((Integer) value).doubleValue();
        } else if (value instanceof String) {
            try {
                String strValue = (String) value;
                // 移除可能的空格和特殊字符
                strValue = strValue.trim();
                // 如果字符串为空
                if (strValue.isEmpty()) {
                    return 0.0;
                }
                return Double.parseDouble(strValue);
            } catch (NumberFormatException e) {
                Log.e("Paichan", "转换效率值失败: " + value, e);
                return 0.0;
            }
        } else if (value instanceof Float) {
            return ((Float) value).doubleValue();
        } else if (value instanceof Long) {
            return ((Long) value).doubleValue();
        }

        return 0.0;
    }
}
