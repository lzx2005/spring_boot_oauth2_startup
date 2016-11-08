## 爬虫数据导入Kafka的Api接口

### Quick Start

Clone项目
```bash
git clone http://gitlab.yscredit.com/lizhengxian/crawler_to_kafka_url_api.git
```

使用IDEA打开项目
右键pom.xml Download导入依赖包

运行
```text
com.lzx2005.application.Application.java
```

看到类似
```text
...
2016-11-07 11:48:22.540  INFO 17544 --- [           main] o.s.j.e.a.AnnotationMBeanExporter        : Registering beans for JMX exposure on startup
2016-11-07 11:48:22.590  INFO 17544 --- [           main] s.b.c.e.t.TomcatEmbeddedServletContainer : Tomcat started on port(s): 8090 (http)
2016-11-07 11:48:22.593  INFO 17544 --- [           main] com.lzx2005.application.Application     : Started Application in 4.737 seconds (JVM running for 5.167)
```
的输出消息，则启动成功

打开浏览器，输入
```text
localhost:8090
```

可以看到相关信息

```xml
<oauth>
    <error_description>
        Full authentication is required to access this resource
    </error_description>
    <error>unauthorized</error>
</oauth>
```

则服务器启动正确

### 接口访问方法:

#### 权限验证
服务器采用Oauth2.0权限验证，请求接口首先需要进行鉴权，打开终端，输入

```bash
curl localhost:8080/oauth/token -d "grant_type=password&scope=read&username=root&password=123456" -u 233668646673605:33b17e044ee6a4fa383f46ec6e28ea1d
```

> 用户名、密码、appId、appSecret可以在`config\application.yml`中配置

> 注意：Windows CMD 下无法使用curl方法，请在Linux环境下使用


返回值类似

```json
{"access_token":"cd511715-e1a7-4fc2-8463-36f2382ca802","token_type":"bearer","refresh_token":"6e27f2d2-18c9-4c94-98ee-b80d12caff91","expires_in":42321,"scope":"read"}
```
则成功拿到access_token
> 我们以这个获取到的access_token为例子。

#### 调用接口

输入

```bash
curl -H "Authorization: bearer cd511715-e1a7-4fc2-8463-36f2382ca802"" localhost:8090/senMsg?json=dasdasdas
```

则可以成功请求，获取到
```json
{"success":true,"code":0,"msg":"请求成功","data":"dasdasdas"}
```

### Java请求AccessToken，请求接口

#### 依赖包
```xml
<dependency>
    <groupId>commons-codec</groupId>
    <artifactId>commons-codec</artifactId>
    <version>1.9</version>
</dependency>
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>fastjson</artifactId>
    <version>1.2.7</version>
</dependency>
<dependency>
    <groupId>commons-httpclient</groupId>
    <artifactId>commons-httpclient</artifactId>
    <version>3.1</version>
</dependency>
```

#### 新建一个类
```java
public class JavaRequest {
    private String url;
    private final static String getAccessTokenUrl = "/oauth/token";

    public JavaRequest(String url) {
        this.url = url;
    }

    //get and set here
}
```
#### 编写获取AccessToken方法

```java
public String getAccessToken(String username, String password, String id, String secret) throws IOException {
    HttpClient client = new HttpClient();
    PostMethod method = new PostMethod(url+getAccessTokenUrl);
    //模拟Authorization
    String auth=id+":"+secret;
    String code = Base64.encodeBase64String(auth.getBytes("UTF-8"));
    method.addRequestHeader("Authorization","Basic " + code);
    method.addRequestHeader("Access-Control-Allow-Origin","*");
    method.addRequestHeader("Access-Control-Allow-Credentials","true");
    method.addRequestHeader("Access-Control-Allow-Methods","POST");
    //设置参数
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
    //使用FastJson拿到AccessToken
    JSONObject jo = (JSONObject)JSONObject.parse(method.getResponseBodyAsString());
    String access_token = jo.getString("access_token");
    return access_token;
}
```
#### 编写请求方法

```java
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
```

#### 使用

```java
JavaRequest javaRequest = new JavaRequest("http://localhost:8090");
String accessToken = javaRequest.getAccessToken("root", "123456", "233668646673605", "33b17e044ee6a4fa383f46ec6e28ea1d");
if(accessToken!=null){
    //获取到accessToken
    HashMap<String, String> map = new HashMap<>();
    map.put("json","dasdasdas");
    String testJson = javaRequest.sendMsg("/send_msg", accessToken, map);
    System.out.println(testJson);
}
```