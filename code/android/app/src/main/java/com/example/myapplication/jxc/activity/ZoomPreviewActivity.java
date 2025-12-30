package com.example.myapplication.jxc.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.jxc.entity.OrderItem;
import com.example.myapplication.jxc.entity.PrintData;
import com.example.myapplication.utils.PrintTemplateManager;

import java.text.DecimalFormat;

public class ZoomPreviewActivity extends AppCompatActivity {

    private ImageView previewImage;
    private Button btnPrint, btnClose;
    private Bitmap previewBitmap;

    private PrintData printData;
    private String printType;

    // 缩放相关变量
    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;
    private PointF start = new PointF();
    private PointF mid = new PointF();
    private float oldDist = 1f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_preview);

        initData();
        initViews();
        setupTouchListener();
        generatePreview();
    }

    private void initData() {
        try {
            Intent intent = getIntent();
            printType = intent.getStringExtra("print_type");
            printData = (PrintData) intent.getSerializableExtra("print_data");

            // 添加调试日志
            Log.d("ZoomPreview", "printType: " + printType);
            Log.d("ZoomPreview", "printData: " + printData);

            if (printData != null) {
                Log.d("ZoomPreview", "订单号: " + printData.getOrderNumber());
                Log.d("ZoomPreview", "店铺名称: " + printData.getStoreName());
                Log.d("ZoomPreview", "商品数量: " + (printData.getItems() != null ? printData.getItems().size() : 0));
            } else {
                Log.e("ZoomPreview", "printData 为 null!");
                Toast.makeText(this, "数据为空", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("ZoomPreview", "initData 错误: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void generatePreview() {
        Log.d("ZoomPreview", "开始生成预览");

        if (printData != null && printType != null) {
            try {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int previewWidth = displayMetrics.widthPixels - 100;

                PrintTemplateManager templateManager = new PrintTemplateManager(this);
                previewBitmap = templateManager.generatePreviewBitmap(printType, printData, previewWidth);

                if (previewBitmap != null) {
                    Log.d("ZoomPreview", "Bitmap 创建成功, 尺寸: " + previewBitmap.getWidth() + "x" + previewBitmap.getHeight());

                    // 先设置图片
                    previewImage.setImageBitmap(previewBitmap);

                    // 延迟设置初始缩放，确保布局已完成
                    new Handler().postDelayed(() -> {
                        setupInitialZoom();
                    }, 100);

                } else {
                    Toast.makeText(this, "生成预览失败", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.e("ZoomPreview", "generatePreview 错误: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void initViews() {
        previewImage = findViewById(R.id.preview_image);
        btnPrint = findViewById(R.id.btn_print);
        btnClose = findViewById(R.id.btn_close);

        // 保持 FIT_CENTER 确保初始显示，但为缩放做准备
        previewImage.setScaleType(ImageView.ScaleType.MATRIX);

        btnClose.setOnClickListener(v -> finish());

        btnPrint.setOnClickListener(v -> {
            if (printData != null) {
                executePrint();
            }
        });
    }

    private void setupTouchListener() {
        previewImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ImageView view = (ImageView) v;

                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        savedMatrix.set(matrix);
                        start.set(event.getX(), event.getY());
                        mode = DRAG;
                        break;

                    case MotionEvent.ACTION_POINTER_DOWN:
                        oldDist = spacing(event);
                        if (oldDist > 10f) {
                            savedMatrix.set(matrix);
                            midPoint(mid, event);
                            mode = ZOOM;
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:
                        mode = NONE;
                        // 限制缩放范围
                        limitScaleAndTranslation();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        if (mode == DRAG) {
                            matrix.set(savedMatrix);
                            matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
                        } else if (mode == ZOOM) {
                            float newDist = spacing(event);
                            if (newDist > 10f) {
                                matrix.set(savedMatrix);
                                float scale = newDist / oldDist;
                                matrix.postScale(scale, scale, mid.x, mid.y);
                            }
                        }
                        break;
                }

                view.setImageMatrix(matrix);
                return true;
            }
        });
    }

    /**
     * 限制缩放和移动范围，避免图片移出视图
     */
    private void limitScaleAndTranslation() {
        if (previewBitmap == null) return;

        float[] values = new float[9];
        matrix.getValues(values);
        float scale = values[Matrix.MSCALE_X];
        float transX = values[Matrix.MTRANS_X];
        float transY = values[Matrix.MTRANS_Y];

        // 限制最小和最大缩放
        float minScale = 0.5f;
        float maxScale = 3.0f;

        if (scale < minScale) {
            matrix.postScale(minScale / scale, minScale / scale, mid.x, mid.y);
        } else if (scale > maxScale) {
            matrix.postScale(maxScale / scale, maxScale / scale, mid.x, mid.y);
        }

        previewImage.setImageMatrix(matrix);
    }



    private void setupInitialZoom() {
        if (previewBitmap != null && previewImage != null) {
            try {
                int viewWidth = previewImage.getWidth();
                int viewHeight = previewImage.getHeight();
                int bitmapWidth = previewBitmap.getWidth();
                int bitmapHeight = previewBitmap.getHeight();

                Log.d("ZoomPreview", "View: " + viewWidth + "x" + viewHeight + ", Bitmap: " + bitmapWidth + "x" + bitmapHeight);

                if (viewWidth > 0 && viewHeight > 0) {
                    // 计算缩放比例以适应屏幕
                    float scaleX = (float) viewWidth / bitmapWidth;
                    float scaleY = (float) viewHeight / bitmapHeight;
                    float scale = Math.min(scaleX, scaleY) * 0.9f; // 稍微缩小一点，留出边距

                    matrix.setScale(scale, scale);

                    // 居中显示
                    float dx = (viewWidth - bitmapWidth * scale) / 2;
                    float dy = (viewHeight - bitmapHeight * scale) / 2;
                    matrix.postTranslate(dx, dy);

                    previewImage.setImageMatrix(matrix);
                    Log.d("ZoomPreview", "初始缩放设置完成，比例: " + scale);
                }
            } catch (Exception e) {
                Log.e("ZoomPreview", "setupInitialZoom 错误: " + e.getMessage());
                // 出错时回退到 FIT_CENTER
                previewImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
            }
        }
    }

    // 计算两点之间的距离
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    // 计算两点的中点
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    private void executePrint() {
        if (printData != null) {
            try {
                PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
                ZoomPrintAdapter adapter = new ZoomPrintAdapter(printData, printType);

                String jobName = "订单打印_" + printData.getOrderNumber();
                printManager.print(jobName, adapter, null);

                Toast.makeText(this, "开始打印", Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "打印启动失败: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (previewBitmap != null && !previewBitmap.isRecycled()) {
            previewBitmap.recycle();
        }
    }

    /**
     * 打印适配器（复制自PrintActivity，稍作修改）
     */
    private class ZoomPrintAdapter extends PrintDocumentAdapter {
        private PrintData printData;
        private String printType;
        private DecimalFormat decimalFormat;

        public ZoomPrintAdapter(PrintData data, String type) {
            this.printData = data;
            this.printType = type;
            this.decimalFormat = new DecimalFormat("0.00");
        }

        @Override
        public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes,
                             android.os.CancellationSignal cancellationSignal,
                             LayoutResultCallback callback, Bundle metadata) {
            if (cancellationSignal.isCanceled()) {
                callback.onLayoutCancelled();
                return;
            }

            android.print.PrintDocumentInfo info = new android.print.PrintDocumentInfo.Builder(
                    "order_print_" + printData.getOrderNumber())
                    .setContentType(android.print.PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                    .setPageCount(1)
                    .build();
            callback.onLayoutFinished(info, true);
        }

        @Override
        public void onWrite(PageRange[] pages, ParcelFileDescriptor destination,
                            android.os.CancellationSignal cancellationSignal, WriteResultCallback callback) {
            android.graphics.pdf.PdfDocument document = null;
            try {
                if (cancellationSignal.isCanceled()) {
                    callback.onWriteCancelled();
                    return;
                }

                document = new android.graphics.pdf.PdfDocument();

                int pageWidth = 400; // 小票标准宽度
                int pageHeight = calculatePdfHeight(printType, printData);

                android.graphics.pdf.PdfDocument.PageInfo pageInfo =
                        new android.graphics.pdf.PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();

                android.graphics.pdf.PdfDocument.Page page = document.startPage(pageInfo);
                android.graphics.Canvas canvas = page.getCanvas();

                canvas.drawColor(android.graphics.Color.WHITE);
                drawReceiptForPrint(canvas, printData, pageWidth);

                document.finishPage(page);

                if (cancellationSignal.isCanceled()) {
                    callback.onWriteCancelled();
                    return;
                }

                document.writeTo(new java.io.FileOutputStream(destination.getFileDescriptor()));
                callback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});

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



        private void drawReceiptForPrint(android.graphics.Canvas canvas, PrintData data, int width) {
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
        private int calculatePdfHeight(String printType, PrintData printData) {
            int baseHeight = 200;
            int itemHeight = 25;
            int itemCount = 0;

            if (printData != null && printData.getItems() != null) {
                itemCount = printData.getItems().size();
            }

            return baseHeight + (itemCount * itemHeight);
        }
    }
}