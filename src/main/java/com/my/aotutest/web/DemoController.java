package com.my.aotutest.web;

import com.my.aotutest.domain.User;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lijx
 * @date 2018/11/6 - 20:16
 */
@RestController
public class DemoController {
    private final Logger LOGGER = LoggerFactory.getLogger(DemoController.class);

    @RequestMapping("upload")
    public Object upload(@PathVariable("file") MultipartFile file) throws Exception {
        if (file == null || file.getInputStream() == null) {
            LOGGER.error("文件上传失败");
            return "文件上传失败";
        }
        if (file.getSize() > 5 * 1024 * 1024) {
            LOGGER.error("文件太大，上传失败");
            return "文件太大，上传失败";
        }
        if (file.getName().endsWith(".xls")) {
            LOGGER.info("开始解析XLS_DOC文件");
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook(file.getInputStream());
            List<User> getData = readOldExcel(hssfWorkbook);
            if (getData == null) {
                LOGGER.error("解析失败...");
                return "文件数据解析失败";
            }
            LOGGER.info("数据解析成功");
            file.getInputStream().close();
            return getData;
        } else if (file.getName().endsWith(".xlsx")) {
            LOGGER.info("开始解析XLSX_DOCX文件");
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(file.getInputStream());
            List<User> getData = readExcel(xssfWorkbook);
            if (getData == null) {
                LOGGER.error("解析失败...");
                return "文件数据解析失败";
            }
            LOGGER.info("数据解析成功");
            file.getInputStream().close();
            return getData;
        } else {
            LOGGER.error("上传文件类型不正确");
            return "文件类型不正确";
        }
    }

    @RequestMapping("download")
    public void download(HttpServletResponse response) throws Exception { //获取模板的输入流
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("templates" + File.separator + "xx.xlsx");
        if (inputStream == null) {
            LOGGER.error("模板文件的路径不正确");

        } //测试数据
        List<User> users = new ArrayList<User>();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setId(i + 1);
            user.setUsername("张三" + i + 1);
            user.setPassword("123" + i + 1);
            users.add(user);
        } //获取模板的工作薄
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheetAt = workbook.getSheetAt(0);
        XSSFRow row = sheetAt.getRow(0);
        short rowHeight = row.getHeight();
        XSSFRow row1 = null;
        XSSFCell cell = null;
        for (int j = 0; j < users.size(); j++) {
            User user = users.get(j);
            row1 = sheetAt.createRow(j + 1);
            row1.setHeight(rowHeight);
            cell = row1.createCell(0);
            cell.setCellValue(user.getId());

            cell = row1.createCell(1);
            cell.setCellValue(user.getUsername());

            cell = row1.createCell(2);
            cell.setCellValue(user.getPassword());
        }
        String filename = new String("文件".getBytes("UTF-8"), "ISO8859-1");
        LOGGER.info("文件名" + filename);
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xlsx");
        workbook.write(response.getOutputStream());
        LOGGER.info("文件下载成功！！");
        response.getOutputStream().flush();
        response.getOutputStream().close();
        workbook.close();


    } //处理2007之前的excel

    private List<User> readOldExcel(HSSFWorkbook hssfWorkbook) {
        List<User> users = new ArrayList<User>();
        HSSFSheet sheetAt = hssfWorkbook.getSheetAt(0);
        HSSFCell cell = null;
        HSSFRow row = null;
        for (int i = sheetAt.getFirstRowNum(); i < sheetAt.getPhysicalNumberOfRows(); i++) {
            row = sheetAt.getRow(i);
            if (row == null) {
                LOGGER.warn("获取到一个空行-------》》》》》》" + i + "行");
                continue;
            }
            Object[] objects = new Object[row.getLastCellNum()];
            for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
                cell = row.getCell(j);
                switch (cell.getCellTypeEnum()) {
                    case STRING:
                        objects[j] = cell.getStringCellValue();
                        System.out.println(cell.getStringCellValue());
                        break;
                    case _NONE:
                        objects[j] = "";
                        break;
                    case BOOLEAN:
                        objects[j] = cell.getBooleanCellValue();
                        System.out.println(cell.getBooleanCellValue());
                        break;
                    case NUMERIC: //处理double类型的  1.0===》1
                        DecimalFormat df = new DecimalFormat("0");
                        String s = df.format(cell.getNumericCellValue());
                        objects[j] = s;
                        System.out.println(s);
                        break;
                    default:
                        objects[j] = cell.toString();
                }
            } //处理数据
            if (objects != null) {
                User user = new User();
                user.setId(Integer.parseInt(objects[0].toString()));
                user.setUsername((String) objects[1]);
                user.setPassword(objects[2].toString());
                users.add(user);
            }
        }
        return users;
    } //处理2007之后的excel

    private List<User> readExcel(XSSFWorkbook xssfWorkbook) {
        List<User> users = new ArrayList<User>();
        //获得excel第一个工作薄
        XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
        //行
        XSSFRow row = null;
        //列
        XSSFCell cell = null;
        for (int i = sheet.getFirstRowNum(); i < sheet.getPhysicalNumberOfRows(); i++) { //获取每一行
            row = sheet.getRow(i);
            //判断是否出现空行
            if (row == null) {
                LOGGER.warn("获取到一个空行-------》》》》》》" + i + "行");
                continue;
            }
            Object[] objects = new Object[row.getLastCellNum()];
            for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
                cell = row.getCell(j);

                if (cell == null) {
                    LOGGER.warn("获取到一个空列------------》》》》》》" + j + "列");
                    continue;
                } //第一行数据
                switch (cell.getCellTypeEnum()) {
                    case STRING:
                        objects[j] = cell.getStringCellValue();
                        System.out.println(cell.getStringCellValue());
                        break;
                    case _NONE:
                        objects[j] = "";
                        break;
                    case BOOLEAN:
                        objects[j] = cell.getBooleanCellValue();
                        System.out.println(cell.getBooleanCellValue());
                        break;
                    case NUMERIC: //处理double类型的  1.0===》1
                        DecimalFormat df = new DecimalFormat("0");
                        String s = df.format(cell.getNumericCellValue());
                        objects[j] = s;
                        System.out.println(s);
                        break;
                    default:
                        objects[j] = cell.toString();
                }
            } //处理数据
            if (objects != null) {
                User user = new User();
                user.setId(Integer.parseInt(objects[0].toString()));
                user.setUsername((String) objects[1]);
                user.setPassword(objects[2].toString());
                users.add(user);
            }
        }
        return users;
    }

}

