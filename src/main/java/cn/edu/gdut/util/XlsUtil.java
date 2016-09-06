package cn.edu.gdut.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class XlsUtil {

    public static List<String[]> readExcel(InputStream ins) throws BiffException, IOException {
        // 创建一个list 用来存储读取的内容
        List<String[]> list = new ArrayList<>();
        Workbook rwb = null;
        Cell cell = null;
        // 创建输入流
        InputStream stream = ins;
        // 获取Excel文件对象
        rwb = Workbook.getWorkbook(stream);
        // 获取文件的指定工作表 默认的第一个
        jxl.Sheet sheet = rwb.getSheet(0);
        // 行数(表头的目录不需要，从1开始)
        for (int i = 0; i < sheet.getRows(); i++) {

            // 创建一个数组 用来存储每一列的值
            String[] strs = new String[sheet.getColumns()];

            // 列数
            for (int j = 0; j < sheet.getColumns(); j++) {
                // 获取第i行，第j列的值
                cell = sheet.getCell(j, i);
                strs[j] = cell.getContents();
            }
            // 把刚获取的列存入list
            list.add(strs);
        }
        //去掉头就可以吃了
        list.remove(0);
        return list;
    }

    public static List<String[]> readExcel(String realPath) throws BiffException, IOException {
        InputStream ins = new FileInputStream(realPath);
        return readExcel(ins);

    }

    public void test() {
        try {
            for (String[] strs : XlsUtil.readExcel("/home/rainj2013/桌面/test.xls"))
                System.out.println(strs[0] + "  " + strs[1]);
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
