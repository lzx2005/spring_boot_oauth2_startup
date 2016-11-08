package com.lzx2005.format;

import com.mongodb.BasicDBObject;
import org.json.JSONObject;

public class Formater {
    public static void format(String json){
        Data_format_xml df = new Data_format_xml(BasicDBObject.parse(json));
        JSONObject jsonArray = df.getJSONData();
        System.out.println("======================");
        System.out.println(jsonArray);
        //new Sender().doSend(msg);
    }

    public static void main(String[] args) {
        String json = "{'gsgsccjcxx': {'抽查检查信息': {'抽查检查信息': {}}}, 'province': '西藏自治区', 'gsgsbaxx': {'备案信息': {'参加经营的家庭成员姓名': {}}}, 'gsgsxzcfxx': {'行政处罚信息': {'行政处罚信息': {}}}, 'gsgsjyycxx': {'经营异常信息': {'经营异常信息': {'datalist': [{'恢复日期': '', '标记日期': '2015年7月28日', '恢复正常记载状态原因': '', '序号': '1', '标记经营异常状态原因': '工商行政管理部门在依法履职过程中通过登记的经营场所及经营者住所无法与个体工商户取得联系的', '作出决定机关': '卡若工商行政管理局'}]}, '年报信息': {}}}, 'catRegNo': '542121600000117', 'gsgsgqczdjxx': {'股权出质登记信息': {}}, 'update_time': '2015-10-16 16:48:14', 'gsgsdcdyxx': {'动产抵押登记信息': {'动产抵押登记信息': {}}}, 'gsgsdjxx': {'登记信息': {'基本信息': {'核准日期': '2012年2月15日', '经营范围': '零售，床上用品、床，', '注册号': '542121600000117', '类型': '个体工商户', '名称': '水星家纺', '经营场所': '昌庆街', '注册日期': '2010年8月2日', '登记机关': '卡若工商行政管理局', '登记状态': '存续', '组成形式': '个人经营', '经营者': '陈益龙'}, '变更信息': {}}}, 'url': 'http://gsxt.xzaic.gov.cn/businessPublicity.jspx?id=C5ECE6640C24C5FD89E6501E50DA23C6&sourceType=1', 'qygsxxqinb': {'个体工商户年报': {}}, 'catInDate': '2015年7月28日', 'gsgsyzwfxx': {'严重违法信息': {}}, 'catEntName': '水星家纺'}";
        format(json);
    }
}
