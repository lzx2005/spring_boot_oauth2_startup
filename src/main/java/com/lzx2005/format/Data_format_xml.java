package com.lzx2005.format;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mongodb.BasicDBObject;

public class Data_format_xml {
    static BasicDBObject dbobj;
    static DocumentBuilder db = null;
    static Document document = null;
    static SAXReader reader = null;
    static HashMap<String, String[]> keymap = new HashMap<>();
    private final static Logger LOGGER = LoggerFactory.getLogger(Data_format_xml.class);
    private static String format = "[Code: {}] [Reason: {}] [ExtraMsg: {}]";

    static {
        //第三层外的都是第三层的同类不并存项, 注意Key大小写敏感
        keymap.put("basicList", new String[]{"gsgsdjxx", "登记信息", "基本信息"});
        keymap.put("shareHolderList", new String[]{"gsgsdjxx", "登记信息", "股东信息", "合伙人信息", "投资人信息",
                "发起人信息", "发起人", "股东信息/发起人信息", "合作各方信息", "股东（发起人）信息"});
        keymap.put("personList", new String[]{"gsgsbaxx", "备案信息", "主要人员信息", "参加经营的家庭成员姓名", "主管部门出资信息", "成员名册"});
        keymap.put("abnormalList", new String[]{"gsgsjyycxx", "经营异常信息", "经营异常信息"});
        keymap.put("alterList", new String[]{"gsgsdjxx", "登记信息", "变更信息"});
        keymap.put("caseInfoList", new String[]{"gsgsxzcfxx", "行政处罚信息", "行政处罚信息"});
        keymap.put("sharesImpawnList", new String[]{"gsgsgqczdjxx", "股权出质登记信息", "股权出质登记信息"});
        keymap.put("illegalList", new String[]{"gsgsyzwfxx", "严重违法信息", "严重违法信息"});
        keymap.put("spotchecksList", new String[]{"gsgsccjcxx", "抽查检查信息", "抽查检查信息"});
        keymap.put("filiationList", new String[]{"gsgsbaxx", "备案信息", "分支机构信息"});
        keymap.put("liquidationList", new String[]{"gsgsbaxx", "备案信息", "清算信息"});
        keymap.put("morDetailList", new String[]{"gsgsdcdyxx", "动产抵押登记信息", "动产抵押登记信息"});
        keymap.put("annualReportList", new String[]{"qygsxxqinb", "", ""});
    }

