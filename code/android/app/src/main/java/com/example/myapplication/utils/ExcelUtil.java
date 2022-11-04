package com.example.myapplication.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.example.myapplication.MyApplication;
import com.example.myapplication.jxc.entity.YhJinXiaoCun;
import com.example.myapplication.jxc.entity.YhJinXiaoCunMingXi;

import org.apache.poi.ss.formula.functions.T;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class ExcelUtil {
    private static WritableFont arial14font = null;
    private static WritableCellFormat arial14format = null;
    private static WritableFont arial10font = null;
    private static WritableCellFormat arial10format = null;
    private static WritableFont arial12font = null;
    private static WritableCellFormat arial12format = null;
    private final static String UTF8_ENCODING = "UTF-8";

    /**
     * 单元格的格式设置 字体大小 颜色 对齐方式、背景颜色等...
     */
    private static void format() {
        try {
            arial14font = new WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD);
            arial14font.setColour(jxl.format.Colour.LIGHT_BLUE);
            arial14format = new WritableCellFormat(arial14font);
            arial14format.setAlignment(jxl.format.Alignment.CENTRE);
            arial14format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
            arial14format.setBackground(jxl.format.Colour.VERY_LIGHT_YELLOW);

            arial10font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
            arial10format = new WritableCellFormat(arial10font);
            arial10format.setAlignment(jxl.format.Alignment.CENTRE);
            arial10format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
            arial10format.setBackground(Colour.GRAY_25);

            arial12font = new WritableFont(WritableFont.ARIAL, 12);
            arial12format = new WritableCellFormat(arial12font);
            //对齐格式
            arial12format.setAlignment(jxl.format.Alignment.CENTRE);
            //设置边框
            arial12format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);


        } catch (WriteException e) {
            e.printStackTrace();
        }
    }


    /**
     * 初始化Excel表格
     *
     * @param filePath  存放excel文件的路径（MPCMS/Export/demo.xls）
     * @param sheetName Excel表格的表名
     * @param colName   excel中包含的列名（可以有多个）
     */
    public static void initExcel(String filePath, String sheetName, String[] colName) {
        format();
        WritableWorkbook workbook = null;
        try {
            File file = new File(Environment.getExternalStorageDirectory().getCanonicalPath()+"/云合未来一体化系统/");
            if (!file.exists()) {
                file.mkdirs();
            }
            File saveFile = new File(file, filePath);
            if (!saveFile.exists()) {
                saveFile.createNewFile();
            }

            workbook = Workbook.createWorkbook(saveFile);
            //设置表格的名字
            WritableSheet sheet = workbook.createSheet(sheetName, 0);
            //创建标题栏
            sheet.addCell((WritableCell) new Label(0, 0, filePath, arial14format));
            for (int col = 0; col < colName.length; col++) {
                sheet.addCell(new Label(col, 0, colName[col], arial10format));
            }
            //设置行高
            sheet.setRowView(0, 340);
            workbook.write();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 将制定类型的List写入Excel中
     *
     * @param list  待写入的list,
     * @param fileName 文件名：用户名_模块_任务名_时间
     * @param c
     */
    @SuppressWarnings("unchecked")
    public static void mingxiToExcel(List<YhJinXiaoCunMingXi> list, String fileName, Context c) {
        if (list != null && list.size() > 0) {
            WritableWorkbook writebook = null;
            InputStream in = null;
            try {
                WorkbookSettings setEncode = new WorkbookSettings();
                setEncode.setEncoding(UTF8_ENCODING);
                File file = new File(Environment.getExternalStorageDirectory().getCanonicalPath()+"/云合未来一体化系统");
                makeDir(file);
                File saveFile = new File(file, fileName);
                if (!saveFile.exists()) {
                    saveFile.createNewFile();
                }

                in = new FileInputStream(saveFile);
                Workbook workbook = Workbook.getWorkbook(in);
                writebook = Workbook.createWorkbook(saveFile, workbook);
                WritableSheet sheet = writebook.getSheet(0);


                for(int i=0;i<list.size();i++){
                    sheet.addCell(new Label(0, i + 1, list.get(i).getOrderid(), arial12format));
                    sheet.addCell(new Label(1, i + 1, list.get(i).getSpDm(), arial12format));
                    sheet.addCell(new Label(2, i + 1, list.get(i).getCpname(), arial12format));
                    sheet.addCell(new Label(3, i + 1, list.get(i).getCplb(), arial12format));
                    sheet.addCell(new Label(4, i + 1, list.get(i).getCpsj(), arial12format));
                    sheet.addCell(new Label(5, i + 1, list.get(i).getCpsl(), arial12format));
                    sheet.addCell(new Label(6, i + 1, list.get(i).getMxtype(), arial12format));
                    sheet.addCell(new Label(7, i + 1, list.get(i).getShijian(), arial12format));
                    sheet.addCell(new Label(8, i + 1, list.get(i).getGsName(), arial12format));
                    sheet.addCell(new Label(9, i + 1, list.get(i).getShou_h(), arial12format));
                    //设置行高
                    sheet.setRowView(i + 1, 350);
                }
                writebook.write();
                Toast.makeText(c, "成功！请前往"+Environment.getExternalStorageDirectory().getCanonicalPath()+"/云合未来一体化系统", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (writebook != null) {
                    try {
                        writebook.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 将制定类型的List写入Excel中
     *
     * @param list  待写入的list,
     * @param fileName 文件名：用户名_模块_任务名_时间
     * @param c
     */
    @SuppressWarnings("unchecked")
    public static void jinXiaoCunToExcel(List<YhJinXiaoCun> list, String fileName, Context c) {
        if (list != null && list.size() > 0) {
            WritableWorkbook writebook = null;
            InputStream in = null;
            try {
                WorkbookSettings setEncode = new WorkbookSettings();
                setEncode.setEncoding(UTF8_ENCODING);
                File file = new File(Environment.getExternalStorageDirectory().getCanonicalPath()+"/云合未来一体化系统");
                makeDir(file);
                File saveFile = new File(file, fileName);
                if (!saveFile.exists()) {
                    saveFile.createNewFile();
                }

                in = new FileInputStream(saveFile);
                Workbook workbook = Workbook.getWorkbook(in);
                writebook = Workbook.createWorkbook(saveFile, workbook);
                WritableSheet sheet = writebook.getSheet(0);


                for(int i=0;i<list.size();i++){
                    sheet.addCell(new Label(0, i + 1, list.get(i).getSp_dm(), arial12format));
                    sheet.addCell(new Label(1, i + 1, list.get(i).getName(), arial12format));
                    sheet.addCell(new Label(2, i + 1, list.get(i).getLei_bie(), arial12format));
                    sheet.addCell(new Label(3, i + 1, list.get(i).getJq_cpsl(), arial12format));
                    sheet.addCell(new Label(4, i + 1, list.get(i).getJq_price(), arial12format));
                    sheet.addCell(new Label(5, i + 1, list.get(i).getMx_ruku_cpsl(), arial12format));
                    sheet.addCell(new Label(6, i + 1, list.get(i).getMx_ruku_price(), arial12format));
                    sheet.addCell(new Label(7, i + 1, list.get(i).getMx_chuku_cpsl(), arial12format));
                    sheet.addCell(new Label(8, i + 1, list.get(i).getMx_chuku_price(), arial12format));
                    sheet.addCell(new Label(9, i + 1, list.get(i).getJc_sl(), arial12format));
                    sheet.addCell(new Label(10, i + 1, list.get(i).getJc_price(), arial12format));
                    //设置行高
                    sheet.setRowView(i + 1, 350);
                }
                writebook.write();
                Toast.makeText(c, "成功！请前往"+Environment.getExternalStorageDirectory().getCanonicalPath()+"/云合未来一体化系统", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (writebook != null) {
                    try {
                        writebook.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void makeDir(File dir) {
        if (!dir.getParentFile().exists()) {
            makeDir(dir.getParentFile());
        }
        dir.mkdir();
    }


}