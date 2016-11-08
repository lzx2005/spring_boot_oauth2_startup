package com.lzx2005.controller;

import com.lzx2005.dto.ApiResult;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableAuthorizationServer
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MainController {

    @RequestMapping("/")
    String home() {
        return "Hello World!";
    }

    @RequestMapping(value = "/ajax_test", method = RequestMethod.GET)
    public ApiResult<String> AjaxTest() {
        return new ApiResult<String>(true,0,"请求成功",null);
    }

    @RequestMapping(value = "/send_msg", method = RequestMethod.POST)
    public ApiResult<String> SendMsg(@RequestParam(value = "json",required = true,defaultValue = "{}")String json) {
        if (!json.equalsIgnoreCase("{}")){
            //Formater.format(json);
            return new ApiResult<String>(true,0,"请求成功",json);
        }else{
            return new ApiResult<String>(false,1,"发送失败","请带上json数据");
        }
    }
}