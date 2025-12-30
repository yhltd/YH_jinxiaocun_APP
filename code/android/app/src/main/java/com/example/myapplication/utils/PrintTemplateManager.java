package com.example.myapplication.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;

import  com.example.myapplication.jxc.entity.PrintData;
import  com.example.myapplication.jxc.entity.OrderItem;
import java.text.DecimalFormat;
import java.util.List;

public class PrintTemplateManager {
    private Context context;
    private DecimalFormat decimalFormat;

    // 打印类型常量
    public static final String TYPE_SIMPLE = "simple";
    public static final String TYPE_DETAILED = "detailed";
    public static final String TYPE_INVOICE = "invoice";

    public PrintTemplateManager(Context context) {
        this.context = context;
        this.decimalFormat = new DecimalFormat("#0.00");
    }

    /**
     * 根据打印类型和数据生成预览Bitmap
     */
    // 在 PrintTemplateManager.java 中检查 generatePreviewBitmap 方法
    public Bitmap generatePreviewBitmap(String printType, PrintData printData, int width) {
        Log.d("PrintTemplate", "开始生成预览 Bitmap");
        Log.d("PrintTemplate", "printType: " + printType + ", width: " + width);

        if (printData == null) {
            Log.e("PrintTemplate", "printData 为 null!");
            return null;
        }

        try {
            // 计算模板高度
            int height = calculateTemplateHeight(printType, printData);
            Log.d("PrintTemplate", "计算的高度: " + height);

            // 创建Bitmap
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);

            // 绘制白色背景
            canvas.drawColor(Color.WHITE);
            Log.d("PrintTemplate", "Canvas 创建完成");

            // 根据类型绘制
            switch (printType) {
                case "TYPE_SIMPLE":
                    drawSimpleReceipt(canvas, printData, width);
                    break;
                case "TYPE_DETAILED":
                    drawDetailedReceipt(canvas, printData, width);
                    break;
                case "TYPE_INVOICE":
                    drawInvoice(canvas, printData, width);
                    break;
                default:
                    drawSimpleReceipt(canvas, printData, width);
            }

            Log.d("PrintTemplate", "绘制完成");
            return bitmap;

        } catch (Exception e) {
            Log.e("PrintTemplate", "生成预览异常: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 计算模板高度
     */
    /**
     * 计算模板高度 - 根据商品数量动态计算
     */
    private int calculateTemplateHeight(String printType, PrintData printData) {
        int baseHeight = 200; // 基础高度（标题、订单信息、底部汇总等）
        int itemHeight = 0;
        int extraHeight = 0;

        if (printData != null && printData.getItems() != null) {
            int itemCount = printData.getItems().size();

            switch (printType) {
                case TYPE_SIMPLE:
                    // 简单模板：每行商品占20像素，表头20像素，间距30像素
                    itemHeight = itemCount * 20 + 20 + 30;
                    break;
                case TYPE_DETAILED:
                    // 详细模板：每行商品占18像素，表头20像素，间距40像素
                    itemHeight = itemCount * 18 + 20 + 40;
                    break;
                case TYPE_INVOICE:
                    // 发票模板：每行商品占15像素，表头20像素，间距50像素
                    itemHeight = itemCount * 15 + 20 + 50;
                    break;
                default:
                    itemHeight = itemCount * 20 + 20 + 30;
            }

            // 如果商品太多，确保最小高度
            if (itemHeight < 100) {
                itemHeight = 100;
            }
        } else {
            // 没有商品时的最小高度
            itemHeight = 100;
        }

        return baseHeight + itemHeight + extraHeight;
    }

    /**
     * 绘制背景
     */
    private void drawBackground(Canvas canvas, int width, int height) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        canvas.drawRect(0, 0, width, height, paint);

        // 绘制边框
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        canvas.drawRect(1, 1, width - 1, height - 1, paint);
    }

    /**
     * 绘制简单小票模板
     */
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

    /**
     * 根据列宽截断文本
     */
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
     * 绘制详细收据模板
     */
    private void drawDetailedReceipt(Canvas canvas, PrintData data, int width) {
        if (data == null) return;

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);

        float y = 30;
        int margin = 25;

        // 标题
        paint.setTextSize(20);
        paint.setFakeBoldText(true);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(data.getStoreName() != null ? data.getStoreName() : "店铺名称", width / 2, y, paint);
        y += 35;

        // 详细信息
        paint.setTextSize(11);
        paint.setFakeBoldText(false);
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("订单号: " + data.getOrderNumber(), margin, y, paint);
        y += 18;
        canvas.drawText("时间: " + (data.getOrderTime() != null ? data.getOrderTime() : ""), margin, y, paint);
        y += 18;

        if (data.getCustomerName() != null) {
            canvas.drawText("客户: " + data.getCustomerName(), margin, y, paint);
            y += 18;
        }
        y += 10;

        // 表头
        paint.setFakeBoldText(true);
        canvas.drawText("商品名称", margin, y, paint);
        canvas.drawText("数量", width / 2 - 30, y, paint);
        canvas.drawText("金额", width - margin - 80, y, paint);
        y += 20;

        // 商品列表
        paint.setFakeBoldText(false);
        if (data.getItems() != null) {
            for (OrderItem item : data.getItems()) {
                canvas.drawText(item.getProductName(), margin, y, paint);
                canvas.drawText("×" + item.getQuantity(), width / 2 - 30, y, paint);
                canvas.drawText("¥" + decimalFormat.format(item.getSubtotal()), width - margin - 80, y, paint);
                y += 18;
            }
        }

        y += 15;
        paint.setStrokeWidth(1);
        canvas.drawLine(margin, y, width - margin, y, paint);
        y += 20;

        // 金额汇总
        paint.setFakeBoldText(true);
        canvas.drawText("合计: ¥" + decimalFormat.format(data.getTotalAmount()), width - margin - 150, y, paint);
        y += 20;
        canvas.drawText("实收: ¥" + decimalFormat.format(data.getPaidAmount()), width - margin - 150, y, paint);
        y += 20;

        double change = data.getPaidAmount() - data.getTotalAmount();
        canvas.drawText("找零: ¥" + decimalFormat.format(change), width - margin - 150, y, paint);
    }

