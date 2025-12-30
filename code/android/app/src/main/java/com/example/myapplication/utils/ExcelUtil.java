package com.example.myapplication.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.example.myapplication.MyApplication;
import com.example.myapplication.fenquan.entity.Workbench;
import com.example.myapplication.jiaowu.entity.AccountManagement;
import com.example.myapplication.jiaowu.entity.JiaoShiKeShi;
import com.example.myapplication.jiaowu.entity.KeShiDetail;
import com.example.myapplication.jiaowu.entity.Payment;
import com.example.myapplication.jiaowu.entity.ShouZhiMingXi;
import com.example.myapplication.jiaowu.entity.Student;
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
                    sheet.addCell(new Label(10, i + 1, list.get(i).getcangku(), arial12format));
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
                    sheet.addCell(new Label(11, i + 1, list.get(i).getcangku(), arial12format));
                    sheet.addCell(new Label(12, i + 1, list.get(i).getzzl(), arial12format));
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
    public static void gongzuotaiToExcel(List<Workbench> list, String fileName, Context c) {
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
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date1 = list.get(i).get日期();
                    Date date2 = list.get(i).getA最后修改日期();
                    String str_date1 = sdf.format(date1);
                    String str_date2 = sdf.format(date2);
                    sheet.addCell(new Label(0, i + 1, list.get(i).get人员(), arial12format));
                    sheet.addCell(new Label(1, i + 1, str_date1, arial12format));
                    sheet.addCell(new Label(2, i + 1, str_date2, arial12format));
                    sheet.addCell(new Label(3, i + 1, list.get(i).getA(), arial12format));
                    sheet.addCell(new Label(4, i + 1, list.get(i).getB(), arial12format));
                    sheet.addCell(new Label(5, i + 1, list.get(i).getC(), arial12format));
                    sheet.addCell(new Label(6, i + 1, list.get(i).getD(), arial12format));
                    sheet.addCell(new Label(7, i + 1, list.get(i).getE(), arial12format));
                    sheet.addCell(new Label(8, i + 1, list.get(i).getF(), arial12format));
                    sheet.addCell(new Label(9, i + 1, list.get(i).getG(), arial12format));
                    sheet.addCell(new Label(10, i + 1, list.get(i).getH(), arial12format));
                    sheet.addCell(new Label(11, i + 1, list.get(i).getI(), arial12format));
                    sheet.addCell(new Label(12, i + 1, list.get(i).getJ(), arial12format));
                    sheet.addCell(new Label(13, i + 1, list.get(i).getK(), arial12format));
                    sheet.addCell(new Label(14, i + 1, list.get(i).getL(), arial12format));
                    sheet.addCell(new Label(15, i + 1, list.get(i).getM(), arial12format));
                    sheet.addCell(new Label(16, i + 1, list.get(i).getN(), arial12format));
                    sheet.addCell(new Label(17, i + 1, list.get(i).getO(), arial12format));
                    sheet.addCell(new Label(18, i + 1, list.get(i).getP(), arial12format));
                    sheet.addCell(new Label(19, i + 1, list.get(i).getQ(), arial12format));
                    sheet.addCell(new Label(20, i + 1, list.get(i).getR(), arial12format));
                    sheet.addCell(new Label(21, i + 1, list.get(i).getS(), arial12format));
                    sheet.addCell(new Label(22, i + 1, list.get(i).getT(), arial12format));
                    sheet.addCell(new Label(23, i + 1, list.get(i).getU(), arial12format));
                    sheet.addCell(new Label(24, i + 1, list.get(i).getV(), arial12format));
                    sheet.addCell(new Label(25, i + 1, list.get(i).getW(), arial12format));
                    sheet.addCell(new Label(26, i + 1, list.get(i).getX(), arial12format));
                    sheet.addCell(new Label(27, i + 1, list.get(i).getY(), arial12format));
                    sheet.addCell(new Label(28, i + 1, list.get(i).getZ(), arial12format));
                    sheet.addCell(new Label(29, i + 1, list.get(i).getAa(), arial12format));
                    sheet.addCell(new Label(30, i + 1, list.get(i).getAb(), arial12format));
                    sheet.addCell(new Label(31, i + 1, list.get(i).getAc(), arial12format));
                    sheet.addCell(new Label(32, i + 1, list.get(i).getAd(), arial12format));
                    sheet.addCell(new Label(33, i + 1, list.get(i).getAe(), arial12format));
                    sheet.addCell(new Label(34, i + 1, list.get(i).getAf(), arial12format));
                    sheet.addCell(new Label(35, i + 1, list.get(i).getAg(), arial12format));
                    sheet.addCell(new Label(36, i + 1, list.get(i).getAh(), arial12format));
                    sheet.addCell(new Label(37, i + 1, list.get(i).getAi(), arial12format));
                    sheet.addCell(new Label(38, i + 1, list.get(i).getAj(), arial12format));
                    sheet.addCell(new Label(39, i + 1, list.get(i).getAk(), arial12format));
                    sheet.addCell(new Label(40, i + 1, list.get(i).getAl(), arial12format));
                    sheet.addCell(new Label(41, i + 1, list.get(i).getAm(), arial12format));
                    sheet.addCell(new Label(42, i + 1, list.get(i).getAn(), arial12format));
                    sheet.addCell(new Label(43, i + 1, list.get(i).getAo(), arial12format));
                    sheet.addCell(new Label(44, i + 1, list.get(i).getAp(), arial12format));
                    sheet.addCell(new Label(45, i + 1, list.get(i).getAq(), arial12format));
                    sheet.addCell(new Label(46, i + 1, list.get(i).getAr(), arial12format));
                    sheet.addCell(new Label(47, i + 1, list.get(i).getAss(), arial12format));
                    sheet.addCell(new Label(48, i + 1, list.get(i).getAt(), arial12format));
                    sheet.addCell(new Label(49, i + 1, list.get(i).getAu(), arial12format));
                    sheet.addCell(new Label(50, i + 1, list.get(i).getAv(), arial12format));
                    sheet.addCell(new Label(51, i + 1, list.get(i).getAw(), arial12format));
                    sheet.addCell(new Label(52, i + 1, list.get(i).getAx(), arial12format));
                    sheet.addCell(new Label(53, i + 1, list.get(i).getAy(), arial12format));
                    sheet.addCell(new Label(54, i + 1, list.get(i).getAz(), arial12format));
                    sheet.addCell(new Label(55, i + 1, list.get(i).getBa(), arial12format));
                    sheet.addCell(new Label(56, i + 1, list.get(i).getBb(), arial12format));
                    sheet.addCell(new Label(57, i + 1, list.get(i).getBc(), arial12format));
                    sheet.addCell(new Label(58, i + 1, list.get(i).getBd(), arial12format));
                    sheet.addCell(new Label(59, i + 1, list.get(i).getBe(), arial12format));
                    sheet.addCell(new Label(60, i + 1, list.get(i).getBf(), arial12format));
                    sheet.addCell(new Label(61, i + 1, list.get(i).getBg(), arial12format));
                    sheet.addCell(new Label(62, i + 1, list.get(i).getBh(), arial12format));
                    sheet.addCell(new Label(63, i + 1, list.get(i).getBi(), arial12format));
                    sheet.addCell(new Label(64, i + 1, list.get(i).getBj(), arial12format));
                    sheet.addCell(new Label(65, i + 1, list.get(i).getBk(), arial12format));
                    sheet.addCell(new Label(66, i + 1, list.get(i).getBl(), arial12format));
                    sheet.addCell(new Label(67, i + 1, list.get(i).getBm(), arial12format));
                    sheet.addCell(new Label(68, i + 1, list.get(i).getBn(), arial12format));
                    sheet.addCell(new Label(69, i + 1, list.get(i).getBo(), arial12format));
                    sheet.addCell(new Label(70, i + 1, list.get(i).getBp(), arial12format));
                    sheet.addCell(new Label(71, i + 1, list.get(i).getBq(), arial12format));
                    sheet.addCell(new Label(72, i + 1, list.get(i).getBr(), arial12format));
                    sheet.addCell(new Label(73, i + 1, list.get(i).getBs(), arial12format));
                    sheet.addCell(new Label(74, i + 1, list.get(i).getBt(), arial12format));
                    sheet.addCell(new Label(75, i + 1, list.get(i).getBu(), arial12format));
                    sheet.addCell(new Label(76, i + 1, list.get(i).getBv(), arial12format));
                    sheet.addCell(new Label(77, i + 1, list.get(i).getBw(), arial12format));
                    sheet.addCell(new Label(78, i + 1, list.get(i).getBx(), arial12format));
                    sheet.addCell(new Label(79, i + 1, list.get(i).getByy(), arial12format));
                    sheet.addCell(new Label(80, i + 1, list.get(i).getBz(), arial12format));
                    sheet.addCell(new Label(81, i + 1, list.get(i).getCa(), arial12format));
                    sheet.addCell(new Label(82, i + 1, list.get(i).getCb(), arial12format));
                    sheet.addCell(new Label(83, i + 1, list.get(i).getCc(), arial12format));
                    sheet.addCell(new Label(84, i + 1, list.get(i).getCd(), arial12format));
                    sheet.addCell(new Label(85, i + 1, list.get(i).getCe(), arial12format));
                    sheet.addCell(new Label(86, i + 1, list.get(i).getCf(), arial12format));
                    sheet.addCell(new Label(87, i + 1, list.get(i).getCg(), arial12format));
                    sheet.addCell(new Label(88, i + 1, list.get(i).getCh(), arial12format));
                    sheet.addCell(new Label(89, i + 1, list.get(i).getCi(), arial12format));
                    sheet.addCell(new Label(90, i + 1, list.get(i).getCj(), arial12format));
                    sheet.addCell(new Label(91, i + 1, list.get(i).getCk(), arial12format));
                    sheet.addCell(new Label(92, i + 1, list.get(i).getCl(), arial12format));
                    sheet.addCell(new Label(93, i + 1, list.get(i).getCm(), arial12format));
                    sheet.addCell(new Label(94, i + 1, list.get(i).getCn(), arial12format));
                    sheet.addCell(new Label(95, i + 1, list.get(i).getCo(), arial12format));
                    sheet.addCell(new Label(96, i + 1, list.get(i).getCp(), arial12format));
                    sheet.addCell(new Label(97, i + 1, list.get(i).getCq(), arial12format));
                    sheet.addCell(new Label(98, i + 1, list.get(i).getCr(), arial12format));
                    sheet.addCell(new Label(99, i + 1, list.get(i).getCs(), arial12format));
                    sheet.addCell(new Label(100, i + 1, list.get(i).getCt(), arial12format));
                    sheet.addCell(new Label(101, i + 1, list.get(i).getCu(), arial12format));
                    sheet.addCell(new Label(102, i + 1, list.get(i).getCv(), arial12format));
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
    public static void jiaowu_zhanghaoToExcel(List<AccountManagement> list, String fileName, Context c) {
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
                    sheet.addCell(new Label(0, i + 1, list.get(i).getUsername(), arial12format));
                    sheet.addCell(new Label(1, i + 1, list.get(i).getPassword(), arial12format));
                    sheet.addCell(new Label(2, i + 1, list.get(i).getRealname(), arial12format));
                    sheet.addCell(new Label(3, i + 1, String.valueOf(list.get(i).getUsetype()), arial12format));
                    sheet.addCell(new Label(4, i + 1, String.valueOf(list.get(i).getAge()), arial12format));
                    sheet.addCell(new Label(5, i + 1, list.get(i).getPhone(), arial12format));
                    sheet.addCell(new Label(6, i + 1, list.get(i).getHome(), arial12format));
                    sheet.addCell(new Label(7, i + 1, list.get(i).getPhoto(), arial12format));
                    sheet.addCell(new Label(8, i + 1, list.get(i).getEducation(), arial12format));
                    sheet.addCell(new Label(9, i + 1, list.get(i).getState(), arial12format));
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
    public static void jiaowu_xueshengxinxiToExcel(List<Student> list, String fileName, Context c) {
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
                    sheet.addCell(new Label(0, i + 1, String.valueOf(list.get(i).getId()), arial12format));
                    sheet.addCell(new Label(1, i + 1, list.get(i).getRealname(), arial12format));
                    sheet.addCell(new Label(2, i + 1, list.get(i).getSex(), arial12format));
                    sheet.addCell(new Label(3, i + 1, list.get(i).getRgdate(), arial12format));
                    sheet.addCell(new Label(4, i + 1, list.get(i).getCourse(), arial12format));
                    sheet.addCell(new Label(5, i + 1, list.get(i).getTeacher(), arial12format));
                    sheet.addCell(new Label(6, i + 1, list.get(i).getClassnum(), arial12format));
                    sheet.addCell(new Label(7, i + 1, list.get(i).getPhone(), arial12format));
                    sheet.addCell(new Label(8, i + 1, String.valueOf(list.get(i).getFee()), arial12format));
                    sheet.addCell(new Label(9, i + 1, String.valueOf(list.get(i).getMall()), arial12format));
                    sheet.addCell(new Label(10, i + 1, String.valueOf(list.get(i).getNocost()), arial12format));
                    sheet.addCell(new Label(11, i + 1, String.valueOf(list.get(i).getNall()), arial12format));
                    sheet.addCell(new Label(12, i + 1, String.valueOf(list.get(i).getNohour()), arial12format));
                    sheet.addCell(new Label(13, i + 1, String.valueOf(list.get(i).getAllhour()), arial12format));
                    sheet.addCell(new Label(14, i + 1, list.get(i).getType(), arial12format));
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
    public static void jiaowu_jiaofeijiluToExcel(List<Payment> list, String fileName, Context c) {
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
                    sheet.addCell(new Label(0, i + 1, list.get(i).getKsdate(), arial12format));
                    sheet.addCell(new Label(1, i + 1, list.get(i).getRealname(), arial12format));
                    sheet.addCell(new Label(2, i + 1, String.valueOf(list.get(i).getPaid()), arial12format));
                    sheet.addCell(new Label(3, i + 1, String.valueOf(list.get(i).getMoney()), arial12format));
                    sheet.addCell(new Label(4, i + 1, list.get(i).getPaiment(), arial12format));
                    sheet.addCell(new Label(5, i + 1, list.get(i).getKeeper(), arial12format));
                    sheet.addCell(new Label(6, i + 1, list.get(i).getRemark(), arial12format));
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

    public static void dbtjToExcel(List<YhJinXiaoCunMingXi> list, String fileName, Context c) {
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
                    sheet.addCell(new Label(9, i + 1, list.get(i).getcangku(), arial12format));
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


    public static void qimotongjiToExcel(List<YhJinXiaoCun> list, String fileName, Context c) {
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
                    sheet.addCell(new Label(0, i + 1, list.get(i).getcangku(), arial12format));
                    sheet.addCell(new Label(1, i + 1, list.get(i).getmonth(), arial12format));
                    sheet.addCell(new Label(2, i + 1, list.get(i).getMx_ruku_cpsl(), arial12format));
                    sheet.addCell(new Label(3, i + 1, list.get(i).getMx_ruku_price(), arial12format));
                    sheet.addCell(new Label(4, i + 1, list.get(i).getMx_chuku_cpsl(), arial12format));
                    sheet.addCell(new Label(5, i + 1, list.get(i).getMx_chuku_price(), arial12format));
                    sheet.addCell(new Label(6, i + 1, list.get(i).getJc_sl(), arial12format));
                    sheet.addCell(new Label(7, i + 1, list.get(i).getJc_price(), arial12format));

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


    public static void jiyatongjiToExcel(List<YhJinXiaoCun> list, String fileName, Context c) {
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
                    sheet.addCell(new Label(0, i + 1, list.get(i).getName(), arial12format));
                    sheet.addCell(new Label(1, i + 1, list.get(i).getSp_dm(), arial12format));
                    sheet.addCell(new Label(2, i + 1, list.get(i).getLei_bie(), arial12format));
                    sheet.addCell(new Label(3, i + 1, list.get(i).getcangku(), arial12format));
                    sheet.addCell(new Label(4, i + 1, list.get(i).getMx_chuku_cpsl(), arial12format));
                    sheet.addCell(new Label(5, i + 1, list.get(i).getJc_sl(), arial12format));
                    sheet.addCell(new Label(6, i + 1, list.get(i).getJc_price(), arial12format));


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
    public static void jiaowu_keshitongjiToExcel(List<KeShiDetail> list, String fileName, Context c) {
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
                    sheet.addCell(new Label(0, i + 1, list.get(i).getRiqi(), arial12format));
                    sheet.addCell(new Label(1, i + 1, list.get(i).getStudent_name(), arial12format));
                    sheet.addCell(new Label(2, i + 1, list.get(i).getCourse(), arial12format));
                    sheet.addCell(new Label(3, i + 1, String.valueOf(list.get(i).getKeshi()), arial12format));
                    sheet.addCell(new Label(4, i + 1, list.get(i).getTeacher_name(), arial12format));
                    sheet.addCell(new Label(5, i + 1, String.valueOf(list.get(i).getJine()), arial12format));
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
    public static void jiaowu_shouzhimingxiToExcel(List<ShouZhiMingXi> list, String fileName, Context c) {
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
                    sheet.addCell(new Label(0, i + 1, list.get(i).getRgdate(), arial12format));
                    sheet.addCell(new Label(1, i + 1, String.valueOf(list.get(i).getMoney()), arial12format));
                    sheet.addCell(new Label(2, i + 1, list.get(i).getMsort(), arial12format));
                    sheet.addCell(new Label(3, i + 1, list.get(i).getMremark(), arial12format));
                    sheet.addCell(new Label(4, i + 1, String.valueOf(list.get(i).getPaid()), arial12format));
                    sheet.addCell(new Label(5, i + 1, list.get(i).getPsort(), arial12format));
                    sheet.addCell(new Label(6, i + 1, list.get(i).getPremark(), arial12format));
                    sheet.addCell(new Label(7, i + 1, list.get(i).getHandle(), arial12format));
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
    public static void jiaowu_qianfeixueyuanToExcel(List<Student> list, String fileName, Context c) {
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
                    sheet.addCell(new Label(0, i + 1, list.get(i).getRealname(), arial12format));
                    sheet.addCell(new Label(1, i + 1, String.valueOf(list.get(i).getNocost()), arial12format));
                    sheet.addCell(new Label(2, i + 1, list.get(i).getRgdate(), arial12format));
                    sheet.addCell(new Label(3, i + 1, list.get(i).getCourse(), arial12format));
                    sheet.addCell(new Label(4, i + 1, list.get(i).getTeacher(), arial12format));
                    sheet.addCell(new Label(5, i + 1, list.get(i).getClassnum(), arial12format));
                    sheet.addCell(new Label(6, i + 1, list.get(i).getPhone(), arial12format));
                    sheet.addCell(new Label(7, i + 1, String.valueOf(list.get(i).getNohour()), arial12format));
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
    public static void jiaowu_jiaoshikeshitongji(List<JiaoShiKeShi> list, String fileName, Context c) {
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
                    sheet.addCell(new Label(0, i + 1, list.get(i).getTeacher_name(), arial12format));
                    sheet.addCell(new Label(1, i + 1, list.get(i).getCourse(), arial12format));
                    sheet.addCell(new Label(2, i + 1, list.get(i).getStudent_name(), arial12format));
                    sheet.addCell(new Label(3, i + 1, list.get(i).getKeshi(), arial12format));
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