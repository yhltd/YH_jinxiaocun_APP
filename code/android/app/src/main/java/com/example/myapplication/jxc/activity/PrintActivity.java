package com.example.myapplication.jxc.activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;
import  com.example.myapplication.jxc.entity.PrintData;
import  com.example.myapplication.jxc.entity.OrderItem;
import com.example.myapplication.utils.PrintTemplateManager;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.jxc.entity.YhJinXiaoCunMingXi;
import com.example.myapplication.jxc.entity.YhJinXiaoCunUser;
import com.example.myapplication.jxc.service.YhJinXiaoCunMingXiService;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PrintActivity extends AppCompatActivity  {

    private Spinner spPrintType, spOrderNumber;
    private LinearLayout llOrderSelection;
    private Button btnPreview, btnPrint;
    private ScrollView svPreview;
    private ImageView ivPreview;

    private YhJinXiaoCunMingXiService yhJinXiaoCunMingXiService;
    private YhJinXiaoCunUser yhJinXiaoCunUser;

    private List<String> orderNumbers = new ArrayList<>();
    private List<PrintData> printDataList = new ArrayList<>();
    private PrintData currentPrintData;
    private String currentPrintType = "";

    // 打印类型常量
    private static final String PRINT_TYPE_SIMPLE = "simple";
    private static final String PRINT_TYPE_DETAILED = "detailed";
    private static final String PRINT_TYPE_INVOICE = "invoice";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);

        initViews();
        initPrintTypeSpinner();
    }

    private void initViews() {
        spPrintType = findViewById(R.id.spPrintType);
        spOrderNumber = findViewById(R.id.spOrderNumber);
        llOrderSelection = findViewById(R.id.llOrderSelection);
        btnPreview = findViewById(R.id.btnPreview);
        btnPrint = findViewById(R.id.btnPrint);
        svPreview = findViewById(R.id.svPreview);
        ivPreview = findViewById(R.id.ivPreview);

        // 初始化用户信息（根据您的实际代码调整）
        yhJinXiaoCunUser = getCurrentUser();

        setupEventListeners();
    }

    private void setupEventListeners() {
        // 打印类型选择监听
        spPrintType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    String[] types = getResources().getStringArray(R.array.print_types_value);
                    currentPrintType = types[position];
                    llOrderSelection.setVisibility(View.VISIBLE);
                    loadOrderNumbers();
                } else {
                    currentPrintType = "";
                    llOrderSelection.setVisibility(View.GONE);
                    btnPreview.setEnabled(false);
                    btnPrint.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // 订单号选择监听
        spOrderNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    String selectedOrder = orderNumbers.get(position - 1);
                    loadOrderData(selectedOrder);
                } else {
                    currentPrintData = null;
                    btnPreview.setEnabled(false);
                    btnPrint.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // 预览按钮
        btnPreview.setOnClickListener(v -> {
            if (currentPrintData != null && !currentPrintType.isEmpty()) {
                // 跳转到新的预览页面
                Intent intent = new Intent(PrintActivity.this, ZoomPreviewActivity.class);
                intent.putExtra("print_type", currentPrintType);
                intent.putExtra("print_data", currentPrintData);
                startActivity(intent);
            } else {
                Toast.makeText(PrintActivity.this, "请先选择订单和打印类型", Toast.LENGTH_SHORT).show();
            }
        });

        // 打印按钮
        btnPrint.setOnClickListener(v -> {
            if (currentPrintData != null) {
                executePrint();
            }
        });
    }

    private void initPrintTypeSpinner() {
        // 设置打印类型Spinner
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this,
                R.array.print_types, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPrintType.setAdapter(typeAdapter);
    }
    // 获取选中打印类型的辅助方法
    private String getSelectedPrintType() {
        if (spPrintType.getSelectedItem() != null) {
            return spPrintType.getSelectedItem().toString();
        }
        return ""; // 或者返回默认类型
    }

    private void loadOrderNumbers() {
        btnPreview.setEnabled(false);
        btnPrint.setEnabled(false);
        String printType = getSelectedPrintType();
        Handler loadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                setupOrderSpinner(orderNumbers);
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    yhJinXiaoCunMingXiService = new YhJinXiaoCunMingXiService();

                    // 查询订单号列表 - 根据您的实际业务调整查询方法
                    List<YhJinXiaoCunMingXi> orderlist = yhJinXiaoCunMingXiService.getdingdan(yhJinXiaoCunUser.getGongsi(),printType);
                    // ！！！关键修复：将查询结果赋值给 orderNumbers ！！！
                    orderNumbers.clear(); // 清空原有数据
                    for (YhJinXiaoCunMingXi order : orderlist) {
                        // 根据实际字段名获取订单号，可能是 getDingdanhao() 或其他
                        orderNumbers.add(order.getOrderid());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() ->
                            Toast.makeText(PrintActivity.this, "加载订单失败: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }

                loadHandler.sendMessage(new Message());
            }
        }).start();
    }

    private void setupOrderSpinner(List<String> orderList) {
        List<String> spinnerItems = new ArrayList<>();
        spinnerItems.add("请选择订单号");
        spinnerItems.addAll(orderList);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, spinnerItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spOrderNumber.setAdapter(adapter);
    }

    private void loadOrderData(String orderNumber) {
        btnPreview.setEnabled(false);
        btnPrint.setEnabled(false);

        Handler loadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (currentPrintData != null) {
                    btnPreview.setEnabled(true);
                    btnPrint.setEnabled(true);
                    Toast.makeText(PrintActivity.this, "订单数据加载成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PrintActivity.this, "加载订单数据失败", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    yhJinXiaoCunMingXiService = new YhJinXiaoCunMingXiService();

                    // 根据订单号查询详细数据 - 根据您的实际业务调整查询方法
                    List<YhJinXiaoCunMingXi> orderDetails = yhJinXiaoCunMingXiService.getchuruku(
                            yhJinXiaoCunUser.getGongsi(),
                            orderNumber
                    );

                    if (orderDetails != null && !orderDetails.isEmpty()) {
                        currentPrintData = convertToPrintData(orderDetails, orderNumber);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    currentPrintData = null;
                }

                loadHandler.sendMessage(new Message());
            }
        }).start();
    }

    private PrintData convertToPrintData(List<YhJinXiaoCunMingXi> orderDetails, String orderNumber) {
        PrintData printData = new PrintData();
        printData.setOrderNumber(orderNumber);

        if (!orderDetails.isEmpty()) {
            YhJinXiaoCunMingXi firstItem = orderDetails.get(0);
            printData.setStoreName(firstItem.getGongsi() != null ? firstItem.getGongsi() : "");
            printData.setOrderTime(firstItem.getShijian() != null ? firstItem.getShijian() : "");
            printData.setCustomerName(firstItem.getShou_h() != null ? firstItem.getShou_h() : "");
            printData.setCangku(firstItem.getcangku() != null ? firstItem.getcangku() : "");
            printData.setShou_h(firstItem.getShou_h() != null ? firstItem.getShou_h() : "");
            printData.setmxtype(firstItem.getMxtype() != null ? firstItem.getMxtype() : "");
        }

        List<OrderItem> items = new ArrayList<>();
        double totalAmount = 0.0;

        for (YhJinXiaoCunMingXi item : orderDetails) {
            // 安全转换数量和单价
            int quantity;
            double price;

            try {
                quantity = Integer.parseInt(item.getCpsl());
            } catch (Exception e) {
                quantity = 0;
            }

            try {
                price = Double.parseDouble(item.getCpsj());
            } catch (Exception e) {
                price = 0.0;
            }

            // 创建 OrderItem 对象 - 构造函数会自动计算 subtotal
            OrderItem orderItem = new OrderItem(
                    item.getCpname() != null ? item.getCpname() : "",
                    item.getcangku() != null ? item.getcangku() : "",
                    item.getShou_h() != null ? item.getShou_h() : "",
                    item.getSpDm() != null ? item.getSpDm() : "",
                    item.getCplb() != null ? item.getCplb() : "",
                    quantity,
                    price
            );
            items.add(orderItem);

            // 累加每行的金额到总金额
            totalAmount += orderItem.getSubtotal();

            // 调试信息
            System.out.println("商品: " + item.getCpname() +
                    ", 数量: " + quantity +
                    ", 单价: " + price +
                    ", 小计: " + orderItem.getSubtotal());
        }

        printData.setItems(items);
        printData.setTotalAmount(totalAmount);
        printData.setPaidAmount(totalAmount);
        printData.setPaymentMethod("现金");

        // 最终验证
        System.out.println("=== 订单汇总 ===");
        System.out.println("订单号: " + orderNumber);
        System.out.println("商品总数: " + items.size());
        System.out.println("计算总金额: " + totalAmount);

        return printData;
    }
    private void generatePreview() {
        if (currentPrintData != null && !currentPrintType.isEmpty()) {
            // 获取预览区域宽度
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int previewWidth = displayMetrics.widthPixels - 100; // 减去padding

            PrintTemplateManager templateManager = new PrintTemplateManager(this);
            Bitmap previewBitmap = templateManager.generatePreviewBitmap(
                    currentPrintType, currentPrintData, previewWidth);
            ivPreview.setImageBitmap(previewBitmap);
        }
    }



    private YhJinXiaoCunUser getCurrentUser() {
        // 根据您的实际代码获取当前用户信息
        YhJinXiaoCunUser user = new YhJinXiaoCunUser();
        user.setGongsi("北京登宏科技有限公司"); // 示例
        return user;
    }

    /**
     * 打印适配器
     */

    private class OptimizedPrintAdapter extends PrintDocumentAdapter {
        private PrintData printData;
        private String printType;
        private DecimalFormat decimalFormat; // 添加这行
        public OptimizedPrintAdapter(PrintData data, String type) {
            this.printData = data;
            this.printType = type;
            this.decimalFormat = new DecimalFormat("0.00"); // 初始化
        }

        @Override
        public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes,
                             CancellationSignal cancellationSignal,
                             LayoutResultCallback callback, Bundle metadata) {
            if (cancellationSignal.isCanceled()) {
                callback.onLayoutCancelled();
                return;
            }

            PrintDocumentInfo info = new PrintDocumentInfo.Builder("order_print_" + printData.getOrderNumber())
                    .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                    .setPageCount(1)
                    .build();
            callback.onLayoutFinished(info, true);
        }

        @Override
        public void onWrite(PageRange[] pages, ParcelFileDescriptor destination,
                            CancellationSignal cancellationSignal, WriteResultCallback callback) {
            android.graphics.pdf.PdfDocument document = null;
            try {
                if (cancellationSignal.isCanceled()) {
                    callback.onWriteCancelled();
                    return;
                }

                document = new android.graphics.pdf.PdfDocument();

                // 使用与预览相同的宽度
                int pageWidth = 400;
                // 使用统一的高度计算方法
                int pageHeight = calculatePdfHeight(printType, printData);

                android.graphics.pdf.PdfDocument.PageInfo pageInfo =
                        new android.graphics.pdf.PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();

                android.graphics.pdf.PdfDocument.Page page = document.startPage(pageInfo);
                Canvas canvas = page.getCanvas();

                // 设置白色背景（与预览一致）
                canvas.drawColor(Color.WHITE);

                // 直接使用预览的绘制逻辑，而不是通过PrintTemplateManager
                drawDirectToPdf(canvas, printType, printData, pageWidth, pageHeight);

                document.finishPage(page);

                if (cancellationSignal.isCanceled()) {
                    callback.onWriteCancelled();
                    return;
                }

                // 写入PDF文件
                try {
                    document.writeTo(new java.io.FileOutputStream(destination.getFileDescriptor()));
                    callback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
                } catch (java.io.IOException e) {
                    callback.onWriteFailed(e.getMessage());
                }

            } catch (Exception e) {
                callback.onWriteFailed("打印失败: " + e.getMessage());
                e.printStackTrace();
            } finally {
                if (document != null) {
                    document.close();
                }
            }
        }

        /**
         * 直接绘制到PDF Canvas（与预览使用相同的绘制逻辑）
         */
        private void drawDirectToPdf(Canvas canvas, String printType, PrintData printData, int width, int height) {
            // 创建与预览相同的Paint对象
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);

            // 根据打印类型调用相应的绘制方法
            switch (printType) {
                default:
                    drawSimpleReceipt(canvas, printData, width);
            }
        }

        private void drawSimpleReceipt(Canvas canvas, PrintData data, int width) {
            if (data == null) return;

            Paint paint = new Paint();
            paint.setColor(Color.BLACK);

            float y = 15;
            int margin = 8;

            // 标题
            paint.setTextSize(22);
            paint.setFakeBoldText(true);
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(data.getStoreName() != null ? data.getStoreName() : "店铺名称", width / 2, y, paint);
            y += 35;

            // 订单信息 - 先计算右边内容宽度
            paint.setTextSize(14);
            paint.setFakeBoldText(false);

            // 先测量右边内容的宽度
            String warehouseText = "仓库:" + (data.getCangku() != null ? data.getCangku() : "");
            String customerText = "客户:" + (data.getShou_h() != null ? data.getShou_h() : "");

            float warehouseWidth = paint.measureText(warehouseText);
            float customerWidth = paint.measureText(customerText);

            // 取两者中较大的作为右边区域宽度
            float rightAreaWidth = Math.max(warehouseWidth, customerWidth) + 10; // 加10像素边距

            // 左边区域的边界（右边区域开始的位置）
            float leftAreaEnd = width - margin - rightAreaWidth;

            // 第一行：订单号（左）和 仓库（右）
            paint.setTextAlign(Paint.Align.LEFT);
            String orderText = "订单号:" + data.getOrderNumber();
            // 确保订单号不超过左边区域边界
            if (paint.measureText(orderText) > leftAreaEnd - margin) {
                // 如果订单号太长，就换行显示
                canvas.drawText(orderText, margin, y, paint);
                y += 22;
                // 仓库信息在新的一行右对齐
                paint.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText(warehouseText, width - margin, y, paint);
            } else {
                canvas.drawText(orderText, margin, y, paint);
                paint.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText(warehouseText, width - margin, y, paint);
            }
            y += 22;

            // 第二行：时间（左）和 客户（右）
            paint.setTextAlign(Paint.Align.LEFT);
            String timeText = "时间:" + (data.getOrderTime() != null ? data.getOrderTime() : "");
            // 确保时间不超过左边区域边界
            if (paint.measureText(timeText) > leftAreaEnd - margin) {
                // 如果时间太长，就换行显示
                canvas.drawText(timeText, margin, y, paint);
                y += 22;
                // 客户信息在新的一行右对齐
                paint.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText(customerText, width - margin, y, paint);
            } else {
                canvas.drawText(timeText, margin, y, paint);
                paint.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText(customerText, width - margin, y, paint);
            }
            y += 22;

            // 第三行：类型（左对齐，占整行）
            paint.setTextAlign(Paint.Align.LEFT);
            String typeText = "类型:" + (data.getmxtype() != null ? data.getmxtype() : "");
            canvas.drawText(typeText, margin, y, paint);
            y += 28;

            // 分隔线
            paint.setStrokeWidth(2);
            canvas.drawLine(margin, y, width - margin, y, paint);
            y += 18;

            // 商品列表 - 基于右边区域宽度调整列位置
            if (data.getItems() != null && !data.getItems().isEmpty()) {
                // 根据右边区域宽度调整商品列表列位置
                int nameStart = margin + 5;
                int codeStart = (int) (width - rightAreaWidth - 50); // 在右边区域前留出空间
                int priceRight = width - margin;

                // 表头
                paint.setFakeBoldText(true);
                paint.setTextSize(15);

                paint.setTextAlign(Paint.Align.LEFT);
                canvas.drawText("商品名称", nameStart, y, paint);
                canvas.drawText("商品代码", codeStart, y, paint);

                paint.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText("金额", priceRight, y, paint);
                y += 24;

                // 表头分隔线
                paint.setStrokeWidth(1);
                canvas.drawLine(margin, y, width - margin, y, paint);
                y += 12;

                // 商品数据
                paint.setFakeBoldText(false);
                paint.setTextSize(13);
                for (OrderItem item : data.getItems()) {
                    // 商品名称（左对齐）
                    paint.setTextAlign(Paint.Align.LEFT);
                    String productName = item.getProductName();
                    if (productName == null) productName = "";
                    // 确保商品名称不超过代码列开始位置
                    if (paint.measureText(productName) > codeStart - nameStart - 10) {
                        productName = truncateTextToWidth(paint, productName, codeStart - nameStart - 10);
                    }
                    canvas.drawText(productName, nameStart, y, paint);

                    // 商品代码（左对齐）
                    String spdm = item.getSpdm() != null ? item.getSpdm() : "";
                    canvas.drawText(spdm, codeStart, y, paint);

                    // 金额（右对齐）
                    String priceText = "¥" + decimalFormat.format(item.getSubtotal());
                    paint.setTextAlign(Paint.Align.RIGHT);
                    canvas.drawText(priceText, priceRight, y, paint);

                    y += 20;
                }
                y += 10;
            }

            // 分隔线
            paint.setStrokeWidth(2);
            canvas.drawLine(margin, y, width - margin, y, paint);
            y += 18;

            // 总计（右对齐）
            paint.setFakeBoldText(true);
            paint.setTextSize(18);
            paint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText("总计: ¥" + decimalFormat.format(data.getTotalAmount()), width - margin, y, paint);
        }

        private String truncateTextToWidth(Paint paint, String text, float maxWidth) {
            if (text == null) return "";

            float textWidth = paint.measureText(text);
            if (textWidth <= maxWidth) {
                return text;
            }

            // 如果文本太长，进行截断
            int maxLength = text.length();
            while (maxLength > 3 && paint.measureText(text.substring(0, maxLength) + "...") > maxWidth) {
                maxLength--;
            }

            return text.substring(0, maxLength) + (maxLength < text.length() ? "..." : "");
        }

        /**
         * 计算PDF页面高度（复用 PrintTemplateManager 的逻辑）
         */
        private int calculatePdfHeight(String printType, PrintData printData) {
            PrintTemplateManager templateManager = new PrintTemplateManager(PrintActivity.this);

            try {
                // 方法1：通过反射调用私有的calculateTemplateHeight方法
                Method method = PrintTemplateManager.class.getDeclaredMethod("calculateTemplateHeight", String.class, PrintData.class);
                method.setAccessible(true);
                return (int) method.invoke(templateManager, printType, printData);
            } catch (Exception e) {
                // 方法2：如果反射失败，使用与预览相同的计算逻辑
                return calculateTemplateHeightDirectly(printType, printData);
            }
        }

        private int calculateTemplateHeightDirectly(String printType, PrintData printData) {
            int baseHeight = 200; // 基础高度（标题、订单信息、总计等）
            int itemHeight = 25;  // 每行商品的高度
            int itemCount = 0;

            if (printData != null && printData.getItems() != null) {
                itemCount = printData.getItems().size();
            }

            // 根据打印类型调整基础高度
            switch (printType) {
                case PrintTemplateManager.TYPE_SIMPLE:
                    baseHeight = 180; // 简单模板高度较小
                    break;
                case PrintTemplateManager.TYPE_DETAILED:
                    baseHeight = 220; // 详细模板高度较大
                    break;
                case PrintTemplateManager.TYPE_INVOICE:
                    baseHeight = 260; // 发票模板高度最大
                    break;
            }

            return baseHeight + (itemCount * itemHeight);
        }
    }

    // 使用简化版
    // 使用优化版的打印方法
    private void executePrint() {
        if (currentPrintData != null && !currentPrintType.isEmpty()) {
            try {
                PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);

                // 使用优化的打印适配器
                OptimizedPrintAdapter adapter = new OptimizedPrintAdapter(currentPrintData, currentPrintType);

                String jobName = "订单打印_" + currentPrintData.getOrderNumber();
                printManager.print(jobName, adapter, null);

                Toast.makeText(this, "开始打印", Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "打印启动失败: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "请先选择订单和打印类型", Toast.LENGTH_SHORT).show();
        }
    }
}