    public Data_format_xml(BasicDBObject dbobj) {
        this.dbobj = dbobj;
        try {
            reader = new SAXReader();
            //document = reader.read(this.getClass().getResourceAsStream("/point_config/Keys.xml"));
            document = reader.read(this.getClass().getResourceAsStream("/point_config/Keys.xml"));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * 数据解析
     * MonggoDB中的数据除了天津市都是三层关系
     *
     * @param firstKey
     * @param secondKey
     * @param thirdKey
     * @param xmlkey
     * @return
     */
    public JSONArray dataAnalysis(String firstKey, String secondKey, String thirdKey, String xmlkey) {
        BasicDBObject djobj = new BasicDBObject();
        JSONObject partJson = new JSONObject();
        net.sf.json.JSONObject datajson = null;
        try {
            if (secondKey.length() == 0) {
                djobj = (BasicDBObject) dbobj.get(firstKey);
                if (djobj == null || djobj.equals("{ }") || djobj.equals("")) {
                    return new JSONArray();
                }
            } else {
                //判断是否有第二层
                djobj = (BasicDBObject) ((BasicDBObject) dbobj.get(firstKey));
                //System.out.println(djobj);
                if (!djobj.containsKey(secondKey) || djobj.get(secondKey).equals("{ }")
                        || djobj.get(secondKey).equals("")) {
                    return new JSONArray();
                }
            }

            BasicDBObject dtobj = new BasicDBObject();
            if (thirdKey.length() == 0) {
                try {
                    for (Entry<String, Object> entry : djobj.entrySet()) {
                        datajson = net.sf.json.JSONObject.fromObject(entry.getValue());
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            } else {
                String[] keys = {"", "暂无数据", "无", "{ }", "此企业暂无", "此企业无"};
                Object tmpObj = djobj.get(secondKey);

                if (tmpObj instanceof String) {
                    String tmpVal = (String) tmpObj;
                    for (String key : keys) {
                        if (tmpVal.indexOf(key) > -1) {
                            return new JSONArray();
                        }
                    }
                }

                dtobj = (BasicDBObject) tmpObj;
                //判断是否有第三层
                if (!dtobj.containsKey(thirdKey) || dtobj.get(thirdKey).equals("{ }")
                        || dtobj.get(thirdKey).equals("")) {
                    return new JSONArray();
                }

                String values = dtobj.get(thirdKey).toString();
                for (String key : keys) {
                    if (values.length() <= 20 && values.indexOf(key) > -1) {
                        return new JSONArray();
                    }
                }
                BasicDBObject db = (BasicDBObject) dtobj.get(thirdKey);
                datajson = net.sf.json.JSONObject.fromObject(db);
            }
        } catch (Exception e) {
            System.out.println(
                    "Exception....." + firstKey + "-" + secondKey + "-" + xmlkey + "--" + dbobj.getString("catRegNo"));
            return new JSONArray();
        }

        Element root = document.getRootElement();
        Element nodeElement = root.element(xmlkey);
        if (null == nodeElement)
            return null;

        return getJson(datajson, nodeElement, partJson);
    }

    /**
     * tianjin数据解析
     * MonggoDB中的数据除了天津市都是三层关系
     *
     * @param firstKey
     * @param secondKey
     * @param xmlkey
     * @return
     */
    public JSONArray tjdataAnalysis(String firstKey, String secondKey, String xmlkey) {
        BasicDBObject djobj = new BasicDBObject();
        JSONObject partJson = new JSONObject();
        try {
            if (secondKey.length() == 0) {
                djobj = (BasicDBObject) dbobj.get(firstKey);
                if (djobj == null || djobj.equals("{ }") || djobj.equals("")) {
                    return new JSONArray();
                }
            } else {
                djobj = (BasicDBObject) ((BasicDBObject) dbobj.get(firstKey));
                if (!djobj.containsKey(secondKey) || djobj.get(secondKey).equals("{ }")
                        || djobj.get(secondKey).equals("")) {
                    return new JSONArray();
                }
            }
        } catch (Exception e) {
            LOGGER.error(format, new String[]{String.valueOf(ErrorCode.PROCESS_CODE_FORMAT.getCode()),
                    ErrorCode.PROCESS_CODE_FORMAT.getMessage(), StringTool.stackTraceToStirng(e)});
            e.printStackTrace();
            System.out.println(
                    "Exception....." + firstKey + "-" + secondKey + "-" + xmlkey + "--" + dbobj.getString("catRegNo"));
            return new JSONArray();
        }
        net.sf.json.JSONObject datajson = null;
        if (secondKey.length() == 0) {
            try {
                for (Entry<String, Object> entry : djobj.entrySet()) {
                    datajson = net.sf.json.JSONObject.fromObject(entry.getValue());
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        } else {
            String[] keys = {"", "暂无数据", "无", "{ }", "此企业暂无", "此企业无"};
            String values = djobj.get(secondKey).toString();
            for (String key : keys) {
                if (values.length() <= 20 && values.indexOf(key) > -1) {
                    return new JSONArray();
                }
            }
            BasicDBObject db = (BasicDBObject) djobj.get(secondKey);
            datajson = net.sf.json.JSONObject.fromObject(db);
        }

        Element root = document.getRootElement();
        //Returns the first element for the given fully qualified name
        Element nodeElement = root.element(xmlkey);
        JSONArray arry = getJson(datajson, nodeElement, partJson);
        return arry;
    }

    /**
     * 递归生成数据
     * keys.xml里的是中英文字段对照，爬虫的数据datajson都是中文的，要解析datajson,根据字段一一对应拼成英文avlidJson顺便处理下数据格式
     * 只能解析三层xml,超过三层的要递归如年报annualreportList,年报递归两层，一层details，一层basicEnterpriseItem、...
     *
     * @param datajson
     * @param nodeElement
     * @param partJson
     * @return
     */
    public JSONArray getJson(net.sf.json.JSONObject datajson, Element nodeElement, JSONObject partJson) {
        net.sf.json.JSONArray dataArray = null;
        if (datajson == null || datajson.isEmpty()) {
            return new JSONArray();
        }
        if (datajson.containsKey("datalist")) {
            dataArray = datajson.getJSONArray("datalist");
        } else {
            dataArray = new net.sf.json.JSONArray();
            dataArray.add(datajson);
        }
        boolean isJudge = true;
        try {
            JSONArray jarray = new JSONArray();
            for (Object object : dataArray) {
                ArrayList<String> avildDatas = new ArrayList<>();
                JSONObject avlidJson = new JSONObject();
                net.sf.json.JSONObject sdb = (net.sf.json.JSONObject) object;
                //Returns the elements contained in this element.
                List node = nodeElement.elements();
                for (Iterator it = node.iterator(); it.hasNext(); ) {
                    Element elm = (Element) it.next();
                    String fkey = elm.getName();

                    // Returns an iterator over all this elements child elements
                    Iterator<Element> eles = elm.elementIterator();

                    boolean hasChild = false;
                    //判断第二层是否有有下层
                    if (eles.hasNext()) {
                        hasChild = true;
                        isJudge = true;
                    } else {
                        if (!fkey.equals("data") && !avlidJson.has(fkey)) {
                            avlidJson.put(fkey, "");
                        }
                        if (avildDatas.isEmpty()) {
                            isJudge = false;
                        }
                    }
                    if (hasChild) {
                        if (elm.elements().size() > 1 && elm.elements("data").size() == 1) {
                            // 递归
                            String value = ((Element) elm.elements("data").get(0)).attributeValue("value");
                            net.sf.json.JSONObject cdb = null;
                            try {
                                cdb = sdb.getJSONObject(value);
                            } catch (Exception e) {
                                // TODO: handle exception
                                cdb = null;
                            }
                            if (cdb == null) {
                                avlidJson.put(fkey, new JSONArray());
                                continue;
                            }
                            if (elm.DOCUMENT_TYPE_NODE != Node.ATTRIBUTE_NODE) {
                                JSONArray jsarry = getJson(cdb, elm, partJson);
                                if (jsarry == null || jsarry.length() == 0) {
                                    avlidJson.put(fkey, new JSONArray());
                                    continue;
                                }
                                boolean isNull = true;
                                for (int i = 0; i < jsarry.length(); i++) {
                                    try {
                                        JSONObject yzJson = (JSONObject) jsarry.get(i);
                                        Iterator ityz = yzJson.keys();
                                        while (it.hasNext()) {
                                            String yzkey = (String) ityz.next();
                                            Object yzData = yzJson.get(yzkey);
                                            if (yzData.toString().length() > 0 && (!yzData.toString().equals("[]"))) {
                                                isNull = false;
                                            }
                                        }
                                    } catch (Exception e) {
                                        // TODO: handle exception
                                    }
                                }
                                if (!isNull) {
                                    avlidJson.put(fkey, jsarry);
                                }
                                else {
                                    avlidJson.put(fkey, new JSONArray());
                                }
                            }
                        } else {
                            while (eles.hasNext()) {
                                Element elethirdElement = eles.next();
                                String value = elethirdElement.attributeValue("value");
                                if (value != null && value.length() > 0) {
                                    if (sdb.containsKey(value)) {
                                        try {
                                            //格式化数据        fkey = schecksReg,value = 检查实施机关 ,alivalue = 重庆市工商行政管理局合川区分局
                                            String alivalue = "";
                                            String cwOri = "";
                                            if (avlidJson.has(fkey) && StringUtils.isNotBlank((String) avlidJson.get(fkey)))
                                                continue;

                                            switch (fkey) {
                                                case "esDate":
                                                case "ancheDate":
                                                case "accondate":
                                                case "condate":
                                                case "abnRtime":
                                                case "schecksDate":
                                                case "regiDate":
                                                case "illegaltime":
                                                case "gqChangeDate":
                                                case "changeTime":
                                                case "reportDate":
                                                case "openFrom":
                                                case "openTo":
                                                case "apprdate":
                                                    //格式化日期
                                                    alivalue = DateUtil.formatDate(getJSONObjectValue(sdb, value).trim());
                                                    avlidJson.put(fkey, alivalue);//{"schecksReg": "重庆市工商行政管理局合川区分局", "schecksRes": "失去联系"}
                                                    avildDatas.add(alivalue);
                                                    break;
                                                case "regCap":
                                                    //regCap截取“万”前面的文字，去空格，转换成数字型，如果没有“万”，去空格，转换成数字型
                                                    cwOri = getJSONObjectValue(sdb, value).trim();
                                                    if (cwOri.contains("万") ) {
                                                        alivalue = cwOri.substring(0, cwOri.indexOf("万"));
                                                    } else {
                                                        alivalue = cwOri;
                                                    }
                                                    avlidJson.put(fkey, alivalue);
                                                    avildDatas.add(alivalue);
                                                    //截取爬虫数据"万"后面的文字去空格,截取字段中如果有元,去除元;如果没有"万",为空
                                                    if (cwOri.contains("万")) {
                                                        alivalue = cwOri.substring(cwOri.indexOf("万") + 1, cwOri.length());
                                                        alivalue = alivalue.replace("元", "");
                                                    } else {
                                                        alivalue = "";
                                                    }
                                                    avlidJson.put("regCapCur", alivalue);
                                                    avildDatas.add(alivalue);
                                                    break;
                                                case "regNo":
                                                    cwOri = getJSONObjectValue(sdb, value).trim();
                                                    if (StringUtils.isNotBlank(cwOri)) {
                                                        String[] two = cwOri.split(" ");
                                                        if (two.length == 1) {
                                                            if (cwOri.length() == 18) {
                                                                alivalue = "";
                                                                avlidJson.put("regCreditNo", cwOri);
                                                                avildDatas.add(cwOri);
                                                                if(sdb.size() == 2){
                                                                    alivalue = cwOri;
                                                                }
                                                            } else {
                                                                alivalue = cwOri;
                                                            }
                                                        } else {
                                                            if (two[0].equals("无")) {
                                                                two[0] = "";
                                                            }
                                                            if (two[1].equals("无")) {
                                                                two[1] = "";
                                                            }
                                                            alivalue = two[0];
                                                            avlidJson.put("regCreditNo", two[1]);
                                                            avildDatas.add(two[1]);
                                                        }
                                                    } else {
                                                        alivalue = "";
                                                    }
                                                    avlidJson.put(fkey, alivalue);
                                                    avildDatas.add(alivalue);
                                                    break;
                                                case "enterpriseStatus":
                                                    alivalue = getJSONObjectValue(sdb, value).trim();
                                                    if (StringUtils.isNotBlank(alivalue)) {
                                                        if (alivalue.equals("存续（在营、开业、在册）") || alivalue.equals("存续") || alivalue.equals("在营") || alivalue.equals("开业") || alivalue.equals("在册") || alivalue.equals("成立")) {
                                                            alivalue = "在营(开业)";
                                                        }
                                                    } else {
                                                        alivalue = "";
                                                    }
                                                    avlidJson.put(fkey, alivalue);
                                                    avildDatas.add(alivalue);
                                                    break;
                                                case "cbuItem":
                                                    cwOri = getJSONObjectValue(sdb, value).trim();
                                                    int abuItem = cwOri.indexOf("许可经营项目");
                                                    int cbuItem = cwOri.indexOf("一般经营项目");
                                                    if (abuItem == -1 && cbuItem == -1) {
                                                        if (!avlidJson.has("operateScope")) {
                                                            avlidJson.put("operateScope", cwOri);
                                                            avildDatas.add(cwOri);
                                                            alivalue = "";
                                                            avlidJson.put(fkey, alivalue);
                                                            avildDatas.add(alivalue);
                                                        }
                                                    } else if (abuItem > -1 && cbuItem == -1) {
                                                        String part = cwOri.substring(abuItem + 7, cwOri.length()).trim();
                                                        part = part.equals("无") ? "" : part;
                                                        avlidJson.put("abuItem", part);
                                                        avildDatas.add(part);
                                                    } else if (abuItem == -1 && cbuItem > -1) {
                                                        String part = cwOri.substring(cbuItem + 7, cwOri.length()).trim();
                                                        part = part.equals("无") ? "" : part;
                                                        avlidJson.put("cbuItem", part);
                                                        avildDatas.add(part);
                                                    } else {
                                                        String parta = "";
                                                        String partc = "";
                                                        if (abuItem < cbuItem) {
                                                            parta = cwOri.substring(abuItem + 7, cbuItem);
                                                            partc = cwOri.substring(cbuItem + 7, cwOri.length());
                                                        } else {
                                                            partc = cwOri.substring(cbuItem + 7, abuItem);
                                                            parta = cwOri.substring(abuItem + 7, cwOri.length());
                                                        }
                                                        parta = parta.equals("无") ? "" : parta;
                                                        partc = partc.equals("无") ? "" : partc;
                                                        avlidJson.put("abuItem", parta);
                                                        avildDatas.add(parta);
                                                        avlidJson.put("cbuItem", partc);
                                                        avildDatas.add(partc);
                                                    }
                                                    break;
                                                default:
                                                    if (!avlidJson.has(fkey) || StringUtils.isBlank((String) avlidJson.get(fkey))) {
                                                        alivalue = getJSONObjectValue(sdb, value);
                                                        avlidJson.put(fkey, alivalue);
                                                        avildDatas.add(alivalue);
                                                    }
                                                    break;
                                            }
                                        } catch (Exception e) {
                                            // TODO: handle exception
                                            e.printStackTrace();
                                        }
                                    } else {
                                        if (!(avlidJson.has(fkey) && avlidJson.get(fkey).toString().length() > 0)) {
                                            avlidJson.put(fkey, "");
                                            avildDatas.add("");
                                        }
                                    }
                                }
                                //System.out.println(value);
                            }
                        }
                    }
                }
                if (isJudge && isAllnullOrempty((String[]) avildDatas.toArray(new String[avildDatas.size()]))) {
                    continue;
                }
                jarray.put(avlidJson);
            }
            return jarray;
        } catch (Exception e) {
            LOGGER.error(format, new String[]{String.valueOf(ErrorCode.PROCESS_CODE_FORMAT.getCode()),
                    ErrorCode.PROCESS_CODE_FORMAT.getMessage(), StringTool.stackTraceToStirng(e)});
            e.printStackTrace();
        }
        return new JSONArray();
    }

    /**
     * 获取map值
     *
     * @param db
     * @param key
     * @return
     */
    public String getJSONObjectValue(net.sf.json.JSONObject db, String key) {
        String value = "";
        try {
            if (db.containsKey(key)) {
                Object objvalue = db.get(key);
                if (objvalue instanceof String) {
                    return String.valueOf(objvalue);
                } else if (objvalue instanceof net.sf.json.JSONObject && objvalue.toString().contains("datalist")) {
                    net.sf.json.JSONObject json = (net.sf.json.JSONObject) objvalue;
                    if(json.containsKey("datalist")){
                        return json.getString("datalist");
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error(format, new String[]{String.valueOf(ErrorCode.PROCESS_CODE_FORMAT.getCode()),
                    ErrorCode.PROCESS_CODE_FORMAT.getMessage(), StringTool.stackTraceToStirng(e)});
        }
        return value;
    }

    /**
     * 判断一组有效的数据组是否全为空
     *
     * @param infos
     * @return
     */
    public Boolean isAllnullOrempty(String[] infos) {
        for (String info : infos) {
            if (info != null && info.length() > 0) {
                return false;
            }
        }
        return true;
    }

    public void map_each(BasicDBObject doc) {
        try {
            for (Entry<String, Object> entry : doc.entrySet()) {
                System.out.println("key= " + entry.getKey() + " :: value= " + entry.getValue());
            }
        } catch (Exception e) {
            LOGGER.error(format, new String[]{String.valueOf(ErrorCode.PROCESS_CODE_FORMAT.getCode()),
                    ErrorCode.PROCESS_CODE_FORMAT.getMessage(), StringTool.stackTraceToStirng(e)});
            e.printStackTrace();
        }
    }

    /**
     * json字符串格式化
     *
     * @param uglyJSONString
     * @return
     */
    public static String jsonFormatter(String uglyJSONString) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(uglyJSONString);
        String prettyJsonString = gson.toJson(je);
        return prettyJsonString;
    }

    public JSONObject getJSONData() {
        JSONObject finalJson = new JSONObject();
        @SuppressWarnings("rawtypes")
        Iterator iter = keymap.entrySet().iterator();
        while (iter.hasNext()) {
            @SuppressWarnings("rawtypes")
            Entry entry = (Entry) iter.next();
            String key = (String) entry.getKey();
            String[] val = (String[]) entry.getValue();
            JSONArray array = null;
            for (int i = 2; i < val.length; i++) {
                array = dataAnalysis(val[0], val[1], val[i], key);
                if (null != array && !array.isNull(0))
                    break;
            }
            try {
                if (null != array)
                    finalJson.put(key, array);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return processFinalJson(finalJson);
    }

    public JSONObject getTjJSONData() {
        JSONObject finalJson = new JSONObject();
        Iterator iter = keymap.entrySet().iterator();
        while (iter.hasNext()) {
            Entry entry = (Entry) iter.next();
            String key = (String) entry.getKey();
            String[] val = (String[]) entry.getValue();
            JSONArray array = null;
            for (int i = 2; i < val.length; i++) {
                array = tjdataAnalysis(val[0], val[i], key);
                if (!array.isNull(0))
                    break;
            }

            try {
                finalJson.put(key, array);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return processFinalJson(finalJson);
    }

    //进一步处理finaljson
    private JSONObject processFinalJson(JSONObject finalJson) {
        try {

            if (finalJson.getJSONArray("basicList") == null || finalJson.getJSONArray("basicList").length() == 0) {
                SystemLog.getInstance().showMsg(dbobj.getString("catRegNo") + "-" + dbobj.getString("catEntName") + "-"
                        + dbobj.getString("province"));
            } else {
                //爬虫年报数据中报送年度最大的年份
                //爬虫年报数据中报送年度最大的年份的发布日期
                if (finalJson.getJSONArray("annualReportList") != null) {
                    String maxYear = "";
                    String maxDate = "";
                    JSONArray arrays = finalJson.getJSONArray("annualReportList");
                    for (int i = 0; i < arrays.length(); i++) {
                        //将每份年报的details值由数组降为对象
                        JSONObject parentObj = arrays.getJSONObject(i);
                        JSONArray obj = arrays.getJSONObject(i).getJSONArray("details");
                        if (!obj.isNull(0)) {                            parentObj.put("details", obj.getJSONObject(0));
                        } else {
                            parentObj.put("details", new JSONObject());
                        }

                        String year = arrays.getJSONObject(i).getString("reportYear");
                        if (year != null && year.toString().length() >= 4) {
                            year = year.substring(0, 4);
                            if (StringUtils.isNotBlank(maxYear)) {
                                if (year.compareTo(maxYear) > 0) {
                                    maxYear = year;
                                }
                            } else {
                                maxYear = year;
                            }
                        }
                        String date = arrays.getJSONObject(i).getString("reportDate");
                        if (date != null) {
                            if (StringUtils.isNotBlank(maxDate)) {
                                if (date.compareTo(maxDate) > 0) {
                                    maxDate = date;
                                }
                            } else {
                                maxDate = date;
                            }
                        }
                    }
                    JSONObject basicInfo = finalJson.getJSONArray("basicList").getJSONObject(0);
                    basicInfo.put("ancheYear", maxYear);
                    basicInfo.put("ancheDate", maxDate);
                }

                //设定股东出资额、出资日期、出资比例
                //出资额：取认缴额（万元），如果有“万”，截取爬“万”前面的文字去空格，转化成数字型
                //出资比例：认缴额/注册资本*100%
                if (finalJson.getJSONArray("shareHolderList") != null) {
                    //注册资本
                    String regCap = finalJson.getJSONArray("basicList").getJSONObject(0).getString("regCap");
                    JSONArray arrays = finalJson.getJSONArray("shareHolderList");
                    for (int i = 0; i < arrays.length(); i++) {
                        JSONObject holder = arrays.getJSONObject(i);
                        if (holder.getJSONArray("detail") != null && !holder.getJSONArray("detail").isNull(0)) {
                            //认缴额
                            String lisubconam = holder.getJSONArray("detail").getJSONObject(0).getJSONArray("shareinvestmentinf")
                                    .getJSONObject(0).getString("lisubconam");
                            //出资日期
                            //subconList为空
                            JSONArray subconList = holder.getJSONArray("detail").getJSONObject(0).getJSONArray("shareinvestmentinf")
                                    .getJSONObject(0).getJSONArray("subconList");
                            if (subconList != null && !subconList.isNull(0)) {
                                String condate = subconList.getJSONObject(0).getString("condate");
                                holder.put("conDate", condate);
                            } else {
                                holder.put("conDate", "");
                            }

                            String subConam = lisubconam.trim();
                            if (lisubconam.contains("万")) {
                                subConam = lisubconam.substring(0, lisubconam.indexOf("万"));
                            }
                            //出资额
                            holder.put("subConam", subConam);
                            //出资比例
                            holder.put("fundedratio", getfundedratio(subConam, regCap));
                        }
                    }
                }
                return finalJson;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    //计算出资比例（无四舍五入，保留一位小数）
    /*
	private String getfundedratio(String acconam, String regCap){
		if(StringUtils.isBlank(acconam) || StringUtils.isBlank(regCap)){
			return "";
		}
		String result = (Float.valueOf(acconam)/Float.valueOf(regCap.replace(",", "").replace(" ", "")))*100 +"";
		int index = result.indexOf(".");
		if(index > -1 && index + 2 <= result.length()){
			result = result.substring(0,index + 2);
		}
		return result + "%";
	}
	*/

    //计算出资比例（四舍五入，保留两位小数）
    private String getfundedratio(String acconam, String regCap) {
        String result = null;
        if(!acconam.isEmpty() && !regCap.isEmpty()) {
            Double dResult = (Double.valueOf(acconam.replace(",", "").replace(" ", "").replace("人民币","")) / Double.valueOf(regCap.replace(",", "").replace(" ", "").replace("人民币",""))) * 100;
            BigDecimal bd = new BigDecimal(dResult);
            result = bd.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
            return result + "%";
        }else{
            return "";
        }
    }
}