    /**
     * 绘制发票模板
     */
    private void drawInvoice(Canvas canvas, PrintData data, int width) {
        if (data == null) return;

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);

        float y = 40;
        int margin = 30;

        // 发票标题
        paint.setTextSize(22);
        paint.setFakeBoldText(true);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("增值税普通发票", width / 2, y, paint);
        y += 40;

        // 发票信息
        paint.setTextSize(11);
        paint.setFakeBoldText(false);
        paint.setTextAlign(Paint.Align.LEFT);

        if (data.getCustomerName() != null) {
            canvas.drawText("购买方: " + data.getCustomerName(), margin, y, paint);
            y += 18;
        }

        if (data.getCustomerPhone() != null) {
            canvas.drawText("电话: " + data.getCustomerPhone(), margin, y, paint);
            y += 18;
        }

        if (data.getCustomerAddress() != null) {
            canvas.drawText("地址: " + data.getCustomerAddress(), margin, y, paint);
            y += 18;
        }
        y += 15;

        // 订单信息
        canvas.drawText("订单号: " + data.getOrderNumber(), margin, y, paint);
        canvas.drawText("开票日期: " + (data.getOrderTime() != null ? data.getOrderTime() : ""), width / 2, y, paint);
        y += 25;

        // 商品表格
        drawInvoiceTable(canvas, data, width, y);
    }

    /**
     * 绘制发票表格
     */
    private void drawInvoiceTable(Canvas canvas, PrintData data, int width, float startY) {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(10);

        float y = startY;
        int margin = 20;
        int colWidth = (width - 2 * margin) / 4;

        // 表头
        paint.setFakeBoldText(true);
        canvas.drawText("商品名称", margin, y, paint);
        canvas.drawText("规格", margin + colWidth, y, paint);
        canvas.drawText("数量", margin + colWidth * 2, y, paint);
        canvas.drawText("金额", margin + colWidth * 3, y, paint);
        y += 15;

        // 表格线
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawLine(margin, y, width - margin, y, paint);
        y += 5;

        // 商品行
        paint.setStyle(Paint.Style.FILL);
        paint.setFakeBoldText(false);
        if (data.getItems() != null) {
            for (OrderItem item : data.getItems()) {
                canvas.drawText(item.getProductName(), margin, y, paint);
                canvas.drawText(item.getSpecification() != null ? item.getSpecification() : "", margin + colWidth, y, paint);
                canvas.drawText(String.valueOf(item.getQuantity()), margin + colWidth * 2, y, paint);
                canvas.drawText("¥" + decimalFormat.format(item.getSubtotal()), margin + colWidth * 3, y, paint);
                y += 15;
            }
        }

        // 底部汇总
        y += 10;
        paint.setFakeBoldText(true);
        canvas.drawText("金额合计: ¥" + decimalFormat.format(data.getTotalAmount()), width - margin - 150, y, paint);
    }

    public void drawToCanvas(Canvas canvas, String printType, PrintData printData, int width) {
        // 绘制背景
        int height = calculateTemplateHeight(printType, printData);
        drawBackground(canvas, width, height);

        // 根据类型绘制内容
        switch (printType) {
            case TYPE_SIMPLE:
                drawSimpleReceipt(canvas, printData, width);
                break;
            case TYPE_DETAILED:
                drawDetailedReceipt(canvas, printData, width);
                break;
            case TYPE_INVOICE:
                drawInvoice(canvas, printData, width);
                break;
            default:
                drawSimpleReceipt(canvas, printData, width);
        }
    }


}