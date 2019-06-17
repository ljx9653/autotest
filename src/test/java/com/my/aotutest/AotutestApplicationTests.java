package com.my.aotutest;

import com.my.aotutest.service.ExcelService;
import com.my.aotutest.util.JsonUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AotutestApplicationTests {

    @Test
    public void write() throws Exception {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("new sheet");
        Row row = sheet.createRow(2);
        row.createCell(0).setCellValue(1.1);
        row.createCell(1).setCellValue(new Date());
        row.createCell(2).setCellValue(Calendar.getInstance());
        row.createCell(3).setCellValue("a string");
        row.createCell(4).setCellValue(true);
        row.createCell(5).setCellType(CellType.ERROR);
        // Write the output to a file
        try (OutputStream fileOut = new FileOutputStream("D:\\接口.xlsx")) {
            wb.write(fileOut);
        }

    }


    @Test
    public void read() throws Exception {
        File excelFile = new File("D:\\接口调研测试用例 .xlsx");
        if (!excelFile.exists()) {
            System.out.println("未读取到内容,请检查路径！");
            return;
        }
        InputStream is = new FileInputStream(excelFile);
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
        Iterator<Sheet> iterator = xssfWorkbook.iterator();
        while (iterator.hasNext()) {
            Sheet next = iterator.next();
            Iterator<Row> iterator1 = next.iterator();
            while (iterator1.hasNext()) {
                Row next1 = iterator1.next();
                Iterator<Cell> iterator2 = next1.iterator();
                while (iterator2.hasNext()) {
                    Cell next2 = iterator2.next();
                    System.out.println(next2);
                }
            }
        }

    }

    @Test
    public void f11() throws Exception {
        String path = "C:\\Users\\ljx\\Desktop\\1";
        File dir = new File(path);
        if (!dir.isDirectory() || !dir.exists()) {
            System.out.println("路径非法");
        }
        String[] fileList = dir.list();
        for (int i = 0; i < fileList.length; i++) {
            String fileName = fileList[i];
            if (fileName.endsWith(".xlsx") && !fileName.startsWith("~$")) {
                File excel = new File(path + fileName);
            }
        }

    }


    @Test
    public void f1() throws Exception {
        new ExcelService().generatePostmanFile("C:\\Users\\ljx\\Desktop\\1");
    }


    @Test
    public void f() {
        String s = "{\n" +
                "  \"STATUS\": \"0000\",\n" +
                "  \"MSG\": \"SUCCESS\",\n" +
                "  \"TXID\": null,\n" +
                "  \"RSP\": {\n" +
                "    \"RSP_CODE\": \"0000\",\n" +
                "    \"RSP_DESC\": \"SUCCESS\",\n" +
                "    \"DATA\": {\n" +
                "      \"UnicomTaskBack\": {\n" +
                "        \"Header\": {\n" +
                "          \"tokenCode\": \"\",\n" +
                "          \"CHANNEL_ID\": null,\n" +
                "          \"OP_CITY\": null,\n" +
                "          \"TaskNo\": \"1234567890123\",\n" +
                "          \"OP_DISTRICT\": null,\n" +
                "          \"TestFlag\": null,\n" +
                "          \"BusinessType\": \"OGKDSL\",\n" +
                "          \"OPERATOR_ID\": null,\n" +
                "          \"ErrorDescript\": \"invalid args, bandwidth must in ' 100M100M@12 10M100M@47 10M10M@38 10M20M@59 10M50M@60 128k1.5M@0 128k2M@29 128k4M@26 128k512k@1 128k768k@3 12M12M@39 15M200M@61 16M16M@41 1M10M@33 1M10M@45 1M20M@46 1M4M@50 1M6M@51 1M8M@44 20M20M@40 20M300M@62 20M500M@19 256k1.5M@5 256k2M@7 256k3M@8 256k4M@9 256k768k@11 256k8M@6 2M10M@53 2M12M@57 2M20M@54 2M2M@35 30M1000M@31 30M30M@4 384k2M@13 384k3M@30 384k4M@14 40M40M@10 4M100M@56 4M10M@48 4M12M@58 4M20M@49 4M4M@32 4M50M@55 50M50M@42 512k1.5M@15 512k10M@34 512k2M@16 512k3M@17 512k4M@18 512k6M@20 512k8M@21 640k4M@22 640k6M@23 640k8M@24 64k256k@25 64k512k@27 768k8M@28 8M8M@43 NO@2 '\",\n" +
                "          \"Version\": \"\",\n" +
                "          \"RetCode\": \"ERROR\",\n" +
                "          \"AreaCode\": \"1\",\n" +
                "          \"BusNo\": \"17319068959\",\n" +
                "          \"CHANNEL_TYPE\": null,\n" +
                "          \"OP_PROVINCE\": null,\n" +
                "          \"TestFlagion\": \"0\"\n" +
                "        },\n" +
                "        \"ResponseBody\": [\n" +
                "          {\n" +
                "            \"code\": \"ERROR\",\n" +
                "            \"desc\": \"invalid args, bandwidth must in ' 100M100M@12 10M100M@47 10M10M@38 10M20M@59 10M50M@60 128k1.5M@0 128k2M@29 128k4M@26 128k512k@1 128k768k@3 12M12M@39 15M200M@61 16M16M@41 1M10M@33 1M10M@45 1M20M@46 1M4M@50 1M6M@51 1M8M@44 20M20M@40 20M300M@62 20M500M@19 256k1.5M@5 256k2M@7 256k3M@8 256k4M@9 256k768k@11 256k8M@6 2M10M@53 2M12M@57 2M20M@54 2M2M@35 30M1000M@31 30M30M@4 384k2M@13 384k3M@30 384k4M@14 40M40M@10 4M100M@56 4M10M@48 4M12M@58 4M20M@49 4M4M@32 4M50M@55 50M50M@42 512k1.5M@15 512k10M@34 512k2M@16 512k3M@17 512k4M@18 512k6M@20 512k8M@21 640k4M@22 640k6M@23 640k8M@24 64k256k@25 64k512k@27 768k8M@28 8M8M@43 NO@2 '\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    },\n" +
                "    \"ATTACH\": null\n" +
                "  }\n" +
                "}\n";
        List<String> list = JsonUtil.getJsonKey(s);
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
}
