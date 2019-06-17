package com.my.aotutest.util;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;

/**
 * @author lijx
 * @date 2018/11/6 - 18:09
 */
public class ExcellUtil {
    /**
     * 获取sheet内的一个单元格的内容
     * @param sheet
     * @param row
     * @param cellIndex
     * @return
     */
    public static String readCell(XSSFSheet sheet, Integer row, Integer cellIndex)  {
        XSSFCell cell = sheet.getRow(row).getCell(cellIndex);
        return cell.toString();
    }


    /**
     * 还未完成
     * @param filePath
     */
    public static void writeExcel(String filePath) {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("new sheet");
        Row row = sheet.createRow(2);
        row.createCell(0).setCellValue(1.1);
        row.createCell(1).setCellValue(new Date());
        row.createCell(2).setCellValue(Calendar.getInstance());
        row.createCell(3).setCellValue("a string");
        row.createCell(4).setCellValue(true);
        row.createCell(5).setCellType(CellType.ERROR);
        try {
            OutputStream fileOut = new FileOutputStream(filePath);
            wb.write(fileOut);
        } catch (FileNotFoundException e) {
            System.err.println("文件路径无效");
        } catch (IOException e1) {
            System.err.println("写出Excel文件失败");
            e1.printStackTrace();
        }
    }

}
