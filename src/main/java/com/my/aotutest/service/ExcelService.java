package com.my.aotutest.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.my.aotutest.util.ExcellUtil;
import com.my.aotutest.util.JsonUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.filechooser.FileSystemView;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author lijx
 * @date 2018/11/8 - 15:51
 */
public class ExcelService {


    /**
     * 生成postman文件的主流程
     * @param dirPath
     * @throws Exception
     */
    public void generatePostmanFile(String dirPath)throws Exception{
        File dir = new File(dirPath);
        if (!dir.isDirectory() || !dir.exists()) {
            System.out.println("路径非法");
            return;
        }
        String[] fileList = dir.list();
        JSONArray itemEle = new JSONArray();
        //解析每个excel文件
        for (int i = 0; i < fileList.length; i++) {
            String fileName = fileList[i];
            if (fileName.endsWith(".xlsx")&&!fileName.startsWith("~$")){
                ArrayList<JSONObject> jsonObjects = parseExcelToJson(dirPath + "\\"+fileName);
                itemEle.add(jsonObjects.get(0));
                itemEle.add(jsonObjects.get(1));
            }
        }

        JSONObject infoEle = new JSONObject(true);
        infoEle.put("_postman_id", "030ecdd3-4f29-4597-8576-bd40f00fc5ba");
        infoEle.put("name", "业务支撑" + System.currentTimeMillis());
        infoEle.put("schema", "https://schema.getpostman.com/json/collection/v2.1.0/collection.json");

        //最终json
        JSONObject result = new JSONObject(2, true);
        result.put("info", infoEle);
        result.put("item", itemEle);
        //将文件写出
        writeFile(FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath() + "\\业务支撑" + System.currentTimeMillis() + ".json", result.toJSONString().getBytes());
    }

    /**
     * 解析excel中的每个用例
     * @param filePath
     * @return
     * @throws Exception
     */
    private ArrayList<JSONObject> parseExcelToJson(String filePath)throws Exception{
        //判断文件是否存在
        File excelFile = new File(filePath);
        if (!excelFile.exists()) {
            System.out.println("未读取到内容,请检查路径！");
            throw new Exception("文件不存在");
        }
        //读取xlsx文件
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(excelFile);
        XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
        //获取用例名
        String interfaceName = ExcellUtil.readCell(sheet, 0, 1);
        //读取正常用例内容
        XSSFRow rowNol = sheet.getRow(4);
        XSSFCell reqNol = rowNol.getCell(6);
        XSSFCell resNol = rowNol.getCell(9);
        //读取异常用例内容
        XSSFRow rowUnNol = sheet.getRow(5);
        XSSFCell reqUnNol = rowUnNol.getCell(6);
        XSSFCell resUnNol = rowUnNol.getCell(9);

        JSONObject infoEle = new JSONObject(true);
        infoEle.put("_postman_id", "030ecdd3-4f29-4597-8576-bd40f00fc5ba");
        infoEle.put("name", "业务支撑" + System.currentTimeMillis());
        infoEle.put("schema", "https://schema.getpostman.com/json/collection/v2.1.0/collection.json");

        //获得正常流程测试用例的json
        JSONObject nolTestCase = JsonUtil.generateCaseJson(interfaceName.toString() + "(正常)", reqNol.toString(), resNol.toString());
        //获得异常流程测试用例的json
        JSONObject unNolTestCase = JsonUtil.generateCaseJson(interfaceName.toString() + "(异常)", reqUnNol.toString(), resUnNol.toString());

        ArrayList<JSONObject> list=new ArrayList<>(2);
        list.add(nolTestCase);
        list.add(unNolTestCase);
        return list;
    }


    /**
     * 生成json文件
     * @param filePath
     * @param fileContent
     */
    private  void writeFile(String filePath, byte[] fileContent) {
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(filePath));
            bos.write(fileContent);
        } catch (IOException e) {
            System.err.println("写出文件失败");
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                System.err.println("关闭资源失败");
                e.printStackTrace();
            }
        }
    }


}
