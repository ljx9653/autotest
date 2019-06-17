package com.my.aotutest.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author lijx
 * @date 2018/11/8 - 17:10
 */
public class JsonUtil {

    /**
     * 获取json中的所有key
     * @param json
     * @return
     */
    public static List getJsonKey(String json){
        if(!isJSONValid(json)){
            System.err.println("非法的json字符串");
            return null;
        }
        ArrayList<String> list = new ArrayList();
        for (int i = 0; json.indexOf("\":", i) != -1; ) {
            int end = json.indexOf(":", i);
            int sta = json.lastIndexOf("\"", end - 2);
            list.add(json.substring(sta + 1, end - 1));
            i = end + 1;
        }
        return list;
    }

    /**
     * 判断字符串是否为json
     * @param json
     * @return
     */
    public static boolean isJSONValid(String json) {
        try {
            JSONObject.parseObject(json);
        } catch (JSONException ex) {
            try {
                JSONObject.parseArray(json);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

    /**
     * 生成一个postman用例的json
     * @param caseName
     * @param reuqestJson
     * @param responseJson
     * @return
     */
    public static JSONObject generateCaseJson(String caseName,String reuqestJson,String responseJson){
        String caseJsonTemplete = "{\n" +
                "\t\"name\": \"uniproxy\",\n" +
                "\t\"event\": [{\n" +
                "\t\t\"listen\": \"test\",\n" +
                "\t\t\"script\": {\n" +
                "\t\t\t\"id\": \"2fe51c21-6c51-400a-853a-fb2c80f2a2d1\",\n" +
                "\t\t\t\"exec\": [\n" +
                "\t\t\t\t\" pm.test(responseBody, function () {\",\n" +
                "\t\t\t\t\"    pm.expect(pm.response.text()).to.include(\\\"SUCCESS\\\");\",\n" +
                "\t\t\t\t\"});\"\n" +
                "\t\t\t],\n" +
                "\t\t\t\"type\": \"text/javascript\"\n" +
                "\t\t}\n" +
                "\t}],\n" +
                "\t\"request\": {\n" +
                "\t\t\"method\": \"POST\",\n" +
                "\t\t\"header\": [],\n" +
                "\t\t\"body\": {\n" +
                "\t\t\t\"mode\": \"raw\",\n" +
                "\t\t\t\"raw\": \"\"\n" +
                "\t\t},\n" +
                "\t\t\"url\": {\n" +
                "\t\t\t\"raw\": \"http://10.125.128.85:9000/cs/ppc/uniproxy/interfaceservice/httpforjson\",\n" +
                "\t\t\t\"protocol\": \"http\",\n" +
                "\t\t\t\"host\": [\n" +
                "\t\t\t\t\"10\",\n" +
                "\t\t\t\t\"125\",\n" +
                "\t\t\t\t\"128\",\n" +
                "\t\t\t\t\"85\"\n" +
                "\t\t\t],\n" +
                "\t\t\t\"port\": \"9000\",\n" +
                "\t\t\t\"path\": [\n" +
                "\t\t\t\t\"cs\",\n" +
                "\t\t\t\t\"ppc\",\n" +
                "\t\t\t\t\"uniproxy\",\n" +
                "\t\t\t\t\"interfaceservice\",\n" +
                "\t\t\t\t\"httpforjson\"\n" +
                "\t\t\t]\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t\"response\": []\n" +
                "}";
        JSONObject testCase = JSON.parseObject(caseJsonTemplete, Feature.OrderedField);
        //修改用例名字
        testCase.put("name", caseName);
        JSONObject requestEle = testCase.getJSONObject("request");
        JSONObject bodyEle = requestEle.getJSONObject("body");
        bodyEle.put("raw",reuqestJson);
//        System.out.println(request);
        ArrayList<Object> scriptExecEle = new ArrayList<>(3);
        scriptExecEle.add(" pm.test(responseBody, function () {");
        StringBuilder scriptCondition = new StringBuilder("    pm.expect(pm.response.text()).to.include(\"SUCCESS\"");
        //解析返回报文，生成脚本
        List<String> list = getJsonKey(responseJson);
        Iterator<String> iterator = list.iterator();
        //追加脚本判断条件
        while (iterator.hasNext()) {
            scriptCondition.append(",\"");
            scriptCondition.append(iterator.next());
            scriptCondition.append("\"");
        }
        scriptCondition.append(");");
        scriptExecEle.add(scriptCondition.toString());
        scriptExecEle.add("});");
        //插入脚本
        JSONArray eventEle = testCase.getJSONArray("event");
        JSONObject scriptEle = eventEle.getJSONObject(0).getJSONObject("script");
        scriptEle.put("exec", new JSONArray(scriptExecEle));
        return testCase;
    }
}
