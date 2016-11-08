package com.lzx2005.tool;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.*;
import java.util.HashMap;
import java.util.Set;

public class JavaRequest {
    private String url;
    private final static String getAccessTokenUrl = "/oauth/token";

    public JavaRequest(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getGetAccessTokenUrl() {
        return getAccessTokenUrl;
    }


    public String getAccessToken(String username, String password, String id, String secret) throws IOException {
        // 设置代理服务器地址和端口

        HttpClient client = new HttpClient();
        //client.getHostConfiguration().setProxy("proxy_host_addr",proxy_port);
        // 使用 GET 方法 ，如果服务器需要通过 HTTPS 连接，那只需要将下面 URL 中的 http 换成 https
        //HttpMethod method=new GetMethod("http://java.sun.com");
        //使用POST方法
        PostMethod method = new PostMethod(url+getAccessTokenUrl);
        String auth=id+":"+secret;
        String code = Base64.encodeBase64String(auth.getBytes("UTF-8"));
        method.addRequestHeader("Authorization","Basic " + code);
        method.addRequestHeader("Access-Control-Allow-Origin","*");
        method.addRequestHeader("Access-Control-Allow-Credentials","true");
        method.addRequestHeader("Access-Control-Allow-Methods","POST");


        method.addParameter("grant_type","password");
        method.addParameter("scope","read");
        method.addParameter("username",username);
        method.addParameter("password",password);
        client.executeMethod(method);

        //打印服务器返回的状态
        System.out.println(method.getStatusLine());
        //打印返回的信息
        System.out.println(method.getResponseBodyAsString());
        //释放连接
        method.releaseConnection();

        JSONObject jo = (JSONObject)JSONObject.parse(method.getResponseBodyAsString());

        String access_token = jo.getString("access_token");

        return access_token;
    }

    public String sendMsg(String requsetMapping, String accessToken, HashMap<String,String> params) throws IOException {
        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod(url+requsetMapping);
        method.addRequestHeader("Authorization","bearer "+accessToken);

        Set<String> strings = params.keySet();
        for(String key : strings){
            method.addParameter(key,params.get(key));
        }
        client.executeMethod(method);
        //打印服务器返回的状态
        System.out.println(method.getStatusLine());
        //打印返回的信息
        System.out.println(method.getResponseBodyAsString());
        //释放连接
        method.releaseConnection();

        return method.getResponseBodyAsString();

    }

    public static void main(String[] args) throws InterruptedException, IOException {
        JavaRequest f = new JavaRequest("http://localhost:8090");
        String accessToken = f.getAccessToken("root", "123456", "233668646673605", "33b17e044ee6a4fa383f46ec6e28ea1d");
        System.out.println(accessToken);
        if(accessToken!=null){
            //获取到accessToken
            HashMap<String, String> map = new HashMap<>();
            map.put("json","dasdasdas");
            String testJson = f.sendMsg("/send_msg", accessToken, map);
            System.out.println(testJson);
        }
    }
}
