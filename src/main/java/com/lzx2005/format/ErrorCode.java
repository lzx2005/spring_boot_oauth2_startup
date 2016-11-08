package com.lzx2005.format;


public enum ErrorCode {
    SUCCESS(100000, "成功"), PROCESS(100001,"正在打码") ,FAILURE(-100001, "打码最大次数尝试依然失败"), TIMEOUT(-100002, "打码超时"),

    UUERROR_1008(-1002,"没有取到验证码结果，返回此值指示codeID已返回"),UUERROR_11009(-11009, "UU账户余额不足"), UUERROR_12010(-12010, "用户余额不足"),
    UUERROR_1009(-1009,"reportID出错"), UNKNOWN_ERROR(-100003, "未知错误"),INTERRUPTED_ERROR(-100004,"线程出错"),
    // 返回给客户端
    SERVERFAIL(500, "服务器出错"), FAIL(-10000, "打码失败"), CRAW_FAIL(-10001, "爬虫出错"), CRAW_TIME_OUT(-10002, "爬虫超时"), CRAW_EMPTY(-10003,
            "无该企业信息"), CRAW_NB_FAIL(-10004, "爬取年报失败"), CRAW_GS_FAIL(-10005, "爬取工商数据失败"), DATA_TRANSFER(-10006,
            "数据格式转换出错"), PROVINCE_ISNOT_EXIST(-10007, "省份不正确"),FAIL_DELETE(-10008,"删除验证码图片失败"),DATA_FORMAT_FAIL(-10009,"数据解析错误"),CRAW_PRE_FAIL(-10010,
            "preCraw出现错误"),
    /*
    LOGGER.error(format, new String[]{String.valueOf(ErrorCode.CRAW_GS_FAIL.getCode()),
            ErrorCode.CRAW_GS_FAIL.getMessage(), StringTool.stackTraceToStirng(e)});
    */

    /* ------------------------------- 新版Code ------------------------------*/
    RESULT_SUCCESS_SUCCESS(100000, "成功"),
    RESULT_SUCCESS_EMPTY(100001,"无该企业信息"),
    RESULT_SUCCESS_PRECRAW(100002,"简介信息（preCraw）爬取成功"),
    RESULT_SUCCESS_NB(100003,"年报信息爬取成功"),
    RESULT_SUCCESS_GS(100004,"工商信息爬取成功"),
    RESULT_SUCCESS_VERIFY(100005, "验证成功"),

    RESULT_FAIL_FAIL(200001,"打码失败"),
    RESULT_FAIL_PRECRAW(200002,"简介信息（preCraw）爬取失败"),
    RESULT_FAIL_NB(200003,"年报信息爬取失败"),
    RESULT_FAIL_GS(200004,"工商信息爬取失败"),
    RESULT_FAIL_PROVINCE(200005,"省份不正确"),

    PROCESS_DAMA_PROCESS(300000, "正在打码"),
    PROCESS_DAMA_MAXTIME(300001,"打码最大次数尝试依然失败"),
    PROCESS_DAMA_TIMEOUT(300002, "打码超时"),
    PROCESS_DAMA_DELTEIMG(300003, "删除验证码图片失败"),
    PROCESS_DAMA_BASE64FAIL(300004,"验证码转换BASE64失败"),
    PROCESS_DAMA_LOCALOK(300005,"从本地库取出验证码成功"),
    PROCESS_DAMA_SUCCESS(300006,"打码成功"),
    PROCESS_DAMA_LOCALSAVEOK(300007, "存入本地验证码库成功"),
    PROCESS_DAMA_LOCALSAVEFAIL(300008, "存入本地验证码库失败"),
    PROCESS_DAMA_SUCCESSFROMDB(300009,"从mongoDB中获得详情页成功"),
    PROCESS_DAMA_SUCCESSFROMGEETEST(300010, "极验打码成功"),
    PROCESS_DAMA_VERIFYFAILED(300011, "打码兔打码失败"),


    PROCESS_SERVER_TIMEOUT(301000,"服务器连接超时"),
    PROCESS_SERVER_REFUSED(301001,"服务器连接拒绝"),
    PROCESS_SERVER_ERROR(301002, "服务器出错"),
    PROCESS_SERVER_HTMLFAIL(301003, "获取HTML页面失败"),
    PROCESS_SERVER_POSTERROR(301004, "发送POST请求异常"),


    PROCESS_CODE_INTERRUPTED(302000, "线程错误"),
    PROCESS_CODE_TRANSFORM(302001,"数据格式转换失败"),
    PROCESS_CODE_FORMAT(302002, "数据解析错误"),
    PROCESS_CODE_RAMERROR(302003, "内存溢出错误"),
    PROCESS_CODE_IOERROR(302004, "输入输出流异常"),
    PROCESS_CODE_TIMEOUT(302005, "连接超时"),
    PROCESS_CODE_UNKNOWNERROR(302006,"未知错误"),
    PROCESS_CODE_RELEASE(302007,"资源释放错误"),
    PROCESS_CODE_TURNPAGE(302008,"控制翻页错误"),


    PROCESS_DAMA2_BALANCE(303000, "余额不足"),
    PROCESS_DAMA2_TIMEOUT(303001,"超时没有取到验证码识别结果"),
    PROCESS_DAMA2_FREQUENTLY(303002, "请求太频繁"),
    PROCESS_DAMA2_INTERNETERROR(303003, "网络错误"),
    PROCESS_DAMA2_CONNSERVERFAIL(303004, "连接服务器失败"),
    PROCESS_DAMA2_INTERNETDOWN(303005, "网络故障"),
    PROCESS_DAMA2_SERVERERROR(303006, "服务器返回错误"),
    PROCESS_DAMA2_DYNAMIC(303007, "需要输入动态验证码"),
    PROCESS_DAMA2_BANSOFTWARE(303008, "禁止使用该软件"),
    PROCESS_DAMA2_EXPIRED(303009, "动态验证码过期"),
    PROCESS_DAMA2_OVERLIMIT(303010, "动态验证码当日发送次数超限"),
    PROCESS_DAMA2_LENGTHERROR(303011, "验证码长度错误"),
    PROCESS_DAMA2_OTHERERROR(303012, "平台其他错误");

    private int code;
    private String message;

    private ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "错误码:" + this.getCode() + "," + "错误信息:" + this.getMessage();
    }
}
